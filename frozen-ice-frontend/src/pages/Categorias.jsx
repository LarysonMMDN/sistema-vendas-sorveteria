
import { useEffect, useMemo, useState } from "react";
import { AnimatePresence, motion } from "framer-motion";

import {
  Search,
  Plus,
  Pencil,
  Trash2,
  Tags,
  CheckCircle,
  XCircle,
  RefreshCw,
  X
} from "lucide-react";

import {
  listarCategorias,
  cadastrarCategoria,
  atualizarCategoria,
  excluirCategoria as excluirCategoriaAPI
} from "../services/categoriaService";

const vazio = {
  nome: "",
  descricao: "",
  tipo: "",
  status: true
};

const erroTexto = (e) =>
  e?.response?.data?.message ||
  e?.response?.data?.erro ||
  e?.response?.data?.detail ||
  e?.message ||
  "Não foi possível concluir a operação.";

export default function Categorias() {
  const [categorias, setCategorias] = useState([]);

  const [busca, setBusca] = useState("");
  const [filtroStatus, setFiltroStatus] = useState("Todas");

  const [modalAberto, setModalAberto] = useState(false);
  const [editando, setEditando] = useState(null);
  const [form, setForm] = useState(vazio);

  const [carregando, setCarregando] = useState(true);
  const [salvando, setSalvando] = useState(false);

  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState("");

  // CARREGAR CATEGORIAS

  async function carregarCategorias() {
    setCarregando(true);
    setErro("");

    try {
      const dados = await listarCategorias();
      setCategorias(dados);
    } catch (e) {
      setErro(erroTexto(e));
    } finally {
      setCarregando(false);
    }
  }

  useEffect(() => {
    carregarCategorias();
  }, []);

  // CONTADORES

  const categoriasAtivas = useMemo(
    () => categorias.filter(c => c.status === true),
    [categorias]
  );

  const categoriasInativas = useMemo(
    () => categorias.filter(c => c.status === false),
    [categorias]
  );

  // PESQUISA E FILTROS

  const filtradas = useMemo(() => {
    const termo = busca
      .toLocaleLowerCase("pt-BR")
      .trim();

    return categorias.filter(c => {
      const correspondeTexto = [
        c.nome,
        c.descricao,
        c.tipo
      ].some(valor =>
        String(valor ?? "")
          .toLocaleLowerCase("pt-BR")
          .includes(termo)
      );

      const correspondeStatus =
        filtroStatus === "Todas" ||
        (filtroStatus === "Ativas" && c.status === true) ||
        (filtroStatus === "Inativas" && c.status === false);

      return correspondeTexto && correspondeStatus;
    });
  }, [categorias, busca, filtroStatus]);

  // ABRIR CADASTRO

  function abrirNovo() {
    setEditando(null);
    setForm(vazio);
    setErro("");
    setSucesso("");
    setModalAberto(true);
  }

  // ABRIR EDIÇÃO

  function abrirEdicao(categoria) {
    setEditando(categoria);

    setForm({
      nome: categoria.nome ?? "",
      descricao: categoria.descricao ?? "",
      tipo: categoria.tipo ?? "",
      status: categoria.status === true
    });

    setErro("");
    setSucesso("");
    setModalAberto(true);
  }

  // ALTERAR CAMPOS

  function mudarCampo(e) {
    const { name, value, type, checked } = e.target;

    setForm(atual => ({
      ...atual,
      [name]: type === "checkbox" ? checked : value
    }));
  }

  // SALVAR CATEGORIA

  async function salvar(e) {
    e.preventDefault();

    setErro("");
    setSucesso("");

    if (!form.nome.trim()) {
      setErro("Informe o nome da categoria.");
      return;
    }

    if (!form.tipo.trim()) {
      setErro("Informe o tipo da categoria.");
      return;
    }

    setSalvando(true);

    try {
      if (editando) {
        await atualizarCategoria(editando.id, form);
      } else {
        await cadastrarCategoria(form);
      }

      const mensagem = editando
        ? "Categoria atualizada com sucesso."
        : "Categoria cadastrada com sucesso.";

      setModalAberto(false);
      setForm(vazio);
      setEditando(null);

      setSucesso(mensagem);

      await carregarCategorias();

    } catch (e) {
      setErro(erroTexto(e));
    } finally {
      setSalvando(false);
    }
  }

  // EXCLUIR CATEGORIA

  async function excluir(id) {
    const categoria = categorias.find(
      c => String(c.id) === String(id)
    );

    if (
      !window.confirm(
        `Deseja realmente excluir a categoria "${categoria?.nome ?? "esta categoria"}"?`
      )
    ) {
      return;
    }

    setErro("");
    setSucesso("");

    try {
      await excluirCategoriaAPI(id);

      setCategorias(atual =>
        atual.filter(c => String(c.id) !== String(id))
      );

      setSucesso("Categoria excluída com sucesso.");

    } catch (e) {
      setErro(erroTexto(e));
    }
  }

  // FECHAR MODAL

  function fecharModal() {
    if (salvando) return;

    setModalAberto(false);
    setErro("");
  }

  return (
    <main className="products-page">

      {/* CABEÇALHO */}

      <motion.div
        className="products-title"
        initial={{ opacity: 0, y: 14 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <div>
          <span className="clients-eyebrow">
            CATÁLOGO FROZEN-ICE
          </span>

          <h2>Categorias 🏷️</h2>

          <p>
            Gerencie os tipos e as categorias dos produtos.
          </p>
        </div>

        <button
          className="add-product-btn"
          onClick={abrirNovo}
        >
          <Plus size={18} />
          Nova categoria
        </button>
      </motion.div>

      {/* MENSAGENS */}

      {(erro || sucesso) && (
        <div
          role="alert"
          style={{
            padding: 12,
            borderRadius: 10,
            marginBottom: 16,
            color: erro ? "#a33b59" : "#397a5b",
            background: erro ? "#fff0f3" : "#effaf3"
          }}
        >
          {erro || sucesso}
        </div>
      )}

      {/* RESUMO */}

      <section className="product-summary">

        <div className="summary-card">
          <div className="summary-icon purple">
            <Tags size={22} />
          </div>

          <div>
            <small>Total de categorias</small>
            <h3>{categorias.length}</h3>
          </div>
        </div>

        <div className="summary-card">
          <div className="summary-icon blue">
            <CheckCircle size={22} />
          </div>

          <div>
            <small>Categorias ativas</small>
            <h3>{categoriasAtivas.length}</h3>
          </div>
        </div>

        <div className="summary-card">
          <div className="summary-icon orange">
            <XCircle size={22} />
          </div>

          <div>
            <small>Categorias inativas</small>
            <h3>{categoriasInativas.length}</h3>
          </div>
        </div>

      </section>

      {/* PESQUISA E FILTROS */}

      <section className="catalog-toolbar">

        <div className="catalog-search">
          <Search size={18} />

          <input
            value={busca}
            onChange={e => setBusca(e.target.value)}
            placeholder="Buscar categoria..."
          />
        </div>

        <select
          value={filtroStatus}
          onChange={e => setFiltroStatus(e.target.value)}
        >
          <option value="Todas">Todos os status</option>
          <option value="Ativas">Ativas</option>
          <option value="Inativas">Inativas</option>
        </select>

        <button
          type="button"
          onClick={carregarCategorias}
          title="Atualizar categorias"
          style={{
            border: 0,
            background: "#f4effb",
            color: "#8050c0",
            borderRadius: 10,
            padding: "0 14px",
            cursor: "pointer"
          }}
        >
          <RefreshCw size={17} />
        </button>

      </section>

      {/* LISTAGEM */}

      {carregando ? (

        <p style={{ padding: 25, textAlign: "center" }}>
          Carregando categorias...
        </p>

      ) : filtradas.length === 0 ? (

        <div
          style={{
            padding: 35,
            textAlign: "center",
            color: "#9586a6"
          }}
        >
          {erro
            ? "Não foi possível carregar as categorias."
            : "Nenhuma categoria encontrada."}
        </div>

      ) : (

        <section className="products-grid">

          <AnimatePresence>
            {filtradas.map(categoria => (

              <motion.article
                className="product-card"
                key={categoria.id}
                layout
                initial={{ opacity: 0, y: 12 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0 }}
              >

                {/* PARTE SUPERIOR */}

                <div
                  className="product-image strawberry"
                  style={{
                    minHeight: 125,
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    position: "relative"
                  }}
                >
                  <span style={{ fontSize: 42 }}>
                    🏷️
                  </span>

                  <span
                    className="product-label"
                    style={{
                      background: categoria.status
                        ? "#e5f8ec"
                        : "#fff0f0",
                      color: categoria.status
                        ? "#39805b"
                        : "#b34d68"
                    }}
                  >
                    {categoria.status ? "Ativa" : "Inativa"}
                  </span>
                </div>

                {/* INFORMAÇÕES */}

                <div style={{ padding: 16 }}>

                  <h3
                    style={{
                      margin: "0 0 8px",
                      color: "#49345f"
                    }}
                  >
                    {categoria.nome}
                  </h3>

                  <p
                    style={{
                      margin: "0 0 10px",
                      color: "#9586a6",
                      fontSize: 13
                    }}
                  >
                    {categoria.descricao ||
                      "Sem descrição cadastrada."}
                  </p>

                  <div
                    style={{
                      display: "inline-block",
                      padding: "5px 10px",
                      borderRadius: 8,
                      background: "#f4effb",
                      color: "#8050c0",
                      fontSize: 12,
                      fontWeight: 600
                    }}
                  >
                    {categoria.tipo || "Sem tipo"}
                  </div>

                  {/* AÇÕES */}

                  <div
                    style={{
                      display: "flex",
                      justifyContent: "flex-end",
                      gap: 8,
                      marginTop: 16
                    }}
                  >

                    <button
                      className="product-action-edit"
                      title="Editar categoria"
                      onClick={() => abrirEdicao(categoria)}
                    >
                      <Pencil size={16} />
                    </button>

                    <button
                      className="product-action-delete"
                      title="Excluir categoria"
                      onClick={() => excluir(categoria.id)}
                    >
                      <Trash2 size={16} />
                    </button>

                  </div>

                </div>

              </motion.article>

            ))}
          </AnimatePresence>

        </section>
      )}

      {/* MODAL DE CADASTRO E EDIÇÃO */}

      <AnimatePresence>
        {modalAberto && (

          <motion.div
            className="product-modal-overlay"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            onMouseDown={e => {
              if (
                e.target === e.currentTarget &&
                !salvando
              ) {
                fecharModal();
              }
            }}
          >

            <motion.div
              className="product-modal"
              initial={{ opacity: 0, y: 18, scale: 0.97 }}
              animate={{ opacity: 1, y: 0, scale: 1 }}
              exit={{ opacity: 0, y: 12 }}
            >

              {/* CABEÇALHO DO MODAL */}

              <div className="product-modal-heading">

                <div>
                  <span>FROZEN-ICE ✦</span>

                  <h2>
                    {editando
                      ? "Editar categoria"
                      : "Nova categoria"}
                  </h2>

                  <p>
                    Preencha os dados da categoria.
                  </p>
                </div>

                <button
                  type="button"
                  onClick={fecharModal}
                  disabled={salvando}
                >
                  <X size={19} />
                </button>

              </div>

              {/* FORMULÁRIO */}

              <form onSubmit={salvar}>

                <div className="product-form-grid">

                  <label>
                    Nome da categoria

                    <input
                      name="nome"
                      value={form.nome}
                      onChange={mudarCampo}
                      required
                      maxLength={120}
                      placeholder="Ex.: Sorvetes"
                    />
                  </label>

                  <label>
                    Tipo

                    <input
                      name="tipo"
                      value={form.tipo}
                      onChange={mudarCampo}
                      required
                      maxLength={100}
                      placeholder="Ex.: Alimentício"
                    />
                  </label>

                  <label style={{ gridColumn: "1 / -1" }}>
                    Descrição

                    <textarea
                      name="descricao"
                      value={form.descricao}
                      onChange={mudarCampo}
                      rows={3}
                      maxLength={500}
                      placeholder="Descreva a categoria..."
                      style={{
                        width: "100%",
                        resize: "vertical"
                      }}
                    />
                  </label>

                  {/* STATUS */}

                  <label
                    style={{
                      gridColumn: "1 / -1",
                      display: "flex",
                      alignItems: "center",
                      gap: 10,
                      cursor: "pointer"
                    }}
                  >
                    <input
                      type="checkbox"
                      name="status"
                      checked={form.status}
                      onChange={mudarCampo}
                      style={{
                        width: 17,
                        height: 17,
                        accentColor: "#8050c0"
                      }}
                    />

                    Categoria ativa
                  </label>

                </div>

                {erro && (
                  <p
                    role="alert"
                    style={{
                      color: "#b34d68",
                      fontSize: 12
                    }}
                  >
                    {erro}
                  </p>
                )}

                {/* BOTÕES */}

                <div className="product-modal-actions">

                  <button
                    type="button"
                    onClick={fecharModal}
                    disabled={salvando}
                  >
                    Cancelar
                  </button>

                  <button
                    className="save-product-btn"
                    type="submit"
                    disabled={salvando}
                  >
                    {salvando
                      ? "Salvando..."
                      : editando
                        ? "Salvar alterações"
                        : "Cadastrar categoria"}
                  </button>

                </div>

              </form>

            </motion.div>

          </motion.div>
        )}
      </AnimatePresence>

    </main>
  );
}
