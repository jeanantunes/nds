package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.List;

public class Estudo extends GenericDTO<Estudo> {

	private static final long serialVersionUID = -8639160268952528714L;

	private Integer id;
	private String status;
	private BigDecimal reparteDistribuir;
	private BigDecimal reparteDistribuirInicial;
	private ProdutoEdicaoBase produto;
	private List<ProdutoEdicaoBase> edicoesBase;
	private List<Cota> cotas; //Cotas que receberam as edições base.
	private boolean distribuicaoPorMultiplos;
	private BigDecimal pacotePadrao;
	private BigDecimal somatoriaVendaMedia;
	private BigDecimal somatoriaReparteEdicoesAbertas;
	// TODO: Checar após a EMS 2027 estar pronta, onde deveremos consultar esses
	// parâmetros
	private boolean complementarAutomatico;
	private BigDecimal percentualProporcaoExcedentePDV;
	private BigDecimal percentualProporcaoExcedenteVenda;
	private BigDecimal totalPDVs;
	//TODO validar campo no DB e como recupera-lo
	private boolean pracaVeraneio;
	// variáveis utilizadas no algoritmo
	private BigDecimal reservaAjuste;
	private BigDecimal excedente;
	private BigDecimal reparteComplementar;

	public Estudo() {
		totalPDVs = BigDecimal.ZERO;
		pacotePadrao = BigDecimal.ZERO;
		reservaAjuste = BigDecimal.ZERO;
		reparteDistribuir = BigDecimal.ZERO;
		reparteDistribuirInicial = BigDecimal.ZERO;
		somatoriaVendaMedia = BigDecimal.ZERO;
		somatoriaReparteEdicoesAbertas = BigDecimal.ZERO;
		percentualProporcaoExcedentePDV = BigDecimal.ZERO;
		percentualProporcaoExcedenteVenda = BigDecimal.ZERO;
		excedente = BigDecimal.ZERO;
	}
	
	/**
	 * Reparte Total a ser distribuído
	 * 
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getReparteDistribuir() {
		return reparteDistribuir;
	}

	public void setReparteDistribuir(BigDecimal reparteDistribuir) {
		this.reparteDistribuir = reparteDistribuir;
	}

	/**
	 * Produto sobre o qual se trata o estudo
	 * 
	 * @return {@link ProdutoEdicao}
	 */
	public ProdutoEdicaoBase getProduto() {
		return produto;
	}

	public void setProduto(ProdutoEdicaoBase produto) {
		this.produto = produto;
	}

	public List<ProdutoEdicaoBase> getEdicoesBase() {
		return edicoesBase;
	}

	public void setEdicoesBase(List<ProdutoEdicaoBase> edicoesBase) {
		this.edicoesBase = edicoesBase;
	}

	public List<Cota> getCotas() {
		return cotas;
	}

	public void setCotas(List<Cota> cotas) {
		this.cotas = cotas;
	}

	/**
	 * Soma da Venda Média de todas as cotas (exceto as marcadas com 'FX', 'PR'
	 * e 'RD')
	 * 
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getSomatoriaVendaMedia() {
		return somatoriaVendaMedia;
	}

	public void setSomatoriaVendaMediaFinal(BigDecimal somatoriaVendaMedia) {
		this.somatoriaVendaMedia = somatoriaVendaMedia;
	}

	/**
	 * Reparte Total a ser distribuído (valor não deverá ser alterado durante o
	 * processo)
	 * 
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getReparteDistribuirInicial() {
		return reparteDistribuirInicial;
	}

	public void setReparteDistribuirInicial(BigDecimal reparteDistribuirInicial) {
		this.reparteDistribuirInicial = reparteDistribuirInicial;
	}

	/**
	 * Soma de Reparte de todas as edições abertas
	 * 
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getSomatoriaReparteEdicoesAbertas() {
		return somatoriaReparteEdicoesAbertas;
	}

	public void setSomatoriaReparteEdicoesAbertas(BigDecimal somatoriaReparteEdicoesAbertas) {
		this.somatoriaReparteEdicoesAbertas = somatoriaReparteEdicoesAbertas;
	}

	/**
	 * Variável que define se o estudo irá executar o processo Complementar
	 * Automático
	 * 
	 * @return boolean
	 */
	public boolean isComplementarAutomatico() {
		return complementarAutomatico;
	}

