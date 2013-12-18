package br.com.abril.nfe.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="NOTA_FISCAL_TRANSPORTADOR")
public class NotaFiscalTransportador extends NotaFiscalPessoa implements Serializable {
	
	private static final long serialVersionUID = -809704604912100870L;

	@Id
	@GeneratedValue()
	@Column(name="ID")
	private Long id;
	
	@Column(name = "FRETE")
	private	Integer frete;
	
	@Column(name = "TRANSPORTADORA_CNPJ")
	private	String transportadoraCNPJ;
	
	@Column(name = "TRANSPORTADORA_NOME")
	private	String transportadoraNome;
	
	@Column(name = "TRANSPORTADORA_INSCRICAO_ESTADUAL")
	private	String transportadoraInscricaoEstadual;
	
	@Column(name = "TRANSPORTADORA_ENDERECO")
	private	String transportadoraEndereco;
	
	@Column(name = "TRANSPORTADORA_MUNICIPIO")
	private	String transportadoraMunicipio;
	
	@Column(name = "TRANSPORTADORA_UF")
	private	String transportadoraUF;
	
	@Column(name = "TRANSPORTADORA_QUANTIDADE")
	private	String transportadoraQuantidade;
	
	@Column(name = "TRANSPORTADORA_ESPECIE")
	private	String transportadoraEspecie;
	
	@Column(name = "TRANSPORTADORA_MARCA")
	private	String transportadoraMarca;
	
	@Column(name = "TRANSPORTADORA_NUMERACAO")
	private	String transportadoraNumeracao;
	
	@Column(name = "TRANSPORTADORA_PESO_BRUTO")
	private	BigDecimal transportadoraPesoBruto;
	
	@Column(name = "TRANSPORTADORA_PESO_LIQUIDO")
	private	BigDecimal transportadoraPesoLiquido;
	
	@Column(name = "TRANSPORTADORA_ANTT")
	private	String transportadoraANTT;
	
	@Column(name = "TRANSPORTADORA_PLACA_VEICULO")
	private	String transportadoraPlacaVeiculo;
	
	@Column(name = "TRANSPORTADORA_PLACA_VEICULO_UF")
	private	String transportadoraPlacaVeiculoUF;
	
	@OneToOne(mappedBy="notaFicalEndereco")
	@JoinColumn(name="NOTA_FICAL_ENDERECO_ID", unique=true)
	private NotaFicalEndereco notaFicalEndereco;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getFrete() {
		return frete;
	}

	public void setFrete(Integer frete) {
		this.frete = frete;
	}

	public String getTransportadoraCNPJ() {
		return transportadoraCNPJ;
	}

	public void setTransportadoraCNPJ(String transportadoraCNPJ) {
		this.transportadoraCNPJ = transportadoraCNPJ;
	}

	public String getTransportadoraNome() {
		return transportadoraNome;
	}

	public void setTransportadoraNome(String transportadoraNome) {
		this.transportadoraNome = transportadoraNome;
	}

	public String getTransportadoraInscricaoEstadual() {
		return transportadoraInscricaoEstadual;
	}

	public void setTransportadoraInscricaoEstadual(
			String transportadoraInscricaoEstadual) {
		this.transportadoraInscricaoEstadual = transportadoraInscricaoEstadual;
	}

	public String getTransportadoraEndereco() {
		return transportadoraEndereco;
	}

	public void setTransportadoraEndereco(String transportadoraEndereco) {
		this.transportadoraEndereco = transportadoraEndereco;
	}

	public String getTransportadoraMunicipio() {
		return transportadoraMunicipio;
	}

	public void setTransportadoraMunicipio(String transportadoraMunicipio) {
		this.transportadoraMunicipio = transportadoraMunicipio;
	}

	public String getTransportadoraUF() {
		return transportadoraUF;
	}

	public void setTransportadoraUF(String transportadoraUF) {
		this.transportadoraUF = transportadoraUF;
	}

	public String getTransportadoraQuantidade() {
		return transportadoraQuantidade;
	}

	public void setTransportadoraQuantidade(String transportadoraQuantidade) {
		this.transportadoraQuantidade = transportadoraQuantidade;
	}

	public String getTransportadoraEspecie() {
		return transportadoraEspecie;
	}

	public void setTransportadoraEspecie(String transportadoraEspecie) {
		this.transportadoraEspecie = transportadoraEspecie;
	}

	public String getTransportadoraMarca() {
		return transportadoraMarca;
	}

	public void setTransportadoraMarca(String transportadoraMarca) {
		this.transportadoraMarca = transportadoraMarca;
	}

	public String getTransportadoraNumeracao() {
		return transportadoraNumeracao;
	}

	public void setTransportadoraNumeracao(String transportadoraNumeracao) {
		this.transportadoraNumeracao = transportadoraNumeracao;
	}

	public BigDecimal getTransportadoraPesoBruto() {
		return transportadoraPesoBruto;
	}

	public void setTransportadoraPesoBruto(BigDecimal transportadoraPesoBruto) {
		this.transportadoraPesoBruto = transportadoraPesoBruto;
	}

	public BigDecimal getTransportadoraPesoLiquido() {
		return transportadoraPesoLiquido;
	}

	public void setTransportadoraPesoLiquido(BigDecimal transportadoraPesoLiquido) {
		this.transportadoraPesoLiquido = transportadoraPesoLiquido;
	}

	public String getTransportadoraANTT() {
		return transportadoraANTT;
	}

	public void setTransportadoraANTT(String transportadoraANTT) {
		this.transportadoraANTT = transportadoraANTT;
	}

	public String getTransportadoraPlacaVeiculo() {
		return transportadoraPlacaVeiculo;
	}

	public void setTransportadoraPlacaVeiculo(String transportadoraPlacaVeiculo) {
		this.transportadoraPlacaVeiculo = transportadoraPlacaVeiculo;
	}

	public String getTransportadoraPlacaVeiculoUF() {
		return transportadoraPlacaVeiculoUF;
	}

	public void setTransportadoraPlacaVeiculoUF(String transportadoraPlacaVeiculoUF) {
		this.transportadoraPlacaVeiculoUF = transportadoraPlacaVeiculoUF;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public NotaFicalEndereco getNotaFicalEndereco() {
		return notaFicalEndereco;
	}

	public void setNotaFicalEndereco(NotaFicalEndereco notaFicalEndereco) {
		this.notaFicalEndereco = notaFicalEndereco;
	}

	@Override
	public String toString() {
		return "Transportador [id=" + id + ", frete=" + frete
				+ ", transportadoraCNPJ=" + transportadoraCNPJ
				+ ", transportadoraNome=" + transportadoraNome
				+ ", transportadoraInscricaoEstadual="
				+ transportadoraInscricaoEstadual + ", transportadoraEndereco="
				+ transportadoraEndereco + ", transportadoraMunicipio="
				+ transportadoraMunicipio + ", transportadoraUF="
				+ transportadoraUF + ", transportadoraQuantidade="
				+ transportadoraQuantidade + ", transportadoraEspecie="
				+ transportadoraEspecie + ", transportadoraMarca="
				+ transportadoraMarca + ", transportadoraNumeracao="
				+ transportadoraNumeracao + ", transportadoraPesoBruto="
				+ transportadoraPesoBruto + ", transportadoraPesoLiquido="
				+ transportadoraPesoLiquido + ", transportadoraANTT="
				+ transportadoraANTT + ", transportadoraPlacaVeiculo="
				+ transportadoraPlacaVeiculo
				+ ", transportadoraPlacaVeiculoUF="
				+ transportadoraPlacaVeiculoUF + "]";
	}
}
