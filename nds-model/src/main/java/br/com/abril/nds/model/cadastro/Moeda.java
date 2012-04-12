package br.com.abril.nds.model.cadastro;

/**
 * Representa o domínio de moedas, utilizado
 * principalmente para a geração de boletos
 * 
 * @author francisco.garcia
 *
 */
public enum Moeda {
	
	REAL(9);
	
	private Moeda(int codigo) {
		codigo = this.codigo;
	}
	
	private int codigo;
	
	/**
	 * @return código da moeda
	 */
	public int getCodigo() {
		return codigo;
	}

}
