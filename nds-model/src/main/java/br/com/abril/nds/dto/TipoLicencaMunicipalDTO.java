package br.com.abril.nds.dto;

import java.io.Serializable;

/**
 * DTO com as informações do tipo de licença municipal
 * 
 * @author francisco.garcia
 *
 */
public class TipoLicencaMunicipalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long codigo;

    private String descricao;

    public TipoLicencaMunicipalDTO() {
    }

    public TipoLicencaMunicipalDTO(Long id, Long codigo, String descricao) {
        this(codigo, descricao);
        this.id = id;
    }
    
    public TipoLicencaMunicipalDTO(Long codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

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
     * @param codigo
     *            the codigo to set
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
     * @param descricao
     *            the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TipoLicencaMunicipalDTO other = (TipoLicencaMunicipalDTO) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
