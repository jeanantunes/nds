package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS3100Input extends IntegracaoDocumentMaster<EMS3100InputItem> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String codigoDistribuidor;
	private Date dataSolicitacao;		
	private String formaSolicitacao;
	private String situacaoSolicitacao;
	
	private List<EMS3100InputItem> item = new ArrayList<EMS3100InputItem>();
	
	/**
	 * @return the codigoDistribuidor
	 */
	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}
	/**
	 * @param codigoDistribuidor the codigoDistribuidor to set
	 */
	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}
	/**
	 * @return the dataSolicitacao
	 */
	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}
	/**
	 * @param dataSolicitacao the dataSolicitacao to set
	 */
	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}
	/**
	 * @return the formaSolicitacao
	 */
	public String getFormaSolicitacao() {
		return formaSolicitacao;
	}
	/**
	 * @param formaSolicitacao the formaSolicitacao to set
	 */
	public void setFormaSolicitacao(String formaSolicitacao) {
		this.formaSolicitacao = formaSolicitacao;
	}
	/**
	 * @return the situacaoSolicitacao
	 */
	public String getSituacaoSolicitacao() {
		return situacaoSolicitacao;
	}
	/**
	 * @param situacaoSolicitacao the situacaoSolicitacao to set
	 */
	public void setSituacaoSolicitacao(String situacaoSolicitacao) {
		this.situacaoSolicitacao = situacaoSolicitacao;
	}
	/**
	 * @return the item
	 */
	public List<EMS3100InputItem> getItem() {
		return item;
	}
	/**
	 * @param item the item to set
	 */
	public void setItem(List<EMS3100InputItem> item) {
		this.item = item;
	}
	@Override
	public void addItem(IntegracaoDocumentDetail docD) {
		item.add((EMS3100InputItem) docD);		
	}
	@Override
	public List<EMS3100InputItem> getItems() {
		return item;
	}
	@Override
	public boolean sameObject(IntegracaoDocumentMaster<?> docM) {		
		return false; 
	}
	
		
}