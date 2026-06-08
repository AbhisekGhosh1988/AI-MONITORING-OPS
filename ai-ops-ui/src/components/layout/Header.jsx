import ThemeSelector from "./ThemeSelector";
const Header = () => {
  const now = new Date();

  const time = now.toLocaleTimeString();

  const date = now.toLocaleDateString(
    "en-US",
    {
      month: "short",
      day: "2-digit",
      year: "numeric",
    }
  );

  return (
    <header className="header">
      <div className="health-status">
        <span className="green-dot"></span>
        System Healthy
      </div>

      <div className="header-right">
         <ThemeSelector />
        <div>
          <div>{time}</div>
          <div>{date}</div>
        </div>

        <div className="avatar">A</div>

        <span>Admin</span>
      </div>
    </header>
  );
};

export default Header;