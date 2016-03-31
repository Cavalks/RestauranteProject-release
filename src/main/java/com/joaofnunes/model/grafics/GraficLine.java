package com.joaofnunes.model.grafics;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

public class GraficLine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BarChartModel animatedModel2;
	private final SimpleDateFormat simple = new SimpleDateFormat("dd/MM");

	private List<PedidoQuery> pedidos;
	private Integer returnOpcao;

	public void init(List<PedidoQuery> pedidos, Integer returnOpcao) {
		this.pedidos = pedidos;
		this.returnOpcao = returnOpcao;
		createAnimatedModels();
	}

	public BarChartModel getAnimatedModel2() {
		return animatedModel2;
	}

	private void createAnimatedModels() {

		Axis yAxis = null;

		animatedModel2 = initBarModel();
		animatedModel2.setTitle("Pedidos");
		animatedModel2.setAnimate(true);
		animatedModel2.setLegendPosition("ne");
		yAxis = animatedModel2.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(5000);
	}

	private BarChartModel initBarModel() {

		BarChartModel model = new BarChartModel();
		ChartSeries boys = new ChartSeries();
		boys.setLabel("Pedidos");
		Collections.reverse(pedidos);
		if (returnOpcao == 1 || returnOpcao == 2 || returnOpcao == 3) {
			System.out.println("entrei");
			for (PedidoQuery pedidoQuery : pedidos) {

				boys.set(simple.format(pedidoQuery.getDate()), pedidoQuery.getValor());

			}

		}
		if (returnOpcao == 4) {

			for (PedidoQuery pedidoQuery : pedidos) {
				@SuppressWarnings("deprecation")
				int mes = pedidoQuery.getDate().getMonth() + 1;

				boys.set(mesCorrespondente(mes, pedidoQuery.getDate()), pedidoQuery.getValor());

			}

		}

		model.addSeries(boys);

		return model;
	}

	public String mesCorrespondente(int value, Date date) {
		SimpleDateFormat simple = new SimpleDateFormat("yyyy");
		String data = simple.format(date);
		if (value == 1) {

			return "Jane/" + data;

		} else if (value == 2) {
			return "Feve/" + data;
		} else if (value == 3) {
			return "Mar/" + data;
		} else if (value == 4) {
			return "Abri/" + data;
		} else if (value == 5) {
			return "Mai/" + data;
		} else if (value == 6) {
			return "Jun/" + data;
		} else if (value == 7) {
			return "Jul/" + data;
		} else if (value == 8) {
			return "Agos/" + data;
		} else if (value == 9) {
			return "Sete/" + data;
		} else if (value == 10) {
			return "Outu/" + data;
		} else if (value == 11) {

			return "Nove/" + data;
		} else if (value == 12) {
			return "Deze/" + data;
		}
		return null;
	}

}