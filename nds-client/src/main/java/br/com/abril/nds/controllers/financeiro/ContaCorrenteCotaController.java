package br.com.abril.nds.controllers.financeiro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jrimum.domkee.financeiro.banco.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ContaCorrenteCotaDTO;
import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/contaCorrenteCota")
public class ContaCorrenteCotaController {
		
	private Result result;

	private HttpServletRequest request;
	
	@Autowired
	private CotaService cotaService;
	
	public ContaCorrenteCotaController(Result result,HttpServletRequest request){
		this.result = result;
		this.request = request;
	}
	
	public void index() {

	
	}
	
	public void buscarCota(Integer numeroCota){
		
		Cota cota = cotaService.obterPorNumeroDaCota(numeroCota);
		if(cota != null){
			result.use(Results.json()).from(cota.getPessoa(), "result").serialize();			 
		}else{
			
			throw new ValidacaoException(TipoMensagem.ERROR,"Cota não encontrado!");
			
		}
	}
	
	@Post
	public void obterListaItemContaCorrenteCota() {
		//TODO Passar lista de conta corrente cota
		List<ContaCorrenteCotaDTO> listaItensContaCorrenteCota =  mokParaGrid();
		obterTableModelParaListItensContaCorrenteCota(listaItensContaCorrenteCota);
	}
	
	private List<ContaCorrenteCotaDTO> mokParaGrid() {
		List<ContaCorrenteCotaDTO> listaItensContaCorrenteCota = new ArrayList<ContaCorrenteCotaDTO>();
		
		ContaCorrenteCotaDTO contaCorrenteCotaDTO = new ContaCorrenteCotaDTO();
		contaCorrenteCotaDTO.setData(new Date());
		contaCorrenteCotaDTO.setValorPostergado(new BigDecimal(5.6));
		contaCorrenteCotaDTO.setNA(new BigDecimal(12.4));
		contaCorrenteCotaDTO.setConsignado(new BigDecimal(123));
		contaCorrenteCotaDTO.setEncalhe(new BigDecimal(111));
		contaCorrenteCotaDTO.setVendaEncalhe(new BigDecimal(12));
		contaCorrenteCotaDTO.setDebCred(new BigDecimal(1.6));
		contaCorrenteCotaDTO.setEncargos(new BigDecimal(3.6));
		contaCorrenteCotaDTO.setPendente(new BigDecimal(1.9));
		//calculo do valor TOTAL (atributos - (encalhe + deb/cred))
		if(contaCorrenteCotaDTO.getValorPostergado() != null && contaCorrenteCotaDTO.getNA() != null){
		BigDecimal totalCalculado = (contaCorrenteCotaDTO.getValorPostergado()
				.add(contaCorrenteCotaDTO.getNA())
				.add(contaCorrenteCotaDTO.getConsignado())
				.add(contaCorrenteCotaDTO.getVendaEncalhe())
				.add(contaCorrenteCotaDTO.getEncargos())
				.add(contaCorrenteCotaDTO.getPendente()))
				.subtract(contaCorrenteCotaDTO.getEncalhe().add(contaCorrenteCotaDTO.getDebCred()));
		
			contaCorrenteCotaDTO.setTotal(totalCalculado);
		}else{
			throw new ValidacaoException(TipoMensagem.ERROR,"Valor Total não calculado devido a valores nulos!");
		}
		
		return listaItensContaCorrenteCota;
	}

	/**
	 * Obtem uma lista de Conta Corrente cota e prepara o Grid para receber os valores
	 * @param itensContaCorrenteCota
	 * @return
	 */
	private TableModel<CellModel> obterTableModelParaListItensContaCorrenteCota(List<ContaCorrenteCotaDTO> itensContaCorrenteCota) {
					
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		
		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();
		
		int counter = 0;
		
		for(ContaCorrenteCotaDTO dto : itensContaCorrenteCota) {
			
			dto.setLineId(counter++);
			
			String data 		     	 = dto.getData().toString();
			String valorPostergado	     = (dto.getValorPostergado() 	== null) 	? "0.0" : dto.getValorPostergado().toString();
			String NA 		     	     = (dto.getNA()			        == null) 	? "0.0" : dto.getNA().toString();
			String consignado 	     	 = (dto.getConsignado()			== null) 	? "0.0" : dto.getConsignado().toString();
			String encalhe 	 	         = (dto.getEncalhe()        	== null) 	? "0.0" : dto.getEncalhe().toString();
			String vendaEncalhe		 	 = (dto.getVendaEncalhe()		== null) 	? "0.0" : dto.getVendaEncalhe().toString();
			String debCred		 	     = (dto.getDebCred() 			== null) 	? "0.0" : dto.getDebCred().toString();
			String encargos		 	     = (dto.getEncargos()			== null) 	? "0.0" : dto.getEncargos().toString() ;
			String pendente				 = (dto.getPendente() 			== null) 	? "0.0" : dto.getPendente().toString();
			String total                 = (dto.getTotal() 			    == null) 	? "0.0" : dto.getTotal().toString();
			
			listaModeloGenerico.add(
					new CellModel( 	
							dto.getLineId(), 
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
			
			
		}
		
		tableModel.setPage(1);
		tableModel.setTotal(listaModeloGenerico.size());
		tableModel.setRows(listaModeloGenerico);
		
		return tableModel;
		
	}

}
