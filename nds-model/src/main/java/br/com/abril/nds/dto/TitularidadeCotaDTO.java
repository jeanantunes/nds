package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Representa as informações sobre titularidade
 * da cota
 * 
 * @author francisco.garcia
 *
 */
public class TitularidadeCotaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Identificador da titularidade
     */
    private Long id;
    
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
     * Documento de identificação do proprietário
     */
    private String documento;
 
    public TitularidadeCotaDTO(Long id, Long idCota, Date inicio, Date fim,
            String nome, String documento) {

        this.id = id;
        this.idCota = idCota;
        if (inicio != null) {
            this.inicio = new Date(inicio.getTime());
        }
        if (fim != null) {
            this.fim = new Date(fim.getTime());
        }
        this.nome = nome;
        this.documento = documento;
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /* (non-Javadoc)
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
        TitularidadeCotaDTO other = (TitularidadeCotaDTO) obj;
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
