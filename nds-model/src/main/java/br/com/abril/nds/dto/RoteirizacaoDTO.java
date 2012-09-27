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

public class RoteirizacaoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	/**
	 * Box da roteirizacao
	 */
	private BoxRoteirizacaoDTO box;
	
	/**
	 * Roteiros selecionados
	 */
	private List<RoteiroRoteirizacaoDTO> roteiros;
	
    /**
     * Tipo da Edição
     */
	private TipoEdicao tipoEdicao;
	
	/**
	 * Box disponíveis
	 */
	private List<BoxRoteirizacaoDTO> boxDisponiveis;
	
    private RoteirizacaoDTO(TipoEdicao tipoEdicao) {
        this.tipoEdicao = tipoEdicao;
    }


    private RoteirizacaoDTO(TipoEdicao tipoEdicao, List<BoxRoteirizacaoDTO> boxDisponiveis) {
        this(tipoEdicao);
        this.boxDisponiveis = boxDisponiveis;
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
    
    
    public TipoEdicao getTipoEdicao() {
        return tipoEdicao;
    }
    
    public void setTipoEdicao(TipoEdicao tipoEdicao) {
        this.tipoEdicao = tipoEdicao;
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
	
    /**
     * Constrói um dto para a criação de uma nova roteirização
     * 
     * @param boxDisponiveis
     *            lista de box disponíveis para a roteirização
     * @return {@link RoteirizacaoDTO} para a criação de uma nova roteirização
     */
	public static RoteirizacaoDTO novaRoteirizacao(List<BoxRoteirizacaoDTO> boxDisponiveis) {
	    return new RoteirizacaoDTO(TipoEdicao.NOVO, boxDisponiveis);
	}
	
    /**
     * Cria o DTO à partir de uma roteirização existente
     * 
     * @param roteirizacao
     *            roteirização existente para criação do DTO
     * @return DTO com as informações da roteirização existente
     */
	public static RoteirizacaoDTO toDTO(Roteirizacao roteirizacao) {
	    RoteirizacaoDTO dto = new RoteirizacaoDTO(TipoEdicao.ALTERACAO);
	    dto.setId(roteirizacao.getId());

        Box box = roteirizacao.getBox();
        if (box != null) {
            BoxRoteirizacaoDTO boxDTO = new BoxRoteirizacaoDTO(box.getId(), box.getNome());
            dto.setBox(boxDTO);
        }

        for(Roteiro roteiro : roteirizacao.getRoteiros()){
            RoteiroRoteirizacaoDTO roteiroDTO = new RoteiroRoteirizacaoDTO(
                    roteiro.getId(), roteiro.getOrdem(),
                    roteiro.getDescricaoRoteiro());
            dto.addRoteiro(roteiroDTO);

            for(Rota rota : roteiro.getRotas()){
                RotaRoteirizacaoDTO rotaDTO = new RotaRoteirizacaoDTO(
                        rota.getId(), rota.getOrdem(), rota.getDescricaoRota());
                roteiroDTO.addRota(rotaDTO);

                for(PDV pdv : rota.getPdvs()){
                    Cota cota = pdv.getCota();
                    String nomeCota = cota.getPessoa().getNome();
                    OrigemEndereco origemEndereco = null;

                    Endereco endereco = null;
                    EnderecoPDV enderecoPdvEntrega  = pdv.getEnderecoEntrega();
                    if (enderecoPdvEntrega != null){
                        endereco = enderecoPdvEntrega .getEndereco();
                        origemEndereco = OrigemEndereco.PDV;
                    } else {
                        EnderecoCota enderecoPrincipalCota = pdv.getCota().getEnderecoPrincipal();
                        if (enderecoPrincipalCota != null){
                            endereco = enderecoPrincipalCota.getEndereco();
                        }    
                        origemEndereco = OrigemEndereco.COTA;
                    }
                    PdvRoteirizacaoDTO pdvDTO = new PdvRoteirizacaoDTO(
                            pdv.getId(), pdv.getNome(), origemEndereco,
                            endereco, cota.getNumeroCota(), nomeCota,
                            pdv.getOrdem());
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
	    return TipoEdicao.NOVO == tipoEdicao;
	}

    
	/**
     * Tipo da edição tela
     * 
     */
    public static enum TipoEdicao {
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
