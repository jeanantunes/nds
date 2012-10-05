package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO;
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
	 * Obtém a quantidade de boletos previstos.
	 * 
	 * @param data - data
	 * 
	 * @return quantidade de boletos previstos
	 */
	Long obterQuantidadeBoletosPrevistos(Date data);
	
	/**
	 * Obtém a quantidade de boletos lidos.
	 * 
	 * @param data - data
	 * 
	 * @return quantidade de boletos lidos
	 */
	Long obterQuantidadeBoletosLidos(Date data);
	
	/**
	 * Obtém a quantidade de boletos baixados.
	 * 
	 * @param data - data
	 * 
	 * @return quantidade de boletos baixados
	 */
	Long obterQuantidadeBoletosBaixados(Date data);
	
	/**
	 * Obtém a quantidade de boletos rejeitados.
	 * 
	 * @param data - data
	 * 
	 * @return quantidade de boletos rejeitados
	 */
	Long obterQuantidadeBoletosRejeitados(Date data);
	
	/**
	 * Obtém a quantidade de boletos baixados com divergência.
	 * 
	 * @param data - data
	 * 
	 * @return quantidade de boletos baixados com divergência
	 */
	Long obterQuantidadeBoletosBaixadosComDivergencia(Date data);
	
	/**
	 * Obtém a quantidade de boletos inadimplentes.
	 * 
	 * @param dataVencimento - data de vencimento
	 * 
	 * @return quantidade de boletos inadimplentes
	 */
	Long obterQuantidadeBoletosInadimplentes(Date dataVencimento);
	
	/**
	 * Obtém o valor total bancário.
	 * 
	 * @param data - data
	 * 
	 * @return valor total bancário
	 */
	BigDecimal obterValorTotalBancario(Date data);

	/**
	 * Obtém os boletos que foram baixados com divergência por data e/ou valor e determinada data.
	 * 
	 * @param filtro - FiltroDetalheBaixaBoletoDTO - filtro indicando data para consulta e dados para paginação.
	 * 
	 * @return List<DetalheBaixaBoletoDTO> - Boletos baixados com divergência de data e/ou valor.
	 */
	List<DetalheBaixaBoletoDTO> obterBoletosBaixadosComDivergencia(FiltroDetalheBaixaBoletoDTO filtro);
	
	/**
	 * Obtém os boletos que foram rejeitados na baixa em determinada data.
	 * 
	 * @param filtro - FiltroDetalheBaixaBoletoDTO - filtro indicando data para consulta e dados para paginação.
	 * 
	 * @return List<DetalheBaixaBoletoDTO> - Boletos rejeitados.
	 */
	List<DetalheBaixaBoletoDTO> obterBoletosRejeitados(FiltroDetalheBaixaBoletoDTO filtro);
}
