package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
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
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT new ").append(RegistroCurvaABCDistribuidorVO.class.getCanonicalName())
				.append(" ( estoqueProdutoCota.cota.numeroCota , ")
				.append("   case when (pessoa.nome is not null) then ( pessoa.nome ) ")
				.append("     when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial ) ")
				.append("     else null end , ")
				.append("   sum(pdv) , " )
				.append("   endereco.cidade , " )
				.append("   (sum(estoqueProdutoCota.qtdeRecebida) - sum(estoqueProdutoCota.qtdeDevolvida)) , ")
				.append("   ( (sum(estoqueProdutoCota.qtdeRecebida) - sum(estoqueProdutoCota.qtdeDevolvida)) * estoqueProdutoCota.produtoEdicao.precoVenda ) ) ");

		hql.append(getWhereQueryObterCurvaABCDistribuidor(filtro));
		
		Query query = this.getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = getParametrosObterCurvaABCDistribuidor(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		/*if (filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}*/
		
		return query.list();		
		
	}

	private String getWhereQueryObterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("from EstoqueProdutoCota as estoqueProdutoCota ")
		.append(" join estoqueProdutoCota.movimentos as movimentos ")
		.append(" join estoqueProdutoCota.produtoEdicao.produto.fornecedores as fornecedores ")
		.append(" join estoqueProdutoCota.cota.enderecos as enderecos ")
		.append(" join enderecos.endereco as endereco ")
		.append(" join estoqueProdutoCota.cota.pdvs as pdv ")
		.append(" join estoqueProdutoCota.cota.pessoa as pessoa ");
		
		hql.append("where movimentos.data between :dataDe and :dataAte ");

		hql.append("and enderecos.principal is true ");
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty()) {
			hql.append("and fornecedores.id = :codigoFornecedor ");
			//hql.append("and produtoEdicao.produto.fornecedores.id = :codigoFornecedor ");
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			hql.append("and estoqueProdutoCota.produtoEdicao.produto.codigo = :codigoProduto ");
		}

		if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isEmpty()) {
			hql.append("and estoqueProdutoCota.produtoEdicao.produto.nome = :nomeProduto ");
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			hql.append("and estoqueProdutoCota.produtoEdicao.numeroEdicao = :edicaoProduto ");
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty()) {
			hql.append("and estoqueProdutoCota.produtoEdicao.produto.editor.codigo = :codigoEditor ");
		}

		if (filtro.getCodigoCota() != null && !filtro.getCodigoCota().isEmpty()) {
			hql.append("and estoqueProdutoCota.cota.numeroCota = :codigoCota ");
		}

		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			hql.append("and pessoa.nome = :nomeCota ");
		}

		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty()) {
			hql.append("and endereco.cidade = :municipio ");
		}
		
		return hql.toString();
		
	}

	/**
	 * Retorna os parametros da consulta de dividas.
	 * @param filtro
	 * @return HashMap<String,Object>
	 */
	private HashMap<String,Object> getParametrosObterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		param.put("dataDe",  filtro.getDataDe());
		param.put("dataAte", filtro.getDataAte());

		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty()) {
			param.put("codigoFornecedor", filtro.getCodigoFornecedor());
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			param.put("codigoProduto", filtro.getCodigoProduto());
		}

		if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isEmpty()) {
			param.put("nomeProduto", filtro.getNomeProduto());
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			param.put("edicaoProduto", filtro.getEdicaoProduto());
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty()) {
			param.put("codigoEditor", filtro.getCodigoEditor());
		}

		if (filtro.getCodigoCota() != null && !filtro.getCodigoCota().isEmpty()) {
			param.put("codigoCota", filtro.getCodigoCota());
		}

		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			param.put("nomeCota", filtro.getNomeCota());
		}

		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty()) {
			param.put("municipio", filtro.getMunicipio());
		}

		return param;
	}
	
}
