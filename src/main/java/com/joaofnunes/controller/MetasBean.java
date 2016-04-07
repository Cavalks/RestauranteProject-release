package com.joaofnunes.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.bean.ViewScoped;

import com.joaofnunes.dao.MetaDAO;
import com.joaofnunes.dao.PedidoDAO;
import com.joaofnunes.model.Meta;
import com.joaofnunes.util.jsf.FacesUtil;

@Named
@ViewScoped
public class MetasBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private MetaDAO metaDao;
	@Inject
	private PedidoDAO pedidoDao;

	private Meta metaSelecioada;

	private Integer returnMeta;

	private List<Meta> metas;

	private boolean mostrarBarra = false;

	public MetasBean() {
		System.out.println("chamado constr");
	}

	public void action(ComponentSystemEvent e) {
		System.out.println("chamado 1");
		if (FacesUtil.isNotPostback()) {
			System.out.println("chamado 2");
			updateAndamentos();
			validarMetas();

		}

	}

	public List<Meta> getMetas() {
		return metas;
	}

	public void setMetas(List<Meta> metas) {
		this.metas = metas;
	}

	public Double getValueBar() {
		return 67d;
	}

	public boolean isMostrarBarra() {
		return mostrarBarra;
	}

	public void setMostrarBarra(boolean mostrarBarra) {
		this.mostrarBarra = mostrarBarra;
	}

	public void test() {
		System.out.println("chamado");
		mostrarBarra = true;
		System.out.println(mostrarBarra);
	}

	public Meta getMetaSelecioada() {
		return metaSelecioada;
	}

	public void setMetaSelecioada(Meta metaSelecioada) {
		this.metaSelecioada = metaSelecioada;
		this.metaSelecioada.setValorMeta(new BigDecimal(80));
	}

	public void validarMetas() {
		List<Meta> metasAndamento = metaDao.getMetasAndamento();

		for (Meta meta : metasAndamento) {

			if (meta.getValorAndamentoMeta().doubleValue() >= meta.getValorMeta().doubleValue()) {

				metaDao.andamentoToExito(meta);
			} else if (meta.getValorAndamentoMeta().doubleValue() < meta.getValorMeta().doubleValue()
					&& new Date().after(meta.getDataFinal())) {

				metaDao.andamentoToFalha(meta);
			}

		}
	}

	public void updateAndamentos() {
		List<Meta> metasAndamento = metaDao.getMetasAndamento();

		for (Meta meta : metasAndamento) {

			BigDecimal valueMeta = pedidoDao.getPedidosTotalIntervaloData(meta.getDataInicial(), meta.getDataFinal());
			metaDao.updateAndamentoValue(meta, valueMeta);

		}
	}

	public Integer getReturnMeta() {
		return returnMeta;
	}

	public void setReturnMeta(Integer returnMeta) {
		this.returnMeta = returnMeta;
	}

	public void actionAjax() {
		if (returnMeta == 1) {
			metas = metaDao.getMetasAndamento();
		} else if (returnMeta == 2) {
			metas = metaDao.getMetasExito();
		} else if (returnMeta == 3) {
			metas = metaDao.getMetasFALHA();
		}
	}

}
