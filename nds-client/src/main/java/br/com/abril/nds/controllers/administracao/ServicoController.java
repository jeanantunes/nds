package br.com.abril.nds.controllers.administracao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ResultadoServicoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
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

		resultadoServicoVO.setCodigo("321");
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

	@Post
	public void salvarServico(String codigo, String descricao, Double taxaFixa,
			Boolean isento, String periodicidade, String baseCalculo, Integer percentualCalculo) {
		
		this.validarServico(codigo, descricao, taxaFixa, isento, 
				periodicidade, baseCalculo, percentualCalculo);

		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Serviço incluido com sucesso."), 
							"result").recursive().serialize();
	}
	
	@Post
	public void removerServico(String codigo) {
		
		System.out.println(codigo);
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Serviço excluido com sucesso."), 
								"result").recursive().serialize();
	}
	
	@Post
	public void buscarServico(String codigo) {
		
		ResultadoServicoVO resultadoServicoVO = new ResultadoServicoVO();

		resultadoServicoVO.setCodigo("123");
		resultadoServicoVO.setDescricao("Descrição de teste");
		resultadoServicoVO.setTaxa("123,2");
		resultadoServicoVO.setIsento("true");
		resultadoServicoVO.setBaseCalculo("B");
		resultadoServicoVO.setPeriodicidade("S");
		resultadoServicoVO.setPercentualCalculoBase("12,5");
	
		this.result.use(Results.json()).from(resultadoServicoVO, "result").recursive().serialize();
	}
	
	private void validarServico(String codigo, String descricao, Double taxaFixa,
			boolean isIsento, String periodicidade, String baseCalculo, Integer percentualCalculoBase) {
		
		if (codigo == null || codigo.isEmpty()) {
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O preenchimento do campo [Código] é obrigatório!");
		}
	}
	
	private void processarServicos(List<ResultadoServicoVO> listaServicos) {
		
	}
	
}
