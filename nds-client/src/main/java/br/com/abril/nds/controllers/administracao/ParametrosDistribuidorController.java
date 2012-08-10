package br.com.abril.nds.controllers.administracao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.DistribuicaoFornecedorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * @author infoA2
 * Controller de parâmetros do distribuidor
 */
@Resource
@Path("/administracao/parametrosDistribuidor")
public class ParametrosDistribuidorController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private DistribuicaoFornecedorService distribuicaoFornecedorService;

	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;

	@Path("/")
	public void index() {
		result.include("parametrosDistribuidor", parametrosDistribuidorService.getParametrosDistribuidor());
		result.include("listaDiaOperacaoFornecedor", distribuicaoFornecedorService.buscarDiasOperacaoFornecedor());
		result.include("fornecedores", fornecedorService.obterFornecedores());
	}

	/**buscarDiasOperacaoFornecedor
	 * Grava as alterações de parametros realizadas para o distribuidor
	 * @param distribuidor
	 */
	public void gravar(ParametrosDistribuidorVO parametrosDistribuidor) {
		distribuidorService.alterar(parametrosDistribuidorService.getDistribuidor(parametrosDistribuidor));
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Parâmetros do Distribuidor alterados com sucesso"),"result").recursive().serialize();
	}
	
	/**
	 * Realiza a exclusão dos dias de distribuição e recolhimento do fornecedor
	 */
	public void excluirDiasDistribuicaoFornecedor(long codigoFornecedor) {
		distribuicaoFornecedorService.excluirDadosFornecedor(codigoFornecedor);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Dias de Distribuição do Fornecedor excluido com sucesso"),"result").recursive().serialize();
	}

	/**
	 * Retorna via json a lista de dias de distribuição e recolhimento do fornecedor
	 */
	public void recarregarDiasDistribuidorFornecedorGrid() {
		result.use(FlexiGridJson.class).from(distribuicaoFornecedorService.buscarDiasOperacaoFornecedor()).serialize();
	}
	
	/**
	 * Grava os dias de distribuição de recolhimento do fornecedor
	 * @param distribuidor
	 */
	@Post
	@Path("/gravarDiasDistribuidorFornecedor")
	public void gravarDiasDistribuidorFornecedor(String selectFornecedoresLancamento, String selectDiasLancamento, String selectDiasRecolhimento) throws Exception {
		
		List<String> listaFornecedoresLancamento = Arrays.asList(selectFornecedoresLancamento.split(","));
		List<String> listaDiasLancamento		 = Arrays.asList(selectDiasLancamento.split(","));
		List<String> listaDiasRecolhimento		 = Arrays.asList(selectDiasRecolhimento.split(","));
		
		validarDadosDiasDistribuidorFornecedor(listaFornecedoresLancamento, listaDiasLancamento, listaDiasRecolhimento);
		Distribuidor distribuidor = distribuidorService.obter();
		distribuicaoFornecedorService.gravarAtualizarDadosFornecedor(listaFornecedoresLancamento, listaDiasLancamento, listaDiasRecolhimento, distribuidor);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Dias de Distribuição do Fornecedor cadastrado com sucesso"),"result").recursive().serialize();
	}

	/**
	 * Valida os dados selecionados ao inserir dados de dias de distribuição por fornecedor
	 * @param selectFornecedoresLancamento
	 * @param selectDiasLancamento
	 * @param selectDiasRecolhimento
	 */
	private void validarDadosDiasDistribuidorFornecedor(List<String> selectFornecedoresLancamento, List<String> selectDiasLancamento, List<String> selectDiasRecolhimento) {
		List<String> listaMensagemValidacao = new ArrayList<String>();

		if (selectFornecedoresLancamento == null || selectFornecedoresLancamento.isEmpty()) {
			listaMensagemValidacao.add("É necessário selecionar no mínimo um Fornecedor!");
		}

		if (selectDiasLancamento == null || selectDiasLancamento.isEmpty()) {
			listaMensagemValidacao.add("É necessário selecionar no mínimo um dia de Lançamento!");
		}

		if (selectFornecedoresLancamento == null || selectFornecedoresLancamento.isEmpty()) {
			listaMensagemValidacao.add("É necessário selecionar no mínimo um dia de Recolhimento!");
		}

		if (!listaMensagemValidacao.isEmpty()) {
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}
	
	}
	
}
