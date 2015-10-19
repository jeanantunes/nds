package br.com.abril.nds.controllers.administracao;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CaracteristicaDistribuicaoSimplesDTO;
import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.ControleCotaDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoSimplesDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.ems0129.route.EMS0129Route;
import br.com.abril.nds.integracao.ems0197.route.EMS0197Route;
import br.com.abril.nds.integracao.ems0198.route.EMS0198Route;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CaracteristicaDistribuicaoService;
import br.com.abril.nds.service.ConsolidadoFinanceiroService;
import br.com.abril.nds.service.ControleCotaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DeparaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.service.VendaEncalheService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.DateUtil;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * @author InfoA2
 * Controller de Cadastro de Tipo de Notas
 */
@Resource
@Path("/administracao/geracaoArquivos")
@Rules(Permissao.ROLE_ADMINISTRACAO_GERACAO_ARQUIVO)
public class GeracaoArquivosController extends BaseController {

	private static Logger LOGGER = LoggerFactory.getLogger(GeracaoArquivosController.class);
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private EMS0129Route route129;		

	@Autowired
	private EMS0197Route route197;		
	
	@Autowired
	private EMS0198Route route198;		
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
    private CalendarioService calendarioService;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService; 
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private DeparaService deparaService;
	
	
	@Autowired
	private ControleCotaService controleCotaService;
	
	
	@Autowired
	private VendaEncalheService vendaEncalheService;
	
	@Autowired
	private CotaService cotaService;
	
	
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired 
	private GerarCobrancaService cobrancaService;
	
	
	
	@Autowired
	private SituacaoCotaService situacaoCotaService;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	 private  CaracteristicaDistribuicaoService caracteristicaDistribuicaoService;
	
	
	@Autowired
	private ConsolidadoFinanceiroService consolidadoFinanceiroService;
	
	
	@Autowired
	private EmailService emailService;
	
    
    @Autowired
    private HttpServletResponse httpResponse;
    
	
	@Path("/")
	public void index() {
		preencherComboArquivos();
	}

	
	
