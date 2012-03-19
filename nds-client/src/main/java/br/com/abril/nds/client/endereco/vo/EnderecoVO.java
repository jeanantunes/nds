package br.com.abril.nds.client.endereco.vo;

import java.io.Serializable;


public class EnderecoVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private TipoLogradouroVO tipoLogradouro;
	private LogradouroVO logradouro;
	private BairroVO bairro;
	private LocalidadeVO localidade;
	private UnidadeFederacaoVO unidadeFederecao;
	private PaisVO pais;
	
	public TipoLogradouroVO getTipoLogradouro() {
		return tipoLogradouro;
	}
	
	public void setTipoLogradouro(TipoLogradouroVO tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}
	
	public LogradouroVO getLogradouro() {
		return logradouro;
	}
	
	public void setLogradouro(LogradouroVO logradouro) {
		this.logradouro = logradouro;
	}
	
	public BairroVO getBairro() {
		return bairro;
	}
	
	public void setBairro(BairroVO bairro) {
		this.bairro = bairro;
	}
	
	public LocalidadeVO getLocalidade() {
		return localidade;
	}
	
	public void setLocalidade(LocalidadeVO localidade) {
		this.localidade = localidade;
	}
	
	public UnidadeFederacaoVO getUnidadeFederecao() {
		return unidadeFederecao;
	}
	
	public void setUnidadeFederecao(UnidadeFederacaoVO unidadeFederecao) {
		this.unidadeFederecao = unidadeFederecao;
	}
	
	public PaisVO getPais() {
		return pais;
	}
	
	public void setPais(PaisVO pais) {
		this.pais = pais;
	}

}
