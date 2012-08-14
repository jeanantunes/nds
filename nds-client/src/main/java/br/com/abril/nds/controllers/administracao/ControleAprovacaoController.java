package br.com.abril.nds.controllers.administracao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ControleAprovacaoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MovimentoAprovacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO.OrdenacaoColunaControleAprovacao;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.ControleAprovacaoService;
import br.com.abril.nds.service.TipoMovimentoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes à
 * tela de controle de aprovações.
 * 
 * @author Discover Technology
 */
@Resource
@Path("/administracao/controleAprovacao")
public class ControleAprovacaoController {

	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private ControleAprovacaoService controleAprovacaoService;
	
	@Autowired
	private TipoMovimentoService tipoMovimentoService;
	
	private static final String FILTRO_PESQUISA_CONTROLE_APROVACAO_SESSION_ATTRIBUTE = "filtroPesquisaControleAprovacao";
	
	private static final String QTD_REGISTROS_PESQUISA_CONTROLE_APROVACAO_SESSION_ATTRIBUTE = "qtdRegistrosPesquisaControleAprovacao";
	
	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_CONTROLE_APROVACAO)
	public void index() {
		
		carregarComboTipoMovimento();
		carregarStatusAprovacao();
	}
	
	private void carregarComboTipoMovimento() {
		
		List<TipoMovimento> listaTipoMovimento = tipoMovimentoService.obterTiposMovimento();
		
		List<ItemDTO<Long, String>> listaTipoMovimentoCombo =
				new ArrayList<ItemDTO<Long,String>>();
		
		for (TipoMovimento tipoMovimento : listaTipoMovimento) {
			
			listaTipoMovimentoCombo.add(
				new ItemDTO<Long, String>(tipoMovimento.getId(), tipoMovimento.getDescricao()));
		}
		
		result.include("listaTipoMovimentoCombo", listaTipoMovimentoCombo);
	}
	
	private void carregarStatusAprovacao() {
		
		HashMap<String, String> listaStatusAprovacao = new HashMap<String, String>();
		
		listaStatusAprovacao.put(StatusAprovacao.APROVADO.getDescricaoAbreviada(), StatusAprovacao.APROVADO.getDescricao());
		listaStatusAprovacao.put(StatusAprovacao.PENDENTE.getDescricaoAbreviada(), StatusAprovacao.PENDENTE.getDescricao());
		listaStatusAprovacao.put(StatusAprovacao.REJEITADO.getDescricaoAbreviada(), StatusAprovacao.REJEITADO.getDescricao());
				
		result.include("listaStatusAprovacao", listaStatusAprovacao);
		
	}
	
	@Path("/pesquisarAprovacoes")
	public void pesquisarAprovacoes(Long idTipoMovimento, String dataMovimentoFormatada, String statusAprovacao,
			 						String sortorder, String sortname, int page, int rp) {
		
		this.validarEntradaDadosPesquisa(dataMovimentoFormatada);
		
		Date dataMovimento = DateUtil.parseDataPTBR(dataMovimentoFormatada);
		
		StatusAprovacao status = StatusAprovacao.getStatusAprovacao(statusAprovacao);
		
		FiltroControleAprovacaoDTO filtro  = 
			this.carregarFiltroPesquisa(idTipoMovimento, dataMovimento, status,sortorder,
										sortname, page, rp);
		
		List<MovimentoAprovacaoDTO> listaMovimentoAprovacaoDTO =
			controleAprovacaoService.obterMovimentosAprovacao(filtro);
		
		if (listaMovimentoAprovacaoDTO == null || listaMovimentoAprovacaoDTO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
			
			Long qtdeTotalRegistros = 
				this.controleAprovacaoService.obterTotalMovimentosAprovacao(filtro);
			
			PaginacaoUtil.armazenarQtdRegistrosPesquisa(
				this.session,
				QTD_REGISTROS_PESQUISA_CONTROLE_APROVACAO_SESSION_ATTRIBUTE,
				listaMovimentoAprovacaoDTO.size());
						
			this.processarAprovacoes(listaMovimentoAprovacaoDTO, filtro,
									 qtdeTotalRegistros.intValue());
		}
		
		result.use(Results.json());
	}
	
	@Post
	public void aprovarMovimento(Long idMovimento) {
		
		controleAprovacaoService.aprovarMovimento(idMovimento, obterUsuario());
		
		PaginacaoUtil.atualizarQtdRegistrosPesquisa(
			this.session, QTD_REGISTROS_PESQUISA_CONTROLE_APROVACAO_SESSION_ATTRIBUTE);
		
		result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Movimento aprovado com sucesso."),
							"result").recursive().serialize();
	}
	
	@Post
	public void rejeitarMovimento(Long idMovimento, String motivo) {
		
		if (motivo == null || motivo.trim().isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"O preenchimento do campo [Motivo] é obrigatório!");
		}
		
		motivo = motivo.trim();
		
		if (motivo.length() > 255) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"O campo [Motivo] deve conter até 255 caracteres!");
		}
		
		controleAprovacaoService.rejeitarMovimento(idMovimento, motivo, obterUsuario());
		
		PaginacaoUtil.atualizarQtdRegistrosPesquisa(
			this.session, QTD_REGISTROS_PESQUISA_CONTROLE_APROVACAO_SESSION_ATTRIBUTE);
		
		result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Movimento rejeitado com sucesso."),
							"result").recursive().serialize();
	}
	
	/**
	 * Processa o resultado dos controles de aprovação.
	 *  
	 * @param listaMovimentoAprovacaoDTO - lista de movimentos para aprovação
	 * @param filtro - filtro da pesquisa
	 * @param qtdeTotalRegistros - quantidade total de registros
	 */
	private void processarAprovacoes(List<MovimentoAprovacaoDTO> listaMovimentoAprovacaoDTO, 
									 FiltroControleAprovacaoDTO filtro,
									 Integer qtdeTotalRegistros) {
		
		List<ControleAprovacaoVO> listaControleAprovacao = new LinkedList<ControleAprovacaoVO>();
		
		ControleAprovacaoVO controleAprovacao = null;
		
		for (MovimentoAprovacaoDTO movimentoAprovacao : listaMovimentoAprovacaoDTO) {
			
			controleAprovacao = new ControleAprovacaoVO();
			
			controleAprovacao.setId(movimentoAprovacao.getIdMovimento().toString());
			
			controleAprovacao.setTipoMovimento(movimentoAprovacao.getDescricaoTipoMovimento());
			
			controleAprovacao.setDataMovimento(
				DateUtil.formatarDataPTBR(movimentoAprovacao.getDataCriacao()));
			
			if (movimentoAprovacao.getNumeroCota() != null) {
				controleAprovacao.setNumeroCota(movimentoAprovacao.getNumeroCota().toString());
			} else {
				controleAprovacao.setNumeroCota("");
			}
			
			controleAprovacao.setNomeCota(
				(movimentoAprovacao.getNomeCota() != null) ? movimentoAprovacao.getNomeCota() : "");
			
			if (movimentoAprovacao.getValor() != null) {
				controleAprovacao.setValor(
					CurrencyUtil.formatarValor(movimentoAprovacao.getValor()));
			} else {
				controleAprovacao.setValor("");
			}
			
			if (movimentoAprovacao.getParcelas() != null) {
				controleAprovacao.setParcelas(movimentoAprovacao.getParcelas().toString());
			} else {
				controleAprovacao.setParcelas("");
			}
			
			if (movimentoAprovacao.getPrazo() != null) {
				controleAprovacao.setPrazo(movimentoAprovacao.getPrazo().toString());
			} else {
				controleAprovacao.setPrazo("");
			}
			
			controleAprovacao.setRequerente(movimentoAprovacao.getNomeUsuarioRequerente());
			
			controleAprovacao.setStatus(movimentoAprovacao.getStatusMovimento().getDescricaoAbreviada());
			
			controleAprovacao.setMotivo(movimentoAprovacao.getMotivo());
			
			listaControleAprovacao.add(controleAprovacao);
		}
		
		TableModel<CellModelKeyValue<ControleAprovacaoVO>> tableModel =
			new TableModel<CellModelKeyValue<ControleAprovacaoVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaControleAprovacao));
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		tableModel.setTotal(qtdeTotalRegistros);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	/**
	 * Valida a entrada de dados para pesquisa de controle de aprovações.
	 * 
	 * @param dataMovimentoFormatada - data de movimento formatado
	 */
	private void validarEntradaDadosPesquisa(String dataMovimentoFormatada) {
		
		if (dataMovimentoFormatada == null 
				|| dataMovimentoFormatada.trim().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O preenchimento do campo [Data] é obrigatório!");
		}
		
		if (!DateUtil.isValidDatePTBR(dataMovimentoFormatada)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Data inválida");
		}
	}
	
	/**
	 * Carrega o filtro da pesquisa de controle de aprovações.
	 * 
	 * @param idTipoMovimento - id tipo de movimento
	 * @param dataMovimento - data do movimento
	 * @param status - status de aprovacao
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 * 
	 * @return Filtro
	 */
	private FiltroControleAprovacaoDTO carregarFiltroPesquisa(Long idTipoMovimento,
															  Date dataMovimento, 
															  StatusAprovacao status,
															  String sortorder, 
															  String sortname, 
															  int page, 
															  int rp) {
		
		FiltroControleAprovacaoDTO filtroAtual =
			new FiltroControleAprovacaoDTO(dataMovimento, idTipoMovimento, status);
		
		this.configurarPaginacaoPesquisa(filtroAtual, sortorder, sortname, page, rp);
		
		FiltroControleAprovacaoDTO filtroSessao =
			(FiltroControleAprovacaoDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_CONTROLE_APROVACAO_SESSION_ATTRIBUTE);
		
		PaginacaoUtil.calcularPaginaAtual(
			this.session,
			QTD_REGISTROS_PESQUISA_CONTROLE_APROVACAO_SESSION_ATTRIBUTE,
			FILTRO_PESQUISA_CONTROLE_APROVACAO_SESSION_ATTRIBUTE,
			filtroAtual, filtroSessao);
		
		return filtroAtual;
	}
	
	/**
	 * Configura a paginação do filtro de pesquisa de controle de aprovações.
	 * 
	 * @param filtro - filtro da pesquisa
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 */
	private void configurarPaginacaoPesquisa(FiltroControleAprovacaoDTO filtro, 
											 String sortorder,
											 String sortname, 
											 int page, 
											 int rp) {
		
		if (filtro != null) {
			
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
	
			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna(
				Util.getEnumByStringValue(OrdenacaoColunaControleAprovacao.values(), sortname));
		}
	}
	
	/**
	 * Obtém usuário logado.
	 * 
	 * @return usuário logado
	 */
	private Usuario obterUsuario() {
		
		//TODO: Aguardando definição de como será obtido o usuário logado
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		return usuario;
	}
	
}
