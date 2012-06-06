package br.com.abril.nds.model.estoque;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;

@Entity
@Table(name = "FECHAMENTO_ENCALHE")
public class FechamentoEncalhe implements Serializable {

	private static final long serialVersionUID = 5694490390709384624L;

	@EmbeddedId
	private FechamentoEncalhePK fechamentoEncalhePK;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private TipoStatusFechamentoEncalhe status;
	
	@Column(name="QUANTIDADE", nullable=true)
	private Long quantidade;

	
	public FechamentoEncalhePK getFechamentoEncalhePK() {
		return fechamentoEncalhePK;
	}

	public void setFechamentoEncalhePK(FechamentoEncalhePK fechamentoEncalhePK) {
		this.fechamentoEncalhePK = fechamentoEncalhePK;
	}

	public TipoStatusFechamentoEncalhe getStatus() {
		return status;
	}

	public void setStatus(TipoStatusFechamentoEncalhe status) {
		this.status = status;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
}
