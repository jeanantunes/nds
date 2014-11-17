package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaDescontoProdutoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO.OrdenacaoColunaConsulta;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DescontoProdutoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do produto
 * 
 * @author Discover Technology
 */
@Repository
public class DescontoProdutoRepositoryImpl extends AbstractRepositoryModel<DescontoProduto,Long> implements DescontoProdutoRepository {

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	public DescontoProdutoRepositoryImpl() {
		super(DescontoProduto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDescontoProdutoDTO> buscarTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {

		StringBuilder hql = new StringBuilder();
				
		hql.append(" SELECT dados.DESCONTO_ID as descontoId, p.CODIGO as codigoProduto, p.NOME as nomeProduto, "
				+"	 	pe.NUMERO_EDICAO as numeroEdicao, d.VALOR as desconto, d.PREDOMINANTE as predominante, u.NOME as nomeUsuario, "
				+"	 	d.DATA_ALTERACAO as dataAlteracao, dados.QTDE_PROX_LANCAMENTOS as qtdeProxLcmtAtual, "
				+"		dados.QTDE_PROX_LANCAMENTOS_ORIGINAL as qtdeProxLcmt, dados.QTDE_COTAS as qtdeCotas ");
		
		addQueryTipoDescontoProduto(hql);
		
		if(filtro.getCodigoProduto() != null)
			hql.append("WHERE p.CODIGO = :codigoProduto ");
		
		addOrderByrTipoDescontoProduto(hql, filtro);
		
		Query q = getSession().createSQLQuery(hql.toString());

		if(filtro.getCodigoProduto() != null)
			q.setParameter("codigoProduto", filtro.getCodigoProduto());

		if (filtro.getPaginacao() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			q.setFirstResult( filtro.getPaginacao().getQtdResultadosPorPagina() * ( (filtro.getPaginacao().getPaginaAtual() - 1 )))
			.setMaxResults( filtro.getPaginacao().getQtdResultadosPorPagina() );
		}

		q.setResultTransformer(Transformers.aliasToBean(TipoDescontoProdutoDTO.class));
		
		return (List<TipoDescontoProdutoDTO>) q.list();
		
	}
	
	private StringBuilder addQueryTipoDescontoProduto(StringBuilder sql) {
			
		sql.append(" FROM (SELECT DESCONTO_ID, PRODUTO_ID, null as EDICAO_ID, hdp.USUARIO_ID, null as QTDE_PROX_LANCAMENTOS, null as QTDE_PROX_LANCAMENTOS_ORIGINAL, null as QTDE_COTAS "  
					+"	 FROM HISTORICO_DESCONTO_PRODUTOS hdp "	
					+"	 UNION "	
					
					+"	 SELECT DESCONTO_ID, PRODUTO_ID, PRODUTO_EDICAO_ID as EDICAO_ID, hdcpe.USUARIO_ID, null as QTDE_PROX_LANCAMENTOS, null as QTDE_PROX_LANCAMENTOS_ORIGINAL, count(COTA_ID) as QTDE_COTAS "
					+"	 FROM HISTORICO_DESCONTO_COTA_PRODUTO_EXCESSOES hdcpe " 
					+"	 where produto_id is not null " 
					+"	 group by DESCONTO_ID "	
					+"	 UNION "	
					
					+"	 SELECT DESCONTO_ID, PRODUTO_ID, PRODUTO_EDICAO_ID as EDICAO_ID, hdpe.USUARIO_ID, null as QTDE_PROX_LANCAMENTOS, null as QTDE_PROX_LANCAMENTOS_ORIGINAL, null as QTDE_COTAS "
					+"	 FROM HISTORICO_DESCONTO_PRODUTO_EDICOES hdpe "	
					+"	 UNION "	
					
					+"	 SELECT dpl.DESCONTO_ID, PRODUTO_ID, null as EDICAO_ID, dpl.USUARIO_ID, QUANTIDADE_PROXIMOS_LANCAMENTOS, QUANTIDADE_PROXIMOS_LANCAMENTOS_ORIGINAL, " 
					+"	 CASE WHEN dpl.APLICADO_A_TODAS_AS_COTAS=1 THEN null ELSE count(dlc.DESCONTO_LANCAMENTO_ID) END as QTDE_COTAS " 
					+"	 FROM DESCONTO_PROXIMOS_LANCAMENTOS dpl "
					+"	 LEFT OUTER JOIN DESCONTO_LANCAMENTO_COTA dlc on (dlc.DESCONTO_LANCAMENTO_ID=dpl.ID) "
					+"	 group by dpl.ID "
					+"	 ) as dados "
					
					+" JOIN DESCONTO d on (dados.DESCONTO_ID=d.ID) "
					+" JOIN USUARIO u on (dados.USUARIO_ID=u.ID) "
					+" LEFT OUTER JOIN PRODUTO p on (dados.PRODUTO_ID=p.ID) "
					+" LEFT OUTER JOIN PRODUTO_EDICAO pe on (dados.EDICAO_ID=pe.ID) ");
			
		return sql;
	}

	private void addOrderByrTipoDescontoProduto(StringBuilder hql,
			FiltroTipoDescontoProdutoDTO filtro) {
		
		OrdenacaoColunaConsulta sortColum = filtro.getOrdenacaoColuna();
		
		String sortOrder = filtro.getPaginacao() != null ? 
						   filtro.getPaginacao().getOrdenacao() == null ? "" : filtro.getPaginacao().getOrdenacao().name() : "";
		
		if(sortColum != null && sortOrder != null)
			hql.append(" order by " + sortColum + " " + sortOrder);
		else
			hql.append(" order by dataAlteracao desc , nomeProduto");
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer buscarQuantidadeTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT count(dados.DESCONTO_ID) ");
		
		addQueryTipoDescontoProduto(hql);
		
		if(filtro.getCodigoProduto()!=null)
			hql.append("WHERE p.CODIGO = :codigoProduto ");
				
		Query q = getSession().createSQLQuery(hql.toString());

		if(filtro.getCodigoProduto()!=null)
			q.setParameter("codigoProduto", filtro.getCodigoProduto());
		
		return ((BigInteger) q.uniqueResult()).intValue();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<CotaDescontoProdutoDTO> obterCotasDoTipoDescontoProduto(Long idDescontoProduto, Ordenacao ordenacao) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT c.NUMERO_COTA as numeroCota, coalesce(p.NOME, p.RAZAO_SOCIAL) as nome ")
			.append(" FROM COTA c ")
			.append(" JOIN PESSOA p ON (c.PESSOA_ID=p.ID) ")
			.append(" LEFT OUTER JOIN HISTORICO_DESCONTO_COTA_PRODUTO_EXCESSOES hdcpe on (hdcpe.COTA_ID=c.ID) ")
			.append(" LEFT OUTER JOIN DESCONTO_LANCAMENTO_COTA dlc ON (dlc.COTA_ID=c.ID) ")
			.append(" WHERE hdcpe.DESCONTO_ID = :descontoID OR dlc.DESCONTO_LANCAMENTO_ID = :descontoID ")
			.append(" UNION ")
			.append(" SELECT c.NUMERO_COTA as numeroCota, coalesce(p.NOME, p.RAZAO_SOCIAL) as nome ")
			.append(" FROM COTA c ")
			.append(" JOIN PESSOA p ON ( c.PESSOA_ID=p.ID ) ")
			.append(" LEFT OUTER JOIN DESCONTO_LANCAMENTO_COTA dlc on( dlc.COTA_ID=c.ID ) ")
			.append(" LEFT OUTER JOIN DESCONTO_PROXIMOS_LANCAMENTOS dpl ON dpl.id = dlc.desconto_lancamento_id ")
			.append(" WHERE dpl.DESCONTO_ID = :descontoID ")
			.append(" GROUP BY numeroCota ");

		sql.append(" order by numeroCota " + ordenacao.name() + " ");
		
		Query query = getSession().createSQLQuery(sql.toString());

		query.setParameter("descontoID", idDescontoProduto);

		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDescontoProdutoDTO.class));

		return query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDescontoProdutoDTO> obterTiposDescontoProdutoPorCota(Long idCota, String sortorder, String sortname) {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT dadosCompletos.* " +
		" FROM " +
		"  (SELECT dados.DESCONTO_ID AS idTipoDesconto, " +
		"          p.CODIGO AS codigoProduto, " +
		"          p.NOME AS nomeProduto, " +
		"          pe.NUMERO_EDICAO AS numeroEdicao, " +
		"          d.VALOR AS desconto, " +
		"          u.NOME AS nomeUsuario, " +
		"          d.DATA_ALTERACAO AS dataAlteracao, " +
		"          dados.QTDE_PROX_LANCAMENTOS AS qtdeProxLcmt, " +
		"          dados.QTDE_COTAS AS qtdeCotas " +
		"   FROM " +
		"     (" + getSubSelectDesconto(idCota) + ") AS dados " +
		"   JOIN DESCONTO d ON (dados.DESCONTO_ID = d.ID) " +
		"   JOIN USUARIO u ON (dados.USUARIO_ID = u.ID) " +
		"   LEFT OUTER JOIN PRODUTO p ON (dados.PRODUTO_ID = p.ID) " +
		"   LEFT OUTER JOIN PRODUTO_EDICAO pe ON (dados.EDICAO_ID = pe.ID) " +
		"   ORDER BY codigoProduto DESC) AS dadosCompletos " +

		" JOIN " +
		" (SELECT " +     
		"          max(d.DATA_ALTERACAO) AS dataAlteracao " +
		"   FROM " +
		"     (" + getSubSelectDesconto(idCota) + ") AS dados " +
		"   JOIN DESCONTO d ON (dados.DESCONTO_ID = d.ID) " +
		   
		"   GROUP BY dados.PRODUTO_ID, " +
		"            dados.EDICAO_ID, " +
		"            dados.QTDE_COTAS " +
		"             ) AS maiorData ON (dadosCompletos.dataAlteracao = maiorData.dataAlteracao) " +
		 
		"  where dadosCompletos.qtdeCotas>0  " +
		"  or CONCAT(coalesce(codigoProduto,''),'-', coalesce(numeroEdicao,'')) not in ( " +
		  
		"  SELECT CONCAT(coalesce(codigoProduto,''),'-', coalesce(numeroEdicao,'')) " +
		" FROM " +
		"  (SELECT dados.DESCONTO_ID AS idTipoDesconto, " +
		"          p.NOME AS nomeProduto, " +
		"          p.CODIGO AS codigoProduto, " +
		"          pe.NUMERO_EDICAO AS numeroEdicao, " +
		"          d.VALOR AS desconto, " +
		"          u.NOME AS nomeUsuario, " +
		"          d.DATA_ALTERACAO AS dataAlteracao, " +
		"          dados.QTDE_PROX_LANCAMENTOS AS qtdeProxLcmt, " +
		"          dados.QTDE_COTAS AS qtdeCotas " +
		"  FROM " +
		"     (" + getSubSelectDesconto(idCota) + ") AS dados " +
		"   JOIN DESCONTO d ON (dados.DESCONTO_ID = d.ID) " +
		"   JOIN USUARIO u ON (dados.USUARIO_ID = u.ID) " +
		"  LEFT OUTER JOIN PRODUTO p ON (dados.PRODUTO_ID = p.ID) " +
		"   LEFT OUTER JOIN PRODUTO_EDICAO pe ON (dados.EDICAO_ID = pe.ID) " +
		"   ORDER BY codigoProduto DESC) AS dadosCompletos " +

		" JOIN " +
		"(SELECT " +     
		"          max(d.DATA_ALTERACAO) AS dataAlteracao " +
		"   FROM " +
		"     (" + getSubSelectDesconto(idCota) + ") AS dados " +
		"   JOIN DESCONTO d ON (dados.DESCONTO_ID = d.ID) " +
		  
		"   GROUP BY dados.PRODUTO_ID, " +
		"            dados.EDICAO_ID, " +
		"            dados.QTDE_COTAS " +
		"             ) AS maiorData ON (dadosCompletos.dataAlteracao = maiorData.dataAlteracao) " +
		
		"  where dadosCompletos.qtdeCotas>0 " + 
		"  ) ");
		
		sql.append(" 	group by 			");
		sql.append("	idTipoDesconto, 	");          
		sql.append("	codigoProduto,  	");        
		sql.append("	nomeProduto,    	");     
		sql.append("	numeroEdicao,   	");     
		sql.append("	desconto,       	");   
		sql.append("	nomeUsuario,    	");       
		sql.append("	dataAlteracao   	");        

		if (sortname != null && !sortname.isEmpty()) { 

			sql.append(" order by ");
			sql.append(sortname);
			sql.append(" ");
			sql.append(sortorder != null ? sortorder : "");
		}

		Query query = getSession().createSQLQuery(sql.toString());

		if (idCota!=null){
			query.setParameter("idCota", idCota);
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(TipoDescontoProdutoDTO.class));

		return query.list();
	}
	
