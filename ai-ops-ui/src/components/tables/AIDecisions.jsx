const getActionClass = (action) => {
  switch (action) {
    case "SCALE_UP":
      return "badge-green";

    case "SCALE_DOWN":
      return "badge-orange";

    case "NO_ACTION":
      return "badge-blue";

    default:
      return "badge-gray";
  }
};

const getConfidenceClass = (confidence) => {
  const value = (confidence || 0) * 100;

  if (value >= 85) return "text-green";
  if (value >= 70) return "text-yellow";

  return "text-red";
};

const AIDecisions = ({ decisions = [] }) => {
  const latestDecisions = decisions
    .slice(-5)
    .reverse();

  return (
    <div className="table-card">
      <div className="table-title">
        Recent AI Decisions
      </div>

      <table>
        <thead>
          <tr>
            <th>Action</th>
            <th>Replicas</th>
            <th>Confidence</th>
            <th>Status</th>
          </tr>
        </thead>

        <tbody>
          {latestDecisions.map((decision) => (
            <tr key={decision.id}>
              <td>
                <span
                  className={getActionClass(
                    decision.action
                  )}
                >
                  {decision.action
                    ?.replaceAll("_", " ") ||
                    "N/A"}
                </span>
              </td>

              <td>
                {decision.replicas ?? "-"}
              </td>

              <td
                className={getConfidenceClass(
                  decision.confidence
                )}
              >
                {decision.confidence
                  ? `${Math.round(
                      decision.confidence *
                        100
                    )}%`
                  : "-"}
              </td>

              <td>
                {decision.executed
                  ? "✅"
                  : "⏳"}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AIDecisions;