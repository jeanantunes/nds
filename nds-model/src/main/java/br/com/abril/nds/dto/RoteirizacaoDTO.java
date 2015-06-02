package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.abril.nds.dto.PdvRoteirizacaoDTO.OrigemEndereco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;
import br.com.abril.nds.util.StringUtil;


public class RoteirizacaoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	    /**
     * Box da roteirização
     */
	private BoxRoteirizacaoDTO box;
	
	    /**
     * Roteiros da roteirização
     */
	private List<RoteiroRoteirizacaoDTO> roteiros = new ArrayList<RoteiroRoteirizacaoDTO>();
	
    /**
     * Tipo da Edição
     */
	private TipoEdicaoRoteirizacao tipoEdicao;
	
	    /**
     * Box disponíveis
     */
	private List<BoxRoteirizacaoDTO> boxDisponiveis = new ArrayList<BoxRoteirizacaoDTO>();
	
	/**
     * Todos Box
     */
    private List<BoxRoteirizacaoDTO> todosBox = new ArrayList<BoxRoteirizacaoDTO>();
    
    /**
     * Todos os roteiros
     */
    private List<RoteiroRoteirizacaoDTO> todosRoteiros = new ArrayList<RoteiroRoteirizacaoDTO>();
    
    /**
     * Coleção de identificadores de roteiros para exclusão
     */
    private Set<Long> roteirosExclusao = new HashSet<Long>();
    
    private Map<Long, Set<RoteiroRoteirizacaoDTO>> roteirosTransferidos = new HashMap<Long, Set<RoteiroRoteirizacaoDTO>>();
    
    /**
     * Lista das rotas pertencentes a outra roteirizacao que tiveram novos pdvs transferidas
     */
    private List<RotaRoteirizacaoDTO> rotasNovosPDVsTransferidos = new ArrayList<RotaRoteirizacaoDTO>();
    
    /**
     * Lista dos roteiros pertencentes a outro roteirizacao que tiveram novas rotas transferidas
     */
    private List<RoteiroRoteirizacaoDTO> roteirosNovasRotasTransferidas = new ArrayList<RoteiroRoteirizacaoDTO>();
    
    private RoteirizacaoDTO(TipoEdicaoRoteirizacao tipoEdicao, List<BoxRoteirizacaoDTO> boxDisponiveis, boolean addBoxEspecial) {
        this.tipoEdicao = tipoEdicao;
        this.boxDisponiveis = new ArrayList<BoxRoteirizacaoDTO>();
//        if (TipoEdicaoRoteirizacao.NOVO == tipoEdicao || boxDisponiveis.isEmpty() || addBoxEspecial) {
//            this.boxDisponiveis.add(BoxRoteirizacaoDTO.ESPECIAL);
//        }
        this.boxDisponiveis.addAll(boxDisponiveis);
        this.todosBox = new ArrayList<BoxRoteirizacaoDTO>(this.boxDisponiveis);
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
     * @return the boxDisponiveis
     */
    public List<BoxRoteirizacaoDTO> getBoxDisponiveis() {
        return boxDisponiveis;
    }

    /**
     * @param boxDisponiveis the boxDisponiveis to set
     */
    public void setBoxDisponiveis(List<BoxRoteirizacaoDTO> boxDisponiveis) {
        this.boxDisponiveis = boxDisponiveis;
    }
    
    
    public TipoEdicaoRoteirizacao getTipoEdicao() {
        return tipoEdicao;
    }
    
    public void setTipoEdicao(TipoEdicaoRoteirizacao tipoEdicao) {
        this.tipoEdicao = tipoEdicao;
    }

	public List<RoteiroRoteirizacaoDTO> getTodosRoteiros() {
        return todosRoteiros;
    }
	
    /**
     * @return the roteirosExclusao
     */
    public Set<Long> getRoteirosExclusao() {
        return roteirosExclusao;
    }

    public List<RotaRoteirizacaoDTO> getRotasNovosPDVsTransferidos() {
		return rotasNovosPDVsTransferidos;
	}

	public List<RoteiroRoteirizacaoDTO> getRoteirosNovasRotasTransferidas() {
		return roteirosNovasRotasTransferidas;
	}
	
	/**
     * Adiciona uma rota que pertence a outra roteirização e teve PDVs
     * adicionados
     * 
     * @param rotaDTO
     */
    public void addRotaNovosPDVsTransferidos(RotaRoteirizacaoDTO rotaDTO) {
    	
    	if(this.rotasNovosPDVsTransferidos == null) {
    		this.rotasNovosPDVsTransferidos = new ArrayList<RotaRoteirizacaoDTO>();
    	}
    	
    	this.rotasNovosPDVsTransferidos.add(rotaDTO);
    }
    
    public RoteiroRoteirizacaoDTO obterRoteirosNovasRotasTransferidas(Roteiro roteiro, List<Rota> listaRotasExistentes) {
    	
    	if (this.roteirosNovasRotasTransferidas == null)
    		this.roteirosNovasRotasTransferidas = new ArrayList<RoteiroRoteirizacaoDTO>();
    	
    	for (RoteiroRoteirizacaoDTO roteiroDTO : this.roteirosNovasRotasTransferidas) {
    		if(roteiroDTO.getId().equals(roteiro.getId())) {
    			return roteiroDTO;
    		}
    	}
    	
    	RoteiroRoteirizacaoDTO roteiroDTO = RoteiroRoteirizacaoDTO.getDTOFrom(roteiro);
    	roteiroDTO.addAllRota(RotaRoteirizacaoDTO.getDTOFrom(listaRotasExistentes));
    	roteirosNovasRotasTransferidas.add(roteiroDTO);
    	
    	return roteiroDTO;
    }
    
    /**
     * Retorna o DTO de uma rota pertencente a outra roteirizacao para transferencia de pdvs.
     * 
     * @param rota
     * @return
     */
    public RotaRoteirizacaoDTO obterRotaNovosPDVsTransferidos(Rota rota) {
    	
    	if (this.rotasNovosPDVsTransferidos == null) 
    		this.rotasNovosPDVsTransferidos = new ArrayList<RotaRoteirizacaoDTO>();
    	
    	for (RotaRoteirizacaoDTO rotaDTO : this.rotasNovosPDVsTransferidos) {
    		
    		if (rotaDTO.getId().equals(rota.getId())) {
    			return rotaDTO;
    		}
    	}
    	
    	RotaRoteirizacaoDTO novaRotaDTO = RotaRoteirizacaoDTO.getDTOFrom(rota);
    	
    	this.addRotaNovosPDVsTransferidos(novaRotaDTO);
    	
    	return novaRotaDTO;
    }

	    /**
     * Adiciona o identificador do roteiro para exclusão
     * 
     * @param idRoteiro identificador do roteiro
     */
	public void addRoteiroExclusao(Long idRoteiro) {
	    if (roteirosExclusao == null) {
	        roteirosExclusao = new HashSet<Long>();
	    }
	    roteirosExclusao.add(idRoteiro);
	}

	    /**
     * Adiciona um novo roteiro à roteirização
     * 
     * @param roteiro roteiro para inclusão
     */
	public void addRoteiro(RoteiroRoteirizacaoDTO roteiro) {
		if (roteiros == null) {
			roteiros = new ArrayList<RoteiroRoteirizacaoDTO>();
		}
		roteiros.add(roteiro);
		todosRoteiros.add(roteiro);
	}
	
	    /**
     * Adiciona novos Roteiros à Roteirizacao
     * 
     * @param listaRoteiro: List<RoteiroRoteirizacaoDTO> para inclusão
     */
	public void addAllRoteiro(Collection<RoteiroRoteirizacaoDTO> listaRoteiro){
		if (roteiros == null){
			roteiros = new ArrayList<RoteiroRoteirizacaoDTO>();
		}
		
		if (listaRoteiro != null){
			roteiros.addAll(listaRoteiro);
			todosRoteiros.addAll(listaRoteiro);
		}
	}
	
    /**
     * Constrói um dto para a criação de uma nova roteirização
     * 
     * @param boxDisponiveis lista de box disponíveis para a roteirização
     * @return {@link RoteirizacaoDTO} para a criação de uma nova roteirização
     */
	public static RoteirizacaoDTO novaRoteirizacao(List<BoxRoteirizacaoDTO> boxDisponiveis) {
	    return new RoteirizacaoDTO(TipoEdicaoRoteirizacao.NOVO, boxDisponiveis, true);
	}
	
    /**
     * Cria o DTO à partir de uma roteirização existente
     * 
     * @param roteirizacao roteirização existente para criação do DTO
     * @param dtos lista de boxes disponíveis
     * @param addBoxEspecial flag indicando se o box especial deve ser
     *            adicionado à lista de box disponíveis da roteirização
     * @return DTO com as informações da roteirização existente
     */
	public static RoteirizacaoDTO toDTO(Roteirizacao roteirizacao, List<Box> disponiveis, boolean addBoxEspecial) {
	    
		RoteirizacaoDTO dto = new RoteirizacaoDTO(TipoEdicaoRoteirizacao.ALTERACAO, BoxRoteirizacaoDTO.toDTOs(disponiveis), addBoxEspecial);
	    
		dto.setId(roteirizacao.getId());

        Box box = roteirizacao.getBox();
        
        BoxRoteirizacaoDTO boxDTO = null;
        
        if (box != null) {
            boxDTO = new BoxRoteirizacaoDTO(box.getId(), box.getNome());
        }
        
        dto.setBox(boxDTO);
        
        for(Roteiro roteiro : roteirizacao.getRoteiros()) {
            
        	RoteiroRoteirizacaoDTO roteiroDTO = new RoteiroRoteirizacaoDTO(roteiro.getId(), roteiro.getOrdem(), roteiro.getDescricaoRoteiro());
            
        	dto.addRoteiro(roteiroDTO);

            for (Rota rota : roteiro.getRotas()) {
            	
            	Entregador entregador = rota.getEntregador();
            	            			
            	Long entregadorId = (entregador == null ? null : entregador.getId());	
                
            	RotaRoteirizacaoDTO rotaDTO = new RotaRoteirizacaoDTO(
                        rota.getId(), rota.getOrdem(), rota.getDescricaoRota(), entregadorId);
                roteiroDTO.addRota(rotaDTO);

                for(RotaPDV rotaPdv : rota.getRotaPDVs()){
                    PDV pdv = rotaPdv.getPdv();
                    Cota cota = rotaPdv.getPdv().getCota();

                    String nomeCota = cota.getPessoa().getNome();
                    OrigemEndereco origemEndereco = null;

                    Endereco endereco = null;
                    
                    if(!boxDTO.getNome().equals("ESPECIAL")) {
	                    Set<EnderecoCota> enderecosCota = cota.getEnderecos();
	                    for (EnderecoCota ec : enderecosCota) {
	                    	if(ec.getTipoEndereco().equals(TipoEndereco.LOCAL_ENTREGA)) {
	                    		endereco = ec.getEndereco();
	                    		break;
	                    	}
	                    }
                    }
                    
                    EnderecoPDV enderecoPdvEntrega = pdv.getEnderecoEntrega();
                    
                    if (endereco == null && enderecoPdvEntrega != null) {
                    	
                        endereco = enderecoPdvEntrega.getEndereco();
                        origemEndereco = OrigemEndereco.PDV;
                    
                    } else if (endereco == null && pdv.getEnderecoPrincipal() != null) {
                    
                    	EnderecoPDV enderecoPdv = pdv.getEnderecoPrincipal();
                    	endereco = enderecoPdv.getEndereco();
                    	origemEndereco = OrigemEndereco.PDV;
                    	
                    } else {
                    	
                    	EnderecoCota enderecoPrincipalCota = cota.getEnderecoPrincipal();
                        
                    	if (enderecoPrincipalCota != null){
                            endereco = enderecoPrincipalCota.getEndereco();
                        }    
                        
                    	origemEndereco = OrigemEndereco.COTA;
                    	
                    }
                    
                    PdvRoteirizacaoDTO pdvDTO = new PdvRoteirizacaoDTO(
                            pdv.getId(), pdv.getNome(), origemEndereco,
                            endereco, cota.getNumeroCota(), nomeCota,
                            rotaPdv.getOrdem());
                    rotaDTO.addPdv(pdvDTO);
                }
            }
        }
        return dto;
	}
	
    /**
     * Verifica se o DTO corresponde à uma nova rorteirização
     * 
     * @return true se é uma nova roteirização, false caso contrário
     */
	public boolean isNovo() {
	    return TipoEdicaoRoteirizacao.NOVO == tipoEdicao;
	}
	
    /**
     * Verifica se a roteirização utiliza o Box Especial
     * 
     * @return true se a roteirização utiliza Box Especial, false em caso
     *         contrário
     */
	public boolean isBoxEspecial() {
	    return "ESPECIAL".equals(box != null ? box.getNome() : null);
	}
	
    /**
     * Recupera a rota da roteirização pelo id
     * 
     * @param id identificador da rota
     * @return rota com o identificador recebido
     */
	public RotaRoteirizacaoDTO getRota(Long id) {
	    RotaRoteirizacaoDTO rota = null;
	    for(RoteiroRoteirizacaoDTO roteiro : todosRoteiros) {
	        rota = roteiro.getRota(id);
	        if (rota != null) {
	            return rota;
	        }
	    }
	    return null;
	}

	    /**
     * Reseta o DTO selecionando um novo box
     * 
     * @param idBox identificador do Box para a roteirização
     */
	public void reset(Long idBox) {
	    this.id = null;
	    this.tipoEdicao = TipoEdicaoRoteirizacao.NOVO;
	    for (BoxRoteirizacaoDTO box : boxDisponiveis) {
	        if (box.getId().equals(idBox)) {
	            this.box = box;
	            break;
	        }
	    }
	    this.roteiros.clear();
	    this.todosRoteiros.clear();
	    this.roteirosExclusao.clear();
	}
	
    /**
     * Filtra o box pelo nome
     * @param nomeBox nome do box para filtragem
     */
	public void filtarBox(String nomeBox) {
        if (!StringUtil.isEmpty(nomeBox)) {
            List<BoxRoteirizacaoDTO> filtrados = new ArrayList<BoxRoteirizacaoDTO>();
            for (BoxRoteirizacaoDTO box : todosBox) {
                if (box.getNome().toUpperCase()
                        .startsWith(nomeBox.toUpperCase())) {
                    filtrados.add(box);
                }
            }
            boxDisponiveis = filtrados;
        } else {
            boxDisponiveis.clear();
            boxDisponiveis.addAll(todosBox);
        }
    }
    
    /**
     * Filtra os roteiros pela descricao
     * 
     * @param descricaoRoteiro descrição do roteiro para filtragem
     */
    public void filtarRoteiros(String descricaoRoteiro) {
        if (!StringUtil.isEmpty(descricaoRoteiro)) {
            List<RoteiroRoteirizacaoDTO> filtrados = new ArrayList<RoteiroRoteirizacaoDTO>();
            for (RoteiroRoteirizacaoDTO roteiro : todosRoteiros) {
                if (roteiro.getNome().toUpperCase()
                        .startsWith(descricaoRoteiro.toUpperCase())) {
                    filtrados.add(roteiro);
                }
            }
            roteiros = filtrados;
        } else {
            roteiros.clear();
            roteiros.addAll(todosRoteiros);
        }
    }

    /**
     * Recupera o roteiro pelo identificador
     * 
     * @param idRoteiro identificador do roteiro
     * @return roteiro com o identificador fornecido ou null caso não exista
     *         roteiro com o identificador fornecido
     */
    public RoteiroRoteirizacaoDTO getRoteiro(Long idRoteiro) {
        for (RoteiroRoteirizacaoDTO roteiro : todosRoteiros) {
            if (roteiro.getId().equals(idRoteiro)) {
                return roteiro;
            }
        }
        return null;
    }
    


	public void removerRoteiro(Long roteiroId) {
		
		removerRoteiroDeRoteiros(roteiroId);
		
		removerRoteiroDeTodosRoteiros(roteiroId);
		
		if (roteiroId != null  && roteiroId >= 0){
			
			this.addRoteiroExclusao(roteiroId);
		}
	}

	public void removerRoteiroTransferido(Long roteiroId) {
		
		removerRoteiroDeRoteiros(roteiroId);
		removerRoteiroDeTodosRoteiros(roteiroId);
	}

	private void removerRoteiroDeTodosRoteiros(Long roteiroId) {
		for (RoteiroRoteirizacaoDTO roteiro : todosRoteiros){
			
			if (roteiro.getId().equals(roteiroId)){
				
				todosRoteiros.remove(roteiro);
				break;
			}
		}
	}



	private void removerRoteiroDeRoteiros(Long roteiroId) {
		for (RoteiroRoteirizacaoDTO roteiro : roteiros){
			
			if (roteiro.getId().equals(roteiroId)){
				
				roteiros.remove(roteiro);
				break;
			}
		}
	}

	public void removerRoteiro(Integer ordemRoteiro) {
		
		for (RoteiroRoteirizacaoDTO roteiro : roteiros){
			
			if (roteiro.getOrdem().equals(ordemRoteiro)){
				
				roteiros.remove(roteiro);
				break;
			}
		}
		
		for (RoteiroRoteirizacaoDTO roteiro : todosRoteiros){
			
			if (roteiro.getOrdem().equals(ordemRoteiro)){
				
				todosRoteiros.remove(roteiro);
				
				if(roteiro.getId() > 0) {
					this.addRoteiroExclusao(roteiro.getId());
				}
				
				break;
			}
		}
	}
	

	public Map<Long, Set<RoteiroRoteirizacaoDTO>> getRoteirosTransferidos() {
		return roteirosTransferidos;
	}


	public void setRoteirosTransferidos(
			Map<Long, Set<RoteiroRoteirizacaoDTO>> roteirosTransferidos) {
		this.roteirosTransferidos = roteirosTransferidos;
	}
	
	    /**
     * Recupera a maior ordem dos roteiros da roteirização
     * 
     * @return valor da maior ordem da lista de roteiro ou 0 caso a lista esteja
     *         vazia
     */
    public int getMaiorOrdemRoteiro() {
        int max = 0;
        for (RoteiroRoteirizacaoDTO roteiro : todosRoteiros) {
            if (roteiro.getOrdem() > max) {
                max = roteiro.getOrdem();
            }
        }
        return max;
    }
    
    /**
     * Adiciona o roteiro aos roteiros da roteirização de acordo com a maior
     * ordem existente
     * 
     * @param roteiro roteiro para inclusão
     */
    public void addRoteiroAposMaiorOrdem(RoteiroRoteirizacaoDTO roteiro) {
       int maiorOrdem = getMaiorOrdemRoteiro();
       maiorOrdem++;
       roteiro.setOrdem(maiorOrdem);
       addRoteiro(roteiro);
    }

    /**
     * Tipo da edição tela
     * 
     */
    public static enum TipoEdicaoRoteirizacao {
        /**
         * Nova Roteirização
         */
        NOVO,
        /**
         * Alteração Roteirzação existente
         */
        ALTERACAO;
    }

}
