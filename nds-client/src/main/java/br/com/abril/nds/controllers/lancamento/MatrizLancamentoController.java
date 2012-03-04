package br.com.abril.nds.controllers.lancamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class MatrizLancamentoController {

	@Autowired
	private Result result;

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private MatrizLancamentoService matrizLancamentoService;
	
	private static final String FORMATO_DATA = "dd/MM/yyyy";

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
		//List<Long> fornecedores = converterIdsFornecedores(idsFornecedores);
		PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder);
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(data,
				idsFornecedores, paginacaoVO, sortname);
		List<LancamentoDTO> dtos = matrizLancamentoService
				.buscarLancamentosBalanceamento(filtro);
		long total = matrizLancamentoService
				.totalBalanceamentoMatrizLancamentos(data, idsFornecedores);

		TableModel<CellModelKeyValue<LancamentoDTO>> tm = new TableModel<CellModelKeyValue<LancamentoDTO>>();
		List<CellModelKeyValue<LancamentoDTO>> cells = CellModelKeyValue
				.toCellModelKeyValue(dtos);

		tm.setRows(cells);
		tm.setPage(page);
		tm.setTotal((int) total);
		result.use(Results.json()).withoutRoot().from(tm).include("rows")
				.serialize();
	}
	
	@Get
	public void resumoPeriodo(Date dataInicial, List<Long> idsFornecedores) {
		List<ResumoPeriodoLancamentoDTO> dtos = matrizLancamentoService
				.obterResumoPeriodo(dataInicial, idsFornecedores);
		result.use(Results.json()).withoutRoot().from(dtos).serialize();
	}


	/**
	 * Transforma a lista de identificadores de fornecedores em formato
	 * texto para a lista de identificadores em formato numérico 
	 * 
	 * @param idsFornecedores identificadores em formato texto
	 * @return lista de identificadores em formato numérico
	 */
	private List<Long> converterIdsFornecedores(String idsFornecedores) {
		List<Long> fornecedores = new ArrayList<Long>();
		for (String id : idsFornecedores.split(",")) {
			fornecedores.add(Long.valueOf(id));
		}
		return fornecedores;
	}

}
