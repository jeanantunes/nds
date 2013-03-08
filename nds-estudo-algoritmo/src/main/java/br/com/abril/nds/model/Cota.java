package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.List;

public class Cota extends GenericDTO<Cota> {

    private static final long serialVersionUID = -2770873222155652560L;

    private Long id;
    private Long idEstudo;
    private List<ProdutoEdicao> edicoesRecebidas;
    private ClassificacaoCota classificacao;
    private BigDecimal reparteCalculado;
    private BigDecimal reparteJuramentadoAFaturar;
    private BigDecimal vendaMediaMaisN;
    private BigDecimal reparteMinimo;
    private BigDecimal reparteMaximo;
    private BigDecimal vendaMedia;
    private BigDecimal vendaMediaNominal;
    private BigDecimal vendaEdicaoMaisRecenteFechada;
    private boolean cotaSoRecebeuEdicaoAberta;
    private BigDecimal percentualEncalheMaximo;
    private BigDecimal somaReparteEdicoesAbertas;
    private BigDecimal indiceCorrecaoTendencia;
    private BigDecimal quantidadePDVs;
    private boolean recebeReparteComplementar;
    // TODO: o q é mix?
    private boolean mix;
    private BigDecimal indiceVendaCrescente;
    private BigDecimal indiceAjusteCota;
    private BigDecimal indiceTratamentoRegional;
    private List<Cota> equivalente;
    private BigDecimal indiceAjusteEquivalente;

    private Long numero;

    public Cota() {
	reparteCalculado = BigDecimal.ZERO;
	classificacao = ClassificacaoCota.SemClassificacao;
    }

    public Long getNumero() {
	return numero;
    }

