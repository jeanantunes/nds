package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroModalFornecedor implements Serializable{

	private static final long serialVersionUID = 5178425158044489594L;
	
	private List<Fornecedor> listFornecedores = new ArrayList<Fornecedor>();
	private List<Fornecedor> listaFornecedorAssociado = new ArrayList<Fornecedor>();
	private List<Long> listaFornecedoresSelecionados = new ArrayList<Long>();

	public List<Long> getListaFornecedoresSelecionados() {
		return listaFornecedoresSelecionados;
	}

	public void setListaFornecedoresSelecionados(List<Long> listaFornecedoresSelecionados) {
		this.listaFornecedoresSelecionados = listaFornecedoresSelecionados;
	}

	public List<Fornecedor> getListFornecedores() {
		return listFornecedores;
	}

	public void setListFornecedores(List<Fornecedor> listFornecedores) {
		this.listFornecedores = listFornecedores;
	}

	public List<Fornecedor> getListaFornecedorAssociado() {
		return listaFornecedorAssociado;
	}

	public void setListaFornecedorAssociado(List<Fornecedor> listaFornecedorAssociado) {
		this.listaFornecedorAssociado = listaFornecedorAssociado;
	}
	
}
