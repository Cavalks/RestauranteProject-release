package com.joaofnunes.model;

public enum FormaPagamento {

	DINHEIRO("Dinheiro"), CARTAO_CREDITO("Cartão de crédito");

	private String descricao;

	FormaPagamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}