package br.com.abril.nds.controllers.devolucao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes à
 * tela de chamadão de publicações.
 * 
 * @author Discover Technology
 */
@Resource
@Path("devolucao/fechamentoEncalhe")
public class FechamentoEncalheController {

	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private FechamentoEncalheService fechamentoEncalheService;

	@Autowired
	private HttpServletResponse response;
	
	@Path("/")
	public void index() {
		
		Distribuidor dist = distribuidorService.obter();
		List<Fornecedor> listaFornecedores = fornecedorService.obterFornecedores();
		List<Box> listaBoxes = boxService.buscarPorTipo(TipoBox.RECOLHIMENTO);
		
		result.include("dataOperacao", DateUtil.formatarDataPTBR(dist.getDataOperacao()));
		result.include("listaFornecedores", listaFornecedores);
		result.include("listaBoxes", listaBoxes);
	}
	
	@Path("/pesquisar")
	public void pesquisar(String dataEncalhe, Long fornecedorId, Long boxId,
			String sortname, String sortorder, int rp, int page) {
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
		filtro.setFornecedorId(fornecedorId);
		filtro.setBoxId(boxId);
		
		List<FechamentoFisicoLogicoDTO> listaEncalhe = fechamentoEncalheService.buscarFechamentoEncalhe(filtro, sortorder, this.resolveSort(sortname), page, rp);
		
		this.result.use(FlexiGridJson.class).from(listaEncalhe).total(listaEncalhe.size()).page(page).serialize();
	}
	
	
	@Path("/salvar")
	public void salvar(String dataEncalhe, Long fornecedorId, Long boxId, String fisico,
			String sortname, String sortorder, int rp, int page) {
		
		String[] valoresFisico = fisico.split(",");
		ArrayList<Long> arrayFisico = new ArrayList<Long>();
		for (String f : valoresFisico) {
			arrayFisico.add(f == "" ? null : Long.valueOf(f));
		}
	
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
		filtro.setFornecedorId(fornecedorId);
		filtro.setBoxId(boxId);
		filtro.setFisico(arrayFisico);
		
		List<FechamentoFisicoLogicoDTO> listaEncalhe = fechamentoEncalheService.salvarFechamentoEncalhe(filtro, sortorder, this.resolveSort(sortname), page, rp);
		
		result.use(FlexiGridJson.class).from(listaEncalhe).total(listaEncalhe.size()).page(page).serialize();
	}
	
	
	@Path("/cotasAusentes")
	public void cotasAusentes(Date dataEncalhe,
			String sortname, String sortorder, int rp, int page) {
		
		List<CotaAusenteEncalheDTO> listaCotasAusenteEncalhe =
			this.fechamentoEncalheService.buscarCotasAusentes(dataEncalhe, sortorder, sortname, page, rp);
		
		int total = this.fechamentoEncalheService.buscarTotalCotasAusentes(dataEncalhe);
		
		this.result.use(FlexiGridJson.class).from(listaCotasAusenteEncalhe).total(total).page(page).serialize();
	}
	
	@Path("/postergarCotas")
	public void postergarCotas(Date dataPostergacao, List<Long> idsCotas) {
			
		Date dataAtual = Calendar.getInstance().getTime();
		
		if (dataAtual.after(dataPostergacao)) {
			// throw validacao exception.
		}
		
		try {
			
			this.fechamentoEncalheService.postergarCotas(dataPostergacao, idsCotas);
			
		} catch (Exception e) {
			this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.ERROR, "Erro ao tentar postergar!"), "result").recursive().serialize();
			throw new ValidacaoException();
		}
		
		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Cotas postergadas com sucesso!"), "result").recursive().serialize();
	}

	@Path("/cobrarCotas")
	public void cobrarCotas(Date dataEncalhe, List<Long> idsCotas) {
		// TODO: cobrar as cotas.
	}
	
	@Get
	@Path("/exportarArquivo")
	public void exportarArquivo(Date dataEncalhe, FileType fileType) {

		List<CotaAusenteEncalheDTO> listaCotasAusenteEncalhe =
			this.fechamentoEncalheService.buscarCotasAusentes(dataEncalhe, null, null, -1, -1);
		
		try {
			
			FileExporter.to("cotas_ausentes", fileType).inHTTPResponse(
				this.getNDSFileHeader(), null, null, listaCotasAusenteEncalhe, 
				CotaAusenteEncalheDTO.class, this.response);
			
		} catch (Exception e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao gerar o arquivo!"));
		}
		
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
	
	
	private String resolveSort(String sortname) {
		
		if (sortname.endsWith("Formatado")) {
			return sortname.substring(0, sortname.indexOf("Formatado"));
		} else {
			return sortname;
		}
	}
}
