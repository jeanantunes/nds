package br.com.abril.nds.integracao.engine;

import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.integracao.engine.data.FileRouteTemplate;
import br.com.abril.nds.integracao.engine.data.FixedLengthField;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;
import br.com.abril.nds.integracao.engine.data.FixedLengthTypeMapping;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component

public class FixedLenghtContentBasedDataRouter extends FileContentBasedRouter {
	private final Logger logger = LoggerFactory.getLogger(FixedLenghtContentBasedDataRouter.class);
	
	@Autowired
	private FixedFormatManager fixedFormatManager;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
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
							
							getSession().flush();
							getSession().clear();
							
							return null;
						}
					});
			}
			else if (fileRouteTemplate.isBulkLoad()) {
				TransactionTemplate template = new TransactionTemplate(
						transactionManager);
		
					template.execute(new TransactionCallback<Void>() {
		
						@Override
						public Void doInTransaction(TransactionStatus status) {
							processFile(fileRouteTemplate, file);
							
							getSession().flush();
							getSession().clear();
							
							return null;
						}
					});
			}
			else {
				processFile(fileRouteTemplate, file);
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
	public void processFile(FileRouteTemplate fileRouteTemplate, File file) {
		try {
			
			final MessageProcessor messageProcessor = fileRouteTemplate.getMessageProcessor();
			
			// Processamento a ser executado ANTES do processamento principal:
			messageProcessor.preProcess();
			
			
			File processingFile = new File(normalizeFileName(file.getParent()), file.getName() + ".processing");
			
			// RENOMEIA O ARQUIVO PARA PROCESSANDO
			file.renameTo(processingFile);
			FileReader in = new FileReader(processingFile);
			
			int lineNumber = 0;
			Scanner scanner = new Scanner(in);
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

					if (messageProcessor != null) {
						if (!fileRouteTemplate.isCommitAtEnd()) {
							TransactionTemplate template = new TransactionTemplate(
									transactionManager);
	
							try {
								template.execute(new TransactionCallback<Void>() {
		
									@Override
									public Void doInTransaction(TransactionStatus status) {
										messageProcessor.processMessage(message);
										
										getSession().flush();
										getSession().clear();

										return null;
									}
								});
							}
							catch (Exception e) {
								logger.error(e.getMessage(), e);
								
								ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.ERRO_INFRA, e.getMessage());								
							}
						}
						// BULK LOAD DO ARQUIVO (TABELA TEMPORARIA)
						else {
							messageProcessor.processMessage(message);
							
							// BATCH DE 100 LINHAS
							if ((lineNumber % 100) == 0) {
								getSession().flush();
								getSession().clear();
							}
						}
					}
				}
			}
			
			// FECHA O STREAM
			scanner.close();
			
			// MARCA COMO PROCESSADO (MOVE PARA A PASTA archive)
			File archiveFile = new File(normalizeFileName(fileRouteTemplate.getArchiveFolder()), file.getName());
			
			// SE EXISTE O ARQUIVO APAGA
			if (archiveFile.exists()) {
				archiveFile.delete();
			}
			
			FileUtils.moveFile(processingFile, archiveFile);
			
			final Message posMessage = new Message();
			posMessage.getHeader().putAll(fileRouteTemplate.getParameters());
			posMessage.getHeader().put(MessageHeaderProperties.URI.getValue(), fileRouteTemplate.getUri());
			posMessage.getHeader().put(MessageHeaderProperties.PAYLOAD.getValue(), null);
			posMessage.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), file.getName());
			posMessage.getHeader().put(MessageHeaderProperties.FILE_CREATION_DATE.getValue(), new Date(file.lastModified()));
			posMessage.getHeader().put(MessageHeaderProperties.LINE_NUMBER.getValue(), Long.valueOf(0));
			posMessage.getHeader().put(MessageHeaderProperties.USER_NAME.getValue(), fileRouteTemplate.getUserName());
			
			// Processamento a ser executado APÓS o processamento principal:
			messageProcessor.posProcess(posMessage);
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
