package br.com.abril.nds.controllers.administracao;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.commons.io.IOUtils;
import org.hibernate.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.ems0129.route.EMS0129Route;
import br.com.abril.nds.integracao.ems0197.route.EMS0197Route;
import br.com.abril.nds.integracao.ems0198.route.EMS0198Route;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.cadastro.Depara;
import br.com.abril.nds.dto.DeparaDTO;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.service.DeparaService;
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
	
	@Path("/")
	public void index() {
	}

	
	@Post
	@Rules(Permissao.ROLE_ADMINISTRACAO_GERACAO_ARQUIVO_ALTERACAO)
	public void unificaror() {	
		 String crlf = System.getProperty("line.separator");
	      String dir_banca = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO).getValor();
	      String arquivo_depara = "c:/tmp/depara.txt";
	      //String cmd ="cmd /c dir";
	      
	      // gerar depara
	     
	      List <DeparaDTO> rs= deparaService.buscarDepara();
	      
	      try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivo_depara), "utf-8"))) {
	    	    for (DeparaDTO dp:rs) {

	    	    	

 
	  	    	  writer.write((dp.getFc()==null|| dp.getFc().trim().length() == 0?"":"00000".substring(dp.getFc().length()) + dp.getFc())
	  	    			  +"|"+
	  	    			  (dp.getDinap()==null||dp.getDinap().trim().length() == 0 ?"":"00000".substring(dp.getDinap().length()) + dp.getDinap())+crlf);
	  	      }
	    	} catch (IOException exd) {
	    		  result.use(Results.json()).from("Erro gerando arquivo depara no diretorio.."+arquivo_depara+" erro:"+exd.getMessage() , "result").serialize();
	    		  return;
	    	}  

	      String cmd="/home/ubuntu/ipv/concatipv.sh "+arquivo_depara+" "+ dir_banca+File.separator+"fc "+dir_banca+File.separator+"dinap "+dir_banca+File.separator+"out";
	
		 try {
		      String line;
		           Process p = Runtime.getRuntime().exec(cmd);
		     
		      BufferedReader bri = new BufferedReader
		        (new InputStreamReader(p.getInputStream()));
		      BufferedReader bre = new BufferedReader
		        (new InputStreamReader(p.getErrorStream()));
		      StringBuffer ret=new StringBuffer();
		      while ((line = bri.readLine()) != null) {
		        ret.append(line.replaceAll("<", "").replaceAll(">", "")+"</br>"+crlf);
		      }
		      bri.close();
		      while ((line = bre.readLine()) != null) {
		    	  ret.append(line.replaceAll("<", "").replaceAll(">", "")+"</br>"+crlf);
		      }
		      bre.close();
		      p.waitFor();
		     System.out.println(ret.toString());
		      result.use(Results.json()).from("Arquivos unificados.."+ret.toString() , "result").serialize();
		    }
		    catch (Exception err) {
		      err.printStackTrace();
		      result.use(Results.json()).from("Erro executando Unificacao</br>"+crlf+"Comando:"+cmd+"</br>"+crlf+"Erro:"+ err , "result").serialize();
		    }
	
	}

	@Post
	@Rules(Permissao.ROLE_ADMINISTRACAO_GERACAO_ARQUIVO_ALTERACAO)
	public void unificar() {	
		  String crlf = System.getProperty("line.separator");
	      String dir_banca = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO).getValor();
	  
	      String dir_fc = dir_banca+"/fc";
	      String dir_dinap = dir_banca+"/dinap";
	      String dir_out = dir_banca+"/dinap_fc";
	      String dir_conflito = dir_banca+"/dinap_fc_conflito";
	      int conflitos=0;
	   try {   
	      // copiar dinap para saida
	      File source = new File(dir_dinap);
	      File dest = new File(dir_out);
	      try {
	          FileUtils.copyDirectory(source, dest);
	      } catch (IOException e) {
	          e.printStackTrace();
	          result.use(Results.json()).from("</br>Erro copiando arquivos de .."+dir_dinap+"  Para "+dir_out+" erro:"+e.getMessage() , "result").serialize();
    		  return;
	      }

	     // processar
	      
	      StringBuffer ret=new StringBuffer();
	      File dir = new File(dir_fc);
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
				   String path_out = dir_out +"/"+fc.replace(boxfc,boxdinap);
				   File arq_out = new File(path_out);
				  
				   if  (arq_out.exists()) { // concatenar
					   String filestr_fc = FileUtils.readFileToString(file);
					   String filestr_dinap = FileUtils.readFileToString(arq_out);
					   
					   FileUtils.write(arq_out, filestr_dinap+filestr_fc); 
				   }
				   else {
					 FileUtils.copyFile(file, arq_out);
				     
				   }
			   }
			
			// processar arquivos fcs sem box dinap
			
			for (File file : files_semdepara) {
				String fc=file.getName();
				String boxfc = fc.split("[.]")[1];
				
				ret.append("Atencao:Box fc="+boxfc+" nao tem box dinap correspondente na tabela depara</br>");
				  
				
				   String path_out = dir_out +"/"+fc;
				   File arq_out = new File(path_out);
				  
				   if  (!arq_out.exists()) { // nao existe,cpiar para unificado
					   FileUtils.copyFile(file, arq_out);
				   } else {  //copiar para conflito
					   conflitos++;
					   ret.append("Conflito:Arquivo fc "+file.getAbsolutePath()+"  existe no dinap mas nao tem o box correspondente.Copiado para "+dir_conflito+"/"+fc +"</br>" );
					 FileUtils.copyFile(file, new File(dir_conflito+"/"+fc));
				   } 
				  
			   }
			
			ret.append("Quantidade de arquivos fc unificados com dinap :"+files.size()+"</br>");
			ret.append("Sem box dinap="+files_semdepara.size()+ " Conflitos="+conflitos+"</br>");
	     
		     result.use(Results.json()).from(ret.toString() , "result").serialize();
		    }
		    catch (Exception err) {
		      err.printStackTrace();
		      String erro="";
		      if ( !new File( dir_banca+"/fc").exists())
		    	  erro+=dir_banca+"/fc  Nao existe";
		      if ( !new File( dir_banca+"/dinap").exists())
		    	  erro+=dir_banca+"/dinap Nao existe";
		     if ( !new File( dir_banca+"/dinap_fc").exists())
		    	erro+=dir_banca+"/dinap_fc  Nao existe";
			 if ( !new File( dir_banca+"/dinap_fc_conflito").exists())
		    	erro+=dir_banca+"/dinap_fc_conflito Nao existe";

		      result.use(Results.json()).from("Erro executando Unificacao</br>"+err.getMessage() +"</br>"+erro, "result").serialize();
		    }
	
	}

	
	@Post
	@Rules(Permissao.ROLE_ADMINISTRACAO_GERACAO_ARQUIVO_ALTERACAO)
	public void gerar(Date dataLctoPrevisto, String operacao) {
		
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
				
				SimpleDateFormat sdf = new SimpleDateFormat("Y-MM-dd");
				ParametroSistema ps = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO);
				File file = new File(ps.getValor() + File.separator +"reparte"+ File.separator +"zip"+ File.separator +"reparte-"+ sdf.format(dataLctoPrevisto) +".zip");
				try {
					InputStream isFile = new FileInputStream(file);    
					
					byte[] arquivo = IOUtils.toByteArray(isFile);
					httpServletResponse.setContentType("application/zip");
					httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + "reparte-"+ sdf.format(dataLctoPrevisto) +".zip");
			        
			        final OutputStream output = httpServletResponse.getOutputStream();
			        output.write(arquivo);
			        
			        httpServletResponse.flushBuffer();
					
				} catch (IOException e) {
					
					throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar o arquivo REPARTE.");
				}    
			}
			
		} else {
			
			qtdArquivosGerados = route198.execute(getUsuarioLogado().getLogin(), dataLctoPrevisto, null);
			if(qtdArquivosGerados > 0) {
				
				SimpleDateFormat sdf = new SimpleDateFormat("Y-MM-dd");
				ParametroSistema ps = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO);
				File file = new File(ps.getValor() + File.separator +"encalhe"+ File.separator +"zip"+ File.separator +"encalhe-"+ sdf.format(dataLctoPrevisto) +".zip");
				try {
					InputStream isFile = new FileInputStream(file);    
					
					byte[] arquivo = IOUtils.toByteArray(isFile);
					httpServletResponse.setContentType("application/zip");
					httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + "encalhe-"+ sdf.format(dataLctoPrevisto) +".zip");
			        
			        final OutputStream output = httpServletResponse.getOutputStream();
			        output.write(arquivo);
			        
			        httpServletResponse.flushBuffer();
					
				} catch (IOException e) {
					
					throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar o arquivo ENCALHE.");
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

}
