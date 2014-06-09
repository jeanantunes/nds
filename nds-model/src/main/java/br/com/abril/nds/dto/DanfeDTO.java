package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DanfeDTO implements Serializable{

	private static final long serialVersionUID = -409988158737123482L;

	private String naturezaOperacao;

	private String formaPagamento;

	private String serie;

	private Long numeroNF;

	private Date dataEmissao;

	private Date dataSaida;

	private String horaSaida;

	private int tipoNF;

	private String ambiente;

	private String chave;

	private String protocolo;

	private String versao;

	private String emissorCNPJ;

	private String emissorNome;

	private String emissorFantasia;

	private String emissorInscricaoEstadual;

	private String emissorInscricaoEstadualSubstituto;

	private String emissorInscricaoMunicipal;

	private String emissorLogradouro;

	private String emissorNumero;

	private String emissorBairro;

	private String emissorMunicipio;

	private String emissorUF;

	private String emissorCEP;

	private String emissorTelefone;

	private String destinatarioCNPJ;

	private String destinatarioNome;

	private String destinatarioInscricaoEstadual;

	private String destinatarioLogradouro;

	private String destinatarioNumero;

	private String destinatarioComplemento;

	private String destinatarioBairro;

	private String destinatarioMunicipio;

	private String destinatarioUF;

	private String destinatarioCEP;

	private String destinatarioTelefone;

	private String destinatarioPais;
	
	private BigDecimal valorBaseICMS;

	private BigDecimal valorICMS;

	private BigDecimal valorBaseICMSSubstituto;

	private BigDecimal valorICMSSubstituto;

	private BigDecimal valorProdutos;

	private BigDecimal valorFrete;

	private BigDecimal valorSeguro;

	private BigDecimal valorDesconto;

	private BigDecimal valorOutro;

	private BigDecimal valorIPI;

	private BigDecimal valorNF;

	private Integer frete;

	private String transportadoraCNPJ;

	private String transportadoraNome;

	private String transportadoraInscricaoEstadual;

	private String transportadoraEndereco;

	private String transportadoraMunicipio;

	private String transportadoraUF;

	private String transportadoraQuantidade;

	private String transportadoraEspecie;

	private String transportadoraMarca;

	private String transportadoraNumeracao;

	private BigDecimal transportadoraPesoBruto;

	private BigDecimal transportadoraPesoLiquido;

	private String transportadoraANTT;

	private String transportadoraPlacaVeiculo;

	private String transportadoraPlacaVeiculoUF;

	private BigDecimal ISSQNTotal;

	private BigDecimal ISSQNBase;

	private BigDecimal ISSQNValor;

	private String informacoesComplementares;
	
	private String numeroFatura;
	
	private BigDecimal valorFatura;
	
	private BigDecimal valorLiquido;
	
	private List<ItemDanfe> itensDanfe;
	
	private List<Duplicata> faturas;

	public String getNaturezaOperacao() {
		return naturezaOperacao;
	}

	public void setNaturezaOperacao(String naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Long getNumeroNF() {
		return numeroNF;
	}

	public void setNumeroNF(Long numeroNF) {
		this.numeroNF = numeroNF;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}

	public String getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(String horaSaida) {
		this.horaSaida = horaSaida;
	}

	public int getTipoNF() {
		return tipoNF;
	}

	public void setTipoNF(int tipoNF) {
		this.tipoNF = tipoNF;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public String getEmissorCNPJ() {
		return emissorCNPJ;
	}

	public void setEmissorCNPJ(String emissorCNPJ) {
		this.emissorCNPJ = emissorCNPJ;
	}

	public String getEmissorNome() {
		return emissorNome;
	}

	public void setEmissorNome(String emissorNome) {
		this.emissorNome = emissorNome;
	}

	public String getEmissorFantasia() {
		return emissorFantasia;
	}

	public void setEmissorFantasia(String emissorFantasia) {
		this.emissorFantasia = emissorFantasia;
	}

	public String getEmissorInscricaoEstadual() {
		return emissorInscricaoEstadual;
	}

	public void setEmissorInscricaoEstadual(String emissorInscricaoEstadual) {
		this.emissorInscricaoEstadual = emissorInscricaoEstadual;
	}

	public String getEmissorInscricaoEstadualSubstituto() {
		return emissorInscricaoEstadualSubstituto;
	}

	public void setEmissorInscricaoEstadualSubstituto(
			String emissorInscricaoEstadualSubstituto) {
		this.emissorInscricaoEstadualSubstituto = emissorInscricaoEstadualSubstituto;
	}

	public String getEmissorInscricaoMunicipal() {
		return emissorInscricaoMunicipal;
	}

	public void setEmissorInscricaoMunicipal(String emissorInscricaoMunicipal) {
		this.emissorInscricaoMunicipal = emissorInscricaoMunicipal;
	}

	public String getEmissorLogradouro() {
		return emissorLogradouro;
	}

	public void setEmissorLogradouro(String emissorLogradouro) {
		this.emissorLogradouro = emissorLogradouro;
	}

	public String getEmissorNumero() {
		return emissorNumero;
	}

	public void setEmissorNumero(String emissorNumero) {
		this.emissorNumero = emissorNumero;
	}

	public String getEmissorBairro() {
		return emissorBairro;
	}

	public void setEmissorBairro(String emissorBairro) {
		this.emissorBairro = emissorBairro;
	}

	public String getEmissorMunicipio() {
		return emissorMunicipio;
	}

	public void setEmissorMunicipio(String emissorMunicipio) {
		this.emissorMunicipio = emissorMunicipio;
	}

	public String getEmissorUF() {
		return emissorUF;
	}

	public void setEmissorUF(String emissorUF) {
		this.emissorUF = emissorUF;
	}

	public String getEmissorCEP() {
		return emissorCEP;
	}

	public void setEmissorCEP(String emissorCEP) {
		this.emissorCEP = emissorCEP;
	}

	public String getEmissorTelefone() {
		return emissorTelefone;
	}

	public void setEmissorTelefone(String emissorTelefone) {
		this.emissorTelefone = emissorTelefone;
	}

	public String getDestinatarioCNPJ() {
		return destinatarioCNPJ;
	}

	public void setDestinatarioCNPJ(String destinatarioCNPJ) {
		this.destinatarioCNPJ = destinatarioCNPJ;
	}

	public String getDestinatarioNome() {
		return destinatarioNome;
	}

	public void setDestinatarioNome(String destinatarioNome) {
		this.destinatarioNome = destinatarioNome;
	}

	public String getDestinatarioInscricaoEstadual() {
		return destinatarioInscricaoEstadual;
	}

	public void setDestinatarioInscricaoEstadual(
			String destinatarioInscricaoEstadual) {
		this.destinatarioInscricaoEstadual = destinatarioInscricaoEstadual;
	}

	public String getDestinatarioLogradouro() {
		return destinatarioLogradouro;
	}

	public void setDestinatarioLogradouro(String destinatarioLogradouro) {
		this.destinatarioLogradouro = destinatarioLogradouro;
	}

	public String getDestinatarioNumero() {
		return destinatarioNumero;
	}

	public void setDestinatarioNumero(String destinatarioNumero) {
		this.destinatarioNumero = destinatarioNumero;
	}

	public String getDestinatarioComplemento() {
		return destinatarioComplemento;
	}

	public void setDestinatarioComplemento(String destinatarioComplemento) {
		this.destinatarioComplemento = destinatarioComplemento;
	}

	public String getDestinatarioBairro() {
		return destinatarioBairro;
	}

	public void setDestinatarioBairro(String destinatarioBairro) {
		this.destinatarioBairro = destinatarioBairro;
	}

	public String getDestinatarioMunicipio() {
		return destinatarioMunicipio;
	}

	public void setDestinatarioMunicipio(String destinatarioMunicipio) {
		this.destinatarioMunicipio = destinatarioMunicipio;
	}

	public String getDestinatarioUF() {
		return destinatarioUF;
	}

	public void setDestinatarioUF(String destinatarioUF) {
		this.destinatarioUF = destinatarioUF;
	}

	public String getDestinatarioCEP() {
		return destinatarioCEP;
	}

	public void setDestinatarioCEP(String destinatarioCEP) {
		this.destinatarioCEP = destinatarioCEP;
	}

	public String getDestinatarioTelefone() {
		return destinatarioTelefone;
	}

	public void setDestinatarioTelefone(String destinatarioTelefone) {
		this.destinatarioTelefone = destinatarioTelefone;
	}
	
	public String getDestinatarioPais() {
		return destinatarioPais;
	}

	public void setDestinatarioPais(String destinatarioPais) {
		this.destinatarioPais = destinatarioPais;
	}

	public BigDecimal getValorBaseICMS() {
		return valorBaseICMS;
	}

	public void setValorBaseICMS(BigDecimal valorBaseICMS) {
		this.valorBaseICMS = valorBaseICMS;
	}

	public BigDecimal getValorICMS() {
		return valorICMS;
	}

	public void setValorICMS(BigDecimal valorICMS) {
		this.valorICMS = valorICMS;
	}

	public BigDecimal getValorBaseICMSSubstituto() {
		return valorBaseICMSSubstituto;
	}

	public void setValorBaseICMSSubstituto(BigDecimal valorBaseICMSSubstituto) {
		this.valorBaseICMSSubstituto = valorBaseICMSSubstituto;
	}

	public BigDecimal getValorICMSSubstituto() {
		return valorICMSSubstituto;
	}

	public void setValorICMSSubstituto(BigDecimal valorICMSSubstituto) {
		this.valorICMSSubstituto = valorICMSSubstituto;
	}

	public BigDecimal getValorProdutos() {
		return valorProdutos;
	}

	public void setValorProdutos(BigDecimal valorProdutos) {
		this.valorProdutos = valorProdutos;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getValorSeguro() {
		return valorSeguro;
	}

	public void setValorSeguro(BigDecimal valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorOutro() {
		return valorOutro;
	}

	public void setValorOutro(BigDecimal valorOutro) {
		this.valorOutro = valorOutro;
	}

	public BigDecimal getValorIPI() {
		return valorIPI;
	}

	public void setValorIPI(BigDecimal valorIPI) {
		this.valorIPI = valorIPI;
	}

	public BigDecimal getValorNF() {
		return valorNF;
	}

	public void setValorNF(BigDecimal valorNF) {
		this.valorNF = valorNF;
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

	public BigDecimal getISSQNTotal() {
		return ISSQNTotal;
	}

	public void setISSQNTotal(BigDecimal iSSQNTotal) {
		ISSQNTotal = iSSQNTotal;
	}

	public BigDecimal getISSQNBase() {
		return ISSQNBase;
	}

	public void setISSQNBase(BigDecimal iSSQNBase) {
		ISSQNBase = iSSQNBase;
	}

	public BigDecimal getISSQNValor() {
		return ISSQNValor;
	}

	public void setISSQNValor(BigDecimal iSSQNValor) {
		ISSQNValor = iSSQNValor;
	}

	public String getInformacoesComplementares() {
		return informacoesComplementares;
	}

	public void setInformacoesComplementares(String informacoesComplementares) {
		this.informacoesComplementares = informacoesComplementares;
	}

	public String getNumeroFatura() {
		return numeroFatura;
	}

	public void setNumeroFatura(String numeroFatura) {
		this.numeroFatura = numeroFatura;
	}

	public BigDecimal getValorFatura() {
		return valorFatura;
	}

	public void setValorFatura(BigDecimal valorFatura) {
		this.valorFatura = valorFatura;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public List<ItemDanfe> getItensDanfe() {
		return itensDanfe;
	}

	public void setItensDanfe(List<ItemDanfe> itensDanfe) {
		this.itensDanfe = itensDanfe;
	}

	public List<Duplicata> getFaturas() {
		return faturas;
	}

	public void setFaturas(List<Duplicata> faturas) {
		this.faturas = faturas;
	}
}