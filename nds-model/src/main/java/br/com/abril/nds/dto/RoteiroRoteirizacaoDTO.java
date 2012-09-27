package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoteiroRoteirizacaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Integer ordem;
	
	private String nome;
	
	private Boolean selecionado;

	private List<RotaRoteirizacaoDTO> rotas;
	
	public RoteiroRoteirizacaoDTO() {
		
	}
	
	public RoteiroRoteirizacaoDTO(Long id, Integer ordem, String nome,
			Boolean selecionado) {
		super();
		this.id = id;
		this.ordem = ordem;
		this.nome = nome;
		this.selecionado = selecionado;
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
}
