package br.com.abril.nds.controllers.devolucao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.EmissaoBandeiraVO;
import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.EmissaoBandeiraDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.service.EmissaoBandeiraService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("devolucao/emissaoBandeira")
public class EmissaoBandeiraController {

	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private EmissaoBandeiraService emissaoBandeiraService;
	
	@Autowired
	private ChamadaEncalheService chamadaEncalheService;

	@Path("/")
	@Rules(Permissao.ROLE_RECOLHIMENTO_EMISSAO_BANDEIRA)
	public void index() {
	
	}
	
	@Path("/pesquisar")
	public void pesquisar(Integer semana, String sortname, String sortorder, int rp, int page) {
		
		List<BandeirasDTO> listaBandeiraDTO = chamadaEncalheService.obterBandeirasDaSemana(semana); 
		
		List<EmissaoBandeiraVO> listaEmissaoBandeiraVO =  new ArrayList<EmissaoBandeiraVO>();
		
		for (BandeirasDTO dto :listaBandeiraDTO ){
			listaEmissaoBandeiraVO.add(new EmissaoBandeiraVO (dto));
		}
		
		if (listaEmissaoBandeiraVO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
			this.result.use(FlexiGridJson.class).from(listaEmissaoBandeiraVO).total(listaEmissaoBandeiraVO.size()).page(page).serialize();
		}
	}


	
	@Get
	@Path("/imprimirArquivo")
	public void imprimirArquivo(Integer semana,	String sortname, String sortorder, int rp, int page, FileType fileType) {
	
	List<BandeirasDTO> listaBandeiraDTO = chamadaEncalheService.obterBandeirasDaSemana(semana); 
		
		List<EmissaoBandeiraVO> listaEmissaoBandeiraVO =  new ArrayList<EmissaoBandeiraVO>();
		
		for (BandeirasDTO dto :listaBandeiraDTO ){
			listaEmissaoBandeiraVO.add(new EmissaoBandeiraVO (dto));
		}
		
		
		if (listaEmissaoBandeiraVO != null && !listaEmissaoBandeiraVO.isEmpty()) {
			try {
				
				FileExporter.to("emissao-bandeira", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, listaEmissaoBandeiraVO,EmissaoBandeiraVO.class, this.response);
				
			} catch (Exception e) {
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao gerar o arquivo!"));
			}
		}
		
		this.result.use(Results.nothing());
	}
	
	/**
	 * Obtém os dados do cabeçalho de exportação.
	 * 
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.obterUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	
	/**
	 * Obtém usuário logado.
	 * 
	 * @return usuário logado
	 */
	private Usuario obterUsuario() {
		
		//TODO: Aguardando definição de como será obtido o usuário logado
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		usuario.setNome("Usuário da Silva");
		
		return usuario;
	}

	@Get("/imprimirBandeira")
	public Download imprimirBandeira(Integer semana, Integer numeroPallets  ) throws Exception{
		
		byte[] comprovate = emissaoBandeiraService.imprimirBandeira(semana, numeroPallets);
		
		return new ByteArrayDownload(comprovate,"application/pdf", "imprimirBandeira.pdf", true);
	}
	
	@Path("/bandeiraManual")
	public void bandeiraManual() {
		
	}
	
	@Get("/imprimirBandeiraManual")
	public Download imprimirBandeiraManual(Integer semana, Integer numeroPallets,String nome, String codigoPracaNoProdin, String praca, String destino, String canal ) throws Exception{
		
		byte[] comprovate = emissaoBandeiraService.imprimirBandeiraManual(semana, numeroPallets, nome, codigoPracaNoProdin, praca, destino, canal);
		
		return new ByteArrayDownload(comprovate,"application/pdf", "imprimirBandeiraManual.pdf", true);
	}
	
	
	
	
	
	


	
}
