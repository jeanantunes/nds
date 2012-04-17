package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

public class FiltroMonitorNfeDTO implements Serializable {
	
	private String box;
	private Date dataInicial;
	private Date dataFinal;
	private String destinatario;
	private String tipoNfe;
	private String numeroNotaInicial;
	private String numeroNotaFinal;
	private String chaveAcesso;
	private String situacaoNfe;
	
	/**
	 * Obtém box
	 *
	 * @return String
	 */
	public String getBox() {
		return box;
	}
	/**
	 * Atribuí box
	 * @param box 
	 */
	public void setBox(String box) {
		this.box = box;
	}
	/**
	 * Obtém dataInicial
	 *
	 * @return Date
	 */
	public Date getDataInicial() {
		return dataInicial;
	}
	/**
	 * Atribuí dataInicial
	 * @param dataInicial 
	 */
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	/**
	 * Obtém dataFinal
	 *
	 * @return Date
	 */
	public Date getDataFinal() {
		return dataFinal;
	}
	/**
	 * Atribuí dataFinal
	 * @param dataFinal 
	 */
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	/**
	 * Obtém destinatario
	 *
	 * @return String
	 */
	public String getDestinatario() {
		return destinatario;
	}
	/**
	 * Atribuí destinatario
	 * @param destinatario 
	 */
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	/**
	 * Obtém tipoNfe
	 *
	 * @return String
	 */
	public String getTipoNfe() {
		return tipoNfe;
	}
	/**
	 * Atribuí tipoNfe
	 * @param tipoNfe 
	 */
	public void setTipoNfe(String tipoNfe) {
		this.tipoNfe = tipoNfe;
	}
	/**
	 * Obtém numeroNotaInicial
	 *
	 * @return String
	 */
	public String getNumeroNotaInicial() {
		return numeroNotaInicial;
	}
	/**
	 * Atribuí numeroNotaInicial
	 * @param numeroNotaInicial 
	 */
	public void setNumeroNotaInicial(String numeroNotaInicial) {
		this.numeroNotaInicial = numeroNotaInicial;
	}
	/**
	 * Obtém numeroNotaFinal
	 *
	 * @return String
	 */
	public String getNumeroNotaFinal() {
		return numeroNotaFinal;
	}
	/**
	 * Atribuí numeroNotaFinal
	 * @param numeroNotaFinal 
	 */
	public void setNumeroNotaFinal(String numeroNotaFinal) {
		this.numeroNotaFinal = numeroNotaFinal;
	}
	/**
	 * Obtém chaveAcesso
	 *
	 * @return String
	 */
	public String getChaveAcesso() {
		return chaveAcesso;
	}
	/**
	 * Atribuí chaveAcesso
	 * @param chaveAcesso 
	 */
	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}
	/**
	 * Obtém situacaoNfe
	 *
	 * @return String
	 */
	public String getSituacaoNfe() {
		return situacaoNfe;
	}
	/**
	 * Atribuí situacaoNfe
	 * @param situacaoNfe 
	 */
	public void setSituacaoNfe(String situacaoNfe) {
		this.situacaoNfe = situacaoNfe;
	}
	
	
	
	
	
	
}
