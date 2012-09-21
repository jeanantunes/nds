package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.NegociacaoDividaDetalheVO;
import br.com.abril.nds.client.vo.NegociacaoDividaVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("financeiro/negociacaoDivida")
public class NegociacaoDividaController {
	
	private static final String FILTRO_NEGOCIACAO_DIVIDA = "FILTRO_NEGOCIACAO_DIVIDA";
	
	@Autowired
	private NegociacaoDividaService negociacaoDividaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private HttpSession session;
	
	
	private Result result;
	
	public NegociacaoDividaController(Result result) {
		super();
		this.result = result;
	}

	
	@Path("/")
	@Rules(Permissao.ROLE_FINANCEIRO_NEGOCIACAO_DIVIDA)
	public void index()
	{}
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroConsultaNegociacaoDivida filtro, String sortname, String sortorder, int rp, int page) {
		
		this.session.setAttribute(FILTRO_NEGOCIACAO_DIVIDA, filtro);
		
		List<NegociacaoDividaVO> listDividas = negociacaoDividaService.obterDividasPorCota(filtro);	
		
		result.use(FlexiGridJson.class).from(listDividas).total(listDividas.size()).page(page).serialize();
	}
	
	
	@Path("/pesquisarDetalhes.json")
	public void pesquisarDetalhes(FiltroConsultaNegociacaoDivida filtro, String sortname, String sortorder, int rp, int page) {
		// TODO
		this.session.setAttribute(FILTRO_NEGOCIACAO_DIVIDA, filtro);
		
		List<NegociacaoDividaVO> list = negociacaoDividaService.obterDividasPorCota(filtro);
		List<NegociacaoDividaDetalheVO> listDividas = new ArrayList<NegociacaoDividaDetalheVO>();
		/*for (Cobranca c : list){
			NegociacaoDividaDetalheVO ndd = new NegociacaoDividaDetalheVO();
			ndd.setData(DateUtil.formatarDataPTBR(c.getDivida().getData()));
			if(c.getStatusCobranca() == StatusCobranca.PAGO)
				ndd.setTipo("Pagamento");
			else
				ndd.setTipo("Dívida");
			ndd.setValor("-"+ CurrencyUtil.formatarValor(c.getDivida().getValor()));
			ndd.setObservacao("TESTE");
			listDividas.add(ndd);
		}*/
		result.use(FlexiGridJson.class).from(listDividas).total(listDividas.size()).page(page).serialize();
	}
	
	@Path("/exportar")
	public void exportar(FileType fileType) throws IOException {
		
		FiltroConsultaNegociacaoDivida filtro = (FiltroConsultaNegociacaoDivida) this.session.getAttribute(FILTRO_NEGOCIACAO_DIVIDA);
		
		List<NegociacaoDividaVO> listDividas = negociacaoDividaService.obterDividasPorCota(filtro);
		
		FileExporter.to("consulta-box", fileType).inHTTPResponse(
				this.getNDSFileHeader(), null, null,
				listDividas, NegociacaoDividaVO.class,
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
