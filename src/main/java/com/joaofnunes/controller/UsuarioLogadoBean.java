package com.joaofnunes.controller;

import java.text.SimpleDateFormat;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.joaofnunes.dao.FuncionarioDAO;
import com.joaofnunes.dao.PedidoDAO;
import com.joaofnunes.security.UsuarioLogado;
import com.joaofnunes.security.UsuarioSistema;

@Named
@RequestScoped
public class UsuarioLogadoBean {
	@Inject
	private PedidoDAO pedidoDao;

	@Inject
	private FuncionarioDAO funcionarioDao;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioSistema;

	public String getNomeUsuario() {

		return usuarioSistema.getUsuario().getNome();
	}

	public String getLogout() {
		funcionarioDao.updateUltimoAcesso(usuarioSistema.getUsuario());
		return "restaurante/j_spring_security_logout";
	}

	public Long getPedidos() {

		return pedidoDao.getPedidoTotalPorFuncionario(usuarioSistema.getUsuario());
	}

	public String getUltimaEntrada() {

		String s = new SimpleDateFormat("dd/MM/yyyy").format(usuarioSistema.getUsuario().getUltimoAcesso());

		return s;
	}

}
