package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
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
            Long idCota, ProdutoEdicao produtoEdicao) {
        
		//TODO: Implementar a prioridade de desconto predominante 
		Query query = null;
		BigInteger descontoId;
		Desconto desconto = null;
		
		//Obtem o desconto do ProdutoEdicao baseado em lancamento futuro
		descontoId = obterDescontoCotaProdutoEdicaoLancamentosFuturos(idCota, lancamento);
		
		if(descontoId != null) return descontoRepository.buscarPorId(descontoId.longValue());
		
		//Obtem o desconto do ProdutoEdicao baseado em excessoes 
		query = obterDescontoCotaProdutoEdicaoExcessoes(idCota, produtoEdicao);
		descontoId = (BigInteger) query.uniqueResult();
		
		if(descontoId != null) return descontoRepository.buscarPorId(descontoId.longValue());
				
		//Obtem o desconto do Produto baseado em excessoes 
		query = obterDescontoCotaProdutoExcessoes(idCota, produtoEdicao);
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
		query = obterDescontoEspecifico(idCota, produtoEdicao);
		descontoId = (BigInteger) query.uniqueResult();
		
		if(descontoId != null) return descontoRepository.buscarPorId(descontoId.longValue());
		
		//Obtem o desconto do Fornecedor 
		query = obterDescontoGeral(produtoEdicao.getProduto().getFornecedor());
		Long descId = (Long) query.uniqueResult();
		descontoId = descId != null ? new BigInteger(descId.toString()) : null;
		
		if(descontoId != null) return descontoRepository.buscarPorId(descontoId.longValue());
		
        return desconto;
        
    }

	private BigInteger obterDescontoCotaProdutoEdicaoLancamentosFuturos(Long idCota, Lancamento lancamento) {
		
		if(lancamento == null || idCota == null ) {
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
				
				if(c.getId().equals(idCota)) {
					
					Integer quantidade = descontoProximosLancamentos.getQuantidadeProximosLancamaentos();

					descontoProximosLancamentos.setQuantidadeProximosLancamaentos(--quantidade);
					
					descontoProximosLancamentosRepository.alterar(descontoProximosLancamentos);
					
					return new BigInteger(descontoProximosLancamentos.getDesconto().getId().toString());
					
				}
				
			}
			
		}
		
		return null;
	}

	private Query obterDescontoCotaProdutoEdicaoExcessoes(Long idCota, ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("SELECT ")
			.append(" vdcfpe.desconto_id AS idDesconto ")
		    .append("FROM VIEW_DESCONTO_COTA_FORNECEDOR_PRODUTOS_EDICOES AS vdcfpe ")
		    .append("WHERE 1 = 1 ");
		
		if (idCota != null) {
		
			hql.append(" AND vdcfpe.cota_id = :idCota ");
		
		}

		if (produtoEdicao != null) {

			hql.append(" AND vdcfpe.fornecedor_id = :idFornecedor ")
			   .append(" AND vdcfpe.produto_id = :idProduto ")
			   .append(" AND vdcfpe.produto_edicao_id = :idProdutoEdicao ");
		}

		Query query = getSession().createSQLQuery(hql.toString());
		
		if (idCota != null) {
			
			query.setParameter("idCota", idCota);
		}
		
		if (produtoEdicao != null) {
			
			query.setParameter("idProdutoEdicao", produtoEdicao.getId());
			query.setParameter("idProduto", produtoEdicao.getProduto().getId());
			query.setParameter("idFornecedor", produtoEdicao.getProduto().getFornecedor().getId());
		}
        
		return query;
	}
	
	private Query obterDescontoCotaProdutoExcessoes(Long idCota, ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("SELECT ")
			.append(" vdcfpe.desconto_id AS idDesconto ")
		    .append(" FROM VIEW_DESCONTO_COTA_FORNECEDOR_PRODUTOS_EDICOES AS vdcfpe ") 
		    .append(" WHERE vdcfpe.produto_edicao_id IS NULL ");
		
		if (idCota != null) {
			
			hql.append(" AND vdcfpe.cota_id = :idCota ");
		}
		
		if (produtoEdicao != null) {

			hql.append(" AND vdcfpe.fornecedor_id = :idFornecedor ")
		       .append(" AND vdcfpe.produto_id = :idProduto ");
		}
		
		Query query = getSession().createSQLQuery(hql.toString());

		if (idCota != null) {
			
			query.setParameter("idCota", idCota);
		}
		
		if (produtoEdicao != null) {
		
			query.setParameter("idFornecedor", produtoEdicao.getProduto().getFornecedor().getId());
	        query.setParameter("idProduto", produtoEdicao.getProduto().getId());
		}
        
		return query;
	}
	
	private Query obterDescontoEspecifico(Long idCota, ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("SELECT ")
			.append(" vdcfpe.desconto_id as idDesconto ")
		    .append(" FROM VIEW_DESCONTO_COTA_FORNECEDOR_PRODUTOS_EDICOES AS vdcfpe ")
			.append(" WHERE vdcfpe.produto_id IS NULL ")
			.append(" AND vdcfpe.produto_edicao_id IS NULL ");
		
		if (idCota != null) {
			
			hql.append(" AND vdcfpe.cota_id = :idCota ");
		}
		
		if (produtoEdicao != null) {
			
			hql.append(" AND vdcfpe.fornecedor_id = :idFornecedor ");
		}

		Query query = getSession().createSQLQuery(hql.toString());
		
		if (idCota != null) {
			
			query.setParameter("idCota", idCota);
		}

		if (produtoEdicao != null) {
		
			query.setParameter("idFornecedor", produtoEdicao.getProduto().getFornecedor().getId());
		}
		
		return query;
	}
	
	private Query obterDescontoGeral(Fornecedor fornecedor) {
		
		StringBuilder hql = new StringBuilder("SELECT ")
			.append(" d.id ")
		    .append(" FROM Fornecedor f JOIN f.desconto d  ");
		
		if (fornecedor != null) {
			 
		    hql.append(" WHERE f.id = :idFornecedor ");
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		if (fornecedor != null) {
			 
			query.setParameter("idFornecedor", fornecedor.getId());
		}

		return query;
	}
	
	private Query obterDescontoProdutoEdicao(ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("SELECT ")
			.append(" vdpe.desconto_id as idDesconto ")
		    .append(" FROM VIEW_DESCONTO_PRODUTOS_EDICOES AS vdpe ");
		
		if (produtoEdicao != null) {
		
			hql.append(" WHERE vdpe.codigo_produto = :codigoProduto ")
			   .append(" AND vdpe.numero_edicao = :numeroEdicao ");
		}

		Query query = getSession().createSQLQuery(hql.toString());

		if (produtoEdicao != null) {
		
			query.setParameter("codigoProduto", produtoEdicao.getProduto().getCodigo());
			query.setParameter("numeroEdicao", produtoEdicao.getNumeroEdicao());
		}

		return query;
	}
	
	private Query obterDescontoProduto(ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("SELECT ")
			.append(" vdpe.desconto_id AS idDesconto ")
		    .append(" FROM VIEW_DESCONTO_PRODUTOS_EDICOES AS vdpe ")
		    .append(" WHERE vdpe.numero_edicao IS NULL ");
		
		if (produtoEdicao != null) {
			
			hql.append(" AND vdpe.codigo_produto = :codigoProduto ");
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

	@SuppressWarnings("unchecked")
	@Override
	public List<DescontoDTO> obterDescontosProdutoEdicao() {

		StringBuilder hql = new StringBuilder("")
			.append(" SELECT cota_id AS cotaId, fornecedor_id AS fornecedorId, editor_id AS editorId, produto_edicao_id AS produtoEdicaoId, produto_id AS produtoId, valor, predominante ")
			.append(" FROM VIEW_DESCONTO_COTA_FORNECEDOR_PRODUTOS_EDICOES as vdcfpe ")
			.append(" UNION ")
			.append(" SELECT null AS cotaId, null AS fornecedorId, null AS editorId, PRODUTO_EDICAO_ID AS produtoEdicaoId, PRODUTO_ID AS produtoId, valor, predominante ")
			.append(" FROM view_desconto_produtos_edicoes ")
			.append(" UNION ")
			.append(" SELECT null AS cotaId, f.id AS fornecedorId, null AS editorId, null AS produtoEdicaoId, null AS produtoId, valor, predominante ")
			.append(" FROM FORNECEDOR f ")
			.append(" INNER JOIN desconto d ON d.id = f.desconto_id ")
			.append(" UNION ")
			.append(" SELECT null AS cotaId, null AS fornecedorId, e.id AS editorId, null AS produtoEdicaoId, null AS produtoId, valor, predominante ")
			.append(" FROM EDITOR e ")
			.append(" INNER JOIN desconto d ON d.id = e.desconto_id ")
			;

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(new AliasToBeanResultTransformer(DescontoDTO.class));
		
		query.addScalar("cotaId", StandardBasicTypes.LONG);
		query.addScalar("produtoEdicaoId", StandardBasicTypes.LONG);
		query.addScalar("produtoId", StandardBasicTypes.LONG);
		query.addScalar("fornecedorId", StandardBasicTypes.LONG);
		query.addScalar("editorId", StandardBasicTypes.LONG);
		query.addScalar("valor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("predominante", StandardBasicTypes.BOOLEAN);

		return query.list();
	}

}
