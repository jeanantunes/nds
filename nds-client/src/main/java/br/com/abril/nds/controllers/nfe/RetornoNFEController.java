package br.com.abril.nds.controllers.nfe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.NFEImportUtil;
import br.com.abril.nds.client.vo.SumarizacaoNotaRetornoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.fiscal.nota.StatusRetornado;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.FileImportUtil;
import br.com.abril.nds.util.StringUtil;
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
	
	private static final String LISTA_NOTAS_DE_RETORNO = "listaNotasDeRetorno";
	
	@Path("/")
	@Rules(Permissao.ROLE_NFE_RETORNO_NFE)
	public void index() {	
	}

	@Post("/pesquisarArquivos.json")
	public void pesquisarArquivosDeRetorno(final Date dataReferencia) {
		
		ParametroSistema pathNFEImportacao = this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTACAO);
		
		List<File> listaNotas = null;
		
		try {
			
			listaNotas = FileImportUtil.importArquivosModificadosEm(pathNFEImportacao.getValor(), dataReferencia, FileType.XML);
		
		} catch (FileNotFoundException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O diretório parametrizado não é válido");
		}
		
		if (listaNotas == null || listaNotas.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foi encontrado nenhum retorno de nota");
		}
			
		List<RetornoNFEDTO> listaNotasRetorno = this.notaFiscalService.processarRetornoNotaFiscal(this.gerarParseListaNotasRetorno(listaNotas));
		
		this.session.setAttribute(LISTA_NOTAS_DE_RETORNO, listaNotasRetorno);
		
		List<SumarizacaoNotaRetornoVO> sumarizacoes = new ArrayList<SumarizacaoNotaRetornoVO>();
		SumarizacaoNotaRetornoVO sumarizacao = this.sumarizarNotasRetorno(listaNotasRetorno);
		sumarizacoes.add(sumarizacao);
		this.result.use(FlexiGridJson.class).from(sumarizacoes).page(1).total(sumarizacoes.size()).serialize();
				
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
	private List<RetornoNFEDTO> gerarParseListaNotasRetorno(final List<File> arquivosNotas) {
		
		final HashMap<String, RetornoNFEDTO> hashNotasRetorno = new HashMap<String, RetornoNFEDTO>();
		final List<RetornoNFEDTO> listaNotas = new ArrayList<RetornoNFEDTO>();
		RetornoNFEDTO arquivoAux = null;
		for (final File nota : arquivosNotas) {
			
			try {
				String schemaPath = this.getClass().getClassLoader().getResource("").getPath();
				
				RetornoNFEDTO arquivoRetorno = NFEImportUtil.processarArquivoRetorno(nota, schemaPath);
			
				if (arquivoAux == null) {
			
					arquivoAux = arquivoRetorno;
							
				} 
				
				if (arquivoAux.getChaveAcesso().equals(arquivoRetorno.getChaveAcesso()) && (arquivoRetorno.getTpEvento() != null && arquivoRetorno.getTpEvento().equals("110111"))) {
				
					arquivoRetorno = this.processarNotaCancelamento(arquivoAux, arquivoRetorno);
				}
							
				hashNotasRetorno.put(arquivoRetorno.getChaveAcesso(), arquivoRetorno);
			
			} catch (Exception e) { 
				continue;
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
}