package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NegociacaoDividaRepository;

@Repository
public class NegociacaoDividaRepositoryImpl extends AbstractRepositoryModel<Negociacao, Long> 
											implements NegociacaoDividaRepository{

	public NegociacaoDividaRepositoryImpl() {
		super(Negociacao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NegociacaoDividaDTO> obterNegociacaoPorCota(FiltroConsultaNegociacaoDivida filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cobranca.dataEmissao as dtEmissao, ");
		hql.append(" cobranca.dataVencimento as dtVencimento, ");
		hql.append(" cobranca.valor as vlDivida, ");
		hql.append(" CASE WHEN (datediff(:dataOperacao, cobranca.dataVencimento)) < 0 ");
		hql.append(" THEN 0 ELSE datediff(:dataOperacao, cobranca.dataVencimento) END  as prazo, ");
		hql.append(" (COALESCE(cobranca.encargos, 0) + cobranca.valor) as total, ");
		hql.append(" cobranca.id as idCobranca, ");
		hql.append(" coalesce(cobranca.encargos, 0) as encargos ");
		
		this.obterNegociacaoPorCotaFrom(hql, filtro);
		
		if (filtro.getPaginacaoVO() != null &&
				filtro.getPaginacaoVO().getSortColumn() != null){
			
			hql.append(" order by ").append(filtro.getPaginacaoVO().getSortColumn());
			
			if (filtro.getPaginacaoVO().getOrdenacao() != null){
				
				hql.append(" ").append(filtro.getPaginacaoVO().getOrdenacao());
			}
		}
	
		Query query = getSession().createQuery(hql.toString());
		if (filtro.getPaginacaoVO() != null) {
			query.setFirstResult((filtro.getPaginacaoVO().getPaginaAtual() - 1) * filtro.getPaginacaoVO().getQtdResultadosPorPagina());
			query.setMaxResults(filtro.getPaginacaoVO().getQtdResultadosPorPagina());
		}
		
		this.setParametrosObterNegociacaoPorCota(query, filtro);

		query.setResultTransformer(new AliasToBeanResultTransformer(NegociacaoDividaDTO.class));
		
		return query.list();
	}
	
	@Override
	public Long obterCotaPorNumeroCount(FiltroConsultaNegociacaoDivida filtro){
		
		StringBuilder hql = new StringBuilder(" select count (cobranca.id) ");
		this.obterNegociacaoPorCotaFrom(hql, filtro);
		
		Query query = getSession().createQuery(hql.toString());
		
		this.setParametrosObterNegociacaoPorCota(query, filtro);
		
		return (Long) query.uniqueResult();
	}
	
	private void obterNegociacaoPorCotaFrom(StringBuilder hql, FiltroConsultaNegociacaoDivida filtro){
		
		hql.append(" FROM Cobranca cobranca ");
		hql.append(" JOIN cobranca.cota ");
		hql.append(" JOIN cobranca.divida divida ");
		hql.append(" WHERE cobranca.cota.numeroCota = :numCota ");
		hql.append(" AND cobranca.statusCobranca = :status ");
		hql.append(" AND divida.status != :statusDivida ");
		
		if(!filtro.isLancamento()){
			hql.append(" AND divida.data < :dataOperacao");
			hql.append(" AND cobranca.dataVencimento <= :dataOperacao ");
		}
	}
	
	private void setParametrosObterNegociacaoPorCota(Query query, FiltroConsultaNegociacaoDivida filtro){
		
		query.setParameter("numCota", filtro.getNumeroCota());
		query.setParameter("status", StatusCobranca.NAO_PAGO);
		query.setParameter("dataOperacao", filtro.getDataOperacao());
		query.setParameter("statusDivida", StatusDivida.PENDENTE_INADIMPLENCIA);
	}

	@Override
	public Negociacao obterNegociacaoPorCobranca(Long id) {
		Query query = getSession().createQuery("select o from Negociacao o join o.cobrancasOriginarias c where c.id = " + id);
		return (Negociacao) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Negociacao> obterNegociacaoPorComissaoCota(Long idCota){
		
		StringBuilder hql = new StringBuilder("select ne from Negociacao ne ");
		hql.append(" join ne.cobrancasOriginarias co ")
		   .append(" join co.cota cota ")
		   .append(" where cota.id = :idCota ")
		   .append(" and ne.comissaoParaSaldoDivida is not null ")
		   .append(" and ne.valorDividaPagaComissao is not null ")
		   .append(" and ne.valorDividaPagaComissao > 0 ")
		   .append(" order by ne.dataCriacao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		
		return query.list();
	}
	
	public Long obterQuantidadeNegociacaoFollowup(FiltroFollowupNegociacaoDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(negociacao.id) ").append(this.obterSqlFromConsultaNegociacao());
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("statusDivida", StatusDivida.NEGOCIADA);
		
		return (Long) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<ConsultaFollowupNegociacaoDTO>obterNegociacaoFollowup(FiltroFollowupNegociacaoDTO filtro){
		
		StringBuilder hql = new StringBuilder();	
		
		hql.append(" select ")
			
			.append(" negociacao.id as idNegociacao,")
			
			.append(" cota.numeroCota as numeroCota, ")
			
			.append(" case pessoa.class when 'F' then pessoa.nome when 'J' then pessoa.razaoSocial end  as nomeJornaleiro,")
			
			.append("(").append(this.obterSubSelectTipoCobranca()).append(")").append(" as tipoCobranca , ")
			
			.append("(").append(this.obterSubSelectDataVencimentoParcela(" negociacao.id ")).append(")").append(" as dataVencimento, ")
			
			.append("(").append(this.obterSubSelectValorParcela()).append(")").append(" as valorParcela, ")
			
			.append("(").append(this.obrterSubSelectNumeroParcelaAtual()).append(")").append(" as numeroParcelaAtual ,")
			
			.append("(").append(this.obterSubSelectCountParcelas()).append(")").append(" as quantidadeParcelas ");
		
		hql.append(this.obterSqlFromConsultaNegociacao());
		
		hql.append(this.obterOrdenacaoConsulta(filtro));
				
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("statusDivida", StatusDivida.NEGOCIADA);
		
		if (filtro.getPaginacao() != null) {

			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}

			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ConsultaFollowupNegociacaoDTO.class));
		
		return query.list();
	}

	private Object obterOrdenacaoConsulta(FiltroFollowupNegociacaoDTO filtro) {
		
		if(filtro.getPaginacao() == null 
				|| filtro.getPaginacao().getSortColumn() == null
				|| filtro.getOrdenacaoColuna() == null){
			
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		switch (filtro.getOrdenacaoColuna()) {
			case DATA_VENCIMENTO:
				hql.append(" order by dataVencimento ");
				break;
				
			case FORMA_PAGAMENTO:
				hql.append(" order by tipoCobranca  ");		
				break;
				
			case NEGOCIACAO:
				hql.append(" order by valorParcela ");
				break;
			
			case NOME_COTA:
				hql.append(" order by nomeJornaleiro ");
				break;
				
			case NUMERO_COTA:
				hql.append(" order by numeroCota ");
				break;
		}
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}

	private String obterSqlFromConsultaNegociacao() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Negociacao negociacao ")
			
			.append(" join negociacao.cobrancasOriginarias cobranca ")
			
			.append(" join cobranca.divida divida ")
			
			.append(" join cobranca.cota cota ")
			
			.append(" join cota.pessoa pessoa ")
			
			.append(" where divida.status =:statusDivida ")
			
			.append(" and negociacao.valorDividaPagaComissao is null  ")
			
			.append(" and EXISTS (").append(this.subSelectUtimaParcelaPendenteAprovacao(" negociacao.id ")).append(")");
		
		return hql.toString();
	}
	
	private Object subSelectUtimaParcelaPendenteAprovacao(String nomeParametroNegociacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select max( baixaCobrancaPendente.dataBaixa ) ")
			
			.append(" from Cobranca cobrancaPendente ")
			
			.append(" join cobrancaPendente.divida dividaPendente ")
			
			.append(" join dividaPendente.consolidados consolidadoPendente ")
			
			.append(" join consolidadoPendente.movimentos movimentoFinanceiroPendente ")
			
			.append(" join movimentoFinanceiroPendente.parcelaNegociacao parcelaPendente ")
			
			.append(" join parcelaPendente.negociacao negociacaoPendente ")
			
			.append(" join cobrancaPendente.baixasCobranca baixaCobrancaPendente ")
			
			.append("  , BaixaManual baixaManual ")
			
			.append(" where negociacaoPendente.id= ").append(nomeParametroNegociacao)
			
			.append(" and baixaManual.id = baixaCobrancaPendente.id")
			
			.append(" and baixaManual.statusAprovacao = 'PENDENTE' ")
			
			.append(" and cobrancaPendente.statusCobranca = 'PAGO' ")
			
			.append(" and parcelaPendente.dataVencimento = ( ").append(obterSubSelectDataVencimentoParcela(nomeParametroNegociacao)).append(" ) ");
		
		return hql.toString();
	}

	private String obterSubSelectDataVencimentoParcela(String nomeParametroNegociacao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select max(parcelaData.dataVencimento) ")
			
			.append(" from Cobranca cobrancaData ")
			
			.append(" join cobrancaData.divida dividaData ")
			
			.append(" join dividaData.consolidados consolidadoData ")
			
			.append(" join consolidadoData.movimentos movimentoFinanceiroData ")
			
			.append(" join movimentoFinanceiroData.parcelaNegociacao parcelaData ")
			
			.append(" join parcelaData.negociacao negociacaoData ")
			
			.append(" where negociacaoData.id = "+nomeParametroNegociacao+" ")
			
			.append(" and cobrancaData.statusCobranca = 'PAGO' ");
		
		return hql.toString();
	}
		
	private String obterSubSelectValorParcela(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cobrancaValor.valor ")
			
			.append(" from Cobranca cobrancaValor ")
			
			.append(" join cobrancaValor.divida dividaValor ")
			
			.append(" join dividaValor.consolidados consolidadoValor ")
			
			.append(" join consolidadoValor.movimentos movimentoFinanceiroValor ")
			
			.append(" join movimentoFinanceiroValor.parcelaNegociacao parcelaValor ")
			
			.append(" join parcelaValor.negociacao negociacaoValor ")
			
			.append(" where negociacaoValor.id= negociacao.id ")
			
			.append(" and cobrancaValor.statusCobranca = 'PAGO' ")
			
			.append(" and parcelaValor.dataVencimento = ( ").append(obterSubSelectDataVencimentoParcela(" negociacao.id ")).append(" ) ");
		
		return hql.toString();
	}
	
	
	private String obterSubSelectTipoCobranca(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cobrancaValor.tipoCobranca ")
			
			.append(" from Cobranca cobrancaValor ")
			
			.append(" join cobrancaValor.divida dividaValor ")
			
			.append(" join dividaValor.consolidados consolidadoValor ")
			
			.append(" join consolidadoValor.movimentos movimentoFinanceiroValor ")
			
			.append(" join movimentoFinanceiroValor.parcelaNegociacao parcelaValor ")
			
			.append(" join parcelaValor.negociacao negociacaoValor ")
			
			.append(" where negociacaoValor.id= negociacao.id ")
			
			.append(" and cobrancaValor.statusCobranca = 'PAGO' ")
			
			.append(" and parcelaValor.dataVencimento = ( ").append(obterSubSelectDataVencimentoParcela(" negociacao.id ")).append(" ) ");
		
		return hql.toString();
	}
	
	
	private String obterSubSelectCountParcelas(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(parcelaNegociacao.id) ")
			
			.append(" from ParcelaNegociacao parcelaNegociacao ")
			
			.append(" join parcelaNegociacao.negociacao negociacaoCount ")
			
			.append("  where negociacaoCount.id = negociacao.id ");
		
		return hql.toString();
	}
	
	private String obrterSubSelectNumeroParcelaAtual(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(parcelaNumeroParcela.id) ")
			
			.append(" from Cobranca cobrancaNumeroParcela ")
			
			.append(" join cobrancaNumeroParcela.divida dividaNumeroParcela ")
			
			.append(" join dividaNumeroParcela.consolidados consolidadoNumeroParcela ")
			
			.append(" join consolidadoNumeroParcela.movimentos movimentoFinanceiroNumeroParcela ")
			
			.append(" join movimentoFinanceiroNumeroParcela.parcelaNegociacao parcelaNumeroParcela ")
			
			.append(" join parcelaNumeroParcela.negociacao negociacaoNumeroParcela ")
			
			.append(" where negociacaoNumeroParcela.id = negociacao.id ")
			
			.append(" and parcelaNumeroParcela.dataVencimento <= (").append(obterSubSelectDataVencimentoParcela(" negociacao.id ")).append(" ) ");
			
		return hql.toString();
	}
	
	public Long obterIdCobrancaPor(Long idNegociacao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select cobrancaValor.id ")

				.append(" from Cobranca cobrancaValor ")

				.append(" join cobrancaValor.divida dividaValor ")

				.append(" join dividaValor.consolidados consolidadoValor ")

				.append(" join consolidadoValor.movimentos movimentoFinanceiroValor ")

				.append(" join movimentoFinanceiroValor.parcelaNegociacao parcelaValor ")

				.append(" join parcelaValor.negociacao negociacaoValor ")

				.append(" where negociacaoValor.id = :idNegociacao ")

				.append(" and cobrancaValor.statusCobranca = :statusCobranca ")

				.append(" and parcelaValor.dataVencimento = ( ").append(obterSubSelectDataVencimentoParcela(" :idNegociacao ")).append(" ) ")
				
				.append(" and EXISTS (").append(this.subSelectUtimaParcelaPendenteAprovacao(" :idNegociacao ")).append(")");

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("idNegociacao", idNegociacao);
		query.setParameter("statusCobranca", StatusCobranca.PAGO);

		try {

			return (Long) query.uniqueResult();

		} catch (NonUniqueResultException e) {

			return null;
		}
	}

	@Override
	public Negociacao obterNegociacaoPorMovFinanceiroId(Long movFinanId) {
		
		Query query = 
			this.getSession().createQuery(
				"select n from Negociacao n join n.movimentosFinanceiroCota m where m.id = :movFinanId");
		
		query.setParameter("movFinanId", movFinanId);
		
		return (Negociacao) query.uniqueResult();
	}

	@Override
	public BigDecimal obterValorPagoDividaNegociadaComissao(Long negociacaoId) {
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" sum (m.valor) from ")
		   .append(" Negociacao n ")
		   .append(" join n.movimentosFinanceiroCota m ")
		   .append(" where n.id = :negociacaoId ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("negociacaoId", negociacaoId);
		
		return (BigDecimal) query.uniqueResult();
	}
}