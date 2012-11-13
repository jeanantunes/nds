package br.com.abril.nds.controllers.cadastro;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.DetalheProdutoVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Brinde;
import br.com.abril.nds.model.cadastro.ClasseSocial;
import br.com.abril.nds.model.cadastro.FaixaEtaria;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Sexo;
import br.com.abril.nds.model.cadastro.TemaProduto;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.BrindeService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.CurrencyUtil;
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
	private BrindeService brindeService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	private static List<ItemDTO<ClasseSocial,String>> listaClasseSocial =  new ArrayList<ItemDTO<ClasseSocial,String>>();
	  
	private static List<ItemDTO<Sexo,String>> listaSexo =  new ArrayList<ItemDTO<Sexo,String>>();
	
	private static List<ItemDTO<FaixaEtaria,String>> listaFaixaEtaria =  new ArrayList<ItemDTO<FaixaEtaria,String>>();

	private static List<ItemDTO<TemaProduto,String>> listaTemaProduto =  new ArrayList<ItemDTO<TemaProduto,String>>();

	
	/** Traz a página inicial. */
	@Get
	@Path("/")
	@Rules(Permissao.ROLE_CADASTRO_EDICAO)
	public void index() {
		
		this.carregarDadosSegmentacao();
		
		List<Brinde> brindes = brindeService.obterBrindes();

		result.include("brindes", brindes);
	}

	
	
	@Post
	@Path("/pesquisarEdicoes.json")
	public void pesquisarEdicoes(String codigoProduto, String nomeProduto,
			Date dataLancamentoDe, Date dataLancamentoAte, BigDecimal precoDe,BigDecimal precoAte , String situacaoLancamento,
			String codigoDeBarras, boolean brinde,
            String sortorder, String sortname, int page, int rp) {
		Intervalo<BigDecimal> intervaloPreco = null;
		Intervalo<Date> intervaloLancamento = null;
		// Validar:
		if (codigoDeBarras == null || codigoDeBarras.isEmpty()) {
			if ((codigoProduto == null || codigoProduto.trim().isEmpty()) 
					|| (nomeProduto == null || nomeProduto.trim().isEmpty())) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o campo 'Código', 'Produto' ou 'Código de Barras'!");
			}
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
		Long qtd = produtoEdicaoService.countPesquisarEdicoes(codigoProduto, nomeProduto, intervaloLancamento, intervaloPreco, statusLancamento, codigoDeBarras, brinde);
		if(qtd > 0){			
			List<ProdutoEdicaoDTO> lst = produtoEdicaoService.pesquisarEdicoes(codigoProduto, nomeProduto, intervaloLancamento, intervaloPreco, statusLancamento, codigoDeBarras, brinde, sortorder, sortname, page, rp);
			
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
		
		ProdutoEdicaoDTO dto = produtoEdicaoService.obterProdutoEdicaoDTO(codigoProduto, idProdutoEdicao);
		
		this.result.use(Results.json()).from(dto, "result").serialize();
	}
	
	@Post
	@Path("/ultimasEdicoes.json")
	public void ultimasEdicoes(String codigoProduto) {
			
		List<ProdutoEdicaoDTO> lst = produtoEdicaoService.pesquisarUltimasEdicoes(codigoProduto, 5);

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
			String boletimInformativo, Integer numeroLancamento, Long descricaoBrinde, String descricaoProduto,
            ClasseSocial classeSocial,Sexo sexo,FaixaEtaria faixaEtaria,TemaProduto temaPrincipal,TemaProduto temaSecundario) {
		
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
		dto.setIdBrinde(descricaoBrinde);
		dto.setNomeComercial(descricaoProduto);
		
		//Segmentação
		dto.setClasseSocial(classeSocial);
		dto.setFaixaEtaria(faixaEtaria);
		dto.setSexo(sexo);
		dto.setTemaPrincipal(temaPrincipal);
		dto.setTemaSecundario(temaSecundario);
		
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
			
			produtoEdicaoService.salvarProdutoEdicao(dto, codigoProduto, contentType, imgInputStream);
			
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
						
		ProdutoEdicao pe = null;
		
		if(codigoProduto == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Código do produto inválido!"));
		}
		
		if(dto.getId()!=null) {

			pe = produtoEdicaoService.obterProdutoEdicao(dto.getId(), false);
			
			if(pe == null) {
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Produto Edição inválido!"));
			}
			
		}
		
		if (pe == null || (pe.getOrigem().equals(br.com.abril.nds.model.Origem.MANUAL))) {
			
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
			
			ProdutoEdicao produtoEdicao = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, dto.getNumeroEdicao().toString());
		
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

			this.produtoEdicaoService.excluirProdutoEdicao(idProdutoEdicao);
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

	
	/**
	 * Carrega os combos do modal de inclusão/edição do Produto-Segmentação.
	 */
	@Post
	public void carregarDadosSegmentacao() {
		
		listaClasseSocial.clear();
		for(ClasseSocial item:ClasseSocial.values()){
			listaClasseSocial.add(new ItemDTO<ClasseSocial,String>(item,item.getDescClasseSocial()));
		}
		result.include("listaClasseSocial",listaClasseSocial);
		
		listaSexo.clear();
		for(Sexo item:Sexo.values()){
			listaSexo.add(new ItemDTO<Sexo,String>(item,item.name()));
		}
		result.include("listaSexo",listaSexo);	
		
		listaFaixaEtaria.clear();
		for(FaixaEtaria item:FaixaEtaria.values()){
			listaFaixaEtaria.add(new ItemDTO<FaixaEtaria,String>(item,item.getDescFaixaEtaria()));
		}
		result.include("listaFaixaEtaria",listaFaixaEtaria);	
		
		listaTemaProduto.clear();
		for(TemaProduto item:TemaProduto.values()){
			listaTemaProduto.add(new ItemDTO<TemaProduto,String>(item,item.getDescTemaProduto()));
		}
		result.include("listaTemaProduto",listaTemaProduto);	
    }
}
