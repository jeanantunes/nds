package br.com.abril.nds.dto;

public class EstudoDTO {

    private Long produtoEdicaoId;
    private String dataLancamento;
    private Long reparteDistribuir;
    
    public Long getProdutoEdicaoId() {
	return produtoEdicaoId;
    }
    public void setProdutoEdicaoId(Long produtoEdicaoId) {
	this.produtoEdicaoId = produtoEdicaoId;
    }
    public String getDataLancamento() {
        return dataLancamento;
    }
    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }
    public Long getReparteDistribuir() {
        return reparteDistribuir;
    }
    public void setReparteDistribuir(Long reparteDistribuir) {
        this.reparteDistribuir = reparteDistribuir;
    }
}
