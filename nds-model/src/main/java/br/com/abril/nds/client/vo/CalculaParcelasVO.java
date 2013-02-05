package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CalculaParcelasVO implements Serializable{

	private static final long serialVersionUID = -7655132940116482115L;
	
	private String numParcela;
	
	private String dataVencimento;
	
	private String parcela;
	
	private String encargos;
	
	private String parcTotal;
	
	private String ativarAoPagar = "";

	public String getNumParcela() {
		return numParcela;
	}

	public void setNumParcela(String numParcela) {
		this.numParcela = numParcela;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getParcela() {
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public String getEncargos() {
		return encargos;
	}

	public void setEncargos(String encargos) {
		this.encargos = encargos;
	}

	public String getParcTotal() {
		return parcTotal;
	}

	public void setParcTotal(String parcTotal) {
		this.parcTotal = parcTotal;
	}

	public String getAtivarAoPagar() {
		return ativarAoPagar;
	}

	public void setAtivarAoPagar(String ativarAoPagar) {
		this.ativarAoPagar = ativarAoPagar;
	}
	
	

}
