package com.joaofnunes.controller;

import java.io.Serializable;

import com.joaofnunes.model.FormaPagamento;
import com.joaofnunes.model.Pedido;

import lombok.Getter;
import lombok.Setter;

public class CondicoesVenda implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private boolean etapa1;
	@Getter
	@Setter
	private boolean etapa2;
	@Getter
	@Setter
	private boolean etapa3;
	@Getter
	@Setter
	private boolean etapa4;
	@Getter
	@Setter
	private boolean dinheiro;
	@Getter
	@Setter
	private boolean cartao;
	@Getter
	@Setter
	private Integer returnPagamento;
	@Getter
	@Setter
	private int index;

	public CondicoesVenda() {
		this.etapa1 = true;
	}

	public void selecionarEtapa(Integer etapaSelecionada, Pedido pedido) {
		this.etapa1 = false;
		this.etapa2 = false;
		this.etapa3 = false;
		this.etapa4 = false;

		if (etapaSelecionada == 1) {
			this.etapa1 = true;
			this.index = 0;
		} else if (etapaSelecionada == 2) {
			this.returnPagamento = 0;
			this.etapa2 = true;
			this.index = 1;
		} else if (etapaSelecionada == 3) {
			this.cartao = false;
			this.dinheiro = false;
			this.etapa3 = true;
			this.index = 2;
		} else if (etapaSelecionada == 4) {
			this.etapa4 = true;
			this.index = 3;
			if (this.returnPagamento == 2) {
				pedido.setFormaPagamento(FormaPagamento.DINHEIRO);
			} else if (this.returnPagamento == 1) {
				pedido.setFormaPagamento(FormaPagamento.CARTAO_CREDITO);
			}
		}

	}

	public void actionPagamentoAjax() {
		System.out.println("chamado");
		if (this.returnPagamento == 2) {
			this.dinheiro = true;
			this.cartao = false;

		} else if (returnPagamento == 1) {
			this.dinheiro = false;
			this.cartao = true;

		} else {
			this.dinheiro = false;
			this.cartao = false;
		}
	}

}
