package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.List;

public class Estudo extends GenericDTO<Estudo> {

    /**
     * 
     */
    private static final long serialVersionUID = -8639160268952528714L;

    private Integer id;
    private BigDecimal reparteDistribuir; // Reparte Total a ser distribuído
    private BigDecimal reparteDistribuirInicial; // Reparte Total a ser distribuído (valor não deverá ser alterado durante o processo)
    private ProdutoEdicao produto; // produto sobre o qual se trata o estudo
    private List<ProdutoEdicao> edicoesBaseInsercaoManual;
    private List<ProdutoEdicao> edicoesBase;
    private List<Cota> cotas;
    private boolean distribuicaoPorMultiplos;
    private BigDecimal pacotePadrao; // Parâmetro definido na tela de distribuição (Configuração atrelada à Distribuição Por Múltiplo)
    private BigDecimal somatoriaVendaMedia; // Soma da Venda Média de todas as cotas (exceto as marcadas com 'FX', 'PR' e 'RD')
    // TODO: Verificar se essa somatória é do estudo total ou somente da cota (Processo: Reparte Proporcional)
    private BigDecimal somatoriaReparteEdicoesAbertas; // Soma de Reparte de todas as edições abertas
    // TODO: Checar após a EMS 2027 estar pronta, onde deveremos consultar esse parâmetro
    private boolean complementarAutomatico; // Variável que define se o estudo irá executar o processo Complementar Automático
    // TODO: Verificar se o correto é deixar esse percentual aqui no estudo
    private BigDecimal percentualProporcaoExcedentePDV;
    private BigDecimal percentualProporcaoExcedenteVenda;
    private BigDecimal totalPDVs; // soma dos PDV's das cotas

    public void calculate() {
	somatoriaVendaMedia = new BigDecimal(0);
	for (Cota cota : cotas) {
	    if (!cota.getClassificacao()
		    .equals(ClassificacaoCota.ReparteFixado)
		    || !cota.getClassificacao().equals(
			    ClassificacaoCota.BancaSoComEdicaoBaseAberta)
			    || !cota.getClassificacao().equals(
				    ClassificacaoCota.RedutorAutomatico))
		somatoriaVendaMedia.add(cota.getVendaMedia());
	}
    }

    public BigDecimal getReparteDistribuir() {
	return reparteDistribuir;
    }

    public void setReparteDistribuir(BigDecimal reparteDistribuir) {
	this.reparteDistribuir = reparteDistribuir;
    }

    public ProdutoEdicao getProduto() {
	return produto;
    }

    public void setProduto(ProdutoEdicao produto) {
	this.produto = produto;
    }

    public List<ProdutoEdicao> getEdicoesBaseInsercaoManual() {
	return edicoesBaseInsercaoManual;
    }

    public void setEdicoesBaseInsercaoManual(
	    List<ProdutoEdicao> edicoesBaseInsercaoManual) {
	this.edicoesBaseInsercaoManual = edicoesBaseInsercaoManual;
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
	return "\nEstudo{\n\t" + "id: " + id + ", \n\treparteCalculado: "
		+ reparteDistribuir + "\n}";
    }

    /**
     * Soma da Venda Média de todas as cotas (exceto as marcadas com 'FX', 'PR' e 'RD')
     * @return {@link BigDecimal}
     */
    public BigDecimal getSomatoriaVendaMedia() {
	return somatoriaVendaMedia;
    }

    public void setSomatoriaVendaMediaFinal(BigDecimal somatoriaVendaMedia) {
	this.somatoriaVendaMedia = somatoriaVendaMedia;
    }

    public BigDecimal getReparteDistribuirInicial() {
	return reparteDistribuirInicial;
    }

    public void setReparteDistribuirInicial(BigDecimal reparteDistribuirInicial) {
	this.reparteDistribuirInicial = reparteDistribuirInicial;
    }

    public BigDecimal getSomatoriaReparteEdicoesAbertas() {
	return somatoriaReparteEdicoesAbertas;
    }

    public void setSomatoriaReparteEdicoesAbertas(BigDecimal somatoriaReparteEdicoesAbertas) {
	this.somatoriaReparteEdicoesAbertas = somatoriaReparteEdicoesAbertas;
    }

    public boolean isComplementarAutomatico() {
	return complementarAutomatico;
    }

    public void setComplementarAutomatico(boolean complementarAutomatico) {
	this.complementarAutomatico = complementarAutomatico;
    }
    
    /**
     * Retorna verdadeiro se o método de distribuição utilizar múltiplos (semelhante ao conceito de Pacote Padrão). 
     * @return boolean
     */
    public boolean isDistribuicaoPorMultiplos() {
	return distribuicaoPorMultiplos;
    }

    public void setDistribuicaoPorMultiplos(boolean distribuicaoPorMultiplos) {
	this.distribuicaoPorMultiplos = distribuicaoPorMultiplos;
    }
    
    /**
     * Retorna o pacote padrão definido para a distribuição por múltiplos na configuração de distribuição do estudo.
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

    public BigDecimal getTotalPDVs() {
	return totalPDVs;
    }

    public void setTotalPDVs(BigDecimal totalPDVs) {
	this.totalPDVs = totalPDVs;
    }
}
