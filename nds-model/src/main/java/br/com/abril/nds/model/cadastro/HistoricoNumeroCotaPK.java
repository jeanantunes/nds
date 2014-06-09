package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class HistoricoNumeroCotaPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name="ID_COTA")
	private Long idCota;
	
	@Column(name="DATA_ALTERACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAlteracao;

	public Long getIdCota() {
		return idCota;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}
	
}
