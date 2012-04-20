package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

@SuppressWarnings("serial")
public class ResultadosContaCorrenteEncalheDTO implements Serializable {
	
	private TableModel<CellModelKeyValue<EncalheCotaDTO>> tableModelEncalhe;
	private String dataEscolhida;
	private List<InfoTotalFornecedorDTO> listaInfoFornecedores = new ArrayList<InfoTotalFornecedorDTO>();
	
	public ResultadosContaCorrenteEncalheDTO(			
			TableModel<CellModelKeyValue<EncalheCotaDTO>> tableModelEncalhe,
			String dataEscolhida,
			List<InfoTotalFornecedorDTO> listaInfoFornecedores){
		super();
		this.tableModelEncalhe = tableModelEncalhe;
		this.dataEscolhida = dataEscolhida;
		this.listaInfoFornecedores = listaInfoFornecedores;
	}
	

	public TableModel<CellModelKeyValue<EncalheCotaDTO>> getTableModelEncalhe() {
		return tableModelEncalhe;
	}



	public void setTableModelEncalhe(
			TableModel<CellModelKeyValue<EncalheCotaDTO>> tableModelEncalhe) {
		this.tableModelEncalhe = tableModelEncalhe;
	}



	public String getDataEscolhida() {
		return dataEscolhida;
	}

	public void setDataEscolhida(String dataEscolhida) {
		this.dataEscolhida = dataEscolhida;
	}

	public List<InfoTotalFornecedorDTO> getListaInfoFornecedores() {
		return listaInfoFornecedores;
	}

	public void setListaInfoFornecedores(
			List<InfoTotalFornecedorDTO> listaInfoFornecedores) {
		this.listaInfoFornecedores = listaInfoFornecedores;
	}		

}
