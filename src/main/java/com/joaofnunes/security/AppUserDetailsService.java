package com.joaofnunes.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.joaofnunes.util.cdi.CDIServiceLocator;
import com.joaofnunes.dao.FuncionarioDAO;
import com.joaofnunes.model.Funcionario;

public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		FuncionarioDAO usuarios = CDIServiceLocator.getBean(FuncionarioDAO.class);
		Funcionario usuario = usuarios.porEmail(email);

		UsuarioSistema user = null;

		if (usuario != null) {
			user = new UsuarioSistema(usuario, getGrupos(usuario));
		}

		return user;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Funcionario usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		authorities.add(new SimpleGrantedAuthority("ADMINISTRADORES".toUpperCase()));

		return authorities;
	}

}
