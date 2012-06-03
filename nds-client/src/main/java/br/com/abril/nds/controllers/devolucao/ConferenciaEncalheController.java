package br.com.abril.nds.controllers.devolucao;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheFinalizadaException;
import br.com.abril.nds.service.exception.EncalheExcedeReparteException;
import br.com.abril.nds.service.exception.EncalheRecolhimentoParcialException;
import br.com.abril.nds.service.exception.EncalheSemPermissaoSalvarException;
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
	
	private static final String VALOR_ENCALHE_JORNALEIRO = "valorEncalheJornaleiro";
	
	private static final String ID_BOX_LOGADO = "idBoxLogado";
	
	private static final String INFO_CONFERENCIA = "infoCoferencia";
	
	private static final String SET_CONFERENCIA_ENCALHE_EXCLUIR = "listaConferenciaEncalheExcluir";
	
	private static final String NOTA_FISCAL_CONFERENCIA = "notaFiscalConferencia";
	
	private static final String HORA_INICIO_CONFERENCIA = "horaInicioConferencia";
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;
	
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
				this.conferenciaEncalheService.obterListaBoxEncalhe(this.getUsuarioLogado().getId());
		
		this.result.include("boxes", boxes);
	}
	
	@Post
	public void salvarIdBoxSessao(Long idBox){
		
		this.limparDadosSessao();
		
		if (idBox != null){
		
			this.session.setAttribute(ID_BOX_LOGADO, idBox);
		} else {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Box de recolhimento é obrigatório.");
		}
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	@Post
	public void verificarReabertura(Integer numeroCota){
		
		if (this.session.getAttribute(ID_BOX_LOGADO) == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Box de recolhimento não informado.");
		}
		
		this.session.setAttribute(HORA_INICIO_CONFERENCIA, new Date());
		
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
/*
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
	*/
	@Post
	public void carregarListaConferencia(Integer numeroCota){
		
		if (numeroCota == null){
			
			InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
			
			if (info != null){
				
				numeroCota = info.getCota().getNumeroCota();
			}
		}
		
		Date horaInicio = (Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA);
		
		if (horaInicio == null){
			
			this.session.setAttribute(HORA_INICIO_CONFERENCIA, new Date());
		}
		
		InfoConferenciaEncalheCota infoConfereciaEncalheCota = this.getInfoConferenciaSession();
		
		if (infoConfereciaEncalheCota == null){
			
			infoConfereciaEncalheCota = 
					conferenciaEncalheService.obterInfoConferenciaEncalheCota(numeroCota);
			
			this.session.setAttribute(INFO_CONFERENCIA, infoConfereciaEncalheCota);
			
			this.setListaConferenciaEncalheToSession(infoConfereciaEncalheCota.getListaConferenciaEncalhe());
		}
		
		Map<String, Object> dados = new HashMap<String, Object>();
		
		dados.put("listaConferenciaEncalhe", infoConfereciaEncalheCota.getListaConferenciaEncalhe());
		
		dados.put("listaDebitoCredito", this.obterTableModelDebitoCreditoCota(infoConfereciaEncalheCota.getListaDebitoCreditoCota()));
		
		dados.put("reparte", infoConfereciaEncalheCota.getReparte() == null ? BigDecimal.ZERO : infoConfereciaEncalheCota.getReparte());
		
		this.calcularValoresMonetarios(dados);
		
		Cota cota = infoConfereciaEncalheCota.getCota();
		
		if (cota != null){
			
			dados.put("razaoSocial", 
				cota.getPessoa() instanceof PessoaFisica ? 
						((PessoaFisica)cota.getPessoa()).getNome() : 
							((PessoaJuridica)cota.getPessoa()).getRazaoSocial());
			
			dados.put("situacao", cota.getSituacaoCadastro().toString());
		}
		
		dados.put("notaFiscal", this.session.getAttribute(NOTA_FISCAL_CONFERENCIA) == null ? "" : this.session.getAttribute(NOTA_FISCAL_CONFERENCIA));
		
		this.calcularTotais(dados);
		
		result.use(CustomMapJson.class).put("result", dados).serialize();
	}
	
	private void calcularTotais(Map<String, Object> dados) {
		
		BigDecimal qtdInformada = BigDecimal.ZERO;
		BigDecimal qtdRecebida = BigDecimal.ZERO;
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info != null){
			
			if (info.getListaConferenciaEncalhe() != null){
			
				for (ConferenciaEncalheDTO conferenciaEncalheDTO : info.getListaConferenciaEncalhe()){
					
					if (conferenciaEncalheDTO.getQtdInformada() != null){
					
						qtdInformada = qtdInformada.add(conferenciaEncalheDTO.getQtdInformada());
					}
					
					if (conferenciaEncalheDTO.getQtdExemplar() != null){
					
						qtdRecebida = qtdRecebida.add(conferenciaEncalheDTO.getQtdExemplar());
					}
				}
			}
		}
		
		dados.put("qtdInformada", qtdInformada);
		
		dados.put("qtdRecebida", qtdRecebida);
	}

	@Post
	public void pesquisarProdutoEdicao(String codigoBarra, Integer sm, Long idProdutoEdicao, Long codigoAnterior, Long quantidade){
		
		this.verificarInicioConferencia();
		
		ProdutoEdicaoDTO produtoEdicao = null;
		
		ConferenciaEncalheDTO conferenciaEncalheDTO = null;
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada.");
		}
		
		Integer numeroCota = info.getCota().getNumeroCota();
		
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
			
		} catch(ChamadaEncalheCotaInexistenteException e){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe deste produto para essa cota.");
		
		} catch(EncalheRecolhimentoParcialException e) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe para produto parcial na data operação.");
			
		}
		
		if (conferenciaEncalheDTO == null && produtoEdicao == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto Edição não encontrado.");
		} else if (conferenciaEncalheDTO == null){
			
			conferenciaEncalheDTO = this.criarConferenciaEncalhe(produtoEdicao, quantidade, false);
		}
		
		if (codigoAnterior != null && quantidade != null){
			
			conferenciaEncalheDTO = this.atualizarQuantidadeConferida(codigoAnterior, quantidade, produtoEdicao);
		}
		
		this.result.use(Results.json()).from(conferenciaEncalheDTO, "result").serialize();
	}
	
	@Post
	public void adicionarProdutoConferido(Long quantidade, Long idProdutoEdicao) throws ChamadaEncalheCotaInexistenteException{
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada.");
		}
		
		ProdutoEdicaoDTO produtoEdicao;
		
		try {
			produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(
					info.getCota().getNumeroCota(), 
					idProdutoEdicao);
		} catch (EncalheRecolhimentoParcialException e) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe para produto parcial na data operação.");
			
		}
		
		this.atualizarQuantidadeConferida(idProdutoEdicao, quantidade, produtoEdicao);
		
		this.carregarListaConferencia(null);
	}
	
	@Post
	public void recalcularConferencia(Long idConferencia, Long qtdExemplares, Boolean juramentada, BigDecimal valorCapa){
		
		List<ConferenciaEncalheDTO> listaConferencia = this.getListaConferenciaEncalheFromSession();
		
		ConferenciaEncalheDTO conf = null;
		
		if (idConferencia != null){
				
			for (ConferenciaEncalheDTO dto : listaConferencia){
				
				if (dto.getIdConferenciaEncalhe().equals(idConferencia)){
					
					dto.setQtdExemplar(new BigDecimal(qtdExemplares));
					
					if (juramentada != null){
					
						dto.setJuramentada(juramentada);
					}
					
					if (valorCapa != null){
						
						dto.setPrecoCapa(valorCapa);
					}
					
					dto.setValorTotal(dto.getPrecoCapa().subtract(dto.getDesconto()).multiply(dto.getQtdExemplar()));
					
					conf = dto;
					
					break;
				}
			}
		}
		
		Map<String, Object> dados = new HashMap<String, Object>();
		
		dados.put("conf", conf);
		
		dados.put("reparte", this.getInfoConferenciaSession().getReparte() == null ? BigDecimal.ZERO : this.getInfoConferenciaSession().getReparte());
		
		this.calcularValoresMonetarios(dados);
		
		this.calcularTotais(dados);
		
		this.result.use(CustomMapJson.class).put("result", dados == null ? "" : dados).serialize();
	}
	
	@Post
	public void salvarConferencia(){
		
		this.verificarInicioConferencia();
		
		ControleConferenciaEncalheCota controleConfEncalheCota = new ControleConferenciaEncalheCota();
		controleConfEncalheCota.setDataInicio((Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA));
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada.");
		}
		
		controleConfEncalheCota.setCota(info.getCota());
		controleConfEncalheCota.setId(this.getInfoConferenciaSession().getIdControleConferenciaEncalheCota());
		
		List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
		for (ConferenciaEncalheDTO dto : lista){
			
			if (dto.getIdConferenciaEncalhe() < 0){
				
				dto.setIdConferenciaEncalhe(null);
			}
		}
		
		try {
			
			this.conferenciaEncalheService.salvarDadosConferenciaEncalhe(
					controleConfEncalheCota, 
					this.getListaConferenciaEncalheFromSession(), 
					this.getSetConferenciaEncalheExcluirFromSession(), 
					this.getUsuarioLogado());
			
		} catch (EncalheSemPermissaoSalvarException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
			
		} catch (ConferenciaEncalheFinalizadaException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
			
		} catch (EncalheExcedeReparteException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
	}
	
	private void verificarInicioConferencia() {
		
		Date horaInicio = (Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA);
		
		if (horaInicio == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Conferência não iniciada.");
		}
	}

	@Post
	public void finalizarConferencia(){
		
		Date horaInicio = (Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA);
		
		if (horaInicio != null){
		
			ControleConferenciaEncalheCota controleConfEncalheCota = new ControleConferenciaEncalheCota();
			controleConfEncalheCota.setDataInicio(horaInicio);
			controleConfEncalheCota.setCota(this.getInfoConferenciaSession().getCota());
			controleConfEncalheCota.setId(this.getInfoConferenciaSession().getIdControleConferenciaEncalheCota());
			controleConfEncalheCota.setNotaFiscalEntradaCota((NotaFiscalEntradaCota) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA));
			
			List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
			
			for (ConferenciaEncalheDTO dto : lista){
				
				if (dto.getIdConferenciaEncalhe() < 0){
					
					dto.setIdConferenciaEncalhe(null);
				}
			}
			
			try {
				this.conferenciaEncalheService.finalizarConferenciaEncalhe(
						controleConfEncalheCota, 
						this.getListaConferenciaEncalheFromSession(), 
						this.getSetConferenciaEncalheExcluirFromSession(), 
						this.getUsuarioLogado());
			} catch (EncalheExcedeReparteException e) {

				this.result.use(Results.json()).from(
						new ValidacaoVO(TipoMensagem.WARNING, "Conferência de encalhe está excedendo quantidade de reparte."), "result").recursive().serialize();
			}
			
			this.result.use(Results.json()).from(
					new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
		} else {
			
			this.result.use(Results.json()).from(
					new ValidacaoVO(TipoMensagem.WARNING, "Conferência não iniciada."), "result").recursive().serialize();
		}
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
	
	@Post
	public void buscarDetalhesProduto(Long idConferenciaEncalhe){
		
		List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
		for (ConferenciaEncalheDTO dto : lista){
			
			if (dto.getIdConferenciaEncalhe().equals(idConferenciaEncalhe)){
				
				result.use(Results.json()).from(dto, "result").serialize();
				return;
			}
		}
		
		result.use(Results.json()).from("", "result").serialize();
	}
	
	@Post
	public void excluirConferencia(Long idConferenciaEncalhe){
		
		List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
		for (ConferenciaEncalheDTO dto : lista){
			
			if (dto.getIdConferenciaEncalhe().equals(idConferenciaEncalhe)){
				
				if (idConferenciaEncalhe > 0){
					
					this.getSetConferenciaEncalheExcluirFromSession().add(idConferenciaEncalhe);
				}
				
				lista.remove(dto);
				break;
			}
		}
		
		this.carregarListaConferencia(null);
	}
	
	@Post
	public void gravarObservacaoConferecnia(Long idConferenciaEncalhe, String observacao){
		
		List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
		for (ConferenciaEncalheDTO dto : lista){
			
			if (dto.getIdConferenciaEncalhe().equals(idConferenciaEncalhe)){
				
				dto.setObservacao(observacao);
				break;
			}
		}
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	@Post
	public void salvarNotaFiscal(NotaFiscalEntradaCota notaFiscal){
		
		this.session.setAttribute(NOTA_FISCAL_CONFERENCIA, notaFiscal);
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	@Post
	public void carregarNotaFiscal(){
		
		NotaFiscalEntradaCota nota = (NotaFiscalEntradaCota) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA);
		
		this.result.use(Results.json()).from(nota == null ? "" : nota, "result").serialize();
	}
	
	@Post
	public void verificarValorTotalNotaFiscal(){
		
		NotaFiscalEntradaCota nota = (NotaFiscalEntradaCota) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA);
		
		BigDecimal valorTotal = this.calcularValoresMonetarios(null);
		
		valorTotal = valorTotal.round(new MathContext(2));
		
		if (nota != null && nota.getValorProdutos() != null && 
				!nota.getValorProdutos().equals(valorTotal)){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Valor total dos produtos difere do valor da nota informada.");
		} else {
			
			this.finalizarConferencia();
		}
	}
	
	@Post
	public void pesquisarProdutoEdicaoPorId(Long idProdutoEdicao){
		
		
		Integer numeroCota = null;
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
			
		if (info != null){
			
			numeroCota = info.getCota().getNumeroCota();
		}
		
		try {
			ProdutoEdicaoDTO p = 
					this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(numeroCota, idProdutoEdicao);
			
			Map<String, Object> dados = new HashMap<String, Object>();
			
			if (p != null){
				
				dados.put("numeroEdicao", p.getNumeroEdicao());
				dados.put("precoVenda", p.getPrecoVenda());
				dados.put("desconto", p.getDesconto());
			}
			
			this.result.use(CustomMapJson.class).put("result", dados).serialize();
			
		} catch (ChamadaEncalheCotaInexistenteException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe deste produto para essa cota.");
		} catch (EncalheRecolhimentoParcialException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
	}
	
	private void limparDadosSessao() {
		
		this.session.removeAttribute(ID_BOX_LOGADO);
		this.session.removeAttribute(INFO_CONFERENCIA);
		this.session.removeAttribute(VALOR_ENCALHE_JORNALEIRO);
		this.session.removeAttribute(NOTA_FISCAL_CONFERENCIA);
		this.session.removeAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
		this.session.removeAttribute(HORA_INICIO_CONFERENCIA);
	}
	
	private InfoConferenciaEncalheCota getInfoConferenciaSession() {
		
		return (InfoConferenciaEncalheCota) this.session.getAttribute(INFO_CONFERENCIA);
	}

	private BigDecimal calcularValoresMonetarios(Map<String, Object> dados){
		
		BigDecimal valorEncalhe = BigDecimal.ZERO;
		BigDecimal valorVendaDia = BigDecimal.ZERO;
		BigDecimal valorDebitoCredito = BigDecimal.ZERO;
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info != null){
			
			if (info.getListaConferenciaEncalhe() != null){
			
				for (ConferenciaEncalheDTO conferenciaEncalheDTO : info.getListaConferenciaEncalhe()){
					
					valorEncalhe = valorEncalhe.add(
							conferenciaEncalheDTO.getPrecoCapa()
							.subtract(conferenciaEncalheDTO.getDesconto())
							.multiply(conferenciaEncalheDTO.getQtdExemplar()));
				}
			}
			
			valorVendaDia = valorVendaDia.add(info.getReparte()).subtract(valorEncalhe);
			
			if (info.getListaDebitoCreditoCota() != null){
			
				for (DebitoCreditoCotaDTO debitoCreditoCotaDTO : info.getListaDebitoCreditoCota()){
					
					valorDebitoCredito = valorDebitoCredito.add(debitoCreditoCotaDTO.getValor());
				}
			}
		}
		
		BigDecimal valorPagar = valorEncalhe.subtract(valorVendaDia).add(valorDebitoCredito);
		
		if (dados != null){
			
			dados.put("valorEncalhe", valorEncalhe);
			dados.put("valorVendaDia", valorVendaDia);
			dados.put("valorDebitoCredito", valorDebitoCredito);
			dados.put("valorPagar", valorPagar);
		}
		
		return valorPagar;
	}
	
	/*
	 * Atualiza quantidade da conferencia ou cria um novo registro caso seja a primeira vez que se esta conferindo o produtoedicao
	 */
	private ConferenciaEncalheDTO atualizarQuantidadeConferida(Long codigoAnterior, Long quantidade, ProdutoEdicaoDTO produtoEdicao) {
		
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
			
			conferenciaEncalheDTOSessao = this.criarConferenciaEncalhe(produtoEdicao, quantidade, true);
		}
		
		return conferenciaEncalheDTOSessao;
	}

	private ConferenciaEncalheDTO criarConferenciaEncalhe(ProdutoEdicaoDTO produtoEdicao, Long quantidade, boolean adicionarGrid) {
		
		ConferenciaEncalheDTO conferenciaEncalheDTO = new ConferenciaEncalheDTO();
		
		conferenciaEncalheDTO.setIdConferenciaEncalhe(new Long((int) System.currentTimeMillis()) *-1);
		conferenciaEncalheDTO.setCodigo(produtoEdicao.getCodigoProduto());
		conferenciaEncalheDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
		conferenciaEncalheDTO.setCodigoSM(produtoEdicao.getSequenciaMatriz());
		conferenciaEncalheDTO.setIdProdutoEdicao(produtoEdicao.getId());
		conferenciaEncalheDTO.setNomeProduto(produtoEdicao.getNomeProduto());
		conferenciaEncalheDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		conferenciaEncalheDTO.setPrecoCapa(produtoEdicao.getPrecoVenda());
		
		if (quantidade != null){
		
			conferenciaEncalheDTO.setQtdExemplar(new BigDecimal(quantidade));
		} else {
			
			conferenciaEncalheDTO.setQtdExemplar(BigDecimal.ONE);
		}
		
		conferenciaEncalheDTO.setDesconto(produtoEdicao.getDesconto());
		
		conferenciaEncalheDTO.setValorTotal(produtoEdicao.getPrecoVenda().subtract(produtoEdicao.getDesconto()).multiply(conferenciaEncalheDTO.getQtdExemplar()));
		
		if (adicionarGrid){
			
			List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
			lista.add(conferenciaEncalheDTO);
			this.setListaConferenciaEncalheToSession(lista);
		}
		
		return conferenciaEncalheDTO;
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
	
	private List<ConferenciaEncalheDTO> getListaConferenciaEncalheFromSession() {
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada.");
		}
		
		List<ConferenciaEncalheDTO> lista = info.getListaConferenciaEncalhe();
		
		if (lista == null){
			
			lista = new ArrayList<ConferenciaEncalheDTO>();
		}
		
		return lista;
	}

	private void setListaConferenciaEncalheToSession(List<ConferenciaEncalheDTO> listaConferenciaEncalheDTO) {
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada.");
		}
		
		info.setListaConferenciaEncalhe(listaConferenciaEncalheDTO);
	}
	
	private Set<Long> getSetConferenciaEncalheExcluirFromSession(){
		
		@SuppressWarnings("unchecked")
		Set<Long> set = (Set<Long>) this.session.getAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
		
		if (set == null){
			
			set = new HashSet<Long>();
		}
		
		return set;
	}
	
	//TODO
	private Usuario getUsuarioLogado(){
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		
		return usuario;
	}
}