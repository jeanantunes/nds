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
	
	public EnderecoAssociacaoDTO(boolean enderecoPrincipal, Endereco endereco, TipoEndereco tipoEndereco, Endereco enderecoPessoa) {
		
		if (tipoEndereco != null){
			this.enderecoPrincipal = enderecoPrincipal;
			this.endereco = EnderecoDTO.fromEndereco(endereco);
			this.tipoEndereco = tipoEndereco;
		} else {
			this.endereco = EnderecoDTO.fromEndereco(enderecoPessoa);
		}
	}

    public EnderecoAssociacaoDTO(Long id, boolean enderecoPrincipal, Endereco endereco,
            TipoEndereco tipoEndereco) {
        this(enderecoPrincipal, endereco, tipoEndereco, null);
        this.id = id;
    }
	
	

	private Long id;
	
	private EnderecoDTO endereco;
	
	private TipoEndereco tipoEndereco;
	
	private boolean enderecoPrincipal;

	private ColunaOrdenacao colunaOrdenacao;
	
	public enum ColunaOrdenacao {
		
		TIPO_ENDERECO,
		LOGRADOURO,
		BAIRRO,
		CEP,
		CIDADE,
		PRINCIPAL
		;
	}
	
	/**
	 * @return the endereco
	 */
	public EnderecoDTO getEndereco() {
		return endereco;
	}

	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(EnderecoDTO endereco) {
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
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the colunaOrdenacao
	 */
	public ColunaOrdenacao getColunaOrdenacao() {
		return colunaOrdenacao;
	}

	/**
	 * @param colunaOrdenacao the colunaOrdenacao to set
	 */
	public void setColunaOrdenacao(ColunaOrdenacao colunaOrdenacao) {
		this.colunaOrdenacao = colunaOrdenacao;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
