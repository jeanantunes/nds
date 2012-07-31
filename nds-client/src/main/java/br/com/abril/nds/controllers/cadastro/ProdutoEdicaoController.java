package br.com.abril.nds.controllers.cadastro;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.DetalheProdutoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Dimensao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
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
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
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

		// Validar:
		if ((codigoProduto == null || codigoProduto.trim().isEmpty()) 
				|| (nomeProduto == null || nomeProduto.trim().isEmpty())) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o campo 'Código' ou 'Produto'!");
		}
		
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
		dto.setFase(produto.getFase());
		dto.setPacotePadrao(produto.getPacotePadrao());
		dto.setPeso(produto.getPeso());
		dto.setDescricaoDesconto("");
		dto.setDesconto(produto.getDescontoLogistica() == null 
				? BigDecimal.ZERO : BigDecimal.valueOf(
						produto.getDescontoLogistica().getPercentualDesconto()));
		
		if (idProdutoEdicao != null && Util.isLong(idProdutoEdicao)) {
			
			Long id = Long.valueOf(idProdutoEdicao);
			ProdutoEdicao pe = peService.obterProdutoEdicao(id);
			dto.setId(id);
			dto.setCodigoProduto(pe.getCodigo());
			dto.setNomeComercialProduto(pe.getNomeComercial());
			dto.setNumeroEdicao(pe.getNumeroEdicao());
			dto.setPacotePadrao(pe.getPacotePadrao());
			dto.setPrecoPrevisto(pe.getPrecoPrevisto());
			dto.setPrecoVenda(pe.getPrecoVenda());
			dto.setExpectativaVenda(pe.getExpectativaVenda());
			dto.setCodigoDeBarras(pe.getCodigoDeBarras());
			dto.setCodigoDeBarrasCorporativo(pe.getCodigoDeBarraCorporativo());
			dto.setChamadaCapa(pe.getChamadaCapa());
			dto.setParcial(pe.isParcial());
			dto.setPossuiBrinde(pe.isPossuiBrinde());
			dto.setDesconto(pe.getDesconto());
			dto.setPeso(pe.getPeso());
			dto.setBoletimInformativo(pe.getBoletimInformativo());
			dto.setOrigemInterface(pe.getOrigemInterface());
			dto.setNumeroLancamento(pe.getNumeroLancamento());
			dto.setPeb(pe.getPeb());
			dto.setEditor(pe.getProduto().getEditor().getNome());
			
			
			
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
				dto.setDataRecolhimentoPrevisto(uLancamento.getDataRecolhimentoPrevista());
				dto.setDataRecolhimentoReal(uLancamento.getDataRecolhimentoDistribuidor());
				dto.setSemanaRecolhimento(DateUtil.obterNumeroSemanaNoAno(uLancamento.getDataRecolhimentoDistribuidor()));
			}
		} else {
			
			// Edição criada pelo Distribuidor:
			dto.setOrigemInterface(false);
			
			dto.setPeb(produto.getPeb());
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
	
	@Post
	public void salvar(UploadedFile imagemCapa,
			String codigoProduto, Long idProdutoEdicao,
			String codigoProdutoEdicao, String nomeComercialProduto,
			Long numeroEdicao, int pacotePadrao,
			TipoLancamento tipoLancamento,
			BigDecimal precoPrevisto, BigDecimal precoVenda,
			Date dataLancamentoPrevisto, 
			BigDecimal repartePrevisto, BigDecimal repartePromocional,
			String codigoDeBarras, String codigoDeBarrasCorporativo,
			BigDecimal desconto, BigDecimal peso, 
			BigDecimal largura, BigDecimal comprimento, BigDecimal espessura,
			String chamadaCapa, boolean parcial, boolean possuiBrinde,
			String boletimInformativo, Integer numeroLancamento) {
		
		// DTO para transportar os dados:
		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
		dto.setId(idProdutoEdicao);
		dto.setCodigoProduto(codigoProdutoEdicao);
		dto.setNomeComercialProduto(nomeComercialProduto);
		dto.setNumeroEdicao(numeroEdicao);
		dto.setPacotePadrao(pacotePadrao);
		dto.setTipoLancamento(tipoLancamento);
		dto.setPrecoPrevisto(precoPrevisto);
		dto.setPrecoVenda(precoVenda);
		dto.setDataLancamentoPrevisto(dataLancamentoPrevisto);
		dto.setRepartePrevisto(repartePrevisto);
		dto.setRepartePromocional(repartePromocional);
		dto.setCodigoDeBarras(codigoDeBarras);
		dto.setCodigoDeBarrasCorporativo(codigoDeBarrasCorporativo);
		dto.setDesconto(desconto);
		dto.setPeso(peso);
		dto.setLargura(largura == null ? 0 : largura.floatValue());
		dto.setComprimento(comprimento == null ? 0 : comprimento.floatValue());
		dto.setEspessura(espessura == null ? 0 : espessura.floatValue());
		dto.setChamadaCapa(chamadaCapa);
		dto.setParcial(parcial);
		dto.setPossuiBrinde(possuiBrinde);
		dto.setNumeroLancamento(numeroLancamento);
		
		this.validarProdutoEdicao(dto);
		
		
		// Dados da Imagem:
		String contentType = null;
		InputStream imgInputStream = null;
		if (imagemCapa != null) {
			contentType = imagemCapa.getContentType();
			imgInputStream = imagemCapa.getFile();
		}
		
		ValidacaoVO vo = null;
		try {
			
			peService.salvarProdutoEdicao(dto, codigoProduto, contentType, imgInputStream);
			vo = new ValidacaoVO(TipoMensagem.SUCCESS, "Edição salva com sucesso!");
		} catch (ValidacaoException e) {
			
			vo = e.getValidacao();
		} catch (Exception e) {
			
			vo = new ValidacaoVO(TipoMensagem.ERROR, e.getMessage());
		} finally {
			
			this.result.use(Results.json()).from(vo, "result").recursive().serialize();
		}
	}
	
	/**
	 * Valida o preenchimento dos campos obrigatórios.
	 * 
	 * @param dto
	 */
	private void validarProdutoEdicao(ProdutoEdicaoDTO dto) {
		
		List<String> listaMensagens = new ArrayList<String>();
		boolean origemInterface = false;
		ProdutoEdicao pe = null;
		try {
			pe = peService.obterProdutoEdicao(dto.getId());
			origemInterface = pe.getOrigemInterface().booleanValue();
		} catch (Exception e) {
			origemInterface = false;
		}
		
		if (pe == null || !origemInterface) {
			
			// Distribuidor:
			if (dto.getCodigoProduto() == null || dto.getCodigoProduto().trim().length() <= 0) {
				listaMensagens.add("Campo 'Código' deve ser preenchido!");
			}
			if (dto.getNomeComercialProduto() == null || dto.getNomeComercialProduto().trim().length() <= 0) {
				listaMensagens.add("Campo 'Nome Comercial Produto' deve ser preenchido!");
			}
			if (dto.getNumeroEdicao() == null || Long.valueOf(0).equals(dto.getNumeroEdicao())) {
				listaMensagens.add("Por favor, digite um valor válido para o 'Número de Edição'!");
			}
			if (dto.getPacotePadrao() <= 0) {
				listaMensagens.add("Por favor, digite um valor válido para o 'Pacote Padrão'!");
			}
			if (dto.getTipoLancamento() == null) {
				listaMensagens.add("Por favor, selecione um 'Tipo de Lançamento'!");
			}
			if (dto.getPrecoPrevisto() == null) {
				listaMensagens.add("Por favor, digite um valor válido para o 'Preço Previsto'!");
			}
			if (dto.getDataLancamentoPrevisto() == null) {
				listaMensagens.add("Campo 'Data de Lançamento Previsto' deve ser preenchido!");
			}
			if (dto.getRepartePrevisto() == null) {
				listaMensagens.add("Por favor, digite um valor válido para o 'Reparte Previsto'!");
			}
			if (dto.getRepartePromocional() == null) {
				listaMensagens.add("Por favor, digite um valor válido para o 'Reparte Promocional'!");
			}
			if (dto.getCodigoDeBarrasCorporativo() == null || dto.getCodigoDeBarrasCorporativo().trim().length() <= 0) {
				listaMensagens.add("Campo 'Código de Barras Corporativo' deve ser preenchido!");
			}
		} else {
			
			// Interface:
			if (dto.getPrecoVenda() == null) {
				listaMensagens.add("Por favor, digite um valor válido para o 'Preço Real'!");
			}
		}
		
		if (dto.getCodigoDeBarras() == null || dto.getCodigoDeBarras().trim().length() <= 0) {
			listaMensagens.add("Campo 'Código de Barras' deve ser preenchido!");
		}
		
		if (!listaMensagens.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, listaMensagens));
		}
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
		
		ValidacaoVO vo = null;
		try {

			this.peService.excluirProdutoEdicao(idProdutoEdicao);
			vo = new ValidacaoVO(TipoMensagem.SUCCESS, "Edição excluída com sucesso!");
		} catch (ValidacaoException e) {
			
			vo = e.getValidacao();
		} catch (Exception e) {
			
			vo = new ValidacaoVO(TipoMensagem.ERROR, e.getMessage());
		} finally {
			
			this.result.use(Results.json()).from(vo, "result").recursive().serialize();
		}
	}
	
	/**
	 * Obtem detalhes de produto edição
	 * @param idProdutoEdicao
	 */
	@Post
	@Path("/obterDetalheProduto.json")
	public void obterDetalheProduto(Long idProdutoEdicao){
		
		DetalheProdutoVO produtoLancamentoVO = null;
		
	    produtoLancamentoVO = this.getDetalheProduto(idProdutoEdicao);

		if (produtoLancamentoVO!=null){
		    this.result.use(Results.json()).from(produtoLancamentoVO, "result").recursive().serialize();
		}
		else{
			result.nothing();
		}
	}
	
	/**
	 * Popula e retorna Value Object com detalhes de produto edição
	 * @param idProdutoEdicao
	 * @return DetalheProdutoVO
	 */
	private DetalheProdutoVO getDetalheProduto(Long idProdutoEdicao){
		
		DetalheProdutoVO produtoLancamentoVO = null;
		
		ProdutoEdicao produtoEdicao = produtoEdicaoService.obterProdutoEdicao(idProdutoEdicao);
		
		if (produtoEdicao!=null){
			
			BigDecimal precoComDesconto = BigDecimal.ZERO;
			
			precoComDesconto = precoComDesconto.add(produtoEdicao.getPrecoVenda()).subtract(produtoEdicao.getDesconto());
			
		    produtoLancamentoVO = new DetalheProdutoVO(produtoEdicao.getId(),
													   produtoEdicao.getProduto().getNome(),
													   produtoEdicao.getCodigo(),
										               (produtoEdicao.getPrecoVenda()!=null?CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda()):""),
										               (precoComDesconto!=null?CurrencyUtil.formatarValor(precoComDesconto):""),
										               (produtoEdicao.getProduto()!=null?(produtoEdicao.getProduto().getFornecedor()!=null?produtoEdicao.getProduto().getFornecedor().getJuridica().getNome():""):""),
										               (produtoEdicao.getProduto()!=null?(produtoEdicao.getProduto().getEditor()!=null?produtoEdicao.getProduto().getEditor().getCodigo().toString():""):""),
										               (produtoEdicao.getProduto()!=null?(produtoEdicao.getProduto().getEditor()!=null?produtoEdicao.getProduto().getEditor().getNome():""):""),
										               produtoEdicao.getChamadaCapa(),
										               (produtoEdicao.isPossuiBrinde()?"Sim":"Não"),
										               Integer.toString(produtoEdicao.getPacotePadrao())
										               );
		}
		return produtoLancamentoVO;
	}

}
