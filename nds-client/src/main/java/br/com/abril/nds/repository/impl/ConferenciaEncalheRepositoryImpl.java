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
		
		hql.append(" 		SELECT                                                                                       ");
		hql.append("                                                                                                     ");
		hql.append(" 		CONF_ENCALHE.ID AS idConferenciaEncalhe,                                                     ");
		hql.append("                                                                                                     ");
		hql.append(" 		MOV_EST_COTA.QTDE AS qtdExemplar,                                                            ");
		hql.append("                                                                                                     ");
		hql.append(" 		MOV_EST_COTA.PRODUTO_EDICAO_ID AS idProdutoEdicao,                                           ");
		hql.append("                                                                                                     ");
		hql.append(" 		PROD_EDICAO.CODIGO_DE_BARRAS AS codigoDeBarras,                                              ");
		hql.append("                                                                                                     ");
		hql.append(" 		PROD_EDICAO.CODIGOSM AS codigoSM,                                                            ");
		hql.append("                                                                                                     ");
		hql.append(" 		PROD.CODIGO AS codigo,                                                                       ");
		hql.append("                                                                                                     ");
		hql.append(" 		PROD.NOME AS nomeProduto,                                                                    ");
		hql.append("                                                                                                     ");
		hql.append(" 		PROD_EDICAO.NUMERO_EDICAO AS numeroEdicao,                                                   ");
		hql.append("                                                                                                     ");
		hql.append(" 		PROD_EDICAO.PRECO_VENDA AS precoCapa,                                                        ");
		hql.append("                                                                                                     ");
		hql.append("         (                                                                                           ");
		hql.append("         		                                                                                     ");
		hql.append("         SELECT                                                                                      ");
		hql.append("             CASE                                                                                    ");
		hql.append("                 WHEN PE.DESCONTO IS NOT NULL THEN PE.DESCONTO                                       ");
		hql.append("                 ELSE CASE                                                                           ");
		hql.append("                     WHEN CT.FATOR_DESCONTO IS NOT NULL THEN CT.FATOR_DESCONTO                       ");
		hql.append("                     ELSE CASE                                                                       ");
		hql.append("                         WHEN DISTRIB.FATOR_DESCONTO IS NOT NULL THEN DISTRIB.FATOR_DESCONTO         ");
		hql.append("                         ELSE 0                                                                      ");
		hql.append("                     END                                                                             ");
		hql.append("                 END                                                                                 ");
		hql.append("             END                                                                                     ");
		hql.append("         FROM                                                                                        ");
		hql.append("             PRODUTO_EDICAO PE CROSS                                                                 ");
		hql.append("         JOIN                                                                                        ");
		hql.append("             COTA CT CROSS                                                                           ");
		hql.append("         JOIN                                                                                        ");
		hql.append("             DISTRIBUIDOR DISTRIB                                                                    ");
		hql.append("         WHERE                                                                                       ");
		hql.append("             CT.ID=MOV_EST_COTA.COTA_ID                                                              ");
		hql.append("             AND PE.ID=MOV_EST_COTA.PRODUTO_EDICAO_ID                                                ");
		hql.append("             AND DISTRIB.ID= :idDistribuidor                                                         ");
		hql.append("             		                                                                                 ");
		hql.append("         ) AS desconto,                                                                              ");
		hql.append("                                                                                                     ");
		hql.append("         MOV_EST_COTA.QTDE * ( PROD_EDICAO.PRECO_VENDA - ( PROD_EDICAO.PRECO_VENDA *                 ");
		hql.append("         (                                                                                           ");
		hql.append("            SELECT                                                                                   ");
		hql.append("             CASE                                                                                    ");
		hql.append("                 WHEN PE.DESCONTO IS NOT NULL THEN PE.DESCONTO                                       ");
		hql.append("                 ELSE CASE                                                                           ");
		hql.append("                     WHEN CT.FATOR_DESCONTO IS NOT NULL THEN CT.FATOR_DESCONTO                       ");
		hql.append("                     ELSE CASE                                                                       ");
		hql.append("                         WHEN DISTRIB.FATOR_DESCONTO IS NOT NULL THEN DISTRIB.FATOR_DESCONTO         ");
		hql.append("                         ELSE 0                                                                      ");
		hql.append("                     END                                                                             ");
		hql.append("                 END                                                                                 ");
		hql.append("             END                                                                                     ");
		hql.append("         FROM                                                                                        ");
		hql.append("             PRODUTO_EDICAO PE CROSS                                                                 ");
		hql.append("         JOIN                                                                                        ");
		hql.append("             COTA CT CROSS                                                                           ");
		hql.append("         JOIN                                                                                        ");
		hql.append("             DISTRIBUIDOR DISTRIB                                                                    ");
		hql.append("         WHERE                                                                                       ");
		hql.append("             CT.ID=MOV_EST_COTA.COTA_ID                                                              ");
		hql.append("             AND PE.ID=MOV_EST_COTA.PRODUTO_EDICAO_ID                                                ");
		hql.append("             AND DISTRIB.ID= :idDistribuidor                                                         ");
		hql.append("             		                                                                                 ");
		hql.append("         )                                                                                           ");
		hql.append("                                                                                                     ");
		hql.append("             /100)) AS valorTotal,                                                                   ");
		hql.append("                                                                                                     ");
		hql.append("         TO_DAYS(MOV_EST_COTA.DATA)-TO_DAYS(CH_ENCALHE.DATA_RECOLHIMENTO) + 1 AS dia,                ");
		hql.append("                                                                                                     ");
		hql.append("         CONF_ENCALHE.OBSERVACAO AS observacao,                                                      ");
		hql.append("                                                                                                     ");
		hql.append("         CONF_ENCALHE.JURAMENTADA AS juramentada                                                     ");
		hql.append("                                                                                                     ");
		hql.append("     FROM                                                                                            ");
		hql.append("                                                                                                     ");
		hql.append("         CONFERENCIA_ENCALHE CONF_ENCALHE,                                                           ");
		hql.append("         MOVIMENTO_ESTOQUE_COTA MOV_EST_COTA,                                                        ");
		hql.append("         PRODUTO_EDICAO PROD_EDICAO,                                                                 ");
		hql.append("         PRODUTO PROD,                                                                               ");
		hql.append("         CHAMADA_ENCALHE_COTA CH_ENCALHE_COTA,                                                       ");
		hql.append("         CHAMADA_ENCALHE CH_ENCALHE                                                                  ");
		hql.append("     WHERE                                                                                           ");
		hql.append("                                                                                                     ");
		hql.append("         CONF_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID=MOV_EST_COTA.ID                                      ");
		hql.append("         AND MOV_EST_COTA.PRODUTO_EDICAO_ID=PROD_EDICAO.ID                                           ");
		hql.append("         AND PROD_EDICAO.PRODUTO_ID=PROD.ID                                                          ");
		hql.append("         AND CONF_ENCALHE.CHAMADA_ENCALHE_COTA_ID=CH_ENCALHE_COTA.ID                                 ");
		hql.append("         AND CH_ENCALHE_COTA.CHAMADA_ENCALHE_ID=CH_ENCALHE.ID                                        ");
		hql.append("         AND CONF_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = :idControleConferenciaEncalheCota   ");
		
		Query query =  this.getSession().createSQLQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ConferenciaEncalheDTO.class));
		
		((SQLQuery)query).addScalar("idConferenciaEncalhe", Hibernate.LONG);
		((SQLQuery)query).addScalar("qtdExemplar");
		((SQLQuery)query).addScalar("idProdutoEdicao", Hibernate.LONG);
		((SQLQuery)query).addScalar("codigoDeBarras");
		((SQLQuery)query).addScalar("codigoSM");
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
