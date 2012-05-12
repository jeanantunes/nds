package br.com.abril.nds.controllers.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ConferenciaEncalheVO;
import br.com.abril.nds.client.vo.DebitoCreditoCotaVO;
import br.com.abril.nds.client.vo.ResultadoConsultaConferenciaEncalheVO;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path(value="/devolucao/conferenciaEncalhe")
public class ConferenciaEncalheController {
	
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;
	
	private static final String LISTA_CONFERENCIA_ENCALHE = "listaConferenciaEncalhe";
	
	private static final String VALOR_ENCALHE_JORNALEIRO = "valorEncalheJornaleiro";
	
	private static final String ID_BOX_LOGADO = "idBoxLogado";
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;

	
	@Path("/")
	public void index() {
		carregarComboBoxEncalhe();
	}
	
	private void carregarComboBoxEncalhe() {
		
		List<ItemDTO<Long, Box>> boxes = new ArrayList<ItemDTO<Long,Box>>();
		
		Box b = new Box();
		b.setCodigo("aaa");
		b.setId(1L);
		b.setNome("Brow");
		b.setPostoAvancado(true);
		b.setTipoBox(TipoBox.LANCAMENTO);
		
		boxes.add(new ItemDTO<Long, Box>(b.getId(), b));
		
		b = new Box();
		b.setCodigo("bbb");
		b.setId(2L);
		b.setNome("ieié");
		b.setPostoAvancado(false);
		b.setTipoBox(TipoBox.RECOLHIMENTO);
		
		boxes.add(new ItemDTO<Long, Box>(b.getId(), b));
		
		this.result.include("boxes", boxes);
	}
	
