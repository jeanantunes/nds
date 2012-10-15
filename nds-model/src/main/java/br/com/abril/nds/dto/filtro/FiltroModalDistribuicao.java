package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroModalDistribuicao implements Serializable{
	
	
	private String nmAssitPromoComercial;
	private String nmGerenteComercial;
	private String observacao;

	private boolean isRepartePontoVenda; 
	private boolean isSolicitacaoNumAtrasoInternet;
	private boolean isRecebeRecolheProdutosParciais;

	private DescricaoTipoEntrega descricaoTipoEntrega; 
	
	private FiltroCheckDistribEmisDoc filtroCheckDistribEmisDoc = new FiltroCheckDistribEmisDoc();
	
	private boolean termoAdesao;
	
	private boolean termoAdesaoRecebido;
	
	private BigDecimal percentualFaturamentoEntregaBranca;
	
	private BigDecimal taxaFixaEntregaBranca;
	
	private BaseCalculo baseCalculo;
	
	private Date carenciaInicioEntregaBranca;
	
	private Date  carenciaFimEntregaBranca;
	
	
	
	private boolean procuracao;
	
	private boolean procuracaoRecebida;
	
	private BigDecimal percentualFaturamentoEntregador;
	
	private Date carenciaInicioEntregador;
	
	private Date carenciaFimEntregador;
	

	public String getNmAssitPromoComercial() {
		return nmAssitPromoComercial;
	}

	public void setNmAssitPromoComercial(String nmAssitPromoComercial) {
		this.nmAssitPromoComercial = nmAssitPromoComercial;
	}

	public String getNmGerenteComercial() {
		return nmGerenteComercial;
	}

	public void setNmGerenteComercial(String nmGerenteComercial) {
		this.nmGerenteComercial = nmGerenteComercial;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public boolean getIsRepartePontoVenda() {
		return isRepartePontoVenda;
	}

	public void setIsRepartePontoVenda(boolean isRepartePontoVenda) {
		this.isRepartePontoVenda = isRepartePontoVenda;
	}

	public boolean getIsSolicitacaoNumAtrasoInternet() {
		return isSolicitacaoNumAtrasoInternet;
	}

	public void setIsSolicitacaoNumAtrasoInternet(
			boolean isSolicitacaoNumAtrasoInternet) {
		this.isSolicitacaoNumAtrasoInternet = isSolicitacaoNumAtrasoInternet;
	}

	public boolean getIsRecebeRecolheProdutosParciais() {
		return isRecebeRecolheProdutosParciais;
	}

	public void setIsRecebeRecolheProdutosParciais(
			boolean isRecebeRecolheProdutosParciais) {
		this.isRecebeRecolheProdutosParciais = isRecebeRecolheProdutosParciais;
	}


	public FiltroCheckDistribEmisDoc getFiltroCheckDistribEmisDoc() {
		return filtroCheckDistribEmisDoc;
	}

	public void setFiltroCheckDistribEmisDoc(
			FiltroCheckDistribEmisDoc filtroCheckDistribEmisDoc) {
		this.filtroCheckDistribEmisDoc = filtroCheckDistribEmisDoc;
	}

	public DescricaoTipoEntrega getDescricaoTipoEntrega() {
		return descricaoTipoEntrega;
	}

	public void setDescricaoTipoEntrega(DescricaoTipoEntrega descricaoTipoEntrega) {
		this.descricaoTipoEntrega = descricaoTipoEntrega;
	}

	public boolean isTermoAdesao() {
		return termoAdesao;
	}

	public void setTermoAdesao(boolean termoAdesao) {
		this.termoAdesao = termoAdesao;
	}

	public boolean isTermoAdesaoRecebido() {
		return termoAdesaoRecebido;
	}

	public void setTermoAdesaoRecebido(boolean termoAdesaoRecebido) {
		this.termoAdesaoRecebido = termoAdesaoRecebido;
	}

	public BigDecimal getPercentualFaturamentoEntregaBranca() {
		return percentualFaturamentoEntregaBranca;
	}

	public void setPercentualFaturamentoEntregaBranca(
			BigDecimal percentualFaturamentoEntregaBranca) {
		this.percentualFaturamentoEntregaBranca = percentualFaturamentoEntregaBranca;
	}

	public BigDecimal getTaxaFixaEntregaBranca() {
		return taxaFixaEntregaBranca;
	}

	public void setTaxaFixaEntregaBranca(BigDecimal taxaFixaEntregaBranca) {
		this.taxaFixaEntregaBranca = taxaFixaEntregaBranca;
	}

	public Date getCarenciaInicioEntregaBranca() {
		return carenciaInicioEntregaBranca;
	}

	public void setCarenciaInicioEntregaBranca(Date carenciaInicioEntregaBranca) {
		this.carenciaInicioEntregaBranca = carenciaInicioEntregaBranca;
	}

	public Date getCarenciaFimEntregaBranca() {
		return carenciaFimEntregaBranca;
	}

	public void setCarenciaFimEntregaBranca(Date carenciaFimEntregaBranca) {
		this.carenciaFimEntregaBranca = carenciaFimEntregaBranca;
	}

	public boolean isProcuracao() {
		return procuracao;
	}

	public void setProcuracao(boolean procuracao) {
		this.procuracao = procuracao;
	}

	public boolean isProcuracaoRecebida() {
		return procuracaoRecebida;
	}

	public void setProcuracaoRecebida(boolean procuracaoRecebida) {
		this.procuracaoRecebida = procuracaoRecebida;
	}

	public BigDecimal getPercentualFaturamentoEntregador() {
		return percentualFaturamentoEntregador;
	}

	public void setPercentualFaturamentoEntregador(
			BigDecimal percentualFaturamentoEntregador) {
		this.percentualFaturamentoEntregador = percentualFaturamentoEntregador;
	}

	public Date getCarenciaInicioEntregador() {
		return carenciaInicioEntregador;
	}

	public void setCarenciaInicioEntregador(Date carenciaInicioEntregador) {
		this.carenciaInicioEntregador = carenciaInicioEntregador;
	}

	public Date getCarenciaFimEntregador() {
		return carenciaFimEntregador;
	}

	public void setCarenciaFimEntregador(Date carenciaFimEntregador) {
		this.carenciaFimEntregador = carenciaFimEntregador;
	}

	public BaseCalculo getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(BaseCalculo baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	
}
