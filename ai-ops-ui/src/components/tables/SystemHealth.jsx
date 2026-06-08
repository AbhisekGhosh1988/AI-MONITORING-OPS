const SystemHealth = ({
  health = {},
}) => {
  const services = [
    {
      name: "Application",
      status: health.application,
    },
    {
      name: "Database",
      status: health.database,
    },
    {
      name: "Kubernetes",
      status: health.kubernetes,
    },
    {
      name: "Prometheus",
      status: health.prometheus,
    },
    {
      name: "AI Service",
      status: health.ai,
    },
  ];

  return (
    <div className="table-card">
      <div className="table-title">
        System Health
      </div>

      {services.map((service) => (
        <div
          key={service.name}
          className="health-row"
        >
          <span>{service.name}</span>

          <span
            className={
              service.status === "UP"
                ? "health-up"
                : "health-down"
            }
          >
            {service.status || "UNKNOWN"}
          </span>
        </div>
      ))}
    </div>
  );
};

export default SystemHealth;