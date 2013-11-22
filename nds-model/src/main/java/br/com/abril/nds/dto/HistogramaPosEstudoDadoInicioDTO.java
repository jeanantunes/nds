package br.com.abril.nds.dto;

import java.io.Serializable;

public class HistogramaPosEstudoDadoInicioDTO implements Serializable {

    private static final long serialVersionUID = -7594276887499370636L;

    private String classificacao;
    private String nomeProduto;
    private String codigoProduto;
    private String dataLcto;
    private String edicao;
    private String estudo;
    private Long idLancamento;
    private Integer lncto;
    private Integer repDistrib;
    private Integer sobra;
    private Integer periodicidadeProduto;
    private boolean estudoLiberado;
    private boolean parcial;
    private TipoSegmentoProdutoDTO tipoSegmentoProduto;

    public String getClassificacao() {
	return classificacao;
    }

    public TipoSegmentoProdutoDTO getTipoSegmentoProduto() {
        return tipoSegmentoProduto;
    }

    public void setTipoSegmentoProduto(TipoSegmentoProdutoDTO tipoSegmentoProduto) {
        this.tipoSegmentoProduto = tipoSegmentoProduto;
    }

    public void setClassificacao(String classificacao) {
	this.classificacao = classificacao;
    }

    public String getNomeProduto() {
	return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
	this.nomeProduto = nomeProduto;
    }

    public String getCodigoProduto() {
	return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
	this.codigoProduto = codigoProduto;
    }

    public String getDataLcto() {
	return dataLcto;
    }

    public void setDataLcto(String dataLcto) {
	this.dataLcto = dataLcto;
    }

    public String getEdicao() {
	return edicao;
    }

    public void setEdicao(String edicao) {
	this.edicao = edicao;
    }

    public String getEstudo() {
	return estudo;
    }

    public void setEstudo(String estudo) {
	this.estudo = estudo;
    }

    public Long getIdLancamento() {
	return idLancamento;
    }

    public void setIdLancamento(Long idLancamento) {
	this.idLancamento = idLancamento;
    }

    public Integer getLncto() {
	return lncto;
    }

    public void setLncto(Integer lncto) {
	this.lncto = lncto;
    }

    public Integer getRepDistrib() {
	return repDistrib;
    }

    public void setRepDistrib(Integer repDistrib) {
	this.repDistrib = repDistrib;
    }

    public Integer getSobra() {
	return sobra;
    }

    public void setSobra(Integer sobra) {
	this.sobra = sobra;
    }

    public Integer getPeriodicidadeProduto() {
	return periodicidadeProduto;
    }

    public void setPeriodicidadeProduto(Integer periodicidadeProduto) {
	this.periodicidadeProduto = periodicidadeProduto;
    }

    public boolean isEstudoLiberado() {
	return estudoLiberado;
    }

    public void setEstudoLiberado(boolean estudoLiberado) {
	this.estudoLiberado = estudoLiberado;
    }

    public boolean isParcial() {
	return parcial;
    }

    public void setParcial(boolean parcial) {
	this.parcial = parcial;
    }
}
