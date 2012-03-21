package br.com.abril.nds.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.seguranca.Usuario;

@MappedSuperclass
public abstract class HistoricoEdicao {
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_EDICAO", nullable = false)
	private Date dataEdicao;
	
	@ManyToOne
	@Column(name = "USUARIO_ID", nullable = false)
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
