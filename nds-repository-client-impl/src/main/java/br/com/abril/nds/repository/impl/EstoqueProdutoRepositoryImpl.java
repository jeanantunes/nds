package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ProdutoEdicaoSuplementarDTO;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstoqueProdutoRespository;

@Repository
public class EstoqueProdutoRepositoryImpl extends AbstractRepositoryModel<EstoqueProduto, Long> implements EstoqueProdutoRespository{

	public EstoqueProdutoRepositoryImpl() {
		super(EstoqueProduto.class);
		
	}

	@Override
	public EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao) {

		Criteria criteria = super.getSession().createCriteria(EstoqueProduto.class, "estoqueProduto");
		
		criteria.createAlias("estoqueProduto.produtoEdicao", "produtoEdicao");
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		
		criteria.setMaxResults(1);
		
		return (EstoqueProduto) criteria.uniqueResult();
	}
	
	public EstoqueProduto buscarEstoqueProdutoPorProdutoEdicao(Long idProdutoEdicao){
		StringBuilder hql = new StringBuilder("select estoqueProduto ");
		hql.append(" from EstoqueProduto estoqueProduto, ProdutoEdicao produtoEdicao ")
		   .append(" where estoqueProduto.produtoEdicao.id = produtoEdicao.id ")
		   .append(" and produtoEdicao.id = :idProdutoEdicao");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setMaxResults(1);
		
		return (EstoqueProduto) query.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicaoSuplementarDTO> obterProdutosEdicaoSuplementarNaoDisponivel(
			Long idCotaAusente, Date dataMovimento) {

		StringBuilder sql = new StringBuilder();

		sql.append(" 	SELECT p.CODIGO AS codigoProduto, 						");
		sql.append(" 		   p.NOME AS nomeProdutoEdicao, 					");
		sql.append(" 		   pe.ID AS idProdutoEdicao, 						");
		sql.append(" 		   pe.NUMERO_EDICAO AS numeroEdicao, 				");
		sql.append(" 		   mec.QTDE AS reparte, 							");
		sql.append(" 		   ep.QTDE_SUPLEMENTAR AS quantidadeDisponivel	 				");
		sql.append(" 	FROM MOVIMENTO_ESTOQUE_COTA mec 						");
		sql.append("	JOIN TIPO_MOVIMENTO tm ON tm.ID = mec.TIPO_MOVIMENTO_ID ");
		sql.append("	JOIN PRODUTO_EDICAO pe ON pe.ID = mec.PRODUTO_EDICAO_ID ");
		sql.append("	JOIN PRODUTO p ON p.ID = pe.PRODUTO_ID 					");
		sql.append("	JOIN ESTOQUE_PRODUTO ep ON ep.PRODUTO_EDICAO_ID = pe.ID ");
		sql.append("	JOIN COTA_AUSENTE ca ON ca.COTA_ID=mec.COTA_ID 			");
		sql.append("	WHERE ca.ID = :idCotaAusente 							");
		sql.append("	AND mec.DATA = :dataMovimento 							");
		sql.append("	AND tm.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimento		");
		sql.append("	AND mec.QTDE >= ep.QTDE_SUPLEMENTAR 								");

		Query query = getSession().createSQLQuery(sql.toString())
							.addScalar("codigoProduto", StandardBasicTypes.STRING)
							.addScalar("nomeProdutoEdicao", StandardBasicTypes.STRING)
							.addScalar("idProdutoEdicao", StandardBasicTypes.LONG)
							.addScalar("numeroEdicao", StandardBasicTypes.LONG)
							.addScalar("reparte", StandardBasicTypes.BIG_INTEGER)
							.addScalar("quantidadeDisponivel", StandardBasicTypes.BIG_INTEGER);

		query.setParameter("idCotaAusente", idCotaAusente);
		query.setParameter("dataMovimento", dataMovimento);
		query.setParameter("grupoMovimento", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());

		query.setResultTransformer(Transformers.aliasToBean(ProdutoEdicaoSuplementarDTO.class));

		return query.list();
	}
}
