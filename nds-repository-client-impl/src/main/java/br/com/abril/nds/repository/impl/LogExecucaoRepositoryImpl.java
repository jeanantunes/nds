package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaInterfacesDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheProcessamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroInterfacesDTO;
import br.com.abril.nds.model.integracao.InterfaceExecucao;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.LogExecucaoRepository;

@Repository
public class LogExecucaoRepositoryImpl extends AbstractRepositoryModel<LogExecucao, Long>  implements LogExecucaoRepository {

	/**
	 * Construtor padrão.
	 */
	public LogExecucaoRepositoryImpl() {
		super(LogExecucao.class);
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public List<ConsultaInterfacesDTO> obterInterfaces(FiltroInterfacesDTO filtro) {
		
		String sql = getConsultaObterInterfaces(new Date(), filtro, false);
		
		Query query = (Query) getSession().createSQLQuery(sql.toString())
				.addScalar("status", StandardBasicTypes.STRING)
				.addScalar("nome", StandardBasicTypes.STRING)
				.addScalar("id", StandardBasicTypes.LONG)
				.addScalar("idLogExecucao", StandardBasicTypes.LONG)
				.addScalar("descricao", StandardBasicTypes.STRING)
				.addScalar("nomeArquivo", StandardBasicTypes.STRING)
				.addScalar("extensaoArquivo", StandardBasicTypes.STRING)
				.addScalar("dataInicio", StandardBasicTypes.TIMESTAMP)
				.addScalar("idInterface", StandardBasicTypes.LONG)
				.addScalar("tipoInterfaceExecucao", StandardBasicTypes.STRING)
				.setResultTransformer(Transformers.aliasToBean(ConsultaInterfacesDTO.class));

			query.setParameter("codigoDistribuidor", filtro.getCodigoDistribuidor());
		
		if(filtro.getPaginacao()!=null) {
			
			if(filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
			
		}

		return query.list();
	}
	
	

	@Override
	public BigInteger obterTotalInterfaces(FiltroInterfacesDTO filtro) {
		String sql = getConsultaObterInterfaces(new Date(), filtro, true);
		Query query = (Query) getSession().createSQLQuery(sql.toString());
		query.setParameter("codigoDistribuidor", filtro.getCodigoDistribuidor());
		return (BigInteger) query.uniqueResult();
	}
	
	private String getConsultaObterInterfaces(Date dataOperacao, FiltroInterfacesDTO filtro, boolean totalizar) {

		StringBuffer sql = new StringBuffer("");

		if (totalizar) {
			sql.append(" SELECT COUNT(*) FROM ("); 
		}
		
		sql.append("select le.id as idLogExecucao, ie.id, ie.nome, ie.extensao_arquivo ");
		sql.append("	, ie.descricao, ie.extensao_arquivo as extensaoArquivo, ie.tipo_interface_execucao as tipoInterfaceExecucao, ");
		sql.append("	case when (le.status = 'S' and lem.nome_arquivo is null and ie.extensao_arquivo <> 'BANCO') then 'V' "); 
		sql.append("	else coalesce(le.status, 'N') end as status ");
		sql.append("	, le.data_inicio as dataInicio, lem.nome_arquivo as nomeArquivo, ");
		sql.append(" ie.id as idInterface ");
		sql.append(" from interface_execucao ie ");
		sql.append(" left join ( ");
		sql.append(" 	select le2.*, max(le2.id) as maxId "); 
		sql.append(" 	from log_execucao le2 "); 
		sql.append(" 	where le2.COD_DISTRIBUIDOR= :codigoDistribuidor ");
		sql.append(" 	group by le2.INTERFACE_EXECUCAO_ID ");
		sql.append(" ) as t1 ");
		sql.append(" on ie.ID=t1.INTERFACE_EXECUCAO_ID ");
		sql.append(" left join log_execucao le on le.ID=t1.maxId ");
		sql.append(" left join log_execucao_mensagem lem on le.id = lem.log_execucao_id ");
		sql.append(" where descricao not like '%MDC%' ");
		sql.append(" group by ie.id ");

		if (filtro.getOrdenacaoColuna() != null) {

			sql.append(" ORDER BY date(dataInicio) desc ");
			
			String orderByColumn = "";
			
				switch (filtro.getOrdenacaoColuna()) {
				
					case DESCRICAO_INTERFACE:
						orderByColumn = ", ie.descricao ";
						break;
					case STATUS:
						orderByColumn = ", status ";
						break;
					default:
						break;
				}
			
			sql.append(orderByColumn);

			if (filtro.getPaginacao() != null && filtro.getPaginacao().getSortOrder() != null) {
				sql.append(filtro.getPaginacao().getSortOrder());
			}
				
		}
	
		if (totalizar) {
			sql.append(") as total");
		}
		
		return sql.toString();
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.LogExecucaoRepository#obterMensagensLogInterface(java.lang.Long, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@SuppressWarnings({"unchecked"})
	@Override
	public List<LogExecucaoMensagem> obterMensagensLogInterface(Long codigoLogExecucao) {

		Criteria criteria = addMensagensLogInterfaceRestrictions(codigoLogExecucao);
		
		return criteria.list();
	}

	/**
	 * Adiciona as restrições á busca de mensagens de log de interface
	 * @param codigoLogExecucao
	 * @return
	 */
	private Criteria addMensagensLogInterfaceRestrictions(Long codigoLogExecucao) {
		Criteria criteria = getSession().createCriteria(LogExecucaoMensagem.class);
		criteria.createCriteria("logExecucao", "logExecucao", Criteria.INNER_JOIN);
		criteria.createCriteria("logExecucao.interfaceExecucao", "interfaceExecucao", Criteria.INNER_JOIN);
		criteria.add(Restrictions.eq("interfaceExecucao.id", codigoLogExecucao));
		return criteria;
	}

	@Override
	public List<LogExecucaoMensagem> obterMensagensErroLogInterface(FiltroDetalheProcessamentoDTO filtro) {
		Criteria criteria = addMensagensLogInterfaceRestrictions(filtro.getCodigoLogExecucao());
		criteria.add(Restrictions.eq("logExecucao.id", filtro.getIdLogExecucao()));
	//	criteria.add(Restrictions.not(Restrictions.eq("logExecucao.status", StatusExecucaoEnum.SUCESSO)));//Ignorar casos de sucesso na apresentacao do detalhe

		boolean desc = true;
		if (filtro.getPaginacao() != null && filtro.getPaginacao().getSortOrder() != null) {
			desc = !filtro.getPaginacao().getSortOrder().equals("asc");
		}
		
		if (filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
				case MENSAGEM:
					if (desc)
						criteria.addOrder(Order.desc("mensagem"));
					else 
						criteria.addOrder(Order.asc("mensagem"));
					break;
				case NUMERO_LINHA:
					if (desc)
						criteria.addOrder(Order.desc("numeroLinha"));
					else
						criteria.addOrder(Order.asc("numeroLinha"));
					break;
				case TIPO_ERRO:
					if (desc)
						criteria.addOrder(Order.desc("logExecucao.status"));
					else
						criteria.addOrder(Order.asc("logExecucao.status"));
					break;
					
				case DEFAULT:
					
					criteria.addOrder(Order.desc("nomeArquivo"));
					criteria.addOrder(Order.desc("numeroLinha"));
					criteria.addOrder(Order.desc("mensagem"));
					
					break;
			}
		}
		
		if(filtro.getPaginacao()!=null) {
			
			if(filtro.getPaginacao().getPosicaoInicial() != null) {
				criteria.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				criteria.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
			
		}		
		
		return criteria.list();
	}
	
	public LogExecucao inserir(LogExecucao logExecucao) {
		getSession().persist(logExecucao);
		getSession().flush();
		return logExecucao;
	}
	
	public void atualizar(LogExecucao logExecucao) {
		getSession().merge(logExecucao);
		getSession().flush();
	}
	
	private Date getPeriodoInicialDia(Date dataOperacao) {
		Calendar dataInicio = Calendar.getInstance();

		dataInicio.setTime(dataOperacao);
		dataInicio.set(Calendar.HOUR_OF_DAY, 0);
		dataInicio.set(Calendar.MINUTE, 0);
		dataInicio.set(Calendar.SECOND, 0);
		dataInicio.set(Calendar.MILLISECOND, 0);
		
		return dataInicio.getTime();
	}

	private Date getPeriodoFinalDia(Date dataOperacao) {
		Calendar dataFim = Calendar.getInstance();
		
		dataFim.setTime(dataOperacao);
		dataFim.set(Calendar.HOUR_OF_DAY, 23);
		dataFim.set(Calendar.MINUTE, 59);
		dataFim.set(Calendar.SECOND, 59);
		dataFim.set(Calendar.MILLISECOND, 999);
		
		return dataFim.getTime();
	}

	@Override
	public Long obterTotalMensagensErroLogInterface(FiltroDetalheProcessamentoDTO filtro) {
		Criteria criteria = addMensagensLogInterfaceRestrictions(filtro.getCodigoLogExecucao());
		criteria.add(Restrictions.eq("logExecucao.id", filtro.getIdLogExecucao()));
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	@Override
	public List<ConsultaInterfacesDTO> obterInterfacesExecucaoMicroDistribuicao() {
		Criteria criteria = getSession().createCriteria(LogExecucao.class);
		criteria.createAlias("interfaceExecucao", "ie");
		criteria.add(Restrictions.eq("ie.tipoInterfaceExecucao", "microDistribuicao"));
		ProjectionList projections = Projections.projectionList();
		projections = projections.add(Projections.max("dataInicio"), "dataInicio");
		projections = projections.add(Projections.max("id"), "idLogExecucao");
		projections = projections.add(Projections.property("ie.descricao"), "descricao");
		projections = projections.add(Projections.property("ie.extensaoArquivo"), "extensaoArquivo");
		projections = projections.add(Projections.property("ie.nome"), "nome");
		projections = projections.add(Projections.property("status"), "statusEnum");
		projections = projections.add(Projections.property("ie.tipoInterfaceExecucao"), "tipoInterfaceExecucao");
		projections = projections.add(Projections.groupProperty("ie.id"), "idInterface");
		criteria = criteria.setProjection(projections);
		criteria.setResultTransformer(Transformers.aliasToBean(ConsultaInterfacesDTO.class));

		return criteria.list();  
	}

	@Transactional
	public InterfaceExecucao findByID(Long id) {
		Criteria criteria = getSession().createCriteria(InterfaceExecucao.class);
		criteria.add(Restrictions.eq("id", id));
		return (InterfaceExecucao) criteria.uniqueResult();
	}
	
	@Transactional
	public InterfaceExecucao findByNome(String nome) {
		Criteria criteria = getSession().createCriteria(InterfaceExecucao.class);
		criteria.add(Restrictions.eq("nome", nome));
		return (InterfaceExecucao) criteria.uniqueResult();
	}

	@Transactional(readOnly = true)
	public Date buscarDataUltimaExecucao(String nome) {
		Criteria criteria = getSession().createCriteria(LogExecucao.class);
		criteria.setFetchMode("interfaceExecucao", FetchMode.JOIN);
		criteria.createAlias("interfaceExecucao", "ie");
		criteria.add(Restrictions.eq("ie.nome", nome));
		criteria.add(Restrictions.eq("status", StatusExecucaoEnum.SUCESSO));
		criteria.addOrder(Order.desc("dataInicio"));
		criteria.setMaxResults(1);

		LogExecucao logExecucao = (LogExecucao) criteria.uniqueResult();

		return logExecucao != null ? logExecucao.getDataInicio() : null;
	}
	
	

}
