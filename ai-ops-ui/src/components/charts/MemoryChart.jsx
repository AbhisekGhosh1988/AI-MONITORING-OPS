import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

const MemoryChart = ({ metricsHistory = [] }) => {
  const chartData = metricsHistory
    .slice(0, 20)
    .reverse()
    .map((item) => ({
      time: `${String(item.createdAt[3]).padStart(
        2,
        "0"
      )}:${String(item.createdAt[4]).padStart(
        2,
        "0"
      )}`,
      memory: Math.round(item.memoryMb),
    }));

  const maxMemory =
    chartData.length > 0
      ? Math.max(
          ...chartData.map(
            (item) => item.memory
          )
        )
      : 0;

  return (
    <div className="chart-card">
      <h3>Memory Usage (MB)</h3>

      <ResponsiveContainer
        width="100%"
        height={250}
      >
        <LineChart data={chartData}>
          <XAxis
            dataKey="time"
            stroke="#94a3b8"
          />

          <YAxis stroke="#94a3b8" />

          <Tooltip />

          <Line
            type="monotone"
            dataKey="memory"
            stroke="#a855f7"
            strokeWidth={3}
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>

      <div
        style={{
          marginTop: "10px",
          color: "#94a3b8",
          fontSize: "12px",
        }}
      >
        Peak Memory: {maxMemory} MB
      </div>
    </div>
  );
};

export default MemoryChart;