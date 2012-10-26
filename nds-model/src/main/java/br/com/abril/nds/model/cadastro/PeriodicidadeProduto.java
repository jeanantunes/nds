package br.com.abril.nds.model.cadastro;

public enum PeriodicidadeProduto {
	
	DIARIO(1),
	SEMANAL(2),
	QUINZENAL(3), 
	MENSAL(4), 
	BIMESTRAL(5),
	TRIMESTRAL(6), 
	SEMESTRAL(7), 
	ANUAL(8),
	INDEFINIDO(9);

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

	public static PeriodicidadeProduto getByOrdem(Integer ordem) {
		for (PeriodicidadeProduto pp : PeriodicidadeProduto.values()) {
			if (pp.getOrdem().equals(ordem)) {
				return pp;
			}
		}
		return null;
	}

	
	
}
