package com.joaofnunes.model.grafics;

import java.text.SimpleDateFormat;
import java.util.List;

import org.primefaces.model.chart.PieChartModel;

public class GraficPie {
	private PieChartModel pieModel1;

	public PieChartModel getPieModel1() {

		return pieModel1;
	}

	private void createPieModel1(List<PedidoQuery> produtos) {
		pieModel1 = new PieChartModel();
		for (PedidoQuery pedidoQuery : produtos) {
			SimpleDateFormat simple = new SimpleDateFormat("dd/MM");
			String data = simple.format(pedidoQuery.getDate());
			System.out.println(data);
			pieModel1.set(data, pedidoQuery.getValor());
			
		}

		pieModel1.setTitle("Pedidos");
		pieModel1.setLegendPosition("w");
	}

	public void preencher(List<PedidoQuery> produtos) {
		createPieModel1(produtos);

	}

}
