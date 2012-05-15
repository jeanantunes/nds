package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO.ColunaOrdenacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;

@Repository
public class MovimentoFinanceiroCotaRepositoryImpl extends AbstractRepository<MovimentoFinanceiroCota, Long> 
												   implements MovimentoFinanceiroCotaRepository {

	public MovimentoFinanceiroCotaRepositoryImpl() {
		super(MovimentoFinanceiroCota.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<MovimentoFinanceiroCota> obterMovimentoFinanceiroCotaDataOperacao(Long idCota, Date dataAtual){
		
		StringBuilder hql = new StringBuilder("select mfc ");
		hql.append(" from MovimentoFinanceiroCota mfc, Distribuidor d ")
		   .append(" where mfc.data = d.dataOperacao ")
		   .append(" and mfc.status = :statusAprovado ");
		
		if (idCota != null){
			hql.append(" and mfc.cota.id = :idCota ");
		}
		
		hql.append(" and mfc.id not in ")
		   .append(" (select mov.id from ConsolidadoFinanceiroCota c join c.movimentos mov where c.dataConsolidado <= :dataAtual) ");
		
		hql.append(" order by mfc.cota.id ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
		query.setParameter("dataAtual", dataAtual);
		
		if (idCota != null){
			query.setParameter("idCota", idCota);
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
}
