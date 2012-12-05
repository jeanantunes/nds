package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.util.OrdenacaoUtil;
import br.com.abril.nds.util.Ordenavel;
import br.com.abril.nds.util.StringUtil;

public class RoteiroRoteirizacaoDTO implements Serializable, Ordenavel {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Integer ordem;
	
	private String nome;

	private List<RotaRoteirizacaoDTO> rotas = new ArrayList<RotaRoteirizacaoDTO>();
	
	private List<RotaRoteirizacaoDTO> todasRotas = new ArrayList<RotaRoteirizacaoDTO>();
	
	private Set<Long> rotasExclusao = new HashSet<Long>();
	
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
		OrdenacaoUtil.sortList(rotas);
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
     * Método que verifica se o roteiro é um novo roteiro
     * 
     * @return true indicando que é um novo roteiro, false indica que é um
     *         roteiro já cadastrado
     */
	public boolean isNovo() {
	    return id != null && id < 0;
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
            if (rota.getId() != null && rota.getId().equals(id)) {
                return rota;
            }
        }
        return null;
    }
	
	 /**
     * Recupera a rota pela ordem
     * @param ordem ordem da rota
     * @return rota
     */
	public RotaRoteirizacaoDTO getRotaByOrdem(Integer ordem) {
        for (RotaRoteirizacaoDTO rota : todasRotas) {
            if (rota.getOrdem() != null && rota.getOrdem().equals(ordem)) {
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
    	
    	if (idRota >= 0){
    		
    		this.adicionarRotaExclusao(idRota);
    	}
    }

	public Set<Long> getRotasExclusao() {
		return rotasExclusao;
	}
	
	public void adicionarRotaExclusao(Long idRota){
		
		if (this.rotasExclusao == null){
			
			this.rotasExclusao = new HashSet<Long>();
		}
		
		this.rotasExclusao.add(idRota);
	}
	
	/**
     * Recupera a maior ordem das rotas do roteiro
     * @return valor da maior ordem da lista de rotas
     * ou 0 caso a lista esteja vazia
     */
    public int getMaiorOrdemRota() {
        int max = 0;
        for (RotaRoteirizacaoDTO rota : todasRotas) {
            if (rota.getOrdem()!= null && rota.getOrdem() > max) {
                max = rota.getOrdem();
            }
        }
        return max;
    }

	public Long getMaiorIdRota() {
		  Long max = 0L;
	        for (RotaRoteirizacaoDTO rota : todasRotas) {
	            if (rota.getId() != null && rota.getId() > max) {
	                max = rota.getId();
	            }
	        }
	        return max;
	}
}
