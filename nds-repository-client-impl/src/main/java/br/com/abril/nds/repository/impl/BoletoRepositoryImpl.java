package br.com.abril.nds.repository.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO.OrdenacaoColunaDetalheBaixaBoleto;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
		
		hql.append(" select count(boleto) as quantidadePrevisao ");
		hql.append(this.obterFromWhereBoletosPrevistos());

		Query query = this.obterQueryBoletosPrevistos(hql.toString(), data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosLidos(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(baixaAutomatica) as quantidadeLidos ");
		hql.append(" from BaixaAutomatica baixaAutomatica ");
		hql.append(" where baixaAutomatica.dataPagamento = :data ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosBaixados(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(baixaAutomatica) as quantidadeBaixados ");
		hql.append(this.obterFromWhereBoletosBaixados());
		
		Query query = this.obterQueryBoletosBaixados(hql.toString(), data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosRejeitados(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(baixaAutomatica) as quantidadeRejeitados ");
		hql.append(this.obterFromWhereConsultaBaixaBoletos());
		
		Query query = this.obterQueryBoletosRejeitados(hql.toString(), data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosBaixadosComDivergencia(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(baixaAutomatica) as quantidadeBaixadosComDivergencia ");
		hql.append(this.obterFromWhereConsultaBaixaBoletos());

		Query query = this.obterQueryBoletosBaixadosComDivergencia(hql.toString(), data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosInadimplentes(Date dataVencimento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(boleto) as quantidadeInadimplentes ");
		hql.append(this.obterFromWhereBoletosInadimplentes());
		
		Query query = this.obterQueryBoletosInadimplentes(hql.toString(), dataVencimento);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterValorTotalBancario(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select sum(baixaAutomatica.valorPago) as valorTotalBancario ");
		hql.append(this.obterFromWhereConsultaTotalBancario());
		
		Query query = this.obterQureryTotalBancario(data, hql);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Long obterQuantidadeTotalBancario(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select banco as valorTotalBancario ");
		hql.append(this.obterFromWhereConsultaTotalBancario());
		hql.append(" group by banco, baixaAutomatica.dataPagamento ");
		
		Query query = this.obterQureryTotalBancario(data, hql);
		
		
		List<Banco> lista = (List<Banco>) query.list();
		
		return Long.valueOf(lista.size());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterBoletosPrevistos(FiltroDetalheBaixaBoletoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select boleto.cota.numeroCota as numeroCota, ")
  		   .append(" 		(case when (boleto.cota.pessoa.nome is not null)")
  		   .append("			then boleto.cota.pessoa.nome")
  		   .append("			else boleto.cota.pessoa.razaoSocial end) as nomeCota, ")
  		   .append(" 		boleto.banco.apelido as nomeBanco, ")
		   .append(" 		concat(boleto.banco.conta, '-', boleto.banco.dvConta) as numeroConta, ")
  		   .append(" 		boleto.nossoNumeroCompleto as nossoNumero, ")
		   .append(" 		boleto.valor as valorBoleto, ")
		   .append(" 		boleto.dataVencimento as dataVencimento ")
		   .append(this.obterFromWhereBoletosPrevistos());

		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(
				this.obterOrdenacaoConsultaBaixaBoletos(
					filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder()));
		}

		Query query = this.obterQueryBoletosPrevistos(hql.toString(), filtro.getData());

		this.paginarConsultasBaixaBoleto(query, filtro);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterBoletosBaixados(FiltroDetalheBaixaBoletoDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append("  boleto.cota.numeroCota as numeroCota, ");
		hql.append(" 		(case when (boleto.cota.pessoa.nome is not null)");
		hql.append("			then boleto.cota.pessoa.nome");
		hql.append("			else boleto.cota.pessoa.razaoSocial end) as nomeCota, ");
		hql.append("  baixaAutomatica.banco.apelido as nomeBanco, ");
		hql.append("  concat(baixaAutomatica.banco.conta, '-', baixaAutomatica.banco.dvConta) as numeroConta, ");
		hql.append("  boleto.nossoNumero as nossoNumero, ");
		hql.append("  baixaAutomatica.valorPago as valorBoleto, ");
		hql.append("  baixaAutomatica.dataPagamento as dataVencimento");
		hql.append(this.obterFromWhereBoletosBaixados());
		
		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(
				this.obterOrdenacaoConsultaBaixaBoletos(
					filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder()));
		}
		
		Query query = this.obterQueryBoletosBaixados(hql.toString(), filtro.getData());
		
		this.paginarConsultasBaixaBoleto(query, filtro);
		
		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterBoletosRejeitados(FiltroDetalheBaixaBoletoDTO filtro) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select baixaAutomatica.status as motivoRejeitado, ")
		   .append(" 		baixaAutomatica.banco.apelido as nomeBanco, ")
		   .append(" 		concat(baixaAutomatica.banco.conta, '-', baixaAutomatica.banco.dvConta) as numeroConta, ")
		   .append(" 		baixaAutomatica.valorPago as valorBoleto ")
		   .append(this.obterFromWhereConsultaBaixaBoletos());
		
		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(
				this.obterOrdenacaoConsultaBaixaBoletos(
					filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder()));
		}

		Query query = this.obterQueryBoletosRejeitados(hql.toString(), filtro.getData());
		
		this.paginarConsultasBaixaBoleto(query, filtro);

		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterBoletosBaixadosComDivergencia(FiltroDetalheBaixaBoletoDTO filtro) {
		
		StringBuilder hql = new StringBuilder("");
		
		hql.append(" select baixaAutomatica.status as motivoDivergencia, ")
		   .append(" 		baixaAutomatica.banco.apelido as nomeBanco, ")
		   .append(" 		concat(baixaAutomatica.banco.conta, '-', baixaAutomatica.banco.dvConta) as numeroConta, ")
		   .append(" 		boleto.valor as valorBoleto, ")
		   .append(" 		baixaAutomatica.valorPago as valorPago, ")
		   .append(" 		boleto.valor - baixaAutomatica.valorPago as valorDiferenca ")
		   .append(this.obterFromWhereConsultaBaixaBoletos());
		
		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(
				this.obterOrdenacaoConsultaBaixaBoletos(
					filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder()));
		}

		Query query = this.obterQueryBoletosBaixadosComDivergencia(hql.toString(), filtro.getData());
		
		this.paginarConsultasBaixaBoleto(query, filtro);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterBoletosInadimplentes(FiltroDetalheBaixaBoletoDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append("  boleto.cota.numeroCota as numeroCota, ");
		hql.append(" 		(case when (boleto.cota.pessoa.nome is not null)");
		hql.append("			then boleto.cota.pessoa.nome");
		hql.append("			else boleto.cota.pessoa.razaoSocial end) as nomeCota, ");
		hql.append("  boleto.banco.apelido as nomeBanco, ");
		hql.append("  concat(boleto.banco.conta, '-', boleto.banco.dvConta) as numeroConta, ");
		hql.append("  boleto.nossoNumero as nossoNumero, ");
		hql.append("  boleto.valor as valorBoleto, ");
		hql.append("  boleto.dataVencimento as dataVencimento");
		hql.append(this.obterFromWhereBoletosInadimplentes());
		
		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(
				this.obterOrdenacaoConsultaBaixaBoletos(
					filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder()));
		}

		Query query = this.obterQueryBoletosInadimplentes(hql.toString(), filtro.getData());
		
		this.paginarConsultasBaixaBoleto(query, filtro);
		
		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterTotalBancario(FiltroDetalheBaixaBoletoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select banco.apelido as nomeBanco, ")
		   .append(" 		concat(banco.conta, '-', banco.dvConta) as numeroConta, ")
  		   .append(" 		sum(baixaAutomatica.valorPago) as valorPago, ")
  		   .append(" 		baixaAutomatica.dataPagamento as dataVencimento ")
  		   .append(this.obterFromWhereConsultaTotalBancario())
  		   .append(" group by banco, baixaAutomatica.dataPagamento ");

		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {
			
			hql.append(
				this.obterOrdenacaoConsultaBaixaBoletos(
					filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder()));
		}
		
		Query query = this.obterQureryTotalBancario(filtro.getData(), hql);
		
		this.paginarConsultasBaixaBoleto(query, filtro);

		return query.list();
	}
	
	private String obterFromWhereBoletosPrevistos() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Boleto boleto ");
		hql.append(" where boleto.dataVencimento = :data ");
		
		return hql.toString();
	}
	
	private String obterFromWhereBoletosBaixados() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from BaixaAutomatica baixaAutomatica ");
		hql.append(" join baixaAutomatica.cobranca boleto ");
		hql.append(" where baixaAutomatica.dataPagamento = :data ");
		hql.append(" and baixaAutomatica.status in (:statusBaixa) ");
		
		return hql.toString();
	}
	
	private String obterFromWhereConsultaBaixaBoletos() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from BaixaAutomatica baixaAutomatica ");
		hql.append(" left join baixaAutomatica.cobranca boleto ");
		hql.append(" where baixaAutomatica.dataPagamento = :data ");
		hql.append(" and baixaAutomatica.status in (:statusBaixa) ");
		
		return hql.toString();
	}
	
	private String obterFromWhereBoletosInadimplentes() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Boleto boleto ");
		hql.append(" left join boleto.baixasCobranca baixaCobranca ");
		hql.append(" where boleto.dataVencimento = :data ");
		hql.append(" and baixaCobranca is null ");
		
		return hql.toString();
	}
	
	private String obterFromWhereConsultaTotalBancario() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from BaixaAutomatica baixaAutomatica ");
		hql.append(" join baixaAutomatica.banco banco ");
		hql.append(" left join baixaAutomatica.cobranca cobranca ");
		hql.append(" where baixaAutomatica.dataPagamento = :data ");
		
		return hql.toString();
	}
	
	private Query obterQueryBoletosPrevistos(String hql, Date data) {

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return query;
	}
	
	private Query obterQueryBoletosBaixados(String hql, Date data) {

		Query query = super.getSession().createQuery(hql.toString());
		
		List<StatusBaixa> listaParametros = new ArrayList<StatusBaixa>();
		
		listaParametros.add(StatusBaixa.PAGO);
		
		query.setParameter("data", data);
		query.setParameterList("statusBaixa", listaParametros);
		
		return query;
	}
	
	private Query obterQueryBoletosRejeitados(String hql, Date data) {

		Query query = super.getSession().createQuery(hql.toString());
		
		List<StatusBaixa> listaParametros = new ArrayList<StatusBaixa>();
		
		listaParametros.add(StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR);
		listaParametros.add(StatusBaixa.NAO_PAGO_DIVERGENCIA_DATA);
		listaParametros.add(StatusBaixa.NAO_PAGO_BAIXA_JA_REALIZADA);
		listaParametros.add(StatusBaixa.PAGO_BOLETO_NAO_ENCONTRADO);
		
		query.setParameter("data", data);
		query.setParameterList("statusBaixa", listaParametros);

		return query;
	}
	
	private Query obterQueryBoletosBaixadosComDivergencia(String hql, Date data) {

		Query query = super.getSession().createQuery(hql.toString());

		List<StatusBaixa> listaParametros = new ArrayList<StatusBaixa>();
		
		listaParametros.add(StatusBaixa.PAGO_DIVERGENCIA_DATA);
		listaParametros.add(StatusBaixa.PAGO_DIVERGENCIA_VALOR);

		query.setParameter("data", data);
		query.setParameterList("statusBaixa", listaParametros);

		return query;
	}
	
	private Query obterQueryBoletosInadimplentes(String hql, Date data) {
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return query;
	}
	
	private Query obterQureryTotalBancario(Date data, StringBuilder hql) {
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return query;
	}
	
	private String obterOrdenacaoConsultaBaixaBoletos(OrdenacaoColunaDetalheBaixaBoleto ordenacao, String orderSort) {
		
		StringBuilder orderBy = new StringBuilder();
		
		orderBy.append(" order by ");
		orderBy.append(ordenacao.toString());
		orderBy.append(" ");
		orderBy.append(orderSort == null ? "asc" : orderSort);

		return orderBy.toString();
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Boleto> obterBoletosGeradosNaDataOperacaoDistribuidor(Date dataOperacao) {
		
		Criteria criteria = this.getSession().createCriteria(Boleto.class);
		criteria.add(Restrictions.eq("dataEmissao", dataOperacao));
		
		ProjectionList projectionList = Projections.projectionList();
	
		projectionList.add(Projections.property("banco").as("banco"));
	    projectionList.add(Projections.property("nossoNumeroCompleto").as("nossoNumeroCompleto"));
	    projectionList.add(Projections.property("dataVencimento").as("dataVencimento"));
	    projectionList.add(Projections.property("valor").as("valor"));
	    projectionList.add(Projections.property("dataEmissao").as("dataEmissao"));
	    
	    criteria.setProjection(projectionList);
	    
	    criteria.setResultTransformer(new AliasToBeanResultTransformer(Boleto.class));
	    
		return criteria.list();
	}
}
