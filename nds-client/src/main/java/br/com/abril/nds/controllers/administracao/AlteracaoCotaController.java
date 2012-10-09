package br.com.abril.nds.controllers.administracao;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.AlteracaoCotaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.HistoricoTitularidadeCotaFinanceiroService;
import br.com.abril.nds.service.TipoEntregaService;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/administracao/alteracaoCota")
public class AlteracaoCotaController {

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private TipoEntregaService tipoEntregaService;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private AlteracaoCotaService alteracaoCotaService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private HttpSession session;

	@Autowired
	private HistoricoTitularidadeCotaFinanceiroService historicoTitularidadeCotaFinanceiroService;
	
	
	private Result result;
	
	public AlteracaoCotaController(Result result) {
		super();
		this.result = result;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_CADASTRO_ALTERACAO_COTA)
	public void index()
	{
		result.include("listFornecedores", fornecedorService.obterFornecedoresAtivos());
		result.include("listBairros", enderecoService.pesquisarTodosBairros());
		List<String> obterMunicipiosCotas = enderecoService.obterMunicipiosCotas();
		result.include("listMunicipios", obterMunicipiosCotas);
		
		List<Integer> listaVencimento = new ArrayList<Integer>();
		for(int i = 1; i < 31; i++){
			listaVencimento.add(i);
		}
		result.include("listaVencimento", listaVencimento);
		result.include("listTipoEntrega", tipoEntregaService.obterTodos());
		result.include("listTipoDesconto", TipoDesconto.values());
		
		result.include("listHistoricoTitularidadeCotaFinanceiro", historicoTitularidadeCotaFinanceiroService.pesquisarTodos());
		
	}
	
	@Path("/pesquisarAlteracaoCota.json")
	public void pesquisarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, String sortname, int page, int rp) {
		
		int startSearch = page * rp - rp;
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortname);
		paginacao.setOrdenacao(paginacao.getOrdenacao().ASC);
		filtroAlteracaoCotaDTO.setPaginacao(paginacao);
		
		List<ConsultaAlteracaoCotaDTO> lista = this.alteracaoCotaService.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);
		
		
		this.result.use(FlexiGridJson.class).from(lista).total(lista.size()).page(page).serialize();
		
	}
	
	@Post
	public void carregarCamposAlteracao(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, String sortname, int page, int rp) {
		
		List<Fornecedor> listaFornecedoresAtivos = fornecedorService.obterFornecedoresAtivos();
		List<Fornecedor> listFornecedoresCota = new ArrayList<Fornecedor>();
		
		//Carregará os dados apenas se o usuário selecionar uma linha do grid p/ alteração.
		if(filtroAlteracaoCotaDTO != null && filtroAlteracaoCotaDTO.getListaLinhaSelecao() != null && filtroAlteracaoCotaDTO.getListaLinhaSelecao().size()== 1){
			String idCotaStr = filtroAlteracaoCotaDTO.getListaLinhaSelecao().get(0);
			if(idCotaStr != null && !"".equals(idCotaStr)){
				listFornecedoresCota.addAll(fornecedorService.obterFornecedoresCota(new Long(idCotaStr)));
				removerFornecedorAssociadoLista(listFornecedoresCota, listaFornecedoresAtivos);
			}
			
			filtroAlteracaoCotaDTO.getFiltroModalFornecedor().setListFornecedores(listaFornecedoresAtivos);
			filtroAlteracaoCotaDTO.getFiltroModalFornecedor().setListaFornecedorAssociado(listFornecedoresCota);
			
			//Set vals aba financeiro
			//filtroAlteracaoCotaDTO.setFiltroModalFinanceiro(pesquisarDadosModalFinanceiro(filtroAlteracaoCotaDTO)
			filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setIdVencimento(1);
			filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setVrMinimo("123,33");
			filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setIsSugereSuspensao(true);
			filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setQtdDividaEmAberto(12);
			filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setVrDividaEmAberto("123,33");
			
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setNmAssitPromoComercial("Tonhao");
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setNmGerenteComercial("Gabril");
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setObservacao("lalalalala");
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIsRepartePontoVenda(true);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIsSolicitacaoNumAtrasoInternet(false);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIsRecebeRecolheProdutosParciais(true);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIdTipoEntrega(2l);
			
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsSkipImpresso(true);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsSkipEmail(false);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoImpresso(false);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoEmail(true);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoSkipImpresso(true);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoSkipEmail(false);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsReciboImpresso(true);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsReciboEmail(true);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsNoteEnvioImpresso(false);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsNoteEnvioEmail(true);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsChamdaEncalheImpresso(false);
			filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsChamdaEncalheEmail(false);
		}

		
		result.use(CustomMapJson.class).put("filtroAlteracaoCotaDTO", filtroAlteracaoCotaDTO).serialize();
	}
	
	@Post
	public void salvarAlteracao(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, String sortname, int page, int rp) {
		System.out.println("Salvar");
	}


	public static void main(String[] args) {
		Fornecedor f = new Fornecedor();
		f.setId(1l);
		
		Fornecedor f2 = new Fornecedor();
		f2.setId(2l);

		Fornecedor f3 = new Fornecedor();
		f3.setId(3l);
		
		List<Fornecedor> listaFornecedor = new ArrayList<Fornecedor>();
		listaFornecedor.add(f);
		listaFornecedor.add(f2);
		listaFornecedor.add(f3);
		
		List<Fornecedor> listaFornecedorCota = new ArrayList<Fornecedor>();
		listaFornecedorCota.add(f);
		listaFornecedorCota.add(f2);
		
		removerFornecedorAssociadoLista(listaFornecedorCota, listaFornecedor);
		
		System.out.println("OK");
	}
	
	private static void removerFornecedorAssociadoLista(List<Fornecedor> listFornecedoresCota, List<Fornecedor> listFornecedores) {
		
		if(listFornecedoresCota != null && listFornecedoresCota.size() > 0 && listFornecedores != null && listFornecedores.size() > 0){
			
			for(int i = 0; i < listFornecedoresCota.size(); i++){
				Fornecedor fornecedorCota = listFornecedoresCota.get(i);
				if(fornecedorCota!= null){
					
					if(listFornecedores.size() == 0)
						break;
					
					for(int j = 0; j < listFornecedores.size(); j++){
						Fornecedor fornecedor = listFornecedores.get(j);
						if(fornecedor!= null){
							
							if(fornecedor.getId().compareTo(fornecedorCota.getId()) == 0){
								listFornecedores.remove(j);
								break;
							}
						}
					}
				}
			}
		}
	}
}
