package com.joaofnunes.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.joaofnunes.model.Meta;
import com.joaofnunes.model.Status;
import com.joaofnunes.security.UsuarioLogado;
import com.joaofnunes.security.UsuarioSistema;
import com.joaofnunes.util.jpa.Transactional;

public class MetaDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Inject
	@UsuarioLogado
	UsuarioSistema usuarioSistema;

	public List<Meta> getMetas() {

		return manager.createQuery("select m from Meta m ", Meta.class).getResultList();

	}

	public List<Meta> getMetasAndamento() {
		return manager.createQuery("select m from Meta m where m.statusMeta = 'EM_ANDAMENTO'  ", Meta.class)
				.getResultList();
	}

	public List<Meta> getMetasExito() {
		return manager.createQuery("select m from Meta m where m.statusMeta = 'EXITO'  ", Meta.class).getResultList();
	}

	public List<Meta> getMetasFALHA() {
		return manager.createQuery("select m from Meta m where m.statusMeta = 'FALHA'  ", Meta.class).getResultList();
	}

	@Transactional
	public Meta andamentoToExito(Meta m) {
		Meta meta = manager.find(Meta.class, m.getId());
		meta = manager.merge(meta);
		meta.setStatusMeta(Status.EXITO);
		return meta;
	}

	@Transactional
	public Meta andamentoToFalha(Meta m) {
		Meta meta = manager.find(Meta.class, m.getId());
		meta = manager.merge(meta);
		meta.setStatusMeta(Status.FALHA);
		return meta;
	}

	@Transactional
	public Meta updateAndamentoValue(Meta m, BigDecimal value) {
		Meta meta = manager.find(Meta.class, m.getId());
		meta = manager.merge(meta);
		meta.setValorAndamentoMeta(value);
		return meta;
	}

	@Transactional
	public Meta gravar(Meta m) {
		Meta meta = m;
		meta = manager.merge(meta);
		

		return meta;
	}

}
