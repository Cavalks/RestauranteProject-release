package com.joaofnunes.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.joaofnunes.dao.PedidoDAO;
import com.joaofnunes.dao.ProdutoDAO;
import com.joaofnunes.model.Pedido;
import com.joaofnunes.model.ProdutoAux;
import com.joaofnunes.util.jsf.FacesUtil;

@ViewScoped
@Named
public class VendasBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private ProdutoDAO produtoDao;
	@Inject
	private PedidoDAO pedidoDao;
	private List<ProdutoAux> produtos = new ArrayList<>();
	private List<ProdutoAux> produtosSelecionados = new ArrayList<>();
	private Double troco;
	private Pedido pedido = new Pedido();
	private Pedido pedidoEditavel;
	private Double dinheiroRecebido;
	private boolean edicao = false;
	private CondicoesVenda condicoesVenda = new CondicoesVenda();

	public void preRender(ComponentSystemEvent e) {

		if (FacesUtil.isNotPostback()) {
			if (pedidoEditavel != null) {
				carregarPedidoEditavel();
			}

			etapa1();
			if (edicao == false) {
				this.produtos = this.produtoDao.carregarProdutos();

			}

		}

	}

	public void zerarListas() {
		this.produtos.clear();
		this.produtosSelecionados.clear();
		this.produtos = this.produtoDao.carregarProdutos();

	}

	public String etapa1() {

		this.condicoesVenda.selecionarEtapa(1, pedido);

		return null;
	}

	public String etapa2() {

		preencherLista();
		if (this.produtosSelecionados.isEmpty()) {
			FacesUtil.addErrorMessage("Nenhum produto foi adicionado!");
		} else {
			this.condicoesVenda.selecionarEtapa(2, pedido);
		}
		return null;
	}

	public String etapa3() {

		this.condicoesVenda.selecionarEtapa(3, pedido);
		return null;
	}

	public String etapa4() {
		if (troco()) {
			this.condicoesVenda.selecionarEtapa(4, pedido);
			this.pedido = this.pedidoDao.popularPedido(produtosSelecionados, pedido, pedidoEditavel);
			this.pedido = this.pedidoDao.guardar(pedido);
		}

		return null;
	}

	public String novoPedido() {
		this.etapa1();
		this.condicoesVenda = new CondicoesVenda();
		this.pedido = new Pedido();
		zerarListas();
		this.edicao = false;
		this.dinheiroRecebido = new Double(0);
		this.pedidoEditavel = null;

		return null;
	}

	public void preencherLista() {
		produtosSelecionados.clear();
		for (ProdutoAux produtoAux : produtos) {
			if (produtoAux.getQuantidade() > 0) {

				produtosSelecionados.add(produtoAux);
			}
		}
	}

	public Double getPedidoTotal() {
		Double valor = new Double(0);
		for (ProdutoAux produtoAux : produtosSelecionados) {
			valor += produtoAux.getValorTotal();
		}
		return valor;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Double getDinheiroRecebido() {
		return dinheiroRecebido;
	}

	public void setDinheiroRecebido(Double dinheiroRecebido) {
		this.dinheiroRecebido = dinheiroRecebido;
	}

	public CondicoesVenda getCondicoesVenda() {
		return condicoesVenda;
	}

	public void setCondicoesVenda(CondicoesVenda condicoesVenda) {
		this.condicoesVenda = condicoesVenda;
	}

	public List<ProdutoAux> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ProdutoAux> produtos) {
		this.produtos = produtos;
	}

	public List<ProdutoAux> getProdutosSelecionados() {
		return produtosSelecionados;
	}

	public void setProdutosSelecionados(List<ProdutoAux> produtosSelecionados) {
		this.produtosSelecionados = produtosSelecionados;
	}

	public boolean troco() {

		if (dinheiroRecebido >= getPedidoTotal()) {
			troco = dinheiroRecebido - getPedidoTotal();
			return true;
		} else {
			FacesUtil.addErrorMessage("Dinheiro insuficiente");
			return false;
		}
	}

	public Double getTroco() {
		return troco;
	}

	public Pedido getPedidoEditavel() {
		return pedidoEditavel;
	}

	public void setPedidoEditavel(Pedido pedidoEditavel) {
		this.pedidoEditavel = pedidoEditavel;
	}

	public void carregarPedidoEditavel() {
		this.pedidoEditavel = pedidoDao.pedidoComItens(this.pedidoEditavel.getId());
		List<ProdutoAux> test = pedidoDao.pedidoToPedidoAux(produtos, this.pedidoEditavel);
		produtos = produtoDao.carregarProdutos();
		this.produtos = produtoDao.carregarDiferençaProdutos(test, produtos);
		this.edicao = true;

	}

	public boolean isEdicao() {
		return edicao;
	}

}
