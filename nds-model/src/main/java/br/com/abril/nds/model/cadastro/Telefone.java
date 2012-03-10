package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
@Entity
@Table(name = "TELEFONE")
@SequenceGenerator(name="TELEFONE_SEQ", initialValue = 1, allocationSize = 1)
public class Telefone {

	@Id
	@GeneratedValue(generator = "TELEFONE_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NUMERO", nullable = false)
	private String numero;
	
	@Column(name = "RAMAL")
	private String ramal;

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
}