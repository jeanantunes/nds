package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaFaturamentoDTO;
import br.com.abril.nds.dto.CotaTransportadorDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO.ColunaOrdenacao;
import br.com.abril.nds.dto.filtro.FiltroRelatorioServicosEntregaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class MovimentoFinanceiroCotaRepositoryImpl extends AbstractRepositoryModel<MovimentoFinanceiroCota, Long> 
												   implements MovimentoFinanceiroCotaRepository {

	public MovimentoFinanceiroCotaRepositoryImpl() {
		super(MovimentoFinanceiroCota.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<MovimentoFinanceiroCota> obterMovimentoFinanceiroCota(Long idCota){
		
		StringBuilder hql = new StringBuilder("select mfc ");
		hql.append(" from MovimentoFinanceiroCota mfc, Distribuidor d ")
		   .append(" join mfc.cota cota ")   
		   .append(" left join mfc.movimentos movimentoEstoque ")
		   .append(" left join movimentoEstoque.produtoEdicao pe ")
		   .append(" left join pe.produto p ")
		   .append(" left join p.fornecedores fornecedor ")
		   .append(" where mfc.data <= d.dataOperacao ")
		   .append(" and mfc.status = :statusAprovado ");
		
		if (idCota != null){
			hql.append(" and cota.id = :idCota ");
		}
		
		hql.append(" and cota.situacaoCadastro != :inativo and cota.situacaoCadastro != :pendente ")
		   .append(" and mfc.id not in ")
		   .append(" (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov) ");
		
		hql.append(" group by mfc.id ")
		   .append(" order by cota.id, fornecedor.id ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
		
		if (idCota != null){
			query.setParameter("idCota", idCota);
		}
		
		query.setParameter("inativo", SituacaoCadastro.INATIVO);
		query.setParameter("pendente", SituacaoCadastro.PENDENTE);
		
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<DebitoCreditoCotaDTO> obterDebitoCreditoCotaDataOperacao(Integer numeroCota, Date dataOperacao, List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados){
		
		StringBuilder hql = new StringBuilder(" select ");
		
		hql.append(" mfc.tipoMovimento.operacaoFinaceira as tipoLancamento, ");
		
		hql.append(" mfc.valor as valor, ");
		
		hql.append(" mfc.data as dataLancamento, ");
		
		hql.append(" mfc.observacao as observacoes");
		
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
		
		hql.append(" order by mfc.data ");
		
		Query query = this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(DebitoCreditoCotaDTO.class));
		
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("dataOperacao", dataOperacao);
		
		if(tiposMovimentoFinanceiroIgnorados!=null && !tiposMovimentoFinanceiroIgnorados.isEmpty()) {
			query.setParameterList("tiposMovimentoFinanceiroIgnorados", tiposMovimentoFinanceiroIgnorados);
		}
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DebitoCreditoCotaDTO> obterDebitoCreditoSumarizadosParaCotaDataOperacao(Integer numeroCota, Date dataOperacao, List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados){
		
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

		Query query = this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(DebitoCreditoCotaDTO.class));
		
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("dataOperacao", dataOperacao);
		
		if(tiposMovimentoFinanceiroIgnorados!=null && !tiposMovimentoFinanceiroIgnorados.isEmpty()) {
			query.setParameterList("tiposMovimentoFinanceiroIgnorados", tiposMovimentoFinanceiroIgnorados);
		}
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public  List<DebitoCreditoCotaDTO> obterDebitoCreditoPorPeriodoOperacao(FiltroConsultaEncalheDTO filtro, List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados){
		
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
		
		Query query = this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(DebitoCreditoCotaDTO.class));
		
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
	public Long obterQuantidadeMovimentoFinanceiroDataOperacao(Date dataAtual){
		
		StringBuilder hql = new StringBuilder("select count(mfc.data) ");
		hql.append(" from MovimentoFinanceiroCota mfc, Distribuidor d ")
		   .append(" where mfc.data = d.dataOperacao ")
		   .append(" and mfc.status = :statusAprovado ");
		
		hql.append(" and mfc.cota.id not in ")
		   .append(" (select distinct c.cota.id from ConsolidadoFinanceiroCota c where c.dataConsolidado <= :dataAtual) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
		query.setParameter("dataAtual", dataAtual);
		
		
		return (Long) query.uniqueResult();
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

		String hql = getQueryObterMovimentosFinanceiroCota(filtroDebitoCreditoDTO) +
					 getOrderByObterMovimentosFinanceiroCota(filtroDebitoCreditoDTO); 

		Query query = criarQueryObterMovimentosFinanceiroCota(hql, filtroDebitoCreditoDTO);

		if (filtroDebitoCreditoDTO.getPaginacao() != null 
				&& filtroDebitoCreditoDTO.getPaginacao().getPosicaoInicial() != null) { 
			
			query.setFirstResult(filtroDebitoCreditoDTO.getPaginacao().getPosicaoInicial());
			
			query.setMaxResults(filtroDebitoCreditoDTO.getPaginacao().getQtdResultadosPorPagina());
		}
		
		return query.list();
	}
	
	private String getQueryObterMovimentosFinanceiroCota(FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {

		StringBuilder hql = new StringBuilder();

		hql.append(" from MovimentoFinanceiroCota movimentoFinanceiroCota ");

		String conditions = "";

		if (filtroDebitoCreditoDTO.getIdTipoMovimento() != null) {

			conditions += conditions == "" ? " where " : " and ";

			conditions += " movimentoFinanceiroCota.tipoMovimento.id = :idTipoMovimento ";
		}

		if (filtroDebitoCreditoDTO.getDataLancamentoInicio() != null && 
				filtroDebitoCreditoDTO.getDataLancamentoFim() != null) {
			
			conditions += conditions == "" ? " where " : " and ";

			conditions += " movimentoFinanceiroCota.dataCriacao between :dataLancamentoInicio and :dataLancamentoFim ";
		}
		
		if (filtroDebitoCreditoDTO.getDataVencimentoInicio() != null && 
				filtroDebitoCreditoDTO.getDataVencimentoFim() != null) {
			
			conditions += conditions == "" ? " where " : " and ";

			conditions += " movimentoFinanceiroCota.data between :dataVencimentoInicio and :dataVencimentoFim ";
		}

		if (filtroDebitoCreditoDTO.getNumeroCota() != null) {

			conditions += conditions == "" ? " where " : " and ";

			conditions += " movimentoFinanceiroCota.cota.numeroCota = :numeroCota ";
		}

		hql.append(conditions);
		
		return hql.toString();
	}

	private Query criarQueryObterMovimentosFinanceiroCota(String hql, FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {
		
		Query query = getSession().createQuery(hql);

		if (filtroDebitoCreditoDTO.getIdTipoMovimento() != null) {

			query.setParameter("idTipoMovimento", filtroDebitoCreditoDTO.getIdTipoMovimento());
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

		hql.append(" where mfc.baixaCobranca.status = :status ");
		
		hql.append(" and mfc.baixaCobranca.cobranca.id = :idCobranca ");
		
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
	    
	    hql.append("              (COALESCE(epc.qtdeRecebida,0) - COALESCE(epc.qtdeDevolvida,0)) * ");
	    
	    hql.append("              (COALESCE(epc.produtoEdicao.precoVenda,0)) ");
	    
	    hql.append("         ),0 ) as faturamentoBruto,");
	    
	    hql.append(" COALESCE(sum( ");
	    
	    hql.append("              (COALESCE(epc.qtdeRecebida,0) - COALESCE(epc.qtdeDevolvida,0)) * ");
	    
	    hql.append("              (COALESCE(epc.produtoEdicao.precoVenda,0) - ");
	    
	    hql.append("               COALESCE(("+obterHQLDesconto("c.id","epc.produtoEdicao.id","fornecedor.id")+"),0)) ");
	    
	    hql.append("         ),0 ) as faturamentoLiquido ");
	    
		hql.append(" from Cota c, MovimentoEstoqueCota mec, EstoqueProdutoCota epc");
		
		hql.append(" left join mec.produtoEdicao.produto.fornecedores fornecedor ");
		
		hql.append(" where epc = mec.estoqueProdutoCota  ");
		
		hql.append(" and mec.cota = c ");
		
		hql.append(" and mec.status = :status  ");
	    
		hql.append(" and ( mec.data >= :dataInicial and mec.data <= :dataFinal )  ");
	    
		hql.append(" and c in (:cotas) ");
	    
		Query query = this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(CotaFaturamentoDTO.class));
		
		query.setParameterList("cotas", cotas);
		
		query.setParameter("status", StatusAprovacao.APROVADO);
		
		query.setParameter("dataInicial", dataInicial);
		
		query.setParameter("dataFinal", dataFinal);
		
		return query.list();
	}


	private String obterHQLDesconto(String cota, String produto, String fornecedor){
	   	
        String auxC = " where ";
	    StringBuilder hql = new StringBuilder("select view.desconto from ViewDesconto view ");
		
   	    if (cota!=null && !"".equals(cota)){
		   hql.append(auxC+" view.cotaId = "+cota);
		   auxC = " and ";
   	    }

        if (produto!=null && !"".equals(produto)){
	       hql.append(auxC+" view.produtoEdicaoId = "+produto);
	 	   auxC = " and ";
	    }

	    if (fornecedor!=null && !"".equals(fornecedor)){
	 	   hql.append(auxC+" view.fornecedorId = "+fornecedor);
	 	   auxC = " and ";
	    }	 

	    return hql.toString();
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
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
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
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
	
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

		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		hql.append("select count(transportador.id)");
		addFromJoinObterDadosTransportador(hql);
		addWhereObterDadosTransportador(hql, filtro.getEntregaDataInicio(), filtro.getEntregaDataFim(), filtro.getIdTransportador(), param);
		hql.append(" group by transportador.id ");

		Query query = getSession().createQuery(hql.toString());

		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		Long count = (Long) query.uniqueResult();
		
		if (count == null) {
			count = 0L;
		}

		return count;
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
	public List<DebitoCreditoCotaDTO> obterCreditoDebitoCota(Long idConsolidado, Date dataCriacao,
			List<TipoMovimentoFinanceiro> tiposMovimento, String sortorder, String sortname){
		
		StringBuilder hql = new StringBuilder("select ");
		boolean indWhere = false;
		
		hql.append(" m.dataCriacao as dataLancamento, ")
		   .append(" m.tipoMovimento.descricao as tipoMovimento, ")
		   .append(" m.valor as valor, ")
		   .append(" m.observacao as observacoes ")
		   .append(" from MovimentoFinanceiroCota m ");
		
		if (idConsolidado != null){
			
			hql.append(" join m.consolidadoFinanceiroCota consolidado ")
			   .append(" where consolidado.id = :idConsolidado");
			indWhere = true;
		}
		
		if (dataCriacao != null){
			
			hql.append(indWhere ? " and " : " where ")
			   .append(" m.dataCriacao = :dataCriacao ");
			indWhere = true;
		}
		
		hql.append(indWhere ? " and " : " where ")
		   .append(" m.tipoMovimento in (:tiposMovimento)");
		
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
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DebitoCreditoCotaDTO.class));
		
		return query.list();
	}
	
	@Override
	public BigDecimal obterSomatorioTipoMovimentoPorConsolidado(Long idConsolidado, Date dataCriacao, 
			Collection<TipoMovimentoFinanceiro> tiposMovimento){
		
		StringBuilder hql = new StringBuilder("select sum (m.valor) ");
		boolean indWhere = false;
		
		hql.append(" from MovimentoFinanceiroCota m ");
		
		if (idConsolidado != null){
			
			hql.append(" join m.consolidadoFinanceiroCota consolidado ")
			   .append(" where consolidado.id = :idConsolidado");
			indWhere = true;
		}
		
		if (dataCriacao != null){
			
			hql.append(indWhere ? " and " : " where ")
			   .append(" m.dataCriacao = :dataCriacao ");
			indWhere = true;
		}
		
		hql.append(indWhere ? " and " : " where ")
		   .append(" m.tipoMovimento in (:tiposMovimento)");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (idConsolidado != null){
			
			query.setParameter("idConsolidado", idConsolidado);
		}
		
		if (dataCriacao != null){
			
			query.setParameter("dataCriacao", dataCriacao);
		}
		
		query.setParameterList("tiposMovimento", tiposMovimento);
		
		return (BigDecimal) query.uniqueResult();
	}
}