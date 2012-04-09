package br.com.abril.nds.controllers.expedicao;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO.ColunaOrdenacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.CotaAusenteService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
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
	private static final String WARNING_COTA_AUSENTE_DUPLICADA =  "Esta cota já foi declarada como ausente na data.";
	private static final String WARNING_CAMPO_DATA_OBRIGATORIO = "O campo \"Data\" é obrigatório.";
	private static final String WARNING_DATA_MAIOR_OPERACAO_ATUAL = "A data informada é inferior a data de operação atual.";
	private static final String WARNING_DATA_INFORMADA_INVALIDA = "A data informada é inválida.";
	private static final String WARNING_NUMERO_COTA_NAO_INFORMADO =  "O campo \"cota\" é obrigatório.";
	private static final String ERRO_ENVIO_SUPLEMENTAR = "Erro não esperado ao realizar envio de suplementar.";
	private static final String SUCESSO_ENVIO_SUPLEMENTAR = "Envio de suplementar realizado com sucesso.";
	@Autowired
	private CotaAusenteService cotaAusenteService;
	@Autowired
	private MovimentoEstoqueCotaService movimentoEstoqueCotaService;
	
	@Autowired
	
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
		
		//try {
			Date data = validaData(dataAusencia);
			
			boolean isDataOperacao = isDataOperacao(data);
					
			FiltroCotaAusenteDTO filtro = new FiltroCotaAusenteDTO(
					data, 
					(box==null || box.isEmpty()) ? null:box, 
					numCota, 
					new PaginacaoVO(page, rp, sortorder), 
					ColunaOrdenacao.valueOf(sortname));
					
			tratarFiltro(filtro);
			
			efetuarConsulta(filtro, isDataOperacao);
		//}
		
		//TODO WARNING_PESQUISA_SEM_RESULTADO
		
	}
	
	/**
	 * Efetua a consulta e monta a estrutura do grid de CotasAusentes.
	 * @param filtro
	 */
	private void efetuarConsulta(FiltroCotaAusenteDTO filtro, boolean isDataOperacao) {
		
		List<CotaAusenteDTO> listaCotasAusentes = null;
		
		try {
			listaCotasAusentes = cotaAusenteService.obterCotasAusentes(filtro) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (listaCotasAusentes == null || listaCotasAusentes.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		if(!isDataOperacao) {
			for(CotaAusenteDTO cotaAusenteDTO : listaCotasAusentes) {
				cotaAusenteDTO.setIdCotaAusente(null);
			}
		}
		
		Long totalRegistros = cotaAusenteService.obterCountCotasAusentes(filtro);
		
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
	
	@Post
	public void enviarParaSuplementar(Integer numCota) {
	
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();
		
		try {
			
			if(numCota == null) 
				throw new ValidacaoException(TipoMensagem.WARNING, WARNING_NUMERO_COTA_NAO_INFORMADO);
						
			cotaAusenteService.declararCotaAusente(numCota, new Date(), null, this.getUsuario().getId());
			
			mensagens.add(SUCESSO_ENVIO_SUPLEMENTAR);
			
		} catch(ValidacaoException e) {
			mensagens.clear();
			mensagens.addAll(e.getValidacao().getListaMensagens());
			status=TipoMensagem.WARNING;
		
		} catch(InvalidParameterException e) {
			mensagens.clear();
			mensagens.add(WARNING_COTA_AUSENTE_DUPLICADA);
			status=TipoMensagem.WARNING;			
		}catch(Exception e) {
			mensagens.clear();
			mensagens.add(ERRO_ENVIO_SUPLEMENTAR);
			status=TipoMensagem.ERROR;
			LOG.error(ERRO_ENVIO_SUPLEMENTAR, e);
		}
		
		Object[] retorno = new Object[2];
		retorno[0] = mensagens;
		retorno[1] = status.name();		
		
		result.use(Results.json()).from(retorno, "result").serialize();
	}

	@Post
	public void carregarDadosRateio(Integer numCota) {
		
		List<MovimentoEstoqueCotaDTO> movimentos = 
				movimentoEstoqueCotaService.obterMovimentoDTOCotaPorTipoMovimento(new Date(), numCota, GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		result.use(Results.json()).from(movimentos, "result").recursive().serialize();
	}
	
	@Post
	public void realizarRateio(List<MovimentoEstoqueCotaDTO> movimentos) {
		
		
	}
	


	//TODO getRealUsuario
	public Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setLogin("fakeUsuario");
		usuario.setNome("Fake Usuario");
		return usuario;
	}
}
