package br.com.abril.nds.integracao.fileimporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang.StringUtils;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.UpdateConflictException;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.lightcouch.View;
import org.lightcouch.ViewResult;
import org.lightcouch.ViewResult.Rows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.integracao.icd.model.DetalheFaltaSobra;
import br.com.abril.nds.integracao.icd.model.MotivoSituacaoFaltaSobra;
import br.com.abril.nds.integracao.icd.model.SolicitacaoFaltaSobra;
import br.com.abril.nds.integracao.model.InterfaceExecucao;
import br.com.abril.nds.integracao.model.LogExecucao;
import br.com.abril.nds.integracao.model.LogExecucaoArquivo;
import br.com.abril.nds.integracao.model.canonic.EMS0128Input;
import br.com.abril.nds.integracao.model.canonic.EMS0128InputItem;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocumentDetail;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocumentMaster;
import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;
import br.com.abril.nds.integracao.model.canonic.TipoInterfaceEnum;
import br.com.abril.nds.integracao.model.enums.StatusExecucaoEnum;
import br.com.abril.nds.integracao.repository.InterfaceExecucaoRepository;
import br.com.abril.nds.integracao.repository.LogExecucaoArquivoRepository;
import br.com.abril.nds.integracao.repository.LogExecucaoRepository;
import br.com.abril.nds.integracao.repository.ParametroSistemaRepository;
import br.com.abril.nds.integracao.service.IcdObjectService;
import br.com.abril.nds.model.dne.Bairro;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.model.dne.UnidadeFederacao;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;

/**
 * Realiza a execução das interfaces de integração. <br>
 * Lê as linhas dos arquivos de entrada, transforma em documentos e grava no CouchDB. 
 */
@Service
public class InterfaceExecutor {
	
	public static final String SPRING_FILE_LOCATION = "classpath:spring/applicationContext-ndsi-cli.xml"; 

	private static ApplicationContext applicationContext;
	
	private static String NAO_HA_ARQUIVOS = "Não há arquivos a serem processados para este distribuidor";
//	private static String TAMANHO_LINHA = "Tamanho da linha é diferente do tamanho definido";
	
	//private static Logger LOGGER = LoggerFactory.getLogger(InterfaceExecutor.class);
	
	@Autowired
	private IcdObjectService icdObjectService;

	@Autowired
	private LogExecucaoRepository logExecucaoRepository;
	@Autowired
	private LogExecucaoArquivoRepository logExecucaoArquivoRepository;
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;	
	@Autowired
	private InterfaceExecucaoRepository interfaceExecucaoRepository;
	
	@Autowired
	private FixedFormatManager ffm;
	
	@Autowired
	private CouchDbProperties couchDbProperties;
		
	private boolean processadoComSucesso = true;

	private String diretorio;

	private String pastaInterna;
	