	private String getSubSelectDesconto(Long idCota) {
		
		StringBuilder sql = new StringBuilder("");
		
		sql.append(" SELECT DESCONTO_ID, "
				+"        PRODUTO_ID, "
				+"        NULL AS EDICAO_ID, "
				+"        USUARIO_ID, "
				+"        NULL AS QTDE_PROX_LANCAMENTOS, "
				+"        0 AS QTDE_COTAS "
				+" FROM HISTORICO_DESCONTO_PRODUTOS hdp "
				+" UNION SELECT DESCONTO_ID, "
				+"              PRODUTO_ID, "
				+"              PRODUTO_EDICAO_ID AS EDICAO_ID, "
				+"              USUARIO_ID, "
				+"              NULL AS QTDE_PROX_LANCAMENTOS, "
				+"              count(COTA_ID) AS QTDE_COTAS "
				+" FROM HISTORICO_DESCONTO_COTA_PRODUTO_EXCESSOES hdcpe "
				+" WHERE produto_id IS NOT NULL ");
				
		if(idCota != null)
			sql.append(" AND COTA_ID=:idCota ");
						
			sql.append(" GROUP BY DESCONTO_ID "
				+" UNION SELECT DESCONTO_ID, "
				+"              PRODUTO_ID, "
				+"              PRODUTO_EDICAO_ID AS EDICAO_ID, "
				+"              USUARIO_ID, "
				+"              NULL AS QTDE_PROX_LANCAMENTOS, "
				+"              0 AS QTDE_COTAS "
				+" FROM HISTORICO_DESCONTO_PRODUTO_EDICOES hdpe "
				+" UNION SELECT dpl.DESCONTO_ID, PRODUTO_ID, NULL AS EDICAO_ID, USUARIO_ID, QUANTIDADE_PROXIMOS_LANCAMENTOS AS QTDE_PROX_LANCAMENTOS, " 
				+" CASE WHEN dpl.APLICADO_A_TODAS_AS_COTAS = 1 THEN 0 ELSE count(dlc.DESCONTO_LANCAMENTO_ID) END AS QTDE_COTAS "
				+" FROM DESCONTO_PROXIMOS_LANCAMENTOS dpl "
				+" LEFT OUTER JOIN DESCONTO_LANCAMENTO_COTA dlc ON (dlc.DESCONTO_LANCAMENTO_ID = dpl.ID) ");
				
		if(idCota != null)
			sql.append(" WHERE dlc.COTA_ID=:idCota ");
				
		sql.append(" GROUP BY dpl.ID ");
		
		return sql.toString();

	}

