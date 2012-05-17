package br.com.abril.nds.controllers.administracao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ControleAprovacaoVO;
import br.com.abril.nds.client.vo.ResultadoServicoVO;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/servico/cadastroServico")
public class ServicoController {


	@Autowired
	private Result result;
	
	@Path("/")
	public void index() {
		
	}
	
	@Path("/pesquisarServicos")
	public void pesquisarServicos(String codigo, 
								  String descricao, 
								  String periodicidade,
								  String sortorder, 
								  String sortname, 
								  int page, 
								  int rp) {
		
		List<ResultadoServicoVO> listaServicos = new ArrayList<ResultadoServicoVO>();
		
		ResultadoServicoVO resultadoServicoVO = new ResultadoServicoVO();

		resultadoServicoVO.setCodigo("123");
		resultadoServicoVO.setDescricao("Descrição de teste");
		resultadoServicoVO.setTaxa("123,2");
		resultadoServicoVO.setIsento("Sim");
		resultadoServicoVO.setBaseCalculo("Faturamento Liquido");
		resultadoServicoVO.setPercentualCalculoBase("12,5");
		
		listaServicos.add(resultadoServicoVO);
		
		resultadoServicoVO = new ResultadoServicoVO();

		resultadoServicoVO.setCodigo("123");
		resultadoServicoVO.setDescricao("Descrição de teste");
		resultadoServicoVO.setTaxa("123,2");
		resultadoServicoVO.setIsento("Não");
		resultadoServicoVO.setBaseCalculo("Faturamento Liquido");
		resultadoServicoVO.setPercentualCalculoBase("12,36");		

		listaServicos.add(resultadoServicoVO);

		TableModel<CellModelKeyValue<ResultadoServicoVO>> tableModel =
				new TableModel<CellModelKeyValue<ResultadoServicoVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaServicos));
		tableModel.setPage(1);
		tableModel.setTotal(2);
		
		this.result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	@Path("/salvarServico")
	public void salvarServico(String codigo, String descricao, Double taxaFixa,
			boolean isIsento, String periodicidade, String baseCalculo, Double percentualCalculoBase) {
		
	}
	
	private boolean validarServico(String codigo, String descricao, Double taxaFixa,
			boolean isIsento, String periodicidade, String baseCalculo, Double percentualCalculoBase) {
		
		// TODO: validar dados.
		
		return true;
	}
	
	private void processarServicos(List<ResultadoServicoVO> listaServicos) {
		
	}
	
}
