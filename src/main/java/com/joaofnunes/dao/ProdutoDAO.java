package com.joaofnunes.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.joaofnunes.filter.ProdutoFilter;
import com.joaofnunes.model.Produto;
import com.joaofnunes.model.ProdutoAux;
import com.joaofnunes.model.queries.Produtos;
import com.joaofnunes.util.jpa.Transactional;

public class ProdutoDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Transactional
	public Produto guardar(Produto produto) {
		return manager.merge(produto);
	}

	@Transactional
	public void remover(Produto produto) {
		try {
			produto = porId(produto.getId());
			manager.remove(produto);
			manager.flush();
		} catch (PersistenceException e) {
			// throw new NegocioException("Produto não pode ser excluído.");
		}
	}

	public Produto porId(Long id) {
		return manager.find(Produto.class, id);
	}

	public List<Produto> porNome(String nome) {
		return this.manager.createQuery("from Produto where upper(nome) like :nome", Produto.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}

	public List<ProdutoAux> carregarProdutos() {

		List<Produto> listaProdutos = this.manager.createQuery("from Produto ", Produto.class).getResultList();
		List<ProdutoAux> listaProdutosAux = new ArrayList<>();
		ProdutoAux prodAux;
		for (Produto produto : listaProdutos) {
			if (produto.getId() != null) {
				prodAux = new ProdutoAux(produto.getNome(), 0);
				prodAux.setId(produto.getId());
				prodAux.setPreco(produto.getValorUnitario().doubleValue());
				listaProdutosAux.add(prodAux);

			}
		}
		return listaProdutosAux;

	}

	public List<ProdutoAux> carregarDiferençaProdutos(List<ProdutoAux> listaProdutosAux,
			List<ProdutoAux> listaProdutos) {

		ProdutoAux produtoAux;

		for (ProdutoAux produto : listaProdutos) {
			for (int i = 0; i < listaProdutosAux.size(); i++) {
				produtoAux = listaProdutosAux.get(i);
				if (produto.getNome() == produtoAux.getNome()) {
					produto.setQuantidade(produtoAux.getQuantidade());
				}
			}
		}

		return listaProdutos;
	}

	@SuppressWarnings("unchecked")

	public List<Produto> filtrados(ProdutoFilter filtro) {
		Session session = this.manager.unwrap(Session.class);

		Criteria criteria = session.createCriteria(Produto.class);

		if (filtro.getIdInicial() != null) {
			// id deve ser maior ou igual (ge = greater or equals) a
			// filtro.numeroDe
			criteria.add(Restrictions.ge("id", filtro.getIdInicial()));
		}

		if (filtro.getIdFinal() != null) {
			// id deve ser menor ou igual (le = lower or equal) a
			// filtro.numeroDe
			criteria.add(Restrictions.le("id", filtro.getIdFinal()));
		}

		if (filtro.getValorInicial() != null) {
			// id deve ser maior ou igual (ge = greater or equals) a
			// filtro.numeroDe
			criteria.add(Restrictions.ge("valorUnitario", filtro.getValorInicial()));
		}

		if (filtro.getValorFinal() != null) {
			// id deve ser menor ou igual (le = lower or equal) a
			// filtro.numeroDe
			criteria.add(Restrictions.le("valorUnitario", filtro.getValorFinal()));
		}

		return criteria.addOrder(Order.asc("id")).list();
	}

	public List<Produto> listaGrafico(int numero) {

		Calendar calendarInicial = Calendar.getInstance();
		Date dataFinal = new Date();

		if (numero == 1) {
			calendarInicial.add(Calendar.DAY_OF_MONTH, -7);
		} else if (numero == 2) {
			calendarInicial.add(Calendar.DAY_OF_MONTH, -15);
		} else if (numero == 3) {
			calendarInicial.add(Calendar.MONTH, -1);
		} else if (numero == 4) {
			calendarInicial.add(Calendar.YEAR, -1);
		}
		System.out.println("Inicial : ");
		System.out.println(new SimpleDateFormat("dd-MM-yyyy").format(calendarInicial.getTime()));
		System.out.println("Final : ");
		System.out.println(new SimpleDateFormat("dd-MM-yyyy").format(dataFinal));

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Produtos> listaConsultaGraficoPorPeriodo(Integer opcao) {
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = null;
		List<Produtos> lista = null;
		if (opcao == 1) {
			dataInicial.add(Calendar.DAY_OF_MONTH, -7);
			dataFinal = Calendar.getInstance();
		} else if (opcao == 2) {
			dataInicial.add(Calendar.DAY_OF_MONTH, -15);
			dataFinal = Calendar.getInstance();
		} else if (opcao == 3) {
			dataInicial.add(Calendar.MONTH, -1);
			dataFinal = Calendar.getInstance();
		} else if (opcao == 4) {
			dataInicial.add(Calendar.YEAR, -1);
			dataFinal = Calendar.getInstance();
		}

		lista = manager
				.createQuery(
						"Select new com.joaofnunes.model.queries.Produtos((itens.produto.nome) , (itens.produto.valorUnitario) , (itens.produto.custo) , (sum(itens.quantidade)) ) from Pedido P join P.itens itens where p.dataCriacao between :dateBegin AND :dateEnd  group by itens.produto.nome , itens.produto.valorUnitario , itens.produto.custo")
				.setParameter("dateBegin", dataInicial.getTime()).setParameter("dateEnd", dataFinal.getTime())
				.getResultList();

		return lista;
	}

	public List<Produtos> produtosTotalGrafic() {
		@SuppressWarnings("unchecked")
		// List<Object[]> lista = manager
		// .createQuery(
		// "Select itens.produto.nome , itens.produto.valorUnitario ,
		// itens.produto.custo , count(itens.quantidade) , sum(itens.quantidade)
		// from Pedido P join P.itens itens group by itens.produto.nome ,
		// itens.produto.valorUnitario , itens.produto.custo ")
		// .getResultList();

		List<Produtos> produtos = manager
				.createQuery(
						"Select new com.joaofnunes.model.queries.Produtos((itens.produto.nome) , (itens.produto.valorUnitario) , (itens.produto.custo) , (sum(itens.quantidade)) ) from Pedido P join P.itens itens  group by itens.produto.nome , itens.produto.valorUnitario , itens.produto.custo ")
				.getResultList();

		return produtos;
	}

	public String porcentagemProdutosTotal(List<Object[]> produtos) {
		StringBuilder sb = new StringBuilder();
		Long quantidade = manager
				.createQuery("Select sum(itens.quantidade) from Pedido P join P.itens itens ", Long.class)
				.getSingleResult();

		for (Object[] objects : produtos) {
			System.out.println(objects[0] + " " + objects[1]);

			Double valorFinal = (((Long) objects[1]).doubleValue() * 100) / quantidade;
			String resultado = String.format("%.2f", valorFinal);
			sb.append(((String) objects[0]) + " :  " + resultado + "%");
			sb.append("<br/>");
		}

		return sb.toString();
	}

	public Long quantidadeProdutos() {
		return (Long) manager.createQuery("Select sum(itens.quantidade) from Pedido P join P.itens itens ")
				.getSingleResult();
	}

	public Integer maiorValorTotal() {
		return manager.createQuery("Select max(itens.quantidade) from Pedido P join P.itens itens", Integer.class)
				.getSingleResult();
	}

	public Long quantidadeProdutosPorPeriodo(Integer opcao) {
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = null;
		Long result = null;
		if (opcao == 1) {
			dataInicial.add(Calendar.DAY_OF_MONTH, -7);
			dataFinal = Calendar.getInstance();
		} else if (opcao == 2) {
			dataInicial.add(Calendar.DAY_OF_MONTH, -15);
			dataFinal = Calendar.getInstance();
		} else if (opcao == 3) {
			dataInicial.add(Calendar.MONTH, -1);
			dataFinal = Calendar.getInstance();
		} else if (opcao == 4) {
			dataInicial.add(Calendar.YEAR, -1);
			dataFinal = Calendar.getInstance();
		}

		result = (Long) manager
				.createQuery(
						"Select sum(itens.quantidade) from Pedido P join P.itens itens where p.dataCriacao between :dateBegin AND :dateEnd ")
				.setParameter("dateBegin", dataInicial.getTime()).setParameter("dateEnd", dataFinal.getTime())
				.getSingleResult();

		return (long) result;
	}

	public List<Produto> getProdutos() {
		return manager.createQuery("from Produto p ", Produto.class).getResultList();
	}

	public List<Produtos> preencherLista(int opcao) {

		Calendar calendar = Calendar.getInstance();
		List<Produtos> pedidos = new ArrayList<>();
		calendar = DateUtils.truncate(calendar, Calendar.DAY_OF_MONTH);
		Produtos produto = null;

		if (opcao == 1) {
			for (int i = 0; i < 7; i++) {
				produto = new Produtos("", new BigDecimal(0), new BigDecimal(0), new Long(0), calendar.getTime(),
						new BigDecimal(0));
				pedidos.add(produto);
				calendar.add(Calendar.DAY_OF_MONTH, -1);

			}
		} else if (opcao == 2) {
			for (int i = 0; i < 15; i++) {
				produto = new Produtos("", new BigDecimal(0), new BigDecimal(0), new Long(0), calendar.getTime(),
						new BigDecimal(0));
				pedidos.add(produto);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
		} else if (opcao == 3) {
			for (int i = 0; i < 30; i++) {
				produto = new Produtos("", new BigDecimal(0), new BigDecimal(0), new Long(0), calendar.getTime(),
						new BigDecimal(0));
				pedidos.add(produto);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
		} else if (opcao == 4) {

			for (int i = 0; i < 12; i++) {
				produto = new Produtos("", new BigDecimal(0), (calendar.get(Calendar.MONTH) + 1),
						calendar.get(Calendar.YEAR), new BigDecimal(0), new BigDecimal(0), new Long(0));
				pedidos.add(produto);
				calendar.add(Calendar.MONTH, -1);
			}
		}

		return pedidos;

	}

	@SuppressWarnings("unchecked")
	public List<Produtos> listaConsultaGraficos(Integer opcao, Long produtoID) {
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = null;
		List<Produtos> lista = null;
		if (opcao == 1) {
			dataInicial.add(Calendar.DAY_OF_MONTH, -7);
			dataFinal = Calendar.getInstance();
		} else if (opcao == 2) {
			dataInicial.add(Calendar.DAY_OF_MONTH, -15);
			dataFinal = Calendar.getInstance();
		} else if (opcao == 3) {
			dataInicial.add(Calendar.MONTH, -1);
			dataFinal = Calendar.getInstance();
		}

		if (opcao == 1 || opcao == 2 || opcao == 3) {

			lista = manager
					.createQuery(
							"Select new com.joaofnunes.model.queries.Produtos((itens.produto.nome) , (itens.produto.valorUnitario) , (itens.produto.custo) , (sum(itens.quantidade)), DATE(p.dataCriacao) , sum(itens.valorUnitario*itens.quantidade) ) from Pedido P  join P.itens itens where itens.produto.id = :num and p.dataCriacao between :dateBegin AND :dateEnd group by itens.produto.nome , itens.produto.valorUnitario , itens.produto.custo,DATE(p.dataCriacao) ")
					.setParameter("dateBegin", dataInicial.getTime()).setParameter("dateEnd", dataFinal.getTime())
					.setParameter("num", produtoID).getResultList();

		} else if (opcao == 4) {
			lista = manager
					.createQuery(
							"Select new com.joaofnunes.model.queries.Produtos((itens.produto.nome) , sum(P.valorTotal) ,MONTH(P.dataCriacao) ,YEAR(P.dataCriacao) ,(itens.produto.valorUnitario), (itens.produto.custo) , (sum(itens.quantidade)) ) from Pedido P   join P.itens itens where itens.produto.id = :num  group by (itens.produto.nome) ,MONTH(P.dataCriacao) ,YEAR(P.dataCriacao) ,(itens.produto.valorUnitario), (itens.produto.custo)  ")
					.setParameter("num", produtoID).getResultList();

		}
		

		return lista;
	}

	public List<Produtos> unificarListasConsultaUnicoProduto(List<Produtos> listaGrande, List<Produtos> listaFiltrada,
			Integer opcao) {
		System.out.println("Unificar chamado");

		if (opcao != 4) {

			for (Produtos prod1 : listaGrande) {

				for (Produtos prod2 : listaFiltrada) {

					if (prod1.getDate().equals(prod2.getDate())) {

						prod1.setDescicao(prod2.getDescricao());
						prod1.setValorUnitario(prod2.getValorUnitario());
						prod1.setCusto(prod2.getCusto());
						prod1.setQuantidade(prod2.getQuantidade());
						prod1.setDate(prod2.getDate());
						prod1.setValorTotal(prod2.getValorTotal());
						prod1.setCusto(prod2.getCusto());
						
					}

				}
			}
		}
		if (opcao == 4) {
			
			for (Produtos prod1 : listaGrande) {

				for (Produtos prod2 : listaFiltrada) {

					if (prod1.getAno().equals(prod2.getAno()) && prod1.getMes().equals(prod2.getMes())) {

						prod1.setDescicao(prod2.getDescricao());
						prod1.setValorUnitario(prod2.getValorUnitario());
						prod1.setCusto(prod2.getCusto());
						prod1.setQuantidade(prod2.getQuantidade());
						prod1.setValorTotal(prod2.getValorTotal());
						prod1.setCusto(prod2.getCusto());
						
					}

				}
			}
		}

		return listaGrande;
	}
	
	

}
