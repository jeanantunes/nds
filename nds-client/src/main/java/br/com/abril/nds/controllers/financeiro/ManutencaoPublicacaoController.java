package br.com.abril.nds.controllers.financeiro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.caelum.vraptor.*;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("financeiro/manutencaoPublicacao")
@Rules(Permissao.ROLE_FINANCEIRO_MANUTENCAO_PUBLICACAO)
public class ManutencaoPublicacaoController {

	@Autowired
	private Result result;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;

	@Autowired
	private MovimentoEstoqueCotaService movimentoEstoqueCotaService;
	
	@Path("/")
	public void index() {}
	
	@Path("/pesquisarProduto")
	public void pesquisar(String codigo, Long numeroEdicao) {
		
		this.validarProdutoEdicao(codigo, numeroEdicao);
		
		BigDecimal precoProduto = produtoEdicaoService.obterPrecoEdicaoParaAlteracao(codigo, numeroEdicao);
		
		if(precoProduto == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Publicação não encontrada."));
		}
		
		result.use(CustomJson.class).from(CurrencyUtil.formatarValor(precoProduto)).serialize();
	}
	
	@Path("/confirmarAlteracaoPreco")
	@Rules(Permissao.ROLE_FINANCEIRO_MANUTENCAO_PUBLICACAO_ALTERACAO)
	public void confirmar(String codigo, Long numeroEdicao, BigDecimal precoProduto){
		
		produtoEdicaoService.executarAlteracaoPrecoCapa(codigo, numeroEdicao, precoProduto);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Alteração efetuada com Sucesso."), "result").recursive().serialize();
	}
	
	private void validarProdutoEdicao(final String codigo, final Long numeroEdicao) {
		
		List<String> mensagens = new ArrayList<>();
		
		if(codigo == null || codigo.isEmpty()) {
			mensagens.add("Informe um valor para o campo [Código]");
		}
		
		if(numeroEdicao == null){
			mensagens.add("Informe um valor para o campo [Edição]");
		}
		
		if(!mensagens.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,mensagens));
		}
	}


	@Get
	public void valorDescontosPorProduto(Long produtoEdicaoId){

		movimentoEstoqueCotaService.obterDescontoPublicaoExpedida(produtoEdicaoId);

	}


	@Path("/pesquisarDescontosProduto")
	@Post
	public void pesquisarDescontosProduto(String codigo, Long numeroEdicao) {

		this.validarProdutoEdicao(codigo, numeroEdicao);

		List<BigDecimal> bigDecimals = produtoEdicaoService.obterDescontosParaAlteracao(codigo, numeroEdicao);

		result.use(Results.json()).from(bigDecimals).serialize();
	}

	@Path("/pesquisarDescontosPorCota")
	@Post
	public void pesquisarDescontosPorCota(Long cotaId) {


		List<BigDecimal> bigDecimals = produtoEdicaoService.obterDescontosParaAlteracaoPorCota(cotaId);

		result.use(Results.json()).from(bigDecimals).serialize();
	}


	@Path("/atualizarDescontos")
	@Post
	public void atualizarDescontos(String codigoProduto, Long numeroEdicao,Long cotaId,Double descontoAtual,Double novoDesconto) {


		if (cotaId!=null){
			this.movimentoEstoqueCotaService.atualizarDescontosDaCota(cotaId,descontoAtual,novoDesconto);
		}else{
			this.movimentoEstoqueCotaService.atualizarDescontosDaPublicacao(codigoProduto,numeroEdicao,descontoAtual,novoDesconto);
		}

		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Alteração efetuada com Sucesso."), "result").recursive().serialize();
	}
}