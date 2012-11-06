package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.math.BigInteger;

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
@Table(name = "FECHAMENTO_DIARIO_LANCAMENTO_SUPLEMENTAR")
@SequenceGenerator(name = "FECHAMENTO_DIARIO_LANCAMENTO_SUPLEMENTAR_SEQ", initialValue = 1, allocationSize = 1)
public class FechamentoDiarioLancamentoSuplementar implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "FECHAMENTO_DIARIO_LANCAMENTO_SUPLEMENTAR_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID")
	private FechamentoDiarioConsolidadoSuplementar fechamentoDiarioConsolidadoSuplementar;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_PRODUTO_EDICAO")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name="QNT_CONTABILIZADA")
	private BigInteger quantidadeContabilizada;
	
	@Column(name="QNT_FISICO")
	private BigInteger quantidadeFisico;
	
	@Column(name="QNT_DIFERENCA")
	private BigInteger quantidadeDiferenca;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FechamentoDiarioConsolidadoSuplementar getFechamentoDiarioConsolidadoSuplementar() {
		return fechamentoDiarioConsolidadoSuplementar;
	}

	public void setFechamentoDiarioConsolidadoSuplementar(
			FechamentoDiarioConsolidadoSuplementar fechamentoDiarioConsolidadoSuplementar) {
		this.fechamentoDiarioConsolidadoSuplementar = fechamentoDiarioConsolidadoSuplementar;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public BigInteger getQuantidadeContabilizada() {
		return quantidadeContabilizada;
	}

	public void setQuantidadeContabilizada(BigInteger quantidadeContabilizada) {
		this.quantidadeContabilizada = quantidadeContabilizada;
	}

	public BigInteger getQuantidadeFisico() {
		return quantidadeFisico;
	}

	public void setQuantidadeFisico(BigInteger quantidadeFisico) {
		this.quantidadeFisico = quantidadeFisico;
	}

	public BigInteger getQuantidadeDiferenca() {
		return quantidadeDiferenca;
	}

	public void setQuantidadeDiferenca(BigInteger quantidadeDiferenca) {
		this.quantidadeDiferenca = quantidadeDiferenca;
	}
	
}
