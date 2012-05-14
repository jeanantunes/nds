package br.com.abril.nds.repository.impl;


import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoGarantiaAceita;
import br.com.abril.nds.repository.DistribuidorRepository;

@Repository
public class DistribuidorRepositoryImpl extends
		AbstractRepository<Distribuidor, Long> implements
		DistribuidorRepository {

	public DistribuidorRepositoryImpl() {
		super(Distribuidor.class);
	}

	@Override
	public Distribuidor obter() {
		String hql = "from Distribuidor";
		Query query = getSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Distribuidor> distribuidores = query.list();
		return distribuidores.isEmpty() ? null : distribuidores.get(0);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DistribuicaoFornecedor> buscarDiasDistribuicaoFornecedor(
															Collection<Long> idsForncedores, 
															OperacaoDistribuidor operacaoDistribuidor) {
		
		StringBuilder hql = 
			new StringBuilder("from DistribuicaoFornecedor where fornecedor.id in (:idsFornecedores) ");
		
		hql.append("and operacaoDistribuidor = :operacaoDistribuidor ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameterList("idsFornecedores", idsForncedores);
		query.setParameter("operacaoDistribuidor", operacaoDistribuidor);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DistribuicaoDistribuidor> buscarDiasDistribuicaoDistribuidor(
															Long idDistruibuidor,
															OperacaoDistribuidor operacaoDistribuidor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("from DistribuicaoDistribuidor ");
		hql.append("where distribuidor.id = :idDistribuidor ");
		hql.append("and operacaoDistribuidor = :operacaoDistribuidor ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idDistribuidor", idDistruibuidor);
		query.setParameter("operacaoDistribuidor", operacaoDistribuidor);
		
		return query.list();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obterEnderecoPrincipal()
	 */
	@Override
	public EnderecoDistribuidor obterEnderecoPrincipal(){
		Criteria criteria=  getSession().createCriteria(EnderecoDistribuidor.class);
		criteria.add(Restrictions.eq("principal", true) );		
		criteria.setMaxResults(1);
		
		return (EnderecoDistribuidor) criteria.uniqueResult();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obtemTiposGarantiasAceitas()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoGarantia> obtemTiposGarantiasAceitas() {		
		Criteria criteria =  getSession().createCriteria(TipoGarantiaAceita.class);		
		criteria.setProjection(Projections.property("tipoGarantia"));
		return criteria.list();
	}

	@Override
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(
			Date dataDe, Date dataAte, String codigoFornecedor,
			String codigoProduto, String nomeProduto, String edicaoProduto,
			String codigoEditor, String codigoCota, String nomeCota,
			String municipio) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("from EstoqueProdutoCota estoque join estoque.movimentos movimentos join estoque.produtoEdicao.produto.fornecedores fornecedores join estoque.cota.enderecos enderecos ");
		hql.append("where movimentos.data >= :dataDe ");
		hql.append("and movimentos.data <= :dataAte ");

		if (codigoFornecedor != null && !codigoFornecedor.isEmpty()) {
			hql.append("and fornecedores.id = :codigoFornecedor ");
			//hql.append("and produtoEdicao.produto.fornecedores.id = :codigoFornecedor ");
		}

		if (codigoProduto != null && !codigoProduto.isEmpty()) {
			hql.append("and estoque.produtoEdicao.produto.codigo <= :codigoProduto ");
		}

		if (nomeProduto != null && !nomeProduto.isEmpty()) {
			hql.append("and estoque.produtoEdicao.produto.nome <= :nomeProduto ");
		}

		if (edicaoProduto != null && !edicaoProduto.isEmpty()) {
			hql.append("and estoque.produtoEdicao.numeroEdicao <= :edicaoProduto ");
		}

		if (codigoEditor != null && !codigoEditor.isEmpty()) {
			hql.append("and estoque.produtoEdicao.produto.editor.codigo <= :codigoEditor ");
		}

		if (codigoCota != null && !codigoCota.isEmpty()) {
			hql.append("and estoque.cota.numeroCota = :codigoCota ");
		}

		if (nomeCota != null && !nomeCota.isEmpty()) {
			hql.append("and estoque.cota.pessoa.nome = :nomeCota ");
		}

		if (municipio != null && !municipio.isEmpty()) {
			hql.append("and enderecos.endereco.cidade <= :municipio ");
		}

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataDe", dataDe);
		query.setParameter("dataAte", dataAte);

		if (codigoFornecedor != null && !codigoFornecedor.isEmpty()) {
			query.setParameter("codigoFornecedor", codigoFornecedor);
		}

		if (codigoProduto != null && !codigoProduto.isEmpty()) {
			query.setParameter("codigoProduto", codigoProduto);
		}

		if (nomeProduto != null && !nomeProduto.isEmpty()) {
			query.setParameter("nomeProduto", nomeProduto);
		}

		if (edicaoProduto != null && !edicaoProduto.isEmpty()) {
			query.setParameter("edicaoProduto", edicaoProduto);
		}

		if (codigoEditor != null && !codigoEditor.isEmpty()) {
			query.setParameter("codigoEditor", codigoEditor);
		}

		if (codigoCota != null && !codigoCota.isEmpty()) {
			query.setParameter("codigoCota", codigoCota);
		}

		if (nomeCota != null && !nomeCota.isEmpty()) {
			query.setParameter("nomeCota", nomeCota);
		}

		if (municipio != null && !municipio.isEmpty()) {
			query.setParameter("municipio", municipio);
		}
		return query.list();
	}

}
