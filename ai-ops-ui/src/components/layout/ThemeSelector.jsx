import { useTheme } from "../../context/ThemeContext";

const ThemeSelector = () => {
  const { theme, setTheme } =
    useTheme();

  return (
    <select
      value={theme}
      onChange={(e) =>
        setTheme(e.target.value)
      }
      className="theme-select"
    >
      <option value="ops">
        Deep Ops
      </option>

      <option value="cyber">
        Cyber Neon
      </option>

      <option value="k8s">
        Kubernetes Blue
      </option>

      <option value="matrix">
        Matrix Green
      </option>

      <option value="light">
        Light Enterprise
      </option>

      <option value="ai">
        Purple AI
      </option>
    </select>
  );
};

export default ThemeSelector;