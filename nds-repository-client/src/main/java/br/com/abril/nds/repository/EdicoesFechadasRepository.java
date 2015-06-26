package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.MovimentoEstoque;


/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * @author InfoA2
 */
public interface EdicoesFechadasRepository extends Repository<MovimentoEstoque, Long> {

	/**
	 * Retorna a lista de edições fechadas filtrada pelo código do fornecedor em um período de datas
	 * @param dataDe
	 * @param dateAte
	 * @param idFornecedor
	 * @param sortorder
	 * @param sortname
	 * @param firstResult
	 * @param maxResults
	 * @param gruposExcluidos
	 * @param statusAprovacao
	 * @return List<RegistroEdicoesFechadasVO>
	 */
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe, 
			                                                             Date dateAte, 
			                                                             Long idFornecedor, 
			                                                             String sortorder, 
			                                                             String sortname, 
			                                                             Integer firstResult, 
			                                                             Integer maxResults,
			    				                                         List<String> gruposExcluidos,
			    				                                         StatusAprovacao statusAprovacao);

	

	/**
	 * Retorna o resultado total do saldo de edições fechadas filtrada pelo código do fornecedor em um período de datas
	 * @param dataDe
	 * @param dateAte
	 * @param idFornecedor
	 * @param gruposExcluidos
	 * @param statusAprovacao
	 * @return BigInteger
	 */
	public BigInteger obterResultadoTotalEdicoesFechadas(Date dataDe, 
			                                             Date dateAte, 
			                                             Long idFornecedor,
				                                         List<String> gruposExcluidos,
				                                         StatusAprovacao statusAprovacao);


	/**
	 * Retorna quantidade de edições fechadas filtrada pelo código do fornecedor em um período de datas
	 * @param parse
	 * @param parse2
	 * @param idFornecedor
	 * @param gruposExcluidos
	 * @param statusAprovacao
	 * @return Long
	 */
	public Long countResultadoEdicoesFechadas(Date parse, 
			                                  Date parse2,
			                                  Long idFornecedor,
		                                      List<String> gruposExcluidos,
		                                      StatusAprovacao statusAprovacao);

}
