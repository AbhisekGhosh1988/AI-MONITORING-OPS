# AI Ops Platform UI Setup Guide

## Overview

This guide explains how to set up and run the AI Ops Platform React Dashboard UI.

### Technology Stack

* React 18+
* Vite
* Recharts
* Axios
* React Icons
* CSS (Custom Theme Engine)
* Spring Boot Backend Integration

---

# Project Structure

```text
src/
│
├── assets/
│   ├── ai-brain.png
│
├── components/
│   │
│   ├── cards/
│   │   ├── MetricCard.jsx
│   │   └── RecommendationCard.jsx
│   │
│   ├── charts/
│   │   ├── CpuChart.jsx
│   │   └── MemoryChart.jsx
│   │
│   ├── layout/
│   │   ├── Header.jsx
│   │   ├── Sidebar.jsx
│   │   └── ThemeSelector.jsx
│   │
│   └── tables/
│       ├── RecentAlerts.jsx
│       ├── AIDecisions.jsx
│       ├── ScalingHistory.jsx
│       └── SystemHealth.jsx
│
├── pages/
│   └── Dashboard.jsx
│
├── services/
│   └── dashboardService.js
│
├── App.jsx
├── App.css
├── index.css
└── main.jsx
```

---

# Installation

## Create Project

```bash
npm create vite@latest ai-ops-ui -- --template react
cd ai-ops-ui
```

---

## Install Dependencies

```bash
npm install
npm install axios
npm install recharts
npm install react-icons
```

---

# Backend Integration

Backend URL:

```http
http://localhost:8080/dashboard/details
```

Create:

```text
src/services/dashboardService.js
```

```javascript
import axios from "axios";

const API_BASE_URL = "http://localhost:8080";

export const getDashboardDetails = async () => {
  const response = await axios.get(
    `${API_BASE_URL}/dashboard/details`
  );

  return response.data;
};
```

---

# Dashboard Data Flow

```text
Dashboard.jsx
       │
       ▼
dashboardService.js
       │
       ▼
Spring Boot API
       │
       ▼
Dashboard Response
       │
       ▼
React Components
```

---

# Current Dashboard Sections

## Header

Displays:

* System Status
* Theme Selector
* Current Time
* Current Date
* User Avatar

---

## Metrics Row

Displays:

* Running Pods
* CPU Usage
* Memory Usage
* Restart Count
* Active Alerts
* Last AI Action

---

## AI Recommendation

Displays:

* AI Brain Image
* Recommendation
* Confidence
* Reason
* Action
* Recommended Replicas
* Executed Status

---

## CPU Chart

Displays:

```text
CPU Usage %
```

Uses:

```jsx
<CpuChart metricsHistory={data.metricsHistory} />
```

---

## Memory Chart

Displays:

```text
Memory Usage MB
```

Uses:

```jsx
<MemoryChart metricsHistory={data.metricsHistory} />
```

---

## Recent Alerts

Displays:

* Alert Message
* Alert Severity
* Time

Uses:

```jsx
<RecentAlerts alerts={data.alerts} />
```

---

## AI Decisions

Displays:

* Action
* Replicas
* Confidence
* Status

Uses:

```jsx
<AIDecisions decisions={data.aiDecisions} />
```

---

## Scaling History

Displays:

* Action
* Previous Replica Count
* Current Replica Count
* Reason

Uses:

```jsx
<ScalingHistory history={data.scalingHistory} />
```

---

## System Health

Displays:

* Application
* Database
* Kubernetes
* Prometheus
* AI Service

Uses:

```jsx
<SystemHealth health={data.health} />
```

---

# Theme Support

Current Themes:

```text
Deep Ops
Cyber Neon
Kubernetes Blue
Matrix Green
Light Mode
AI Purple
```

Theme classes:

```css
.theme-ops
.theme-cyber
.theme-k8s
.theme-matrix
.theme-light
.theme-ai
```

Theme selected from:

```jsx
ThemeSelector.jsx
```

---

# Assets

Place AI Brain image here:

```text
src/assets/ai-brain.png
```

Used by:

```jsx
RecommendationCard.jsx
```

---

# Run Application

```bash
npm run dev
```

Default URL:

```text
http://localhost:5173
```

---

# Production Build

```bash
npm run build
```

Preview:

```bash
npm run preview
```

---

# Future Enhancements

## AI Insights

Add:

```text
Predicted CPU
Predicted Memory
Cluster Risk Score
```

---

## Root Cause Analysis

Add:

```text
Affected Service
Timeline
Probable Cause
Confidence
```

---

## AI Summary

Add:

```text
Cluster Health Summary
Resource Usage Summary
Incident Summary
```

---

## AI Copilot

New Endpoint:

```http
POST /ai/copilot
```

Features:

* Ask AI
* Root Cause Explanation
* Scaling Suggestions
* Pod Diagnostics

---

# Backend API Contract

```http
GET /dashboard/details
```

Must return:

```json
{
  "summary": {},
  "metricsHistory": [],
  "alerts": [],
  "aiDecisions": [],
  "scalingHistory": [],
  "health": {}
}
```

---

# Completed Features

✅ Sidebar Navigation

✅ Dashboard Header

✅ Theme Switching

✅ Metric Cards

✅ AI Recommendation Panel

✅ CPU Chart

✅ Memory Chart

✅ Recent Alerts

✅ AI Decisions

✅ Scaling History

✅ System Health

✅ Backend Integration

✅ Responsive Layout

✅ Dark Theme UI

✅ Real-time Dashboard Rendering

---

Version: 1.0.0

Project: AI Ops Platform

Frontend: React + Vite

Backend: Spring Boot + Kubernetes + Prometheus
