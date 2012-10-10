package br.com.abril.nds.model.cadastro;

/**
 * Representa o domínio de moedas, utilizado
 * principalmente para a geração de boletos
 * 
 * @author francisco.garcia
 *
 */
public enum Moeda {
	
	REAL(9L,"Real");
	
	private Long codigo;
	private String descricao;
	
	private Moeda(Long codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	/**
	 * @return Descricao da moeda
	 */
	@Override
	public String toString() {
		return this.descricao;
	}
	
	/**
	 * @return Codigo da moeda
	 */
	public Long getCodigo() {
		return this.codigo;
	}

}
