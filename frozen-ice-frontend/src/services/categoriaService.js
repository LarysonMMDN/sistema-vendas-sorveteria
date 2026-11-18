
import api from "./api";

const BASE_URL = "/api/categorias";

function normalizarLista(data) {
  if (Array.isArray(data)) return data;
  if (Array.isArray(data?.content)) return data.content;

  throw new Error("A API de categorias retornou um formato inesperado.");
}

export async function listarCategorias() {
  const { data } = await api.get(BASE_URL);
  return normalizarLista(data);
}

export async function buscarCategoria(id) {
  const { data } = await api.get(`${BASE_URL}/${id}`);
  return data;
}

export async function cadastrarCategoria(categoria) {
  const { data } = await api.post(
    BASE_URL,
    prepararPayload(categoria)
  );

  return data;
}

export async function atualizarCategoria(id, categoria) {
  const { data } = await api.put(
    `${BASE_URL}/${id}`,
    prepararPayload(categoria)
  );

  return data;
}

export async function excluirCategoria(id) {
  await api.delete(`${BASE_URL}/${id}`);
}

function prepararPayload({ nome, descricao, tipo, status }) {
  return {
    nome: nome.trim(),
    descricao: descricao.trim(),
    tipo: tipo.trim(),
    status: Boolean(status)
  };
}
