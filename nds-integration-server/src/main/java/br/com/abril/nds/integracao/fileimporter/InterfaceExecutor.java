package br.com.abril.nds.integracao.fileimporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.integracao.model.InterfaceEnum;
import br.com.abril.nds.integracao.model.canonic.EMS0110FilialInput;
import br.com.abril.nds.integracao.model.canonic.EMS0110Input;
import br.com.abril.nds.integracao.model.canonic.EMS0128Input;
import br.com.abril.nds.integracao.model.canonic.EMS0128InputItem;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocumentDetail;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocumentMaster;
import br.com.abril.nds.integracao.model.canonic.TipoInterfaceEnum;
import br.com.abril.nds.integracao.repository.InterfaceExecucaoRepository;
import br.com.abril.nds.integracao.repository.LogExecucaoArquivoRepository;
import br.com.abril.nds.integracao.repository.LogExecucaoRepository;
import br.com.abril.nds.integracao.repository.ParametroDistribuidorRepository;
import br.com.abril.nds.integracao.route.RouteTemplate;
import br.com.abril.nds.integracao.service.IcdObjectService;
import br.com.abril.nds.model.dne.Bairro;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.model.dne.UnidadeFederacao;
import br.com.abril.nds.model.integracao.InterfaceExecucao;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoArquivo;
import br.com.abril.nds.model.integracao.ParametroDistribuidor;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
import br.com.abril.nds.model.integracao.TipoDistribuidor;
import br.com.abril.nds.model.integracao.icd.DetalheFaltaSobra;
import br.com.abril.nds.model.integracao.icd.MotivoSituacaoFaltaSobra;
import br.com.abril.nds.model.integracao.icd.SolicitacaoFaltaSobra;
import br.com.abril.nds.repository.ParametroSistemaRepository;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;

/**
 * Realiza a execução das interfaces de integração. <br>
 * Lê as linhas dos arquivos de entrada, transforma em documentos e grava no
 * CouchDB.
 */
@Service
public class InterfaceExecutor {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(InterfaceExecucao.class);
	
	public static final String SPRING_FILE_LOCATION = "classpath:spring/applicationContext-ndsi-cli.xml"; 

	private static ApplicationContext applicationContext;
	
    private static String NAO_HA_ARQUIVOS = "Não há arquivos a serem processados para este distribuidor";
    
    private static String HA_ARQUIVOS = "Arquivos a serem processados para este distribuidor";

