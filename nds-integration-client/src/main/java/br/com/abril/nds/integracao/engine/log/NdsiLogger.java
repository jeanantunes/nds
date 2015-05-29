package br.com.abril.nds.integracao.engine.log;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.model.integracao.EventoExecucao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.InterfaceExecucao;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
import br.com.abril.nds.repository.AbstractRepository;

/**
 * Insere registros de log de execução da interface no banco de dados.
 */
@Component
@Scope("prototype")

public class NdsiLogger extends AbstractRepository {

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	private Logger LOGGER = LoggerFactory.getLogger(NdsiLogger.class);
	private StatusExecucaoEnum statusProcesso = StatusExecucaoEnum.SUCESSO;
	private LogExecucao logExecucao = null;
	
	    /**
     * Insere o log de início do processamento da interface
     * 
     * @param route rota sendo processada
     */
	public void logBeginning(RouteTemplate route) {
		
		if (logExecucao != null) {
            throw new IllegalStateException("logBeginning já executado para este batch");
		}
		
		InterfaceExecucao interfaceExecucao = new InterfaceExecucao();
		interfaceExecucao.setId(route.getRouteInterface().getId().longValue());
		
		logExecucao = new LogExecucao();
		
		logExecucao.setCodigoDistribuidor(route.getCodigoDistribuidor());
		logExecucao.setDataInicio(new Date());
		logExecucao.setInterfaceExecucao(interfaceExecucao);
		logExecucao.setNomeLoginUsuario(route.getUserName());
        logExecucao.setStatus(StatusExecucaoEnum.ERRO);// Inicia o processo como Não processado - No final atualiza status
		
		try {
			TransactionTemplate template = new TransactionTemplate(transactionManager, new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
			template.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
					getSession().persist(logExecucao);
					return null;
				}
			});
		} catch(Exception e) {
            LOGGER.warn("ATENÇÃO: Erro inserindo entrada de log na base; continuando sem log.", e);

		}
	}
	
	
	/**
	 * Insere o log de fim do processamento da interface
	 * @param route rota sendo processada
	 */
	public void logEnd(RouteTemplate route) {
		
		logExecucao.setStatus(this.statusProcesso);
		logExecucao.setDataFim(new Date());
		
		try {
			TransactionTemplate template = new TransactionTemplate(transactionManager, new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
			template.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
					getSession().merge(logExecucao);
					return null;
				}
			});
		} catch(Exception e) {
            LOGGER.warn("ATENÇÃO: Erro atualizando entrada de log na base; continuando sem log.", e);

		}
	}
	
	
	public void logError(Message message, EventoExecucaoEnum eventoExecucaoEnum, String descricaoErro) {
		
		this.statusProcesso = StatusExecucaoEnum.FALHA;
		message.getHeader().put(descricaoErro, MessageHeaderProperties.ERRO_PROCESSAMENTO.getValue());
		this.logMessage(message, eventoExecucaoEnum, descricaoErro, null);
	}
	
	
	public void logWarning(Message message, EventoExecucaoEnum eventoExecucaoEnum, String descricaoErro) {

		if(this.statusProcesso != StatusExecucaoEnum.FALHA)//Nao deve sobrescrever fatal
			this.statusProcesso = StatusExecucaoEnum.AVISO;
		
		this.logMessage(message, eventoExecucaoEnum, descricaoErro, null);
	}
	
	
	public void logInfo(Message message, EventoExecucaoEnum eventoExecucaoEnum, String descricaoErro) {
		
		this.logMessage(message, eventoExecucaoEnum, descricaoErro, null);
	}
	
	
	public void logInfo(Message message, EventoExecucaoEnum eventoExecucaoEnum, String descricaoErro, String mensagemInfo) {
		
		this.logMessage(message, eventoExecucaoEnum, descricaoErro, mensagemInfo);
	}
	
	
	    /**
     * Faz a inserção da mensagem de log
     * 
     * @param message
     * @param eventoExecucaoEnum
     * @param descricaoErro
     */
	
	private void logMessage(Message message, EventoExecucaoEnum eventoExecucaoEnum, String descricaoErro, String mensagemInfo) {
		
		// TODO: criar enum
		EventoExecucao eventoExecucao = new EventoExecucao();
		eventoExecucao.setId(eventoExecucaoEnum.getCodigo());
		
		logExecucao.setStatus(this.statusProcesso);
		
		final LogExecucaoMensagem logExecucaoMensagem = new LogExecucaoMensagem();
		
		logExecucaoMensagem.setLogExecucao(this.logExecucao);
		logExecucaoMensagem.setEventoExecucao(eventoExecucao);
		
		String descricaoErroTamanhoLimitado = descricaoErro;
		if(descricaoErro != null && descricaoErro.length() > 499){
			descricaoErroTamanhoLimitado = descricaoErro.substring(0, 499);
		}
		
		logExecucaoMensagem.setMensagem(descricaoErroTamanhoLimitado);
		logExecucaoMensagem.setMensagemInfo(mensagemInfo);

		if (message != null && message.getHeader() != null) {
			if (message.getHeader().containsKey(MessageHeaderProperties.FILE_NAME.getValue()))
				logExecucaoMensagem.setNomeArquivo((String) message.getHeader().get(MessageHeaderProperties.FILE_NAME.getValue()));
			
			
			if (message.getHeader().containsKey(MessageHeaderProperties.LINE_NUMBER.getValue()))
			    logExecucaoMensagem.setNumeroLinha((Integer) message.getHeader().get(MessageHeaderProperties.LINE_NUMBER.getValue()));
			
		} else {
		   logExecucaoMensagem.setNomeArquivo("");
           logExecucaoMensagem.setNumeroLinha(0);
		}

		try {
			TransactionTemplate template = new TransactionTemplate(transactionManager, new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
			template.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
					getSession().persist(logExecucaoMensagem);
					return null;
				}
			});
		} catch(Exception e) {
            LOGGER.warn("ATENÇÃO: Erro inserindo mensagem de log na base; continuando sem log.", e);
		}
	}
	
	public void setStatusProcesso(StatusExecucaoEnum status){
		this.statusProcesso = status;
	}
	
	
	
    


}