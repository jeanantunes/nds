package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaDescontoProdutoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
import br.com.abril.nds.repository.DescontoProdutoRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do produto
 * 
 * @author Discover Technology
 */
@Repository
public class DescontoProdutoRepositoryImpl extends AbstractRepositoryModel<DescontoProduto,Long> implements DescontoProdutoRepository {

	public DescontoProdutoRepositoryImpl() {
		super(DescontoProduto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDescontoProdutoDTO> buscarTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {
		
		Criteria criteria = this.getSession().createCriteria(DescontoProduto.class);
		
		criteria.createAlias("usuario", "usuario");
		criteria.createAlias("produtoEdicao", "produtoEdicao");
		criteria.createAlias("produtoEdicao.produto", "produto");

		ProjectionList projectionList = (ProjectionList) getAliasProjectionTipoDescontoProduto();

		if (filtro != null && filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {

			criteria.add(Restrictions.eq("produto.codigo", filtro.getCodigoProduto()));
		}

		if (filtro != null && filtro.getPaginacao() != null) {
			
			if(filtro.getPaginacao().getPosicaoInicial()!= null){

				criteria.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina()!= null){				

				criteria.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());

			}
			
			if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao().getOrdenacao() != null) {

				switch(filtro.getPaginacao().getOrdenacao()) {

				case ASC:
					criteria.addOrder(Order.asc(filtro.getOrdenacaoColuna().toString()));
					break;
				case DESC:
					criteria.addOrder(Order.desc(filtro.getOrdenacaoColuna().toString()));
					break;
				}
			}
		}

		criteria.setProjection(projectionList);

		criteria.setResultTransformer(new AliasToBeanResultTransformer(TipoDescontoProdutoDTO.class));

		return criteria.list();
	}

	/*
	 * Retorna uma projeção com os alias da consulta de Tipo Desconto Produto.
	 * 
	 * Esses alias serão utilizados para transformar o resultado da consulta em um objeto TipoDescontoProdutoDTO.
	 * 
	 */
	private Projection getAliasProjectionTipoDescontoProduto() {

		ProjectionList projectionList = Projections.projectionList().create();

		projectionList.add(Projections.alias(Projections.property("id"), "idTipoDesconto"));
		projectionList.add(Projections.alias(Projections.property("desconto"), "desconto"));
		projectionList.add(Projections.alias(Projections.property("produto.nome"), "nomeProduto"));
		projectionList.add(Projections.alias(Projections.property("usuario.nome"), "nomeUsuario"));
		projectionList.add(Projections.alias(Projections.property("dataAlteracao"), "dataAlteracao"));
		projectionList.add(Projections.alias(Projections.property("produto.codigo"), "codigoProduto"));
		projectionList.add(Projections.alias(Projections.property("produtoEdicao.numeroEdicao"), "numeroEdicao"));

		return projectionList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer buscarQuantidadeTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {

		Criteria criteria = this.getSession().createCriteria(DescontoProduto.class);

		criteria.setProjection(Projections.rowCount());

		return ((Long) criteria.uniqueResult()).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<CotaDescontoProdutoDTO> obterCotasDoTipoDescontoProduto(Long idDescontoProduto, Ordenacao ordenacao) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cota.numeroCota as numeroCota, ");
		hql.append(" case when cota.pessoa.nome is not null then ");
		hql.append(" cota.pessoa.nome ");
		hql.append(" else ");
		hql.append(" cota.pessoa.razaoSocial ");
		hql.append(" end ");
		hql.append(" as nome ");
		hql.append(" from DescontoProduto as descontoProduto ");
		hql.append(" inner join descontoProduto.cotas as cota ");
		hql.append(" where descontoProduto.id = :idDescontoProduto ");
		hql.append(" order by cota.numeroCota ");
		hql.append(ordenacao.getOrdenacao() == null ? "" : ordenacao.getOrdenacao());

		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idDescontoProduto", idDescontoProduto);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDescontoProdutoDTO.class));
		
		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDescontoProdutoDTO> obterTiposDescontoProdutoPorCota(Long idCota) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select produtoEdicao.codigo as codigoProduto, ");
		hql.append(" produtoEdicao.nomeComercial as nomeProduto, ");
		hql.append(" produtoEdicao.numeroEdicao as numeroEdicao, ");
		hql.append(" descontoProduto.desconto as desconto, ");
		hql.append(" descontoProduto.dataAlteracao as dataAlteracao, ");
		hql.append(" usuario.nome as nomeUsuario ");
		hql.append(" from DescontoProduto as descontoProduto ");
		hql.append(" join descontoProduto.produtoEdicao as produtoEdicao ");
		hql.append(" join descontoProduto.cotas as cota ");
		hql.append(" join descontoProduto.usuario as usuario ");
		
		if (idCota!=null){
		    hql.append(" where cota.id = :idCota ");
		}
		
		hql.append(" order by descontoProduto.dataAlteracao ");

		Query query = getSession().createQuery(hql.toString());
		
		if (idCota!=null){
		    query.setParameter("idCota", idCota);
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(TipoDescontoProdutoDTO.class));
		
		return query.list();
	}
}
