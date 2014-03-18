package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DataCEConferivelDTO implements Serializable {
	
	private List<Date> listaDataConferivelProdutoNaoParcial;
	
	private List<Date> listaDataConferivelProdutoParcial;

	public List<Date> getListaDataConferivelProdutoNaoParcial() {
		return listaDataConferivelProdutoNaoParcial;
	}

	public void setListaDataConferivelProdutoNaoParcial(
			final List<Date> listaDataConferivelProdutoNaoParcial) {
		this.listaDataConferivelProdutoNaoParcial = listaDataConferivelProdutoNaoParcial;
	}

	public List<Date> getListaDataConferivelProdutoParcial() {
		return listaDataConferivelProdutoParcial;
	}

	public void setListaDataConferivelProdutoParcial(
			final List<Date> listaDataConferivelProdutoParcial) {
		this.listaDataConferivelProdutoParcial = listaDataConferivelProdutoParcial;
	}
	
}
