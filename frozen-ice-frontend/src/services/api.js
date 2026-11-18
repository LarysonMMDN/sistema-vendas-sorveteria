
import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

// Envia o token automaticamente.
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("frozen_ice_token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

// Limpa a sessão quando a autenticação expirar.
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("frozen_ice_token");
      localStorage.removeItem("frozen_ice_usuario");
    }

    return Promise.reject(error);
  }
);

export default api;
