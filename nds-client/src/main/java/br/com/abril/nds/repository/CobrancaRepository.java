package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.financeiro.Cobranca;

public interface CobrancaRepository extends Repository<Cobranca, Long>{

	/**
	 * Obt�m data em que houve a primeira inadimpl�ncia com cobran�a ainda em aberto
	 * 
	 * @param idCota -  C�digo da Cota
	 * @return dia
	 */
	Date obterDataAberturaDividas(Long idCota);
	
	/**
	 * Obt�m total de cobran�as n�o pagas pela cota
	 * 
	 * @param idCota - C�digo da Cota
	 * @return valor
	 */
	Double obterDividaAcumuladaCota(Long idCota);
	
	/**
	 * Obt�r cobra�as n�o pagas da cota
	 * 
	 * @param idCota
	 * @return
	 */
	List<Cobranca> obterCobrancasDaCotaEmAberto(Long idCota);
	
}
