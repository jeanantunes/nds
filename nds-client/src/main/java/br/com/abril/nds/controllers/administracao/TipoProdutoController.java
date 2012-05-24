package br.com.abril.nds.controllers.administracao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/tipoProduto")
public class TipoProdutoController {

	@Autowired
	private Result resutl;
	
	@Autowired
	private TipoProdutoService tipoProdutoService;
	
	@Path("/")
	public void index() {
		
	}
	
	@Post("/busca.json")
	public void busca(String descricao, String codigo, 
			String codigoNCM, String codigoNBM, String sortname, String sortorder, int rp, int page ) {
		
		List<TipoProduto> listaTipoProdutos = this.tipoProdutoService.busca(descricao, codigo, codigoNCM, codigoNBM,
				sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp, rp);
		
		Long quantidade = this.tipoProdutoService.quantidade(descricao, codigo, codigoNCM, codigoNBM);
		
		this.resutl.use(FlexiGridJson.class).from(listaTipoProdutos)
			.total(quantidade.intValue()).page(page).serialize();
	}
	
	@Post("/getById.json")
	public void buscaPorId(Long id) {
		
		TipoProduto tipoProduto = this.tipoProdutoService.buscaPorId(id);
		
		this.resutl.use(Results.json()).from(tipoProduto, "tipoProduto").serialize();
	}
	
	@Post("/salva.json")
	public void salvar(TipoProduto tipoProduto) {
		
		this.valida(tipoProduto);
			
		this.tipoProdutoService.merge(tipoProduto);
		
		this.resutl.use(Results.json()).from(new ValidacaoVO
				(TipoMensagem.SUCCESS, "Tipo de Produto salva com Sucesso."), "result").recursive().serialize();
	}
	
	@Post("/remove.json")
	public void remove(long id) {
		
		try {
			this.tipoProdutoService.remover(id);
		} catch (Exception e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
		}
		
		this.resutl.use(Results.nothing());
	}
	
	
	private void valida(TipoProduto tipoProduto) {
				
		if (tipoProduto == null || StringUtil.isEmpty(tipoProduto.getDescricao())) {
			throw new ValidacaoException(
					new ValidacaoVO(TipoMensagem.ERROR, "O preenchimento do campo [Tipo de Produto] é obrigatório."));
		}
	
	}
}
