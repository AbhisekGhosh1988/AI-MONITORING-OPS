import { useEffect, useState } from "react";

import Sidebar from "../components/layout/Sidebar";
import Header from "../components/layout/Header";
import MetricCard from "../components/cards/MetricCard";
import RecommendationCard from "../components/cards/RecommendationCard";
import CpuChart from "../components/charts/CpuChart";
import MemoryChart from "../components/charts/MemoryChart";
import RecentAlerts from "../components/tables/RecentAlerts";
import AIDecisions from "../components/tables/AIDecisions";
import SystemHealth from "../components/tables/SystemHealth";
import ScalingHistory from "../components/tables/ScalingHistory";
import { getDashboardDetails } from "../services/dashboardService";

const Dashboard = () => {
  const [dashboardData, setDashboardData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      const response = await getDashboardDetails();
      setDashboardData(response);
    } catch (error) {
      console.error("Dashboard API Error:", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div
        style={{
          color: "white",
          padding: "40px",
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
          padding: "40px",
        }}
      >
        Failed to load dashboard data.
      </div>
    );
  }

  const latestDecision =
    dashboardData.aiDecisions?.[dashboardData.aiDecisions.length - 1] || null;

  return (
    <div className="app-layout">
      <Sidebar />

      <div className="main-content">
        <Header />

        <div className="dashboard-content">
          <h1 className="dashboard-title">Dashboard</h1>

          <p className="dashboard-subtitle">
            Real-time overview of your Kubernetes environment
          </p>

          <div className="metrics-grid">
            <MetricCard
              title="Running Pods"
              value={dashboardData.summary.podDetails.runningPods}
              subtitle="Healthy Pods"
              color="#22c55e"
            />

            <MetricCard
              title="CPU Usage"
              value={`${(dashboardData.summary.cpuPercent * 100).toFixed(0)}%`}
              subtitle="Current Usage"
              color="#ef4444"
            />

            <MetricCard
              title="Memory Usage"
              value={`${Math.round(dashboardData.summary.memoryMb)} MB`}
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
              value={dashboardData.summary?.podDetails?.restartCount || 0}
              subtitle="Last 24h"
              color="#3b82f6"
            />

            <MetricCard
              title="Last AI Action"
              value={
                latestDecision?.action?.replaceAll("_", " ")?.toUpperCase() ||
                "N/A"
              }
              subtitle="Latest Decision"
              color="#22c55e"
            />
          </div>

          <RecommendationCard decision={latestDecision} />

         <div className="charts-grid">
  <CpuChart metricsHistory={dashboardData.metricsHistory} />

  <MemoryChart metricsHistory={dashboardData.metricsHistory} />

  <RecentAlerts alerts={dashboardData.alerts} />
</div>

        <div className="bottom-grid">
  <AIDecisions decisions={dashboardData.aiDecisions} />


  <ScalingHistory history={dashboardData.scalingHistory} />

  <SystemHealth health={dashboardData.health} />
</div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
