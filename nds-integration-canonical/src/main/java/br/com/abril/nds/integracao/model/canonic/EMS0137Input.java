package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0137Input extends IntegracaoDocument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codigoDistribuidor;
	
	private List<EMS0137InputItem> itens = new ArrayList<EMS0137InputItem>();

	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	public List<EMS0137InputItem> getItens() {
		return itens;
	}

	public void setItens(List<EMS0137InputItem> itens) {
		this.itens = itens;
	}
	
}