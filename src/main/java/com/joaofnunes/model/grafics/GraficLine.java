package com.joaofnunes.model.grafics;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.joaofnunes.dao.ProdutoDAO;
import com.joaofnunes.model.queries.Pedidos;
import com.joaofnunes.model.queries.Produtos;

public class GraficLine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BarChartModel animatedModel2;
	private final SimpleDateFormat simple = new SimpleDateFormat("dd/MM");
	@Inject
	private ProdutoDAO produtoDao;
	private List<Pedidos> pedidos;
	private List<Produtos> produtos;
	private Integer returnOpcaoPedidos;
	private Integer returnOpcaoProdutos;
	private Integer returnOpcaoProdutosUnico;

	public void initPedidos(List<Pedidos> pedidos, Integer returnOpcao) {
		this.pedidos = pedidos;
		this.returnOpcaoPedidos = returnOpcao;

		this.produtos = null;
		this.returnOpcaoProdutos = null;
		this.returnOpcaoProdutosUnico = null;
		createAnimatedModels();
	}

	public void initProdutosUnitario(List<Produtos> produtos, Integer returnOpcao) {
		this.produtos = produtos;
		this.returnOpcaoProdutosUnico = returnOpcao;

		this.pedidos = null;
		this.returnOpcaoProdutos = null;
		this.returnOpcaoPedidos = null;
		createAnimatedModels();
	}

	public void initProdutos(List<Produtos> produtos, Integer returnOpcao) {
		this.produtos = produtos;
		this.returnOpcaoProdutos = returnOpcao;

		this.pedidos = null;
		this.returnOpcaoProdutosUnico = null;
		this.returnOpcaoPedidos = null;

		createAnimatedModels();
	}

	public BarChartModel getAnimatedModel2() {
		return animatedModel2;
	}

	private void createAnimatedModels() {

		Axis yAxis = null;
		animatedModel2 = null;
		if (returnOpcaoProdutos != null) {
			animatedModel2 = initBarModelProdutos();
			animatedModel2.setTitle("Produtos");
		} else if (returnOpcaoPedidos != null) {
			animatedModel2 = initBarModelPedidos();
			animatedModel2.setTitle("Pedidos");
		} else if (returnOpcaoProdutosUnico != null) {
			animatedModel2 = initBarModelProdutoUnico();
			animatedModel2.setTitle("Produto");
		}
		animatedModel2.setAnimate(true);
		animatedModel2.setLegendPosition("ne");
		yAxis = animatedModel2.getAxis(AxisType.Y);

		yAxis.setMin(0);
		yAxis.setMax(valorMaximo());

	}

	public Double valorMaximo() {
		Double aux = new Double(0);

		if (returnOpcaoProdutos != null) {
			for (Produtos produtos2 : produtos) {

				if (produtos2.getQuantidade() > aux) {
					aux = produtos2.getQuantidade().doubleValue();
				}

			}
		} else if (returnOpcaoPedidos != null) {
			for (Pedidos pedido : pedidos) {

				if (pedido.getValorTotal().doubleValue() > aux) {
					aux = pedido.getValorTotal().doubleValue();
				}

			}
		} else if (returnOpcaoProdutosUnico != null) {
			for (Produtos produtos2 : produtos) {

				if (produtos2.getValorTotal() > aux) {
					aux = produtos2.getValorTotal().doubleValue();
				}

			}

		}

		return aux;
	}

	private BarChartModel initBarModelPedidos() {

		BarChartModel model = new BarChartModel();
		ChartSeries boys = new ChartSeries();
		boys.setLabel("Pedidos");

		Collections.reverse(pedidos);
		if (returnOpcaoPedidos == 1 || returnOpcaoPedidos == 2 || returnOpcaoPedidos == 3) {

			for (Pedidos pedidoQuery : pedidos) {

				boys.set(simple.format(pedidoQuery.getData()), pedidoQuery.getValorTotal());

			}

		}
		if (returnOpcaoPedidos == 4) {

			for (Pedidos pedidoQuery : pedidos) {
				@SuppressWarnings("deprecation")
				int mes = pedidoQuery.getData().getMonth() + 1;

				boys.set(mesCorrespondente(mes, pedidoQuery.getData(), null), pedidoQuery.getValorTotal());

			}

		}

		model.addSeries(boys);

		return model;
	}

	private BarChartModel initBarModelProdutos() {

		BarChartModel model = new BarChartModel();
		ChartSeries produto[] = new ChartSeries[produtos.size()];
		List<Produtos> produtosAuxiliar = new ArrayList<>();

		for (int i = 0; i < produto.length; i++) {
			produto[i] = new ChartSeries();
			Produtos produtoAux = produtos.get(i);
			String nome = produtoAux.getDescricao();

			produto[i].setLabel(nome);
			if (returnOpcaoProdutos == 1) {
				produto[i].set("7 dias", produtoAux.getQuantidade());
			} else if (returnOpcaoProdutos == 2) {
				produto[i].set("15 dias", (Long) produtoAux.getQuantidade());
			} else if (returnOpcaoProdutos == 3) {
				produto[i].set("30 dias", (Long) produtoAux.getQuantidade());
			} else if (returnOpcaoProdutos == 4) {
				produto[i].set("1 ano ", (Long) produtoAux.getQuantidade());

			}

			produtoAux.setPorcentagem(calcularPorcentagem(
					produtoDao.quantidadeProdutosPorPeriodo(this.returnOpcaoProdutos), produtoAux.getQuantidade()));
			model.addSeries(produto[i]);
			produtosAuxiliar.add(produtoAux);

		}

		return model;
	}

	private BarChartModel initBarModelProdutoUnico() {

		BarChartModel model = new BarChartModel();
		ChartSeries boys = new ChartSeries();
		boys.setLabel("Produto");

		Collections.reverse(produtos);
		if (returnOpcaoProdutosUnico == 1 || returnOpcaoProdutosUnico == 2 || returnOpcaoProdutosUnico == 3) {

			for (Produtos prod : produtos) {

				boys.set(simple.format(prod.getDate()), prod.getValorTotal());

			}

		}
		if (returnOpcaoProdutosUnico == 4) {
			for (Produtos prod : produtos) {

				boys.set((prod.getMes() + "/" + prod.getAno()), prod.getValorTotal());

			}
		}

		model.addSeries(boys);

		return model;
	}

	public List<Produtos> atualizarLista() {
		return this.produtos;
	}

	public String mesCorrespondente(int value, Date date, Integer ano) {

		if (date != null) {
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
		}
		if (ano != null) {
			if (value == 1) {

				return "Jane/" + ano;

			} else if (value == 2) {
				return "Feve/" + ano;
			} else if (value == 3) {
				return "Mar/" + ano;
			} else if (value == 4) {
				return "Abri/" + ano;
			} else if (value == 5) {
				return "Mai/" + ano;
			} else if (value == 6) {
				return "Jun/" + ano;
			} else if (value == 7) {
				return "Jul/" + ano;
			} else if (value == 8) {
				return "Agos/" + ano;
			} else if (value == 9) {
				return "Sete/" + ano;
			} else if (value == 10) {
				return "Outu/" + ano;
			} else if (value == 11) {

				return "Nove/" + ano;
			} else if (value == 12) {
				return "Deze/" + ano;
			}
		}

		return null;
	}

	public String calcularPorcentagem(Long value, Long quantidade) {

		Double valorFinal = (quantidade.doubleValue() / value.doubleValue()) * 100;
		return String.format("%.2f", valorFinal) + "%";
	}

}