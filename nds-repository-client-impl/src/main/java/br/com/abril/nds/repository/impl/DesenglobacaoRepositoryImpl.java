package br.com.abril.nds.repository.impl;

import java.util.List;

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
		return getSession().createCriteria(Desenglobacao.class)
			.add(Restrictions.eq("desenglobaNumeroCota", cotaId))
			.list();
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
}
