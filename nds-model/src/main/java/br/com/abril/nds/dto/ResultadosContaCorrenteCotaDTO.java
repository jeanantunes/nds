package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;

@SuppressWarnings("serial")
public class ResultadosContaCorrenteCotaDTO implements Serializable {
	

	private TableModel<CellModel> tableModel;
	private String dataEscolhida;
	private List<InfoTotalFornecedorDTO> listaInfoFornecedores = new ArrayList<InfoTotalFornecedorDTO>();
	
	public ResultadosContaCorrenteCotaDTO(
			TableModel<CellModel> tableModel,
			String dataEscolhida,
			List<InfoTotalFornecedorDTO> listaInfoFornecedores){
		super();
		this.tableModel = tableModel;
		this.dataEscolhida = dataEscolhida;
		this.listaInfoFornecedores = listaInfoFornecedores;
	}

	public TableModel<CellModel> getTableModel() {
		return tableModel;
	}

	public void setTableModel(TableModel<CellModel> tableModel) {
		this.tableModel = tableModel;
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
