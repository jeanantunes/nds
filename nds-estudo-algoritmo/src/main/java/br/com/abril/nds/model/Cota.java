package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cota extends GenericDTO<Cota> {

    private static final long serialVersionUID = -2770873222155652560L;

    private Long id;
    private String nomePessoa;
    private Long numero;
    private ClassificacaoCota classificacao;
    private BigDecimal reparteCalculado;
    private BigDecimal reparteJuramentadoAFaturar;
    private BigDecimal vendaMediaMaisN;
    private BigDecimal reparteMinimo;
    private BigDecimal reparteMaximo;
    private BigDecimal vendaMedia;
    private BigDecimal vendaEdicaoMaisRecenteFechada;
    private boolean cotaSoRecebeuEdicaoAberta;
    private List<ProdutoEdicao> edicoesBase;
    private List<ProdutoEdicao> edicoesRecebidas;
    private List<EstoqueProdutoCota> estoqueProdutoCotas;
    private BigDecimal percentualEncalheMaximo;
    // TODO: verificar se essa somatória é de todas as edições da cota ou é somente a última (Processo: Reparte Proporcional)
    private BigDecimal somaReparteEdicoesAbertas;
    private BigDecimal indiceCorrecaoTendencia;
    private BigDecimal quantidadePDVs;
    private boolean mix;
    private BigDecimal indiceVendaCrescente;

    public Cota() {
    	vendaMedia = BigDecimal.ZERO;
    	vendaMediaMaisN = BigDecimal.ZERO;
    	reparteCalculado = BigDecimal.ZERO;
    	reparteMinimo = BigDecimal.ZERO;
    	somaReparteEdicoesAbertas = BigDecimal.ZERO;
    	percentualEncalheMaximo = BigDecimal.ZERO;
    	classificacao = ClassificacaoCota.SemClassificacao;
    	
    	edicoesBase = new ArrayList<ProdutoEdicao>();
    	edicoesRecebidas = new ArrayList<ProdutoEdicao>();
    }

    public void calculate() {
    	// Cálculo da Venda Média Final
    	BigDecimal soma = BigDecimal.ZERO;
    	for (ProdutoEdicao edicao : edicoesRecebidas) {
    	    soma.add(edicao.getVenda());
    	}
    	if (edicoesRecebidas.size() != 0) {
    		vendaMedia = soma.divide(new BigDecimal(edicoesRecebidas.size()), 2, BigDecimal.ROUND_HALF_UP);
    	}

    	// Verificação se a cota só recebeu edições abertas e somatória delas
    	// TODO: confirmar se é para verificar em todas as edições que a cota recebeu mesmo ou somente nas edições bases
    	cotaSoRecebeuEdicaoAberta = true;
    	somaReparteEdicoesAbertas = BigDecimal.ZERO;
    	for (ProdutoEdicao edicao : edicoesRecebidas) {
    		if (edicao.isEdicaoAberta()) {
    			somaReparteEdicoesAbertas = somaReparteEdicoesAbertas.add(edicao.getReparte());
    		} else {
    			cotaSoRecebeuEdicaoAberta = false;
    		}
    	}
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getNomePessoa() {
	return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
	this.nomePessoa = nomePessoa;
    }

    /**
     * Edições que servirão de base para o estudo
     * @return List<{@link ProdutoEdicao}>
     */
    public List<ProdutoEdicao> getEdicoesBase() {
	return edicoesBase;
    }

    public void setEdicoesBase(List<ProdutoEdicao> edicoesBase) {
	this.edicoesBase = edicoesBase;
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

    public BigDecimal getVendaEdicaoMaisRecenteFechada() {
	if (vendaEdicaoMaisRecenteFechada == null) {
	    // Busca para encontrar qual é a venda da edição mais recente fechada
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

    public List<EstoqueProdutoCota> getEstoqueProdutoCotas() {
	return estoqueProdutoCotas;
    }

    public void setEstoqueProdutoCotas(List<EstoqueProdutoCota> estoqueProdutoCotas) {
	this.estoqueProdutoCotas = estoqueProdutoCotas;
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

    public Long getNumero() {
	return numero;
    }

    public void setNumero(Long numero) {
	this.numero = numero;
    }

	public void setReparteJuramentadoAFaturar(BigDecimal reparteJuramentadoAFaturar) {
		this.reparteJuramentadoAFaturar = reparteJuramentadoAFaturar;
	}
}
