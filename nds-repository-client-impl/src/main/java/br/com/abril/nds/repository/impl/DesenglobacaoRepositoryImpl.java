package br.com.abril.nds.repository.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.distribuicao.Desenglobacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DesenglobacaoRepository;

@Repository
public class DesenglobacaoRepositoryImpl extends AbstractRepositoryModel<Desenglobacao, Long> implements DesenglobacaoRepository {

	@Autowired JdbcTemplate jdbcTemplate;
	
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
				" VALUES(?,?,?,?,?,?,?,?)";
		
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Desenglobacao desenglobacao = cotasDesenglobadas.get(i);
				ps.setLong(1, desenglobacao.getDesenglobaNumeroCota());
				ps.setString(2, desenglobacao.getDesenglobaNomePessoa());
				ps.setLong(3, (desenglobacao.getTipoPDV() != null) ? desenglobacao.getTipoPDV().getId() : 4); //verifica se está nulo, caso esteja, coloque tipoPDV = Outros
				ps.setLong(4, desenglobacao.getResponsavel().getId());
				ps.setLong(5, desenglobacao.getEnglobadaNumeroCota());
				ps.setString(6, desenglobacao.getEnglobadaNomePessoa());
				ps.setFloat(7, desenglobacao.getEnglobadaPorcentagemCota());
				ps.setDate(8, new Date(desenglobacao.getDataAlteracao().getTime()));
			}
			
			@Override
			public int getBatchSize() {
				return cotasDesenglobadas.size();
			}
		});
	}
}
