package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;

public interface CobrancaService {
    
	public BigDecimal calcularJuros(Distribuidor distribuidor, Cota cota,
									BigDecimal valor, Date dataVencimento, Date dataCalculoJuros);
	
	public BigDecimal calcularMulta(Distribuidor distribuidor, Cota cota, BigDecimal valor);
	
}