	static {
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(SPRING_FILE_LOCATION);
		classPathXmlApplicationContext.registerShutdownHook();
		applicationContext = classPathXmlApplicationContext;
	}

	
	public CouchDbConnector initCouchDbClient(String dataBaseName) throws MalformedURLException {
		HttpClient authenticatedHttpClient = new StdHttpClient.Builder()
                .url(
                		new URL(
                		couchDbProperties.getProtocol(), 
                		couchDbProperties.getHost(), 
                		couchDbProperties.getPort(), 
                		"")
                	)
                .username(couchDbProperties.getUsername())
                .password(couchDbProperties.getPassword())
                .build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(authenticatedHttpClient);
		return dbInstance.createConnector(dataBaseName, true);				
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
		InterfaceExecucao interfaceExecucao = interfaceExecucaoRepository.findById(interfaceEnum.getCodigoInterface());
		
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
			} else if (interfaceEnum.getTipoInterfaceEnum().equals(TipoInterfaceEnum.DB)) {
				this.executarInterfaceDB(interfaceEnum, interfaceExecucao, logExecucao, codigoDistribuidor, nomeUsuario);
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
	@Transactional
	public void executarRetornosIcd(List<String> distribuidores) {		 
		

		for (String distribuidor: distribuidores) {
			
			
			if (new File(diretorio + distribuidor + File.separator + pastaInterna + File.separator).exists()) {

				CouchDbClient couchDbClient = this.getCouchDbClientInstance("db_" + StringUtils.leftPad(distribuidor, 8, "0"));
										
				View view = couchDbClient.view("importacao/porTipoDocumento");
								
				view.key("EMS0128");
				view.includeDocs(true);
				try {
					ViewResult<String, Void, ?> result = view.queryView(String.class, Void.class, EMS0128Input.class);
					for (@SuppressWarnings("rawtypes") Rows row: result.getRows()) {						
						
						EMS0128Input doc = (EMS0128Input) row.getDoc();
						
						if (doc.getSituacaoSolicitacao().equals("SOLICITADO")) {
							icdObjectService.insereSolicitacao(doc);
							doc.setSituacaoSolicitacao("AGUARDANDO_GFS");
						} else if (
								doc.getSituacaoSolicitacao().equals("AGUARDANDO_GFS") 
								|| doc.getSituacaoSolicitacao().equals("EM PROCESSAMENTO")) {
							
							SolicitacaoFaltaSobra solicitacao = icdObjectService.recuperaSolicitacao(Long.valueOf(distribuidor), doc);
							
							doc.setSituacaoSolicitacao(solicitacao.getCodigoSituacao());
							
							List<DetalheFaltaSobra> listaDetalhes = solicitacao.getItens();
							
							for (DetalheFaltaSobra item : listaDetalhes)
							{
								for ( EMS0128InputItem eitem : doc.getItems()) {
									if (item.getDfsPK().getNumeroSequencia().equals(eitem.getNumSequenciaDetalhe())) {
										eitem.setSituacaoAcerto(item.getCodigoAcerto());
										eitem.setNumeroDocumentoAcerto(item.getNumeroDocumentoAcerto());
										eitem.setDataEmicaoDocumentoAcerto(item.getDataEmissaoDocumentoAcerto());
										
										MotivoSituacaoFaltaSobra motivo = icdObjectService.recuperaMotivoPorDetalhe(item.getDfsPK());
										
										if (null!=motivo) {
											eitem.setDescricaoMotivo(motivo.getDescricaoMotivo());
											eitem.setCodigoOrigemMotivo(motivo.getCodigoMotivo());
										}
									}
								}
							}							
						}
						couchDbClient.update(doc);
						
					}
					

				} catch (NoDocumentException ex ) {
						
				}			
			}
		}
		
	}

	
	public List<String> recuperaDistribuidores(Long codigoDistribuidor) {
		this.diretorio = parametroSistemaRepository.getParametro("INBOUND_DIR");
		this.pastaInterna = parametroSistemaRepository.getParametro("INTERNAL_DIR");
		List<String> distribuidores = this.getDistribuidores(this.diretorio, codigoDistribuidor);
		return distribuidores;
	}

	private void executarInterfaceDB(InterfaceEnum interfaceEnum,
			InterfaceExecucao interfaceExecucao, LogExecucao logExecucao,
			Long codigoDistribuidor, String nomeUsuario) {
		
	}

	/**
	 * Executa uma interface de carga de arquivo.
	 */
	
	private void executarInterfaceArquivo(InterfaceEnum interfaceEnum, InterfaceExecucao interfaceExecucao, LogExecucao logExecucao, Long codigoDistribuidor, String nomeUsuario) {
		
		List<String> distribuidores = recuperaDistribuidores(codigoDistribuidor);
		
		// Processa arquivos do distribuidor
		for (String distribuidor: distribuidores) {
		
			List<File> arquivos = this.recuperaArquivosProcessar(this.diretorio, this.pastaInterna, interfaceExecucao, distribuidor);
			
			if (arquivos == null || arquivos.isEmpty()) {
				this.logarArquivo(logExecucao, distribuidor, null, StatusExecucaoEnum.FALHA, NAO_HA_ARQUIVOS);
				continue;
			}
			
			CouchDbClient couchDbClient = this.getCouchDbClientInstance("db_" + StringUtils.leftPad(distribuidor, 8, "0"));
			
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
		
		String diretorio = parametroSistemaRepository.getParametro("IMAGE_DIR");
		CouchDbClient couchDbClient = this.getCouchDbClientInstance("capas");
				
		File[] imagens = new File(diretorio).listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg");
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
			
				
			    doc = couchDbClient.find(IntegracaoDocument.class, doc.get_id());
			
			
				FileInputStream in = null;
				try {
					in = new FileInputStream(imagem);					
					couchDbClient.saveAttachment(in, imagem.getName().replace(".jpeg", ".jpg"), "image/jpeg", doc.get_id(), doc.get_rev());
				} catch (FileNotFoundException e1) {
					//TODO: remover o printStackTrace e trocar por log
					e1.printStackTrace();
				} finally {
					if (null != in) {
						try {
							in.close();
						} catch (IOException e1) {							
							e1.printStackTrace();
						}
					}
				}
				
			} catch (Exception e) {
				//TODO: remover o printStackTrace e trocar por log
				e.printStackTrace();
			}
			
		}
		
		couchDbClient.shutdown();
	}
	
