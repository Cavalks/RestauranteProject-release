package com.joaofnunes.model;

public enum Status {

	EXITO("Exito"),EM_ANDAMENTO("Andamento"), FALHA("Falha");

	private String descricao;

	Status(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
