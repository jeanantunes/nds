package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.CalendarioFeriadoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CalendarioService.TipoPesquisaFeriado;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/cadastroCalendario")
public class CadastroCalendarioController {
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private Result result;
	
	private final String FILTRO_PESQUISA = "filtroPesquisaCalendario";
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse response;

	
	public CadastroCalendarioController() {
		
	}
	
	@Path("/")
	public void index(){
		
		adicionarAnoCorrentePesquisa();
		
		carregarComboTipoFeriado();
		
		carregarComboMunicipio();
		
	}
	
	private void carregarComboTipoFeriado() {
		
		List<String> tiposFeriado = new LinkedList<String>();
		
		tiposFeriado.add(TipoFeriado.FEDERAL.name());
		tiposFeriado.add(TipoFeriado.ESTADUAL.name());
		tiposFeriado.add(TipoFeriado.MUNICIPAL.name());
		
		this.result.include("tiposFeriado", tiposFeriado);
		
	}
	
	private void carregarComboMunicipio() {
		
		List<Localidade> listaLocalidade = calendarioService.obterListaLocalidadeCotas();
		
		this.result.include("listaLocalidade", listaLocalidade);
		
	}
	
	private void validarCadastroFeriado(
			String dtFeriado, 
			String descTipoFeriado, 
			String descricao,
			Long idLocalidade) {
		
		List<String> msgErro = new ArrayList<String>();
		
		if (!DateUtil.isValidDate(dtFeriado, "dd/MM/yyyy")) {
			msgErro.add("Data do feriado inválida.");
		} 
		
		if(descricao == null || descricao.isEmpty()) {
			msgErro.add("Nenhuma descrição para o feriado.");
		}
		
		TipoFeriado tipoFeriado = null;
		
		try {
			tipoFeriado = TipoFeriado.valueOf(descTipoFeriado);
		} catch(Exception e) {
			msgErro.add("Nenhuma tipo de feriado selecionado.");
		}
		
		if(tipoFeriado != null && TipoFeriado.MUNICIPAL.equals(tipoFeriado)) {
			if(idLocalidade == null || idLocalidade < 0) {
				msgErro.add("Nenhum município associado ao feriado Municipal.");
			}
		}
		
		if(!msgErro.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgErro));
		}
		
	}

	public void excluirCadastroFeriado(
			String dtFeriado, 
			String descTipoFeriado, 
			Long idLocalidade,
			boolean indRepeteAnualmente) {
		
		validarCadastroFeriado(dtFeriado, descTipoFeriado, "-", idLocalidade);
		
		CalendarioFeriadoDTO calendarioFeriado = new CalendarioFeriadoDTO();
		
		calendarioFeriado.setDataFeriado(DateUtil.parseDataPTBR(dtFeriado));
		calendarioFeriado.setTipoFeriado(TipoFeriado.valueOf(descTipoFeriado));
		calendarioFeriado.setIdLocalidade(idLocalidade);
		calendarioFeriado.setIndRepeteAnualmente(indRepeteAnualmente);
		
		calendarioService.excluirFeriado(calendarioFeriado);
		
		result.use(Results.json()).from("Feriado excluído com sucesso").serialize();
		
	}

	
	public void cadastrarFeriado(			
			String dtFeriado, 
			String descTipoFeriado, 
			String descricao,
			Long idLocalidade,
			boolean indOpera, 
			boolean indEfetuaCobranca,
			boolean indRepeteAnualmente			
			){
		
		validarCadastroFeriado(dtFeriado, descTipoFeriado, descricao, idLocalidade);
		
		CalendarioFeriadoDTO calendarioFeriado = new CalendarioFeriadoDTO();
		
		calendarioFeriado.setDataFeriado(DateUtil.parseDataPTBR(dtFeriado));
		calendarioFeriado.setTipoFeriado(TipoFeriado.valueOf(descTipoFeriado));
		calendarioFeriado.setDescricaoFeriado(descricao);
		
		calendarioFeriado.setIndOpera(indOpera);
		calendarioFeriado.setIndEfetuaCobranca(indEfetuaCobranca);
		calendarioFeriado.setIndRepeteAnualmente(indRepeteAnualmente);

		calendarioFeriado.setIdLocalidade(idLocalidade);
		
		calendarioService.cadastrarFeriado(calendarioFeriado);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Novo feriado gravado com sucesso"), "result").recursive().serialize();
		
	}
	
	public void obterListaCalendarioFeriado(String data) {
		
		validarFormatoDataRecolhimento(data);
		
		List<CalendarioFeriadoDTO> listaCalendarioFeriado =  calendarioService.obterListaCalendarioFeriadoDataEspecifica(DateUtil.parseDataPTBR(data));
		
		result.use(FlexiGridJson.class).from(listaCalendarioFeriado).total(listaCalendarioFeriado.size()).page(1).serialize();
	
	}
	
	
	private void validarFormatoDataRecolhimento(String dataRecolhimento){
		
		if (!DateUtil.isValidDate(dataRecolhimento, "dd/MM/yyyy")) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Data inválida!");
			
		} 
		
	}
	
	public void obterFeriadosDoMes(int mes) {
		
		FiltroCalendarioFeriado filtro = (FiltroCalendarioFeriado) session.getAttribute(FILTRO_PESQUISA);
		
		if(filtro == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Dados da pesquisa inválidos.");
		}
		
		filtro.setMesFeriado(mes);
		
		session.setAttribute(FILTRO_PESQUISA, filtro);
		
		List<CalendarioFeriadoDTO> listaCalendarioFeriado = calendarioService.obterListaCalendarioFeriadoMensal(filtro.getMesFeriado(), filtro.getAnoFeriado());

		result.use(FlexiGridJson.class).from(listaCalendarioFeriado).total(listaCalendarioFeriado.size()).page(1).serialize();
		
	}
	
	public void gerarRelatorioCalendario(FileType fileType, TipoPesquisaFeriado tipoPesquisaFeriado) throws IOException {
		
		FiltroCalendarioFeriado filtroCalendario = (FiltroCalendarioFeriado) this.session.getAttribute(FILTRO_PESQUISA);
		
		if (filtroCalendario == null) {
			result.redirectTo("index");
			return;
		}
		
		byte[] relatorio = calendarioService.obterRelatorioCalendarioFeriado(fileType, tipoPesquisaFeriado, filtroCalendario.getMesFeriado(), filtroCalendario.getAnoFeriado());
		
		escreverArquivoParaResponse(relatorio, "relatorio-feriado");
		
	}
	
	private void escreverArquivoParaResponse(byte[] arquivo, String nomeArquivo) throws IOException {
		
		this.response.setContentType("application/pdf");
		
		this.response.setHeader("Content-Disposition", "attachment; filename="+nomeArquivo +".pdf");

		OutputStream output = this.response.getOutputStream();
		
		output.write(arquivo);

		response.getOutputStream().close();
		
		result.use(Results.nothing());
		
	}
	
	public void obterFeriados(int anoVigencia) {
		
		if(anoVigencia == 0) {
			anoVigencia = getAnoCorrente();
		}
		
		FiltroCalendarioFeriado filtro = (FiltroCalendarioFeriado) this.session.getAttribute(FILTRO_PESQUISA);
		
		if (filtro == null) {
			filtro = new FiltroCalendarioFeriado();
			filtro.setAnoFeriado(anoVigencia);
			session.setAttribute(FILTRO_PESQUISA, filtro);
		}
		
		Map<Date, String> mapaFeriados = calendarioService.obterListaDataFeriado(filtro.getAnoFeriado());
		
		Map<String, Object> resposta = new HashMap<String, Object>();

		resposta.put("datasDestacar", mapaFeriados);
		
		resposta.put("anoVigencia", new Integer(anoVigencia));
		
		result.use(CustomJson.class).from(resposta).serialize();
	
	}
	
	private void adicionarAnoCorrentePesquisa() {
		result.include("anoCorrente", getAnoCorrente());
	}

	private Integer getAnoCorrente() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	/**
	 * Filtro da pesquisa dos feriados.
	 */
	class FiltroCalendarioFeriado {
		
		private int anoFeriado;
		
		private int mesFeriado;
		
		private Date dataFeriado;

		public int getAnoFeriado() {
			return anoFeriado;
		}

		public void setAnoFeriado(int anoFeriado) {
			this.anoFeriado = anoFeriado;
		}

		public int getMesFeriado() {
			return mesFeriado;
		}

		public void setMesFeriado(int mesFeriado) {
			this.mesFeriado = mesFeriado;
		}

		public Date getDataFeriado() {
			return dataFeriado;
		}

		public void setDataFeriado(Date dataFeriado) {
			this.dataFeriado = dataFeriado;
		}
		
		
	}
	

}
