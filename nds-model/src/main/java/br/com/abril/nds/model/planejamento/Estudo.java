package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "ESTUDO", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"DATA_LANCAMENTO", "PRODUTO_EDICAO_ID" }) })
@SequenceGenerator(name = "ESTUDO_SEQ", initialValue = 1, allocationSize = 1)
public class Estudo implements Serializable {
	
	private static final long serialVersionUID = -6789370916662533013L;
	
	@Id
	@GeneratedValue(generator = "ESTUDO_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "QTDE_REPARTE", nullable = false)
	private BigInteger qtdeReparte;
	@Column(name = "DATA_LANCAMENTO", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataLancamento;
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID", nullable = false)
	private ProdutoEdicao produtoEdicao;
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "estudo")
	private Set<Lancamento> lancamentos = new HashSet<Lancamento>();
	
	@OneToMany(mappedBy = "estudo")
	private Set<EstudoCota> estudoCotas = new HashSet<EstudoCota>();
	
	/** Status do Estudo. */
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusLancamento status;
	
	/** Data de cadastro do Estudo no sistema. */
	@Column(name = "DATA_CADASTRO", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;
	
	/** Data de alteração do Estudo no sistema. */
	@Column(name = "DATA_ALTERACAO")
	@Temporal(TemporalType.DATE)
	private Date dataAlteracao;
	
	@Column(name = "LIBERADO")
	private Integer liberado;
	
	@Column(name = "REPARTE_DISTRIBUIR")
	private BigInteger reparteDistribuir;
	
	@Column(name = "DISTRIBUICAO_POR_MULTIPLOS")
	private Integer distribuicaoPorMultiplos; //TODO no estudo usa boolean, verificar alteração
	
	@Column(name = "PACOTE_PADRAO")
	private BigInteger pacotePadrao; //TODO BigDecimal
	
	@Column(name = "PERCENTUAL_PROPORCAO_EXCEDENTE_PDV")
	private BigDecimal percentualProporcaoExcedentePDV;
	
	@Column(name = "PERCENTUAL_PROPORCAO_EXCEDENTE_VENDA")
	private BigDecimal percentualProporcaoExcedenteVenda;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigInteger getQtdeReparte() {
		return qtdeReparte;
	}
	
	public void setQtdeReparte(BigInteger qtdeReparte) {
		this.qtdeReparte = qtdeReparte;
	}
	
	public Date getDataLancamento() {
		return dataLancamento;
	}
	
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public Lancamento getLancamento() {
		return lancamentos.isEmpty() ? null : lancamentos.iterator().next();
	}

	/**
	 * @return the estudoCotas
	 */
	public Set<EstudoCota> getEstudoCotas() {
		return estudoCotas;
	}

	/**
	 * @param estudoCotas the estudoCotas to set
	 */
	public void setEstudoCotas(Set<EstudoCota> estudoCotas) {
		this.estudoCotas = estudoCotas;
	}

	public StatusLancamento getStatus() {
		return status;
	}

	public void setStatus(StatusLancamento status) {
		this.status = status;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Integer getLiberado() {
		return liberado;
	}

	public void setLiberado(Integer liberado) {
		this.liberado = liberado;
	}

	public Set<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(Set<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public BigInteger getReparteDistribuir() {
		return reparteDistribuir;
	}

	public void setReparteDistribuir(BigInteger reparteDistribuir) {
		this.reparteDistribuir = reparteDistribuir;
	}

	public Integer getDistribuicaoPorMultiplos() {
		return distribuicaoPorMultiplos;
	}

	public void setDistribuicaoPorMultiplos(Integer distribuicaoPorMultiplos) {
		this.distribuicaoPorMultiplos = distribuicaoPorMultiplos;
	}

	public BigInteger getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(BigInteger pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	public BigDecimal getPercentualProporcaoExcedentePDV() {
		return percentualProporcaoExcedentePDV;
	}

	public void setPercentualProporcaoExcedentePDV(
			BigDecimal percentualProporcaoExcedentePDV) {
		this.percentualProporcaoExcedentePDV = percentualProporcaoExcedentePDV;
	}

	public BigDecimal getPercentualProporcaoExcedenteVenda() {
		return percentualProporcaoExcedenteVenda;
	}

	public void setPercentualProporcaoExcedenteVenda(
			BigDecimal percentualProporcaoExcedenteVenda) {
		this.percentualProporcaoExcedenteVenda = percentualProporcaoExcedenteVenda;
	}
	
}
