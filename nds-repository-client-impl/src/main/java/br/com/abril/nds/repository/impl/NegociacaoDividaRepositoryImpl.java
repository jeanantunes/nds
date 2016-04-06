package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.ConsultaNegociacaoDividaDTO;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacoesDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoNegociacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

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
		hql.append(" datediff(' ");
		hql.append( filtro.getDataOperacao());
		hql.append("', cobranca.dataVencimento) as prazo, ");
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
		hql.append(" AND cobranca.dataPagamento is null ");
		
		hql.append(" AND divida.status not in (:statusDividas) ");
	}
	
	private void setParametrosObterNegociacaoPorCota(Query query, FiltroConsultaNegociacaoDivida filtro){
		
		query.setParameter("numCota", filtro.getNumeroCota());
		query.setParameter("status", StatusCobranca.NAO_PAGO);
		query.setParameterList("statusDividas", Arrays.asList(StatusDivida.PENDENTE_INADIMPLENCIA, StatusDivida.POSTERGADA));
		
	}

	@Override
	public Negociacao obterNegociacaoPorCobranca(Long id) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select n.* from consolidado_financeiro_cota cfc ");
		sql.append(" join consolidado_mvto_financeiro_cota cfmc on cfmc.CONSOLIDADO_FINANCEIRO_ID=cfc.ID ");
		sql.append(" join movimento_financeiro_cota mfc on cfmc.MVTO_FINANCEIRO_COTA_ID=mfc.ID ");
		sql.append(" join parcela_negociacao pn on pn.MOVIMENTO_FINANCEIRO_ID=mfc.ID ");
		sql.append(" join negociacao n on n.ID=pn.NEGOCIACAO_ID ");
		sql.append(" where cfc.ID=:idConsolidado ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		query.setParameter("idConsolidado", id); 
		
		((SQLQuery) query).addEntity(Negociacao.class);
		
		query.setMaxResults(1);

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

	@Override
	public Negociacao obterNegociacaoPorComissaoCota(Long idCota){
		
		final String hql = "select ne from Negociacao ne " +
				" join ne.cobrancasOriginarias co "+
				" join co.cota cota "+
				" where cota.id = :idCota "+
				" and ne.comissaoParaSaldoDivida is not null "+
				" and ne.valorDividaPagaComissao is not null "+
				" and ne.valorDividaPagaComissao > 0 "+
				" and ne.tipoNegociacao = :tipoComissao "+
				" order by ne.dataCriacao ";
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		query.setParameter("tipoComissao", TipoNegociacao.COMISSAO);
		
		query.setMaxResults(1);

		return (Negociacao) query.uniqueResult();
	}
	
	public Long obterQuantidadeNegociacaoFollowup(FiltroFollowupNegociacaoDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(negociacao.id) ").append(this.obterSqlFromConsultaNegociacao());
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("statusDivida", StatusDivida.NEGOCIADA);
		
		query.setParameter("tipoNegociacaoComissao", TipoNegociacao.COMISSAO);
		
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
			
			//.append("(").append(this.obterSubSelectDataOperacao(" negociacao.id ")).append(")").append(" as dataVencimento, ")
			
			
			.append(" cobranca.dataVencimento ").append(" as dataVencimento, ")
			
			
			.append("(").append(this.obterSubSelectValorParcela()).append(")").append(" as valorParcela, ")
			
			.append("(").append(this.obrterSubSelectNumeroParcelaAtual()).append(")").append(" as numeroParcelaAtual ,")
			
			.append("(").append(this.obterSubSelectCountParcelas()).append(")").append(" as quantidadeParcelas ");

		
		hql.append(this.obterSqlFromConsultaNegociacao());
		
		hql.append(this.obterOrdenacaoConsulta(filtro));
				
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("statusDivida", StatusDivida.NEGOCIADA);
		
		query.setParameter("tipoNegociacaoComissao", TipoNegociacao.COMISSAO);
		
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
			
			.append(" and cobranca.dataVencimento = ("+this.obterSubSelectDataOperacao()+")")
			
			.append(" and ((negociacao.tipoNegociacao <> :tipoNegociacaoComissao) or ")
			
			.append("      (negociacao.valorDividaPagaComissao is not null and negociacao.valorDividaPagaComissao > 0))  ")
			
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
			
			.append(" and parcelaPendente.dataVencimento = ( ").append(obterSubSelectDataOperacao()).append(" ) ");
		
		return hql.toString();
	}

	private String obterSubSelectDataOperacao(){
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select distrib.dataOperacao from Distribuidor distrib");

		return hql.toString();
	}
	
    private String obterSubSelectDataVencimentoUltimaParcela(String nomeParametroNegociacao){
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select max(parcelaData.dataVencimento) ")
			
			.append(" from Cobranca cobrancaData ")
			
			.append(" join cobrancaData.divida dividaData ")
			
			.append(" join dividaData.consolidados consolidadoData ")
			
			.append(" join consolidadoData.movimentos movimentoFinanceiroData ")
			
			.append(" join movimentoFinanceiroData.parcelaNegociacao parcelaData ")
			
			.append(" join parcelaData.negociacao negociacaoData ")
			
			.append(" where negociacaoData.id = "+nomeParametroNegociacao+" ");

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
			
			.append(" and parcelaValor.dataVencimento = ( ").append(obterSubSelectDataVencimentoUltimaParcela(" negociacao.id ")).append(" ) ");

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
			
			.append(" and parcelaValor.dataVencimento = ( ").append(obterSubSelectDataVencimentoUltimaParcela(" negociacao.id ")).append(" ) ");
		
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
			
			.append(" and parcelaNumeroParcela.dataVencimento <= (").append(obterSubSelectDataVencimentoUltimaParcela(" negociacao.id ")).append(" ) ");
			
		return hql.toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> obterListaNossoNumeroPorNegociacao(Long idNegociacao) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" select cob.NOSSO_NUMERO as nossoNumero from negociacao n ");
		sql.append(" join parcela_negociacao pn on pn.NEGOCIACAO_ID=n.ID ");
		sql.append(" join movimento_financeiro_cota mfc on pn.MOVIMENTO_FINANCEIRO_ID=mfc.ID "); 
		sql.append(" join consolidado_mvto_financeiro_cota cmfc on cmfc.MVTO_FINANCEIRO_COTA_ID=mfc.ID ");
		sql.append(" join consolidado_financeiro_cota cfc on cfc.ID=cmfc.CONSOLIDADO_FINANCEIRO_ID ");
		sql.append(" join divida_consolidado dc on dc.CONSOLIDADO_ID=cfc.ID ");
		sql.append(" join divida d on d.ID=dc.DIVIDA_ID ");
		sql.append(" join cobranca cob on cob.DIVIDA_ID=d.ID ");
		sql.append(" where n.ID=:idNegociacao ");
		sql.append(" order by cob.DT_VENCIMENTO ");		
		
		Query query = this.getSession().createSQLQuery(sql.toString());
		
		((SQLQuery) query).addScalar("nossoNumero");
		
		query.setParameter("idNegociacao", idNegociacao);
		
		return query.list();
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

				.append(" and parcelaValor.dataVencimento = ( ").append(obterSubSelectDataVencimentoUltimaParcela(" :idNegociacao ")).append(" ) ")
				
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
    
    
    @Override
	public void updateValorDividaValorMovimento(final Long idConsolidado,final List<GrupoMovimentoFinaceiro> grupoMovimentoFinaceiros){
    	final StringBuilder sql =  new StringBuilder();
    	sql.append("update NEGOCIACAO nego ");
    	sql.append("join NEGOCIACAO_MOV_FINAN nego_movi on ");
    	sql.append("nego_movi.NEGOCIACAO_ID = nego.id ");
    	sql.append("join MOVIMENTO_FINANCEIRO_COTA movi on ");
    	sql.append("movi.id = nego_movi.MOV_FINAN_ID ");
    	sql.append("join TIPO_MOVIMENTO tipo on ");
    	sql.append("movi.TIPO_MOVIMENTO_ID = tipo.id and tipo.tipo = 'FINANCEIRO' ");
    	sql.append("join CONSOLIDADO_MVTO_FINANCEIRO_COTA con on ");
    	sql.append("con.MVTO_FINANCEIRO_COTA_ID = movi.id ");


    	sql.append("set VALOR_DIVIDA_PAGA_COMISSAO =  VALOR_DIVIDA_PAGA_COMISSAO + movi.VALOR ");

    	sql.append("where con.CONSOLIDADO_FINANCEIRO_ID = :idConsolidado ");
    	sql.append("and tipo.GRUPO_MOVIMENTO_FINANCEIRO in (:grupoMovimentoFinaceiros) ");
    	sql.append("AND NOT EXISTS(SELECT parcela.id FROM PARCELA_NEGOCIACAO parcela where parcela.NEGOCIACAO_ID = nego.id)");
    	
    	 this.getSession().createSQLQuery(sql.toString() )
	        .setParameter("idConsolidado", idConsolidado)
	        .setParameterList("grupoMovimentoFinaceiros", grupoMovimentoFinaceiros)
	        .executeUpdate();
    }
    
    @Override
	public void removeNegociacaoMovimentoFinanceiroByIdConsolidadoAndGrupos(Long idConsolidado, List<GrupoMovimentoFinaceiro> grupoMovimentoFinaceiros){
		
    	final StringBuilder sql =  new StringBuilder();
    	sql.append("DELETE nego_movi from NEGOCIACAO_MOV_FINAN nego_movi ");
    	sql.append("join MOVIMENTO_FINANCEIRO_COTA movi on ");
    	sql.append("movi.id = nego_movi.MOV_FINAN_ID ");
    	sql.append("join TIPO_MOVIMENTO tipo on ");
    	sql.append("movi.TIPO_MOVIMENTO_ID = tipo.id and tipo.tipo = 'FINANCEIRO' ");
    	sql.append("join CONSOLIDADO_MVTO_FINANCEIRO_COTA con on ");
    	sql.append("con.MVTO_FINANCEIRO_COTA_ID = movi.id ");


    	sql.append("where con.CONSOLIDADO_FINANCEIRO_ID = :idConsolidado ");
    	sql.append("and tipo.GRUPO_MOVIMENTO_FINANCEIRO in (:grupoMovimentoFinaceiros) ");
    	sql.append("AND NOT EXISTS(SELECT parcela.id FROM PARCELA_NEGOCIACAO parcela where parcela.NEGOCIACAO_ID = nego_movi.NEGOCIACAO_ID)");
    	
    	 this.getSession().createSQLQuery(sql.toString() )
	        .setParameter("idConsolidado", idConsolidado)
	        .setParameterList("grupoMovimentoFinaceiros", grupoMovimentoFinaceiros)
	        .executeUpdate();
		
	}
    
    
	@Override
	@SuppressWarnings("unchecked")
	public List<ConsultaNegociacaoDividaDTO> buscarNegociacaoDivida(FiltroConsultaNegociacoesDTO filtro){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("   SELECT  ");
		sql.append("       ct.NUMERO_COTA as numeroCota, ");
		sql.append("       coalesce(ps.NOME_FANTASIA, ps.RAZAO_SOCIAL, ps.NOME, '') as nomeCota, ");
		sql.append("       ct.SITUACAO_CADASTRO as statusCota, ");
		sql.append("       ROUND(ng.VALOR_ORIGINAL, 2) as dividaInicial, ");
		sql.append("       ROUND(cb.ENCARGOS, 2) as valorEncargos, ");
		sql.append("       ROUND((COALESCE(cb.ENCARGOS, 0) + cb.VALOR), 2) as dividaNegociada, ");
		sql.append("       cb.DT_EMISSAO as dataNegociacao, ");
		sql.append("       pn.DATA_VENCIMENTO as dataVencimento, ");
		sql.append("       (select count(id) from parcela_negociacao where NEGOCIACAO_ID = nco.NEGOCIACAO_ID)  as countParcelas,  ");
		sql.append("       ROUND(if(mfc2.valor is null, mfc.VALOR, mfc2.valor), 2) valorParcela, ");

//		sql.append("       if(cb2.STATUS_COBRANCA is null, cb.STATUS_COBRANCA, cb2.STATUS_COBRANCA) situacaoParcela, ");

		sql.append("       if(cb2.STATUS_COBRANCA = 'NAO_PAGO', ");
		sql.append("            CASE ");
		sql.append("              WHEN  DATEDIFF(pn.data_vencimento, (select data_operacao from distribuidor)) > 0 THEN 'A_VENCER' ");
		sql.append("              WHEN  DATEDIFF(pn.data_vencimento, (select data_operacao from distribuidor)) <= 0 THEN cb2.STATUS_COBRANCA  ");
		sql.append("            END,  ");
		sql.append("            if(cb2.STATUS_COBRANCA is null, cb.STATUS_COBRANCA,cb2.STATUS_COBRANCA)) as situacaoParcela, ");
		
		sql.append("       cb2.id as idCobranca, ");
		sql.append("       ng.id as idNegociacao ");
		
		sql.append("   FROM cobranca cb ");
		
		sql.append("   JOIN divida dv ");
		sql.append("     ON cb.DIVIDA_ID = dv.ID ");
		sql.append("   JOIN negociacao_cobranca_originaria nco ");
		sql.append("     ON nco.COBRANCA_ID = cb.ID ");
		sql.append("   JOIN negociacao ng ");
		sql.append("     ON nco.NEGOCIACAO_ID = ng.ID ");
		sql.append("   JOIN forma_cobranca fc ");
		sql.append("     ON ng.FORMA_COBRANCA_ID = fc.id ");
		sql.append("   JOIN parcela_negociacao pn ");
		sql.append("     ON pn.NEGOCIACAO_ID = ng.ID ");
		sql.append("   JOIN movimento_financeiro_cota mfc  ");
		sql.append("     ON pn.MOVIMENTO_FINANCEIRO_ID = mfc.id ");
		sql.append("   LEFT OUTER JOIN consolidado_mvto_financeiro_cota cmfc  ");
		sql.append("     ON cmfc.MVTO_FINANCEIRO_COTA_ID = mfc.id ");
		sql.append("   LEFT OUTER JOIN consolidado_financeiro_cota cfc  ");
		sql.append("     ON cfc.id = cmfc.CONSOLIDADO_FINANCEIRO_ID ");
		sql.append("   LEFT OUTER JOIN divida_consolidado dc  ");
		sql.append("     ON dc.CONSOLIDADO_ID = cfc.ID ");
		sql.append("   LEFT OUTER JOIN movimento_financeiro_cota mfc2 ");
		sql.append("     ON cmfc.MVTO_FINANCEIRO_COTA_ID = mfc2.ID ");
		sql.append("   LEFT OUTER JOIN divida dv2 ");
		sql.append("     ON dv2.id = dc.divida_id ");
		sql.append("   LEFT OUTER JOIN cobranca cb2 ");
		sql.append("     ON dv2.id = cb2.divida_id ");
		sql.append("   JOIN cota ct ");
		sql.append("     ON cb.COTA_ID = ct.ID ");
		sql.append("   JOIN pessoa ps  ");
		sql.append("     ON ct.PESSOA_ID = ps.ID ");
		sql.append("   WHERE 1 = 1 ");
		
		this.getParametrosBuscarNegociacaoDivida(filtro, sql, null);
		
		sql.append(" ORDER BY ");
		
		if (filtro.getPaginacao() != null){
		
        	if(filtro.getPaginacao().getSortColumn().equalsIgnoreCase("PARCELA")){
        		sql.append(" dividaInicial ");
        	}else{
        		sql.append(filtro.getPaginacao().getSortColumn());
        	}

        	sql.append(" "+filtro.getPaginacao().getSortOrder());
			
		} else {
			sql.append(" dataNegociacao desc ");
		}
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		this.getParametrosBuscarNegociacaoDivida(filtro, null, query);

		query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
		query.addScalar("nomeCota", StandardBasicTypes.STRING);
		query.addScalar("statusCota", StandardBasicTypes.STRING);
		query.addScalar("dividaInicial", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("valorEncargos", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("dividaNegociada", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("dataNegociacao", StandardBasicTypes.DATE);
		query.addScalar("dataVencimento", StandardBasicTypes.DATE);
		query.addScalar("countParcelas", StandardBasicTypes.INTEGER);
		query.addScalar("valorParcela", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("situacaoParcela", StandardBasicTypes.STRING);
		query.addScalar("idCobranca", StandardBasicTypes.LONG);
		query.addScalar("idNegociacao", StandardBasicTypes.LONG);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaNegociacaoDividaDTO.class));
		
		this.configurarPaginacao(filtro, query);
		
		return query.list();
	}
	
	public void getParametrosBuscarNegociacaoDivida(FiltroConsultaNegociacoesDTO filtro, StringBuilder sql, Query query){
		
		if(filtro.getNumeroCota() != null && !filtro.getNumeroCota().equals("")){
			if(sql != null){
				sql.append(" AND ct.NUMERO_COTA = :numeroCota ");
			}else{
				query.setParameter("numeroCota", filtro.getNumeroCota());
			}
		}
		
		if(filtro.getSituacaoParcela() != null && !filtro.getSituacaoParcela().equals("")){
			if(sql != null){
				if(filtro.getSituacaoParcela().equalsIgnoreCase("A_VENCER")){
					
					sql.append(" AND DATEDIFF(pn.data_vencimento, (select data_operacao from distribuidor)) > 0 ");
					sql.append(" AND cb2.STATUS_COBRANCA = 'NAO_PAGO' ");
					
				}else{
					if(filtro.getSituacaoParcela().equalsIgnoreCase("NAO_PAGO")){
						sql.append(" AND DATEDIFF(pn.data_vencimento, (select data_operacao from distribuidor)) <= 0 ");
						sql.append(" AND cb2.STATUS_COBRANCA = 'NAO_PAGO' ");
						
					}else{
						if(filtro.getSituacaoParcela().equalsIgnoreCase("PAGO")){
							sql.append(" AND cb2.STATUS_COBRANCA = 'PAGO' ");
						}
					}
				}
				
			}else{	
//				query.setParameter("numeroCota", filtro.getNumeroCota());
			}
		}
		
		if(filtro.getDataNegociacaoDe() != null && filtro.getDataNegociacaoAte() != null){
			if(sql != null){
				sql.append(" AND cb.DT_EMISSAO BETWEEN DATE_FORMAT(:dataNegociacaoDe,'%Y-%m-%d') AND DATE_FORMAT(:dataNegociacaoAte,'%Y-%m-%d') ");
			}else{
				query.setParameter("dataNegociacaoDe", filtro.getDataNegociacaoDe());
				query.setParameter("dataNegociacaoAte", filtro.getDataNegociacaoAte());
			}
		}
		
		if(filtro.getDataVencimentoDe() != null && filtro.getDataVencimentoAte() != null){
			if(sql != null){
				sql.append(" AND pn.DATA_VENCIMENTO BETWEEN DATE_FORMAT(:dataVencimentoDe,'%Y-%m-%d') AND DATE_FORMAT(:dataVencimentoAte,'%Y-%m-%d') ");
			}else{
				query.setParameter("dataVencimentoDe", filtro.getDataVencimentoDe());
				query.setParameter("dataVencimentoAte", filtro.getDataVencimentoAte());
			}
		}
		
	}
	
	private void configurarPaginacao(FiltroDTO filtro, Query query) {

		PaginacaoVO paginacao = filtro.getPaginacao();

		if (paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}

		if(paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}

		if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}
	}
}