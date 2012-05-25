package br.com.abril.nds.integracao.fileimporter;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang.StringUtils;
import org.lightcouch.CouchDbClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;
import br.com.abril.nds.integracao.persistence.dao.InterfaceExecucaoHibernateDAO;
import br.com.abril.nds.integracao.persistence.dao.LogExecucaoArquivoHibernateDAO;
import br.com.abril.nds.integracao.persistence.dao.LogExecucaoHibernateDAO;
import br.com.abril.nds.integracao.persistence.dao.ParametroSistemaHibernateDAO;
import br.com.abril.nds.integracao.persistence.model.InterfaceExecucao;
import br.com.abril.nds.integracao.persistence.model.LogExecucao;
import br.com.abril.nds.integracao.persistence.model.LogExecucaoArquivo;
import br.com.abril.nds.integracao.persistence.model.enums.StatusExecucaoEnum;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;

/**
 * Realiza a execução das interfaces de integração. <br>
 * Lê as linhas dos arquivos de entrada, transforma em documentos e grava no CouchDB. 
 */
public class InterfaceExecutor {
	
	private static ApplicationContext applicationContext;
	
	private static String NAO_HA_ARQUIVOS = "Não há arquivos a serem processados para este distribuidor";
//	private static String TAMANHO_LINHA = "Tamanho da linha é diferente do tamanho definido";
	
	private LogExecucaoHibernateDAO logExecucaoDAO;
	private LogExecucaoArquivoHibernateDAO logExecucaoArquivoAO;
	private ParametroSistemaHibernateDAO parametroSistemaDAO;
	private InterfaceExecucaoHibernateDAO interfaceExecucaoDAO;
	private FixedFormatManager ffm;
	private Properties couchDbProperties;
	
	private boolean processadoComSucesso = true;
	
	static {
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		classPathXmlApplicationContext.registerShutdownHook();
		applicationContext = classPathXmlApplicationContext;
	}
	
	public InterfaceExecutor() {
		
		this.logExecucaoDAO = (LogExecucaoHibernateDAO) applicationContext.getBean("logExecucaoDAO");
		this.logExecucaoArquivoAO = (LogExecucaoArquivoHibernateDAO) applicationContext.getBean("logExecucaoArquivoDAO");
		this.parametroSistemaDAO = (ParametroSistemaHibernateDAO) applicationContext.getBean("parametroSistemaDAO");
		this.interfaceExecucaoDAO = (InterfaceExecucaoHibernateDAO) applicationContext.getBean("interfaceExecucaoDAO");
		this.ffm = (FixedFormatManagerImpl) applicationContext.getBean("ffm");
	}
	
	/**
	 * Executa a interface selecionada para todos os distribuidores.
	 * 
	 * @param nomeUsuario login do usuário, para efeitos de log
	 * @param interfaceEnum interface a ser executada
	 */
	public void executarInterface(String nomeUsuario, InterfaceEnum interfaceEnum) {
		this.executarInterface(nomeUsuario, interfaceEnum, null);
	}
	
	/**
	 * Executa a interface selecionada para o distribuidor selecionado.
	 * 
	 * @param nomeUsuario login do usuário, para efeitos de log
	 * @param interfaceEnum interface a ser executada
	 * @param codigoDistribuidor código do distribuidor
	 */
	public void executarInterface(String nomeUsuario, InterfaceEnum interfaceEnum, String codigoDistribuidor) {
		
		// Busca dados de configuracao
		this.carregaCouchDbProperties();
		InterfaceExecucao interfaceExecucao = interfaceExecucaoDAO.findById(interfaceEnum.getCodigoInterface());
		
		// Loga início
		Date dataInicio = new Date();
		LogExecucao logExecucao = this.logarInicio(dataInicio, interfaceExecucao, nomeUsuario);
		
		// Executa interface
		if (interfaceEnum.equals(InterfaceEnum.EMS0134)) {
			this.executarInterfaceImagem();
		} else {
			this.executarInterfaceArquivo(interfaceEnum, interfaceExecucao, logExecucao, codigoDistribuidor, nomeUsuario);
		}
		
		// Loga fim
		this.logarFim(logExecucao);
	}
	
