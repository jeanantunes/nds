package br.com.abril.nds.controllers.estoque;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.VisaoEstoqueService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("estoque/visaoEstoque")
public class VisaoEstoqueController {
	
	private static final String FILTRO_VISAO_ESTOQUE = "FILTRO_VISAO_ESTOQUE"; 
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private VisaoEstoqueService visaoEstoqueService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private HttpSession session;
	
	
	private Result result;
	
	public VisaoEstoqueController(Result result) {
		super();
		this.result = result;
	}

	
	@Path("/")
	@Rules(Permissao.ROLE_ESTOQUE_VISAO_DO_ESTOQUE)
	public void index()
	{
		List<Fornecedor> listFornecedores = fornecedorService.obterFornecedores();
		result.include("listFornecedores", listFornecedores);
		result.include("dataAtual", DateUtil.formatarData(new Date(), "dd/MM/yyyy"));
	}
	
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroConsultaVisaoEstoque filtro, String sortname, String sortorder, int rp, int page) {
		
		this.session.setAttribute(FILTRO_VISAO_ESTOQUE, filtro);
		
		//List<VisaoEstoqueDTO> listVisaoEstoque = visaoEstoqueService.obterVisaoEstoque(filtro);
		
		VisaoEstoqueDTO dto = new VisaoEstoqueDTO();
		dto.setEstoque("Teste");
		dto.setProdutos(10L);
		dto.setExemplares(1000L);
		dto.setValor(BigDecimal.TEN);
		
		List<VisaoEstoqueDTO> listVisaoEstoque = new ArrayList<VisaoEstoqueDTO>();
		listVisaoEstoque.add(dto);
		
		result.use(FlexiGridJson.class).from(listVisaoEstoque).total(listVisaoEstoque.size()).page(page).serialize();
	}
	
	
	
	public void exportarConsulta(FileType fileType) throws IOException {
		
		FiltroConsultaVisaoEstoque filtro = (FiltroConsultaVisaoEstoque) this.session.getAttribute(FILTRO_VISAO_ESTOQUE);
		
		List<VisaoEstoqueDTO> listVisaoEstoque = visaoEstoqueService.obterVisaoEstoque(filtro);
		
		FileExporter.to("consulta-box", fileType).inHTTPResponse(
				this.getNDSFileHeader(), null, null,
				listVisaoEstoque, VisaoEstoqueDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	
	private NDSFileHeader getNDSFileHeader() {

		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		Distribuidor distribuidor = distribuidorService.obter();

		if (distribuidor != null) {
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}

		ndsFileHeader.setData(new Date());
		ndsFileHeader.setNomeUsuario(getUsuario().getNome());
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
