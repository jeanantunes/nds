package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AddLoteRegiaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.RegiaoNMaiores_CotaDTO;
import br.com.abril.nds.dto.RegiaoNMaiores_ProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroRegiaoNMaioresProdDTO;
import br.com.abril.nds.dto.filtro.FiltroRegiaoNMaioresRankingDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RegiaoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.upload.XlsUploaderUtils;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/regiao")
@Rules(Permissao.ROLE_DISTRIBUICAO_REGIAO)
public class RegiaoController extends BaseController {
	private Result result;

	private static final String FILTRO_SESSION_ATTRIBUTE = "FiltroCotasRegiao";
	
	private static final String FILTRO_SESSION_ATTRIBUTE_NMaiores = "FiltroProdutosNMaiores";

	@Autowired
	private RegiaoService regiaoService;
	
	@Autowired
	private ProdutoService prodService;
	
	@Autowired
	private HttpSession session;

	@Autowired
	private HttpServletResponse httpResponse;

	@Autowired
	private CotaService cotaService;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	public RegiaoController(Result result) {
		this.result = result;
	}

	@Path("/")
	public void index(){
		this.carregarComboRegiao();
		this.carregarComboSegmento();
		this.carregarComboClassificacao();
	}

	@Post
	@Path("/salvarRegiao")
    @Rules(Permissao.ROLE_DISTRIBUICAO_REGIAO_ALTERACAO)
	public void salvarRegiao (String nome, boolean isFixa){

		this.validarEntradaRegiao(nome);

		Regiao regiao = new Regiao();
		regiao.setNomeRegiao(nome);
		regiao.setRegiaoIsFixa(isFixa);
		regiao.setIdUsuario(this.usuarioService.getUsuarioLogado());

		Date dataEHora = new Date();
		Timestamp data = new Timestamp(dataEHora.getTime());

		regiao.setDataRegiao(data);

		regiaoService.salvarRegiao(regiao);

		List <RegiaoDTO> listaRegiao = regiaoService.buscarRegiao();

		result.use(Results.json()).from(listaRegiao, "listaRegiao").recursive().serialize();
	}

