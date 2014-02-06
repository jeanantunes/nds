package br.com.abril.nds.integracao.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.data.FileRouteTemplate;
import br.com.abril.nds.integracao.engine.data.FixedLengthField;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;
import br.com.abril.nds.integracao.engine.data.FixedLengthTypeMapping;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.ParseException;

@Component("fixedLenghtContentBasedDataRouter")
public class FixedLenghtContentBasedDataRouter extends FileContentBasedRouter {
	
	private final Logger logger = Logger.getLogger(FixedLenghtContentBasedDataRouter.class);
	
	private static final int START_TIME_POSITION_COLON = 100;
	
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
			AtomicReference<Object> tempVar = null;
			// Processamento a ser executado ANTES do processamento principal:
			messageProcessor.preProcess(tempVar);
					
			File processingFile = new File(normalizeFileName(file.getParent()), file.getName() + ".processing");

			// RENOMEIA O ARQUIVO PARA PROCESSANDO
			Files.copy(file.toPath(), processingFile.toPath(), StandardCopyOption.REPLACE_EXISTING );
			Files.delete(file.toPath());
			
			FileReader in = new FileReader(processingFile);
			
			int lineNumber = 0;
			Scanner scanner = new Scanner(in);
			while (scanner.hasNextLine()) {
				final Message message = new Message();
				
				try {
					// ADICIONA OUTRAS INFORMACOES NO HEADER (METADATA)
					message.getHeader().put(MessageHeaderProperties.URI.getValue(), fileRouteTemplate.getUri());
					message.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), file.getName());
					message.getHeader().put(MessageHeaderProperties.FILE_CREATION_DATE.getValue(), new Date(file.lastModified()));
					message.getHeader().put(MessageHeaderProperties.LINE_NUMBER.getValue(), lineNumber);
					message.getHeader().put(MessageHeaderProperties.USER_NAME.getValue(), fileRouteTemplate.getUserName());
					message.setTempVar(tempVar);
					
					String line = scanner.nextLine();
					lineNumber++;
	
					if (StringUtils.isEmpty(line)) {
						continue;
					}
	
					Class<?> targetClass = findType(line, (FixedLengthRouteTemplate) fileRouteTemplate);
	
					if (targetClass != null) {
						Object bean = fixedFormatManager.load(targetClass, line);
	

						// FAZ MERGE COM OS PARAMETROS
						message.getHeader().putAll(fileRouteTemplate.getParameters());

						message.getHeader().put(MessageHeaderProperties.PAYLOAD.getValue(), line);
						message.setBody(bean);
	
						if (messageProcessor != null) {
							if (!fileRouteTemplate.isCommitAtEnd()) {
								TransactionTemplate template = new TransactionTemplate(transactionManager);
		
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
								}catch (Exception e) {
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
					
				}catch (Exception e) {
					logger.error(e.getMessage(), e);
					
					if(e instanceof ParseException){
						
						String nomeArquivo = (String) message.getHeader().get(MessageHeaderProperties.FILE_NAME.getValue());
						Integer numeroLinha = (Integer) message.getHeader().get(MessageHeaderProperties.LINE_NUMBER.getValue());
						
						ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.ERRO_INFRA, "Erro no layout do arquivo: "+  nomeArquivo + " linha: "+numeroLinha);								
					}else{
						
						ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.ERRO_INFRA, e.getMessage());								
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
			
			// Processamento a ser executado APÓS o processamento principal:
			messageProcessor.posProcess(tempVar);
			
		} catch (SecurityException e) {			
			throw new RuntimeException("Não Conseguiu renomear o Arquivo", e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}


	private synchronized void renameFile(File file, File processingFile) {
		if (!file.renameTo(processingFile)) {
			throw new RuntimeException("Não Conseguiu renomear o Arquivo");
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
