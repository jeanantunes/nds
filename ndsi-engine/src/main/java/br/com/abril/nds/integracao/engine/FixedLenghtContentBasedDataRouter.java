package br.com.abril.nds.integracao.engine;

import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.integracao.engine.data.FileRouteTemplate;
import br.com.abril.nds.integracao.engine.data.FixedLengthField;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;
import br.com.abril.nds.integracao.engine.data.FixedLengthTypeMapping;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.EventoExecucaoEnum;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;

@Component
public class FixedLenghtContentBasedDataRouter extends FileContentBasedRouter {
	private final FixedFormatManager fixedFormatManager = new FixedFormatManagerImpl();
	
	@Autowired
	private PlatformTransactionManager transactionManager;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	NdsiLoggerFactory ndsiLoggerFactory;

	@Override
	public void routeFile(final FileRouteTemplate fileRouteTemplate, final File file) {
		try {
			if (fileRouteTemplate.isCommitAtEnd()) {
				TransactionTemplate template = new TransactionTemplate(
						transactionManager);
		
					template.execute(new TransactionCallback<Void>() {
		
						@Override
						public Void doInTransaction(TransactionStatus status) {
							processFile(fileRouteTemplate, file);
							
							entityManager.flush();
							entityManager.clear();
							
							return null;
						}
					});
			}
			else {
				processFile(fileRouteTemplate, file);
			}
		}
		catch (Exception e) {
			// TODO: LOGAR Exception
			e.printStackTrace();
		}
		finally {
			// TODO: FINALLY
		}
	}
	
	public void processFile(FileRouteTemplate fileRouteTemplate, File file) {
		try {
			FileReader in = new FileReader(file);

			Scanner scanner = new Scanner(in);
			
			System.currentTimeMillis();
			
			int lineNumber = 0;
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lineNumber++;

				if (StringUtils.isEmpty(line)) {
					continue;
				}

				Class<?> targetClass = findType(line,
						(FixedLengthRouteTemplate) fileRouteTemplate);

				if (targetClass != null) {
					Object bean = fixedFormatManager.load(targetClass, line);

					final Message message = new Message();
					
					// FAZ MERGE COM OS PARAMETROS
					message.getHeader().putAll(fileRouteTemplate.getParameters());
					
					// ADICIONA OUTRAS INFORMACOES NO HEADER (METADATA)
					message.getHeader().put(MessageHeaderProperties.URI.getValue(), fileRouteTemplate.getUri());
					message.getHeader().put(MessageHeaderProperties.PAYLOAD.getValue(), line);
					message.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), file.getName());
					message.getHeader().put(MessageHeaderProperties.FILE_CREATION_DATE.getValue(), new Date(file.lastModified()));
					message.getHeader().put(MessageHeaderProperties.LINE_NUMBER.getValue(), lineNumber);
					message.getHeader().put(MessageHeaderProperties.USER_NAME.getValue(), fileRouteTemplate.getUserName());
					
					message.setBody(bean);

					final MessageProcessor messageProcessor = fileRouteTemplate
							.getMessageProcessor();

					if (messageProcessor != null) {
						if (!fileRouteTemplate.isCommitAtEnd()) {
							TransactionTemplate template = new TransactionTemplate(
									transactionManager);
	
							try {
								template.execute(new TransactionCallback<Void>() {
		
									@Override
									public Void doInTransaction(TransactionStatus status) {
										messageProcessor.processMessage(message);
										
										entityManager.flush();
										entityManager.clear();

										return null;
									}
								});
							}
							catch (Exception e) {
								ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.ERRO_INFRA, e.getMessage());
								e.printStackTrace();
							}
						}
						else {
							messageProcessor.processMessage(message);
							
							// BATCH DE 100 LINHAS
							if ((lineNumber % 100) == 0) {
								entityManager.flush();
								entityManager.clear();
							}
						}
					}
				}
			}
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static Class<?> findType(String line,
			FixedLengthRouteTemplate fixedLengthInputModel) {
		for (FixedLengthTypeMapping<?> mapping : fixedLengthInputModel
				.getTypeMappings()) {
			FixedLengthField field = mapping.getField();

			// PARA FICAR IGUAL AO FRAMEWORK Fixedformat4j, O OFFSET COMECA EM 1
			int offset = field.getOffset() - 1;

			if (field != null
					&& field.getValue().equals(
							line.substring(offset, offset + field.getLength()))) {
				return mapping.getTargetClass();
			}
		}

		return fixedLengthInputModel.getTypeMapping().getTargetClass();
	}
}
