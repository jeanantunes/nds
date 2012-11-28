package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.util.Ordenavel;

public class RotaRoteirizacaoDTO implements Serializable, Ordenavel {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Integer ordem;
	
	private String nome;
	
	private Boolean selecionado;
	
	private List<PdvRoteirizacaoDTO> pdvs = new ArrayList<PdvRoteirizacaoDTO>();
	
	private Set<Long> pdvsExclusao = new HashSet<Long>();
	
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


	/**
	 * Altera a ordem do PDV 
	 * @param idPdv identificador do PDV para alteração
	 * @param ordem nova ordem do PDV
	 * @return true indicando que a alteração de ordem foi executada
	 * com sucesso, false caso a ordem não for válida
	 */
	public boolean alterarOrdemPdv(Long idPdv, Integer ordem) {
	    if (ordem <= 0) {
	        return false;
	    } else {
	        PdvRoteirizacaoDTO pdvAlteracao = getPdv(idPdv);
	        PdvRoteirizacaoDTO pdvExistente = getPdvByOrdem(ordem);
	        if (pdvExistente != null && !pdvAlteracao.equals(pdvExistente)) {
	            return false;
	        }
	        pdvAlteracao.setOrdem(ordem);
	        return true;
	    }
	}
	
	/**
	 * Recupera o PDV pelo identificador
	 * @param idPdv identificador do PDV para recuperação
	 * @return {@link PdvRoteirizacaoDTO}  correspondente ao identificador
	 * recebido ou null caso o PDV não for encontrado
	 */
	public PdvRoteirizacaoDTO getPdv(Long idPdv) {
	    for (PdvRoteirizacaoDTO pdv : getPdvs()) {
	        if (pdv.getId().equals(idPdv)) {
	            return pdv;
	        }
	    }
	    return null;
	}
	
	/**
	 * Recupera o PDV pela ordem
	 * @param ordem ordem do PDV para recuperação
	 * @return {@link PdvRoteirizacaoDTO} com a ordem recebida ou null
	 * caso não exista um PDV com a ordem
	 */
	public PdvRoteirizacaoDTO getPdvByOrdem(Integer ordem) {
	    for (PdvRoteirizacaoDTO pdv : getPdvs()) {
            if (pdv.getOrdem().equals(ordem)) {
                return pdv;
            }
        }
        return null;
	}
	
	/**
	 * Remove o Pdv da Rota
	 * @param idPdv identificador do PDV para remoção
	 */
	public void removerPdv(Long idPdv) {
	    Iterator<PdvRoteirizacaoDTO> iterator = pdvs.iterator();
	    while(iterator.hasNext()) {
	        PdvRoteirizacaoDTO pdv = iterator.next();
	        if (pdv.getId().equals(idPdv)) {
	            iterator.remove();
	            
	            if (idPdv >= 0){
	    			
	    			this.adicionarPdvExclusao(idPdv);
	    		}
	        }
	    }
	}

	/**
     * Método que verifica se a rota é uma nova rota
     * 
     * @return true indicando que é uma nova rota, false indica que é uma
     *         rota já cadastrada
     */
	public boolean isNovo() {
	    return id != null && id < 0;
	}
	
	
	public Set<Long> getPdvsExclusao() {
        return pdvsExclusao;
    }

	public void adicionarPdvExclusao(Long idPdv){
	    
	    if (this.pdvsExclusao == null){
	        
	        this.pdvsExclusao = new HashSet<Long>();
	    }
	    
	    this.pdvsExclusao.add(idPdv);
	}
	
	/**
	 * Recupera a maior ordem dos pdvs da rota
	 * @return valor da maior ordem da lista de pdvs
	 * ou 0 caso a lista esteja vazia
	 */
	private int getMaiorOrdem() {
	    int max = 0;
	    for (PdvRoteirizacaoDTO pdv : getPdvs()) {
	        if (pdv.getOrdem() > max) {
	            max = pdv.getOrdem();
	        }
	    }
	    return max;
	}

	public void setPdvsExclusao(Set<Long> pdvsExclusao) {
		this.pdvsExclusao = pdvsExclusao;
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RotaRoteirizacaoDTO other = (RotaRoteirizacaoDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

    /**
     * Adiciona a coleção de pdvs ao final dos pdvs
     * de acordo com a maior ordem existente
     * @param pdvs coleção de pdvs para inclusão
     */
	public void addPdvsAposMaiorOrdem(Collection<PdvRoteirizacaoDTO> pdvs) {
        for (PdvRoteirizacaoDTO pdv : pdvs) {
            PdvRoteirizacaoDTO existente = getPdv(pdv.getId());
            if (existente == null) {
                int ordemFinal = getMaiorOrdem();
                pdv.setOrdem(++ordemFinal);
                addPdv(pdv);
            }
        }
    }
}