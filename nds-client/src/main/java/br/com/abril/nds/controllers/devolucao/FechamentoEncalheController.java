package br.com.abril.nds.controllers.devolucao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.DateUtil;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

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
	public void pesquisar(String dataEncalhe, Long idFornecedor, Long idBox,
			String sortname, String sortorder, int rp, int page) {
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
		filtro.setFornecedorId(idFornecedor);
		filtro.setBoxId(idBox);
		
		List<FechamentoFisicoLogicoDTO> listaEncalhe = fechamentoEncalheService.buscarFechamentoEncalhe(filtro, sortorder, sortname, page, rp);
		
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
	public void postergarCotas(Date dataEncalhe, List<Long> idsCotas) {
		
		System.out.println(idsCotas);
		
	}

	@Path("/cobrarCotas")
	public void cobrarCotas(Date dataEncalhe, List<Long> idsCotas) {

		System.out.println(idsCotas);
	}
	
}
