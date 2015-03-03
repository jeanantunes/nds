package br.com.abril.nds.controllers.cadastro;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.DetalheProdutoVO;
import br.com.abril.nds.client.vo.PeriodoLancamentosProdutoEdicaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO.ModoTela;
import br.com.abril.nds.dto.filtro.FiltroProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Brinde;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.BrindeService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.abril.nds.service.TipoSegmentoProdutoService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.upload.XlsUploaderUtils;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

import com.google.common.base.Strings;

@Resource
@Path("/cadastro/edicao")
@Rules(Permissao.ROLE_CADASTRO_EDICAO)
public class ProdutoEdicaoController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoEdicaoController.class);

	@Autowired
	private Result result;
	
	@Autowired
	private BrindeService brindeService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;

	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private TipoSegmentoProdutoService tipoSegmentoProdutoService;
	
	@Autowired
	private TipoClassificacaoProdutoService tipoClassificacaoProdutoService;
	
	@Autowired
	private ProdutoService prodService;
	
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
	    
		final List<ItemDTO<GrupoProduto,String>> listaGrupoProduto = new ArrayList<ItemDTO<GrupoProduto,String>>();
		for(GrupoProduto item:GrupoProduto.values()){
			listaGrupoProduto.add(new ItemDTO<GrupoProduto,String>(item,item.getNome()));
		}
		result.include("listaGrupoProduto",listaGrupoProduto);

		final List<ItemDTO<StatusLancamento,String>> listaStatusLancamento =  new ArrayList<ItemDTO<StatusLancamento,String>>();
		for (StatusLancamento statusLancamento : StatusLancamento.values()) {
			
			listaStatusLancamento.add(
				new ItemDTO<StatusLancamento, String>(
					statusLancamento, statusLancamento.getDescricao()));
		}
		result.include("listaStatusLancamento", listaStatusLancamento);

		final List<ItemDTO<Long,String>> listaTipoSegmentoProduto =  new ArrayList<ItemDTO<Long,String>>();
		for (TipoSegmentoProduto tipoSegmentoProduto : tipoSegmentoProdutoService.obterTipoSegmentoProduto()) {
			listaTipoSegmentoProduto.add(
					new ItemDTO<Long, String>(
							tipoSegmentoProduto.getId(), tipoSegmentoProduto.getDescricao()));
		}
		result.include("listaTipoSegmentoProduto", listaTipoSegmentoProduto);

		final List<TipoClassificacaoProduto> classificacoes = tipoClassificacaoProdutoService.obterTodos();
		final List<ItemDTO<Long,String>> comboClassificacao =  new ArrayList<ItemDTO<Long,String>>();
		for (TipoClassificacaoProduto tipoClassificacaoProduto : classificacoes) {
			comboClassificacao.add(new ItemDTO<Long,String>(tipoClassificacaoProduto.getId(), tipoClassificacaoProduto.getDescricao()));
		}
		result.include("listaClassificacao",comboClassificacao);
    }

	@Post
	@Path("/obterBrindes.json")
	public void obterBrindes(){
	    
		final List<Brinde> brindes = brindeService.obterBrindes();
		
		final List<ItemDTO<Long,String>> comboBrindes =  new ArrayList<ItemDTO<Long,String>>();
		
		for (Brinde b : brindes) {
			
			comboBrindes.add(new ItemDTO<Long,String>(b.getId(), b.getDescricao()));
		}
	    
		result.use(Results.json()).from(comboBrindes, "result").recursive().serialize();
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
	public void pesquisarEdicoes(FiltroProdutoDTO filtro,
			Date dataLancamentoDe, Date dataLancamentoAte, Double precoDe,
			Double precoAte , StatusLancamento situacaoLancamento,
			String codigoDeBarras, boolean brinde, ModoTela modoTela,
            String sortorder, String sortname, int page, int rp) {
		
		Intervalo<Double> intervaloPreco = null;
		Intervalo<Date> intervaloLancamento = null;
		
		// Validar:
		if(dataLancamentoDe == null ^ dataLancamentoAte == null ) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Por favor, preencha o intervalo válido de 'Lançamento'!");
		} else if(dataLancamentoDe != null && dataLancamentoAte != null) {
			if(dataLancamentoDe.after(dataLancamentoAte)){
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Por favor, preencha o intervalo válido de 'Lançamento'!");
			}
			
			intervaloLancamento = new Intervalo<Date>(dataLancamentoDe, dataLancamentoAte);		
		}
		
		if(precoDe == null ^ precoAte == null ) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o intervalo válido de 'Preço'!");
		} else if(precoDe != null && precoAte != null ) {
			if(precoDe.compareTo(precoAte) > 0){
                throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o intervalo válido de 'Preço'!");
			}
			intervaloPreco = new Intervalo<Double>(precoDe, precoAte);
		}	
	
		// Pesquisar:
		Integer qtd = produtoEdicaoService.countPesquisarEdicoes(
				StringUtils.leftPad(filtro.getCodigo(), 8, '0'), filtro.getNome(), intervaloLancamento, 
				intervaloPreco, situacaoLancamento, codigoDeBarras, brinde).intValue();
		
		if(qtd > 0) {		
			
			List<ProdutoEdicaoDTO> lst = 
					produtoEdicaoService.pesquisarEdicoes(StringUtils.leftPad(filtro.getCodigo(), 8, '0'), filtro.getNome(), 
							intervaloLancamento, intervaloPreco, situacaoLancamento, codigoDeBarras, 
							brinde, sortorder, sortname, page, rp);
			
			for (ProdutoEdicaoDTO dto : lst) {
				dto.setModoTela(modoTela);
			}
			
			this.result.use(FlexiGridJson.class).from(lst).total(qtd.intValue()).page(page).serialize();
		} else {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
 "Registros não encontrados."));
		}
	}
	
	@Post
	@Path("/carregarDadosProdutoEdicao.json")
    public void carregarDadosProdutoEdicao(FiltroProdutoDTO filtro, Long idProdutoEdicao, String situacaoProdutoEdicao,
            boolean redistribuicao) {
		
		if (filtro.getCodigo() == null || filtro.getCodigo().trim().isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, escolha um produto para adicionar a Edição!");
		}
		
		ProdutoEdicaoDTO dto = produtoEdicaoService.obterProdutoEdicaoDTO(filtro.getCodigo(), idProdutoEdicao, redistribuicao, situacaoProdutoEdicao);
		
		this.result.use(Results.json()).from(dto, "result").serialize();
	}
	
	@Post
	public void atualizarReparteLancamento(Long idLancamento, BigInteger reparte, BigInteger repartePromocional) {
		
		if (idLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Lançamento não existe.");
		}
		
		this.lancamentoService.atualizarReparteLancamento(idLancamento, reparte, repartePromocional);
		
		this.result.nothing();
	}
	
	@Post
	@Rules(Permissao.ROLE_CADASTRO_EDICAO_ALTERACAO)
	public void salvar(UploadedFile imagemCapa,
			ProdutoEdicaoDTO produtoEdicaoDTO, ModoTela modoTela,boolean istrac29) {
			
		produtoEdicaoDTO.setModoTela(modoTela);
		
		if(FormaComercializacao.CONTA_FIRME.equals(produtoEdicaoDTO.getFormaComercializacao())){
			
			produtoEdicaoDTO.setDataRecolhimentoDistribuidor(produtoEdicaoDTO.getDataLancamentoPrevisto());
			produtoEdicaoDTO.setDataRecolhimentoPrevisto(produtoEdicaoDTO.getDataLancamentoPrevisto());
		} else {
			
			produtoEdicaoDTO.setDataRecolhimentoDistribuidor(produtoEdicaoDTO.getDataRecolhimentoReal());
		}
		 
		try {
			
			this.validarProdutoEdicao(produtoEdicaoDTO, produtoEdicaoDTO.getCodigoProduto(), modoTela);
			
			// Dados da Imagem:
			String contentType = null;
			
			InputStream imgInputStream = null;
			
			if (imagemCapa != null) {
               			
				contentType = imagemCapa.getContentType();
				imgInputStream = imagemCapa.getFile();
			}
			
			produtoEdicaoService.salvarProdutoEdicao(produtoEdicaoDTO, produtoEdicaoDTO.getCodigoProduto(), contentType, imgInputStream,istrac29, modoTela);
			
            this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Edição salva com sucesso!"), "result").recursive().serialize();
			
		} catch (ValidacaoException e) {

			this.result.use(Results.json()).from(e.getValidacao(), "result").recursive().serialize();
		} catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
			
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.ERROR, "O seguinte erro ocorreu:" + e.getMessage()), "result").recursive().serialize();
		}
	}
	
	@Post
	@Path("/addLote")
	public void addCotasEmLote (UploadedFile xls) throws IOException {  

		List<ProdutoEdicaoDTO> listaEdicaoDto = XlsUploaderUtils.getBeanListFromXls(ProdutoEdicaoDTO.class, xls);
		
		List<ProdutoEdicaoDTO> listaEdicaoDtoInvalidos = new ArrayList<>();
		
		if(listaEdicaoDto.size()<1){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Documento vazio, por favor revise-o!"));
		}else{
		
			formatarLista(listaEdicaoDto);
			
			List<String> validacaoEdicao = new ArrayList<>();
			
			
			for (ProdutoEdicaoDTO prodEdicao : listaEdicaoDto) {
				
				if(prodEdicao.getCodigoProduto()==null || prodService.obterProdutoPorProdin(prodEdicao.getCodigoProduto())==null){
					
					if(prodEdicao.getCodigoProduto()!=null){
                        validacaoEdicao.add("Código " + prodEdicao.getCodigoProduto() + " de produto não existente! ");
					}else{
						if(prodEdicao.getNomeComercial()!=null){
                            validacaoEdicao.add("Código do produto cujo nome é " + prodEdicao.getNomeComercial()
                                    + " não existe! ");
						}else{
							if(prodEdicao.getNumeroEdicao()!=null){
                                validacaoEdicao.add("Produto com a edicao " + prodEdicao.getNumeroEdicao()
                                        + " não existente! ");
							}else{
                                validacaoEdicao.add("Edição sem código, sem nome e sem Produto!");
							}
						}
					}
					
					listaEdicaoDtoInvalidos.add(prodEdicao);
				}else{
					List<String> mensagens = validarDadosEdicao(prodEdicao, prodEdicao.getCodigoProduto(), null);	
					
					try {
					    produtoEdicaoService.tratarInformacoesAdicionaisProdutoEdicaoArquivo(prodEdicao);
					} catch(ValidacaoException ex) {
					    mensagens.add(ex.getMessage());
					}
					
					if(!mensagens.isEmpty()){
						
						if(prodEdicao.getNumeroEdicao() != null){
							
                            validacaoEdicao.add("Produto " + prodEdicao.getCodigoProduto() + " com a Edição "
                                    + prodEdicao.getNumeroEdicao() + " está inválido. Por favor revise-o." + "\n"
                                    + mensagens);
						}else{
                            validacaoEdicao.add("Produto " + prodEdicao.getCodigoProduto()
                                    + " está inválido. Por favor revise-o." + "\r\n" + mensagens);
						}
						listaEdicaoDtoInvalidos.add(prodEdicao);
					}
				}
				
			}
			
			if(!listaEdicaoDtoInvalidos.isEmpty()){
				listaEdicaoDto.removeAll(listaEdicaoDtoInvalidos);
			}
			
			if(listaEdicaoDto.isEmpty()){
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, validacaoEdicao));
			}else{
				addProdEdicaoLote(listaEdicaoDto, validacaoEdicao);
			}
		
		}
		
	}

    private void addProdEdicaoLote(List<ProdutoEdicaoDTO> listaEdicaoDto, List<String> listaMensagem) {
		
		for (ProdutoEdicaoDTO prodEdicao : listaEdicaoDto) {
			
			try {
				
				// Dados da Imagem:
				String contentType = null;
				InputStream imgInputStream = null;
				
				produtoEdicaoService.salvarProdutoEdicao(
				        prodEdicao, prodEdicao.getCodigoProduto(), contentType, imgInputStream,false,
				        null);
				
			} 
			catch (Exception e) {
			    if(e instanceof ValidacaoException)
			        listaMensagem.add("Produto " + prodEdicao.getCodigoProduto() + " com a Edição "
			                + prodEdicao.getNumeroEdicao() + " está inválido. " + e.getMessage());
			    else
			        listaMensagem.add("Produto " + prodEdicao.getCodigoProduto() + " com a Edição "
			                + prodEdicao.getNumeroEdicao() + " está inválido. Por favor revise-o.");
			
			} 
			
		}
		
		if(listaMensagem.isEmpty()){
		
            this.result.use(PlainJSONSerialization.class)
                    .from(new ValidacaoVO(TipoMensagem.SUCCESS, "Edições inseridas com sucesso!!"), "result")
                    .recursive().serialize();

		}else{
			
			this.result.use(PlainJSONSerialization.class).from(new ValidacaoVO(TipoMensagem.WARNING, listaMensagem), "result").recursive().serialize();
			
		}
		
	}
	

	private void formatarLista(List<ProdutoEdicaoDTO> listaEdicaoDto) {
		
		for (ProdutoEdicaoDTO peDTO : listaEdicaoDto) {
			
			if(peDTO.getLancamento()!=null){
				
				peDTO.setTipoLancamento(TipoLancamento.getByDescricao(peDTO.getLancamento()));
			}

			if(!Strings.isNullOrEmpty(peDTO.getClassificacao())) {

				TipoClassificacaoProduto tipoClassificacao = 
						this.tipoClassificacaoProdutoService.obterPorClassificacao(peDTO.getClassificacao());
				
				peDTO.setTipoClassificacaoProduto(tipoClassificacao);
			}
			
			if(peDTO.getDtLancPrevisto()!=null)
			peDTO.setDataLancamentoPrevisto(DateUtil.parseDataPTBR(peDTO.getDtLancPrevisto()));
			
			if(peDTO.getDtRecPrevisto()!=null)
			peDTO.setDataRecolhimentoPrevisto(DateUtil.parseDataPTBR(peDTO.getDtRecPrevisto()));
			
			peDTO.setNomeComercialProduto(peDTO.getNomeComercial());
			
			if(peDTO.getCodigoProduto()!=null){
				Produto produto = prodService.obterProdutoPorCodigo(peDTO.getCodigoProduto());
				
				if(produto != null){
					peDTO.setDesconto(produto.getDesconto()!=null?produto.getDesconto():new BigDecimal(0));
					peDTO.setDescricaoDesconto(produto.getDescricaoDesconto()!=null?produto.getDescricaoDesconto():"PRODUTO");	
				}
			}
			
			
            // baseado no método salvar, desta classe.
			peDTO.setDataRecolhimentoDistribuidor(peDTO.getDataRecolhimentoPrevisto());
			
		}
		
	}
	
	
	
	                                                                                        /**
     * Valida o preenchimento dos campos obrigatórios.
     * 
     * @param dto
     */
	private void validarProdutoEdicao(ProdutoEdicaoDTO dto, String codigoProduto, ModoTela modoTela) {
		
		List<String> listaMensagensValidacao = produtoEdicaoService.validarDadosBasicosEdicao(dto, codigoProduto);
		
		if (!listaMensagensValidacao.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagensValidacao));
		}else{
			listaMensagensValidacao = validarDadosEdicao(dto, codigoProduto, modoTela);
			if (!listaMensagensValidacao.isEmpty()) {
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagensValidacao));
			}
		}
		
	}
	

	private List<String> validarDadosEdicao(ProdutoEdicaoDTO dto, String codigoProduto, ModoTela modoTela) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		ProdutoEdicao pe = null;
		
		if(dto.getId()!=null) {
			pe = produtoEdicaoService.obterProdutoEdicao(dto.getId(), false);
		}
		
        if (pe == null || pe.getOrigem().equals(Origem.MANUAL)) {
			
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
                listaMensagens.add("Campo 'Data de Lançamento Previsto' não está em um formato válido.(ex: '21/01/2001')");
			}
			if (dto.getDataRecolhimentoPrevisto() == null) {
				listaMensagens.add("Campo 'Data de Recolhimento Previsto' não está em um formato válido.(ex: '21/01/2001')");
			}
			
			
			if (!dto.isParcial() && dto.getRepartePrevisto() == null) {
                listaMensagens.add("Por favor, digite um valor válido para o 'Reparte Previsto'!");
			}
			if (!dto.isParcial() && dto.getRepartePromocional() == null) {
                listaMensagens.add("Por favor, digite um valor válido para o 'Reparte Promocional'!");
			}
			if (dto.getDescricaoDesconto() == null || dto.getDescricaoDesconto().trim().isEmpty()){
                listaMensagens.add("Por favor, digite um valor válido para o 'Tipo de Desconto'!");
			}
			if (dto.getDesconto() == null){
                listaMensagens.add("Por favor, digite um valor válido para o 'Desconto %'!");
			}
			if (dto.getTipoClassificacaoProduto() == null || dto.getTipoClassificacaoProduto().getId() == null){
                listaMensagens.add("Por favor, selecione um valor válido para a 'Classificação'");
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
		
		if (modoTela != null && modoTela.equals(ModoTela.REDISTRIBUICAO)) {
		
			Date maiorDataLancamento =
				this.lancamentoService.getMaiorDataLancamento(dto.getId());
			
            if (maiorDataLancamento != null && dto.getDataLancamento() != null
					&& dto.getDataLancamento().compareTo(maiorDataLancamento) <= 0) {
				
            	listaMensagens.add("Data de redistribuição deve ser maior que ultima data de lançamento cadastrada!");
			}
		}
		
		return listaMensagens;
	}
	
	                                                                                        /**
     * Remove uma Edição.
     * 
     * @param idProdutoEdicao
     */
	@Post
	@Path("/validarRemocaoEdicao.json")
	@Rules(Permissao.ROLE_CADASTRO_EDICAO_ALTERACAO)
	public void validarRemocaoEdicao(Long idProdutoEdicao) {

		if (idProdutoEdicao == null || Long.valueOf(0).equals(idProdutoEdicao)) {
			throw new ValidacaoException(TipoMensagem.ERROR,
 "Por favor, selecione uma Edição válida!");
		}
		
		Map<String, String> validacaoMap = new HashMap<String, String>();
		
		try {
			
			validacaoMap = this.produtoEdicaoService.isProdutoEdicaoValidoParaRemocao(idProdutoEdicao);
			
		} catch (Exception e) {
			
		}

		this.result.use(Results.json()).from(validacaoMap, "result").recursive().serialize();
	}

	                                                                                        /**
     * Remove uma Edição.
     * 
     * @param idProdutoEdicao
     */
	@Post
	@Path("/removerEdicao.json")
	@Rules(Permissao.ROLE_CADASTRO_EDICAO_ALTERACAO)
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
     * 
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
     * 
     * @param produtoEdicaoId
     */
	@Post
	public void carregarLancamentosPeriodo(Long produtoEdicaoId, String sortorder, String sortname) {

		List<PeriodoLancamentosProdutoEdicaoVO> listaPeriodosLancamentos = new ArrayList<>();
		
		Set<Integer> numerosPeriodo = new HashSet<>();
		
		for (Lancamento lancamento : lancamentoService.obterLancamentosEdicao(produtoEdicaoId)) {
			
			Integer numeroPeriodo = null;
			
			PeriodoLancamentoParcial periodoLancamentoParcial = lancamento.getPeriodoLancamentoParcial();
			
			if (periodoLancamentoParcial != null) {
			
				numeroPeriodo = periodoLancamentoParcial.getNumeroPeriodo();
			}
			
			PeriodoLancamentosProdutoEdicaoVO periodoLancamento = new PeriodoLancamentosProdutoEdicaoVO();
			
            periodoLancamento.setIdLancamento(lancamento.getId());
			periodoLancamento.setNumeroPeriodo(numeroPeriodo);
			periodoLancamento.setNumeroLancamento(lancamento.getNumeroLancamento());
			periodoLancamento.setDataLancamentoDistribuidor(lancamento.getDataLancamentoDistribuidor());
			periodoLancamento.setDataLancamentoPrevista(lancamento.getDataLancamentoPrevista());
			periodoLancamento.setDataRecolhimentoDistribuidor(lancamento.getDataRecolhimentoDistribuidor());
			periodoLancamento.setDataRecolhimentoPrevista(lancamento.getDataRecolhimentoPrevista());
			periodoLancamento.setStatus(lancamento.getStatus().getDescricao());
			periodoLancamento.setReparte(lancamento.getReparte());
			periodoLancamento.setRepartePromocional(lancamento.getRepartePromocional());
			
			if (numerosPeriodo.contains(numeroPeriodo)) {
				periodoLancamento.setDestacarLinha(true);
			}
			
			numerosPeriodo.add(numeroPeriodo);
			
			listaPeriodosLancamentos.add(periodoLancamento);
		}
		
		this.result.use(FlexiGridJson.class).from(listaPeriodosLancamentos).total(listaPeriodosLancamentos.size()).serialize();
	}
	
	                                                                                        /**
     * Popula e retorna Value Object com detalhes de produto edição
     * 
     * @param idProdutoEdicao
     * @return DetalheProdutoVO
     */
	private DetalheProdutoVO getDetalheProduto(Long idProdutoEdicao){
		
		DetalheProdutoVO produtoLancamentoVO = null;
		
		ProdutoEdicao produtoEdicao = produtoEdicaoService.obterProdutoEdicao(idProdutoEdicao, true);
		
		if (produtoEdicao!=null){
		    
		    BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
		    
		    BigDecimal percentualDesconto = BigDecimal.ZERO;
		    
		    if(Origem.INTERFACE.equals(produtoEdicao.getOrigem())){
		    	
		    	 percentualDesconto = (produtoEdicao.getProduto().getDescontoLogistica()!= null)
				    		? produtoEdicao.getProduto().getDescontoLogistica().getPercentualDesconto()
				    				:BigDecimal.ZERO;
		    }
		    else{
		    	
		    	percentualDesconto = (produtoEdicao.getDesconto()!= null)
		    			? produtoEdicao.getDesconto()
		    					: BigDecimal.ZERO;
		    }
		    
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
										               (precoComDesconto!=null?CurrencyUtil.formatarValorQuatroCasas(precoComDesconto):""),
										               (produtoEdicao.getProduto()!=null?(produtoEdicao.getProduto().getFornecedor()!=null?produtoEdicao.getProduto().getFornecedor().getJuridica().getNome():""):""),
										               (produtoEdicao.getProduto()!=null?(produtoEdicao.getProduto().getEditor()!=null?produtoEdicao.getProduto().getEditor().getCodigo().toString():""):""),
										               razaoSocial,
										               produtoEdicao.getChamadaCapa(),
 (produtoEdicao.isPossuiBrinde() ? "Sim"
                            : "Não"),
										               Integer.toString(produtoEdicao.getPacotePadrao())
										               );
			
			
		}
		return produtoLancamentoVO;
	}
    
    @Post("/removerLancamento.json")
    @Rules(Permissao.ROLE_CADASTRO_EDICAO_ALTERACAO)
    public void removerLancamento(Long idLancamento) {
        this.lancamentoService.removerLancamento(idLancamento);
        result.use(Results.json())
                .from(new ValidacaoVO(TipoMensagem.SUCCESS, "Lançamento excluido com Sucesso."), "result").recursive()
                .serialize();
    }

	

}
