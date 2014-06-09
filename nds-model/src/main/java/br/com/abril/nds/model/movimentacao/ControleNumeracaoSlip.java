package br.com.abril.nds.model.movimentacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.TipoSlip;

@Entity
@Table(name = "CONTROLE_NUMERACAO_SLIP")
@SequenceGenerator(name="CONTROLE_NUMERACAO_SLIP_SEQ", initialValue = 1, allocationSize = 1)
public class ControleNumeracaoSlip {
	
	@Id
	@GeneratedValue(generator = "CONTROLE_NUMERACAO_SLIP_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Enumerated(EnumType.STRING)	
	@Column(name = "TIPO_SLIP", nullable = false, unique = true)
	private TipoSlip tipoSlip;
	
	@Column(name = "PROXIMO_NUMERO_SLIP", nullable = false)
	private Long proximoNumeroSlip;

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
	 * Obtém tipoSlip
	 *
	 * @return TipoSlip
	 */
	public TipoSlip getTipoSlip() {
		return tipoSlip;
	}

	/**
	 * Atribuí tipoSlip
	 * @param tipoSlip 
	 */
	public void setTipoSlip(TipoSlip tipoSlip) {
		this.tipoSlip = tipoSlip;
	}

	/**
	 * Obtém proximoNumeroSlip
	 *
	 * @return Long
	 */
	public Long getProximoNumeroSlip() {
		return proximoNumeroSlip;
	}

	/**
	 * Atribuí proximoNumeroSlip
	 * @param proximoNumeroSlip 
	 */
	public void setProximoNumeroSlip(Long proximoNumeroSlip) {
		this.proximoNumeroSlip = proximoNumeroSlip;
	}
	
}