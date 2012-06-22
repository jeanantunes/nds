package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
/**
 * veicTransp <br/>
 * Grupo Veículo

 * @author Diego Fernandes
 *
 */
@Embeddable
public class Veiculo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1032722287961928669L;
	
	

	/**
	 * placa
	 */
	@Column(name="PLACA_VEICULO_TRANS", nullable=true, length=7)
	private String placa;
	
	/**
	 * UF
	 */
	@Column(name="UF_VEICULO_TRANS", nullable=true, length=2)
	private String uf;
	
	
	/**
	 * RNTC - Registro Nacional de Transportador de Carga (ANTT)
	 */
	@Column(name="RNTC_VEICULO_TRANS", nullable=true, length=2)
	private String registroTransCarga;


	/**
	 * @return the placa
	 */
	public String getPlaca() {
		return placa;
	}


	/**
	 * @param placa the placa to set
	 */
	public void setPlaca(String placa) {
		this.placa = placa;
	}


	/**
	 * @return the uf
	 */
	public String getUf() {
		return uf;
	}


	/**
	 * @param uf the uf to set
	 */
	public void setUf(String uf) {
		this.uf = uf;
	}


	/**
	 * @return the registroTransCarga
	 */
	public String getRegistroTransCarga() {
		return registroTransCarga;
	}


	/**
	 * @param registroTransCarga the registroTransCarga to set
	 */
	public void setRegistroTransCarga(String registroTransCarga) {
		this.registroTransCarga = registroTransCarga;
	}

}
