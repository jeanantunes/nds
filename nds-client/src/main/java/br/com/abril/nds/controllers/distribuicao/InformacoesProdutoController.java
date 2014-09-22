package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.dto.InfoProdutosBonificacaoDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.InformacoesVendaEPerceDeVendaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ProdutoBaseSugeridaDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.InformacoesProdutoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/informacoesProduto")
@Rules(Permissao.ROLE_DISTRIBUICAO_INFORMACOES_PRODUTO)
public class InformacoesProdutoController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private InformacoesProdutoService infoProdService;
	
	@Autowired
	private ProdutoService prodService;
	
	@Autowired
	private EstudoService estudoService;
	
	@Autowired
	private HttpSession session;
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroInfoProdutos";
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	public InformacoesProdutoController(Result result) {
		this.result = result;
	}
	
	@Path("/")
	public void index(){
		this.carregarComboClassificacao();
	}
	
	private void carregarComboClassificacao(){
		List<ItemDTO<Long,String>> comboClassificacao =  new ArrayList<ItemDTO<Long,String>>();
		List<TipoClassificacaoProduto> classificacoes = infoProdService.buscarClassificacao();
		
		for (TipoClassificacaoProduto tipoClassificacaoProduto : classificacoes) {
			comboClassificacao.add(new ItemDTO<Long,String>(tipoClassificacaoProduto.getId(), tipoClassificacaoProduto.getDescricao()));
		}
		result.include("listaClassificacao",comboClassificacao);		
	}
	
	@Post
	@Path("/buscarProduto")
	public void buscarProduto (FiltroInformacoesProdutoDTO filtro, String sortorder, String sortname, int page, int rp){
		
		this.tratarArgumentosFiltro(filtro);
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		filtro.setOrdemColuna(Util.getEnumByStringValue(FiltroInformacoesProdutoDTO.OrdemColuna.values(), sortname));
		
		tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<InformacoesProdutoDTO>> tableModel = gridProdutos(filtro, sortname);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}
	
	private TableModel<CellModelKeyValue<InformacoesProdutoDTO>> gridProdutos (FiltroInformacoesProdutoDTO filtro, String sortname) {
		
		Produto produto = prodService.obterProdutoPorCodigo(filtro.getCodProduto());
        if (StringUtils.isBlank(produto.getCodigoICD())) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Produto não possui codigo ICD cadastrado.");
        }
        filtro.setCodProduto(produto.getCodigoICD());
		
		List<InformacoesProdutoDTO> produtos = infoProdService.buscarProduto(filtro);

		if (produtos == null || produtos.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		Collections.min(produtos, new Comparator<InformacoesProdutoDTO>() {
			
			@Override
			public int compare(InformacoesProdutoDTO o1, InformacoesProdutoDTO o2) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
				if(o1 != null && o2 != null) {
					try {
						if(sdf.parse(o1.getDataLcto()).compareTo(sdf.parse(o2.getDataLcto())) < 0) {
							o2.setVenda(BigInteger.ZERO);
							return -1;
						} else {
							return 1;
						}
					} catch (ParseException e) {
					}
				} else if(o1 != null && o2 == null) {
					return 1;
				} else if(o1 == null && o2 != null) {
					return -1;
				}
				
				return 0;
			}
		});

		TableModel<CellModelKeyValue<InformacoesProdutoDTO>> tableModel = new TableModel<CellModelKeyValue<InformacoesProdutoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(produtos));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());

		return tableModel;
	}
	
	
	@Post
	@Path("/buscarBaseSugerida")
	public void baseSugerida (Long idEstudo){
		
		TableModel<CellModelKeyValue<ProdutoBaseSugeridaDTO>> tableModel = gridBaseSugerida(idEstudo);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}
	
	private TableModel<CellModelKeyValue<ProdutoBaseSugeridaDTO>> gridBaseSugerida (Long idEstudo) {
		
		List<ProdutoBaseSugeridaDTO> baseSugerida = infoProdService.buscarBaseSugerida(idEstudo);
		
		TableModel<CellModelKeyValue<ProdutoBaseSugeridaDTO>> tableModel = new TableModel<CellModelKeyValue<ProdutoBaseSugeridaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(baseSugerida));

		tableModel.setPage(1);

		tableModel.setTotal(baseSugerida.size());

		return tableModel;
	}

	@Post
	@Path("/buscarBaseEstudo")
	public void baseEstudo (Long idEstudo){
		
		TableModel<CellModelKeyValue<EdicaoBaseEstudoDTO>> tableModel = gridBaseEstudo(idEstudo);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}
	
	private TableModel<CellModelKeyValue<EdicaoBaseEstudoDTO>> gridBaseEstudo(Long idEstudo) {
		
		List<EdicaoBaseEstudoDTO> baseSugerida = infoProdService.buscarBases(idEstudo);
		
		TableModel<CellModelKeyValue<EdicaoBaseEstudoDTO>> tableModel = new TableModel<CellModelKeyValue<EdicaoBaseEstudoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(baseSugerida));

		tableModel.setPage(1);

		tableModel.setTotal(baseSugerida.size());

		return tableModel;
	}
	
	@Post
	@Path("/buscarItemRegiao")
	public void buscarItemRegiao (Long idEstudo){
		
		TableModel<CellModelKeyValue<InfoProdutosBonificacaoDTO>> tableModel = gridItemRegiao(idEstudo);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	
	}
	
	private TableModel<CellModelKeyValue<InfoProdutosBonificacaoDTO>> gridItemRegiao (Long idEstudo) {
		
		List<InfoProdutosBonificacaoDTO> itensRegiao = infoProdService.buscarItemRegiao(idEstudo);
		
		TableModel<CellModelKeyValue<InfoProdutosBonificacaoDTO>> tableModel = new TableModel<CellModelKeyValue<InfoProdutosBonificacaoDTO>>();
	
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(itensRegiao));
	
		tableModel.setPage(1);
	
		tableModel.setTotal(itensRegiao.size());
	
		return tableModel;
	}
	
	@Post
	@Path("/buscarCaracteristicaProduto")
	public void buscarCaracteristicaProduto(String codProd, Long numEdicao){

		InformacoesCaracteristicasProdDTO caracteristicas = infoProdService.buscarCaracteristicas(codProd, numEdicao);
		
		result.use(Results.json()).from(caracteristicas, "result").serialize();

	}
	
	@Post
	@Path("/buscarVendas")
	public void buscarVendas (String codProduto, Long numEdicao){

		InformacoesVendaEPerceDeVendaDTO vendas = infoProdService.buscarVendas(codProduto, numEdicao);
		
		result.use(Results.json()).from(vendas, "result").serialize();
	}
	
	@Post
	@Path("/buscarReparteSobra")
	public void buscarReparteSobra(Long idEstudo,Long codigoProduto, Long numeroEdicao){

		ResumoEstudoHistogramaPosAnaliseDTO resumo = estudoService.obterResumoEstudo(idEstudo, codigoProduto, numeroEdicao);
			
		validarDadosReparteEstudo(resumo);
		
		result.use(Results.json()).from(resumo, "result").serialize();

	}

	private void validarDadosReparteEstudo(ResumoEstudoHistogramaPosAnaliseDTO resumo) {
		// Validar dados do Resumo
		
		//abrangencia prevista
		if(resumo.getAbrangenciaSugerida()==null)
			resumo.setAbrangenciaSugerida(new BigDecimal(0));
		
		//abrangência real
		if(resumo.getAbrangenciaEstudo()==null)
			resumo.setAbrangenciaEstudo(new BigDecimal(0));
		
		//reparte minimo previsto
		if(resumo.getQtdReparteMinimoSugerido()==null)
			resumo.setQtdReparteMinimoSugerido(new BigInteger("0"));
		
		//reparte minimo real
		if(resumo.getQtdReparteMinimoEstudo()==null)
			resumo.setQtdReparteMinimoEstudo(new BigInteger("0"));
		
		//reparte Total
		if(resumo.getQtdReparteDistribuidor()==null)
			resumo.setReparteDistribuido(new BigDecimal(0));
		
		//reparte Promocional = saldo
		if(resumo.getSaldo()==null)
			resumo.setSaldo(new BigDecimal(0));
		
		//reparteDistribuido
		if(resumo.getQtdReparteDistribuidoEstudo()==null)
			resumo.setQtdReparteDistribuidoEstudo(new BigDecimal(0));
		
		//sobra
		if(resumo.getQtdSobraEstudo()==null)
			resumo.setQtdSobraEstudo(new BigDecimal(0));
		
		//reparte promocional
		if(resumo.getQtdRepartePromocional()==null)
			resumo.setQtdRepartePromocional(new BigInteger("0"));
		
	}
	
	private void tratarFiltro(FiltroInformacoesProdutoDTO filtroAtual) {
		
		FiltroInformacoesProdutoDTO filtroSession = (FiltroInformacoesProdutoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {
			
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}
	
	private void tratarArgumentosFiltro (FiltroInformacoesProdutoDTO filtro){
		
		if(filtro.getCodProduto() == null || filtro.getCodProduto().isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"Informe o Código e o Nome do produto.");
		}
		
		if(filtro.getNomeProduto() == null || filtro.getNomeProduto().isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"Informe o Código e o Nome do produto.");
		}
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroInformacoesProdutoDTO filtro = (FiltroInformacoesProdutoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		List<InformacoesProdutoDTO> produtos = infoProdService.buscarProduto(filtro);
		
		
			if(produtos.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A pesquisa realizada n�o obteve resultado.");
			}
			
			FileExporter.to("Informações_Produto", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					produtos, InformacoesProdutoDTO.class, this.httpResponse);
		
		result.nothing();
	}
}