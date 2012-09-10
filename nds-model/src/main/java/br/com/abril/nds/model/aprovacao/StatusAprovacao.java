package br.com.abril.nds.model.aprovacao;

public enum StatusAprovacao {
	
	PENDENTE("Pendente", "P"), 
	APROVADO("Aprovado", "A"), 
	REJEITADO("Rejeitado", "R"),
	PERDA_GANHO("Perda/Ganho", "PG");

	private String descricao;
	
	private String descricaoAbreviada;
	
	private StatusAprovacao(String descricao, String descricaoAbreviada) {
		this.descricao = descricao;
		this.descricaoAbreviada = descricaoAbreviada;
	}
	
	public static StatusAprovacao getStatusAprovacao(String status) {
		
		if (status == null || status.isEmpty()) {
			return null;
		}
		
		char statusOp = status.toUpperCase().charAt(0);
		
		switch (statusOp) {
		
		case 'P':
			
			return StatusAprovacao.PENDENTE;
			
		case 'A':
			
			return StatusAprovacao.APROVADO;
			
		case 'R':
			
			return StatusAprovacao.REJEITADO;
		
		default:
			return null;
		}
	}
	
	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the descricaoAbreviada
	 */
	public String getDescricaoAbreviada() {
		return descricaoAbreviada;
	}

	/**
	 * @param descricaoAbreviada the descricaoAbreviada to set
	 */
	public void setDescricaoAbreviada(String descricaoAbreviada) {
		this.descricaoAbreviada = descricaoAbreviada;
	}
	
}
