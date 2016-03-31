package com.joaofnunes.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.joaofnunes.filter.PedidoFilter;
import com.joaofnunes.model.DataValor;
import com.joaofnunes.model.Funcionario;
import com.joaofnunes.model.ItemPedido;
import com.joaofnunes.model.Pedido;
import com.joaofnunes.model.Produto;
import com.joaofnunes.model.ProdutoAux;
import com.joaofnunes.model.grafics.PedidoQuery;
import com.joaofnunes.util.jpa.Transactional;
import com.joaofnunes.util.jsf.FacesUtil;

public class PedidoDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Transactional
	public Pedido guardar(Pedido pedido) {
		System.out.println(pedido.getId());
		return manager.merge(pedido);
	}

	@Transactional
	public void remover(Pedido pedido) {
		try {
			pedido = porId(pedido.getId());
			manager.remove(pedido);
			manager.flush();
		} catch (PersistenceException e) {
			FacesUtil.addErrorMessage("Produto não pode ser apagado.");
		}
	}

	@SuppressWarnings("unchecked")
	public List<Pedido> filtrados(PedidoFilter filtro) {
		Session session = this.manager.unwrap(Session.class);

		Criteria criteria = session.createCriteria(Pedido.class)

				// fazemos uma associação (join) com vendedor e nomeamos como
				// "v"
				.createAlias("vendedor", "v");

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

		if (filtro.getDateInicial() != null) {
			criteria.add(Restrictions.ge("dataCriacao", filtro.getDateInicial()));
		}

		if (filtro.getDateFinal() != null) {
			criteria.add(Restrictions.le("dataCriacao", filtro.getDateFinal()));
		}

		if (filtro.getValorInicial() != null) {
			// id deve ser maior ou igual (ge = greater or equals) a
			// filtro.numeroDe
			criteria.add(Restrictions.ge("valorTotal", filtro.getValorInicial()));
		}

		if (filtro.getValorFinal() != null) {
			// id deve ser menor ou igual (le = lower or equal) a
			// filtro.numeroDe
			criteria.add(Restrictions.le("valorTotal", filtro.getValorFinal()));
		}

		if (StringUtils.isNotBlank(filtro.getFuncionario())) {
			// acessamos o nome do vendedor associado ao pedido pelo alias
			// "v",
			// criado anteriormente
			criteria.add(Restrictions.ilike("v.nome", filtro.getFuncionario(), MatchMode.ANYWHERE));

		}

		return criteria.addOrder(Order.asc("id")).list();
	}

	public Pedido porId(Long id) {
		return manager.find(Pedido.class, id);
	}

	public Pedido popularPedido(List<ProdutoAux> produtosAux, Pedido p, Pedido pedidoEditavel) {
		Produto produto = null;
		List<ItemPedido> ipl = new ArrayList<>();
		ItemPedido ip = new ItemPedido();
		Double valorT = new Double(0);

		for (ProdutoAux produtoAux : produtosAux) {

			produto = manager.find(Produto.class, produtoAux.getId());
			ip.setProduto(produto);
			ip.setQuantidade(produtoAux.getQuantidade());
			ip.setValorUnitario(new BigDecimal(produtoAux.getPreco()));

			ip.setPedido(p);
			ipl.add(ip);
			ip = new ItemPedido();

		}
		p.setItens(ipl);

		for (ItemPedido itemPedido : ipl) {
			System.out.println(itemPedido.getProduto().getNome() + " " + itemPedido.getValorTotal());
			valorT += itemPedido.getValorTotal().doubleValue();
		}
		p.setValorTotal(new BigDecimal(valorT));
		System.out.println(p.getValorTotal());
		Funcionario funcionario = manager.find(Funcionario.class, new Long(1));
		System.out.println(funcionario.getNome());
		p.setVendedor(funcionario);
		p.setDataCriacao(new Date());
		if (pedidoEditavel != null) {
			p.setId(pedidoEditavel.getId());
		}
		System.out.println(p.getId());
		return p;
	}

	public Pedido pedidoComItens(Long id) {

		Pedido p = (Pedido) manager.createQuery("select p from Pedido p join p.itens a where p.id = ?")
				.setParameter(1, id).getSingleResult();

		return p;
	}

	public List<ProdutoAux> pedidoToPedidoAux(List<ProdutoAux> produtosSelecionados, Pedido pedido) {
		ProdutoAux produtoAux = null;

		for (ItemPedido ip : pedido.getItens()) {
			produtoAux = new ProdutoAux(ip.getProduto().getNome(), ip.getQuantidade());
			produtoAux.setPreco(ip.getValorUnitario().doubleValue());
			produtoAux.setId(ip.getProduto().getId());
			produtosSelecionados.add(produtoAux);
		}

		return produtosSelecionados;
	}

	@SuppressWarnings({ "unchecked" })
	public Map<Date, BigDecimal> valoresTotaisPorData(Integer numeroDeDias, Funcionario criadoPor, Session s) {
		Session session = s;

		numeroDeDias -= 1;

		Calendar dataInicial = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);

		Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDeDias, dataInicial);

		Criteria criteria = session.createCriteria(Pedido.class);

		// select date(data_criacao) as data, sum(valor_total) as valor
		// from pedido where data_criacao >= :dataInicial and vendedor_id =
		// :criadoPor
		// group by date(data_criacao)

		criteria.setProjection(Projections.projectionList()
				.add(Projections.sqlGroupProjection("date(data_criacao) as data", "date(data_criacao)",
						new String[] { "data" }, new Type[] { StandardBasicTypes.DATE }))
				.add(Projections.sum("valorTotal").as("valor")))
				.add(Restrictions.ge("dataCriacao", dataInicial.getTime()));

		if (criadoPor != null) {
			criteria.add(Restrictions.eq("vendedor", criadoPor));
		}

		List<DataValor> valoresPorData = criteria.setResultTransformer(Transformers.aliasToBean(DataValor.class))
				.list();

		for (DataValor dataValor : valoresPorData) {
			resultado.put(dataValor.getData(), dataValor.getValor());
		}

		return resultado;
	}

	private Map<Date, BigDecimal> criarMapaVazio(Integer numeroDeDias, Calendar dataInicial) {
		dataInicial = (Calendar) dataInicial.clone();
		Map<Date, BigDecimal> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= numeroDeDias; i++) {
			mapaInicial.put(dataInicial.getTime(), BigDecimal.ZERO);
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}

		return mapaInicial;
	}

	public List<PedidoQuery> preencherLista(int opcao) {

		Calendar calendar = Calendar.getInstance();
		List<PedidoQuery> pedidos = new ArrayList<>();
		calendar = DateUtils.truncate(calendar, Calendar.DAY_OF_MONTH);
		PedidoQuery pedidoQuery = null;

		if (opcao == 1) {
			for (int i = 0; i < 7; i++) {
				pedidoQuery = new PedidoQuery(calendar.getTime(), new BigDecimal(0));
				pedidos.add(pedidoQuery);
				calendar.add(Calendar.DAY_OF_MONTH, -1);

			}
		} else if (opcao == 2) {
			for (int i = 0; i < 15; i++) {
				pedidoQuery = new PedidoQuery(calendar.getTime(), new BigDecimal(0));
				pedidos.add(pedidoQuery);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
		} else if (opcao == 3) {
			for (int i = 0; i < 30; i++) {
				pedidoQuery = new PedidoQuery(calendar.getTime(), new BigDecimal(0));
				pedidos.add(pedidoQuery);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
		} else if (opcao == 4) {
			for (int i = 0; i < 12; i++) {
				pedidoQuery = new PedidoQuery(calendar.getTime(), new BigDecimal(0));
				pedidos.add(pedidoQuery);
				calendar.add(Calendar.MONTH, -1);
			}
		}

		return pedidos;

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> listaConsultaGraficos(Integer opcao) {
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = null;
		List<Object[]> lista = null;
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
							"Select DATE(c.dataCriacao),sum(c.valorTotal) from Pedido c  where c.dataCriacao between :dateBegin AND :dateEnd group by DATE(c.dataCriacao)")
					.setParameter("dateBegin", dataInicial.getTime()).setParameter("dateEnd", dataFinal.getTime())
					.getResultList();

		} else if (opcao == 4) {
			lista = manager
					.createQuery(
							"Select MONTH(P.dataCriacao) ,YEAR(P.dataCriacao) ,sum(P.valorTotal) from Pedido P group by MONTH(P.dataCriacao) ,YEAR(P.dataCriacao)")
					.getResultList();

		}

		return lista;
	}

	@SuppressWarnings("deprecation")
	public List<PedidoQuery> operarListas(List<Object[]> lista1, List<PedidoQuery> lista2, Integer retorno) {

		if (retorno != 4) {
			for (Object[] pedido : lista1) {

				for (PedidoQuery pedido2 : lista2) {
					Date date = (Date) pedido[0];
					if (date.equals(pedido2.getDate())) {
						BigDecimal valor = (BigDecimal) pedido[1];
						pedido2.setValor(valor);
					}

				}

			}
		}
		if (retorno == 4) {

			for (Object[] pedido : lista1) {

				for (PedidoQuery pedido2 : lista2) {

					int mes = (int) pedido[0] - 1;
					int ano = (int) pedido[1] - 1900;
					if ((pedido2.getDate().getMonth()) == mes && pedido2.getDate().getYear() == ano) {

						BigDecimal valor = (BigDecimal) pedido[2];
						pedido2.setValor(valor);
					}

				}

			}
		}
		return lista2;
	}

}
