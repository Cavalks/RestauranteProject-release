package com.joaofnunes.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.joaofnunes.dao.PedidoDAO;
import com.joaofnunes.model.grafics.GraficLine;
import com.joaofnunes.model.grafics.GraficPie;
import com.joaofnunes.model.grafics.PedidoQuery;

@Named
@ViewScoped
public class GraficosBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoDAO pedidoDao;
	List<Object[]> listafiltrada;
	@Inject
	private GraficLine line;
	private Integer resultadoLista;
	List<PedidoQuery> pedidos;
	private GraficPie pie = new GraficPie();
	private boolean produtosValidation = false;
	private boolean pedidosValidation = false;

	public void actionGraficoAjax() {

		montarGrafico();

	}

	public Integer getResultadoLista() {
		return resultadoLista;
	}

	public void setResultadoLista(Integer resultadoLista) {
		this.resultadoLista = resultadoLista;
	}

	public GraficPie getPie() {
		return pie;
	}

	public void montarGrafico() {

		pedidos = pedidoDao.preencherLista(this.resultadoLista);
		listafiltrada = pedidoDao.listaConsultaGraficos(this.resultadoLista);
		pedidos = pedidoDao.operarListas(listafiltrada, pedidos, resultadoLista);
		line.init(pedidos, resultadoLista);

	}

	public GraficLine getLine() {
		return line;
	}

	public boolean isProdutosValidation() {
		return produtosValidation;
	}

	public void setProdutosValidation(boolean produtosValidation) {
		this.produtosValidation = produtosValidation;
	}

	public boolean isPedidosValidation() {
		return pedidosValidation;
	}

	public void setPedidosValidation(boolean pedidosValidation) {
		this.pedidosValidation = pedidosValidation;
	}

	public String actionProdutos() {

		produtosValidation = true;
		pedidosValidation = false;
		
		return null;
	}

	public String actionPedidos() {

		produtosValidation = false;
		pedidosValidation = true;
		
		

		return null;
	}

}
