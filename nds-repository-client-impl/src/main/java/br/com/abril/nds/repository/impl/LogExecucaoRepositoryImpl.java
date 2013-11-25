<<<<<<< HEAD
package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaInterfacesDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheProcessamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroInterfacesDTO;
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

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.LogExecucaoRepository#obterResultadoPaginado(java.lang.String, java.lang.String, int, int)
	 */
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
				.setResultTransformer(Transformers.aliasToBean(ConsultaInterfacesDTO.class));

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
		return (BigInteger) query.uniqueResult();
	}
	
	private String getConsultaObterInterfaces(Date dataOperacao, FiltroInterfacesDTO filtro, boolean totalizar) {

		StringBuffer sql = new StringBuffer("");

		if (totalizar) {
			sql.append(" SELECT COUNT(*) FROM ("); 
		}
		
		sql.append("select le.id as idLogExecucao, ie.id, ie.nome, ie.extensao_arquivo ");
		sql.append("	, ie.descricao, ie.extensao_arquivo as extensaoArquivo, ");
		sql.append("	case when (le.status = 'S' and lem.nome_arquivo is null and ie.extensao_arquivo <> 'BANCO') then 'V' "); 
		sql.append("	else coalesce(le.status, 'N') end as status ");
		sql.append("	, le.data_inicio as dataInicio, lem.nome_arquivo as nomeArquivo, ");
		sql.append("case when descricao like 'Prodin > NDS%' then 0 ");
		sql.append("when descricao like 'MDC > NDS%' then 1 ");
		sql.append("when descricao like 'NDS > MDC%' then 2 ");
		sql.append("else 3 end as ordenacao, ");
		sql.append(" ie.id as idInterface ");
		sql.append(" from interface_execucao ie ");
		sql.append(" left join log_execucao le on le.id = (select MAX(lei.id) as id from log_execucao lei where lei.interface_execucao_id = ie.id) ");
		sql.append(" left join log_execucao_mensagem lem on le.id = lem.log_execucao_id ");
		sql.append(" group by ie.id ");

		if (filtro.getOrdenacaoColuna() != null) {

			sql.append(" ORDER BY ");
			
			String orderByColumn = "";
			
				switch (filtro.getOrdenacaoColuna()) {
				
					case DESCRICAO_INTERFACE:
						orderByColumn = "ordenacao, ie.descricao ";
						break;
					case STATUS:
						orderByColumn = "ordenacao, status ";
						break;
					case DATA_PROCESSAMENTO:
						orderByColumn = "ordenacao, dataInicio ";
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
		criteria.add(Restrictions.not(Restrictions.eq("logExecucao.status", StatusExecucaoEnum.SUCESSO)));//Ignorar casos de sucesso na apresentacao do detalhe

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
		/*dataInicio.add(Calendar.DAY_OF_MONTH, -1);
		dataInicio.set(Calendar.HOUR_OF_DAY, 23);
		dataInicio.set(Calendar.MINUTE, 59);
		dataInicio.set(Calendar.SECOND, 59);
		dataInicio.set(Calendar.MILLISECOND, 999);*/
		dataInicio.set(Calendar.HOUR_OF_DAY, 0);
		dataInicio.set(Calendar.MINUTE, 0);
		dataInicio.set(Calendar.SECOND, 0);
		dataInicio.set(Calendar.MILLISECOND, 0);
		
		return dataInicio.getTime();
	}

	private Date getPeriodoFinalDia(Date dataOperacao) {
		Calendar dataFim = Calendar.getInstance();
		
		dataFim.setTime(dataOperacao);
		/*dataFim.add(Calendar.DAY_OF_MONTH, 1);
		dataFim.set(Calendar.HOUR_OF_DAY, 0);
		dataFim.set(Calendar.MINUTE, 0);
		dataFim.set(Calendar.SECOND, 0);
		dataFim.set(Calendar.MILLISECOND, 0);*/
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

}
=======
package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaInterfacesDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheProcessamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroInterfacesDTO;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.LogExecucaoRepository;

@Repository
public class LogExecucaoRepositoryImpl extends AbstractRepositoryModel<LogExecucao, Long>  implements LogExecucaoRepository {

	@Autowired
	DistribuidorRepository distribuidorRepository;
	
	/**
	 * Construtor padrão.
	 */
	public LogExecucaoRepositoryImpl() {
		super(LogExecucao.class);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.LogExecucaoRepository#obterResultadoPaginado(java.lang.String, java.lang.String, int, int)
	 */
	@SuppressWarnings({"unchecked"})
	@Override
	public List<ConsultaInterfacesDTO> obterInterfaces(FiltroInterfacesDTO filtro) {
		
		String sql = getConsultaObterInterfaces(distribuidorRepository.obterDataOperacaoDistribuidor(), filtro, false);
		
		Query query = (Query) getSession().createSQLQuery(sql.toString())
				.addScalar("status", StandardBasicTypes.STRING)
				.addScalar("nome", StandardBasicTypes.STRING)
				.addScalar("id", StandardBasicTypes.LONG)
				.addScalar("nomeArquivo", StandardBasicTypes.STRING)
				.addScalar("dataInicio", StandardBasicTypes.TIMESTAMP)
				.setResultTransformer(Transformers.aliasToBean(ConsultaInterfacesDTO.class));

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
		String sql = getConsultaObterInterfaces(distribuidorRepository.obterDataOperacaoDistribuidor(), filtro, true);
		Query query = (Query) getSession().createSQLQuery(sql.toString());
		return (BigInteger) query.uniqueResult();
	}
	
	private String getConsultaObterInterfaces(Date dataOperacao, FiltroInterfacesDTO filtro, boolean totalizar) {

		StringBuffer sql = new StringBuffer("");

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		
		if (totalizar) {

			sql.append(" SELECT COUNT(*) FROM ("); 

		}
		
		sql.append(" SELECT "); 
		sql.append(" LOG_EXECUCAO.STATUS AS status, ");
		sql.append(" INTERFACE_EXECUCAO.NOME AS nome, ");
		sql.append(" INTERFACE_EXECUCAO.ID AS id, ");
		sql.append(" MAX(LOG_EXECUCAO_MENSAGEM.NOME_ARQUIVO) AS nomeArquivo, ");
		sql.append(" MAX(LOG_EXECUCAO.DATA_INICIO) AS dataInicio ");

		sql.append(" FROM ");
		sql.append(" LOG_EXECUCAO_MENSAGEM ");
		sql.append(" INNER JOIN LOG_EXECUCAO ON (LOG_EXECUCAO_MENSAGEM.LOG_EXECUCAO_ID=LOG_EXECUCAO.ID) ");
		sql.append(" INNER JOIN INTERFACE_EXECUCAO ON (LOG_EXECUCAO.INTERFACE_EXECUCAO_ID = INTERFACE_EXECUCAO.ID) ");

		sql.append(" WHERE ");
		sql.append(" LOG_EXECUCAO.DATA_INICIO BETWEEN '" + sdf.format(this.getPeriodoInicialDia(dataOperacao)) + " 00:00:00' AND '" + sdf.format(this.getPeriodoFinalDia(dataOperacao)) + " 23:59:59'");

		sql.append(" GROUP BY ");
		sql.append(" LOG_EXECUCAO.STATUS, ");
		sql.append(" INTERFACE_EXECUCAO.NOME, ");
		sql.append(" INTERFACE_EXECUCAO.ID ");
		//sql.append(" DATE_FORMAT(LOG_EXECUCAO.DATA_INICIO, '%Y-%m-%d') ");

		if (filtro.getOrdenacaoColuna() != null) {

			sql.append(" ORDER BY ");
			
			String orderByColumn = "";
			
				switch (filtro.getOrdenacaoColuna()) {
				
					case NOME:
						orderByColumn = " INTERFACE_EXECUCAO.NOME ";
						break;
					case STATUS:
						orderByColumn = " LOG_EXECUCAO.STATUS ";
						break;
					case DATA_PROCESSAMENTO:
						orderByColumn = " LOG_EXECUCAO.DATA_INICIO ";
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
	public List<LogExecucaoMensagem> obterMensagensErroLogInterface(Long codigoLogExecucao, Date dataOperacao, FiltroDetalheProcessamentoDTO filtro) {
		Criteria criteria = addMensagensLogInterfaceRestrictions(codigoLogExecucao);
		criteria.add( Restrictions.between("logExecucao.dataInicio", this.getPeriodoInicialDia(dataOperacao), this.getPeriodoFinalDia(dataOperacao)) );

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
		/*dataInicio.add(Calendar.DAY_OF_MONTH, -1);
		dataInicio.set(Calendar.HOUR_OF_DAY, 23);
		dataInicio.set(Calendar.MINUTE, 59);
		dataInicio.set(Calendar.SECOND, 59);
		dataInicio.set(Calendar.MILLISECOND, 999);*/
		dataInicio.set(Calendar.HOUR_OF_DAY, 0);
		dataInicio.set(Calendar.MINUTE, 0);
		dataInicio.set(Calendar.SECOND, 0);
		dataInicio.set(Calendar.MILLISECOND, 0);
		
		return dataInicio.getTime();
	}

	private Date getPeriodoFinalDia(Date dataOperacao) {
		Calendar dataFim = Calendar.getInstance();
		
		dataFim.setTime(dataOperacao);
		/*dataFim.add(Calendar.DAY_OF_MONTH, 1);
		dataFim.set(Calendar.HOUR_OF_DAY, 0);
		dataFim.set(Calendar.MINUTE, 0);
		dataFim.set(Calendar.SECOND, 0);
		dataFim.set(Calendar.MILLISECOND, 0);*/
		dataFim.set(Calendar.HOUR_OF_DAY, 23);
		dataFim.set(Calendar.MINUTE, 59);
		dataFim.set(Calendar.SECOND, 59);
		dataFim.set(Calendar.MILLISECOND, 999);
		
		return dataFim.getTime();
	}

	@Override
	public Long obterTotalMensagensErroLogInterface(long codigoLogExecucao, Date dataOperacao, FiltroDetalheProcessamentoDTO filtro) {
		Criteria criteria = addMensagensLogInterfaceRestrictions(codigoLogExecucao);
		criteria.add( Restrictions.between("logExecucao.dataInicio", this.getPeriodoInicialDia(dataOperacao), this.getPeriodoFinalDia(dataOperacao)) );
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

}
>>>>>>> fase2
