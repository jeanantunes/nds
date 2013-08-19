package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "EXPEDICAO")
@SequenceGenerator(name = "EXPEDICAO_SEQ", initialValue = 1, allocationSize = 1)
public class Expedicao implements Serializable {

	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8509202379247048936L;

	@Id
	@GeneratedValue(generator = "EXPEDICAO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DATA_EXPEDICAO", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataExpedicao;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO")
	private Usuario responsavel;
	
	@OneToMany(mappedBy = "expedicao")
	List<Lancamento> lancamentos = new ArrayList<Lancamento>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataExpedicao() {
		return dataExpedicao;
	}

	public void setDataExpedicao(Date dataExpedicao) {
		this.dataExpedicao = dataExpedicao;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}
	
	
}
