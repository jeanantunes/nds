package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ComposicaoCobrancaSlipDTO;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;

@Repository
public class ConferenciaEncalheRepositoryImpl extends
		AbstractRepositoryModel<ConferenciaEncalhe, Long> implements
		ConferenciaEncalheRepository {

	public ConferenciaEncalheRepositoryImpl() {
		super(ConferenciaEncalhe.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConferenciaEncalheRepository#obterDadosSlipConferenciaEncalhe(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicaoSlipDTO> obterDadosSlipConferenciaEncalhe(Long idControleConferenciaEncalheCota, Long idDistribuidor) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		
		hql.append(" conferencia.chamadaEncalheCota.chamadaEncalhe.id as idChamadaEncalhe, ");
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.produto.nome as nomeProduto,	");
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.numeroEdicao as numeroEdicao,	");
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.id as idProdutoEdicao,			");
		
		hql.append(" (conferencia.movimentoEstoqueCota.produtoEdicao.precoVenda -	");
		hql.append(" ( conferencia.movimentoEstoqueCota.produtoEdicao.precoVenda * ("+ this.obterHQLDesconto("conferencia.movimentoEstoqueCota.cota.id", "conferencia.movimentoEstoqueCota.produtoEdicao.id", "fornecedor.id") + ") / 100 )) as precoVenda,	");
		
		hql.append(" conferencia.movimentoEstoqueCota.qtde as encalhe, ");
		
		hql.append(" ((conferencia.movimentoEstoqueCota.produtoEdicao.precoVenda -  			");
		hql.append(" ( conferencia.movimentoEstoqueCota.produtoEdicao.precoVenda * ("+ this.obterHQLDesconto("conferencia.movimentoEstoqueCota.cota.id", "conferencia.movimentoEstoqueCota.produtoEdicao.id", "fornecedor.id") +") / 100 ))  ");
		hql.append(" * conferencia.movimentoEstoqueCota.qtde) as valorTotal, ");
		hql.append(" conferencia.controleConferenciaEncalheCota.dataOperacao as dataOperacao,");
		hql.append(" conferencia.chamadaEncalheCota.chamadaEncalhe.dataRecolhimento as dataRecolhimento");
		
		
		hql.append(" from ConferenciaEncalhe conferencia	");
		
		hql.append(" join conferencia.movimentoEstoqueCota.produtoEdicao.produto.fornecedores fornecedor ");

		hql.append(" where	");
		
		hql.append(" conferencia.controleConferenciaEncalheCota.id = :idControleConferenciaEncalheCota ");
		
		Query query =  this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoSlipDTO.class));
		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		
		return query.list();
	
	}
	
	/**
	 * Obtém a chamada de encalhe 'fechada' relacionada à um movimento financeiro
	 * @param idMovimentoDevolucao
	 * @return ChamadaEncalheCota
	 */
	@Override
	public ChamadaEncalheCota obterChamadaEncalheDevolucao(Long idMovimentoDevolucao){
		
		StringBuilder hql = new StringBuilder("");
		
		hql.append(" select cec ");

		hql.append(" from ConferenciaEncalhe ce, MovimentoFinanceiroCota mfc ");
		
		hql.append(" join ce.movimentoEstoqueCota mec ");
		
		hql.append(" join mfc.movimentos mov ");
		
		hql.append(" join ce.chamadaEncalheCota cec ");
		
		hql.append(" where mec.id = mov.id ");
		
		hql.append(" and cec.fechado = true ");
		
		hql.append(" and mfc.id = :idMov ");
		
		hql.append(" order by cec.id ");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("idMov", idMovimentoDevolucao);
		
		query.setMaxResults(1);
		
		return (ChamadaEncalheCota) query.uniqueResult();
	}
	
	/**
     * Obtém composição de cobrança da cota na data de operação para a exibição no Slip
     * @param numeroCota
     * @param dataOperacao
     * @param tiposMovimentoFinanceiroIgnorados
     * @return List<ComposicaoCobrancaSlipDTO>
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<ComposicaoCobrancaSlipDTO> obterComposicaoCobrancaSlip(Integer numeroCota, Date dataOperacao, List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados){
		
		StringBuilder hql = new StringBuilder(" select ");

		hql.append(" mfc.id as idMovimentoFinanceiro, ");
		
		hql.append(" tipoMovimento.descricao as descricao, ");
		
		hql.append(" (case when tipoMovimento.operacaoFinaceira = 'CREDITO' then 'C' else 'D' end) as operacaoFinanceira, ");
		
		hql.append(" coalesce(mfc.valor,0) as valor ");
		
		hql.append(" from MovimentoFinanceiroCota mfc ");
		
		hql.append(" join mfc.tipoMovimento tipoMovimento ");
        
		hql.append(" where ");
		
		hql.append(" mfc.data = :dataOperacao ");
		
		hql.append(" and mfc.status = :statusAprovado ");
		
		hql.append(" and mfc.cota.numeroCota = :numeroCota ");
		
		if(tiposMovimentoFinanceiroIgnorados!=null && !tiposMovimentoFinanceiroIgnorados.isEmpty()) {
			hql.append(" and mfc.tipoMovimento not in (:tiposMovimentoFinanceiroIgnorados) ");
		}
		
		hql.append(" and mfc.id not in ");
		
		hql.append(" (   ");
		
		hql.append("     select distinct(movimentos.id) ");

		hql.append("     from ConsolidadoFinanceiroCota c join c.movimentos movimentos ");
		
		hql.append("     where ");
		
		hql.append("     c.cota.numeroCota = :numeroCota  ");
		
		hql.append(" ) ");
		
		hql.append(" order by mfc.data ");
		
		Query query = this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ComposicaoCobrancaSlipDTO.class));
		
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("dataOperacao", dataOperacao);
		
		if(tiposMovimentoFinanceiroIgnorados!=null && !tiposMovimentoFinanceiroIgnorados.isEmpty()) {
			query.setParameterList("tiposMovimentoFinanceiroIgnorados", tiposMovimentoFinanceiroIgnorados);
		}
		
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConferenciaEncalheRepository#obterListaConferenciaEncalheDTOContingencia(java.lang.Long, java.lang.Integer, java.util.Date, java.util.Date, boolean, boolean, java.util.Set)
	 */
	@SuppressWarnings("unchecked")
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTOContingencia(
			Long idDistribuidor,
			Integer numeroCota,
			Date dataInicial,
			Date dataFinal,
			boolean indFechado,
			boolean indPostergado,
			Set<Long> listaIdProdutoEdicao) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select	");
		
		hql.append(" PROD_EDICAO.ID as idProdutoEdicao,	");
		hql.append(" PROD_EDICAO.CODIGO_DE_BARRAS as codigoDeBarras, ");
		
		hql.append(" ( ");
		hql.append( subSqlQuerySequenciaMatriz() );
		hql.append(" ) AS codigoSM, ");
		
		hql.append(" 0 AS qtdExemplar, 		");
		hql.append(" 0 AS qtdInformada, 	");
		hql.append(" 0 AS valorTotal, 		");
		hql.append(" PROD_EDICAO.PRECO_VENDA AS precoCapaInformado,          ");
		
		hql.append(" PROD_EDICAO.PARCIAL AS parcial,						 ");
		
		hql.append(" CH_ENCALHE.DATA_RECOLHIMENTO AS dataRecolhimento,  	 ");
		hql.append(" CH_ENCALHE.TIPO_CHAMADA_ENCALHE AS tipoChamadaEncalhe,	 ");
		hql.append(" PROD.CODIGO AS codigo,");
		hql.append(" PROD.NOME AS nomeProduto,                               ");
		
		hql.append(" PROD_EDICAO.NUMERO_EDICAO AS numeroEdicao,              ");
		hql.append(" PROD_EDICAO.PRECO_VENDA AS precoCapa,                   ");		
		
		hql.append("        ( PROD_EDICAO.PRECO_VENDA *  (  ");
		hql.append("          coalesce ((SELECT DESCONTO FROM VIEW_DESCONTO WHERE COTA_ID = COTA.ID AND PRODUTO_ID = PROD_EDICAO.ID AND FORNECEDOR_ID = (SELECT F.ID FROM FORNECEDOR F, PRODUTO_FORNECEDOR PF WHERE F.ID = PF.FORNECEDORES_ID AND PF.PRODUTO_ID = PROD.ID)),0)"	);		
		hql.append("         ) / 100 ) AS desconto         	");
		
		hql.append("    FROM    ");
		
		hql.append("    CHAMADA_ENCALHE_COTA AS CH_ENCALHE_COTA 	");
		
		hql.append("	inner join COTA AS COTA ON ");
		hql.append("	(COTA.ID = CH_ENCALHE_COTA.COTA_ID)	");
		
		hql.append("	inner join CHAMADA_ENCALHE AS CH_ENCALHE ON ");
		hql.append("	(CH_ENCALHE_COTA.CHAMADA_ENCALHE_ID = CH_ENCALHE.ID)	");
		
		hql.append("	inner join PRODUTO_EDICAO as PROD_EDICAO ON ");
		hql.append("	(PROD_EDICAO.ID = CH_ENCALHE.PRODUTO_EDICAO_ID)	");

		hql.append("	inner join PRODUTO as PROD ON ");
		hql.append("	(PROD_EDICAO.PRODUTO_ID = PROD.ID)	");

		hql.append("	WHERE   ");
		
		hql.append("	COTA.NUMERO_COTA = :numeroCota AND ");
		hql.append("	(CH_ENCALHE.DATA_RECOLHIMENTO between :dataInicial AND :dataFinal) AND ");
		hql.append("	CH_ENCALHE_COTA.FECHADO = :indFechado AND	");
		hql.append("	CH_ENCALHE_COTA.POSTERGADO = :indPostergado 	");
		
		if(listaIdProdutoEdicao!=null && !listaIdProdutoEdicao.isEmpty()) {
			
			hql.append(" AND CH_ENCALHE.PRODUTO_EDICAO_ID NOT IN (:listaIdProdutoEdicao) ");
			
		}
		
		hql.append("  	ORDER BY codigoSM ");
		
		Query query =  this.getSession().createSQLQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ConferenciaEncalheDTO.class));
		
		((SQLQuery)query).addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("codigoDeBarras");
		((SQLQuery)query).addScalar("codigoSM", StandardBasicTypes.INTEGER);
		
		((SQLQuery)query).addScalar("qtdExemplar", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery)query).addScalar("qtdInformada", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery)query).addScalar("precoCapaInformado", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery)query).addScalar("valorTotal", StandardBasicTypes.BIG_DECIMAL);
		
		((SQLQuery)query).addScalar("dataRecolhimento");
		((SQLQuery)query).addScalar("tipoChamadaEncalhe");
		((SQLQuery)query).addScalar("codigo");
		((SQLQuery)query).addScalar("nomeProduto");
		((SQLQuery)query).addScalar("numeroEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("precoCapa");
		((SQLQuery)query).addScalar("parcial");
		((SQLQuery)query).addScalar("desconto");
		
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);
		query.setParameter("indFechado", indFechado);
		query.setParameter("indPostergado", indPostergado);
		
		if(listaIdProdutoEdicao!=null && !listaIdProdutoEdicao.isEmpty()) {
			
			query.setParameterList("listaIdProdutoEdicao", listaIdProdutoEdicao);
			
		}
		
		return query.list();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConferenciaEncalheRepository#obterListaConferenciaEncalheDTO(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTO(Long idControleConferenciaEncalheCota, Long idDistribuidor) {
	
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT                                             		");
		hql.append(" CONF_ENCALHE.ID AS idConferenciaEncalhe,           		");
		hql.append(" CONF_ENCALHE.QTDE AS qtdExemplar,                  		");
		hql.append(" CONF_ENCALHE.QTDE_INFORMADA AS qtdInformada,       		");
		hql.append(" CONF_ENCALHE.PRECO_CAPA_INFORMADO AS precoCapaInformado,   ");
		hql.append(" CONF_ENCALHE.PRODUTO_EDICAO_ID AS idProdutoEdicao, 		");
		hql.append(" PROD_EDICAO.CODIGO_DE_BARRAS AS codigoDeBarras,    		");

		hql.append(" ( ");
		hql.append(    subSqlQuerySequenciaMatriz() );
		hql.append(" ) AS codigoSM, ");
		
		hql.append(" CH_ENCALHE.DATA_RECOLHIMENTO AS dataRecolhimento,  	 ");
		hql.append(" CH_ENCALHE.TIPO_CHAMADA_ENCALHE AS tipoChamadaEncalhe,	 ");
		hql.append(" PROD.CODIGO AS codigo,                                  ");
		hql.append(" PROD.NOME AS nomeProduto,                               ");
		hql.append(" PROD_EDICAO.NUMERO_EDICAO AS numeroEdicao,              ");
		hql.append(" PROD_EDICAO.PRECO_VENDA AS precoCapa,                   ");
		hql.append(" PROD_EDICAO.PARCIAL AS parcial, 						 ");
		
		hql.append("        ( PROD_EDICAO.PRECO_VENDA *  ( ");
		hql.append("          coalesce ((SELECT DESCONTO FROM VIEW_DESCONTO WHERE COTA_ID = CH_ENCALHE_COTA.COTA_ID AND PRODUTO_ID = PROD_EDICAO.ID AND FORNECEDOR_ID = (SELECT F.ID FROM FORNECEDOR F, PRODUTO_FORNECEDOR PF WHERE F.ID = PF.FORNECEDORES_ID AND PF.PRODUTO_ID = PROD.ID)),0)"	);		
		hql.append("         ) / 100 ) AS desconto,        ");
		
		hql.append("         CONF_ENCALHE.QTDE * ( PROD_EDICAO.PRECO_VENDA - ( PROD_EDICAO.PRECO_VENDA *  ");
		
		hql.append(" ( 							");
		hql.append("          coalesce ((SELECT DESCONTO FROM VIEW_DESCONTO WHERE COTA_ID = CH_ENCALHE_COTA.COTA_ID AND PRODUTO_ID = PROD_EDICAO.ID AND FORNECEDOR_ID = (SELECT F.ID FROM FORNECEDOR F, PRODUTO_FORNECEDOR PF WHERE F.ID = PF.FORNECEDORES_ID AND PF.PRODUTO_ID = PROD.ID)),0)"	);
		hql.append(" ) 							");
		hql.append(" /100)) AS valorTotal,  	");
		
		hql.append("         TO_DAYS(CONF_ENCALHE.DATA)-TO_DAYS(CH_ENCALHE.DATA_RECOLHIMENTO) + 1 AS dia,                ");
		hql.append("         CONF_ENCALHE.OBSERVACAO AS observacao,                                                      ");
		hql.append("         CONF_ENCALHE.JURAMENTADA AS juramentada                                                     ");

		hql.append("     FROM    ");

		hql.append("         CONFERENCIA_ENCALHE CONF_ENCALHE,     ");
		hql.append("         PRODUTO_EDICAO PROD_EDICAO,           ");
		hql.append("         PRODUTO PROD,                         ");
		hql.append("         CHAMADA_ENCALHE_COTA CH_ENCALHE_COTA, ");
		hql.append("         CHAMADA_ENCALHE CH_ENCALHE            ");

		hql.append("     WHERE   ");
		
		hql.append("         CONF_ENCALHE.PRODUTO_EDICAO_ID=PROD_EDICAO.ID           ");
		hql.append("         AND PROD_EDICAO.PRODUTO_ID=PROD.ID                          ");
		hql.append("         AND CONF_ENCALHE.CHAMADA_ENCALHE_COTA_ID=CH_ENCALHE_COTA.ID ");
		hql.append("         AND CH_ENCALHE_COTA.CHAMADA_ENCALHE_ID=CH_ENCALHE.ID        ");
		hql.append("         AND CONF_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = :idControleConferenciaEncalheCota   ");
		
		hql.append("  ORDER BY codigoSM ");
		
		Query query =  this.getSession().createSQLQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ConferenciaEncalheDTO.class));
		
		((SQLQuery)query).addScalar("idConferenciaEncalhe", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("qtdExemplar", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("qtdInformada", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("juramentada");
		((SQLQuery)query).addScalar("precoCapaInformado");
		((SQLQuery)query).addScalar("tipoChamadaEncalhe");
		((SQLQuery)query).addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("codigoDeBarras");
		((SQLQuery)query).addScalar("codigoSM", StandardBasicTypes.INTEGER);
		((SQLQuery)query).addScalar("dataRecolhimento");
		((SQLQuery)query).addScalar("codigo");
		((SQLQuery)query).addScalar("nomeProduto");
		((SQLQuery)query).addScalar("observacao");
		((SQLQuery)query).addScalar("numeroEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("precoCapa");
		((SQLQuery)query).addScalar("parcial");
		((SQLQuery)query).addScalar("desconto");
		((SQLQuery)query).addScalar("valorTotal");
		((SQLQuery)query).addScalar("dia", StandardBasicTypes.INTEGER);

		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		
		return query.list();
		        		
	}
	
	/**
	 * Obtém String de subSQL que retorna valor sequenciaMatriz
	 * para determinado ProdutoEdicao para a dataRecolhimento mais atual.
	 * 
	 * @return String
	 */
	private String subSqlQuerySequenciaMatriz() {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT LANCTO.SEQUENCIA_MATRIZ ");
		sql.append(" FROM LANCAMENTO LANCTO 		");
		sql.append(" WHERE LANCTO.PRODUTO_EDICAO_ID = PROD_EDICAO.ID AND ");
		sql.append(" LANCTO.DATA_LCTO_DISTRIBUIDOR = ");
		
		sql.append(" ( SELECT MAX(LCTO.DATA_LCTO_DISTRIBUIDOR) FROM LANCAMENTO LCTO 	");
		sql.append(" WHERE LCTO.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ) 			");
		
		return sql.toString();
		
	}
	
	/**
	 * Obtém String de subSQL que retorna valor de desconto
	 * de acordo com ProdutoEdicao, Cota e Distribuidor.
	 * 
	 * @return String
	 */
//	private String getSubSqlQueryValorDesconto() {
//		
//		StringBuffer sql = new StringBuffer();
//		
//		sql.append("            SELECT                                                                                   ");
//		sql.append("             CASE                                                                                    ");
//		sql.append("                 WHEN PE.DESCONTO IS NOT NULL THEN PE.DESCONTO                                       ");
//		sql.append("                 ELSE CASE                                                                           ");
//		sql.append("                     WHEN CT.FATOR_DESCONTO IS NOT NULL THEN CT.FATOR_DESCONTO                       ");
//		sql.append("                     ELSE CASE                                                                       ");
//		sql.append("                         WHEN DISTRIB.FATOR_DESCONTO IS NOT NULL THEN DISTRIB.FATOR_DESCONTO         ");
//		sql.append("                         ELSE 0                        ");
//		sql.append("                     END                               ");
//		sql.append("                 END                                   ");
//		sql.append("             END                                       ");
//		sql.append("         FROM                                          ");
//		sql.append("             PRODUTO_EDICAO PE CROSS                   ");
//		sql.append("         JOIN                                          ");
//		sql.append("             COTA CT CROSS                             ");
//		sql.append("         JOIN                                          ");
//		sql.append("             DISTRIBUIDOR DISTRIB                      ");
//		sql.append("         WHERE                                         ");
//		sql.append("             CT.ID=CH_ENCALHE_COTA.COTA_ID             ");
//		sql.append("             AND PE.ID=CH_ENCALHE.PRODUTO_EDICAO_ID  ");
//		sql.append("             AND DISTRIB.ID= :idDistribuidor           ");
//		
//		return sql.toString();
//		
//	}
	

	/**
	 * Obtém o valorTotal de uma operação de conferência de encalhe. Para o calculo do valor
	 * é levado em conta o preco com desconto de acordo com a regra de comissão que verifica 
	 * desconto no níveis de produtoedicao, cota.
	 * 
	 * @param idControleConferenciaEncalhe
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal obterValorTotalEncalheOperacaoConferenciaEncalhe(Long idControleConferenciaEncalhe) {
	
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select sum( (conferencia.precoCapaInformado - (conferencia.precoCapaInformado * ("+ this.obterHQLDesconto("cota.id", "produtoEdicao.id", "fornecedor.id") +") / 100 )) * conferencia.qtdeInformada ) ");
		
		hql.append(" from ConferenciaEncalhe conferencia  ");
		
		hql.append("  join conferencia.controleConferenciaEncalheCota controleConferenciaEncalheCota ");
		
		hql.append("  join controleConferenciaEncalheCota.cota cota ");
		
		hql.append("  join conferencia.produtoEdicao produtoEdicao ");
		
		hql.append("  join produtoEdicao.produto produto ");
		
		hql.append("  join produto.fornecedores fornecedor ");
		
		hql.append(" WHERE conferencia.controleConferenciaEncalheCota.id = :idControleConferenciaEncalhe  ");
		
		Query query =  this.getSession().createQuery(hql.toString());
		
		query.setParameter("idControleConferenciaEncalhe", idControleConferenciaEncalhe);
		
		
		return (BigDecimal) query.uniqueResult();
	}

	
    private String obterHQLDesconto(String cota, String produto, String fornecedor){
    	
		String auxC = " where ";
		StringBuilder hql = new StringBuilder("coalesce ((select view.desconto from ViewDesconto view ");
		

    	 if (cota!=null && !"".equals(cota)){
 		    hql.append(auxC+" view.cotaId = "+cota);
 		    auxC = " and ";
 		 }


		 if (produto!=null && !"".equals(produto)){
	 	     hql.append(auxC+" view.produtoEdicaoId = "+produto);
	 	     auxC = " and ";
	     }


		 if (fornecedor!=null && !"".equals(fornecedor)){
	 	     hql.append(auxC+" view.fornecedorId = "+fornecedor);
	 	     auxC = " and ";
		 }
		 
		 hql.append("),0)");

		return hql.toString();
	}
    
}
