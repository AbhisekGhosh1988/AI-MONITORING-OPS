const ForecastCard = ({ forecast }) => {

  if (!forecast) return null;

  return (
    <div className="ai-card">

      <div className="ai-card-title">
        Forecast
      </div>

      <div
        style={{
          display: "grid",
          gridTemplateColumns: "1fr 1fr",
          gap: "20px"
        }}
      >

        <div>
          <h4 style={{ color: "#22c55e" }}>
            CPU
          </h4>

          <p>Now: {forecast.currentCpu}%</p>
          <p>15m: {forecast.cpu15Min}%</p>
          <p>30m: {forecast.cpu30Min}%</p>
        </div>

        <div>
          <h4 style={{ color: "#a855f7" }}>
            Memory
          </h4>

          <p>Now: {forecast.currentMemory} MB</p>
          <p>15m: {forecast.memory15Min} MB</p>
          <p>30m: {forecast.memory30Min} MB</p>
        </div>

      </div>

    </div>
  );
};

export default ForecastCard;