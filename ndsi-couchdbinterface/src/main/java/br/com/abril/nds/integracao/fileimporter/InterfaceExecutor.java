package br.com.abril.nds.integracao.fileimporter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang.StringUtils;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbException;
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
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.dne.Bairro;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.model.dne.UnidadeFederacao;
import br.com.abril.nds.vo.EnderecoVO;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.healthmarketscience.jackcess.Cursor;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;

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
		CouchDbClient couchDbClient = this.getCouchDbClientInstance("capas");
				
		File[] imagens = new File(diretorio).listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".jpg");
		    }
		});
		
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
		CouchDbClient couchDbClient = this.getCouchDbClientInstance("correios");
		
		try {
			
			Database db = Database.open(new File(diretorio + "dnecom.mdb"));
			/*
			Table tblBairro = db.getTable("LOG_BAIRRO");
			for(Map<String, Object> row : tblBairro) {
				
				System.out.println(
						  row.get("BAI_NU").toString() 
						  + row.get("UFE_SG").toString() 
						  + row.get("LOC_NU").toString() 
						  + row.get("BAI_NO").toString()  
						  + row.get("BAI_NO_ABREV")
				);
				  
				Bairro doc = new Bairro();
				doc.setTipoDocumento("CEP");					
				doc.setSubTipoDocumento("BAIRRO");
				doc.set_id(row.get("BAI_NU").toString());
				doc.setNome(row.get("BAI_NO").toString());
				doc.setUf(row.get("UFE_SG").toString());
				Localidade l = new Localidade();
				l.set_id(row.get("LOC_NU").toString());
				doc.setLocalidade(l);
				try {
					couchDbClient.save(doc);
				} catch (CouchDbException ce) {
					
				}
				
			}

			Table tblLogradouro = db.getTable("LOG_LOGRADOURO");
			for(Map<String, Object> row : tblLogradouro) {
				
				System.out.println(
					  row.get("LOG_NU").toString() 
					  + row.get("UFE_SG").toString() 
					  + row.get("LOC_NU").toString() 
					  + row.get("BAI_NU_INI").toString()  
					  + row.get("BAI_NU_FIM").toString()
					  + row.get("LOG_NO").toString()
					  + row.get("LOG_COMPLEMENTO").toString()
					  + row.get("CEP").toString()
					  + row.get("TLO_TX").toString()
					  + row.get("LOG_STA_TLO").toString()
					  + row.get("LOG_NO_ABREV").toString()
					  );
				
				Logradouro doc = new Logradouro();
				
				doc.setTipoDocumento("CEP");					
				doc.setSubTipoDocumento("LOGRADOURO");
				doc.set_id(row.get("LOG_NU").toString());
				doc.setNome(row.get("LOG_NO").toString());
				doc.setComplemento(row.get("LOG_COMPLEMENTO").toString());
				doc.setCep(row.get("CEP").toString());
				doc.setUf(row.get("UFE_SG").toString());				
				doc.setAbreviatura(row.get("LOG_NO_ABREV").toString());
				
				Localidade l = new Localidade();
				l.set_id(row.get("LOC_NU").toString());
				doc.setLocalidade(l);
				try {
					couchDbClient.save(doc);
				} catch (CouchDbException ce) {
					
				}
			}
			
			Table tblLocalidade = db.getTable("LOG_LOCALIDADE");
			for(Map<String, Object> row : tblLocalidade) {
				 
				System.out.println(
					  row.get("LOC_NU").toString() 
					  + row.get("UFE_SG").toString() 
					  + row.get("LOC_NO").toString() 
					  + row.get("CEP").toString()  
					  + row.get("LOC_IN_SIT").toString()
					  + row.get("LOC_IN_TIPO_LOC").toString()
					  + row.get("LOC_NU_SUB").toString()
					  + row.get("LOC_NO_ABREV").toString()
					  + row.get("MUN_NU").toString()
					  );
				
				Localidade doc = new Localidade();
				
				doc.setTipoDocumento("CEP");					
				doc.setSubTipoDocumento("LOCALIDADE");
				doc.set_id(row.get("LOC_NU").toString());
				doc.setNome(row.get("LOG_NO").toString());
				doc.setCep(row.get("CEP").toString());
				doc.setAbreviatura(row.get("LOG_NO_ABREV").toString());
				
				UnidadeFederacao u = new UnidadeFederacao();
				u.set_id(row.get("UFE_SG").toString());
				doc.setUnidadeFederacao(u);
				try {
					couchDbClient.save(doc);
				} catch (CouchDbException ce) {
					
				}
			}

			Table tblUf = db.getTable("LOG_FAIXA_UF");
			for(Map<String, Object> row : tblUf) {
				
				System.out.println(						
					  row.get("UFE_SG").toString() 
					  + row.get("UFE_CEP_INI").toString() 
					  + row.get("UFE_CEP_FIM").toString() 						  
					  );
				
				UnidadeFederacao doc = new UnidadeFederacao();
				
				doc.setTipoDocumento("CEP");					
				doc.setSubTipoDocumento("LOCALIDADE");
				doc.set_id(row.get("UFE_SG").toString());
				doc.setFaixaCepInicial(row.get("UFE_CEP_INI").toString() );
				doc.setFaixaCepFinal(row.get("UFE_CEP_FIM").toString() );
				try {
					couchDbClient.save(doc);
				} catch (CouchDbException ce) {
					
				}
			}
			*/
			
			Table tblBairro = db.getTable("LOG_BAIRRO");
			Table tblLogradouro = db.getTable("LOG_LOGRADOURO");
			Table tblLocalidade = db.getTable("LOG_LOCALIDADE");
			Table tblUf = db.getTable("LOG_FAIXA_UF");
			
			
			for(Map<String, Object> rowLog : tblLogradouro) {
				
				if(rowLog != null && !rowLog.isEmpty()) {
				
					
					EnderecoVO doc = new EnderecoVO();
										
					if ( rowLog.get("LOC_NU") != null ) {
						
						Map<String, Object> rowLoc = Cursor.findRow(tblLocalidade, Collections.singletonMap("LOC_NU", rowLog.get("LOC_NU")));
						
						doc.setLocalidade((rowLoc != null && rowLoc.get("LOC_NO") != null ? rowLoc.get("LOC_NO").toString() : "" ));
						doc.setCodigoCidadeIBGE((rowLoc != null && rowLoc.get("MUN_NU") != null ? Long.valueOf( rowLoc.get("MUN_NU").toString()) : null ));
						
					}
					
					if ( rowLog.get("BAI_NU_INI") != null ) {
						
						Map<String, Object> rowBai = Cursor.findRow(tblBairro, Collections.singletonMap("BAI_NU", rowLog.get("BAI_NU_INI")));
						doc.setBairro((rowBai != null ? rowBai.get("BAI_NO").toString() : "" ));
						
					}
					
									
					doc.setTipoDocumento("CEP");					
					doc.setSubTipoDocumento("CEP");
					doc.setCep((rowLog.get("CEP") != null ? rowLog.get("CEP").toString() : "" ));
					doc.setIdLocalidade((rowLog.get("LOC_NU") != null ? Long.valueOf( rowLog.get("LOC_NU").toString() ) : null ));										
					doc.setUf((rowLog.get("UFE_SG") != null ? rowLog.get("UFE_SG").toString() : "" ));
					doc.setComplemento((rowLog.get("LOG_COMPLEMENTO") != null ? rowLog.get("LOG_COMPLEMENTO").toString() : "" ));
					doc.setLogradouro((rowLog.get("LOG_NO") != null ? rowLog.get("LOG_NO").toString() : "" ));
					doc.setTipoLogradouro((rowLog.get("TLO_TX") != null ? rowLog.get("TLO_TX").toString() : "" ));
				
					try {
						couchDbClient.save(doc);
					} catch (CouchDbException ce) {
						System.out.println(ce);
					}
				}
			}

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
			
			// Ignora linha vazia e aquele caracter estranho em formato de seta para direita
			if (StringUtils.isEmpty(linha) ||  ((int) linha.charAt(0)  == 26) ) {
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
