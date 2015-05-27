package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "NOTA_FISCAL_TRIBUTACAO")
public class NotaFiscalTributacao implements Serializable {
	
	
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1770966775042385935L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "COD_EMPRESA", length = 9)
	private String codigoEmpresa;
	
	@Column(name = "TIP_OPERACAO", length = 1)
	private String tipoOperacao;
	
	@Column(name = "UF_ORIGEM", length = 2)
	private String ufOrigem;
	
	@Column(name = "UF_DESTINO", length = 2)
	private String ufDestino;
	
	@Column(name = "NAT_OPERACAO")
	private Integer naturezaOperacao;
	
	@Column(name = "COD_NOP", length = 20)
	private String codigoNaturezaOperacao;
	
	@Column(name = "COD_NBM", length = 10)
	private String codigoNBM;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_VIGENCIA")
	private Date dataVigencia;
	
	@Column(name = "TRIBUTACAO_ICMS", length = 1)
	private String tributacaoICMS;
	
	@Column(name = "CST_ICMS", length = 3)
	private String cstICMS;
	
	@Column(name = "FLG_BSC_ICMS", length = 1)
	private String indicadorBaseCalculoICMS;
	
	@Column(name = "ALIQUOTA_ICMS", precision=18, scale=4)
	private BigDecimal aliquotaICMS;
	
	@Column(name = "FLG_VLR_ICMS", length = 1)
	private String indicadorValorICMS;
	
	@Column(name = "FLG_VLR_ISE_ICMS", length = 1)
	private String indicadorValorIsentoICMS;
	
	@Column(name = "FLG_VLR_OUT_ICMS", length = 1)
	private String indicadorValorOutrosICMS;
	
	@Column(name = "TIP_BSC_ICMS", length = 1)
	private String tipoBaseCalculoICMS;
	
	@Column(name = "TRIBUTACAO_IPI", length = 1)
	private String tributacaoIPI;
	
	@Column(name = "CST_IPI", length = 3)
	private String cstIPI;
	
	@Column(name = "FLG_BSC_IPI", length = 1)
	private String indicadorBaseCalculoIPI;
	
	@Column(name = "ALIQUOTA_IPI", precision=18, scale=4)
	private BigDecimal aliquotaIPI;
	
	@Column(name = "FLG_VLR_IPI", length = 1)
	private String indicadorValorIPI;
	
	@Column(name = "FLG_VLR_ISE_IPI", length = 1)
	private String  indicadorValorIsentoIPI;
	
	@Column(name = "FLG_VLR_OUT_IPI", length = 1)
	private String indicadorValorOutrosIPI;
	
	@Column(name = "TRIBUTACAO_PIS", length = 1)
	private String tributacaoPIS;
	
	@Column(name = "CST_PIS", length = 3)
	private String cstPIS;
	
	@Column(name = "FLG_BSC_DEB_PIS", length = 1)
	private String indicadorBaseCalculoDebitoPIS;
	
	@Column(name = "FLG_BSC_CRE_PIS", length = 1)
	private String indicadorBaseCalculoCreditoPIS;
	
	@Column(name = "ALIQUOTA_PIS", precision=18, scale=4)
	private BigDecimal aliquotaPIS;
	
	@Column(name = "FLG_VLR_DEB_PIS", length = 1)
	private String indicadorValorDebitoPIS;
	
	@Column(name = "FLG_VLR_CRE_PIS", length = 1)
	private String indicadorValorCreditoPIS;
	
	@Column(name = "TRIBUTACAO_COFINS", length = 1)
	private String tributacaoCOFINS;
	
	@Column(name = "CST_COFINS", length = 2)
	private String cstCOFINS;
	
	@Column(name = "FLG_BSC_DEB_COFINS", length = 1)
	private String indicadorBaseCalculoDebitoCOFINS;
	
	@Column(name = "FLG_BSC_CRE_COFINS", length = 1)
	private String indicadorBaseCalculoCreditoCOFINS;
	
	@Column(name = "ALIQUOTA_COFINS", precision=18, scale=4)
	private BigDecimal aliquotaCOFINS;
	
	@Column(name = "FLG_VLR_DEB_COF", length = 1)
	private String indicadorValorDebitoCOFINS;
	
	@Column(name = "FLG_VLR_CRE_COF", length = 1)
	private String indicadorValorCreditoCOFINS;

	/**
	 * Construtor padrão.
	 */
	public NotaFiscalTributacao() {
		
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the codigoEmpresa
	 */
	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	/**
	 * @param codigoEmpresa the codigoEmpresa to set
	 */
	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	/**
	 * @return the tipoOperacao
	 */
	public String getTipoOperacao() {
		return tipoOperacao;
	}

	/**
	 * @param tipoOperacao the tipoOperacao to set
	 */
	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	/**
	 * @return the ufOrigem
	 */
	public String getUfOrigem() {
		return ufOrigem;
	}

	/**
	 * @param ufOrigem the ufOrigem to set
	 */
	public void setUfOrigem(String ufOrigem) {
		this.ufOrigem = ufOrigem;
	}

	/**
	 * @return the ufDestino
	 */
	public String getUfDestino() {
		return ufDestino;
	}

	/**
	 * @param ufDestino the ufDestino to set
	 */
	public void setUfDestino(String ufDestino) {
		this.ufDestino = ufDestino;
	}

	/**
	 * @return the naturezaOperacao
	 */
	public Integer getNaturezaOperacao() {
		return naturezaOperacao;
	}

	/**
	 * @param naturezaOperacao the naturezaOperacao to set
	 */
	public void setNaturezaOperacao(Integer naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}

	/**
	 * @return the codigoNaturezaOperacao
	 */
	public String getCodigoNaturezaOperacao() {
		return codigoNaturezaOperacao;
	}

	/**
	 * @param codigoNaturezaOperacao the codigoNaturezaOperacao to set
	 */
	public void setCodigoNaturezaOperacao(String codigoNaturezaOperacao) {
		this.codigoNaturezaOperacao = codigoNaturezaOperacao;
	}

	/**
	 * @return the codigoNBM
	 */
	public String getCodigoNBM() {
		return codigoNBM;
	}

	/**
	 * @param codigoNBM the codigoNBM to set
	 */
	public void setCodigoNBM(String codigoNBM) {
		this.codigoNBM = codigoNBM;
	}

	/**
	 * @return the dataVigencia
	 */
	public Date getDataVigencia() {
		return dataVigencia;
	}

	/**
	 * @param dataVigencia the dataVigencia to set
	 */
	public void setDataVigencia(Date dataVigencia) {
		this.dataVigencia = dataVigencia;
	}

	/**
	 * @return the tributacaoICMS
	 */
	public String getTributacaoICMS() {
		return tributacaoICMS;
	}

	/**
	 * @param tributacaoICMS the tributacaoICMS to set
	 */
	public void setTributacaoICMS(String tributacaoICMS) {
		this.tributacaoICMS = tributacaoICMS;
	}

	/**
	 * @return the cstICMS
	 */
	public String getCstICMS() {
		return cstICMS;
	}

	/**
	 * @param cstICMS the cstICMS to set
	 */
	public void setCstICMS(String cstICMS) {
		this.cstICMS = cstICMS;
	}

	/**
	 * @return the indicadorBaseCalculoICMS
	 */
	public String getIndicadorBaseCalculoICMS() {
		return indicadorBaseCalculoICMS;
	}

	/**
	 * @param indicadorBaseCalculoICMS the indicadorBaseCalculoICMS to set
	 */
	public void setIndicadorBaseCalculoICMS(String indicadorBaseCalculoICMS) {
		this.indicadorBaseCalculoICMS = indicadorBaseCalculoICMS;
	}

	/**
	 * @return the aliquotaICMS
	 */
	public BigDecimal getAliquotaICMS() {
		return aliquotaICMS;
	}

	/**
	 * @param aliquotaICMS the aliquotaICMS to set
	 */
	public void setAliquotaICMS(BigDecimal aliquotaICMS) {
		this.aliquotaICMS = aliquotaICMS;
	}

	/**
	 * @return the indicadorValorICMS
	 */
	public String getIndicadorValorICMS() {
		return indicadorValorICMS;
	}

	/**
	 * @param indicadorValorICMS the indicadorValorICMS to set
	 */
	public void setIndicadorValorICMS(String indicadorValorICMS) {
		this.indicadorValorICMS = indicadorValorICMS;
	}

	/**
	 * @return the indicadorValorIsentoICMS
	 */
	public String getIndicadorValorIsentoICMS() {
		return indicadorValorIsentoICMS;
	}

	/**
	 * @param indicadorValorIsentoICMS the indicadorValorIsentoICMS to set
	 */
	public void setIndicadorValorIsentoICMS(String indicadorValorIsentoICMS) {
		this.indicadorValorIsentoICMS = indicadorValorIsentoICMS;
	}

	/**
	 * @return the indicadorValorOutrosICMS
	 */
	public String getIndicadorValorOutrosICMS() {
		return indicadorValorOutrosICMS;
	}

	/**
	 * @param indicadorValorOutrosICMS the indicadorValorOutrosICMS to set
	 */
	public void setIndicadorValorOutrosICMS(String indicadorValorOutrosICMS) {
		this.indicadorValorOutrosICMS = indicadorValorOutrosICMS;
	}

	/**
	 * @return the tipoBaseCalculoICMS
	 */
	public String getTipoBaseCalculoICMS() {
		return tipoBaseCalculoICMS;
	}

	/**
	 * @param tipoBaseCalculoICMS the tipoBaseCalculoICMS to set
	 */
	public void setTipoBaseCalculoICMS(String tipoBaseCalculoICMS) {
		this.tipoBaseCalculoICMS = tipoBaseCalculoICMS;
	}

	/**
	 * @return the tributacaoIPI
	 */
	public String getTributacaoIPI() {
		return tributacaoIPI;
	}

	/**
	 * @param tributacaoIPI the tributacaoIPI to set
	 */
	public void setTributacaoIPI(String tributacaoIPI) {
		this.tributacaoIPI = tributacaoIPI;
	}

	/**
	 * @return the cstIPI
	 */
	public String getCstIPI() {
		return cstIPI;
	}

	/**
	 * @param cstIPI the cstIPI to set
	 */
	public void setCstIPI(String cstIPI) {
		this.cstIPI = cstIPI;
	}

	/**
	 * @return the indicadorBaseCalculoIPI
	 */
	public String getIndicadorBaseCalculoIPI() {
		return indicadorBaseCalculoIPI;
	}

	/**
	 * @param indicadorBaseCalculoIPI the indicadorBaseCalculoIPI to set
	 */
	public void setIndicadorBaseCalculoIPI(String indicadorBaseCalculoIPI) {
		this.indicadorBaseCalculoIPI = indicadorBaseCalculoIPI;
	}

	/**
	 * @return the aliquotaIPI
	 */
	public BigDecimal getAliquotaIPI() {
		return aliquotaIPI;
	}

	/**
	 * @param aliquotaIPI the aliquotaIPI to set
	 */
	public void setAliquotaIPI(BigDecimal aliquotaIPI) {
		this.aliquotaIPI = aliquotaIPI;
	}

	/**
	 * @return the indicadorValorIPI
	 */
	public String getIndicadorValorIPI() {
		return indicadorValorIPI;
	}

	/**
	 * @param indicadorValorIPI the indicadorValorIPI to set
	 */
	public void setIndicadorValorIPI(String indicadorValorIPI) {
		this.indicadorValorIPI = indicadorValorIPI;
	}

	/**
	 * @return the indicadorValorIsentoIPI
	 */
	public String getIndicadorValorIsentoIPI() {
		return indicadorValorIsentoIPI;
	}

	/**
	 * @param indicadorValorIsentoIPI the indicadorValorIsentoIPI to set
	 */
	public void setIndicadorValorIsentoIPI(String indicadorValorIsentoIPI) {
		this.indicadorValorIsentoIPI = indicadorValorIsentoIPI;
	}

	/**
	 * @return the indicadorValorOutrosIPI
	 */
	public String getIndicadorValorOutrosIPI() {
		return indicadorValorOutrosIPI;
	}

	/**
	 * @param indicadorValorOutrosIPI the indicadorValorOutrosIPI to set
	 */
	public void setIndicadorValorOutrosIPI(String indicadorValorOutrosIPI) {
		this.indicadorValorOutrosIPI = indicadorValorOutrosIPI;
	}

	/**
	 * @return the tributacaoPIS
	 */
	public String getTributacaoPIS() {
		return tributacaoPIS;
	}

	/**
	 * @param tributacaoPIS the tributacaoPIS to set
	 */
	public void setTributacaoPIS(String tributacaoPIS) {
		this.tributacaoPIS = tributacaoPIS;
	}

	/**
	 * @return the cstPIS
	 */
	public String getCstPIS() {
		return cstPIS;
	}

	/**
	 * @param cstPIS the cstPIS to set
	 */
	public void setCstPIS(String cstPIS) {
		this.cstPIS = cstPIS;
	}

	/**
	 * @return the indicadorBaseCalculoDebitoPIS
	 */
	public String getIndicadorBaseCalculoDebitoPIS() {
		return indicadorBaseCalculoDebitoPIS;
	}

	/**
	 * @param indicadorBaseCalculoDebitoPIS the indicadorBaseCalculoDebitoPIS to set
	 */
	public void setIndicadorBaseCalculoDebitoPIS(
			String indicadorBaseCalculoDebitoPIS) {
		this.indicadorBaseCalculoDebitoPIS = indicadorBaseCalculoDebitoPIS;
	}

	/**
	 * @return the indicadorBaseCalculoCreditoPIS
	 */
	public String getIndicadorBaseCalculoCreditoPIS() {
		return indicadorBaseCalculoCreditoPIS;
	}

	/**
	 * @param indicadorBaseCalculoCreditoPIS the indicadorBaseCalculoCreditoPIS to set
	 */
	public void setIndicadorBaseCalculoCreditoPIS(
			String indicadorBaseCalculoCreditoPIS) {
		this.indicadorBaseCalculoCreditoPIS = indicadorBaseCalculoCreditoPIS;
	}

	/**
	 * @return the aliquotaPIS
	 */
	public BigDecimal getAliquotaPIS() {
		return aliquotaPIS;
	}

	/**
	 * @param aliquotaPIS the aliquotaPIS to set
	 */
	public void setAliquotaPIS(BigDecimal aliquotaPIS) {
		this.aliquotaPIS = aliquotaPIS;
	}

	/**
	 * @return the indicadorValorDebitoPIS
	 */
	public String getIndicadorValorDebitoPIS() {
		return indicadorValorDebitoPIS;
	}

	/**
	 * @param indicadorValorDebitoPIS the indicadorValorDebitoPIS to set
	 */
	public void setIndicadorValorDebitoPIS(String indicadorValorDebitoPIS) {
		this.indicadorValorDebitoPIS = indicadorValorDebitoPIS;
	}

	/**
	 * @return the indicadorValorCreditoPIS
	 */
	public String getIndicadorValorCreditoPIS() {
		return indicadorValorCreditoPIS;
	}

	/**
	 * @param indicadorValorCreditoPIS the indicadorValorCreditoPIS to set
	 */
	public void setIndicadorValorCreditoPIS(String indicadorValorCreditoPIS) {
		this.indicadorValorCreditoPIS = indicadorValorCreditoPIS;
	}

	/**
	 * @return the tributacaoCOFINS
	 */
	public String getTributacaoCOFINS() {
		return tributacaoCOFINS;
	}

	/**
	 * @param tributacaoCOFINS the tributacaoCOFINS to set
	 */
	public void setTributacaoCOFINS(String tributacaoCOFINS) {
		this.tributacaoCOFINS = tributacaoCOFINS;
	}

	/**
	 * @return the cstCOFINS
	 */
	public String getCstCOFINS() {
		return cstCOFINS;
	}

	/**
	 * @param cstCOFINS the cstCOFINS to set
	 */
	public void setCstCOFINS(String cstCOFINS) {
		this.cstCOFINS = cstCOFINS;
	}

	/**
	 * @return the indicadorBaseCalculoDebitoCOFINS
	 */
	public String getIndicadorBaseCalculoDebitoCOFINS() {
		return indicadorBaseCalculoDebitoCOFINS;
	}

	/**
	 * @param indicadorBaseCalculoDebitoCOFINS the indicadorBaseCalculoDebitoCOFINS to set
	 */
	public void setIndicadorBaseCalculoDebitoCOFINS(
			String indicadorBaseCalculoDebitoCOFINS) {
		this.indicadorBaseCalculoDebitoCOFINS = indicadorBaseCalculoDebitoCOFINS;
	}

	/**
	 * @return the indicadorBaseCalculoCreditoCOFINS
	 */
	public String getIndicadorBaseCalculoCreditoCOFINS() {
		return indicadorBaseCalculoCreditoCOFINS;
	}

	/**
	 * @param indicadorBaseCalculoCreditoCOFINS the indicadorBaseCalculoCreditoCOFINS to set
	 */
	public void setIndicadorBaseCalculoCreditoCOFINS(
			String indicadorBaseCalculoCreditoCOFINS) {
		this.indicadorBaseCalculoCreditoCOFINS = indicadorBaseCalculoCreditoCOFINS;
	}

	/**
	 * @return the aliquotaCOFINS
	 */
	public BigDecimal getAliquotaCOFINS() {
		return aliquotaCOFINS;
	}

	/**
	 * @param aliquotaCOFINS the aliquotaCOFINS to set
	 */
	public void setAliquotaCOFINS(BigDecimal aliquotaCOFINS) {
		this.aliquotaCOFINS = aliquotaCOFINS;
	}

	/**
	 * @return the indicadorValorDebitoCOFINS
	 */
	public String getIndicadorValorDebitoCOFINS() {
		return indicadorValorDebitoCOFINS;
	}

	/**
	 * @param indicadorValorDebitoCOFINS the indicadorValorDebitoCOFINS to set
	 */
	public void setIndicadorValorDebitoCOFINS(String indicadorValorDebitoCOFINS) {
		this.indicadorValorDebitoCOFINS = indicadorValorDebitoCOFINS;
	}

	/**
	 * @return the indicadorValorCreditoCOFINS
	 */
	public String getIndicadorValorCreditoCOFINS() {
		return indicadorValorCreditoCOFINS;
	}

	/**
	 * @param indicadorValorCreditoCOFINS the indicadorValorCreditoCOFINS to set
	 */
	public void setIndicadorValorCreditoCOFINS(String indicadorValorCreditoCOFINS) {
		this.indicadorValorCreditoCOFINS = indicadorValorCreditoCOFINS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotaFiscalTributacao other = (NotaFiscalTributacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
