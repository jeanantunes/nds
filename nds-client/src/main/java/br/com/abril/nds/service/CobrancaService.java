package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.dto.PagamentoDividasDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.financeiro.Cobranca;

public interface CobrancaService {
    
	/**
	 * Obtém o valor do juros, respeitando a ordem dos parâmetros informados,
	 * ou seja, se existir taxa de juros no banco informado, utiliza essa taxa.
	 * Senão utilizada da cota caso exista ou do distribuídor.
	 * 
	 * @param banco - banco
	 * @param cota - cota
	 * @param distribuidor - distribuidor
	 * @param valor - valor a ser calculado
	 * @param dataVencimento - data de vencimento
	 * @param dataCalculoJuros - data a ser calculado o juros
	 * 
	 * @return valor calculado com o juros
	 */	
	public BigDecimal calcularJuros(Banco banco, Cota cota, Distribuidor distribuidor,
									BigDecimal valor, Date dataVencimento, Date dataCalculoJuros);
	
	/**
	 * Obtém o valor da multa, respeitando a ordem dos parâmetros informados,
	 * ou seja, se existir taxa ou valor de multa no banco informado, utiliza essa informação.
	 * Senão utilizada da cota caso exista ou do distribuidor.
	 * 
	 * @param banco - banco
	 * @param cota - cota
	 * @param distribuidor - distribuidor
	 * @param valor - valor a ser calculado
	 * 
	 * @return valor calculado com a multa
	 */
	public BigDecimal calcularMulta(Banco banco, Cota cota,
									Distribuidor distribuidor, BigDecimal valor);

	
	/**
	 * Método responsável por obter cobranças por numero da cota e vencimento
	 * @param filtro
	 * @return Lista de cobrancas encontrados
	 */
	List<Cobranca> obterCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro);

	
	/**
	 * Método responsável por obter quantidade cobranças por numero da cota e vencimento
	 * @param filtro
	 * @return int
	 */
	int obterQuantidadeCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro);

	
	/**
	 * Método responsável por obter lista de dados de cobranças por numero da cota e vencimento
	 * @param filtro
	 * @return Lista de value objects com dados de cobrancas encontradas
	 */
	List<CobrancaVO> obterDadosCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro);
	
	
	/**
	 * Método responsável por obter dados de cobrança por código
	 * @param idCobranca
	 * @return value object com dados da cobranca encontrada
	 */
	CobrancaVO obterDadosCobranca(Long idCobranca);
	
	
	/**
	 * Método responsável por obter dados somados de cobranças por códigos
	 * @param List<Long> idCobrancas
	 * @return Data Transfer object com dados somados das cobrancas encontradas e calculadas.
	 */
	PagamentoDividasDTO obterDadosCobrancas(List<Long> idCobrancas);
}
