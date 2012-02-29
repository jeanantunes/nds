package br.com.abril.nds.controllers.estoque;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ResultadoConsultaDetallheNFVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.DetalheNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO.ColunaOrdenacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
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
	private FornecedorService fornecedorService;

	@Autowired
	private TipoNotaFiscalService tipoNotaFiscalService;

	@Autowired
	private NotaFiscalService notaFiscalService;
	
	@Path("/")
	public void index() {
		
		preencherCombos();
		
		throw new ValidacaoException(TipoMensagem.ERROR, "fucking test");
	}

	public void pesquisarNotas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal, int isNotaRecebida,
							   String sortorder, String sortname, int page, int rp) {

		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

		filtroConsultaNotaFiscal.setPaginacao(paginacao);

		filtroConsultaNotaFiscal.setColunaOrdenacao(Util.getEnumByStringValue(ColunaOrdenacao.values(), sortname));

		if (isNotaRecebida >= 0) {
			filtroConsultaNotaFiscal.setNotaRecebida(isNotaRecebida == NOTA_RECEBIDA);
		}

		try {

			List<NotaFiscal> listaNotasFiscais =
				notaFiscalService.obterNotasFiscaisCadastradas(filtroConsultaNotaFiscal);

			Integer quantidadeRegistros = this.notaFiscalService.obterQuantidadeNotasFicaisCadastradas(filtroConsultaNotaFiscal);
	
			TableModel<CellModel> tableModel = getTableModelNotasFiscais(listaNotasFiscais);
			tableModel.setTotal(quantidadeRegistros);
			tableModel.setPage(page);

			if (listaNotasFiscais == null || listaNotasFiscais.isEmpty()) {

//				result.use(Results.json()).from(
//					new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado."),
//					Constantes.PARAM_MSGS
//				).recursive().serialize();
				
				throw new RuntimeException("fucking test");

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

			result.use(Results.json()).from(
					new String[]{Constantes.TIPO_MSG_WARNING, "Nenhum registro encontrado."}, 
								 Constantes.PARAM_MSGS).serialize();

			return;
		}
		
		TableModel<CellModel> tableModelDetalhesNota = getTableModelDetalhesNotaFiscal(detalheNotaFiscal.getItensDetalhados());
		tableModelDetalhesNota.setPage(1);
		tableModelDetalhesNota.setTotal(detalheNotaFiscal.getItensDetalhados().size());

		DecimalFormat decimalFormat = new DecimalFormat("0.##");

		ResultadoConsultaDetallheNFVO resultadoConsultaDetallheNF = 
			new ResultadoConsultaDetallheNFVO(
				tableModelDetalhesNota, decimalFormat.format(detalheNotaFiscal.getTotalExemplares()), 
					decimalFormat.format(detalheNotaFiscal.getValorTotalSumarizado()));

		this.result.use(Results.json()).withoutRoot().from(resultadoConsultaDetallheNF).recursive().serialize();
	}

	private void preencherCombos() {

		List<Fornecedor> fornecedores = fornecedorService.obterFornecedoresAtivos();

		List<TipoNotaFiscal> tiposNotaFiscal = tipoNotaFiscalService.obterTiposNotasFiscais();

		result.include("fornecedores", fornecedores);
		result.include("tiposNotaFiscal", tiposNotaFiscal);		
	}
	
	private TableModel<CellModel> getTableModelNotasFiscais(List<NotaFiscal> listaNotasFiscais) {

		List<CellModel> listaCellModels = new LinkedList<CellModel>();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		for (NotaFiscal notaFiscal : listaNotasFiscais) {
			
			CellModel cellModel = 
					new CellModel(notaFiscal.getId().intValue(), notaFiscal.getNumero(), 
								  simpleDateFormat.format(notaFiscal.getDataEmissao()), 
								  simpleDateFormat.format(notaFiscal.getDataExpedicao()), 
								  notaFiscal.getTipoNotaFiscal().getDescricao(), 
								  notaFiscal.getEmitente().getRazaoSocial(), 
								  StatusNotaFiscal.RECEBIDA.equals(notaFiscal.getStatusNotaFiscal()) ? "*" : " ", 
								  " ", String.valueOf(notaFiscal.getId()));

			listaCellModels.add(cellModel);
		}
		
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		tableModel.setRows(listaCellModels);

		return tableModel;
	}
	
	private TableModel<CellModel> getTableModelDetalhesNotaFiscal(List<DetalheItemNotaFiscalDTO> listaDetalhesNotaFiscal) {
		
		List<CellModel> listaCellModels = new LinkedList<CellModel>();

		for (DetalheItemNotaFiscalDTO detalheNotaFiscalVO : listaDetalhesNotaFiscal) {
			
			String sobrasFaltas = detalheNotaFiscalVO.getSobrasFaltas() != null ? 
							((TipoDiferenca.FALTA_DE.equals(detalheNotaFiscalVO.getTipoDiferenca()) || 
										  TipoDiferenca.FALTA_EM.equals(detalheNotaFiscalVO.getTipoDiferenca())) ?
												  "-" + detalheNotaFiscalVO.getSobrasFaltas() :
													  "" + detalheNotaFiscalVO.getSobrasFaltas()) : "-";

		    DecimalFormat decimalFormat = new DecimalFormat("0.##");
												  
			CellModel cellModel = 
					new CellModel(
							detalheNotaFiscalVO.getCodigoItem().intValue(),
							String.valueOf(detalheNotaFiscalVO.getCodigoProduto() == null ? "-" : detalheNotaFiscalVO.getCodigoProduto()),
							String.valueOf(detalheNotaFiscalVO.getNomeProduto() == null   ? "-" : detalheNotaFiscalVO.getNomeProduto()),
							String.valueOf(detalheNotaFiscalVO.getNumeroEdicao() == null  ? "-" : detalheNotaFiscalVO.getNumeroEdicao()),
							String.valueOf(detalheNotaFiscalVO.getPrecoVenda() == null    ? "-" : decimalFormat.format(detalheNotaFiscalVO.getPrecoVenda())),
							String.valueOf(detalheNotaFiscalVO.getQuantidadeExemplares() == null ? "-" : detalheNotaFiscalVO.getQuantidadeExemplares()),
							sobrasFaltas, 
							detalheNotaFiscalVO.getValorTotal() == null ? "-" : decimalFormat.format(detalheNotaFiscalVO.getValorTotal()));

			listaCellModels.add(cellModel);
		}
		
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		tableModel.setRows(listaCellModels);

		return tableModel;
	}
	
}
