package br.com.abril.nds.repository.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO.OrdenacaoColunaDetalheBaixaBoleto;
import br.com.abril.nds.model.StatusCobranca;
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
		
		hql.append(" select count(boleto) as quantidadePrevisao ");
		
		hql.append(this.obterFromWhereBoletosPrevistos());

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosLidos(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(baixaCobranca) as quantidadeLidos ");
		hql.append(" from BaixaCobranca baixaCobranca ");
		hql.append(" where baixaCobranca.dataBaixa = :data ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosBaixados(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(boleto) as quantidadeBaixados ");
		hql.append(" from Boleto boleto ");
		hql.append(" join boleto.baixaCobranca baixaCobranca ");
		hql.append(" where baixaCobranca.dataBaixa = :data ");
		hql.append(" and boleto.statusCobranca = :statusCobranca ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		query.setParameter("statusCobranca", StatusCobranca.PAGO);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosRejeitados(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(boleto) as quantidadeRejeitados ");
		hql.append(obterFromWhereConsultaBaixaBoletos());
		
		Query query = obterQueryBoletosRejeitados(hql.toString(), data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosBaixadosComDivergencia(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(boleto) as quantidadeBaixadosComDivergencia ");
		hql.append(obterFromWhereConsultaBaixaBoletos());

		Query query = obterQueryBoletosBaixadosComDivergencia(hql.toString(), data);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeBoletosInadimplentes(Date dataVencimento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(boleto) as quantidadeInadimplentes ");
		hql.append(" from Boleto boleto ");
		hql.append(" where boleto.dataVencimento = :dataVencimento ");
		hql.append(" and boleto.statusCobranca = :statusCobranca ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataVencimento", dataVencimento);
		query.setParameter("statusCobranca", StatusCobranca.NAO_PAGO);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterValorTotalBancario(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select sum(baixaCobranca.valorPago) as valorTotalBancario ");
		hql.append(this.obterFromWhereConsultaTotalBancario());
		
		Query query = this.obterQureryTotalBancario(data, hql);
		
		return (BigDecimal) query.uniqueResult();
	}

	private Query obterQureryTotalBancario(Date data, StringBuilder hql) {
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		return query;
	}

	private String obterFromWhereConsultaBaixaBoletos() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Boleto boleto ");
		hql.append(" join boleto.baixaCobranca baixaCobranca ");
		hql.append(" where baixaCobranca.dataBaixa = :data ");
		hql.append(" and baixaCobranca.status in (:statusBoletos) ");
		
		return hql.toString();
	}
	
	private String obterFromWhereBoletosPrevistos() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Boleto boleto ");
		hql.append(" where boleto.dataVencimento >= :data ");
		
		return hql.toString();
	}
	
	private String obterFromWhereConsultaTotalBancario() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from BaixaCobranca baixaCobranca ");
		hql.append(" join baixaCobranca.banco banco ");
		hql.append(" where baixaCobranca.dataBaixa = :data ");
		
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
	
	private void paginarConsultasTotalBancario(Query query, FiltroDetalheBaixaBoletoDTO filtro) {
		
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
		   .append(" 		boleto.banco.nome as nomeBanco, ")
		   .append(" 		boleto.banco.conta ||'-'|| boleto.banco.dvConta as numeroConta, ")
		   .append(" 		boleto.valor as valorBoleto, ")
		   .append(" 		baixaCobranca.valorPago as valorPago, ")
		   .append(" 		boleto.valor - baixaCobranca.valorPago as valorDiferenca ")
		   
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

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select baixaCobranca.status as motivoRejeitado, ")
		   .append(" 		boleto.banco.nome as nomeBanco, ")
		   .append(" 		boleto.banco.conta||'-'||boleto.banco.dvConta as numeroConta, ")
		   .append(" 		boleto.valor as valorBoleto ")
		   
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterBoletosPrevistos(FiltroDetalheBaixaBoletoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select boleto.cota.numeroCota as numeroCota, ")
  		   .append(" 		boleto.cota.pessoa.nome as nomeCota, ")
  		   .append(" 		boleto.banco.nome as nomeBanco, ")
		   .append(" 		concat(boleto.banco.conta, '-', boleto.banco.dvConta) as numeroConta, ")
  		   .append(" 		boleto.nossoNumeroCompleto as nossoNumero, ")
		   .append(" 		boleto.valor as valorBoleto, ")
		   .append(" 		boleto.dataVencimento as dataVencimento ");

		hql.append(this.obterFromWhereBoletosPrevistos());

		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(obterOrdenacaoConsultaBaixaBoletos(
				filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder())
			);
		}

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("data", filtro.getData());

		paginarConsultasBaixaBoleto(query, filtro);
		
		return query.list();
	}
	
	/**
	 * Obtem consulta de dividas
	 * @return StringBuilder
	 */
	private StringBuilder obterHqlDividas(){
		
        StringBuilder hql = new StringBuilder(" select ");
		
		hql.append("  cota.numeroCota as numeroCota, ")
		   .append("  pessoa.nome as nomeCota, ")
		   .append("  banco.apelido as nomeBanco, ")
		   .append("  banco.conta||'-'||banco.dvConta as numeroConta, ")
		   .append("  boleto.nossoNumero as nossoNumero, ")
		   .append("  boleto.valor as valorBoleto, ")
		   .append("  boleto.dataVencimento as dataVencimento")
		   
		   .append(" from Boleto boleto ")
		   .append(" join boleto.cota cota ")
		   .append(" join cota.pessoa pessoa ")
		   .append(" join boleto.banco banco ");
		
		return hql;
	}
	
	/**
	 * Obtém Paginação e Ordenação
	 */
	private StringBuilder obterPaginacaoEOrdenacao(FiltroDetalheBaixaBoletoDTO filtro, StringBuilder hql){
		
		if (filtro.getOrdenacaoColuna() != null) {
			switch (filtro.getOrdenacaoColuna()) {
			    case NUMERO_COTA:
				    hql.append(" order by numeroCota ");
				    break;
			    case NOME_COTA:
				    hql.append(" order by nomeCota ");
				    break;
			    case NOME_BANCO:
				    hql.append(" order by nomeBanco ");
				    break;    
			    case NUMERO_CONTA:
					hql.append(" order by numeroConta ");
					break;	    
				case NOSSO_NUMERO:
					hql.append(" order by nossoNumero ");
					break;
				case VALOR_BOLETO:
					hql.append(" order by valorBoleto ");
					break;	
				case DATA_VENCIMENTO:
					hql.append(" order by dataVencimento ");
					break;
				default:
					break;
			}
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}	
		}
		
		return hql;
	}
	
	/**
	 * Obtém lista de Inadimplentes por data de vencimento
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * @return List<DetalheBaixaBoletoDTO>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DetalheBaixaBoletoDTO> obterInadimplentesPorData(FiltroDetalheBaixaBoletoDTO filtro){
		
		StringBuilder hql= this.obterHqlDividas();
		 
		hql.append(" where boleto.statusCobranca = :status ");
		hql.append(" and boleto.dataVencimento = :dataVencimento ");
		
		hql = this.obterPaginacaoEOrdenacao(filtro, hql);
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("status", StatusCobranca.NAO_PAGO);
		query.setParameter("dataVencimento", filtro.getData());
		
		if (filtro.getPaginacao() != null) {
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(Transformers.aliasToBean(DetalheBaixaBoletoDTO.class));
		
		return query.list();
	}
	
	/**
	 * Obtém lista de Baixados por data de vencimento
	 * @param FiltroDetalheBaixaBoletoDTO filtro
	 * @return List<DetalheBaixaBoletoDTO>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DetalheBaixaBoletoDTO> obterBaixadosPorData(FiltroDetalheBaixaBoletoDTO filtro){
		
		StringBuilder hql= this.obterHqlDividas();
		 
		hql.append(" join boleto.baixaCobranca baixaCobranca ");
		hql.append(" where boleto.statusCobranca = :status ");
		hql.append(" and baixaCobranca.dataBaixa = :dataBaixa ");
		
		hql = this.obterPaginacaoEOrdenacao(filtro, hql);
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("status", StatusCobranca.PAGO);
		query.setParameter("dataBaixa", filtro.getData());
		
		if (filtro.getPaginacao() != null) {
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(Transformers.aliasToBean(DetalheBaixaBoletoDTO.class));

		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheBaixaBoletoDTO> obterTotalBancario(FiltroDetalheBaixaBoletoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select banco.nome as nomeBanco, ")
		   .append(" 		concat(banco.conta, '-', banco.dvConta) as numeroConta, ")
  		   .append(" 		sum(baixaCobranca.valorPago) as valorPago ")
  		   .append(this.obterFromWhereConsultaTotalBancario());

		if (filtro.getOrdenacaoColuna() != null && filtro.getPaginacao() != null) {

			hql.append(
				this.obterOrdenacaoConsultaBaixaBoletos(
					filtro.getOrdenacaoColuna(), filtro.getPaginacao().getSortOrder()));
		}
		
		Query query = this.obterQureryTotalBancario(filtro.getData(), hql);
		
		this.paginarConsultasTotalBancario(query, filtro);

		return query.list();
	}
	
}
