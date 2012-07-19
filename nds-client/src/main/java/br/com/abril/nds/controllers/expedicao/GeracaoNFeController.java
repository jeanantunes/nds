package br.com.abril.nds.controllers.expedicao;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.GeracaoNFeService;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/expedicao/geracaoNFe")
public class GeracaoNFeController {

	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired 
	private GeracaoNFeService geracaoNFeService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;

	@Path("/")
	public void index() {
		result.include("fornecedores", fornecedorService
				.obterFornecedoresIdNome(SituacaoCadastro.ATIVO, true));
	}
	
	@Post("/busca.json")
	public void busca(Intervalo<String> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento, List<Long> listIdFornecedor, List<Long> listIdProduto ,Long tipoNotaFiscal, String sortname,
			String sortorder, int rp, int page) {
		
		//TODO: quantidade
		
		List<CotaExemplaresDTO> cotaExemplaresDTOs=	geracaoNFeService.busca(intervaloBox, intervalorCota, intervaloDateMovimento, listIdFornecedor, listIdProduto, tipoNotaFiscal,  sortname, sortorder, rp, page);
		
		
		result.use(FlexiGridJson.class).from(cotaExemplaresDTOs).page(page).total(cotaExemplaresDTOs.size()).serialize();

	}
	
	@Post("/buscaCotasSuspensas.json")
	public void buscaCotasSuspensas(Intervalo<String> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento, List<Long> listIdFornecedor, List<Long> listIdProduto ,Long tipoNotaFiscal, String sortname,
			String sortorder, int rp, int page) {
		
		//TODO: quantidade
		
		List<CotaExemplaresDTO> cotaExemplaresDTOs=	geracaoNFeService.busca(intervaloBox, intervalorCota, intervaloDateMovimento, listIdFornecedor, listIdProduto, tipoNotaFiscal, sortname, sortorder, rp, page);
		
		
		result.use(FlexiGridJson.class).from(cotaExemplaresDTOs).page(page).total(cotaExemplaresDTOs.size()).serialize();

	}
	
	@Post("/hasCotasSuspensas.json")
	public void hasCotasSuspensas(Intervalo<String> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento, List<Long> listIdFornecedor,Long tipoNotaFiscal){
		
		
		result.use(CustomMapJson.class).put("cotasSuspensas", true).serialize();
	}
	
	@Post("/gerar.json")
	public void gerar(Intervalo<String> intervaloBox,
			Intervalo<Long> intervalorCota,
			Intervalo<Calendar> intervaloDateMovimento, List<Long> listIdFornecedor,Long tipoNotaFiscal, Calendar dataEmissao, List<Long> idCotasSuspensas, boolean todasCotasSuspensa){
		
		
		result.use(CustomMapJson.class).put("result", true).serialize();
	}
	
	
	public void exportar(Intervalo<String> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento, List<Long> listIdFornecedor, List<Long> listIdProduto, Long tipoNotaFiscal,String sortname,
			String sortorder,FileType fileType) throws IOException {
		
		
		List<CotaExemplaresDTO> cotaExemplaresDTOs=	geracaoNFeService.busca(intervaloBox, intervalorCota, intervaloDateMovimento, listIdFornecedor, listIdProduto, tipoNotaFiscal, sortname, sortorder, null, null);
		
		FileExporter.to("consignado-encalhe", fileType).inHTTPResponse(
				this.getNDSFileHeader(), null, null,
				cotaExemplaresDTOs, CotaExemplaresDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
		
	}
	
	
	/*
	 * Obtém os dados do cabeçalho de exportação.
	 * 
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {

		NDSFileHeader ndsFileHeader = new NDSFileHeader();

		Distribuidor distribuidor = this.distribuidorService.obter();

		if (distribuidor != null) {

			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica()
					.getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica()
					.getCnpj());
		}

		ndsFileHeader.setData(new Date());

		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());

		return ndsFileHeader;
	}

	// TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {

		Usuario usuario = new Usuario();

		usuario.setId(1L);

		usuario.setNome("Jornaleiro da Silva");

		return usuario;
	}

}
