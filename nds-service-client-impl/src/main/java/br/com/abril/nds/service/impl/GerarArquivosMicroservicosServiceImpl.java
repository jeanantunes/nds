package br.com.abril.nds.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

import br.com.abril.nds.dto.ConsultaInterfacesDTO;
import br.com.abril.nds.dto.integracao.micro.Ems0106Deapr;
import br.com.abril.nds.dto.integracao.micro.Ems0107Deajo;
import br.com.abril.nds.dto.integracao.micro.Ems0108Matriz;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiretorioTransferenciaArquivo;
import br.com.abril.nds.model.integracao.EventoExecucao;
import br.com.abril.nds.model.integracao.InterfaceExecucao;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EventoExecucaoRepository;
import br.com.abril.nds.repository.LogExecucaoMensagemRepository;
import br.com.abril.nds.repository.LogExecucaoRepository;
import br.com.abril.nds.repository.TransferenciaArquivoRepository;
import br.com.abril.nds.service.GerarArquivosMicroDistribuicaoService;
import br.com.abril.nds.service.InterfaceExecucaoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;

@Service
public class GerarArquivosMicroservicosServiceImpl implements GerarArquivosMicroDistribuicaoService {
	
	private static final long ID_EVENTO_EXECUCAO_4L = 4L;

	private static final long ID_INTERFACE_108L = 108L;

	private static final String MATRIZ_NEW = "/MATRIZ.NEW";
	
	private static final String DEAPR19_NEW = "/deapr19.new";
	
	private static final String DEAJO19_NEW = "/deajo19.new";
	
	public static final long ID_DIRETORIO_MICROSERVICO = 6L;
	
	private static final String NOME_INTERFACE_EXECUCAO_MATRIZ = "EMS9001";

	private static final String NOME_INTERFACE_EXECUCAO_ESTUDO = "EMS9002";
	
	private static final String INTERFACE_REPROCESSADA_SUCESSO = "A interface %s foi reprocessada com sucesso, havia %d registros e a data selecionada era %s";
	
	private static final String EVENTO_INTERFACE_MICRO_DISTRIBUICAO = "Arquivo";
	
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private FixedFormatManager fixedFormatManager;
	
	@Autowired
	private TransferenciaArquivoRepository transferenciaArquivosRepository;
	
	@Autowired
	private LogExecucaoRepository logExecucaoRepository;
	
	@Autowired
	private EventoExecucaoRepository eventoExecucaoRepository; 
	
