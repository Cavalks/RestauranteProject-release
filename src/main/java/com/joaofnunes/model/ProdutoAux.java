package com.joaofnunes.model;

import java.io.Serializable;

public class ProdutoAux implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nome;
	private Integer quantidade;
	private Long id;
	private Double preco;

	public ProdutoAux(String nome, Integer quantidade) {
		this.nome = nome;
		this.quantidade = quantidade;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String incrementar() {

		this.quantidade++;
		return null;
	}

	public String decrementar() {

		if (getQuantidade() > 0) {
			this.quantidade--;
		}

		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public Double getValorTotal() {
		return this.quantidade * this.preco;
	}

}
