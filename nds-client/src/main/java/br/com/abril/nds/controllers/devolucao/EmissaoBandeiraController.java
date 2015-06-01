package br.com.abril.nds.controllers.devolucao;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.service.EmissaoBandeiraService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
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
	private EmissaoBandeiraService emissaoBandeiraService;
	
	@Autowired
	private ChamadaEncalheService chamadaEncalheService;
	
	@Autowired
	private EstoqueProdutoService estoqueProdutoService;
	
	@Autowired
	private FornecedorService fornecedorService;

	@Path("/")
	public void index() {
		
		this.result.include("fornecedores", this.fornecedorService.obterFornecedoresDesc());
	}
	
	@Path("/pesquisar")
	public void pesquisar(Integer anoSemana, Long fornecedor, String sortname, String sortorder, int rp, int page) {
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder, sortname);
		
		int total = chamadaEncalheService.countObterBandeirasDaSemana(anoSemana, fornecedor).intValue();
		
		if (total <= 0 ) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
		    List<BandeirasDTO> listaBandeiraDTO = chamadaEncalheService.obterBandeirasDaSemana(anoSemana, fornecedor, paginacaoVO); 
			this.result.use(FlexiGridJson.class)
				.from(listaBandeiraDTO)
				.total(total)
				.page(page).serialize();
		}
	}

	@Get
	@Path("/imprimirArquivo")
	public void imprimirArquivo(Integer anoSemana, Long fornecedor, String sortname, String sortorder, int rp, int page, FileType fileType) {
	
		List<BandeirasDTO> listaBandeiraDTO = chamadaEncalheService.obterBandeirasDaSemana(anoSemana, fornecedor, null);
		
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
	

	@Get("/imprimirBandeira")
	public Download imprimirBandeira(Integer anoSemana, Long fornecedor, Integer numeroPallets,
			Date dataEnvio) throws Exception{
		
		byte[] comprovate = emissaoBandeiraService.imprimirBandeira(anoSemana, numeroPallets, dataEnvio, fornecedor);
		
		return new ByteArrayDownload(comprovate,"application/pdf", "bandeira_" + anoSemana + ".pdf", true);
	}
	
	@Path("/bandeiraManual")
	public void bandeiraManual() {
		
	}
	
	@Get("/imprimirBandeiraManual")
	public Download imprimirBandeiraManual(String anoSemana, Integer numeroPallets, String fornecedor, 
			String praca, String canal, String dataEnvio, String titulo) throws Exception{
		
		byte[] comprovate = 
			emissaoBandeiraService.imprimirBandeiraManual(
				anoSemana, numeroPallets, fornecedor, praca, canal, dataEnvio, titulo);
		
		return new ByteArrayDownload(comprovate,"application/pdf", "imprimirBandeiraManual.pdf", true);
	}
	
	public Download obterLogoDistribuidor() {
		
		return new InputStreamDownload(super.getLogoDistribuidor(), null, null);
	}
}
