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
import br.com.abril.nds.vo.PeriodoVO;
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
	public void matrizLancamento(Date data, Long[] idsFornecedores ) {
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO();
		PeriodoVO periodo = new PeriodoVO(new Date(), new Date());
		filtro.setPeriodo(periodo);
		
		List<LancamentoDTO> dtos = matrizLancamentoService.buscarLancamentosBalanceamento(filtro);
		TableModel<CellModelKeyValue<LancamentoDTO>> tm = new TableModel<CellModelKeyValue<LancamentoDTO>>();
		List<CellModelKeyValue<LancamentoDTO>> cells = CellModelKeyValue.toCellModelKeyValue(dtos);
		
		tm.setRows(cells);
		tm.setPage(1);
		tm.setTotal(10);
		result.use(Results.json()).withoutRoot().from(tm).include("rows").serialize();
	}
	
	@Get
	public void resumoPeriodo(Date data) {
		List<ResumoPeriodoLancamentoDTO> dtos = new ArrayList<ResumoPeriodoLancamentoDTO>();
		for (int i = 0; i < 6; i++) {
			ResumoPeriodoLancamentoDTO dto = new ResumoPeriodoLancamentoDTO();
			dto.setData(DateUtil.formatarData(data, FORMATO_DATA));
			dto.setPesoTotal("100");
			dto.setQtdeExemplares("1000");
			dto.setQtdeTitulos(5L);
			dto.setValorTotal("10.000,00");
			dtos.add(dto);
		}
		result.use(Results.json()).withoutRoot().from(dtos).serialize();
	}
	
	

}
