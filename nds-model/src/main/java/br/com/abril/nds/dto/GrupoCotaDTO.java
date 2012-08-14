package br.com.abril.nds.dto;

import java.io.Serializable;

public class GrupoCotaDTO implements Serializable {

	private static final long serialVersionUID = 4640422019892739724L;

	private Long idGrupo;
	private String nome;
	private String recolhimento;
	
	/**
	 * @return the idGrupo
	 */
	public Long getIdGrupo() {
		return idGrupo;
	}
	
	/**
	 * @param idGrupo the idGrupo to set
	 */
	public void setIdGrupo(Long idGrupo) {
		this.idGrupo = idGrupo;
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
	 * @return the recolhimento
	 */
	public String getRecolhimento() {
		return recolhimento;
	}

	/**
	 * @param recolhimento the recolhimento to set
	 */
	public void setRecolhimento(String recolhimento) {
		this.recolhimento = recolhimento;
	}
	
	
}