    private static String NAO_HA_IMAGENS = "Não há imagens a serem processados";

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
	private ParametroDistribuidorRepository parametroDistribuidorRepository;
	
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
        } catch (Exception e) {
			this.processadoComSucesso = false;
            LOGGER.error(e.getMessage(), e);
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
										eitem.setDataEmissaoDocumentoAcerto(item.getDataEmissaoDocumentoAcerto());
										
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
		List<String> distribuidores = this.getDistribuidores(this.diretorio, codigoDistribuidor);
		return distribuidores;
	}
	
	public void carregarDiretorios(InterfaceEnum interfaceEnum) {
		
		String parametroDir = "INBOUND_DIR";
	
		this.diretorio = parametroSistemaRepository.getParametroInterface(parametroDir);
		
		this.pastaInterna = parametroSistemaRepository.getParametroInterface("INTERNAL_DIR");
	}
	
	private void executarInterfaceDB(InterfaceEnum interfaceEnum,
			InterfaceExecucao interfaceExecucao, LogExecucao logExecucao,
			Long codigoDistribuidor, String nomeUsuario) {
		
		getRouteTemplate(interfaceExecucao.getNome()).execute("user");
		
	}
	
	private RouteTemplate getRouteTemplate(String interfaceName) {
		
		try {
			
			String classe = "br.com.abril.nds.integracao."+ interfaceName.toLowerCase() +".route."+ interfaceName.toUpperCase() +"Route";
			
			return (RouteTemplate) applicationContext.getBean(Class.forName(classe));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Executa uma interface de carga de arquivo.
	 */
	private void executarInterfaceArquivo(InterfaceEnum interfaceEnum, InterfaceExecucao interfaceExecucao, LogExecucao logExecucao, Long codigoDistribuidor, String nomeUsuario) {
		
		this.carregarDiretorios(interfaceEnum);
		List<String> distribuidores = recuperaDistribuidores(codigoDistribuidor);
		LOGGER.error("Interface de arquivos.. Numero de distribuidores="+distribuidores.size()+"  "+distribuidores.toString());
		// Processa arquivos do distribuidor
		for (String distribuidor: distribuidores) {
			
			LOGGER.error("Interface de arquivos.. Processando distribuidor="+distribuidor+" Procurando arquivos em "+this.diretorio+" "+this.pastaInterna);
			List<File> arquivos = this.recuperaArquivosProcessar(this.diretorio, this.pastaInterna, interfaceExecucao, distribuidor);
			
			if(arquivos == null || arquivos.isEmpty()) {
				LOGGER.error("NAO HA ARQUIVOS PARA ESTE DISTRIBUIDOR");
				this.logarArquivo(logExecucao, distribuidor, null, StatusExecucaoEnum.FALHA, NAO_HA_ARQUIVOS);
				continue;
			} else {
				this.logarArquivo(logExecucao, distribuidor, null, StatusExecucaoEnum.SUCESSO, HA_ARQUIVOS);
			}
			
			CouchDbClient couchDbClient = this.getCouchDbClientInstance("db_" + StringUtils.leftPad(distribuidor, 8, "0"));
			LOGGER.error("Couchdb="+couchDbClient.toString());
			interfaceEnum = this.tratarInterfaceEnumDistribuidorFilial(interfaceEnum,distribuidor);
			
			for (File arquivo: arquivos) {
				
				try {
					
					this.trataArquivo(couchDbClient, arquivo, interfaceEnum, logExecucao.getDataInicio(), nomeUsuario,interfaceExecucao);
					this.logarArquivo(logExecucao, distribuidor, arquivo.getAbsolutePath(), StatusExecucaoEnum.SUCESSO, "Inseridos no couchDB");
					arquivo.delete();
					
				} catch (Exception e) {
					
					this.logarArquivo(logExecucao, distribuidor, arquivo.getAbsolutePath(), StatusExecucaoEnum.FALHA, e.getMessage());
					LOGGER.error(e.getMessage(), e);
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
		
		String diretorio = parametroSistemaRepository.getParametroInterface("IMAGE_DIR");
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
					
					imagem.delete();
				} catch (FileNotFoundException e1) {
                    this.logarArquivo(StatusExecucaoEnum.AVISO, NAO_HA_IMAGENS);
				} finally {
					if (null != in) {
						try {
							in.close();
                        } catch (IOException ex) {
                            LOGGER.error(ex.getMessage(), ex);
						}
					}
				}
				
			} catch (Exception e) {
                this.logarArquivo(StatusExecucaoEnum.AVISO, NAO_HA_IMAGENS);
				//LOGGER.error(e.getMessage(), e);
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
		
		String diretorio = parametroSistemaRepository.getParametroInterface("CORREIOS_DIR");
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

		} catch (Exception e) {
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
	@SuppressWarnings({ "unchecked", "static-access" })
	private void trataArquivo(CouchDbClient couchDbClient, 
			File arquivo, 
			InterfaceEnum interfaceEnum, 
			Date dataInicio, 
			String nomeUsuario,
			InterfaceExecucao interfaceExecucao) throws Exception {

		//FileReader in = new FileReader(arquivo);
		//Scanner scanner = new Scanner(in);
		//Scanner scanner = new Scanner(arquivo, Files.probeContentType(arquivo.toPath()).equals("text/plain") ? StandardCharsets.ISO_8859_1.toString() : StandardCharsets.UTF_8.toString());
		
		if(interfaceEnum == null) {
			
			throw new Exception("Parâmetro necessário está nulo.");
		}
		
		Scanner scanner = new Scanner(arquivo, Files.probeContentType(arquivo.toPath()).equals("text/plain") ? "iso-8859-1" : "utf-8");
		
		int linhaArquivo = 0;
		IntegracaoDocumentMaster<?> docM = null;
		
		while (scanner.hasNextLine()) {
			
			String linha = scanner.nextLine();
			linhaArquivo++;
			
			// Ignora linha vazia e aquele caracter estranho em formato de seta para direita
			if (StringUtils.isEmpty(linha) ||  ((int) linha.charAt(0)  == 26) ) {
				continue;
			} 
			
			try {
				
				switch (interfaceEnum.getTipoInterfaceEnum()) {
				case SIMPLES:
					IntegracaoDocument doc = (IntegracaoDocument) this.ffm.load(interfaceEnum.getClasseLinha(), linha);
					
					doc.setTipoDocumento(interfaceEnum.name());
					doc.setNomeArquivo(arquivo.getName());
					doc.setLinhaArquivo(linhaArquivo);
					doc.setDataHoraExtracao(dataInicio);
					doc.setNomeUsuarioExtracao(nomeUsuario);
						
					couchDbClient.save(doc);
					break;

				case DETALHE_INLINE:
					
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
					break;
					
				default:
					break;
				}
				
					

				
			} catch(IllegalArgumentException | com.ancientprogramming.fixedformat4j.format.ParseException pe) {
				
				String mensagemErro = "ERRO: Ocorreu um erro na leitura do arquivo %s na linha número %d, porém o arquivo foi processado!";
				
				mensagemErro = mensagemErro.format(mensagemErro,arquivo.getName(),linhaArquivo);
				
				LOGGER.error(mensagemErro,pe);
			}
		}
		if (docM != null) {
			couchDbClient.save(docM);							
		}
		
//		in.close();
		scanner.close();
	}

	/**
	 * Verifica se o distribuidor é uma filial, caso seja muda a classe de parse para EMS0110
	 * 
	 * @param interfaceEnum
	 * @param distribuidor
	 * @return InterfaceEnum
	 */
	private InterfaceEnum tratarInterfaceEnumDistribuidorFilial(InterfaceEnum interfaceEnum,String distribuidor){
		
		if(interfaceEnum == null){
			return interfaceEnum;
		}
		
		if(distribuidor == null || distribuidor.isEmpty()){
			return interfaceEnum;
		}
		
		ParametroDistribuidor parametroDistribuidor = parametroDistribuidorRepository.findByCodigoDinapFC(Long.parseLong(distribuidor));
		
		if(parametroDistribuidor == null){
			LOGGER.warn("PARAMETRO DO DISTRIBUIDOR: Parâmetro do distribuidor não foi encontrado para o código [ " + Long.parseLong(distribuidor) +"]" );
		}
		
		LOGGER.error("TIPO DISTRIBUIDOR  para codigo "+Long.parseLong(distribuidor)+"  ="+parametroDistribuidor.getTipoDistribuidor());
		boolean isDistribuidorFilial = ((parametroDistribuidor != null 
				&& TipoDistribuidor.FILIAL.equals(parametroDistribuidor.getTipoDistribuidor())));
		
		LOGGER.error("ISDISTRIBUIDORFILAL"+isDistribuidorFilial);
		
		
		
		if(InterfaceEnum.EMS0110.equals(interfaceEnum)){
			LOGGER.error("PROCESSANDO ARQUIVO COM INTERFACE EMS110");
			
			if(isDistribuidorFilial){
			  interfaceEnum = InterfaceEnum.EMS0110.getInterfaceEnum(EMS0110FilialInput.class);
			  LOGGER.error("FILIAL");
			}else{
			  interfaceEnum = InterfaceEnum.EMS0110.getInterfaceEnum(EMS0110Input.class);
			  LOGGER.error("DISTRIBUIDOR");
			}
		}
		
		LOGGER.error("PROCESSANDO ARQUIVO COM INTERFACE "+interfaceEnum);
		return interfaceEnum;
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
		
		String pattern = (interfaceExecucao.getMascaraArquivo()!= null) 
				? interfaceExecucao.getMascaraArquivo().trim()
						:interfaceExecucao.getMascaraArquivo();
				
		String dirPath = diretorio + codigoDistribuidor + File.separator + pastaInterna + File.separator;
		LOGGER.error("PROCURANDO ARQUIVOS EM "+dirPath+" com  padrao="+pattern);
		File dir = new File(dirPath);
		
		FilenameFilter filter = (FilenameFilter) new RegexFileFilter(pattern, IOCase.INSENSITIVE);
		
		File[] files = dir.listFiles(filter);
				
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
		LOGGER.error("COUCHDB="+couchDbProperties.getHost()+":"+couchDbProperties.getPort()+"  database="+databaseName);
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
	
    private void logarArquivo(StatusExecucaoEnum status, String mensagem) {
        this.logarArquivo(null, null, null, status, mensagem);
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
