package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Diego Fernandes
 *
 */
public class TipoImpressaoInformeEncalheDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8019735061312349749L;

	public enum Capas {
		NAO, FIM,PAR;
	}

	private boolean dados;
	
	private Capas capas;
	
	private List<String> colunas;

	/**
	 * @return the dados
	 */
	public boolean isDados() {
		return dados;
	}

	/**
	 * @param dados the dados to set
	 */
	public void setDados(boolean dados) {
		this.dados = dados;
	}

	/**
	 * @return the capas
	 */
	public Capas getCapas() {
		return capas;
	}

	/**
	 * @param capas the capas to set
	 */
	public void setCapas(Capas capas) {
		this.capas = capas;
	}

	/**
	 * @return the colunas
	 */
	public List<String> getColunas() {
		return colunas;
	}

	/**
	 * @param colunas the colunas to set
	 */
	public void setColunas(List<String> colunas) {
		this.colunas = colunas;
	}
	
	
	
}
