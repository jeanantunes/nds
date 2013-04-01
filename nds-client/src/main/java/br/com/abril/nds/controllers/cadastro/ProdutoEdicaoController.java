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
import br.com.abril.nds.client.vo.PeriodoLancamentosProdutoEdicaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Brinde;
import br.com.abril.nds.model.cadastro.ClasseSocial;
import br.com.abril.nds.model.cadastro.FaixaEtaria;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Sexo;
import br.com.abril.nds.model.cadastro.TemaProduto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.BrindeService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.MathUtil;
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
@Rules(Permissao.ROLE_CADASTRO_EDICAO)
public class ProdutoEdicaoController extends BaseController {

	@Autowired
	private Result result;
	
	@Autowired
	private BrindeService brindeService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;

	@Autowired
	private LancamentoService lancamentoService;

	private static List<ItemDTO<ClasseSocial,String>> listaClasseSocial =  new ArrayList<ItemDTO<ClasseSocial,String>>();
	  
	private static List<ItemDTO<Sexo,String>> listaSexo =  new ArrayList<ItemDTO<Sexo,String>>();
	
	private static List<ItemDTO<FaixaEtaria,String>> listaFaixaEtaria =  new ArrayList<ItemDTO<FaixaEtaria,String>>();

	private static List<ItemDTO<TemaProduto,String>> listaTemaProduto =  new ArrayList<ItemDTO<TemaProduto,String>>();

	private static List<ItemDTO<GrupoProduto,String>> listaGrupoProduto =  new ArrayList<ItemDTO<GrupoProduto,String>>();
	
	private static List<ItemDTO<StatusLancamento,String>> listaStatusLancamento =  new ArrayList<ItemDTO<StatusLancamento,String>>();

	
	/** Traz a página inicial. */
	@Get
	@Path("/")
	public void index() {
		
		this.carregarDadosCombo();
	}

	/**
	 * Carrega os combos do modal de inclusão/edição do Produto-Segmentação.
	 */
	private void carregarDadosCombo() {
		
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
		
		listaGrupoProduto.clear();
		for(GrupoProduto item:GrupoProduto.values()){
			listaGrupoProduto.add(new ItemDTO<GrupoProduto,String>(item,item.getNome()));
		}
		result.include("listaGrupoProduto",listaGrupoProduto);
		
		listaStatusLancamento.clear();
		
		for (StatusLancamento statusLancamento : StatusLancamento.values()) {
			
			listaStatusLancamento.add(
				new ItemDTO<StatusLancamento, String>(
					statusLancamento, statusLancamento.getDescricao()));
		}
		
		result.include("listaStatusLancamento", listaStatusLancamento);
		
		List<Brinde> brindes = brindeService.obterBrindes();
		result.include("brindes", brindes);
    }

	@Post
	public void pesquisarProdutoCodBarra(String codBarra){
		
		List<ProdutoEdicao> produtosEdicao = produtoEdicaoService.buscarProdutoPorCodigoBarras(codBarra);
		
		if (produtosEdicao == null || produtosEdicao.isEmpty()) {
			
			this.result.nothing();
			
			return;
		}

		List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();

		for (ProdutoEdicao produtoEdicao : produtosEdicao) {

			listaProdutos.add(new ItemAutoComplete(
					produtoEdicao.getProduto().getNome() + " - " + produtoEdicao.getNumeroEdicao(), 
					null, produtoEdicao.getId()));
		}

		result.use(Results.json()).from(listaProdutos, "result").recursive().serialize();
	}
	
