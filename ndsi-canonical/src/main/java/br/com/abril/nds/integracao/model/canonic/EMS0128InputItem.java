package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatBoolean;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0128InputItem extends IntegracaoDocumentDetail implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int numSequenciaDetalhe;
	private int tipoAcerto;
	private String codigoProduto;
	private Long numeroEdicao;
	private BigInteger qtd;
	private BigDecimal precoCapa;
	private Float percentualDesconto;
	private String situacaoAcerto;
	/**
	 * @return the numSequenciaDetalhe
	 */
	public int getNumSequenciaDetalhe() {
		return numSequenciaDetalhe;
	}
	/**
	 * @param numSequenciaDetalhe the numSequenciaDetalhe to set
	 */
	public void setNumSequenciaDetalhe(int numSequenciaDetalhe) {
		this.numSequenciaDetalhe = numSequenciaDetalhe;
	}
	/**
	 * @return the tipoAcerto
	 */
	public int getTipoAcerto() {
		return tipoAcerto;
	}
	/**
	 * @param tipoAcerto the tipoAcerto to set
	 */
	public void setTipoAcerto(int tipoAcerto) {
		this.tipoAcerto = tipoAcerto;
	}
	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}
	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	/**
	 * @return the numeroEdicao
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	/**
	 * @return the qtd
	 */
	public BigInteger getQtd() {
		return qtd;
	}
	/**
	 * @param qtd the qtd to set
	 */
	public void setQtd(BigInteger qtd) {
		this.qtd = qtd;
	}
	/**
	 * @return the precoCapa
	 */
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	/**
	 * @param precoCapa the precoCapa to set
	 */
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}
	/**
	 * @return the percentualDesconto
	 */
	public Float getPercentualDesconto() {
		return percentualDesconto;
	}
	/**
	 * @param percentualDesconto the percentualDesconto to set
	 */
	public void setPercentualDesconto(Float percentualDesconto) {
		this.percentualDesconto = percentualDesconto;
	}
	/**
	 * @return the situacaoAcerto
	 */
	public String getSituacaoAcerto() {
		return situacaoAcerto;
	}
	/**
	 * @param situacaoAcerto the situacaoAcerto to set
	 */
	public void setSituacaoAcerto(String situacaoAcerto) {
		this.situacaoAcerto = situacaoAcerto;
	}

		
}