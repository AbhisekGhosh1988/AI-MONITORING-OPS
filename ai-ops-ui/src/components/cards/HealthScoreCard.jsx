const HealthScoreCard = ({ healthScore }) => {

  const score = healthScore?.score || 0;

const color =
  score >= 90
    ? "#22c55e"   // Green
    : score >= 70
    ? "#f59e0b"   // Orange
    : "#ef4444";  // Red

  return (
    <div className="metric-card">
      <div
        style={{
          fontSize: "12px",
          color,
          marginBottom: "10px",
        }}
      >
        Cluster Health
      </div>

      <div
        style={{
          fontSize: "36px",
          fontWeight: "bold",
          color,
        }}
      >
        {score}
      </div>

      <div
        style={{
          color: "#94a3b8",
          fontSize: "12px",
        }}
      >
        {healthScore?.status}
      </div>
    </div>
  );
};

export default HealthScoreCard;