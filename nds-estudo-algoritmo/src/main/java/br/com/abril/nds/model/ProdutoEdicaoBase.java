package br.com.abril.nds.model;

import java.util.Date;

public class ProdutoEdicaoBase extends GenericDTO<ProdutoEdicao> {

    private static final long serialVersionUID = 3298542950250981102L;

    private Long id;
    private Long idEstudo;
    private Long numeroEdicao;
    private Long idProduto;
    private String codigoProduto;
    private boolean parcial;
    private boolean edicaoAberta;
    private Integer peso;
    private Long idLancamento;
    private Date dataLancamento;
    private boolean colecao; // Atributo que define se o Produto é um fascículo/coleção
    private Integer periodo;
    private TipoSegmentoProduto tipoSegmentoProduto;

    public ProdutoEdicaoBase() {
	peso = 1;
    }

    public ProdutoEdicaoBase(String codigoProduto) {
	this();
	this.codigoProduto = codigoProduto;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getNumeroEdicao() {
	return numeroEdicao;
    }

    public void setNumeroEdicao(Long numeroEdicao) {
	this.numeroEdicao = numeroEdicao;
    }

    public Integer getPeso() {
	return peso;
    }

    public void setPeso(Integer peso) {
	this.peso = peso;
    }

    public Long getIdProduto() {
	return idProduto;
    }

    public void setIdProduto(Long idProduto) {
	this.idProduto = idProduto;
    }

    public String getCodigoProduto() {
	return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
	this.codigoProduto = codigoProduto;
    }

    public boolean isParcial() {
	return parcial;
    }

    public void setParcial(boolean parcial) {
	this.parcial = parcial;
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

    public Long getIdEstudo() {
	return idEstudo;
    }

    public void setIdEstudo(Long idEstudo) {
	this.idEstudo = idEstudo;
    }

    @Override
    public String toString() {
	return String.valueOf(numeroEdicao);
    }

    public Integer getPeriodo() {
	return periodo;
    }

    public void setPeriodo(Integer periodo) {
	this.periodo = periodo;
    }

    public TipoSegmentoProduto getTipoSegmentoProduto() {
	return tipoSegmentoProduto;
    }

    public void setTipoSegmentoProduto(TipoSegmentoProduto tipoSegmentoProduto) {
	this.tipoSegmentoProduto = tipoSegmentoProduto;
    }

}
