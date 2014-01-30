package br.com.abril.nds.util.export;

import java.math.BigDecimal;

/**
 * Classe auxiliar responsável pela manipulação dos dados que serão contabilizados para 
 * gerar rodapé de arquivos de exportação.
 * 
 * @author Discover Technology
 *
 */
public class FooterHelper {
	
	private final String footerName;
	
	private BigDecimal value;
	
	/**
	 * Cria uma instância com o identificador da coluna a ser contabilizada. 
	 * 
	 * @param footerName - Nome da coluna (field) ou método que será o identificador dos campos.
	 */
	public FooterHelper(String footerName) {
		
		this.value = BigDecimal.ZERO;
		
		this.footerName = footerName;
	}
	
	/**
	 * Realiza a contagem de dados.
	 * 
	 * @return FooterHelper
	 */
	public FooterHelper count(String val) {

		if (val != null && !val.isEmpty()) {
		
			this.value = this.value.add(BigDecimal.ONE);
		}
		
		return this;
	}
	
	/**
	 * Realiza a média de dados.
	 * 
	 * @param val - Valor atual da coluna.
	 * 
	 * @param offset - Posição atual da coluna em relação às linhas do grid de exportação.
	 * 
	 * @param limit - Quantidade total das linhas do grid. 
	 * 
	 * @return FooterHelper
	 */
	public FooterHelper avg(String val, int offset, int limit) {
		
		BigDecimal newValue = this.validarOperacao(val);
		
		this.value = this.value.add(newValue);
		
		if (offset == limit) {

			this.value = this.value.divide(new BigDecimal(limit));
		}

		return this;
	}
	
	/**
	 * Realiza a soma de dados.
	 * 
	 * @param val - Valor atual a ser sumarizado.
	 * 
	 * @return FooterHelper
	 */
	public FooterHelper sum(String val) {
		
		BigDecimal newValue = this.validarOperacao(val);
	
		this.value = this.value.add(newValue);
		
		return this;
	}

	/**
	 * Valida os dados da coluna.
	 * 
	 * @param val - Valor atual.
	 * 
	 * @return - Valor devidamente validado.
	 */
	private BigDecimal validarOperacao(String val) {
		
		if (val == null || val.isEmpty()) {

			return BigDecimal.ZERO;
		}
		
		try {
			
			val = val.replace(".", "").replace(",", ".");

			return new BigDecimal(val);
		
		} catch (NumberFormatException e) {
			
			throw new IllegalArgumentException(this.footerName + " não pode ser convertido em número.");
		}
	}

	/**
	 * @return the footerName
	 */
	public String getFooterName() {
		return footerName;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return this.value.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((footerName == null) ? 0 : footerName.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FooterHelper other = (FooterHelper) obj;
		if (footerName == null) {
			if (other.footerName != null)
				return false;
		} else if (!footerName.equals(other.footerName))
			return false;
		return true;
	}

	
}
