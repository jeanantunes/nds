package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.List;

public class Cota {

	private Long id;
	private String nome;
	private ClassificacaoCota classificacao;
	private BigDecimal reparteCalculado;
	private boolean vendaMediaMaisN;
	private BigDecimal ajusteReparte; // parametro VendaMedia + n na tela de Ajuste de Reparte
	private BigDecimal vendaMediaFinal;
	private BigDecimal reparteMinimo; // parametro ReparteMinimo na tela de bonificações ou na tela Mix de Produto
	private BigDecimal vendaMediaNominalCota; // VendaMediaNominalCota = SomatoriaVendasCota / QtdeEdicoesRecebidasCota
	private BigDecimal vendaEdicaoMaisRecenteFechada;
	private boolean cotaSoRecebeuEdicaoAberta;
	private List<ProdutoEdicao> edicoesBase;
	private List<EstoqueProdutoCota> estoqueProdutoCotas;

	public void calculate() {

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

	public boolean isVendaMediaMaisN() {
		return vendaMediaMaisN;
	}

	public void setVendaMediaMaisN(boolean vendaMediaMaisN) {
		this.vendaMediaMaisN = vendaMediaMaisN;
	}
	/**
	 * O AjusteReparte estará preenchido com o valor do parâmetro VendaMedia + n na tela de Ajuste de Reparte
	 * @return BigDecimal
	 */
	public BigDecimal getAjusteReparte() {
		return ajusteReparte;
	}

	public void setAjusteReparte(BigDecimal ajusteReparte) {
		this.ajusteReparte = ajusteReparte;
	}

	public BigDecimal getVendaMediaFinal() {
		return vendaMediaFinal;
	}

	public void setVendaMediaFinal(BigDecimal vendaMediaFinal) {
		this.vendaMediaFinal = vendaMediaFinal;
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
	/**
	 * VendaMediaNominalCota = SomatoriaVendasCota / QtdeEdicoesRecebidasCota
	 * É a somatória das Vendas Reais da Cota dividido pela Quantidade de Edições Recebidas por esta Cota
	 * @return BigDecimal
	 */
	public BigDecimal getVendaMediaNominalCota() {
		return vendaMediaNominalCota;
	}

	public void setVendaMediaNominalCota(BigDecimal vendaMediaNominalCota) {
		this.vendaMediaNominalCota = vendaMediaNominalCota;
	}

	public BigDecimal getVendaEdicaoMaisRecenteFechada() {
		return vendaEdicaoMaisRecenteFechada;
	}

	public void setVendaEdicaoMaisRecenteFechada(BigDecimal vendaEdicaoMaisRecenteFechada) {
		this.vendaEdicaoMaisRecenteFechada = vendaEdicaoMaisRecenteFechada;
	}
	
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
}
