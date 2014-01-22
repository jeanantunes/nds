package br.com.abril.nds.dto;

import java.io.Serializable;

public class EstudoDTO implements Serializable {

    private static final long serialVersionUID = 8906513605649269719L;

    private Long produtoEdicaoId;
    private String dataLancamento;
    private Long reparteDistribuir;
    private Long lancamentoId;
    
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
	public Long getLancamentoId() {
		return lancamentoId;
	}
	public void setLancamentoId(Long lancamentoId) {
		this.lancamentoId = lancamentoId;
	}
}
