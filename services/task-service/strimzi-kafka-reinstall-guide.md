# ✅ Strimzi Kafka Re-installation Guide (KRaft Mode)

### **Purpose**
This guide provides **step-by-step commands** to delete any existing Kafka namespace and reinstall a working single-node Strimzi Kafka cluster (KRaft mode).  
Compatible with Rancher Desktop / Minikube / Kind / EKS / AKS / GKE.

---

## 🧹 1. Clean-up any existing Strimzi setup

```bash
kubectl delete namespace kafka --ignore-not-found
kubectl delete clusterrole,clusterrolebinding,crd,cr --ignore-not-found -l app.kubernetes.io/name=strimzi
```

(Optional) verify it’s gone:
```bash
kubectl get ns | grep kafka || echo "Kafka namespace deleted"
```

---

## 🏗️ 2. Create fresh namespace

```bash
kubectl create namespace kafka
```

---

## 📦 3. Add Strimzi Helm repo & update

```bash
helm repo add strimzi https://strimzi.io/charts/
helm repo update
```

---

## ⚙️ 4. Install Strimzi Operator via Helm

```bash
helm install strimzi-kafka-operator strimzi/strimzi-kafka-operator   -n kafka   --set watchNamespaces="{kafka}"
```

Verify:
```bash
kubectl get pods -n kafka
```

Expected:
```
NAME                                            READY   STATUS    AGE
strimzi-cluster-operator-xxxxx                  1/1     Running   1m
```

---

## 🧩 5. Deploy the Kafka cluster (KRaft mode)

> Strimzi ≥ 0.48.0 uses **KafkaNodePool** instead of `spec.kafka.replicas` and **no ZooKeeper**.

### 5.1 KafkaNodePool — controller + broker (single-node)
```bash
cat > kafka-nodepool.yaml << 'EOF'
apiVersion: kafka.strimzi.io/v1beta2
kind: KafkaNodePool
metadata:
  name: kafka-cluster-pool
  namespace: kafka
  labels:
    strimzi.io/cluster: kafka-cluster
spec:
  replicas: 1
  roles:
    - broker
    - controller
  storage:
    type: ephemeral
EOF

kubectl apply -f kafka-nodepool.yaml
```

### 5.2 Kafka cluster resource
```bash
cat > kafka-cluster.yaml << 'EOF'
apiVersion: kafka.strimzi.io/v1beta2
kind: Kafka
metadata:
  name: kafka-cluster
  namespace: kafka
  annotations:
    strimzi.io/node-pools: enabled
spec:
  kafka:
    listeners:
      - name: plain
        port: 9092
        type: internal
        tls: false
    config:
      offsets.topic.replication.factor: 1
      transaction.state.log.replication.factor: 1
      transaction.state.log.min.isr: 1
  entityOperator:
    topicOperator: {}
    userOperator: {}
EOF

kubectl apply -f kafka-cluster.yaml
```

---

## 🚀 6. Verify deployment

### 6.1 Pods
```bash
kubectl get pods -n kafka -w
```
Expected:
```
kafka-cluster-kafka-0                 Running
kafka-cluster-entity-operator-xxxxx   Running
strimzi-cluster-operator-xxxxx        Running
```

### 6.2 Services
```bash
kubectl get svc -n kafka
```
Expected:
```
kafka-cluster-kafka-bootstrap   ClusterIP   ...   9092/TCP
kafka-cluster-kafka-brokers     ClusterIP   ...   9092/TCP
```

**Bootstrap address for applications:**
```
kafka-cluster-kafka-bootstrap.kafka:9092
```

---

## 🔍 7. (Optional) Kafka sanity check

Exec into the broker:
```bash
kubectl exec -it -n kafka kafka-cluster-kafka-0 -- sh
```

Inside container:
```sh
bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
bin/kafka-topics.sh --bootstrap-server localhost:9092   --create --topic test-topic --partitions 1 --replication-factor 1
```

Exit:
```sh
exit
```

---

## 🧾 8. Re-install summary commands (quick run)

When reinstalling later, you can run all at once:

```bash
# Cleanup
kubectl delete namespace kafka --ignore-not-found
kubectl create namespace kafka

# Operator
helm repo add strimzi https://strimzi.io/charts/
helm repo update
helm install strimzi-kafka-operator strimzi/strimzi-kafka-operator -n kafka --set watchNamespaces="{kafka}"

# NodePool + Kafka cluster
kubectl apply -f kafka-nodepool.yaml
kubectl apply -f kafka-cluster.yaml

# Verify
kubectl get pods -n kafka
kubectl get svc -n kafka
```

---

## 🧩 9. Troubleshooting cheatsheet

| Issue | Cause | Fix |
|-------|--------|-----|
| `No KafkaNodePools found` | NodePool missing | Apply `kafka-nodepool.yaml` |
| `At least one controller role required` | Missing controller role | Add `controller` under `roles` |
| No `kafka-cluster-kafka-bootstrap` service | Kafka pod not yet running | Wait or check pod logs |
| Broker CrashLoopBackOff | Storage or version mismatch | Check `kubectl logs kafka-cluster-kafka-0 -n kafka` |
| DNS resolution fails from app | Service name incorrect | Use `kafka-cluster-kafka-bootstrap.kafka:9092` |
