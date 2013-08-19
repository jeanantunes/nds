package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.financeiro.DescontoProximosLancamentos;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;
import br.com.abril.nds.repository.DescontoProximosLancamentosRepository;
import br.com.abril.nds.repository.DescontoRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do produto edição
 * 
 * @author Discover Technology
 */

@Repository
public class DescontoProdutoEdicaoRepositoryImpl extends AbstractRepositoryModel<DescontoProdutoEdicao, Long> implements DescontoProdutoEdicaoRepository {
 
	@Autowired
	private DescontoProximosLancamentosRepository descontoProximosLancamentosRepository;
	
	@Autowired
	private DescontoRepository descontoRepository;
	
	private static final int QUINHENTOS = 500;
	
	/**
	 * Construtor padrão.
	 */
	public DescontoProdutoEdicaoRepositoryImpl() {
		
		super(DescontoProdutoEdicao.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DescontoProdutoEdicao buscarDescontoProdutoEdicao(TipoDesconto tipoDesconto,
															 Fornecedor fornecedor, 
															 Cota cota,
															 ProdutoEdicao produto) {
		
		Criteria criteria = getSession().createCriteria(DescontoProdutoEdicao.class);

		if (fornecedor != null) {
		
			criteria.add(Restrictions.eq("fornecedor", fornecedor));
			
		}

		if (cota != null) {
		
			criteria.add(Restrictions.eq("cota", cota));
		}

		if (produto != null) {
		
			criteria.add(Restrictions.eq("produtoEdicao", produto));
		}
		
		if (tipoDesconto != null) {
			
			criteria.add(Restrictions.eq("tipoDesconto", tipoDesconto));
		}
		
		criteria.setMaxResults(1);
		
		return (DescontoProdutoEdicao) criteria.uniqueResult();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontosProdutoEdicao(Fornecedor fornecedor) {

		return obterDescontoProdutoEdicaoCotaFornecedor(fornecedor, null, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontosProdutoEdicao(Cota cota) {
		
		return obterDescontoProdutoEdicaoCotaFornecedor(null, cota, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontosProdutoEdicao(Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoProdutoEdicaoCotaFornecedor(fornecedor, cota, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontosProdutoEdicao(ProdutoEdicao produtoEdicao) {
		
		return obterDescontoProdutoEdicaoCotaFornecedor(null, null, produtoEdicao,null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DescontoProdutoEdicao> obterDescontoProdutoEdicaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor) {
		
		return obterDescontoSemTipoDesconto(tipoDesconto, fornecedor, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DescontoProdutoEdicao> obterDescontoProdutoEdicaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoSemTipoDesconto(tipoDesconto, fornecedor, cota);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontoProdutoEdicao(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoProdutoEdicaoCotaFornecedor(fornecedor, cota, null,tipoDesconto);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontoProdutoEdicao(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota,ProdutoEdicao produtoEdicao) {
		
		return obterDescontoProdutoEdicaoCotaFornecedor(fornecedor, cota, produtoEdicao,tipoDesconto);
	}
	
	@SuppressWarnings("unchecked")
	private Set<DescontoProdutoEdicao> obterDescontoProdutoEdicaoCotaFornecedor(Fornecedor fornecedor, Cota cota, ProdutoEdicao produtoEdicao,TipoDesconto tipoDesconto){
		
		Criteria criteria = getSession().createCriteria(DescontoProdutoEdicao.class);
		
		if (fornecedor != null) {
			
			criteria.add(Restrictions.eq("fornecedor", fornecedor));
		}
		
		if (cota != null) {
			
			criteria.add(Restrictions.eq("cota", cota));
		}
		
		if (produtoEdicao != null) {
			
			criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao));
		}
		
		if(tipoDesconto!= null){
			
			criteria.add(Restrictions.eq("tipoDesconto", tipoDesconto));
		}
		
		criteria.setFetchMode("cota", FetchMode.JOIN);
		
		return new HashSet<DescontoProdutoEdicao>(criteria.list());
	}
	
	@SuppressWarnings("unchecked")
	private List<DescontoProdutoEdicao> obterDescontoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota){
		
		Criteria criteria = getSession().createCriteria(DescontoProdutoEdicao.class);
		
		criteria.add(Restrictions.eq("fornecedor", fornecedor));

		if (cota!= null) {
			
			criteria.add(Restrictions.eq("cota", cota));
		}
		
		criteria.add(Restrictions.not(Restrictions.eq("tipoDesconto", tipoDesconto)));
	
		return criteria.list();
	}

    /**
     * {@inheritDoc}
     */
	@Override
    public Desconto obterDescontoPorCotaProdutoEdicao(Lancamento lancamento,
            Cota cota, ProdutoEdicao produtoEdicao) {
        
		//TODO: Implementar a prioridade de desconto predoominante 
		Query query = null;
		BigInteger descontoId;
		Desconto desconto = null;
		
		//Obtem o desconto do ProdutoEdicao baseado em lancamento futuro
		descontoId = obterDescontoCotaProdutoEdicaoLancamentosFuturos(cota, lancamento);
		
		if(descontoId != null) return descontoRepository.buscarPorId(descontoId.longValue());
		
		//Obtem o desconto do ProdutoEdicao baseado em excessoes 
		query = obterDescontoCotaProdutoEdicaoExcessoes(cota, produtoEdicao);
		descontoId = (BigInteger) query.uniqueResult();
		
		if(descontoId != null) return descontoRepository.buscarPorId(descontoId.longValue());
				
		//Obtem o desconto do Produto baseado em excessoes 
		query = obterDescontoCotaProdutoExcessoes(cota, produtoEdicao);
		descontoId = (BigInteger) query.uniqueResult();
		
		if(descontoId != null) return descontoRepository.buscarPorId(descontoId.longValue());
		
		//Obtem o desconto do ProdutoEdicao 
		query = obterDescontoProdutoEdicao(produtoEdicao);
		descontoId = (BigInteger) query.uniqueResult();
		
		if(descontoId != null) return descontoRepository.buscarPorId(descontoId.longValue());
		
		//Obtem o desconto do Produto 
		query = obterDescontoProduto(produtoEdicao);
		descontoId = (BigInteger) query.uniqueResult();
		
		if(descontoId != null) return descontoRepository.buscarPorId(descontoId.longValue());
		
		//Obtem o desconto da Cota-Fornecedor 
		query = obterDescontoEspecifico(cota, produtoEdicao);
		descontoId = (BigInteger) query.uniqueResult();
		
		if(descontoId != null) return descontoRepository.buscarPorId(descontoId.longValue());
		
		//Obtem o desconto do Fornecedor 
		query = obterDescontoGeral(produtoEdicao.getProduto().getFornecedor());
		Long descId = (Long) query.uniqueResult();
		descontoId = descId != null ? new BigInteger(descId.toString()) : null;
		
		if(descontoId != null) return descontoRepository.buscarPorId(descontoId.longValue());
		
        return desconto;
        
    }

	private BigInteger obterDescontoCotaProdutoEdicaoLancamentosFuturos(Cota cota, Lancamento lancamento) {
		
		if(lancamento == null || cota == null ) {
			return null;
		}
		
		DescontoProximosLancamentos descontoProximosLancamentos = this.descontoProximosLancamentosRepository.
				obterDescontoProximosLancamentosPor(lancamento.getProdutoEdicao().getProduto().getId(), 
						lancamento.getDataLancamentoPrevista());
		
		if(descontoProximosLancamentos != null && descontoProximosLancamentos.isAplicadoATodasAsCotas()) {
			return new BigInteger(descontoProximosLancamentos.getDesconto().getId().toString());
		}

		if (descontoProximosLancamentos != null) {	

			for(Cota c : descontoProximosLancamentos.getCotas()) {
				
				if(c.getId() == cota.getId()) {
					
					Integer quantidade = descontoProximosLancamentos.getQuantidadeProximosLancamaentos();

					descontoProximosLancamentos.setQuantidadeProximosLancamaentos(--quantidade);
					
					descontoProximosLancamentosRepository.alterar(descontoProximosLancamentos);
					
					return new BigInteger(descontoProximosLancamentos.getDesconto().getId().toString());
					
				}
				
			}
			
		}
		
		return null;
	}

	private Query obterDescontoCotaProdutoEdicaoExcessoes(Cota cota, ProdutoEdicao produtoEdicao) {
		
		boolean indWhere = false;
		
		StringBuilder hql = new StringBuilder("select ")
			.append(" vdcfpe.desconto_id as idDesconto ")
		    .append("from VIEW_DESCONTO_COTA_FORNECEDOR_PRODUTOS_EDICOES as vdcfpe ");
		
		if (cota != null) {
		
			hql.append(" where vdcfpe.cota_id = :idCota ");
		
			indWhere = true;
		}

		if (produtoEdicao != null) {

			hql.append(indWhere ? " and " : " where ")
			   .append(" vdcfpe.fornecedor_id = :idFornecedor ")
			   .append(" and vdcfpe.produto_id = :idProduto ")
			   .append(" and vdcfpe.produto_edicao_id = :idProdutoEdicao ");
		}

		Query query = getSession().createSQLQuery(hql.toString());
		
		if (cota != null) {
			
			query.setParameter("idCota", cota.getId());
		}
		
		if (produtoEdicao != null) {
			
			query.setParameter("idProdutoEdicao", produtoEdicao.getId());
			query.setParameter("idProduto", produtoEdicao.getProduto().getId());
			query.setParameter("idFornecedor", produtoEdicao.getProduto().getFornecedor().getId());
		}
        
		return query;
	}
	
	private Query obterDescontoCotaProdutoExcessoes(Cota cota, ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("select ")
			.append(" vdcfpe.desconto_id as idDesconto ")
		    .append("from VIEW_DESCONTO_COTA_FORNECEDOR_PRODUTOS_EDICOES as vdcfpe ") 
		    .append("where vdcfpe.produto_edicao_id is null ");
		
		if (cota != null) {
			
			hql.append(" and vdcfpe.cota_id = :idCota ");
		}
		
		if (produtoEdicao != null) {

			hql.append(" and vdcfpe.fornecedor_id = :idFornecedor ")
		       .append(" and vdcfpe.produto_id = :idProduto ");
		}
		
		Query query = getSession().createSQLQuery(hql.toString());

		if (cota != null) {
			
			query.setParameter("idCota", cota.getId());
		}
		
		if (produtoEdicao != null) {
		
			query.setParameter("idFornecedor", produtoEdicao.getProduto().getFornecedor().getId());
	        query.setParameter("idProduto", produtoEdicao.getProduto().getId());
		}
        
		return query;
	}
	
	private Query obterDescontoEspecifico(Cota cota, ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("select ")
			.append(" vdcfpe.desconto_id as idDesconto ")
		    .append("from VIEW_DESCONTO_COTA_FORNECEDOR_PRODUTOS_EDICOES as vdcfpe ")
			.append(" where vdcfpe.produto_id is null ")
			.append(" and vdcfpe.produto_edicao_id is null ");
		
		if (cota != null) {
			
			hql.append(" and vdcfpe.cota_id = :idCota ");
		}
		
		if (produtoEdicao != null) {
			
			hql.append(" and vdcfpe.fornecedor_id = :idFornecedor ");
		}

		Query query = getSession().createSQLQuery(hql.toString());
		
		if (cota != null) {
			
			query.setParameter("idCota", cota.getId());
		}

		if (produtoEdicao != null) {
		
			query.setParameter("idFornecedor", produtoEdicao.getProduto().getFornecedor().getId());
		}
		
		return query;
	}
	
	private Query obterDescontoGeral(Fornecedor fornecedor) {
		
		StringBuilder hql = new StringBuilder("select ")
			.append(" d.id ")
		    .append("from Fornecedor f join f.desconto d  ");
		
		if (fornecedor != null) {
			 
		    hql.append(" where f.id = :idFornecedor ");
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		if (fornecedor != null) {
			 
			query.setParameter("idFornecedor", fornecedor.getId());
		}

		return query;
	}
	
	private Query obterDescontoProdutoEdicao(ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("select ")
			.append(" vdpe.desconto_id as idDesconto ")
		    .append(" from VIEW_DESCONTO_PRODUTOS_EDICOES as vdpe ");
		
		if (produtoEdicao != null) {
		
			hql.append(" where vdpe.codigo_produto = :codigoProduto ")
			   .append(" and vdpe.numero_edicao = :numeroEdicao ");
		}

		Query query = getSession().createSQLQuery(hql.toString());

		if (produtoEdicao != null) {
		
			query.setParameter("codigoProduto", produtoEdicao.getProduto().getCodigo());
			query.setParameter("numeroEdicao", produtoEdicao.getNumeroEdicao());
		}

		return query;
	}
	
	private Query obterDescontoProduto(ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("select ")
			.append(" vdpe.desconto_id as idDesconto ")
		    .append(" from VIEW_DESCONTO_PRODUTOS_EDICOES as vdpe ")
		    .append(" where vdpe.numero_edicao is null ");
		
		if (produtoEdicao != null) {
			
			hql.append(" and vdpe.codigo_produto = :codigoProduto ");
		}
		
		Query query = getSession().createSQLQuery(hql.toString());

		if (produtoEdicao != null) {
		
			query.setParameter("codigoProduto", produtoEdicao.getProduto().getCodigo());
		}

		return query;
	}

	@Override
	public void salvarListaDescontoProdutoEdicao(List<DescontoProdutoEdicao> lista) {
		
		int i = 0;
		
		for (DescontoProdutoEdicao descontoProdutoEdicao : lista) {
			this.merge(descontoProdutoEdicao);
			i++;
			if (i % QUINHENTOS == 0) {
				getSession().flush();
				getSession().clear();
			}
		}
		
		getSession().flush();
		getSession().clear();
		
	}

}
