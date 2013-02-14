package br.com.abril.nds.model.integracao.icd;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.abril.nds.model.integracao.icd.pks.CEPK;

@Entity
@Table(name = "CHAMADA_ENCALHE")
public class ChamadaEncalheIcd {
	
/*	COD_DISTRIBUIDOR
	NUM_CHAMADA_ENCALHE_CHEN
	para a chamada_encalhe
	NUM_DOCUMENTO_DCEN
	NUM_CHAMADA_ENCALHE_CHEN
	NUM_ITEM_ITCE
	para a item
*/
	
	@EmbeddedId
	private CEPK cePK;
	
	@Column(name = "COD_DISTRIBUIDOR", nullable = false)
	private Long codigoDistribuidor;
	
	@Column(name = "DATA_EMISSAO_CHEN")
	private Date dataEmissao;
	
	@Column(name = "DATA_ANO_REFERENCIA_CHEN")
	private Long dataAnoReferencia;
	
	@Column(name = "TIPO_STATUS_CHEN")
	private String tipoStatus;
	
	@Column(name = "COD_TIPO_CHAMADA_ENC_TPCE")
	private Long codigoTipoChamadaEncalhe;
	
	@Column(name = "COD_NATUREZA_OPERACAO_NTOP")
	private Long codigoNaturezaOperacao;
	
	@Column(name = "NUM_SEMANA_REFERENCIA_CHEN")
	private Long nuemroSemanaReferencia;
	
	@Column(name = "NUM_CONTROLE_CHEN")
	private Long nuemroControle;
	
	@OneToMany
	List<ChamadaEncalheIcdItem> chamadaEncalheItens;

	/**
	 * Getters e Setters 
	 */
	public CEPK getCePK() {
		return cePK;
	}

	public void setCePK(CEPK cePK) {
		this.cePK = cePK;
	}
	
	public Long getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(Long codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Long getDataAnoReferencia() {
		return dataAnoReferencia;
	}

	public void setDataAnoReferencia(Long dataAnoReferencia) {
		this.dataAnoReferencia = dataAnoReferencia;
	}
	
	public String getTipoStatus() {
		return tipoStatus;
	}

	public void setTipoStatus(String tipoStatus) {
		this.tipoStatus = tipoStatus;
	}

	public Long getCodigoTipoChamadaEncalhe() {
		return codigoTipoChamadaEncalhe;
	}

	public void setCodigoTipoChamadaEncalhe(Long codigoTipoChamadaEncalhe) {
		this.codigoTipoChamadaEncalhe = codigoTipoChamadaEncalhe;
	}

	public Long getCodigoNaturezaOperacao() {
		return codigoNaturezaOperacao;
	}

	public void setCodigoNaturezaOperacao(Long codigoNaturezaOperacao) {
		this.codigoNaturezaOperacao = codigoNaturezaOperacao;
	}

	public Long getNuemroSemanaReferencia() {
		return nuemroSemanaReferencia;
	}

	public void setNuemroSemanaReferencia(Long nuemroSemanaReferencia) {
		this.nuemroSemanaReferencia = nuemroSemanaReferencia;
	}

	public Long getNuemroControle() {
		return nuemroControle;
	}

	public void setNuemroControle(Long nuemroControle) {
		this.nuemroControle = nuemroControle;
	}

	public List<ChamadaEncalheIcdItem> getChamadaEncalheItens() {
		return chamadaEncalheItens;
	}

	public void setChamadaEncalheItens(List<ChamadaEncalheIcdItem> chamadaEncalheItens) {
		this.chamadaEncalheItens = chamadaEncalheItens;
	}
	
}
