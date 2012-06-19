package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
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
	 * @see br.com.abril.nds.repository.ConferenciaEncalheRepository#obterDadosSlipConferenciaEncalhe(java.lang.Long, java.lang.Long)
	 */
	public List<ProdutoEdicaoSlipDTO> obterDadosSlipConferenciaEncalhe(Long idControleConferenciaEncalheCota, Long idDistribuidor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.produto.nome as nomeProduto,	");
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.numeroEdicao as numeroEdicao,	");
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.id as idProdutoEdicao,			");
		
		hql.append(" (conferencia.movimentoEstoqueCota.produtoEdicao.precoVenda -	");
		hql.append(" ( conferencia.movimentoEstoqueCota.produtoEdicao.precoVenda * ("+ getSubHqlQueryValorDesconto() +") / 100 )) as precoVenda,	");
		
		hql.append(" conferencia.movimentoEstoqueCota.qtde as encalhe, ");
		
		hql.append(" ((conferencia.movimentoEstoqueCota.produtoEdicao.precoVenda -  			");
		hql.append(" ( conferencia.movimentoEstoqueCota.produtoEdicao.precoVenda * ("+ getSubHqlQueryValorDesconto() +") / 100 ))  ");
		hql.append(" * conferencia.movimentoEstoqueCota.qtde) as valorTotal ");
		
		hql.append(" from ConferenciaEncalhe conferencia	");
		
		hql.append(" where	");
		
		hql.append(" conferencia.controleConferenciaEncalheCota.id = :idControleConferenciaEncalheCota ");
		
		Query query =  this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoSlipDTO.class));
		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		query.setParameter("idDistribuidor", idDistribuidor);
		
		return query.list();
	
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConferenciaEncalheRepository#obterListaConferenciaEncalheDTO(java.lang.Long, java.lang.Long)
	 */
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
		hql.append( subSqlQuerySequenciaMatriz() );
		hql.append(" ) AS codigoSM, ");
		
		hql.append(" CH_ENCALHE.DATA_RECOLHIMENTO AS dataRecolhimento,  	 ");
		hql.append(" CH_ENCALHE.TIPO_CHAMADA_ENCALHE AS tipoChamadaEncalhe,	 ");
		hql.append(" PROD.CODIGO AS codigo,                                  ");
		hql.append(" PROD.NOME AS nomeProduto,                               ");
		hql.append(" PROD_EDICAO.NUMERO_EDICAO AS numeroEdicao,              ");
		hql.append(" PROD_EDICAO.PRECO_VENDA AS precoCapa,                   ");
		
		hql.append("        ( PROD_EDICAO.PRECO_VENDA *  ( ");
		hql.append(    getSubSqlQueryValorDesconto()			);		
		hql.append("         ) / 100 ) AS desconto,        ");
		
		hql.append("         CONF_ENCALHE.QTDE * ( PROD_EDICAO.PRECO_VENDA - ( PROD_EDICAO.PRECO_VENDA *  ");
		
		hql.append(" ( 							");
		hql.append(getSubSqlQueryValorDesconto()	 );
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
		((SQLQuery)query).addScalar("qtdExemplar");
		((SQLQuery)query).addScalar("qtdInformada");
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
		((SQLQuery)query).addScalar("desconto");
		((SQLQuery)query).addScalar("valorTotal");
		((SQLQuery)query).addScalar("dia", StandardBasicTypes.INTEGER);

		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		query.setParameter("idDistribuidor", idDistribuidor);
		
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
	private String getSubSqlQueryValorDesconto() {
		
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
		sql.append("             CT.ID=CH_ENCALHE_COTA.COTA_ID             ");
		sql.append("             AND PE.ID=CONF_ENCALHE.PRODUTO_EDICAO_ID  ");
		sql.append("             AND DISTRIB.ID= :idDistribuidor           ");
		
		return sql.toString();
		
	}
	
	
	public BigDecimal obterValorTotalEncalheOperacaoConferenciaEncalhe(Long idControleConferenciaEncalhe, Long idDistribuidor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select sum( conferencia.produtoEdicao.precoVenda - (conferencia.produtoEdicao.precoVenda * ("+ getSubHqlQueryValorDesconto() +") / 100 ) ) ");
		
		hql.append(" from ConferenciaEncalhe conferencia  ");
		
		hql.append(" where conferencia.controleConferenciaEncalheCota.id = :idControleConferenciaEncalhe  ");
		
		Query query =  this.getSession().createQuery(hql.toString());
		
		query.setParameter("idControleConferenciaEncalhe", idControleConferenciaEncalhe);
		
		query.setParameter("idDistribuidor", idDistribuidor);
		
		return (BigDecimal) query.uniqueResult();
		
	}
	
	/**
	 * Obtém String de subHQL que retorna valor de desconto
	 * de acordo com ProdutoEdicao, Cota e Distribuidor.
	 * 
	 * @return String
	 */
	private String getSubHqlQueryValorDesconto() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select case when ( pe.desconto is not null ) then pe.desconto else ");
		
		hql.append(" ( case when ( ct.fatorDesconto is not null ) then ct.fatorDesconto  else  ");
		
		hql.append(" ( case when ( distribuidor.fatorDesconto is not null ) then distribuidor.fatorDesconto else 0 end ) end  ");
		
		hql.append(" ) end ");
		
		hql.append(" from ProdutoEdicao pe, Cota ct, Distribuidor distribuidor ");
		
		hql.append(" where ");
		
		hql.append(" ct.id = conferencia.chamadaEncalheCota.cota.id and ");

		hql.append(" pe.id = conferencia.produtoEdicao.id and ");

		hql.append(" distribuidor.id = :idDistribuidor ");
		
		return hql.toString();
		
	}
	
}
