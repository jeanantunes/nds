package br.com.abril.nds.service.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.model.fiscal.nota.COFINS;
import br.com.abril.nds.model.fiscal.nota.ICMS;
import br.com.abril.nds.model.fiscal.nota.ICMSST;
import br.com.abril.nds.model.fiscal.nota.IPI;
import br.com.abril.nds.model.fiscal.nota.PIS;

/**
 * Implementacao padrao para calculo de impostos.
 */
public class NFeCalculatorImpl {

	private static final Logger logger = LoggerFactory.getLogger(NFeCalculatorImpl.class);
	
	public static BigDecimal calculate(ICMS icms) {
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

	public static BigDecimal calculate(ICMSST icms) {
		
		BigDecimal taxValue = icms.getValor();
		validate(icms.getAliquota(), icms.getValorBaseCalculo());

		if (taxValue==null) {
			taxValue = internalCalculate(icms.getValorBaseCalculo(), icms.getAliquota());
			logger.debug("Valor calculado do ICMSST: {}.", taxValue);
			return taxValue;
		}
		logger.warn("Valor nao recalculado do ICMSST: {}.", taxValue);
		
		return taxValue;
	}

	public static BigDecimal calculate(IPI ipi) {
		BigDecimal taxValue = BigDecimal.ZERO;
		validate(ipi.getAliquota(), ipi.getValorBaseCalculo());

		if (taxValue==null) {
			taxValue = internalCalculate(ipi.getValorBaseCalculo(), ipi.getAliquota());
			logger.debug("Valor calculado do IPI: {}.", taxValue);
			return taxValue;
		}
		logger.warn("Valor nao recalculado do IPI: {}.", taxValue);
		
		return taxValue;
	}

	public static BigDecimal calculate(PIS pis) {
		BigDecimal taxValue = BigDecimal.ZERO;
		validate(pis.getPercentualAliquota(), pis.getValorBaseCalculo());

		if (taxValue==null || taxValue.intValue() == 0) {
			taxValue = internalCalculate(pis.getValorBaseCalculo(), pis.getPercentualAliquota());
			logger.debug("Valor calculado do PIS: {}.", taxValue);
			return taxValue;
		}
		logger.warn("Valor nao recalculado do PIS: {}.", taxValue);
		
		return taxValue;
	}

	public static BigDecimal calculate(COFINS cofins) {
		BigDecimal taxValue = BigDecimal.ZERO;
		validate(cofins.getPercentualAliquota(), cofins.getValorBaseCalculo());

		if (taxValue==null || taxValue.intValue() == 0) {
			taxValue = internalCalculate(cofins.getValorBaseCalculo(), cofins.getPercentualAliquota());
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
	protected static void validate(BigDecimal aliquota, BigDecimal valorBaseCalculo) {
		if (aliquota==null) {
			throw new IllegalArgumentException("Valor da aliquota não pode ser nulo");
		}
		if (valorBaseCalculo==null) {
			throw new IllegalArgumentException("Valor da base de cálculo não pode ser nulo");
		}
	}
	
	/**
	 * Calcula o imposto como um percentual da base.
	 * 
	 * @param valorBaseCalculo valor da base de calculo
	 * @param aliquota aliquota
	 */
	protected static BigDecimal internalCalculate(BigDecimal valorBaseCalculo, BigDecimal aliquota) {
		return valorBaseCalculo.multiply(aliquota).divide(new BigDecimal(100));
	}
}
