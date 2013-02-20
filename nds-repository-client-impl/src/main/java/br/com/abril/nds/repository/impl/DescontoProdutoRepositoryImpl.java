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
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDescontoProdutoDTO> buscarTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {

		StringBuilder hql = new StringBuilder();
				
		hql.append(" SELECT dados.DESCONTO_ID as idTipoDesconto,p.CODIGO as codigoProduto, p.NOME as nomeProduto, "
				+"	 	pe.NUMERO_EDICAO as numeroEdicao, d.VALOR as desconto, u.NOME as nomeUsuario, "
				+"	 	d.DATA_ALTERACAO as dataAlteracao, dados.QTDE_PROX_LANCAMENTOS as qtdeProxLcmt, dados.QTDE_COTAS as qtdeCotas");
		
		addQueryTipoDescontoProduto(hql);
		
		if(filtro.getCodigoProduto()!=null)
			hql.append("WHERE p.CODIGO = :codigoProduto ");
		
		addOrderByrTipoDescontoProduto(hql, filtro);
		
		
		Query q = getSession().createSQLQuery(hql.toString());

		if(filtro.getCodigoProduto()!=null)
			q.setParameter("codigoProduto", filtro.getCodigoProduto());

		if (filtro.getPaginacao() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			q.setFirstResult( filtro.getPaginacao().getQtdResultadosPorPagina() * ( (filtro.getPaginacao().getPaginaAtual() - 1 )))
			.setMaxResults( filtro.getPaginacao().getQtdResultadosPorPagina() );
		}

		q.setResultTransformer(Transformers.aliasToBean(TipoDescontoProdutoDTO.class));
		
		return (List<TipoDescontoProdutoDTO>) q.list();
		
	}
	
	private StringBuilder addQueryTipoDescontoProduto(StringBuilder sql) {
			
		sql.append(" FROM (SELECT DESCONTO_ID, PRODUTO_ID, null as EDICAO_ID, USUARIO_ID, null as QTDE_PROX_LANCAMENTOS, null as QTDE_COTAS "  
					+"	 FROM HISTORICO_DESCONTO_PRODUTOS hdp "	
					+"	 UNION "	
					
					+"	 SELECT DESCONTO_ID, PRODUTO_ID, PRODUTO_EDICAO_ID as EDICAO_ID, USUARIO_ID, null as QTDE_PROX_LANCAMENTOS, count(COTA_ID) as QTDE_COTAS "
					+"	 FROM HISTORICO_DESCONTO_COTA_PRODUTO_EXCESSOES hdcpe " 
					+"	 where produto_id is not null " 
					+"	 group by DESCONTO_ID "	
					+"	 UNION "	
					
					+"	 SELECT DESCONTO_ID, PRODUTO_ID, PRODUTO_EDICAO_ID as EDICAO_ID, USUARIO_ID, null as QTDE_PROX_LANCAMENTOS, null as QTDE_COTAS "
					+"	 FROM HISTORICO_DESCONTO_PRODUTO_EDICOES hdpe "	
					+"	 UNION "	
					
					+"	 SELECT dpl.DESCONTO_ID, PRODUTO_ID, null as EDICAO_ID, USUARIO_ID, QUANTIDADE_PROXIMOS_LANCAMENTOS as QTDE_PROX_LANCAMENTOS, " 
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
		
		String sortOrder = filtro.getPaginacao().getOrdenacao().name();
		
		if(sortColum != null && sortOrder != null)
			hql.append(" order by " + sortColum + " " + sortOrder);
		else
			hql.append(" order by nomeProduto, dataAlteracao desc ");
		
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
			
		sql = new StringBuilder();
		sql.append(" SELECT c.NUMERO_COTA as numeroCota, coalesce(p.NOME, p.RAZAO_SOCIAL) as nome FROM COTA c "
					+ " JOIN PESSOA p on (c.PESSOA_ID=p.ID) "
					+ " LEFT OUTER JOIN HISTORICO_DESCONTO_COTA_PRODUTO_EXCESSOES hdcpe on (hdcpe.COTA_ID=c.ID) "
					+ " LEFT OUTER JOIN DESCONTO_LANCAMENTO_COTA dlc on(dlc.COTA_ID=c.ID) "
					+ " WHERE hdcpe.DESCONTO_ID=:descontoID or dlc.DESCONTO_LANCAMENTO_ID=:descontoID "
					+ " group by c.NUMERO_COTA ");

		sql.append(" order by c.NUMERO_COTA " + ordenacao.name() + " ");
		
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

		StringBuilder hql = new StringBuilder();

		hql.append("SELECT p.CODIGO as codigoProduto, p.NOME as nomeProduto, pe.NUMERO_EDICAO as numeroEdicao, " 
				+ "		d.VALOR as desconto, d.DATA_ALTERACAO as dataAlteracao, "
				+ "		dados.QTDE_PROX_LANCAMENTOS as qtdeProxLcmt FROM ( "
				
				+ "	SELECT DESCONTO_ID, PRODUTO_ID, PRODUTO_EDICAO_ID as EDICAO_ID,COTA_ID, null as QTDE_PROX_LANCAMENTOS "
				+ "	FROM HISTORICO_DESCONTO_COTA_PRODUTO_EXCESSOES hdcpe  "
				+ "	where produto_id is not null ");
		
		if (idCota!=null)
			hql.append(" AND COTA_ID=:idCota ");
		
		hql.append("	group by DESCONTO_ID	 "
				+ "	UNION	 "
				
				+ "	SELECT dpl.DESCONTO_ID, PRODUTO_ID, null as EDICAO_ID,dlc.COTA_ID, QUANTIDADE_PROXIMOS_LANCAMENTOS as QTDE_PROX_LANCAMENTOS "
				+ "	FROM DESCONTO_PROXIMOS_LANCAMENTOS dpl "
				+ "	LEFT OUTER JOIN DESCONTO_LANCAMENTO_COTA dlc on (dlc.DESCONTO_LANCAMENTO_ID=dpl.ID) ");
		
		if (idCota!=null)
			hql.append(" AND dlc.COTA_ID=:idCota ");
		
		hql.append("	group by dpl.ID "
				+ "	) as dados "
				
				+ "JOIN DESCONTO d on (dados.DESCONTO_ID=d.ID) "
				+ "LEFT OUTER JOIN PRODUTO p on (dados.PRODUTO_ID=p.ID) "
				+ "LEFT OUTER JOIN PRODUTO_EDICAO pe on (dados.EDICAO_ID=pe.ID) ");
		

		if (sortname != null && !sortname.isEmpty()) { 

			hql.append(" order by ");
			hql.append(sortname);
			hql.append(" ");
			hql.append(sortorder != null ? sortorder : "");
		}

		Query query = getSession().createSQLQuery(hql.toString());

		if (idCota!=null){
			query.setParameter("idCota", idCota);
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(TipoDescontoProdutoDTO.class));

		return query.list();
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
