package com.joaofnunes.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

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

}
