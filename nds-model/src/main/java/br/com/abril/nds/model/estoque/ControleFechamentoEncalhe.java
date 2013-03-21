package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CONTROLE_FECHAMENTO_ENCALHE")
public class ControleFechamentoEncalhe implements Serializable {

	private static final long serialVersionUID = -9071854803148613570L;

	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_ENCALHE", nullable = false)
	private Date dataEncalhe;
	
	public Date getDataEncalhe() {
		return dataEncalhe;
	}

	public void setDataEncalhe(Date dataEncalhe) {
		this.dataEncalhe = dataEncalhe;
	}
}
