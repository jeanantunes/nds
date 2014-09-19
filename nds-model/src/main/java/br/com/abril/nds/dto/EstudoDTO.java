package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

public class EstudoDTO implements Serializable {

    private static final long serialVersionUID = 8906513605649269719L;

    private Long produtoEdicaoId;
    private String dataLancamento;
    private Long reparteDistribuir;
    private Long reparteDistribuido;
    private Long lancamentoId;
    private BigInteger reparteTotal;
    
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
    public Long getReparteDistribuido() {
        return reparteDistribuido;
    }
    public void setReparteDistribuido(Long reparteDistribuido) {
        this.reparteDistribuido = reparteDistribuido;
    }
    public Long getLancamentoId() {
		return lancamentoId;
	}
	public void setLancamentoId(Long lancamentoId) {
		this.lancamentoId = lancamentoId;
	}
	public BigInteger getReparteTotal() {
		return reparteTotal;
	}
	public void setReparteTotal(BigInteger reparteTotal) {
		this.reparteTotal = reparteTotal;
	}
	
}
