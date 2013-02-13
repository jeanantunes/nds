package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Estudo extends GenericDTO<Estudo> {

	private static final long serialVersionUID = -8639160268952528714L;

	private Integer id;
	private BigDecimal reparteDistribuir;
	private BigDecimal reparteDistribuirInicial;
	private ProdutoEdicao produto;
	private List<ProdutoEdicao> edicoesBase;
	private List<Cota> cotas;
	private boolean distribuicaoPorMultiplos;
	private BigDecimal pacotePadrao;
	private BigDecimal somatoriaVendaMedia;
	// TODO: Verificar se essa somatória é do estudo total ou somente da cota
	// (Processo: Reparte Proporcional)
	private BigDecimal somatoriaReparteEdicoesAbertas;
	// TODO: Checar após a EMS 2027 estar pronta, onde deveremos consultar esse
	// parâmetro
	private boolean complementarAutomatico;
	// TODO: Verificar se o correto é deixar esse percentual aqui no estudo
	// mesmo ou em outra entidade
	private BigDecimal percentualProporcaoExcedentePDV;
	private BigDecimal percentualProporcaoExcedenteVenda;
	private BigDecimal totalPDVs;

	public Estudo() {
		edicoesBase = new ArrayList<ProdutoEdicao>();
		cotas = new ArrayList<Cota>();
		
		pacotePadrao = BigDecimal.ZERO;
	}
	
	public void calculate() {
		somatoriaVendaMedia = new BigDecimal(0);
		for (Cota cota : cotas) {
			if (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
					|| !cota.getClassificacao().equals(ClassificacaoCota.BancaSoComEdicaoBaseAberta)
					|| !cota.getClassificacao().equals(ClassificacaoCota.RedutorAutomatico))
				somatoriaVendaMedia.add(cota.getVendaMedia());
		}
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
	public ProdutoEdicao getProduto() {
		return produto;
	}

	public void setProduto(ProdutoEdicao produto) {
		this.produto = produto;
	}

	public List<ProdutoEdicao> getEdicoesBase() {
		return edicoesBase;
	}

	public void setEdicoesBase(List<ProdutoEdicao> edicoesBase) {
		this.edicoesBase = edicoesBase;
	}

	public List<Cota> getCotas() {
		return cotas;
	}

	public void setCotas(List<Cota> cotas) {
		this.cotas = cotas;
	}

	@Override
	public String toString() {
		return "\nEstudo{\n\t" + "id: " + id + ", \n\treparteCalculado: " + reparteDistribuir + "\n}";
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
}
