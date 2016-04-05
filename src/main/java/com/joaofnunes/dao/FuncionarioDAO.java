package com.joaofnunes.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.joaofnunes.model.Funcionario;

public class FuncionarioDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager manager;

	public Funcionario porId(Long id) {
		return manager.find(Funcionario.class, id);
	}

	public List<Funcionario> porNome(String nome) {
		return this.manager.createQuery("from Produto where upper(nome) like :nome", Funcionario.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}

	public List<String> getFuncionarios() {
		return this.manager.createQuery("select F.nome from Funcionario F ", String.class).getResultList();
	}

	public Funcionario porEmail(String email) {
		Funcionario funcionario = null;

		try {
			funcionario = this.manager.createQuery("from Funcionario where lower(email) = :email", Funcionario.class)
					.setParameter("email", email.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {

		}

		return funcionario;
	}

}
