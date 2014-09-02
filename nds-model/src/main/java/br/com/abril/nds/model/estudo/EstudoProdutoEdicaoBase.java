package br.com.abril.nds.model.estudo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.EstudoGerado;

@Entity
@SuppressWarnings("serial")
@Table(name = "estudo_produto_edicao_base")
@SequenceGenerator(name="ESTUDO_PRODUTO_EDICAO_BASE_SEQ", initialValue = 1, allocationSize = 1)
public class EstudoProdutoEdicaoBase implements Serializable {

	@Id
    @GeneratedValue(generator = "ESTUDO_PRODUTO_EDICAO_BASE_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "ESTUDO_ID")
    private EstudoGerado estudo;
	
	@ManyToOne
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name = "COLECAO", nullable = false)
	private Long colecao;
	
	@Column(name = "PARCIAL", nullable = false)
	private Long parcial;
	
	@Column(name = "EDICAO_ABERTA", nullable = false)
	private Long edicaoAberta;
	
	@Column(name = "PESO", nullable = false)
	private Long peso;
	
	@Column(name = "VENDA_CORRIGIDA")
	private BigDecimal vendaCorrigida;
	
	@Column(name = "PERIODO_PARCIAL")
	private Long periodoParcial;
	
	@Column(name = "isConsolidado")
	private boolean isConsolidado = false;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EstudoGerado getEstudo() {
		return estudo;
	}

	public void setEstudo(EstudoGerado estudo) {
		this.estudo = estudo;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public Long getColecao() {
		return colecao;
	}

	public void setColecao(Long colecao) {
		this.colecao = colecao;
	}

	public Long getParcial() {
		return parcial;
	}

	public void setParcial(Long parcial) {
		this.parcial = parcial;
	}

	public Long getEdicaoAberta() {
		return edicaoAberta;
	}

	public void setEdicaoAberta(Long edicaoAberta) {
		this.edicaoAberta = edicaoAberta;
	}

	public Long getPeso() {
		return peso;
	}

	public void setPeso(Long peso) {
		this.peso = peso;
	}

	public BigDecimal getVendaCorrigida() {
		return vendaCorrigida;
	}

	public void setVendaCorrigida(BigDecimal vendaCorrigida) {
		this.vendaCorrigida = vendaCorrigida;
	}

	public Long getPeriodoParcial() {
		return periodoParcial;
	}

	public void setPeriodoParcial(Long periodoParcial) {
		this.periodoParcial = periodoParcial;
	}

	public boolean isConsolidado() {
		return isConsolidado;
	}

	public void setConsolidado(boolean isConsolidado) {
		this.isConsolidado = isConsolidado;
	}
	
}
