const getSeverityColor = (severity) => {
  switch (severity) {
    case "HIGH":
      return "#ef4444";

    case "MEDIUM":
      return "#f59e0b";

    case "ERROR":
      return "#dc2626";

    default:
      return "#3b82f6";
  }
};

const formatTime = (createdAt) => {
  if (!createdAt) return "--";

  const hour = createdAt[3];
  const minute = createdAt[4];

  const date = new Date();
  date.setHours(hour);
  date.setMinutes(minute);

  return date.toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
  });
};

const RecentAlerts = ({ alerts = [] }) => {
  const latestAlerts = alerts.slice(-5).reverse();

  return (
    <div className="table-card recent-alerts-card">
      <div className="table-header">
        <div className="table-title">
          Recent Alerts
        </div>

        <span className="view-all">
          View All
        </span>
      </div>

      {latestAlerts.map((alert) => (
        <div
          key={alert.id}
          className="alert-item"
        >
          <div className="alert-left">
            <div className="alert-title-row">
              <span
                className="alert-dot"
                style={{
                  backgroundColor:
                    getSeverityColor(
                      alert.severity
                    ),
                }}
              />

              <span className="alert-title">
                {alert.message}
              </span>
            </div>
          </div>

          <div className="alert-right">
            <div className="alert-time">
              {formatTime(
                alert.createdAt
              )}
            </div>

            <span
              className="alert-badge"
              style={{
                backgroundColor:
                  getSeverityColor(
                    alert.severity
                  ),
              }}
            >
              {alert.severity}
            </span>
          </div>
        </div>
      ))}
    </div>
  );
};

export default RecentAlerts;