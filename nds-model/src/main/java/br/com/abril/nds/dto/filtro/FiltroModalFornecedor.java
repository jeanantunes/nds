package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroModalFornecedor implements Serializable{

	private static final long serialVersionUID = 5178425158044489594L;
	
	private List<ItemDTO<Long, String>> listFornecedores = new ArrayList<ItemDTO<Long, String>>();
	private List<ItemDTO<Long, String>> listaFornecedorAssociado = new ArrayList<ItemDTO<Long, String>>();
	private List<Long> listaFornecedoresSelecionados = new ArrayList<Long>();

	public List<Long> getListaFornecedoresSelecionados() {
		return listaFornecedoresSelecionados;
	}

	public void setListaFornecedoresSelecionados(List<Long> listaFornecedoresSelecionados) {
		this.listaFornecedoresSelecionados = listaFornecedoresSelecionados;
	}

	public List<ItemDTO<Long, String>> getListFornecedores() {
		return listFornecedores;
	}

	public void setListFornecedores(List<ItemDTO<Long, String>> fornecedoresAtivos) {
		this.listFornecedores = fornecedoresAtivos;
	}

	public List<ItemDTO<Long, String>> getListaFornecedorAssociado() {
		return listaFornecedorAssociado;
	}

	public void setListaFornecedorAssociado(List<ItemDTO<Long,String>> listFornecedoresCota) {
		this.listaFornecedorAssociado = listFornecedoresCota;
	}
	
}
