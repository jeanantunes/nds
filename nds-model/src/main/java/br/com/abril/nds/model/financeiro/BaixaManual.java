package br.com.abril.nds.model.financeiro;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@DiscriminatorValue(value = "MANUAL")
public class BaixaManual extends BaixaCobranca {

	@ManyToOne(optional = true)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario responsavel;

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

}
