package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.abril.nds.model.cadastro.Box;

public class BoxRoteirizacaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String nome;
	
	public static final BoxRoteirizacaoDTO ESPECIAL = new BoxRoteirizacaoDTO(Box.ESPECIAL.getId(), Box.ESPECIAL.getNome());
	
	public BoxRoteirizacaoDTO() {
	}
	
	public BoxRoteirizacaoDTO(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
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
     * Cria uma lista de {@link BoxRoteirizacaoDTO} à partir da lista de box
     * 
     * @param colecao
     *            de box
     * @return lista de {@link BoxRoteirizacaoDTO} criada à partir da coleção de
     *         box
     */
	public static List<BoxRoteirizacaoDTO> toDTOs(Collection<Box> colecao) {
	    List<BoxRoteirizacaoDTO> dtos = new ArrayList<BoxRoteirizacaoDTO>();
	    for(Box box : colecao) {
	        dtos.add(new BoxRoteirizacaoDTO(box.getId(), box.getNome()));
	    }
	    return dtos;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

   
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BoxRoteirizacaoDTO other = (BoxRoteirizacaoDTO) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
	
}
