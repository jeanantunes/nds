package br.com.abril.nds.integracao.engine.log;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.model.EventoExecucao;
import br.com.abril.nds.integracao.model.EventoExecucaoEnum;
import br.com.abril.nds.integracao.model.InterfaceExecucao;
import br.com.abril.nds.integracao.model.LogExecucao;
import br.com.abril.nds.integracao.model.LogExecucaoMensagem;
import br.com.abril.nds.integracao.model.StatusExecucaoEnum;

/**
 * Insere registros de log de execução da interface no banco de dados.
 */
@Component
@Scope("prototype")
public class NdsiLogger {

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private Logger LOGGER = Logger.getLogger(NdsiLogger.class);
	private boolean hasError = false;
	private boolean hasWarning = false;
	private LogExecucao logExecucao = null;
	private LogExecucaoMensagem logExecucaoMensagem = null;
	
	/**
	 * Insere o log de início do processamento da interface
	 * @param route rota sendo processada
	 */
	public void logBeginning(RouteTemplate route) {
		
		if (logExecucao != null) {
			throw new IllegalStateException("logBeginning já executado para este batch");
		}
		
		InterfaceExecucao interfaceExecucao = new InterfaceExecucao();
		interfaceExecucao.setId(route.getRouteInterface().getId().longValue()); 
		
		logExecucao = new LogExecucao();
		logExecucao.setDataInicio(new Date());
		logExecucao.setInterfaceExecucao(interfaceExecucao);
		logExecucao.setNomeLoginUsuario(route.getUserName());
		logExecucao.setStatus(StatusExecucaoEnum.SUCESSO);
		
		try {
			TransactionTemplate template = new TransactionTemplate(transactionManager, new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
			template.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
					entityManager.persist(logExecucao);
					return null;
				}
			});
		} catch(Exception e) {
			LOGGER.warn("ATENÇÃO: Erro inserindo entrada de log na base; continuando sem log.");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Insere o log de fim do processamento da interface
	 * @param route rota sendo processada
	 */
	public void logEnd(RouteTemplate route) {
		
		if (hasError) {
			logExecucao.setStatus(StatusExecucaoEnum.FALHA);
		} else if (hasWarning) {
			logExecucao.setStatus(StatusExecucaoEnum.AVISO);
		} else {
			logExecucao.setStatus(StatusExecucaoEnum.SUCESSO);
		}
		logExecucao.setDataFim(new Date());
		
		try {
			TransactionTemplate template = new TransactionTemplate(transactionManager, new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
			template.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
					entityManager.merge(logExecucao);
					return null;
				}
			});
		} catch(Exception e) {
			LOGGER.warn("ATENÇÃO: Erro atualizando entrada de log na base; continuando sem log.");
			e.printStackTrace();
		}
	}
	
	
	public void logError(Message message, EventoExecucaoEnum eventoExecucaoEnum, String descricaoErro) {
		
		hasError = true;
		this.logMessage(message, eventoExecucaoEnum, descricaoErro);
	}
	
	
	public void logWarning(Message message, EventoExecucaoEnum eventoExecucaoEnum, String descricaoErro) {
		
		hasWarning = true;
		this.logMessage(message, eventoExecucaoEnum, descricaoErro);
	}
	
	
	public void logInfo(Message message, EventoExecucaoEnum eventoExecucaoEnum, String descricaoErro) {
		
		this.logMessage(message, eventoExecucaoEnum, descricaoErro);
	}
	
	
	/**
	 * Faz a inserção da mensagem de log
	 * @param message
	 * @param eventoExecucaoEnum
	 * @param descricaoErro
	 */
	private void logMessage(Message message, EventoExecucaoEnum eventoExecucaoEnum, String descricaoErro) {
		
		hasError = true;
		
		// TODO: criar enum
		EventoExecucao eventoExecucao = new EventoExecucao();
		eventoExecucao.setId(eventoExecucaoEnum.getCodigo());
		
		logExecucaoMensagem = new LogExecucaoMensagem();
		logExecucaoMensagem.setLogExecucao(this.logExecucao);
		logExecucaoMensagem.setEventoExecucao(eventoExecucao);
		logExecucaoMensagem.setMensagem(descricaoErro);
		logExecucaoMensagem.setNomeArquivo((String) message.getHeader().get(MessageHeaderProperties.FILE_NAME.getValue()));
		logExecucaoMensagem.setNumeroLinha((Integer) message.getHeader().get(MessageHeaderProperties.LINE_NUMBER.getValue()));
		
		try {
			TransactionTemplate template = new TransactionTemplate(transactionManager, new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
			template.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
					entityManager.persist(logExecucaoMensagem);
					return null;
				}
			});
		} catch(Exception e) {
			LOGGER.warn("ATENÇÃO: Erro inserindo mensagem de log na base; continuando sem log.");
			e.printStackTrace();
		}
	}
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
