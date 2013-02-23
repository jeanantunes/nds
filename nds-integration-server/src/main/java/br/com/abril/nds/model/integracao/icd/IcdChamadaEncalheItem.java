package br.com.abril.nds.model.integracao.icd;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.integracao.icd.pks.CEItemPK;

@Entity
@Table(name = "ITEM_CHAMADA_ENCALHE")
public class IcdChamadaEncalheItem {

	@EmbeddedId
	private CEItemPK ceItemPK;
	
	@Column(name = "DATA_RECOLHIMENTO_ITCE")
	private Date dataRecolhimento;
	
	@Column(name = "NUM_DOCUMENTO_DCEN")
	private Long numeroDocumento;
	
	@JoinColumn(name = "COD_LANCTO_EDICAO")
	@OneToOne
	private IcdLancamentoEdicaoPublicacao lancamentoEdicaoPublicacao;

	@Column(name = "COD_VALE_DESCONTO_VLDS")
	private Long codigoValeDesconto;

	@Column(name = "QTDE_ENVIADA_ITCE")
	private Long quantidadeEnviada;

	@Column(name = "COD_FORMA_DEVOLUCAO_CEE")
	private Long codigoFormaDevolucao;
	
	@Column(name = "NUM_CONTROLE_ITCE")
	private Long numeroControle;
	
	@Column(name = "COD_REGIME_RECOLHIMENTO")
	private String codigoRegimeRecolhimento;
	
	@Column(name = "VLR_PRECO_UNIT_ITCE")
	private BigDecimal valorPrecoUnitario;
	
	@Column(name = "QTDE_DEVOLUCAO_APURADA_ITCE")
	private Long quantidadeDevolucaoApurada;
	
	@Column(name = "QTDE_VENDA_APURADA_ITCE")
	private Long quantidadeVendaApurada;
	
	@Column(name = "VLR_VENDA_APURADA_ITCE")
	private BigDecimal valorVendaApurada;

	@Column(name = "QTDE_DEVOLUCAO_INFORMADA_ITCE")
	private Long quantidadeDevolucaoInformada;
	
	@Column(name = "QTDE_VENDA_INFORMADA_ITCE")
	private Long quantidadeVendaInformada;
	
	@Column(name = "VLR_VENDA_INFORMADA_ITCE")
	private BigDecimal valorVendaInformada;

	@Column(name = "NUM_NOTA_ENVIO_ITCE")
	private Long numeroNotaEnvio;
	
	@Column(name = "COD_NOTA_ENVIO_MULTIPLA_ITCE")
	private String codigoNotaEnvioMultipla;
	
	@Column(name = "TIPO_PRODUTO_ITCE")
	private String tipoProduto;
	
	@Column(name = "COD_PREENCHIMENTO_ITCE")
	private String codigoPreenchimento;
	
	@Column(name = "TIPO_STATUS_ITCE")
	private String tipoStatus;
	
	@Column(name = "QTDE_DEVOLUCAO_PARCIAL_ITCE")
	private Long quantidadeDevolucaoParcial;
	
	@Column(name = "NUM_EDICAO_VALE_DESCONTO_VLDS")
	private Long numeroEdicaoValeDesconto;
	
	@Column(name = "VLR_MARGEM_INFORMADO_ITCE")
	private BigDecimal valorMargemInformado;

	@Column(name = "VLR_MARGEM_APURADO_ITCE")
	private BigDecimal valorMargemApurado;
	
	@Column(name = "NUM_ACESSO_NOTA_ENVIO")
	private String numeroAcessoNotaEnvio;
	
	@Column(name = "DAT_EMISSAO_NOTA_ENVIO")
	private Date dataEmissaoNotaEnvio;
	
	@Column(name = "TIPO_MODELO_NOTA_ENVIO")
	private Long tipoModeloNotaEnvio;
	
	@Column(name = "COD_SERIE_NOTA_ENVIO")
	private Long codigoSerieNotaEnvio;
	
	/**
	 * 
	 */
	public CEItemPK getCeItemPK() {
		return ceItemPK;
	}

