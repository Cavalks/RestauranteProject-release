package com.joaofnunes.controller;

import java.text.SimpleDateFormat;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.joaofnunes.dao.FuncionarioDAO;
import com.joaofnunes.dao.PedidoDAO;
import com.joaofnunes.security.UsuarioLogado;
import com.joaofnunes.security.UsuarioSistema;

@Named
@RequestScoped
public class UsuarioLogadoBean {
	@Inject
	PedidoDAO pedidoDao;

	@Inject
	FuncionarioDAO funcionarioDao;

	public String getNomeUsuario() {
		String nome = null;

		UsuarioSistema usuarioLogado = getUsuarioLogado();

		if (usuarioLogado != null) {
			nome = usuarioLogado.getUsuario().getNome();
		}

		return nome;
	}

	@Produces
	@UsuarioLogado
	private UsuarioSistema getUsuarioLogado() {
		UsuarioSistema usuario = null;

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext
				.getCurrentInstance().getExternalContext().getUserPrincipal();

		if (auth != null && auth.getPrincipal() != null) {
			usuario = (UsuarioSistema) auth.getPrincipal();

		}

		return usuario;
	}

	public String getLogout() {
		funcionarioDao.updateUltimoAcesso(getUsuarioLogado().getUsuario());
		return "restaurante/j_spring_security_logout";
	}

	public Long getPedidos() {

		return pedidoDao.getPedidoTotalPorFuncionario(getUsuarioLogado().getUsuario());
	}

	public String getUltimaEntrada() {

		String s = new SimpleDateFormat("dd/MM/yyyy").format(getUsuarioLogado().getUsuario().getUltimoAcesso());

		return s;
	}

}
