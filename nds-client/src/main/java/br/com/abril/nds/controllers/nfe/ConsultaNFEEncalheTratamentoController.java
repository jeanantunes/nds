package br.com.abril.nds.controllers.nfe;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.ConsultaNFENotasPendentesDTO;
import br.com.abril.nds.dto.ConsultaNFENotasRecebidasDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNFEEncalheTratamento;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.CFOPService;
import br.com.abril.nds.service.ConsultaNFEEncalheTratamentoNotasRecebidasService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.NotaFiscalEntradaService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path(value="/nfe/consultaNFEEncalheTratamento")
public class ConsultaNFEEncalheTratamentoController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE_CONSULTA = "filtroConsultaNFEEncalheTratamento";
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private ConsultaNFEEncalheTratamentoNotasRecebidasService consultaNFEEncalheTratamentoNotasRecebidasService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private NotaFiscalEntradaService notaFiscalEntradaService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;
	
	@Autowired
	private TipoNotaFiscalService tipoNotaFiscalService;
	
	@Autowired
	private CFOPService cfopService;
	
	@Autowired
	private CotaService cotaService;
	
	@Path("/")
	@Rules(Permissao.ROLE_NFE_CONSULTA_NFE_ENCALHE_TRATAMENTO)
	public void index(){
		carregarComboStatusNota();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void carregarComboStatusNota() {
		
	List<ItemDTO<String, String>> comboStatusNota = new ArrayList<ItemDTO<String, String>>();
		
		comboStatusNota.add(new ItemDTO(StatusNotaFiscalEntrada.RECEBIDA.name(), StatusNotaFiscalEntrada.RECEBIDA.getDescricao()));
		comboStatusNota.add(new ItemDTO(StatusNotaFiscalEntrada.PENDENTE_RECEBIMENTO.name(), StatusNotaFiscalEntrada.PENDENTE_RECEBIMENTO.getDescricao()));
		comboStatusNota.add(new ItemDTO(StatusNotaFiscalEntrada.PENDENTE_EMISAO.name(), StatusNotaFiscalEntrada.PENDENTE_EMISAO.getDescricao()));

		result.include("comboStatusNota", comboStatusNota);
		
	}
	
	@Post
	@Path("/pesquisarNotasRecebidas")
	public void pesquisarNotasRecebidas(FiltroConsultaNFEEncalheTratamento filtro, String sortorder, String sortname, int page, int rp){

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<ConsultaNFENotasRecebidasDTO>> tableModel = efetuarConsultaNotasRecebidas(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ConsultaNFENotasRecebidasDTO>> efetuarConsultaNotasRecebidas(FiltroConsultaNFEEncalheTratamento filtro) {
		
		List<ConsultaNFENotasRecebidasDTO> listaNotasRecebidas = this.consultaNFEEncalheTratamentoNotasRecebidasService.buscarNFNotasRecebidas(filtro, "limitar");
		
		TableModel<CellModelKeyValue<ConsultaNFENotasRecebidasDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaNFENotasRecebidasDTO>>();
		
		Integer totalRegistros = this.consultaNFEEncalheTratamentoNotasRecebidasService.buscarTodasNFENotasRecebidas(filtro);
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNotasRecebidas));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	@Post
	@Path("/pesquisarNotasPendentes")
	public void pesquisarNotasPendentes(FiltroConsultaNFEEncalheTratamento filtro, String sortorder, String sortname, int page, int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<ConsultaNFENotasPendentesDTO>> tableModel = efetuarConsultaNotasPendentes(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<ConsultaNFENotasPendentesDTO>> efetuarConsultaNotasPendentes(FiltroConsultaNFEEncalheTratamento filtro) {
		
		List<ConsultaNFENotasPendentesDTO> listaNotasRecebidas = this.consultaNFEEncalheTratamentoNotasRecebidasService.buscarNFNotasPendentes(filtro, "limitar");
		
		TableModel<CellModelKeyValue<ConsultaNFENotasPendentesDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaNFENotasPendentesDTO>>();
		
		Integer totalRegistros = this.consultaNFEEncalheTratamentoNotasRecebidasService.buscarTodasNFENotasRecebidas(filtro);
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNotasRecebidas));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	@Post
	@Path("/pesquisarItensPorNota")
	public void pesquisarItensPorNota(FiltroConsultaNFEEncalheTratamento filtro, String sortorder, String sortname, int page, int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<ItemNotaFiscalPendenteDTO>> tableModel = efetuarConsultaItensPorNota(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ItemNotaFiscalPendenteDTO>> efetuarConsultaItensPorNota(FiltroConsultaNFEEncalheTratamento filtro) {
		
		List<ItemNotaFiscalPendenteDTO> listaNotasRecebidas = this.consultaNFEEncalheTratamentoNotasRecebidasService.buscarItensPorNota(filtro);
		
		TableModel<CellModelKeyValue<ItemNotaFiscalPendenteDTO>> tableModel = new TableModel<CellModelKeyValue<ItemNotaFiscalPendenteDTO>>();
		
		Integer totalRegistros = this.consultaNFEEncalheTratamentoNotasRecebidasService.buscarTodasItensPorNota(filtro);
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNotasRecebidas));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	

	@Post
	@Path("/cadastrarNota")
	public void cadastrarNota(NotaFiscalEntradaCota nota, Integer numeroCota){
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		CFOP cfop  = this.cfopService.buscarPorId(1l);
		PessoaJuridica pj = this.pessoaJuridicaService.buscarPorId(1L);
		TipoNotaFiscal tp = this.tipoNotaFiscalService.obterPorId(1L);
		
		nota.setDataEmissao(new Date());
		nota.setDataExpedicao(new Date());
		nota.setOrigem(Origem.INTERFACE);
		nota.setValorBruto(BigDecimal.TEN);
		nota.setValorLiquido(BigDecimal.TEN);
		nota.setValorDesconto(BigDecimal.TEN);
		nota.setCota(cota);
		nota.setStatusNotaFiscal(StatusNotaFiscalEntrada.RECEBIDA);
		
		nota.setCfop(cfop);
		nota.setEmitente(pj);
		nota.setTipoNotaFiscal(tp);
		
		this.notaFiscalEntradaService.inserirNotaFiscal(nota);
		
	}
	
	private void validarEntrada(FiltroConsultaNFEEncalheTratamento filtro) {
				
		if(filtro.getStatusNotaFiscalEntrada() == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Pelo menos um filtro deve ser preenchido!");			
		}
	}
	
	private void tratarFiltro(FiltroConsultaNFEEncalheTratamento filtroAtual) {

		FiltroConsultaNFEEncalheTratamento filtroSession = (FiltroConsultaNFEEncalheTratamento) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSULTA);
		
		if (filtroSession != null && filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_CONSULTA, filtroAtual);
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroConsultaNFEEncalheTratamento filtro = (FiltroConsultaNFEEncalheTratamento) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSULTA);
		
		if(filtro.getStatusNotaFiscalEntrada().name().equals("RECEBIDA")){
			List<ConsultaNFENotasRecebidasDTO> listaNotasRecebidas = this.consultaNFEEncalheTratamentoNotasRecebidasService.buscarNFNotasRecebidas(filtro, "naoLimitar");
			
			if(listaNotasRecebidas.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("consulta_notas_recebidas", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					listaNotasRecebidas, ConsultaNFENotasRecebidasDTO.class, this.httpResponse);			
		}else{
			
			List<ConsultaNFENotasPendentesDTO> listaNotasRecebidas = this.consultaNFEEncalheTratamentoNotasRecebidasService.buscarNFNotasPendentes(filtro, "naoLimitar");
			
			if(listaNotasRecebidas.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("consulta_notas_pendentes", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					listaNotasRecebidas, ConsultaNFENotasPendentesDTO.class, this.httpResponse);
			
		}
			
		
		result.nothing();
	}
	
	@Post
	@Path("/buscarCotaPorNumero")
	public void buscarCotaPorNumero(Integer numeroCota) {
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		if (cota == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Cota inexistente.");
		}

		Pessoa pessoa = cota.getPessoa();

		String nomeCota = null;

		if (pessoa instanceof PessoaFisica) {

			nomeCota = ((PessoaFisica) pessoa).getNome();

		} else if (pessoa instanceof PessoaJuridica) {

			nomeCota = ((PessoaJuridica) pessoa).getRazaoSocial();
		}

		this.result.use(Results.json()).from(nomeCota, "result").recursive().serialize();
	}
	
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	public Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Lazaro Jornaleiro");
		return usuario;
	}

}
