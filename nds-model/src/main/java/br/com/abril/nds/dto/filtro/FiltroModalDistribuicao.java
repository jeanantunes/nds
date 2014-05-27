package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.ModalidadeCobranca;
import br.com.abril.nds.model.cadastro.PeriodicidadeCobranca;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroModalDistribuicao implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5105701443356674581L;
	
	private String nmAssitPromoComercial;
	
	private String nmGerenteComercial;
	
	private String observacao;

	private boolean repartePontoVenda; 
	
	private boolean solicitacaoNumAtrasoInternet;
	
	private boolean recebeRecolheProdutosParciais;
	
	private boolean recebeComplementar;

	private DescricaoTipoEntrega descricaoTipoEntrega; 
	
	private FiltroCheckDistribEmisDoc filtroCheckDistribEmisDoc = new FiltroCheckDistribEmisDoc();
	
	private boolean termoAdesao;
	
	private boolean termoAdesaoRecebido;
	
	private BigDecimal percentualFaturamento;
	
	private BigDecimal taxaFixa;
	
	private Date carenciaInicio;
	
	private Date  carenciaFim;
	
	private boolean procuracao;
	
	private boolean procuracaoRecebida;
	
	private BaseCalculo baseCalculo;
	
	private ModalidadeCobranca modalidadeCobranca;
	
	private boolean porEntrega;
	
	private PeriodicidadeCobranca periodicidadeCobranca;
	
	private Integer diaCobranca;
	
	private DiaSemana diaSemanaCobranca;
	
	private List<ItemDTO<BaseCalculo,String>> basesCalculo;

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

	public boolean isRepartePontoVenda() {
		return repartePontoVenda;
	}

	public void setRepartePontoVenda(boolean isRepartePontoVenda) {
		this.repartePontoVenda = isRepartePontoVenda;
	}

	public boolean isSolicitacaoNumAtrasoInternet() {
		return solicitacaoNumAtrasoInternet;
	}

	public void setSolicitacaoNumAtrasoInternet(boolean solicitacaoNumAtrasoInternet) {
		this.solicitacaoNumAtrasoInternet = solicitacaoNumAtrasoInternet;
	}

	public boolean isRecebeRecolheProdutosParciais() {
		return recebeRecolheProdutosParciais;
	}

	public void setRecebeRecolheProdutosParciais(boolean isRecebeRecolheProdutosParciais) {
		this.recebeRecolheProdutosParciais = isRecebeRecolheProdutosParciais;
	}

	public boolean isRecebeComplementar() {
		return recebeComplementar;
	}

	public void setRecebeComplementar(boolean recebeComplementar) {
		this.recebeComplementar = recebeComplementar;
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

	public BaseCalculo getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(BaseCalculo baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	public List<ItemDTO<BaseCalculo,String>> getBasesCalculo() {
		return basesCalculo;
	}

	public void setBasesCalculo(List<ItemDTO<BaseCalculo,String>> basesCalculo) {
		this.basesCalculo = basesCalculo;
	}

	public BigDecimal getPercentualFaturamento() {
		return percentualFaturamento;
	}

	public void setPercentualFaturamento(BigDecimal percentualFaturamento) {
		this.percentualFaturamento = percentualFaturamento;
	}

	public BigDecimal getTaxaFixa() {
		return taxaFixa;
	}

	public void setTaxaFixa(BigDecimal taxaFixa) {
		this.taxaFixa = taxaFixa;
	}

	public Date getCarenciaInicio() {
		return carenciaInicio;
	}

	public void setCarenciaInicio(Date carenciaInicio) {
		this.carenciaInicio = carenciaInicio;
	}

	public Date getCarenciaFim() {
		return carenciaFim;
	}

	public void setCarenciaFim(Date carenciaFim) {
		this.carenciaFim = carenciaFim;
	}

	public ModalidadeCobranca getModalidadeCobranca() {
		return modalidadeCobranca;
	}

	public void setModalidadeCobranca(ModalidadeCobranca modalidadeCobranca) {
		this.modalidadeCobranca = modalidadeCobranca;
	}

	public boolean isPorEntrega() {
		return porEntrega;
	}

	public void setPorEntrega(boolean porEntrega) {
		this.porEntrega = porEntrega;
	}

	public PeriodicidadeCobranca getPeriodicidadeCobranca() {
		return periodicidadeCobranca;
	}

	public void setPeriodicidadeCobranca(PeriodicidadeCobranca periodicidadeCobranca) {
		this.periodicidadeCobranca = periodicidadeCobranca;
	}

	public Integer getDiaCobranca() {
		return diaCobranca;
	}

	public void setDiaCobranca(Integer diaCobranca) {
		this.diaCobranca = diaCobranca;
	}

	public DiaSemana getDiaSemanaCobranca() {
		return diaSemanaCobranca;
	}

	public void setDiaSemanaCobranca(DiaSemana diaSemanaCobranca) {
		this.diaSemanaCobranca = diaSemanaCobranca;
	}
	
	

}