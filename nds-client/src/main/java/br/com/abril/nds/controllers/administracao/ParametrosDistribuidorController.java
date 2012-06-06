package br.com.abril.nds.controllers.administracao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.RegistroDiaOperacaoFornecedorVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.service.DistribuicaoFornecedorService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ParametroSistemaService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

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
	private ParametroSistemaService parametroSistemaService;
	
	@Autowired
	private DistribuicaoFornecedorService distribuicaoFornecedorService;

	@Autowired
	private FornecedorService fornecedorService;

	@Path("/")
	public void index() {

		List<RegistroDiaOperacaoFornecedorVO> listaDiaOperacaoFornecedor = distribuicaoFornecedorService.buscarDiasOperacaoFornecedor();
		/*for (DistribuicaoFornecedor distribuicaoFornecedor : listaDistribuicaoFornecedor) {
			distribuicaoFornecedor.getDiaSemana();
			distribuicaoFornecedor.getFornecedor();
		}*/
		
		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores();

		result.include("listaDiaOperacaoFornecedor", listaDiaOperacaoFornecedor);
		result.include("fornecedores", fornecedores);
		result.include("CNPJ", parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.CNPJ));
		result.include("distribuidor", distribuidorService.obter());
	}

	/**
	 * Grava as alterações de parametros realizadas para o distribuidor
	 * @param distribuidor
	 */
	public void gravar(Distribuidor distribuidor) {
		distribuidorService.alterar(distribuidor);
	}
	
	/**
	 * Realiza a exclusão dos dias de distribuição e recolhimento do fornecedor
	 */
	public void excluirDiasDistribuicaoFornecedor(long codigoFornecedor) {
		distribuicaoFornecedorService.excluirDadosFornecedor(codigoFornecedor);
		result.redirectTo(ParametrosDistribuidorController.class).index();
	}

	/**
	 * Grava os dias de distribuição de recolhimento do fornecedor
	 * @param distribuidor
	 */
	@Post
	@Path("/gravarDiasDistribuidorFornecedor")
	public void gravarDiasDistribuidorFornecedor(List<String> selectFornecedoresLancamento, List<String> selectDiasLancamento, List<String> selectDiasRecolhimento) throws Exception {

		validarDadosDiasDistribuidorFornecedor(selectFornecedoresLancamento, selectDiasLancamento, selectDiasRecolhimento);
		Distribuidor distribuidor = distribuidorService.obter();
		distribuicaoFornecedorService.gravarAtualizarDadosFornecedor(selectFornecedoresLancamento, selectDiasLancamento, selectDiasRecolhimento, distribuidor);
		
		result.redirectTo(ParametrosDistribuidorController.class).index();
		
	}

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
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR,
					listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}
	
	}
	
}