	/**
	 * Chama o shell script "cep-export", que converte o arquivo .mdb em um arquivo texto <br>
	 * contendo comandos sql, e compacta esse arquivo em .tar.gz. Em seguida, sobe esse arquivo <br>
	 * para o CouchDB como anexo a um documento.
	 */
	
	private void executarInterfaceCorreios() {
		
		String diretorio = parametroSistemaRepository.getParametro("CORREIOS_DIR");
		CouchDbConnector couchDbClient = null;
		try {
			couchDbClient = initCouchDbClient("correios");
		} catch (MalformedURLException e1) {
			return;
		}
		
		try {
			
			Database db = Database.open(new File(diretorio + "dnecom.mdb"));

			Table tblBairro = db.getTable("LOG_BAIRRO");
			Table tblLogradouro = db.getTable("LOG_LOGRADOURO");
			Table tblLocalidade = db.getTable("LOG_LOCALIDADE");
			Table tblUf = db.getTable("LOG_FAIXA_UF");

			for(Map<String, Object> row : tblBairro) {
					
				if(row != null && !row.isEmpty()) {
					
					Bairro doc = new Bairro();
					doc.setTipoDocumento("bairro");					
					doc.set_id( "bairro/" + (row.get("BAI_NU") != null ? row.get("BAI_NU").toString() : "" ));
					
					doc.setNome((row.get("BAI_NO") != null ? row.get("BAI_NO").toString() : "" ));
					doc.setUf((row.get("UFE_SG") != null ? row.get("UFE_SG").toString() : "" ));
					Localidade l = new Localidade();
					l.set_id( "localidade/" + (row.get("LOC_NU") != null ? row.get("LOC_NU").toString() : "" ));
					doc.setLocalidade(l);

					saveOrUpdate(couchDbClient, doc);
				}
			}

			for(Map<String, Object> row : tblLogradouro) {
				
							
				Logradouro doc = new Logradouro();
				
				doc.setTipoDocumento("logradouro");
				doc.set_id("logradouro/" + (row.get("LOG_NU") != null ? row.get("LOG_NU").toString() : "" ));
				doc.setNome((row.get("LOG_NO") != null ? row.get("LOG_NO").toString() : "" ));
				doc.setComplemento((row.get("LOG_COMPLEMENTO") != null ? row.get("LOG_COMPLEMENTO").toString() : "" ));
				doc.setCep((row.get("CEP") != null ? row.get("CEP").toString() : "" ));
				doc.setUf((row.get("UFE_SG") != null ? row.get("UFE_SG").toString() : "" ));				
				doc.setAbreviatura((row.get("LOG_NO_ABREV") != null ? row.get("LOG_NO_ABREV").toString() : "" ));
				doc.setTipoLogradouro((row.get("TLO_TX") != null ? row.get("TLO_TX").toString() : "" ));
				
				Localidade l = new Localidade();
				l.set_id("localidade/" + (row.get("LOC_NU") != null ? row.get("LOC_NU").toString() : "" ));
				doc.setLocalidade(l);
				
				
				Bairro bi = new Bairro();
				bi.set_id("bairro/" + (row.get("BAI_NU_INI") != null ? row.get("BAI_NU_INI").toString() : "" ));
				doc.setBairroInicial(bi);
				
				Bairro bf = new Bairro();
				bf.set_id("bairro/" + (row.get("BAI_NU_FIM") != null ? row.get("BAI_NU_FIM").toString() : "" ));
				doc.setBairroFinal(bf);

				saveOrUpdate(couchDbClient, doc);
			}
			
			for(Map<String, Object> row : tblLocalidade) {
				 
				Localidade doc = new Localidade();
				
				doc.setTipoDocumento("localidade");					
				doc.set_id("localidade/" + (row.get("LOC_NU") != null ? row.get("LOC_NU").toString() : "" ));
				doc.setNome((row.get("LOC_NO") != null ? row.get("LOC_NO").toString() : "" ));
				doc.setCep((row.get("CEP") != null ? row.get("CEP").toString() : "" ));
				doc.setAbreviatura((row.get("LOG_NO_ABREV") != null ? row.get("LOG_NO_ABREV").toString() : "" ));
				doc.setCodigoMunicipioIBGE((row.get("MUN_NU") != null ? Long.valueOf( row.get("MUN_NU").toString() ) : null ));
				
				UnidadeFederacao u = new UnidadeFederacao();
				u.set_id("uf/" + (row.get("UFE_SG") != null ? row.get("UFE_SG").toString() : "" ));
				doc.setUnidadeFederacao(u);

				saveOrUpdate(couchDbClient, doc);
			}

			for(Map<String, Object> row : tblUf) {
								
				
				UnidadeFederacao doc = new UnidadeFederacao();
				
				doc.setTipoDocumento("uf");					
				doc.set_id("uf/" + (row.get("UFE_SG") != null ? row.get("UFE_SG").toString() : "" ));
				doc.setSigla((row.get("UFE_SG") != null ? row.get("UFE_SG").toString() : "" ));
				doc.setFaixaCepInicial((row.get("UFE_CEP_INI") != null ? row.get("UFE_CEP_INI").toString() : "" ));
				doc.setFaixaCepFinal((row.get("UFE_CEP_FIM") != null ? row.get("UFE_CEP_FIM").toString() : "" ));

				saveOrUpdate(couchDbClient, doc);
			}

		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		
	}


	private <T extends IntegracaoDocument> void saveOrUpdate(CouchDbConnector couchDbClient, T doc) {
		try {
			couchDbClient.create(doc);
		} catch (UpdateConflictException ex) {
			try {
				doc.set_rev( couchDbClient.get(doc.getClass(), doc.get_id()).get_rev() );
				couchDbClient.update(doc);
			} catch (UpdateConflictException exx) {
			}
		}
	}
	
	
	/**
	 * Recupera distribuidores a serem processados.
	 */
	private List<String> getDistribuidores(String diretorio, Long codigoDistribuidor) {
		
		List<String> distribuidores = new ArrayList<String>();
		
		if (codigoDistribuidor == null) {
			
			FilenameFilter numericFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					 return name.matches("\\d+");  
				}
			};
			
			File dirDistribs = new File(diretorio);
			distribuidores.addAll(Arrays.asList(dirDistribs.list( numericFilter )));
			
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
		IntegracaoDocumentMaster<?> docM = null;
		
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
			
			if (interfaceEnum.getTipoInterfaceEnum() == TipoInterfaceEnum.SIMPLES ) {
				IntegracaoDocument doc = (IntegracaoDocument) this.ffm.load(interfaceEnum.getClasseLinha(), linha);
				
				doc.setTipoDocumento(interfaceEnum.name());
				doc.setNomeArquivo(arquivo.getName());
				doc.setLinhaArquivo(linhaArquivo);
				doc.setDataHoraExtracao(dataInicio);
				doc.setNomeUsuarioExtracao(nomeUsuario);
	
				couchDbClient.save(doc);
			} else if (interfaceEnum.getTipoInterfaceEnum() == TipoInterfaceEnum.DETALHE_INLINE) {

				IntegracaoDocumentDetail docD = (IntegracaoDocumentDetail) this.ffm.load(interfaceEnum.getClasseDetail(), linha);
				
				if (((IntegracaoDocumentMaster<?>) this.ffm.load(interfaceEnum.getClasseMaster(), linha)).sameObject(docM)) {
					docM.addItem(docD);				
				} else {
					if (docM != null) {
						couchDbClient.save(docM);							
					}
					
					docM = (IntegracaoDocumentMaster<IntegracaoDocumentDetail>) this.ffm.load(interfaceEnum.getClasseMaster(), linha);
					
					docM.setTipoDocumento(interfaceEnum.name());
					docM.setNomeArquivo(arquivo.getName());
					docM.setLinhaArquivo(linhaArquivo);
					docM.setDataHoraExtracao(dataInicio);
					docM.setNomeUsuarioExtracao(nomeUsuario);
					docM.addItem(docD);				
		
					
				}
			}
			
			
		}
		if (docM != null) {
			couchDbClient.save(docM);							
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
	private List<File> recuperaArquivosProcessar(String diretorio, String pastaInterna, InterfaceExecucao interfaceExecucao, String codigoDistribuidor) {
		
		List<File> listaArquivos = new ArrayList<File>();
		
		File dir = new File(diretorio + codigoDistribuidor + File.separator + pastaInterna + File.separator);
		File[] files = dir.listFiles((FilenameFilter) new RegexFileFilter(interfaceExecucao.getMascaraArquivo(), IOCase.INSENSITIVE));
				
		if (null != files) {
			Arrays.sort(files, 0, files.length);
			listaArquivos.addAll(Arrays.asList(files));
		}
		
		return listaArquivos;
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
				couchDbProperties.getProtocol(),
				couchDbProperties.getHost(),
				couchDbProperties.getPort(),
				couchDbProperties.getUsername(),
				couchDbProperties.getPassword()
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
		logExecucao.setDataFim(dataInicio);
		logExecucao.setStatus(StatusExecucaoEnum.SUCESSO);
		
		return logExecucaoRepository.inserir(logExecucao);
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
		
		this.logExecucaoArquivoRepository.inserir(logExecucaoArquivo);
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
		
		logExecucaoRepository.atualizar(logExecucao);
	}
}