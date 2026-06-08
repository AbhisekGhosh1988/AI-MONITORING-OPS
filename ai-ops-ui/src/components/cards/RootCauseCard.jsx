const RootCauseCard = ({ data }) => {

  if (!data) return null;

  return (
    <div className="ai-card">

      <div className="ai-card-title">
        Root Cause Analysis
      </div>

      <div
        style={{
          color: "#ffffff",
          fontWeight: "600",
          marginBottom: "12px"
        }}
      >
        {data.cause}
      </div>

      <div
        style={{
          color: "#22c55e",
          marginBottom: "12px"
        }}
      >
        Confidence: {data.confidence}%
      </div>

      <div
        style={{
          color: "#94a3b8",
          lineHeight: "1.7"
        }}
      >
        {data.recommendation}
      </div>

    </div>
  );
};

export default RootCauseCard;