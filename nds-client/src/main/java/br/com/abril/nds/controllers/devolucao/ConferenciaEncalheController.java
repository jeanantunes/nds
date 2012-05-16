package br.com.abril.nds.controllers.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
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
	
	private static final String INFO_CONFERENCIA = "infoCoferencia";
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
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
		
		this.limparDadosSessao();
		
		if (idBox != null){
		
			this.session.setAttribute(ID_BOX_LOGADO, idBox);
		}
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	private void limparDadosSessao() {
		
		this.session.removeAttribute(ID_BOX_LOGADO);
		this.session.removeAttribute(INFO_CONFERENCIA);
		this.session.removeAttribute(LISTA_CONFERENCIA_ENCALHE);
		this.session.removeAttribute(NUMERO_COTA_CONFERENCIA);
		this.session.removeAttribute(VALOR_ENCALHE_JORNALEIRO);
	}
	
	@Post
	public void verificarReabertura(Integer numeroCota){
		
		try {
			
			this.conferenciaEncalheService.verificarChamadaEncalheCota(numeroCota);
		} catch (ConferenciaEncalheExistenteException e) {
			
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "REABERTURA"), "result").recursive().serialize();
			return;
		} catch (ChamadaEncalheCotaInexistenteException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe para essa cota.");
		}
		
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
		
		InfoConferenciaEncalheCota infoConfereciaEncalheCota = this.getInfoConferenciaSession();
		
		if (infoConfereciaEncalheCota == null){
			
			infoConfereciaEncalheCota = 
					conferenciaEncalheService.obterInfoConferenciaEncalheCota(this.getIdCotaConferenciaSession());
			
			this.session.setAttribute(INFO_CONFERENCIA, infoConfereciaEncalheCota);
			
			this.setListaConferenciaEncalheToSession(infoConfereciaEncalheCota.getListaConferenciaEncalhe());
		}
		
		Object[] dados = new Object[7];
		
		dados[0] = this.obterTableModelConferenciaEncalhe(infoConfereciaEncalheCota.getListaConferenciaEncalhe());
		
		dados[1] = this.obterTableModelDebitoCreditoCota(infoConfereciaEncalheCota.getListaDebitoCreditoCota());
		
		dados[2] = infoConfereciaEncalheCota.getReparte() == null ? BigDecimal.ZERO : infoConfereciaEncalheCota.getReparte();
		
		calcularValoresMonetarios(dados);
		
		result.use(Results.json()).withoutRoot().from(dados).recursive().serialize();
	}
	
	private InfoConferenciaEncalheCota getInfoConferenciaSession() {
		
		return (InfoConferenciaEncalheCota) this.session.getAttribute(INFO_CONFERENCIA);
	}

	private void calcularValoresMonetarios(Object[] dados){
		
		BigDecimal valorEncalhe = BigDecimal.ZERO;
		BigDecimal valorVendaDia = BigDecimal.ZERO;
		BigDecimal valorDebitoCredito = BigDecimal.ZERO;
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info != null){
		
			for (ConferenciaEncalheDTO conferenciaEncalheDTO : info.getListaConferenciaEncalhe()){
				
				valorEncalhe = valorEncalhe.add(
						conferenciaEncalheDTO.getPrecoCapa()
						.subtract(conferenciaEncalheDTO.getDesconto())
						.multiply(conferenciaEncalheDTO.getQtdExemplar()));
			}
			
			valorVendaDia = valorVendaDia.add(info.getReparte()).subtract(valorEncalhe);
			
			for (DebitoCreditoCotaDTO debitoCreditoCotaDTO : info.getListaDebitoCreditoCota()){
				
				valorDebitoCredito = valorDebitoCredito.add(debitoCreditoCotaDTO.getValor());
			}
		}
		
		dados[3] = valorEncalhe;
		dados[4] = valorVendaDia;
		dados[5] = valorDebitoCredito;
		dados[6] = valorEncalhe.subtract(valorVendaDia).add(valorDebitoCredito);
	}
	
	@Post
	public void pesquisarProdutoEdicao(String codigoBarra, Long sm, Long idProdutoEdicao, Long codigoAnterior, Long quantidade){
		
		ProdutoEdicao produtoEdicao = null;
		
		ConferenciaEncalheDTO conferenciaEncalheDTO = null;
		
		Integer numeroCota = (Integer) this.session.getAttribute(NUMERO_COTA_CONFERENCIA);
		
		List<ConferenciaEncalheDTO> listaConfSessao = this.getListaConferenciaEncalheFromSession();
		
		try {
			if (codigoBarra != null && !codigoBarra.trim().isEmpty()){
				
				for (ConferenciaEncalheDTO dto : listaConfSessao){
					
					if (codigoBarra.equals(dto.getCodigoDeBarras())){
						
						conferenciaEncalheDTO = dto;
						break;
					}
				}
				
				if (conferenciaEncalheDTO == null){
				
					produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorCodigoDeBarras(numeroCota, codigoBarra);
				}
			} else if (sm != null){
				
				for (ConferenciaEncalheDTO dto : listaConfSessao){
					
					if (sm.equals(dto.getCodigoSM())){
						
						conferenciaEncalheDTO = dto;
						break;
					}
				}
				
				if (conferenciaEncalheDTO == null){
				
					produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorSM(numeroCota, sm);
				}
			} else if (idProdutoEdicao != null){
				
				for (ConferenciaEncalheDTO dto : listaConfSessao){
					
					if (idProdutoEdicao.equals(dto.getIdProdutoEdicao())){
						
						conferenciaEncalheDTO = dto;
						break;
					}
				}
				
				if (conferenciaEncalheDTO == null){
				
					produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(numeroCota, idProdutoEdicao);
				}
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Informe código de barras, SM ou código.");
			}
		} catch (ChamadaEncalheCotaInexistenteException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe deste produto para essa cota.");
		}
		
		if (conferenciaEncalheDTO == null && produtoEdicao == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto Edição não encontrado.");
		}
		
		if (codigoAnterior != null && quantidade != null){
			
			conferenciaEncalheDTO = this.atualizarQuantidadeConferida(codigoAnterior, quantidade, produtoEdicao);
		}
		
		this.result.use(Results.json()).from(conferenciaEncalheDTO, "result").serialize();
	}
	
	@Post
	public void adicionarProdutoConferido(Long quantidade, Long idProdutoEdicao) throws ChamadaEncalheCotaInexistenteException{
		
		ProdutoEdicao produtoEdicao = 
				this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(
						this.getIdCotaConferenciaSession(), 
						idProdutoEdicao);
		
		this.atualizarQuantidadeConferida(null, quantidade, produtoEdicao);
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	@Post
	public void salvarConferencia(){
		
		//TODO
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
	}
	
	@Post
	public void pesquisarProdutoPorCodigoNome(String codigoNomeProduto){
		
		List<ProdutoEdicao> listaProdutoEdicao = this.produtoEdicaoService.obterProdutoPorCodigoNome(codigoNomeProduto);
		
		if (listaProdutoEdicao != null && !listaProdutoEdicao.isEmpty()){
			
			List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
			
			for (ProdutoEdicao produtoEdicao : listaProdutoEdicao){
				
				listaProdutos.add(
						new ItemAutoComplete(
								produtoEdicao.getProduto().getCodigo() + " - " + produtoEdicao.getProduto().getNome() + " - " + produtoEdicao.getNumeroEdicao(), 
								null,
								new Object[]{produtoEdicao.getProduto().getCodigo(), produtoEdicao.getId()}));
			}
			
			result.use(Results.json()).from(listaProdutos, "result").recursive().serialize();
		} else {
		
			result.use(Results.json()).from("", "result").serialize();
		}
	}
	
	/*
	 * Atualiza quantidade da conferencia ou cria um novo registro caso seja a primeira vez que se esta conferindo o produtoedicao
	 */
	private ConferenciaEncalheDTO atualizarQuantidadeConferida(Long codigoAnterior, Long quantidade, ProdutoEdicao produtoEdicao) {
		
		ConferenciaEncalheDTO conferenciaEncalheDTOSessao = null;
		
		if (codigoAnterior != null){
		
			//busca conferencia na sessão
			List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
			
			for (ConferenciaEncalheDTO dto : lista){
				
				if (dto.getIdProdutoEdicao().equals(codigoAnterior)){
					
					conferenciaEncalheDTOSessao = dto;
					break;
				}
			}
		}
		
		if (conferenciaEncalheDTOSessao != null){
			
			conferenciaEncalheDTOSessao.setQtdExemplar(new BigDecimal(quantidade));
		} else {
			
			conferenciaEncalheDTOSessao = new ConferenciaEncalheDTO();
			
			conferenciaEncalheDTOSessao.setIdConferenciaEncalhe(new Long((int) System.currentTimeMillis()) *-1);
			conferenciaEncalheDTOSessao.setCodigo(produtoEdicao.getProduto().getCodigo());
			conferenciaEncalheDTOSessao.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
			conferenciaEncalheDTOSessao.setCodigoSM(produtoEdicao.getCodigoSM());
			conferenciaEncalheDTOSessao.setIdProdutoEdicao(produtoEdicao.getId());
			conferenciaEncalheDTOSessao.setNomeProduto(produtoEdicao.getProduto().getNome());
			conferenciaEncalheDTOSessao.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			conferenciaEncalheDTOSessao.setPrecoCapa(produtoEdicao.getPrecoVenda());
			conferenciaEncalheDTOSessao.setQtdExemplar(new BigDecimal(quantidade));
			conferenciaEncalheDTOSessao.setDesconto(produtoEdicao.getDesconto());
			
			this.getListaConferenciaEncalheFromSession().add(conferenciaEncalheDTOSessao);
		}
		
		return conferenciaEncalheDTOSessao;
	}

	/**
	 * Obtém tableModel para grid de conferencia encalhe.
	 * 
	 * @param listaConferenciaEncalhe
	 * 
	 * @return TableModel<CellModelKeyValue<ConferenciaEncalheVO>>
	 */
	private TableModel<CellModelKeyValue<ConferenciaEncalheDTO>> obterTableModelConferenciaEncalhe(List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {

		TableModel<CellModelKeyValue<ConferenciaEncalheDTO>> tableModelConferenciaEncalhe = 
				new TableModel<CellModelKeyValue<ConferenciaEncalheDTO>>();
		
		List<CellModelKeyValue<ConferenciaEncalheDTO>> list =
				new ArrayList<CellModelKeyValue<ConferenciaEncalheDTO>>();
		
		for (ConferenciaEncalheDTO dto : listaConferenciaEncalhe){
			
			list.add(new CellModelKeyValue<ConferenciaEncalheDTO>(dto.getIdConferenciaEncalhe().intValue(), dto));
		}
		
		tableModelConferenciaEncalhe.setRows(list);
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
	private TableModel<CellModelKeyValue<DebitoCreditoCotaDTO>> 
		obterTableModelDebitoCreditoCota(List<DebitoCreditoCotaDTO> listaDebitoCreditoCota) {

		TableModel<CellModelKeyValue<DebitoCreditoCotaDTO>> tableModelDebitoCreditoCota = 
				new TableModel<CellModelKeyValue<DebitoCreditoCotaDTO>>();
		
		tableModelDebitoCreditoCota.setRows(CellModelKeyValue.toCellModelKeyValue(listaDebitoCreditoCota));
		tableModelDebitoCreditoCota.setTotal((listaDebitoCreditoCota!= null) ? listaDebitoCreditoCota.size() : 0);
		tableModelDebitoCreditoCota.setPage(1);
		
		return tableModelDebitoCreditoCota;
	}
	
	public void carregarEmSessionValorCEJornaleiro() {
		//TODO codificar
	}
	
	@SuppressWarnings("unchecked")
	private List<ConferenciaEncalheDTO> getListaConferenciaEncalheFromSession() {
		return (List<ConferenciaEncalheDTO>) session.getAttribute(LISTA_CONFERENCIA_ENCALHE);
	}

	private void setListaConferenciaEncalheToSession(List<ConferenciaEncalheDTO> listaConferenciaEncalheDTO) {
		session.setAttribute(LISTA_CONFERENCIA_ENCALHE, listaConferenciaEncalheDTO);
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
	
	//TODO
	private Long getIdUsuarioLogado(){
		
		return 1L;
	}
}