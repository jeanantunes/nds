package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoteiroRoteirizacaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Integer ordem;
	
	private String nome;

	private List<RotaRoteirizacaoDTO> rotas;
	
	public RoteiroRoteirizacaoDTO() {
	}
	
	public RoteiroRoteirizacaoDTO(Long id, Integer ordem, String nome) {
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
	 * @return the listaRota
	 */
	public List<RotaRoteirizacaoDTO> getRotas() {
		return rotas;
	}

	/**
	 * @param listaRota the listaRota to set
	 */
	public void setRotas(List<RotaRoteirizacaoDTO> listaRota) {
		this.rotas = listaRota;
	}
	
	/**
	 * Adiciona uma nova rota ao roteiro
	 * @param rota rota para inclusão
	 */
	public void addRota(RotaRoteirizacaoDTO rota) {
		if (rotas == null) {
			rotas = new ArrayList<RotaRoteirizacaoDTO>();
		}
		rotas.add(rota);
	}
	
	/**
	 * Adiciona novas Rotas ao Roteiro
	 * @param listaRota: List<RotaRoteirizacaoDTO> para inclusão
	 */
	public void addAllRota(List<RotaRoteirizacaoDTO> listaRota){
		if (rotas == null){
			rotas = new ArrayList<RotaRoteirizacaoDTO>();
		}
		rotas.addAll(listaRota);
	}

    /**
     * Recupera a rota pelo identificador
     * @param id identificador da rota
     * @return rota com o identificador recebido
     */
	public RotaRoteirizacaoDTO getRota(Long id) {
        for (RotaRoteirizacaoDTO rota : rotas) {
            if (rota.getId().equals(id)) {
                return rota;
            }
        }
        return null;
    }
	
}
