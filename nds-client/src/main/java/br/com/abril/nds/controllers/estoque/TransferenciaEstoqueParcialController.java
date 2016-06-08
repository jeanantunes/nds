package br.com.abril.nds.controllers.estoque;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.TransferenciaEstoqueParcialService;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/estoque/transferencia/parcial")
@Rules(Permissao.ROLE_ESTOQUE_TRANSFERENCIA_PARCIAL)
public class TransferenciaEstoqueParcialController extends BaseController {

	@Autowired
	private Result result;
	
	@Autowired
	private TransferenciaEstoqueParcialService transferenciaEstoqueParcialService;
	
	@Path("/")
	public void index(){ }
	
	@Post
	public void buscarQtdeEstoqueParaTransferenciaParcial(String codigoProduto, Long numeroEdicao) {
		
		BigInteger qtdeEstoqueParaTransferencia = 
			this.transferenciaEstoqueParcialService.buscarQuantidadeParaTransferencia(codigoProduto, numeroEdicao);
		
		this.result.use(Results.json()).withoutRoot().from(qtdeEstoqueParaTransferencia).serialize();
	}
	
	@Post
	public void validarLancamento(String codigoProduto, Long numeroEdicao) {
		
		this.transferenciaEstoqueParcialService.temLancamentoOrigemBalanceado(codigoProduto, numeroEdicao);
		this.result.use(Results.json()).withoutRoot().from(0).serialize();
			
	}
	
	@Post
	public void transferir(String codigoProduto, Long numeroEdicaoOrigem, Long numeroEdicaoDestino) {
		
			this.transferenciaEstoqueParcialService.transferir(codigoProduto, numeroEdicaoOrigem, numeroEdicaoDestino, super.getUsuarioLogado().getId());
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Transferência efetuada com sucesso."), "result").recursive().serialize();
	}
	
}