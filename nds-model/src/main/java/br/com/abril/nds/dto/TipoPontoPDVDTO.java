package br.com.abril.nds.dto;

import java.io.Serializable;

/**
 * DTO com as informações do Tipo de Ponto do PDV
 * 
 * @author francisco.garcia
 *
 */
public class TipoPontoPDVDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id; 

    private Long codigo;
    
    private String descricao;

    public TipoPontoPDVDTO() {
    }
    
    public TipoPontoPDVDTO(Long codigo, String descricao) {
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
