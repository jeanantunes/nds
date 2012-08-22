package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Representa as informações sobre proprietário
 * da cota
 * 
 * @author francisco.garcia
 *
 */
public class CotaProprietarioDTO implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * Identificador da Cota
     */
    private Long idCota;
    
    /**
     * Início do período de propriedade da cota
     */
    private Date inicio;
    
    /**
     * Fim do período de propriedade da cota
     */
    private Date fim;
    
    /**
     * Nome/Razão social do proprietário 
     */
    private String nome;
    
    /**
     * Documento de identificação di proprietário
     */
    private String documento;
 
    public CotaProprietarioDTO(Long idCota, Date inicio, Date fim, String nome,
            String documento) {
        this.idCota = idCota;
        this.inicio = inicio;
        this.fim = fim;
        this.nome = nome;
        this.documento = documento;
    }

    /**
     * @return the idCota
     */
    public Long getIdCota() {
        return idCota;
    }

    /**
     * @return the inicio
     */
    public Date getInicio() {
        return inicio;
    }

    /**
     * @return the fim
     */
    public Date getFim() {
        return fim;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the documento
     */
    public String getDocumento() {
        return documento;
    }

}
