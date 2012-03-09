package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.TipoEndereco;

/**
 * DTO que representa a associação de um endereço com uma especialização de pessoa.
 * 
 * @author Discover Technology
 *
 */
public class EnderecoAssociacaoDTO implements Serializable {

	private static final long serialVersionUID = 5679162468044592256L;

	/**
	 * Construtor padrão.
	 */
	public EnderecoAssociacaoDTO() { }
	
	private Endereco endereco;
	
	private TipoEndereco tipoEndereco;
	
	private boolean enderecoPrincipal;

	/**
	 * @return the endereco
	 */
	public Endereco getEndereco() {
		return endereco;
	}

	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	/**
	 * @return the tipoEndereco
	 */
	public TipoEndereco getTipoEndereco() {
		return tipoEndereco;
	}

	/**
	 * @param tipoEndereco the tipoEndereco to set
	 */
	public void setTipoEndereco(TipoEndereco tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}

	/**
	 * @return the enderecoPrincipal
	 */
	public boolean isEnderecoPrincipal() {
		return enderecoPrincipal;
	}

	/**
	 * @param enderecoPrincipal the enderecoPrincipal to set
	 */
	public void setEnderecoPrincipal(boolean enderecoPrincipal) {
		this.enderecoPrincipal = enderecoPrincipal;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnderecoAssociacaoDTO other = (EnderecoAssociacaoDTO) obj;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		return true;
	}
}
