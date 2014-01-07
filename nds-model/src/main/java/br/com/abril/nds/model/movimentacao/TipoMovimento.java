package br.com.abril.nds.model.movimentacao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TIPO_MOVIMENTO")
@SequenceGenerator(name = "TP_MOVIMENTO_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class TipoMovimento implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2534609323819976781L;

	@Id
	@GeneratedValue(generator = "TP_MOVIMENTO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;
	
	@Column(name = "APROVACAO_AUTOMATICA", nullable = false)
	private boolean aprovacaoAutomatica;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public boolean isAprovacaoAutomatica() {
		return aprovacaoAutomatica;
	}
	
	public void setAprovacaoAutomatica(boolean aprovacaoAutomatica) {
		this.aprovacaoAutomatica = aprovacaoAutomatica;
	}

}
