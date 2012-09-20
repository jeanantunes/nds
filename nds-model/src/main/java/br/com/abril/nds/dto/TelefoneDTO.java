package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Telefone;

/**
 * DTO com as informações de telefone
 * 
 * @author francisco.garcia
 *
 */
public class TelefoneDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
	
	private String numero;
	
	private String ramal;
	
    private String ddd;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	
	public String toString(){
		return ddd + " - " + numero;
	}
	
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ddd == null) ? 0 : ddd.hashCode());
        result = prime * result + ((numero == null) ? 0 : numero.hashCode());
        result = prime * result + ((ramal == null) ? 0 : ramal.hashCode());
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
        TelefoneDTO other = (TelefoneDTO) obj;
        if (ddd == null) {
            if (other.ddd != null) {
                return false;
            }
        } else if (!ddd.equals(other.ddd)) {
            return false;
        }
        if (numero == null) {
            if (other.numero != null) {
                return false;
            }
        } else if (!numero.equals(other.numero)) {
            return false;
        }
        if (ramal == null) {
            if (other.ramal != null) {
                return false;
            }
        } else if (!ramal.equals(other.ramal)) {
            return false;
        }
        return true;
    }

    /**
     * Method Factory para criação do {@link TelefoneDTO} à 
     * partir da entidade telefone
     * @param telefone entidade telefone para criação do DTO
     * @return {@link TelefoneDTO} criado à partir da entidade telefone
     */
	public static TelefoneDTO fromTelefone(Telefone telefone) {
        TelefoneDTO dto = new TelefoneDTO();
        dto.setId(telefone.getId());
        dto.setDdd(telefone.getDdd());
        dto.setNumero(telefone.getNumero());
        dto.setRamal(telefone.getRamal());
        return dto;
    }
    
}