package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaFaturamentoDTO;
import br.com.abril.nds.dto.CotaTransportadorDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroDTO;
import br.com.abril.nds.dto.ProcessamentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO.ColunaOrdenacao;
import br.com.abril.nds.dto.filtro.FiltroRelatorioServicosEntregaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class MovimentoFinanceiroCotaRepositoryImpl extends AbstractRepositoryModel<MovimentoFinanceiroCota, Long> 
												   implements MovimentoFinanceiroCotaRepository {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	public MovimentoFinanceiroCotaRepositoryImpl() {
		super(MovimentoFinanceiroCota.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<DebitoCreditoCota> obterValorFinanceiroNaoConsolidadoDeNegociacaoNaoAvulsaMaisEncargos(
	        final Integer numeroCota, final List<Date> datas, final Long idFornecedor) {
		
		final StringBuilder sql = new StringBuilder("");
		
		sql.append("    SELECT ");
		sql.append("        SUM(MFC.VALOR) AS valor, ");
		sql.append("        PN.DATA_VENCIMENTO AS dataVencimento, ");
		sql.append("        MFC.DATA as dataLancamento, ");
		sql.append("        MFC.OBSERVACAO as observacoes ");
		sql.append("    FROM ");
		sql.append("        PARCELA_NEGOCIACAO PN");
		sql.append("    INNER JOIN ");
		sql.append("        NEGOCIACAO N  ");
		sql.append("   ON ( ");
		sql.append("       N.ID = PN.NEGOCIACAO_ID ");
		sql.append("   )  ");
		sql.append("    INNER JOIN ");
		sql.append("        MOVIMENTO_FINANCEIRO_COTA MFC  ");
		sql.append("   ON ( ");
		sql.append("       PN.MOVIMENTO_FINANCEIRO_ID = MFC.ID       ");
		sql.append("   )  ");
		sql.append("    INNER JOIN ");
		sql.append("        COTA  ");
		sql.append("   ON ( ");
		sql.append("       COTA.ID = MFC.COTA_ID   ");
		sql.append("   )  ");
		sql.append("    LEFT JOIN ");
		sql.append("        CONSOLIDADO_MVTO_FINANCEIRO_COTA CMFC  ");
		sql.append("   ON ( ");
		sql.append("       CMFC.MVTO_FINANCEIRO_COTA_ID = MFC.ID     ");
		sql.append("   )  ");
		sql.append("    LEFT JOIN ");
		sql.append("        CONSOLIDADO_FINANCEIRO_COTA CFC  ");
		sql.append("   ON ( ");
		sql.append("       CFC.ID = CMFC.CONSOLIDADO_FINANCEIRO_ID   ");
		sql.append("   )  ");
		sql.append("    WHERE ");
		sql.append("        N.NEGOCIACAO_AVULSA = false  ");
		sql.append("        AND       COTA.NUMERO_COTA = :numeroCota  ");
		sql.append("        AND    CFC.ID IS NULL AND MFC.DATA IN (:datas) ");
		
		if (idFornecedor != null){
		    
		    sql.append(" AND MFC.FORNECEDOR_ID = :idFornecedor ");
		}
		
		sql.append("    GROUP BY ");
		sql.append("        PN.ID, ");
		sql.append("        MFC.ID ");
			
		final Map<String, Object> parameters = new HashMap<String, Object>();
		final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

		parameters.put("numeroCota", numeroCota);
		
		parameters.put("datas", datas);
		
		if (idFornecedor != null){
		    
		    parameters.put("idFornecedor", idFornecedor);
		}
		
		@SuppressWarnings("rawtypes")
		RowMapper cotaRowMapper = new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				final DebitoCreditoCota dto = new DebitoCreditoCota();
				
				dto.setValor(rs.getBigDecimal("valor"));
				dto.setDataVencimento(rs.getDate("dataVencimento"));
				dto.setDataLancamento(rs.getDate("dataLancamento"));
				dto.setTipoLancamentoEnum(OperacaoFinaceira.DEBITO);
				dto.setObservacoes(rs.getString("observacoes"));
				
				return dto;
			}
		};

		return (List<DebitoCreditoCota>) namedParameterJdbcTemplate.query(sql.toString(), parameters, cotaRowMapper);
	}
	
	/**
	 * Obtem movimentos financeiros da cota ainda nao processados e não consolidados
	 * 
	 * @param idCota
	 * @param dataOperacao
	 * @param tiposCota
	 * @return List<MovimentoFinanceiroCota> 
	 */
	@SuppressWarnings("unchecked")
	public List<MovimentoFinanceiroCota> obterMovimentoFinanceiroCota(Long idCota, Date dataOperacao, List<TipoCota> tiposCota){
		
		StringBuilder hql = new StringBuilder("select mfc ")
	   .append(" from MovimentoFinanceiroCota mfc ")
	   .append(" join mfc.cota cota ")
	   .append(" join mfc.fornecedor fornecedor ")
	   .append(" where mfc.data <= :dataOperacao ")
	   .append(" and mfc.status = :statusAprovado ");
		
		if (idCota != null){
			
			hql.append(" and cota.id = :idCota ");
		}
		
        if (tiposCota !=null && !tiposCota.isEmpty()){
			
        	hql.append(" and cota.tipoCota in (:tiposCota) ");
		}

		hql.append(" and cota.situacaoCadastro != :inativo and cota.situacaoCadastro != :pendente ")
		   .append(" and mfc.id not in (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ")
		   .append(" group by mfc.id ")
		   .append(" order by cota.id, fornecedor.id ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
		
		if (idCota != null){
			
			query.setParameter("idCota", idCota);
		}
			
		query.setParameter("inativo", SituacaoCadastro.INATIVO);
		query.setParameter("pendente", SituacaoCadastro.PENDENTE);
		query.setParameter("dataOperacao", dataOperacao);

        if (tiposCota !=null && !tiposCota.isEmpty()){
			
        	query.setParameterList("tiposCota", tiposCota);
		}

		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DebitoCreditoCota> obterDebitoCreditoCotaDataOperacao(final Integer numeroCota, final List<Date> datas, 
			final List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados, final Long idFornecedor){
		
		final StringBuilder sql = new StringBuilder();
		
		sql.append("select ");
		sql.append("	tipomovime1_.OPERACAO_FINANCEIRA as tipoLancamentoDescricao, ");
		sql.append("	tipomovime1_.DESCRICAO as observacoes, ");
		sql.append("	movimentof0_.VALOR as valor, ");
		sql.append("	movimentof0_.DATA as dataLancamento, ");
		sql.append("	movimentof0_.OBSERVACAO as descricao ");
		sql.append(" from ");
		sql.append("	MOVIMENTO_FINANCEIRO_COTA movimentof0_, ");
		sql.append("	TIPO_MOVIMENTO tipomovime1_ cross  ");
		sql.append(" join ");
		sql.append("	COTA cota3_  ");
		sql.append(" where ");
		sql.append("	movimentof0_.TIPO_MOVIMENTO_ID=tipomovime1_.ID  ");
		sql.append("	and movimentof0_.COTA_ID=cota3_.ID  ");
		sql.append("	and movimentof0_.DATA IN (:datas)  ");
		sql.append("	and movimentof0_.STATUS = :statusAprovado  ");
		sql.append("	and cota3_.NUMERO_COTA = :numeroCota  ");
		
		if(tiposMovimentoFinanceiroIgnorados!=null && !tiposMovimentoFinanceiroIgnorados.isEmpty()) {
			
			sql.append("	and ( ");
			sql.append("		movimentof0_.TIPO_MOVIMENTO_ID not in  ( ");
			sql.append("			:tiposMovimentoFinanceiroIgnorados ");
			sql.append("		) ");
			sql.append("	)  ");
		}
		
		sql.append("	and ( ");
		sql.append("		movimentof0_.ID not in  ( ");
		sql.append("			select ");
		sql.append("				distinct movimentof6_.ID  ");
		sql.append("			from ");
		sql.append("				CONSOLIDADO_FINANCEIRO_COTA consolidad4_  ");
		sql.append("			inner join ");
		sql.append("				CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos5_  ");
		sql.append("					on consolidad4_.ID=movimentos5_.CONSOLIDADO_FINANCEIRO_ID  ");
		sql.append("			inner join ");
		sql.append("				MOVIMENTO_FINANCEIRO_COTA movimentof6_  ");
		sql.append("					on movimentos5_.MVTO_FINANCEIRO_COTA_ID=movimentof6_.ID cross  ");
		sql.append("			join ");
		sql.append("				COTA cota7_  ");
		sql.append("			where ");
		sql.append("				consolidad4_.COTA_ID=cota7_.ID  ");
		sql.append("				and cota7_.NUMERO_COTA = :numeroCota ");
		sql.append("		) ");
		sql.append("	)  ");
		
		if (idFornecedor != null){
		    
		    sql.append(" and movimentof0_.FORNECEDOR_ID = :idFornecedor ");
		}
		
		sql.append(" order by ");
		sql.append("	movimentof0_.DATA ");
		
		final SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setResultTransformer(new AliasToBeanResultTransformer(DebitoCreditoCota.class));
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO.name());
		query.setParameter("numeroCota", numeroCota);
		query.setParameterList("datas", datas);
		
		if(tiposMovimentoFinanceiroIgnorados!=null && 
				!tiposMovimentoFinanceiroIgnorados.isEmpty()) {
			
			query.setParameterList("tiposMovimentoFinanceiroIgnorados", getListaTiposMovimentoFinanceiroIgnorados(tiposMovimentoFinanceiroIgnorados));
		}
		
		if (idFornecedor != null){
            
            query.setParameter("idFornecedor", idFornecedor);
        }
		
		query.addScalar("tipoLancamentoDescricao", StandardBasicTypes.STRING);
		query.addScalar("descricao", StandardBasicTypes.STRING);
		query.addScalar("valor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("dataLancamento", StandardBasicTypes.DATE);
		query.addScalar("observacoes", StandardBasicTypes.STRING);
		
		return (List<DebitoCreditoCota>) query.list();
	}
	
	private List<Long> getListaTiposMovimentoFinanceiroIgnorados(
			List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados) {
		ArrayList<Long> ret = new ArrayList<Long>();
		for(TipoMovimentoFinanceiro tipo : tiposMovimentoFinanceiroIgnorados){
			ret.add(tipo.getId());
		}
		
		return ret;
	}

	@SuppressWarnings("unchecked")
	public List<DebitoCreditoCota> obterDebitoCreditoSumarizadosParaCotaDataOperacao(Integer numeroCota, Date dataOperacao, List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados){
		
		StringBuilder hql = new StringBuilder(" select ");
		
		hql.append(" mfc.tipoMovimento.operacaoFinaceira as tipoLancamento, ");
		hql.append(" sum(mfc.valor) as valor ");
		
		hql.append(" from MovimentoFinanceiroCota mfc ");
		   
		hql.append(" where ");
		
		hql.append(" mfc.data = :dataOperacao ");
		
		hql.append(" and mfc.status = :statusAprovado ");
		
		hql.append(" and mfc.cota.numeroCota = :numeroCota ");
		
		if(tiposMovimentoFinanceiroIgnorados!=null && !tiposMovimentoFinanceiroIgnorados.isEmpty()) {
			hql.append(" and mfc.tipoMovimento not in (:tiposMovimentoFinanceiroIgnorados) ");
		}
		
		hql.append(" and mfc.id not in ");
		
		hql.append(" (   ");
		
		hql.append(" select distinct(movimentos.id) ");

		hql.append(" from ConsolidadoFinanceiroCota c join c.movimentos movimentos ");
		
		hql.append(" where ");
		
		hql.append(" c.cota.numeroCota = :numeroCota  ");
		
		hql.append(" ) ");
		
		hql.append(" group by mfc.tipoMovimento.operacaoFinaceira ");

		Query query = this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(DebitoCreditoCota.class));
		
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("dataOperacao", dataOperacao);
		
		if(tiposMovimentoFinanceiroIgnorados!=null && !tiposMovimentoFinanceiroIgnorados.isEmpty()) {
			query.setParameterList("tiposMovimentoFinanceiroIgnorados", tiposMovimentoFinanceiroIgnorados);
		}
		
		return query.list();
	}
								   
	@SuppressWarnings("unchecked")
	public List<MovimentoFinanceiroCota> obterMovimentoFinanceiroDaOperacaoConferenciaEncalhe(Long idControleConfEncalheCota) {

		StringBuffer hql = new StringBuffer();
		
		hql.append(" select mfc from ");
		
		hql.append(" ControleConferenciaEncalheCota ccec ");

		hql.append(" inner join ccec.conferenciasEncalhe confEncalhe	");

		hql.append(" inner join confEncalhe.movimentoEstoqueCota mec	");
		
		hql.append(" inner join mec.movimentoFinanceiroCota mfc			");
		
		hql.append(" where	");
		
		hql.append(" ccec.id = :idControleConfEncalheCota ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idControleConfEncalheCota", idControleConfEncalheCota);
		
		query.setMaxResults(1);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public  List<DebitoCreditoCota> obterDebitoCreditoPorPeriodoOperacao(FiltroConsultaEncalheDTO filtro, List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados){
		
		StringBuilder hql = new StringBuilder(" select ");
		
		hql.append(" mfc.tipoMovimento.operacaoFinaceira as tipoLancamento, ");
		
		hql.append(" mfc.valor as valor, ");
		
		hql.append(" mfc.data as dataLancamento, ");
		
		hql.append(" mfc.observacao as observacoes");

		hql.append(" from MovimentoFinanceiroCota mfc ");
		   
		hql.append(" where ");
		
		hql.append(" mfc.data between :dataOperacaoInicial and :dataOperacaoFinal");
		
		hql.append(" and mfc.status = :statusAprovado ");
		
		hql.append(" and mfc.cota.id = :idCota ");
		
		if(tiposMovimentoFinanceiroIgnorados!=null && !tiposMovimentoFinanceiroIgnorados.isEmpty()) {
			hql.append(" and mfc.tipoMovimento not in (:tiposMovimentoFinanceiroIgnorados) ");
		}
		
		hql.append(" and mfc.id not in ");
		
		hql.append(" (   ");
		
		hql.append(" select distinct(movimentos.id) ");

		hql.append(" from ConsolidadoFinanceiroCota c join c.movimentos movimentos ");
		
		hql.append(" where ");
		
		hql.append(" c.cota.id = :idCota  ");
		
		hql.append(" ) ");
		
		hql.append(" order by mfc.data ");
		
		Query query = this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(DebitoCreditoCota.class));
		
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);

		query.setParameter("idCota", filtro.getIdCota());

		query.setParameter("dataOperacaoInicial", filtro.getDataRecolhimentoInicial());

		query.setParameter("dataOperacaoFinal", filtro.getDataRecolhimentoFinal());

		if(tiposMovimentoFinanceiroIgnorados!=null && !tiposMovimentoFinanceiroIgnorados.isEmpty()) {
			query.setParameterList("tiposMovimentoFinanceiroIgnorados", tiposMovimentoFinanceiroIgnorados);
		}
		
		return query.list();
	}

	@Override
	public Integer obterContagemMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {
		
		String hql = " select count(movimentoFinanceiroCota) " + 
					 getQueryObterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);

		Query query = criarQueryObterMovimentosFinanceiroCota(hql, filtroDebitoCreditoDTO);

		return ((Long) query.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> obterIdsMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {
		
		String hql = " select movimentoFinanceiroCota.id " + 
					 getQueryObterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);

		Query query = criarQueryObterMovimentosFinanceiroCota(hql, filtroDebitoCreditoDTO);

		return (List<Long>) query.list();
	}

	@Override
	public BigDecimal obterSomatorioValorMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {
		
		String hql = " select sum(movimentoFinanceiroCota.valor) " + 
					 getQueryObterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);

		Query query = criarQueryObterMovimentosFinanceiroCota(hql, filtroDebitoCreditoDTO);

		return (BigDecimal) query.uniqueResult();
	}
	
	/**
	 * @see br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository#obterMovimentosFinanceiroCota()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {

		String hql = " select  movimentoFinanceiroCota "+getQueryObterMovimentosFinanceiroCota(filtroDebitoCreditoDTO) +
					 getOrderByObterMovimentosFinanceiroCota(filtroDebitoCreditoDTO); 

		Query query = criarQueryObterMovimentosFinanceiroCota(hql, filtroDebitoCreditoDTO);

		if (filtroDebitoCreditoDTO.getPaginacao() != null 
				&& filtroDebitoCreditoDTO.getPaginacao().getPosicaoInicial() != null) { 
			
			boolean selecionouFiltro = false;
			if(filtroDebitoCreditoDTO.getNumeroCota() != null || filtroDebitoCreditoDTO.getIdTipoMovimento() != null ||
					filtroDebitoCreditoDTO.getDataLancamentoInicio() != null || filtroDebitoCreditoDTO.getDataLancamentoFim() != null ||
					filtroDebitoCreditoDTO.getDataVencimentoInicio() != null || filtroDebitoCreditoDTO.getDataVencimentoFim() != null){
				selecionouFiltro = true;
			}
			
			if(!selecionouFiltro){
				query.setFirstResult(filtroDebitoCreditoDTO.getPaginacao().getPosicaoInicial());
			}else{
				
				int obterContagemMovimentosFinanceiroCota = obterContagemMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
				
				if(filtroDebitoCreditoDTO.getPaginacao().getQtdResultadosPorPagina() < obterContagemMovimentosFinanceiroCota){
					
					query.setFirstResult(filtroDebitoCreditoDTO.getPaginacao().getPosicaoInicial());
				}
			}
		
			query.setMaxResults(filtroDebitoCreditoDTO.getPaginacao().getQtdResultadosPorPagina());
		}
		
		return query.list();
	}
	
	private String getQueryObterMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {

		StringBuilder hql = new StringBuilder();

		hql.append(" from MovimentoFinanceiroCota movimentoFinanceiroCota ");

		String conditions = " where movimentoFinanceiroCota.tipoMovimento.id in ( " + getTipoMovimentoPorGrupoFinanceiros()+" ) ";

		if (filtroDebitoCreditoDTO.getIdTipoMovimento() != null) {

			conditions += " and movimentoFinanceiroCota.tipoMovimento.id in ( :idTipoMovimento) ";
		}

		if (filtroDebitoCreditoDTO.getDataLancamentoInicio() != null && 
				filtroDebitoCreditoDTO.getDataLancamentoFim() != null) {
			
			conditions += " and movimentoFinanceiroCota.dataCriacao between :dataLancamentoInicio and :dataLancamentoFim ";
		}
		
		if (filtroDebitoCreditoDTO.getDataVencimentoInicio() != null && 
				filtroDebitoCreditoDTO.getDataVencimentoFim() != null) {
			
			conditions += " and movimentoFinanceiroCota.data between :dataVencimentoInicio and :dataVencimentoFim ";
		}

		if (filtroDebitoCreditoDTO.getNumeroCota() != null) {

			conditions += " and movimentoFinanceiroCota.cota.numeroCota = :numeroCota ";
		}

		hql.append(conditions);
		
		return hql.toString();
	}

	private StringBuilder getTipoMovimentoPorGrupoFinanceiros() {
		
		StringBuilder hql = new StringBuilder("select t.id ");
		hql.append(" from TipoMovimentoFinanceiro t ")
		   .append(" where t.grupoMovimentoFinaceiro in (:grupoMovimentosFinanceiros)");
	
		return hql;
	}

	private Query criarQueryObterMovimentosFinanceiroCota(String hql, FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {
		
		Query query = getSession().createQuery(hql);
		
		query.setParameterList("grupoMovimentosFinanceiros", filtroDebitoCreditoDTO.getGrupoMovimentosFinanceirosDebitosCreditos());
		
		if (filtroDebitoCreditoDTO.getIdTipoMovimento() != null) {
			
			if(filtroDebitoCreditoDTO.getIdsTipoMovimentoTaxaEntrega()!= null){
				
				filtroDebitoCreditoDTO.getIdsTipoMovimentoTaxaEntrega().add(filtroDebitoCreditoDTO.getIdTipoMovimento());
				
				query.setParameterList("idTipoMovimento", filtroDebitoCreditoDTO.getIdsTipoMovimentoTaxaEntrega());
			}
			else{
				
				query.setParameterList("idTipoMovimento", Arrays.asList(filtroDebitoCreditoDTO.getIdTipoMovimento()));
			}
		}

		if (filtroDebitoCreditoDTO.getDataLancamentoInicio() != null && 
				filtroDebitoCreditoDTO.getDataLancamentoFim() != null) {

			query.setParameter("dataLancamentoInicio", filtroDebitoCreditoDTO.getDataLancamentoInicio());
			query.setParameter("dataLancamentoFim", filtroDebitoCreditoDTO.getDataLancamentoFim());
		}
		
		if (filtroDebitoCreditoDTO.getDataVencimentoInicio() != null && 
				filtroDebitoCreditoDTO.getDataVencimentoFim() != null) {
			
			query.setParameter("dataVencimentoInicio", filtroDebitoCreditoDTO.getDataVencimentoInicio());
			query.setParameter("dataVencimentoFim", filtroDebitoCreditoDTO.getDataVencimentoFim());
		}

		if (filtroDebitoCreditoDTO.getNumeroCota() != null) {

			query.setParameter("numeroCota", filtroDebitoCreditoDTO.getNumeroCota());
		}
		
		return query;
	}
	
	private String getOrderByObterMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {

		ColunaOrdenacao colunaOrdenacao = filtroDebitoCreditoDTO.getColunaOrdenacao();
		
		String orderBy = " order by ";
		
		switch (colunaOrdenacao) {
		
		case DATA_LANCAMENTO:
			orderBy += " movimentoFinanceiroCota.dataCriacao ";
			break;
		case DATA_VENCIMENTO:
			orderBy += " movimentoFinanceiroCota.data ";
			break;
		case NOME_COTA:
			orderBy += " case when movimentoFinanceiroCota.cota.pessoa.class = 'PessoaJuridica' " 
					+ " then movimentoFinanceiroCota.cota.pessoa.razaoSocial " 
					+ " else movimentoFinanceiroCota.cota.pessoa.nome  " 
					+ " end ";
			break;
		case NUMERO_COTA:
			orderBy += " movimentoFinanceiroCota.cota.numeroCota ";
			break;
		case OBSERVACAO:
			orderBy += " movimentoFinanceiroCota.observacao ";
			break;
		case TIPO_LANCAMENTO:
			orderBy += " movimentoFinanceiroCota.tipoMovimento.descricao ";
			break;
		case VALOR:
			orderBy += " movimentoFinanceiroCota.valor ";
			break;
		default:
			orderBy += " movimentoFinanceiroCota.tipoMovimento.descricao ";
			break;
		}
		
		orderBy += filtroDebitoCreditoDTO.getPaginacao().getOrdenacao();

		return orderBy;
	}
	
	/**
	 * Obtém movimentos financeiros de uma cota por operação financeira
	 * @param numeroCota
	 * @param operacao
	 * @return BigDecimal valor
	 */
	@Override
	public BigDecimal obterSaldoCotaPorOperacao(Integer numeroCota, OperacaoFinaceira operacao) {
		
		StringBuilder hql = new StringBuilder(" select ");

		if (operacao == OperacaoFinaceira.CREDITO){
		    hql.append(" sum(mfc.valor - mfc.baixaCobranca.valorJuros - mfc.baixaCobranca.valorMulta + mfc.baixaCobranca.valorDesconto) ");
	    }
		if (operacao == OperacaoFinaceira.DEBITO){
			hql.append(" sum(mfc.valor + mfc.baixaCobranca.valorJuros + mfc.baixaCobranca.valorMulta - mfc.baixaCobranca.valorDesconto) ");
		}
		
		hql.append(" from MovimentoFinanceiroCota mfc ");

		hql.append(" where mfc.status = :statusAprovado ");
		
		hql.append(" and mfc.cota.numeroCota = :numeroCota ");
		
		hql.append(" and mfc.tipoMovimento.operacaoFinaceira = :operacao");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("operacao", operacao);

		return (BigDecimal) query.uniqueResult();
	}
	
	/**
	 * Obtém o movimentos de uma cobrança
	 * @param idCobranca
	 * @return List<MovimentoFinanceiroCota>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentoFinanceiroCota> obterMovimentosFinanceirosPorCobranca(Long idCobranca) {
		StringBuilder hql = new StringBuilder();

		hql.append(" from MovimentoFinanceiroCota mfc ");
		hql.append(" join mfc.baixaCobranca baixaCobranca ");
		hql.append(" join baixaCobranca.cobranca cobranca ");
		hql.append(" where baixaCobranca.status = :status ");
		hql.append(" and cobranca.id = :idCobranca ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("status", StatusBaixa.PAGAMENTO_PARCIAL);
		
		query.setParameter("idCobranca", idCobranca);

		return query.list();
	}
	
	/**
	 * Obtém movimentos financeiros de uma cobrança por operação
	 * @param idCobranca
	 * @param operacao
	 * @return BigDecimal valor
	 */
	@Override
	public BigDecimal obterSaldoCobrancaPorOperacao(Long idCobranca, OperacaoFinaceira operacao) {
		
		StringBuilder hql = new StringBuilder(" select ");

		if (operacao == OperacaoFinaceira.CREDITO){
		    hql.append(" sum(mfc.valor - mfc.baixaCobranca.valorJuros - mfc.baixaCobranca.valorMulta + mfc.baixaCobranca.valorDesconto) ");
	    }
		if (operacao == OperacaoFinaceira.DEBITO){
			hql.append(" sum(mfc.valor + mfc.baixaCobranca.valorJuros + mfc.baixaCobranca.valorMulta - mfc.baixaCobranca.valorDesconto) ");
		}
	
		hql.append(" from MovimentoFinanceiroCota mfc ");

		hql.append(" where mfc.baixaCobranca.status = :status ");
		
		hql.append(" and mfc.baixaCobranca.cobranca.id = :idCobranca ");
		
		hql.append(" and mfc.tipoMovimento.operacaoFinaceira = :operacao");
		
		Query query = this.getSession().createQuery(hql.toString());
		
        query.setParameter("status", StatusBaixa.PAGAMENTO_PARCIAL);
		
		query.setParameter("idCobranca", idCobranca);
		
		query.setParameter("operacao", operacao);

		return (BigDecimal) query.uniqueResult();
	}

	/**
	 * Obtém faturamento das cotas por período
	 * @param cotas
	 * @param dataInicial
	 * @param dataFinal
	 * @return List<CotaFaturamentoDTO>: Faturamento das Cotas
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CotaFaturamentoDTO> obterFaturamentoCotasPorPeriodo(List<Cota> cotas,Date dataInicial, Date dataFinal) {

		StringBuilder hql = new StringBuilder(" select ");

		hql.append(" c.id as idCota, ");
        
	    hql.append(" COALESCE(sum( ");
	    
	    hql.append("              case when mec.tipoMovimento.grupoMovimentoEstoque.operacaoEstoque = :grupoEntrada then mec.qtde else (mec.qtde * -1) end ");
	    
	    hql.append("              * (COALESCE(valApl.precoVenda,0)) ");
	    
	    hql.append("         ),0 ) as faturamentoBruto,");
	    
	    hql.append(" COALESCE(sum( ");
	    
	    hql.append("              case when mec.tipoMovimento.grupoMovimentoEstoque.operacaoEstoque = :grupoEntrada then mec.qtde else (mec.qtde * -1) end ");
	    
	    hql.append("              * (COALESCE(valApl.precoComDesconto, valApl.precoVenda, 0)) ");
	    
	    hql.append("         ),0 ) as faturamentoLiquido ");
	    
		hql.append(" from MovimentoEstoqueCota mec ");
		
		hql.append(" join mec.valoresAplicados valApl ");
		
		hql.append(" join mec.cota c ");
		
		hql.append(" join mec.estoqueProdutoCota epc ");
		
		hql.append(" join epc.produtoEdicao produtoEdicao ");
		
		hql.append(" join produtoEdicao.produto produto ");
		
		hql.append(" join produtoEdicao.lancamentos lanc ");
		
		hql.append(" left join produto.fornecedores fornecedor ");
		
		hql.append(" where mec.status = :status  ");
		
		hql.append(" and lanc.status in (:statusLancamento) ");
	    
		hql.append(" and ( lanc.dataRecolhimentoDistribuidor between :dataInicial and :dataFinal )  ");
	    
		hql.append(" and c in (:cotas) ");
		
		hql.append(" group by c.id ");
	    
		Query query = this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(CotaFaturamentoDTO.class));
		
		query.setParameterList("cotas", cotas);
		
		query.setParameter("status", StatusAprovacao.APROVADO);
		
		query.setParameter("dataInicial", dataInicial);
		
		query.setParameter("dataFinal", dataFinal);
		
		query.setParameterList("statusLancamento", Arrays.asList(
                StatusLancamento.EM_RECOLHIMENTO, 
                StatusLancamento.RECOLHIDO, 
                StatusLancamento.FECHADO));
		
		query.setParameter("grupoEntrada", OperacaoEstoque.ENTRADA);
		
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CotaTransportadorDTO> obterResumoTransportadorCota(FiltroRelatorioServicosEntregaDTO filtro) {

		PaginacaoVO paginacaoVO = filtro.getPaginacao();
		HashMap<String, Object> param = new HashMap<String, Object>();
				
		StringBuilder hql = new StringBuilder();
		
		hql.append("select 	 pessoaTransportador.razaoSocial as transportador, " +
						 	"roteiro.descricaoRoteiro as roteiro, " +
						 	"rota.descricaoRota as rota, " +
						 	"cota.numeroCota as numCota, " +
						 	"pessoaCota.nome as nomeCota, " +
						 	"sum(movimento.valor) as valor, " +
						 	"transportador.id as idTransportador, " +
						 	"cota.id as idCota ");
		
		addFromJoinObterDadosTransportador(hql);
		
		addWhereObterDadosTransportador(hql, filtro.getEntregaDataInicio(), filtro.getEntregaDataFim(), filtro.getIdTransportador(), param);
		
		hql.append(" group by transportador.id ");
		
		getOrderByObterResumoTransportadorCota(paginacaoVO); 
		
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, param);
		if (paginacaoVO != null && paginacaoVO.getPosicaoInicial() != null) { 
			
			query.setFirstResult(paginacaoVO.getPosicaoInicial());
			
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(CotaTransportadorDTO.class));
		
		return query.list();
	}
		
	private String getOrderByObterResumoTransportadorCota(PaginacaoVO paginacaoVO) {

		String orderBy = " order by ";
		
		String coluna = paginacaoVO.getSortColumn();
		
		if ("roteiro".equals(coluna))
			orderBy += " roteiro ";
		else if("rota".equals(coluna))
			orderBy += " rota ";
		else if("numCota".equals(coluna))
			orderBy += " numCota ";
		else if("nomeCota".equals(coluna))
			orderBy += " nomeCota ";
		else if("valor".equals(coluna))
			orderBy += " valor ";
		
		orderBy += paginacaoVO.getSortOrder();
		
		return orderBy;
	}

	private void addFromJoinObterDadosTransportador(StringBuilder hql) {

		hql.append(" from MovimentoFinanceiroCota movimento ");
		hql.append(" 	join movimento.cota cota ");
		hql.append("    join cota.pdvs pdv ");
		hql.append("    join cota.pessoa pessoaCota ");
		hql.append("    join pdv.rotas rotaPDV  ");
		hql.append("    join rotaPDV.rota rota ");
		hql.append("    join rota.roteiro roteiro ");
		hql.append("    join rota.associacoesVeiculoMotoristaRota assossiacaoRotaVeiculo ");
		hql.append("    join assossiacaoRotaVeiculo.transportador transportador ");
		hql.append("    join transportador.pessoaJuridica pessoaTransportador ");
		
	}
	
	private void addWhereObterDadosTransportador(StringBuilder hql, Date dataDe, Date dataAte, Long idTransportador, HashMap<String, Object> params) {

		hql.append(" where movimento.tipoMovimento.grupoMovimentoFinaceiro = :grupoMovimento");
		params.put("grupoMovimento", GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
		
		if (dataDe != null) {
			hql.append(" and movimento.dataCriacao >= :dataDe ");			
			params.put("dataDe", dataDe);
		}

		if (dataAte != null) {
			hql.append(" and movimento.dataCriacao <= :dataAte ");
			params.put("dataAte", dataAte);
		}
	
		if (idTransportador != null) {
			hql.append(" and transportador.id = :idTransportador ");
			params.put("idTransportador", idTransportador);
		}		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentoFinanceiroDTO> obterDetalhesTrasportadorPorCota(FiltroRelatorioServicosEntregaDTO filtro) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
				
		hql.append("select 	 movimento.observacao as descricao, " +
						 	"movimento.valor as valor, " +
						 	"movimento.dataCriacao as data  ");
		
		addFromJoinObterDadosTransportador(hql);
		
		addWhereobterDetalhesTrasportadorPorCota(hql, filtro.getEntregaDataInicio(), filtro.getEntregaDataFim(), filtro.getIdTransportador(), filtro.getIdCota(), param);
				
		hql.append(" order by movimento.dataCriacao asc ");
		
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, param);
	
		query.setResultTransformer(Transformers.aliasToBean(MovimentoFinanceiroDTO.class));
		
		return query.list();
	}

	private void addWhereobterDetalhesTrasportadorPorCota(StringBuilder hql, Date dataDe, Date dataAte, Long idTransportador, Long idCota, HashMap<String, Object> params) {

		hql.append(" where movimento.tipoMovimento.grupoMovimentoFinaceiro = :grupoMovimento");
		params.put("grupoMovimento", GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
		
		if (dataDe != null) {
			hql.append(" and movimento.dataCriacao >= :dataDe ");			
			params.put("dataDe", dataDe);
		}

		if (dataAte != null) {
			hql.append(" and movimento.dataCriacao <= :dataAte ");
			params.put("dataAte", dataAte);
		}
	
		if (idTransportador != null) {
			hql.append(" and transportador.id = :idTransportador ");
			params.put("idTransportador", idTransportador);
		}		
		
		if (idCota != null) {
			hql.append(" and cota.id = :idCota");
			params.put("idCota", idCota);
		}		
	}

	@Override
	public Long obterCountResumoTransportadorCota(FiltroRelatorioServicosEntregaDTO filtro) {
		PaginacaoVO paginacaoVO = filtro.getPaginacao();
		HashMap<String, Object> param = new HashMap<String, Object>();
				
		StringBuilder hql = new StringBuilder();
		
		hql.append("select 	 pessoaTransportador.razaoSocial as transportador, " +
						 	"roteiro.descricaoRoteiro as roteiro, " +
						 	"rota.descricaoRota as rota, " +
						 	"cota.numeroCota as numCota, " +
						 	"pessoaCota.nome as nomeCota, " +
						 	"sum(movimento.valor) as valor, " +
						 	"transportador.id as idTransportador, " +
						 	"cota.id as idCota ");
		
		addFromJoinObterDadosTransportador(hql);
		
		addWhereObterDadosTransportador(hql, filtro.getEntregaDataInicio(), filtro.getEntregaDataFim(), filtro.getIdTransportador(), param);
		
		hql.append(" group by transportador.id ");
		
		getOrderByObterResumoTransportadorCota(paginacaoVO); 
		
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, param);
		
		if (paginacaoVO != null && paginacaoVO.getPosicaoInicial() != null) { 
			
			query.setFirstResult(paginacaoVO.getPosicaoInicial());
			
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(CotaTransportadorDTO.class));
		
		return Long.parseLong(String.valueOf(query.list()));
	}
		
	@Override
	public BigDecimal obterSaldoDistribuidor(Date data, 
									 	     TipoCota tipoCota, 
									 	     OperacaoFinaceira operacaoFinaceira) {

		StringBuilder hql = new StringBuilder(" select sum(mfc.valor) ");
		
		hql.append(" from MovimentoFinanceiroCota mfc ");

		hql.append(" where mfc.status = :statusAprovacao ");
		
		if (operacaoFinaceira != null) {
			
			hql.append(" and mfc.tipoMovimento.operacaoFinaceira = :operacaoFinanceira ");
		}
		
		if (tipoCota != null) {
			
			hql.append(" and mfc.cota.parametroCobranca.tipoCota = :tipoCota ");
		}
		
		if (data != null) {
		
			hql.append(" and mfc.data = :data ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("statusAprovacao", StatusAprovacao.APROVADO);

		if (operacaoFinaceira != null) {
			
			query.setParameter("operacaoFinanceira", operacaoFinaceira);
		}
		
		if (tipoCota != null) {
			
			query.setParameter("tipoCota", tipoCota);
		}
		
		if (data != null) {
			
			query.setParameter("data", data);
		}

		Object result = query.uniqueResult();

		return (result == null) ? BigDecimal.ZERO : (BigDecimal) result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentoFinanceiroCota> obterMovimentosFinanceirosCotaPorTipoMovimento(
			Long idCota, Long idConsolidado, Collection<TipoMovimentoFinanceiro> tiposMovimento, 
			Date dataCriacao){
		
		StringBuilder hql = new StringBuilder("select m from MovimentoFinanceiroCota m ");
		boolean indWhere = false;
		
		if (idConsolidado != null){
			
			hql.append(" join m.consolidadoFinanceiroCota c ")
			   .append(" where c.id = :idConsolidado ");
			
			indWhere = true;
		}
		
		if (idCota != null){
			
			hql.append(indWhere ? " and " : " where ")
			   .append(" m.cota.id = :idCota ");
			
			indWhere = true; 
		}
		
		if (dataCriacao != null){
			
			hql.append(indWhere ? " and " : " where ")
			   .append(" m.dataCriacao = :data ");
			indWhere = true;
		}
		
		hql.append(indWhere ? " and " : " where ")
		   .append(" m.tipoMovimento in (:postergados) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (idConsolidado != null){
			
			query.setParameter("idConsolidado", idConsolidado);
		}
		
		if (idCota != null){
			
			query.setParameter("idCota", idCota);
		}
		
		if (dataCriacao != null){
			
			query.setParameter("data", DateUtil.removerTimestamp(dataCriacao));
		}
		
		query.setParameterList("postergados", tiposMovimento);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DebitoCreditoCota> obterCreditoDebitoCota(Long idConsolidado, Date dataCriacao,
			Integer numeroCota, List<TipoMovimentoFinanceiro> tiposMovimento, String sortorder, String sortname){
		
		StringBuilder hql = new StringBuilder("select ");
		boolean indWhere = false;
		
		hql.append(" m.data as dataLancamento, ")
		   .append(" m.tipoMovimento.grupoMovimentoFinaceiro as tipoMovimento, ")
		   .append(" m.valor as valor, ")
		   .append(" m.observacao as observacoes ")
		   .append(" from MovimentoFinanceiroCota m ")
		   .append(" join m.cota cota ");
		
		if (idConsolidado != null){
			
			hql.append(" join m.consolidadoFinanceiroCota consolidado ")
			   .append(" where consolidado.id = :idConsolidado");
			indWhere = true;
		}
		
		if (dataCriacao != null){
			
			hql.append(indWhere ? " and " : " where ")
			   .append(" m.data = :dataCriacao ");
			indWhere = true;
		}
		
		hql.append(indWhere ? " and " : " where ")
		   .append(" m.tipoMovimento in (:tiposMovimento)")
		   .append(" and cota.numeroCota = :numeroCota ");
		
		if (idConsolidado == null){
			
			hql.append(" and m.id not in ")
			   .append(" (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ");
		}
		
		if (sortname != null){
			
			hql.append(" order by ").append(sortname);
			
			if (sortorder != null){
				
				hql.append(" ").append(sortorder);
			}
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (idConsolidado != null){
			
			query.setParameter("idConsolidado", idConsolidado);
		}
		
		if (dataCriacao != null){
			
			query.setParameter("dataCriacao", dataCriacao);
		}
		
		query.setParameterList("tiposMovimento", tiposMovimento);
		query.setParameter("numeroCota", numeroCota);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DebitoCreditoCota.class));
		
		return query.list();
	}
	
	@Override
	public BigDecimal obterSomatorioTipoMovimentoPorConsolidado(Long idConsolidado, Date data, 
			Integer numeroCota, Collection<TipoMovimentoFinanceiro> tiposMovimento){
		
		StringBuilder hql = new StringBuilder("select sum (m.valor) ");
		boolean indWhere = false;
		
		hql.append(" from MovimentoFinanceiroCota m ")
		   .append(" join m.cota cota ");
		
		if (idConsolidado != null){
			
			hql.append(" join m.consolidadoFinanceiroCota consolidado ")
			   .append(" where consolidado.id = :idConsolidado");
			indWhere = true;
		}
		
		if (data != null){
			
			hql.append(indWhere ? " and " : " where ")
			   .append(" m.data = :data ");
			indWhere = true;
		}
		
		hql.append(indWhere ? " and " : " where ")
		   .append(" m.tipoMovimento in (:tiposMovimento)")
		   .append(" and cota.numeroCota = :numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (idConsolidado != null){
			
			query.setParameter("idConsolidado", idConsolidado);
		}
		
		if (data != null){
			
			query.setParameter("data", data);
		}
		
		query.setParameterList("tiposMovimento", tiposMovimento);
		query.setParameter("numeroCota", numeroCota);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	/**
	 * Obtem Quantidade de Informações para o processamento financeiro (Geração de MovimentoFinanceiroCota, Divida e Cobrança) das Cotas
	 * @param numeroCota
	 * @return Long
	 */
	@Override
	public Long obterQuantidadeProcessamentoFinanceiroCota(Integer numeroCota){
	    
	    StringBuilder hql = new StringBuilder("select ");
	    
	    hql.append(" count(c) ")
	
	       .append(" from Cota c ")
	       
	       .append(" where c.tipoCota = :tipoCota ");
	    
	    if (numeroCota != null){
	      
	      hql.append(" and c.numeroCota = :numeroCota ");
	    }
	    
	    Query query = this.getSession().createQuery(hql.toString());
	
	    if (numeroCota != null){
	      
	      query.setParameter("numeroCota", numeroCota);
	    }
	    
	    query.setParameter("tipoCota", TipoCota.A_VISTA);
	    
	    return (Long) query.uniqueResult();
	}
    
    /**
	 * FROM: Movimentos financeiros de Debito da cota
	 * @param paramIdCota
	 * @return String
	 */
    public String getFromDebitoCota(String paramIdCota){
    	
    	StringBuilder hql = new StringBuilder("")
    	
    	.append("  from MovimentoFinanceiroCota mfc ")
	       
	       .append("  join mfc.cota c3 ")
	       
	       .append("  join mfc.tipoMovimento tm ")
	    
	       .append("  where mfc.data <= :data ")
	       
	       .append("  and tm.grupoMovimentoFinaceiro in (:gruposMovimentoFinanceiroDebito) ")
	    
	       .append("  and mfc.id not in (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ")
	    
	       .append("  and c3.id = ").append(paramIdCota);
    	
		return hql.toString();
	}
    
    /**
   	 * FROM: Movimentos financeiros de Credito da cota
   	 * @param paramIdCota
   	 * @return String
   	 */
    public String getFromCreditoCota(String paramIdCota){
    	
    	StringBuilder hql = new StringBuilder("")
    	
    	.append("  from MovimentoFinanceiroCota mfc ")
	       
	      .append("  join mfc.cota c4 ")
	       
	      .append("  join mfc.tipoMovimento tm ")
	    
	      .append("  where mfc.data <= :data ")
	       
	      .append("  and tm.grupoMovimentoFinaceiro in (:gruposMovimentoFinanceiroCredito) ")
	       
	      .append("  and mfc.id not in (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ")
	       
	      .append("  and c4.id = ").append(paramIdCota);
    	
		return hql.toString();
	}
    
    /**
   	 * FROM: Movimentos financeiros de Débito Pendentes da cota
   	 * @param paramIdCota
   	 * @return String
   	 */
    public String getFromPendenteDebitoCota(String paramIdCota){
    	
    	StringBuilder hql = new StringBuilder("")
    	
    	.append("  from MovimentoFinanceiroCota mfc ")
	       
	      .append("  join mfc.cota c4 ")
	       
	      .append("  join mfc.tipoMovimento tm ")
	    
	      .append("  where mfc.data <= :data ")
	      
	      .append("  and tm.grupoMovimentoFinaceiro in (:gruposMovimentoFinanceiroDebito) ")
	       
	      .append("  and mfc.id not in (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ")
	       
	      .append("  and c4.id = ").append(paramIdCota);
    	
		return hql.toString();
	}
    
    /**
   	 * FROM: Movimentos financeiros de Crédito Pendentes da cota
   	 * @param paramIdCota
   	 * @return String
   	 */
    public String getFromPendenteCreditoCota(String paramIdCota){
    	
    	StringBuilder hql = new StringBuilder("")
    	
    	.append("  from MovimentoFinanceiroCota mfc ")
	       
	      .append("  join mfc.cota c4 ")
	       
	      .append("  join mfc.tipoMovimento tm ")
	    
	      .append("  where mfc.data <= :data ")
	      
	      .append("  and tm.grupoMovimentoFinaceiro in (:gruposMovimentoFinanceiroCredito) ")
	       
	      .append("  and mfc.id not in (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ")
	       
	      .append("  and c4.id = ").append(paramIdCota);
    	
		return hql.toString();
	}
	
	/**
	 * Obtem Informações para o processamento financeiro (Geração de MovimentoFinanceiroCota, Divida e Cobrança) das Cotas
	 * @param numeroCota
	 * @param data
	 * @param sortorder
	 * @param sortname
	 * @param initialResult
	 * @param maxResults
	 * @return List<ProcessamentoFinanceiroCotaDTO>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessamentoFinanceiroCotaDTO> obterProcessamentoFinanceiroCota(Integer numeroCota,
			                                                                     Date data,
	                                                                             String sortorder, 
	                                                                             String sortname,
	                                                                             int initialResult, 
	                                                                             int maxResults){
	    
	    StringBuilder hql = new StringBuilder("select ");
	    
	    hql.append(" c.numeroCota as numeroCota, ")
	    
	
	       .append(" case when (p.nome is not null) then ( p.nome )")
	       
	       .append("      when (p.razaoSocial is not null) then ( p.razaoSocial )")
	       
	       .append("      else null end as nomeCota, ")
	
	      
	       .append(" coalesce((")
   
	       .append("  select sum(mec.qtde * (case when mec.valoresAplicados is not null then case when mec.valoresAplicados.precoComDesconto is not null then mec.valoresAplicados.precoComDesconto else pe.precoVenda end else pe.precoVenda end)) ")

	       .append(this.movimentoEstoqueCotaRepository.getFromConsignadoCotaAVista("c.id"))

	       .append("),0) as valorConsignado, ")
	       
	       .append(" coalesce((")
	       
	       .append("  select sum(mec.qtde * (case when mec.valoresAplicados is not null then case when mec.valoresAplicados.precoComDesconto is not null then mec.valoresAplicados.precoComDesconto else pe.precoVenda end else pe.precoVenda end)) ")

	       .append(this.movimentoEstoqueCotaRepository.getFromAVistaCotaAVista("c.id"))

	       .append("),0) as valorAVista, ")

	       
	       .append(" coalesce((")
	       
	       .append("  select sum(mec.qtde * (case when mec.valoresAplicados is not null then case when mec.valoresAplicados.precoComDesconto is not null then mec.valoresAplicados.precoComDesconto else pe.precoVenda end else pe.precoVenda end)) ")
	      
	       .append(this.movimentoEstoqueCotaRepository.getFromEstornoCotaAVista("c.id"))
	    
	       .append("),0) as valorEstornado, ")
	       
	
	       .append(" coalesce((")
	       
	       .append("  select sum(mfc.valor) ")
	      
	       .append(this.getFromCreditoCota("c.id"))
	       
	       .append("),0) as creditos, ")
	       
	 
	       .append(" coalesce((")
	       
	       .append("  select sum(mfc.valor) ")
	      
	       .append(this.getFromDebitoCota("c.id"))
	    
	       .append("),0) as debitos, ")
	
	       
	       .append(" (")
	       
	         .append(" (")
	         
	           .append(" coalesce(((")
	           
	           .append("  select sum(mec.qtde * (case when mec.valoresAplicados is not null then case when mec.valoresAplicados.precoComDesconto is not null then mec.valoresAplicados.precoComDesconto else pe.precoVenda end else pe.precoVenda end)) ")
	          
	           .append(this.movimentoEstoqueCotaRepository.getFromConsignadoCotaAVista("c.id"))
	        
	           .append(")*(-1)),0) + ")
	           
	           
	           .append(" coalesce(((")
	       
	           .append("  select sum(mec.qtde * (case when mec.valoresAplicados is not null then case when mec.valoresAplicados.precoComDesconto is not null then mec.valoresAplicados.precoComDesconto else pe.precoVenda end else pe.precoVenda end)) ")

	           .append(this.movimentoEstoqueCotaRepository.getFromAVistaCotaAVista("c.id"))

	           .append(")*(-1)),0) + ")
	           
	           
	           .append(" coalesce((")
	           
	           .append("  select sum(mec.qtde * (case when mec.valoresAplicados is not null then case when mec.valoresAplicados.precoComDesconto is not null then mec.valoresAplicados.precoComDesconto else pe.precoVenda end else pe.precoVenda end)) ")
	          
	           .append(this.movimentoEstoqueCotaRepository.getFromEstornoCotaAVista("c.id"))
	        
	           .append("),0) ")
	           
	         .append(" ) + ")
	         
	         .append(" (")
	         
	           .append(" coalesce(((")
	           
	           .append("  select sum(coalesce(mfc.valor,0)) ")
	          
	           .append(this.getFromDebitoCota("c.id"))
	        
	           .append(")*(-1)),0) + ")
	           
	    
	           .append(" coalesce((")
	           
	           .append("  select sum(coalesce(mfc.valor,0)) ")
	          
	           .append(this.getFromCreditoCota("c.id"))
	           
	           .append("),0) )")
	
	         .append(") *(-1) as saldo  ")

	       .append(" from Cota c ")
	       
	       .append(" join c.pessoa p ")
	       
	       .append(" where c.tipoCota = :tipoCota ");
	    
	    if (numeroCota != null){
	      
	        hql.append(" and c.numeroCota = :numeroCota ");
	    }
	
	    if (sortname != null){
	      
	        hql.append(" order by ").append(sortname);
	      
	        if (sortorder != null){
	        
	            hql.append(" ").append(sortorder);
	        }
	    }
	    
	    Query query = this.getSession().createQuery(hql.toString());
	    
	    query.setFirstResult(initialResult > 1?maxResults:0);
	    
	    query.setMaxResults(maxResults);
	    
        this.setParametrosProcessamentoFinanceiroCota(query, numeroCota, data);
	
	    query.setResultTransformer(new AliasToBeanResultTransformer(ProcessamentoFinanceiroCotaDTO.class));
	    
        return query.list();
	}
	
	/**
	 * Obtem Movimentos Financeiros da Cota na data que ainda nao foram consolidados
	 * Movimentos de reparte ou encalhe
	 * @param numeroCota
	 * @param dataOperacao
	 * @return List<MovimentoFinanceiroCota>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentoFinanceiroCota> obterMovimentosFinanceirosCotaConferenciaNaoConsolidados(Integer numeroCota, Date dataOperacao){
		
		StringBuilder hql = new StringBuilder("");
		
		hql.append("  select mfc from MovimentoFinanceiroCota mfc ");
		
		hql.append("  join mfc.cota c ");
        
        hql.append("  join mfc.tipoMovimento tm ");
     
        hql.append("  where mfc.data = :data ");
        
        hql.append("  and c.numeroCota = :numeroCota ");
        
        hql.append("  and tm.grupoMovimentoFinaceiro in (:gruposMovimentoFinanceiroReparteEncalhe) ");
        
        hql.append("  and mfc.id not in (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ");
        
        Query query = this.getSession().createQuery(hql.toString());
          
        query.setParameter("data", dataOperacao);
        
        query.setParameter("numeroCota", numeroCota);
        
        query.setParameterList("gruposMovimentoFinanceiroReparteEncalhe", Arrays.asList(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE, 
        																				GrupoMovimentoFinaceiro.ENVIO_ENCALHE));
        return query.list();
	}

	/**
	 * Obtem valor total de Débitos pendentes da Cota
	 * @param numeroCota
	 * @return BigDecimal
	 */
	@Override
    public BigDecimal obterTotalDebitoCota(Integer numeroCota, Date dataOperacao){
    	
    	StringBuilder hql = new StringBuilder("")
    	
    	.append("  select sum(coalesce(mfc.valor,0)) ")
    	
    	.append("  from MovimentoFinanceiroCota mfc ")
	       
        .append("  join mfc.cota c3 ")
       
        .append("  join mfc.tipoMovimento tm ")
    	
    	.append("  where tm.grupoMovimentoFinaceiro in (:gruposMovimentoFinanceiroDebito) ")
    	    
        .append("  and mfc.id not in (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ");
    
        if (dataOperacao!=null){
        	
        	hql.append(" and mfc.data <= :data ");
        }
    
        if (numeroCota != null){
        	
        	hql.append(" and c3.numeroCota = :numeroCota");
        }

        Query query = this.getSession().createQuery(hql.toString());
 	    
 	    if (numeroCota != null){
 	      
 	        query.setParameter("numeroCota", numeroCota);
 	    }
 	    
 	    if (dataOperacao!=null){
 	        
 	    	query.setParameter("data", dataOperacao);
 	    }

	    query.setParameterList("gruposMovimentoFinanceiroDebito", Arrays.asList(GrupoMovimentoFinaceiro.DEBITO,
																	            GrupoMovimentoFinaceiro.DEBITO_SOBRE_FATURAMENTO,
																	            GrupoMovimentoFinaceiro.COMPRA_NUMEROS_ATRAZADOS,
																	            GrupoMovimentoFinaceiro.POSTERGADO_DEBITO,
																	            GrupoMovimentoFinaceiro.COMPRA_ENCALHE_SUPLEMENTAR,
																	            GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE,
																	            GrupoMovimentoFinaceiro.JUROS,
																	            GrupoMovimentoFinaceiro.MULTA,
																	            GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO,                                                            				
																	            GrupoMovimentoFinaceiro.LANCAMENTO_CAUCAO_LIQUIDA,	                                                             				
																	            GrupoMovimentoFinaceiro.VENDA_TOTAL,	                                                             				                                                          					                                                             				
																	            GrupoMovimentoFinaceiro.PENDENTE));
 	    
		return (BigDecimal) query.uniqueResult();
	}
    
    /**
	 * Obtem valor total de Créditos pendentes da Cota
	 * @param numeroCota
	 * @return BigDecimal
	 */
	@Override
    public BigDecimal obterTotalCreditoCota(Integer numeroCota, Date dataOperacao){
    	
    	StringBuilder hql = new StringBuilder("")
    	
    	.append("  select sum(coalesce(mfc.valor,0)) ")
    	
    	.append("  from MovimentoFinanceiroCota mfc ")
	       
        .append("  join mfc.cota c3 ")
       
        .append("  join mfc.tipoMovimento tm ")
    	
    	.append("  where tm.grupoMovimentoFinaceiro in (:gruposMovimentoFinanceiroCredito) ")
    	    
        .append("  and mfc.id not in (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ");
    
        if (dataOperacao!=null){
        	
        	hql.append(" and mfc.data <= :data ");
        }
    
        if (numeroCota != null){
        	
        	hql.append(" and c3.numeroCota = :numeroCota");
        }

        Query query = this.getSession().createQuery(hql.toString());
 	    
 	    if (numeroCota != null){
 	      
 	        query.setParameter("numeroCota", numeroCota);
 	    }
 	    
 	    if (dataOperacao!=null){
 	        
 	    	query.setParameter("data", dataOperacao);
 	    }
    	
 	    query.setParameterList("gruposMovimentoFinanceiroCredito", Arrays.asList(GrupoMovimentoFinaceiro.CREDITO,
																                 GrupoMovimentoFinaceiro.CREDITO_SOBRE_FATURAMENTO,
																                 GrupoMovimentoFinaceiro.POSTERGADO_CREDITO,
																                 GrupoMovimentoFinaceiro.ENVIO_ENCALHE,
																                 GrupoMovimentoFinaceiro.RESGATE_CAUCAO_LIQUIDA));

		return (BigDecimal) query.uniqueResult();
	}
	
    /**
     * Obtem saldo das cotas com tipo À Vista na data de operação atual
     * @param numeroCota
     * @param data
     * @return BigDecimal
     */
	@Override
	public BigDecimal obterSaldoCotasAVista(Integer numeroCota, Date data){
		
       StringBuilder hql = new StringBuilder("");
		
	   hql.append(" select sum ")
		    
		  .append(" (")
		       
	        .append(" (")
	        
	          .append(" coalesce(((")
	          
	          .append("  select sum(mec.qtde * (case when mec.valoresAplicados is not null then case when mec.valoresAplicados.precoComDesconto is not null then mec.valoresAplicados.precoComDesconto else pe.precoVenda end else pe.precoVenda end)) ")
	         
	          .append(this.movimentoEstoqueCotaRepository.getFromConsignadoCotaAVista("c.id"))
	       
	          .append(")*(-1)),0) + ")
	          
	          
	          .append(" coalesce(((")
	      
	          .append("  select sum(mec.qtde * (case when mec.valoresAplicados is not null then case when mec.valoresAplicados.precoComDesconto is not null then mec.valoresAplicados.precoComDesconto else pe.precoVenda end else pe.precoVenda end)) ")
	
	          .append(this.movimentoEstoqueCotaRepository.getFromAVistaCotaAVista("c.id"))
	
	          .append(")*(-1)),0) + ")
	          
	          
	          .append(" coalesce((")
	          
	          .append("  select sum(mec.qtde * (case when mec.valoresAplicados is not null then case when mec.valoresAplicados.precoComDesconto is not null then mec.valoresAplicados.precoComDesconto else pe.precoVenda end else pe.precoVenda end)) ")
	         
	          .append(this.movimentoEstoqueCotaRepository.getFromEstornoCotaAVista("c.id"))
	       
	          .append("),0) ")
	          
	        .append(" ) + ")
	        
	        .append(" (")
	        
	          .append(" coalesce(((")
	          
	          .append("  select sum(coalesce(mfc.valor,0)) ")
	         
	          .append(this.getFromDebitoCota("c.id"))
	       
	          .append(")*(-1)),0) + ")
	          
	   
	          .append(" coalesce((")
	          
	          .append("  select sum(coalesce(mfc.valor,0)) ")
	         
	          .append(this.getFromCreditoCota("c.id"))
	          
	          .append("),0) ")
	
	        .append(") + ")
	
	        .append("(")
	        
	          .append(" coalesce(((")
	          
	          .append("  select sum(coalesce(mfc.valor,0)) ")
	         
	          .append(this.getFromPendenteDebitoCota("c.id"))
	       
	          .append(")*(-1)),0) + ")
	          
	   
	          .append(" coalesce((")
	          
	          .append("  select sum(coalesce(mfc.valor,0)) ")
	         
	          .append(this.getFromPendenteCreditoCota("c.id"))
	          
	          .append("),0) ")
	
	        .append(") ")
	
	      .append(")*(-1) as saldo ")
	      
	      
	      .append(" from Cota c ")
	      
	      .append(" join c.pessoa p ")
	      
	      .append(" where c.tipoCota = :tipoCota ");

	   if (numeroCota != null){
		   
	      hql.append(" and c.numeroCota = :numeroCota ");	 
	   }
	   
	   Query query = this.getSession().createQuery(hql.toString());
	   
       this.setParametrosProcessamentoFinanceiroCota(query, numeroCota, data);	
	   
	   return (BigDecimal) query.uniqueResult();
	}	
	
	/**
	 * Define parametros da query de consulta de processamento financeiro de cotas À Vista
	 * @param query
	 * @param numeroCota
	 * @param data
	 */
	private void setParametrosProcessamentoFinanceiroCota(Query query, Integer numeroCota, Date data){
		
	    if (numeroCota != null){
		      
	        query.setParameter("numeroCota", numeroCota);
	    }
	    
	    //TODO tipoCota == A vista
	    
	    query.setParameter("tipoCota", TipoCota.CONSIGNADO);

	    query.setParameter("formaComercializacaoProduto", FormaComercializacao.CONTA_FIRME);

	    query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
	    
	    query.setParameterList("gruposMovimentoReparte", Arrays.asList(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR, 
	                                               				       GrupoMovimentoEstoque.COMPRA_ENCALHE, 
	                                                                   GrupoMovimentoEstoque.RECEBIMENTO_REPARTE));
	    
	    query.setParameterList("gruposMovimentoEstorno", Arrays.asList(GrupoMovimentoEstoque.ESTORNO_COMPRA_ENCALHE, 
	                                                                   GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR));
	    
	    query.setParameterList("gruposMovimentoFinanceiroCredito", Arrays.asList(GrupoMovimentoFinaceiro.CREDITO,
	                                                                             GrupoMovimentoFinaceiro.CREDITO_SOBRE_FATURAMENTO,
	                                                                             GrupoMovimentoFinaceiro.POSTERGADO_CREDITO,
	                                                                             GrupoMovimentoFinaceiro.ENVIO_ENCALHE,
	                                                                             GrupoMovimentoFinaceiro.RESGATE_CAUCAO_LIQUIDA));
	    
	    query.setParameterList("gruposMovimentoFinanceiroDebito", Arrays.asList(GrupoMovimentoFinaceiro.DEBITO,
	                                                                            GrupoMovimentoFinaceiro.DEBITO_SOBRE_FATURAMENTO,
	                                                                            GrupoMovimentoFinaceiro.COMPRA_NUMEROS_ATRAZADOS,
	                                                                            GrupoMovimentoFinaceiro.POSTERGADO_DEBITO,
	                                                                            GrupoMovimentoFinaceiro.COMPRA_ENCALHE_SUPLEMENTAR,
	                                                                            GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE,
	                                                                            GrupoMovimentoFinaceiro.JUROS,
	                                                                            GrupoMovimentoFinaceiro.MULTA,
	                                                                            GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO,                                                            				
	                                                                            GrupoMovimentoFinaceiro.LANCAMENTO_CAUCAO_LIQUIDA,	                                                             				
	                                                                            GrupoMovimentoFinaceiro.VENDA_TOTAL,	                                                             				                                                          					                                                             				
	                                                                            GrupoMovimentoFinaceiro.PENDENTE));
	
	    query.setParameter("statusAprovacao", StatusAprovacao.APROVADO);
	    
	    query.setParameter("statusOperacaoConferencia", StatusOperacao.CONCLUIDO);
	    
	    query.setParameter("data", data);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MovimentoFinanceiroDTO> obterDetalhesVendaDia(Integer numeroCota, 
			Long idConsolidado, List<Long> tiposMovimento, Date data){
		
		StringBuilder hql = new StringBuilder();
		hql.append("select m.valor as valor, ")
		
		   .append(" m.data as data, ")
		   
		   .append(" (case when m.tipoMovimento.grupoMovimentoFinaceiro = 'ENVIO_ENCALHE' then 'Encalhe' ")
		   .append("       when m.tipoMovimento.grupoMovimentoFinaceiro = 'RECEBIMENTO_REPARTE' then 'Reparte' ")
		   .append("       else m.tipoMovimento.descricao end) || (case when m.observacao is not null then (' - ' || m.observacao) else '' end) ")
		   .append("       || (case when pj.razaoSocial is null then '' else (' - ' || pj.razaoSocial) end) as descricao ")
		   
		   .append(" from MovimentoFinanceiroCota m ")
		   
		   .append(" join m.cota cota ")
		   
		   .append(" join m.fornecedor f ")
		
		   .append(" join f.juridica pj ");
		
		if (idConsolidado != null){
			
			hql.append(" join m.consolidadoFinanceiroCota consolidado ");
		}
		
		hql.append(" where cota.numeroCota = :numeroCota ")
		
		   .append(" and m.tipoMovimento.id in (:tiposMovimento) ");
		
		if (data != null){
			
			hql.append(" and m.data = :data ");
		}
		
		if (idConsolidado != null){
			
			hql.append(" and consolidado.id = :idConsolidado");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(MovimentoFinanceiroDTO.class));
		
		query.setParameter("numeroCota", numeroCota);
		
		if (data != null){
			
			query.setParameter("data", data);
		}
		
		if (idConsolidado != null){
			
			query.setParameter("idConsolidado", idConsolidado);
		}
		
		query.setParameterList("tiposMovimento", tiposMovimento);
		
		return query.list();
	}

	/**
	 * Verifica existência de MovimentoFinanceiroCota Consolidado por id
	 * @param idMovimentoFinanceiroCota
	 * @return boolean
	 */
	@Override
    public boolean isMovimentoFinanceiroCotaConsolidado(Long idMovimentoFinanceiroCota){
    	
    	StringBuilder hql = new StringBuilder("")
    	
    	.append("  select mfc ")
    	
    	.append("  from MovimentoFinanceiroCota mfc ")
    	
    	.append("  where mfc.id = :idMovimentoFinanceiroCota ")
    	    
        .append("  and mfc.id in (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ");

        Query query = this.getSession().createQuery(hql.toString());

 	    query.setParameter("idMovimentoFinanceiroCota", idMovimentoFinanceiroCota);

		return ((MovimentoFinanceiroCota) query.uniqueResult())!=null;
	}
	
	
    @SuppressWarnings("unchecked")
    public List<MovimentoFinanceiroCota> obterMovimentoFinanceiroCotaDeConsolidado(final Long idConsolidado){
    	
    	StringBuilder hql = new StringBuilder("")
    	
    	.append("  SELECT DISTINCT  MFC.*   FROM ")
    	.append("   MOVIMENTO_FINANCEIRO_COTA MFC ")
    	.append("   INNER JOIN  CONSOLIDADO_MVTO_FINANCEIRO_COTA CMFC ON MFC.ID = CMFC.MVTO_FINANCEIRO_COTA_ID AND CONSOLIDADO_FINANCEIRO_ID = :idConsolidado ");

        Query query = this.getSession().createSQLQuery(hql.toString()).addEntity(MovimentoFinanceiroCota.class);

 	    query.setParameter("idConsolidado", idConsolidado);

		return query.list();
		
	}
    
    @SuppressWarnings("unchecked")
    @Override
	public void removeByIdConsolidadoAndGrupos(Long idConsolidado, List<String> grupoMovimentoFinaceiros){
		
        //pesquisa por ids de mov. finan. do consolidado vindo como parametro
        final StringBuilder sql =  new StringBuilder();
        sql.append("select movi.id FROM MOVIMENTO_FINANCEIRO_COTA movi ");
        sql.append("join TIPO_MOVIMENTO tipo on ");
        sql.append("movi.TIPO_MOVIMENTO_ID = tipo.id and tipo.tipo = 'FINANCEIRO' ");
        sql.append("join CONSOLIDADO_MVTO_FINANCEIRO_COTA con on ");
        sql.append("con.MVTO_FINANCEIRO_COTA_ID = movi.id ");
        sql.append("where con.CONSOLIDADO_FINANCEIRO_ID = :idConsolidado ");
        sql.append("and tipo.GRUPO_MOVIMENTO_FINANCEIRO in (:grupoMovimentoFinaceiros)");
        
        final List<Long> idsMovs = this.getSession().createSQLQuery(sql.toString())
                .setParameter("idConsolidado", idConsolidado)
                .setParameterList("grupoMovimentoFinaceiros", grupoMovimentoFinaceiros)
                .list();
        
        //apaga o registro de ligação entre mov. finan. e consolidado
        this.getSession().createSQLQuery(
                "delete from CONSOLIDADO_MVTO_FINANCEIRO_COTA where CONSOLIDADO_FINANCEIRO_ID = :idConsolidado")
                .setParameter("idConsolidado", idConsolidado)
                .executeUpdate();
        
        //apaga o mov. finan. com os ids encontrados
        //apaga-los diretamente fazendo join com a tabela de ligação resultaria em exceptiom de integridade referencial
        if (idsMovs != null && !idsMovs.isEmpty()){
            this.getSession().createSQLQuery(
                    "delete from MOVIMENTO_FINANCEIRO_COTA where id in (:idsMovs)")
                    .setParameterList("idsMovs", idsMovs)
                    .executeUpdate();
        }
	}

    @Override
    public void removeByCotaAndDataOpAndGrupos(Long idCota, Date dataOperacao, List<String> grupoMovimentoFinaceiros) {
        
        final String sql = "delete movi FROM MOVIMENTO_FINANCEIRO_COTA movi "+
            "join TIPO_MOVIMENTO tipo on " +
            "movi.TIPO_MOVIMENTO_ID = tipo.id and tipo.tipo = 'FINANCEIRO' " +
            "where movi.COTA_ID = :idCota " +
            "and movi.DATA = :dataOperacao " +
            "and tipo.GRUPO_MOVIMENTO_FINANCEIRO in (:grupoMovimentoFinaceiros)";
        
        this.getSession().createSQLQuery(sql)
                .setParameter("idCota", idCota)
                .setParameter("dataOperacao", dataOperacao)
                .setParameterList("grupoMovimentoFinaceiros", grupoMovimentoFinaceiros)
                .executeUpdate();
    }
    
    
    public void adicionarEmLoteDTO(final List<MovimentoFinanceiroCotaDTO> movimentoFinanceiroCota) {
        
        if (movimentoFinanceiroCota == null || movimentoFinanceiroCota.isEmpty()) {
            return;
        }
        
        final Session session = this.getSession();
        
        session.doWork(new Work() {
            @Override
            public void execute(final Connection conn) throws SQLException {
                
                final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
                 
                final StringBuilder sqlQry = new StringBuilder()
                .append("insert ")
                .append("into MOVIMENTO_FINANCEIRO_COTA ")
                .append("(APROVADO_AUTOMATICAMENTE,")
                .append("DATA_APROVACAO,")
                .append("MOTIVO,")
                .append("STATUS,")
                .append("DATA,")
                .append("DATA_CRIACAO,")
                .append("APROVADOR_ID,")
                .append("TIPO_MOVIMENTO_ID,")
                .append("USUARIO_ID,")
                .append("PARCELAS,")
                .append("PRAZO,")
                .append("VALOR,")
                .append("LANCAMENTO_MANUAL,")
                .append("OBSERVACAO,")
                .append("BAIXA_COBRANCA_ID,")
                .append("COTA_ID,")
                .append("DATA_INTEGRACAO,")
                .append("STATUS_INTEGRACAO,")
                .append("FORNECEDOR_ID, ID)")
                .append("values ")
                .append("( :aprovacaoAutomatica,")
                .append(" :dataAprovacao,")
                .append(" :motivo,")
                .append(" :status,")
                .append(" :dataVencimento,")
                .append(" :dataCriacao,")
                .append(" :usuarioAprovadorId,")
                .append(" :idTipoMovimento,")
                .append(" :idUsuario,")
                .append(" :parcelas,")
                .append(" :prazo,")
                .append(" :valor,")
                .append(" :lancamentoManual,")
                .append(" :observacao,")
                .append(" :idBaixaCobranca,")
                .append(" :idCota,")
                .append(" :dataIntegracao,")
                .append(" :statusIntegracao,")
                .append(" :idFornecedor, -1 )");
                
                final SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(movimentoFinanceiroCota.toArray());
                
                namedParameterJdbcTemplate.batchUpdate(sqlQry.toString(), params);
                
            }
        });
        
    }
}