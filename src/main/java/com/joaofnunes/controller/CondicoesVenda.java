package com.joaofnunes.controller;

import java.io.Serializable;

import com.joaofnunes.model.FormaPagamento;
import com.joaofnunes.model.Pedido;

public class CondicoesVenda implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean etapa1;
	private boolean etapa2;
	private boolean etapa3;
	private boolean etapa4;
	private boolean dinheiro;
	private boolean cartao;
	private Integer returnPagamento;
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
			this.etapa2 = true;
			this.index = 1;
		} else if (etapaSelecionada == 3) {
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

	public boolean isEtapa1() {
		return etapa1;
	}

	public void setEtapa1(boolean etapa1) {
		this.etapa1 = etapa1;
	}

	public boolean isEtapa2() {
		return etapa2;
	}

	public void setEtapa2(boolean etapa2) {
		this.etapa2 = etapa2;
	}

	public boolean isEtapa3() {
		return etapa3;
	}

	public void setEtapa3(boolean etapa3) {
		this.etapa3 = etapa3;
	}

	public boolean isEtapa4() {
		return etapa4;
	}

	public void setEtapa4(boolean etapa4) {
		this.etapa4 = etapa4;
	}

	public boolean isDinheiro() {
		return dinheiro;
	}

	public void setDinheiro(boolean dinheiro) {
		this.dinheiro = dinheiro;
	}

	public boolean isCartao() {
		return cartao;
	}

	public void setCartao(boolean cartao) {
		this.cartao = cartao;
	}

	public Integer getReturnPagamento() {
		return returnPagamento;
	}

	public void setReturnPagamento(Integer returnPagamento) {
		this.returnPagamento = returnPagamento;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	

}
