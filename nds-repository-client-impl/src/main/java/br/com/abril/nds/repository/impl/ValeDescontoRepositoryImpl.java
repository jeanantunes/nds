package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.FiltroValeDescontoVO;
import br.com.abril.nds.client.vo.ValeDescontoVO;
import br.com.abril.nds.client.vo.ValeDescontoVO.PublicacaoCuponada;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.ValeDesconto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ValeDescontoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ValeDescontoRepositoryImpl extends AbstractRepositoryModel<ValeDesconto, Long>
		implements ValeDescontoRepository {
	
	public ValeDescontoRepositoryImpl() {
		super(ValeDesconto.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValeDescontoVO obterPorId(Long id) {
		
		Criteria criteria = this.createValeDescontoVOCriteria();
		
		criteria.add(Restrictions.eq("id", id));
		
		return (ValeDescontoVO) criteria.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ValeDescontoVO> obterPorFiltro(FiltroValeDescontoVO filtroValeDesconto) {
		
		Criteria criteria = this.getSession().createCriteria(ValeDesconto.class);
		
		criteria.createAlias("produto", "produto");
		criteria.createAlias("produtosAplicacao", "publicacoesCuponadas", JoinType.LEFT_OUTER_JOIN);
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("id"), "id");
		projectionList.add(Projections.property("produto.codigo"), "codigo");
		projectionList.add(Projections.property("produto.nome"), "nome");
		projectionList.add(Projections.property("numeroEdicao"), "numeroEdicao");
		projectionList.add(Projections.groupProperty("id"));
		
		criteria.setProjection(projectionList);
		
		this.setParamsConsultaObterPorFiltro(filtroValeDesconto, criteria);
		
		this.configPaginacao(criteria, filtroValeDesconto.getPaginacaoVO());

		return criteria.setResultTransformer(Transformers.aliasToBean(ValeDescontoVO.class)).list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long obterCountPesquisaPorFiltro(FiltroValeDescontoVO filtroValeDesconto) {
		
		Criteria criteria = this.getSession().createCriteria(ValeDesconto.class);
		
		criteria.createAlias("produto", "produto");
		criteria.createAlias("produtosAplicacao", "publicacoesCuponadas", JoinType.LEFT_OUTER_JOIN);

		criteria.setProjection(
			Projections.projectionList().add(Projections.countDistinct("id"))
		);
		
		this.setParamsConsultaObterPorFiltro(filtroValeDesconto, criteria);
		
		return (Long) criteria.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValeDescontoVO obterPorCodigo(String codigo) {

		Criteria criteria = this.createValeDescontoVOCriteria();
		
		criteria.add(Restrictions.eq("produto.codigo", codigo));
		criteria.setMaxResults(1);

		return (ValeDescontoVO) criteria.uniqueResult();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValeDescontoVO obterUltimaEdicao(String codigo) {

		DetachedCriteria subCriteria = DetachedCriteria.forClass(ValeDesconto.class);
		
		subCriteria.createAlias("produto", "produto");
		subCriteria.add(Restrictions.eq("produto.codigo", codigo));
		subCriteria.setProjection(Projections.max("numeroEdicao"));
		
		Criteria criteria = this.createValeDescontoVOCriteria();
		
		criteria.add(Restrictions.eq("produto.codigo", codigo));
		criteria.add(Subqueries.propertyEq("numeroEdicao", subCriteria));	
		
		return (ValeDescontoVO) criteria.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PublicacaoCuponada> obterPublicacoesCuponadas(Long idValeDesconto) {

		Criteria criteria = this.getSession().createCriteria(ValeDesconto.class);

		criteria.createAlias("produtosAplicacao", "publicacaoCuponada");
		criteria.createAlias("publicacaoCuponada.produto", "produtoPublicacao");
		criteria.createAlias("publicacaoCuponada.lancamentos", "lancamento");
		
		criteria.setProjection(
			Projections.projectionList().add(Projections.property("produtoPublicacao.codigo"), "codigo")
										.add(Projections.property("produtoPublicacao.nome"), "nome")
										.add(Projections.property("lancamento.dataRecolhimentoDistribuidor"), "dataRecolhimento")
										.add(Projections.property("lancamento.status"), "situacao")
										.add(Projections.property("publicacaoCuponada.numeroEdicao"), "numeroEdicao")
										.add(Projections.property("publicacaoCuponada.id"), "id")
		);

		criteria.add(Restrictions.eq("id", idValeDesconto));

		criteria.setResultTransformer(Transformers.aliasToBean(PublicacaoCuponada.class));

		return criteria.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ValeDesconto> obterPorNome(String nome) {

		Criteria criteria = this.getSession().createCriteria(ValeDesconto.class);
		
		criteria.add(Restrictions.eq("produto.nome", nome));
		
		return criteria.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ValeDescontoVO> obterPorCodigoOuNome(String filtro) {

		Criteria criteria = this.createValeDescontoVOCriteria();

		criteria.add(Restrictions.disjunction()
				.add(Restrictions.ilike("produto.nome", filtro, MatchMode.ANYWHERE))
				.add(Restrictions.ilike("produto.codigo", filtro, MatchMode.START))
		);	

		return criteria.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PublicacaoCuponada obterCuponadasPorCodigoEdicao(String codigo, Long numeroEdicao) {

		Criteria criteria = this.getSession().createCriteria(ProdutoEdicao.class);

		criteria.createAlias("produto", "produtoPublicacao");
		criteria.createAlias("lancamentos", "lancamento");
		
		criteria.setProjection(
			Projections.projectionList().add(Projections.property("produtoPublicacao.codigo"), "codigo")
										.add(Projections.property("produtoPublicacao.nome"), "nome")
										.add(Projections.property("lancamento.dataRecolhimentoDistribuidor"), "dataRecolhimento")
										.add(Projections.property("lancamento.status"), "situacao")
										.add(Projections.property("numeroEdicao"), "numeroEdicao")
										.add(Projections.property("id"), "id")
		);

		criteria.add(Restrictions.eq("produtoPublicacao.codigo", codigo));
		criteria.add(Restrictions.eq("numeroEdicao", numeroEdicao));

		criteria.setResultTransformer(Transformers.aliasToBean(PublicacaoCuponada.class));

		return (PublicacaoCuponada) criteria.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PublicacaoCuponada> obterCuponadasPorCodigoOuNome(String filtro) {

		Criteria criteria = this.getSession().createCriteria(ValeDesconto.class);
		
		criteria.createAlias("produtosAplicacao", "publicacaoCuponada");
		criteria.createAlias("publicacaoCuponada.produto", "produtoPublicacao");

		criteria.setProjection(
			Projections.projectionList().add(Projections.property("produtoPublicacao.codigo"), "codigo")
										.add(Projections.property("produtoPublicacao.nome"), "nome")
										.add(Projections.property("publicacaoCuponada.numeroEdicao"), "numeroEdicao")
										.add(Projections.property("publicacaoCuponada.id"), "id")
		);

		criteria.add(Restrictions.disjunction()
				.add(Restrictions.ilike("produtoPublicacao.nome", filtro, MatchMode.ANYWHERE))
				.add(Restrictions.ilike("produtoPublicacao.codigo", filtro, MatchMode.START))
		);
		
		criteria.setResultTransformer(Transformers.aliasToBean(PublicacaoCuponada.class));
		
		return criteria.list();
	}

	private void setParamsConsultaObterPorFiltro(FiltroValeDescontoVO filtroValeDesconto, Criteria criteria) {
		
		HashMap<String, Object> params  = new HashMap<String, Object>();
		
		if (filtroValeDesconto.getCodigo() != null) {
			params.put("produto.codigo", filtroValeDesconto.getCodigo());
		}
		
		if (filtroValeDesconto.getNome() != null) {
			params.put("produto.nome", filtroValeDesconto.getNome());
		}
		
		if (filtroValeDesconto.getNumeroEdicao() != null) {
			params.put("numeroEdicao", filtroValeDesconto.getNumeroEdicao());
		}
		
		if (filtroValeDesconto.getEdicaoCuponada() != null) {
			params.put("publicacoesCuponadas.id", filtroValeDesconto.getEdicaoCuponada());
		}
		
		Iterator<Entry<String, Object>> paramsIterator = params.entrySet().iterator(); 
		
		while (paramsIterator.hasNext()) {

			Entry<String, Object> entry = paramsIterator.next();
			
			criteria.add(Restrictions.eq(
				entry.getKey(), 
				entry.getValue())
			);
		}
	}
	
	private void configPaginacao(Criteria criteria, PaginacaoVO paginacao) {
		
		if (paginacao == null) {
			return;
		}
		
		if (paginacao.getSortColumn() != null && !paginacao.getSortColumn().isEmpty()) {
			
			switch (paginacao.getOrdenacao()) {
			case DESC:
				criteria.addOrder(Order.desc(paginacao.getSortColumn()));
				break;
			default:
				criteria.addOrder(Order.asc(paginacao.getSortColumn()));
				break;
			}
		}
		
		if (paginacao.getPosicaoInicial() != null) {
			
			criteria.setFirstResult(paginacao.getPosicaoInicial());
		}
		
		if (paginacao.getQtdResultadosPorPagina() != null) {
			
			criteria.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}
	}
	
	private Criteria createValeDescontoVOCriteria() {
		
		Criteria criteria = this.getSession().createCriteria(ValeDesconto.class);

		criteria.createAlias("produto", "produto");
		criteria.createAlias("produto.editor", "editor");
		criteria.createAlias("produto.fornecedores", "fornecedor");
		criteria.createAlias("lancamentos", "lancamento", JoinType.LEFT_OUTER_JOIN);

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("id"), "id");
		projectionList.add(Projections.property("produto.id"), "idProduto");
		projectionList.add(Projections.property("produto.codigo"), "codigo");
		projectionList.add(Projections.property("lancamento.id"), "idLancamento");
		projectionList.add(Projections.property("produto.nome"), "nome");
		projectionList.add(Projections.property("numeroEdicao"), "numeroEdicao");
		projectionList.add(Projections.property("lancamento.status"), "situacao");
		projectionList.add(Projections.property("lancamento.dataRecolhimentoPrevista"), "dataRecolhimentoPrevista");
		projectionList.add(Projections.property("lancamento.dataRecolhimentoDistribuidor"), "dataRecolhimentoReal");
		projectionList.add(Projections.property("fornecedor.id"), "idFornecedor");
		projectionList.add(Projections.property("editor.id"), "idEditor");
		projectionList.add(Projections.property("precoVenda"), "valor");
		projectionList.add(Projections.property("codigoDeBarras"), "codigoBarras");
		projectionList.add(Projections.property("historico"), "historico");
		projectionList.add(Projections.property("vincularRecolhimentoProdutosCuponados"), "vincularRecolhimento");
		
		criteria.setProjection(projectionList);

		criteria.setResultTransformer(Transformers.aliasToBean(ValeDescontoVO.class));
		
		return criteria;
	}
}
