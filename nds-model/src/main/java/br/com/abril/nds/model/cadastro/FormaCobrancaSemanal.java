package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


@Entity
@DiscriminatorValue(value = "S")
public class FormaCobrancaSemanal extends FormaCobranca implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 526487374913437599L;
	
	//ATRIBUTOS TRANSFERIDOS DE PARAMETRO COBRANCA COTA
	@OneToMany(mappedBy="formaCobranca")
	private Set<ConcentracaoCobrancaCota> concentracaoCobrancaCota = new HashSet<ConcentracaoCobrancaCota>();
	
	
	public Set<ConcentracaoCobrancaCota> getConcentracaoCobrancaCota() {
		return concentracaoCobrancaCota;
	}
	
	public void setConcentracaoCobrancaCota(
			Set<ConcentracaoCobrancaCota> concentracaoCobrancaCota) {
		this.concentracaoCobrancaCota = concentracaoCobrancaCota;
	}
	
}
