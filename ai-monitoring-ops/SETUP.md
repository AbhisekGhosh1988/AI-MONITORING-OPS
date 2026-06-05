# COMPLETE INSTALLATION GUIDE

# Spring Boot Order Service + Oracle Kubernetes Engine (OKE) + Prometheus + Grafana + AI Ops

## Purpose

This guide is intended for a developer who:

* Has never worked with Kubernetes before
* Has only downloaded the Order Service source code from GitHub
* Wants to deploy the application to Oracle Cloud
* Wants Prometheus and Grafana monitoring
* Wants to access everything externally
* Wants to run the AI Ops platform against the Oracle cluster

This is an installation and operations guide, not an architecture document.

---

# 1. System Requirements

## Local Machine

Minimum:

* Windows 10/11
* 8 GB RAM
* 20 GB Free Disk

Recommended:

* 16 GB RAM
* 50 GB Free Disk

---

# 2. Required Software

Install everything before continuing.

## Git

Download:

https://git-scm.com/downloads

Verify:

```bash
git --version
```

---

## Java 21

Verify:

```bash
java -version
```

Expected:

```text
openjdk version "21"
```

---

## Maven

Verify:

```bash
mvn -version
```

---

## Docker Desktop

Download:

https://www.docker.com/products/docker-desktop

Enable:

* Kubernetes (optional)
* WSL2 Backend

Verify:

```bash
docker version
```

---

## kubectl

Install:

```powershell
winget install Kubernetes.kubectl
```

Verify:

```bash
kubectl version --client
```

---

## Helm

Install:

```powershell
winget install Helm.Helm
```

Verify:

```bash
helm version
```

---

## OCI CLI

Download:

https://docs.oracle.com/en-us/iaas/Content/API/SDKDocs/cliinstall.htm

Verify:

```bash
oci --version
```

---

# 3. Oracle Cloud Setup

Create:

* Oracle Cloud Account
* Compartment
* User
* API Key

---

# 4. Configure OCI CLI

Run:

```bash
oci setup config
```

Provide:

```text
User OCID
Tenancy OCID
Region
Private Key
Fingerprint
```

Verify:

```bash
oci os ns get
```

If successful:

```json
{
  "data": "your-namespace"
}
```

---

# 5. Create Oracle Kubernetes Engine (OKE)

Open:

```text
OCI Console
→ Developer Services
→ Kubernetes Clusters
```

Create Cluster.

Choose:

```text
Quick Create
```

Recommended:

```text
Cluster Name:
ai-ops-cluster

Node Shape:
VM.Standard.E4.Flex

Nodes:
2

Kubernetes Version:
Latest Stable
```

Create.

Wait:

```text
Status = Active
```

---

# 6. Configure kubectl for OKE

Navigate:

```text
Cluster
→ Access Cluster
→ Local Access
```

Run generated command.

Example:

```bash
oci ce cluster create-kubeconfig \
--cluster-id <cluster-id> \
--file $HOME/.kube/config \
--region ap-hyderabad-1 \
--token-version 2.0.0
```

Verify:

```bash
kubectl get nodes
```

Expected:

```text
NAME       STATUS
worker-1   Ready
worker-2   Ready
```

---

# 7. Clone Order Service

```bash
git clone <repo-url>

cd order-service
```

---

# 8. Build Application

```bash
mvn clean package
```

Verify:

```text
BUILD SUCCESS
```

---

# 9. Run Locally

```bash
java -jar target/order-service.jar
```

Test:

```bash
curl http://localhost:8081/actuator/health
```

Expected:

```json
{
  "status":"UP"
}
```

---

# 10. Enable Prometheus Metrics

application.yml

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus

  endpoint:
    health:
      show-details: always

  prometheus:
    metrics:
      export:
        enabled: true
```

Dependencies:

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

Verify:

```bash
curl http://localhost:8081/actuator/prometheus
```

---

# 11. Dockerize Application

Dockerfile

```dockerfile
FROM eclipse-temurin:21-jre

COPY target/order-service.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app.jar"]
```

Build:

```bash
docker build -t order-service:1.0 .
```

Run:

```bash
docker run -p 8081:8081 order-service:1.0
```

---

# 12. Push Image to Docker Hub

Login:

```bash
docker login
```

Tag:

```bash
docker tag order-service:1.0 youruser/order-service:1.0
```

Push:

```bash
docker push youruser/order-service:1.0
```

---

# 13. Create Deployment

deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment

metadata:
  name: order-service

spec:
  replicas: 2

  selector:
    matchLabels:
      app: order-service

  template:
    metadata:
      labels:
        app: order-service

    spec:
      containers:
      - name: order-service
        image: youruser/order-service:1.0

        ports:
        - containerPort: 8081
```

Deploy:

```bash
kubectl apply -f deployment.yaml
```

Verify:

```bash
kubectl get pods
```

---

# 14. Create Service

service.yaml

```yaml
apiVersion: v1
kind: Service

metadata:
  name: order-service

  labels:
    app: order-service

spec:
  selector:
    app: order-service

  ports:
  - name: http
    port: 8081
    targetPort: 8081

  type: LoadBalancer
```

Deploy:

```bash
kubectl apply -f service.yaml
```

---

# 15. Obtain External URL

```bash
kubectl get svc
```

Example:

