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

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * Cadastro de lançamento e recolhimento parcial, 
 * contém a data inicial de lançamento e a última
 * data de recolhimento 
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "LANCAMENTO_PARCIAL")
@SequenceGenerator(name = "LANCAMENTO_PARCIAL_SEQ", initialValue = 1, allocationSize = 1)
public class LancamentoParcial implements Serializable{
	
	private static final long serialVersionUID = -7421475045435988608L;

	@Id
	@GeneratedValue(generator = "LANCAMENTO_PARCIAL_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID", nullable = false, unique = true)
	private ProdutoEdicao produtoEdicao;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "LANCAMENTO_INICIAL", nullable = false)
	private Date lancamentoInicial;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "RECOLHIMENTO_FINAL", nullable = false)
	private Date recolhimentoFinal;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private StatusLancamentoParcial status;

	@OneToMany(mappedBy = "lancamentoParcial", cascade={CascadeType.REMOVE})
	private List<PeriodoLancamentoParcial> periodos = new ArrayList<PeriodoLancamentoParcial>();
	
	public PeriodoLancamentoParcial getPrimeiroPeriodoParcial(){
		
		for(PeriodoLancamentoParcial item : this.periodos ){
			if(item.getNumeroPeriodo() == 1 && item.getLancamentoPeriodoParcial().getNumeroLancamento() == 1){
				return item;
			}
		}
		return null;
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
	 * @return the produtoEdicao
	 */
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	/**
	 * @param produtoEdicao the produtoEdicao to set
	 */
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	/**
	 * @return the lancamentoInicial
	 */
	public Date getLancamentoInicial() {
		return lancamentoInicial;
	}

	/**
	 * @param lancamentoInicial the lancamentoInicial to set
	 */
	public void setLancamentoInicial(Date lancamentoInicial) {
		this.lancamentoInicial = lancamentoInicial;
	}

	/**
	 * @return the recolhimentoFinal
	 */
	public Date getRecolhimentoFinal() {
		return recolhimentoFinal;
	}

	/**
	 * @param recolhimentoFinal the recolhimentoFinal to set
	 */
	public void setRecolhimentoFinal(Date recolhimentoFinal) {
		this.recolhimentoFinal = recolhimentoFinal;
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
	 * @return the periodos
	 */
	public List<PeriodoLancamentoParcial> getPeriodos() {
		return periodos;
	}

	/**
	 * @param periodos the periodos to set
	 */
	public void setPeriodos(List<PeriodoLancamentoParcial> periodos) {
		this.periodos = periodos;
	}
	
	/**
	 * Obtém um {@link PeriodoLancamentoParcial} através de seu {@link PeriodoLancamentoParcial#getNumeroPeriodo()} <br/>
	 * Caso o número passado seja nulo, será retornado o primeiro período.
	 * 
	 * @param numeroPeriodo 
	 * 
	 * @return {@link PeriodoLancamentoParcial}
	 */
	public PeriodoLancamentoParcial getPeriodoPorNumero(Integer numeroPeriodo) {

		return this.getPeriodoPorNumero(numeroPeriodo, null);
		
	}	
	
	public PeriodoLancamentoParcial getPeriodoFinal() {
		
		for (PeriodoLancamentoParcial periodoAtual : this.periodos) {

			if (TipoLancamentoParcial.FINAL.equals(periodoAtual.getTipo())) {
				
				return periodoAtual;
			}
		}
		
		return null;
	}
	
	/**
	 * Obtém um {@link PeriodoLancamentoParcial} através de seu {@link PeriodoLancamentoParcial#getNumeroPeriodo()} <br/>
	 * Caso haja dois períodos com o mesmo número (momento da inclusão de um novo período) retornará o período diferente do argumento <br/>
	 * Caso o número passado seja nulo, será retornado o primeiro período.
	 * 
	 * @param numeroPeriodo 
	 * @param periodo
	 * 
	 * @return {@link PeriodoLancamentoParcial}
	 */
	public PeriodoLancamentoParcial getPeriodoPorNumero(Integer numeroPeriodo, PeriodoLancamentoParcial periodo) {

		numeroPeriodo = numeroPeriodo == null ? 1 : numeroPeriodo;

		for (PeriodoLancamentoParcial periodoAtual : this.periodos) {

			if (numeroPeriodo.equals(periodoAtual.getNumeroPeriodo())) {

				 if (periodo == null) {

					return periodoAtual;

				 } else if (!periodoAtual.equals(periodo)) {

					 return periodoAtual;
				 }
			}
		}
		
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getLancamentoInicial() == null) ? 0 : this.getLancamentoInicial().hashCode());
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
		LancamentoParcial other = (LancamentoParcial) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getLancamentoInicial() == null) {
			if (other.getLancamentoInicial() != null)
				return false;
		} else if (!this.getLancamentoInicial().equals(other.getLancamentoInicial()))
			return false;
		if (this.getProdutoEdicao() == null) {
			if (other.getProdutoEdicao() != null)
				return false;
		} else if (!this.getProdutoEdicao().equals(other.getProdutoEdicao()))
			return false;
		return true;
		
	}

}