	@Post
	@Path("/pesquisarEdicoes.json")
	public void pesquisarEdicoes(String codigoProduto, String nomeProduto,
			Date dataLancamentoDe, Date dataLancamentoAte, BigDecimal precoDe,
			BigDecimal precoAte , StatusLancamento situacaoLancamento,
			String codigoDeBarras, boolean brinde,
            String sortorder, String sortname, int page, int rp) {
		Intervalo<BigDecimal> intervaloPreco = null;
		Intervalo<Date> intervaloLancamento = null;
		// Validar:
		if(dataLancamentoDe == null ^ dataLancamentoAte == null ){
			throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o intervalo válido de 'Lançamento'!");
		}else if(dataLancamentoDe != null && dataLancamentoAte != null){
			if(dataLancamentoDe.after(dataLancamentoAte)){
				throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o intervalo válido de 'Lançamento'!");
			}
			
			intervaloLancamento = new Intervalo<Date>(dataLancamentoDe, dataLancamentoAte);		
		}
		if(precoDe == null ^ precoAte == null ){
			throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o intervalo válido de 'Preço'!");
		}else if(precoDe != null && precoAte != null ){
			if(precoDe.compareTo(precoAte) > 0){
				throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o intervalo válido de 'Preço'!");
			}
			intervaloPreco = new Intervalo<BigDecimal>(precoDe, precoAte);
		}	
	
		// Pesquisar:
		Long qtd = produtoEdicaoService.countPesquisarEdicoes(
				codigoProduto, nomeProduto, intervaloLancamento, 
				intervaloPreco, situacaoLancamento, codigoDeBarras, brinde);
		
		if(qtd > 0){		
			
			List<ProdutoEdicaoDTO> lst = 
					produtoEdicaoService.pesquisarEdicoes(codigoProduto, nomeProduto, 
							intervaloLancamento, intervaloPreco, situacaoLancamento, codigoDeBarras, 
							brinde, sortorder, sortname, page, rp);
			
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
	public void salvar(UploadedFile imagemCapa,
			String codigoProduto, Long idProdutoEdicao,
			String codigoProdutoEdicao, String nomeComercialProduto,Integer peb,
			Long numeroEdicao, int pacotePadrao,
			TipoLancamento tipoLancamento,
			String precoPrevisto, String precoVenda,GrupoProduto categoria,
			Date dataLancamentoPrevisto, Date dataRecolhimentoPrevisto,
			BigInteger repartePrevisto, BigInteger repartePromocional,
			String codigoDeBarras, String codigoDeBarrasCorporativo,
			BigDecimal desconto, String descricaoDesconto,Long peso, 
			BigDecimal largura, BigDecimal comprimento, BigDecimal espessura,
			String chamadaCapa, boolean parcial, boolean possuiBrinde,
			String boletimInformativo, Integer numeroLancamento, Long descricaoBrinde, String descricaoProduto,
            ClasseSocial classeSocial,Sexo sexo,FaixaEtaria faixaEtaria,TemaProduto temaPrincipal,TemaProduto temaSecundario) {
			
		BigDecimal pPrevisto = precoPrevisto!=null?new BigDecimal(this.getValorSemMascara(precoPrevisto)):null;
		BigDecimal pVenda = precoVenda!=null?new BigDecimal(this.getValorSemMascara(precoVenda)):null;
		
		// DTO para transportar os dados:
		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
		
		dto.setId(idProdutoEdicao);
		dto.setNomeComercialProduto(nomeComercialProduto);
		dto.setPeb( (peb == null)?0:peb);
		dto.setCaracteristicaProduto(descricaoProduto);
		dto.setNumeroEdicao(numeroEdicao);
		dto.setCodigoProduto(codigoProdutoEdicao);
		dto.setPacotePadrao(pacotePadrao);
		dto.setTipoLancamento(tipoLancamento);
		dto.setPrecoPrevisto(pPrevisto);
		dto.setPrecoVenda(pVenda);
		dto.setDataLancamentoPrevisto(dataLancamentoPrevisto);
		dto.setDataRecolhimentoPrevisto(dataRecolhimentoPrevisto);
		dto.setDataRecolhimentoDistribuidor(dataRecolhimentoPrevisto);
		dto.setRepartePrevisto(repartePrevisto);
		dto.setRepartePromocional(repartePromocional);
		dto.setCodigoDeBarras(codigoDeBarras);
		dto.setCodigoDeBarrasCorporativo(codigoDeBarrasCorporativo);
		dto.setDesconto(desconto);
		dto.setDescricaoDesconto(descricaoDesconto);
		dto.setPeso(peso);
		dto.setLargura(largura == null ? 0 : largura.floatValue());
		dto.setComprimento(comprimento == null ? 0 : comprimento.floatValue());
		dto.setEspessura(espessura == null ? 0 : espessura.floatValue());
		dto.setChamadaCapa(chamadaCapa);
		dto.setParcial(parcial);
		dto.setPossuiBrinde(possuiBrinde);
		dto.setNumeroLancamento(numeroLancamento);
		dto.setIdBrinde(descricaoBrinde);
		dto.setBoletimInformativo(boletimInformativo);
		dto.setGrupoProduto(categoria);
		
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
			
			if(!validarDataLancamentoMenorRecolhimento(dto)) {
				listaMensagens.add(" Campo 'Data de Lançamento Previsto' deve ser menor do que o campo 'Data de Recolhimento Previsto' ");
			}
			
			if (dto.getRepartePrevisto() == null) {
				listaMensagens.add("Por favor, digite um valor válido para o 'Reparte Previsto'!");
			}
			if (dto.getRepartePromocional() == null) {
				listaMensagens.add("Por favor, digite um valor válido para o 'Reparte Promocional'!");
			}
			if (dto.getDescricaoDesconto() == null || dto.getDescricaoDesconto().trim().isEmpty()){
				listaMensagens.add("Por favor, digite um valor válido para o 'Tipo de Desconto'!");
			}
			if (dto.getDesconto() == null){
				listaMensagens.add("Por favor, digite um valor válido para o 'Desconto %'!");
			}
			
			//Essa validação só será feita na terceira fase do projeto.
//			if (dto.getCodigoDeBarrasCorporativo() == null || dto.getCodigoDeBarrasCorporativo().trim().length() <= 0) {
//				listaMensagens.add("Campo 'Código de Barras Corporativo' deve ser preenchido!");
//			}
			
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
	
	private boolean validarDataLancamentoMenorRecolhimento(ProdutoEdicaoDTO dto) {
		return DateUtil.isDataInicialMaiorDataFinal(dto.getDataRecolhimentoPrevisto(), dto.getDataLancamentoPrevisto());
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
	 * Obtém todos os períodos de lançamento da edição do produto
	 * @param produtoEdicaoId
	 */
	@Post
	public void carregarLancamentosPeriodo(Long produtoEdicaoId, String sortorder, String sortname) {

		List<PeriodoLancamentosProdutoEdicaoVO> listaPeriodosLancamentos = new ArrayList<>();
		
		for (Lancamento lancamento : lancamentoService.obterLancamentosEdicao(produtoEdicaoId, sortorder, sortname)) {
			PeriodoLancamentosProdutoEdicaoVO periodoLancamento = new PeriodoLancamentosProdutoEdicaoVO();
			periodoLancamento.setDataLancamentoPrevista(lancamento.getDataLancamentoPrevista());
			periodoLancamento.setDataRecolhimentoPrevista(lancamento.getDataRecolhimentoPrevista());
			listaPeriodosLancamentos.add(periodoLancamento); 
		}
		
		if (!listaPeriodosLancamentos.isEmpty()) {
			this.result.use(FlexiGridJson.class).from(listaPeriodosLancamentos).total(listaPeriodosLancamentos.size()).serialize();
		} else {
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
		    BigDecimal percentualDesconto = Util.nvl(produtoEdicao.getDescontoProdutoEdicao()!=null?produtoEdicao.getDescontoProdutoEdicao().getValor():BigDecimal.ZERO, BigDecimal.ZERO);

            BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);
			
			BigDecimal precoComDesconto = null;
			
			if (precoVenda != null && valorDesconto != null) {
				
				precoComDesconto = precoVenda.subtract(valorDesconto);
			}
			
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

	private String getValorSemMascara(String valor) {

		String chr = String.valueOf(valor.charAt(valor.length()-3));
		if (",".equals(chr)){
		    valor = valor.replaceAll("\\.", "");
		    valor = valor.replaceAll(",", "\\.");
		}
		
		if (".".equals(chr)){
		    valor = valor.replaceAll(",", "");
		}

		return valor;
	}
}
