package br.com.abril.nds.repository.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO.OrdenacaoColunaDetalheBaixaBoleto;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.vo.PaginacaoVO;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.financeiro.Boleto}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class BoletoRepositoryImpl extends AbstractRepositoryModel<Boleto,Long> implements BoletoRepository {

	
	/**
	 * Construtor padrão
	 */
	public BoletoRepositoryImpl() {
		super(Boleto.class);
	}

	/**
	 * Método responsável por obter a quantidade de boletos
	 * @param filtro
	 * @return quantidade: quantidade de boletos
	 */
	@Override
	public long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro){
		long quantidade = 0;
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(b) from Boleto b where ");		
		hql.append(" b.cota.numeroCota = :ncota ");
		
		if (filtro.getDataVencimentoDe()!=null){
		    hql.append(" and b.dataVencimento >= :vctode ");
		}
		if (filtro.getDataVencimentoAte()!=null){
		    hql.append(" and b.dataVencimento <= :vctoate ");
		}
		if (filtro.getStatus()!=null){	  
			hql.append(" and b.statusCobranca = :status");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("ncota", filtro.getNumeroCota());
		
		if (filtro.getDataVencimentoDe()!=null){
		    query.setDate("vctode", filtro.getDataVencimentoDe());
		}
		if (filtro.getDataVencimentoAte()!=null){
		    query.setDate("vctoate", filtro.getDataVencimentoAte());
		}
		if (filtro.getStatus()!=null){	
		    query.setParameter("status", filtro.getStatus());
		}
		
		quantidade = (Long) query.uniqueResult();
		return quantidade;
	}
	
	/**
	 * Método responsável por obter uma lista de boletos
	 * @param filtro
	 * @return query.list(): lista de boletos
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Boleto> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {

		StringBuilder hql = new StringBuilder();
		hql.append(" from Boleto b where ");		
		hql.append(" b.cota.numeroCota = :ncota ");

		if (filtro.getDataVencimentoDe()!=null){
		    hql.append(" and b.dataVencimento >= :vctode ");
		}
		if (filtro.getDataVencimentoAte()!=null){
		    hql.append(" and b.dataVencimento <= :vctoate ");
		}
		if (filtro.getStatus()!=null){	  
			hql.append(" and b.statusCobranca = :status");
		}
		
		if (filtro.getOrdenacaoColuna() != null) {
			switch (filtro.getOrdenacaoColuna()) {
				case NOSSO_NUMERO:
					hql.append(" order by b.cota.numeroCota ");
					break;
				case DATA_EMISSAO:
					hql.append(" order by b.dataEmissao ");
					break;
				case DATA_VENCIMENTO:
					hql.append(" order by b.dataVencimento ");
					break;
				case DATA_PAGAMENTO:
					hql.append(" order by b.dataPagamento ");
					break;
				case ENCARGOS:
					hql.append(" order by b.encargos ");
					break;
				case VALOR:
					hql.append(" order by b.valor ");
					break;
				case TIPO_BAIXA:
					hql.append(" order by b.tipoBaixa ");
					break;
				case STATUS_COBRANCA:
					hql.append(" order by b.statusCobranca ");
					break;
				default:
					break;
			}
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}	
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("ncota", filtro.getNumeroCota());
		
		if (filtro.getDataVencimentoDe()!=null){
		    query.setDate("vctode", filtro.getDataVencimentoDe());
		}
		if (filtro.getDataVencimentoAte()!=null){
		    query.setDate("vctoate", filtro.getDataVencimentoAte());
		}
		if (filtro.getStatus()!=null){	
		    query.setParameter("status", filtro.getStatus());
		}

        if (filtro.getPaginacao() != null) {
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}

		return query.list();
	}
	
	/**
	 * Método responsável por obter Boleto individual 
	 * @param nossoNumero
	 * @param dividaAcumulada
	 */
	@Override
	public Boleto obterPorNossoNumero(String nossoNumero, Boolean dividaAcumulada) {
		
		Criteria criteria = super.getSession().createCriteria(Boleto.class);
		
		criteria.add(Restrictions.eq("nossoNumero", nossoNumero));
		
		if (dividaAcumulada != null) {
			
			criteria.createAlias("divida", "divida");
			
			criteria.add(Restrictions.eq("divida.acumulada", dividaAcumulada));
		}
		
		criteria.setMaxResults(1);
		
		return (Boleto) criteria.uniqueResult();
	}
	
	@Override
	public Boleto obterPorNossoNumeroCompleto(String nossoNumeroCompleto, Boolean dividaAcumulada) {
		
		Criteria criteria = super.getSession().createCriteria(Boleto.class);
		
		criteria.add(Restrictions.eq("nossoNumeroCompleto", nossoNumeroCompleto));
		
		if (dividaAcumulada != null) {
			
			criteria.createAlias("divida", "divida");
			
			criteria.add(Restrictions.eq("divida.acumulada", dividaAcumulada));
		}
		
		criteria.setMaxResults(1);
		
		return (Boleto) criteria.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosPrevistos(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(cobranca) as quantidadePrevisao ");
		hql.append(" from Cobranca cobranca ");
		hql.append(" where cobranca.dataVencimento = :data ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosLidos(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(cobranca) as quantidadeLidos ");
		hql.append(" from Cobranca cobranca ");
		hql.append(" join cobranca.baixaCobranca baixaCobranca ");
		hql.append(" where baixaCobranca.dataBaixa = :data ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosBaixados(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(cobranca) as quantidadeBaixados ");
		hql.append(" from Cobranca cobranca ");
		hql.append(" join cobranca.baixaCobranca baixaCobranca ");
		hql.append(" where baixaCobranca.dataBaixa = :data ");
		hql.append(" and baixaCobranca.status in (:statusBoletosBaixados) ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		List<StatusBaixa> listaParametros = new ArrayList<StatusBaixa>();
		
		listaParametros.add(StatusBaixa.PAGO);
		listaParametros.add(StatusBaixa.PAGO_DIVERGENCIA_DATA);
		listaParametros.add(StatusBaixa.PAGO_DIVERGENCIA_VALOR);
		
		query.setParameter("data", data);
		query.setParameterList("statusBoletosBaixados", listaParametros);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosRejeitados(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(cobranca) as quantidadeRejeitados ");
		hql.append(obterFromWhereConsultaBaixaBoletos());
		
		Query query = obterQueryBoletosRejeitados(hql.toString(), data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosBaixadosComDivergencia(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(cobranca) as quantidadeBaixadosComDivergencia ");
		hql.append(obterFromWhereConsultaBaixaBoletos());

		Query query = obterQueryBoletosBaixadosComDivergencia(hql.toString(), data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosInadimplentes(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		// TODO:
		
		hql.append(" select ");
		hql.append(" count(cobranca) as quantidadeInadimplentes from Cobranca cobranca ");
		hql.append(" where cobranca.dataVencimento = :data ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterValorTotalBancario(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		// TODO:
		
		hql.append(" select ");
		hql.append(" sum(cobranca.valor) as valorTotalBancario from Cobranca cobranca ");
		hql.append(" where cobranca.dataVencimento = :data ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return (BigDecimal) query.uniqueResult();
	}

	private String obterFromWhereConsultaBaixaBoletos() {
		
		StringBuilder hql = new StringBuilder("");
		
		hql.append(" from Cobranca cobranca ");
		hql.append(" join cobranca.baixaCobranca baixaCobranca ");
		hql.append(" where baixaCobranca.dataBaixa = :data ");
		hql.append(" and baixaCobranca.status in (:statusBoletos) ");
		
		return hql.toString();
	}

	private String obterOrdenacaoConsultaBaixaBoletos(OrdenacaoColunaDetalheBaixaBoleto ordenacao, String orderSort) {
		
		StringBuilder orderBy = new StringBuilder();
		
		orderBy.append(" order by ");
		orderBy.append(ordenacao.toString());
		orderBy.append(" ");
		orderBy.append(orderSort == null ? "asc" : orderSort);

		return orderBy.toString();
	}
	
	private Query obterQueryBoletosBaixadosComDivergencia(String hql, Date data) {

		Query query = super.getSession().createQuery(hql.toString());

		List<StatusBaixa> listaParametros = new ArrayList<StatusBaixa>();
		
		listaParametros.add(StatusBaixa.PAGO_DIVERGENCIA_DATA);
		listaParametros.add(StatusBaixa.PAGO_DIVERGENCIA_VALOR);

		query.setParameter("data", data);
		query.setParameterList("statusBoletos", listaParametros);

		return query;
	}

	private Query obterQueryBoletosRejeitados(String hql, Date data) {

		Query query = super.getSession().createQuery(hql.toString());
		
		List<StatusBaixa> listaParametros = new ArrayList<StatusBaixa>();
		
		listaParametros.add(StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR);
		listaParametros.add(StatusBaixa.NAO_PAGO_DIVERGENCIA_DATA);
		listaParametros.add(StatusBaixa.NAO_PAGO_BAIXA_JA_REALIZADA);
		
		query.setParameter("data", data);
		query.setParameterList("statusBoletos", listaParametros);

		return query;
	}
	
	private void paginarConsultasBaixaBoleto(Query query, FiltroDetalheBaixaBoletoDTO filtro) {
		
		if (filtro.getPaginacao() != null) {

			PaginacaoVO paginacao = filtro.getPaginacao();

			if (paginacao.getPosicaoInicial() != null) {
			
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}

			if (paginacao.getQtdResultadosPorPagina() != null) {
				
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(DetalheBaixaBoletoDTO.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterBoletosBaixadosComDivergencia(FiltroDetalheBaixaBoletoDTO filtro) {
		
		StringBuilder hql = new StringBuilder("");
		
		hql.append(" select baixaCobranca.status as motivoDivergencia, ")
		   .append(" 		cobranca.banco.nome as nomeBanco, ")
		   .append(" 		cobranca.banco.conta as numeroConta, ")
		   .append(" 		cobranca.valor as valorBoleto, ")
		   .append(" 		baixaCobranca.valorPago as valorPago, ")
		   .append(" 		cobranca.valor - baixaCobranca.valorPago as valorDiferenca ")
		   
		   .append(obterFromWhereConsultaBaixaBoletos());
		
		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(obterOrdenacaoConsultaBaixaBoletos(
				filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder())
			);
		}

		Query query = obterQueryBoletosBaixadosComDivergencia(hql.toString(), filtro.getData());
		
		paginarConsultasBaixaBoleto(query, filtro);
		
		return query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterBoletosRejeitados(FiltroDetalheBaixaBoletoDTO filtro) {
		// TODO Auto-generated method stub
		
		StringBuilder hql = new StringBuilder("");
		
		hql.append(" select baixaCobranca.status as motivoRejeitado, ")
		   .append(" 		cobranca.banco.nome as nomeBanco, ")
		   .append(" 		cobranca.banco.conta as numeroConta, ")
		   .append(" 		cobranca.valor as valorBoleto ")
		   
		   .append(obterFromWhereConsultaBaixaBoletos());
		
		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(obterOrdenacaoConsultaBaixaBoletos(
				filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder())
			);
		}

		Query query = obterQueryBoletosRejeitados(hql.toString(), filtro.getData());
		
		paginarConsultasBaixaBoleto(query, filtro);

		return query.list();
	}
}
