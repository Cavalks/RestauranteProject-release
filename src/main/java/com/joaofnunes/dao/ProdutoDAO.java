package com.joaofnunes.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.joaofnunes.filter.ProdutoFilter;
import com.joaofnunes.model.Produto;
import com.joaofnunes.model.ProdutoAux;
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

		// CriteriaBuilder builder = manager.getCriteriaBuilder();
		// CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		// Root<Produto> p = query.from(Produto.class);
		// query.select(p);
		// Session session = this.manager.unwrap(Session.class);
		//
		// Criteria criteria = session.createCriteria(Produto.class);
		//
		// // id deve ser maior ou igual (ge = greater or equals) a
		// // filtro.numeroDe
		// criteria.add(Restrictions.ge("id", calendarInicial.getTime()));
		//
		// // id deve ser menor ou igual (le = lower or equal) a
		// // filtro.numeroDe
		// criteria.add(Restrictions.le("id", dataFinal));

		return null;
	}

}