	@Override
	public DescontoProduto buscarUltimoDescontoValido(Cota cota,ProdutoEdicao produtoEdicao) {

		return obterDescontoValido(null, cota, produtoEdicao);
	}

	@Override
	public DescontoProduto buscarUltimoDescontoValido(Long idDesconto,Cota cota, ProdutoEdicao produtoEdicao) {

		return obterDescontoValido(idDesconto, cota, produtoEdicao);
	}

	private DescontoProduto obterDescontoValido(Long idDesconto, Cota cota, ProdutoEdicao produtoEdicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select desconto from DescontoProduto desconto JOIN desconto.produtoEdicao produtoEdicao JOIN desconto.cotas cota  ")
		.append("where desconto.dataAlteracao = ")
		.append(" ( select max(descontoSub.dataAlteracao) from DescontoProduto descontoSub  ")
		.append(" JOIN descontoSub.produtoEdicao produtoEdicaoSub JOIN descontoSub.cotas cotaSub ")
		.append(" where produtoEdicaoSub.id =:idProdutoEdicao ")
		.append(" and cotaSub.id =:idCota ");
		if(idDesconto!= null){
			hql.append(" and descontoSub.id not in (:idUltimoDesconto) ");
		}

		hql.append(" ) ")
		.append(" AND produtoEdicao.id =:idProdutoEdicao ")
		.append(" AND cota.id =:idCota ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao",produtoEdicao.getId());

		if(idDesconto!= null){

			query.setParameter("idUltimoDesconto", idDesconto);
		}

		query.setParameter("idCota", cota.getId());

		query.setMaxResults(1);

		return (DescontoProduto)  query.uniqueResult();
	}
}