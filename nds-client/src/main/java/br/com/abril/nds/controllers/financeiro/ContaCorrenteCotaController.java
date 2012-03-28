package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.ViewContaCorrenteCota;
import br.com.abril.nds.service.ContaCorrenteCotaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/contaCorrenteCota")
public class ContaCorrenteCotaController {
	
	@Autowired
	private Result result;

	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private ContaCorrenteCotaService contaCorrenteCotaService;
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroContaCorrente";
	
		
	public ContaCorrenteCotaController(){		
	}
	
	public void index() {	
	}
					
	public void consultarContaCorrenteCota( FiltroViewContaCorrenteCotaDTO filtroViewContaCorrenteCotaDTO,String sortname, String sortorder, int rp, int page) {
			
				
		this.validarDadosEntradaPesquisa(filtroViewContaCorrenteCotaDTO.getNumeroCota());
		
		prepararFiltro(filtroViewContaCorrenteCotaDTO, sortorder, sortname, page, rp);
		
		tratarFiltro(filtroViewContaCorrenteCotaDTO);
		
		List<ViewContaCorrenteCota> listaItensContaCorrenteCota = contaCorrenteCotaService.obterListaConsolidadoPorCota(filtroViewContaCorrenteCotaDTO);
			
		if (listaItensContaCorrenteCota == null || listaItensContaCorrenteCota.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		TableModel<CellModel> tableModel =  obterTableModelParaListItensContaCorrenteCota(listaItensContaCorrenteCota);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private void prepararFiltro(FiltroViewContaCorrenteCotaDTO filtroViewContaCorrenteCotaDTO,	String sortorder, String sortname, int page, int rp) {
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

		filtroViewContaCorrenteCotaDTO.setPaginacao(paginacao);
		
		paginacao.setPaginaAtual(page);
		
		filtroViewContaCorrenteCotaDTO.setColunaOrdenacao(Util.getEnumByStringValue(FiltroViewContaCorrenteCotaDTO.ColunaOrdenacao.values(), sortname));
		
		
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroViewContaCorrenteCotaDTO filtroViewContaCorrenteCotaDTO) {

		FiltroViewContaCorrenteCotaDTO filtroContaCorrenteSession = (FiltroViewContaCorrenteCotaDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroContaCorrenteSession != null
				&& !filtroContaCorrenteSession.equals(filtroViewContaCorrenteCotaDTO)) {

			filtroViewContaCorrenteCotaDTO.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroViewContaCorrenteCotaDTO);
	}
	
	
	/**
	 * Obtem uma lista de Conta Corrente cota e prepara o Grid para receber os valores
	 * @param itensContaCorrenteCota
	 * @return
	 */
	private TableModel<CellModel> obterTableModelParaListItensContaCorrenteCota(List<ViewContaCorrenteCota> itensContaCorrenteCota) {
					
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		
		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();
		
		int counter = 1;
		
		Integer codCota = null;
		
		for(ViewContaCorrenteCota dto : itensContaCorrenteCota) {
			
			
			codCota = dto.getNumeroCota();
			String data 		     	 = dto.getDataConsolidado().toString();
			String valorPostergado	     = (dto.getValorPostergado() 	== null) 	? "0.0" : dto.getValorPostergado().toString();
			String NA 		     	     = (dto.getNumeroAtrasados()    == null) 	? "0.0" : dto.getNumeroAtrasados().toString();
			String consignado 	     	 = (dto.getConsignado()			== null) 	? "0.0" : dto.getConsignado().toString();
			String encalhe 	 	         = (dto.getEncalhe()        	== null) 	? "0.0" : dto.getEncalhe().toString();
			String vendaEncalhe		 	 = (dto.getVendaEncalhe()		== null) 	? "0.0" : dto.getVendaEncalhe().toString();
			String debCred		 	     = (dto.getDebitoCredito() 		== null) 	? "0.0" : dto.getDebitoCredito().toString();
			String encargos		 	     = (dto.getEncargos()			== null) 	? "0.0" : dto.getEncargos().toString() ;
			String pendente				 = (dto.getPendente() 			== null) 	? "0.0" : dto.getPendente().toString();
			String total                 = (dto.getTotal() 			    == null) 	? "0.0" : dto.getTotal().toString();
			
			listaModeloGenerico.add(
					new CellModel( 	
							counter, 
							data, 
							valorPostergado, 
							NA, 
							consignado, 
							encalhe,
							vendaEncalhe,
							debCred,
							encargos,
							pendente,
							total
					));
			
			counter++;
		}
		
		Cota cota = cotaService.obterPorNumeroDaCota(codCota);
		
		result.include("cotaNome",cota.getNumeroCota()+" "+cota.getPessoa() );
		
		tableModel.setPage(1);
		tableModel.setTotal(listaModeloGenerico.size());
		tableModel.setRows(listaModeloGenerico);
		
		return tableModel;
		
	}
			
	private void validarDadosEntradaPesquisa(Integer numeroCota) {
		List<String> listaMensagemValidacao = new ArrayList<String>();
		
		if (numeroCota == null){
			listaMensagemValidacao.add("O Preenchimento do campo Cota é obrigatório.");
		}else{
			if(!Util.isNumeric(numeroCota.toString())){
				listaMensagemValidacao.add("A Cota permite apenas valores números.");
			}
		}
		
		if (!listaMensagemValidacao.isEmpty()){
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.WARNING, listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}
	}
}
