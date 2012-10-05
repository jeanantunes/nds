package br.com.abril.nds.client.singleton;

import br.com.abril.nds.vo.ValidacaoVO;

public class VerificadorProgressoGravacaoDescontoGeralSingleton {
	private static VerificadorProgressoGravacaoDescontoGeralSingleton instance = null;

	private Boolean ativo = false;
	
	private ValidacaoVO validacao;
	
	private VerificadorProgressoGravacaoDescontoGeralSingleton() {
	}

	public static VerificadorProgressoGravacaoDescontoGeralSingleton getInstance() {
		if (instance == null) {
			instance = new VerificadorProgressoGravacaoDescontoGeralSingleton();
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