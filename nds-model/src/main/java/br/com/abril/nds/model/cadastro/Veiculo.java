package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "VEICULO")
@SequenceGenerator(name="VEICULO_SEQ", initialValue = 1, allocationSize = 1)
public class Veiculo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6564598213118529805L;
	
	@Id
	@GeneratedValue(generator = "VEICULO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "TIPO_VEICULO")
	private String tipoVeiculo;
	
	@Column(name = "PLACA")
	private String placa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoVeiculo() {
		return tipoVeiculo;
	}

	public void setTipoVeiculo(String tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}
}