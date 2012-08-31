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
	
    public static TelefoneDTO fromTelefone(Telefone telefone) {
        TelefoneDTO dto = new TelefoneDTO();
        dto.setId(telefone.getId());
        dto.setDdd(telefone.getDdd());
        dto.setNumero(telefone.getNumero());
        dto.setRamal(telefone.getRamal());
        return dto;
    }
    
}