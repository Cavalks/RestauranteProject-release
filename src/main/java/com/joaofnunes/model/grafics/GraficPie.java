package com.joaofnunes.model.grafics;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.primefaces.model.chart.PieChartModel;

import com.joaofnunes.dao.ProdutoDAO;
import com.joaofnunes.model.queries.Produtos;

public class GraficPie implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PieChartModel pieModel1;
	private List<Produtos> produtos;
	
	@Inject
	private ProdutoDAO produtoDAO;

	public PieChartModel getPieModel1() {

		return pieModel1;
	}

	private void createPieModel1() {
		pieModel1 = new PieChartModel();

		for (Produtos objects : produtos) {
			pieModel1.set(objects.getDescricao(), objects.getQuantidade());
			objects.setPorcentagem(calcularPorcentagem(produtoDAO.quantidadeProdutos(), objects.getQuantidade()));
			
		}

		pieModel1.setTitle("Produtos");
		pieModel1.setLegendPosition("w");
	}

	
	public void preencher(List<Produtos> produtos) {
		this.produtos = produtos;
		createPieModel1();

	}

	public String calcularPorcentagem(Long value, Long quantidade) {

		Double valorFinal = (quantidade.doubleValue()/value.doubleValue())*100;
		return String.format("%.2f", valorFinal) + "%";
	}

}
