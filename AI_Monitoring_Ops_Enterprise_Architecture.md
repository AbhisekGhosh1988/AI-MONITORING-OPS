# AI Monitoring Ops Platform - Enterprise Architecture

## 1. Executive Summary

AI Monitoring Ops is an enterprise AIOps platform built using:

- Spring Boot 3.5.3
- Java 21
- React Dashboard
- PostgreSQL
- Prometheus
- Kubernetes Client
- Ollama / OpenRouter LLM Integration
- Spring AI

Primary objectives:

1. Real-time monitoring
2. AI-driven health scoring
3. Intelligent recommendations
4. Auto-scaling decisions
5. Root cause analysis
6. Forecasting
7. Autonomous remediation

---

# 2. High Level Architecture (HLD)

```mermaid
flowchart LR

User[React Dashboard]

API[Spring Boot AIOps API]

Prometheus[(Prometheus)]
K8s[Kubernetes Cluster]
Postgres[(PostgreSQL)]
AI[LLM Layer<br/>Ollama/OpenRouter]

User --> API

API --> Prometheus
API --> K8s
API --> Postgres
API --> AI
```

---

# 3. Enterprise Architecture

```mermaid
flowchart TB

subgraph Presentation Layer
React[React Dashboard]
end

subgraph API Layer
Controllers[REST Controllers]
end

subgraph Core Services
Metrics[Metrics Service]
Health[Health Service]
Scaling[Scaling Service]
Alerting[Alert Service]
AIService[AI Recommendation Service]
Forecast[Forecast Engine]
end

subgraph Integrations
Prometheus[(Prometheus)]
K8s[Kubernetes API]
LLM[Ollama/OpenRouter]
end

subgraph Persistence
Postgres[(PostgreSQL)]
end

React --> Controllers

Controllers --> Metrics
Controllers --> Health
Controllers --> Scaling
Controllers --> Alerting
Controllers --> AIService
Controllers --> Forecast

Metrics --> Prometheus
Scaling --> K8s
AIService --> LLM

Metrics --> Postgres
Health --> Postgres
Scaling --> Postgres
Alerting --> Postgres
AIService --> Postgres
```

---

# 4. Current Package Design (LLD)

```text
com.ai.ops

├── controller
│   ├── DashBoardController
│   ├── AlertController
│   ├── ScaleController
│   ├── HealthController
│   ├── ChatController
│   └── AIDecisionController
│
├── service
│   ├── Metrics Service
│   ├── Prometheus Service
│   ├── AI Service
│   ├── Scaling Service
│   ├── Health Score Service
│   └── Forecast Service
│
├── repository
├── entity
├── client
├── config
└── event
```

---

# 5. Dashboard Request Flow

```mermaid
sequenceDiagram

actor User
participant React
participant API
participant Prometheus
participant DB

User->>React: Open Dashboard

React->>API: GET /dashboard

API->>Prometheus: Query metrics

Prometheus-->>API: CPU/Memory/Pods

API->>DB: Load historical data

DB-->>API: Trends

API-->>React: Dashboard Response

React-->>User: Render Dashboard
```

---

# 6. AI Recommendation Flow

```mermaid
sequenceDiagram

participant Dashboard
participant API
participant Metrics
participant AI

Dashboard->>API: Generate Recommendation

API->>Metrics: Fetch Metrics

Metrics-->>API: Current State

API->>AI: Build Prompt

AI-->>API: Recommendation

API-->>Dashboard: Scale Up / Scale Down
```
---

# 7. Auto Scaling Flow

```mermaid
flowchart LR

Metrics --> HealthScore

HealthScore --> AIEngine

AIEngine --> Decision

Decision --> ScaleUp
Decision --> ScaleDown
Decision --> NoAction

ScaleUp --> Kubernetes
ScaleDown --> Kubernetes
```

---

# 8. Database Design

## metric_snapshot

| Column | Type |
|----------|----------|
| id | bigint |
| created_at | timestamp |
| cpu_percent | double |
| memory_mb | double |
| running_pods | integer |
| restart_count | integer |

## alerts

| Column | Type |
|----------|----------|
| id | bigint |
| created_at | timestamp |
| severity | varchar |
| message | text |

## ai_decisions

| Column | Type |
|----------|----------|
| id | bigint |
| created_at | timestamp |
| action | varchar |
| replicas | integer |
| reason | text |
| executed | boolean |
| confidence | integer |
| ai_explanation | text |

## scaling_history

| Column | Type |
|----------|----------|
| id | bigint |
| created_at | timestamp |
| action | varchar |
| replicas | integer |
| reason | text |
| confidence | integer |
| ai_explanation | text |

---

# 9. Entity Relationship Diagram

```mermaid
erDiagram

METRIC_SNAPSHOT ||--o{ AI_DECISIONS : drives
AI_DECISIONS ||--o{ SCALING_HISTORY : creates
METRIC_SNAPSHOT ||--o{ ALERTS : generates
```

---

# 10. Health Score Engine

Health Score Formula

Health = 100
 - CPU Weight
 - Memory Weight
 - Restart Weight
 - Alert Weight

Suggested weights:

- CPU = 30%
- Memory = 30%
- Restarts = 20%
- Alerts = 20%

---

# 11. Future Enterprise Roadmap

Phase 1
- Health Score
- Explainability
- Root Cause Analysis
- Forecasting

Phase 2
- AI Chat Assistant
- Anomaly Detection
- Executive Summary

Phase 3
- Autonomous Remediation
- Cost Optimization

Phase 4
- Deployment Risk Analysis
- Multi Cluster Intelligence
- SRE Copilot

---

# 12. Recommended Enterprise Enhancements

## Vector Database

PGVector

Store:

- Incident history
- RCA reports
- Scaling actions
- Deployment failures
- Knowledge articles

## RAG Architecture

```mermaid
flowchart LR

UserQuestion

UserQuestion --> Embedding

Embedding --> PGVector

PGVector --> Context

Context --> LLM

LLM --> Answer
```

---

# 13. Observability Architecture

```mermaid
flowchart TB

Applications

Applications --> OpenTelemetry

OpenTelemetry --> Prometheus

OpenTelemetry --> Logs

Prometheus --> AIOps

Logs --> AIOps

AIOps --> AIEngine

AIEngine --> Recommendations
```

---

# 14. Security Architecture

- JWT Authentication
- RBAC
- Audit Logging
- API Gateway
- Secrets Management
- TLS Everywhere
- Kubernetes Service Accounts

---

# 15. Production Deployment

```mermaid
flowchart TB

Internet

Internet --> Ingress

Ingress --> ReactUI

Ingress --> SpringBoot

SpringBoot --> PostgreSQL

SpringBoot --> Prometheus

SpringBoot --> Ollama

SpringBoot --> Kubernetes
```

This document was generated from the uploaded AI Monitoring Ops project structure and enhanced with enterprise-grade architecture recommendations aligned with the platform roadmap.
