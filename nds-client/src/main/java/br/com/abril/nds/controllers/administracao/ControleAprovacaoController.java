package br.com.abril.nds.controllers.administracao;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO.OrdenacaoColunaControleAprovacao;
import br.com.abril.nds.model.aprovacao.Aprovacao;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.service.ControleAprovacaoService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
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
	
	private ControleAprovacaoService controleAprovacaoService;
	
	private static final String FILTRO_PESQUISA_CONTROLE_APROVACAO_SESSION_ATTRIBUTE = "filtroPesquisaControleAprovacao";
	
	@Path("/")
	public void index() {
		
	}
	
	@Path("/pesquisarAprovacoes")
	public void pesquisarAprovacoes(String tipoMovimento, String dataMovimentoFormatada,
			 						String sortorder, String sortname, int page, int rp) {
		
		this.validarEntradaDadosPesquisa(dataMovimentoFormatada);
		
		Date dataMovimento = DateUtil.parseDataPTBR(dataMovimentoFormatada);
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = new TipoMovimentoFinanceiro();
		
		//TODO:
		FiltroControleAprovacaoDTO filtro  = 
			this.carregarFiltroPesquisa(tipoMovimentoFinanceiro, dataMovimento, sortorder,
										sortname, page, rp);
		
		List<Aprovacao> listaAprovacao =
			controleAprovacaoService.obterAprovacoes(filtro);
		
		if (listaAprovacao == null || listaAprovacao.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			
		} else {
			
			Long qtdeTotalRegistros = this.controleAprovacaoService.obterTotalAprovacoes(filtro);
			
			this.processarAprovacoes(listaAprovacao, filtro, qtdeTotalRegistros.intValue());
		}
		
		result.use(Results.json());
	}
	
	/*
	 * Processa o resultado dos controles de aprovação.
	 *  
	 * @param listaAprovacao - lista de diferenças
	 * @param filtro - filtro da pesquisa
	 * @param qtdeTotalRegistros - quantidade total de registros
	 */
	private void processarAprovacoes(List<Aprovacao> listaAprovacao, 
									 FiltroControleAprovacaoDTO filtro,
									 Integer qtdeTotalRegistros) {
		
		
	}
	
	/*
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
	
	/*
	 * Carrega o filtro da pesquisa de controle de aprovações.
	 * 
	 * @param tipoMovimento - tipo de movimento
	 * @param dataMovimento - data do movimento
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 * 
	 * @return Filtro
	 */
	private FiltroControleAprovacaoDTO carregarFiltroPesquisa(TipoMovimento tipoMovimento,
															  Date dataMovimento, 
															  String sortorder, 
															  String sortname, 
															  int page, 
															  int rp) {
		
		FiltroControleAprovacaoDTO filtroAtual =
			new FiltroControleAprovacaoDTO(dataMovimento, tipoMovimento);
		
		this.configurarPaginacaoPesquisa(filtroAtual, sortorder, sortname, page, rp);
		
		FiltroControleAprovacaoDTO filtroSessao =
			(FiltroControleAprovacaoDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_CONTROLE_APROVACAO_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
		
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.session.setAttribute(FILTRO_PESQUISA_CONTROLE_APROVACAO_SESSION_ATTRIBUTE, filtroAtual);
		
		return filtroAtual;
	}
	
	/*
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
	
}
