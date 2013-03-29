package br.com.abril.nds.model.estudo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.BonificacaoDTO;
import br.com.abril.nds.model.planejamento.Estudo;

public class EstudoTransient extends Estudo {

	private static final long serialVersionUID = 8058482081570920501L;

	private BigInteger reparteDistribuirInicial;
	private ProdutoEdicaoEstudo produto; //TODO refatorar nome qdo possivel...
	private LinkedList<ProdutoEdicaoEstudo> edicoesBase;
	private List<CotaEstudo> cotas; //Cotas que receberam as edições base.
	private BigInteger reparteMinimo;
	private BigDecimal somatoriaVendaMedia;
	private BigDecimal somatoriaReparteEdicoesAbertas;
	private BigDecimal totalPDVs;
	private BigInteger ajusteReparte;
	private BigDecimal excedente;
	private BigInteger reparteComplementar;
	private String statusEstudo;
	private BigDecimal percentualMaximoFixacao;
	private List<BonificacaoDTO> bonificacoes;
	
	private boolean complementarAutomatico;
	private boolean pracaVeraneio;
	private boolean geracaoAutomatica;
	private BigDecimal percentualMaximoFixacao;
	private BigInteger vendaMediaMais;
	
	private Map<String, PercentualExcedenteEstudo> percentualExcedente;
	
	public BigInteger getReparteDistribuirInicial() {
		return reparteDistribuirInicial;
	}
	public void setReparteDistribuirInicial(BigInteger reparteDistribuirInicial) {
		this.reparteDistribuirInicial = reparteDistribuirInicial;
	}
	public ProdutoEdicaoEstudo getProduto() {
		return produto;
	}
	public void setProduto(ProdutoEdicaoEstudo produto) {
		this.produto = produto;
	}
	public LinkedList<ProdutoEdicaoEstudo> getEdicoesBase() {
		return edicoesBase;
	}
	public void setEdicoesBase(LinkedList<ProdutoEdicaoEstudo> edicoesBase) {
		this.edicoesBase = edicoesBase;
	}
	public List<CotaEstudo> getCotas() {
		return cotas;
	}
	public void setCotas(List<CotaEstudo> cotas) {
		this.cotas = cotas;
	}
	public BigInteger getReparteMinimo() {
		return reparteMinimo;
	}
	public void setReparteMinimo(BigInteger reparteMinimo) {
		this.reparteMinimo = reparteMinimo;
	}
	public BigDecimal getSomatoriaVendaMedia() {
		return somatoriaVendaMedia;
	}
	public void setSomatoriaVendaMedia(BigDecimal somatoriaVendaMedia) {
		this.somatoriaVendaMedia = somatoriaVendaMedia;
	}
	public BigDecimal getSomatoriaReparteEdicoesAbertas() {
		return somatoriaReparteEdicoesAbertas;
	}
	public void setSomatoriaReparteEdicoesAbertas(
			BigDecimal somatoriaReparteEdicoesAbertas) {
		this.somatoriaReparteEdicoesAbertas = somatoriaReparteEdicoesAbertas;
	}
	public boolean isComplementarAutomatico() {
		return complementarAutomatico;
	}
	public void setComplementarAutomatico(boolean complementarAutomatico) {
		this.complementarAutomatico = complementarAutomatico;
	}
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
	public BigInteger getAjusteReparte() {
		return ajusteReparte;
	}
	public void setAjusteReparte(BigInteger ajusteReparte) {
		this.ajusteReparte = ajusteReparte;
	}
	public BigDecimal getExcedente() {
		return excedente;
	}
	public void setExcedente(BigDecimal excedente) {
		this.excedente = excedente;
	}
	public BigInteger getReparteComplementar() {
		return reparteComplementar;
	}
	public void setReparteComplementar(BigInteger reparteComplementar) {
		this.reparteComplementar = reparteComplementar;
	}
	public boolean isDistribuicaoPorMultiplos() {
		return getDistribuicaoPorMultiplos() == 1;
	}
	public boolean isGeracaoAutomatica() {
		return geracaoAutomatica;
	}
	public void setGeracaoAutomatica(boolean geracaoAutomatica) {
		this.geracaoAutomatica = geracaoAutomatica;
	}
	public BigInteger getVendaMediaMais() {
		return vendaMediaMais;
	}
	public void setVendaMediaMais(BigInteger vendaMediaMais) {
		this.vendaMediaMais = vendaMediaMais;
	}
	public BigDecimal getPercentualMaximoFixacao() {
	    return percentualMaximoFixacao;
	}
	public void setPercentualMaximoFixacao(BigDecimal percentualMaximoFixacao) {
	    this.percentualMaximoFixacao = percentualMaximoFixacao;
	}
	public List<BonificacaoDTO> getBonificacoes() {
	    return bonificacoes;
	}
	public void setBonificacoes(List<BonificacaoDTO> bonificacoes) {
	    this.bonificacoes = bonificacoes;
	}
	public String getStatusEstudo() {
	    return statusEstudo;
	}
	public void setStatusEstudo(String statusEstudo) {
	    this.statusEstudo = statusEstudo;
	}
	public Map<String, PercentualExcedenteEstudo> getPercentualExcedente() {
		return percentualExcedente;
	}
	public void setPercentualExcedente(Map<String, PercentualExcedenteEstudo> percentualExcedente) {
		this.percentualExcedente = percentualExcedente;
	}
	public BigDecimal getPercentualMaximoFixacao() {
	    return percentualMaximoFixacao;
	}
	public void setPercentualMaximoFixacao(BigDecimal percentualMaximoFixacao) {
	    this.percentualMaximoFixacao = percentualMaximoFixacao;
	}
}
