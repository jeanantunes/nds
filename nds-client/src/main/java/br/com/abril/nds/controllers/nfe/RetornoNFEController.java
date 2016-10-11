package br.com.abril.nds.controllers.nfe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.NFEImportUtil;
import br.com.abril.nds.client.vo.SumarizacaoNotaRetornoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.nota.StatusRetornado;
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro01;
import br.com.abril.nds.model.ftf.retorno.FTFRetornoRET;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.FTFService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.NFeService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.TipoMovimentoFinanceiroService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.FileImportUtil;
import br.com.abril.nds.util.PDFUtil;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * 
 * Classe responsável por controlar as ações da pagina Retorno NFe.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path(value="/nfe/retornoNFe")
@Rules(Permissao.ROLE_NFE_RETORNO_NFE)
public class RetornoNFEController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
	@Autowired
	private NotaFiscalService notaFiscalService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private FTFService ftfService;
	
	@Autowired
	private TipoMovimentoFinanceiroService tipoMovimentoFinanceiroService;
	
	@Autowired
	private DebitoCreditoCotaService debitoCreditoCotaService;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private DocumentoCobrancaService documentoCobrancaService;
	
	@Autowired
	private NFeService nfeService; 
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	private static final String LISTA_NOTAS_DE_RETORNO = "listaNotasDeRetorno";
	
	private static final String NFE_APLICATIVO_FTF = "EMISSAO_NFE_APLICATIVO_CONTRIBUINTE";
	
	private static final String EXISTE_NFE = "EXISTE_NFE";
	
	@Path("/")
	@Rules(Permissao.ROLE_NFE_RETORNO_NFE)
	public void index() {
		ParametroSistemaGeralDTO parametroSistema = parametroSistemaService.buscarParametroSistemaGeral();
		
		result.include("tipoEmissor", parametroSistema.getNfeInformacoesTipoEmissor());
	}

	@Post("/obterTipoEmissor")
    public void obterTipoEmissor() {
        ParametroSistemaGeralDTO parametroSistema = parametroSistemaService.buscarParametroSistemaGeral();
        
        result.use(Results.json()).from(parametroSistema.getNfeInformacoesTipoEmissor()).serialize();
    }
	
	
	@Post("/pesquisarArquivos.json")
	public void pesquisarArquivosDeRetorno(final Date dataReferencia, final String tipoRetorno, String tipoEmissor) {
		
		List<File> listaNotas = null;
		List<RetornoNFEDTO> listaNotasRetorno = null;
		
		listaNotas = this.retornarListaNotas(dataReferencia, tipoRetorno, tipoEmissor, listaNotas);
			
		listaNotasRetorno = this.listaNotasFiscais(tipoRetorno, listaNotas, tipoEmissor);
		
		this.session.setAttribute(LISTA_NOTAS_DE_RETORNO, listaNotasRetorno);
		
		List<SumarizacaoNotaRetornoVO> sumarizacoes = new ArrayList<SumarizacaoNotaRetornoVO>();
		SumarizacaoNotaRetornoVO sumarizacao = this.sumarizarNotasRetorno(listaNotasRetorno);
		sumarizacoes.add(sumarizacao);
		this.result.use(FlexiGridJson.class).from(sumarizacoes).page(1).total(sumarizacoes.size()).serialize();
				
	}

	private List<File> retornarListaNotas(final Date dataReferencia, String tipoRetorno, String tipoEmissor, List<File> listaNotas) {
		
		ParametroSistema pathNFEImportacao = null;
		
		if(tipoEmissor.equals(NFE_APLICATIVO_FTF)) {
			pathNFEImportacao = this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTACAO_FTF);
		} else {			
			pathNFEImportacao = this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTACAO);
		}
		
		try {
			
			if(tipoEmissor.equals(NFE_APLICATIVO_FTF)) {
				listaNotas = FileImportUtil.importArquivosModificadosEm(pathNFEImportacao.getValor(), dataReferencia, FileType.RET);
			} else {						
				listaNotas = FileImportUtil.importArquivosModificadosEm(pathNFEImportacao.getValor(), dataReferencia, FileType.XML);
			}
			
		
		} catch (FileNotFoundException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O diretório parametrizado não é válido");
		}
		
		if (listaNotas == null || listaNotas.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foi encontrado nenhum item para o retorno de nota");
		}
		return listaNotas;
	}

	private List<RetornoNFEDTO> listaNotasFiscais(final String tipoRetorno, List<File> listaNotas, String tipoEmissor) {
		
		List<RetornoNFEDTO> listaNotasRetorno = null;
		
		if(tipoEmissor.equals(NFE_APLICATIVO_FTF)) {
			listaNotasRetorno = this.ftfService.processarRetornoFTF(this.gerarParseListaFTFRetorno(listaNotas, tipoRetorno));
		} else {
			listaNotasRetorno = this.notaFiscalService.processarRetornoNotaFiscal(this.gerarParseListaNotasRetorno(listaNotas, tipoRetorno));
		}
		
		return listaNotasRetorno;
	}
	
	
	@SuppressWarnings("unchecked")
	@Post("/confirmar.json")
	@Rules(Permissao.ROLE_NFE_RETORNO_NFE_ALTERACAO)
	public void confirmar() {
		
		final List<RetornoNFEDTO> listaNotasRetorno = (List<RetornoNFEDTO>) this.session.getAttribute(LISTA_NOTAS_DE_RETORNO);
		
		if (listaNotasRetorno == null || listaNotasRetorno.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não existem notas de retorno para serem atualizadas"));
		}
		
		for (final RetornoNFEDTO notaRetorno : listaNotasRetorno) {
			
			switch (notaRetorno.getStatus()) {
			
			case AUTORIZADO:
				this.notaFiscalService.autorizarNotaFiscal(notaRetorno);
				continue;
			
			case CANCELAMENTO_HOMOLOGADO:
				this.notaFiscalService.cancelarNotaFiscal(notaRetorno);
				continue;
			
			case USO_DENEGADO:
				this.notaFiscalService.denegarNotaFiscal(notaRetorno);
				continue;
			
			default:
				continue;
			}
		}
		
		this.limparSessao();
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Retorno efetuado com sucesso."), "result").recursive().serialize();
	}
	
	/**
	 * limpa atributos da sessão
	 */
	private void limparSessao() {
		session.removeAttribute(LISTA_NOTAS_DE_RETORNO);
	}
	
	/**
	 * Sumariza as notas de retorno, informando total de notas,  notas aprovadas e notas rejeitadas
	 * 
	 * @param listaNotasRetorno
	 * @return objeto de sumarizacao
	 */
	private SumarizacaoNotaRetornoVO sumarizarNotasRetorno(final List<RetornoNFEDTO> listaNotasRetorno) {
		
		Long totalArquivos = (long) listaNotasRetorno.size();
		Long notasRejeitadas = 0L;
		Long notasAprovadas = 0L;
		
		for (RetornoNFEDTO nota : listaNotasRetorno) {
			
			if(StatusRetornado.AUTORIZADO.equals(nota.getStatus())) {
				notasAprovadas ++;
			} else {
				notasRejeitadas ++;
			}
		}
		
		SumarizacaoNotaRetornoVO sumarizacao = new SumarizacaoNotaRetornoVO();
		
		sumarizacao.setNumeroTotalArquivos(totalArquivos);
		sumarizacao.setNumeroNotasRejeitadas(notasRejeitadas);
		sumarizacao.setNumeroNotasAprovadas(notasAprovadas);
		
		return sumarizacao;
	}
	
	/**
	 * Gera lista de notas de retorno
	 * 
	 * @param listaNotas path das notas dentro do diretório
	 * @return lista de notas
	 */
	private List<RetornoNFEDTO> gerarParseListaNotasRetorno(final List<File> arquivosNotas, String tipoRetorno) {
		
		final HashMap<String, RetornoNFEDTO> hashNotasRetorno = new HashMap<String, RetornoNFEDTO>();
		final List<RetornoNFEDTO> listaNotas = new ArrayList<RetornoNFEDTO>();
		RetornoNFEDTO arquivoAux = null;
		for (final File nota : arquivosNotas) {
			
			try {
				String schemaPath = this.getClass().getClassLoader().getResource("").getPath();
				
				RetornoNFEDTO arquivoRetorno = NFEImportUtil.processarArquivoRetorno(nota, schemaPath, tipoRetorno);
			
				if (arquivoAux == null) {
			
					arquivoAux = arquivoRetorno;
							
				} 
				
				if (arquivoAux.getChaveAcesso().equals(arquivoRetorno.getChaveAcesso()) && (arquivoRetorno.getTpEvento() != null && arquivoRetorno.getTpEvento().equals("110111"))) {
				
					arquivoRetorno = this.processarNotaCancelamento(arquivoAux, arquivoRetorno);
				}
				arquivoRetorno.setFtf(false);			
				hashNotasRetorno.put(arquivoRetorno.getChaveAcesso(), arquivoRetorno);
			
			} catch (Exception e) { 
				continue;
			}
		}
		
		listaNotas.addAll(hashNotasRetorno.values());
		
		return listaNotas;
	}
	
	/**
	 * Gera lista de notas de retorno
	 * 
	 * @param listaNotas path das notas dentro do diretório
	 * @return lista de notas
	 */
	private List<RetornoNFEDTO> gerarParseListaFTFRetorno(final List<File> arquivosNotas, String tipoRetorno) {
		
		final HashMap<String, RetornoNFEDTO> hashNotasRetorno = new HashMap<String, RetornoNFEDTO>();
		
		final List<RetornoNFEDTO> listaNotas = new ArrayList<RetornoNFEDTO>();
		
		List<FTFRetornoRET> retornoFTF = this.ftfService.processarArquivosRet(arquivosNotas);
		
		for (FTFRetornoRET ftf : retornoFTF) {
			
			for(FTFRetTipoRegistro01 tipoRegistro : ftf.getTipo01List()){
				final RetornoNFEDTO retornoNFEDTO = new RetornoNFEDTO();
				
				retornoNFEDTO.setIdNota(Long.valueOf(tipoRegistro.getNumeroDocumentoOrigem()));
				retornoNFEDTO.setNumeroNotaFiscal(Long.valueOf(tipoRegistro.getNumeroDocumentoOrigem()));
	    		retornoNFEDTO.setCpfCnpj(tipoRegistro.getCnpjCpfDestinatario());
	    		retornoNFEDTO.setChaveAcesso(tipoRegistro.getChaveAcessoNFe());
	    		retornoNFEDTO.setProtocolo(Long.valueOf(tipoRegistro.getNumeroProtocoloAutorizacao().trim()));
	    		retornoNFEDTO.setSerie(tipoRegistro.getSerieNFe());
	    		retornoNFEDTO.setNumeroNotaFiscal(Long.valueOf(tipoRegistro.getNumeroNFe()));
	    		
	    		try {
					retornoNFEDTO.setDataRecebimento(Util.formataData(tipoRegistro.getDataEmissaoNFe()));
				} catch (Exception e) {
					e.printStackTrace();
				}
	    		
	    		retornoNFEDTO.setMotivo("ARQUIVO " +" / " + "Retornado pelo sistema FTF");
	    		retornoNFEDTO.setTpEvento(tipoRegistro.getTipoRegistro());
	    		retornoNFEDTO.setFtf(true);
	    		StatusRetornado statusRetornado = null; 
	    		switch (tipoRegistro.getStatusNFe().trim()) {
				
	    		case "NF-e AUTORIZADA":
					
					statusRetornado = StatusRetornado.AUTORIZADO; 
					
					break;
				
				case "NF-e CANCELADA":
					
					statusRetornado = StatusRetornado.CANCELAMENTO_HOMOLOGADO; 
					
					break;	

				case "NF-e DENEGADA":
					
					statusRetornado = StatusRetornado.USO_DENEGADO; 
					
					break;
				
				default:
					break;
				}
	    		
	    		retornoNFEDTO.setStatus(statusRetornado);
	    		
	    		hashNotasRetorno.put(retornoNFEDTO.getChaveAcesso(), retornoNFEDTO);
			}
		}
		
		listaNotas.addAll(hashNotasRetorno.values());
		
		return listaNotas;
	}
	
	/**
	 * Retorna um objeto com merge dos dois arquivos passados por paramêtro;
	 * 
	 * Obs: uma nota de cancelamento gerada pelo emissor é composta por 2 arquivos distintos.
	 * Um arquivo contém as informações de Id Nota Fiscal e Chave de Acesso.
	 * Outro arquivo contém as demais informações.
	 * 
	 * @param arquivo01
	 * @param arquivo02
	 * @return Objeto com os dados dos arquivos
	 */
	private RetornoNFEDTO processarNotaCancelamento(final RetornoNFEDTO arquivo01, final RetornoNFEDTO arquivo02) {
		
		final RetornoNFEDTO notaCancelamentoMerged = new RetornoNFEDTO();
		notaCancelamentoMerged.setChaveAcesso(arquivo01.getChaveAcesso());
		notaCancelamentoMerged.setNumeroNotaFiscal(arquivo01.getNumeroNotaFiscal() != null ? arquivo01.getNumeroNotaFiscal() : arquivo02.getNumeroNotaFiscal());
		notaCancelamentoMerged.setDataRecebimento(arquivo01.getDataRecebimento() != null ? arquivo01.getDataRecebimento() : arquivo02.getDataRecebimento());
		notaCancelamentoMerged.setMotivo(StringUtil.isEmpty(arquivo01.getMotivo()) ? arquivo02.getMotivo() : arquivo01.getMotivo());
		notaCancelamentoMerged.setCpfCnpj(StringUtil.isEmpty(arquivo01.getCpfCnpj()) ? arquivo02.getCpfCnpj() : arquivo01.getCpfCnpj());
		notaCancelamentoMerged.setProtocolo(arquivo01.getProtocolo() != null ? arquivo01.getProtocolo() : arquivo02.getProtocolo());
		notaCancelamentoMerged.setStatus(arquivo01.getStatus() != null? arquivo01.getStatus() : arquivo02.getStatus());
		
		return notaCancelamentoMerged;
	}
	
	@Path("/imprimirBoleto")
	public void imprimirBoletoNFE(final Date dataReferencia) {
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = this.tipoMovimentoFinanceiroService.obterTipoMovimentoFincanceiroPorGrupoFinanceiroEOperacaoFinanceira(GrupoMovimentoFinaceiro.BOLETO_NFE, OperacaoFinaceira.DEBITO);
		
		// Obter Boleto Cota NFE
		List<DebitoCreditoDTO> listaBoletosAvulso = nfeService.listaBoletoNFE(dataReferencia);
		
		List<Long> idNotas = new ArrayList<Long>(); 
		
		if(listaBoletosAvulso != null && listaBoletosAvulso.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi encontrado nenhum registro para pesquisa.");
		}
		
		List<MovimentoFinanceiroCota> movimentosFinanceirosCota = new ArrayList<MovimentoFinanceiroCota>();
		
		for(DebitoCreditoDTO debitoCredito : listaBoletosAvulso) {
			
			debitoCredito.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
			
			debitoCredito.setIdUsuario(getUsuarioLogado().getId());
			
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            
			debitoCredito.setDataVencimento(f.format(dataReferencia));
			
			MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = debitoCreditoCotaService.gerarMovimentoFinanceiroCotaDTO(debitoCredito);
			
			movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
			
			movimentoFinanceiroCotaDTO.setObservacao(debitoCredito.getObservacao());
			
			movimentosFinanceirosCota.addAll(this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO));
			
			idNotas.add(debitoCredito.getIdNota());
		}
		
		Set<String> setNossoNumero = new HashSet<>();
		
		this.gerarCobrancaBoletoNFe(movimentosFinanceirosCota, setNossoNumero);
		
		for(Long idNota : idNotas) {
			this.nfeService.obterNFEPorID(idNota);
		}
		
		List<byte[]> docs = new ArrayList<byte[]>();
		
		byte[] arquivo = null;
		
		for(String s :  setNossoNumero) {
			docs.add(this.documentoCobrancaService.gerarDocumentoCobranca(s));
		}
		
		arquivo = PDFUtil.mergePDFs(docs);
		
		String nomeArquivo = "boleto-nota-fiscal-eletronica"; 
		
    	try {
			
    		this.httpResponse.setContentType("application/pdf");
    		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=" + nomeArquivo + new Date()+ ".pdf");

    		OutputStream output = this.httpResponse.getOutputStream();
    		output.write(arquivo);

    		this.httpResponse.getOutputStream().close();

    		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS," sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
	        
		} catch (Exception e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma informação encontrada para os filtros escolhidos.");
		}
	}
	
	@Path("/existeNotaNaData")
	public void existeNotaNaData(final Date dataReferencia) {
		
		final boolean existeNfe = nfeService.existeNotaNaData(dataReferencia);
		
		if(!existeNfe) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não tem nota para gerar boleto na data.");
		}
		
		this.result.use(CustomMapJson.class).put(EXISTE_NFE, existeNfe).serialize();
	}
	
	private void gerarCobrancaBoletoNFe(List<MovimentoFinanceiroCota> movimentosFinanceirosCota, Set<String> setNossoNumero) {
    	
    	for (MovimentoFinanceiroCota movimentoFinanceiroCota : movimentosFinanceirosCota) {
			
    		try {
				this.gerarCobrancaService.gerarCobrancaBoletoNFe(getUsuarioLogado().getId(), movimentoFinanceiroCota, setNossoNumero);
			} catch (GerarCobrancaValidacaoException e) {
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar cobrança por Boleto Avulso.");
			}
		}
    }
}