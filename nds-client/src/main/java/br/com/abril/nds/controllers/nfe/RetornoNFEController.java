package br.com.abril.nds.controllers.nfe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.NFEImportUtil;
import br.com.abril.nds.client.vo.ArquivoRetornoNFEVO;
import br.com.abril.nds.client.vo.SumarizacaoNotaRetornoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.service.ParametroSistemaService;
import br.com.abril.nds.util.FileImportUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter.FileType;
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
public class RetornoNFEController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
	//@Autowired
	//private NotaFiscalService notaFiscalService;
	
	@Autowired
	private HttpSession session;
	
	private static final String LISTA_NOTAS_DE_RETORNO = "listaNotasDeRetorno";
	
	@Path("/")
	public void index() {	
	}

	@Post("/pesquisarArquivos.json")
	public void pesquisarArquivosDeRetorno(Date dataReferencia) {
		
		ParametroSistema pathNFEImportacao = 
				this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTACAO);
		
		this.limparSessao();
		
		List<File> listaNotas = null;
		
		try {
		
			listaNotas = FileImportUtil.importArquivosModificadosEm( pathNFEImportacao.getValor(), dataReferencia, FileType.XML);
		
		} catch (FileNotFoundException e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "O diretório parametrizado não é válido"));
		}
		
		if (listaNotas == null || listaNotas.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não foi encontrado nenhuma nota para a data informada"));
		}
				
		List<ArquivoRetornoNFEVO> listaNotasRetorno = this.gerarListaNotasRetorno(listaNotas);
						
		this.session.setAttribute(LISTA_NOTAS_DE_RETORNO, listaNotasRetorno);
		
		SumarizacaoNotaRetornoVO sumarizacao = this.sumarizarNotasRetorno(listaNotasRetorno);
		
		this.result.use(Results.json()).from(sumarizacao, "sumarizacao").serialize();
	}
	
	
	@SuppressWarnings("unchecked")
	@Post("/confirmar.json")
	public void confirmar() {
		
		List<ArquivoRetornoNFEVO> listaNotasRetorno = (List<ArquivoRetornoNFEVO>) this.session.getAttribute(LISTA_NOTAS_DE_RETORNO);
		
		if (listaNotasRetorno == null || listaNotasRetorno.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não existem notas de retorno para serem atualizadas"));
		}
		
		for (ArquivoRetornoNFEVO notaRetorno : listaNotasRetorno) {
			
			if (Status.AUTORIZADO.equals(notaRetorno.getStatus())) {
			
				RetornoComunicacaoEletronica retornoComunicacaoEletronica = new RetornoComunicacaoEletronica();
			
				retornoComunicacaoEletronica.setMotivo(notaRetorno.getMotivo());
				retornoComunicacaoEletronica.setProtocolo(notaRetorno.getProtocolo());
				retornoComunicacaoEletronica.setStatus(notaRetorno.getStatus());
				retornoComunicacaoEletronica.setDataRecebimento(notaRetorno.getDataRecebimento());
							
				//this.notaFiscalService.processarRetornoNotaFiscal(notaRetorno.getIdNotaFiscal(), retornoComunicacaoEletronica);
		
			} 
		}
		
		this.limparSessao();
		
		this.result.use(Results.json()).from("OK").serialize();
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
	private SumarizacaoNotaRetornoVO sumarizarNotasRetorno(List<ArquivoRetornoNFEVO> listaNotasRetorno) {
		
		Long totalArquivos = (long) listaNotasRetorno.size();
		Long notasRejeitadas = 0L;
		Long notasAprovadas = 0L;
		
		for (ArquivoRetornoNFEVO nota : listaNotasRetorno) {
			
			if(Status.AUTORIZADO.equals(nota.getStatus())) {
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
	private List<ArquivoRetornoNFEVO> gerarListaNotasRetorno (List<File> arquivosNotas) {
		
		HashMap<String, ArquivoRetornoNFEVO> hashNotasRetorno = new HashMap<String, ArquivoRetornoNFEVO>();
		
		ArquivoRetornoNFEVO arquivoRetornoAuxiliar = null;
		
		for (File nota : arquivosNotas) {
									
			ArquivoRetornoNFEVO arquivoRetorno = NFEImportUtil.processarArquivoRetorno(nota);
			
			if (arquivoRetornoAuxiliar == null) {
			
				arquivoRetornoAuxiliar = arquivoRetorno;
							
			} else if (arquivoRetornoAuxiliar.getChaveAcesso().equals(arquivoRetorno)) {
				
				arquivoRetorno = this.processarNotaCancelamento(arquivoRetornoAuxiliar, arquivoRetorno);
			}
							
			hashNotasRetorno.put(arquivoRetorno.getChaveAcesso(), arquivoRetorno);
		}
		
		List<ArquivoRetornoNFEVO> listaNotas = new ArrayList<ArquivoRetornoNFEVO>();
		
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
	 * @return
	 */
	private ArquivoRetornoNFEVO processarNotaCancelamento(ArquivoRetornoNFEVO arquivo01, ArquivoRetornoNFEVO arquivo02) {
		
		ArquivoRetornoNFEVO notaCancelamentoMerged = new ArquivoRetornoNFEVO();
		
		notaCancelamentoMerged.setChaveAcesso(arquivo01.getChaveAcesso());
		
		notaCancelamentoMerged.setIdNotaFiscal(
				arquivo01.getIdNotaFiscal() != null ? 
						arquivo01.getIdNotaFiscal() : arquivo02.getIdNotaFiscal());
		
		notaCancelamentoMerged.setDataRecebimento(
				arquivo01.getDataRecebimento() != null ?
						arquivo01.getDataRecebimento() : arquivo02.getDataRecebimento());
		
		notaCancelamentoMerged.setMotivo(
				arquivo01.getMotivo() != null ?
						arquivo01.getMotivo() : arquivo02.getMotivo());
		
		notaCancelamentoMerged.setProtocolo(
				arquivo01.getProtocolo() != null ?
						arquivo01.getProtocolo() : arquivo02.getProtocolo());
		
		notaCancelamentoMerged.setStatus(
				arquivo01.getStatus() != null?
						arquivo01.getStatus() : arquivo02.getStatus());
		
		return notaCancelamentoMerged;
	}
}