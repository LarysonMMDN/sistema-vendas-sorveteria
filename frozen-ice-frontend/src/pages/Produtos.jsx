
import { useEffect, useMemo, useState } from "react";
import { AnimatePresence, motion } from "framer-motion";
import {
  Search, Plus, Pencil, Trash2, IceCream,
  Package, X, RefreshCw
} from "lucide-react";

import {
  listarProdutos,
  listarCategorias,
  cadastrarProduto,
  atualizarProduto,
  excluirProduto as excluirProdutoAPI
} from "../services/produtosService";

const vazio = {
  nome: "",
  sabor: "",
  preco: "",
  estoque: "0",
  categoriaId: ""
};

const moeda = (valor) =>
  Number(valor || 0).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL"
  });

const erroTexto = (e) =>
  e?.response?.data?.message ||
  e?.response?.data?.erro ||
  e?.message ||
  "Não foi possível concluir a operação.";

export default function Produtos() {
  const [produtos, setProdutos] = useState([]);
  const [categorias, setCategorias] = useState([]);

  const [busca, setBusca] = useState("");
  const [categoriaFiltro, setCategoriaFiltro] = useState("Todas");

  const [modalAberto, setModalAberto] = useState(false);
  const [editando, setEditando] = useState(null);
  const [form, setForm] = useState(vazio);

  const [carregando, setCarregando] = useState(true);
  const [salvando, setSalvando] = useState(false);

  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState("");

  async function carregarProdutos() {
    setCarregando(true);
    setErro("");

    try {
      const dados = await listarProdutos();
      setProdutos(dados);
    } catch (e) {
      setErro(erroTexto(e));
    } finally {
      setCarregando(false);
    }
  }

async function carregarCategorias() {
  try {
    const dados = await listarCategorias();

    console.log("CATEGORIAS RECEBIDAS:", dados);

    setCategorias(dados);
  } catch (e) {
    setErro(erroTexto(e));
  }
}

  async function atualizarDados() {
    await Promise.all([
      carregarProdutos(),
      carregarCategorias()
    ]);
  }

  useEffect(() => {
    atualizarDados();
  }, []);

  const categoriasAtivas = useMemo(
    () => categorias.filter(c => c.status === true),
    [categorias]
  );

  const categoriasFiltro = useMemo(
    () => [
      { id: "Todas", nome: "Todas as categorias" },
      ...categorias
    ],
    [categorias]
  );

  const filtrados = useMemo(() => {
    const termo = busca.toLocaleLowerCase("pt-BR").trim();

    return produtos.filter(p => {
      const categoria = p.categoriaNome ?? "";

      const texto = [
        p.nome,
        p.sabor,
        categoria
      ].some(v =>
        String(v ?? "")
          .toLocaleLowerCase("pt-BR")
          .includes(termo)
      );

      const correspondeCategoria =
        categoriaFiltro === "Todas" ||
        p.categoriaId === categoriaFiltro;

      return texto && correspondeCategoria;
    });
  }, [produtos, busca, categoriaFiltro]);

  const totalProdutos = produtos.length;

  function abrirNovo() {
    setEditando(null);
    setForm(vazio);
    setErro("");
    setSucesso("");
    setModalAberto(true);
  }

  function abrirEdicao(p) {
    setEditando(p);

    setForm({
      nome: p.nome ?? "",
      sabor: p.sabor ?? "",
      preco: String(p.preco ?? ""),
      estoque: String(p.estoque ?? 0),
      categoriaId: String(p.categoriaId ?? "")
    });

    setErro("");
    setSucesso("");
    setModalAberto(true);
  }

  function mudarCampo(e) {
    const { name, value } = e.target;

    setForm(atual => ({
      ...atual,
      [name]: value
    }));
  }

  async function salvar(e) {
    e.preventDefault();

    setErro("");
    setSucesso("");

    if (!form.nome.trim()) {
      setErro("Informe o nome do produto.");
      return;
    }

    if (
      !Number.isFinite(Number(form.preco)) ||
      Number(form.preco) <= 0
    ) {
      setErro("Informe um preço válido maior que zero.");
      return;
    }

    if (
      form.estoque === "" ||
      !Number.isInteger(Number(form.estoque)) ||
      Number(form.estoque) < 0
    ) {
      setErro("Informe uma quantidade de estoque válida.");
      return;
    }

    if (!form.categoriaId) {
      setErro("Selecione uma categoria.");
      return;
    }

    const categoriaSelecionada = categorias.find(
      c => String(c.id) === String(form.categoriaId)
    );

    if (!categoriaSelecionada || categoriaSelecionada.status !== true) {
      setErro("Selecione uma categoria ativa.");
      return;
    }

    const dadosProduto = {
      nome: form.nome.trim(),
      sabor: form.sabor,
      preco: Number(form.preco),
      estoque: Number(form.estoque),
      categoriaId: form.categoriaId
    };

    setSalvando(true);

    try {
      if (editando) {
        await atualizarProduto(editando.id, dadosProduto);
      } else {
        await cadastrarProduto(dadosProduto);
      }

      const mensagem = editando
        ? "Produto atualizado com sucesso."
        : "Produto cadastrado com sucesso.";

      setModalAberto(false);
      setForm(vazio);
      setEditando(null);

      setSucesso(mensagem);

      await carregarProdutos();

    } catch (e) {
      setErro(erroTexto(e));
    } finally {
      setSalvando(false);
    }
  }

  async function excluir(id) {
    const p = produtos.find(
      item => String(item.id) === String(id)
    );

    if (
      !window.confirm(
        `Deseja realmente excluir "${p?.nome ?? "este produto"}"?`
      )
    ) {
      return;
    }

    setErro("");
    setSucesso("");

    try {
      await excluirProdutoAPI(id);

      setProdutos(atual =>
        atual.filter(item => String(item.id) !== String(id))
      );

      setSucesso("Produto excluído com sucesso.");

    } catch (e) {
      setErro(erroTexto(e));
    }
  }

  return (
    <main className="products-page">

      <motion.div
        className="products-title"
        initial={{ opacity: 0, y: 14 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <div>
          <span className="clients-eyebrow">
            CATÁLOGO FROZEN-ICE
          </span>

          <h2>Produtos 🍨</h2>

          <p>
            Gerencie sabores e preços da sorveteria.
          </p>
        </div>

        <button
          className="add-product-btn"
          onClick={abrirNovo}
        >
          <Plus size={18} />
          Novo produto
        </button>
      </motion.div>

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

      <section className="product-summary">

        <div className="summary-card">
          <div className="summary-icon purple">
            <IceCream size={22} />
          </div>

          <div>
            <small>Produtos cadastrados</small>
            <h3>{totalProdutos}</h3>
          </div>
        </div>

        <div className="summary-card">
          <div className="summary-icon blue">
            <Package size={22} />
          </div>

          <div>
            <small>Categorias cadastradas</small>
            <h3>{categorias.length}</h3>
          </div>
        </div>

        <div className="summary-card">
          <div className="summary-icon orange">
            <IceCream size={22} />
          </div>

          <div>
            <small>Categorias ativas</small>
            <h3>{categoriasAtivas.length}</h3>
          </div>
        </div>

      </section>

      <section className="catalog-toolbar">

        <div className="catalog-search">
          <Search size={18} />

          <input
            value={busca}
            onChange={e => setBusca(e.target.value)}
            placeholder="Buscar produto ou sabor..."
          />
        </div>

        <select
          value={categoriaFiltro}
          onChange={e => setCategoriaFiltro(e.target.value)}
        >
          {categoriasFiltro.map(c => (
            <option key={c.id} value={c.id}>
              {c.nome}
            </option>
          ))}
        </select>

        <button
          type="button"
          onClick={atualizarDados}
          title="Atualizar catálogo"
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

      {carregando ? (
        <p style={{ padding: 25, textAlign: "center" }}>
          Carregando produtos...
        </p>

      ) : filtrados.length === 0 ? (

        <div
          style={{
            padding: 35,
            textAlign: "center",
            color: "#9586a6"
          }}
        >
          {erro
            ? "Não foi possível carregar o catálogo."
            : "Nenhum produto encontrado."}
        </div>

      ) : (

        <section className="products-grid">

          <AnimatePresence>
            {filtrados.map(p => {

              const categoria =
                p.categoriaNome ?? "Sem categoria";

              return (
                <motion.article
                  className="product-card"
                  key={p.id}
                  layout
                  initial={{ opacity: 0, y: 12 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0 }}
                >

                  <div className="product-image strawberry">
                    <span>🍦</span>

                    <span className="product-label">
                      {categoria}
                    </span>
                  </div>

                  <div style={{ padding: 16 }}>

                    <h3
                      style={{
                        margin: "0 0 5px",
                        color: "#49345f"
                      }}
                    >
                      {p.nome}
                    </h3>

                    <p
                      style={{
                        margin: "0 0 12px",
                        color: "#9586a6",
                        fontSize: 12
                      }}
                    >
                      {p.sabor || "Sabor não informado"}
                    </p>

                    <div
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                        gap: 8
                      }}
                    >
                      <strong
                        style={{
                          color: "#8050c0",
                          fontSize: 17
                        }}
                      >
                        {moeda(p.preco)}
                      </strong>
                    </div>

                    <div
                      style={{
                        marginTop: "8px",
                        display: "flex",
                        alignItems: "center",
                        gap: "6px"
                      }}
                    >
                      <Package size={15} color="#9586a6" />

                      <span
                        style={{
                          fontSize: "13px",
                          fontWeight: "600",
                          color: Number(p.estoque ?? 0) === 0
                            ? "#c0395b"
                            : "#65816d"
                        }}
                      >
                        Estoque: {Number.isFinite(Number(p.estoque))
                          ? Number(p.estoque)
                          : 0} unidades
                      </span>
                    </div>

                    <div
                      style={{
                        display: "flex",
                        justifyContent: "flex-end",
                        gap: 8,
                        marginTop: 14
                      }}
                    >
                      <button
                        className="product-action-edit"
                        title="Editar"
                        onClick={() => abrirEdicao(p)}
                      >
                        <Pencil size={16} />
                      </button>

                      <button
                        className="product-action-delete"
                        title="Excluir"
                        onClick={() => excluir(p.id)}
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>

                  </div>

                </motion.article>
              );
            })}
          </AnimatePresence>

        </section>
      )}

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
                setModalAberto(false);
              }
            }}
          >

            <motion.div
              className="product-modal"
              initial={{ opacity: 0, y: 18, scale: 0.97 }}
              animate={{ opacity: 1, y: 0, scale: 1 }}
              exit={{ opacity: 0, y: 12 }}
            >

              <div className="product-modal-heading">

                <div>
                  <span>FROZEN-ICE ✦</span>

                  <h2>
                    {editando
                      ? "Editar produto"
                      : "Novo produto"}
                  </h2>

                  <p>
                    Preencha os dados do item do catálogo.
                  </p>
                </div>

                <button
                  type="button"
                  onClick={() => setModalAberto(false)}
                  disabled={salvando}
                >
                  <X size={19} />
                </button>

              </div>

              <form onSubmit={salvar}>

                <div className="product-form-grid">

                  <label>
                    Nome do produto

                    <input
                      name="nome"
                      value={form.nome}
                      onChange={mudarCampo}
                      required
                      maxLength={120}
                      placeholder="Ex.: Sorvete de morango"
                    />
                  </label>

                  <label>
                    Sabor

                    <input
                      name="sabor"
                      value={form.sabor}
                      onChange={mudarCampo}
                      placeholder="Ex.: Morango cremoso"
                    />
                  </label>

                  <label>
                    Preço (R$)

                    <input
                      name="preco"
                      type="number"
                      min="0.01"
                      step="0.01"
                      value={form.preco}
                      onChange={mudarCampo}
                      required
                    />
                  </label>

                  <label>
                    Quantidade em estoque

                    <input
                      name="estoque"
                      type="number"
                      min="0"
                      step="1"
                      value={form.estoque}
                      onChange={mudarCampo}
                      required
                      placeholder="Ex.: 50"
                    />
                  </label>

                  <label>
                    Categoria

                    <select
                      name="categoriaId"
                      value={form.categoriaId}
                      onChange={mudarCampo}
                      required
                    >
                      <option value="">
                        Selecione uma categoria
                      </option>

                      {categorias.map(categoria => (
                        <option
                          key={categoria.id}
                          value={categoria.id}
                          disabled={categoria.status !== true}
                        >
                          {categoria.nome}
                          {categoria.status !== true
                            ? " (Inativa)"
                            : ""}
                        </option>
                      ))}
                    </select>
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

                <div className="product-modal-actions">

                  <button
                    type="button"
                    onClick={() => setModalAberto(false)}
                    disabled={salvando}
                  >
                    Cancelar
                  </button>

                  <button
                    className="save-product-btn"
                    type="submit"
                    disabled={salvando || categoriasAtivas.length === 0}
                  >
                    {salvando
                      ? "Salvando..."
                      : editando
                        ? "Salvar alterações"
                        : "Cadastrar produto"}
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
