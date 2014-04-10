package br.com.abril.nds.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalheBox;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalheBoxPK;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoEncalheBoxRepository;

@Repository
public class FechamentoEncalheBoxRepositoryImpl extends AbstractRepositoryModel<FechamentoEncalheBox, FechamentoEncalheBoxPK> implements FechamentoEncalheBoxRepository {

	@Autowired
	private DataSource dataSource;
	
	public FechamentoEncalheBoxRepositoryImpl() {
		super(FechamentoEncalheBox.class);
	}
	
	
	@Override
	public List<FechamentoEncalheBox> buscarFechamentoEncalheBox(FiltroFechamentoEncalheDTO filtro) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append("	this_.BOX_ID, ");
		sql.append("	this_.DATA_ENCALHE, ");
		sql.append("	this_.PRODUTO_EDICAO_ID, ");
		sql.append("	this_.QUANTIDADE ");
		sql.append(" from ");
		sql.append("	FECHAMENTO_ENCALHE_BOX this_  ");
		sql.append(" where ");
		sql.append("	this_.BOX_ID=:idBox ");
		sql.append("	and this_.DATA_ENCALHE= :dataEncalhe ");

		Map<String, Object> parameters = new HashMap<String, Object>();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		parameters.put("idBox", filtro.getBoxId());
		parameters.put("dataEncalhe", filtro.getDataEncalhe());
		
		RowMapper<FechamentoEncalheBox> cotaRowMapper = new RowMapper<FechamentoEncalheBox>() {

			public FechamentoEncalheBox mapRow(ResultSet rs, int arg1) throws SQLException {

				FechamentoEncalheBox fechamento = new FechamentoEncalheBox();
				Box box = new Box();
				box.setId(rs.getLong("BOX_ID"));
				
				fechamento.setFechamentoEncalheBoxPK(new FechamentoEncalheBoxPK());
				
				fechamento.getFechamentoEncalheBoxPK().setBox(box);
				fechamento.getFechamentoEncalheBoxPK().setFechamentoEncalhe(new FechamentoEncalhe());
				fechamento.getFechamentoEncalheBoxPK().getFechamentoEncalhe().setFechamentoEncalhePK(new FechamentoEncalhePK());
				fechamento.getFechamentoEncalheBoxPK().getFechamentoEncalhe().getFechamentoEncalhePK().setDataEncalhe(rs.getDate("DATA_ENCALHE"));
				fechamento.getFechamentoEncalheBoxPK().getFechamentoEncalhe().getFechamentoEncalhePK().setProdutoEdicao(new ProdutoEdicao());
				fechamento.getFechamentoEncalheBoxPK().getFechamentoEncalhe().getFechamentoEncalhePK().getProdutoEdicao().setId(rs.getLong("PRODUTO_EDICAO_ID"));
				
				fechamento.setQuantidade(rs.getLong("QUANTIDADE"));
				
				return fechamento;
			}
		};
		
		return namedParameterJdbcTemplate.query(sql.toString(), parameters, cotaRowMapper);
	}
	

	
	public boolean verificarExistenciaFechamentoEncalheDetalhado(Date dataEncalhe) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select case when (count(f.BOX_ID) > 0)  then true else false end as verificacao" );
		sql.append(" from FECHAMENTO_ENCALHE_BOX f ");
		sql.append(" where f.DATA_ENCALHE = :dataEncalhe and ");
		sql.append(" f.QUANTIDADE > 0 ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		((SQLQuery) query ).addScalar("verificacao", BooleanType.INSTANCE);
		
		query.setParameter("dataEncalhe", dataEncalhe);
		
		return (Boolean) query.uniqueResult();
		
	}

}
