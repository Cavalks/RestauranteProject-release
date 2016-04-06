package com.joaofnunes.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.joaofnunes.dao.FuncionarioDAO;
import com.joaofnunes.dao.PedidoDAO;
import com.joaofnunes.filter.PedidoFilter;
import com.joaofnunes.model.Funcionario;
import com.joaofnunes.model.Pedido;
import com.joaofnunes.model.ProdutoAux;

@Named
@ViewScoped
public class PedidosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ProdutoAux> test = new ArrayList<>();

	private List<Pedido> produtosFiltrados = new ArrayList<>();

	private Funcionario funcionario;

	private Pedido pedidoSelecionado;

	private boolean pesquisaPedidos = false;

	private boolean fecharCaixa = false;

	private Date dataCaixa = null;

	@Inject
	private PedidoDAO pedidoDao;

	private PedidoFilter pedidoFilter = new PedidoFilter();

	@Inject
	private FuncionarioDAO funcionarioDao;

	public List<String> lista(String lista) {

		return funcionarioDao.getFuncionarios();
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public PedidoFilter getPedidoFilter() {
		return pedidoFilter;
	}

	public void setPedidoFilter(PedidoFilter pedidoFilter) {
		this.pedidoFilter = pedidoFilter;
	}

	public String pesquisarFiltrados() {

		this.produtosFiltrados = pedidoDao.filtrados(pedidoFilter);

		return null;
	}

	public List<Pedido> getFiltrados() {
		return this.produtosFiltrados;
	}

	public void excluir() {
		pedidoDao.remover(this.pedidoSelecionado);
		pesquisarFiltrados();

	}

	public Pedido getPedidoSelecionado() {
		return pedidoSelecionado;
	}

	public void setPedidoSelecionado(Pedido pedidoSelecionado) {
		this.pedidoSelecionado = pedidoSelecionado;

	}

	public void editar() {
		this.test.clear();
		pedidoSelecionado = pedidoDao.pedidoComItens(this.pedidoSelecionado.getId());
		pedidoDao.pedidoToPedidoAux(this.test, this.pedidoSelecionado);
		for (ProdutoAux ip : this.test) {
			System.out.println(ip.getNome());
		}

	}

	public void actionPesquisar() {
		this.fecharCaixa = false;
		this.pesquisaPedidos = true;
	}

	public void actionCaixa() {
		this.fecharCaixa = true;
		this.pesquisaPedidos = false;
	}

	public boolean isFecharCaixa() {
		return fecharCaixa;
	}

	public void setFecharCaixa(boolean fecharCaixa) {
		this.fecharCaixa = fecharCaixa;
	}

	public boolean isPesquisaPedidos() {
		return pesquisaPedidos;
	}

	public void setPesquisaPedidos(boolean pesquisaPedidos) {
		this.pesquisaPedidos = pesquisaPedidos;
	}

	public String getValorCaixa() {

		return pedidoDao.caixaDiaSelecionado(dataCaixa);

	}

	public Date getDataCaixa() {
		return dataCaixa;
	}

	public void setDataCaixa(Date dataCaixa) {
		this.dataCaixa = dataCaixa;
	}

	public void ajaxDataCaixa() {
		getValorCaixa();
	}

}
