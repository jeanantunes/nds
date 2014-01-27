package br.com.abril.nds.model.planejamento;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "ESTRATEGIA_BASE_DISTRIBUICAO")
@SequenceGenerator(name = "ESTRATEGIA_BASE_DISTRIBUICAO_SEQ", initialValue = 1, allocationSize = 1)
public class EdicaoBaseEstrategia implements Serializable {

    private static final long serialVersionUID = 5092043864439659671L;

    @Id
    @GeneratedValue(generator = "ESTRATEGIA_BASE_DISTRIBUICAO_SEQ")
    @Column(name = "ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "PRODUTO_EDICAO_ID", nullable = false)
    private ProdutoEdicao produtoEdicao;
    
    @Column(name = "PESO", nullable = false)
    private Integer peso;
    
    @Column(name = "PERIODO_EDICAO")
    private Integer periodoEdicao;
    
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "ESTRATEGIA_ID", nullable = false)
    private Estrategia estrategia;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
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

	public Estrategia getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(Estrategia estrategia) {
		this.estrategia = estrategia;
	}
}
