package br.com.abril.nds.controllers.cadastro;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Dimensao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/edicao")
public class ProdutoEdicaoController {

	@Autowired
	private Result result;
	
	@Autowired
	private ProdutoEdicaoService peService;
	
	@Autowired
	private ProdutoService pService;
	
	@Autowired
	private LancamentoService lService;
	
	/** Traz a página inicial. */
	@Get
	@Path("/")
	public void index() { }
	
	@Post
	@Path("/pesquisarEdicoes.json")
	public void pesquisarEdicoes(String codigoProduto, String nomeProduto,
			Date dataLancamento, String situacaoLancamento,
			String codigoDeBarras, boolean brinde,
            String sortorder, String sortname, int page, int rp) {

		/* TODO: Remover ao finalizar a implementação:
		
		// Validar:
		if ((codigoProduto == null || codigoProduto.trim().isEmpty()) 
				|| (nomeProduto == null || nomeProduto.trim().isEmpty())) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o campo 'Código' ou 'Produto'!");
		}
		*/
		
		// Popular o DTO:
		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
		dto.setCodigoProduto(codigoProduto);
		dto.setNomeProduto(nomeProduto);
		dto.setDataLancamento(dataLancamento);
		dto.setCodigoDeBarras(codigoDeBarras);
		dto.setPossuiBrinde(brinde);
		dto.setSituacaoLancamento(null);
		for (StatusLancamento status : StatusLancamento.values()) {
			if (status.getDescricao().equals(situacaoLancamento)) {
				dto.setSituacaoLancamento(status);
			}
		}
		
		// Pesquisar:
		Long qtd = peService.countPesquisarEdicoes(dto);
		List<ProdutoEdicaoDTO> lst = peService.pesquisarEdicoes(dto, sortorder, sortname, page, rp);
		
		this.result.use(FlexiGridJson.class).from(lst).total(qtd.intValue()).page(page).serialize();
	}
	
	@Post
	@Path("/carregarDadosProdutoEdicao.json")
	public void carregarDadosProdutoEdicao(String codigoProduto, String idProdutoEdicao) {
		
		if (codigoProduto == null || codigoProduto.trim().isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, escolha um produto para adicionar a Edição!");
		}
		
		Produto produto = pService.obterProdutoPorCodigo(codigoProduto);
		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
		dto.setNomeProduto(produto.getNome());
		
		String nomeFornecedor = "";
		if (produto.getFornecedor() != null 
				&& produto.getFornecedor().getJuridica() != null) {
			nomeFornecedor = produto.getFornecedor().getJuridica().getNomeFantasia();
		}
		dto.setNomeFornecedor(nomeFornecedor);
		
		// TODO: Solicitar esses campos:
		//dto.setFase(produto.fase);
		//dto.setNumeroLancamento(produto.numerolancamento);
		dto.setPacotePadrao(produto.getPacotePadrao());
		
		
		//
		dto.setPeso(produto.getPeso());
		
		
		if (idProdutoEdicao != null && !idProdutoEdicao.trim().isEmpty()) {
			
			Long id = Long.valueOf(idProdutoEdicao);
			ProdutoEdicao pe = peService.obterProdutoEdicao(id);
			dto.setCodigoProduto(pe.getCodigo());
			dto.setNomeComercialProduto(pe.getNomeComercial());
			dto.setNumeroEdicao(pe.getNumeroEdicao());
			dto.setPacotePadrao(pe.getPacotePadrao());
			dto.setPrecoPrevisto(pe.getPrecoPrevisto());
			dto.setPrecoVenda(pe.getPrecoVenda());
			dto.setReparteDistribuido(pe.getReparteDistribuido());
			dto.setCodigoDeBarras(pe.getCodigoDeBarras());
			dto.setCodigoDeBarrasCorporativo(pe.getCodigoDeBarraCorporativo());
			dto.setChamadaCapa(pe.getChamadaCapa());
			dto.setParcial(pe.isParcial());
			dto.setPossuiBrinde(pe.isPossuiBrinde());
			// descrição tipo de desconto
			dto.setDesconto(pe.getDesconto());
			dto.setPeso(pe.getPeso());
			dto.setBoletimInformativo(pe.getBoletimInformativo());
			
			Dimensao dimEdicao = pe.getDimensao();
			if (dimEdicao == null) {
				dto.setComprimento(0);
				dto.setEspessura(0);
				dto.setLargura(0);
			} else {
				dto.setComprimento(dimEdicao.getComprimento());
				dto.setEspessura(dimEdicao.getEspessura());
				dto.setLargura(dimEdicao.getLargura());
			}
			
			Lancamento uLancamento = lService.obterUltimoLancamentoDaEdicao(pe.getId());
			if (uLancamento != null) {
				dto.setSituacaoLancamento(uLancamento.getStatus());
				dto.setTipoLancamento(uLancamento.getTipoLancamento());
				dto.setDataLancamentoPrevisto(uLancamento.getDataLancamentoPrevista());
				dto.setDataLancamento(uLancamento.getDataLancamentoDistribuidor());
				dto.setRepartePrevisto(uLancamento.getReparte());
				dto.setRepartePromocional(uLancamento.getRepartePromocional());
			}
		}
		
		/* 
		 * Regra: Se não houver edições já cadatradas para este produto, deve-se
		 * obrigar a cadastrar o número 1. 
		 */
		ProdutoEdicaoDTO countEdicao = new ProdutoEdicaoDTO();
		countEdicao.setCodigoProduto(codigoProduto);
		Long qtdEdicoes = peService.countPesquisarEdicoes(countEdicao);
		if (qtdEdicoes == 0 || Long.valueOf(0).equals(qtdEdicoes)) {
			dto.setNumeroEdicao(1L);
		}
		
		
		this.result.use(Results.json()).from(dto, "result").serialize();
	}
	
	@Post
	@Path("/ultimasEdicoes.json")
	public void ultimasEdicoes(String codigoProduto) {
		
		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
		dto.setCodigoProduto(codigoProduto);
		
		List<ProdutoEdicaoDTO> lst = peService.pesquisarUltimasEdicoes(dto, 5);

		this.result.use(FlexiGridJson.class).from(lst).total(lst.size()).page(1).serialize();
	}
	
	/**
	 * Remove uma Edição.
	 * 
	 * @param idProdutoEdicao
	 */
	@Post
	@Path("/removerEdicao.json")
	public void removerEdicao(Long idProdutoEdicao) {

		if (idProdutoEdicao == null || Long.valueOf(0).equals(idProdutoEdicao)) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Por favor, selecione uma Edição válida!");
		}

		try {

			this.peService.excluirProdutoEdicao(idProdutoEdicao);

		} catch (Exception e) {
			
			if (e instanceof ValidacaoException) {
				
				this.result.use(Results.json()).from(
						((ValidacaoException) e).getValidacao(), "result")
						.recursive().serialize();
				
			} else {
				
				this.result.use(Results.json()).from(
						new ValidacaoVO(TipoMensagem.ERROR, e.getMessage() + ""), 
							"result").recursive().serialize();
			}
			
			throw new ValidacaoException();
		}

		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS,
						"Edição excluída com sucesso!"), "result").recursive().serialize();
	}

}