	@Post
	@Path("/excluirCotaDaRegiao")
    @Rules(Permissao.ROLE_DISTRIBUICAO_REGIAO_ALTERACAO)
	public void excluirCota(Long id) {

		this.regiaoService.excluirRegistroCotaRegiao(id);

		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Cota removida com sucesso."), 
				"result").recursive().serialize();
	}

	@Post
	@Path("/excluirRegiao")
    @Rules(Permissao.ROLE_DISTRIBUICAO_REGIAO_ALTERACAO)
	public void excluirRegiao(Long id) {

		Regiao regiao = regiaoService.obterRegiaoPorId(id);

		if (regiao.isRegiaoIsFixa() == true){
            throw new ValidacaoException(TipoMensagem.WARNING, "Não é possível excluir uma região marcada como FIXA.");
		}else{

		this.regiaoService.excluirRegiao(id);
            this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Regiao excluída com sucesso."),
				"result").recursive().serialize();
		}
	}

	@Post
	@Path("/alterarRegiao")
    @Rules(Permissao.ROLE_DISTRIBUICAO_REGIAO_ALTERACAO)
	public void alterarRegiao(Long id) {

		Regiao regiao = regiaoService.obterRegiaoPorId(id);

		if (regiao.isRegiaoIsFixa() == true){
			regiao.setRegiaoIsFixa(false);
		}else{
			regiao.setRegiaoIsFixa(true);
		}

		regiaoService.alterarRegiao(regiao);

        this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Região alterada com sucesso."),
				"result").recursive().serialize();
	}

	@Post
	@Path("/carregarCotasRegiao")
	public void carregarCotasRegiao (FiltroCotasRegiaoDTO filtro, String sortorder, String sortname, int page, int rp){

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));

		this.tratarFiltroCotasRegiao(filtro);

		TableModel<CellModelKeyValue<RegiaoCotaDTO>> tableModel = efetuarConsultaCotasDaRegiao(filtro);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}

	private TableModel<CellModelKeyValue<RegiaoCotaDTO>> efetuarConsultaCotasDaRegiao(FiltroCotasRegiaoDTO filtro) {
		
		List<RegiaoCotaDTO> listaCotasRegiaoDTO = regiaoService.carregarCotasRegiao(filtro);
		
		setFaturamentoCota(listaCotasRegiaoDTO);
		
		if(filtro.getPaginacao().getSortColumn().equalsIgnoreCase("FATURAMENTO")){
			
			Collections.sort(listaCotasRegiaoDTO, new Comparator<RegiaoCotaDTO>() {
				@Override
				public int compare(RegiaoCotaDTO o1, RegiaoCotaDTO o2) {
					return o1.getFaturamentoParaOrdenacao().compareTo(o2.getFaturamentoParaOrdenacao());
				}
			});
			
			if(filtro.getPaginacao().getSortOrder().equalsIgnoreCase("DESC")){
				Collections.reverse(listaCotasRegiaoDTO);
			}
			
		}

		TableModel<CellModelKeyValue<RegiaoCotaDTO>> tableModel = new TableModel<CellModelKeyValue<RegiaoCotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasRegiaoDTO));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());

		return tableModel;
	}

	private void setFaturamentoCota(List<RegiaoCotaDTO> listaCotasRegiaoDTO) {
		
	    final Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.distribuidorService.obterDataOperacaoDistribuidor());
        
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        final Date de = calendar.getTime();
        
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        final Date ate = calendar.getTime();
        
        final Intervalo<Date> intervalo = new Intervalo<Date>(de, ate);
	    
		for (RegiaoCotaDTO regiaoCotaDTO : listaCotasRegiaoDTO) {
			
			BigDecimal faturamento = regiaoService.calcularFaturamentoCota(regiaoCotaDTO.getCotaId(), intervalo);
			
			if(faturamento != null){
				regiaoCotaDTO.setFaturamento(faturamento);
			}else{
                regiaoCotaDTO.setFaturamento(BigDecimal.ZERO);
			}
		}
		
	}
	
	@Post
	@Path("/carregarRegiao")
	public void carregarRegiao (String sortorder, String sortname, int page, int rp){

		List<RegiaoDTO> listaRegiaoDTO = regiaoService.buscarRegiao();

		TableModel<CellModelKeyValue<RegiaoDTO>> tableModel = consultaRegiaoCadastrada(listaRegiaoDTO);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	private TableModel<CellModelKeyValue<RegiaoDTO>> consultaRegiaoCadastrada(List<RegiaoDTO> listaRegiao) {

		TableModel<CellModelKeyValue<RegiaoDTO>> tableModel = new TableModel<CellModelKeyValue<RegiaoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaRegiao));

		tableModel.setPage(1);

		tableModel.setTotal(listaRegiao.size());

		return tableModel;
	}

	@Post
	@Path("/buscarPorCep")
	public void buscarPorCep(FiltroCotasRegiaoDTO filtro, String sortorder, String sortname, int page, int rp){

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));

		this.tratarFiltroCotasRegiao(filtro);

		TableModel<CellModelKeyValue<RegiaoCotaDTO>> tableModel = montarTableModelBuscaCep(filtro);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}

	private TableModel<CellModelKeyValue<RegiaoCotaDTO>> montarTableModelBuscaCep (FiltroCotasRegiaoDTO filtro) {

		List<RegiaoCotaDTO> listaCotasCep = regiaoService.buscarPorCEP(filtro);

		if (listaCotasCep == null || listaCotasCep.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		TableModel<CellModelKeyValue<RegiaoCotaDTO>> tableModel = new TableModel<CellModelKeyValue<RegiaoCotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasCep));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());

		return tableModel;
	}

	@Post
	@Path("/buscarPorSegmento")
	public void buscarPorSegmento(FiltroCotasRegiaoDTO filtro){

		tratarFiltroSegmento(filtro);

		TableModel<CellModelKeyValue<RegiaoCotaDTO>> tableModel = montarTableModelBuscaSegmento(filtro);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}

	private TableModel<CellModelKeyValue<RegiaoCotaDTO>> montarTableModelBuscaSegmento (FiltroCotasRegiaoDTO filtro) {

		List<RegiaoCotaDTO> listaCotasSegmento = regiaoService.buscarPorSegmento(filtro);

		if (listaCotasSegmento == null || listaCotasSegmento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		TableModel<CellModelKeyValue<RegiaoCotaDTO>> tableModel = new TableModel<CellModelKeyValue<RegiaoCotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasSegmento));

		tableModel.setPage(1);

		tableModel.setTotal(listaCotasSegmento.size());

		return tableModel;
	}


	@Post
	@Path("/incluirCota")
    @Rules(Permissao.ROLE_DISTRIBUICAO_REGIAO_ALTERACAO)
	public void incluirCota (List<Integer> cotas, long idRegiao){

		popularRegistroESalvarCota(cotas, idRegiao);

	}
	
	
	private String obterMensagemValidacao(Integer numCota, long idRegiao) {
		
		Cota objCota = cotaService.obterPorNumeroDaCota(numCota);
		
		if (objCota == null) {
            return "A cota [" + numCota + "] não existe.";
		}
		
		if (objCota.getSituacaoCadastro().toString().equalsIgnoreCase("Inativo")){
            return "A cota [" + numCota + "] está com status de Inativo.";
		}
		
		List<Integer> cotasCadas =  this.regiaoService.buscarNumeroCotasPorIdRegiao(idRegiao);
		
		for (Integer cotasCad : cotasCadas) {
			
			if(numCota.equals(cotasCad)) {
                return "A cota [" + numCota + "] já está cadastrada.";
			}
		}
		
		return null;
	}
	

	private void popularRegistroESalvarCota(List<Integer> cotas, long idRegiao) {
		
		List<String> mensagens = new ArrayList<String>();
		
		for (Integer cota : cotas) {

			String msg = obterMensagemValidacao(cota, idRegiao);
			
			if (!StringUtils.isEmpty(msg)) {
				
				mensagens.add(msg);
			}
			
			Cota objCota = cotaService.obterPorNumeroDaCota(cota);
			Regiao regiao = this.regiaoService.obterRegiaoPorId(idRegiao);
			
			if (objCota != null && regiao != null) {
				
				RegistroCotaRegiao registro = new RegistroCotaRegiao();
				
				if(!objCota.getSituacaoCadastro().toString().equalsIgnoreCase("inativo")){
	
					Date dataEHora = new Date();
					Timestamp data = new Timestamp(dataEHora.getTime());
	
					registro.setRegiao(regiao);
					registro.setCota(objCota);
					registro.setUsuario(this.usuarioService.getUsuarioLogado());
					registro.setDataAlteracao(data);
				
					regiaoService.addCotaNaRegiao(registro);

				}
			}
		}
		
		if(!mensagens.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, mensagens);
		}
		
        this.result.use(Results.json())
