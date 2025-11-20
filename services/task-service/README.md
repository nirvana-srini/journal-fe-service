helm upgrade --install deepfocus-task-service services/task-service/chart \
  -n ${NAMESPACE} \
  --create-namespace \
  --wait \
  --set image.repository="srinivaspwd/deepfocus-task-service" \
  --set image.tag="local-test-open" \
  --set postgres.host="pg-postgresql" \
  --set postgres.user="postgres" \
  --set postgres.password="721dWjWbPf" \
  --set postgres.db="taskdb" \
  --set kafka.bootstrapServers="kafka-cluster-kafka-bootstrap.kafka:9092" \
  --set apicurio.registryUrl="http://apicurio-apicurio-registry.apicurio:8080/apis/registry/v2" \
  --set oidc.issuerUri="http://keycloak-http.keycloak.svc.cluster.local/auth/realms/deepfocus" \
  --set env.SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI="http://keycloak-http.keycloak.svc.cluster.local/auth/realms/deepfocus" \
  --set env.JAVA_OPTS="-Doutbox.poll-ms=10000"


=========Outbox fix===============

helm upgrade --install deepfocus-task-service services/task-service/chart \
  -n ${NAMESPACE} \
  --create-namespace \
  --wait \
  --set image.repository="srinivaspwd/deepfocus-task-service" \
  --set image.tag="local-test-outboxfix" \
  --set postgres.host="pg-postgresql" \
  --set postgres.user="postgres" \
  --set postgres.password="721dWjWbPf" \
  --set postgres.db="taskdb" \
  --set kafka.bootstrapServers="kafka-cluster-kafka-bootstrap.kafka:9092" \
  --set apicurio.registryUrl="http://apicurio-apicurio-registry.apicurio:8080/apis/registry/v2" \
  --set oidc.issuerUri="http://keycloak-http.keycloak.svc.cluster.local/auth/realms/deepfocus" \
  --set env.SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI="http://keycloak-http.keycloak.svc.cluster.local/auth/realms/deepfocus" \
  --set env.JAVA_OPTS="-Doutbox.poll-ms=10000"

Release "deepfocus-task-service" has been upgraded. Happy Helming!
NAME: deepfocus-task-service
LAST DEPLOYED: Mon Oct 27 19:18:14 2025
NAMESPACE: deepfocus
STATUS: deployed
REVISION: 48
TEST SUITE: None



======Token

kubectl exec -it -n keycloak keycloak-0 -- sh -c '                      
curl -s -X POST "http://keycloak-http.keycloak.svc.cluster.local/auth/realms/deepfocus/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=task-client" \
  -d "username=testuser" \
  -d "password=testpass"                      
'

