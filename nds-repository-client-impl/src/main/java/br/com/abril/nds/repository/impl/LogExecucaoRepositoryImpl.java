package br.com.abril.nds.repository.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaInterfacesDTO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
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
	public List<ConsultaInterfacesDTO> obterInterfaces() {
		
		String sql = getConsultaObterInterfaces(distribuidorRepository.obterDataOperacaoDistribuidor());
		
		Query query = (Query) getSession().createSQLQuery(sql.toString())
				.addScalar("status", StandardBasicTypes.STRING)
				.addScalar("nome", StandardBasicTypes.STRING)
				.addScalar("id", StandardBasicTypes.BIG_DECIMAL)
				.addScalar("nomeArquivo", StandardBasicTypes.STRING)
				.addScalar("dataInicio", StandardBasicTypes.DATE)
				.setResultTransformer(Transformers.aliasToBean(ConsultaInterfacesDTO.class));
		
		/*Distribuidor distribuidor = distribuidorRepository.obter();
		
		Criteria criteria =  getSession().createCriteria(LogExecucaoMensagem.class, "logExecucaoMensagem");	
		
		criteria.createCriteria("logExecucao", "logExecucao", Criteria.LEFT_JOIN);
		criteria.createCriteria("logExecucao.interfaceExecucao", "interfaceExecucao", Criteria.LEFT_JOIN);
		
		criteria.setProjection( Projections.projectionList()
			    .add(Projections.groupProperty("logExecucao.status"), "status")  
			    .add(Projections.groupProperty("interfaceExecucao.nome"), "nome")  
			    .add(Projections.groupProperty("interfaceExecucao.id"), "id")  
			    .add(Projections.groupProperty("logExecucao.dataInicio"), "dataInicio")
			    .add(Projections.max("logExecucaoMensagem.nomeArquivo"), "nomeArquivo"))
			    .setResultTransformer(Transformers.aliasToBean(ConsultaInterfacesDTO.class));*/ 

		//this.setPeriodoDia(distribuidor.getDataOperacao(), criteria);
		
		return query.list();
	}

	private String getConsultaObterInterfaces(Date dataOperacao) {

		StringBuffer sql = new StringBuffer("");

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
		
		sql.append(" SELECT "); 
		sql.append(" LOG_EXECUCAO.STATUS AS status, ");
		sql.append(" INTERFACE_EXECUCAO.NOME AS nome, ");
		sql.append(" INTERFACE_EXECUCAO.ID AS id, ");
		sql.append(" MAX(LOG_EXECUCAO_MENSAGEM.NOME_ARQUIVO) AS nomeArquivo, ");
		sql.append(" DATE_FORMAT(LOG_EXECUCAO.DATA_INICIO, '%Y-%m-%d') AS dataInicio ");

		sql.append(" FROM ");
		sql.append(" LOG_EXECUCAO_MENSAGEM ");
		sql.append(" INNER JOIN LOG_EXECUCAO ON (LOG_EXECUCAO_MENSAGEM.LOG_EXECUCAO_ID=LOG_EXECUCAO.ID) ");
		sql.append(" INNER JOIN INTERFACE_EXECUCAO ON (LOG_EXECUCAO.INTERFACE_EXECUCAO_ID = INTERFACE_EXECUCAO.ID) ");

		sql.append(" WHERE ");
		sql.append(" LOG_EXECUCAO.DATA_INICIO BETWEEN " + sdf.format(this.getPeriodoInicialDia(dataOperacao)) + " AND " + sdf.format(this.getPeriodoFinalDia(dataOperacao)) + ") ");

		sql.append(" GROUP BY ");
		sql.append(" LOG_EXECUCAO.STATUS, ");
		sql.append(" INTERFACE_EXECUCAO.NOME, ");
		sql.append(" INTERFACE_EXECUCAO.ID, ");
		sql.append(" MAX(LOG_EXECUCAO_MENSAGEM.NOME_ARQUIVO), ");
		sql.append(" DATE_FORMAT(LOG_EXECUCAO.DATA_INICIO, '%Y-%m-%d') ");

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
	public List<LogExecucaoMensagem> obterMensagensErroLogInterface(Long codigoLogExecucao, Date dataOperacao) {
		Criteria criteria = addMensagensLogInterfaceRestrictions(codigoLogExecucao);
		criteria.add( Restrictions.between("logExecucao.dataInicio", this.getPeriodoInicialDia(dataOperacao), this.getPeriodoFinalDia(dataOperacao)) );
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
		dataInicio.add(Calendar.DAY_OF_MONTH, -1);
		dataInicio.set(Calendar.HOUR_OF_DAY, 23);
		dataInicio.set(Calendar.MINUTE, 59);
		dataInicio.set(Calendar.SECOND, 59);
		dataInicio.set(Calendar.MILLISECOND, 999);

		return dataInicio.getTime();
	}

	private Date getPeriodoFinalDia(Date dataOperacao) {
		Calendar dataFim = Calendar.getInstance();
		
		dataFim.setTime(dataOperacao);
		dataFim.add(Calendar.DAY_OF_MONTH, 1);
		dataFim.set(Calendar.HOUR_OF_DAY, 0);
		dataFim.set(Calendar.MINUTE, 0);
		dataFim.set(Calendar.SECOND, 0);
		dataFim.set(Calendar.MILLISECOND, 0);
		
		return dataFim.getTime();
	}

}
