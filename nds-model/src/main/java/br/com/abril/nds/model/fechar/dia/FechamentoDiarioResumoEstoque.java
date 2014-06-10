package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.estoque.TipoEstoque;

@Entity
@Table(name = "FECHAMENTO_DIARIO_RESUMO_ESTOQUE")
@SequenceGenerator(name = "FECHAMENTO_DIARIO_RESUMO_ESTOQUE_SEQ", initialValue = 1, allocationSize = 1)
public class FechamentoDiarioResumoEstoque implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "FECHAMENTO_DIARIO_RESUMO_ESTOQUE_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_ID")
	private FechamentoDiario fechamentoDiario;
	
	@Column(name="TIPO_ESTOQUE")
	@Enumerated(EnumType.STRING)
	private TipoEstoque tipoEstoque;
	
	@Column(name="QNT_PRODUTO")
	private Integer quantidadeProduto;
	
	@Column(name="QNT_EXEMPLARES")
	private Integer quantidadeExemplares;
	
	@Column(name="VALOR_TOTAL")
	private BigDecimal valorTotal;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoEstoque getTipoEstoque() {
		return tipoEstoque;
	}

	public void setTipoEstoque(TipoEstoque tipoEstoque) {
		this.tipoEstoque = tipoEstoque;
	}

	public Integer getQuantidadeProduto() {
		return quantidadeProduto;
	}

	public void setQuantidadeProduto(Integer quantidadeProduto) {
		this.quantidadeProduto = quantidadeProduto;
	}

	public Integer getQuantidadeExemplares() {
		return quantidadeExemplares;
	}

	public void setQuantidadeExemplares(Integer quantidadeExemplares) {
		this.quantidadeExemplares = quantidadeExemplares;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public FechamentoDiario getFechamentoDiario() {
		return fechamentoDiario;
	}

	public void setFechamentoDiario(FechamentoDiario fechamentoDiario) {
		this.fechamentoDiario = fechamentoDiario;
	}
	
}
