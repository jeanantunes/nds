package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.financeiro.Cobranca;

public interface CobrancaRepository extends Repository<Cobranca, Long>{

	/**
	 * Obtém data em que houve a primeira inadimplência com cobrança ainda em aberto
	 * 
	 * @param idCota -  Código da Cota
	 * @return dia
	 */
	Date obterDataAberturaDividas(Long idCota);
	
	/**
	 * Obtém total de cobranças não pagas pela cota
	 * 
	 * @param idCota - Código da Cota
	 * @return valor
	 */
	Double obterDividaAcumuladaCota(Long idCota);
}
