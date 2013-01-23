package br.com.abril.nds.controllers.distribuicao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AreaInfluenciaGeradorFluxoDTO;
import br.com.abril.nds.dto.CotaNaoRecebeSegmentoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.SegmentoNaoRecebidoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Path("/distribuicao/segmentoNaoRecebido")
@Resource
public class SegmentoNaoRecebidoController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private SegmentoNaoRecebidoService segmentoNaoRecebidoService;
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_SEGMENTO_NAO_RECEBIDO)
	public void index(){

		// POPULANDO FILTROS
		this.carregarComboSegmento();
	}

	@Post("/pesquisarCotasNaoRecebemSegmento")
	public void pesquisarCotasNaoRecebemSegmento(FiltroSegmentoNaoRecebidoDTO filtro, String sortorder, String sortname, int page, int rp){
	
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		List<CotaNaoRecebeSegmentoDTO> listaCotaNaoRecebeSegmentoDTO = segmentoNaoRecebidoService.obterCotasNaoRecebemSegmento(filtro);
		
		TableModel<CellModelKeyValue<CotaNaoRecebeSegmentoDTO>> tableModel = montarTableModelCotasNaoRecebemSegmento(filtro, listaCotaNaoRecebeSegmentoDTO);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<CotaNaoRecebeSegmentoDTO>> montarTableModelCotasNaoRecebemSegmento(
			FiltroSegmentoNaoRecebidoDTO filtro, 
			List<CotaNaoRecebeSegmentoDTO> listaCotaNaoRecebeSegmentoDTO) {
		
		TableModel<CellModelKeyValue<CotaNaoRecebeSegmentoDTO>> tableModel = new TableModel<CellModelKeyValue<CotaNaoRecebeSegmentoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotaNaoRecebeSegmentoDTO));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(10);
		
		return tableModel;
	}

	private void carregarComboSegmento() {

		List<TipoSegmentoProduto> listaTipoSegmentoProduto = segmentoNaoRecebidoService.obterTipoSegmentoProduto();

		// Lista usada para popular o combobox
		List<ItemDTO<Long, String>> listaTipoSegmentoProdutoCombobox = new ArrayList<ItemDTO<Long, String>>();

		for (TipoSegmentoProduto tipoSegmentoProduto : listaTipoSegmentoProduto) {

			// Preenchendo a lista que irá representar o combobox TipoSegmentoProduto
			listaTipoSegmentoProdutoCombobox.add(new ItemDTO<Long, String>(tipoSegmentoProduto.getId(), tipoSegmentoProduto.getDescricao()));
		}

		result.include("listaTipoSegmentoProduto", listaTipoSegmentoProdutoCombobox);
	}
	
}