```text
NAME            TYPE           EXTERNAL-IP
order-service   LoadBalancer   140.245.196.185
```

Application URL:

```text
http://140.245.196.185:8081
```

Health:

```text
http://140.245.196.185:8081/actuator/health
```

Metrics:

```text
http://140.245.196.185:8081/actuator/prometheus
```

---

# 16. Install Prometheus + Grafana

Add Helm Repo

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts

helm repo update
```

Create Namespace

```bash
kubectl create namespace monitoring
```

Install

```bash
helm install monitoring prometheus-community/kube-prometheus-stack \
-n monitoring
```

Verify:

```bash
kubectl get pods -n monitoring
```

Expected:

```text
Prometheus
Grafana
Alertmanager
Operator
Node Exporter
```

---

# 17. Access Grafana

```bash
kubectl get svc -n monitoring
```

Example:

```text
monitoring-grafana
LoadBalancer
129.159.xxx.xxx
```

Open:

```text
http://129.159.xxx.xxx
```

Username:

```text
admin
```

Password:

```bash
kubectl get secret monitoring-grafana \
-n monitoring \
-o jsonpath="{.data.admin-password}" | base64 -d
```

---

# 18. Access Prometheus

Find External IP:

```bash
kubectl get svc -n monitoring
```

Example:

```text
monitoring-kube-prometheus-prometheus
129.159.224.174
```

Open:

```text
http://129.159.224.174:9090
```

---

# 19. Create ServiceMonitor

servicemonitor.yaml

```yaml
apiVersion: monitoring.coreos.com/v1
kind: ServiceMonitor

metadata:
  name: order-service-monitor
  namespace: monitoring

  labels:
    release: monitoring

spec:

  namespaceSelector:
    matchNames:
      - default

  selector:
    matchLabels:
      app: order-service

  endpoints:
    - port: http
      path: /actuator/prometheus
      interval: 15s
```

Apply:

```bash
kubectl apply -f servicemonitor.yaml
```

Verify:

```bash
kubectl get servicemonitor -A
```

---

# 20. Verify Metrics

Open Prometheus.

Query:

```promql
up
```

Should show:

```text
job="order-service"
value=1
```

---

# 21. Useful Prometheus Queries

CPU

```promql
sum(rate(container_cpu_usage_seconds_total{pod=~"order-service.*"}[5m]))
```

Memory

```promql
sum(container_memory_working_set_bytes{pod=~"order-service.*"})
```

Restarts

```promql
sum(kube_pod_container_status_restarts_total{pod=~"order-service.*"})
```

Pod Count

```promql
count(kube_pod_status_phase{phase="Running",pod=~"order-service.*"} == 1)
```

Pod Status

```promql
kube_pod_status_phase{pod=~"order-service.*"} == 1
```

Heap Memory

```promql
sum(jvm_memory_used_bytes{area="heap"})
```

Non Heap Memory

```promql
sum(jvm_memory_used_bytes{area="nonheap"})
```

Process Start Time

```promql
max(process_start_time_seconds)
```

---

# 22. Create Grafana Dashboard

Create variables:

Application

```promql
label_values(application)
```

Instance

```promql
label_values(jvm_memory_used_bytes{application="$application"},instance)
```

Heap Pool

```promql
label_values(jvm_memory_used_bytes{application="$application",area="heap"},id)
```

Non Heap Pool

```promql
label_values(jvm_memory_used_bytes{application="$application",area="nonheap"},id)
```

---

# 23. Run AI Ops Application

Build:

```bash
mvn clean package
```

Run:

```bash
java -jar target/ai-monitoring-ops.jar
```

---

# 24. Configure AI Ops to Use OKE Prometheus

application.yml

```yaml
prometheus:
  url: http://129.159.224.174:9090
```

Never use:

```yaml
prometheus:
  url: http://localhost:9090
```

unless Prometheus is running locally.

---

# 25. Auto Scaling

AI Ops scales deployments using:

```bash
kubectl scale deployment order-service --replicas=5
```

Manual test:

```bash
kubectl scale deployment order-service --replicas=5
```

Verify:

```bash
kubectl get pods
```

---

# 26. Common Troubleshooting

## ServiceMonitor Not Found

```bash
kubectl get servicemonitor -A
```

Verify:

```yaml
release: monitoring
```

---

## Metrics Not Showing

Verify:

```bash
curl http://<external-ip>:8081/actuator/prometheus
```

---

## Grafana No Data

Verify:

```promql
up
```

returns results.

---

## 401 Unauthorized

Verify:

```bash
kubectl get pods
```

works.

Use kubectl-based scaling instead of Kubernetes Java Client.

---

## Connection Refused

Verify:

```bash
curl http://129.159.224.174:9090/-/healthy
```

---

# 27. Useful Commands

```bash
kubectl get pods

kubectl get svc

kubectl get deployments

kubectl logs <pod>

kubectl describe pod <pod>

kubectl scale deployment order-service --replicas=5

kubectl delete pod <pod>

kubectl rollout restart deployment order-service
```

---

# Setup Completed

At this point you should have:

* OKE Cluster
* Order Service
* Prometheus
* Grafana
* ServiceMonitor
* External URLs
* AI Ops Application
* Auto Scaling
* Monitoring Dashboard
* Oracle Cloud Deployment
