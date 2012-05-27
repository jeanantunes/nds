package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFornecedorDTO.ColunaOrdenacao;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
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
 * Controller para a tela de cadastro de fornecedores.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/cadastro/fornecedor")
public class FornecedorController {

	@Autowired
	private Result result;

	@Autowired
	private FornecedorService fornecedorService;

	@Path("/")
	public void index() { }

	/**
	 * 
	 * @param filtroConsultaFornecedor
	 */
	@Post
	public void pesquisarFornecedores(FiltroConsultaFornecedorDTO filtroConsultaFornecedor,
									  int page, int rp, String sortname, String sortorder) {
		
		filtroConsultaFornecedor = prepararFiltroFornecedor(filtroConsultaFornecedor, page, sortname, sortorder, rp);
		
		List<FornecedorDTO> listaFornecedores = 
				this.fornecedorService.obterFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		if (listaFornecedores == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		Long quantidadeRegistros =
				this.fornecedorService.obterContagemFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		TableModel<CellModelKeyValue<FornecedorDTO>> tableModel = obterTableModelFornecedores(listaFornecedores);
		
		tableModel.setTotal(quantidadeRegistros.intValue());
		
		tableModel.setPage(page);

		this.result.use(Results.json()).from(tableModel).recursive().serialize();
	}
	
	/*
	 * Método responsável por compor um TableModel para grid de fornecedores. 
	 */
	private TableModel<CellModelKeyValue<FornecedorDTO>> obterTableModelFornecedores(List<FornecedorDTO> listaFornecedores) {
		
		TableModel<CellModelKeyValue<FornecedorDTO>> tableModel = new TableModel<CellModelKeyValue<FornecedorDTO>>();
		
		List<CellModelKeyValue<FornecedorDTO>> listaCellModel = new ArrayList<CellModelKeyValue<FornecedorDTO>>();
		
		for (FornecedorDTO fornecedorDTO : listaFornecedores) {
			
			CellModelKeyValue<FornecedorDTO> cellModelKeyValue = new CellModelKeyValue<FornecedorDTO>(
				fornecedorDTO.getIdFornecedor().intValue(), 
				fornecedorDTO
			);

			listaCellModel.add(cellModelKeyValue);
		}
		
		tableModel.setRows(listaCellModel);
		
		return tableModel;
	}
	
	/*
	 * Método que prepara o filtro para utilização na pesquisa.
	 */
	private FiltroConsultaFornecedorDTO prepararFiltroFornecedor(
					FiltroConsultaFornecedorDTO filtroFornecedor, 
					int page, String sortname, String sortorder, int rp) {
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		
		filtroFornecedor.setPaginacao(paginacao);
		
		filtroFornecedor.setColunaOrdenacao(
				Util.getEnumByStringValue(ColunaOrdenacao.values(), sortname));

		return filtroFornecedor;
	}
}
