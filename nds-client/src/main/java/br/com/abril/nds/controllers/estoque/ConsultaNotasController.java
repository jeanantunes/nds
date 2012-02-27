package br.com.abril.nds.controllers.estoque;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.estoque.DetalheNotaFiscalVO;
import br.com.abril.nds.vo.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.vo.filtro.FiltroConsultaNotaFiscalDTO.ColunaOrdenacao;
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
	
	@Path("/consultaNotas")
	public void index() {
		preencherCombos();
	}

	@Path("/consultaNotas/pesquisarNotas")
	public void pesquisarNotas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal, int isNotaRecebida,
							   String sortorder, String sortname, int page, int rp) {

		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

		filtroConsultaNotaFiscal.setPaginacao(paginacao);

		filtroConsultaNotaFiscal.setColunaOrdenacao(Util.getEnumByStringValue(ColunaOrdenacao.values(), sortname));

		if (isNotaRecebida >= 0) {
			filtroConsultaNotaFiscal.setNotaRecebida(isNotaRecebida == NOTA_RECEBIDA);
		}

		List<NotaFiscal> listaNotasFiscais =
			notaFiscalService.obterNotasFiscaisCadastradas(filtroConsultaNotaFiscal);

		Integer quantidadeRegistros = this.notaFiscalService.obterQuantidadeNotasFicaisCadastradas(filtroConsultaNotaFiscal);

		TableModel<CellModel> tableModel = getTableModelNotasFiscais(listaNotasFiscais);
		tableModel.setTotal(quantidadeRegistros);
		tableModel.setPage(page);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	public void pesquisarDetalhesNotaFiscal(Long idNota) {

		List<DetalheNotaFiscalVO> listaDetalhesNotaFiscal = getDetalhesNotaFiscal();
				//this.notaFiscalService.obterDetalhesNotaFical(idNota);
		
		TableModel<CellModel> tableModelDetalhesNota = getTableModelDetalhesNotaFiscal(listaDetalhesNotaFiscal);
		tableModelDetalhesNota.setPage(1);
		tableModelDetalhesNota.setTotal(listaDetalhesNotaFiscal.size());

		this.result.use(Results.json()).withoutRoot().from(tableModelDetalhesNota).recursive().serialize();
	}

	private void preencherCombos() {

		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores();

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
	
	private TableModel<CellModel> getTableModelDetalhesNotaFiscal(List<DetalheNotaFiscalVO> listaDetalhesNotaFiscal) {
		
		List<CellModel> listaCellModels = new LinkedList<CellModel>();

		for (DetalheNotaFiscalVO detalheNotaFiscalVO : listaDetalhesNotaFiscal) {
			
			String sobrasFaltas = TipoDiferenca.FALTA_DE.equals(detalheNotaFiscalVO.getTipoDiferenca()) || 
								  TipoDiferenca.FALTA_EM.equals(detalheNotaFiscalVO.getTipoDiferenca()) ?
										  "-" + detalheNotaFiscalVO.getSobrasFaltas() :
											  "" + detalheNotaFiscalVO.getSobrasFaltas();

			CellModel cellModel = 
					new CellModel(detalheNotaFiscalVO.getCodigoItem().intValue(), 
								  String.valueOf(detalheNotaFiscalVO.getNomeProduto()),
								  String.valueOf(detalheNotaFiscalVO.getNumeroEdicao()), 
								  String.valueOf(detalheNotaFiscalVO.getQuantidadeExemplares()),
								  sobrasFaltas);

			listaCellModels.add(cellModel);
		}
		
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		tableModel.setRows(listaCellModels);

		return tableModel;
	}
	
	private List<DetalheNotaFiscalVO> getDetalhesNotaFiscal() {
		
		List<DetalheNotaFiscalVO> listaDetalheNotaFiscal = new ArrayList<DetalheNotaFiscalVO>();
		
		DetalheNotaFiscalVO detalheNotaFiscalVO = new DetalheNotaFiscalVO();
		detalheNotaFiscalVO.setCodigoItem(1L);
		detalheNotaFiscalVO.setNomeProduto("VEJA");
		detalheNotaFiscalVO.setNumeroEdicao(5L);
		detalheNotaFiscalVO.setQuantidadeExemplares(new BigDecimal("3"));
		detalheNotaFiscalVO.setSobrasFaltas(new BigDecimal("2"));
		detalheNotaFiscalVO.setTipoDiferenca(TipoDiferenca.FALTA_DE);
		listaDetalheNotaFiscal.add(detalheNotaFiscalVO);
		
		detalheNotaFiscalVO = new DetalheNotaFiscalVO();
		detalheNotaFiscalVO.setCodigoItem(2L);
		detalheNotaFiscalVO.setNomeProduto("VEJA");
		detalheNotaFiscalVO.setNumeroEdicao(5L);
		detalheNotaFiscalVO.setQuantidadeExemplares(new BigDecimal("3"));
		detalheNotaFiscalVO.setSobrasFaltas(new BigDecimal("2"));
		detalheNotaFiscalVO.setTipoDiferenca(TipoDiferenca.FALTA_EM);
		listaDetalheNotaFiscal.add(detalheNotaFiscalVO);
		
		detalheNotaFiscalVO = new DetalheNotaFiscalVO();
		detalheNotaFiscalVO.setCodigoItem(3L);
		detalheNotaFiscalVO.setNomeProduto("VEJA");
		detalheNotaFiscalVO.setNumeroEdicao(5L);
		detalheNotaFiscalVO.setQuantidadeExemplares(new BigDecimal("3"));
		detalheNotaFiscalVO.setSobrasFaltas(new BigDecimal("2"));
		detalheNotaFiscalVO.setTipoDiferenca(TipoDiferenca.SOBRA_DE);
		listaDetalheNotaFiscal.add(detalheNotaFiscalVO);
		
		
		detalheNotaFiscalVO = new DetalheNotaFiscalVO();
		detalheNotaFiscalVO.setCodigoItem(4L);
		detalheNotaFiscalVO.setNomeProduto("VEJA");
		detalheNotaFiscalVO.setNumeroEdicao(5L);
		detalheNotaFiscalVO.setQuantidadeExemplares(new BigDecimal("3"));
		detalheNotaFiscalVO.setSobrasFaltas(new BigDecimal("2"));
		detalheNotaFiscalVO.setTipoDiferenca(TipoDiferenca.SOBRA_EM);
		listaDetalheNotaFiscal.add(detalheNotaFiscalVO);
		
		
		detalheNotaFiscalVO = new DetalheNotaFiscalVO();
		detalheNotaFiscalVO.setCodigoItem(5L);
		detalheNotaFiscalVO.setNomeProduto("VEJA");
		detalheNotaFiscalVO.setNumeroEdicao(5L);
		detalheNotaFiscalVO.setQuantidadeExemplares(new BigDecimal("3"));
		detalheNotaFiscalVO.setSobrasFaltas(new BigDecimal("2"));
		detalheNotaFiscalVO.setTipoDiferenca(TipoDiferenca.FALTA_DE);
		listaDetalheNotaFiscal.add(detalheNotaFiscalVO);
		
		return listaDetalheNotaFiscal;
	}
}