	/**
	 * Executa uma interface de carga de arquivo.
	 */
	private void executarInterfaceArquivo(InterfaceEnum interfaceEnum, InterfaceExecucao interfaceExecucao, LogExecucao logExecucao, String codigoDistribuidor, String nomeUsuario) {
		
		// Recupera distribuidores
		String diretorio = parametroSistemaDAO.getParametro("INBOUND_DIR");
		List<String> distribuidores = this.getDistribuidores(diretorio, interfaceExecucao, codigoDistribuidor);
		
		// Processa arquivos do distribuidor
		for (String distribuidor: distribuidores) {
		
			CouchDbClient couchDbClient = this.getCouchDbClientInstance("db_" + StringUtils.leftPad(distribuidor, 7, "0"));
			List<File> arquivos = this.recuperaArquivosProcessar(diretorio, interfaceExecucao, distribuidor);
			
			if (arquivos == null || arquivos.isEmpty()) {
				this.logarArquivo(logExecucao, distribuidor, null, StatusExecucaoEnum.FALHA, NAO_HA_ARQUIVOS);
				continue;
			}
			
			for (File arquivo: arquivos) {
				
				try {
					
					this.trataArquivo(couchDbClient, arquivo, interfaceEnum, logExecucao.getDataInicio(), nomeUsuario);
					this.logarArquivo(logExecucao, distribuidor, arquivo.getAbsolutePath(), StatusExecucaoEnum.SUCESSO, null);
					
				} catch (Throwable e) {
					
					this.logarArquivo(logExecucao, distribuidor, arquivo.getAbsolutePath(), StatusExecucaoEnum.FALHA, e.getMessage());
					e.printStackTrace();
					continue;
					
				} finally {
					
					// TODO: arquivar arquivo
				}
			}
			
			couchDbClient.shutdown();
		}
	}
	
	/**
	 * Executa a interface de carga de imagens EMS0134.
	 */
	private void executarInterfaceImagem() {
		/*
		String diretorio = parametroSistemaDAO.getParametro("IMAGE_DIR");
		CouchDbClient couchDbClient = this.getCouchDbClientInstance("");
		
		File[] imagens = new File(diretorio).listFiles();
		
		for (File imagem: imagens) {
			
		}
		
		couchDbClient.shutdown();*/
	}
	
	/**
	 * Recupera distribuidores a serem processados.
	 */
	private List<String> getDistribuidores(String diretorio, InterfaceExecucao interfaceExecucao, String codigoDistribuidor) {
		
		List<String> distribuidores = new ArrayList<String>();
		
		if (codigoDistribuidor == null) {
			
			File dirDistribs = new File(diretorio);
			distribuidores.addAll(Arrays.asList(dirDistribs.list()));
			
		} else {
			
			distribuidores.add(codigoDistribuidor);
		}
		
		return distribuidores;
	}
	
	/**
	 * Processa o arquivo, lendo suas linhas e gravando no CouchDB.
	 */
	private void trataArquivo(CouchDbClient couchDbClient, File arquivo, InterfaceEnum interfaceEnum, Date dataInicio, String nomeUsuario) throws Exception {

		FileReader in = new FileReader(arquivo);
		Scanner scanner = new Scanner(in);
		int linhaArquivo = 0;

		while (scanner.hasNextLine()) {

			String linha = scanner.nextLine();
			linhaArquivo++;

			if (StringUtils.isEmpty(linha)) {
				continue;
			} 

			// TODO: verificar tamanho correto das linhas nos arquivos: difere da definição
//			if (linha.length() != interfaceEnum.getTamanhoLinha().intValue()) {
//				throw new ValidacaoException(TAMANHO_LINHA);
//			}
			
			IntegracaoDocument doc = (IntegracaoDocument) this.ffm.load(interfaceEnum.getClasseLinha(), linha);
			
			doc.setTipoDocumento(interfaceEnum.name());
			doc.setNomeArquivo(arquivo.getName());
			doc.setLinhaArquivo(linhaArquivo);
			doc.setDataHoraExtracao(dataInicio);
			doc.setNomeUsuarioExtracao(nomeUsuario);

			couchDbClient.save(doc);
		}
	}

