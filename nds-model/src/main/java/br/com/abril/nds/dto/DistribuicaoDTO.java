package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;

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
	private DescricaoTipoEntrega descricaoTipoEntrega;
	private String observacao;
	private Boolean repPorPontoVenda;
	private Boolean solNumAtras;
	private Boolean recebeRecolhe;
	private Boolean neImpresso;
	private Boolean neEmail;
	private Boolean ceImpresso;
	private Boolean ceEmail;
	private Boolean slipImpresso;
	private Boolean slipEmail;
	private Boolean boletoImpresso;
	private Boolean boletoEmail;
	private Boolean boletoSlipImpresso;
	private Boolean boletoSlipEmail;
	private Boolean reciboImpresso;
	private Boolean reciboEmail;
	private String gerenteComercial;
	private Boolean utilizaProcuracao;
	private Boolean procuracaoRecebida;
	private BigDecimal taxaFixa;
	private BigDecimal percentualFaturamento;
	private String inicioPeriodoCarencia;
	private String fimPeriodoCarencia;
	private ArquivoDTO termoAdesao;
	
	private List<ItemDTO<DescricaoTipoEntrega, String>> tiposEntrega;
	
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
	 * @return the descricaoTipoEntrega
	 */
	public DescricaoTipoEntrega getDescricaoTipoEntrega() {
		return descricaoTipoEntrega;
	}

	/**
	 * @param descricaoTipoEntrega the descricaoTipoEntrega to set
	 */
	public void setDescricaoTipoEntrega(DescricaoTipoEntrega descricaoTipoEntrega) {
		this.descricaoTipoEntrega = descricaoTipoEntrega;
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
	public List<ItemDTO<DescricaoTipoEntrega, String>> getTiposEntrega() {
		return tiposEntrega;
	}

	/**
	 * @param tiposEntrega the tiposEntrega to set
	 */
	public void setTiposEntrega(List<ItemDTO<DescricaoTipoEntrega, String>> tiposEntrega) {
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

	public String getGerenteComercial() {
		return gerenteComercial;
	}

	public void setGerenteComercial(String gerenteComercial) {
		this.gerenteComercial = gerenteComercial;
	}

	public Boolean getBoletoImpresso() {
		return boletoImpresso;
	}

	public void setBoletoImpresso(Boolean boletoImpresso) {
		this.boletoImpresso = boletoImpresso;
	}

	public Boolean getBoletoEmail() {
		return boletoEmail;
	}

	public void setBoletoEmail(Boolean boletoEmail) {
		this.boletoEmail = boletoEmail;
	}

	public Boolean getBoletoSlipImpresso() {
		return boletoSlipImpresso;
	}

	public void setBoletoSlipImpresso(Boolean boletoSlipImpresso) {
		this.boletoSlipImpresso = boletoSlipImpresso;
	}

	public Boolean getBoletoSlipEmail() {
		return boletoSlipEmail;
	}

	public void setBoletoSlipEmail(Boolean boletoSlipEmail) {
		this.boletoSlipEmail = boletoSlipEmail;
	}

	public Boolean getReciboImpresso() {
		return reciboImpresso;
	}

	public void setReciboImpresso(Boolean reciboImpresso) {
		this.reciboImpresso = reciboImpresso;
	}

	public Boolean getReciboEmail() {
		return reciboEmail;
	}

	public void setReciboEmail(Boolean reciboEmail) {
		this.reciboEmail = reciboEmail;
	}	

	/**
	 * @return the utilizaProcuracao
	 */
	public Boolean getUtilizaProcuracao() {
		return utilizaProcuracao;
	}

	/**
	 * @param utilizaProcuracao the utilizaProcuracao to set
	 */
	public void setUtilizaProcuracao(Boolean utilizaProcuracao) {
		this.utilizaProcuracao = utilizaProcuracao;
	}

	/**
	 * @return the procuracaoRecebida
	 */
	public Boolean getProcuracaoRecebida() {
		return procuracaoRecebida;
	}

	/**
	 * @param procuracaoRecebida the procuracaoRecebida to set
	 */
	public void setProcuracaoRecebida(Boolean procuracaoRecebida) {
		this.procuracaoRecebida = procuracaoRecebida;
	}

	/**
	 * @return the taxaFixa
	 */
	public BigDecimal getTaxaFixa() {
		return taxaFixa;
	}

	/**
	 * @param taxaFixa the taxaFixa to set
	 */
	public void setTaxaFixa(BigDecimal taxaFixa) {
		this.taxaFixa = taxaFixa;
	}

	/**
	 * @return the percentualFaturamento
	 */
	public BigDecimal getPercentualFaturamento() {
		return percentualFaturamento;
	}

	/**
	 * @param percentualFaturamento the percentualFaturamento to set
	 */
	public void setPercentualFaturamento(BigDecimal percentualFaturamento) {
		this.percentualFaturamento = percentualFaturamento;
	}

	/**
	 * @return the inicioPeriodoCarencia
	 */
	public String getInicioPeriodoCarencia() {
		return inicioPeriodoCarencia;
	}

	/**
	 * @param inicioPeriodoCarencia the inicioPeriodoCarencia to set
	 */
	public void setInicioPeriodoCarencia(String inicioPeriodoCarencia) {
		this.inicioPeriodoCarencia = inicioPeriodoCarencia;
	}

	/**
	 * @return the fimPeriodoCarencia
	 */
	public String getFimPeriodoCarencia() {
		return fimPeriodoCarencia;
	}

	/**
	 * @param fimPeriodoCarencia the fimPeriodoCarencia to set
	 */
	public void setFimPeriodoCarencia(String fimPeriodoCarencia) {
		this.fimPeriodoCarencia = fimPeriodoCarencia;
	}
	

	/**
	 * @return the termoAdesao
	 */
	public ArquivoDTO getTermoAdesao() {
		return termoAdesao;
	}

	/**
	 * @param termoAdesao the termoAdesao to set
	 */
	public void setTermoAdesao(ArquivoDTO termoAdesao) {
		this.termoAdesao = termoAdesao;
	}
}