	public void preencherComboArquivos()  {
		 List <String> list = new ArrayList();
       
		try {
		 String dirBanca = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO).getValor();
			
		 String path  = dirBanca+"/dinap";
		
		
		 FilenameFilter fileFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					
					if (name.endsWith(".zip") ) {
						return true;
					} else {
						return false;
					}
				}
			};
			File[] files = new File(path).listFiles(fileFilter);
			Arrays.sort(files, new Comparator<File>(){
			    public int compare(File f1, File f2)
			    {
			        return -1*Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			    } });
			
			for(File input : files) {				
				list.add(input.getName());				
			}
		
	
		} catch (Exception e) {
			LOGGER.error("Erro obtendo arquivos dinap",e);
		}
		
		this.result.include("arquivosDinapList", list);
		
	}

	@Path("/getFile")
	public void getFile() throws IOException {
		String path =  (String) session.getValue("PATH_VENDA");
		byte[] arquivo=("Nao encontrado "+path).getBytes();
		if ( path == null ) { // se nao foi gerado agora pegar o o mais novo, caso senha
		   // LOGGER.warn("Arquivo nao gerado");
			//result.use(Results.nothing());
			//return;
			/*
			 String dirBanca = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO).getValor();
			for ( int i=-14; i < 30;i++ ){
				 Calendar cal = Calendar.getInstance();
			     cal.add(Calendar.DATE, -i);   
			     Date d = cal.getTime();
			   path  = dirBanca+"/caruso/"+String.format("%05d",90100)+DateUtil.formatarData(d,"ddMMyyyy")+".ENP";
			   File file = new File(path);
				  if ( file.exists()) {
					 break;
				  }
			}
			*/
			 arquivo=("Nao ha arquivo gerado").getBytes();
			 httpResponse.setContentType("application/txt");
		        
		        httpResponse.setHeader("Content-Disposition", "attachment; filename=nao encontrado");
		        
		      
		} else {
		
		  File file = new File(path);
		  if ( file.exists()) {
			  arquivo =  java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(path));
		  }
		  httpResponse.setContentType("application/txt");
	        
	        httpResponse.setHeader("Content-Disposition", "attachment; filename="+file.getName());
	        
	      
		 
		}
		  
		  final OutputStream output = httpResponse.getOutputStream();
	        output.write(arquivo);
	        
	        httpResponse.getOutputStream().close();
	        
	        result.use(Results.nothing());
		  
		
		
		
		
	}
	
	
	// gerar arquivo com vendas reparte agregado ( caso caruso )
	@Post
	@Rules(Permissao.ROLE_ADMINISTRACAO_GERACAO_ARQUIVO_ALTERACAO)
	public void gerarVenda(Date dataLctoPrevisto, String operacao) {
		try {
			 Date data = dataLctoPrevisto;
		     if ( data == null )
		        data = new Date(); // pegar data de hoje
			 LOGGER.warn("INICIANDO GERACAO DE VENDAS  para data="+data);
			  String crlf = System.getProperty("line.separator");
			  
		      String dirBanca = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO).getValor();
		      
		      List <ControleCotaDTO> ccList= controleCotaService.buscarControleCota();
		            
		      Map <Integer,Set <Integer> > cotasMaster = new HashMap();
		      
		      // agrupar cotas por cotas master
		      
		      for ( ControleCotaDTO cc: ccList ) {
		    	  
		    	   Set <Integer> lista = cotasMaster.get( cc.getNumeroCotaMaster());
		    	   
		    	   if ( lista == null ) {
		    		   
		    		  lista = new  HashSet();
		    		  lista.add(cc.getNumeroCotaMaster()); // insere cota master na lista, just in case nao esteja cadastrada
		    		                                          // como cota agrupada a ela mesmo ..
		    		  
		    	       }
		    	      lista.add(cc.getNumeroCota());
		    	      
		    	      cotasMaster.put(cc.getNumeroCotaMaster(),lista);
		    	  
		    	   }
		    	   
		      // para cota master, gerar vendas/reparte de todas as cotas subsidiaria
		      // gerar arquivo (9010202092015.ENP) no formato abaixo
		      /*
01107573500090109901099010902092015000692892015000007897823436443GUIA ARTE COM MAOS CUP CAKE   00000002CASA DOIS COMUNICACAO LTDA         0000001999000000139930 MODELOS PASSO A PASSO      0209201500000002
		    select '011',lpad(trim(b.cod_filial_prodin),7,0) as cod_filial,

		      lpad(a.cod_jornaleiro,7,0) as cod_jornaleiro,

		      lpad(a.cod_jornaleiro,5,0) as cod_jornaleiro,

		      lpad(a.cod_jornaleiro,5,0) as cod_jornaleiro,

		      replace(a.dt_mov,'/','') as dat_mov,

		      lpad(a.cod_publicacao,8,0) as cod_edicao,

		      lpad(trim(a.edicao_capa),4,'0') as edicao_capa,

		      lpad(a.cod_barra,18,0) as cod_barra,

		      rpad(trim(a.desc_public),30,' ') as desc_public,

		      lpad(a.reparte,8,0) as reparte,

		      rpad(trim(d.nome_editor),35,' ') as nome_editor,

		      replace(to_char(lpad(trunc(vr_venda,2),11,0)),'.','') as vr_venda,

		      replace(to_char(lpad(trunc(vr_custo,2),11,0)),'.','') as vr_custo,

		      rpad(trim(a.rep_capa),30,' ') as rep_capa,

		      replace(a.dt_mov,'/','') as dat_mov,

		      lpad(a.devol,8,0) as devol 
		      */
		      
		     
		     
		        		
		      StringBuffer mensagem = new StringBuffer();
		      
		      String codigoDistribuidor = distribuidorService.obter().getCodigoDistribuidorDinap();
		      if ( codigoDistribuidor == null || codigoDistribuidor.trim().length() == 0 || "0".equals(codigoDistribuidor))
		    	  codigoDistribuidor = distribuidorService.obter().getCodigoDistribuidorFC();
		     
		      
		      for ( Integer cotaMaster: cotasMaster.keySet()) {
		    	  String path  = dirBanca+"/caruso/"+String.format("%05d",cotaMaster)+DateUtil.formatarData(data,"ddMMyyyy")+".ENP";
		    	  LOGGER.warn("GERANDO COTAS CARUSO EM ARQUIVO"+path);
		    	  int cont=0;
		    	  PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, false)));
		    	  // pesquisar reparte do master
		    	  //out.println("01107573500090109901099010902092015000692892015000007897823436443GUIA ARTE COM MAOS CUP CAKE   00000002CASA DOIS COMUNICACAO LTDA         0000001999000000139930 MODELOS PASSO A PASSO      0209201500000002");
		    	   Cota cotam = cotaService.obterPorNumeroDaCota(cotaMaster);
		    	   if ( cotam == null ) {
		    		   LOGGER.error("Cota master "+cotaMaster+"  cadastrada em conttrole_cota nao e' valida");
		    	   continue;
		    	   }
	                            
		    	//    String email = cotam.getEnderecoPrincipal().getEndereco().getPessoa().getEmail();
		    	 //   if ( email == null )
		    	  // String 	email = cotam.getPDVPrincipal().getEmail();
		    	 //   if ( email == null )
		    	   String 	email = "odemir.olivatti@gmail.com";
		    	  // pesquisar reparte das cotas
		    	  for (Integer cota : cotasMaster.get(cotaMaster)) {
		    		  
		    		  Cota cotac = cotaService.obterPorNumeroDaCota(cota);
			    	   if ( cotac == null ) {
			    		   LOGGER.error("Cota  "+cota+"  cadastrada em conttrole_cota nao e' valida");
			    	   continue;
			    	   }
		    		  FiltroConsolidadoEncalheCotaDTO filtro = new  FiltroConsolidadoEncalheCotaDTO();
		    		  filtro.setDataConsolidado(data);
		    		  filtro.setNumeroCota(cota.intValue());
		    		        
		    		  List<EncalheCotaDTO> listaEncalheCota = consolidadoFinanceiroService.obterMovimentoEstoqueCotaEncalhe(filtro);
		    		 
		    		  Map <String,EncalheCotaDTO> mapEncalheCota = new HashMap();
		    		  
		    		  for(EncalheCotaDTO encalhe : listaEncalheCota)
		    			  mapEncalheCota.put(encalhe.getCodigoProduto()+encalhe.getNumeroEdicao(), encalhe);
		    		 
		    		  FiltroConsolidadoConsignadoCotaDTO filtroc = new FiltroConsolidadoConsignadoCotaDTO();
		    		  filtroc.setDataConsolidado(data);
		    		  filtroc.setNumeroCota(cota.intValue());
		    		 
		    		  
		    		  List<ConsignadoCotaDTO> listaConsignadoCota = consolidadoFinanceiroService.obterMovimentoEstoqueCotaConsignado(filtroc);
		    		    
		    		  if ( listaConsignadoCota == null ) {
		    		    LOGGER.error("COTA "+cota+" cadastrado em controle_cota nao existe na base de dados");
		    		    mensagem.append("Cota numero "+cota.intValue()+" nao encontrada.Corrigir controle_cota</br>");
		    		  } else
		    		  for(ConsignadoCotaDTO consignado:listaConsignadoCota ) {
			    		  StringBuffer sb = new StringBuffer();
			    		  
			    		  EncalheCotaDTO encalhe  = mapEncalheCota.get(consignado.getCodigoProduto()+consignado.getNumeroEdicao());
			    		  
			    		  sb.append("011"+String.format("%07d", Integer.parseInt(codigoDistribuidor)));
			    		  
			    		  Integer codigoJornaleiro=cota;
			    		  sb.append(String.format("%07d",codigoJornaleiro));
			    		  sb.append(String.format("%05d",codigoJornaleiro));
			    		  sb.append(String.format("%05d",codigoJornaleiro));
			    		  
			    		  Date dtMov = data;
			    		  sb.append(DateUtil.formatarData(dtMov, "ddMMyyyy"));
			    		  
			    		  String codPublicacao=consignado.getCodigoProduto();
			    		  
			    		  
	                      sb.append(StringUtils.leftPad(codPublicacao,8,'0'));
	                      
					      String edicaoCapa=consignado.getNumeroEdicao().toString();
	                      sb.append(StringUtils.leftPad(edicaoCapa,4,'0'));
	                     
	                    
	                      
	                      String codBarra=  consignado.getCodigoBarras();
	                      
	                      if (codBarra == null )
	                    	  codBarra="0";
					      sb.append(StringUtils.leftPad(codBarra,18,'0'));
					    
					      String descPublic=consignado.getNomeProduto();
	                      sb.append(String.format("%-30s",descPublic).substring(0,30));
					     
					      Long reparte=consignado.getReparteFinal().longValue();
					      sb.append(String.format("%08d",reparte));
					      
					      FiltroConsultaCaracteristicaDistribuicaoSimplesDTO filtrocp = new FiltroConsultaCaracteristicaDistribuicaoSimplesDTO();
					      filtrocp.setCodigoProduto(consignado.getCodigoProduto());
					      List<CaracteristicaDistribuicaoSimplesDTO> cp= caracteristicaDistribuicaoService.buscarComFiltroSimples(filtrocp);
					      
					                
					      String nomeEditor=(cp != null ? cp.get(0).getNomeEditor(): consignado.getNomeEditor());//produto.getEditor().getPessoaJuridica().getNome();
					     
					      sb.append(String.format("%-35s",nomeEditor).substring(0,35));
					   
					      
	                      BigDecimal vrVenda =  consignado.getPrecoCapa();
	                      sb.append(String.format("%011.2f",vrVenda.floatValue()).replace(",", "").replace("\\.", ""));
	                      
	                      BigDecimal vrCusto = consignado.getPrecoComDesconto();
	                      sb.append(String.format("%011.2f",vrCusto.floatValue()).replace(",","").replace("\\.", ""));
	
	                     
	                      
	                     
					      String repCapa= consignado.getChamadaCapa();//produtoEdicao.getChamadaCapa()!= null ?produtoEdicao.getChamadaCapa():"" ;//"30 MODELOS PASSO A PASSO";
					    	
					      
					      sb.append(StringUtils.rightPad(repCapa,30,' ').substring(0,30));
					     
					     
				    	  sb.append(DateUtil.formatarData(dtMov, "ddMMyyyy"));
					     
	                      Long devol=encalhe != null ? encalhe.getEncalhe().longValue():0;
	                      
	                      sb.append(String.format("%08d",devol));
	                      cont++;
			    		  out.println(sb.toString());
		    		  }
		    	  }
		    	  out.close();
		    	  // enviar email
		    	  
		    	  LOGGER.warn("ENVIANDO  ARQUIVO CARUSO"+path +" para email" + email);
		    	  mensagem.append("GERADO ARQUIVO "+path+" com "+cont+" registros</br>");
		    	  /* comentando, enquanto email nao habilitado
		    	  AnexoEmail anexoEmail = new AnexoEmail();
		    	  anexoEmail.setNome(path);
		    	 
		    	  mensagem.append("Enviado email para  "+email+"</br>");
		       
		    		List<AnexoEmail> anexosEmail = new ArrayList<AnexoEmail>();
		    		
		    	
		    	
		             byte[] anexo =  java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(path));
		             session.putValue("PATH_VENDA",path);
		                
		    		
		    	    anexosEmail.add(new AnexoEmail(cotaMaster.toString()+DateUtil.formatarData(data,"ddMMyyyy"),anexo,TipoAnexo.ENP));
		         
		    	  this.emailService.enviar("Arquivo Unificado", 
							 "Segue arquivo em anexo.", 
							 new String[]{email}, 
							 anexosEmail);	
		        */
		      }
		      
		      
		     
			     
				
			     result.use(Results.json()).from("PROCESSAMENTO OK</br>"+mensagem.toString() , "result").serialize();
		      
		     
		    }
		    catch (Exception err) {
		      err.printStackTrace();
		      result.use(Results.json()).from("Erro gerando arquivo agrupado de vendas de cotas</br>.Erro:"+ err , "result").serialize();
		    }
	
	}
	
	
	

	@Post
	@Rules(Permissao.ROLE_ADMINISTRACAO_GERACAO_ARQUIVO_ALTERACAO)
	public void unificaror() {	
		  String crlf = System.getProperty("line.separator");
	      String dirBanca = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO).getValor();
	  
	      String dirFc = dirBanca+"/fc";
	     
	      String dirDinap = dirBanca+"/dinap";
	      String dirOut = dirBanca+"/dinap_fc";
	      String dirConflito = dirBanca+"/dinap_fc_conflito";
	      int conflitos=0;
	   try {   
		   
		 
	      // copiar dinap para saida
	      File source = new File(dirDinap);
	      File dest = new File(dirOut);
	      try {
	          FileUtils.copyDirectory(source, dest);
	      } catch (IOException e) {
	          e.printStackTrace();
	          result.use(Results.json()).from("</br>Erro copiando arquivos de .."+dirDinap+"  Para "+dirOut+" erro:"+e.getMessage() , "result").serialize();
    		  return;
	      }

	     // processar
	      
	      StringBuffer ret=new StringBuffer();
	      File dir = new File(dirFc);
		  String[] extensions = new String[] { "LCT", "RCL" };
			
			List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
			List <File> files_semdepara = new <File> ArrayList();
			for (File file : files) {
				String fc=file.getName();
				String boxfc = fc.split("[.]")[1];
				String boxdinap = deparaService.obterBoxDinap(boxfc);	
				 if ( boxdinap == null || boxdinap.trim().length() == 0 ) {
				 //  ret.append( "Box fc="+boxfc+" nao tem box  dinap correspondente.Incluir box dinap para box fc="+boxfc +"na tabela depara</br>");
				   files_semdepara.add(file);
				   continue;
				 } 
				 // trocar box no arquivo fc para dinap
				  // e trocar filial fc para filial dinap
				  String filialFc = "0757350";
				  String filialDinap ="5318019";
				   String pathOut = dirOut +"/"+fc.replace(boxfc,boxdinap).replace(filialFc,filialDinap);
				   File arqOut = new File(pathOut);
				  
				   if  (arqOut.exists()) { // concatenar
					   String filestr_fc = FileUtils.readFileToString(file);
					   String filestr_dinap = FileUtils.readFileToString(arqOut);
					   
					   FileUtils.write(arqOut, filestr_dinap+filestr_fc); 
				   }
				   else {
					 FileUtils.copyFile(file, arqOut);
				     
				   }
			   }
			
			// processar arquivos fcs sem box dinap
			
			for (File file : files_semdepara) {
				String fc=file.getName();
				String boxfc = fc.split("[.]")[1];
				
				ret.append("Atencao:Box fc="+boxfc+" nao tem box dinap correspondente na tabela depara</br>");
				  
				
				   String pathOut = dirOut +"/"+fc;
				   File arqOut = new File(pathOut);
				  
				   if  (!arqOut.exists()) { // nao existe,cpiar para unificado
					   FileUtils.copyFile(file, arqOut);
				   } else {  //copiar para conflito
					   conflitos++;
					   ret.append("Conflito:Arquivo fc "+file.getAbsolutePath()+"  existe no dinap mas nao tem o box correspondente.Copiado para "+dirConflito+"/"+fc +"</br>" );
					 FileUtils.copyFile(file, new File(dirConflito+"/"+fc));
				   } 
				  
			   }
			
			ret.append("Quantidade de arquivos fc unificados com dinap :"+files.size()+"</br>");
			ret.append("Sem box dinap="+files_semdepara.size()+ " Conflitos="+conflitos+"</br>");
	     
			
		     result.use(Results.json()).from(ret.toString() , "result").serialize();
		    }
		    catch (Exception err) {
		      err.printStackTrace();
		      String erro="";
		      if ( !new File( dirBanca+"/fc").exists())
		    	  erro+=dirBanca+"/fc  Nao existe";
		      if ( !new File( dirBanca+"/dinap").exists())
		    	  erro+=dirBanca+"/dinap Nao existe";
		     if ( !new File( dirBanca+"/dinap_fc").exists())
		    	erro+=dirBanca+"/dinap_fc  Nao existe";
			 if ( !new File( dirBanca+"/dinap_fc_conflito").exists())
		    	erro+=dirBanca+"/dinap_fc_conflito Nao existe";

		      result.use(Results.json()).from("Erro executando Unificacao</br>"+err.getMessage() +"</br>"+erro, "result").serialize();
		    }
	
	}

	public void limparDir(String dir) {
		Iterator it= FileUtils.iterateFiles(new File(dir) , new String[] {"LCT","RCL"},false);
					while (it.hasNext()) {
						File file = (File) it.next();
						file.delete();
		
					}
	}
	
	@Post
	@Rules(Permissao.ROLE_ADMINISTRACAO_GERACAO_ARQUIVO_ALTERACAO)
	public void unificar(Date dataLctoPrevisto, String operacao,String nomeArquivo) {	
		session.removeAttribute("PATH_VENDA");
		 if ( nomeArquivo == null ) {
			  throw new ValidacaoException(TipoMensagem.WARNING, "Escolher o arquivo Dinap.");
		  }
		  String crlf = System.getProperty("line.separator");
	      String dirBanca = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO).getValor();
	  
	      String dirFc = dirBanca+"/fc";
	     
	      String dirDinap = dirBanca+"/dinap";
	      String dirOut = dirBanca+"/dinap_fc";
	      String dirConflito = dirBanca+"/dinap_fc_conflito";
	      int conflitos=0;
	   try {   
		   // pegar arquivoz zip dinap
		   StringBuffer ret=new StringBuffer();
		   
		   String dinapName = nomeArquivo;
		   File zipDinap = new File(dirDinap+"/"+nomeArquivo);
		 
		   ret.append("Unificando arquivo dinap "+zipDinap.getPath()+"</br>");
		  //deletar RCL E LCT de diretorios
		   limparDir(dirDinap);
		   limparDir(dirFc);
		   limparDir(dirOut);
		
		 // descompactar dinap e fc
		  
		   descompactar(zipDinap.getPath(),dirDinap);
		  
		 
		   // obter data encalhe  e descompactar zip encalhe RCL
		   FilenameFilter fileFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					
					if (name.toUpperCase().endsWith(".RCL") || name.toUpperCase().endsWith(".LCT") ) {
						return true;
					} else {
						return false;
					}
				}
			};
			
			String rcl=null;
			String lct=null;
			int reparte=0;
			int encalhe=0;
			for(File input : new File(dirDinap).listFiles(fileFilter)) {
				
				if(input.isDirectory()) {
					continue;
				}
				if ( input.getName().endsWith(".RCL")) {
					rcl = input.getName();
					encalhe++;
				}
				if ( input.getName().endsWith(".LCT")) {
					reparte++;
					lct = input.getName();
				}
				
			}
		   
		   if ( lct == null && rcl == null ) {
				  throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo Dinap "+dinapName+" nao possui nem reparte nem encalhe");
					
		   }
		   ret.append("Quantidade de arquivos  reparte LCT DINAP  "+reparte+"</br>");
		   ret.append("Quantidade de arquivos  encalhe RCL DINAP  "+encalhe+"</br>");
		   
		   if ( lct == null ) {
			   ret.append("ARQUIVO zip dinap nao possui Reparte LCT ");
		   } else {
			   String zipReparteFc=dirBanca+"/fc/reparte-"+lct.split("\\.")[2]+".zip";
			   ret.append("Unificando arquivo reparte fc  "+zipReparteFc+"</br>");
			   if (new File(zipReparteFc).exists()) {
			   int cont=descompactar(zipReparteFc,dirFc);
			   ret.append("Quantidade de arquivos  reparte fc  "+cont+"</br>");
			   } else
				   ret.append("ATENCAO.. Arquivo reparte fc  nao encontrado "+zipReparteFc+"</br>");
		   }
		   if ( rcl == null ) {
			   ret.append("ARQUIVO zip dinap nao possui Encalhe RCL ");
		   } else {
		   String zipEncalheFc=dirBanca+"/fc/encalhe-"+rcl.split("\\.")[2]+".zip";
		   ret.append("Unificando arquivo encalhe fc  "+zipEncalheFc+"</br>");
		   if (new File(zipEncalheFc).exists()) {
		   int cont=descompactar(zipEncalheFc,dirFc);
		   ret.append("Quantidade de arquivos  encalhe fc  "+cont+"</br>");
		   } else
			   ret.append("ATENCAO.. Arquivo encalhe fc  nao encontrado "+zipEncalheFc+"</br>");
		   }
		   // obter data reparte e descompactar zip de reparte LCT
		  
		   
	      // copiar dinap para saida
	      File source = new File(dirDinap);
	      File dest = new File(dirOut);
	      try {
	          FileUtils.copyDirectory(source, dest);
	          
	      } catch (IOException e) {
	          e.printStackTrace();
	          result.use(Results.json()).from("</br>Erro copiando arquivos de .."+dirDinap+"  Para "+dirOut+" erro:"+e.getMessage() , "result").serialize();
    		  return;
	      }

	     // processar
	      
	     
	      File dir = new File(dirFc);
		  String[] extensions = new String[] { "LCT", "RCL" };
			
			List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
			List <File> files_semdepara = new <File> ArrayList();
			for (File file : files) {
				String fc=file.getName();
				String boxfc = fc.split("[.]")[1];
				String boxdinap = deparaService.obterBoxDinap(boxfc);	
				 if ( boxdinap == null || boxdinap.trim().length() == 0 ) {
				 //  ret.append( "Box fc="+boxfc+" nao tem box  dinap correspondente.Incluir box dinap para box fc="+boxfc +"na tabela depara</br>");
				   files_semdepara.add(file);
				   continue;
				 } 
				 // trocar box no arquivo fc para dinap
				  // e trocar filial fc para filial dinap
				  String filialFc = "0757350";
				  String filialDinap ="5318019";
				   String pathOut = dirOut +"/"+fc.replace(boxfc,boxdinap).replace(filialFc,filialDinap);
				   File arqOut = new File(pathOut);
				  
				   if  (arqOut.exists()) { // concatenar
					   String filestr_fc = FileUtils.readFileToString(file);
					   String filestr_dinap = FileUtils.readFileToString(arqOut);
					   
					   FileUtils.write(arqOut, filestr_dinap+filestr_fc); 
				   }
				   else {
					 FileUtils.copyFile(file, arqOut);
				     
				   }
			   }
			
			// processar arquivos fcs sem box dinap
			
			for (File file : files_semdepara) {
				String fc=file.getName();
				String boxfc = fc.split("[.]")[1];
				
				ret.append("Atencao:Box fc="+boxfc+" nao tem box dinap correspondente na tabela depara</br>");
				  
				
				   String pathOut = dirOut +"/"+fc;
				   File arqOut = new File(pathOut);
				  
				   if  (!arqOut.exists()) { // nao existe,cpiar para unificado
					   FileUtils.copyFile(file, arqOut);
				   } else {  //copiar para conflito
					   conflitos++;
					   ret.append("Conflito:Arquivo fc "+file.getAbsolutePath()+"  existe no dinap mas nao tem o box correspondente.Copiado para "+dirConflito+"/"+fc +"</br>" );
					 FileUtils.copyFile(file, new File(dirConflito+"/"+fc));
				   } 
				  
			   }
			
			ret.append("Quantidade de arquivos fc unificados com dinap :"+files.size()+"</br>");
			ret.append("Sem box dinap="+files_semdepara.size()+ " Conflitos="+conflitos+"</br>");
			String arqDinapFc="unificado_"+dinapName;
			compactarArquivos(dirOut,arqDinapFc);
			 //llimpar RCL E LCT de diretorios
			   limparDir(dirDinap);
			   limparDir(dirFc);
			   limparDir(dirOut);
			   
			ret.append("Compactado resultado para "+arqDinapFc+"</br>");
			 session.putValue("PATH_VENDA",dirOut+"/"+arqDinapFc);
		     result.use(Results.json()).from(ret.toString() , "result").serialize();
		     /*
		     InputStream isFile = new FileInputStream(new File(dirOut+"/"+arqDinapFc));  
				
				
				byte[] arquivo = IOUtils.toByteArray(isFile);
				httpServletResponse.setContentType("application/zip");
				httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + arqDinapFc);
		        
		        final OutputStream output = httpServletResponse.getOutputStream();
		        output.write(arquivo);
		        
		        httpServletResponse.flushBuffer();
		        */
		    }
		    catch (Exception err) {
		      err.printStackTrace();
		      String erro="";
		      if ( !new File( dirBanca+"/fc").exists())
		    	  erro+=dirBanca+"/fc  Nao existe";
		      if ( !new File( dirBanca+"/dinap").exists())
		    	  erro+=dirBanca+"/dinap Nao existe";
		     if ( !new File( dirBanca+"/dinap_fc").exists())
		    	erro+=dirBanca+"/dinap_fc  Nao existe";
			 if ( !new File( dirBanca+"/dinap_fc_conflito").exists())
		    	erro+=dirBanca+"/dinap_fc_conflito Nao existe";

		      result.use(Results.json()).from("Erro executando Unificacao</br>"+err.getMessage() +"</br>"+erro, "result").serialize();
		    }
	
	}
	
	@Post
	@Rules(Permissao.ROLE_ADMINISTRACAO_GERACAO_ARQUIVO_ALTERACAO)
	public void gerar(Date dataLctoPrevisto, String operacao,String nomeArquivo) {
		session.removeAttribute("PATH_VENDA");
		validarDataDeGeracao(dataLctoPrevisto);

		int qtdArquivosGerados = 0;
		
		if (operacao.equals("PICKING")) {
			
			String mensageValidacao = 
				route129.execute(getUsuarioLogado().getLogin(), dataLctoPrevisto, null);
			
			if (mensageValidacao != null) {
				
				throw new ValidacaoException(TipoMensagem.WARNING, mensageValidacao);
			}
			
			qtdArquivosGerados = 1;
			
		} else if (operacao.equals("REPARTE")) {
			
			qtdArquivosGerados = route197.execute(getUsuarioLogado().getLogin(), dataLctoPrevisto, null);
			if(qtdArquivosGerados > 0) {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				ParametroSistema ps = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO);
					
					File file = new File(ps.getValor() + File.separator +"reparte"+ File.separator +"zip"+ File.separator +"reparte-"+ sdf.format(dataLctoPrevisto) +".zip");
				try {
					String dirFc = ps.getValor()+ File.separator +"fc";
					FileUtils.copyFileToDirectory(file, new File(dirFc));
					
					InputStream isFile = new FileInputStream(file);  
					
					
					byte[] arquivo = IOUtils.toByteArray(isFile);
					httpServletResponse.setContentType("application/zip");
					httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + "reparte-"+ sdf.format(dataLctoPrevisto) +".zip");
			        
			        final OutputStream output = httpServletResponse.getOutputStream();
			        output.write(arquivo);
			        
			        httpServletResponse.flushBuffer();
					
				} catch (IOException e) {
					LOGGER.error("Erro ao gerar arquivo de reparte",e);
 					throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar o arquivo REPARTE.Tente outra ");
				}    
			}
			
		} else {
			
			qtdArquivosGerados = route198.execute(getUsuarioLogado().getLogin(), dataLctoPrevisto, null);
			if(qtdArquivosGerados > 0) {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				ParametroSistema ps = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO);
				
				File file = new File(ps.getValor() + File.separator +"encalhe"+ File.separator +"zip"+ File.separator +"encalhe-"+ sdf.format(dataLctoPrevisto) +".zip");
				try {
					String dirFc = ps.getValor()+ File.separator +"fc";
					FileUtils.copyFileToDirectory(file, new File(dirFc));
					InputStream isFile = new FileInputStream(file);    
					
					byte[] arquivo = IOUtils.toByteArray(isFile);
					httpServletResponse.setContentType("application/zip");
					httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + "encalhe-"+ sdf.format(dataLctoPrevisto) +".zip");
			        
			        final OutputStream output = httpServletResponse.getOutputStream();
			        output.write(arquivo);
			        
			        httpServletResponse.flushBuffer();
					
				} catch (IOException e) {
					LOGGER.error("Erro ao gerar arquivo de encalhe",e);
					throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar o arquivo ENCALHE."+e);
				}    
			}
		}
		
		result.use(Results.json()).from(Integer.valueOf(qtdArquivosGerados), "result").serialize();
	}

	private void validarDataDeGeracao(Date dataLctoPrevisto) {
		Date dataOperacaoSistema = distribuidorService.obterDataOperacaoDistribuidor(); 
		
		if(DateUtil.isDataInicialMaiorDataFinal(dataOperacaoSistema, dataLctoPrevisto)){
			throw new ValidacaoException(TipoMensagem.WARNING, "A data de geração não pode ser anterior a data de operação do sistema.");
		}
	}
	
	@Post
	public void alterarDataCalendario(String tipoArquivo) {
		
		Date dataOperacaoSistema = distribuidorService.obterDataOperacaoDistribuidor();
		String data;
		
		if(tipoArquivo.equalsIgnoreCase("REPARTE")){
			data = DateUtil.formatarDataPTBR(calendarioService.adicionarDiasUteis(dataOperacaoSistema, 1));
		}else{
			data = DateUtil.formatarDataPTBR(calendarioService.adicionarDiasUteis(dataOperacaoSistema, 2));
		}
			
		result.use(Results.json()).from(data, "data").recursive().serialize();
	}
	
	
	
	private void compactarArquivos(String dir,String arquivo) {
		
		
		File diretorio = new File(dir); 
		FileOutputStream fos = null;
		ZipOutputStream zipOut = null;
		FileInputStream fis = null;
		try {
			
			fos = new FileOutputStream(dir
						+ File.separator +arquivo);
			zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
			FilenameFilter fileFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					
					if (name.toUpperCase().endsWith(".RCL") || name.toUpperCase().endsWith(".LCT") ) {
						return true;
					} else {
						return false;
					}
				}
			};

			for(File input : diretorio.listFiles(fileFilter)) {
				
				if(input.isDirectory()) {
					continue;
				}
				
				fis = new FileInputStream(input);
				ZipEntry ze = new ZipEntry(input.getName());
			
				zipOut.putNextEntry(ze);
				byte[] tmp = new byte[4 * 1024];
				int size = 0;
				while((size = fis.read(tmp)) != -1) {
					
					zipOut.write(tmp, 0, size);
				}
				zipOut.flush();
				fis.close();
			}
			
			zipOut.close();
			
			for(File input : diretorio.listFiles(fileFilter)) {
				
				if(input.isDirectory()) {
					continue;
				}
				
				input.delete();
			}
			
		} catch (FileNotFoundException e) {
			
			LOGGER.error("Falha ao obter arquivo.", e);
		} catch (IOException e) {
			
			LOGGER.error("IOException", e);
		} finally {
			try {
				
				if(fos != null) { 
					fos.close();
				}
			} catch(Exception ex) {

				LOGGER.error("Falha ao fechar arquivo.", ex);
			}
		}
	}
	
