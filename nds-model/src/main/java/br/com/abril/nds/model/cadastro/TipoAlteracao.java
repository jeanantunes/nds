package br.com.abril.nds.model.cadastro;

public enum TipoAlteracao {
	
	INCLUSAO("Inclusão"),
	EXCLUSAO("Exclusão");
	
	private String tipoAlteracao;
	
	private TipoAlteracao(String tipoAlteracao) {
		this.tipoAlteracao = tipoAlteracao;
	}

	public String getTipoAlteracao() {
		return tipoAlteracao;
	}

	public void setTipoAlteracao(String tipoAlteracao) {
		this.tipoAlteracao = tipoAlteracao;
	}

}
