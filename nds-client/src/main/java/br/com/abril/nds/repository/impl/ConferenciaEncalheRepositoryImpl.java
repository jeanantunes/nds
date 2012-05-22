package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;

@Repository
public class ConferenciaEncalheRepositoryImpl extends
		AbstractRepository<ConferenciaEncalhe, Long> implements
		ConferenciaEncalheRepository {

	public ConferenciaEncalheRepositoryImpl() {
		super(ConferenciaEncalhe.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConferenciaEncalheRepository#obterListaConferenciaEncalheDTO(java.lang.Long, java.lang.Long)
	 */
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTO(Long idControleConferenciaEncalheCota, Long idDistribuidor) {
	
		StringBuilder hql = new StringBuilder();
		
		hql.append(" 		SELECT                                             		");
		hql.append(" 		CONF_ENCALHE.ID AS idConferenciaEncalhe,           		");
		hql.append(" 		MOV_EST_COTA.QTDE AS qtdExemplar,                  		");
		hql.append(" 		MOV_EST_COTA.PRODUTO_EDICAO_ID AS idProdutoEdicao, 		");
		hql.append(" 		PROD_EDICAO.CODIGO_DE_BARRAS AS codigoDeBarras,    		");
		hql.append(" 		LANCTO.SEQUENCIA_MATRIZ AS codigoSM,               		");
		hql.append("        CH_ENCALHE.DATA_RECOLHIMENTO AS dataRecolhimento,  		");
		hql.append("		CH_ENCALHE.TIPO_CHAMADA_ENCALHE AS tipoChamadaEncalhe,	");
		hql.append(" 		PROD.CODIGO AS codigo,                                  ");
		hql.append(" 		PROD.NOME AS nomeProduto,                               ");
		hql.append(" 		PROD_EDICAO.NUMERO_EDICAO AS numeroEdicao,              ");
		hql.append(" 		PROD_EDICAO.PRECO_VENDA AS precoCapa,                   ");
		
		hql.append("        ( PROD_EDICAO.PRECO_VENDA *  ( ");
		hql.append(    subSqlQueryValorDesconto()			);		
		hql.append("         ) / 100 ) AS desconto,        ");
		
		hql.append("         MOV_EST_COTA.QTDE * ( PROD_EDICAO.PRECO_VENDA - ( PROD_EDICAO.PRECO_VENDA *  ");
		
		hql.append(" ( 							");
		hql.append(subSqlQueryValorDesconto()	 );
		hql.append(" ) 							");
		hql.append(" /100)) AS valorTotal,  	");
		
		hql.append("         TO_DAYS(MOV_EST_COTA.DATA)-TO_DAYS(CH_ENCALHE.DATA_RECOLHIMENTO) + 1 AS dia,                ");
		hql.append("         CONF_ENCALHE.OBSERVACAO AS observacao,                                                      ");
		hql.append("         CONF_ENCALHE.JURAMENTADA AS juramentada                                                     ");

		hql.append("     FROM    ");
		hql.append("         CONFERENCIA_ENCALHE CONF_ENCALHE,     ");
		hql.append("         LANCAMENTO LANCTO,					   ");
		hql.append("         MOVIMENTO_ESTOQUE_COTA MOV_EST_COTA,  ");
		hql.append("         PRODUTO_EDICAO PROD_EDICAO,           ");
		hql.append("         PRODUTO PROD,                         ");
		hql.append("         CHAMADA_ENCALHE_COTA CH_ENCALHE_COTA, ");
		hql.append("         CHAMADA_ENCALHE CH_ENCALHE            ");

		hql.append("     WHERE   ");
		
		hql.append("		 CONF_ENCALHE.LANCAMENTO_ID = LANCTO.ID						 ");
		hql.append("         AND CONF_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID=MOV_EST_COTA.ID  ");
		hql.append("         AND MOV_EST_COTA.PRODUTO_EDICAO_ID=PROD_EDICAO.ID           ");
		hql.append("         AND PROD_EDICAO.PRODUTO_ID=PROD.ID                          ");
		hql.append("         AND CONF_ENCALHE.CHAMADA_ENCALHE_COTA_ID=CH_ENCALHE_COTA.ID ");
		hql.append("         AND CH_ENCALHE_COTA.CHAMADA_ENCALHE_ID=CH_ENCALHE.ID        ");
		hql.append("         AND CONF_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = :idControleConferenciaEncalheCota   ");
		
		hql.append("  ORDER BY LANCTO.SEQUENCIA_MATRIZ ");
		
		Query query =  this.getSession().createSQLQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ConferenciaEncalheDTO.class));
		
		((SQLQuery)query).addScalar("idConferenciaEncalhe", Hibernate.LONG);
		((SQLQuery)query).addScalar("qtdExemplar");
		((SQLQuery)query).addScalar("idProdutoEdicao", Hibernate.LONG);
		((SQLQuery)query).addScalar("codigoDeBarras");
		((SQLQuery)query).addScalar("codigoSM");
		((SQLQuery)query).addScalar("dataRecolhimento");
		((SQLQuery)query).addScalar("codigo");
		((SQLQuery)query).addScalar("nomeProduto");
		((SQLQuery)query).addScalar("observacao");
		((SQLQuery)query).addScalar("numeroEdicao", Hibernate.LONG);
		((SQLQuery)query).addScalar("precoCapa");
		((SQLQuery)query).addScalar("desconto");
		((SQLQuery)query).addScalar("valorTotal");
		((SQLQuery)query).addScalar("dia", Hibernate.INTEGER);

		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		query.setParameter("idDistribuidor", idDistribuidor);
		
		return query.list();
		        		
	}
	
	/**
	 * Obtém String de subSQL que retorna valor de desconto
	 * de acordo com ProdutoEdicao, Cota e Distribuidor.
	 * @return
	 */
	private String subSqlQueryValorDesconto() {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("            SELECT                                                                                   ");
		sql.append("             CASE                                                                                    ");
		sql.append("                 WHEN PE.DESCONTO IS NOT NULL THEN PE.DESCONTO                                       ");
		sql.append("                 ELSE CASE                                                                           ");
		sql.append("                     WHEN CT.FATOR_DESCONTO IS NOT NULL THEN CT.FATOR_DESCONTO                       ");
		sql.append("                     ELSE CASE                                                                       ");
		sql.append("                         WHEN DISTRIB.FATOR_DESCONTO IS NOT NULL THEN DISTRIB.FATOR_DESCONTO         ");
		sql.append("                         ELSE 0                        ");
		sql.append("                     END                               ");
		sql.append("                 END                                   ");
		sql.append("             END                                       ");
		sql.append("         FROM                                          ");
		sql.append("             PRODUTO_EDICAO PE CROSS                   ");
		sql.append("         JOIN                                          ");
		sql.append("             COTA CT CROSS                             ");
		sql.append("         JOIN                                          ");
		sql.append("             DISTRIBUIDOR DISTRIB                      ");
		sql.append("         WHERE                                         ");
		sql.append("             CT.ID=MOV_EST_COTA.COTA_ID                ");
		sql.append("             AND PE.ID=MOV_EST_COTA.PRODUTO_EDICAO_ID  ");
		sql.append("             AND DISTRIB.ID= :idDistribuidor           ");
		
		return sql.toString();
		
	}
	
	/**
	 * Retorna String referente a uma subquery que obtém o valor comissionamento 
	 * (percentual de desconto) para determinado produtoEdicao a partir de idCota e idDistribuidor. 
	 * 
	 * @return String
	 */
	private static String getSubQueryConsultaValorComissionamento() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" ( select case when ( pe.desconto is not null ) then pe.desconto else ");
		
		hql.append(" ( case when ( ct.fatorDesconto is not null ) then ct.fatorDesconto  else  ");
		
		hql.append(" ( case when ( distribuidor.fatorDesconto is not null ) then distribuidor.fatorDesconto else 0 end ) end  ");
		
		hql.append(" ) end ");
		
		hql.append(" from ProdutoEdicao pe, Cota ct, Distribuidor distribuidor ");
		
		hql.append(" where ");
		
		hql.append(" ct.id = conferenciaEncalhe.movimentoEstoqueCota.cota.id and ");

		hql.append(" pe.id = conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.id and ");

		hql.append(" distribuidor.id = :idDistribuidor ) ");
		
		return hql.toString();
		
	}
	
}
