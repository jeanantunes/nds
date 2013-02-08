package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.List;

public class Cota {

	private Long id;
	private String nome;
	private ClassificacaoCota classificacao;
	private BigDecimal reparteCalculado;
	private BigDecimal vendaMediaMaisN; // parametro VendaMedia + n na tela de Ajuste de Reparte
	private BigDecimal reparteMinimo; // parametro ReparteMinimo na tela de bonificações ou na tela Mix de Produto
	private BigDecimal vendaMedia; // VendaMediaNominalCota = SomatoriaVendasCota / QtdeEdicoesRecebidasCota
	private BigDecimal vendaEdicaoMaisRecenteFechada;
	private boolean cotaSoRecebeuEdicaoAberta;
	private List<ProdutoEdicao> edicoesBase; // edições que servirão de base para o estudo
	private List<ProdutoEdicao> edicoesRecebidas; // todas as edições que essa cota recebeu
	private List<EstoqueProdutoCota> estoqueProdutoCotas;
	private BigDecimal percentualEncalheMaximo; // Percentual de encalhe máximo definido na tela Ajuste de Reparte (por Cota)
	// TODO: verificar se essa somatória é de todas as edições da cota ou é somente a última (Processo: Reparte Proporcional)
	private BigDecimal somaReparteEdicoesAbertas; // Soma do Reparte de todas as edições que essa cota recebeu

	public void calculate() {
		// Cálculo da Venda Média Final
		BigDecimal soma = new BigDecimal(0);
		for (ProdutoEdicao edicao : edicoesRecebidas) {
			soma.add(edicao.getVenda());
		}
		vendaMedia = soma.divide(new BigDecimal(edicoesRecebidas.size()), 2, BigDecimal.ROUND_FLOOR);

		// Cálculo da Venda Média Nominal
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
	 * O AjusteReparte estará preenchido com o valor do parâmetro VendaMedia + n na tela de Ajuste de Reparte
	 * @return BigDecimal
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
	 * O ReparteMinimo estará preenchido com o valor parâmetro ReparteMinimo na tela de bonificações ou na tela Mix de Produto
	 * @return BigDecimal
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
	
	public List<EstoqueProdutoCota> getEstoqueProdutoCotas() {
		return estoqueProdutoCotas;
	}

	public void setEstoqueProdutoCotas(List<EstoqueProdutoCota> estoqueProdutoCotas) {
		this.estoqueProdutoCotas = estoqueProdutoCotas;
	}

	public List<ProdutoEdicao> getEdicoesRecebidas() {
		return edicoesRecebidas;
	}

	public void setEdicoesRecebidas(List<ProdutoEdicao> edicoesRecebidas) {
		this.edicoesRecebidas = edicoesRecebidas;
	}

	public BigDecimal getSomaReparteEdicoesAbertas() {
		return somaReparteEdicoesAbertas;
	}

	public void setSomaReparteEdicoesAbertas(BigDecimal somaReparteEdicoesAbertas) {
		this.somaReparteEdicoesAbertas = somaReparteEdicoesAbertas;
	}

	public BigDecimal getPercentualEncalheMaximo() {
		return percentualEncalheMaximo;
	}

	public void setPercentualEncalheMaximo(BigDecimal percentualEncalheMaximo) {
		this.percentualEncalheMaximo = percentualEncalheMaximo;
	}
}
