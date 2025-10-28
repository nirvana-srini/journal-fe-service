```markdown
# task-service Helm chart

This chart deploys the DeepFocus task-service.

Quick usage (example):
1. Update values.yaml or pass --set flags to helm upgrade/install.
2. Install:

   helm upgrade --install deepfocus-task-service ./services/task-service/chart \
     -n deepfocus --create-namespace \
     --set image.repository=srinivaspwd/deepfocus-task-service \
     --set image.tag=local-test \
     --set postgres.host=pg-postgresql \
     --set kafka.bootstrapServers=kafka-cluster-kafka-bootstrap.kafka:9092 \
     --set apicurio.registryUrl=http://apicurio-apicurio-registry.apicurio:8080/apis/registry/v2 \
     --set oidc.issuerUri="http://keycloak.keycloak/realms/deepfocus" \
     --set env.JAVA_OPTS="-Doutbox.poll-ms=0" \
     --wait

Notes:
- env.JAVA_OPTS: set -Doutbox.poll-ms=0 to disable the scheduled outbox publisher when using Debezium (CDC).
- Ensure Postgres is configured with wal_level=logical and a replication user exists for Debezium.
- If not using Debezium, remove the -Doutbox.poll-ms=0 setting so the app publishes via scheduled publisher.

Test / debug:
- helm lint ./services/task-service/chart
- helm template ./services/task-service/chart | less
```