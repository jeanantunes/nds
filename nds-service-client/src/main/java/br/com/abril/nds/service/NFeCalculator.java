package br.com.abril.nds.service;

import java.math.BigDecimal;

import br.com.abril.nds.model.fiscal.nota.COFINS;
import br.com.abril.nds.model.fiscal.nota.ICMS;
import br.com.abril.nds.model.fiscal.nota.ICMSST;
import br.com.abril.nds.model.fiscal.nota.IPI;
import br.com.abril.nds.model.fiscal.nota.PIS;

/**
 * Interface para  calculo de impostos.
 */
public interface NFeCalculator {
	
	public BigDecimal calculate(ICMS icms);

	public BigDecimal calculate(ICMSST icms);
	
	public BigDecimal calculate(IPI ipi);

	public BigDecimal calculate(PIS pis);

	public BigDecimal calculate(COFINS cofins);

}
