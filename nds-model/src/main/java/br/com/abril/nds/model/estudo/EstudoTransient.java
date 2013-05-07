package br.com.abril.nds.model.estudo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.BonificacaoDTO;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.seguranca.Usuario;

public class EstudoTransient extends Estudo {

    private static final long serialVersionUID = 8058482081570920501L;

    private BigInteger reparteDistribuirInicial;
    private ProdutoEdicaoEstudo produtoEdicaoEstudo;
    private LinkedList<ProdutoEdicaoEstudo> edicoesBase;
    private LinkedList<CotaEstudo> cotas; //Cotas que receberam as edições base.
    private LinkedList<CotaEstudo> cotasExcluidas; //Cotas excluídas durante o estudo
    private BigInteger reparteMinimo;
    private BigDecimal somatoriaVendaMedia;
    private BigDecimal somatoriaReparteEdicoesAbertas;
    private BigDecimal totalPDVs;
    private BigInteger ajusteReparte;
    private BigDecimal excedente;
    private BigInteger reparteComplementar;
    private String statusEstudo;
    private List<BonificacaoDTO> bonificacoes;
    private Usuario usuario;
    private boolean usarFixacao;

    private boolean complementarAutomatico;
    private boolean pracaVeraneio;
    private boolean geracaoAutomatica;
    private BigDecimal percentualMaximoFixacao;
    private BigInteger vendaMediaMais;

    private Map<String, PercentualExcedenteEstudo> percentualExcedente;

    public EstudoTransient() {
	usarFixacao = true;
	complementarAutomatico = true; //Default conforme documentação.
	cotasExcluidas = new LinkedList<>();
    }

    public BigInteger getReparteDistribuirInicial() {
	return reparteDistribuirInicial;
    }
    public void setReparteDistribuirInicial(BigInteger reparteDistribuirInicial) {
	this.reparteDistribuirInicial = reparteDistribuirInicial;
    }
    public ProdutoEdicaoEstudo getProdutoEdicaoEstudo() {
	return produtoEdicaoEstudo;
    }
    public void setProdutoEdicaoEstudo(ProdutoEdicaoEstudo produtoEdicaoEstudo) {
	this.produtoEdicaoEstudo = produtoEdicaoEstudo;
    }
    public LinkedList<ProdutoEdicaoEstudo> getEdicoesBase() {
	return edicoesBase;
    }
    public void setEdicoesBase(LinkedList<ProdutoEdicaoEstudo> edicoesBase) {
	this.edicoesBase = edicoesBase;
    }
    public LinkedList<CotaEstudo> getCotas() {
	return cotas;
    }
    public void setCotas(LinkedList<CotaEstudo> cotas) {
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

    public boolean isUsarFixacao() {
	return usarFixacao;
    }

    public void setUsarFixacao(boolean usarFixacao) {
	this.usarFixacao = usarFixacao;
    }

    public Usuario getUsuario() {
	return usuario;
    }

    public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
    }

    public LinkedList<CotaEstudo> getCotasExcluidas() {
	return cotasExcluidas;
    }

    public void setCotasExcluidas(LinkedList<CotaEstudo> cotasExcluidas) {
	this.cotasExcluidas = cotasExcluidas;
    }

    @Override
    public String toString() {
	return "EstudoTransient [reparteDistribuirInicial="
		+ reparteDistribuirInicial + ", produtoEdicaoEstudo="
		+ produtoEdicaoEstudo + ", edicoesBase=" + edicoesBase
		+ ", reparteMinimo=" + reparteMinimo + ", somatoriaVendaMedia="
		+ somatoriaVendaMedia + ", somatoriaReparteEdicoesAbertas="
		+ somatoriaReparteEdicoesAbertas + ", totalPDVs=" + totalPDVs
		+ ", ajusteReparte=" + ajusteReparte + ", excedente="
		+ excedente + ", reparteComplementar=" + reparteComplementar
		+ ", statusEstudo=" + statusEstudo + ", bonificacoes="
		+ bonificacoes + ", complementarAutomatico="
		+ complementarAutomatico + ", pracaVeraneio=" + pracaVeraneio
		+ ", geracaoAutomatica=" + geracaoAutomatica
		+ ", percentualMaximoFixacao=" + percentualMaximoFixacao
		+ ", vendaMediaMais=" + vendaMediaMais
		+ ", percentualExcedente=" + percentualExcedente.values() + "]";
    }
}
