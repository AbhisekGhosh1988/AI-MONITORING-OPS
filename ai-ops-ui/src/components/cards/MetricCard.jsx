import {
  Cpu,
  MemoryStick,
  Bell,
  RotateCcw,
  Bot,
  Boxes,
} from "lucide-react";

const icons = {
  "Running Pods": <Boxes size={28} />,
  "CPU Usage": <Cpu size={28} />,
  "Memory Usage": <MemoryStick size={28} />,
  "Restart Count": <RotateCcw size={28} />,
  "Active Alerts": <Bell size={28} />,
  "Last AI Action": <Bot size={28} />,
};

const MetricCard = ({
  title,
  value,
  subtitle,
  color,
}) => {
  return (
    <div className="metric-card">
      <div className="metric-top">
        <div
          style={{
            color,
          }}
        >
          {icons[title]}
        </div>

        <div>
          <h4>{title}</h4>
        </div>
      </div>

      <div
        className="metric-value"
        style={{ color }}
      >
        {value}
      </div>

      <div className="metric-footer">
        {subtitle}
      </div>

      <div
        className="metric-progress"
        style={{
          background: color,
        }}
      />
    </div>
  );
};

export default MetricCard;