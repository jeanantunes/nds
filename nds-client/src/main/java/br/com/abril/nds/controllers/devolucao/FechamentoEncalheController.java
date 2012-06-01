package br.com.abril.nds.controllers.devolucao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.DistribuidorService;
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
	
	
	@Path("/")
	public void index() {
		
		Distribuidor dist = distribuidorService.obter();
		List<Fornecedor> listaFornecedores = fornecedorService.obterFornecedores();
		List<Box> listaBoxes = boxService.buscarPorTipo(TipoBox.RECOLHIMENTO);
		
		result.include("dataOperacao", DateUtil.formatarData(dist.getDataOperacao(), "dd/MM/yyyy"));
		result.include("listaFornecedores", listaFornecedores);
		result.include("listaBoxes", listaBoxes);
	}
}
