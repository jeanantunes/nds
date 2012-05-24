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

/**
 * Controller responsável pela tela de serviços.
 * 
 * @author Discover Technology
 */
@Resource
@Path("/servico/cadastroServico")
public class ServicoController {


	@Autowired
	private Result result;
	
	/**
	 * Método chamado assim que iniciada a tela.
	 */
	@Path("/")
	public void index() {
		
	}
	
	/**
	 * Método responsável por pesquisar serviços.
	 * 
	 * @param codigo
	 * @param descricao
	 * @param periodicidade
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
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

		resultadoServicoVO.setId(1L);
		resultadoServicoVO.setCodigo("123");
		resultadoServicoVO.setDescricao("Descrição de teste");
		resultadoServicoVO.setTaxa(123.2);
		resultadoServicoVO.setBaseCalculo("Faturamento Liquido");
		resultadoServicoVO.setPercentualCalculoBase(12);
		
		listaServicos.add(resultadoServicoVO);
		
		resultadoServicoVO = new ResultadoServicoVO();

		resultadoServicoVO.setId(2L);
		resultadoServicoVO.setCodigo("321");
		resultadoServicoVO.setDescricao("Descrição de teste");
		resultadoServicoVO.setTaxa(123.2);
		resultadoServicoVO.setBaseCalculo("Faturamento Liquido");
		resultadoServicoVO.setPercentualCalculoBase(50);		

		listaServicos.add(resultadoServicoVO);

		TableModel<CellModelKeyValue<ResultadoServicoVO>> tableModel =
				new TableModel<CellModelKeyValue<ResultadoServicoVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaServicos));
		tableModel.setPage(1);
		tableModel.setTotal(2);
		
		this.result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	/**
	 * Método responsável por salvar um serviço
	 * 
	 * @param codigo
	 * @param descricao
	 * @param taxaFixa
	 * @param isento
	 * @param periodicidade
	 * @param baseCalculo
	 * @param percentualCalculo
	 */
	@Post
	public void salvarServico(Long id, String codigo, String descricao, Double taxaFixa,
			String periodicidade, String baseCalculo, Integer percentualCalculo, 
			String periodicidadeDiaria, String diaMes) {
		
		this.validarServico(codigo, descricao, taxaFixa, false, 
				periodicidade, baseCalculo, percentualCalculo);

		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Serviço incluido com sucesso."), 
							"result").recursive().serialize();
	}
	
	/**
	 * Método responsável por remover um serviço.
	 * 
	 * @param id
	 */
	@Post
	public void removerServico(Long id) {
		
		System.out.println(id);
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Serviço excluido com sucesso."), 
								"result").recursive().serialize();
	}
	
	/**
	 * Método capaz de buscar um serviço pelo ID.
	 * 
	 * @param id
	 */
	@Post
	public void buscarServicoPeloCodigo(Long id) {
		
		ResultadoServicoVO resultadoServicoVO = new ResultadoServicoVO();
		
		resultadoServicoVO.setId(1L);
		resultadoServicoVO.setCodigo("123");
		resultadoServicoVO.setDescricao("Descrição de teste");
		resultadoServicoVO.setTaxa(123.2);
		resultadoServicoVO.setBaseCalculo("B");
		resultadoServicoVO.setPeriodicidade("S");
		resultadoServicoVO.setPercentualCalculoBase(20);
	
		this.result.use(Results.json()).from(resultadoServicoVO, "result").recursive().serialize();
	}
	
	/**
	 * Método capaz de validar um serviço.
	 * 
	 * @param codigo
	 * @param descricao
	 * @param taxaFixa
	 * @param isento
	 * @param periodicidade
	 * @param baseCalculo
	 * @param percentualCalculoBase
	 */
	private void validarServico(String codigo, String descricao, Double taxaFixa,
			boolean isento, String periodicidade, String baseCalculo, Integer percentualCalculoBase) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		// FIXME: receber diaMes e periodicidadeDiaria e Validar.
		
		if (codigo == null || codigo.isEmpty()) {
			listaMensagens.add("O preenchimento do campo [Código] é obrigatório!");
		}

		if (descricao == null || descricao.isEmpty()) {
			listaMensagens.add("O preenchimento do campo [Descrição] é obrigatório!");
		}

		if (taxaFixa == null || taxaFixa.isNaN() || taxaFixa <= 0D) {
			listaMensagens.add("O preenchimento do campo [Taxa Fixa R$] é obrigatório!");
		}

		if (periodicidade == null || periodicidade.isEmpty()) {
			listaMensagens.add("O preenchimento do campo [Periodicidade] é obrigatório!");
		}

		if (baseCalculo == null || baseCalculo.isEmpty()) {
			listaMensagens.add("O preenchimento do campo [Base de Cálculo] é obrigatório!");
		}

		if (percentualCalculoBase == null || percentualCalculoBase <= 0) {
			listaMensagens.add("O preenchimento do campo [(%) para cálculo sobre base] é obrigatório!");
		}

		if (listaMensagens != null && !listaMensagens.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, listaMensagens));
		}
	}
	
	/**
	 * Método capaz de processar o serviço para exibir na tela.
	 * 
	 * @param listaServicos
	 */
	private void processarServicos(List<ResultadoServicoVO> listaServicos) {
		
	}
	
}
