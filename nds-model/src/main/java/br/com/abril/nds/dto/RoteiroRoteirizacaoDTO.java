package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.util.StringUtil;

public class RoteiroRoteirizacaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Integer ordem;
	
	private String nome;

	private List<RotaRoteirizacaoDTO> rotas = new ArrayList<RotaRoteirizacaoDTO>();
	
	private List<RotaRoteirizacaoDTO> todasRotas = new ArrayList<RotaRoteirizacaoDTO>();
	
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
	
	public List<RotaRoteirizacaoDTO> getTodasRotas() {
        return todasRotas;
    }
	
	/**
	 * Adiciona uma nova rota ao roteiro
	 * @param rota rota para inclusão
	 */
	public void addRota(RotaRoteirizacaoDTO rota) {
		if (rotas == null) {
			rotas = new ArrayList<RotaRoteirizacaoDTO>();
		}
		
		for (RotaRoteirizacaoDTO dto : todasRotas){
			
			if (dto.getOrdem() <= rota.getOrdem()){
				
				rota.setOrdem(dto.getOrdem() + 1);
			}
		}
		
		rotas.add(rota);
		todasRotas.add(rota);
	}
	
	/**
	 * Adiciona novas Rotas       ao Roteiro
	 * @param listaRota: List<RotaRoteirizacaoDTO> para inclusão
	 */
	public void addAllRota(List<RotaRoteirizacaoDTO> listaRota){
		
		if (rotas == null){
			rotas = new ArrayList<RotaRoteirizacaoDTO>();
		}
		rotas.addAll(listaRota);
		todasRotas.addAll(listaRota);
	}

    /**
     * Recupera a rota pelo identificador
     * @param id identificador da rota
     * @return rota com o identificador recebido
     */
	public RotaRoteirizacaoDTO getRota(Long id) {
        for (RotaRoteirizacaoDTO rota : todasRotas) {
            if (rota.getId().equals(id)) {
                return rota;
            }
        }
        return null;
    }

    /**
     * Filtra as rotas pela descricao
     * 
     * @param descricaoRota
     *            descrição da rota para filtragem
     */
    public void filtarRotas(String descricaoRota) {
        if (!StringUtil.isEmpty(descricaoRota)) {
            List<RotaRoteirizacaoDTO> filtrados = new ArrayList<RotaRoteirizacaoDTO>();
            for (RotaRoteirizacaoDTO rota : todasRotas) {
                if (rota.getNome().toUpperCase()
                        .startsWith(descricaoRota.toUpperCase())) {
                    filtrados.add(rota);
                }
            }
            rotas = filtrados;
        } else {
            rotas.clear();
            rotas.addAll(todasRotas);
        }
    }
	
    public void removerRota(Long idRota){
    	
    	if (this.rotas != null){
    		
    		for (RotaRoteirizacaoDTO rota : this.rotas){
    			
    			if (rota.getId().equals(idRota)){
    				
    				this.rotas.remove(rota);
    				break;
    			}
    		}
    	}
    	
    	if (this.todasRotas != null){
    		
    		for (RotaRoteirizacaoDTO rota : this.todasRotas){
    			
    			if (rota.getId().equals(idRota)){
    				
    				this.todasRotas.remove(rota);
    				break;
    			}
    		}
    	}
    }
}
