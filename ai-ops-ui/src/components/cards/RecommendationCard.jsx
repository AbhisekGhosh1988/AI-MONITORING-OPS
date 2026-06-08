import aiBrain from "../../assets/ai-brain.png";

const RecommendationCard = ({ decision }) => {
  if (!decision) {
    return null;
  }

  const confidence = decision.confidence
    ? Math.round(decision.confidence * 100)
    : 0;

  const actionText = decision.action
    ?.replaceAll("_", " ")
    ?.toUpperCase();

  const actionColor =
    decision.action === "SCALE_UP"
      ? "#22c55e"
      : decision.action === "SCALE_DOWN"
        ? "#f59e0b"
        : "#3b82f6";

  return (
    <div className="recommendation-card">
      <div className="recommendation-left">
        <div className="ai-icon">
          <img
            src={aiBrain}
            alt="AI Brain"
            className="brain-image"
          />
        </div>

        <div>
          <div className="recommendation-title">
            AI Recommendation
          </div>

          <div className="recommendation-badge">
            {confidence}% Confidence
          </div>

          <h2
            className="scale-up"
            style={{
              color: actionColor,
            }}
          >
            {actionText || "NO ACTION"}
          </h2>

          <p className="recommendation-text">
            {decision.reason ||
              "No recommendation available"}
          </p>

          <div className="replicas">
            <div>
              <span>
                Recommended Replicas
              </span>

              <h3>
                {decision.replicas ?? "-"}
              </h3>
            </div>

            <div>
              <span>Executed</span>

              <h3>
                {decision.executed
                  ? "YES"
                  : "NO"}
              </h3>
            </div>
          </div>
        </div>
      </div>

      <div className="recommendation-right">
        <div className="confidence-circle">
          <div>
            <h1>{confidence}%</h1>

            <span>Confidence</span>
          </div>
        </div>

        <div className="reason-box">
          <h4>Reason</h4>

          <p>
            {decision.reason ||
              "No reason available"}
          </p>

          <h4>Action</h4>

          <p>
            {actionText || "NO ACTION"}
          </p>
        </div>
      </div>
    </div>
  );
};

export default RecommendationCard;