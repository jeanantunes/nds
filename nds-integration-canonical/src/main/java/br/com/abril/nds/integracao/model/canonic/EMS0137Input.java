package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0137Input extends IntegracaoDocumentMaster<EMS0137InputItem> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codigoDistribuidor;
	
	private List<EMS0137InputItem> chamadaEncalheItens = new ArrayList<EMS0137InputItem>();

	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	public List<EMS0137InputItem> getItens() {
		return chamadaEncalheItens;
	}

	public void setItens(List<EMS0137InputItem> itens) {
		this.chamadaEncalheItens = itens;
	}
	
	public void setChamadaEncalheItens(List<EMS0137InputItem> itens) {
		this.chamadaEncalheItens = itens;
	}

	@Override
	public void addItem(IntegracaoDocumentDetail docD) {
		chamadaEncalheItens.add((EMS0137InputItem) docD);		
	}
	
	@Override
	public List<EMS0137InputItem> getItems() {
		return chamadaEncalheItens;
	}
	
	@Override
	public boolean sameObject(IntegracaoDocumentMaster<?> docM) {	
		//FIXME: Sérgio: Colocar a propriedade identificadora
		return (null == docM ? false : ((EMS0137Input)docM).getCodigoDistribuidor().equals(this.codigoDistribuidor)) ;
	}
	
}