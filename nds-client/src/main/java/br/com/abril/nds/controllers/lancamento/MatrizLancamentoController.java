package br.com.abril.nds.controllers.lancamento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class MatrizLancamentoController {

	@Autowired
	private Result result;

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private MatrizLancamentoService matrizLancamentoService;
	
	private static final String FORMATO_DATA = "dd/MM/yyyy";

	@Path("/matrizLancamento")
	public void index() {
		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(
				true, SituacaoCadastro.ATIVO);
		Distribuidor distribuidor = distribuidorService.obter();
		String dataOperacao = DateUtil.formatarData(distribuidor.getDataOperacao(), FORMATO_DATA);
		result.include("dataOperacao", dataOperacao);
		result.include("fornecedores", fornecedores);
	}
	
	
	@Post
	public void matrizLancamento(Long[] fornecedores, String dataInicio,
			String dataFim) {
	}

}
