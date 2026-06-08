const ExecutiveSummaryCard = ({ summary }) => {

  if (!summary) return null;

  const riskColor =
    summary.riskLevel === "HIGH"
      ? "#ef4444"
      : summary.riskLevel === "MEDIUM"
      ? "#f59e0b"
      : "#22c55e";

  return (
    <div
      className="ai-card"
      style={{
        border:
          "1px solid rgba(96,165,250,0.3)"
      }}
    >

      <div
        style={{
          fontSize: "22px",
          color: "#60a5fa",
          marginBottom: "15px",
          fontWeight: "bold"
        }}
      >
        🧠 Executive Summary
      </div>

      <div
        style={{
          color: "#e2e8f0",
          lineHeight: "1.9"
        }}
      >
        {summary.summary}
      </div>

      <div
        style={{
          marginTop: "20px",
          display: "flex",
          gap: "30px"
        }}
      >
        <div
          style={{
            color: riskColor,
            fontWeight: "bold"
          }}
        >
          Risk Level:
          {summary.riskLevel}
        </div>

        <div
          style={{
            color: "#22c55e",
            fontWeight: "bold"
          }}
        >
          Health Score:
          {summary.healthScore}
        </div>
      </div>

    </div>
  );
};

export default ExecutiveSummaryCard;