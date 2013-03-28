package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;

public interface FormaCobrancaService {
	
	 /**
	 * Verifica se ja existe a Forma Cobranca Mensal que o usuário deseja cadastrar(Valida por Fornecedor e Concentração)
	 * @param idPoliticaCobranca
	 * @param idDistribuidor
	 * @param idFornecedores
	 * @param diasDoMes
	 * @return Boolean
	 */
	boolean validarFormaCobrancaMensal(Long idPoliticaCobranca, Long idDistribuidor, TipoFormaCobranca tipoFormaCobranca,
			                           List<Long> idFornecedores, List<Integer> diasDoMes);
	
	 /**
	 * Verifica se ja existe a Forma Cobranca Semanal que o usuário deseja cadastrar(Valida por Fornecedor e Concentração)
	 * @param idPoliticaCobranca
	 * @param idDistribuidor
	 * @param tipoFormaCobranca
	 * @param idFornecedores
	 * @param domingo
	 * @param segunda
	 * @param terca
	 * @param quarta
	 * @param quinta
	 * @param sexta
	 * @param sabado
	 * @return Boolean
	 */
	boolean validarFormaCobrancaSemanal(Long idPoliticaCobranca, Long idDistribuidor, TipoFormaCobranca tipoFormaCobranca,
			List<Long> idFornecedores, Boolean domingo, Boolean segunda,
			Boolean terca, Boolean quarta, Boolean quinta, Boolean sexta,
			Boolean sabado);

	/**
	 * Verifica se ja existe a Forma Cobranca Mensal que o usuário deseja cadastrar(Valida por Fornecedor e Concentração)
	 * @param idFormaCobranca
	 * @param idCota
	 * @param idFornecedores
	 * @param tipoFormaCobranca
	 * @param diasDoMes
	 * @return Boolean
	 */
	 boolean validarFormaCobrancaMensal(Long idFormaCobranca, Long idCota, List<Long> idFornecedores, TipoFormaCobranca tipoFormaCobranca, List<Integer> diasDoMes);
		 
	 /**
	 * Verifica se ja existe a Forma Cobranca Semanal que o usuário deseja cadastrar(Valida por Fornecedor e Concentração)
	 * @param idFormaCobranca
	 * @param idCota
	 * @param idFornecedores
	 * @param tipoFormaCobranca
	 * @param domingo
	 * @param segunda
	 * @param terca
	 * @param quarta
	 * @param quinta
	 * @param sexta
	 * @param sabado
	 * @return Boolean
	 */
	 boolean validarFormaCobrancaSemanal(Long idFormaCobranca, Long idCota, List<Long> idFornecedores, TipoFormaCobranca tipoFormaCobranca, Boolean domingo, Boolean segunda, Boolean terca, Boolean quarta, Boolean quinta, Boolean sexta, Boolean sabado);

	/**
	 * Obtem FormaCobranca da Cota com os Parâmetros passados
	 * @param idCota
	 * @param idFornecedor
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
    FormaCobranca obterFormaCobrancaCota(Long idCota, Long idFornecedor, Date data, BigDecimal valor);

    /**
	 * Obtem FormaCobranca do Distribuidor com os Parâmetros passados
	 * @param idFornecedor
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
    FormaCobranca obterFormaCobrancaDistribuidor(Long idFornecedor, Date data, BigDecimal valor);
    
    /**
   	 * Obtem FormaCobranca da Cota com os Parâmetros passados, caso não encontre, busca FormaCobranca do Distribuidor 
   	 * @param idCota
   	 * @param idFornecedor
   	 * @param data
   	 * @param valor
   	 * @return FormaCobranca
   	 */
     FormaCobranca obterFormaCobranca(Long idCota, Long idFornecedor, Date data, BigDecimal valor);
    
    /**
	 * Obtem FormaCobranca da Cota com os Parâmetros passados, caso não encontre, busca FormaCobranca do Distribuidor 
	 * Caso não encontre Forma de Cobranca, retorna excecao com informacoes
	 * @param idCota
	 * @param idFornecedor
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
    FormaCobranca obterFormaCobrancaValidacao(Long idCota, Long idFornecedor, Date data, BigDecimal valor);
    
    /**
     * Obtem FormaCobranca principal da Cota
     * @param idCota
     * @return FormaCobranca
     */
    FormaCobranca obterFormaCobrancaPrincipalCota(Long idCota);
    
    /**
     * Obtem FormaCobranca principal do Distribuidor
     * @return FormaCobranca
     */
    FormaCobranca obterFormaCobrancaPrincipalDistribuidor();
}
