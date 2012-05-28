package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaProdutoDTO;
import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
@Repository
public class ProdutoEdicaoRepositoryImpl extends AbstractRepository<ProdutoEdicao, Long> 
										 implements ProdutoEdicaoRepository {

	/**
	 * Construtor padrão.
	 */
	public ProdutoEdicaoRepositoryImpl() {
		super(ProdutoEdicao.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ProdutoEdicaoRepository#obterPercentualComissionamento(java.lang.Long, java.lang.Integer, java.lang.Long)
	 */
	public BigDecimal obterFatorDesconto(Long idProdutoEdicao, Integer numeroCota, Long idDistribuidor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select case when ( pe.desconto is not null ) then pe.desconto else ");
		
		hql.append(" ( case when ( ct.fatorDesconto is not null ) then ct.fatorDesconto  else  ");
		
		hql.append(" ( case when ( distribuidor.fatorDesconto is not null ) then distribuidor.fatorDesconto else 0 end ) end  ");
		
		hql.append(" ) end ");
		
		hql.append(" from ProdutoEdicao pe, Cota ct, Distribuidor distribuidor ");
		
		hql.append(" where ");
		
		hql.append(" ct.numeroCota = :numeroCota and ");

		hql.append(" pe.id = :idProdutoEdicao and ");

		hql.append(" distribuidor.id = :idDistribuidor ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("idDistribuidor", idDistribuidor);
		
		return (BigDecimal) query.uniqueResult();
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterProdutoEdicaoPorNomeProduto(String nomeProduto) {
		String hql = "from ProdutoEdicao produtoEdicao " 
				   + " join fetch produtoEdicao.produto " 
				   + " where upper(produtoEdicao.produto.nome) like upper(:nomeProduto)";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("nomeProduto", nomeProduto + "%");
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterListaProdutoEdicao(Produto produto, ProdutoEdicao produtoEdicao ) {
		
		String codigoProduto = produto.getCodigo();
		Long numeroEdicao = produtoEdicao.getNumeroEdicao();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select p from ProdutoEdicao p ");
		
		hql.append(" where ");
		
		hql.append(" p.numeroEdicao = :numeroEdicao and ");
		
		hql.append(" p.produto.codigo = :codigoProduto ");
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("codigoProduto",  codigoProduto );

		query.setParameter("numeroEdicao", numeroEdicao);
		
		return query.list();
	}
	
	
	@Override
	public FuroProdutoDTO obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
			String codigo, String nomeProduto, Long edicao, Date dataLancamento) {
		StringBuilder hql = new StringBuilder();
		hql.append("select new ")
		   .append(FuroProdutoDTO.class.getCanonicalName())
		   .append("(produto.codigo, produto.nome, produtoEdicao.numeroEdicao, estudo.qtdeReparte, lancamento.reparte, ")
		   .append("   lancamento.dataLancamentoDistribuidor, lancamento.id, produtoEdicao.id)")
		   .append(" from Produto produto, ProdutoEdicao produtoEdicao, ")
		   .append("      Lancamento lancamento ")
		   .append(" left join lancamento.estudo as estudo ")
		   .append(" where produtoEdicao.produto.id              = produto.id ")
		   .append(" and   produtoEdicao.id                      = lancamento.produtoEdicao.id ")
		   .append(" and   produto.codigo                        = :codigo ")
		   .append(" and   produtoEdicao.numeroEdicao            = :edicao")
		   .append(" and   lancamento.dataLancamentoDistribuidor = :dataLancamento ")
		   .append(" and   lancamento.status                     != :statusFuro");
		
		if (nomeProduto != null && !nomeProduto.isEmpty()){
			hql.append(" and produto.nome = :nomeProduto ");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("codigo", codigo);
		query.setParameter("edicao", edicao);
		query.setParameter("dataLancamento", dataLancamento);
		query.setParameter("statusFuro", StatusLancamento.FURO);
		
		if (nomeProduto != null && !nomeProduto.isEmpty()){
			query.setParameter("nomeProduto", nomeProduto);
		}
		
		query.setMaxResults(1);
		
		return (FuroProdutoDTO) query.uniqueResult();
	}
	
	@Override
	public ProdutoEdicao obterProdutoEdicaoPorCodProdutoNumEdicao(String codigoProduto,
																  Long numeroEdicao) {
		
		String hql = "from ProdutoEdicao produtoEdicao " 
				   + " join fetch produtoEdicao.produto " 
				   + " where produtoEdicao.produto.codigo = :codigoProduto "
				   + " and 	 produtoEdicao.numeroEdicao   = :numeroEdicao";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("codigoProduto", codigoProduto);
		query.setParameter("numeroEdicao", numeroEdicao);
		
		query.setMaxResults(1);
		
		return (ProdutoEdicao) query.uniqueResult();
	}
	
	@Override
	public ProdutoEdicao obterProdutoEdicaoPorCodigoBarra(String codigoBarra){
		
		Criteria criteria = this.getSession().createCriteria(ProdutoEdicao.class);
		criteria.add(Restrictions.eq("codigoDeBarras", codigoBarra));
		
		criteria.setMaxResults(1);
		
		return (ProdutoEdicao) criteria.uniqueResult();
	}
	

	@SuppressWarnings("unchecked")
	public List<ProdutoEdicao> obterProdutosEdicaoPorCodigoProduto(String codigoProduto) {
		
		Criteria criteria = super.getSession().createCriteria(ProdutoEdicao.class);
		
		criteria.createAlias("produto", "produto");
		
		criteria.add(Restrictions.eq("produto.codigo", codigoProduto));
				
		return  criteria.list();
				
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterProdutoPorCodigoNome(String codigoNomeProduto) {
		
		StringBuilder hql = new StringBuilder("select pe ");
		hql.append(" from ProdutoEdicao pe ")
		   .append(" where pe.produto.nome like :nomeProduto ")
		   .append(" or pe.produto.codigo = :codigoProduto ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("nomeProduto", "%" + codigoNomeProduto + "%");
		query.setParameter("codigoProduto", codigoNomeProduto);
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> obterProdutosEdicaoPorId(Set<Long> idsProdutoEdicao) {
		
		String hql = "from ProdutoEdicao produtoEdicao " 
				   + " where produtoEdicao.id in (:idsProdutoEdicao)";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameterList("idsProdutoEdicao", idsProdutoEdicao);
		
		return query.list();
	}
	
	public ProdutoEdicao obterProdutoEdicaoPorSequenciaMatriz(Integer sequenciaMatriz) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select lancamento.produtoEdicao from Lancamento lancamento ");
		
		hql.append(" where lancamento.sequenciaMatriz = :sequenciaMatriz ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("sequenciaMatriz", sequenciaMatriz);
		
		return (ProdutoEdicao) query.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ProdutoEdicaoRepository#obterCodigoMatrizPorProdutoEdicao(java.lang.Long)
	 */
	public Integer obterCodigoMatrizPorProdutoEdicao(Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select lancamento.sequenciaMatriz from Lancamento lancamento ");
		
		hql.append(" where lancamento.produtoEdicao.id = :idProdutoEdicao ");

		hql.append(" and lancamento.dataLancamentoDistribuidor = ");

		hql.append(" ( ");
		
		hql.append(" select max(lancamento.dataRecolhimentoDistribuidor) from Lancamento lancamento  ");
		
		hql.append(" where lancamento.produtoEdicao.id = :idProdutoEdicao   ");
		
		hql.append(" ) ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		return (Integer) query.uniqueResult();
	}

	@Override
	public ProdutoEdicao obterProdutoEdicaoPorSM(Long sm) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Pesquisa as Edições já cadastradas.<br>
	 * Possui como opções de filtro:<br>
	 * <ul>
	 * <li>Código do Produto;</li>
	 * <li>Nome do Produto;</li>
	 * <li>Data de Lançamento;</li>
	 * <li>Situação do Lançamento;</li>
	 * <li>Código de Barra da Edição;</li>
	 * <li>Contém brinde;</li>
	 * </ul>
	 * 
	 * @param dto
	 * @param sortorder
	 * @param sortname
	 * @param initialResult
	 * @param maxResults
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicaoDTO> pesquisarEdicoes(ProdutoEdicaoDTO dto,
			String sortorder, String sortname, int initialResult, int maxResults) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT pr.codigo as codigoProduto, pe.nomeComercial as nomeProduto, ");
		hql.append("        pe.numeroEdicao as numeroEdicao, jr.razaoSocial as nomeFornecedor, ");
		hql.append("        ln.tipoLancamento as tipoLancamento, ln.status as status, ");
		hql.append("        pe.possuiBrinde as possuiBrinde");
		hql.append("   FROM ProdutoEdicao pe ");
		hql.append("        JOIN pe.produto pr ");
		hql.append("        JOIN pr.fornecedores fr JOIN fr.juridica jr ");
		hql.append("        JOIN pe.lancamentos ln ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoDTO.class));
		
		//query.setMaxResults(initialResult);
		//query.setFirstResult(maxResults);
		
//		//hql.append(" FROM ProdutoEdicao pe JOIN FETCH pe.produto LEFT OUTER JOIN FETCH pe.lancamentos as la WHERE 1=1 ");
//		Criteria criteria =  getSession().createCriteria(ProdutoEdicao.class);
//		
//		// Parâmetros opcionais da pesquisa:
//		
//		if (dto.getCodigoProduto() != null && dto.getCodigoProduto().trim().length() > 0) {
//			//hql.append("  AND UPPER(pe.produto.codigo) LIKE UPPER(:codigoProduto) ");
//			criteria.add(Restrictions.ilike("produto.codigoProduto",
//					dto.getCodigoProduto(), MatchMode.ANYWHERE));
//		}
//		if (dto.getNomeProduto() != null && dto.getNomeProduto().trim().length() > 0) {
//			//hql.append("  AND UPPER(pe.produto.nome) LIKE UPPER(:nomeProduto) ");
//			criteria.add(Restrictions.ilike("produto.nome",
//					dto.getNomeProduto(), MatchMode.ANYWHERE));
//		}
//		
//		if (dto.getSituacaoLancamento() != null || dto.getDataLancamento() != null) {
//			Criteria cLancamento = criteria.createCriteria("lancamentos");
//			if (dto.getSituacaoLancamento() != null && dto.getSituacaoLancamento().trim().length() > 0) {
//				//hql.append("  AND la.status = :statusLancamento ");
//				cLancamento.add(Restrictions.eq("status", dto.getSituacaoLancamento()));
//			}
//			if (dto.getDataLancamento() != null) {
//				//hql.append("  AND (la.dataLancamentoDistribuidor = :dataLancamento OR la.dataLancamentoPrevista = :dataLancamento) ");
//				cLancamento.add(Restrictions.or(
//						Restrictions.eq("dataLancamentoDistribuidor", dto.getSituacaoLancamento()),
//						Restrictions.eq("dataLancamentoPrevista", dto.getSituacaoLancamento())));
//			}
//		}
//		
//		
//		// 
//		if (dto.getCodigoDeBarras() != null && dto.getCodigoDeBarras().trim().length() > 0) {
//			//hql.append("  AND pe.codigoDeBarras LIKE :codigoDeBarras ");
//			criteria.add(Restrictions.ilike("codigoDeBarras",
//					dto.getCodigoDeBarras(), MatchMode.ANYWHERE));
//		}
//		if (dto.isPossuiBrinde()) {
//			//hql.append("  AND pe.possuiBrindie = :possuiBrinde ");
//			criteria.add(Restrictions.eq("possuiBrinde", Boolean.valueOf(dto.isPossuiBrinde())));
//		}
//		
//
//		// Ordenação:
//		//hql.append(" ORDER BY pe.numeroEdicao :sortorder ");
//		if(Ordenacao.ASC.toString().equals(sortorder)){
//			criteria.addOrder(Order.asc(sortname));
//		}else if(Ordenacao.DESC.toString().equals(sortorder)){
//			criteria.addOrder(Order.desc(sortname));
//		}
//		
//		criteria.setMaxResults(maxResults);
//		criteria.setFirstResult(initialResult);
//		
//		
//		Query query = getSession().createQuery(hql.toString());
//		query.setMaxResults(maxResults);
//		query.setFirstResult(initialResult);
//		
//		// Parâmetros opcionais da pesquisa:
//		if (dto.getCodigoProduto() != null && dto.getCodigoProduto().trim().length() > 0) {
//			query.setString("codigoProduto", dto.getCodigoProduto());
//		}
//		if (dto.getNomeProduto() != null && dto.getNomeProduto().trim().length() > 0) {
//			query.setString("nomeProduto", dto.getNomeProduto());
//		}
//		if (dto.getCodigoDeBarras() != null && dto.getCodigoDeBarras().trim().length() > 0) {
//			query.setString("codigoDeBarras", dto.getCodigoDeBarras());
//		}
//		if (dto.getSituacaoLancamento() != null && dto.getSituacaoLancamento().trim().length() > 0) {
//			query.setString("statusLancamento", dto.getSituacaoLancamento());
//		}
//		if (dto.getDataLancamento() != null) {
//			query.setDate("dataLancamento", dto.getDataLancamento());
//		}
//		if (dto.isPossuiBrinde()) {
//			query.setBoolean("possuiBrinde", dto.isPossuiBrinde());
//		}
//		
//		// Ordenação
//		if (sortname != null && sortname.trim().length() > 0) {
//			
//			query.setString("sortorder", sortorder.toUpperCase());
//			if (sortorder != null && sortorder.trim().length() > 0) {
//				query.setString("sortorder", sortorder.toUpperCase());
//				
//			}
//		}
//		
//		
//		
//		return criteria.list();
		
		return 	query.list();
		//return Collections.<ProdutoEdicao>emptyList();
	}
	
}
