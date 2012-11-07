package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaDescontoProdutoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
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
	public List<TipoDescontoProdutoDTO> buscarTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {
		
		List<TipoDescontoProdutoDTO> descontosProduto = new ArrayList<TipoDescontoProdutoDTO>();
		
		//List<ProdutoEdicao> produtosEdicao 
		descontosProduto = produtoEdicaoRepository.obterProdutosEdicoesPorCodigoProdutoComDesconto(filtro.getCodigoProduto());
		
		/*for(ProdutoEdicao pe : produtosEdicao) {
			TipoDescontoProdutoDTO tdp = new TipoDescontoProdutoDTO();
			tdp.setCodigoProduto(filtro.getCodigoProduto());
			tdp.setDataAlteracao(new Date());
			tdp.setDesconto(pe.getDesconto());
			tdp.setIdTipoDesconto(3L);
			tdp.setNomeProduto(pe.getProduto().getNome());
			tdp.setNomeUsuario("Anonimo");
			tdp.setNumeroEdicao(pe.getNumeroEdicao());
			
			descontosProduto.add(tdp);
		}*/
		
		return descontosProduto;
		
		/*
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

		return criteria.list();*/
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
	public List<TipoDescontoProdutoDTO> obterTiposDescontoProdutoPorCota(Long idCota, String sortorder, String sortname) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select produtoEdicao.produto.codigo as codigoProduto, ");
		hql.append(" produtoEdicao.produto.nome as nomeProduto, ");
		hql.append(" produtoEdicao.numeroEdicao as numeroEdicao, ");
		hql.append(" descontoProduto.desconto as desconto, ");
		hql.append(" descontoProduto.dataAlteracao as dataAlteracao, ");
		hql.append(" usuario.nome as nomeUsuario ");
		hql.append(" from DescontoProduto as descontoProduto ");
		hql.append(" join descontoProduto.cotas as cota ");
		hql.append(" join descontoProduto.usuario as usuario ");
		hql.append(" join descontoProduto.produtoEdicao as produtoEdicao ");
		
		if (idCota!=null){
		    hql.append(" where cota.id = :idCota ");
		}
		
		if (sortname != null && !sortname.isEmpty()) { 
		
			hql.append(" order by ");
			hql.append(sortname);
			hql.append(" ");
			hql.append(sortorder != null ? sortorder : "");
		}

		Query query = getSession().createQuery(hql.toString());
		
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
