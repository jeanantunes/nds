package br.com.abril.nds.repository.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstudoCotaGeradoRepository;
import br.com.abril.nds.util.Intervalo;

/**
 * Classe de implementação referente ao acesso a dados da entidade {@link br.com.abril.nds.model.planejamento.EstudoCota}.
 * 
 * @author Discover Technology
 *
 */
@Repository
public class EstudoCotaGeradoRepositoryImpl extends AbstractRepositoryModel<EstudoCotaGerado, Long> implements EstudoCotaGeradoRepository {
	
	/**
	 * Construtor.
	 */
	public EstudoCotaGeradoRepositoryImpl() {
		super(EstudoCotaGerado.class);
	}
	
//	@Autowired
//    private NamedParameterJdbcTemplate jdbcTemplate;
	
//	@Value("#{query_estudo.insertCotasEstudoCotaGerado}")
//    private String insertEstudoCotas;

	@Autowired
    private DataSource dataSource;

	@Override
	public EstudoCotaGerado obterEstudoCota(Integer numeroCota, Date dataReferencia) {
		
		String hql = " from EstudoCotaGerado estudoCota"
				   + " where estudoCota.cota.numeroCota = :numeroCota"
				   + " and estudoCota.estudo.dataLancamento >= :dataReferencia";
		
		Query query = super.getSession().createQuery(hql);
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("dataReferencia", dataReferencia);
		query.setMaxResults(1);
		
		return (EstudoCotaGerado) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EstudoCotaGerado> obterEstudoCota(Long idCota, Date dataDe, Date dataAte) {
		
		String hql = " from EstudoCotaGerado estudoCota "
				   + " join estudoCota.estudo estudo "
				   + " join estudoCota.cota cota "
				   + " join estudo.lancamentos lancamento "
				   + " where cota.id = :idCota"
				   + " and lancamento.dataLancamentoDistribuidor between :dataDe AND :dataAte ";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("idCota", idCota);
		query.setParameter("dataDe", dataDe);
		query.setParameter("dataAte", dataAte);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EstudoCotaDTO> obterEstudoCotaPorDataProdutoEdicao(Date dataLancamento, Long idProdutoEdicao) {
			
		String hql = " select estudoCota.id as id, " 
				   + " estudoCota.qtdeEfetiva as qtdeEfetiva, "
				   + " cota.id as idCota, "
				   + " estudoCota.tipoEstudo as tipoEstudo "  
				   + " from EstudoCota estudoCota "
				   + " join estudoCota.estudo estudo "
				   + " join estudoCota.cota cota "
				   + " join estudo.produtoEdicao produtoEdicao "
				   + " where estudo.dataLancamento = :dataLancamento " 
				   + " and produtoEdicao.id = :idProdutoEdicao";
		
		Query query = super.getSession().createQuery(hql);
		query.setParameter("dataLancamento", dataLancamento);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setResultTransformer(Transformers.aliasToBean(EstudoCotaDTO.class));
		
		return query.list();
	}
	
	@Override
	public EstudoCotaGerado obterEstudoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota) {
			
		String hql = " from EstudoCotaGerado estudoCota "
				   + " where estudoCota.estudo.dataLancamento= :dataLancamento " 
				   + " and estudoCota.estudo.produtoEdicao.id= :idProdutoEdicao " 
				   + " and estudoCota.cota.id = :idCota";
		
		Query query = super.getSession().createQuery(hql);
		query.setParameter("dataLancamento", dataLancamento);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("idCota", idCota);

		query.setMaxResults(1);
		
		return (EstudoCotaGerado) query.uniqueResult();
	}
	
    public EstudoCotaGerado obterEstudoCotaDeLancamentoComEstudoFechado(Date dataLancamentoDistribuidor, Long idProdutoEdicao, Integer numeroCota) {
		
		String hql = " from EstudoCotaGerado estudoCota "
				   + " where estudoCota.estudo.dataLancamento <= :dataLancamentoDistribuidor " 
				   + " and estudoCota.estudo.produtoEdicao.id = :idProdutoEdicao " 
				   + " and estudoCota.cota.numeroCota = :numeroCota "
				   + " order by estudoCota.estudo.dataLancamento desc ";
		
		Query query = super.getSession().createQuery(hql);
		query.setParameter("dataLancamentoDistribuidor", dataLancamentoDistribuidor);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("numeroCota", numeroCota);
		query.setMaxResults(1);
		
		return (EstudoCotaGerado) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EstudoCotaGerado> obterEstudosCotaParaNotaEnvio(List<Long> idCotas, 
														  Intervalo<Date> periodo, 
														  List<Long> listaIdsFornecedores,
														  String exibirNotasEnvio) {
		
		StringBuffer sql = new StringBuffer("SELECT DISTINCT estudoCota ");

		sql.append(" FROM EstudoCotaGerado estudoCota ");
		sql.append(" JOIN estudoCota.cota cota ");
		sql.append(" JOIN estudoCota.estudo estudo ");
		sql.append(" JOIN estudo.lancamentos lancamento ");
		sql.append(" JOIN estudo.produtoEdicao produtoEdicao ");
		sql.append(" JOIN produtoEdicao.produto produto ");
		sql.append(" JOIN produto.fornecedores fornecedor ");

		sql.append(" LEFT JOIN estudoCota.itemNotaEnvios itemNotaEnvios ");
		sql.append(" WHERE cota.id IN (:idCotas) ");
		sql.append(" AND estudo.dataLancamento = lancamento.dataLancamentoPrevista ");
		sql.append(" AND lancamento.status NOT IN (:listaExclusaoStatusLancamento) ");
		
		if("EMITIDAS".equals(exibirNotasEnvio)) {
			sql.append(" AND itemNotaEnvios is not null ");
		} else if("AEMITIR".equals(exibirNotasEnvio)) {
			sql.append(" AND itemNotaEnvios is null ");
		}
		
		if (listaIdsFornecedores != null && !listaIdsFornecedores.isEmpty()) {
			
			sql.append(" AND (fornecedor IS NULL OR fornecedor.id IN (:listaFornecedores)) ");
		}
		
        if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
			
			sql.append(" AND lancamento.dataLancamentoDistribuidor BETWEEN :dataInicio AND :dataFim ");
		}	
		
		Query query = getSession().createQuery(sql.toString());
		
		query.setParameterList("idCotas", idCotas);

		query.setParameterList("listaExclusaoStatusLancamento", new StatusLancamento[] {StatusLancamento.FURO, StatusLancamento.PLANEJADO, StatusLancamento.FECHADO, StatusLancamento.CONFIRMADO, StatusLancamento.EM_BALANCEAMENTO, StatusLancamento.CANCELADO});
	
		if (listaIdsFornecedores != null && !listaIdsFornecedores.isEmpty()) {
			query.setParameterList("listaFornecedores", listaIdsFornecedores);
		}
		if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
			query.setParameter("dataInicio", periodo.getDe());
			
			query.setParameter("dataFim", periodo.getAte());
		}

	return query.list();
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<EstudoCotaGerado> obterEstudosCota(Long idEstudo) {
	String hql = " from EstudoCotaGerado estudoCota where estudoCota.estudo.id = :estudo";

	Query query = super.getSession().createQuery(hql);
	query.setParameter("estudo", idEstudo);
		
	return query.list();
	}
	
	

	@Override
	public void removerEstudoCotaPorEstudo(Long idEstudo) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" delete from EstudoCotaGerado estudoCota");
		hql.append(" where estudoCota.estudo.id = :idEstudo");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idEstudo", idEstudo);
		
