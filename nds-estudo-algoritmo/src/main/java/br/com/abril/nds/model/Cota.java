package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.List;

public class Cota extends GenericDTO<Cota> {

    private static final long serialVersionUID = -2770873222155652560L;

    private Long id;
    private String nome;
    private ClassificacaoCota classificacao;
    private BigDecimal reparteCalculado;
    private BigDecimal reparteJuramentadoAFaturar;
    private BigDecimal vendaMediaMaisN;
    private BigDecimal reparteMinimo;
    private BigDecimal reparteMaximo;
    private BigDecimal reparteFinalCota;
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
    private boolean recebeReparteComplementar=false;
    
    public Cota() {
    	vendaMedia = BigDecimal.ZERO;
    	vendaMediaMaisN = BigDecimal.ZERO;
    	reparteCalculado = BigDecimal.ZERO;
    	reparteMinimo = BigDecimal.ZERO;
    	classificacao = ClassificacaoCota.SemClassificacao;
    }
    
    public void calculate() {
	// Cálculo da Venda Média Final
	BigDecimal soma = BigDecimal.ZERO;
	for (ProdutoEdicao edicao : edicoesRecebidas) {
	    soma.add(edicao.getVenda());
	}
	vendaMedia = soma.divide(new BigDecimal(edicoesRecebidas.size()), 2,
		BigDecimal.ROUND_FLOOR);
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
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
		    vendaEdicaoMaisRecenteFechada = edicoesRecebidas.get(i)
			    .getVenda();
		    break;
		}
	    }
	}
	return vendaEdicaoMaisRecenteFechada;
    }

    public boolean isCotaSoRecebeuEdicaoAberta() {
	// FIXME: verificar qual é o melhor momento para executar esse trecho de código (for)
	cotaSoRecebeuEdicaoAberta = true;
	// Busca para verificar se a cota só receber edições abertas
	for (int i = 0; i < edicoesRecebidas.size(); i++) {
	    if (!edicoesRecebidas.get(i).isEdicaoAberta()) {
		cotaSoRecebeuEdicaoAberta = false;
		break;
	    }
	}
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

    public void setSomaReparteEdicoesAbertas(
	    BigDecimal somaReparteEdicoesAbertas) {
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

	public void setVendaEdicaoMaisRecenteFechada(
	    BigDecimal vendaEdicaoMaisRecenteFechada) {
	this.vendaEdicaoMaisRecenteFechada = vendaEdicaoMaisRecenteFechada;
    }

    public List<EstoqueProdutoCota> getEstoqueProdutoCotas() {
	return estoqueProdutoCotas;
    }

    public void setEstoqueProdutoCotas(
	    List<EstoqueProdutoCota> estoqueProdutoCotas) {
	this.estoqueProdutoCotas = estoqueProdutoCotas;
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

	public void setReparteJuramentadoAFaturar(BigDecimal reparteJuramentadoAFaturar) {
		this.reparteJuramentadoAFaturar = reparteJuramentadoAFaturar;
	}

	public boolean isRecebeReparteComplementar() {
		return recebeReparteComplementar;
	}

	public void setRecebeReparteComplementar(boolean recebeReparteComplementar) {
		this.recebeReparteComplementar = recebeReparteComplementar;
	}

	public BigDecimal getReparteFinalCota() {
		return reparteFinalCota;
	}

	public void setReparteFinalCota(BigDecimal reparteFinalCota) {
		this.reparteFinalCota = reparteFinalCota;
	}
	
	
	
}
