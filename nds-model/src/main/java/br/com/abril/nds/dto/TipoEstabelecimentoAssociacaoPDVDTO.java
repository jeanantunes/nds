package br.com.abril.nds.dto;

import br.com.abril.nds.model.cadastro.CodigoDescricao;

/**
 * DTO com as informações do tipo de estabelecimento em que o PDV pode estar
 * associado
 * 
 * @author francisco.garcia
 * 
 */
public class TipoEstabelecimentoAssociacaoPDVDTO  extends CodigoDescricao {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
    private Long codigo;
    
    private String descricao;
    
    public TipoEstabelecimentoAssociacaoPDVDTO() {
    }
    
    public TipoEstabelecimentoAssociacaoPDVDTO(Long codigo, String descricao) {
        this(null, codigo, descricao);
    }

    public TipoEstabelecimentoAssociacaoPDVDTO(Long id, Long codigo,
            String descricao) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
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
     * @return the codigo
     */
    public Long getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
