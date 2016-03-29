package com.joaofnunes.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.joaofnunes.dao.ProdutoDAO;
import com.joaofnunes.filter.ProdutoFilter;
import com.joaofnunes.model.Produto;

@ViewScoped
@Named
public class ProdutoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private ProdutoDAO produtoDao;
	private ProdutoFilter produtoFilter = new ProdutoFilter();
	private List<Produto> produtosFiltrados = new ArrayList<>();
	private Produto produtoSelecionado;

	private Produto novoProduto = new Produto();

	public ProdutoDAO getProdutoDao() {
		return produtoDao;
	}

	public void setProdutoDao(ProdutoDAO produtoDao) {
		this.produtoDao = produtoDao;
	}

	public ProdutoFilter getProdutoFilter() {
		return produtoFilter;
	}

	public void setProdutoFilter(ProdutoFilter produtoFilter) {
		this.produtoFilter = produtoFilter;
	}

	public String pesquisar() {

		produtosFiltrados = produtoDao.filtrados(produtoFilter);

		return null;
	}

	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public String confirmarEdicao() {
		this.produtoSelecionado = this.produtoDao.guardar(produtoSelecionado);

		return null;
	}

	public String novoPedido() {

		return null;
	}

	public String adicionarProduto() {

		this.novoProduto = this.produtoDao.guardar(novoProduto);
		this.novoProduto = new Produto();

		pesquisar();

		return null;
	}

	public Produto getNovoProduto() {
		return novoProduto;
	}

	public void setNovoProduto(Produto novoProduto) {
		this.novoProduto = novoProduto;
	}

}
