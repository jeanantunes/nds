package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.model.financeiro.Boleto;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.financeiro.Boleto}  
 * 
 * @author Discover Technology
 *
 */
public interface BoletoRepository extends Repository<Boleto,Long> {

	/**
	 * Obtém uma lista de Boletos para os parametros passados.
	 * 
	 * @param filtro - parametros de busca
	 * 
	 * @return {@link List<Boleto>}
	 */
	List<Boleto> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro);
	
	/**
	 * Obtém a quantidade de Boletos para os parametros passados.
	 * 
	 * @param filtro - parametros de busca
	 * 
	 * @return quantidade
	 */
	long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro);
	
	/**
	 * Obtém um boleto de acordo com o nosso número.
	 * 
	 * @param nossoNumeroCompleto - nosso numero
	 * @param dividaAcumulada - divida é acumulada
	 * 
	 * @return {@link Boleto}
	 */
	Boleto obterPorNossoNumero(String nossoNumero, Boolean dividaAcumulada);
	
	/**
	 * Obtém um boleto de acordo com o nosso número completo.
	 * 
	 * @param nossoNumeroCompleto - nosso numero completo
	 * @param dividaAcumulada - divida é acumulada
	 * 
	 * @return {@link Boleto}
	 */
	public Boleto obterPorNossoNumeroCompleto(String nossoNumeroCompleto, Boolean dividaAcumulada);

	/**
	 * Obtém o resumo da baixa financeira de boletos.
	 * 
	 * @param data - data
	 * 
	 * @return resumo da baixa de boletos
	 */
	ResumoBaixaBoletosDTO obterResumoBaixaFinanceiraBoletos(Date data);
	
}