	public void setCeItemPK(CEItemPK ceItemPK) {
		this.ceItemPK = ceItemPK;
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	public Long getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(Long numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public IcdLancamentoEdicaoPublicacao getLancamentoEdicaoPublicacao() {
		return lancamentoEdicaoPublicacao;
	}

	public void setLancamentoEdicaoPublicacao(IcdLancamentoEdicaoPublicacao lancamentoEdicaoPublicacao) {
		this.lancamentoEdicaoPublicacao = lancamentoEdicaoPublicacao;
	}

	public Long getCodigoValeDesconto() {
		return codigoValeDesconto;
	}

	public void setCodigoValeDesconto(Long codigoValeDesconto) {
		this.codigoValeDesconto = codigoValeDesconto;
	}

	public Long getQuantidadeEnviada() {
		return quantidadeEnviada;
	}

	public void setQuantidadeEnviada(Long quantidadeEnviada) {
		this.quantidadeEnviada = quantidadeEnviada;
	}

	public Long getCodigoFormaDevolucao() {
		return codigoFormaDevolucao;
	}

	public void setCodigoFormaDevolucao(Long codigoFormaDevolucao) {
		this.codigoFormaDevolucao = codigoFormaDevolucao;
	}

	public Long getNumeroControle() {
		return numeroControle;
	}

	public void setNumeroControle(Long numeroControle) {
		this.numeroControle = numeroControle;
	}

	public String getCodigoRegimeRecolhimento() {
		return codigoRegimeRecolhimento;
	}

	public void setCodigoRegimeRecolhimento(String codigoRegimeRecolhimento) {
		this.codigoRegimeRecolhimento = codigoRegimeRecolhimento;
	}

	public BigDecimal getValorPrecoUnitario() {
		return valorPrecoUnitario;
	}

	public void setValorPrecoUnitario(BigDecimal valorPrecoUnitario) {
		this.valorPrecoUnitario = valorPrecoUnitario;
	}

	public Long getQuantidadeDevolucaoApurada() {
		return quantidadeDevolucaoApurada;
	}

	public void setQuantidadeDevolucaoApurada(Long quantidadeDevolucaoApurada) {
		this.quantidadeDevolucaoApurada = quantidadeDevolucaoApurada;
	}

	public Long getQuantidadeVendaApurada() {
		return quantidadeVendaApurada;
	}

	public void setQuantidadeVendaApurada(Long quantidadeVendaApurada) {
		this.quantidadeVendaApurada = quantidadeVendaApurada;
	}

	public BigDecimal getValorVendaApurada() {
		return valorVendaApurada;
	}

	public void setValorVendaApurada(BigDecimal valorVendaApurada) {
		this.valorVendaApurada = valorVendaApurada;
	}

	public Long getQuantidadeDevolucaoInformada() {
		return quantidadeDevolucaoInformada;
	}

	public void setQuantidadeDevolucaoInformada(Long quantidadeDevolucaoInformada) {
		this.quantidadeDevolucaoInformada = quantidadeDevolucaoInformada;
	}

	public Long getQuantidadeVendaInformada() {
		return quantidadeVendaInformada;
	}

	public void setQuantidadeVendaInformada(Long quantidadeVendaInformada) {
		this.quantidadeVendaInformada = quantidadeVendaInformada;
	}

	public BigDecimal getValorVendaInformada() {
		return valorVendaInformada;
	}

	public void setValorVendaInformada(BigDecimal valorVendaInformada) {
		this.valorVendaInformada = valorVendaInformada;
	}

	public Long getNumeroNotaEnvio() {
		return numeroNotaEnvio;
	}

	public void setNumeroNotaEnvio(Long numeroNotaEnvio) {
		this.numeroNotaEnvio = numeroNotaEnvio;
	}

	public String getCodigoNotaEnvioMultipla() {
		return codigoNotaEnvioMultipla;
	}

	public void setCodigoNotaEnvioMultipla(String codigoNotaEnvioMultipla) {
		this.codigoNotaEnvioMultipla = codigoNotaEnvioMultipla;
	}

	public String getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(String tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public String getCodigoPreenchimento() {
		return codigoPreenchimento;
	}

	public void setCodigoPreenchimento(String codigoPreenchimento) {
		this.codigoPreenchimento = codigoPreenchimento;
	}

	public String getTipoStatus() {
		return tipoStatus;
	}

	public void setTipoStatus(String tipoStatus) {
		this.tipoStatus = tipoStatus;
	}

	public Long getQuantidadeDevolucaoParcial() {
		return quantidadeDevolucaoParcial;
	}

	public void setQuantidadeDevolucaoParcial(Long quantidadeDevolucaoParcial) {
		this.quantidadeDevolucaoParcial = quantidadeDevolucaoParcial;
	}

	public Long getNumeroEdicaoValeDesconto() {
		return numeroEdicaoValeDesconto;
	}

	public void setNumeroEdicaoValeDesconto(Long numeroEdicaoValeDesconto) {
		this.numeroEdicaoValeDesconto = numeroEdicaoValeDesconto;
	}

	public BigDecimal getValorMargemInformado() {
		return valorMargemInformado;
	}

	public void setValorMargemInformado(BigDecimal valorMargemInformado) {
		this.valorMargemInformado = valorMargemInformado;
	}

	public BigDecimal getValorMargemApurado() {
		return valorMargemApurado;
	}

	public void setValorMargemApurado(BigDecimal valorMargemApurado) {
		this.valorMargemApurado = valorMargemApurado;
	}

	public String getNumeroAcessoNotaEnvio() {
		return numeroAcessoNotaEnvio;
	}

	public void setNumeroAcessoNotaEnvio(String numeroAcessoNotaEnvio) {
		this.numeroAcessoNotaEnvio = numeroAcessoNotaEnvio;
	}

	public Date getDataEmissaoNotaEnvio() {
		return dataEmissaoNotaEnvio;
	}

	public void setDataEmissaoNotaEnvio(Date dataEmissaoNotaEnvio) {
		this.dataEmissaoNotaEnvio = dataEmissaoNotaEnvio;
	}

	public Long getTipoModeloNotaEnvio() {
		return tipoModeloNotaEnvio;
	}

	public void setTipoModeloNotaEnvio(Long tipoModeloNotaEnvio) {
		this.tipoModeloNotaEnvio = tipoModeloNotaEnvio;
	}

	public Long getCodigoSerieNotaEnvio() {
		return codigoSerieNotaEnvio;
	}

	public void setCodigoSerieNotaEnvio(Long codigoSerieNotaEnvio) {
		this.codigoSerieNotaEnvio = codigoSerieNotaEnvio;
	}
	
}