		query.executeUpdate();
	}
	
    @SuppressWarnings("unchecked")
    @Override
    public List<EstudoCotaGerado> obterEstudoCotaPorEstudo(EstudoGerado estudo) {

	String hql = " from EstudoCotaGerado estudoCota where estudoCota.estudo = :estudo ";

	Query query = super.getSession().createQuery(hql);

	query.setParameter("estudo", estudo);

	return query.list();
    }
    
    @Override
    @Transactional
    public void inserirProdutoBase(EstudoGerado estudo) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("insert into estudo_produto_edicao_base ");
		sql.append(" (estudo_id, produto_edicao_id, colecao, parcial, edicao_aberta, peso) ");
		sql.append(" values (:estudo_id, :produto_edicao_id, 0, 0, 0, 1) ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		query.setParameter("estudo_id", estudo.getId());
		query.setParameter("produto_edicao_id", estudo.getProdutoEdicao().getId());
		query.executeUpdate();
    }
    
    @Override
	@Transactional
    public void inserirProdutoBase(Long idEstudo, Long idProdutoEdicao, Long pesoEdicao, boolean parcial, boolean edicao_aberta, Long periodoParcial) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("insert into estudo_produto_edicao_base ");
		sql.append(" (estudo_id, produto_edicao_id, colecao, parcial, edicao_aberta, peso, periodo_parcial) ");
		sql.append(" values (:estudo_id, :produto_edicao_id, 0, :parcial, :edicao_aberta, :peso, :periodoParcial) ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("estudo_id", idEstudo);
		query.setParameter("produto_edicao_id", idProdutoEdicao);
		query.setParameter("peso", pesoEdicao);
		query.setParameter("parcial", parcial);
		query.setParameter("edicao_aberta", edicao_aberta);
		query.setParameter("periodoParcial", periodoParcial);
		
		query.executeUpdate();
    }
    
    @Override
	public void removerEstudosCotaPorEstudos(List<Long> listIdEstudos) {
		
		StringBuilder hql = new StringBuilder(" delete from EstudoCotaGerado ec ");
		
		hql.append(" where ec.estudo.id in (:listIdEstudos) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameterList("listIdEstudos", listIdEstudos);
		
		query.executeUpdate();
	}

	@Override
	public EstudoCotaGerado obterEstudoCotaGerado(Long cotaId, Long estudoId) {
		String hql = " from EstudoCotaGerado estudoCota "
				   + " where estudoCota.estudo.id = :estudo " 
				   + " and estudoCota.cota.id = :cotaId ";
		
		Query query = super.getSession().createQuery(hql);
		query.setParameter("estudo", estudoId);
		query.setParameter("cotaId", cotaId);
		query.setMaxResults(1);
		
		return (EstudoCotaGerado) query.uniqueResult();
	}

	@Override
	public void removerEstudoCotaGerado(Long idEstudoCotaGerado) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" delete from estudo_cota_gerado WHERE ID = :estudoCotaGeradoId ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("estudoCotaGeradoId", idEstudoCotaGerado);
		
		query.executeUpdate();
		
	}
	
	@Override
	public void gravarCotasEstudoCotaGerado(final List<EstudoCotaGerado> cotas){
//		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(cotas.toArray());
//		jdbcTemplate.batchUpdate(insertEstudoCotas, batch);

		if (cotas == null || cotas.isEmpty()) {
			return;
		}

		final Session session = this.getSession();

		session.doWork(new Work() {
			@Override
			public void execute(final Connection conn) throws SQLException {

				final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

				final StringBuilder sqlQry = new StringBuilder();

				sqlQry.append(" insert into ");
				sqlQry.append(" 	estudo_cota_gerado( ");
				sqlQry.append(" 		qtde_prevista, ");
				sqlQry.append(" 		qtde_efetiva, ");
				sqlQry.append(" 		estudo_id, ");
				sqlQry.append(" 		cota_id, ");
				sqlQry.append(" 		reparte_minimo, ");
				sqlQry.append(" 		reparte,			 ");
				sqlQry.append(" 		classificacao, ");
				sqlQry.append(" 		venda_media_nominal, ");
				sqlQry.append(" 		reparte_juramentado_a_faturar, ");
				sqlQry.append(" 		quantidade_pdvs,			 ");
				sqlQry.append(" 		reparte_maximo, ");
				sqlQry.append(" 		venda_media_mais_n,			 ");
				sqlQry.append(" 		indice_correcao_tendencia, ");
				sqlQry.append(" 		indice_venda_crescente, ");
				sqlQry.append(" 		percentual_encalhe_maximo, ");
				sqlQry.append(" 		mix, ");
				sqlQry.append(" 		venda_media, ");
				sqlQry.append(" 		cota_nova, ");
				sqlQry.append(" 		reparte_inicial ");
				sqlQry.append(" 	 ");
				sqlQry.append(" 	) values ( ");
				sqlQry.append(" 		:qtdePrevista, ");
				sqlQry.append(" 		:qtdeEfetiva, ");
				sqlQry.append(" 		:estudo.id, ");
				sqlQry.append(" 		:cota.id, ");
				sqlQry.append(" 		:reparteMinimo, ");
				sqlQry.append(" 		:reparte, ");
				sqlQry.append(" 		:classificacao, ");
				sqlQry.append(" 		:vendaMediaNominal, ");
				sqlQry.append(" 		:reparteJuramentadoAFaturar, ");
				sqlQry.append(" 		:quantidadePDVS, ");
				sqlQry.append(" 		:reparteMaximo, ");
				sqlQry.append(" 		:vendaMediaMaisN, ");
				sqlQry.append(" 		:indiceCorrecaoTendencia, ");
				sqlQry.append(" 		:indiceVendaCrescente, ");
				sqlQry.append(" 		:percentualEncalheMaximo, ");
				sqlQry.append(" 		:mix, ");
				sqlQry.append(" 		:vendaMedia, ");
				sqlQry.append(" 		:cotaNova, ");
				sqlQry.append(" 		:reparteInicial ");
				sqlQry.append(" 	) ");

				final SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(cotas.toArray());

				namedParameterJdbcTemplate.batchUpdate(sqlQry.toString(), params);

			}
		});
	        
	}
	
}
