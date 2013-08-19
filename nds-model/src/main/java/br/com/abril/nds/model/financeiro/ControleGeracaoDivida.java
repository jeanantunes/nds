package br.com.abril.nds.model.financeiro;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "CONTROLE_GERACAO_DIVIDA")
@SequenceGenerator(name="CTRL_GER_DIVIDA_SEQ", initialValue = 1, allocationSize = 1)
public class ControleGeracaoDivida {
	
	@Id
	@GeneratedValue(generator = "CTRL_GER_DIVIDA_SEQ")
	private Long id;
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA")
	private Date data;
	@ManyToOne
	@JoinColumn(name = "USUARIO_ID")
	private Usuario responsavel;
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private StatusControle status;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public Usuario getResponsavel() {
		return responsavel;
	}
	
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}
	
	public StatusControle getStatus() {
		return status;
	}
	
	public void setStatus(StatusControle status) {
		this.status = status;
	}

}
