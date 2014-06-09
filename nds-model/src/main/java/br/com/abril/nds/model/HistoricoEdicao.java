package br.com.abril.nds.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.seguranca.Usuario;

@MappedSuperclass
public abstract class HistoricoEdicao implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6324406820354733337L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_EDICAO", nullable = false)
	private Date dataEdicao;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario responsavel;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_EDICAO", nullable = false)
	private TipoEdicao tipoEdicao;

	public Date getDataEdicao() {
		return dataEdicao;
	}

	public void setDataEdicao(Date dataEdicao) {
		this.dataEdicao = dataEdicao;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public TipoEdicao getTipoEdicao() {
		return tipoEdicao;
	}

	public void setTipoEdicao(TipoEdicao tipoEdicao) {
		this.tipoEdicao = tipoEdicao;
	}
	
	

}
