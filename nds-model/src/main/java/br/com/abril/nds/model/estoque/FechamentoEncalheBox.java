package br.com.abril.nds.model.estoque;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.abril.nds.model.estoque.pk.FechamentoEncalheBoxPK;

@Entity
@Table(name = "FECHAMENTO_ENCALHE_BOX")
public class FechamentoEncalheBox implements Serializable {

	private static final long serialVersionUID = -6350173880585134447L;

	@EmbeddedId
	private FechamentoEncalheBoxPK fechamentoEncalheBoxPK;
	
	@Column(name="QUANTIDADE", nullable=true)
	private Long quantidade;


	public FechamentoEncalheBoxPK getFechamentoEncalheBoxPK() {
		return fechamentoEncalheBoxPK;
	}

	public void setFechamentoEncalheBoxPK(
			FechamentoEncalheBoxPK fechamentoEncalheBoxPK) {
		this.fechamentoEncalheBoxPK = fechamentoEncalheBoxPK;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
}
