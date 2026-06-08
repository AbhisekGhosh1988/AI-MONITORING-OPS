import Dashboard from "./pages/Dashboard";
import { useTheme } from "./context/ThemeContext";

function App() {
  const { theme } = useTheme();

  return (
    <div className={`theme-${theme}`}>
      <Dashboard />
    </div>
  );
}

export default App;