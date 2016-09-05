package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.client.vo.FormaCobrancaDefaultVO;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;

public interface FormaCobrancaService {
	
	 /**
	 * Verifica se ja existe a Forma Cobranca Mensal que o usuário deseja cadastrar(Valida por Fornecedor e Concentração)
	 * @param tipoCobranca
	 * @param idPoliticaCobranca
	 * @param idDistribuidor
	 * @param idFornecedores
	 * @param diasDoMes
	 * @return Boolean
	 */
	boolean validarFormaCobrancaMensal(TipoCobranca tipoCobranca, Long idPoliticaCobranca, Long idDistribuidor, TipoFormaCobranca tipoFormaCobranca,
			                           List<Long> idFornecedores, List<Integer> diasDoMes);
	
	 /**
	 * Verifica se ja existe a Forma Cobranca Semanal que o usuário deseja cadastrar(Valida por Fornecedor e Concentração)
	 * @param tipoCobranca
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
	boolean validarFormaCobrancaSemanal(TipoCobranca tipoCobranca, Long idPoliticaCobranca, Long idDistribuidor, TipoFormaCobranca tipoFormaCobranca,
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
	 * @return FormaCobranca
	 */
    FormaCobranca obterFormaCobrancaCota(Long idCota, Long idFornecedor, Date data);

    /**
	 * Obtem FormaCobranca do Distribuidor com os Parâmetros passados
	 * @param idFornecedor
	 * @param data
	 * @return FormaCobranca
	 */
    FormaCobranca obterFormaCobrancaDistribuidor(Long idFornecedor, Date data);
    
    /**
	 * Obtem FormaCobranca da Cota com os Parâmetros passados. 
	 * Caso não encontre e a cota não tenha FormaCobranca cadastrada, busca FormaCobranca do Distribuidor 
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

    List<FormaCobrancaDefaultVO> obterFormaCobrancaDefault();
    
	FormaCobranca obterFormaCobrancaPrincipalCota(Integer numeroCota);

	/**
	 * Obtem mapa com as formas de cobrança ativas
	 * De todos os fornecedores cadastrados
	 * Onde a concentração de pagamento é compatível com a data de operação atual
	 * 
	 * @param dataOperacao
	 * @return Map<Fornecedor,List<FormaCobranca>>
	 */
	Map<Fornecedor, List<FormaCobranca>> obterMapFornecedorFormasCobranca(Date dataOperacao);

    FormaCobranca obterFormaCobrancaPrincipalDistribuidorCompleto();

    /**
	 * Verifica se valor à cobrar é menor ou igual ao valor minimo para cobranca estipulado para a Cota
	 * 
	 * @param valorMinimoCota
	 * @param valorTotalCobrar
	 * @return boolean
	 */
	boolean isValorMinimoAtingido(BigDecimal valorMinimoCota, BigDecimal valorTotalCobrar);

	/**
	 * Verifica se valor à cobrar é menor ou igual ao valor minimo para cobranca estipulado para a Cota
	 * 
	 * @param idCota
	 * @param valorTotalCobrar
	 * @return boolean
	 */
	boolean isValorMinimoAtingido(Long idCota, BigDecimal valorTotalCobrar);
	
	FormaCobranca obterFormaCobrancaBoletoAvulso(TipoCobranca tipoCobranca);
}
