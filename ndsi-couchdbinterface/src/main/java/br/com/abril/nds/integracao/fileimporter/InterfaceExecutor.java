package br.com.abril.nds.integracao.fileimporter;

import java.io.File;
import java.io.FileInputStream;
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
import org.lightcouch.NoDocumentException;
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
	
	//private static Logger LOGGER = LoggerFactory.getLogger(InterfaceExecutor.class);
	
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
	public void executarInterface(String nomeUsuario, InterfaceEnum interfaceEnum, Long codigoDistribuidor) {
		
		// Busca dados de configuracao
		this.carregaCouchDbProperties();
		InterfaceExecucao interfaceExecucao = interfaceExecucaoDAO.findById(interfaceEnum.getCodigoInterface());
		
		if (interfaceExecucao == null) {
			throw new RuntimeException("Interface " + interfaceEnum.getCodigoInterface() + " nao cadastrada");
		}
		
		// Loga início
		Date dataInicio = new Date();
		LogExecucao logExecucao = this.logarInicio(dataInicio, interfaceExecucao, nomeUsuario);
		
		try {
			// Executa interface
			if (interfaceEnum.equals(InterfaceEnum.EMS0134)) {
				this.executarInterfaceImagem();
			} else if (interfaceEnum.equals(InterfaceEnum.EMS0185)) {
				this.executarInterfaceCorreios();
			} else {
				this.executarInterfaceArquivo(interfaceEnum, interfaceExecucao, logExecucao, codigoDistribuidor, nomeUsuario);
			}
		} catch (Throwable t) {
			this.processadoComSucesso = false;
			t.printStackTrace();
		} finally {
			// Loga fim
			this.logarFim(logExecucao);
		}
	}
	
	/**
	 * Executa uma interface de carga de arquivo.
	 */
	private void executarInterfaceArquivo(InterfaceEnum interfaceEnum, InterfaceExecucao interfaceExecucao, LogExecucao logExecucao, Long codigoDistribuidor, String nomeUsuario) {
		
		// Recupera distribuidores
		String diretorio = parametroSistemaDAO.getParametro("INBOUND_DIR");
		List<String> distribuidores = this.getDistribuidores(diretorio, interfaceExecucao, codigoDistribuidor);
		
		// Processa arquivos do distribuidor
		for (String distribuidor: distribuidores) {
		
			List<File> arquivos = this.recuperaArquivosProcessar(diretorio, interfaceExecucao, distribuidor);
			
			if (arquivos == null || arquivos.isEmpty()) {
				this.logarArquivo(logExecucao, distribuidor, null, StatusExecucaoEnum.FALHA, NAO_HA_ARQUIVOS);
				continue;
			}
			
			CouchDbClient couchDbClient = this.getCouchDbClientInstance("db_" + StringUtils.leftPad(distribuidor, 7, "0"));
			
			for (File arquivo: arquivos) {
				
				try {
					
					this.trataArquivo(couchDbClient, arquivo, interfaceEnum, logExecucao.getDataInicio(), nomeUsuario);
					this.logarArquivo(logExecucao, distribuidor, arquivo.getAbsolutePath(), StatusExecucaoEnum.SUCESSO, null);
					arquivo.delete();
					
				} catch (Throwable e) {
					
					this.logarArquivo(logExecucao, distribuidor, arquivo.getAbsolutePath(), StatusExecucaoEnum.FALHA, e.getMessage());
					e.printStackTrace();
					continue;
					
				}
			}
			
			couchDbClient.shutdown();
		}
	}
	
	/**
	 * Executa a interface de carga de imagens EMS0134.
	 */
	private void executarInterfaceImagem() {
		
		String diretorio = parametroSistemaDAO.getParametro("IMAGE_DIR");
		CouchDbClient couchDbClient = this.getCouchDbClientInstance("db_integracao");
		
		File[] imagens = new File(diretorio).listFiles();
		
		for (File imagem: imagens) {
			
			IntegracaoDocument doc = null;
			String id = imagem.getName().substring(0, imagem.getName().indexOf(".")); 
			
			try {
				
				doc = couchDbClient.find(IntegracaoDocument.class, id);
			
			} catch (NoDocumentException e) {
				
				doc = new IntegracaoDocument();
				doc.set_id(id);
				doc.setTipoDocumento("ImagemCapa");
				couchDbClient.save(doc);
			}
				
			doc = couchDbClient.find(IntegracaoDocument.class, doc.get_id());
			
			try {
				FileInputStream in = new FileInputStream(imagem);
				couchDbClient.saveAttachment(in, imagem.getName(), "image/jpeg", doc.get_id(), doc.get_rev());
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			imagem.delete();
		}
		
		couchDbClient.shutdown();
	}
	
	/**
	 * Chama o shell script "cep-export", que converte o arquivo .mdb em um arquivo texto <br>
	 * contendo comandos sql, e compacta esse arquivo em .tar.gz. Em seguida, sobe esse arquivo <br>
	 * para o CouchDB como anexo a um documento.
	 */
	private void executarInterfaceCorreios() {
		
		String diretorio = parametroSistemaDAO.getParametro("CORREIOS_DIR");
		CouchDbClient couchDbClient = this.getCouchDbClientInstance("db_integracao");
		
		try {

			Process process = Runtime.getRuntime().exec(diretorio + "bin/cep-export");
			int retorno = process.waitFor();
			
			if (retorno != 0) {
				throw new RuntimeException("ERRO");
			}

			IntegracaoDocument doc = new IntegracaoDocument();
			doc.set_id("AtualizacaoCep");
			
			try {
				doc = couchDbClient.find(IntegracaoDocument.class, doc.get_id());
			} catch (NoDocumentException e) {
				doc.setTipoDocumento("AtualizacaoCep");
				couchDbClient.save(doc);
			}

			FileInputStream in = new FileInputStream(new File(diretorio + "data/dnecom.tar.gz"));
			couchDbClient.saveAttachment(in, "dnecom", "application/x-gzip-compressed", doc.get_id(), doc.get_rev());
			
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * Recupera distribuidores a serem processados.
	 */
	private List<String> getDistribuidores(String diretorio, InterfaceExecucao interfaceExecucao, Long codigoDistribuidor) {
		
		List<String> distribuidores = new ArrayList<String>();
		
		if (codigoDistribuidor == null) {
			
			File dirDistribs = new File(diretorio);
			distribuidores.addAll(Arrays.asList(dirDistribs.list()));
			
		} else {
			
			distribuidores.add(codigoDistribuidor.toString());
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
		
		in.close();
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
		
		File dir = new File(diretorio + codigoDistribuidor + File.separator);
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
