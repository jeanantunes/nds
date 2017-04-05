package br.com.abril.nds.controllers.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ConsultaEncalheVO;
import br.com.abril.nds.client.vo.ResultadoConsultaEncalheVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.ConsultaEncalheService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * 
 * Classe responsável por controlar as ações da pagina de Consulta de Encalhe.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path(value="/devolucao/consultaBaseEncalhe")
@Rules(Permissao.ROLE_RECOLHIMENTO_CONSULTA_BASE_ENCALHE_COTA)
public class ConsultaBaseEncalheController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
    private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private ConsultaEncalheService consultaEncalheService;
	
	private static final String SUFIXO_DIA = "º Dia";
	
	@Path("/")
	public void index(){
		
		this.carregarComboFornecedores();
		
		this.iniciarComboBox();

        this.iniciarComboRota();

        this.iniciarComboRoteiro();
        
        boolean isRecebe = distribuidorService.verificarParametroDistribuidorEmissaoDocumentosImpressaoCheck(null, TipoParametrosDistribuidorEmissaoDocumento.SLIP);
		result.include("isDistribGeraSlip", isRecebe);
		
		result.include("listaBoxes", carregarBoxes(boxService.buscarTodos(TipoBox.ENCALHE)));
		
		result.include("data", DateUtil.formatarDataPTBR(distribuidorService.obterDataOperacaoDistribuidor()));
		
	}
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private void carregarComboFornecedores() {
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresAtivos();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		result.include("listaFornecedores",listaFornecedoresCombo );
	}
	
	 /**
     * Inicia o combo Box
     */
    private void iniciarComboBox() {

        result.include("listaBox", this.roteirizacaoService.getComboTodosBoxes());
    }
	
	@Post
	@Path("/pesquisarDetalheBaseReparte")
	public void pesquisarDetalheBaseReparte(FiltroConsultaEncalheDTO filtro, String sortorder, String sortname, int page, int rp) {
		
		this.efetuarPesquisaBaseReparte(filtro);
	}
	
	/**
     * Inicia o combo Rota
     */
    private void iniciarComboRota() {

        result.include("rotas", this.roteirizacaoService.getComboTodosRotas());
    }
	
    /**
     * Inicia o combo Roteiro
     */
    private void iniciarComboRoteiro() {
    	
        result.include("roteiros", this.roteirizacaoService.getComboTodosRoteiros());
    }
    
    /**
	 * Carrega a lista de Boxes
	 * @return 
	 */
	private List<ItemDTO<Integer, String>> carregarBoxes(List<Box> listaBoxes){
		
		this.sortByCodigo(listaBoxes);
		
		List<ItemDTO<Integer, String>> boxes = new ArrayList<ItemDTO<Integer,String>>();
				
		for(Box box : listaBoxes){
			
			boxes.add(new ItemDTO<Integer, String>(box.getId().intValue(),box.getId().intValue() + " - " + box.getNome()));
		}
		
		return boxes;			
	}
	
	private void sortByCodigo(List<Box> listaBoxes) {
		Collections.sort(listaBoxes, new Comparator<Box>() {
			@Override
			public int compare(Box box1, Box box2) {
				if(box1.getCodigo()==null)
					return -1;
				return box1.getCodigo().compareTo(box2.getCodigo());
			}
		});
	}
    
	private void efetuarPesquisaBaseReparte(FiltroConsultaEncalheDTO filtro) {
		
		InfoConsultaEncalheDTO infoConsultaEncalhe = consultaEncalheService.pesquisarBaseReparte(filtro);
		
		List<ConsultaEncalheDTO> listaResultado = infoConsultaEncalhe.getListaConsultaEncalhe();
		
		ResultadoConsultaEncalheVO resultadoPesquisa = new ResultadoConsultaEncalheVO();
		
		Integer quantidadeRegistros = infoConsultaEncalhe.getQtdeConsultaEncalhe();
		
		List<ConsultaEncalheVO> listaResultadosVO = getListaConsultaEncalheVO(listaResultado, filtro);
		
		TableModel<CellModelKeyValue<ConsultaEncalheVO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEncalheVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaResultadosVO));
		
		tableModel.setTotal( (quantidadeRegistros!= null) ? quantidadeRegistros : 0);
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		resultadoPesquisa.setTableModel(tableModel);
		
		result.use(Results.json()).withoutRoot().from(resultadoPesquisa).recursive().serialize();
		
	}
	
	private List<ConsultaEncalheVO> getListaConsultaEncalheVO( List<ConsultaEncalheDTO> listaConsultaEncalheDTO, FiltroConsultaEncalheDTO filtro ) {
		
		List<ConsultaEncalheVO> listaResultadosVO = new ArrayList<ConsultaEncalheVO>();
		
		if (listaConsultaEncalheDTO == null || listaConsultaEncalheDTO.isEmpty()) {
			
			return listaResultadosVO;
		}
		
		ConsultaEncalheVO consultaEncalheVO = null;
		
		String idProdutoEdicao 		= null;
		String codigoProduto 		= null;
		String nomeProduto 			= null;
		String numeroEdicao 		= null;
		String precoVenda 			= null;
		String precoComDesconto 	= null;
		String reparte 				= null;
		String encalhe 				= null;
		String venda 				= null;
		String idFornecedor			= null;
		String idCota				= null;
		String fornecedor			= null;
		String valorRepartePrecoCapa = null;
		String valorEncalhePrecoCapa = null;
		String valorVenda           = null;
		String valorRepartePrecoDesc = null;
		String valorEncalhePrecoDes = null;
		String valorVendaDesc		= null;
		String valorComDesconto		= null;
		String recolhimento 		= null;
		String dataRecolhimento		= null;
		String dataMovimento		= null;
		String valor  		        = null;
		Integer totalQtdeVenda      = 0;
		
		for(ConsultaEncalheDTO consultaEncalheDTO : listaConsultaEncalheDTO) {
			
			idProdutoEdicao		= (consultaEncalheDTO.getIdProdutoEdicao() != null) ? consultaEncalheDTO.getIdProdutoEdicao().toString() : "";
			codigoProduto 		= (consultaEncalheDTO.getCodigoProduto() != null) ? consultaEncalheDTO.getCodigoProduto() : "";
			nomeProduto 		= (consultaEncalheDTO.getNomeProduto() != null) ? consultaEncalheDTO.getNomeProduto() : "";
			numeroEdicao 		= (consultaEncalheDTO.getNumeroEdicao() != null) ? consultaEncalheDTO.getNumeroEdicao().toString() : "";
			precoVenda 			= CurrencyUtil.formatarValor(consultaEncalheDTO.getPrecoVenda());
			precoComDesconto 	= CurrencyUtil.formatarValorQuatroCasas(consultaEncalheDTO.getPrecoComDesconto());
			reparte 			= Util.getValorQtdeIntegerFormatado(consultaEncalheDTO.getReparte() == null ? 0 : consultaEncalheDTO.getReparte().intValue());
			encalhe 			= Util.getValorQtdeIntegerFormatado(consultaEncalheDTO.getEncalhe() == null ? 0 : consultaEncalheDTO.getEncalhe().intValue());
			valor               = CurrencyUtil.formatarValor(consultaEncalheDTO.getValor());
			
			if(consultaEncalheDTO.getEncalhe() != null && consultaEncalheDTO.getReparte() != null) {				
				venda 			    = Util.getValorQtdeIntegerFormatado(consultaEncalheDTO.getReparte().intValue() - consultaEncalheDTO.getEncalhe().intValue());
				BigDecimal calculoVenda = new BigDecimal(consultaEncalheDTO.getReparte().intValue() - consultaEncalheDTO.getEncalhe().intValue());
				valorVenda          = CurrencyUtil.formatarValor(calculoVenda.multiply(consultaEncalheDTO.getPrecoVenda()));				
				valorVendaDesc 		= CurrencyUtil.formatarValor(calculoVenda.multiply(consultaEncalheDTO.getPrecoComDesconto()));
				totalQtdeVenda = totalQtdeVenda + (consultaEncalheDTO.getReparte().intValue() - consultaEncalheDTO.getEncalhe().intValue());
			}
			
			if(consultaEncalheDTO.getEncalhe() != null && consultaEncalheDTO.getReparte() != null) {		
				valorRepartePrecoDesc = CurrencyUtil.formatarValor(consultaEncalheDTO.getReparte().multiply(consultaEncalheDTO.getPrecoComDesconto()));
				valorEncalhePrecoDes  = CurrencyUtil.formatarValor(consultaEncalheDTO.getEncalhe().multiply(consultaEncalheDTO.getPrecoComDesconto()));
			}
			
			idFornecedor		= (consultaEncalheDTO.getIdFornecedor()!=null) ? consultaEncalheDTO.getIdFornecedor().toString() : "";
			idCota				= (consultaEncalheDTO.getIdCota()!=null) ? consultaEncalheDTO.getIdCota().toString() : "";
			fornecedor			= (consultaEncalheDTO.getFornecedor()!=null) ? consultaEncalheDTO.getFornecedor() : "";
			valorRepartePrecoCapa = CurrencyUtil.formatarValor(consultaEncalheDTO.getPrecoVenda().multiply(consultaEncalheDTO.getReparte()));
			valorEncalhePrecoCapa = CurrencyUtil.formatarValor(consultaEncalheDTO.getPrecoVenda().multiply(consultaEncalheDTO.getEncalhe()));
			valorComDesconto	= CurrencyUtil.formatarValorQuatroCasas(consultaEncalheDTO.getValorComDesconto());
			dataRecolhimento	= (consultaEncalheDTO.getDataDoRecolhimentoDistribuidor() != null) ? DateUtil.formatarDataPTBR(consultaEncalheDTO.getDataDoRecolhimentoDistribuidor()) : "" ;
			dataMovimento		= (consultaEncalheDTO.getDataMovimento() != null) ? DateUtil.formatarDataPTBR(consultaEncalheDTO.getDataMovimento()) : "" ;
			
			boolean diaUnico = DateUtils.isSameDay(filtro.getDataRecolhimentoInicial(), filtro.getDataRecolhimentoFinal());
			
			
			if( !diaUnico ) {
				
				if(consultaEncalheDTO.getDataDoRecolhimentoDistribuidor()!=null) {
					
					recolhimento = DateUtil.formatarDataPTBR(consultaEncalheDTO.getDataDoRecolhimentoDistribuidor());
					
				} else {
					
					recolhimento = "";
					
				}
			
			} else {
				
				if(consultaEncalheDTO.getRecolhimento() != null) {
					
					recolhimento = consultaEncalheDTO.getRecolhimento().toString() + SUFIXO_DIA;
					
				} else {
					
					recolhimento = "";
				}
			}
			
			consultaEncalheVO = new ConsultaEncalheVO();
			
			consultaEncalheVO.setIdProdutoEdicao(idProdutoEdicao);
			consultaEncalheVO.setCodigoProduto(codigoProduto);
			consultaEncalheVO.setNomeProduto(nomeProduto);
			consultaEncalheVO.setNumeroEdicao(numeroEdicao);
			consultaEncalheVO.setPrecoVenda(precoVenda);
			consultaEncalheVO.setPrecoComDesconto(precoComDesconto);
			consultaEncalheVO.setReparte(reparte);
			consultaEncalheVO.setEncalhe(encalhe);
			consultaEncalheVO.setVenda(venda);
			consultaEncalheVO.setIdCota(idCota);
			consultaEncalheVO.setIdFornecedor(idFornecedor);
			consultaEncalheVO.setFornecedor(fornecedor);
			consultaEncalheVO.setValorRepartePrecoCapa(valorRepartePrecoCapa);
			consultaEncalheVO.setValorEncalhePrecoCapa(valorEncalhePrecoCapa);
			consultaEncalheVO.setValorVenda(valorVenda);
			consultaEncalheVO.setValorVendaDesc(valorVendaDesc);
			consultaEncalheVO.setValorComDesconto(valorComDesconto);
			consultaEncalheVO.setValor(valor);
			consultaEncalheVO.setValorReparteDesconto(valorRepartePrecoDesc);
			consultaEncalheVO.setValorEncalheDesconto(valorEncalhePrecoDes);
			consultaEncalheVO.setRecolhimento(recolhimento);
			consultaEncalheVO.setDataMovimento(dataMovimento);
			consultaEncalheVO.setDataRecolhimento(dataRecolhimento);
			consultaEncalheVO.setIndPossuiObservacaoConferenciaEncalhe(consultaEncalheDTO.getIndPossuiObservacaoConferenciaEncalhe());
			consultaEncalheVO.setTotalQtdeVenda(totalQtdeVenda);
			
			listaResultadosVO.add(consultaEncalheVO);
		}
		
		return listaResultadosVO;
	}
}