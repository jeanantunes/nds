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
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros.TipoNota;
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
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EntradaNFETerceirosService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.NaturezaOperacaoService;
import br.com.abril.nds.service.NotaFiscalEntradaService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.PessoaJuridicaService;
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
    private NotaFiscalService notaFiscalService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;
	
	@Autowired
	private NaturezaOperacaoService tipoNotaFiscalService;
	
	@Autowired
	private CotaService cotaService;
	
	@Path("/")
	public void index(){
		this.carregarComboStatusNota();
		this.carregarFornecedores();
		this.carregarTipoNota();
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void carregarTipoNota() {
        
        List<ItemDTO<String, String>> tiposNotas = new ArrayList<ItemDTO<String, String>>();
        
        tiposNotas.add(new ItemDTO(TipoNota.TODAS.name(), TipoNota.TODAS.getDescricao()));
        tiposNotas.add(new ItemDTO(TipoNota.ENTRADA.name(), TipoNota.ENTRADA.getDescricao()));
        tiposNotas.add(new ItemDTO(TipoNota.SAIDA.name(), TipoNota.SAIDA.getDescricao()));

        result.include("tiposNotas", tiposNotas);
        
    }
	
	@Post
	@Path("/pesquisarNotasRecebidas")
	public void pesquisarNotasRecebidas(final FiltroEntradaNFETerceiros filtro, final String sortorder, final String sortname, final int page, int rp){

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		final TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>> tableModel = consultarNotasRecebidas(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>> consultarNotasRecebidas(final FiltroEntradaNFETerceiros filtro) {
		
		final List<ConsultaEntradaNFETerceirosRecebidasDTO> notasRecebidas = this.entradaNFETerceirosService.consultarNotasRecebidas(filtro, true);

		final Integer qtdeNotasRecebidas = this.entradaNFETerceirosService.qtdeNotasRecebidas(filtro);
		
		TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosRecebidasDTO>>();
		
		if(notasRecebidas == null || notasRecebidas.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(notasRecebidas));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(qtdeNotasRecebidas);
		
		return tableModel;
	}
	
	@Post
	@Path("/pesquisarNotasPendentesRecebimento")
	public void pesquisarNotasPendentesRecebimento(final FiltroEntradaNFETerceiros filtro, final String sortorder, final String sortname, final int page, final int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		final TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>> tableModel = consultaNotasPendentesRecebimento(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>> consultaNotasPendentesRecebimento(final FiltroEntradaNFETerceiros filtro) {

		List<ConsultaEntradaNFETerceirosPendentesDTO> listaNotasPendentes = this.entradaNFETerceirosService.consultaNotasPendentesRecebimento(filtro, true);

		Integer quantidade = this.entradaNFETerceirosService.qtdeNotasPendentesRecebimento(filtro);
		
		TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>>();
		
		if(listaNotasPendentes.size() == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNotasPendentes));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(quantidade);
		
		return tableModel;
	}
	
	@Post
    @Path("/pesquisarNotasPedenteEmissao")
    public void pesquisarNotasPedenteEmissao(final FiltroEntradaNFETerceiros filtro, final String sortorder, final String sortname, final int page, final int rp){
        
        filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
        
        this.validarEntrada(filtro);
        
        this.tratarFiltro(filtro);
        
        final TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>> tableModel = consultaNotasPendentesEmissao(filtro);
        
        result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
    }
	
	private TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>> consultaNotasPendentesEmissao(final FiltroEntradaNFETerceiros filtro) {

        List<ConsultaEntradaNFETerceirosPendentesDTO> listaNotasPendentesEmissao = this.entradaNFETerceirosService.consultaNotasPendentesEmissao(filtro, true);

        Integer tamanhoListaNotasPendentes = this.entradaNFETerceirosService.qtdeNotasPendentesEmissao(filtro);
        
        TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEntradaNFETerceirosPendentesDTO>>();
        
        if(listaNotasPendentesEmissao.size() == 0){
            throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
        }
        
        tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNotasPendentesEmissao));
        
        tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
        
        tableModel.setTotal(tamanhoListaNotasPendentes);
        
        return tableModel;
    }
	
	@Path("/pesquisarItensPorNota")
	public void pesquisarItensPorNota(final long idControleConferencia, final String sortorder, final String sortname, final int page, final int rp){
		
		final Integer total = this.notaFiscalService.qtdeNota(idControleConferencia);
		
		if (total <= 0) {		
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}
		
		final List<ItemNotaFiscalPendenteDTO> listItemNota = this.notaFiscalService.buscarItensPorNota(idControleConferencia, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page * rp - rp, rp);
		
		result.use(FlexiGridJson.class).from(listItemNota).page(page).total(total).serialize();
		
	}

	
	@Post
	@Path("/cadastrarNota")
	@Rules(Permissao.ROLE_NFE_ENTRADA_NFE_TERCEIROS_ALTERACAO)	
	public void cadastrarNota(final NotaFiscalEntradaCota nota, final Integer numeroCota, final Long idControleConferenciaEncalheCota){
	    
		this.notaFiscalEntradaService.inserirNotaFiscal(nota, numeroCota, idControleConferenciaEncalheCota);

		final ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Cadastro efetuado com sucesso.");
		
		result.use(Results.json()).from(validacao, "result").recursive().serialize();
		
	}
	
	private void validarEntrada(final FiltroEntradaNFETerceiros filtro) {
				
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
		
		if (filtro.getFornecedor() != null  && filtro.getFornecedor().getId() != null && filtro.getFornecedor().getId() > 0) {
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
	
	private void tratarFiltro(final FiltroEntradaNFETerceiros filtroAtual) {

		FiltroEntradaNFETerceiros filtroSession = (FiltroEntradaNFETerceiros) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSULTA);
		
		if (filtroSession != null && filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_CONSULTA, filtroAtual);
	}
	
	@Get
	public void exportar(final FileType fileType) throws IOException {
		
		FiltroEntradaNFETerceiros filtro = (FiltroEntradaNFETerceiros) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSULTA);
		
		if(filtro.getStatusNotaFiscalEntrada().name().equals("RECEBIDA")){
			//List<ConsultaEntradaNFETerceirosRecebidasDTO> listaNotasRecebidas = this.entradaNFETerceirosService.buscarNFNotasRecebidas(filtro, false);
			List<ConsultaEntradaNFETerceirosRecebidasDTO> listaNotasRecebidas = new ArrayList<ConsultaEntradaNFETerceirosRecebidasDTO>();
			/*if(listaNotasRecebidas.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}*/
			
			FileExporter.to("consulta_notas_recebidas", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, listaNotasRecebidas, ConsultaEntradaNFETerceirosRecebidasDTO.class, this.httpResponse);			
		}else{
			
			List<ConsultaEntradaNFETerceirosPendentesDTO> listaNotasPendentes = this.entradaNFETerceirosService.consultaNotasPendentesRecebimento(filtro, false);
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
	public void buscarCotaPorNumero(final Integer numeroCota) {
		
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

		result.use(Results.json()).from(nomeCota, "result").recursive().serialize();
	}

}
