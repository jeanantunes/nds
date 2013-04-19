package br.com.abril.nds.integracao.ems0107.processor;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0107.inbound.EMS0107Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS0107MessageProcessor extends AbstractRepository implements MessageProcessor  {
	
	private static EMS0107MessageProcessor instance = new EMS0107MessageProcessor();
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	public static EMS0107MessageProcessor getInstance() {
		return instance;
	}
	
	private EMS0107MessageProcessor() {

	}
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void processMessage(Message message) {
		
		EMS0107Input input = (EMS0107Input) message.getBody();
		
		String codigoPublicacao = input.getCodigoPublicacao();
		Long edicao = input.getEdicao();
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoPublicacao, edicao);
		
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"NAO ENCONTROU Produto de codigo: " + codigoPublicacao + "/ edicao: " + edicao);
			return;
		}
			
		Lancamento lancamento = this.getLancamentoPrevistoMaisProximo(produtoEdicao);
		/*if (lancamento == null) {
			
			lancamento = getLancamentoPrevistoAnteriorMaisProximo(produtoEdicao);*/
			
			if (lancamento == null) {
				this.ndsiLoggerFactory.getLogger().logError(message,
						EventoExecucaoEnum.RELACIONAMENTO,
						"NAO ENCONTROU Lancamento para o Produto de codigo: " + codigoPublicacao + "/ edicao: " + edicao);
				return;
			}
		//}
		
		if (lancamento.getStatus() == StatusLancamento.CONFIRMADO) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO, 
					"Lancamento para o Produto de codigo: " + codigoPublicacao + "/ edicao: " + edicao + " está com STATUS 'EXPEDIDO' e portanto, não gerará ou alterará o estudo cota!");
			return;
		}		
		
		Estudo estudo = lancamento.getEstudo();		
		if (estudo == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"NAO ENCONTROU Estudo para o Produto de codigo: " + codigoPublicacao + "/ edicao: " + edicao + " no Lancamento: " + lancamento.getDataLancamentoPrevista().toString() );
			return;
		}
		
		Integer numeroCota = input.getCodigoCota();
		Cota cota = this.obterCota(numeroCota);
		if (cota == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"NAO ENCONTROU a Cota :" + numeroCota );
			return;
		}
		
		boolean hasEstudoCota = this.hasEstudoCota(estudo, cota);
		if (hasEstudoCota) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.REGISTRO_JA_EXISTENTE,
					"JA EXISTE EstudoCota para a numero de Cota: " + numeroCota + "para o Produto de codigo: " + codigoPublicacao + "/ edicao: " + edicao + " no Lancamento: " + lancamento.getDataLancamentoPrevista().toString() );
			return;
		}
		
		// Novo EstudoCota:
		BigInteger qtdReparte = BigInteger.valueOf(input.getQuantidadeReparte());
		EstudoCota eCota = new EstudoCota();
		eCota.setEstudo(estudo);
		eCota.setCota(cota);
		eCota.setQtdePrevista(qtdReparte);
		eCota.setQtdeEfetiva(qtdReparte);

		this.ndsiLoggerFactory.getLogger().logError(message,
				EventoExecucaoEnum.INF_DADO_ALTERADO,
				"EstudoCota para a numero de Cota: " + numeroCota + "para o Produto de codigo: " + codigoPublicacao + "/ edicao: " + edicao + " no Lancamento: " + lancamento.getDataLancamentoPrevista().toString() + " Inserido com sucesso!");
		
		this.getSession().persist(eCota);
	}	
	
	/**
	 * Obtém o Produto Edição cadastrado previamente.
	 * 
	 * @param codigoPublicacao Código da Publicação.
	 * @param edicao Número da Edição.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ProdutoEdicao obterProdutoEdicao(String codigoPublicacao,
			Long edicao) {

		try {

			StringBuilder sql = new StringBuilder();
			
			sql.append("SELECT * FROM produto_edicao pe ");
			sql.append("INNER JOIN produto p ");
			sql.append("WHERE pe.produto_id = p.id ");
			sql.append("AND p.codigo = ? ");
			sql.append("AND pe.numero_edicao = ? ");
			
			@SuppressWarnings("rawtypes")
			RowMapper produtoEdicaoRowMapper = new RowMapper() {

				public Object mapRow(ResultSet rs, int arg1) throws SQLException {

					ProdutoEdicao produtoEdicao = new ProdutoEdicao();
					produtoEdicao.setId(rs.getLong("ID"));
					
					return produtoEdicao;
				}
			};
			

			jdbcTemplate = new JdbcTemplate(dataSource);
			 
			return jdbcTemplate.queryForObject(sql.toString(), new Object[] { 
					codigoPublicacao, 
					edicao}, 
					produtoEdicaoRowMapper);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Cota obterCota(Integer numeroCota) {
		
		StringBuilder sql = new StringBuilder();		
		sql.append("SELECT * FROM cota c ");
		sql.append("WHERE c.numero_cota = ? ");
		
		@SuppressWarnings("rawtypes")
		RowMapper cotaRowMapper = new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				Cota cota = new Cota();
				cota.setId(rs.getLong("ID"));
				
				return cota;
			}
		};
		

		jdbcTemplate = new JdbcTemplate(dataSource);
		 
		return jdbcTemplate.queryForObject(sql.toString(), new Object[] { 
				numeroCota}, 
				cotaRowMapper);
		
	}

	/**
	 * Obtém o Lançamento com data de lançamento mais próximo do dia corrente.
	 *  
	 * @param produtoEdicao
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Lancamento getLancamentoPrevistoMaisProximo(ProdutoEdicao produtoEdicao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT l.*, e.ID as ESTUDO_ID FROM lancamento l ");
		sql.append("INNER JOIN produto_edicao pe ON pe.id = l.PRODUTO_EDICAO_ID ");
		sql.append("INNER JOIN estudo e ON e.PRODUTO_EDICAO_ID = pe.ID AND e.DATA_LANCAMENTO = l.DATA_LCTO_PREVISTA ");
		sql.append("WHERE pe.ID = ? ");
		sql.append("AND l.DATA_LCTO_PREVISTA >= ? ");
		sql.append("ORDER BY l.DATA_LCTO_PREVISTA ASC");
		
		RowMapper lancamentoRowMapper = new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				Lancamento lancamento = new Lancamento();
				
				Estudo estudo = new Estudo();
				
				lancamento.setId(rs.getLong("ID"));
				lancamento.setDataCriacao(rs.getDate("DATA_CRIACAO"));
				lancamento.setDataLancamentoPrevista(rs.getDate("DATA_LCTO_PREVISTA"));
				lancamento.setStatus(StatusLancamento.valueOf(rs.getString("STATUS")));
				estudo.setId(rs.getLong("ESTUDO_ID"));
				lancamento.setEstudo(estudo);
				
				return lancamento;

			}

		};
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		 
		return jdbcTemplate.queryForObject(sql.toString(), new Object[] { 
				produtoEdicao.getId(), 
				distribuidorService.obter().getDataOperacao() }, 
				lancamentoRowMapper);
		
	}
	
	/**
	 * Obtém o Lançamento com data de lançamento mais próximo do dia corrente.
	 *  
	 * @param produtoEdicao
	 * @return
	 */
	/*private Lancamento getLancamentoPrevistoAnteriorMaisProximo(
			ProdutoEdicao produtoEdicao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT lcto FROM Lancamento lcto ");
		sql.append("      JOIN FETCH lcto.produtoEdicao pe ");
		sql.append("    WHERE pe = :produtoEdicao ");
		sql.append("      AND lcto.dataLancamentoPrevista < :dataOperacao ");
		sql.append(" ORDER BY lcto.dataLancamentoPrevista DESC");
		
		Query query = getSession().createQuery(sql.toString());
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		query.setParameter("produtoEdicao", produtoEdicao);
		query.setDate("dataOperacao", dataOperacao);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}*/	
	
	/**
	 * Verifica se já existe EstudoCota cadastrado.
	 * 
	 * @param estudo
	 * @param cota
	 * @return true: Já existe pelo menos 1 EstudoCota cadastrado;<br>
	 * false: Não existe nenhum EstudoCota para o Estudo e Cota passado;
	 */
	private boolean hasEstudoCota(Estudo estudo, Cota cota) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COUNT(0) FROM estudo_cota ec ");
		sql.append("INNER JOIN estudo e ON ec.estudo_id = e.id ");
		sql.append("WHERE e.id = ? ");
		sql.append("AND ec.cota_id = ? ");
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		 
		Long qtd = jdbcTemplate.queryForLong(sql.toString(), new Object[] { 
					estudo.getId(), 
					cota.getId() });
		
		return (qtd != null && qtd.intValue() > 0);
		
		/*
		stringbuilder hql = new stringbuilder();
		hql.append(" select count(ec) from estudocota ec " );
		hql.append("  where ec.estudo = :estudo and ec.cota = :cota");
		
		query query = getsession().createquery(hql.tostring());
		query.setparameter("estudo", estudo);
		query.setparameter("cota", cota);
		
		long qtd = (long) query.uniqueresult();
		return (qtd != null && qtd.intvalue() > 0);
		*/
	}
	
	@Override
	public void posProcess(Object tempVar) {
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		
		/*
		 * Regras de validação para EMS-107:
		 * 
		 * 01) Não deve existir Estudo sem EstudoCota:
		 * Todo Estudo deve possuir pelo menos um (ou mais) EstudoCota;
		 * 
		 * 02) A soma de todos os EstudoCota de um Estudo deve ser igual ao 
		 * valor contido na "quantidade Efetiva" do respectivo Estudo.
		 */
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT id FROM estudo e ");
		sql.append("WHERE e.ID NOT IN (SELECT ec.ESTUDO_ID FROM estudo_cota ec WHERE ec.ESTUDO_ID = e.ID) ");
		
		List<Map<String, Object>> lstEstudos = jdbcTemplate.queryForList(sql.toString());
		
		// 01) Verificar se existe algum Estudo sem EstudoCota;
		if (lstEstudos != null && !lstEstudos.isEmpty()) {
			
			// this.ndsiLoggerFactory.getLogger().logError(message,
			// EventoExecucaoEnum.INF_DADO_ALTERADO,
			// "NAO EXISTE EstudoCota para a publicacao: " + estudo.getProdutoEdicao());
			
			final List<Long> idsAExcluir = new ArrayList<Long>();
			
			for (Map<String, Object> estudo : lstEstudos) {
				
				idsAExcluir.add((Long) estudo.get("id"));
			
			}
			
			sql = new StringBuilder();				
			sql.append("DELETE FROM estudo WHERE id = ? ");			
			
			jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {

				@Override
				public int getBatchSize() {
					return idsAExcluir.size();
				}

				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					
					ps.setLong(1, (Long) idsAExcluir.get(i));
					
				}
				
			});
			
		}
		
		// 02) Verificar se a soma de todos os qtdeEfetiva e qtdePrevista de um
		// EstudoCota batem com a qtdeReparte do respectivo Estudo
		sql = new StringBuilder();
		sql.append(" SELECT e.id ");
		sql.append(" FROM estudo e, ");
		sql.append("       (SELECT ec.estudo_id AS estudo_id, SUM(ec.qtde_prevista) AS qtde ");
		sql.append("          FROM estudo_cota ec GROUP BY ec.estudo_id ");
		sql.append("       ) AS c");
		sql.append(" WHERE e.id = c.estudo_id ");
		sql.append(" AND e.qtde_reparte <> c.qtde ");
				
		List<Map<String, Object>> lstEstudosId = jdbcTemplate.queryForList(sql.toString());
		if (lstEstudosId != null && !lstEstudosId.isEmpty()) {
			
			final List<Long> idsAExcluir = new ArrayList<Long>();
			
			for (Map<String, Object> estudo : lstEstudosId) {
				
				idsAExcluir.add((Long) estudo.get("id"));
			
			}
			
			sql = new StringBuilder();				
			sql.append("DELETE FROM estudo WHERE id = ? ");			
			
			jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {

				@Override
				public int getBatchSize() {
					return idsAExcluir.size();
				}

				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					
					ps.setLong(1, (Long) idsAExcluir.get(i));
					
				}
				
			});
		}
				
	}
			
}