public int 	descompactar(String zipFile,String outputFolder) {

	     byte[] buffer = new byte[1024];
	     int cont=0;
	     try{
	    		
	    	//create output directory is not exists
	    	File folder = new File(outputFolder);
	    	if(!folder.exists()){
	    		folder.mkdir();
	    	}
	    		
	    	//get the zip file content
	    	ZipInputStream zis = 
	    		new ZipInputStream(new FileInputStream(zipFile));
	    	//get the zipped file list entry
	    	ZipEntry ze = zis.getNextEntry();
	    		
	    	while(ze!=null){
	    	   cont++;
	    	   String fileName = ze.getName();
	           File newFile = new File(outputFolder + File.separator + fileName);
	                
	          
	                
	            //create all non exists folders
	            //else you will hit FileNotFoundException for compressed folder
	            new File(newFile.getParent()).mkdirs();
	              
	            FileOutputStream fos = new FileOutputStream(newFile);             

	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	       		fos.write(buffer, 0, len);
	            }
	        		
	            fos.close();   
	            ze = zis.getNextEntry();
	    	}
	    	
	        zis.closeEntry();
	    	zis.close();
	    		
	    	
	    		
	    }catch(IOException ex){
	     
	       LOGGER.error("ERRO UNZIP ARQUIVO",ex);
	    }
	     
	     return cont;
	   }    

   public int descompactar (  String diretorio) {
	   return 0;
   }
   

}
