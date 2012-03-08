package br.com.abril.nds.controllers.lancamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoLancamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
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

	@Path("/matrizLancamento")
	public void index() {
		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(
				true, SituacaoCadastro.ATIVO);
		String data = DateUtil.formatarData(new Date(), FORMATO_DATA);
		result.include("data", data);
		result.include("fornecedores", fornecedores);
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
		List<ResumoPeriodoLancamentoDTO> dtos = matrizLancamentoService
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
		List<String> mensagens = new ArrayList<String>();
		if (idsFornecedores == null || idsFornecedores.isEmpty()) {
			mensagens.add(localization.getMessage(CAMPO_REQUERIDO_KEY,
					"Fornecedor"));
		}
		if (data == null) {
			mensagens.add(localization.getMessage(CAMPO_REQUERIDO_KEY,
					"Data de Lançamento Matriz/Distribuidor"));
		}
		if (!mensagens.isEmpty()) {
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.ERROR, mensagens);
			throw new ValidacaoException(validacao);
		}
	}

}
