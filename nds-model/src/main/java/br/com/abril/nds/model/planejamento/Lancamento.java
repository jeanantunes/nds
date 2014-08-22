package br.com.abril.nds.model.planejamento;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "LANCAMENTO", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"DATA_LCTO_DISTRIBUIDOR", "PRODUTO_EDICAO_ID", "PERIODO_LANCAMENTO_PARCIAL_ID" })
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
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "DATA_FIN_MAT_DISTRIB")
	private Date dataFinMatDistrib;
	
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
	
	@OneToMany(orphanRemoval=true)
	private Set<ItemRecebimentoFisico> recebimentos = new HashSet<ItemRecebimentoFisico>();
	
	@OneToMany(mappedBy = "lancamento", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<HistoricoLancamento> historicos = new ArrayList<HistoricoLancamento>();
		
	@Column(name = "NUMERO_REPROGRAMACOES")
	private Integer numeroReprogramacoes;
	
	@Column(name = "SEQUENCIA_MATRIZ", nullable = true)
	private Integer sequenciaMatriz;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "ESTUDO_ID", insertable = true, updatable = true)
	private Estudo estudo;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "EXPEDICAO_ID")
	private Expedicao expedicao;
	
	@ManyToOne
    @JoinColumn(name = "PERIODO_LANCAMENTO_PARCIAL_ID")
	private PeriodoLancamentoParcial periodoLancamentoParcial;

	@OneToMany(mappedBy = "lancamento")
	private List<MovimentoEstoqueCota> movimentoEstoqueCotas;
	
	@ManyToMany(mappedBy="lancamentos", targetEntity=ChamadaEncalhe.class)
	private Set<ChamadaEncalhe> chamadaEncalhe;
	
	
	@Column(name="ALTERADO_INTERFACE", nullable = false)
	private boolean alteradoInteface = false;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@Column(name = "NUMERO_LANCAMENTO", nullable = true)
	private Integer numeroLancamento;
	
	@Column(name="JURAMENTADO", nullable = true)
    private Boolean juramentado;
	
	@Transient
	public static final long PEB_MINIMA_LANCAMENTO = 7;

	public Lancamento(Long id) {
		this.id= id; 
	}
	
	public Lancamento() {
		
	}

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
	
	public boolean isCancelamento() {
		return StatusLancamento.CANCELADO.equals(status);
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

	/**
	 * @return the alteradoInteface
	 */
	public boolean isAlteradoInteface() {
		return alteradoInteface;
	}

	/**
	 * @param alteradoInteface the alteradoInteface to set
	 */
	public void setAlteradoInteface(boolean alteradoInteface) {
		this.alteradoInteface = alteradoInteface;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the numeroLancamento
	 */
	public Integer getNumeroLancamento() {
		return numeroLancamento;
	}

	/**
	 * @param numeroLancamento the numeroLancamento to set
	 */
	public void setNumeroLancamento(Integer numeroLancamento) {
		this.numeroLancamento = numeroLancamento;
	}

	
    public Boolean getJuramentado() {
        return juramentado;
    }

    
    public void setJuramentado(Boolean juramentado) {
        this.juramentado = juramentado;
    }

    public Date getDataFinMatDistrib() {
		return dataFinMatDistrib;
	}

	public void setDataFinMatDistrib(Date dataFinMatDistrib) {
		this.dataFinMatDistrib = dataFinMatDistrib;
	}
	
	public void voltarStatusOriginal() {
		
		if (this.produtoEdicao == null) {
			
			return;
		}

		Origem origem = this.produtoEdicao.getOrigem();
		
		if (Origem.MANUAL.equals(origem)) {
			
			this.status = StatusLancamento.PLANEJADO;
		
		} else if (Origem.INTERFACE.equals(origem)) {
			
			this.status = StatusLancamento.CONFIRMADO;
		}

	}
	
	
	/**
	 * Caso o lançamento seja parcial, será necessário reajustar os próximos períodos para se adequar
	 * à nova data de recolhimento. <br/>
	 * Ambas datas devem ser validadas antes. Este método não valida se o dia é útil ou não.
	 * 
	 * <br /> <br />
	 * 
	 * <b> Utilizar este método dentro de uma transação para garantir a integridade do objeto. </b>
	 * 
	 * @param dataRecolhimento - Data de recolhimento do lançamento atual. 
	 * @param proximaDataLancamento - Data do próximo lançamento neste período 
	 * (calculada, conforme o parâmetro <code> Fator de Relançamento Parcial </code> do distribuidor).
	 */
	public void novoRecolhimentoParcial(Date dataRecolhimento, Date proximaDataLancamento) {
		
		this.ajustarRecolhimentoParcial(dataRecolhimento, proximaDataLancamento);
	}

	/**
	 * Quando se altera um lançamento parcial, é necessário reajustar o lançamento do
	 * próximo período para se adequar à nova data de recolhimento. <br/>
	 * Ambas datas devem ser validadas antes. Este método não valida se o dia é útil ou não.
	 * 
	 * <br /> <br />
	 * 
	 * <b> Utilizar este método dentro de uma transação para garantir a integridade do objeto. </b>
	 * 
	 * @param dataRecolhimento - Data de recolhimento do lançamento atual. 
	 * @param proximaDataLancamento - Data do próximo lançamento neste período 
	 * (calculada, conforme o parâmetro <code> Fator de Relançamento Parcial </code> do distribuidor).
	 */
	public void alterarRecolhimentoParcial(Date dataRecolhimento, Date proximaDataLancamento) {
		
		this.ajustarRecolhimentoParcial(dataRecolhimento, proximaDataLancamento);
	}

	/**
	 * Realiza os ajustes do lançamento e os períodos que o sucedem. 
	 * 
	 * @param dataRecolhimento
	 * @param proximaDataLancamento
	 */
	private void ajustarRecolhimentoParcial(Date dataRecolhimento, Date proximaDataLancamento) {

		this.dataRecolhimentoDistribuidor = dataRecolhimento;

		if (this.periodoLancamentoParcial == null) {

			return;
		}

		this.periodoLancamentoParcial.reajustarProximoLancamentoParcial(proximaDataLancamento);
	}

	public boolean isRecolhido() {
		
		return Arrays.asList(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO,
							 StatusLancamento.BALANCEADO_RECOLHIMENTO,
							 StatusLancamento.EM_RECOLHIMENTO,
							 StatusLancamento.RECOLHIDO,
							 StatusLancamento.FECHADO).contains(this.status);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getNumeroLancamento() == null) ? 0 : this.getNumeroLancamento().hashCode());
		result = prime * result + ((this.getProdutoEdicao() == null) ? 0 : this.getProdutoEdicao().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lancamento other = (Lancamento) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getNumeroLancamento() == null) {
			if (other.getNumeroLancamento() != null)
				return false;
		} else if (!this.getNumeroLancamento().equals(other.getNumeroLancamento()))
			return false;
		if (this.getProdutoEdicao() == null) {
			if (other.getProdutoEdicao() != null)
				return false;
		} else if (!this.getProdutoEdicao().equals(other.getProdutoEdicao()))
			return false;
		return true;
	}

}
