
import api from "./api";

const TOKEN_KEY = "frozen_ice_token";
const USUARIO_KEY = "frozen_ice_usuario";

export async function fazerLogin(email, senha) {
  const resposta = await api.post("/api/auth/login", {
    email,
    senha,
  });

  const { token, usuario } = resposta.data;

  if (!token || !usuario) {
    throw new Error("A resposta do servidor não contém token e usuário.");
  }

  localStorage.setItem(TOKEN_KEY, token);
  localStorage.setItem(USUARIO_KEY, JSON.stringify(usuario));

  return usuario;
}

export function fazerLogout() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USUARIO_KEY);
}

export function obterToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function obterUsuario() {
  const usuario = localStorage.getItem(USUARIO_KEY);

  return usuario ? JSON.parse(usuario) : null;
}
