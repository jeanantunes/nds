package br.com.abril.nds.controllers.nfe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CFOPService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EntradaNFETerceirosService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.NotaFiscalEntradaService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path(value="/nfe/entradaNFETerceiros")
@Rules(Permissao.ROLE_NFE_ENTRADA_NFE_TERCEIROS)	
public class EntradaNFETerceirosController extends BaseController {
	
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
	public void index(){
		this.carregarComboStatusNota();
		this.carregarFornecedores();
	}

	private void carregarFornecedores() {
		
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
		
		List<ConsultaEntradaNFETerceirosRecebidasDTO> listaNotasRecebidas = this.entradaNFETerceirosService.buscarNFNotasRecebidas(filtro, true);

		Integer tamanhoListaNotasRecebidas = this.entradaNFETerceirosService.buscarTodasNFNotas(filtro);
		
		TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>>();
		
		if(listaNotasRecebidas.size() == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}
		
		/*List<ConsultaEntradaNFETerceirosRecebidasDTO> listaNotasRecebidas = new ArrayList<ConsultaEntradaNFETerceirosRecebidasDTO>();
		
		ConsultaEntradaNFETerceirosRecebidasDTO nota1 = new ConsultaEntradaNFETerceirosRecebidasDTO();
		nota1.setChaveAcesso("chaveAcesso");
		nota1.setContemDiferenca(Integer.valueOf(1));
		nota1.setDataEmissao(new Date());
		nota1.setNome("Victor Henrique");
		nota1.setNumeroNota(Long.valueOf("12231"));
		nota1.setSerie("192837456");
		nota1.setTipoNotaFiscal("SAIDA");
		nota1.setValorNota(new BigDecimal(9090));
		
		ConsultaEntradaNFETerceirosRecebidasDTO nota2 = new ConsultaEntradaNFETerceirosRecebidasDTO();
		nota2.setChaveAcesso("chaveAcesso2");
		nota2.setContemDiferenca(Integer.valueOf(0));
		nota2.setDataEmissao(new Date());
		nota2.setNome("Victor Henrique");
		nota2.setNumeroNota(Long.valueOf(445566));
		nota2.setSerie("910293758921");
		nota2.setTipoNotaFiscal("SAIDA");
		nota2.setValorNota(new BigDecimal(9090));
		
		listaNotasRecebidas.add(nota1);
		listaNotasRecebidas.add(nota2);
		
		TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>>();*/
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNotasRecebidas));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(tamanhoListaNotasRecebidas);
		
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


		List<ConsultaEntradaNFETerceirosPendentesDTO> listaNotasPendentes = this.entradaNFETerceirosService.buscarNFNotasPendentes(filtro, true);

		Integer tamanhoListaNotasPendentes = this.entradaNFETerceirosService.buscarTodasNFNotas(filtro);
		
		TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>>();
		
		if(listaNotasPendentes.size() == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}
		
		/*List<ConsultaEntradaNFETerceirosPendentesDTO> listaNotasPendentes = new ArrayList<ConsultaEntradaNFETerceirosPendentesDTO>();
		
		ConsultaEntradaNFETerceirosPendentesDTO nota1 = new ConsultaEntradaNFETerceirosPendentesDTO();
		nota1.setChaveAcesso("1234");
		nota1.setDataEncalhe(new Date());
		nota1.setDiferenca(new BigDecimal(444));
//		nota1.setIdNotaFiscalEntrada(Long.valueOf(15));
		nota1.setNome("Victor Montanher");
		nota1.setNumeroCota(Integer.valueOf(1234));
		nota1.setNumeroNfe(Long.valueOf(778899));
		nota1.setSerie("4356");
		nota1.setStatus("APROVADO");
		nota1.setTipoNotaFiscal("Entrada");
		nota1.setValorNota(new BigDecimal(999));
		nota1.setValorReal(new BigDecimal(999));
		
		ConsultaEntradaNFETerceirosPendentesDTO nota2 = new ConsultaEntradaNFETerceirosPendentesDTO();
		nota2.setChaveAcesso("9876");
		nota2.setDataEncalhe(new Date());
		nota2.setDiferenca(new BigDecimal(444));
//		nota2.setIdNotaFiscalEntrada(Long.valueOf(15));
		nota2.setNome("Victor Henrique Montanher");
		nota2.setNumeroCota(Integer.valueOf(1234));
		nota2.setNumeroNfe(Long.valueOf(778899));
		nota2.setSerie("4356");
		nota2.setStatus("APROVADO");
		nota2.setTipoNotaFiscal("Complementar");
		nota2.setValorNota(new BigDecimal(999));
		nota2.setValorReal(new BigDecimal(999));
		
		listaNotasPendentes.add(nota1);
		listaNotasPendentes.add(nota2);
		
		TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>>();*/
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNotasPendentes));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(tamanhoListaNotasPendentes);
		
		return tableModel;
	}
	
	
	@Path("/pesquisarItensPorNota")
	public void pesquisarItensPorNota(long idControleConferencia,String sortorder, String sortname, int page, int rp){
		
		Integer total = this.entradaNFETerceirosService.buscarTodasItensPorNota(idControleConferencia);
		
		if (total <= 0) {		

			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
			
		}
		List<ItemNotaFiscalPendenteDTO> listItemNota = this.entradaNFETerceirosService.buscarItensPorNota  (
						idControleConferencia, sortname,
						Ordenacao.valueOf(sortorder.toUpperCase()), page
								* rp - rp, rp);
		
		result.use(FlexiGridJson.class).from(listItemNota).page(page).total(total).serialize();
		
	}

	
	@Post
	@Path("/cadastrarNota")
	@Rules(Permissao.ROLE_NFE_ENTRADA_NFE_TERCEIROS_ALTERACAO)	
	public void cadastrarNota(NotaFiscalEntradaCota nota, Integer numeroCota, Long idControleConferenciaEncalheCota){
		this.notaFiscalEntradaService.inserirNotaFiscal(nota, numeroCota, idControleConferenciaEncalheCota);

		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Cadastro efetuado com sucesso.");
		
		this.result.use(Results.json()).from(validacao, "result").recursive().serialize();
		
	}
	
	private void validarEntrada(FiltroEntradaNFETerceiros filtro) {
				
		if (filtro.getCota() != null && filtro.getCota().getNumeroCota() != null) {
			Cota cota = notaFiscalEntradaService.obterPorNumerDaCota(filtro.getCota().getNumeroCota());
			if (cota != null) {
				filtro.setCota(cota);
			} else {
				throw new ValidacaoException(TipoMensagem.WARNING, "Número da cota inválido!");			
			}
		} else {
			filtro.setCota(null);
		}
		
		if (filtro.getFornecedor() != null
				&& filtro.getFornecedor().getId() != null
				&& filtro.getFornecedor().getId() > 0) {
			Fornecedor fornecedor = notaFiscalEntradaService.obterFornecedorPorID(filtro.getFornecedor().getId());
			if (fornecedor != null) {
				filtro.setFornecedor(fornecedor);
			} else {
				throw new ValidacaoException(TipoMensagem.WARNING, "Fornecedor não encotrado!");			
			}
		} else {
			filtro.setFornecedor(null);
		}
		
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
			
			List<ConsultaEntradaNFETerceirosPendentesDTO> listaNotasPendentes = this.entradaNFETerceirosService.buscarNFNotasPendentes(filtro, false);
			// List<ConsultaEntradaNFETerceirosPendentesDTO> listaNotasPendentes = new ArrayList<ConsultaEntradaNFETerceirosPendentesDTO>();
			/*if(listaNotasRecebidas.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}*/
			
			FileExporter.to("notasPendentes", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, listaNotasPendentes, ConsultaEntradaNFETerceirosPendentesDTO.class, this.httpResponse);
			
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

}