{"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJGS0NaZGs0akpKV29LZ25IQnA1S3pKY0U3VTZJRDI5NmxXVVJIdkgxVG5JIn0.eyJleHAiOjE3NjE1NzMzNzAsImlhdCI6MTc2MTU3MzA3MCwianRpIjoiMGRmZGM0N2ItNGI5YS00MzQ1LWIxYWYtYWY4YTFmYjNkYTlkIiwiaXNzIjoiaHR0cDovL2tleWNsb2FrLWh0dHAua2V5Y2xvYWsuc3ZjLmNsdXN0ZXIubG9jYWwvYXV0aC9yZWFsbXMvZGVlcGZvY3VzIiwic3ViIjoiYTk2OWE1N2EtYTdmYy00MGQyLWJjMzAtODIxNGU0ZjE0ZWVhIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidGFzay1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiZTMzNDJkNWEtNTBmOC00OGRmLWJlOTktMDY3MzQ2MGZmMDk2IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJST0xFX1RBU0tfVVBEQVRFIiwiUk9MRV9UQVNLX0RFTEVURSIsIlJPTEVfVEFTS19DUkVBVEUiLCJST0xFX1RBU0tfUkVBRCJdfSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTMzNDJkNWEtNTBmOC00OGRmLWJlOTktMDY3MzQ2MGZmMDk2IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0dXNlciJ9.ZztN--0x4rJ8TPcO-sA5kmtNOcXd4yAqlbocoVVUegQpVtCUHYleX-GbdQMkCdF31AG-zXUZgr7lritKm1iH0nwhpLpt_UwGeqsuRlcE5gxhSpuIZBcumtLT5LcdImX4jrh0tvisos2wu4-FhNwue3OOGolH-J2VN5sqtiidQHRA7xN0QYL2x5vwTeKmgttJOEFFUVwfDXr9jdubXo19dohTwWJEb86deC-tHlzvHGeNKzsGcAxmYbtbZjwqHSr76ADQ2p2tRyK5nGEoyR-kvlbUED8foGRftKJyftUT3psOeLE_qu6HNFAIS-Oe0i6-G0w1Qxe1zYJpJUZpgLN16w","expires_in":300,"refresh_expires_in":1800,"refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIxYzYwODYwNS1hMjk3LTQxZTktYjYwNS0xNTRlOWY5YzIxNTMifQ.eyJleHAiOjE3NjE1NzQ4NzAsImlhdCI6MTc2MTU3MzA3MCwianRpIjoiMjY0NTBlNTYtNzczOC00MmZjLWE3M2MtNDBkYTEwYzRhYzVlIiwiaXNzIjoiaHR0cDovL2tleWNsb2FrLWh0dHAua2V5Y2xvYWsuc3ZjLmNsdXN0ZXIubG9jYWwvYXV0aC9yZWFsbXMvZGVlcGZvY3VzIiwiYXVkIjoiaHR0cDovL2tleWNsb2FrLWh0dHAua2V5Y2xvYWsuc3ZjLmNsdXN0ZXIubG9jYWwvYXV0aC9yZWFsbXMvZGVlcGZvY3VzIiwic3ViIjoiYTk2OWE1N2EtYTdmYy00MGQyLWJjMzAtODIxNGU0ZjE0ZWVhIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6InRhc2stY2xpZW50Iiwic2Vzc2lvbl9zdGF0ZSI6ImUzMzQyZDVhLTUwZjgtNDhkZi1iZTk5LTA2NzM0NjBmZjA5NiIsInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsInNpZCI6ImUzMzQyZDVhLTUwZjgtNDhkZi1iZTk5LTA2NzM0NjBmZjA5NiJ9.1mFOEw_BplE2bY-Ku8umoa6YNGzPjRDykVICUaDGOzU","token_type":"Bearer","not-before-policy":0,"session_state":"e3342d5a-50f8-48df-be99-0673460ff096","scope":"profile email"}


# First Successful POST request

curl --location 'http://localhost:8080/v1/tasks' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJGS0NaZGs0akpKV29LZ25IQnA1S3pKY0U3VTZJRDI5NmxXVVJIdkgxVG5JIn0.eyJleHAiOjE3NjE1NzMzNzAsImlhdCI6MTc2MTU3MzA3MCwianRpIjoiMGRmZGM0N2ItNGI5YS00MzQ1LWIxYWYtYWY4YTFmYjNkYTlkIiwiaXNzIjoiaHR0cDovL2tleWNsb2FrLWh0dHAua2V5Y2xvYWsuc3ZjLmNsdXN0ZXIubG9jYWwvYXV0aC9yZWFsbXMvZGVlcGZvY3VzIiwic3ViIjoiYTk2OWE1N2EtYTdmYy00MGQyLWJjMzAtODIxNGU0ZjE0ZWVhIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidGFzay1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiZTMzNDJkNWEtNTBmOC00OGRmLWJlOTktMDY3MzQ2MGZmMDk2IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJST0xFX1RBU0tfVVBEQVRFIiwiUk9MRV9UQVNLX0RFTEVURSIsIlJPTEVfVEFTS19DUkVBVEUiLCJST0xFX1RBU0tfUkVBRCJdfSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZTMzNDJkNWEtNTBmOC00OGRmLWJlOTktMDY3MzQ2MGZmMDk2IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0dXNlciJ9.ZztN--0x4rJ8TPcO-sA5kmtNOcXd4yAqlbocoVVUegQpVtCUHYleX-GbdQMkCdF31AG-zXUZgr7lritKm1iH0nwhpLpt_UwGeqsuRlcE5gxhSpuIZBcumtLT5LcdImX4jrh0tvisos2wu4-FhNwue3OOGolH-J2VN5sqtiidQHRA7xN0QYL2x5vwTeKmgttJOEFFUVwfDXr9jdubXo19dohTwWJEb86deC-tHlzvHGeNKzsGcAxmYbtbZjwqHSr76ADQ2p2tRyK5nGEoyR-kvlbUED8foGRftKJyftUT3psOeLE_qu6HNFAIS-Oe0i6-G0w1Qxe1zYJpJUZpgLN16w' \
--header 'Cookie: JSESSIONID=41F2D537C2887512A6D9678B9C8004AE' \
--data '{
    "title": "Cluster test task",
    "description": "Token path is working",
    "tags": [
        "k8s",
        "auth"
    ],
    "intervalPlanId": "550e8400-e29b-41d4-a716-446655440000"
}'