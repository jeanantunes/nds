package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.util.DateUtil;

/**
 * Cadastro de período de lançamento parcial,
 * com a definição de um período de lançamento e 
 * recolhimento definido respeitando os limites
 * de data do lancamento parcial 
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "PERIODO_LANCAMENTO_PARCIAL")
@SequenceGenerator(name = "PERIODO_LANCAMENTO_PARCIAL_SEQ", initialValue = 1, allocationSize = 1)
public class PeriodoLancamentoParcial implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2248452950369852623L;

	@Id
	@GeneratedValue(generator = "PERIODO_LANCAMENTO_PARCIAL_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "LANCAMENTO_PARCIAL_ID")
	private LancamentoParcial lancamentoParcial;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private StatusLancamentoParcial status;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO", nullable = false)
	private TipoLancamentoParcial tipo;

	@OneToMany(mappedBy="periodoLancamentoParcial", cascade=CascadeType.ALL)
	private List<Lancamento> lancamentos;
	
	/** Número do Período do lançamento parcial. */
	@Column(name = "NUMERO_PERIODO", nullable = false)
	private Integer numeroPeriodo;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "DATA_CRIACAO")
	private Date dataCriacao;
	
	public PeriodoLancamentoParcial() {
		
	}
	/**
	 * Retorna o lançamento do periodo que não é redistribuição.
	 * @return lancamento
	 */
	public Lancamento getLancamentoPeriodoParcial(){
		
		if(this.lancamentos == null ) {
			return null;
		}
		
		for(Lancamento item : this.lancamentos) {
			if(TipoLancamento.LANCAMENTO.equals(item.getTipoLancamento())) {
				return item;
			}
		}
		
		throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum lançamento do tipo \"LANCAMENTO\" foi encontrado para este período.");
	}

	public Lancamento getPrimeiroLancamento() {
		
		return this.getLancamento(1);
	}

	public Lancamento getUltimoLancamento() {
		
		if(this.lancamentos == null || this.lancamentos.isEmpty()) {
			return null;
		}
		
		Lancamento maiorLancamento = null;
		
		Integer maiorNumeroLancamento = null;
		
		for (Lancamento item : this.lancamentos) {
			
			if (item.getTipoLancamento() != null && item.getTipoLancamento().equals(TipoLancamento.LANCAMENTO)
					&& (maiorNumeroLancamento == null || maiorNumeroLancamento < item.getNumeroLancamento())) {
				
				maiorNumeroLancamento = item.getNumeroLancamento();
				
				maiorLancamento = item;
			}
		}
		
		return maiorLancamento;
	}
	
	public Lancamento getLancamento(Integer numeroLancamento) {
		
		if(this.lancamentos == null || this.lancamentos.isEmpty()) {
			return null;
		}
		
		for (Lancamento lancamento : this.lancamentos) {
			
			if (numeroLancamento.equals(lancamento.getNumeroLancamento())) {
				
				return lancamento;
			}
		}
		
		return null;
	}
	
	public Lancamento getLancamentoAnterior(Integer numeroLancamento) {
		
		if(this.lancamentos == null || this.lancamentos.isEmpty()) {
			return null;
		}
		
		if (numeroLancamento == 1) {
			return null;
		}
		
		Integer numeroLancamentoAnterior = numeroLancamento - 1;
		
		for (Lancamento lancamento : this.lancamentos) {
			
			if (numeroLancamentoAnterior.equals(lancamento.getNumeroLancamento())) {
				
				return lancamento;
			}
		}
		
		return null;
	}
	
	public Lancamento getLancamentoPosterior(Integer numeroLancamento) {
		
		if(this.lancamentos == null || this.lancamentos.isEmpty()) {
			return null;
		}
		
		Integer numeroLancamentoPosterior = numeroLancamento + 1;
		
		for (Lancamento lancamento : this.lancamentos) {
			
			if (numeroLancamentoPosterior.equals(lancamento.getNumeroLancamento())) {
				
				return lancamento;
			}
		}
		
		return null;
	}
	
	public List<Lancamento> getRedistribuicoes() {

		if (this.lancamentos == null) {

			return null;
		}
		
		List<Lancamento> redistribuicoes = new ArrayList<Lancamento>(this.lancamentos);

		redistribuicoes.remove(this.getPrimeiroLancamento());

		return redistribuicoes;
	}
	
	/**
	 * Obtém o período anterior ao lançamento parcial atual.
	 * 
	 * @return {@link PeriodoLancamentoParcial} - O período anterior.
	 */
	public PeriodoLancamentoParcial anterior() {
		
		if (this.lancamentoParcial == null) {
			
			return null;
		}

		Integer numeroPeriodo = this.numeroPeriodo == null ? null : this.numeroPeriodo-1;

		return this.lancamentoParcial.getPeriodoPorNumero(numeroPeriodo);
	}

	/**
	 * Obtém o próximo período do lançamento parcial atual.
	 * 
	 * @return {@link PeriodoLancamentoParcial} - O próximo período.
	 */
	public PeriodoLancamentoParcial proximo() {
		
		if (this.lancamentoParcial == null) {
			
			return null;
		}

		PeriodoLancamentoParcial proximoPeriodo = this.lancamentoParcial.getPeriodoPorNumero(this.numeroPeriodo, this);

		if (proximoPeriodo == null) {

			Integer numeroPeriodo = this.numeroPeriodo == null ? null : this.numeroPeriodo+1;

			proximoPeriodo = this.lancamentoParcial.getPeriodoPorNumero(numeroPeriodo, this);
		}

		return proximoPeriodo;
	}

	/**
	 * Obtém o próximo lançamento parcial para realizar o ajuste da data de lançamento.
	 * 
	 * @param novaDataLancamento
	 */
	public void reajustarProximoLancamentoParcial(Date novaDataLancamento) {
		
		PeriodoLancamentoParcial proximoPeriodo = this.proximo();

		if (proximoPeriodo != null) {

			Lancamento lancamento = proximoPeriodo.getPrimeiroLancamento();
			
			if (lancamento == null) {

				return;
			}

			lancamento.setDataLancamentoPrevista(novaDataLancamento);
			lancamento.setDataLancamentoDistribuidor(novaDataLancamento);

			proximoPeriodo.validarDataLancamentoRedistribuicoesPeriodo();
		}		
	}
	
	/**
	 * Valida se há alguma redistribuição cadastrada neste período com uma data igual ou anterior à nova data de lançamento.
	 */
	public void validarDataLancamentoRedistribuicoesPeriodo() {
		
		Lancamento primeiroLancamento = this.getPrimeiroLancamento();
		
		for (Lancamento lancamento : this.lancamentos) {

			if (TipoLancamento.REDISTRIBUICAO.equals(lancamento.getTipoLancamento())
					&& DateUtil.isDataInicialMaiorIgualDataFinal(
							primeiroLancamento.getDataLancamentoDistribuidor(), 
							lancamento.getDataLancamentoDistribuidor())) {
	
				throw new ValidacaoException(TipoMensagem.WARNING, "A próxima data de lançamento interfere em uma redistribuição já cadastrada."+ "Prod. " +lancamento.getProdutoEdicao().getNomeComercial() + "Ed."+ lancamento.getProdutoEdicao().getNumeroEdicao());
			}
		}
	}

	public boolean before(PeriodoLancamentoParcial other) {
		
		if (other == null) {
			
			return true;
		}
		
		return this.numeroPeriodo < other.getNumeroPeriodo();
	}
	
	public boolean after(PeriodoLancamentoParcial other) {
		
		if (other == null) {
			
			return false;
		}

		return this.numeroPeriodo > other.getNumeroPeriodo();
	}

	public long peb() {

		if (this.getPrimeiroLancamento() == null) {
			
			return 0l;
		}
		
		Date dataLancamento = this.getPrimeiroLancamento().getDataLancamentoDistribuidor();
		Date dataRecolhimento = this.getPrimeiroLancamento().getDataRecolhimentoDistribuidor();
		
		return DateUtil.obterDiferencaDias(dataLancamento, dataRecolhimento);
	}
	
	/**
	 * Incrementa o número atual do período.
	 */
	public void incrementarNumero() {
		this.numeroPeriodo++;
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the lancamentoParcial
	 */
	public LancamentoParcial getLancamentoParcial() {
		return lancamentoParcial;
	}

	/**
	 * @param lancamentoParcial the lancamentoParcial to set
	 */
	public void setLancamentoParcial(LancamentoParcial lancamentoParcial) {
		this.lancamentoParcial = lancamentoParcial;
	}

	/**
	 * @return the status
	 */
	public StatusLancamentoParcial getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusLancamentoParcial status) {
		this.status = status;
	}

	/**
	 * @return the tipo
	 */
	public TipoLancamentoParcial getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(TipoLancamentoParcial tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * @return the lancamentos
	 */
	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	/**
	 * @param lancamentos the lancamentos to set
	 */
	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public Integer getNumeroPeriodo() {
		return numeroPeriodo;
	}

	public void setNumeroPeriodo(Integer numeroPeriodo) {
		this.numeroPeriodo = numeroPeriodo;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeriodoLancamentoParcial other = (PeriodoLancamentoParcial) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}
}
