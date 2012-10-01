package br.com.abril.nds.client.singleton;

public class VerificadorProgressoGravacaoDescontoGeralSingleton {
	private static VerificadorProgressoGravacaoDescontoGeralSingleton instance = null;

	private Boolean ativo = false;
	
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
	
}