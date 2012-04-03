package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.financeiro.Cobranca;

public interface CobrancaRepository extends Repository<Cobranca, Long>{

	/**
	 * Obtem data em que houve a primeira inadimplencia com cobrança ainda em aberto
	 * 
	 * @param idCota -  Código da Cota
	 * @return dia
	 */
	Date obterDataAberturaDividas(Long idCota);

	/**
	 * Obter cobraças não pagas da cota
	 * 
	 * @param idCota
	 * @return
	 */
	List<Cobranca> obterCobrancasDaCotaEmAberto(Long idCota);
	
	/**
	 * Obtém a cobrança pelo nosso numero
	 * 
	 * @param nossoNumero
	 * @return Cobranca
	 */
	Cobranca obterCobrancaPorNossoNumero(String nossoNumero);
	
	/**
	 * Incrementa o valor de vias
	 * 
	 * @param nossoNumero
	 */
	void incrementarVia(String... nossoNumero);
	
}
