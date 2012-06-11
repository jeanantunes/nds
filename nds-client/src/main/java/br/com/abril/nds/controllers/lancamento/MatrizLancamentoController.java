package br.com.abril.nds.controllers.lancamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ResultadoResumoBalanceamentoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.view.Results;

@Resource
public class MatrizLancamentoController {

	@Autowired
	private Result result;

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private MatrizLancamentoService matrizLancamentoService;
	
	@Autowired
	private Validator validator;
	
	@Autowired 
	private Localization localization;
	
	private static final String FORMATO_DATA = "dd/MM/yyyy";
	
	private static final String CAMPO_REQUERIDO_KEY = "required_field";
	private static final String CAMPO_MAIOR_IGUAL_KEY = "validator.must.be.greaterEquals";

	@Path("/matrizLancamento")
	public void index() {
		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(
				true, SituacaoCadastro.ATIVO);
		String data = DateUtil.formatarData(new Date(), FORMATO_DATA);
		result.include("data", data);
		result.include("fornecedores", fornecedores);
	}
	
	@Post
	public void obterMatrizLancamento(Date dataLancamento, List<Long> idsFornecedores) {
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = null;
		
		// TODO: criar filtro e setar na sessão
		
		// TODO: chamar o service para retornar a matriz de balanceamento
		
		// TODO: setar a matriz na sessão
		
		ResultadoResumoBalanceamentoVO resumoPeriodoBalanceamento =
			obterResumoBalanceamentoLancamento(balanceamentoLancamento);
		
		this.result.use(Results.json()).from(resumoPeriodoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	public void obterGridMatrizLancamento(String sortorder, String sortname, int page, int rp) {
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = null;
		
		// TODO: obter a matriz da sessão
		
		if (balanceamentoLancamento == null
				|| balanceamentoLancamento.getMatrizLancamento() == null
				|| balanceamentoLancamento.getMatrizLancamento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		List<ProdutoLancamentoDTO> listaProdutoLancamento = null;
		
		// TODO: montar a lista de produtoLancamentoDTO através do map
		
		if (listaProdutoLancamento != null && !listaProdutoLancamento.isEmpty()) {
			
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			processarBalanceamento(listaProdutoLancamento, paginacao);
			
		} else {
			
			this.result.use(Results.json()).from(Results.nothing()).serialize();
		}
	}
	
	@Post
	public void confirmarMatrizLancamento() {
		
		// TODO: obter a matriz da sessão
		
		// TODO: chamar a service de confirmação
	}
	
	@Post
	public void voltarConfiguracaoOriginal() {
		
		// TODO: montar a matriz inicial e setar na sessão
	}
	
	@Post
	public void reprogramarLancamentosSelecionados(List<ProdutoLancamentoDTO> listaProdutoLancamento,
												   String novaDataFormatada, String dataAntigaFormatada) {
		
		// TODO: reprogramar os lançamentos selecionados
		
		// TODO: atualizar a matriz q estava na sessão
		
		// TODO: setar a matriz na sessão
	}
	
	@Post
	public void reprogramarLancamentoUnico(ProdutoLancamentoDTO produtoLancamento,
										   String dataAntigaFormatada) {
		
		// TODO: reprogramar o lançamento informado
		
		// TODO: atualizar a matriz q estava na sessão
		
		// TODO: setar a matriz na sessão
	}	
	
	/**
	 * Obtém o resumo do período de balanceamento de lançamento.
	 */
	private ResultadoResumoBalanceamentoVO obterResumoBalanceamentoLancamento(
											BalanceamentoLancamentoDTO balanceamentoLancamento) {
		
		// TODO: montar o resumo de balanceamento de lançamento
		
		return null;
	}
	
	private void processarBalanceamento(List<ProdutoLancamentoDTO> listaProdutoLancamento,
										PaginacaoVO paginacao) {
		
		TableModel<CellModelKeyValue<ProdutoLancamentoDTO>> tableModel =
			new TableModel<CellModelKeyValue<ProdutoLancamentoDTO>>();
		
		tableModel.setPage(paginacao.getPaginaAtual());
		//tableModel.setTotal(totalRegistros);
		
		List<CellModelKeyValue<ProdutoLancamentoDTO>> listaCellModel =
			new ArrayList<CellModelKeyValue<ProdutoLancamentoDTO>>();
		
		CellModelKeyValue<ProdutoLancamentoDTO> cellModel = null;
		
		for (ProdutoLancamentoDTO dto : listaProdutoLancamento) {
			
			cellModel =
				new CellModelKeyValue<ProdutoLancamentoDTO>(dto.getIdLancamento().intValue(),
															dto);
			
			listaCellModel.add(cellModel);
		}
		
		tableModel.setRows(listaCellModel);
		
		// TODO: montar o DTO ou VO (se necessário) para exibir no grid
		
		// TODO: paginação e ordenação em memória
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	
	
	
	@Post
	public void matrizLancamento(Date data, List<Long> idsFornecedores,
			String sortorder, String sortname, int page, int rp) {
		validar();
		verificarCamposObrigatorios(data, idsFornecedores);
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				idsFornecedores, paginacaoVO, sortname);
		List<LancamentoDTO> dtos = matrizLancamentoService
				.buscarLancamentosBalanceamento(filtro);
		SumarioLancamentosDTO sumario = matrizLancamentoService
				.sumarioBalanceamentoMatrizLancamentos(data, idsFornecedores);

		TableModel<CellModelKeyValue<LancamentoDTO>> tm = new TableModel<CellModelKeyValue<LancamentoDTO>>();
		List<CellModelKeyValue<LancamentoDTO>> cells = CellModelKeyValue
				.toCellModelKeyValue(dtos);

		tm.setRows(cells);
		tm.setPage(page);
		tm.setTotal(sumario.getTotalLancamentos().intValue());
		Object[] resultado = {tm, sumario.getValorTotalFormatado()};
		result.use(Results.json()).withoutRoot().from(resultado).serialize();
	}

	@Get
	public void resumoPeriodo(Date dataInicial, List<Long> idsFornecedores) {
		validar();
		verificarCamposObrigatorios(dataInicial, idsFornecedores);
		List<ResumoPeriodoBalanceamentoDTO> dtos = matrizLancamentoService
				.obterResumoPeriodo(dataInicial, idsFornecedores);
		result.use(Results.json()).withoutRoot().from(dtos).serialize();
	}

	private void validar() {
		if (validator.hasErrors()) {
			List<String> mensagens = new ArrayList<String>();
			for (Message message : validator.getErrors()) {
				mensagens.add(message.getMessage());
			}
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.ERROR, mensagens);
			throw new ValidacaoException(validacao);
		}
	}

	private void verificarCamposObrigatorios(Date data,
			List<Long> idsFornecedores) {
		Date atual = DateUtil.removerTimestamp(new Date());
		List<String> mensagens = new ArrayList<String>();
		if (idsFornecedores == null || idsFornecedores.isEmpty()) {
			mensagens.add(localization.getMessage(CAMPO_REQUERIDO_KEY,
					"Fornecedor"));
		}
		if (data == null) {
			mensagens.add(localization.getMessage(CAMPO_REQUERIDO_KEY,
					"Data de Lançamento Matriz/Distribuidor"));
		} else if (data.before(atual)) {
			mensagens.add(localization.getMessage(CAMPO_MAIOR_IGUAL_KEY,
					"Data de Lançamento Matriz/Distribuidor", DateUtil.formatarDataPTBR(atual)));
		}
		if (!mensagens.isEmpty()) {
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.ERROR, mensagens);
			throw new ValidacaoException(validacao);
		}
	}

}
