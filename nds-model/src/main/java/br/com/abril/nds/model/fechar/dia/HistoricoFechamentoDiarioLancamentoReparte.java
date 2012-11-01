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
@Table(name = "HISTORICO_FECHAMENTO_DIARIO_LANCAMENTO_REPARTE")
@SequenceGenerator(name = "HISTORICO_FECHAMENTO_DIARIO_LANCAMENTO_REPARTE_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoFechamentoDiarioLancamentoReparte implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "HISTORICO_FECHAMENTO_DIARIO_LANCAMENTO_REPARTE_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID")
	private HistoricoFechamentoDiarioConsolidadoReparte historicoConsolidadoReparte;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_PRODUTO_EDICAO")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name="QNT_REPARTE")
	private BigInteger quantidadeReparte;
	
	@Column(name="QNT_SOBRA_EM")
	private BigInteger quantidadeSobraEM;
	
	@Column(name="QNT_FALTA_EM")
	private BigInteger quantidadeFaltaEM;
	
	@Column(name="QNT_TRANSFERENCIA")
	private BigInteger quantidadeTranferencia;
	
	@Column(name="QNT_A_DISTRIBUIR")
	private BigInteger quantidadeADistribuir;
	
	@Column(name="QNT_DISTRIBUIDO")
	private BigInteger quantidadeDistribuido;
	
	@Column(name="QNT_SOBRA_DISTRIBUIDO")
	private BigInteger quantidadeSobraDistribuido;
	
	@Column(name="QNT_DIFERENCA")
	private BigInteger quantidadeDiferenca;

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

	public BigInteger getQuantidadeReparte() {
		return quantidadeReparte;
	}

	public void setQuantidadeReparte(BigInteger quantidadeReparte) {
		this.quantidadeReparte = quantidadeReparte;
	}

	public BigInteger getQuantidadeSobraEM() {
		return quantidadeSobraEM;
	}

	public void setQuantidadeSobraEM(BigInteger quantidadeSobraEM) {
		this.quantidadeSobraEM = quantidadeSobraEM;
	}

	public BigInteger getQuantidadeFaltaEM() {
		return quantidadeFaltaEM;
	}

	public void setQuantidadeFaltaEM(BigInteger quantidadeFaltaEM) {
		this.quantidadeFaltaEM = quantidadeFaltaEM;
	}

	public BigInteger getQuantidadeTranferencia() {
		return quantidadeTranferencia;
	}

	public void setQuantidadeTranferencia(BigInteger quantidadeTranferencia) {
		this.quantidadeTranferencia = quantidadeTranferencia;
	}

	public BigInteger getQuantidadeADistribuir() {
		return quantidadeADistribuir;
	}

	public void setQuantidadeADistribuir(BigInteger quantidadeADistribuir) {
		this.quantidadeADistribuir = quantidadeADistribuir;
	}

	public BigInteger getQuantidadeDistribuido() {
		return quantidadeDistribuido;
	}

	public void setQuantidadeDistribuido(BigInteger quantidadeDistribuido) {
		this.quantidadeDistribuido = quantidadeDistribuido;
	}

	public BigInteger getQuantidadeSobraDistribuido() {
		return quantidadeSobraDistribuido;
	}

	public void setQuantidadeSobraDistribuido(BigInteger quantidadeSobraDistribuido) {
		this.quantidadeSobraDistribuido = quantidadeSobraDistribuido;
	}

	public BigInteger getQuantidadeDiferenca() {
		return quantidadeDiferenca;
	}

	public void setQuantidadeDiferenca(BigInteger quantidadeDiferenca) {
		this.quantidadeDiferenca = quantidadeDiferenca;
	}

	public HistoricoFechamentoDiarioConsolidadoReparte getHistoricoConsolidadoReparte() {
		return historicoConsolidadoReparte;
	}

	public void setHistoricoConsolidadoReparte(
			HistoricoFechamentoDiarioConsolidadoReparte historicoConsolidadoReparte) {
		this.historicoConsolidadoReparte = historicoConsolidadoReparte;
	}
	
	
}