	/**
	 * Recupera a lista de arquivos a serem processados.
	 * 
	 * @param interfaceExecucao interface sendo executada
	 * @param codigoDistribuidor código do distribuidor
	 * @return lista de arquivos a serem processados
	 */
	private List<File> recuperaArquivosProcessar(String diretorio, InterfaceExecucao interfaceExecucao, String codigoDistribuidor) {
		
		List<File> listaArquivos = new ArrayList<File>();
		
		File dir = new File(diretorio + "\\" + codigoDistribuidor + "\\");
		File[] files = dir.listFiles((FilenameFilter) new RegexFileFilter(interfaceExecucao.getMascaraArquivo()));
		listaArquivos.addAll(Arrays.asList(files));
		
		return listaArquivos;
	}
	
	/**
	 * Carrega os dados do arquivo couchdb.properties
	 */
	private void carregaCouchDbProperties() {
		
		try {
			couchDbProperties = new Properties();
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("couchdb.properties");
			couchDbProperties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Retorna o client para o CouchDB na database correspondente ao distribuidor.
	 * 
	 * @param codigoDistribuidor codigo do distribuidor
	 * @return client
	 */
	private CouchDbClient getCouchDbClientInstance(String databaseName) {
		
		return new CouchDbClient(
				databaseName,
				true,
				this.couchDbProperties.getProperty("couchdb.protocol"),
				this.couchDbProperties.getProperty("couchdb.host"),
				Integer.valueOf(this.couchDbProperties.getProperty("couchdb.port")),
				this.couchDbProperties.getProperty("couchdb.username"),
				this.couchDbProperties.getProperty("couchdb.password")
		);
	}
	
	/**
	 * Loga o início da execução de uma interface de integração.
	 */
	private LogExecucao logarInicio(Date dataInicio, InterfaceExecucao interfaceExecucao, String nomeLoginUsuario) {
		
		LogExecucao logExecucao = new LogExecucao();
		logExecucao.setDataInicio(dataInicio);
		logExecucao.setInterfaceExecucao(interfaceExecucao);
		logExecucao.setNomeLoginUsuario(nomeLoginUsuario);
		
		return logExecucaoDAO.inserir(logExecucao);
	}
	
	/**
	 * Loga o processamento de um arquivo
	 */
	private void logarArquivo(LogExecucao logExecucao, String distribuidor, String caminhoArquivo, StatusExecucaoEnum status, String mensagem) {
		
		if (status.equals(StatusExecucaoEnum.FALHA)) {
			this.processadoComSucesso = false;
		}
		
		LogExecucaoArquivo logExecucaoArquivo = new LogExecucaoArquivo();
		logExecucaoArquivo.setLogExecucao(logExecucao);
		logExecucaoArquivo.setCaminhoArquivo(caminhoArquivo);
		logExecucaoArquivo.setDistribuidor(Integer.valueOf(distribuidor));
		logExecucaoArquivo.setStatus(status);
		logExecucaoArquivo.setMensagem(StringUtils.abbreviate(mensagem, 500));
		
		this.logExecucaoArquivoAO.inserir(logExecucaoArquivo);
	}
	
	/**
	 * Loga o final da execução da interface de integração.
	 */
	private void logarFim(LogExecucao logExecucao) {
		
		if (this.processadoComSucesso) {
			logExecucao.setStatus(StatusExecucaoEnum.SUCESSO);
		} else {
			logExecucao.setStatus(StatusExecucaoEnum.FALHA);
		}
		logExecucao.setDataFim(new Date());
		
		logExecucaoDAO.atualizar(logExecucao);
	}
}
