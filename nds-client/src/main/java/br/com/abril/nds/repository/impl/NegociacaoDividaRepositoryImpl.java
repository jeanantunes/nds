package br.com.abril.nds.repository.impl;

import java.util.List;

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
import br.com.abril.nds.repository.NegociacaoDividaRepository;

@Repository
public class NegociacaoDividaRepositoryImpl extends AbstractRepositoryModel<Negociacao, Long> 
											implements NegociacaoDividaRepository{

	public NegociacaoDividaRepositoryImpl() {
		super(Negociacao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NegociacaoDividaDTO> obterCotaPorNumero(FiltroConsultaNegociacaoDivida filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cobranca.dataEmissao as dtEmissao, ");
		hql.append(" cobranca.dataVencimento as dtVencimento, ");
		hql.append(" cobranca.valor as vlDivida, ");
		hql.append(" cobranca.encargos as encargos, ");
		hql.append(" CASE WHEN (datediff(cobranca.dataVencimento, current_date())) < 0 ");
		hql.append(" THEN 0 ELSE datediff(cobranca.dataVencimento, current_date()) END  as prazo, ");
		hql.append(" (cobranca.encargos + cobranca.valor) as total, ");
		hql.append(" cobranca.id as idCobranca ");
		
		this.getObterCotaPorNumeroFrom(hql, filtro);
		
		if (filtro.getPaginacaoVO() != null &&
				filtro.getPaginacaoVO().getSortColumn() != null){
			
			hql.append(" order by ").append(filtro.getPaginacaoVO().getSortColumn());
			
			if (filtro.getPaginacaoVO().getOrdenacao() != null){
				
				hql.append(" ").append(filtro.getPaginacaoVO().getOrdenacao());
			}
		}
	
		Query query = getSession().createQuery(hql.toString());
		query.setFirstResult((filtro.getPaginacaoVO().getPaginaAtual() - 1) * filtro.getPaginacaoVO().getQtdResultadosPorPagina());
		query.setMaxResults(filtro.getPaginacaoVO().getQtdResultadosPorPagina());
		this.setParametrosObterCotaPorNumero(query, filtro);

		query.setResultTransformer(new AliasToBeanResultTransformer(NegociacaoDividaDTO.class));
		
		return query.list();
	}
	
	@Override
	public Long obterCotaPorNumeroCount(FiltroConsultaNegociacaoDivida filtro){
		
		StringBuilder hql = new StringBuilder(" select count (cobranca.id) ");
		this.getObterCotaPorNumeroFrom(hql, filtro);
		
		Query query = getSession().createQuery(hql.toString());
		
		this.setParametrosObterCotaPorNumero(query, filtro);
		
		return (Long) query.uniqueResult();
	}
	
	private void getObterCotaPorNumeroFrom(StringBuilder hql, FiltroConsultaNegociacaoDivida filtro){
		
		hql.append(" FROM Cobranca cobranca ");
		hql.append(" JOIN cobranca.cota ");
		hql.append(" WHERE cobranca.cota.numeroCota = :numCota ");
		hql.append(" AND cobranca.statusCobranca = :status ");
		
		if(!filtro.isLancamento()){
			hql.append(" AND cobranca.dataVencimento <= current_date() ");
		}
	}
	
	private void setParametrosObterCotaPorNumero(Query query, FiltroConsultaNegociacaoDivida filtro){
		
		query.setParameter("numCota", filtro.getNumeroCota());
		query.setParameter("status", StatusCobranca.NAO_PAGO);
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
		   .append(" and ne.valorDividaPagaComissao > 0 ");
		
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
			
			.append(" negociacao.formaCobranca.tipoCobranca as tipoCobranca , ")
			
			.append("(").append(this.obterSubSelectDataVencimentoParcela()).append(")").append(" as dataVencimento, ")
			
			.append("(").append(this.obterSubSelectValorParcela()).append(")").append(" as valorParcela, ")
			
			.append("(").append(this.obrterSubSelectNumeroParcelaAtual()).append(")").append(" as numeroParcelaAtual ,")
			
			.append("(").append(this.obterSubSelectCountParcelas()).append(")").append(" as quantidadeParcelas ");
		
		hql.append(this.obterSqlFromConsultaNegociacao());
		
		hql.append(this.obterOrdenacaoConsulta(filtro));
				
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("statusDivida", StatusDivida.NEGOCIADA);
		query.setParameter("statusCobranca", StatusCobranca.NAO_PAGO);
		
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
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		hql.append(" order by cota.numeroCota ");
		
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
			
			.append(" and negociacao.valorDividaPagaComissao is null  ");
		
		return hql.toString();
	}
	
	private String obterSubSelectDataVencimentoParcela(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select min(parcelaData.dataVencimento) ")
			
			.append(" from Cobranca cobrancaData ")
			
			.append(" join cobrancaData.divida dividaData ")
			
			.append(" join dividaData.consolidado consolidadoData ")
			
			.append(" join consolidadoData.movimentos movimentoFinanceiroData ")
			
			.append(" join movimentoFinanceiroData.parcelaNegociacao parcelaData ")
			
			.append(" join parcelaData.negociacao negociacaoData ")
			
			.append(" where negociacaoData.id = negociacao.id ")
			
			.append(" and cobrancaData.statusCobranca = :statusCobranca ");
		
		return hql.toString();
	}
	
	private String obterSubSelectValorParcela(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cobrancaValor.valor ")
			
			.append(" from Cobranca cobrancaValor ")
			
			.append(" join cobrancaValor.divida dividaValor ")
			
			.append(" join dividaValor.consolidado consolidadoValor ")
			
			.append(" join consolidadoValor.movimentos movimentoFinanceiroValor ")
			
			.append(" join movimentoFinanceiroValor.parcelaNegociacao parcelaValor ")
			
			.append(" join parcelaValor.negociacao negociacaoValor ")
			
			.append(" where negociacaoValor.id= negociacao.id ")
			
			.append(" and cobrancaValor.statusCobranca = :statusCobranca ")
			
			.append(" and parcelaValor.dataVencimento = ( ").append(obterSubSelectDataVencimentoParcela()).append(" ) ");
		
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
			
			.append(" join dividaNumeroParcela.consolidado consolidadoNumeroParcela ")
			
			.append(" join consolidadoNumeroParcela.movimentos movimentoFinanceiroNumeroParcela ")
			
			.append(" join movimentoFinanceiroNumeroParcela.parcelaNegociacao parcelaNumeroParcela ")
			
			.append(" join parcelaNumeroParcela.negociacao negociacaoNumeroParcela ")
			
			.append(" where negociacaoNumeroParcela.id = negociacao.id ")
			
			.append(" and parcelaNumeroParcela.dataVencimento <= (").append(obterSubSelectDataVencimentoParcela()).append(" ) ");
			
		return hql.toString();
	}
	
}