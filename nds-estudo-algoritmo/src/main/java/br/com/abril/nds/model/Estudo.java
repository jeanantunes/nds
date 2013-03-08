package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Estudo extends GenericDTO<Estudo> {

	private static final long serialVersionUID = -8639160268952528714L;

	private Integer id;
	private String status;
	private BigDecimal reparteDistribuir;
	private BigDecimal reparteDistribuirInicial;
	private ProdutoEdicaoBase produto;
	private LinkedList<ProdutoEdicaoBase> edicoesBase;
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
	private BigDecimal ajusteReparte;
	private BigDecimal excedente;
	private BigDecimal reparteComplementar;
	// atributos utilizados 
	private Date dataCadastro;
	
	public Estudo() {
	    dataCadastro = new Date();
	    status = "ESTUDO_FECHADO";
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

	public LinkedList<ProdutoEdicaoBase> getEdicoesBase() {
		return edicoesBase;
	}

	public void setEdicoesBase(LinkedList<ProdutoEdicaoBase> edicoesBase) {
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

	public BigDecimal getAjusteReparte() {
		return ajusteReparte;
	}

	public void setAjusteReparte(BigDecimal ajusteReparte) {
		this.ajusteReparte = ajusteReparte;
	}

	public Date getDataCadastro() {
	    return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
	    this.dataCadastro = dataCadastro;
	}

	@Override
	public String toString() {
		String strcotas = "";
		for (Cota cota : cotas) {
			strcotas += cota.toString() +"\n";
		}
		return "Estudo [id=" + id + ", status=" + status + ", reparteDistribuir=" + reparteDistribuir + ", reparteDistribuirInicial="
				+ reparteDistribuirInicial + ", produto=" + produto + ", edicoesBase=" + edicoesBase
				+ ", distribuicaoPorMultiplos=" + distribuicaoPorMultiplos + ", pacotePadrao=" + pacotePadrao + ", somatoriaVendaMedia="
				+ somatoriaVendaMedia + ", somatoriaReparteEdicoesAbertas=" + somatoriaReparteEdicoesAbertas + ", complementarAutomatico="
				+ complementarAutomatico + ", percentualProporcaoExcedentePDV=" + percentualProporcaoExcedentePDV
				+ ", percentualProporcaoExcedenteVenda=" + percentualProporcaoExcedenteVenda + ", totalPDVs=" + totalPDVs
				+ ", pracaVeraneio=" + pracaVeraneio + ", ajusteReparte=" + ajusteReparte + ", excedente=" + excedente
				+ ", reparteComplementar=" + reparteComplementar + "] \n"+ strcotas;
	}
}