	@Autowired
	private LogExecucaoMensagemRepository logExecucaoMensagemRepository;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private InterfaceExecucaoService interfaceExecucaoService;
	
	
	@Transactional
	public void gerarArquivoMatriz(String caminhoArquivo, Date data, Usuario usuario, String nomeInterface) {
		
		List<Ems0108Matriz> dadosMatriz = lancamentoService.obterDadosMatriz(data);
		
		StringBuilder sb = new StringBuilder();
		
		String quebraLinha = System.getProperty("line.separator");
		 
		for (Ems0108Matriz ems0108Matriz : dadosMatriz) {
			sb.append(fixedFormatManager.export(ems0108Matriz) + quebraLinha);
		}
		
		
		
		try {
			
			String diretorio = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTEGRATION_ANALISTA).getValor();
			File file = new File(diretorio);
            file.mkdirs();
            File destino = new File(file, MATRIZ_NEW);
            
            Writer out = new BufferedWriter(new OutputStreamWriter(
            	    new FileOutputStream(destino.getAbsolutePath()), "UTF-8"));
            
            out.write(sb.toString());
            out.close();
            
            LogExecucao logExecucao=new LogExecucao();
            logExecucao.setStatus(StatusExecucaoEnum.SUCESSO);
            logExecucao.setDataInicio(new Date());
            InterfaceExecucao interfaceExecucao = logExecucaoRepository.findByNome(NOME_INTERFACE_EXECUCAO_MATRIZ);
            logExecucao.setInterfaceExecucao(interfaceExecucao);
            logExecucao.setNomeLoginUsuario(usuario.getNome());
            logExecucaoRepository.adicionar(logExecucao);
            
            logExecucaoMensagemRepository.inserir(criarLogExecucaoMensagem(logExecucao, interfaceExecucao.getNome(), dadosMatriz.size(),data, MATRIZ_NEW));
			
		} catch (FileNotFoundException e) {
			// TODO Add Msg de log
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Add Msg de log
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Add Msg de log
			e.printStackTrace();
		}
		
	}
	
	@Transactional
	private void inserirLogExecucao(Usuario usuario){
		 LogExecucao logExecucao=new LogExecucao();
         logExecucao.setStatus(StatusExecucaoEnum.SUCESSO);
         logExecucao.setDataInicio(new Date());
         InterfaceExecucao interfaceExecucao = logExecucaoRepository.findByNome(NOME_INTERFACE_EXECUCAO_MATRIZ);
         logExecucao.setInterfaceExecucao(interfaceExecucao);
         logExecucao.setNomeLoginUsuario(usuario.getNome());
         logExecucaoRepository.adicionar(logExecucao);
	}
	
	@Transactional
	private LogExecucaoMensagem criarLogExecucaoMensagem(LogExecucao logExecucao, String idInterface, Integer numeroRegistro, Date data, String nomeArquivo){
		EventoExecucao eventoExecucao = eventoExecucaoRepository.findByNome(EVENTO_INTERFACE_MICRO_DISTRIBUICAO);
		LogExecucaoMensagem logExecucaoMensagem = new LogExecucaoMensagem();
		logExecucaoMensagem.setLogExecucao(logExecucao);
		logExecucaoMensagem.setEventoExecucao(eventoExecucao);
		logExecucaoMensagem.setNomeArquivo(nomeArquivo.substring(1));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		logExecucaoMensagem.setMensagem(String.format(INTERFACE_REPROCESSADA_SUCESSO, idInterface,numeroRegistro,simpleDateFormat.format(data)));
		return logExecucaoMensagem;
	}
	

	@Transactional
	@Override
	public void gerarArquivoDeajo(Date data, Usuario usuario) {

		List<Ems0107Deajo> dadosDeajo = lancamentoService.obterDadosDeajo(data);
		
		StringBuffer sb = new StringBuffer();
		
		String quebraLinha = System.getProperty("line.separator");
		 
		for (Ems0107Deajo ems0107Deajo : dadosDeajo) {
			sb.append(fixedFormatManager.export(ems0107Deajo) + quebraLinha);
		}
		
		DiretorioTransferenciaArquivo parametroPath = transferenciaArquivosRepository.buscarPorId(ID_DIRETORIO_MICROSERVICO);
		
		
		try {
			String diretorio = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTEGRATION_ANALISTA).getValor();
			File file = new File(diretorio);
			file.mkdirs();
			File destino = new File(file, DEAJO19_NEW);
			Writer out = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(destino.getAbsolutePath()), "UTF-8"));
			
			out.write(sb.toString());
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Add Msg de log
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Add Msg de log
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Add Msg de log
			e.printStackTrace();
		}
		
	}

	@Transactional
	@Override
	public void gerarArquivoDeapr(Date data, Usuario usuario) {
		
		List<Ems0106Deapr> dadosDeapr = lancamentoService.obterDadosDeapr(data);
		
		StringBuffer sb = new StringBuffer();
		
		String quebraLinha = System.getProperty("line.separator");
		 
		for (Ems0106Deapr ems0106Deapr : dadosDeapr) {
			sb.append(fixedFormatManager.export(ems0106Deapr) + quebraLinha);
		}
		
		DiretorioTransferenciaArquivo parametroPath = transferenciaArquivosRepository.buscarPorId(ID_DIRETORIO_MICROSERVICO);
		
		
		try {
			
			String diretorio = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTEGRATION_ANALISTA).getValor();
			File file = new File(diretorio);
			file.mkdirs();
			File destino = new File(file, DEAPR19_NEW);
			Writer out = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(destino.getAbsolutePath()), "UTF-8"));
			
			out.write(sb.toString());
			out.close();
			 LogExecucao logExecucao=new LogExecucao();
	         logExecucao.setStatus(StatusExecucaoEnum.SUCESSO);
	         logExecucao.setDataInicio(new Date());
	         InterfaceExecucao interfaceExecucao = logExecucaoRepository.findByNome(NOME_INTERFACE_EXECUCAO_ESTUDO);
	         logExecucao.setInterfaceExecucao(interfaceExecucao);
	         logExecucao.setNomeLoginUsuario(usuario.getNome());
	         logExecucaoRepository.adicionar(logExecucao);
		} catch (FileNotFoundException e) {
			// TODO Add Msg de log
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Add Msg de log
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Add Msg de log
			e.printStackTrace();
		} 
		
	}
	
	@Override
	@Transactional
	public void processarMicrodistribuicao(String idInterface, Usuario usuario, String codDistribuidor) throws IOException {
		
		idInterface = "matriz";
		
		if (usuario != null) {
			

			//Definir diretório a depender do Tipo (Matriz, Deajo, Deapr).
			DiretorioTransferenciaArquivo parametroPath = transferenciaArquivosRepository
					.buscarPorId(ID_DIRETORIO_MICROSERVICO);

			LogExecucao logExecucao = getLogExecucao(usuario, codDistribuidor);
			
			//Listas para serem persistidas na base.
			//idInterface de exemplo, a ser definido ainda.
			if(idInterface.equals("matriz")) {
				List<Ems0108Matriz> listaMatriz = converteArquivoEmListaMatriz(parametroPath);
			}
			if(idInterface.equals("deajo")) {
				List<Ems0107Deajo> listaDeajo = converteArquivoEmListaDeajo(parametroPath);
			}
			if(idInterface.equals("deapr")) {
				List<Ems0106Deapr> listaDeapr = converteArquivoEmListaDeapr(parametroPath);
			}
			
			logExecucao.setDataFim(new Date());
			logExecucaoRepository.inserir(logExecucao );
		}

	}

	private LogExecucao getLogExecucao(Usuario usuario, String codDistribuidor) {
		InterfaceExecucao interfaceExecucao = new InterfaceExecucao();
		interfaceExecucao.setId(ID_INTERFACE_108L);
		LogExecucao logExecucao = new LogExecucao();
		logExecucao.setCodigoDistribuidor(codDistribuidor);
		logExecucao.setDataInicio(new Date());			
		logExecucao.setInterfaceExecucao(interfaceExecucao);
		logExecucao.setNomeLoginUsuario(usuario.getLogin());
		logExecucao.setStatus(StatusExecucaoEnum.VAZIO);
		logExecucao.setListLogExecucaoMensagem(listLogExecucaoMensagem());
		return logExecucao;
	}
	
	private List<LogExecucaoMensagem> listLogExecucaoMensagem() {
		
		List<LogExecucaoMensagem> listaMensagens = new ArrayList<LogExecucaoMensagem>(0);
		
		EventoExecucao eventoExecucao = new EventoExecucao();		
		eventoExecucao.setId(ID_EVENTO_EXECUCAO_4L);

		LogExecucaoMensagem logExecMensagem = new LogExecucaoMensagem();
		logExecMensagem.setEventoExecucao(eventoExecucao);
		logExecMensagem.setMensagem("Processamento de arquivo de micro distribuição.");
		logExecMensagem.setNomeArquivo(MATRIZ_NEW);
		logExecMensagem.setNumeroLinha(1);

		listaMensagens.add(logExecMensagem);
		
		return listaMensagens;
	}

	/**
	 * 
	 * @param nome 
	 */
	private List<Ems0108Matriz> converteArquivoEmListaMatriz(DiretorioTransferenciaArquivo diretorio) {
		
		List<Ems0108Matriz> listaEms0108 = new ArrayList<Ems0108Matriz>();

		try {
			FileReader arq = null;
			try {
				
				arq = obterArquivo(diretorio);

				// Leitura do arquivo
				carregarListaEms0108Matriz(listaEms0108, arq);
				
			} catch (FileNotFoundException e1) {
				
				e1.printStackTrace();
			} finally {
				
				if (arq != null) {
					arq.close();
				}
			}
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}

		return listaEms0108;
	}

	private void carregarListaEms0108Matriz(List<Ems0108Matriz> lista, FileReader arq) throws IOException {
		BufferedReader lerArq = new BufferedReader(arq);
		String linha = lerArq.readLine();
		
		while (linha != null) {

			Ems0108Matriz obj = fixedFormatManager.load(Ems0108Matriz.class, linha);
			linha = lerArq.readLine();
			lista.add(obj);

		}
		
		lerArq.close();
	}
	
	private void carregarListaEms0106Deapr(List<Ems0106Deapr> lista, FileReader arq) throws IOException {
		BufferedReader lerArq = new BufferedReader(arq);
		String linha = lerArq.readLine();
		
		while (linha != null) {

			Ems0106Deapr obj = fixedFormatManager.load(Ems0106Deapr.class, linha);
			linha = lerArq.readLine();
			lista.add(obj);

		}
		
		lerArq.close();
	}
	
	private void carregarListaEms0107Deajo(List<Ems0107Deajo> lista, FileReader arq) throws IOException {
		BufferedReader lerArq = new BufferedReader(arq);
		String linha = lerArq.readLine();
		
		while (linha != null) {

			Ems0107Deajo obj = fixedFormatManager.load(Ems0107Deajo.class, linha);
			linha = lerArq.readLine();
			lista.add(obj);

		}
		
		lerArq.close();
	}
	
	private List<Ems0106Deapr> converteArquivoEmListaDeapr(DiretorioTransferenciaArquivo diretorio) {
		
		List<Ems0106Deapr> listaEms0106 = new ArrayList<Ems0106Deapr>();
		
		try{
			FileReader arq = null;
			try {
				//arquivoPath = "/opt/ambiente4/parametros_nds/micro/entrada/matriz.new";
				arq = obterArquivo(diretorio);
				
				//Leitura do arquivo
				carregarListaEms0106Deapr(listaEms0106, arq);
			    
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			finally {
				if(arq != null) {
					arq.close();
				}
			}
      
		  } catch (IOException e) {
		      System.err.printf("Erro na abertura do arquivo: %s.\n",
		        e.getMessage());
		  }
		
        return listaEms0106;
	}
	
	private List<Ems0107Deajo> converteArquivoEmListaDeajo(DiretorioTransferenciaArquivo diretorio) {
		
		List<Ems0107Deajo> listaEms0107 = new ArrayList<Ems0107Deajo>();
		
		try{
			FileReader arq = null;
			try {
				//arquivoPath = "/opt/ambiente4/parametros_nds/micro/entrada/matriz.new";
				arq = obterArquivo(diretorio);
				
				//Leitura do arquivo
				carregarListaEms0107Deajo(listaEms0107, arq);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			finally {
				if(arq != null) {
					arq.close();
				}
			}
      
		  } catch (IOException e) {
		      System.err.printf("Erro na abertura do arquivo: %s.\n",
		        e.getMessage());
		  }
		
        return listaEms0107;
	}
	
	

	private FileReader obterArquivo(DiretorioTransferenciaArquivo diretorio) throws FileNotFoundException {
		FileReader arq;
		File diretorioFile = new File(diretorio.getEnderecoDiretorio());

		if (diretorioFile.listFiles() == null || diretorioFile.listFiles().length == 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há arquivos no diretório.");
		}

		File arquivo = null;
		for (File file : diretorioFile.listFiles()) {
			arquivo = file;
			break;
		}

		arq = new FileReader(arquivo);
		return arq;
	}
	
	
	private String gerarMensagem(String mensagem, String date, Integer totalRegistro){
		return String.format(mensagem, totalRegistro, date );
	}

	public void setParametroSistemaService(ParametroSistemaService parametroSistemaService) {
		this.parametroSistemaService = parametroSistemaService;
	}
	
}
