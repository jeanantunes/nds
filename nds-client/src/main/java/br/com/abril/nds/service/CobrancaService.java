package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;

public interface CobrancaService {
    
	public BigDecimal calcularJuros(Distribuidor distribuidor, Cota cota,
									BigDecimal valor, Date dataVencimento, Date dataCalculoJuros);
	
	public BigDecimal calcularMulta(Distribuidor distribuidor, Cota cota, BigDecimal valor);
	
	
	/**
	 * Obtem juros calculado, considerando parametros do banco
	 * @param distribuidor
	 * @param cota
	 * @param valor
	 * @param dataVencimento
	 * @param dataCalculoJuros
	 * @return
	 */
	public BigDecimal calcularJurosBanco(Banco banco, Distribuidor distribuidor, Cota cota,
			BigDecimal valor, Date dataVencimento, Date dataCalculoJuros);
	
    /**
     * Obtem jmulta calculada, considerando parametros do banco
     * @param distribuidor
     * @param cota
     * @param valor
     * @return
     */
    public BigDecimal calcularMultaBanco(Banco banco, Distribuidor distribuidor, Cota cota, BigDecimal valor);

}
