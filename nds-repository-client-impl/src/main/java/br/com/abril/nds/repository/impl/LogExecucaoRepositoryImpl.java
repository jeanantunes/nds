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
				.addScalar("descricao", StandardBasicTypes.STRING)
				.addScalar("nomeArquivo", StandardBasicTypes.STRING)
				.addScalar("extensaoArquivo", StandardBasicTypes.STRING)
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
		/*
		sql.append(" SELECT ");
		sql.append(" LOG_EXECUCAO.STATUS AS status, ");
		sql.append(" INTERFACE_EXECUCAO.NOME AS nome, ");
		sql.append(" INTERFACE_EXECUCAO.ID AS id, ");
		sql.append(" INTERFACE_EXECUCAO.DESCRICAO AS descricao, ");
		sql.append(" INTERFACE_EXECUCAO.EXTENSAO_ARQUIVO AS extensaoArquivo, ");
		sql.append(" MAX(LOG_EXECUCAO_MENSAGEM.NOME_ARQUIVO) AS nomeArquivo, ");
		sql.append(" MAX(LOG_EXECUCAO.DATA_INICIO) AS dataInicio ");

		sql.append(" FROM ");
		sql.append(" LOG_EXECUCAO_MENSAGEM ");
		sql.append(" RIGHT OUTER JOIN LOG_EXECUCAO ON (LOG_EXECUCAO_MENSAGEM.LOG_EXECUCAO_ID=LOG_EXECUCAO.ID) ");
		sql.append(" RIGHT OUTER JOIN INTERFACE_EXECUCAO ON (LOG_EXECUCAO.INTERFACE_EXECUCAO_ID = INTERFACE_EXECUCAO.ID) ");
		*/
		
		sql.append(" select ie.id, ie.nome, ie.extensao_arquivo, ie.descricao, ie.extensao_arquivo as extensaoArquivo, le.status, le.data_inicio as dataInicio, rs1.nome_arquivo as nomeArquivo");
		sql.append(" from interface_execucao ie ");
		sql.append(" left join log_execucao le on ie.id = le.interface_execucao_id ");
		sql.append(" left join ( ");
		sql.append(" select id, log_execucao_id, MAX(NOME_ARQUIVO) as NOME_ARQUIVO ");
		sql.append(" from log_execucao_mensagem lem ");
		sql.append(" group by log_execucao_id) rs1 on le.id = rs1.log_execucao_id ");
		sql.append(" where le.data_inicio between '" + sdf.format(this.getPeriodoInicialDia(dataOperacao)) + " 00:00:00' and '" + sdf.format(this.getPeriodoFinalDia(dataOperacao)) + " 23:59:59'");
		sql.append(" group by interface_execucao_id ");
		/*
		sql.append(" WHERE ");
		sql.append(" LOG_EXECUCAO.DATA_INICIO BETWEEN '" + sdf.format(this.getPeriodoInicialDia(dataOperacao)) + " 00:00:00' AND '" + sdf.format(this.getPeriodoFinalDia(dataOperacao)) + " 23:59:59'");

		sql.append(" GROUP BY ");
		sql.append(" LOG_EXECUCAO.STATUS, ");
		sql.append(" INTERFACE_EXECUCAO.NOME, ");
		sql.append(" INTERFACE_EXECUCAO.ID ");
		*/
		//sql.append(" DATE_FORMAT(LOG_EXECUCAO.DATA_INICIO, '%Y-%m-%d') ");
/*
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
		*/
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
