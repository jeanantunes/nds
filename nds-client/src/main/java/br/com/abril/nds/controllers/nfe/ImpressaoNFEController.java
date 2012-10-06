package br.com.abril.nds.controllers.nfe;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.NfeImpressaoDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ImpressaoNFEService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.RotaService;
import br.com.abril.nds.service.RoteiroService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path(value="/nfe/impressaoNFE")
public class ImpressaoNFEController {

	@Autowired
	private HttpSession session;
	
	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private TipoNotaFiscalService tipoNotaFiscalService;
	
	@Autowired
	private NotaFiscalService notaFiscalService;

	@Autowired
	ImpressaoNFEService impressaoNFEService;

	@Autowired
	DistribuidorService distribuidorService;

	@Autowired
	RoteiroService roteiroService;

	@Autowired
	RotaService rotaService;

	@Autowired
	private Result result;

	@Path("/")
	@Rules(Permissao.ROLE_NFE_IMPRESSAO_NFE)
	public void index() {

		List<Fornecedor> fornecedores = this.fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO);

		this.result.include("produtos", impressaoNFEService.obterProdutosExpedicaoConfirmada(fornecedores));
		this.result.include("fornecedores", fornecedores);
		this.result.include("tipoNotas", tipoNotaFiscalService.carregarComboTiposNotasFiscais(distribuidorService.obter().getTipoAtividade()));
		this.result.include("roteiros", roteiroService.obterRoteiros());
		this.result.include("rotas", rotaService.obterRotas());
		this.result.include("tipoEmissao", TipoEmissaoNfe.values());
		this.result.include("dataAtual", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

	}

	@Post
	public void pesquisarImpressaoNFE(FiltroImpressaoNFEDTO filtro, String sortorder, String sortname, int page, int rp){

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		if(!filtro.isFiltroValido()) {
			
			HashMap<String, String> erros = filtro.validarFiltro();
			for(Iterator i = erros.keySet().iterator(); i.hasNext(); ) {
				String key = (String) i.next();
				throw new ValidacaoException(TipoMensagem.WARNING, erros.get(key));
			}
									
		}
				
		//notaFiscalService.obterItensNotaFiscalPor(distribuidor, cota, periodo, listaIdFornecedores, listaIdProdutos, tipoNotaFiscal)
		List<NfeImpressaoDTO> listaNFe = notaFiscalService.buscarNFeParaImpressao(filtro);
		
		TableModel<CellModelKeyValue<NfeImpressaoDTO>> tableModel = new TableModel<CellModelKeyValue<NfeImpressaoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNFe));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(notaFiscalService.buscarNFeParaImpressaoTotalQtd(filtro));

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

}
