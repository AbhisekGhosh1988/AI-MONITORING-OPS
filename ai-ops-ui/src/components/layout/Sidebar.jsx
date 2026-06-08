import {
  LayoutDashboard,
  Bell,
  Brain,
  Activity,
  BarChart3,
  HeartPulse,
  Settings,
} from "lucide-react";

const Sidebar = () => {
  const menus = [
    {
      name: "Dashboard",
      icon: <LayoutDashboard size={18} />,
    },
    {
      name: "Alerts",
      icon: <Bell size={18} />,
    },
    {
      name: "AI Decisions",
      icon: <Brain size={18} />,
    },
    {
      name: "Scaling History",
      icon: <Activity size={18} />,
    },
    {
      name: "Metrics",
      icon: <BarChart3 size={18} />,
    },
    {
      name: "System Health",
      icon: <HeartPulse size={18} />,
    },
    {
      name: "Settings",
      icon: <Settings size={18} />,
    },
  ];

  return (
    <aside className="sidebar">
      <div className="logo-section">
        <h2>AI OPS PLATFORM</h2>
        <span>Kubernetes Operations</span>
      </div>

      <ul className="menu-list">
        {menus.map((item, index) => (
          <li
            key={item.name}
            className={
              index === 0
                ? "menu-item active"
                : "menu-item"
            }
          >
            {item.icon}
            <span>{item.name}</span>
          </li>
        ))}
      </ul>
    </aside>
  );
};

export default Sidebar;