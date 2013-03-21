package br.com.abril.nds.controllers.distribuicao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.RegiaoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.upload.KeyValue;
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
public class RegiaoController extends BaseController {
	private Result result;
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "FiltroCotasRegiao";
	
	@Autowired
	private RegiaoService regiaoService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public RegiaoController(Result result) {
		this.result = result;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_DISTRIBUICAO_REGIAO)
	public void index(){
		this.carregarComboRegiao();
		this.carregarComboSegmento();
		this.carregarComboClassificacao();
		
	}
	
	@Post
	@Path("/salvarRegiao")
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
	public void excluirCota(Long id) {
		
		this.regiaoService.excluirRegistroCotaRegiao(id);
			
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Cota removida com sucesso."), 
				"result").recursive().serialize();
	}
	
	@Post
	@Path("/excluirRegiao")
	public void excluirRegiao(Long id) {
		
		Regiao regiao = regiaoService.obterRegiaoPorId(id);
		
		if (regiao.isRegiaoIsFixa() == true){
			throw new ValidacaoException(TipoMensagem.WARNING, "Não é possível excluir uma região marcada como FIXA.");
		}else{
			
		this.regiaoService.excluirRegiao(id);
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Regiao excluída com sucesso."), 
				"result").recursive().serialize();
		}
	}
	
	@Post
	@Path("/alterarRegiao")
	public void alterarRegiao(Long id) {
		
		Regiao regiao = regiaoService.obterRegiaoPorId(id);
		
		if (regiao.isRegiaoIsFixa() == true){
			regiao.setRegiaoIsFixa(false);
		}else{
			regiao.setRegiaoIsFixa(true);
		}
		
		regiaoService.alterarRegiao(regiao);
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Região alterada com sucesso."), 
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

		TableModel<CellModelKeyValue<RegiaoCotaDTO>> tableModel = new TableModel<CellModelKeyValue<RegiaoCotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasRegiaoDTO));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
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
		
//		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
//		this.tratarFiltroCotasRegiao(filtro);
		
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
		
//		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
//
//		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());

		return tableModel;
	}

	
	@Post
	@Path("/incluirCota")
	public void incluirCota (List<Integer> cotas, long idRegiao){
		
		
		this.validarEntradaDeVariasCotas(cotas, idRegiao);
		
		popularRegistroESalvarCota(cotas, idRegiao);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Cota inserida com sucesso!"), 
				"result").recursive().serialize();
	}

	private void popularRegistroESalvarCota(List<Integer> cotas, long idRegiao) {
		for (int numeroCota : cotas) {
			
			RegistroCotaRegiao registro = new RegistroCotaRegiao();

			Date dataEHora = new Date();
			Timestamp data = new Timestamp(dataEHora.getTime());
			
			registro.setRegiao(this.regiaoService.obterRegiaoPorId(idRegiao));
			registro.setCota(this.cotaService.obterPorNumeroDaCota(numeroCota));
			registro.setUsuario(this.usuarioService.getUsuarioLogado());
			registro.setDataAlteracao(data);
			
			regiaoService.addCotaNaRegiao(registro);
			
		}
	}
	
	@Post
	@Path("/addLote")
	public void addCotasEmLote (UploadedFile xls, Long idRegiao) throws IOException {  
		List<Integer> numerosCota = new ArrayList<Integer>();
		
		File x = upLoadArquivo(xls);

		List<KeyValue> lista = XlsUploaderUtils.returnKeyValueFromXls(x);
		
		for (KeyValue keyValue : lista) {
			if(keyValue.getValue() != null){
			Double xjiji = (Double) keyValue.getValue();
			numerosCota.add(xjiji.intValue());
			}
		}
		
		//Implementar a mensagem de erro...
		this.validarEntradaDeVariasCotas(numerosCota, idRegiao);
		this.popularRegistroESalvarCotasEmLote(numerosCota, idRegiao);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Cotas inseridas com sucesso!"),
			"result").recursive().serialize();

	}

	private File upLoadArquivo(UploadedFile xls) throws IOException, FileNotFoundException {
		File x = new File(xls.getFileName());
	    File destino = new File(xls.getFileName());  
	    destino.createNewFile();  
	    InputStream stream = xls.getFile();  
	    IOUtils.copy(stream,new FileOutputStream(destino));
		return x;
	}
	
	private void popularRegistroESalvarCotasEmLote(List<Integer> cotas, long idRegiao) {
		boolean mensagem = false;
		String numCotaCadastrada = "";
		
		for (int numeroCota : cotas) {
			
			RegistroCotaRegiao registro = new RegistroCotaRegiao();

			Date dataEHora = new Date();
			Timestamp data = new Timestamp(dataEHora.getTime());
			
			registro.setRegiao(this.regiaoService.obterRegiaoPorId(idRegiao));
			registro.setCota(this.cotaService.obterPorNumeroDaCota(numeroCota));
			registro.setUsuario(this.usuarioService.getUsuarioLogado());
			registro.setDataAlteracao(data);
			
			
			if(registro.getCota() == null){
				mensagem = true;
				numCotaCadastrada += "["+numeroCota+"] ";
			}
			regiaoService.addCotaNaRegiao(registro);
		}
		
		if(mensagem == true){
			throw new ValidacaoException(TipoMensagem.WARNING, "As cotas " + numCotaCadastrada + " não existem.");
		}
	}
	
	private void validarEntradaDeVariasCotas(List<Integer> cotas, Long idRegiao) {
		
		List<Integer> cotasCadas =  this.regiaoService.buscarNumeroCotasPorIdRegiao(idRegiao);
		
		boolean mensagem = false;
		String numCotaCadastrada = "";
		
		for (Integer numCota : cotasCadas) {
			for (Integer cota : cotas) {
				if(numCota == cota){
					mensagem = true;
					numCotaCadastrada += "["+cota+"] ";
				}
			}
		}
		if(mensagem == true){
			throw new ValidacaoException(TipoMensagem.WARNING, "As cotas " + numCotaCadastrada + " já estão cadastradas, retire-as e faça uma nova inclusão.");
		}
	}
	
	private void carregarComboRegiao() {
		
		List<ItemDTO<Long,String>> comboRegiao =  new ArrayList<ItemDTO<Long,String>>();
		List<RegiaoDTO> regioes = regiaoService.buscarRegiao();
		
		for (RegiaoDTO itemRegiao : regioes) {
			comboRegiao.add(new ItemDTO<Long,String>(itemRegiao.getIdRegiao() , itemRegiao.getNomeRegiao()));
		}
		
		result.include("listaRegiao",comboRegiao );
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
		
		if(filtro.getLimiteBuscaPorSegmento() > 4) {
			filtro.setLimiteBuscaPorSegmento(filtro.getLimiteBuscaPorSegmento() + 1);
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
		
		if(listaCotasRegiaoDTO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A pesquisa realizada não obteve resultado.");
		}
		
		FileExporter.to("Cotas_Cadastradas_Na_Região", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				listaCotasRegiaoDTO, RegiaoCotaDTO.class, this.httpResponse);
		
		result.nothing();
	}
}
