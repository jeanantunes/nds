package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.cadastro.Pessoa;

@Embeddable
public class IdentificacaoEmitente implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4715921368300274189L;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PESSOA_EMITENTE_ID")
	private Pessoa pessoaEmitente;
	
	/**
	 * Construtor padrão.
	 */
	public IdentificacaoEmitente() {
		
	}

	/**
	 * @return the pessoaEmitente
	 */
	public Pessoa getPessoaEmitente() {
		return pessoaEmitente;
	}

	/**
	 * @param pessoaEmitente the pessoaEmitente to set
	 */
	public void setPessoaEmitente(Pessoa pessoaEmitente) {
		this.pessoaEmitente = pessoaEmitente;
	}

}
