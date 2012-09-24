package br.com.abril.nds.controllers.nfe;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ImpressaoNFEService;
import br.com.abril.nds.service.RotaService;
import br.com.abril.nds.service.RoteiroService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path(value="/nfe/impressaoNFE")
public class ImpressaoNFEController {
	
	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private TipoNotaFiscalService tipoNotaFiscalService;

	@Autowired
	ImpressaoNFEService impressaoNFEService;

	@Autowired
	DistribuidorService distribuidorService;

	@Autowired
	RoteiroService roteiroService;

	@Autowired
	RotaService rotaService;
	
	@Autowired
	private Result result;

	@Path("/")
	@Rules(Permissao.ROLE_NFE_IMPRESSAO_NFE)
	public void index(){
		
		List<Fornecedor> fornecedores = this.fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO);

		this.result.include("produtos", impressaoNFEService.obterProdutosExpedicaoConfirmada(fornecedores));
		this.result.include("fornecedores", fornecedores);
		this.result.include("tipoNotas", tipoNotaFiscalService.carregarComboTiposNotasFiscais(distribuidorService.obter().getTipoAtividade()));
		this.result.include("roteiros", roteiroService.obterRoteiros());
		this.result.include("rotas", rotaService.obterRotas());
		this.result.include("tipoEmissao", TipoEmissaoNfe.values());
		
	}
	
	@Post
	@Path("/pesquisarImpressaoNFE")
	public void pesquisarImpressaoNFE(FiltroImpressaoNFEDTO filtro, String sortorder, String sortname, int page, int rp){
		System.out.println("ID DO ROTEIRO SELECIONADO: "+filtro.getIdRoteiro());
	}

}
