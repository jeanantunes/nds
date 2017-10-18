package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CotaCouchDTO implements Serializable{
	
	private static final long serialVersionUID = 1392799667450591672L;
	private Long id;
	private String codigoDistribuidor;
	private String jornaleiro;
	private String codigoCota;
	private String codigoPontoVenda;
	private Date dataMovimento;
	private String sistema;
	private String nome;
	private String tipoLogradouro;
	private String enderecoLogradouro;
	private String enderecoNumero;
	private String enderecoComplemento;
	private String enderecoBairro;
	private String enderecoCep;
	private String enderecoCidade;
	private String enderecoUf;
	private Date dataInclusao;
	
	
	private List<ProdutoCouchDTO> produtos;
	
	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}
	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}
	public String getJornaleiro() {
		return jornaleiro;
	}
	public void setJornaleiro(String jornaleiro) {
		this.jornaleiro = jornaleiro;
	}
	public String getCodigoCota() {
		return codigoCota;
	}
	public void setCodigoCota(String codigoCota) {
		this.codigoCota = codigoCota;
	}
	public String getCodigoPontoVenda() {
		return codigoPontoVenda;
	}
	public void setCodigoPontoVenda(String codigoPontoVenda) {
		this.codigoPontoVenda = codigoPontoVenda;
	}
	public Date getDataMovimento() {
		return dataMovimento;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}
	public Date getDataInclusao() {
		return dataInclusao;
	}
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	public List<ProdutoCouchDTO> getProdutos() {
		return produtos;
	}
	public void setProdutos(List<ProdutoCouchDTO> produtos) {
		this.produtos = produtos;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTipoLogradouro() {
		return tipoLogradouro;
	}
	public void setTipoLogradouro(String tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}
	
	public String getEnderecoLogradouro() {
		return enderecoLogradouro;
	}
	public void setEnderecoLogradouro(String enderecoLogradouro) {
		this.enderecoLogradouro = enderecoLogradouro;
	}
	public String getEnderecoNumero() {
		return enderecoNumero;
	}
	public void setEnderecoNumero(String enderecoNumero) {
		this.enderecoNumero = enderecoNumero;
	}
	
	public String getEnderecoComplemento() {
		return enderecoComplemento;
	}
	public void setEnderecoComplemento(String enderecoComplemento) {
		this.enderecoComplemento = enderecoComplemento;
	}
	public String getEnderecoBairro() {
		return enderecoBairro;
	}
	public void setEnderecoBairro(String enderecoBairro) {
		this.enderecoBairro = enderecoBairro;
	}
	public String getEnderecoCep() {
		return enderecoCep;
	}
	public void setEnderecoCep(String enderecoCep) {
		this.enderecoCep = enderecoCep;
	}
	public String getEnderecoCidade() {
		return enderecoCidade;
	}
	public void setEnderecoCidade(String enderecoCidade) {
		this.enderecoCidade = enderecoCidade;
	}
	public String getEnderecoUf() {
		return enderecoUf;
	}
	public void setEnderecoUf(String enderecoUf) {
		this.enderecoUf = enderecoUf;
	}

}
