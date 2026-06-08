import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  ReferenceLine,
} from "recharts";

const CpuChart = ({ metricsHistory = [] }) => {
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
      cpu: Number(
        (item.cpuPercent * 100).toFixed(2)
      ),
    }));

  return (
    <div className="chart-card">
      <h3>CPU Usage (%)</h3>

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

          <ReferenceLine
            y={80}
            stroke="#ef4444"
            strokeDasharray="5 5"
            label="80%"
          />

          <Line
            type="monotone"
            dataKey="cpu"
            stroke="#3b82f6"
            strokeWidth={3}
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default CpuChart;