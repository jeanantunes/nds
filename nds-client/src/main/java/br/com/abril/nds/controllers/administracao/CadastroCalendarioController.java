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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CalendarioFeriadoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CalendarioService.TipoPesquisaFeriado;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/cadastroCalendario")
@Rules(Permissao.ROLE_ADMINISTRACAO_CALENDARIO)
public class CadastroCalendarioController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CadastroCalendarioController.class);
    
    @Autowired
    private CalendarioService calendarioService;
    
    @Autowired
    private Result result;
    
    private static final String FILTRO_PESQUISA = "filtroPesquisaCalendario";
    
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
        
        final List<String> tiposFeriado = new LinkedList<String>();
        
        tiposFeriado.add(TipoFeriado.FEDERAL.name());
        tiposFeriado.add(TipoFeriado.ESTADUAL.name());
        tiposFeriado.add(TipoFeriado.MUNICIPAL.name());
        
        result.include("tiposFeriado", tiposFeriado);
        
    }
    
    private void carregarComboMunicipio() {
        
        final List<String> listaLocalidade = calendarioService.obterListaLocalidadePdv();
        
        result.include("listaLocalidade", listaLocalidade);
        
    }
    
    private void validarCadastroFeriado(
            final String dtFeriado,
            final String descTipoFeriado,
            final String descricao,
            final String idLocalidade) {
        
        final List<String> msgErro = new ArrayList<String>();
        
        if (!DateUtil.isValidDate(dtFeriado, "dd/MM/yyyy")) {
            msgErro.add("Data do feriado inválida.");
        }
        
        if(descricao == null || descricao.isEmpty()) {
            msgErro.add("Nenhuma descrição para o feriado.");
        }
        
        TipoFeriado tipoFeriado = null;
        
        try {
            tipoFeriado = TipoFeriado.valueOf(descTipoFeriado);
        } catch(final Exception e) {
            LOGGER.debug(e.getMessage(), e);
            msgErro.add("Nenhuma tipo de feriado selecionado.");
        }
        
        if (tipoFeriado != null && TipoFeriado.MUNICIPAL.equals(tipoFeriado) && idLocalidade == null) {
            msgErro.add("Nenhum município associado ao feriado Municipal.");
            
        }
        
        if(!msgErro.isEmpty()) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgErro));
        }
    }
    
    @Rules(Permissao.ROLE_ADMINISTRACAO_CALENDARIO_ALTERACAO)
    public void excluirCadastroFeriado(final Long idFeriado) {
        
        calendarioService.excluirFeriado(idFeriado);
        result.use(Results.json()).from("Feriado excluído com sucesso").serialize();
    }
    
    @Rules(Permissao.ROLE_ADMINISTRACAO_CALENDARIO_ALTERACAO)
    public void cadastrarFeriado(
            final Long idFeriado,
            final String dtFeriado,
            final String descTipoFeriado,
            final String descricao,
            final String idLocalidade,
            final boolean indOpera,
            final boolean indEfetuaCobranca,
            final boolean indRepeteAnualmente
            ){
        
        validarCadastroFeriado(dtFeriado, descTipoFeriado, descricao, idLocalidade);
        
        final CalendarioFeriadoDTO calendarioFeriado = new CalendarioFeriadoDTO();
        
        calendarioFeriado.setIdFeriado(idFeriado);
        calendarioFeriado.setDataFeriado(DateUtil.parseDataPTBR(dtFeriado));
        calendarioFeriado.setTipoFeriado(TipoFeriado.valueOf(descTipoFeriado));
        calendarioFeriado.setDescricaoFeriado(descricao);
        
        calendarioFeriado.setIndOpera(indOpera);
        calendarioFeriado.setIndEfetuaCobranca(indEfetuaCobranca);
        calendarioFeriado.setIndRepeteAnualmente(indRepeteAnualmente);
        
        calendarioFeriado.setLocalidade(idLocalidade);
        
        calendarioService.cadastrarFeriado(calendarioFeriado);
        
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Novo feriado gravado com sucesso"), "result").recursive().serialize();
        
    }
    
    public void obterListaCalendarioFeriado(final String data) {
        
        validarFormatoDataRecolhimento(data);
        
        final List<CalendarioFeriadoDTO> listaCalendarioFeriado =  calendarioService.obterListaCalendarioFeriadoDataEspecifica(DateUtil.parseDataPTBR(data));
        
        result.use(FlexiGridJson.class).from(listaCalendarioFeriado).total(listaCalendarioFeriado.size()).page(1).serialize();
        
    }
    
    
    private void validarFormatoDataRecolhimento(final String dataRecolhimento){
        
        if (!DateUtil.isValidDate(dataRecolhimento, "dd/MM/yyyy")) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Data inválida!");
        }
    }
    
    public void obterFeriadosDoMes(final int mes) {
        
        final FiltroCalendarioFeriado filtro = (FiltroCalendarioFeriado) session.getAttribute(FILTRO_PESQUISA);
        
        if(filtro == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Dados da pesquisa inválidos.");
        }
        
        filtro.setMesFeriado(mes);
        
        session.setAttribute(FILTRO_PESQUISA, filtro);
        
        final List<CalendarioFeriadoDTO> listaCalendarioFeriado = calendarioService.obterListaCalendarioFeriadoMensal(filtro.getMesFeriado(), filtro.getAnoFeriado());
        
        result.use(FlexiGridJson.class).from(listaCalendarioFeriado).total(listaCalendarioFeriado.size()).page(1).serialize();
        
    }
    
    public void gerarRelatorioCalendario(final FileType fileType, final TipoPesquisaFeriado tipoPesquisaFeriado) throws IOException {
        
        final FiltroCalendarioFeriado filtroCalendario = (FiltroCalendarioFeriado) session.getAttribute(FILTRO_PESQUISA);
        
        if (filtroCalendario == null) {
            result.redirectTo("index");
            return;
        }
        
        final byte[] relatorio
        = calendarioService.obterRelatorioCalendarioFeriado(fileType, tipoPesquisaFeriado,
                filtroCalendario.getMesFeriado(),
                filtroCalendario.getAnoFeriado(),getLogoDistribuidor());
        
        escreverArquivoParaResponse(relatorio, "relatorio-feriado", fileType);
        
    }
    
    private void escreverArquivoParaResponse(final byte[] arquivo, final String nomeArquivo, final FileType fileType) throws IOException {
        
        response.setContentType(fileType.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename="+nomeArquivo +fileType.getExtension());
        
        final OutputStream output = response.getOutputStream();
        
        output.write(arquivo);
        
        response.getOutputStream().close();
        
        result.use(Results.nothing());
        
    }
    
    public void exportarArquivo(final FileType fileType, final TipoPesquisaFeriado tipoPesquisaFeriado) {
        
        final FiltroCalendarioFeriado filtro = (FiltroCalendarioFeriado) session.getAttribute(FILTRO_PESQUISA);
        
        if (filtro == null) {
            result.redirectTo("index");
            return;
        }
        
        List<CalendarioFeriadoDTO> listaFeriados;
        
        if (tipoPesquisaFeriado.equals(TipoPesquisaFeriado.FERIADO_MENSAL)) {
            listaFeriados =
                    calendarioService.obterListaCalendarioFeriadoMensal(filtro.getMesFeriado(),
                            filtro.getAnoFeriado());
        } else {
            
            listaFeriados = calendarioService.obterFeriadosPorAno(filtro.getAnoFeriado());
        }
        
        if (listaFeriados != null && !listaFeriados.isEmpty()) {
            
            try {
                
                FileExporter.to("relatorio-feriado", fileType).inHTTPResponse(
                        this.getNDSFileHeader(), null, null, listaFeriados,
                        CalendarioFeriadoDTO.class, response);
                
            } catch (final Exception e) {
                final String msg = "Erro ao gerar o arquivo!";
                LOGGER.error(msg, e);
                throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, msg));
            }
        }
        
        result.use(Results.nothing());
    }
    
    public void obterFeriados(int anoVigencia) {
        final int ano;
        if(anoVigencia == 0) {
            anoVigencia = getAnoCorrente();
        }
        
        FiltroCalendarioFeriado filtro = (FiltroCalendarioFeriado) session.getAttribute(FILTRO_PESQUISA);
        
        if (filtro == null) {
            filtro = new FiltroCalendarioFeriado();
            session.setAttribute(FILTRO_PESQUISA, filtro);
        }
        
        filtro.setAnoFeriado(anoVigencia);
        
        final Map<Date, String> mapaFeriados = calendarioService.obterListaDataFeriado(filtro.getAnoFeriado());
        
        final Map<String, Object> resposta = new HashMap<String, Object>();
        
        resposta.put("datasDestacar", mapaFeriados);
        
        resposta.put("anoVigencia", Integer.valueOf(anoVigencia));
        
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
    static class FiltroCalendarioFeriado {
        
        private int anoFeriado;
        
        private int mesFeriado;
        
        private Date dataFeriado;
        
        public int getAnoFeriado() {
            return anoFeriado;
        }
        
        public void setAnoFeriado(final int anoFeriado) {
            this.anoFeriado = anoFeriado;
        }
        
        public int getMesFeriado() {
            return mesFeriado;
        }
        
        public void setMesFeriado(final int mesFeriado) {
            this.mesFeriado = mesFeriado;
        }
        
        public Date getDataFeriado() {
            return dataFeriado;
        }
        
        public void setDataFeriado(final Date dataFeriado) {
            this.dataFeriado = dataFeriado;
        }
        
        
    }
    
    
}
