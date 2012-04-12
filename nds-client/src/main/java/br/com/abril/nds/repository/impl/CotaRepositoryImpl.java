package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ProdutoValorDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.repository.CotaRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.Cota}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class CotaRepositoryImpl extends AbstractRepository<Cota, Long> implements CotaRepository {

	@Value("#{queries.suspensaoCota}")
	protected String querySuspensaoCota;
	
	@Value("#{queries.countSuspensaoCota}")
	protected String queryCountSuspensaoCota;
	
	/**
	 * Construtor.
	 */
	public CotaRepositoryImpl() {
		
		super(Cota.class);
	}

	public Cota obterPorNumerDaCota(Integer numeroCota) {
		
		Criteria criteria = super.getSession().createCriteria(Cota.class);
		
		criteria.add(Restrictions.eq("numeroCota", numeroCota));
		
		criteria.setMaxResults(1);
		
		return (Cota) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Cota> obterCotasPorNomePessoa(String nome) {
		
		Criteria criteria = super.getSession().createCriteria(Cota.class);
		
		criteria.createAlias("pessoa", "pessoa");
		
		criteria.add(
			Restrictions.or(
				Restrictions.ilike("pessoa.nome", nome, MatchMode.ANYWHERE),
				Restrictions.ilike("pessoa.razaoSocial", nome, MatchMode.ANYWHERE)
			)
		);
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Cota> obterPorNome(String nome) {

		Criteria criteria = super.getSession().createCriteria(Cota.class);
		
		criteria.createAlias("pessoa", "pessoa");
		
		criteria.add(
			Restrictions.or(
				Restrictions.eq("pessoa.nome", nome),
				Restrictions.eq("pessoa.razaoSocial", nome)
			)
		);
		
		return criteria.list();
	}

	/**
	 * @see br.com.abril.nds.repository.CotaRepository#obterEnderecosPorIdCota(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(Long idCota) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select enderecoCota.id as id, enderecoCota.endereco as endereco, ")
		   .append(" enderecoCota.principal as enderecoPrincipal, ")
		   .append(" enderecoCota.tipoEndereco as tipoEndereco ")
		   .append(" from EnderecoCota enderecoCota ")
		   .append(" where enderecoCota.cota.id = :idCota ");

		Query query = getSession().createQuery(hql.toString());

		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(EnderecoAssociacaoDTO.class);
		
		query.setResultTransformer(resultTransformer);
		
		query.setParameter("idCota", idCota);
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<CotaSuspensaoDTO> obterCotasSujeitasSuspensao(String sortOrder,	String sortColumn,Integer inicio, Integer rp) {
	
		StringBuilder sql = new StringBuilder(querySuspensaoCota);
		
		sql.append(obterOrderByCotasSujeitasSuspensao(sortOrder, sortColumn));
		
		if(inicio!= null && rp!= null) {
			sql.append(" LIMIT :inicio,:qtdeResult");
		}
		
		Query query = getSession().createSQLQuery(sql.toString())
				.addScalar("idCota")
				.addScalar("numCota")
				.addScalar("vlrConsignado")
				.addScalar("vlrReparte")
				.addScalar("dividaAcumulada")
				.addScalar("nome")
				.addScalar("razaoSocial")
				.addScalar("dataAbertura");
		
		if(inicio!= null && rp!= null) {
			query.setInteger("inicio", inicio);
			query.setInteger("qtdeResult", rp);
		}
			
		query.setResultTransformer(Transformers.aliasToBean(CotaSuspensaoDTO.class));
				
		return query.list();
	}	

	private String obterOrderByCotasSujeitasSuspensao(String sortOrder,String sortColumn) {
		String sql = "";
		
		if(sortColumn == null || sortOrder == null) {
			return sql;
		}
		
		sql += " ORDER BY ";
		
		if(sortColumn.equalsIgnoreCase("numCota")) {
			sql += "numCota";
		} else if(sortColumn.equalsIgnoreCase("nome")) {
			sql += "nome";
		} else if(sortColumn.equalsIgnoreCase("vlrConsignado")) {
			sql += "vlrConsignado";
		} else if(sortColumn.equalsIgnoreCase("vlrReparte")) {
			sql += "vlrReparte";
		} else if(sortColumn.equalsIgnoreCase("dividaAcumulada")) {
			sql += "dividaAcumulada";
		} else if(sortColumn.equalsIgnoreCase("diasAberto")) {
			sql += "dataAbertura";
		} else {
			return "";
		}
		
		sql += sortOrder.equalsIgnoreCase("asc") ?  " ASC " : " DESC ";		
		
		return sql;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoValorDTO> obterReparteDaCotaNoDia(Long idCota, Date date) {

		Criteria criteria = getSession().createCriteria(MovimentoEstoqueCota.class,"movimento");
		
		criteria.createAlias("movimento.produtoEdicao", "produtoEdicao");
		criteria.createAlias("movimento.tipoMovimento", "tipoMovimento");
		
		criteria.add(Restrictions.eq("data", date));
		criteria.add(Restrictions.eq("tipoMovimento.operacaoEstoque", OperacaoEstoque.ENTRADA));
		criteria.add(Restrictions.eq("cota.id", idCota));
		
		ProjectionList projections =  Projections.projectionList();
		projections.add(Projections.alias(Projections.property("qtde"),"quantidade"));
		projections.add(Projections.alias(Projections.property("produtoEdicao.precoVenda"),"preco"));
		
		criteria.setProjection(projections);
		
		criteria.setResultTransformer(Transformers.aliasToBean(ProdutoValorDTO.class));		
				
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoValorDTO> obterValorConsignadoDaCota(Long idCota) {
		
		Criteria criteria = getSession().createCriteria(EstoqueProdutoCota.class,"epCota");
		criteria.createAlias("epCota.produtoEdicao", "produtoEdicao");
		criteria.createAlias("epCota.cota", "cota");
				
		criteria.add(Restrictions.eq("cota.id", idCota));
	
		ProjectionList projections =  Projections.projectionList();
		projections.add(Projections.alias(Projections.property("epCota.qtdeRecebida"),"quantidade"));
		projections.add(Projections.alias(Projections.property("produtoEdicao.precoVenda"),"preco"));
		
		criteria.setProjection(projections);
		
		criteria.setResultTransformer(Transformers.aliasToBean(ProdutoValorDTO.class));		
				
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> obterDiasConcentracaoPagamentoCota(Long idCota) {
		StringBuilder hql = new StringBuilder("select cc.codigoDiaSemana ");
		hql.append(" from ConcentracaoCobrancaCota cc, Cota cota, ParametroCobrancaCota p ")
		   .append(" where cota.id                     = p.cota.id ")
		   .append(" and   cc.parametroCobrancaCota.id = p.id ")
		   .append(" and   cota.id                     = :idCota");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		
		return query.list();
	}

	@Override
	public Long obterTotalCotasSujeitasSuspensao() {
		
		StringBuilder sql = new StringBuilder(queryCountSuspensaoCota);
						
		Query query = getSession().createSQLQuery(sql.toString());
		
		Long qtde = ((BigInteger) query.uniqueResult()).longValue();
		
		return qtde == null ? 0 : qtde;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Cota> obterCotaAssociadaFiador(Long idFiador){
		Criteria criteria = this.getSession().createCriteria(Cota.class);
		criteria.add(Restrictions.eq("fiador.id", idFiador));
		
		return criteria.list();
	}
}