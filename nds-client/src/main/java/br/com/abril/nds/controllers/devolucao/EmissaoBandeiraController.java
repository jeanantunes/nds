package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.sun.xml.wss.util.DateUtils;

import br.com.abril.icd.axis.util.DateUtil;
import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.EmissaoBandeirasService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("devolucao/emissaoBandeira")
@Rules(Permissao.ROLE_RECOLHIMENTO_EMISSAO_BANDEIRA)
public class EmissaoBandeiraController extends BaseController {

	@Autowired
	private Result result;
	
	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private EstoqueProdutoService estoqueProdutoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private EmissaoBandeirasService emissaoBandeirasService;

	@Path("/")
	public void index() {
		
		this.result.include("fornecedores", this.fornecedorService.obterFornecedoresDesc());
	}
	
	@Path("/pesquisar")
	public void pesquisar(Date dataEmissao, Long fornecedor, String numeroNotaDe, String numeroNotaAte, String sortname, String sortorder, int rp, int page) {
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder, sortname);
		
		if(dataEmissao == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Favor preencher o Data de Emissao.");
		}
		
		int total = emissaoBandeirasService.countObterBandeirasDaSemana(dataEmissao, fornecedor).intValue();
		
		if (total <= 0 ) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
		    List<BandeirasDTO> listaBandeiraDTO = emissaoBandeirasService.obterBandeirasDaSemana(dataEmissao, fornecedor, numeroNotaDe, numeroNotaAte, paginacaoVO); 
			this.result.use(FlexiGridJson.class)
				.from(listaBandeiraDTO)
				.total(total)
				.page(page).serialize();
		}
	}

	@Get
	@Path("/imprimirArquivo")
	public void imprimirArquivo(Date dataEmissao, Long fornecedor, String numeroNotaDe, String numeroNotaAte, String sortname, String sortorder, int rp, int page, FileType fileType) {
	
		List<BandeirasDTO> listaBandeiraDTO = emissaoBandeirasService.obterBandeirasDaSemana(dataEmissao, fornecedor, numeroNotaDe, numeroNotaAte, null);
		
		if (listaBandeiraDTO != null && !listaBandeiraDTO.isEmpty()) {
			try {
				
				FileExporter.to("emissao-bandeira", fileType).inHTTPResponse(
					this.getNDSFileHeader(), null, listaBandeiraDTO, BandeirasDTO.class, this.response);
				
			} catch (Exception e) {
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao gerar o arquivo!"));
			}
		}
		
		this.result.use(Results.nothing());
	}
	
	@Post
	public void imprimirBandeira(Date dataEmissao, Long fornecedor, Integer numeroPallets[], Date dataEnvio[],
			 Integer nota[], Integer serie[], String numeroNotaDe, String numeroNotaAte) throws Exception {

		byte[] comprovate = emissaoBandeirasService.imprimirBandeira(dataEmissao, fornecedor, dataEnvio, numeroPallets,nota,serie, numeroNotaDe, numeroNotaAte);
		
		this.escreverArquivoParaResponse(comprovate, "bandeira_" + DateUtil.formatarData(dataEmissao, "ddMMyyyyHHmm"));
		
	}
	
	@Path("/bandeiraManual")
	public void bandeiraManual() {
		
	}
	
	@Get("/imprimirBandeiraManual")
	public Download imprimirBandeiraManual(String anoSemana, Integer numeroPallets, String fornecedor, 
			String praca, String canal, String dataEnvio, String titulo) throws Exception{
		
		byte[] comprovate = emissaoBandeirasService.imprimirBandeiraManual(anoSemana, numeroPallets, fornecedor, praca, canal, dataEnvio, titulo);
		
		return new ByteArrayDownload(comprovate,"application/pdf", "imprimirBandeiraManual.pdf", true);
	}
	
	public Download obterLogoDistribuidor() {
		
		return new InputStreamDownload(super.getLogoDistribuidor(), null, null);
	}
	
	/**
	 * Metodo utilitario para escrever arquivo em pdf
	 */
	private void escreverArquivoParaResponse(byte[] arquivo, String nomeArquivo) {
		
		this.response.setContentType("application/pdf");

		this.response.setHeader("Content-Disposition", "attachment; filename="+ nomeArquivo +".pdf");

		OutputStream output;
		try {
			
			output = this.response.getOutputStream();
			
			output.write(arquivo);

			this.response.getOutputStream().flush();
			
			this.response.getOutputStream().close();
			
			result.use(Results.nothing());
			
			if(!this.response.isCommitted()) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao gerar relatorio");
			}
			
		} catch (IOException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao gerar relatorio");
		}
	}
	
}