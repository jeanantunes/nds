package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TRANSPORTADOR")
@SequenceGenerator(name="TRANSPORTADOR_SEQ", initialValue = 1, allocationSize = 1)
public class Transportador implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2299012453352522772L;
	
	@Id
	@GeneratedValue(generator = "TRANSPORTADOR_SEQ")
	@Column(name = "ID")
	private Long id;

	@OneToOne(optional = false)
	@JoinColumn(name = "PESSOA_JURIDICA_ID")
	private PessoaJuridica pessoaJuridica;
	
	@Column(name = "RESPONSAVEL")
	private String responsavel;
	
	@OneToMany(mappedBy = "transportador")
	private List<Veiculo> veiculos;
	
	@OneToMany(mappedBy = "transportador")
	private List<Motorista> motoristas;
	
	@OneToMany(mappedBy = "transportador")
	private List<AssociacaoVeiculoMotoristaRota> associacoesVeiculoMotoristaRota;
	
	@OneToMany(mappedBy = "transportador")
	private List<TelefoneTransportador> telefonesTransportador;
	
	@OneToMany(mappedBy = "transportador")
	private List<EnderecoTransportador> enderecosTransportador;
	
	@OneToOne
	@JoinColumn(name = "PARAM_COB_TRANSP_ID")
	private ParametroCobrancaTransportador parametroCobrancaTransportador;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public List<AssociacaoVeiculoMotoristaRota> getAssociacoesVeiculoMotoristaRota() {
		return associacoesVeiculoMotoristaRota;
	}

	public void setAssociacoesVeiculoMotoristaRota(
			List<AssociacaoVeiculoMotoristaRota> associacoesVeiculoMotoristaRota) {
		this.associacoesVeiculoMotoristaRota = associacoesVeiculoMotoristaRota;
	}

	public List<TelefoneTransportador> getTelefonesTransportador() {
		return telefonesTransportador;
	}

	public void setTelefonesTransportador(
			List<TelefoneTransportador> telefonesTransportador) {
		this.telefonesTransportador = telefonesTransportador;
	}

	public List<EnderecoTransportador> getEnderecosTransportador() {
		return enderecosTransportador;
	}

	public void setEnderecosTransportador(
			List<EnderecoTransportador> enderecosTransportador) {
		this.enderecosTransportador = enderecosTransportador;
	}

	public List<Motorista> getMotoristas() {
		return motoristas;
	}

	public void setMotoristas(List<Motorista> motoristas) {
		this.motoristas = motoristas;
	}

	public ParametroCobrancaTransportador getParametroCobrancaTransportador() {
		return parametroCobrancaTransportador;
	}

	public void setParametroCobrancaTransportador(
			ParametroCobrancaTransportador parametroCobrancaTransportador) {
		this.parametroCobrancaTransportador = parametroCobrancaTransportador;
	}
}