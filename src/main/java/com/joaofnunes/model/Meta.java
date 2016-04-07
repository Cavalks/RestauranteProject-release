package com.joaofnunes.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "meta")
public class Meta {

	private Long id;
	private BigDecimal valorMeta;
	private BigDecimal valorAndamentoMeta;
	private Date dataInicial;
	private Date dataFinal;
	private Funcionario funcionario;
	private Status statusMeta;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@Column(name = "valor_meta", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorMeta() {
		return valorMeta;
	}

	public void setValorMeta(BigDecimal valorMeta) {
		this.valorMeta = valorMeta;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicial", nullable = false)
	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_final", nullable = false)
	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	@NotNull
	@OneToOne
	@JoinColumn(name = "funcionario_id", nullable = false)
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	public Status getStatusMeta() {
		return statusMeta;
	}

	public void setStatusMeta(Status statusMeta) {
		this.statusMeta = statusMeta;
	}

	@Column(name = "valor_meta_andamento", nullable = true, precision = 10, scale = 2)
	public BigDecimal getValorAndamentoMeta() {
		return valorAndamentoMeta;
	}

	public void setValorAndamentoMeta(BigDecimal valorAndamentoMeta) {
		this.valorAndamentoMeta = valorAndamentoMeta;
	}

}
