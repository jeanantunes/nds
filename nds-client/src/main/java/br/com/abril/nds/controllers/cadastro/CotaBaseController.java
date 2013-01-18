package br.com.abril.nds.controllers.cadastro;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaBaseService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/cotaBase")
public class CotaBaseController {
	
	@Autowired
	private CotaBaseService cotaBaseService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private Result result;
	
	@Path("/")
	@Rules(Permissao.ROLE_CADASTRO_COTA_BASE)
	public void index(){
		
	}
	
	@Post
	@Path("/pesquisarCotaNova")
	public void pesquisarCotaNova(Integer numeroCota){
		if(numeroCota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Número da cota inválido!");
		}
		
		FiltroCotaBaseDTO filtro = this.cotaBaseService.obterDadosFiltro(numeroCota);		
		
		this.result.use(Results.json()).from(filtro, "result").recursive().serialize();		
	}
	
	@Post
	@Path("/pesquisarCotasBase")
	public void pesquisarCotasBase(Integer numeroCota){
		
		List<CotaBaseDTO> listaCotaBase = this.cotaBaseService.obterCotasBases(this.cotaService.obterPorNumeroDaCota(numeroCota));
		
		int qtdeInicialPadrao = 3;
		
		for (int indice = listaCotaBase.size(); indice < 3 ; indice++) {
			
			CotaBaseDTO produtoVO = new CotaBaseDTO();

			listaCotaBase.add(produtoVO);
		}
		
		TableModel<CellModelKeyValue<CotaBaseDTO>> tableModel =
				new TableModel<CellModelKeyValue<CotaBaseDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotaBase));
		
		tableModel.setTotal(qtdeInicialPadrao);
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	
	
		

	
	

}
