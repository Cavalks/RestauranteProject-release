package com.joaofnunes.converter;

import com.joaofnunes.dao.ProdutoDAO;
import com.joaofnunes.model.Produto;
import com.joaofnunes.util.cdi.CDIServiceLocator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Produto.class)
public class ProdutoConverter implements Converter {

	// @Inject
	private ProdutoDAO produtos;

	public ProdutoConverter() {
		produtos = CDIServiceLocator.getBean(ProdutoDAO.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Produto retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = produtos.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Produto produto = (Produto) value;
			return produto.getId() == null ? null : produto.getId().toString();
		}

		return "";
	}

}
