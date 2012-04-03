package br.com.abril.nds.controllers.expedicao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import br.com.abril.nds.client.vo.DividaGeradaVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO.ColunaOrdenacao;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.repository.CotaAusenteRepository;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class CotaAusenteController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroCotaAusente";
	
	private static final String WARNING_PESQUISA_SEM_RESULTADO = "Não há resultados para a pesquisa realizada.";
	private static final String WARNING_CAMPO_DATA_OBRIGATORIO = "O campo \"Data\" é obrigatório.";
	private static final String WARNING_DATA_MAIOR_OPERACAO_ATUAL = "A data informada é inferior a data de operação atual.";
	private static final String WARNING_DATA_INFORMADA_INVALIDA = "A data informada é inválida.";
	
	
	@Autowired
	private CotaAusenteRepository cotaAusenteRepository;
	
	
	private static final Logger LOG = LoggerFactory
			.getLogger(CotaAusenteController.class);
	
	private final Result result;
	private final HttpSession session;
	
	public CotaAusenteController(Result result, HttpSession session) {
		this.result=result;
		this.session = session;
	}
	
	public void cotaAusente() {
		
	}
	
	/**
	 * Inicializa dados da tela
	 */
	public void index() {
		
		result.forwardTo(CotaAusenteController.class).cotaAusente();
	}
	
	/**
	 * Realiza pesquisa de Cotas Ausentes
	 * 
	 * @param dataAusente
	 * @param numCota
	 * @param nomeCota
	 * @param box
	 */
	@Post
	public void pesquisarCotasAusentes(String dataAusencia, Integer numCota, 
			String box,String sortorder, String sortname, int page, int rp) {
		
		Date data = validaData(dataAusencia);
		
		boolean isDataOperacao = isDataOperacao(data);
				
		FiltroCotaAusenteDTO filtro = new FiltroCotaAusenteDTO(
				data, 
				(box==null || box.isEmpty()) ? null:box, 
				numCota, 
				new PaginacaoVO(page, rp, sortorder), 
				ColunaOrdenacao.valueOf(sortname));
				
		tratarFiltro(filtro);
		
		efetuarConsulta(filtro);
		
	}
	
	/**
	 * Efetua a consulta e monta a estrutura do grid de CotasAusentes.
	 * @param filtro
	 */
	private void efetuarConsulta(FiltroCotaAusenteDTO filtro) {
		
		List<CotaAusenteDTO> listaCotasAusentes = cotaAusenteRepository.obterCotasAusentes(filtro) ;
		
		
		if (listaCotasAusentes == null || listaCotasAusentes.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		Long totalRegistros = cotaAusenteRepository.obterCountCotasAusentes(filtro);
		
		TableModel<CellModelKeyValue<CotaAusenteDTO>> tableModel = new TableModel<CellModelKeyValue<CotaAusenteDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasAusentes));
		
		tableModel.setPage(1);
		
		tableModel.setTotal( (totalRegistros == null)?0:totalRegistros.intValue());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}

	private boolean isDataOperacao(Date data) {
		
		Long dia = 86400000L;
		
		if( (data.getTime()/dia) == (new Date().getTime()/dia) )
			return true;
		
		return false;
	}

	private Date validaData(String dataAusencia) {

		if ( dataAusencia == null || dataAusencia.isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, WARNING_CAMPO_DATA_OBRIGATORIO );
		
		Date data = null;
		
		try {
			data = DateUtil.parseDataPTBR(dataAusencia);
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.WARNING, WARNING_DATA_INFORMADA_INVALIDA);
		}

		if ( data.getTime() > (new Date().getTime()) )
			throw new ValidacaoException(TipoMensagem.WARNING, WARNING_DATA_MAIOR_OPERACAO_ATUAL );
		
		return data;
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroCotaAusenteDTO filtro) {

		FiltroCotaAusenteDTO filtroSession = (FiltroCotaAusenteDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) {

			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	public void cancelarCotaAusente(Long idCotaAusente) {
		System.out.println("ID_COTA_AUSENTE" + idCotaAusente);
	}
		
	@Post
	public void gerarNovaCotaAusente(Integer numCota) {
		List<Integer> lista = new ArrayList<Integer>();
		lista.add(numCota);
		result.use(Results.json()).from(lista, "result").serialize();
	}
}
