package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.dto.PdvRoteirizacaoDTO.OrigemEndereco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
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
     * Cotas destinadas a copia para determinada rota.
     */
    private RotaRoteirizacaoDTO rotaCotasCopia;
    
    private RoteirizacaoDTO(TipoEdicaoRoteirizacao tipoEdicao, List<BoxRoteirizacaoDTO> boxDisponiveis) {
        this.tipoEdicao = tipoEdicao;
        this.boxDisponiveis = new ArrayList<BoxRoteirizacaoDTO>();
        if (TipoEdicaoRoteirizacao.NOVO == tipoEdicao || boxDisponiveis.isEmpty()) {
            this.boxDisponiveis.add(BoxRoteirizacaoDTO.ESPECIAL);
        }
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
    
	/**
	 * @return the rotaCotasCopia
	 */
	public RotaRoteirizacaoDTO getRotaCotasCopia() {
		return rotaCotasCopia;
	}

	/**
	 * @param rotaCotasCopia the rotaCotasCopia to set
	 */
	public void setRotaCotasCopia(RotaRoteirizacaoDTO rotaCotasCopia) {
		this.rotaCotasCopia = rotaCotasCopia;
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
		todosRoteiros.add(roteiro);
	}
	
	/**
	 * Adiciona novos Roteiros à Roteirizacao
	 * @param listaRoteiro: List<RoteiroRoteirizacaoDTO> para inclusão
	 */
	public void addAllRoteiro(List<RoteiroRoteirizacaoDTO> listaRoteiro){
		if (roteiros == null){
			roteiros = new ArrayList<RoteiroRoteirizacaoDTO>();
		}
		roteiros.addAll(listaRoteiro);
	}
	
    /**
     * Constrói um dto para a criação de uma nova roteirização
     * 
     * @param boxDisponiveis
     *            lista de box disponíveis para a roteirização
     * @return {@link RoteirizacaoDTO} para a criação de uma nova roteirização
     */
	public static RoteirizacaoDTO novaRoteirizacao(List<BoxRoteirizacaoDTO> boxDisponiveis) {
	    return new RoteirizacaoDTO(TipoEdicaoRoteirizacao.NOVO, boxDisponiveis);
	}
	
    /**
     * Cria o DTO à partir de uma roteirização existente
     * 
     * @param roteirizacao
     *            roteirização existente para criação do DTO
     * @param dtos lista de boxes disponíveis
     * @return DTO com as informações da roteirização existente
     */
	public static RoteirizacaoDTO toDTO(Roteirizacao roteirizacao, List<Box> disponiveis) {
	    RoteirizacaoDTO dto = new RoteirizacaoDTO(TipoEdicaoRoteirizacao.ALTERACAO, BoxRoteirizacaoDTO.toDTOs(disponiveis));
	    dto.setId(roteirizacao.getId());

        Box box = roteirizacao.getBox();
        BoxRoteirizacaoDTO boxDTO = null;
        if (box != null) {
            boxDTO = new BoxRoteirizacaoDTO(box.getId(), box.getNome());
        } else {
            boxDTO = BoxRoteirizacaoDTO.ESPECIAL;
        }
        dto.setBox(boxDTO);
        
        for(Roteiro roteiro : roteirizacao.getRoteiros()){
            RoteiroRoteirizacaoDTO roteiroDTO = new RoteiroRoteirizacaoDTO(
                    roteiro.getId(), roteiro.getOrdem(),
                    roteiro.getDescricaoRoteiro());
            dto.addRoteiro(roteiroDTO);

            for(Rota rota : roteiro.getRotas()){
                RotaRoteirizacaoDTO rotaDTO = new RotaRoteirizacaoDTO(
                        rota.getId(), rota.getOrdem(), rota.getDescricaoRota());
                roteiroDTO.addRota(rotaDTO);

                for(RotaPDV rotaPdv : rota.getRotaPDVs()){
                    PDV pdv = rotaPdv.getPdv();
                    Cota cota = rotaPdv.getPdv().getCota();
                    String nomeCota = cota.getPessoa().getNome();
                    OrigemEndereco origemEndereco = null;

                    Endereco endereco = null;
                    EnderecoPDV enderecoPdvEntrega  = pdv.getEnderecoEntrega();
                    if (enderecoPdvEntrega != null){
                        endereco = enderecoPdvEntrega .getEndereco();
                        origemEndereco = OrigemEndereco.PDV;
                    } else {
                        EnderecoCota enderecoPrincipalCota = cota.getEnderecoPrincipal();
                        if (enderecoPrincipalCota != null){
                            endereco = enderecoPrincipalCota.getEndereco();
                        }    
                        origemEndereco = OrigemEndereco.COTA;
                    }
                    PdvRoteirizacaoDTO pdvDTO = new PdvRoteirizacaoDTO(
                            rotaPdv.getId(), pdv.getNome(), origemEndereco,
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
     * Recupera a rota da roteirização pelo id
     * 
     * @param id
     *            identificador da rota
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
     * @param idRoteiro
     *            identificador do roteiro
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
