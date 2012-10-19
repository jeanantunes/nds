package br.com.abril.nds.controllers.cadastro;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.DetalheProdutoVO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Dimensao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;
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
	@Rules(Permissao.ROLE_CADASTRO_EDICAO)
	public void index() { }

	
	
	@Post
	@Path("/pesquisarEdicoes.json")
	public void pesquisarEdicoes(String codigoProduto, String nomeComercial,
			Date dataLancamentoDe, Date dataLancamentoAte, BigDecimal precoDe,BigDecimal precoAte , String situacaoLancamento,
			String codigoDeBarras, boolean brinde,
            String sortorder, String sortname, int page, int rp) {
		Intervalo<BigDecimal> intervaloPreco = null;
		Intervalo<Date> intervaloLancamento = null;
		// Validar:
		if ((codigoProduto == null || codigoProduto.trim().isEmpty()) 
				|| (nomeComercial == null || nomeComercial.trim().isEmpty())) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o campo 'Código' ou 'Produto'!");
		}
		if(dataLancamentoDe == null ^ dataLancamentoAte == null ){
			throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o interválo válido de 'Lançamento'!");
		}else if(dataLancamentoDe != null && dataLancamentoAte != null){
			if(dataLancamentoDe.after(dataLancamentoAte)){
				throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o interválo válido de 'Lançamento'!");
			}
			
			intervaloLancamento = new Intervalo<Date>(dataLancamentoDe, dataLancamentoAte);		
		}
		if(precoDe == null ^ precoAte == null ){
			throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o interválo válido de 'Preço'!");
		}else if(precoDe != null && precoAte != null ){
			if(precoDe.compareTo(precoAte) > 0){
				throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o interválo válido de 'Preço'!");
			}
			intervaloPreco = new Intervalo<BigDecimal>(precoDe, precoAte);
		}		
		
		StatusLancamento statusLancamento = null;
		for (StatusLancamento status : StatusLancamento.values()) {
			if (status.getDescricao().equals(situacaoLancamento)) {
				statusLancamento = status;
			}
		}		
	
		// Pesquisar:
		Long qtd = peService.countPesquisarEdicoes(codigoProduto, nomeComercial, intervaloLancamento, intervaloPreco, statusLancamento, codigoDeBarras, brinde);
		if(qtd > 0){			
			List<ProdutoEdicaoDTO> lst = peService.pesquisarEdicoes(codigoProduto, nomeComercial, intervaloLancamento, intervaloPreco, statusLancamento, codigoDeBarras, brinde, sortorder, sortname, page, rp);
			
			this.result.use(FlexiGridJson.class).from(lst).total(qtd.intValue()).page(page).serialize();
		}else{
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
					"Registros não encontrados."));
		}
	}
	
	@Post
	@Path("/carregarDadosProdutoEdicao.json")
	public void carregarDadosProdutoEdicao(String codigoProduto, String idProdutoEdicao) {
		
		if (codigoProduto == null || codigoProduto.trim().isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, escolha um produto para adicionar a Edição!");
		}
		
		ProdutoEdicaoDTO dto = peService.obterProdutoEdicaoDTO(codigoProduto, idProdutoEdicao);
		
		this.result.use(Results.json()).from(dto, "result").serialize();
	}
	
	@Post
	@Path("/ultimasEdicoes.json")
	public void ultimasEdicoes(String codigoProduto) {
			
		List<ProdutoEdicaoDTO> lst = peService.pesquisarUltimasEdicoes(codigoProduto, 5);

		this.result.use(FlexiGridJson.class).from(lst).total(lst.size()).page(1).serialize();
	}
	
	
	@Post
	public void salvar(UploadedFile imagemCapa,
			String codigoProduto, Long idProdutoEdicao,
			String codigoProdutoEdicao, String nomeComercialProduto,
			Long numeroEdicao, int pacotePadrao,
			TipoLancamento tipoLancamento,
			BigDecimal precoPrevisto, BigDecimal precoVenda,
			Date dataLancamentoPrevisto, Date dataRecolhimentoPrevisto,
			BigInteger repartePrevisto, BigInteger repartePromocional,
			String codigoDeBarras, String codigoDeBarrasCorporativo,
			BigDecimal desconto, Long peso, 
			BigDecimal largura, BigDecimal comprimento, BigDecimal espessura,
			String chamadaCapa, boolean parcial, boolean possuiBrinde,
			String boletimInformativo, Integer numeroLancamento, String descricaoBrinde, String descricaoProduto) {
		
		// DTO para transportar os dados:
		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
		
		dto.setId(idProdutoEdicao);
		dto.setNomeComercialProduto(nomeComercialProduto);
		dto.setNumeroEdicao(numeroEdicao);
		dto.setCodigoProduto(codigoProdutoEdicao);
		dto.setPacotePadrao(pacotePadrao);
		dto.setTipoLancamento(tipoLancamento);
		dto.setPrecoPrevisto(precoPrevisto);
		dto.setPrecoVenda(precoVenda);
		dto.setDataLancamentoPrevisto(dataLancamentoPrevisto);
		dto.setDataRecolhimentoPrevisto(dataRecolhimentoPrevisto);
		dto.setDataRecolhimentoDistribuidor(dataRecolhimentoPrevisto);
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
		dto.setDescricaoBrinde(descricaoBrinde);
		dto.setNomeComercial(descricaoProduto);
		
		ValidacaoVO vo = null;
		 
		try {
			
			this.validarProdutoEdicao(dto, codigoProduto);
			
			// Dados da Imagem:
			String contentType = null;
			
			InputStream imgInputStream = null;
			
			if (imagemCapa != null) {
				contentType = imagemCapa.getContentType();
				imgInputStream = imagemCapa.getFile();
			}
			
			peService.salvarProdutoEdicao(dto, codigoProduto, contentType, imgInputStream);
			
			vo = new ValidacaoVO(TipoMensagem.SUCCESS, "Edição salva com sucesso!");
			
		} catch (ValidacaoException e) {
			
			vo = e.getValidacao();

		} catch (Throwable e) {
			
			vo = new ValidacaoVO(TipoMensagem.ERROR, e.getMessage());
		
		} finally {
			
			this.result.use(PlainJSONSerialization.class).from(vo, "result").recursive().serialize();
		}
	}
	
	/**
	 * Valida o preenchimento dos campos obrigatórios.
	 * 
	 * @param dto
	 */
	private void validarProdutoEdicao(ProdutoEdicaoDTO dto, String codigoProduto) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		boolean origemManual = false;
		
		ProdutoEdicao pe = null;
		
		if(codigoProduto == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Código do produto inválido!"));
		}
		
		if(dto.getId()!=null) {

			pe = peService.obterProdutoEdicao(dto.getId(), false);
			
			if(pe == null) {
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Produto Edição inválido!"));
			}
			
			origemManual = (pe.getOrigemInterface() == null) ? true : pe.getOrigemInterface().booleanValue();
		}
		
		if (pe == null || origemManual) {
			
			// Distribuidor:
			if (dto.getCodigoProduto() == null || dto.getCodigoProduto().trim().length() <= 0) {
				listaMensagens.add("Campo 'Código' deve ser preenchido!");
			}
			if (dto.getNomeComercialProduto() == null || dto.getNomeComercialProduto().trim().length() <= 0) {
				listaMensagens.add("Campo 'Nome Comercial Produto' deve ser preenchido!");
			}
			if (dto.getNumeroEdicao() == null || dto.getNumeroEdicao() == 0L) {
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
			if (dto.getDataRecolhimentoPrevisto() == null) {
				listaMensagens.add("Campo 'Data de Recolhimento Previsto' deve ser preenchido!");
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
		
		if (dto.getNumeroEdicao() != null && dto.getNumeroEdicao() != 0L) {
			
			ProdutoEdicao produtoEdicao = peService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, dto.getNumeroEdicao().toString());
		
			if (produtoEdicao != null && !produtoEdicao.getId().equals(dto.getId())) {
				listaMensagens.add("O 'Número de Edição' deve ser unico para esse Produto!");
			}
			
		}
		
		if (!listaMensagens.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
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
		
		ProdutoEdicao produtoEdicao = produtoEdicaoService.obterProdutoEdicao(idProdutoEdicao, true);
		
		if (produtoEdicao!=null){
		    
		    BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
		    BigDecimal percentualDesconto = Util.nvl(produtoEdicao.getProduto().getDesconto(), BigDecimal.ZERO);
            BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);
			
			BigDecimal precoComDesconto = precoVenda.subtract(valorDesconto);

			String razaoSocial = "";
			
			if(	produtoEdicao!=null && 
				produtoEdicao.getProduto()!=null &&
				produtoEdicao.getProduto().getEditor() != null &&
				produtoEdicao.getProduto().getEditor().getPessoaJuridica() != null ) {
				
				razaoSocial = produtoEdicao.getProduto().getEditor().getPessoaJuridica().getNome();
				
				razaoSocial = (razaoSocial == null) ? "" : razaoSocial;
				
			}
			
			
			
			produtoLancamentoVO = new DetalheProdutoVO(produtoEdicao.getId(),
													   produtoEdicao.getProduto().getNome(),
													   produtoEdicao.getProduto().getCodigo(),
										               (precoVenda!=null?CurrencyUtil.formatarValor(precoVenda):""),
										               (precoComDesconto!=null?CurrencyUtil.formatarValor(precoComDesconto):""),
										               (produtoEdicao.getProduto()!=null?(produtoEdicao.getProduto().getFornecedor()!=null?produtoEdicao.getProduto().getFornecedor().getJuridica().getNome():""):""),
										               (produtoEdicao.getProduto()!=null?(produtoEdicao.getProduto().getEditor()!=null?produtoEdicao.getProduto().getEditor().getCodigo().toString():""):""),
										               razaoSocial,
										               produtoEdicao.getChamadaCapa(),
										               (produtoEdicao.isPossuiBrinde()?"Sim":"Não"),
										               Integer.toString(produtoEdicao.getPacotePadrao())
										               );
		}
		return produtoLancamentoVO;
	}

}
