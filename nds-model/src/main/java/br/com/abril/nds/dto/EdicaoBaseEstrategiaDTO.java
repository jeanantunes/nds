package br.com.abril.nds.dto;

public class EdicaoBaseEstrategiaDTO {

    private ProdutoEdicaoDTO produtoEdicao;
    private Integer peso;
    private Integer periodoEdicao;

    public ProdutoEdicaoDTO getProdutoEdicao() {
        return produtoEdicao;
    }
    public void setProdutoEdicao(ProdutoEdicaoDTO produtoEdicao) {
        this.produtoEdicao = produtoEdicao;
    }
    public Integer getPeso() {
        return peso;
    }
    public void setPeso(Integer peso) {
        this.peso = peso;
    }
    public Integer getPeriodoEdicao() {
        return periodoEdicao;
    }
    public void setPeriodoEdicao(Integer periodoEdicao) {
        this.periodoEdicao = periodoEdicao;
    }
}
