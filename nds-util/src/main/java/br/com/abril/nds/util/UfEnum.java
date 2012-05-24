package br.com.abril.nds.util;

/**
 * Enum para descrever as UFs Brasileiras.
 *  
 * @author Discover
 */
public enum UfEnum {

	// Região Norte:
	AC("AC", "Acre"),
	AM("AM", "Amazonas"),
	AP("AP", "Amapá"),
	PA("PA", "Pará"),
	RO("RO", "Rondônia"),
	RR("RR", "Roraima"),
	TO("TO", "Tocantins"),
	
	// Região Nordeste:
	AL("AL", "Alagoas"),
	BA("BA", "Bahia"),
	CE("CE", "Ceará"),
	MA("MA", "Maranhão"),
	PB("PB", "Paraíba"),
	PE("PE", "Pernambuco"),
	PI("PI", "Piauí"),
	RN("RN", "Rio Grande do Norte"),
	SE("SE", "Sergipe"),
	
	// Região Centro-Oeste:
	DF("DF", "Distrito Federal"),
	GO("GO", "Goiás"),
	MS("MS", "Mato Grosso do Sul"),
	MT("MT", "Mato Grosso"),
	
	// Região Sudeste:
	ES("ES", "Espírito Santo"),
	MG("MG", "Minas Gerais"),
	RJ("RJ", "Rio de Janeiro"),
	SP("SP", "São Paulo"),

	// Região Sul:
	PR("PR", "Paraná"),
	RS("RS", "Rio Grande do Sul"),
	SC("SC", "Santa Catarina");
	
	
	/** Sigla da UF. */
	private String sigla;

	/** Descrição da UF. */
	private String descricao;

	
	private UfEnum(String sigla, String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}
	

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * @param sigla
	 *            the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
