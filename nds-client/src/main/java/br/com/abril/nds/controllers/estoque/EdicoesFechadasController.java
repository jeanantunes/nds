package br.com.abril.nds.controllers.estoque;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.ResultadoEdicoesFechadasVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.filtro.FiltroEdicoesFechadasDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.EdicoesFechadasService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/estoque/edicoesFechadas")
public class EdicoesFechadasController {

	@Autowired
	private Result result;

	@Autowired
	private EdicoesFechadasService edicoesFechadasService;
	
	private static final String FORMATO_DATA = "dd/MM/yyyy";

	@Path("/")
	public void index() {
		String data = DateUtil.formatarData(new Date(), FORMATO_DATA);
		result.include("data", data);
	}

	/**
	 * Valida os dados enviados para a pesquisa
	 * @param dataDe
	 * @param dataAte
	 */
	private void validarDadosPesquisa(Date dataDe, Date dataAte) {
		List<String> listaMensagemValidacao = new ArrayList<String>();

		if (dataDe == null) {
			listaMensagemValidacao
					.add("O preenchimento do campo Período De é obrigatório!");
		}

		if (dataAte == null) {
			listaMensagemValidacao
					.add("O preenchimento do campo Período Até é obrigatório!");
		}

		if (!listaMensagemValidacao.isEmpty()) {
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR,
					listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}

	}
	
	/**
	 * Realiza a pesquisa de edições fechadas
	 * @param filtroEdicoesFechadasDTO
	 * @throws Exception
	 */
	@Post
	@Path("/pesquisar")
	public void pesquisarEdicoesFechadas(FiltroEdicoesFechadasDTO filtroEdicoesFechadasDTO) throws Exception {

		this.validarDadosPesquisa(filtroEdicoesFechadasDTO.getDateDe(), filtroEdicoesFechadasDTO.getDateAte());

		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);
		
		ResultadoEdicoesFechadasVO resultado = null;
		try {
			resultado = null; //edicoesFechadasService.obterResultadoEdicoesFechadas(sdf.parse(dataDe), sdf.parse(dataAte), codigoFornecedor);
		} catch (Exception e) {

			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar registros: " + e.getMessage());
			}
		}

		if (resultado == null) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Nenhum registro encontrado.");
		} else {

			/*TableModel<CellModelKeyValue<ResultadoEdicoesFechadasVO>> tableModel = new TableModel<CellModelKeyValue<ResultadoEdicoesFechadasVO>>();
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultado));
			tableModel.setPage(filtroCurvaABCDistribuidorDTO.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);
			resultado.setTableModel(tableModel);*/
			
			result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();

		}
		
	}

	@Post
	@Path("/verificarSaldo")
	public void verificarSaldo(String dataDe, String dataAte, String codigoEditor) throws Exception {
			
	}
	
}
