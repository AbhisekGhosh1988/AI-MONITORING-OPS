import axios from "axios";

const API_URL = "http://localhost:8080/dashboard/details";

export const getDashboardDetails = async () => {
  const response = await axios.get(API_URL);
  return response.data;
};