	@Post
	public void salvarIdBoxSessao(Long idBox){
		
		this.session.setAttribute(ID_BOX_LOGADO, idBox);
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	public void pesquisarCota() {
		
		InfoConferenciaEncalheCota infoConfereciaEncalheCota = conferenciaEncalheService.obterInfoConferenciaEncalheCota(null);
		
		ResultadoConsultaConferenciaEncalheVO resultConsultaConferenciaEncalhe = new ResultadoConsultaConferenciaEncalheVO();
		
		String encalhe 					= (infoConfereciaEncalheCota.getEncalhe()!=null) ? 
				infoConfereciaEncalheCota.getEncalhe().toString() : "";
		
		String reparte 					= (infoConfereciaEncalheCota.getReparte()!=null) ? 
				infoConfereciaEncalheCota.getReparte().toString() : "";
		
		String totalDebitoCreditoCota 	= (infoConfereciaEncalheCota.getTotalDebitoCreditoCota()!=null) ? 
				infoConfereciaEncalheCota.getTotalDebitoCreditoCota().toString() : "";
				
		String valorPagar 				= (infoConfereciaEncalheCota.getValorPagar()!=null) ? 
				infoConfereciaEncalheCota.getValorPagar().toString() : "";
		
		String valorVendaDia 			= (infoConfereciaEncalheCota.getValorVendaDia()!=null) ? 
				infoConfereciaEncalheCota.getValorVendaDia().toString() : "";
		
		
		resultConsultaConferenciaEncalhe.setEncalhe(encalhe);
		resultConsultaConferenciaEncalhe.setReparte(reparte);
		resultConsultaConferenciaEncalhe.setTotalDebitoCreditoCota(totalDebitoCreditoCota);
		resultConsultaConferenciaEncalhe.setValorPagar(valorPagar);
		resultConsultaConferenciaEncalhe.setValorVendaDia(valorVendaDia);
		
		List<ConferenciaEncalheVO> listaConferenciaEncalhe = obterListaConferenciaEncalheVOFromDTO(infoConfereciaEncalheCota.getListaConferenciaEncalhe());
		resultConsultaConferenciaEncalhe.setTableModelConferenciaEncalhe(
				obterTableModelConferenciaEncalhe(listaConferenciaEncalhe));
		
		List<DebitoCreditoCotaVO> listaDebitoCreditoCota = obterListaDebitoCreditoCotaVOFromDTO(infoConfereciaEncalheCota.getListaDebitoCreditoCota());
		resultConsultaConferenciaEncalhe.setTableModelDebitoCreditoCota(
				obterTableModelDebitoCreditoCota(listaDebitoCreditoCota));
		
		result.use(Results.json()).withoutRoot().from(resultConsultaConferenciaEncalhe).recursive().serialize();
		
	}
	
	
	/**
	 * Obtém tableModel para grid de conferencia encalhe.
	 * 
	 * @param listaConferenciaEncalhe
	 * 
	 * @return TableModel<CellModelKeyValue<ConferenciaEncalheVO>>
	 */
	private TableModel<CellModelKeyValue<ConferenciaEncalheVO>> obterTableModelConferenciaEncalhe(List<ConferenciaEncalheVO> listaConferenciaEncalhe) {

		TableModel<CellModelKeyValue<ConferenciaEncalheVO>> tableModelConferenciaEncalhe = 
				new TableModel<CellModelKeyValue<ConferenciaEncalheVO>>();
		
		tableModelConferenciaEncalhe.setRows(CellModelKeyValue.toCellModelKeyValue(listaConferenciaEncalhe));
		tableModelConferenciaEncalhe.setTotal((listaConferenciaEncalhe!= null) ? listaConferenciaEncalhe.size() : 0);
		tableModelConferenciaEncalhe.setPage(1);
		
		return tableModelConferenciaEncalhe;
		
	}
	
	/**
	 * Obtém tableModel para grid OutrosValores (Debitos e Creditos da cota).
	 * 
	 * @param listaDebitoCreditoCota
	 * 
	 * @return TableModel<CellModelKeyValue<DebitoCreditoCotaVO>>
	 */
	private TableModel<CellModelKeyValue<DebitoCreditoCotaVO>> obterTableModelDebitoCreditoCota(List<DebitoCreditoCotaVO> listaDebitoCreditoCota) {

		TableModel<CellModelKeyValue<DebitoCreditoCotaVO>> tableModelDebitoCreditoCota = 
				new TableModel<CellModelKeyValue<DebitoCreditoCotaVO>>();
		
		tableModelDebitoCreditoCota.setRows(CellModelKeyValue.toCellModelKeyValue(listaDebitoCreditoCota));
		tableModelDebitoCreditoCota.setTotal((listaDebitoCreditoCota!= null) ? listaDebitoCreditoCota.size() : 0);
		tableModelDebitoCreditoCota.setPage(1);
		
		return tableModelDebitoCreditoCota;
		
	}

	
	
	
	public void carregarEmSessionValorCEJornaleiro() {
		//TODO codificar
	}
	
	@SuppressWarnings("unchecked")
	private List<ConferenciaEncalheVO> getListaConferenciaEncalheFromSession() {
		return (List<ConferenciaEncalheVO>) session.getAttribute(LISTA_CONFERENCIA_ENCALHE);
	}

	private void setListaConferenciaEncalheToSession(List<ConferenciaEncalheVO> listaConferenciaEncalheVO) {
		session.setAttribute(LISTA_CONFERENCIA_ENCALHE, listaConferenciaEncalheVO);
	}

	private BigDecimal getValorEncalheJornaleiroFromSession() {
		return (BigDecimal) session.getAttribute(VALOR_ENCALHE_JORNALEIRO);
	}

	private void setValorEncalheJornaleiroToSession(BigDecimal valorEncalheJornaleiro) {
		session.setAttribute(VALOR_ENCALHE_JORNALEIRO, valorEncalheJornaleiro);
	}

	
	/**
	 * Obtém lista de objetos do tipo ConferenciaEncalheVO a partir de lista de
	 * objetos do tipo ConferenciaEncalheDTO.
	 * 
	 * @param listaConferenciaEncalheDTO
	 * 
	 * @return List - ConferenciaEncalheVO
	 */
	private List<ConferenciaEncalheVO> obterListaConferenciaEncalheVOFromDTO(List<ConferenciaEncalheDTO> listaConferenciaEncalheDTO) {
		
		List<ConferenciaEncalheVO> listaConferenciaEncalheVO = new LinkedList<ConferenciaEncalheVO>();
		
		String qtdExemplar 		= null;
		String codigoDeBarras 	= null;
		String codigoSM 		= null;
		String codigo 			= null;
		String nomeProduto 		= null;
		String numeroEdicao 	= null;
		String precoCapa 		= null;
		String desconto 		= null;
		String valorTotal 		= null;
		String dia 				= null;
		boolean juramentada 	= false;
		
		
		for(ConferenciaEncalheDTO conferenciaDTO : listaConferenciaEncalheDTO) {
			
			qtdExemplar 	= ( conferenciaDTO.getQtdExemplar()     != null ) ? conferenciaDTO.getQtdExemplar().toString()  	: "";
			codigoDeBarras 	= ( conferenciaDTO.getCodigoDeBarras()	!= null ) ? conferenciaDTO.getCodigoDeBarras()				: "";
			codigoSM 		= ( conferenciaDTO.getCodigoSM()        != null ) ? conferenciaDTO.getCodigoSM().toString()  		: "";
			codigo 			= ( conferenciaDTO.getCodigo()          != null ) ? conferenciaDTO.getCodigo()  					: "";
			nomeProduto 	= ( conferenciaDTO.getNomeProduto()     != null ) ? conferenciaDTO.getNomeProduto()  				: "";
			numeroEdicao 	= ( conferenciaDTO.getNumeroEdicao()    != null ) ? conferenciaDTO.getNumeroEdicao().toString()  	: "";
			precoCapa 		= ( conferenciaDTO.getPrecoCapa()       != null ) ? conferenciaDTO.getPrecoCapa().toString()  		: "";
			desconto 		= ( conferenciaDTO.getDesconto()        != null ) ? conferenciaDTO.getDesconto().toString()  		: "";
			valorTotal 		= ( conferenciaDTO.getValorTotal()      != null ) ? conferenciaDTO.getValorTotal().toString()  		: "";
			dia 			= ( conferenciaDTO.getDia()             != null ) ? conferenciaDTO.getDia().toString()  			: "";
			
			juramentada 	= conferenciaDTO.isJuramentada();
			
			ConferenciaEncalheVO conferenciaVO = new ConferenciaEncalheVO();
			
			conferenciaVO.setQtdExemplar(qtdExemplar);
			conferenciaVO.setCodigoDeBarras(codigoDeBarras);
			conferenciaVO.setCodigoSM(codigoSM);
			conferenciaVO.setCodigo(codigo);
			conferenciaVO.setNomeProduto(nomeProduto);
			conferenciaVO.setNumeroEdicao(numeroEdicao);
			conferenciaVO.setPrecoCapa(precoCapa);
			conferenciaVO.setDesconto(desconto);
			conferenciaVO.setValorTotal(valorTotal);
			conferenciaVO.setDia(dia);
			conferenciaVO.setJuramentada(juramentada);
			
			listaConferenciaEncalheVO.add(conferenciaVO);
			
		}
		
		return listaConferenciaEncalheVO;
		
		
	}
	
	/**
	 * Obtém lista de objetos do tipo DebitoCreditoCotaVO a partir de 
	 * lista de objetos do tipo DebitoCreditoCotaDTO.
	 * 
	 * @param listaDebitoCreditoCotaDTO
	 * 
	 * @return List - DebitoCreditoCotaVO
	 */
	private List<DebitoCreditoCotaVO> obterListaDebitoCreditoCotaVOFromDTO(List<DebitoCreditoCotaDTO> listaDebitoCreditoCotaDTO) {
		
		List<DebitoCreditoCotaVO> listaDebitoCreditoCotaVO = new LinkedList<DebitoCreditoCotaVO>();

		String tipoLancamento 	= null;
		String valor 			= null;
		String dataLancamento	= null;
		String dataVencimento   = null;
		String numeroCota		= null;
	
		for(DebitoCreditoCotaDTO debitoCreditoCotaDTO : listaDebitoCreditoCotaDTO) {
			
			tipoLancamento 	= (debitoCreditoCotaDTO.getTipoLancamento() != null ) ?  debitoCreditoCotaDTO.getTipoLancamento().toString() : ""; 
			valor 			= (debitoCreditoCotaDTO.getValor() 			!= null ) ?  debitoCreditoCotaDTO.getValor().toString() 		 : "";
			dataLancamento	= (debitoCreditoCotaDTO.getDataLancamento() != null ) ?  debitoCreditoCotaDTO.getDataLancamento().toString() : "";
			dataVencimento  = (debitoCreditoCotaDTO.getDataVencimento() != null ) ?  debitoCreditoCotaDTO.getDataVencimento().toString() : "";
			numeroCota		= (debitoCreditoCotaDTO.getNumeroCota() 	!= null ) ?  debitoCreditoCotaDTO.getNumeroCota().toString() 	 : "";
			
			DebitoCreditoCotaVO debitoCreditoCotaVO = new DebitoCreditoCotaVO();
			
			debitoCreditoCotaVO.setTipoLancamento(tipoLancamento);
			debitoCreditoCotaVO.setValor(valor);
			debitoCreditoCotaVO.setDataLancamento(dataLancamento);
			debitoCreditoCotaVO.setDataVencimento(dataVencimento);
			debitoCreditoCotaVO.setNumeroCota(numeroCota);			
			
			listaDebitoCreditoCotaVO.add(debitoCreditoCotaVO);
			
		}
		
		return listaDebitoCreditoCotaVO;
		
	}
	
	
	
	
}
