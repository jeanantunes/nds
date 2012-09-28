package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RotaRoteirizacaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Integer ordem;
	
	private String nome;
	
	private Boolean selecionado;
	
	private List<PdvRoteirizacaoDTO> pdvs;
	
	public RotaRoteirizacaoDTO() {
	}
	
	public RotaRoteirizacaoDTO(Long id, Integer ordem, String nome) {
		this.id = id;
		this.ordem = ordem;
		this.nome = nome;
	}

	/**
	 * @return the ordem
	 */
	public Integer getOrdem() {
		return ordem;
	}

	/**
	 * @param ordem the ordem to set
	 */
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
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
	 * @return the selecionado
	 */
	public Boolean getSelecionado() {
		return selecionado;
	}

	/**
	 * @param selecionado the selecionado to set
	 */
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
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
	 * @return the listaPdv
	 */
	public List<PdvRoteirizacaoDTO> getPdvs() {
		return pdvs;
	}

	/**
	 * @param listaPdv the listaPdv to set
	 */
	public void setPdvs(List<PdvRoteirizacaoDTO> pdvs) {
		this.pdvs = pdvs;
	}
	
	/**
	 * Adiciona um PDV à Rota
	 * @param pdv: PDV para inclusão
	 */
	public void addPdv(PdvRoteirizacaoDTO pdv) {
		if (pdvs == null) {
			pdvs = new ArrayList<PdvRoteirizacaoDTO>();
		}
		pdvs.add(pdv);
	}
	
	/**
	 * Adiciona novos PDV's à Rota
	 * @param listaPdv: List<PdvRoteirizacaoDTO> para inclusão
	 */
	public void addAllPdv(List<PdvRoteirizacaoDTO> listaPdv){
		if (pdvs == null){
			pdvs = new ArrayList<PdvRoteirizacaoDTO>();
		}
		pdvs.addAll(listaPdv);
	}
	
}
