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
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path(value="/devolucao/conferenciaEncalhe")
public class ConferenciaEncalheController {
	
	private static final String LISTA_CONFERENCIA_ENCALHE = "listaConferenciaEncalhe";
	
	private static final String VALOR_ENCALHE_JORNALEIRO = "valorEncalheJornaleiro";
	
	private static final String ID_BOX_LOGADO = "idBoxLogado";
	
	private static final String NUMERO_COTA_CONFERENCIA = "numeroCotaConferencia";
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;
	
	@Autowired
	private CotaService cotaService;
	
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
		
		List<Box> boxes = 
				this.conferenciaEncalheService.obterListaBoxEncalhe(this.getIdUsuarioLogado());
		
		this.result.include("boxes", boxes);
	}
	
	@Post
	public void salvarIdBoxSessao(Long idBox){
		
		this.session.setAttribute(ID_BOX_LOGADO, idBox);
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	@Post
	public void pesquisarCota(Integer numeroCota) {
		
		if (numeroCota != null){
			
			Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
			
			if (cota != null){
				
				List<String> dados = new ArrayList<String>();
				dados.add(
					cota.getPessoa() instanceof PessoaFisica ? ((PessoaFisica)cota.getPessoa()).getNome() : ((PessoaJuridica)cota.getPessoa()).getRazaoSocial());
				
				dados.add(cota.getSituacaoCadastro().toString());
				
				this.result.use(Results.json()).from(dados, "result").serialize();
				this.session.setAttribute(NUMERO_COTA_CONFERENCIA, numeroCota);
				
				return;
			}
		}
		
		this.session.removeAttribute(NUMERO_COTA_CONFERENCIA);
		
		throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada.");
	}
	
	@Post
	public void carregarListaConferencia(){
		
		InfoConferenciaEncalheCota infoConfereciaEncalheCota = 
				conferenciaEncalheService.obterInfoConferenciaEncalheCota(this.getIdCotaConferenciaSession());
		
		Object[] dados = new Object[6];
		
		List<ConferenciaEncalheVO> listaConferenciaEncalhe = 
				obterListaConferenciaEncalheVOFromDTO(infoConfereciaEncalheCota.getListaConferenciaEncalhe());
		
		dados[0] = obterTableModelConferenciaEncalhe(listaConferenciaEncalhe);
		
		dados[1] = infoConfereciaEncalheCota.getReparte() == null ? BigDecimal.ZERO : infoConfereciaEncalheCota.getReparte();
		dados[2] = infoConfereciaEncalheCota.getEncalhe() == null ? BigDecimal.ZERO : infoConfereciaEncalheCota.getEncalhe();
		dados[3] = infoConfereciaEncalheCota.getValorVendaDia() == null ? BigDecimal.ZERO : infoConfereciaEncalheCota.getValorVendaDia();
		dados[4] = infoConfereciaEncalheCota.getTotalDebitoCreditoCota() == null ? BigDecimal.ZERO : infoConfereciaEncalheCota.getTotalDebitoCreditoCota();
		dados[5] = infoConfereciaEncalheCota.getValorPagar() == null ? BigDecimal.ZERO : infoConfereciaEncalheCota.getValorPagar();
		
		result.use(Results.json()).withoutRoot().from(dados).recursive().serialize();
	}
	
	@Post
	public void pesquisarProdutoEdicao(String codigoBarra, Long sm, Long codigo){
		
		ProdutoEdicao produtoEdicao = null;
		
		Integer numeroCota = (Integer) this.session.getAttribute(NUMERO_COTA_CONFERENCIA);
		
		try {
			if (codigoBarra != null && !codigoBarra.trim().isEmpty()){
				
				produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorCodigoDeBarras(numeroCota, codigoBarra);
			} else if (sm != null){
				
				produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorSM(numeroCota, sm);
			} else if (codigo != null){
				
				produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(numeroCota, codigo);
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Informe código de barras, SM ou código.");
			}
		} catch (ChamadaEncalheCotaInexistenteException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe deste produto para essa cota.");
		}
		
		if (produtoEdicao == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto Edição não encontrado.");
		}
		
		this.result.use(Results.json()).from(produtoEdicao, "result").serialize();
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
	
	@SuppressWarnings("unused")
	private Integer getIdCotaConferenciaSession(){
		
		return (Integer) this.session.getAttribute(NUMERO_COTA_CONFERENCIA);
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
		
		if (listaConferenciaEncalheDTO != null){
		
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
	
	//TODO
	private Long getIdUsuarioLogado(){
		
		return 1L;
	}
	
	
}
