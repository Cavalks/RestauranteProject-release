package com.joaofnunes.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.joaofnunes.model.Funcionario;
import com.joaofnunes.model.ItemPedido;
import com.joaofnunes.model.Pedido;
import com.joaofnunes.model.Produto;
import com.joaofnunes.model.ProdutoAux;
import com.joaofnunes.util.jpa.Transactional;

public class PedidoDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Transactional
	public Pedido guardar(Pedido produto) {
		return manager.merge(produto);
	}

	@Transactional
	public void remover(Pedido pedido) {
		try {
			pedido = porId(pedido.getId());
			manager.remove(pedido);
			manager.flush();
		} catch (PersistenceException e) {
			// throw new NegocioException("Produto não pode ser excluído.");
		}
	}

	public Pedido porId(Long id) {
		return manager.find(Pedido.class, id);
	}

	public Pedido popularPedido(List<ProdutoAux> produtosAux, Pedido p) {
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
		//p.setFormaPagamento(FormaPagamento.DINHEIRO);
		return p;
	}

}