.from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso!"),
                "result").recursive()
                .serialize();
	}
	
	@Post
	@Path("/addLote")
    @Rules(Permissao.ROLE_DISTRIBUICAO_REGIAO_ALTERACAO)
	public void addCotasEmLote (UploadedFile xls, Long idRegiao) throws IOException {  
		List<Integer> numerosCota = new ArrayList<Integer>();

		List<AddLoteRegiaoDTO> listaDto = XlsUploaderUtils.getBeanListFromXls(AddLoteRegiaoDTO.class, xls);
		
		for (AddLoteRegiaoDTO list : listaDto) {
			numerosCota.add(list.getNumeroCota());
		}
		
		popularRegistroESalvarCota(numerosCota, idRegiao);
	}

	@Post
	@Path("/buscarProduto")
	public void buscarProduto (FiltroRegiaoNMaioresProdDTO filtro, String sortorder, String sortname, int page, int rp){
		
		this.tratarArgumentosFiltro(filtro);
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		tratarFiltroNMaiores(filtro);
		
		TableModel<CellModelKeyValue<RegiaoNMaiores_ProdutoDTO>> tableModel = gridProdutos(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}
	
	private TableModel<CellModelKeyValue<RegiaoNMaiores_ProdutoDTO>> gridProdutos (FiltroRegiaoNMaioresProdDTO filtro) {
		
		Produto produto = prodService.obterProdutoPorCodigo(filtro.getCodigoProduto());
		filtro.setCodigoProduto(produto.getCodigoICD());
				
		List<RegiaoNMaiores_ProdutoDTO> produtos = regiaoService.buscarProdutos(filtro);
		
		if (produtos == null || produtos.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		TableModel<CellModelKeyValue<RegiaoNMaiores_ProdutoDTO>> tableModel = new TableModel<CellModelKeyValue<RegiaoNMaiores_ProdutoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(produtos));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());

		return tableModel;
	}
	
	@Post
	@Path("/rankingCota")
	public void rankingCota(FiltroRegiaoNMaioresRankingDTO filtro) throws Exception{

		List<RegiaoNMaiores_CotaDTO> cotasRanking = new ArrayList<>();

		TableModel<CellModelKeyValue<RegiaoNMaiores_CotaDTO>> tableModel = montarTableModelRanking(filtro, cotasRanking);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	

	private TableModel<CellModelKeyValue<RegiaoNMaiores_CotaDTO>> montarTableModelRanking (FiltroRegiaoNMaioresRankingDTO filtro, List<RegiaoNMaiores_CotaDTO> cotasRanking) throws Exception {
		
		List<RegiaoNMaiores_CotaDTO> ranking = null;
		
		List<String> ids = tratarCodigosENumeros(filtro);
		Integer limite = filtro.getLimitePesquisa();
		
		if(ids != null){
			ranking = regiaoService.rankingCotas(ids, limite);
		}
		
		if (ranking == null || ranking.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Não foi possível montar um ranking, por produto edição! Não há dados.");
		}

		TableModel<CellModelKeyValue<RegiaoNMaiores_CotaDTO>> tableModel = new TableModel<CellModelKeyValue<RegiaoNMaiores_CotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(ranking));

		tableModel.setPage(1);

		tableModel.setTotal(ranking.size());

		return tableModel;
	}
	
	@Post
	@Path("/filtroRankingCota")
	public void filtroRankingCota(Integer numCota) {

		TableModel<CellModelKeyValue<RegiaoNMaiores_CotaDTO>> tableModel = montarTableFiltroRanking(numCota);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	

	private TableModel<CellModelKeyValue<RegiaoNMaiores_CotaDTO>> montarTableFiltroRanking (Integer numCota) {
		
		List<RegiaoNMaiores_CotaDTO> filtroCotasRanking = regiaoService.filtroRankingCotas(numCota);

		TableModel<CellModelKeyValue<RegiaoNMaiores_CotaDTO>> tableModel = new TableModel<CellModelKeyValue<RegiaoNMaiores_CotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(filtroCotasRanking));

		tableModel.setPage(1);

		tableModel.setTotal(1);

		return tableModel;
	}
	
	private List<String> tratarCodigosENumeros(FiltroRegiaoNMaioresRankingDTO filtro) {
		String codigos[] = null;
		for (String cod : filtro.getCodigoProduto()) {
			codigos = cod.split(",");
		}
		
		String numeros[] = null;
		for (String num : filtro.getNumeroEdicao()) {
			numeros = num.split(",");
		}
		
		List<String> ids = obterIdsProdEdicaoParaRanking(codigos, numeros); 
		
		return ids;
	}

	private List<String> obterIdsProdEdicaoParaRanking(String[] codigos, String[] numeros) {
		
		List<String> idS_prodEdicao = new ArrayList<>();
		
		for (int i = 0; i < codigos.length; i++) {
			
			for (int u = 0; u < numeros.length; u++) {

				if(i == u){
				String cod = codigos[i];
				String num = numeros[u];
				
				List<String> idsTemp = regiaoService.listaIdProdEdicaoParaRanking(cod, num); 
				
				idS_prodEdicao.addAll(idsTemp);
				
				}
			}
		}
		return idS_prodEdicao;
	}
	
	private void tratarArgumentosFiltro (FiltroRegiaoNMaioresProdDTO filtro){
		
		if(filtro.getCodigoProduto() == null || filtro.getCodigoProduto().isEmpty()){
            throw new ValidacaoException(TipoMensagem.WARNING, "Informe o Código e o Nome do produto.");
		}
		
		if(filtro.getNome() == null || filtro.getNome().isEmpty()){
            throw new ValidacaoException(TipoMensagem.WARNING, "Informe o Código e o Nome do produto.");
		}
	}
	
	private void tratarFiltroNMaiores(FiltroRegiaoNMaioresProdDTO filtroAtual) {
		
		FiltroRegiaoNMaioresProdDTO filtroSession = (FiltroRegiaoNMaioresProdDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_NMaiores);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {
			
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_NMaiores, filtroAtual);
	}
	
	@Post   
	public void validarRegiaoCota(Integer cota, Long idRegiao) {
		
		String msg = obterMensagemValidacao(cota, idRegiao);
		
		if (!StringUtils.isEmpty(msg)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, msg);
		}
		
		result.use(Results.json()).from(Results.nothing()).serialize();
	}
	
	@Post
	@Path("/carregarComboRegiao")
	public void carregarComboRegiao() {

		List<ItemDTO<Long,String>> comboRegiao =  new ArrayList<ItemDTO<Long,String>>();
		List<RegiaoDTO> regioes = regiaoService.buscarRegiao();

		for (RegiaoDTO itemRegiao : regioes) {
			comboRegiao.add(new ItemDTO<Long,String>(itemRegiao.getIdRegiao() , itemRegiao.getNomeRegiao()));
		}

		result.include("listaRegiao",comboRegiao);
	}

	private void carregarComboSegmento() {

		List<ItemDTO<Long,String>> comboSegmento =  new ArrayList<ItemDTO<Long,String>>();

		List<TipoSegmentoProduto> segmentos = regiaoService.carregarSegmentos();

		for (TipoSegmentoProduto itemSegmento : segmentos) {
			comboSegmento.add(new ItemDTO<Long,String>(itemSegmento.getId(), itemSegmento.getDescricao()));
		}

		result.include("listaSegmento",comboSegmento );
	}

	private void carregarComboClassificacao(){
		List<ItemDTO<Long,String>> comboClassificacao =  new ArrayList<ItemDTO<Long,String>>();

		List<TipoClassificacaoProduto> classificacoes = regiaoService.buscarClassificacao();

		for (TipoClassificacaoProduto tipoClassificacaoProduto : classificacoes) {
			comboClassificacao.add(new ItemDTO<Long,String>(tipoClassificacaoProduto.getId(), tipoClassificacaoProduto.getDescricao()));
		}
		result.include("listaClassificacao",comboClassificacao);		
	}

	private void tratarFiltroSegmento(FiltroCotasRegiaoDTO filtro) {

		if(filtro.getLimiteBuscaPorSegmento() == null || filtro.getLimiteBuscaPorSegmento() == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe a quantidade de Cotas.");
		}
	}

	private void validarEntradaRegiao(String nomeRegiao) {
		if (nomeRegiao == null || (nomeRegiao.isEmpty())) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nome da regiao é obrigatório.");
		}

		List<RegiaoDTO> listaRegiaoDTO = regiaoService.buscarRegiao();

		for (RegiaoDTO regiaoDTO : listaRegiaoDTO) {
			if (regiaoDTO.getNomeRegiao().equalsIgnoreCase(nomeRegiao)) {
                throw new ValidacaoException(TipoMensagem.WARNING, "Região já cadastrada.");
			}
		}
	}

	private void tratarFiltroCotasRegiao (FiltroCotasRegiaoDTO filtroAtual) {

		FiltroCotasRegiaoDTO filtroSession = (FiltroCotasRegiaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);

		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}

		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}

	@Get
	public void exportar(FileType fileType) throws IOException {

		FiltroCotasRegiaoDTO filtro = (FiltroCotasRegiaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);

		List<RegiaoCotaDTO> listaCotasRegiaoDTO = regiaoService.carregarCotasRegiao(filtro);

        FileExporter.to("Cotas_Cadastradas_Na_Região", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro,
				listaCotasRegiaoDTO, RegiaoCotaDTO.class, this.httpResponse);

		result.nothing();
	}
}