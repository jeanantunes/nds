package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0137Input extends IntegracaoDocumentMaster<EMS0137InputItem> implements Serializable {

	/**
	 * reevio
	 */
	private static final long serialVersionUID = 1L;
	
	private CEPKInput cePK;
	
	private String codigoDistribuidor;
	
	private String tipoDocumento;
	
	private String baseDeDados;
	
	private String usuarioBaseDeDados;
	
	private Date dataEmissao;
	
	private Integer dataAnoReferencia;
	
	private String tipoStatus;
	
	private Integer codigoTipoChamadaEncalhe;
	
	private Long codigoNaturezaOperacao;
	
	private Integer numeroSemanaReferencia;
	
	private Long numeroControle;
	
	private Date dataLimiteRecebimento;
	
	private BigDecimal valorTotalVendaApurada;	
	
	private BigDecimal valorTotalCreditoApurado;
	
	private String codigoPreenchimento;
	
	private BigDecimal valorTotalVendaInformada;
	
	private BigDecimal valorTotalCreditoInformado;
	
	private BigDecimal valorTotalMargemInformado;
	
	private BigDecimal valorTotalMargemApurado;
	
	private BigDecimal valorNotaValoresDiversos;
	
	private String codigoStatusTransmissao;
	
	private String indiceFechamentoParcial;
	
	private String indiceCEProvisoria;
	
	private List<EMS0137InputItem> chamadaEncalheItens = new ArrayList<EMS0137InputItem>();

	public CEPKInput getCePK() {
		return cePK;
	}

	public void setCePK(CEPKInput cePK) {
		this.cePK = cePK;
	}

	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getBaseDeDados() {
		return baseDeDados;
	}

	public void setBaseDeDados(String baseDeDados) {
		this.baseDeDados = baseDeDados;
	}

	public String getUsuarioBaseDeDados() {
		return usuarioBaseDeDados;
	}

	public void setUsuarioBaseDeDados(String usuarioBaseDeDados) {
		this.usuarioBaseDeDados = usuarioBaseDeDados;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Integer getDataAnoReferencia() {
		return dataAnoReferencia;
	}

	public void setDataAnoReferencia(Integer dataAnoReferencia) {
		this.dataAnoReferencia = dataAnoReferencia;
	}

	public String getTipoStatus() {
		return tipoStatus;
	}

	public void setTipoStatus(String tipoStatus) {
		this.tipoStatus = tipoStatus;
	}

	public Integer getCodigoTipoChamadaEncalhe() {
		return codigoTipoChamadaEncalhe;
	}

	public void setCodigoTipoChamadaEncalhe(Integer codigoTipoChamadaEncalhe) {
		this.codigoTipoChamadaEncalhe = codigoTipoChamadaEncalhe;
	}

	public Long getCodigoNaturezaOperacao() {
		return codigoNaturezaOperacao;
	}

	public void setCodigoNaturezaOperacao(Long codigoNaturezaOperacao) {
		this.codigoNaturezaOperacao = codigoNaturezaOperacao;
	}

	public Integer getNumeroSemanaReferencia() {
		return numeroSemanaReferencia;
	}

	public void setNumeroSemanaReferencia(Integer nuemroSemanaReferencia) {
		this.numeroSemanaReferencia = nuemroSemanaReferencia;
	}

	public Long getNumeroControle() {
		return numeroControle;
	}

	public void setNumeroControle(Long numeroControle) {
		this.numeroControle = numeroControle;
	}

	public Date getDataLimiteRecebimento() {
		return dataLimiteRecebimento;
	}

	public void setDataLimiteRecebimento(Date dataLimiteRecebimento) {
		this.dataLimiteRecebimento = dataLimiteRecebimento;
	}

	public BigDecimal getValorTotalVendaApurada() {
		return valorTotalVendaApurada;
	}

	public void setValorTotalVendaApurada(BigDecimal valorTotalVendaApurada) {
		this.valorTotalVendaApurada = valorTotalVendaApurada;
	}

	public BigDecimal getValorTotalCreditoApurado() {
		return valorTotalCreditoApurado;
	}

	public void setValorTotalCreditoApurado(BigDecimal valorTotalCreditoApurado) {
		this.valorTotalCreditoApurado = valorTotalCreditoApurado;
	}

	public String getCodigoPreenchimento() {
		return codigoPreenchimento;
	}

	public void setCodigoPreenchimento(String codigoPreenchimento) {
		this.codigoPreenchimento = codigoPreenchimento;
	}

	public BigDecimal getValorTotalVendaInformada() {
		return valorTotalVendaInformada;
	}

	public void setValorTotalVendaInformada(BigDecimal valorTotalVendaInformada) {
		this.valorTotalVendaInformada = valorTotalVendaInformada;
	}

	public BigDecimal getValorTotalCreditoInformado() {
		return valorTotalCreditoInformado;
	}

	public void setValorTotalCreditoInformado(BigDecimal valorTotalCreditoInformado) {
		this.valorTotalCreditoInformado = valorTotalCreditoInformado;
	}

	public BigDecimal getValorTotalMargemInformado() {
		return valorTotalMargemInformado;
	}

	public void setValorTotalMargemInformado(BigDecimal valorTotalMargemInformado) {
		this.valorTotalMargemInformado = valorTotalMargemInformado;
	}

	public BigDecimal getValorTotalMargemApurado() {
		return valorTotalMargemApurado;
	}

	public void setValorTotalMargemApurado(BigDecimal valorTotalMargemApurado) {
		this.valorTotalMargemApurado = valorTotalMargemApurado;
	}

	public BigDecimal getValorNotaValoresDiversos() {
		return valorNotaValoresDiversos;
	}

	public void setValorNotaValoresDiversos(BigDecimal valorNotaValoresDiversos) {
		this.valorNotaValoresDiversos = valorNotaValoresDiversos;
	}

	public String getCodigoStatusTransmissao() {
		return codigoStatusTransmissao;
	}

	public void setCodigoStatusTransmissao(String codigoStatusTransmissao) {
		this.codigoStatusTransmissao = codigoStatusTransmissao;
	}

	public String getIndiceFechamentoParcial() {
		return indiceFechamentoParcial;
	}

	public void setIndiceFechamentoParcial(String indiceFechamentoParcial) {
		this.indiceFechamentoParcial = indiceFechamentoParcial;
	}

	public String getIndiceCEProvisoria() {
		return indiceCEProvisoria;
	}

	public void setIndiceCEProvisoria(String indiceCEProvisoria) {
		this.indiceCEProvisoria = indiceCEProvisoria;
	}

	public List<EMS0137InputItem> getChamadaEncalheItens() {
		return chamadaEncalheItens;
	}
	
	public void setChamadaEncalheItens(List<EMS0137InputItem> itens) {
		this.chamadaEncalheItens = itens;
	}

	public List<EMS0137InputItem> getItens() {
		return chamadaEncalheItens;
	}

	public void setItens(List<EMS0137InputItem> itens) {
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
	
	public class CEPKInput {
		
		private Long numeroChamadaEncalhe;
		
		public Long getNumeroChamadaEncalhe() {
			return numeroChamadaEncalhe;
		}

		public void setNumeroChamadaEncalhe(Long numeroChamadaEncalhe) {
			this.numeroChamadaEncalhe = numeroChamadaEncalhe;
		}
		
	}
	
}