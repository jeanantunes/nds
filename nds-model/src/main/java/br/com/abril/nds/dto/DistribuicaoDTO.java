package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Representação de Campos da tela de Distribuição (Cadastro de Cota)
 * 
 * @author guilherme.morais
 *
 */
public class DistribuicaoDTO implements Serializable  {

	private static final long serialVersionUID = 7056333731558241749L;

	
	private Integer numCota;
	private Integer qtdePDV;
	private Boolean qtdeAutomatica;
	private String box;
	private String assistComercial;
	private Long tipoEntrega;
	private String observacao;
	private Boolean arrendatario;
	private Boolean repPorPontoVenda;
	private Boolean solNumAtras;
	private Boolean recebeRecolhe;
	private Boolean neImpresso;
	private Boolean neEmail;
	private Boolean ceImpresso;
	private Boolean ceEmail;
	private Boolean slipImpresso;
	private Boolean slipEmail;
	
	private List<ItemDTO<Long, String>> tiposEntrega;
	
	public DistribuicaoDTO(){
		
	}

	/**
	 * @return the numCota
	 */
	public Integer getNumCota() {
		return numCota;
	}

	/**
	 * @param numCota the numCota to set
	 */
	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
	}

	/**
	 * @return the qtdePDV
	 */
	public Integer getQtdePDV() {
		return qtdePDV;
	}

	/**
	 * @param qtdePDV the qtdePDV to set
	 */
	public void setQtdePDV(Integer qtdePDV) {
		this.qtdePDV = qtdePDV;
	}

	/**
	 * @return the box
	 */
	public String getBox() {
		return box;
	}

	/**
	 * @param box the box to set
	 */
	public void setBox(String box) {
		this.box = box;
	}

	/**
	 * @return the assistComercial
	 */
	public String getAssistComercial() {
		return assistComercial;
	}

	/**
	 * @param assistComercial the assistComercial to set
	 */
	public void setAssistComercial(String assistComercial) {
		this.assistComercial = assistComercial;
	}

	/**
	 * @return the tipoEntrega
	 */
	public Long getTipoEntrega() {
		return tipoEntrega;
	}

	/**
	 * @param tipoEntrega the tipoEntrega to set
	 */
	public void setTipoEntrega(Long tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
	}

	/**
	 * @return the observacao
	 */
	public String getObservacao() {
		return observacao;
	}

	/**
	 * @param observacao the observacao to set
	 */
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	/**
	 * @return the arrendatario
	 */
	public Boolean getArrendatario() {
		return arrendatario;
	}

	/**
	 * @param arrendatario the arrendatario to set
	 */
	public void setArrendatario(Boolean arrendatario) {
		this.arrendatario = arrendatario;
	}
	
	/**
	 * @return the recebeRecolhe
	 */
	public Boolean getRecebeRecolhe() {
		return recebeRecolhe;
	}

	/**
	 * @param recebeRecolhe the recebeRecolhe to set
	 */
	public void setRecebeRecolhe(Boolean recebeRecolhe) {
		this.recebeRecolhe = recebeRecolhe;
	}

	/**
	 * @return the neImpresso
	 */
	public Boolean getNeImpresso() {
		return neImpresso;
	}

	/**
	 * @param neImpresso the neImpresso to set
	 */
	public void setNeImpresso(Boolean neImpresso) {
		this.neImpresso = neImpresso;
	}

	/**
	 * @return the neEmail
	 */
	public Boolean getNeEmail() {
		return neEmail;
	}

	/**
	 * @param neEmail the neEmail to set
	 */
	public void setNeEmail(Boolean neEmail) {
		this.neEmail = neEmail;
	}

	/**
	 * @return the ceImpresso
	 */
	public Boolean getCeImpresso() {
		return ceImpresso;
	}

	/**
	 * @param ceImpresso the ceImpresso to set
	 */
	public void setCeImpresso(Boolean ceImpresso) {
		this.ceImpresso = ceImpresso;
	}

	/**
	 * @return the ceEmail
	 */
	public Boolean getCeEmail() {
		return ceEmail;
	}

	/**
	 * @param ceEmail the ceEmail to set
	 */
	public void setCeEmail(Boolean ceEmail) {
		this.ceEmail = ceEmail;
	}

	/**
	 * @return the slipImpresso
	 */
	public Boolean getSlipImpresso() {
		return slipImpresso;
	}

	/**
	 * @param slipImpresso the slipImpresso to set
	 */
	public void setSlipImpresso(Boolean slipImpresso) {
		this.slipImpresso = slipImpresso;
	}

	/**
	 * @return the slipEmail
	 */
	public Boolean getSlipEmail() {
		return slipEmail;
	}

	/**
	 * @param slipEmail the slipEmail to set
	 */
	public void setSlipEmail(Boolean slipEmail) {
		this.slipEmail = slipEmail;
	}

	/**
	 * @return the tiposEntrega
	 */
	public List<ItemDTO<Long, String>> getTiposEntrega() {
		return tiposEntrega;
	}

	/**
	 * @param tiposEntrega the tiposEntrega to set
	 */
	public void setTiposEntrega(List<ItemDTO<Long, String>> tiposEntrega) {
		this.tiposEntrega = tiposEntrega;
	}

	/**
	 * @return the repPorPontoVenda
	 */
	public Boolean getRepPorPontoVenda() {
		return repPorPontoVenda;
	}

	/**
	 * @param repPorPontoVenda the repPorPontoVenda to set
	 */
	public void setRepPorPontoVenda(Boolean repPorPontoVenda) {
		this.repPorPontoVenda = repPorPontoVenda;
	}

	/**
	 * @return the solNumAtras
	 */
	public Boolean getSolNumAtras() {
		return solNumAtras;
	}

	/**
	 * @param solNumAtras the solNumAtras to set
	 */
	public void setSolNumAtras(Boolean solNumAtras) {
		this.solNumAtras = solNumAtras;
	}

	/**
	 * @return the qtdeAutomatica
	 */
	public Boolean getQtdeAutomatica() {
		return qtdeAutomatica;
	}

	/**
	 * @param qtdeAutomatica the qtdeAutomatica to set
	 */
	public void setQtdeAutomatica(Boolean qtdeAutomatica) {
		this.qtdeAutomatica = qtdeAutomatica;
	}
	
	
}
