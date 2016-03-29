package com.joaofnunes.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
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

	public Pedido popularPedido(List<ProdutoAux> produtosAux, Pedido p,Pedido pedidoEditavel) {
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
		if (pedidoEditavel!=null) {
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

}
