package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.Date;

public class ProdutoEdicao extends GenericDTO<ProdutoEdicao> {

    /**
     * 
     */
    private static final long serialVersionUID = -7621958461677915584L;

    private Long id;
    private String nome;
    private Long numeroEdicao;
    private Long codigoProduto;
    private Integer peso;
    private BigDecimal reparte;
    private BigDecimal venda;
    private boolean parcial;
    private Integer pacotePadrao;
    private boolean edicaoAberta;
    private Long idLancamento;
    private Long idProduto;
    private Date dataLancamento;
    private boolean colecao; // Atributo que define se o Produto é um fascículo/coleção
    private BigDecimal reparteMinimo; // Reparte mínimo configurado na tela de Mix de Produto
    private BigDecimal reparteMaximo; // Reparte máximo configurado na tela de Mix de Produto
    private BigDecimal indiceCorrecao;
    private BigDecimal vendaCorrigida;

    public ProdutoEdicao() {
	this.reparte = BigDecimal.ZERO;
	this.venda = BigDecimal.ZERO;
    	this.peso = 0;
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

    public Long getNumeroEdicao() {
	return numeroEdicao;
    }

    public void setNumeroEdicao(Long numeroEdicao) {
	this.numeroEdicao = numeroEdicao;
    }

    public Long getCodigoProduto() {
	return codigoProduto;
    }

    public void setCodigoProduto(Long codigoProduto) {
	this.codigoProduto = codigoProduto;
    }

    public Integer getPeso() {
	return peso;
    }

    public void setPeso(Integer peso) {
	this.peso = peso;
    }

    public BigDecimal getReparte() {
	return reparte;
    }

    public void setReparte(BigDecimal reparte) {
	this.reparte = reparte;
    }

    public BigDecimal getVenda() {
	return venda;
    }

    public void setVenda(BigDecimal venda) {
	this.venda = venda;
    }

    public boolean isParcial() {
	return parcial;
    }

    public void setParcial(boolean parcial) {
	this.parcial = parcial;
    }

    public Integer getPacotePadrao() {
	return pacotePadrao;
    }

    public void setPacotePadrao(Integer pacotePadrao) {
	this.pacotePadrao = pacotePadrao;
    }

    public boolean isEdicaoAberta() {
	return edicaoAberta;
    }

    public void setEdicaoAberta(boolean edicaoAberta) {
	this.edicaoAberta = edicaoAberta;
    }

    public Long getIdLancamento() {
	return idLancamento;
    }

    public void setIdLancamento(Long idLancamento) {
	this.idLancamento = idLancamento;
    }

    public Long getIdProduto() {
	return idProduto;
    }

    public void setIdProduto(Long idProduto) {
	this.idProduto = idProduto;
    }

    public Date getDataLancamento() {
	return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
	this.dataLancamento = dataLancamento;
    }

    public boolean isColecao() {
	return colecao;
    }

    public void setColecao(boolean colecao) {
	this.colecao = colecao;
    }

    public BigDecimal getReparteMinimo() {
	return reparteMinimo;
    }

    public void setReparteMinimo(BigDecimal reparteMinimo) {
	this.reparteMinimo = reparteMinimo;
    }

    public BigDecimal getReparteMaximo() {
	return reparteMaximo;
    }

    public void setReparteMaximo(BigDecimal reparteMaximo) {
	this.reparteMaximo = reparteMaximo;
    }

    public BigDecimal getIndiceCorrecao() {
	return indiceCorrecao;
    }

    public void setIndiceCorrecao(BigDecimal indiceCorrecao) {
	this.indiceCorrecao = indiceCorrecao;
    }

    public BigDecimal getVendaCorrigida() {
	return vendaCorrigida;
    }

    public void setVendaCorrigida(BigDecimal vendaCorrigida) {
	this.vendaCorrigida = vendaCorrigida;
    }
}
