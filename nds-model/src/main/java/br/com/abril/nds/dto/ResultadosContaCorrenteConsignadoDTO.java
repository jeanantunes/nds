package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

@SuppressWarnings("serial")
public class ResultadosContaCorrenteConsignadoDTO implements Serializable {
	
	private TableModel<CellModelKeyValue<ConsignadoCotaDTO>> tableModelConsignado;
	private String dataEscolhida;
	private Collection<InfoTotalFornecedorDTO> listaInfoFornecedores = new ArrayList<InfoTotalFornecedorDTO>();
	
	public ResultadosContaCorrenteConsignadoDTO(
			TableModel<CellModelKeyValue<ConsignadoCotaDTO>> tableModelConsignado,			
			String dataEscolhida,
			Collection<InfoTotalFornecedorDTO> listaInfoFornecedores){
		super();
		this.tableModelConsignado = tableModelConsignado;
		this.dataEscolhida = dataEscolhida;
		this.listaInfoFornecedores = listaInfoFornecedores;
	}
	

	public TableModel<CellModelKeyValue<ConsignadoCotaDTO>> getTableModelConsignado() {
		return tableModelConsignado;
	}


	public void setTableModelConsignado(
			TableModel<CellModelKeyValue<ConsignadoCotaDTO>> tableModelConsignado) {
		this.tableModelConsignado = tableModelConsignado;
	}


	public String getDataEscolhida() {
		return dataEscolhida;
	}

	public void setDataEscolhida(String dataEscolhida) {
		this.dataEscolhida = dataEscolhida;
	}

	public Collection<InfoTotalFornecedorDTO> getListaInfoFornecedores() {
		return listaInfoFornecedores;
	}

	public void setListaInfoFornecedores(
			Collection<InfoTotalFornecedorDTO> listaInfoFornecedores) {
		this.listaInfoFornecedores = listaInfoFornecedores;
	}		

}
