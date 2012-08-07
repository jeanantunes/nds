package br.com.abril.nds.model.planejamento;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "LANCAMENTO", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"DATA_LCTO_PREVISTA", "PRODUTO_EDICAO_ID" }),
	@UniqueConstraint(columnNames = {"DATA_LCTO_DISTRIBUIDOR", "PRODUTO_EDICAO_ID" })
})
@SequenceGenerator(name = "LANCAMENTO_SEQ", initialValue = 1, allocationSize = 1)
public class Lancamento implements Serializable {

	private static final long serialVersionUID = -6120726728298323723L;

	@Id
	@GeneratedValue(generator = "LANCAMENTO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "DATA_CRIACAO", nullable = false)
	private Date dataCriacao;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "DATA_LCTO_PREVISTA", nullable = false)
	private Date dataLancamentoPrevista;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "DATA_LCTO_DISTRIBUIDOR", nullable = false)
	private Date dataLancamentoDistribuidor;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "DATA_REC_PREVISTA", nullable = false)
	private Date dataRecolhimentoPrevista;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "DATA_REC_DISTRIB", nullable = false)
	private Date dataRecolhimentoDistribuidor;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_LANCAMENTO", nullable = false)
	private TipoLancamento tipoLancamento;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_STATUS", nullable = false)
	private Date dataStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private StatusLancamento status;
	
	@Column(name = "REPARTE", nullable = false)
	private BigInteger reparte;
	
	@Column(name = "REPARTE_PROMOCIONAL", nullable = true)
	private BigInteger repartePromocional;
	
	@OneToMany
	private Set<ItemRecebimentoFisico> recebimentos = new HashSet<ItemRecebimentoFisico>();
	
	@OneToMany(mappedBy = "lancamento", cascade = CascadeType.REMOVE)
	private List<HistoricoLancamento> historicos = new ArrayList<HistoricoLancamento>();
	
	@Column(name = "NUMERO_REPROGRAMACOES")
	private Integer numeroReprogramacoes;
	
	@Column(name = "SEQUENCIA_MATRIZ", nullable = true)
	private Integer sequenciaMatriz;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
	@JoinColumns({
		@JoinColumn(name = "PRODUTO_EDICAO_ID", referencedColumnName = "PRODUTO_EDICAO_ID", insertable = false, updatable = false),
		@JoinColumn(name = "DATA_LCTO_PREVISTA", referencedColumnName = "DATA_LANCAMENTO", insertable = false, updatable = false) })
	private Estudo estudo;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "EXPEDICAO_ID")
	private Expedicao expedicao;
	
	@OneToOne(mappedBy="lancamento")
	private PeriodoLancamentoParcial periodoLancamentoParcial;

	@OneToMany(mappedBy = "lancamento")
	private List<MovimentoEstoqueCota> movimentoEstoqueCotas;
	
	@ManyToMany(mappedBy="lancamentos", targetEntity=ChamadaEncalhe.class)
	private Set<ChamadaEncalhe> chamadaEncalhe;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDataCriacao() {
		return dataCriacao;
	}
	
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	public Date getDataLancamentoPrevista() {
		return dataLancamentoPrevista;
	}
	
	public void setDataLancamentoPrevista(Date dataLancamentoPrevista) {
		this.dataLancamentoPrevista = dataLancamentoPrevista;
	}
	
	public Date getDataLancamentoDistribuidor() {
		return dataLancamentoDistribuidor;
	}
	
	public void setDataLancamentoDistribuidor(Date dataLancamentoDistribuidor) {
		this.dataLancamentoDistribuidor = dataLancamentoDistribuidor;
	}
	
	public Date getDataRecolhimentoPrevista() {
		return dataRecolhimentoPrevista;
	}
	
	public void setDataRecolhimentoPrevista(Date dataRecolhimentoPrervista) {
		this.dataRecolhimentoPrevista = dataRecolhimentoPrervista;
	}
	
	public Date getDataRecolhimentoDistribuidor() {
		return dataRecolhimentoDistribuidor;
	}
	
	public void setDataRecolhimentoDistribuidor(Date dataRecolhimentoDistribuidor) {
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
	}
	
	public Date getDataStatus() {
		return dataStatus;
	}
	
	public void setDataStatus(Date dataStatus) {
		this.dataStatus = dataStatus;
	}
	
	public StatusLancamento getStatus() {
		return status;
	}
	
	public void setStatus(StatusLancamento status) {
		this.status = status;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}
	
	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}
	
	public BigInteger getReparte() {
		return reparte;
	}
	
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	
	public Set<ItemRecebimentoFisico> getRecebimentos() {
		return recebimentos;
	}
	
	public void setRecebimentos(Set<ItemRecebimentoFisico> recebimentos) {
		this.recebimentos = recebimentos;
	}
	
	public List<HistoricoLancamento> getHistoricos() {
		return historicos;
	}
	
	public void setHistoricos(List<HistoricoLancamento> historicos) {
		this.historicos = historicos;
	}
	
	public void addRecebimento(ItemRecebimentoFisico itemRecebimentoFisico) {
		this.recebimentos.add(itemRecebimentoFisico);
	}
	
	public Integer getNumeroReprogramacoes() {
		return numeroReprogramacoes;
	}
	
	public void setNumeroReprogramacoes(Integer numeroReprogramacoes) {
		this.numeroReprogramacoes = numeroReprogramacoes;
	}
	
	public Integer getSequenciaMatriz() {
		return sequenciaMatriz;
	}

	public void setSequenciaMatriz(Integer sequenciaMatriz) {
		this.sequenciaMatriz = sequenciaMatriz;
	}

	public Estudo getEstudo() {
		return estudo;
	}
	
	public void setEstudo(Estudo estudo) {
		this.estudo = estudo;
	}

	public BigInteger getTotalRecebimentoFisico() {
		BigInteger total = BigInteger.ZERO;
		for (ItemRecebimentoFisico recebimento : recebimentos) {
			total = total.add(recebimento.getQtdeFisico());
		}
		return total;
	}

	public Expedicao getExpedicao() {
		return expedicao;
	}

	public void setExpedicao(Expedicao expedicao) {
		this.expedicao = expedicao;
	}
	
	public boolean isFuro() {
		return StatusLancamento.FURO.equals(status);
	}
	
	public boolean isCancelamentoGD() {
		return StatusLancamento.CANCELADO_GD.equals(status);
	}
	
	public boolean isExpedido() {
		return StatusLancamento.EXPEDIDO.equals(status);
	}

	public boolean isEstudoFechado() {
		return StatusLancamento.ESTUDO_FECHADO.equals(status);
	}
	
	public boolean isSemRecebimentoFisico() {
		return recebimentos.isEmpty();
	}

	/**
	 * @return the repartePromocional
	 */
	public BigInteger getRepartePromocional() {
		return repartePromocional;
	}

	/**
	 * @param repartePromocional the repartePromocional to set
	 */
	public void setRepartePromocional(BigInteger repartePromocional) {
		this.repartePromocional = repartePromocional;
	}

	/**
	 * @return the periodoLancamentoParcial
	 */
	public PeriodoLancamentoParcial getPeriodoLancamentoParcial() {
		return periodoLancamentoParcial;
	}

	/**
	 * @param periodoLancamentoParcial the periodoLancamentoParcial to set
	 */
	public void setPeriodoLancamentoParcial(
			PeriodoLancamentoParcial periodoLancamentoParcial) {
		this.periodoLancamentoParcial = periodoLancamentoParcial;
	}

	/**
	 * @return the movimentoEstoqueCotas
	 */
	public List<MovimentoEstoqueCota> getMovimentoEstoqueCotas() {
		return movimentoEstoqueCotas;
	}

	/**
	 * @param movimentoEstoqueCotas the movimentoEstoqueCotas to set
	 */
	public void setMovimentoEstoqueCotas(
			List<MovimentoEstoqueCota> movimentoEstoqueCotas) {
		this.movimentoEstoqueCotas = movimentoEstoqueCotas;
	}

	/**
	 * @return the chamadaEncalhe
	 */
	public Set<ChamadaEncalhe> getChamadaEncalhe() {
		return chamadaEncalhe;
	}

	/**
	 * @param chamadaEncalhe the chamadaEncalhe to set
	 */
	public void setChamadaEncalhe(Set<ChamadaEncalhe> chamadaEncalhe) {
		this.chamadaEncalhe = chamadaEncalhe;
	}

	
	

}
