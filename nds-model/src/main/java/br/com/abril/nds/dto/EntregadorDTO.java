package br.com.abril.nds.dto;

import java.io.Serializable;

public class EntregadorDTO implements Serializable {

	private static final long serialVersionUID = 4358177424603794804L;

	private Long idEntregador;
	
	private String nomeEntregador;
	
	private Integer codigoBox;
	
	private String descricaoRoteiro;
	
	private String descricaoRota;

	public EntregadorDTO() {
		
	}
			
	public EntregadorDTO(Long idEntregador, String nomeEntregador) {
		super();
		this.idEntregador = idEntregador;
		this.nomeEntregador = nomeEntregador;
	}

	/**
	 * @return the idEntregador
	 */
	public Long getIdEntregador() {
		return idEntregador;
	}

	/**
	 * @param idEntregador the idEntregador to set
	 */
	public void setIdEntregador(Long idEntregador) {
		this.idEntregador = idEntregador;
	}

	/**
	 * @return the nomeEntregador
	 */
	public String getNomeEntregador() {
		return nomeEntregador;
	}

	/**
	 * @param nomeEntregador the nomeEntregador to set
	 */
	public void setNomeEntregador(String nomeEntregador) {
		this.nomeEntregador = nomeEntregador;
	}

    
    public Integer getCodigoBox() {
        return codigoBox;
    }

    
    public void setCodigoBox(Integer codigoBox) {
        this.codigoBox = codigoBox;
    }

    
    public String getDescricaoRoteiro() {
        return descricaoRoteiro;
    }

    
    public void setDescricaoRoteiro(String descricaoRoteiro) {
        this.descricaoRoteiro = descricaoRoteiro;
    }

    
    public String getDescricaoRota() {
        return descricaoRota;
    }

    
    public void setDescricaoRota(String descricaoRota) {
        this.descricaoRota = descricaoRota;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idEntregador == null) ? 0 : idEntregador.hashCode());
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
        EntregadorDTO other = (EntregadorDTO) obj;
        if (idEntregador == null) {
            if (other.idEntregador != null)
                return false;
        } else if (!idEntregador.equals(other.idEntregador))
            return false;
        return true;
    }
}
