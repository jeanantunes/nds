package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ASSOC_VEICULO_MOTORISTA_ROTA")
@SequenceGenerator(name="ASSOC_VEIC_MOT_ROTA_SEQ", initialValue = 1, allocationSize = 1)
public class AssociacaoVeiculoMotoristaRota implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2668219843880783863L;

	@Id
	@GeneratedValue(generator = "ASSOC_VEIC_MOT_ROTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "TRANSPORTADOR_ID")
	private Transportador transportador;
	
	@OneToOne
	@JoinColumn(name = "VEICULO")
	private Veiculo veiculo;
	
	@OneToOne
	@JoinColumn(name = "MOTORISTA")
	private Motorista motorista;
	
	@OneToOne
	@JoinColumn(name = "ROTA")
	private Rota rota;

	public Transportador getTransportador() {
		return transportador;
	}

	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		AssociacaoVeiculoMotoristaRota other = (AssociacaoVeiculoMotoristaRota) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}