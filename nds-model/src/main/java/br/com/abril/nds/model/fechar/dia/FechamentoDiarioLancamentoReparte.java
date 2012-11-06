package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;

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
@Table(name = "FECHAMENTO_DIARIO_LANCAMENTO_REPARTE")
@SequenceGenerator(name = "FECHAMENTO_DIARIO_LANCAMENTO_REPARTE_SEQ", initialValue = 1, allocationSize = 1)
public class FechamentoDiarioLancamentoReparte implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "FECHAMENTO_DIARIO_LANCAMENTO_REPARTE_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID")
	private FechamentoDiarioConsolidadoReparte fechamentoDiarioConsolidadoReparte;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_PRODUTO_EDICAO")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name="QNT_REPARTE")
	private Integer quantidadeReparte;
	
	@Column(name="QNT_SOBRA_EM")
	private Integer quantidadeSobraEM;
	
	@Column(name="QNT_FALTA_EM")
	private Integer quantidadeFaltaEM;
	
	@Column(name="QNT_TRANSFERENCIA")
	private Integer quantidadeTranferencia;
	
	@Column(name="QNT_A_DISTRIBUIR")
	private Integer quantidadeADistribuir;
	
	@Column(name="QNT_DISTRIBUIDO")
	private Integer quantidadeDistribuido;
	
	@Column(name="QNT_SOBRA_DISTRIBUIDO")
	private Integer quantidadeSobraDistribuido;
	
	@Column(name="QNT_DIFERENCA")
	private Integer quantidadeDiferenca;

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

	public Integer getQuantidadeReparte() {
		return quantidadeReparte;
	}

	public void setQuantidadeReparte(Integer quantidadeReparte) {
		this.quantidadeReparte = quantidadeReparte;
	}

	public Integer getQuantidadeSobraEM() {
		return quantidadeSobraEM;
	}

	public void setQuantidadeSobraEM(Integer quantidadeSobraEM) {
		this.quantidadeSobraEM = quantidadeSobraEM;
	}

	public Integer getQuantidadeFaltaEM() {
		return quantidadeFaltaEM;
	}

	public void setQuantidadeFaltaEM(Integer quantidadeFaltaEM) {
		this.quantidadeFaltaEM = quantidadeFaltaEM;
	}

	public Integer getQuantidadeTranferencia() {
		return quantidadeTranferencia;
	}

	public void setQuantidadeTranferencia(Integer quantidadeTranferencia) {
		this.quantidadeTranferencia = quantidadeTranferencia;
	}

	public Integer getQuantidadeADistribuir() {
		return quantidadeADistribuir;
	}

	public void setQuantidadeADistribuir(Integer quantidadeADistribuir) {
		this.quantidadeADistribuir = quantidadeADistribuir;
	}

	public Integer getQuantidadeDistribuido() {
		return quantidadeDistribuido;
	}

	public void setQuantidadeDistribuido(Integer quantidadeDistribuido) {
		this.quantidadeDistribuido = quantidadeDistribuido;
	}

	public Integer getQuantidadeSobraDistribuido() {
		return quantidadeSobraDistribuido;
	}

	public void setQuantidadeSobraDistribuido(Integer quantidadeSobraDistribuido) {
		this.quantidadeSobraDistribuido = quantidadeSobraDistribuido;
	}

	public Integer getQuantidadeDiferenca() {
		return quantidadeDiferenca;
	}

	public void setQuantidadeDiferenca(Integer quantidadeDiferenca) {
		this.quantidadeDiferenca = quantidadeDiferenca;
	}

	public FechamentoDiarioConsolidadoReparte getFechamentoDiarioConsolidadoReparte() {
		return fechamentoDiarioConsolidadoReparte;
	}

	public void setFechamentoDiarioConsolidadoReparte(
			FechamentoDiarioConsolidadoReparte fechamentoDiarioConsolidadoReparte) {
		this.fechamentoDiarioConsolidadoReparte = fechamentoDiarioConsolidadoReparte;
	}
	
	
}
