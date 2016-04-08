package com.joaofnunes.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.joaofnunes.dao.PedidoDAO;
import com.joaofnunes.dao.ProdutoDAO;
import com.joaofnunes.model.Produto;
import com.joaofnunes.model.grafics.GraficLine;
import com.joaofnunes.model.grafics.GraficPie;
import com.joaofnunes.model.queries.Pedidos;
import com.joaofnunes.model.queries.Produtos;

@Named
@ViewScoped
public class GraficosBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoDAO pedidoDao;
	@Inject
	private ProdutoDAO produtoDao;
	List<Pedidos> listafiltradaPedido = new ArrayList<>();
	@Inject
	private GraficLine line;
	private Integer resultadoListaPedidos;
	private Integer resultadoListaProdutos;
	List<Pedidos> pedidos = new ArrayList<>();
	List<Produtos> produtos;
	@Inject
	private GraficPie pie;
	private boolean produtosValidation = false;
	private boolean pedidosValidation = false;
	private Integer resultadoComboBox;
	private List<Produtos> produtosQuery;
	private List<Produtos> produtosUnicosFiltrados = new ArrayList<>();
	private List<Produto> produtosListaMenuBox = new ArrayList<>();
	private Produto produtoSelecionadoCombo;

	public void carregarCombo(ComponentSystemEvent e) {
		this.produtosListaMenuBox = produtoDao.getProdutos();

	}

	public void actionGraficoAjaxPedido() {

		montarGraficoPedido();
		this.resultadoComboBox = 0;
		this.resultadoListaProdutos = 0;

	}

	public void actionGraficoAjaxProduto() {

		this.produtos = produtoDao.listaConsultaGraficoPorPeriodo(resultadoListaProdutos);
		line.initProdutos(produtos, resultadoListaProdutos);
		this.resultadoComboBox = 0;
		this.resultadoListaPedidos = 0;
		produtos = line.atualizarLista();

	}

	public GraficPie getPie() {
		return pie;
	}

	public void montarGraficoPedido() {

		pedidos = pedidoDao.preencherLista(this.resultadoListaPedidos);
		List<Pedidos> listafiltradaa = pedidoDao.listaConsultaGraficos(this.resultadoListaPedidos);
		pedidos = pedidoDao.operarListas(pedidos, listafiltradaa, resultadoListaPedidos);

		listafiltradaPedido.clear();
		for (Pedidos pedidos : pedidos) {
			if (pedidos.getValorTotal().doubleValue() > 0) {

				listafiltradaPedido.add(pedidos);
			}
		}

		line.initPedidos(pedidos, resultadoListaPedidos);

	}

	public GraficLine getLine() {
		return line;
	}

	public boolean isProdutosValidation() {
		return produtosValidation;
	}

	public void setProdutosValidation(boolean produtosValidation) {
		this.produtosValidation = produtosValidation;
	}

	public boolean isPedidosValidation() {
		return pedidosValidation;
	}

	public void setPedidosValidation(boolean pedidosValidation) {
		this.pedidosValidation = pedidosValidation;
	}

	public String actionProdutos() {

		produtosValidation = true;
		pedidosValidation = false;
		carregarPie();

		return null;
	}

	public String actionPedidos() {

		produtosValidation = false;
		pedidosValidation = true;

		return null;
	}

	public void carregarPie() {
		produtosQuery = produtoDao.produtosTotalGrafic();
		pie.preencher(produtosQuery);
	}

	public Integer resultadoListaPedidos() {
		return resultadoListaPedidos;
	}

	public void setResultadoListaPedidos(Integer resultadoListaPedidos) {
		this.resultadoListaPedidos = resultadoListaPedidos;
	}

	public Integer getResultadoListaProdutos() {
		return resultadoListaProdutos;
	}

	public void setResultadoListaProdutos(Integer resultadoListaProdutos) {
		this.resultadoListaProdutos = resultadoListaProdutos;
	}

	public Integer getResultadoListaPedidos() {
		return resultadoListaPedidos;
	}

	public List<Produtos> getProdutosQuery() {
		return produtosQuery;
	}

	public List<Produtos> getProdutos() {
		return produtos;
	}

	public Double getLucroTotalProdutosPorPeriodo() {
		Double aux = new Double(0);
		for (Produtos p : produtos) {
			aux += p.getLucro();
		}
		return aux;
	}

	public Double getGastoTotalProdutosPorPeriodo() {
		Double aux = new Double(0);
		for (Produtos p : produtos) {
			aux += p.getCusto().doubleValue() * p.getQuantidade().doubleValue();
		}
		return aux;
	}

	public Double getLucroTotalProdutos() {
		Double aux = new Double(0);
		for (Produtos p : produtosQuery) {
			aux += p.getLucro();
		}
		return aux;
	}

	public Double getGastoTotalProdutos() {
		Double aux = new Double(0);

		for (Produtos p : produtosQuery) {
			aux += p.getCusto().doubleValue() * p.getQuantidade().doubleValue();
		}
		return aux;
	}

	public Double getLucroTotalPedidos() {
		Double aux = new Double(0);
		for (Pedidos pedidos : listafiltradaPedido) {
			aux += pedidos.getLucro().doubleValue();
		}
		return aux;
	}

	public Double getGastoTotalPedidos() {
		Double aux = new Double(0);

		for (Pedidos pedidos : listafiltradaPedido) {
			aux += pedidos.getCustoTotal().doubleValue();
		}
		return aux;
	}

	public List<Produto> getProdutosListaMenuBox() {
		return produtosListaMenuBox;
	}

	public Produto getProdutoSelecionadoCombo() {
		return produtoSelecionadoCombo;
	}

	public void setProdutoSelecionadoCombo(Produto produtoSelecionadoCombo) {
		this.produtoSelecionadoCombo = produtoSelecionadoCombo;
	}

	public String processar() {
		this.resultadoListaProdutos = 0;

		List<Produtos> listaGrande = produtoDao.preencherLista(resultadoComboBox);

		List<Produtos> listafiltrada = produtoDao.listaConsultaGraficos(resultadoComboBox,
				produtoSelecionadoCombo.getId());

		List<Produtos> listaFinal = produtoDao.unificarListasConsultaUnicoProduto(listaGrande, listafiltrada,
				resultadoComboBox);

		produtosUnicosFiltrados.clear();
		for (Produtos produtos : listaFinal) {

			if (produtos.getValorTotal() > 0) {
				Produtos produto = produtos;
				produto.setLucro((produto.getValorUnitario().doubleValue() - produto.getCusto().doubleValue())
						* produto.getQuantidade());
				if (resultadoComboBox == 4) {
					produto.setDataProdutoUnitario(produtos.getMes() + "/" + produtos.getAno());
				} else if (resultadoComboBox == 1 || resultadoComboBox == 2 || resultadoComboBox == 3) {
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					produto.setDataProdutoUnitario(format.format(produto.getDate()));
				}

				produtosUnicosFiltrados.add(produto);

			}

		}

		line.initProdutosUnitario(listaFinal, resultadoComboBox);

		return null;
	}

	public Integer getResultadoComboBox() {
		return resultadoComboBox;
	}

	public void setResultadoComboBox(Integer resultadoComboBox) {
		this.resultadoComboBox = resultadoComboBox;
	}

	public List<Produtos> getProdutosUnicosFiltrados() {
		return produtosUnicosFiltrados;
	}

	public Double getLucroProdutoUnico() {
		Double valor = new Double(0);
		for (Produtos p : produtosUnicosFiltrados) {
			valor += p.getLucro();
		}
		return valor;
	}

	public Double getCustoProdutoUnico() {
		Double valor = new Double(0);
		for (Produtos p : produtosUnicosFiltrados) {
			valor += (p.getCusto().doubleValue() * p.getQuantidade());
		}
		return valor;
	}

	public List<Pedidos> getPedidos() {
		return pedidos;
	}

	public List<Pedidos> getListafiltradaPedido() {

		return listafiltradaPedido;
	}

	public boolean isRenderizavelGraficos() {
		return pedidoDao.existenciaPedidos();
	}

}
