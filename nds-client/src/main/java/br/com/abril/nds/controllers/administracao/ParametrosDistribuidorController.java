package br.com.abril.nds.controllers.administracao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.RegistroDiaOperacaoFornecedorVO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.service.DistribuicaoFornecedorService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ParametroSistemaService;
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
		System.out.println("excluir fornecedor");
		
		distribuicaoFornecedorService.excluirDadosFornecedor(codigoFornecedor);
		
	}

	/**
	 * Grava os dias de distribuição de recolhimento do fornecedor
	 * @param distribuidor
	 */
	@Post
	@Path("/gravarDiasDistribuidorFornecedor")
	public void gravarDiasDistribuidorFornecedor(List<String> selectFornecedoresLancamento, List<String> selectDiasLancamento, List<String> selectDiasRecolhimento) {
		System.out.println("teste");
		//distribuidorService.alterar(distribuidor);
	}

}
