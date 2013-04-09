package br.com.abril.nds.model.integracao.icd;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class IcdEdicaoBaseEstrategia {

	@Id
	private Long id;
    private String codigoProduto;
    private Long numeroEdicao;
    private Integer periodo;
    private Integer peso;
    @ManyToOne
    private IcdEstrategia estrategia;
    
    public String getCodigoProduto() {
        return codigoProduto;
    }
    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }
    public Long getNumeroEdicao() {
        return numeroEdicao;
    }
    public void setNumeroEdicao(Long numeroEdicao) {
        this.numeroEdicao = numeroEdicao;
    }
    public Integer getPeriodo() {
        return periodo;
    }
    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }
    public Integer getPeso() {
        return peso;
    }
    public void setPeso(Integer peso) {
        this.peso = peso;
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public IcdEstrategia getEstrategia() {
		return estrategia;
	}
	public void setEstrategia(IcdEstrategia estrategia) {
		this.estrategia = estrategia;
	}
}
