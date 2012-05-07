package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
	
	@OneToMany
	@JoinTable(name = "TRANSPORTADO_ASSOCIACAO", joinColumns = {@JoinColumn(name = "TRANSPORTADOR_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "ASSOCIACAO_ID")})
	private List<AssociacaoVeiculoMotoristaRota> associacoesVeiculoMotoristaRota;
	
	@OneToMany(mappedBy = "transportador")
	private List<TelefoneTransportador> telefonesTransportador;
	
	@OneToMany(mappedBy = "transportador")
	private List<EnderecoTransportador> enderecosTransportador;

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
}