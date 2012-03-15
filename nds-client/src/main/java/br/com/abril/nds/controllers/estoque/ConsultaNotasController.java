package br.com.abril.nds.controllers.estoque;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ResultadoConsultaDetallheNFVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.DetalheNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO.ColunaOrdenacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.NotaFiscalEntradaService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela tela de consulta de Notas Fiscais. 
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/estoque/consultaNotas")
public class ConsultaNotasController {
	
	/**
	 * Indicador para nota recebida.
	 */
	private static int NOTA_RECEBIDA = 1;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private TipoNotaFiscalService tipoNotaFiscalService;

	@Autowired
	private NotaFiscalEntradaService notaFiscalService;

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroConsultaNotaFiscal";
	
	@Path("/")
	public void index() {
		
		preencherCombos();
		
		inserirDataAtual();
	}
	
	public void pesquisarNotas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal, int isNotaRecebida,
							   String dataInicial, String dataFinal, String sortorder, String sortname, int page, int rp) {

		prepararFiltro(filtroConsultaNotaFiscal, isNotaRecebida, dataInicial, dataFinal, sortorder, sortname, page, rp);

		try {

			List<NotaFiscalEntradaFornecedor> listaNotasFiscais =
				notaFiscalService.obterNotasFiscaisCadastradas(filtroConsultaNotaFiscal);

			Integer quantidadeRegistros = this.notaFiscalService.obterQuantidadeNotasFicaisCadastradas(filtroConsultaNotaFiscal);
	
			TableModel<CellModel> tableModel = getTableModelNotasFiscais(listaNotasFiscais);
			tableModel.setTotal(quantidadeRegistros);
			tableModel.setPage(filtroConsultaNotaFiscal.getPaginacao().getPaginaAtual());

			if (listaNotasFiscais == null || listaNotasFiscais.isEmpty()) {

				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");

			} else {

				result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
			}

		} catch (IllegalArgumentException e) {

			throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage());			
		}
	}

	public void pesquisarDetalhesNotaFiscal(Long idNota) {

		DetalheNotaFiscalDTO detalheNotaFiscal = this.notaFiscalService.obterDetalhesNotaFical(idNota);

		if (detalheNotaFiscal == null || detalheNotaFiscal.getItensDetalhados() == null
									  || detalheNotaFiscal.getItensDetalhados().isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		TableModel<CellModel> tableModelDetalhesNota = getTableModelDetalhesNotaFiscal(detalheNotaFiscal.getItensDetalhados());
		tableModelDetalhesNota.setPage(1);
		tableModelDetalhesNota.setTotal(detalheNotaFiscal.getItensDetalhados().size());

		DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

		ResultadoConsultaDetallheNFVO resultadoConsultaDetallheNF = 
			new ResultadoConsultaDetallheNFVO(
				tableModelDetalhesNota, String.valueOf(detalheNotaFiscal.getTotalExemplares().intValue()), 
					decimalFormat.format(detalheNotaFiscal.getValorTotalSumarizado().intValue()));

		this.result.use(Results.json()).withoutRoot().from(resultadoConsultaDetallheNF).recursive().serialize();
	}

	private void preencherCombos() {

		List<Fornecedor> fornecedores = fornecedorService.obterFornecedoresAtivos();

		List<TipoNotaFiscal> tiposNotaFiscal = tipoNotaFiscalService.obterTiposNotasFiscais();

		result.include("fornecedores", fornecedores);
		result.include("tiposNotaFiscal", tiposNotaFiscal);		
	}

	private void inserirDataAtual() {
		
		result.include("dataAtual", DateUtil.formatarData(new Date(), "dd/MM/yyyy"));
	}

	private TableModel<CellModel> getTableModelNotasFiscais(List<NotaFiscalEntradaFornecedor> listaNotasFiscais) {

		List<CellModel> listaCellModels = new LinkedList<CellModel>();

		for (NotaFiscalEntradaFornecedor notaFiscal : listaNotasFiscais) {

			CellModel cellModel = 
					new CellModel(
							notaFiscal.getId().intValue(), 
							itemExibicaoToString(notaFiscal.getNumero()),
							itemExibicaoToString(DateUtil.formatarDataPTBR(notaFiscal.getDataEmissao())), 
							itemExibicaoToString(DateUtil.formatarDataPTBR(notaFiscal.getDataExpedicao())), 
							itemExibicaoToString(notaFiscal.getTipoNotaFiscal().getDescricao()), 
							itemExibicaoToString(notaFiscal.getFornecedor().getJuridica().getRazaoSocial()),
							StatusNotaFiscal.RECEBIDA.equals(notaFiscal.getStatusNotaFiscal()) ? "*" : " ", 
							" ", 
							itemExibicaoToString(notaFiscal.getId()));

			listaCellModels.add(cellModel);
		}
		
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		tableModel.setRows(listaCellModels);

		return tableModel;
	}
	
	private TableModel<CellModel> getTableModelDetalhesNotaFiscal(List<DetalheItemNotaFiscalDTO> listaDetalhesNotaFiscal) {
		
		List<CellModel> listaCellModels = new LinkedList<CellModel>();

		for (DetalheItemNotaFiscalDTO detalheNotaFiscalVO : listaDetalhesNotaFiscal) {

			boolean isFalta = 
					TipoDiferenca.FALTA_DE.equals(detalheNotaFiscalVO.getTipoDiferenca()) || 
					  TipoDiferenca.FALTA_EM.equals(detalheNotaFiscalVO.getTipoDiferenca());

			String sobrasFaltas = isFalta ? "-" : "";
			sobrasFaltas += itemExibicaoToString(detalheNotaFiscalVO.getSobrasFaltas());

		    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

			CellModel cellModel = 
					new CellModel(
							detalheNotaFiscalVO.getCodigoItem().intValue(),
							itemExibicaoToString(detalheNotaFiscalVO.getCodigoProduto()),
							itemExibicaoToString(detalheNotaFiscalVO.getNomeProduto()),
							itemExibicaoToString(detalheNotaFiscalVO.getNumeroEdicao()),
							itemExibicaoToString(decimalFormat.format(detalheNotaFiscalVO.getPrecoVenda())),
							itemExibicaoToString(detalheNotaFiscalVO.getQuantidadeExemplares().intValue()),
							sobrasFaltas, 
							itemExibicaoToString(decimalFormat.format(detalheNotaFiscalVO.getValorTotal())));

			listaCellModels.add(cellModel);
		}
		
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		tableModel.setRows(listaCellModels);

		return tableModel;
	}
	
	private String itemExibicaoToString(Object itemExibicao) {
		
		return String.valueOf(itemExibicao == null ? "-" : itemExibicao);
	}
	
	private void prepararFiltro(
			FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal, int isNotaRecebida,
			String dataInicial, String dataFinal, String sortorder, String sortname, int page, int rp) {

		if (filtroConsultaNotaFiscal.getIdFornecedor() == -1L) {
			
			filtroConsultaNotaFiscal.setIdFornecedor(null);
		}
		
		if (filtroConsultaNotaFiscal.getIdTipoNotaFiscal() == -1) {
			
			filtroConsultaNotaFiscal.setIdTipoNotaFiscal(null);
		}

		PeriodoVO periodo = obterPeriodoValidado(dataInicial, dataFinal);
		
		filtroConsultaNotaFiscal.setPeriodo(periodo);

		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

		filtroConsultaNotaFiscal.setPaginacao(paginacao);

		String[] sortNames = sortname.split(",");
		
		List<ColunaOrdenacao> listaColunaOrdenacao = new ArrayList<ColunaOrdenacao>();
		
		for (String sort : sortNames) {

			listaColunaOrdenacao.add(Util.getEnumByStringValue(ColunaOrdenacao.values(), sort.trim()));
		}
		
		filtroConsultaNotaFiscal.setListaColunaOrdenacao(listaColunaOrdenacao);

		if (isNotaRecebida > -1) {

			filtroConsultaNotaFiscal.setIsNotaRecebida(NOTA_RECEBIDA == isNotaRecebida);
		}

		FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscalSession =
			(FiltroConsultaNotaFiscalDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroConsultaNotaFiscalSession != null 
				&& !filtroConsultaNotaFiscalSession.equals(filtroConsultaNotaFiscal)) {
			
			page = 1;
		}
		
		paginacao.setPaginaAtual(page);
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroConsultaNotaFiscal);
	}
	
	private PeriodoVO obterPeriodoValidado(String dataInicial, String dataFinal) {
		
		ValidacaoVO validacao = new ValidacaoVO();
		
		validacao.setTipoMensagem(TipoMensagem.ERROR);
		
		List<String> mensagens = new ArrayList<String>();
		
		if (dataInicial == null || dataInicial.isEmpty()) {
		
			mensagens.add("O preenchimento do campo \"Data Inicial\" é obrigatório");
		} 
		
		if (dataFinal == null || dataFinal.isEmpty()) {
			
			mensagens.add("O preenchimento do campo \"Data Final\" é obrigatório");
		} 

		if (!mensagens.isEmpty()) {

			validacao.setListaMensagens(mensagens);
			
			throw new ValidacaoException(validacao);
		}
		
		if (!DateUtil.isValidDate(dataInicial, "dd/MM/yyyy")) {
			
			mensagens.add("Data inicial inválida");
		} 
		
		if (!DateUtil.isValidDate(dataFinal, "dd/MM/yyyy")) {
			
			mensagens.add("Data final inválida");
		}
		
		if (!mensagens.isEmpty()) {

			validacao.setListaMensagens(mensagens);
			
			throw new ValidacaoException(validacao);
		}
		
		return new PeriodoVO(DateUtil.parseData(dataInicial, "dd/MM/yyyy"), DateUtil.parseData(dataFinal, "dd/MM/yyyy"));
	}
}
