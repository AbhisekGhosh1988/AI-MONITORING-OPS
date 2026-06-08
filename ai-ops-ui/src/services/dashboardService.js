import axios from "axios";

const API_BASE = "http://localhost:8080";

export const getDashboardDetails = async () => {
  const response = await axios.get(
    `${API_BASE}/dashboard/details`
  );
  return response.data;
};

export const getHealthScore = async () => {
  const response = await axios.get(
    `${API_BASE}/health/health-score`
  );
  return response.data;
};

export const getRootCause = async () => {
  const response = await axios.get(
    `${API_BASE}/ai/root-cause`
  );
  return response.data;
};

export const getForecast = async () => {
  const response = await axios.get(
    `${API_BASE}/ai/forecast`
  );
  return response.data;
};
export const getAnomalies = async () => {

  const response =
    await axios.get(
      `${API_BASE}/ai/anomalies`
    );

  return response.data;
};
export const getExecutiveSummary = async () => {

  const response = await axios.get(
    `${API_BASE}/ai/executive-summary`
  );

  return response.data;
};