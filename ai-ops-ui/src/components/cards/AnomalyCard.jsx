const AnomalyCard = ({ anomalies }) => {

  return (
    <div className="ai-card">

      <div className="ai-card-title">
        Anomaly Detection
      </div>

      {anomalies.length === 0 ? (

        <div
          style={{
            color: "#22c55e",
            fontSize: "15px"
          }}
        >
          ✅ No anomalies detected
        </div>

      ) : (

        anomalies.map((a, index) => (

          <div
            key={index}
            style={{
              marginBottom: "12px",
              padding: "12px",
              borderRadius: "10px",
              background:
                "rgba(239,68,68,0.08)"
            }}
          >
            <strong>
              {a.type}
            </strong>

            <br />

            {a.message}

            <br />

            <span
              style={{
                color: "#ef4444"
              }}
            >
              {a.severity}
            </span>
          </div>

        ))

      )}

    </div>
  );
};

export default AnomalyCard;