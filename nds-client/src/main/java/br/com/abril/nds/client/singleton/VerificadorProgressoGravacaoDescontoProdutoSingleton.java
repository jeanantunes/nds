package br.com.abril.nds.client.singleton;

import br.com.abril.nds.vo.ValidacaoVO;

public class VerificadorProgressoGravacaoDescontoProdutoSingleton {
	private static VerificadorProgressoGravacaoDescontoProdutoSingleton instance = null;

	private Boolean ativo = false;
	
	private ValidacaoVO validacao;
	
	private VerificadorProgressoGravacaoDescontoProdutoSingleton() {
	}

	public static VerificadorProgressoGravacaoDescontoProdutoSingleton getInstance() {
		if (instance == null) {
			instance = new VerificadorProgressoGravacaoDescontoProdutoSingleton();
		}
		return instance;
	}

	public Boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public ValidacaoVO getValidacao() {
		return validacao;
	}

	public void setValidacao(ValidacaoVO validacao) {
		this.validacao = validacao;
	}

}
