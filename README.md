# Module 9 – Microservices, Monitoring, Kubernetes (Java 21 / Maven / Spring Boot)

Services: `micro-sender` (8081), `micro-recipient` (8082), `micro-collector` (8083, saves to Postgres), `micro-visualizer` (8084), plus RabbitMQ, Postgres, Prometheus, Grafana.

## Build
    mvn -B package -DskipTests

## Docker Compose (tasks 1-10, 20-21)
    docker compose up -d --build
    curl -X POST localhost:8081/notification -H "Content-Type: application/json" -d '{"user":"Name LastName","message":"hi"}'
    curl localhost:8082/message ; curl localhost:8084/saved-messages
Prometheus http://localhost:9090, Grafana http://localhost:3000 (admin/admin, dashboard "Micro services"). Networks: sender, recipient, monitoring. The collector runs 2 replicas, all scraped in one Prometheus job through DNS service discovery.

## Kubernetes (tasks 11-21, tested on kind)
    kind create cluster --name micro --config k8s/kind-config.yaml
    docker tag micro-collector micro-collector:1.1 ; docker tag micro-collector micro-collector:2.0 ; docker tag micro-recipient micro-recipient:2.0
    kind load docker-image --name micro <all images>
    kubectl create configmap grafana-dashboards --from-file=monitoring/grafana/provisioning/dashboards
    kubectl apply -f k8s/00-infra.yaml -f k8s/10-apps.yaml
NodePorts: sender 30081, visualizer 30084, Prometheus 30090, Grafana 30300.

- Blue-green (task 16): `kubectl apply -f k8s/20-collector-green.yaml`, patch the `micro-collector` Service selector to `slot=green`, scale blue to 0.
- Canary (tasks 18-19): `kubectl apply -f k8s/30-canary-v2.yaml` (recipient and collector v2 read one message per call; the collector interval is 5*N).
- Replicas (20) are scraped via the headless `micro-collector-metrics` service (21).

Screenshots are in [screenshots/](screenshots).
