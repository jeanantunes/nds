package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Motorista;
import br.com.abril.nds.model.cadastro.Veiculo;

public class AssociacaoVeiculoMotoristaRotaDTO implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -7316664606283093106L;

	public AssociacaoVeiculoMotoristaRotaDTO(Long id, Veiculo veiculo, Motorista motorista, RotaRoteiroDTO rota){
		
		if (id == null){
			
			this.id = (long) (Math.random() * -10000);
		} else {
			
			this.id = id;
		}
		
		this.veiculo = veiculo;
		this.motorista = motorista;
		this.rota = rota;
	}
	
	private Long id;
	
	private Veiculo veiculo;
	
	private Motorista motorista;
	
	private RotaRoteiroDTO rota;

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

	public RotaRoteiroDTO getRota() {
		return rota;
	}

	public void setRota(RotaRoteiroDTO rota) {
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
		AssociacaoVeiculoMotoristaRotaDTO other = (AssociacaoVeiculoMotoristaRotaDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}