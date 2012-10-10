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
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.CFOPService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EntradaNFETerceirosService;
import br.com.abril.nds.service.FornecedorService;
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
@Path(value="/nfe/entradaNFETerceiros")
public class EntradaNFETerceirosController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE_CONSULTA = "filtroConsultaNFEEncalheTratamento";
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private EntradaNFETerceirosService entradaNFETerceirosService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
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
	@Rules(Permissao.ROLE_NFE_ENTRADA_NFE_TERCEIROS)	
	public void index(){
		carregarComboStatusNota();
		
		List<Fornecedor> listFornecedores = fornecedorService.obterFornecedores();
		result.include("listFornecedores", listFornecedores);
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
	public void pesquisarNotasRecebidas(FiltroEntradaNFETerceiros filtro, String sortorder, String sortname, int page, int rp){

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>> tableModel = efetuarConsultaNotasRecebidas(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>> efetuarConsultaNotasRecebidas(FiltroEntradaNFETerceiros filtro) {
		
		/*List<ConsultaEntradaNFETerceirosRecebidasDTO> listaNotasRecebidas = this.entradaNFETerceirosService.buscarNFNotasRecebidas(filtro, true);
		
		TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>>();
		
		if(listaNotasRecebidas.size() == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}*/
		
		List<ConsultaEntradaNFETerceirosRecebidasDTO> listaNotasRecebidas = new ArrayList<ConsultaEntradaNFETerceirosRecebidasDTO>();
		
		ConsultaEntradaNFETerceirosRecebidasDTO nota1 = new ConsultaEntradaNFETerceirosRecebidasDTO();
		nota1.setChaveAcesso("chaveAcesso");
		nota1.setContemDiferenca(new Integer(1));
		nota1.setDataEmissao(new Date());
		nota1.setNome("Victor Henrique");
		nota1.setNumeroNota(new Long("12231"));
		nota1.setSerie("192837456");
		nota1.setTipoNotaFiscal(TipoOperacao.SAIDA);
		nota1.setValorNota(new BigDecimal(9090));
		
		ConsultaEntradaNFETerceirosRecebidasDTO nota2 = new ConsultaEntradaNFETerceirosRecebidasDTO();
		nota2.setChaveAcesso("chaveAcesso2");
		nota2.setContemDiferenca(new Integer(0));
		nota2.setDataEmissao(new Date());
		nota2.setNome("Victor Henrique");
		nota2.setNumeroNota(new Long(445566));
		nota2.setSerie("910293758921");
		nota2.setTipoNotaFiscal(TipoOperacao.SAIDA);
		nota2.setValorNota(new BigDecimal(9090));
		
		listaNotasRecebidas.add(nota1);
		listaNotasRecebidas.add(nota2);
		
		TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNotasRecebidas));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(listaNotasRecebidas.size());
		
		return tableModel;
	}
	
	@Post
	@Path("/pesquisarNotasPendentes")
	public void pesquisarNotasPendentes(FiltroEntradaNFETerceiros filtro, String sortorder, String sortname, int page, int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>> tableModel = efetuarConsultaNotasPendentes(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>> efetuarConsultaNotasPendentes(FiltroEntradaNFETerceiros filtro) {


		/*List<ConsultaEntradaNFETerceirosPendentesDTO> listaNotasPendentes = this.entradaNFETerceirosService.buscarNFNotasPendentes(filtro, true);

		
		TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>>();
		
		if(listaNotasPendentes.size() == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}*/
		
		List<ConsultaEntradaNFETerceirosPendentesDTO> listaNotasPendentes = new ArrayList<ConsultaEntradaNFETerceirosPendentesDTO>();
		
		ConsultaEntradaNFETerceirosPendentesDTO nota1 = new ConsultaEntradaNFETerceirosPendentesDTO();
		nota1.setChaveAcesso("1234");
		nota1.setDataEncalhe(new Date());
		nota1.setDiferenca(new BigDecimal(444));
//		nota1.setIdNotaFiscalEntrada(new Long(15));
		nota1.setNome("Victor Montanher");
		nota1.setNumeroCota(new Integer(1234));
		nota1.setNumeroNfe(new Long(778899));
		nota1.setSerie("4356");
		nota1.setStatus("APROVADO");
		nota1.setTipoNotaFiscal(TipoOperacao.ENTRADA);
		nota1.setValorNota(new BigDecimal(999));
		nota1.setValorReal(new BigDecimal(999));
		
		ConsultaEntradaNFETerceirosPendentesDTO nota2 = new ConsultaEntradaNFETerceirosPendentesDTO();
		nota2.setChaveAcesso("9876");
		nota2.setDataEncalhe(new Date());
		nota2.setDiferenca(new BigDecimal(444));
//		nota2.setIdNotaFiscalEntrada(new Long(15));
		nota2.setNome("Victor Henrique Montanher");
		nota2.setNumeroCota(new Integer(1234));
		nota2.setNumeroNfe(new Long(778899));
		nota2.setSerie("4356");
		nota2.setStatus("APROVADO");
		nota2.setTipoNotaFiscal(TipoOperacao.ENTRADA);
		nota2.setValorNota(new BigDecimal(999));
		nota2.setValorReal(new BigDecimal(999));
		
		listaNotasPendentes.add(nota1);
		listaNotasPendentes.add(nota2);
		
		TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNotasPendentes));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(listaNotasPendentes.size());
		
		return tableModel;
	}
	
	@Post
	@Path("/pesquisarItensPorNota")
	public void pesquisarItensPorNota(FiltroEntradaNFETerceiros filtro, String sortorder, String sortname, int page, int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<ItemNotaFiscalPendenteDTO>> tableModel = efetuarConsultaItensPorNota(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ItemNotaFiscalPendenteDTO>> efetuarConsultaItensPorNota(FiltroEntradaNFETerceiros filtro) {
		
		/*List<ItemNotaFiscalPendenteDTO> listaNotasRecebidas = this.entradaNFETerceirosService.buscarItensPorNota(filtro);
		
		TableModel<CellModelKeyValue<ItemNotaFiscalPendenteDTO>> tableModel = new TableModel<CellModelKeyValue<ItemNotaFiscalPendenteDTO>>();
		
		Integer totalRegistros = this.entradaNFETerceirosService.buscarTodasItensPorNota(filtro);
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}*/
		
		List<ItemNotaFiscalPendenteDTO> listaItens =  new ArrayList<ItemNotaFiscalPendenteDTO>();
		
		ItemNotaFiscalPendenteDTO item1 = new ItemNotaFiscalPendenteDTO("1234", "Prod Teste", new Long(444), new BigDecimal(10), new BigDecimal(10), 
				new BigDecimal(10), new BigDecimal(5), new BigDecimal(10), new Date(), new Date());
		ItemNotaFiscalPendenteDTO item2 = new ItemNotaFiscalPendenteDTO("4321", "Prod Teste2", new Long(555), new BigDecimal(55), new BigDecimal(55), 
				new BigDecimal(20), new BigDecimal(20), new BigDecimal(10), new Date(), new Date());
		
		listaItens.add(item1);
		listaItens.add(item2);
		
		TableModel<CellModelKeyValue<ItemNotaFiscalPendenteDTO>> tableModel = new TableModel<CellModelKeyValue<ItemNotaFiscalPendenteDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaItens));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(listaItens.size());
		
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
		nota.setTipoNotaFiscal(tp);
		
		this.notaFiscalEntradaService.inserirNotaFiscal(nota);
		
	}
	
	private void validarEntrada(FiltroEntradaNFETerceiros filtro) {
				
		if(filtro.getStatusNotaFiscalEntrada() == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Pelo menos um filtro deve ser preenchido!");			
		}
	}
	
	private void tratarFiltro(FiltroEntradaNFETerceiros filtroAtual) {

		FiltroEntradaNFETerceiros filtroSession = (FiltroEntradaNFETerceiros) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSULTA);
		
		if (filtroSession != null && filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_CONSULTA, filtroAtual);
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroEntradaNFETerceiros filtro = (FiltroEntradaNFETerceiros) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSULTA);
		
		if(filtro.getStatusNotaFiscalEntrada().name().equals("RECEBIDA")){
			//List<ConsultaEntradaNFETerceirosRecebidasDTO> listaNotasRecebidas = this.entradaNFETerceirosService.buscarNFNotasRecebidas(filtro, false);
			List<ConsultaEntradaNFETerceirosRecebidasDTO> listaNotasRecebidas = new ArrayList<ConsultaEntradaNFETerceirosRecebidasDTO>();
			/*if(listaNotasRecebidas.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}*/
			
			FileExporter.to("consulta_notas_recebidas", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					listaNotasRecebidas, ConsultaEntradaNFETerceirosRecebidasDTO.class, this.httpResponse);			
		}else{
			
			//List<ConsultaEntradaNFETerceirosPendentesDTO> listaNotasPendentes = this.entradaNFETerceirosService.buscarNFNotasPendentes(filtro, false);
			List<ConsultaEntradaNFETerceirosPendentesDTO> listaNotasPendentes = new ArrayList<ConsultaEntradaNFETerceirosPendentesDTO>();
			/*if(listaNotasRecebidas.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}*/
			
			FileExporter.to("consulta_notas_pendentes", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					listaNotasPendentes, ConsultaEntradaNFETerceirosPendentesDTO.class, this.httpResponse);
			
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