    public void setNumero(Long numero) {
	this.numero = numero;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public ClassificacaoCota getClassificacao() {
	return classificacao;
    }

    public void setClassificacao(ClassificacaoCota classificacao) {
	this.classificacao = classificacao;
    }

    public BigDecimal getReparteCalculado() {
	return reparteCalculado;
    }

    public void setReparteCalculado(BigDecimal reparteCalculado) {
	this.reparteCalculado = reparteCalculado;
    }

    /**
     * Valor do parâmetro "VendaMedia + n" configurado na tela de Ajuste de Reparte
     * @return {@link BigDecimal}
     */
    public BigDecimal getVendaMediaMaisN() {
	return vendaMediaMaisN;
    }

    public void setVendaMediaMaisN(BigDecimal vendaMediaMaisN) {
	this.vendaMediaMaisN = vendaMediaMaisN;
    }

    /**
     * VendaMediaNominalCota = SomatoriaVendasCota / QtdeEdicoesRecebidasCota
     * É a somatória das Vendas da Cota dividido pela Quantidade de Edições Recebidas por esta Cota
     * @return BigDecimal
     */
    public BigDecimal getVendaMedia() {
	return vendaMedia;
    }

    public void setVendaMedia(BigDecimal vendaMedia) {
	this.vendaMedia = vendaMedia;
    }

    /**
     * Valor do parâmetro ReparteMinimo na tela de bonificações ou na tela Mix de Produto
     * @return {@link BigDecimal}
     */
    public BigDecimal getReparteMinimo() {
	return reparteMinimo;
    }

    public void setReparteMinimo(BigDecimal reparteMinimo) {
	this.reparteMinimo = reparteMinimo;
    }

    /**
     * Venda da edição mais recente fechada da cota.
     * @return {@link BigDecimal}
     */
    public BigDecimal getVendaEdicaoMaisRecenteFechada() {
	if (vendaEdicaoMaisRecenteFechada == null) {
	    // Busca para encontrar qual é a venda da edição mais recente fechada
	    vendaEdicaoMaisRecenteFechada = BigDecimal.ZERO;
	    for (int i = edicoesRecebidas.size() - 1; i >= 0; i--) {
		if (!edicoesRecebidas.get(i).isEdicaoAberta()) {
		    vendaEdicaoMaisRecenteFechada = edicoesRecebidas.get(i).getVenda();
		    break;
		}
	    }
	}
	return vendaEdicaoMaisRecenteFechada;
    }
    
    public void setVendaEdicaoMaisRecenteFechada(BigDecimal vendaEdicaoMaisRecenteFechada) {
	this.vendaEdicaoMaisRecenteFechada = vendaEdicaoMaisRecenteFechada;
    }

    /**
     * Busca para verificar se a cota só receber edições abertas
     * @return boolean
     */
    public boolean isCotaSoRecebeuEdicaoAberta() {
	return cotaSoRecebeuEdicaoAberta;
    }

    public void setCotaSoRecebeuEdicaoAberta(boolean cotaSoRecebeuEdicaoAberta) {
	this.cotaSoRecebeuEdicaoAberta = cotaSoRecebeuEdicaoAberta;
    }

    /**
     * Soma do reparte de todas as edições que essa cota recebeu
     * @return {@link BigDecimal}
     */
    public BigDecimal getSomaReparteEdicoesAbertas() {
	return somaReparteEdicoesAbertas;
    }

    public void setSomaReparteEdicoesAbertas(BigDecimal somaReparteEdicoesAbertas) {
	this.somaReparteEdicoesAbertas = somaReparteEdicoesAbertas;
    }

    public void setPercentualEncalheMaximo(BigDecimal percentualEncalheMaximo) {
	this.percentualEncalheMaximo = percentualEncalheMaximo;
    }

    /**
     * Percentual de encalhe máximo definido na tela Ajuste de Reparte (por Cota)
     * @return {@link BigDecimal}
     */
    public BigDecimal getPercentualEncalheMaximo() {
	return percentualEncalheMaximo;
    }

    public BigDecimal getIndiceCorrecaoTendencia() {
	return indiceCorrecaoTendencia;
    }

    public void setIndiceCorrecaoTendencia(BigDecimal indiceCorrecaoTendencia) {
	this.indiceCorrecaoTendencia = indiceCorrecaoTendencia;
    }

    /**
     * Quantidade de PDV's que a cota possui
     * @return {@link BigDecimal}
     */
    public BigDecimal getQuantidadePDVs() {
	return quantidadePDVs;
    }

    public void setQuantidadePDVs(BigDecimal quantidadePDVs) {
	this.quantidadePDVs = quantidadePDVs;
    }

    public BigDecimal getReparteMaximo() {
	return reparteMaximo;
    }

    public void setReparteMaximo(BigDecimal reparteMaximo) {
	this.reparteMaximo = reparteMaximo;
    }

    public boolean isMix() {
	return mix;
    }

    public void setMix(boolean mix) {
	this.mix = mix;
    }

    public void setReparteJuramentadoAFaturar(BigDecimal reparteJuramentadoAFaturar) {
	this.reparteJuramentadoAFaturar = reparteJuramentadoAFaturar;
    }

    public BigDecimal getReparteJuramentadoAFaturar() {
	return reparteJuramentadoAFaturar;
    }

    /**
     * Todas as edições que essa cota recebeu
     * @return List<{@link ProdutoEdicao}>
     */
    public List<ProdutoEdicao> getEdicoesRecebidas() {
	return edicoesRecebidas;
    }

    public void setEdicoesRecebidas(List<ProdutoEdicao> edicoesRecebidas) {
	this.edicoesRecebidas = edicoesRecebidas;
    }

    public BigDecimal getIndiceVendaCrescente() {
	return indiceVendaCrescente;
    }

    public void setIndiceVendaCrescente(BigDecimal indiceVendaCrescente) {
	this.indiceVendaCrescente = indiceVendaCrescente;
    }

    /**
     * Método que possui o resultado da venda média pura da cota sem as alterações
     * da correção de vendas e outros cálculos
     * @return {@link BigDecimal}
     */
    public BigDecimal getVendaMediaNominal() {
	return vendaMediaNominal;
    }

    public void setVendaMediaNominal(BigDecimal vendaMediaNominal) {
	this.vendaMediaNominal = vendaMediaNominal;
    }

    public BigDecimal getIndiceAjusteCota() {
	return indiceAjusteCota;
    }

    public void setIndiceAjusteCota(BigDecimal indiceAjusteCota) {
	this.indiceAjusteCota = indiceAjusteCota;
    }

    public BigDecimal getIndiceTratamentoRegional() {
	return indiceTratamentoRegional;
    }

    public void setIndiceTratamentoRegional(BigDecimal indiceTratamentoRegional) {
	this.indiceTratamentoRegional = indiceTratamentoRegional;
    }

    public boolean isRecebeReparteComplementar() {
	return recebeReparteComplementar;
    }

    public Long getIdEstudo() {
	return idEstudo;
    }

    public void setIdEstudo(Long idEstudo) {
	this.idEstudo = idEstudo;
    }

    public void setRecebeReparteComplementar(boolean recebeReparteComplementar) {
	this.recebeReparteComplementar = recebeReparteComplementar;
    }

    public List<Cota> getEquivalente() {
	return equivalente;
    }

    public void setEquivalente(List<Cota> equivalente) {
	this.equivalente = equivalente;
    }

    public boolean isNova() {
	return (this.equivalente != null && !this.equivalente.isEmpty());
    }

    public BigDecimal getIndiceAjusteEquivalente() {
	return indiceAjusteEquivalente;
    }

    public void setIndiceAjusteEquivalente(BigDecimal indiceAjusteEquivalente) {
	this.indiceAjusteEquivalente = indiceAjusteEquivalente;
    }

    @Override
    public String toString() {
	return "Cota [id=" + id + ", numero=" + numero + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Cota other = (Cota) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }
}
