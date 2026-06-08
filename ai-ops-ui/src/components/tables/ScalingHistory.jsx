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

const ScalingHistory = ({
  history = [],
}) => {
  const rows = history
    .slice(-5)
    .reverse();

  return (
    <div className="table-card">
      <div className="table-title">
        Scaling History
      </div>

      <table>
        <thead>
          <tr>
            <th>Action</th>
            <th>Replicas</th>
            <th>Reason</th>
          </tr>
        </thead>

        <tbody>
          {rows.map((row) => (
            <tr key={row.id}>
              <td>
                <span
                  className={getActionClass(
                    row.action
                  )}
                >
                  {row.action
                    ?.replaceAll("_", " ")}
                </span>
              </td>

              <td>
                <strong>
                  {row.previousReplicas}
                </strong>

                {" → "}

                <strong>
                  {row.replicas}
                </strong>
              </td>

              <td>{row.reason}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ScalingHistory;