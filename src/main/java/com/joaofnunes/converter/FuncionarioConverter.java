package com.joaofnunes.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.joaofnunes.dao.FuncionarioDAO;
import com.joaofnunes.model.Funcionario;
import com.joaofnunes.util.cdi.CDIServiceLocator;



//@FacesConverter(forClass=Funcionario.class)
public class FuncionarioConverter implements Converter {

	
	private FuncionarioDAO funcionarios;
	
	public FuncionarioConverter() {
		this.funcionarios = (FuncionarioDAO) CDIServiceLocator.getBean(FuncionarioDAO.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Funcionario retorno = null;

		if (value != null) {
			retorno = this.funcionarios.porId(new Long(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			return ((Funcionario) value).getId().toString();
		}
		return "";
	}

}