	public void setComplementarAutomatico(boolean complementarAutomatico) {
		this.complementarAutomatico = complementarAutomatico;
	}

	/**
	 * Retorna verdadeiro se o método de distribuição utilizar múltiplos
	 * (semelhante ao conceito de Pacote Padrão).
	 * 
	 * @return boolean
	 */
	public boolean isDistribuicaoPorMultiplos() {
		return distribuicaoPorMultiplos;
	}

	public void setDistribuicaoPorMultiplos(boolean distribuicaoPorMultiplos) {
		this.distribuicaoPorMultiplos = distribuicaoPorMultiplos;
	}

	/**
	 * Retorna o pacote padrão definido para a distribuição por múltiplos na
	 * configuração de distribuição do estudo. (Configuração atrelada à
	 * Distribuição Por Múltiplo)
	 * 
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(BigDecimal pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	public BigDecimal getPercentualProporcaoExcedentePDV() {
		return percentualProporcaoExcedentePDV;
	}

	public void setPercentualProporcaoExcedentePDV(BigDecimal percentualProporcaoExcedentePDV) {
		this.percentualProporcaoExcedentePDV = percentualProporcaoExcedentePDV;
	}

	public BigDecimal getPercentualProporcaoExcedenteVenda() {
		return percentualProporcaoExcedenteVenda;
	}

	public void setPercentualProporcaoExcedenteVenda(BigDecimal percentualProporcaoExcedenteVenda) {
		this.percentualProporcaoExcedenteVenda = percentualProporcaoExcedenteVenda;
	}

	/**
	 * Soma das quantidades de PDV's das cotas
	 * 
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getTotalPDVs() {
		return totalPDVs;
	}

	public void setTotalPDVs(BigDecimal totalPDVs) {
		this.totalPDVs = totalPDVs;
	}

	public boolean isPracaVeraneio() {
	    return pracaVeraneio;
	}

	public void setPracaVeraneio(boolean pracaVeraneio) {
	    this.pracaVeraneio = pracaVeraneio;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getReservaAjuste() {
		return reservaAjuste;
	}

	public void setReservaAjuste(BigDecimal reservaAjuste) {
		this.reservaAjuste = reservaAjuste;
	}

	public BigDecimal getExcedente() {
		return excedente;
	}

	public void setExcedente(BigDecimal excedente) {
		this.excedente = excedente;
	}

	public BigDecimal getReparteComplementar() {
		return reparteComplementar;
	}

	public void setReparteComplementar(BigDecimal reparteComplementar) {
		this.reparteComplementar = reparteComplementar;
	}

	@Override
	public String toString() {
		return "Estudo [id=" + id + "\n status=" + status + "\n reparteDistribuir=" + reparteDistribuir + "\n reparteDistribuirInicial="
				+ reparteDistribuirInicial + "\n produto=" + produto + "\n edicoesBase=" + edicoesBase + "\n cotas=" + cotas
				+ "\n distribuicaoPorMultiplos=" + distribuicaoPorMultiplos + "\n pacotePadrao=" + pacotePadrao + "\n somatoriaVendaMedia="
				+ somatoriaVendaMedia + "\n somatoriaReparteEdicoesAbertas=" + somatoriaReparteEdicoesAbertas + "\n complementarAutomatico="
				+ complementarAutomatico + "\n percentualProporcaoExcedentePDV=" + percentualProporcaoExcedentePDV
				+ "\n percentualProporcaoExcedenteVenda=" + percentualProporcaoExcedenteVenda + "\n totalPDVs=" + totalPDVs
				+ "\n pracaVeraneio=" + pracaVeraneio + "\n reservaAjuste=" + reservaAjuste + "]";
	}
}
