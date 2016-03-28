package com.joaofnunes.filter;

import java.math.BigDecimal;
import java.util.Date;

public class PedidoFilter {

	private Date dateInicial;
	private Date dateFinal;
	private BigDecimal valorInicial;
	private BigDecimal valorFinal;
	private Long idInicial;
	private Long idFinal;
	private String funcionario;

	public Date getDateInicial() {
		return dateInicial;
	}

	public void setDateInicial(Date dateInicial) {
		this.dateInicial = dateInicial;
	}

	public Date getDateFinal() {
		return dateFinal;
	}

	public void setDateFinal(Date dateFinal) {
		this.dateFinal = dateFinal;
	}

	public BigDecimal getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}

	public BigDecimal getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(BigDecimal valorFinal) {
		this.valorFinal = valorFinal;
	}

	public Long getIdInicial() {
		return idInicial;
	}

	public void setIdInicial(Long idInicial) {
		this.idInicial = idInicial;
	}

	public Long getIdFinal() {
		return idFinal;
	}

	public void setIdFinal(Long idFinal) {
		this.idFinal = idFinal;
	}

	public String getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}

}
