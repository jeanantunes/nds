package br.com.abril.nds.integracao.ems0131.outbound;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatBoolean;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0131Output implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// CAMPO -- NOME DA TABELA
	
	private Integer codigoDaCota; //NUMERO_COTA -- COTA
	private String nomeDoJornaleiro; //NOME -- PESSOA
	private Integer quantidadeDeCotas; 
	private String endereco; //LOGRADOURO -- ENDERECO
	private String codigoDoBairro; // CODIGO_BAIRRO -- ENDERECO
	private String municipio; //CIDADE -- ENDERECO
	private String siglaUf; //UF -- ENDERECO
	private String cep; //CEP -- ENDEERCO
	private String ddd; //DDD -- TELEFONE
	private String telefone; //NUMERO -- TELEFONE
	private String situacaoCota; //SITUACAO_CADASTRO -- COTA
	private String condPrazoPagamento;
	private String codigoDoBox; //CODIGO -- BOX
	private String codigoTipoBox; //TIPO_BOX -- BOX
	private boolean repartePorPdv;
	private Long codigoDoCapataz; //CODIGO -- ENTREGADOR
	private String cpfCnpj; //CPF -- PESSOA / CNPJ -- PESSOA
	private String tipoDePessoa; //TIPO -- PESSOA
	private String numeroDoLogradouro; //NUMERO -- ENDERECO
	private Integer codigoDaCidade; //CODIGO_CIDADE_IBGE -- ENDERECO
	private String inscricaoEstadual; //INSC_ESTADUAL -- PESSOA
	private String inscricaoMunicipal;//INSC_MUNICIPAL -- PESSOA
	
	
	
	
	@Field(offset = 1, length = 4)
	public Integer getCodigoDaCota() {
		return codigoDaCota;
	}
	public void setCodigoDaCota(Integer codigoDaCota) {
		this.codigoDaCota = codigoDaCota;
	}
	
	@Field(offset = 5, length = 30)
	public String getNomeDoJornaleiro() {
		return nomeDoJornaleiro;
	}
	public void setNomeDoJornaleiro(String nomeDoJornaleiro) {
		this.nomeDoJornaleiro = nomeDoJornaleiro;
	}
	
	@Field(offset = 35, length = 4)
	public Integer getQuantidadeDeCotas() {
		return quantidadeDeCotas;
	}
	public void setQuantidadeDeCotas(Integer quantidadeDeCotas) {
		this.quantidadeDeCotas = quantidadeDeCotas;
	}
	
	@Field(offset = 39, length = 40)
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	@Field(offset = 81, length = 3)
	public String getCodigoDoBairro() {
		return codigoDoBairro;
	}
	public void setCodigoDoBairro(String codigoDoBairro) {
		this.codigoDoBairro = codigoDoBairro;
	}
	
	@Field(offset = 84, length = 20)
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	
	@Field(offset = 104, length = 2)
	public String getSiglaUf() {
		return siglaUf;
	}
	public void setSiglaUf(String siglaUf) {
		this.siglaUf = siglaUf;
	}
	
	@Field(offset = 106, length = 8)
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	@Field(offset = 114, length = 4)
	public String getDdd() {
		return ddd;
	}
	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	
	@Field(offset = 118, length = 7)
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	@Field(offset = 125, length = 1)
	public String getSituacaoCota() {
		return situacaoCota;
	}
	public void setSituacaoCota(String situacaoCota) {
		this.situacaoCota = situacaoCota;
	}
	
	@Field(offset = 126, length = 1)
	public String getCondPrazoPagamento() {
		return condPrazoPagamento;
	}
	public void setCondPrazoPagamento(String condPrazoPagamento) {
		this.condPrazoPagamento = condPrazoPagamento;
	}
	
	@Field(offset = 127, length = 2)
	public String getCodigoDoBox() {
		return codigoDoBox;
	}
	public void setCodigoDoBox(String codigoDoBox) {
		this.codigoDoBox = codigoDoBox;
	}
	
	@Field(offset = 129, length = 2)
	public String getCodigoTipoBox() {
		return codigoTipoBox;
	}
	public void setCodigoTipoBox(String codigoTipoBox) {
		this.codigoTipoBox = codigoTipoBox;
	}
	
	@Field(offset = 131, length = 1)
	@FixedFormatBoolean(trueValue = "1", falseValue = "2")
	public boolean isRepartePorPdv() {
		return repartePorPdv;
	}
	public void setRepartePorPdv(boolean repartePorPdv) {
		this.repartePorPdv = repartePorPdv;
	}
	
	@Field(offset = 132, length = 6)
	public Long getCodigoDoCapataz() {
		return codigoDoCapataz;
	}
	public void setCodigoDoCapataz(Long codigoDoCapataz) {
		this.codigoDoCapataz = codigoDoCapataz;
	}
	
	@Field(offset = 138, length = 14)
	public String getCpfCnpj() {
		return cpfCnpj;
	}
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}
	
	@Field(offset = 152, length = 1)
	public String getTipoDePessoa() {
		return tipoDePessoa;
	}
	public void setTipoDePessoa(String tipoDePessoa) {
		this.tipoDePessoa = tipoDePessoa;
	}
	
	@Field(offset = 153, length = 6)
	public String getNumeroDoLogradouro() {
		return numeroDoLogradouro;
	}
	public void setNumeroDoLogradouro(String numeroDoLogradouro) {
		this.numeroDoLogradouro = numeroDoLogradouro;
	}
	
	@Field(offset = 159, length = 7)
	public Integer getCodigoDaCidade() {
		return codigoDaCidade;
	}
	public void setCodigoDaCidade(Integer codigoDaCidade) {
		this.codigoDaCidade = codigoDaCidade;
	}
	
	@Field(offset = 166, length = 20)
	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}
	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}
	
	@Field(offset = 186, length = 15)
	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}
	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}
	
	

}
