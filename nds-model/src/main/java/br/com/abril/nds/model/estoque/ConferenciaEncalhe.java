package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entidade de abstrai os movimentos de envio de encalhe.
 * 
 * @author Discover Technology
 *
 */
@Entity
@Table(name = "CONFERENCIA_ENCALHE")
@SequenceGenerator(name="CONFERENCIA_ENCALHE_SEQ", initialValue = 1, allocationSize = 1)
public class ConferenciaEncalhe implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CONFERENCIA_ENCALHE_SEQ")
	private Long id;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "MOVIMENTO_ESTOQUE_COTA_ID")
	private MovimentoEstoqueCota movimentoEstoqueCota;
	
	/**
	 * O campo abaixo refere-se a dataRecolhimento original 
	 * registrada na tbl Lancamento. Esta data é necessaria
	 * aqui pois o movimento de envio encalhe pode ter ocorrido
	 * antecipadamente, e com este campo sera possível identificar
	 * a qual data de recolhimento original este pertence.
	 */
	@Column(name = "DATA_RECOLHIMENTO", nullable = false)
	private Date dataRecolhimento;

	/**
	 * Obtém id
	 *
	 * @return Long
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Atribuí id
	 * @param id 
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtém movimentoEstoqueCota
	 *
	 * @return MovimentoEstoqueCota
	 */
	public MovimentoEstoqueCota getMovimentoEstoqueCota() {
		return movimentoEstoqueCota;
	}

	/**
	 * Atribuí movimentoEstoqueCota
	 * @param movimentoEstoqueCota 
	 */
	public void setMovimentoEstoqueCota(MovimentoEstoqueCota movimentoEstoqueCota) {
		this.movimentoEstoqueCota = movimentoEstoqueCota;
	}

	/**
	 * Obtém dataRecolhimento
	 *
	 * @return Date
	 */
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	/**
	 * Atribuí dataRecolhimento
	 * @param dataRecolhimento 
	 */
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	
	
}
