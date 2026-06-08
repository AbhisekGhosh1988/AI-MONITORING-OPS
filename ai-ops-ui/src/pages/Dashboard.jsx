import { useEffect, useState } from "react";

import Sidebar from "../components/layout/Sidebar";
import Header from "../components/layout/Header";

import MetricCard from "../components/cards/MetricCard";
import RecommendationCard from "../components/cards/RecommendationCard";
import HealthScoreCard from "../components/cards/HealthScoreCard";
import RootCauseCard from "../components/cards/RootCauseCard";
import ForecastCard from "../components/cards/ForecastCard";
import AnomalyCard from "../components/cards/AnomalyCard";
import ExecutiveSummaryCard from "../components/cards/ExecutiveSummaryCard";

import CpuChart from "../components/charts/CpuChart";
import MemoryChart from "../components/charts/MemoryChart";

import RecentAlerts from "../components/tables/RecentAlerts";
import AIDecisions from "../components/tables/AIDecisions";
import ScalingHistory from "../components/tables/ScalingHistory";
import SystemHealth from "../components/tables/SystemHealth";

import AIOpsAssistant from "../components/chat/AIOpsAssistant";

import {
  getDashboardDetails,
  getHealthScore,
  getRootCause,
  getForecast,
  getAnomalies,
  getExecutiveSummary
} from "../services/dashboardService";

const Dashboard = () => {

  const [dashboardData, setDashboardData] = useState(null);
  const [loading, setLoading] = useState(true);

  const [healthScore, setHealthScore] = useState(null);
  const [rootCause, setRootCause] = useState(null);
  const [forecast, setForecast] = useState(null);
  const [anomalies, setAnomalies] = useState([]);
  const [executiveSummary, setExecutiveSummary] = useState(null);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {

    try {

      const [
        dashboard,
        health,
        root,
        forecastData,
        anomalyData,
        summaryData
      ] = await Promise.all([
        getDashboardDetails(),
        getHealthScore(),
        getRootCause(),
        getForecast(),
        getAnomalies(),
        getExecutiveSummary()
      ]);

      setDashboardData(dashboard);
      setHealthScore(health);
      setRootCause(root);
      setForecast(forecastData);
      setAnomalies(anomalyData);
      setExecutiveSummary(summaryData);

    } catch (error) {

      console.error(error);

    } finally {

      setLoading(false);

    }
  };

  if (loading) {
    return (
      <div
        style={{
          color: "white",
          padding: "40px"
        }}
      >
        Loading Dashboard...
      </div>
    );
  }

  if (!dashboardData) {
    return (
      <div
        style={{
          color: "white",
          padding: "40px"
        }}
      >
        Failed to load dashboard data.
      </div>
    );
  }

  const latestDecision =
    dashboardData.aiDecisions?.[
      dashboardData.aiDecisions.length - 1
    ] || null;

  return (
    <div className="app-layout">

      <Sidebar />

      <div className="main-content">

        <Header />

        <div className="dashboard-content">

          <h1 className="dashboard-title">
            Dashboard
          </h1>

          <p className="dashboard-subtitle">
            Real-time overview of your Kubernetes environment
          </p>

          {/* Metrics */}

          <div className="metrics-grid">

            <MetricCard
              title="Running Pods"
              value={
                dashboardData.summary.podDetails.runningPods
              }
              subtitle="Healthy Pods"
              color="#22c55e"
            />

            <MetricCard
              title="CPU Usage"
              value={`${dashboardData.summary.cpuPercent.toFixed(0)}%`}
              subtitle="Current Usage"
              color="#ef4444"
            />

            <MetricCard
              title="Memory Usage"
              value={`${Math.round(
                dashboardData.summary.memoryMb
              )} MB`}
              subtitle="Current Usage"
              color="#f59e0b"
            />

            <MetricCard
              title="Active Alerts"
              value={dashboardData.summary.alerts}
              subtitle="Total Alerts"
              color="#ef4444"
            />

            <MetricCard
              title="Restart Count"
              value={
                dashboardData.summary?.podDetails
                  ?.restartCount || 0
              }
              subtitle="Last 24h"
              color="#3b82f6"
            />

            <HealthScoreCard
              healthScore={healthScore}
            />

          </div>

          {/* AI Recommendation */}

          <RecommendationCard
            decision={latestDecision}
          />

          {/* Root Cause + Forecast */}

          <div
            style={{
              display: "grid",
              gridTemplateColumns: "1fr 1fr",
              gap: "20px",
              marginTop: "20px",
              marginBottom: "20px"
            }}
          >
            <RootCauseCard
              data={rootCause}
            />

            <ForecastCard
              forecast={forecast}
            />
          </div>

          {/* Anomaly Detection */}

          <div
            style={{
              marginBottom: "20px"
            }}
          >
            <AnomalyCard
              anomalies={anomalies}
            />
          </div>

          {/* Executive Summary */}

          <div
            style={{
              marginBottom: "20px"
            }}
          >
            <ExecutiveSummaryCard
              summary={executiveSummary}
            />
          </div>

          {/* Charts */}

          <div className="charts-grid">

            <CpuChart
              metricsHistory={
                dashboardData.metricsHistory
              }
            />

            <MemoryChart
              metricsHistory={
                dashboardData.metricsHistory
              }
            />

            <RecentAlerts
              alerts={dashboardData.alerts}
            />

          </div>

          {/* Bottom Grid */}

          <div className="bottom-grid">

            <AIDecisions
              decisions={
                dashboardData.aiDecisions
              }
            />

            <ScalingHistory
              history={
                dashboardData.scalingHistory
              }
            />

            <SystemHealth
              health={
                dashboardData.health
              }
            />

          </div>

        </div>

      </div>

      <AIOpsAssistant />

    </div>
  );
};

export default Dashboard;