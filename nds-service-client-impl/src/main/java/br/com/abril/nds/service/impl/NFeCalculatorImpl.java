package br.com.abril.nds.service.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.model.fiscal.nota.COFINS;
import br.com.abril.nds.model.fiscal.nota.ICMS;
import br.com.abril.nds.model.fiscal.nota.ICMSST;
import br.com.abril.nds.model.fiscal.nota.IPI;
import br.com.abril.nds.model.fiscal.nota.PIS;
import br.com.abril.nds.service.NFeCalculator;

/**
 * Implementa��o padrao para calculo de impostos.
 */
public class NFeCalculatorImpl implements NFeCalculator {

	private static final Logger logger = LoggerFactory.getLogger(NFeCalculatorImpl.class);
	
	public BigDecimal calculate(ICMS icms) {
		BigDecimal taxValue = icms.getValor();
		validate(icms.getAliquota(), icms.getValorBaseCalculo());

		if (taxValue==null) {
			taxValue = internalCalculate(icms.getValorBaseCalculo(), icms.getAliquota());
			logger.debug("Valor calculado do ICMS: {}.", taxValue);
			return taxValue;
		}
		logger.warn("Valor nao recalculado do ICMS: {}.", taxValue);
		
		return taxValue;
	}

	public BigDecimal calculate(ICMSST icms) {
		BigDecimal taxValue = calculate(icms);
		
		
		return taxValue;
	}

	public BigDecimal calculate(IPI ipi) {
		BigDecimal taxValue = ipi.getValor();
		validate(ipi.getAliquota(), ipi.getValorBaseCalculo());

		if (taxValue==null) {
			taxValue = internalCalculate(ipi.getValorBaseCalculo(), ipi.getValor());
			logger.debug("Valor calculado do IPI: {}.", taxValue);
			return taxValue;
		}
		logger.warn("Valor nao recalculado do IPI: {}.", taxValue);
		
		return taxValue;
	}

	public BigDecimal calculate(PIS pis) {
		BigDecimal taxValue = pis.getValor();
		validate(pis.getValorAliquota(), pis.getValorBaseCalculo());

		if (taxValue==null) {
			taxValue = internalCalculate(pis.getValorBaseCalculo(), pis.getValor());
			logger.debug("Valor calculado do PIS: {}.", taxValue);
			return taxValue;
		}
		logger.warn("Valor nao recalculado do PIS: {}.", taxValue);
		
		return taxValue;
	}

	public BigDecimal calculate(COFINS cofins) {
		BigDecimal taxValue = cofins.getValor();
		validate(cofins.getValorAliquota(), cofins.getValorBaseCalculo());

		if (taxValue==null) {
			taxValue = internalCalculate(cofins.getValorBaseCalculo(), cofins.getValor());
			logger.debug("Valor calculado do CONFINS: {}.", taxValue);
			return taxValue;
		}
		logger.warn("Valor nao recalculado do CONFINS: {}.", taxValue);
		
		return taxValue;
	}
	
	/**
	 * Validar se algum dos parametros for nulo.
	 * 
	 * @param aliquota
	 * @param valorBaseCalculo
	 */
	protected void validate(BigDecimal aliquota, BigDecimal valorBaseCalculo) {
		if (aliquota==null) {
			throw new IllegalArgumentException("Valor da aliquota n�o pode ser nulo");
		}
		if (valorBaseCalculo==null) {
			throw new IllegalArgumentException("Valor da base de c�lculo n�o pode ser nulo");
		}
	}
	
	/**
	 * Calcula o imposto como um percentual da base.
	 * 
	 * @param valorBaseCalculo valor da base de calculo
	 * @param aliquota aliquota
	 */
	protected BigDecimal internalCalculate(BigDecimal valorBaseCalculo, BigDecimal aliquota) {
		return valorBaseCalculo.multiply(aliquota).divide(new BigDecimal(100));
	}
}
