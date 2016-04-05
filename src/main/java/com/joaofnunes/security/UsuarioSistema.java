package com.joaofnunes.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.joaofnunes.model.Funcionario;

public class UsuarioSistema extends User {

	private static final long serialVersionUID = 1L;

	private Funcionario usuario;

	public UsuarioSistema(Funcionario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getEmail(), usuario.getSenha(), authorities);
		this.usuario = usuario;
	}

	public Funcionario getUsuario() {
		return usuario;
	}

}
