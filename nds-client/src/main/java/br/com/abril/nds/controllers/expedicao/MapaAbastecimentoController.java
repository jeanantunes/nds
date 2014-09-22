package br.com.abril.nds.controllers.expedicao;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoMapaDTO;
import br.com.abril.nds.dto.ProdutoMapaRotaDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EntregadorService;
import br.com.abril.nds.service.MapaAbastecimentoService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RotaService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/mapaAbastecimento")
@Rules(Permissao.ROLE_EXPEDICAO_MAPA_ABASTECIMENTO)
public class MapaAbastecimentoController extends BaseController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMapaAbastecimento";

	@Autowired
	private HttpSession session;

	@Autowired
	private Result result;

	@Autowired
	private MapaAbastecimentoService mapaAbastecimentoService;

	@Autowired
	private CotaService cotaService;

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;

	@Autowired
	private BoxService boxService;

	@Autowired
	private RoteirizacaoService roteirizacaoService;

	@Autowired
	private RotaService rotaService;

	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private EntregadorService entregadorService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	private HttpServletResponse response;

	public void mapaAbastecimento() {

	}

	/**
	 * Inicializa dados da tela
	 */
	@Path("/")
	public void index() {

		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);

		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

		result.include("data",data);

		result.include("listaBoxes",carregarBoxes(boxService.buscarTodos(TipoBox.LANCAMENTO)));
		result.include("listaRotas",carregarRota(rotaService.obterRotas()));

		result.forwardTo(MapaAbastecimentoController.class).mapaAbastecimento();
	}

	/**
	 * Carrega a lista de Produtos.
	 */
	@Post
	public void getProdutos(Date dataLancamento) {

	    List<ItemDTO<String, String>> listaProdutos = produtoEdicaoService.obterProdutosBalanceados(dataLancamento);

		if (listaProdutos.isEmpty()) {

			throw new ValidacaoException(
					TipoMensagem.WARNING, "Não existem produtos balanceados na data informada!");
		}

		this.result.use(Results.json()).from(listaProdutos, "result").recursive().serialize();
	}

	@Post
	public void getProdutosPorCodigo(FiltroProdutoDTO filtro, Date dataLancamento) throws ValidacaoException{

		Produto produto = produtoService.obterProdutoBalanceadosPorCodigo(filtro.getCodigo(), dataLancamento);

		if (produto == null) {

			throw new ValidacaoException(
					TipoMensagem.WARNING, "Não existe produto balanceado com o código \"" + filtro.getCodigo() + "\" na data informada!");

		} else {

			result.use(Results.json()).from(produto, "result").serialize();
		}	
	}

	/**
	 * Carrega a lista de Boxes
	 * @return
	 */
	private List<ItemDTO<Long, String>> carregarBoxes(List<Box> listaBoxes){

		List<ItemDTO<Long, String>> boxes = new ArrayList<ItemDTO<Long,String>>();

		for(Box box : listaBoxes){

			boxes.add(new ItemDTO<Long, String>(box.getId(),box.getCodigo() + " - " + box.getNome()));
		}

		return boxes;	
	}

	/**
	 * Retorna uma lista de Rota no formato ItemDTO
	 * @param rotas
	 * @return
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> carregarRota(List<Rota> rotas){

		List<ItemDTO<Long, String>> listaRotas = new ArrayList<ItemDTO<Long,String>>();

		for(Rota rota : rotas){

			listaRotas.add(new ItemDTO<Long, String>(rota.getId(), rota.getDescricaoRota()));
		}

		return listaRotas;
	}

	/**
	 * Retorna uma lista de Rota no formato ItemDTO
	 * @param rotas
	 * @return
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> carregarRoteiro(List<Roteiro> roteiros){

		List<ItemDTO<Long, String>> listaRoteiros = new ArrayList<ItemDTO<Long,String>>();

		for(Roteiro item : roteiros){

			listaRoteiros.add(new ItemDTO<Long, String>(item.getId(),item.getDescricaoRoteiro()));
		}

		return listaRoteiros;
	}
	
	private List<ItemDTO<Long, String>> carregarEntregadores(List<Entregador> entregadores){
	    
	    List<ItemDTO<Long, String>> listaEntregadores = new ArrayList<ItemDTO<Long,String>>();

        for(Entregador item : entregadores){

            listaEntregadores.add(new ItemDTO<Long, String>(item.getId(),item.getPessoa().getNome()));
        }

        return listaEntregadores;
	}

	@Post
	public void pesquisar(FiltroMapaAbastecimentoDTO filtro, Integer page, Integer rp, String sortname, String sortorder) {

		validarFiltroPesquisa(filtro);

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		tratarFiltro(filtro);

		switch(filtro.getTipoConsulta()) {

		case BOX:
			this.popularGridPorBox(filtro);
			break;
		case COTA:
			this.popularGridPorCota(filtro);
			break;
		case ROTA:
			this.popularGridPorRota(filtro);
			break;
		case PRODUTO:
			this.popularGridPorProduto(filtro);
			break;
		case PROMOCIONAL:
			this.popularGridPorRepartePromocional(filtro);
			break;
		case PRODUTO_ESPECIFICO:
			this.popularGridPorProdutoEspecifico(filtro);
			break;
		case PRODUTO_X_COTA:
			this.popularGridPorProdutoCota(filtro);
			break;
		case ENTREGADOR:
			this.popularGridPorEntregador(filtro);
		default:
			break;
		}
	}

	private void validarFiltroPesquisa(FiltroMapaAbastecimentoDTO filtro) {

		if(filtro == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Parâmetros de consulta inválidos.");
		}

		if(filtro.getTipoConsulta() == null)
			throw new ValidacaoException(TipoMensagem.WARNING, " 'Tipo de consulta' deve ser selecionado.");

		if(filtro.getDataDate() == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "'Data de Lançamento' não é válida.");

		if(filtro.getDataLancamento() == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "'Data de Lançamento' é obrigatória.");
	}

	@Post
	public void pesquisarDetalhes(Long idBox, Integer numeroCota, String data, String sortname, String sortorder) {

		FiltroMapaAbastecimentoDTO filtro = null;
        try {
            filtro = (FiltroMapaAbastecimentoDTO) BeanUtils.cloneBean(session.getAttribute(FILTRO_SESSION_ATTRIBUTE));
        } catch (Exception e) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao pesquisar detalhes.");
        }
	    
	    filtro.setDataLancamento(data);
		filtro.setCodigoCota(numeroCota);

		filtro.setPaginacaoDetalhes(new PaginacaoVO(null, null, sortorder, sortname));

		List<ProdutoAbastecimentoDTO> lista = mapaAbastecimentoService.obterDetlhesDadosAbastecimento(idBox, filtro);

		result.use(FlexiGridJson.class).from(lista).page(1).total(lista.size()).serialize();
	}

	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 *
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroMapaAbastecimentoDTO filtroAtual) {

		switch(filtroAtual.getTipoConsulta()) {	
		case BOX:
			break;
		case COTA:
			if(filtroAtual.getCodigoCota()==null)
				throw new ValidacaoException(TipoMensagem.WARNING, "'Cota' não foi preenchida.");
			break;
		case PRODUTO:
			if(filtroAtual.getCodigosProduto()==null)
				throw new ValidacaoException(TipoMensagem.WARNING, "'Produto' não foi preenchido.");
			break;
		case PROMOCIONAL:
			
			break;
		case PRODUTO_ESPECIFICO:

			List<String> mensagens = new ArrayList<String>();

			if (filtroAtual.getCodigosProduto()==null){
				
			    mensagens.add("'Produto' não foi preenchido.");
			} else if (filtroAtual.getCodigosProduto().size() > 1){
				
			    mensagens.add("Deve ser escolhido apenas um 'Produto'.");
			}
			
			if (filtroAtual.getNumerosEdicao()==null){
				
			    mensagens.add("'Edição' não foi preenchida.");
			}
			
			if (!mensagens.isEmpty())
				
			    throw new ValidacaoException(TipoMensagem.WARNING, mensagens);
			break;
		case PRODUTO_X_COTA:
			if(filtroAtual.getCodigosProduto()==null)
				throw new ValidacaoException(TipoMensagem.WARNING, "'Produto' não foi preenchido.");
			break;
		case ROTA:

			break;
		case ENTREGADOR:
			
			break;
		default:
			throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de consulta inexistente.");
		}

		FiltroMapaAbastecimentoDTO filtroSession = (FiltroMapaAbastecimentoDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);

		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(filtroAtual.getPaginacao().getPaginaAtual());
		}

		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}

	@Post
	public void buscarBoxRotaPorCota(Integer numeroCota) {

		Object[] combos = new Object[4];

		if(numeroCota != null) {
			List<Box> boxes = new ArrayList<Box>();
			Box box = boxService.obterBoxPorCota(numeroCota);

			if(box != null)
				boxes.add(box);

			combos[0] = carregarBoxes(boxes);
			combos[1] = carregarRota(roteirizacaoService.obterRotasPorCota(numeroCota));
			combos[2] = carregarRoteiro(roteirizacaoService.obterRoteirosPorCota(numeroCota));
		} else {
			combos[0] = carregarBoxes(boxService.buscarTodos(TipoBox.LANCAMENTO));
			combos[1] = carregarRota(roteirizacaoService.buscarRotas());
			combos[2] = carregarRoteiro(roteirizacaoService.obterRoteirosPorCota(null));
		}
		
		combos[3] = carregarEntregadores(entregadorService.obterTodos());

		this.result.use(Results.json()).from(combos, "result").recursive().serialize();
	}

	public void imprimirMapaAbastecimento() throws URISyntaxException, JRException, IOException {

		FiltroMapaAbastecimentoDTO filtro = (FiltroMapaAbastecimentoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);

		validarFiltroPesquisa(filtro);

		if(filtro == null) {	
			result.forwardTo(MapaAbastecimentoController.class).impressaoFalha("Nenhuma pesquisa foi realizada.");
			return;
		}
		
		filtro.setPaginacao(null);
		
		final URL diretorio = Thread.currentThread().getContextClassLoader().getResource("reports/");
		String nomeRelatorio = null;
		String path = diretorio.toURI().getPath();
		
		final Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("SUBREPORT_DIR", diretorio.toURI().getPath());
        parameters.put("IMAGEM", this.parametrosDistribuidorService.getLogotipoDistribuidor());
        parameters.put("NOME_DISTRIBUIDOR", this.distribuidorService.obterRazaoSocialDistribuidor());
        
		@SuppressWarnings("rawtypes")
        Collection dados = null;
		
		PaginacaoVO paginacao = new PaginacaoVO();
		
		paginacao.setOrdenacao(Ordenacao.ASC);
		
		filtro.setPaginacao(paginacao);

		try {

			switch(filtro.getTipoConsulta()) {
			case BOX:
				
			    if (filtro.getQuebraPorCota()){
				    
				    dados = mapaAbastecimentoService.obterMapaDeImpressaoPorBoxQuebraPorCota(filtro).entrySet();
			        
			        parameters.put("DATA", filtro.getDataLancamento());
			        
			        nomeRelatorio = "Mapa de Abastecimento por Box Quebra Cota";
			        
			        path += "rel_box_por_quebra_cota_principal.jasper";
			        
				} else {
				    
				    final Map<String, ProdutoMapaDTO> mapa = mapaAbastecimentoService.obterMapaDeImpressaoPorBox(filtro);
				    
				    for (String key : mapa.keySet()){
		                completaTabelaMapaBox(mapa, key);
		            }
				    
				    dados = mapa.entrySet();
				    
				    nomeRelatorio = "Mapa de Abastecimento por Box";
				    
				    path += "rel_por_box_principal.jasper";
				}
			break;
			case ROTA:
				
				filtro.getPaginacao().setSortColumn("codigoBox");
				
			    if (filtro.getQuebraPorCota()){
			        
			        dados = mapaAbastecimentoService.obterMapaDeImpressaoPorBoxRotaQuebraCota(filtro).entrySet();
			        
			        parameters.put("DATA", filtro.getDataLancamento());
                    
                    nomeRelatorio = "Mapa de Abastecimento por Rota Quebra Cota";
                    
                    path += "rel_rota_quebra_principal.jasper";
			        
			    } else {
			        
			        Map<String, Map<String, ProdutoMapaRotaDTO>> mapa = 
			                mapaAbastecimentoService.obterMapaDeImpressaoPorBoxRota(filtro);
                    
			        for (String key : mapa.keySet()) {
			            completaTabelaMapaRota(mapa.get(key), key);
			        }
			        
			        dados = mapa.entrySet();
			        
                    nomeRelatorio = "Mapa de Abastecimento por Rota";
                    
                    path += "rel_box_por_rota_principal.jasper";
                    
			    }
				
			break;
			case COTA:
				
			    dados = Arrays.asList(
				        mapaAbastecimentoService.obterMapaDeImpressaoPorCota(filtro));
                
                nomeRelatorio = "Mapa de Abastecimento por Cota";
                
                path += "rel_box_por_cota_principal.jasper";
				
			break;
			case PRODUTO:
			    
			    dados = mapaAbastecimentoService.obterMapaDeImpressaoPorCota(filtro).getProdutos().entrySet();
			    
			    nomeRelatorio = "Mapa de Abastecimento por Produto";
			    
			    path += "rel_prod_principal.jasper";
			    
			break;	
			case PROMOCIONAL:
				
			    filtro.setPorRepartePromocional(true);
				
			    dados = mapaAbastecimentoService.obterMapaDeImpressaoPorCota(filtro).getProdutos().values();
                
                nomeRelatorio = "Mapa de Abastecimento por Reparte Promocional";
                
                path += "rel_prod_promo_principal.jasper";
								
			break;
			case PRODUTO_ESPECIFICO:
				
				filtro.getPaginacao().setSortColumn("codigoBox");
				
				dados = Arrays.asList(
				        mapaAbastecimentoService.obterMapaDeImpressaoPorProdutoEdicao(filtro));

				nomeRelatorio = "Mapa de Abastecimento por Produto Específico";
				
				path += "rel_prod_espec_principal.jasper";

			break;
			case PRODUTO_X_COTA:
				
				filtro.getPaginacao().setSortColumn("codigoCota");
				
				dados = mapaAbastecimentoService.obterMapaDeImpressaoPorProdutoQuebrandoPorCota(filtro);
				
				nomeRelatorio = "Mapa de Abastecimento de Produto por Cota";
				
				path += "rel_prod_por_cota_principal.jasper";
				
			break;	
			case ENTREGADOR:
				
			    dados = mapaAbastecimentoService.obterMapaDeImpressaoPorEntregador(filtro).entrySet();
			    
			    nomeRelatorio = "Mapa de Abastecimento por Entregador";
                
                path += "rel_entregador_principal.jasper";
                
			break;
			default:
				throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de consulta inexistente.");
			}

		} catch(ValidacaoException e) {
			impressaoFalha(e.getMessage());
		}
		
		parameters.put("NOME_RELATORIO", nomeRelatorio);
		
		final byte[] mapa = JasperRunManager.runReportToPdf(path, parameters, new JRBeanCollectionDataSource(dados));
        
        this.response.setContentType("application/pdf");
        this.response.setHeader("Content-Disposition", "attachment; filename=mapa_abastecimento.pdf");
        
        this.response.getOutputStream().write(mapa);
        
        this.response.getOutputStream().close();
        
        this.result.use(Results.nothing());
	}

	private void completaTabelaMapaRota(Map<String, ProdutoMapaRotaDTO> map, String key) {
	    
	    // Número de colunas
        final int nColunas = 6;

        for (ProdutoMapaRotaDTO produto : map.values()) {
            
            // Tamanho do Map
            int tamanhoQtde = produto.getRotasQtde().size();
            
            // Testa se existem mais de 6 registros no Map
            if (tamanhoQtde > nColunas) {
                // Faz subtrações até sobrar menos que 6
                do {
                    tamanhoQtde -= nColunas;
                } while (tamanhoQtde > nColunas);
                // Cria uma Map<String, Integer> com valores para completar a
                // tabela
                Map<String, Integer> completaValores = new HashMap<>();
                for (int i = 1; i <= (6 - tamanhoQtde); i++) {
                    completaValores.put("|1" + i, 0);
                }
                // Adiciona os dados a tabela
                produto.getRotasQtde().putAll(completaValores);
            }
        }
    }

    private void completaTabelaMapaBox(Map<String, ProdutoMapaDTO> mapa, String key) {
	    
	    //Número de colunas
	    final int nColunas = 6;

        //Tamanho do Map
        int tamanhoQtde = mapa.get(key).getBoxQtde().size();

        //Testa se existem mais de 6 registros no Map
        if(tamanhoQtde > nColunas){
            //Faz subtrações até sobrar menos que 6
            do {
                tamanhoQtde -= nColunas;
            } while (tamanhoQtde > nColunas);

            //Cria uma Map<Integer, Integer> com valores para completar a tabela
            Map<Integer, Integer> completaValores= new HashMap<Integer, Integer>();
            
            for(int i = 1 ; i <= (6 - tamanhoQtde) ; i++){
                completaValores.put((20000 * i), (20000 * i));
            }
            //Adiciona os dados a tabela
            mapa.get(key).getBoxQtde().putAll(completaValores);
        }
    }

    public void impressaoFalha(String mensagemErro){
		result.include(mensagemErro);	
	}

	private void mostrarMensagemListaVazia() {
		throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado na pesquisa.");
	}

	private void popularGridPorBox(FiltroMapaAbastecimentoDTO filtro) {

		List<AbastecimentoDTO> lista = this.mapaAbastecimentoService.obterDadosAbastecimento(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = this.mapaAbastecimentoService.countObterDadosAbastecimento(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorCota(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorCota(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorCota(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorRota(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorBoxRota(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorBoxRota(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorProduto(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorProdutoEdicao(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorProdutoEdicao(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}
	
	private void popularGridPorRepartePromocional(FiltroMapaAbastecimentoDTO filtro) {
		
		filtro.setPorRepartePromocional(true);
		
		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorCota(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorCota(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorProdutoEspecifico(FiltroMapaAbastecimentoDTO filtro) {
		
		filtro.setProdutoEspecifico(true);
		
		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorProdutoEdicao(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorProdutoEdicao(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorProdutoCota(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaDeAbastecimentoPorProdutoQuebrandoPorCota(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaDeAbastecimentoPorProdutoQuebrandoPorCota(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorEntregador(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaDeAbastecimentoPorEntregador(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaDeAbastecimentoPorEntregador(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();

	}

	@Post
	public void buscarRotaPorRoteiro(Long idRoteiro) {
		List<Rota> rotas = roteirizacaoService.buscarRotasPorRoteiro(idRoteiro);	
		result.use(CustomMapJson.class).put("rotas", carregarRota(rotas)).serialize();
	}

	@Post
	public void buscarRoteiroPorBox(Long idBox) {
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiroDeBox(idBox);
		List<Rota> rotas;
		if (idBox != null) {
			rotas = roteirizacaoService.buscarRotaDeBox(idBox);
		}else{
			rotas = roteirizacaoService.buscarRotas();
		}
		result.use(CustomMapJson.class).put("roteiros", carregarRoteiro(roteiros)).put("rotas", carregarRota(rotas)).serialize();

	}

}
