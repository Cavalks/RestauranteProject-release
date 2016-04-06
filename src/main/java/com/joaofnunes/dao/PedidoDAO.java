package com.joaofnunes.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.joaofnunes.filter.PedidoFilter;
import com.joaofnunes.model.Funcionario;
import com.joaofnunes.model.ItemPedido;
import com.joaofnunes.model.Pedido;
import com.joaofnunes.model.Produto;
import com.joaofnunes.model.ProdutoAux;
import com.joaofnunes.model.queries.Pedidos;
import com.joaofnunes.security.UsuarioLogado;
import com.joaofnunes.security.UsuarioSistema;
import com.joaofnunes.util.jpa.Transactional;
import com.joaofnunes.util.jsf.FacesUtil;

public class PedidoDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	@UsuarioLogado
	UsuarioSistema usuarioSistema;

	@Inject
	private EntityManager manager;

	@Transactional
	public Pedido guardar(Pedido pedido) {

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

				.createAlias("vendedor", "v");

		if (filtro.getIdInicial() != null) {

			criteria.add(Restrictions.ge("id", filtro.getIdInicial()));
		}

		if (filtro.getIdFinal() != null) {

			criteria.add(Restrictions.le("id", filtro.getIdFinal()));
		}

		if (filtro.getDateInicial() != null) {
			criteria.add(Restrictions.ge("dataCriacao", filtro.getDateInicial()));
		}

		if (filtro.getDateFinal() != null) {
			criteria.add(Restrictions.le("dataCriacao", filtro.getDateFinal()));
		}

		if (filtro.getValorInicial() != null) {

			criteria.add(Restrictions.ge("valorTotal", filtro.getValorInicial()));
		}

		if (filtro.getValorFinal() != null) {

			criteria.add(Restrictions.le("valorTotal", filtro.getValorFinal()));
		}

		if (StringUtils.isNotBlank(filtro.getFuncionario())) {

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
		Funcionario funcionario = usuarioSistema.getUsuario();
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

	public List<Pedidos> preencherLista(int opcao) {

		Calendar calendar = Calendar.getInstance();
		List<Pedidos> pedidos = new ArrayList<>();
		calendar = DateUtils.truncate(calendar, Calendar.DAY_OF_MONTH);
		Pedidos pedidoQuery = null;

		if (opcao == 1) {
			for (int i = 0; i < 7; i++) {
				pedidoQuery = new Pedidos(calendar.getTime(), new BigDecimal(0), new BigDecimal(0));
				pedidos.add(pedidoQuery);
				calendar.add(Calendar.DAY_OF_MONTH, -1);

			}
		} else if (opcao == 2) {
			for (int i = 0; i < 15; i++) {
				pedidoQuery = new Pedidos(calendar.getTime(), new BigDecimal(0), new BigDecimal(0));
				pedidos.add(pedidoQuery);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
		} else if (opcao == 3) {
			for (int i = 0; i < 30; i++) {
				pedidoQuery = new Pedidos(calendar.getTime(), new BigDecimal(0), new BigDecimal(0));
				pedidos.add(pedidoQuery);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
		} else if (opcao == 4) {
			for (int i = 0; i < 12; i++) {
				pedidoQuery = new Pedidos(calendar.getTime(), new BigDecimal(0), new BigDecimal(0));
				pedidos.add(pedidoQuery);
				calendar.add(Calendar.MONTH, -1);
			}
		}

		return pedidos;

	}

	@SuppressWarnings("unchecked")
	public List<Pedidos> listaConsultaGraficos(Integer opcao) {
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = null;
		List<Pedidos> lista = null;
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

		if (opcao == 1 || opcao == 2 || opcao == 3) {

			lista = manager
					.createQuery("Select new com.joaofnunes.model.queries.Pedidos ( Date(p.dataCriacao) , "
							+ "sum(itens.quantidade * itens.produto.valorUnitario) , sum(itens.produto.custo*itens.quantidade)  ) from Pedido P join P.itens itens where p.dataCriacao between :dateBegin AND :dateEnd group by  Date(p.dataCriacao)")
					.setParameter("dateBegin", dataInicial.getTime()).setParameter("dateEnd", dataFinal.getTime())
					.getResultList();

		} else if (opcao == 4) {

			lista = manager
					.createQuery(
							"Select new com.joaofnunes.model.queries.Pedidos (Year(p.dataCriacao) , Month(p.dataCriacao), sum(itens.quantidade * itens.produto.valorUnitario) , sum(itens.produto.custo*itens.quantidade)  ) from Pedido P join P.itens itens where p.dataCriacao between :dateBegin AND :dateEnd group by  Month(p.dataCriacao),Year(p.dataCriacao)")
					.setParameter("dateBegin", dataInicial.getTime()).setParameter("dateEnd", dataFinal.getTime())
					.getResultList();

		}

		return lista;
	}

	@SuppressWarnings("deprecation")
	public List<Pedidos> operarListas(List<Pedidos> grande, List<Pedidos> filtrada, Integer retorno) {

		if (retorno != 4) {

			for (Pedidos pedidos1 : grande) {
				for (Pedidos pedidos2 : filtrada) {
					if (pedidos1.getData().equals(pedidos2.getData())) {
						System.out.println("IGUAIS");
						pedidos1.setData(pedidos2.getData());
						pedidos1.setValorTotal(pedidos2.getValorTotal());
						pedidos1.setCustoTotal(pedidos2.getCustoTotal());
						pedidos1.setLucro(pedidos2.getLucro());
					}
				}
			}

		}

		if (retorno == 4) {

			for (Pedidos pedidos1 : grande) {

				for (Pedidos pedidos2 : filtrada) {

					if ((pedidos1.getData().getMonth() + 1) == pedidos2.getMes()
							&& (pedidos1.getData().getYear() + 1900) == pedidos2.getAno()) {

						pedidos2.setData(pedidos1.getData());
						pedidos1.setValorTotal(pedidos2.getValorTotal());
						pedidos1.setCustoTotal(pedidos2.getCustoTotal());
						pedidos1.setLucro(pedidos2.getLucro());
					}
				}

			}

		}

		return grande;
	}

	public Long getPedidoTotalPorFuncionario(Funcionario f) {

		return (Long) manager.createQuery("select count(p.id) from Pedido P where P.vendedor.id = :num")
				.setParameter("num", f.getId()).getSingleResult();
	}

	public String caixaDiaSelecionado(Date d) {
		if (d != null) {

			BigDecimal num = (BigDecimal) manager
					.createQuery("select sum(p.valorTotal) from Pedido p where DATE(p.dataCriacao)=:num")
					.setParameter("num", d).getSingleResult();
			if (num != null) {
				return "R$ " + num.toString();
			}

		}
		return "Sem registros.";

	}

	public boolean existenciaPedidos() {

		Long id = (Long) manager.createQuery("select count(p.id) from Pedido P").getSingleResult();
		System.out.println(id);
		if (id > 0) {
			return false;
		}
		return true;
	}

}
