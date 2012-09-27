package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoteirizacaoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private BoxRoteirizacaoDTO box;
	
	private List<RoteiroRoteirizacaoDTO> roteiros;
	
	enum AcaoRegistro{
    	REGISTRO_NOVO,
    	REGISTRO_EXCLUIDO,
    	REGISTRO_ATUAL
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
	 * @return the box
	 */
	public BoxRoteirizacaoDTO getBox() {
		return box;
	}

	/**
	 * @param box the box to set
	 */
	public void setBox(BoxRoteirizacaoDTO box) {
		this.box = box;
	}

	/**
	 * @return the roteiros
	 */
	public List<RoteiroRoteirizacaoDTO> getRoteiros() {
		return roteiros;
	}

	/**
	 * @param roteiros the roteiros to set
	 */
	public void setRoteiros(List<RoteiroRoteirizacaoDTO> roteiros) {
		this.roteiros = roteiros;
	}

	/**
	 * Adiciona um novo roteiro à roteirização
	 * @param roteiro roteiro para inclusão
	 */
	public void addRoteiro(RoteiroRoteirizacaoDTO roteiro) {
		if (roteiros == null) {
			roteiros = new ArrayList<RoteiroRoteirizacaoDTO>();
		}
		roteiros.add(roteiro);
	}
}
