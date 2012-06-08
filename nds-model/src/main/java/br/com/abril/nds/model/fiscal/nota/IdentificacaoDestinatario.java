package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.cadastro.Pessoa;

@Embeddable
public class IdentificacaoDestinatario implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3558149602330018787L;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PESSOA_DESTINATARIO_ID")
	private Pessoa pessoaDestinatario;
	
	/**
	 * Construtor padrão.
	 */
	public IdentificacaoDestinatario() {
		
	}

	/**
	 * @return the pessoaDestinatario
	 */
	public Pessoa getPessoaDestinatario() {
		return pessoaDestinatario;
	}

	/**
	 * @param pessoaDestinatario the pessoaDestinatario to set
	 */
	public void setPessoaDestinatario(Pessoa pessoaDestinatario) {
		this.pessoaDestinatario = pessoaDestinatario;
	}

}
