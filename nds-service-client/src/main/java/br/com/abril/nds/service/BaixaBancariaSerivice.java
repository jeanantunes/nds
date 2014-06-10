package br.com.abril.nds.service;

import java.util.Date;

public interface BaixaBancariaSerivice {

	/**
	 * Verifica existencia de baixa bancária em determinada data
	 * 
	 * @param data - Data
	 * @return existencia ou não da baixa
	 */
	boolean verificarBaixaBancariaNaData(Date data);
}
