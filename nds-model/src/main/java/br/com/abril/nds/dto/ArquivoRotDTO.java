package br.com.abril.nds.dto;

import br.com.abril.nds.rot.util.ArquivoRotBaseDTO;
import br.com.abril.nds.rot.util.ArquivoRotfield;

public class ArquivoRotDTO extends ArquivoRotBaseDTO {
	
	@ArquivoRotfield(tamanho=6, tipo="char", ordem=1)
	private String roteiroOrdem;
	
	@ArquivoRotfield(tamanho=30, tipo="char", ordem=2)
	private String roteiroDescricao;
	
	@ArquivoRotfield(tamanho=3, tipo="char", ordem=3)
	private String rotaOrdem;
	
	@ArquivoRotfield(tamanho=30, tipo="char", ordem=4)
	private String rota;
	
	@ArquivoRotfield(tamanho=7, tipo="char", ordem=5)
	private String codigoDistribuidor;
	
	@ArquivoRotfield(tamanho=10, tipo="char", ordem=6)
	private String dataOperacao;
	
	@ArquivoRotfield(tamanho=5, tipo="char", ordem=7)
	private String numeroCota;
	
	@ArquivoRotfield(tamanho=35, tipo="char", ordem=8)
	private String nome;
	
	@ArquivoRotfield(tamanho=5, tipo="char", ordem=9)
	private String tipoLogradouro;
	
	@ArquivoRotfield(tamanho=30, tipo="char", ordem=10)
	private String logradouro;
	
	@ArquivoRotfield(tamanho=5, tipo="char", ordem=11)
	private String numero;
	
	@ArquivoRotfield(tamanho=20, tipo="char", ordem=12)
	private String complemento;
	
	@ArquivoRotfield(tamanho=30, tipo="char", ordem=13)
	private String bairro;
	
	@ArquivoRotfield(tamanho=30, tipo="char", ordem=14)
	private String cidade;
	
	@ArquivoRotfield(tamanho=8, tipo="char", ordem=15)
	private String codigoProduto;
	
	@ArquivoRotfield(tamanho=30, tipo="char", ordem=16)
	private String nomeComercial;
	
	@ArquivoRotfield(tamanho=4, tipo="char", ordem=17)
	private String edicao;
	
	@ArquivoRotfield(tamanho=10, tipo="char", ordem=18)
	private String qtdeEfetiva;
	
	@ArquivoRotfield(tamanho=10, tipo="char", ordem=19)
	private String divisaoQtdeEfetivaPacotePadrao;
	
	@ArquivoRotfield(tamanho=10, tipo="char", ordem=20)
	private String modQtdeEfetivaPacotePadrao;
	
	public String getRoteiroOrdem() {
		return roteiroOrdem;
	}
	
	public void setRoteiroOrdem(String roteiroOrdem) {
		this.roteiroOrdem = roteiroOrdem;
	}
	
	public String getRoteiroDescricao() {
		return roteiroDescricao;
	}
	
	public void setRoteiroDescricao(String roteiroDescricao) {
		this.roteiroDescricao = roteiroDescricao;
	}
	
	public String getRotaOrdem() {
		return rotaOrdem;
	}
	
	public void setRotaOrdem(String rotaOrdem) {
		this.rotaOrdem = rotaOrdem;
	}
	
	public String getRota() {
		return rota;
	}
	
	public void setRota(String rota) {
		this.rota = rota;
	}
	
	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}
	
	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}
	
	public String getDataOperacao() {
		return dataOperacao;
	}
	
	public void setDataOperacao(String dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
	
	public String getNumeroCota() {
		return numeroCota;
	}
	
	public void setNumeroCota(String numeroCota) {
		this.numeroCota = numeroCota;
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
	
	public String getLogradouro() {
		return logradouro;
	}
	
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getComplemento() {
		return complemento;
	}
	
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	public String getBairro() {
		return bairro;
	}
	
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	public String getCidade() {
		return cidade;
	}
	
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	public String getCodigoProduto() {
		return codigoProduto;
	}
	
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	public String getNomeComercial() {
		return nomeComercial;
	}

	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}
	
	public String getEdicao() {
		return edicao;
	}
	
	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}
	
	public String getQtdeEfetiva() {
		return qtdeEfetiva;
	}
	
	public void setQtdeEfetiva(String qtdeEfetiva) {
		this.qtdeEfetiva = qtdeEfetiva;
	}
	
	public String getDivisaoQtdeEfetivaPacotePadrao() {
		return divisaoQtdeEfetivaPacotePadrao;
	}
	
	public void setDivisaoQtdeEfetivaPacotePadrao(String divisaoQtdeEfetivaPacotePadrao) {
		this.divisaoQtdeEfetivaPacotePadrao = divisaoQtdeEfetivaPacotePadrao;
	}
	
	public String getModQtdeEfetivaPacotePadrao() {
		return modQtdeEfetivaPacotePadrao;
	}
	
	public void setModQtdeEfetivaPacotePadrao(String modQtdeEfetivaPacotePadrao) {
		this.modQtdeEfetivaPacotePadrao = modQtdeEfetivaPacotePadrao;
	}
}
