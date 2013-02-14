package br.com.abril.nds.model;

import java.math.BigDecimal;

public class EstoqueProdutoCota extends GenericDTO<EstoqueProdutoCota> {

    /**
     * 
     */
    private static final long serialVersionUID = -2130979892047102000L;

    private Long id;
    private BigDecimal quantidadeDevolvida;
    private BigDecimal quantidadeRecebida;
    private Integer versao;
    private Cota cota;
    private ProdutoEdicao produtoEdicao;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public BigDecimal getQuantidadeDevolvida() {
	return quantidadeDevolvida;
    }

    public void setQuantidadeDevolvida(BigDecimal quantidadeDevolvida) {
	this.quantidadeDevolvida = quantidadeDevolvida;
    }

    public BigDecimal getQuantidadeRecebida() {
	return quantidadeRecebida;
    }

    public void setQuantidadeRecebida(BigDecimal quantidadeRecebida) {
	this.quantidadeRecebida = quantidadeRecebida;
    }

    public Integer getVersao() {
	return versao;
    }

    public void setVersao(Integer versao) {
	this.versao = versao;
    }

    public Cota getCota() {
	return cota;
    }

    public void setCota(Cota cota) {
	this.cota = cota;
    }

    public ProdutoEdicao getProdutoEdicao() {
	return produtoEdicao;
    }

    public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
	this.produtoEdicao = produtoEdicao;
    }

}
