package br.com.abril.nds.model.cadastro;

public enum PeriodicidadeProduto {
	
	SEMANAL(1),
	QUINZENAL(2), 
	MENSAL(3), 
	BIMESTRAL(4),
	TRIMESTRAL(5), 
	SEMESTRAL(6), 
	ANUAL(7);

	private Integer ordem;
	
	private PeriodicidadeProduto(Integer ordem) {
		this.ordem = ordem;
	}

	/**
	 * @return the ordem
	 */
	public Integer getOrdem() {
		return ordem;
	}
	
}
