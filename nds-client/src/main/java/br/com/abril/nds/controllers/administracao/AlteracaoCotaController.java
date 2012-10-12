package br.com.abril.nds.controllers.administracao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.AlteracaoCotaService;
import br.com.abril.nds.service.CotaService;
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
	private CotaService cotaService;

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
		result.include("listTipoEntrega", DescricaoTipoEntrega.values());
		result.include("listTipoDesconto", TipoDesconto.values());
		
		result.include("listHistoricoTitularidadeCotaFinanceiro", historicoTitularidadeCotaFinanceiroService.pesquisarTodos());
		
	}
	
	@Path("/pesquisarAlteracaoCota.json")
	public void pesquisarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, String sortname, int page, int rp) {
		
		int startSearch = page * rp - rp;
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortname);
		paginacao.setOrdenacao(paginacao.getOrdenacao().ASC);
		filtroAlteracaoCotaDTO.setPaginacao(paginacao);
		
		filtroAlteracaoCotaDTO.setNomeCota(PessoaUtil.removerSufixoDeTipo(filtroAlteracaoCotaDTO.getNomeCota()));
		
		List<ConsultaAlteracaoCotaDTO> lista = this.alteracaoCotaService.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);
		
		
		this.result.use(FlexiGridJson.class).from(lista).total(lista.size()).page(page).serialize();
		
	}
	
	@Post
	public void carregarCamposAlteracao(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, String sortname, int page, int rp) {
		
		List<Fornecedor> listaFornecedoresAtivos = fornecedorService.obterFornecedoresAtivos();
		
		//Carregará os dados apenas se o usuário selecionar uma linha do grid p/ alteração.
		if(filtroAlteracaoCotaDTO != null && filtroAlteracaoCotaDTO.getListaLinhaSelecao() != null && filtroAlteracaoCotaDTO.getListaLinhaSelecao().size()== 1){
			
			List<Fornecedor> listFornecedoresCota = new ArrayList<Fornecedor>();
			
			String idCotaStr = filtroAlteracaoCotaDTO.getListaLinhaSelecao().get(0);
			
			Cota cota = alteracaoCotaService.obterCotaComHistoricoTitularidade(new Long(idCotaStr));
			
			if(idCotaStr != null && !"".equals(idCotaStr)){
				listFornecedoresCota.addAll(fornecedorService.obterFornecedoresCota(new Long(idCotaStr)));
				removerFornecedorAssociadoLista(listFornecedoresCota, listaFornecedoresAtivos);
			}
			
			filtroAlteracaoCotaDTO.getFiltroModalFornecedor().setListFornecedores(listaFornecedoresAtivos);
			filtroAlteracaoCotaDTO.getFiltroModalFornecedor().setListaFornecedorAssociado(listFornecedoresCota);
			
			filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setIsSugereSuspensao(cota.isSugereSuspensao());
			
			if(cota.getTitularesCota().iterator().hasNext()){
				
				HistoricoTitularidadeCota historicoTitularidadeCota = cota.getTitularesCota().iterator().next();
				preencherFiltroFinanceiro(filtroAlteracaoCotaDTO, historicoTitularidadeCota);
				preencherFiltroDistribuicao(filtroAlteracaoCotaDTO, historicoTitularidadeCota);
			}

			
		}else{
			filtroAlteracaoCotaDTO.getFiltroModalFornecedor().setListFornecedores(listaFornecedoresAtivos);
		}

		
		result.use(CustomMapJson.class).put("filtroAlteracaoCotaDTO", filtroAlteracaoCotaDTO).serialize();
	}
	
	@Post
	public void salvarAlteracao(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, String sortname, int page, int rp) {
		
		for(String idCota : filtroAlteracaoCotaDTO.getListaLinhaSelecao()){
			//****FORNECEDORES****//
			//Encontra Cota a Ser Alterada
			Cota cota = alteracaoCotaService.obterCotaComHistoricoTitularidade(new Long(idCota));
			
			//Altera Fornecedores da Cota
			Set<Fornecedor> fornecedoresCota = new HashSet<Fornecedor>();
			fornecedoresCota.add(fornecedorService.obterFornecedorPorId(new Long(2)));
			/*for (Long  id : filtroAlteracaoCotaDTO.getFiltroModalFornecedor().getListaFornecedoresSelecionados()){
				;
			}*/
			cota.setFornecedores(fornecedoresCota);
			
			
			//****FINANCEIRO****//
			//Sugere Suspensao
			cota.setSugereSuspensao(filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getIsSugereSuspensao());
		
			if(cota.getTitularesCota().iterator().hasNext()){
				
				HistoricoTitularidadeCota historicoTitularidadeCota = cota.getTitularesCota().iterator().next();
				//Fator Vencimento
				historicoTitularidadeCota.getFinanceiro().setFatorVencimento(filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getIdVencimento());
				//Valor Minimo
				historicoTitularidadeCota.getFinanceiro().setValorMininoCobranca(new BigDecimal(filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getVrMinimo()));
				//Suspensao = true -> Cria Politica de Suspensao
				if (cota.isSugereSuspensao()){
					PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
					politicaSuspensao.setNumeroAcumuloDivida(filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getQtdDividaEmAberto());
					politicaSuspensao.setValor(new BigDecimal(filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getVrDividaEmAberto()));
					historicoTitularidadeCota.getFinanceiro().setPoliticaSuspensao(politicaSuspensao);
				}
				
				//****DISTRIBUICAO****//
				historicoTitularidadeCota.getDistribuicao().setAssistenteComercial(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getNmAssitPromoComercial());
				historicoTitularidadeCota.getDistribuicao().setGerenteComercial(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getNmGerenteComercial());
				historicoTitularidadeCota.getDistribuicao().setObservacao(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getObservacao());
				//historicoTitularidadeCota.getDistribuicao().setTipoEntrega(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getIdTipoEntrega());
	
				historicoTitularidadeCota.getDistribuicao().setEntregaReparteVenda(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getIsRepartePontoVenda());
				historicoTitularidadeCota.getDistribuicao().setSolicitaNumAtrasados(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getIsSolicitacaoNumAtrasoInternet());
				historicoTitularidadeCota.getDistribuicao().setRecebeRecolheParcias(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getIsRecebeRecolheProdutosParciais());
				
				//--Emissao Documentos
				historicoTitularidadeCota.getDistribuicao().setSlipEmail(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsSlipEmail());
				historicoTitularidadeCota.getDistribuicao().setSlipImpresso(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsSlipImpresso());
				historicoTitularidadeCota.getDistribuicao().setBoletoEmail(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsBoletoEmail());
				historicoTitularidadeCota.getDistribuicao().setBoletoImpresso(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsBoletoImpresso());
				historicoTitularidadeCota.getDistribuicao().setBoletoSlipEmail(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsBoletoSlipEmail());
				historicoTitularidadeCota.getDistribuicao().setBoletoSlipImpresso(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsBoletoSlipImpresso());
				historicoTitularidadeCota.getDistribuicao().setReciboEmail(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsReciboEmail());
				historicoTitularidadeCota.getDistribuicao().setReciboImpresso(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsReciboImpresso());
				historicoTitularidadeCota.getDistribuicao().setNotaEnvioEmail(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsNoteEnvioEmail());
				historicoTitularidadeCota.getDistribuicao().setNotaEnvioImpresso(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsNoteEnvioImpresso());
				historicoTitularidadeCota.getDistribuicao().setChamadaEncalheEmail(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsChamdaEncalheEmail());
				historicoTitularidadeCota.getDistribuicao().setChamadaEncalheImpresso(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().getIsChamdaEncalheImpresso());
				
			}
			
			
			cotaService.alterarCota(cota);
		}
		
	}
	
	
	public void preencherFiltroFinanceiro(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, HistoricoTitularidadeCota historicoTitularidadeCota){
		
		//FINANCEIRO
		filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setIdVencimento(historicoTitularidadeCota.getFinanceiro().getFatorVencimento());
		filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setVrMinimo(String.valueOf(historicoTitularidadeCota.getFinanceiro().getValorMininoCobranca()));
		filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setQtdDividaEmAberto(historicoTitularidadeCota.getFinanceiro().getPoliticaSuspensao().getNumeroAcumuloDivida());
		filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setVrDividaEmAberto(String.valueOf(historicoTitularidadeCota.getFinanceiro().getPoliticaSuspensao().getValor()));
	}
	
	public void preencherFiltroDistribuicao(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, HistoricoTitularidadeCota historicoTitularidadeCota){
		
		//DISTRIBUICAO
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setNmAssitPromoComercial(historicoTitularidadeCota.getDistribuicao().getAssistenteComercial());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setNmGerenteComercial(historicoTitularidadeCota.getDistribuicao().getGerenteComercial());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setObservacao(historicoTitularidadeCota.getDistribuicao().getObservacao());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIsRepartePontoVenda(historicoTitularidadeCota.getDistribuicao().getEntregaReparteVenda());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIsSolicitacaoNumAtrasoInternet(historicoTitularidadeCota.getDistribuicao().getSolicitaNumAtrasados());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIsRecebeRecolheProdutosParciais(historicoTitularidadeCota.getDistribuicao().getRecebeRecolheParcias());
		//filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIdTipoEntrega(historicoTitularidadeCota.getDistribuicao());
		
		//--Emissao Documentos
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsSlipImpresso(historicoTitularidadeCota.getDistribuicao().getSlipImpresso());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsSlipEmail(historicoTitularidadeCota.getDistribuicao().getSlipEmail());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoImpresso(historicoTitularidadeCota.getDistribuicao().getBoletoImpresso());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoEmail(historicoTitularidadeCota.getDistribuicao().getBoletoEmail());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoSlipImpresso(historicoTitularidadeCota.getDistribuicao().getBoletoSlipImpresso());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoSlipEmail(historicoTitularidadeCota.getDistribuicao().getBoletoSlipEmail());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsReciboImpresso(historicoTitularidadeCota.getDistribuicao().getReciboImpresso());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsReciboEmail(historicoTitularidadeCota.getDistribuicao().getReciboEmail());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsNoteEnvioImpresso(historicoTitularidadeCota.getDistribuicao().getNotaEnvioImpresso());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsNoteEnvioEmail(historicoTitularidadeCota.getDistribuicao().getNotaEnvioEmail());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsChamdaEncalheImpresso(historicoTitularidadeCota.getDistribuicao().getChamadaEncalheImpresso());
		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsChamdaEncalheEmail(historicoTitularidadeCota.getDistribuicao().getChamadaEncalheEmail());
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
