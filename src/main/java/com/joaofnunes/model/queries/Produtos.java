package com.joaofnunes.model.queries;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Produtos implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String descricao;

	private BigDecimal valorUnitario;

	private BigDecimal custo;

	private Long quantidade;

	private Double lucro;

	private Long quantidadeTotal;

	private Double valorTotal;

	private String porcentagem;

	private Date date;

	private String dataProdutoUnitario;

	private Integer mes;
	private Integer ano;

	public Produtos() {

	}

	public Produtos(String descicao, BigDecimal valorUnitario, BigDecimal custo, Long quantidade) {

		this.descricao = descicao;
		this.valorUnitario = valorUnitario;
		this.custo = custo;
		this.quantidade = quantidade;
		lucro = ((valorUnitario.doubleValue() - custo.doubleValue()) * quantidade);
		valorTotal = quantidade * valorUnitario.doubleValue();

	}

	public Produtos(String descicao, BigDecimal valorUnitario, BigDecimal custo, Long quantidade, Date date,
			BigDecimal valorTotal, Integer mes, Integer ano) {

		this.descricao = descicao;
		this.valorUnitario = valorUnitario;
		this.custo = custo;
		this.quantidade = quantidade;
		this.date = date;
		this.valorTotal = valorTotal.doubleValue();

	}

	public Produtos(String descicao, BigDecimal valorTotal, Integer mes, Integer ano, BigDecimal valorUnitario,
			BigDecimal custo, Long quantidade) {

		this.descricao = descicao;
		this.valorUnitario = valorUnitario;
		this.custo = custo;
		this.quantidade = quantidade;
		this.mes = mes;
		this.ano = ano;
		this.valorTotal = valorTotal.doubleValue();

	}

	public Produtos(String descicao, BigDecimal valorUnitario, BigDecimal custo, Long quantidade, Date date,
			BigDecimal valorTotal) {

		this.descricao = descicao;
		this.valorUnitario = valorUnitario;
		this.custo = custo;
		this.quantidade = quantidade;
		this.date = date;
		this.valorTotal = valorTotal.doubleValue();

	}

	public Produtos(Long quantidade) {

		this.quantidade = quantidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescicao(String descicao) {
		this.descricao = descicao;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BigDecimal getCusto() {
		return custo;
	}

	public void setCusto(BigDecimal custo) {
		this.custo = custo;
	}

	public Double getLucro() {
		return lucro;
	}

	public void setLucro(Double lucro) {
		this.lucro = lucro;
	}

	public Long getQuantidadeTotal() {
		return quantidadeTotal;
	}

	public void setQuantidadeTotal(Long quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public String getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(String porcentagem) {
		this.porcentagem = porcentagem;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public String getDataProdutoUnitario() {
		return dataProdutoUnitario;
	}

	public void setDataProdutoUnitario(String dataProdutoUnitario) {
		this.dataProdutoUnitario = dataProdutoUnitario;
	}

}
