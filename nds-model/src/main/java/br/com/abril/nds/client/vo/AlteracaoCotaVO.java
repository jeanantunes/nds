package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;

public class AlteracaoCotaVO implements Serializable {

	private List<BaseComboVO> listFornecedores = new ArrayList<BaseComboVO>();
	private List<BaseComboVO> listaFornecedorAssociado = new ArrayList<BaseComboVO>();
	
	public List<BaseComboVO> getListFornecedores() {
		return listFornecedores;
	}
	public void setListFornecedores(List<BaseComboVO> listFornecedores) {
		this.listFornecedores = listFornecedores;
	}
	public List<BaseComboVO> getListaFornecedorAssociado() {
		return listaFornecedorAssociado;
	}
	public void setListaFornecedorAssociado(
			List<BaseComboVO> listaFornecedorAssociado) {
		this.listaFornecedorAssociado = listaFornecedorAssociado;
	}
	
	public List<BaseComboVO> parseComboFornecedor(List<Fornecedor> lista) {
		
		List<BaseComboVO> listaRet = new ArrayList<BaseComboVO>();

		for (Fornecedor obj : lista) {
			if(obj != null && obj.getJuridica() != null && obj.getJuridica().getRazaoSocial() != null && !"".equals(obj.getJuridica().getRazaoSocial())){
				listaRet.add(new BaseComboVO(obj.getId(), obj.getJuridica().getRazaoSocial()));
			}
		}
		
		return listaRet;
	}
}
