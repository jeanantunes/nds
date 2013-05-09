package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.distribuicao.Desenglobacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DesenglobacaoRepository;

@Repository
public class DesenglobacaoRepositoryImpl extends AbstractRepositoryModel<Desenglobacao, Long> implements DesenglobacaoRepository {

	@Autowired NamedParameterJdbcTemplate jdbcTemplate;
	
	public DesenglobacaoRepositoryImpl() {
		super(Desenglobacao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Desenglobacao> obterDesenglobacaoPorCota(Long cotaId) {
		
		StringBuilder hql = new StringBuilder("");
		hql.append(" from Desenglobacao d where d.englobadaNumeroCota = :cotaId ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("cotaId", cotaId);
		return query.list();

	}
	
	public List<Desenglobacao> obterDesenglobacaoPorCotaDesenglobada(Long cotaNumero) {
		
		StringBuilder hql = new StringBuilder("");
		hql.append(" from Desenglobacao d where d.desenglobaNumeroCota = :cotaNumero ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("cotaNumero", cotaNumero);
		return query.list();

	}
	
	public Float verificaPorcentagemCota(Long cotaId) {
		return ((Number) getSession().createCriteria(Desenglobacao.class)
				.add(Restrictions.eq("desenglobaNumeroCota", cotaId))
				.setProjection(Projections.sum("englobadaPorcentagemCota"))
				.uniqueResult()).floatValue();
	}

	@Override
	public void inserirCotasDesenglobadas(final List<Desenglobacao> cotasDesenglobadas) {
		
		String sql = "INSERT INTO DESENGLOBACAO " +
				"(COTA_ID_DESENGLOBADA, NOME_COTA_DESENGLOBADA, TIPO_PDV_ID, USUARIO_ID, " +
				"COTA_ID_ENGLOBADA, NOME_COTA_ENGLOBADA, PORCENTAGEM_COTA_ENGLOBADA, DATA_ALTERACAO)" +
				" VALUES(:desenglobaNumeroCota,:desenglobaNomePessoa,:tipoPDV.id,:responsavel.id,:englobadaNumeroCota,:englobadaNomePessoa,:englobadaPorcentagemCota,:dataAlteracao)";
		
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(cotasDesenglobadas.toArray());
		jdbcTemplate.batchUpdate(sql, params);
	}

	@Override
	public boolean removerPorCotaDesenglobada(Long cotaNumeroDesengloba) {
		boolean res=Boolean.TRUE;
		try {
			String hql = "delete from Desenglobacao d where d.desenglobaNumeroCota= :cotaNumeroDesengloba";
			getSession().createQuery(hql).setString("cotaNumeroDesengloba", cotaNumeroDesengloba.toString())
					.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			res=Boolean.FALSE;
		}
		
		return res;
		
		
	}
}
