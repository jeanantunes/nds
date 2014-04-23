package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoNegociacao;
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
		hql.append(" (COALESCE(cobranca.encargos, 0) + ceil(cobranca.valor)) as total, ");
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
		
		filtro.setCount(true);
		
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
		query.setParameter("statusDivida", StatusDivida.PENDENTE_INADIMPLENCIA);
		
		if(!filtro.isLancamento() ||  !filtro.isCount()) {
			query.setParameter("dataOperacao", filtro.getDataOperacao());
		}
	}

	@Override
	public Negociacao obterNegociacaoPorCobranca(Long id) {
		Query query = getSession().createQuery("select o from Negociacao o join o.cobrancasOriginarias c where c.id = " + id);
		return (Negociacao) query.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Negociacao obterNegociacaoPorDivida(Long id) {

		Query query = getSession().createQuery("select n from Negociacao n join n.cobrancasOriginarias c join c.divida d where d.id = " + id);
		return (Negociacao) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Negociacao> obterNegociacaoPorComissaoCota(Long idCota){
		
		final String hql = "select ne from Negociacao ne " +
				" join ne.cobrancasOriginarias co "+
				" join co.cota cota "+
				" where cota.id = :idCota "+
				" and ne.comissaoParaSaldoDivida is not null "+
				" and ne.valorDividaPagaComissao is not null "+
				" and ne.valorDividaPagaComissao > 0 "+
				" and ne.tipoNegociacao = :tipoComissao "+
				" group by ne.id "+
				" order by ne.dataCriacao ";
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		query.setParameter("tipoComissao", TipoNegociacao.COMISSAO);
		
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

    @Override
    public boolean verificarAtivacaoCotaAposPgtoParcela(Long idCobranca) {
        
        SQLQuery query = this.getSession().createSQLQuery(
                " select "+
                "    case when parcelaneg7_.ID = i.idParcelaAtivar then true else false end as ativa "+
                " from "+
                "    COBRANCA cobranca0_  "+
                " inner join "+
                "    COTA cota1_  "+
                "        on cobranca0_.COTA_ID=cota1_.ID  "+
                " inner join "+
                "    DIVIDA divida2_  "+
                "        on cobranca0_.DIVIDA_ID=divida2_.ID  "+
                " inner join "+
                "    DIVIDA_CONSOLIDADO consolidad3_  "+
                "        on divida2_.ID=consolidad3_.DIVIDA_ID  "+
                " inner join "+
                "    CONSOLIDADO_FINANCEIRO_COTA consolidad4_  "+
                "        on consolidad3_.CONSOLIDADO_ID=consolidad4_.ID  "+
                " inner join "+
                "    CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos5_  "+
                "        on consolidad4_.ID=movimentos5_.CONSOLIDADO_FINANCEIRO_ID  "+
                " inner join "+
                "    MOVIMENTO_FINANCEIRO_COTA movimentof6_  "+
                "        on movimentos5_.MVTO_FINANCEIRO_COTA_ID=movimentof6_.ID  "+
                " inner join "+
                "    PARCELA_NEGOCIACAO parcelaneg7_  "+
                "        on movimentof6_.ID=parcelaneg7_.MOVIMENTO_FINANCEIRO_ID  "+
                " inner join "+
                "    NEGOCIACAO negociacao8_  "+
                "        on parcelaneg7_.NEGOCIACAO_ID=negociacao8_.ID  "+
                " join "+
                "   ( "+
                "   select  "+
                "       n.id as idNeg "+
                "       , count(pn2.ID) as parcelaAtv "+
                "       , pn.id as idParcelaAtivar "+
                "   from  "+
                "       parcela_negociacao pn "+
                "   join  "+
                "       negociacao n on n.id = pn.NEGOCIACAO_ID "+
                "   join  "+
                "       parcela_negociacao pn2 on pn2.NEGOCIACAO_ID = n.id and pn2.DATA_VENCIMENTO <= pn.DATA_VENCIMENTO "+
                "   group by  "+
                "       pn.id "+
                "   order by  "+
                "       pn.DATA_VENCIMENTO) i on i.idNeg = negociacao8_.id "+
                " where "+
                "    ( "+
                "        negociacao8_.ATIVAR_PAGAMENTO_APOS_PARCELA is not null "+
                "    )  "+
                "    and cobranca0_.ID= :idCobranca  "+
                "    and cota1_.SITUACAO_CADASTRO<> :ativo "+
                "    and i.parcelaAtv = negociacao8_.ATIVAR_PAGAMENTO_APOS_PARCELA");
        
        query.setParameter("idCobranca", idCobranca);
        query.setParameter("ativo", SituacaoCadastro.ATIVO.name());
        
        query.addScalar("ativa", StandardBasicTypes.BOOLEAN);
        
        final Object ret = query.uniqueResult();
        
        if (ret == null){
            return false;
        }
        
        return (boolean) ret;
    }
}