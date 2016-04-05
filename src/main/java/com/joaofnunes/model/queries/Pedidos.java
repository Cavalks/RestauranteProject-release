package com.joaofnunes.model.queries;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Pedidos {
	private Date data;

	private BigDecimal valorTotal;
	private BigDecimal lucro;
	private BigDecimal custoTotal;
	private Integer mes;
	private Integer ano;

	// Date(p.data_criacao) , sum(ip.quantidade*ip.valor_unitario),
	// sum(prod.custo*ip.quantidade) as custo
	public Pedidos(Date date, BigDecimal valorTotal, BigDecimal custo) {
		this.data = date;
		this.valorTotal = valorTotal;
		this.custoTotal = custo;
		double aux = valorTotal.doubleValue() - custo.doubleValue();
		System.out.println(valorTotal);
		System.out.println(custo);
		this.lucro = new BigDecimal(aux);

	}

	public Pedidos(Integer ano, Integer mes, BigDecimal valorTotal, BigDecimal custo) {
		this.ano = ano;
		this.mes = mes;
		this.valorTotal = valorTotal;
		this.custoTotal = custo;
		double aux = valorTotal.doubleValue() - custo.doubleValue();
		System.out.println(valorTotal);
		System.out.println(custo);
		this.lucro = new BigDecimal(aux);

	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getLucro() {
		return lucro;
	}

	public void setLucro(BigDecimal lucro) {
		this.lucro = lucro;
	}

	public BigDecimal getCustoTotal() {
		return custoTotal;
	}

	public void setCustoTotal(BigDecimal custoTotal) {
		this.custoTotal = custoTotal;
	}

	@Override
	public String toString() {
		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		return "Pedidos [data=" + date.format(data) + ", valorTotal=" + valorTotal + ", lucro=" + lucro
				+ ", custoTotal=" + custoTotal + "]";
	}

	public String getDataFormatada() {
		return new SimpleDateFormat("dd/MM/yyyy").format(data);
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
	

}
