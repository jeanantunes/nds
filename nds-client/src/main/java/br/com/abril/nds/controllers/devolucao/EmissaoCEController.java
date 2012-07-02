package br.com.abril.nds.controllers.devolucao;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.TipoMovimentoDTO;
import br.com.abril.nds.dto.TipoMovimentoDTO.IncideDivida;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoMovimento;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.TipoMovimentoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/emissaoCE")
public class EmissaoCEController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroEmissaoCE";
	
	@Autowired
	private HttpSession session;
			
	@Autowired
	private Result result;
	
	public void emissaoCE() {
		
	}
	
	/**
	 * Inicializa dados da tela
	 */
	public void index() {
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);
		
		result.forwardTo(EmissaoCEController.class).emissaoCE();
	}
		


	
	@Post
	public void pesquisar(FiltroMapaAbastecimentoDTO filtro, Integer page, Integer rp, String sortname, String sortorder) {
				
//		if(filtro.getTipoConsulta() == null)
//			throw new ValidacaoException(TipoMensagem.WARNING, " 'Tipo de consulta' deve ser selecionado.");
//				
//		if(filtro.getDataDate() == null && !filtro.getDataLancamento().isEmpty())
//			throw new ValidacaoException(TipoMensagem.WARNING, "'Data de Lançamento' não é válida.");
//		
//		if(filtro.getDataLancamento() == null || filtro.getDataLancamento().isEmpty())
//			throw new ValidacaoException(TipoMensagem.WARNING, "'Data de Lançamento' é obrigatória.");
//		
//		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
//		
//		tratarFiltro(filtro);
//		
//		List<AbastecimentoDTO> lista = mapaAbastecimentoService.obterDadosAbastecimento(filtro);
//		
//		Long totalRegistros = mapaAbastecimentoService.countObterDadosAbastecimento(filtro);
//
//		result.use(FlexiGridJson.class).from(lista).page(1).total(totalRegistros.intValue()).serialize();
	}
	
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroTipoMovimento filtroAtual) {

		FiltroTipoMovimento filtroSession = (FiltroTipoMovimento) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}
}
