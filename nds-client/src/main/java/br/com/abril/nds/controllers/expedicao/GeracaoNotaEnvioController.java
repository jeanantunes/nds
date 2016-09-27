package br.com.abril.nds.controllers.expedicao;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.GeracaoNotaEnvioService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.NFeService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/expedicao/geracaoNotaEnvio")
@Rules(Permissao.ROLE_EXPEDICAO_GERACAO_NOTA_ENVIO)
public class GeracaoNotaEnvioController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeracaoNotaEnvioController.class); 

    @Autowired
    private Result result;

    @Autowired
    private DistribuidorService distribuidorService;

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private GeracaoNotaEnvioService geracaoNotaEnvioService;

    @Autowired
    private RoteirizacaoService roteirizacaoService;

    @Autowired
    private MovimentoEstoqueCotaService movimentoEstoqueCotaService;

    @Autowired
    private NFeService nfeService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletResponse httpResponse;
    
    private static final String FILTRO_CONSULTA_NOTA_ENVIO = "filtroConsultaNotaEnvio";

    private static final String COTAS_ID = "cotasId";

    @Path("/")
    public void index() {

        result.include("fornecedores",  fornecedorService.obterFornecedoresIdNome(SituacaoCadastro.ATIVO, true));

        this.iniciarComboBox();

        this.iniciarComboRota();

        this.iniciarComboRoteiro();

    }

    /**
     * Inicia o combo Roteiro
     */
    private void iniciarComboRoteiro() {
    	
        result.include("roteiros", this.roteirizacaoService.getComboTodosRoteiros());
    }

    /**
     * Inicia o combo Rota
     */
    private void iniciarComboRota() {

        result.include("rotas", this.roteirizacaoService.getComboTodosRotas());
    }

    /**
     * Inicia o combo Box
     */
    private void iniciarComboBox() {

        result.include("listaBox", this.roteirizacaoService.getComboTodosBoxes());
    }

    @Post
    public void obterDataDistribuidor(){

    	this.result.use(Results.json()).from(DateUtil.formatarDataPTBR(this.distribuidorService.obterDataOperacaoDistribuidor()), "result").recursive().serialize();
    }

    @Post
    public void pesquisar(Integer intervaloBoxDe, Integer intervaloBoxAte,
        Integer intervaloCotaDe, Integer intervaloCotaAte,
        Date intervaloMovimentoDe, Date intervaloMovimentoAte, Date dataEmissao,
        List<Long> listaIdFornecedores, Long idRoteiro, Long idRota, String exibirNotasEnvio,
        String sortname, String sortorder, int rp, int page) {

    	if(listaIdFornecedores==null || listaIdFornecedores.isEmpty())
    	    throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum fornecedor foi selecionado.");
    
    	FiltroConsultaNotaEnvioDTO filtro = this.setFiltroNotaEnvioSessao(intervaloBoxDe, intervaloBoxAte, intervaloCotaDe, 
    			intervaloCotaAte, intervaloMovimentoDe, intervaloMovimentoAte, dataEmissao, 
    			listaIdFornecedores, idRoteiro, idRota, exibirNotasEnvio, sortname, sortorder, rp, page);
    
    	Integer qtdResult = geracaoNotaEnvioService.buscaCotasNotasDeEnvioQtd(filtro);
    	
    	if (qtdResult == null || qtdResult == 0){
    	    throw new ValidacaoException(TipoMensagem.WARNING, "Não existem notas a serem geradas para o filtro escolhido.");
    	}
    
    	List<ConsultaNotaEnvioDTO> listaCotaExemplares = this.geracaoNotaEnvioService.busca(filtro);

	    result.use(FlexiGridJson.class).from(listaCotaExemplares).page(page).total(qtdResult).serialize();
    }

    @Post
    public void hasCotasAusentes() {

		boolean hasCotasAusentes = false;
	
		FiltroConsultaNotaEnvioDTO filtro = this.getFiltroNotaEnvioSessao();
	
		filtro.setCadastro(SituacaoCadastro.SUSPENSO);
		filtro.setPaginacaoVO(new PaginacaoVO());
	
		List<ConsultaNotaEnvioDTO> cotasAusentes = geracaoNotaEnvioService.busca(filtro);
	
		if (cotasAusentes != null && !cotasAusentes.isEmpty())
		    hasCotasAusentes = true;
	
		result.use(CustomJson.class).from(hasCotasAusentes).serialize();

    }

    @Post
    public void exportar(FileType fileType) throws IOException {

        FiltroConsultaNotaEnvioDTO filtro = this.getFiltroNotaEnvioSessao();

        filtro.getPaginacaoVO().setPaginaAtual(null);
        filtro.getPaginacaoVO().setQtdResultadosPorPagina(null);

        List<ConsultaNotaEnvioDTO> consultaNotaEnvioDTO = geracaoNotaEnvioService.busca(filtro);

        FileExporter.to("nota-envio", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, consultaNotaEnvioDTO, ConsultaNotaEnvioDTO.class,this.httpServletResponse);

        result.use(Results.nothing());

    }

    @Post
    public void transferirSuplementar(List<Long> listaIdCotas) {

        ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = this.distribuidorService.parametrosRecolhimentoDistribuidor();

        FiltroConsultaNotaEnvioDTO filtro = this.getFiltroNotaEnvioSessao();

        this.movimentoEstoqueCotaService.transferirReparteParaSuplementar(parametrosRecolhimentoDistribuidor, listaIdCotas, filtro.getIntervaloMovimento(), filtro.getIdFornecedores(), null, null);

        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Transferência de reparte para suplementar realizada com sucesso."), Constantes.PARAM_MSGS).recursive().serialize();

    }

    @Post
    @Rules(Permissao.ROLE_EXPEDICAO_GERACAO_NOTA_ENVIO_ALTERACAO)
    public void gerarNotaEnvio(List<Long> listaIdCotas) {

		session.setAttribute(COTAS_ID, listaIdCotas);
	
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Geração de NE."), Constantes.PARAM_MSGS).recursive().serialize();

    }

    private byte[] getNotas() throws Exception{

		FiltroConsultaNotaEnvioDTO filtro = this.getFiltroNotaEnvioSessao();
		
		filtro.setImpressao(true);
		filtro.setDistribEnviaEmail(this.distribuidorService.verificarParametroDistribuidorEmissaoDocumentosEmailCheck(null, TipoParametrosDistribuidorEmissaoDocumento.NOTA_ENVIO));
	
		List<NotaEnvio> notasEnvio = this.geracaoNotaEnvioService.gerarNotasEnvio(filtro);
	
		if(notasEnvio == null || notasEnvio.size() < 1) {
	
			filtro.setValidacaoVO(new ValidacaoVO(TipoMensagem.WARNING, "Não foram encontrado itens para exportar"));
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não foram encontrado itens para exportar"));
		}
	
		byte[] notasGeradas = null;
		try {
			notasGeradas = nfeService.obterNEsPDF(notasEnvio, false, filtro.getIntervaloMovimento());
		} catch(Exception e) {
			LOGGER.error("Erro ao gerar Nota de Envio.", e);
			if(e != null && e.getLocalizedMessage() != null) {
				throw new ValidacaoException(TipoMensagem.ERROR, e.getLocalizedMessage());
			} else if(e != null && e.getMessage() != null) {
				filtro.setValidacaoVO(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
				throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage());
			} else {
				throw e;
			}
		}
	
		return notasGeradas;

    }
    
    @Post
    public void getErrorGetArquivos(){
    	FiltroConsultaNotaEnvioDTO filtro = this.getFiltroNotaEnvioSessao();

    	ValidacaoVO validacao = filtro.getValidacaoVO() != null ? filtro.getValidacaoVO() : new ValidacaoVO(TipoMensagem.ERROR, "Erro ao emitir NE");
    	
    	throw new ValidacaoException(validacao);
    	
    }

    @Post
    public void getArquivoNotaEnvio() {

    	FiltroConsultaNotaEnvioDTO filtro = this.getFiltroNotaEnvioSessao();
    	
    	 filtro.getPaginacaoVO().setPaginaAtual(null);
         filtro.getPaginacaoVO().setQtdResultadosPorPagina(null);
         
        try {
            
            byte[] notasGeradas = this.getNotas();
    
            if (notasGeradas != null) {
            	
            	if(!distribuidorService.verificarParametroDistribuidorEmissaoDocumentosImpressaoCheck(null, TipoParametrosDistribuidorEmissaoDocumento.NOTA_ENVIO)){
             		filtro.setValidacaoVO(new ValidacaoVO(TipoMensagem.ERROR, "Notas de envio não podem ser impressas, distribuidor não aceita impressão deste documento."));
             		throw new ValidacaoException(TipoMensagem.ERROR, "Notas de envio não podem ser impressas, distribuidor não aceita impressão deste documento.");
             	}
    
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-ddhhmmss");
        
                this.httpResponse.setHeader("Content-Disposition", "attachment; filename=notas-envio" + sdf.format(new Date()) + ".pdf");
        
                OutputStream output;
        
                output = this.httpResponse.getOutputStream();
        
                output.write(notasGeradas);
        
                httpResponse.getOutputStream().close();
        
                session.setAttribute(COTAS_ID, null);
        
                result.use(Results.nothing());
            }
        } catch (ValidacaoException e) {
            LOGGER.error("Erro de validação ao gerar arquivos de notas de envio: " + e.getMessage(), e);
            filtro.setValidacaoVO(e.getValidacao());
            result.use(Results.json()).from(e.getValidacao(), Constantes.PARAM_MSGS).recursive().serialize();
        } catch (Exception e) {
            LOGGER.error("Erro genérico ao gerar arquivos de notas de envio: " + e.getMessage(), e);
            filtro.setValidacaoVO(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
            result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()),Constantes.PARAM_MSGS).recursive().serialize();
        }
    }

    /**
     * Obtem o filtro da sessão
     * 
     * @return filtro
     */
    private FiltroConsultaNotaEnvioDTO getFiltroNotaEnvioSessao() {

        FiltroConsultaNotaEnvioDTO filtro = (FiltroConsultaNotaEnvioDTO) this.session.getAttribute(FILTRO_CONSULTA_NOTA_ENVIO);

        if (filtro == null) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não foram encontrado itens para exportar"));
        }

        return filtro;

    }


    private FiltroConsultaNotaEnvioDTO setFiltroNotaEnvioSessao(Integer intervaloBoxDe, Integer intervaloBoxAte,
        Integer intervaloCotaDe, Integer intervaloCotaAte,
        Date intervaloMovimentoDe, Date intervaloMovimentoAte, Date dataEmissao,
        List<Long> listaIdFornecedores, Long idRoteiro, Long idRota, String exibirNotasEnvio,
        String sortname, String sortorder, int rp, int page) {

        Intervalo<Integer> intervaloBox = new Intervalo<Integer>(intervaloBoxDe, intervaloBoxAte);

        Intervalo<Integer> intervaloCota = new Intervalo<Integer>(intervaloCotaDe, intervaloCotaAte);

        Intervalo<Date> intervaloDateMovimento = new Intervalo<Date>(intervaloMovimentoDe, intervaloMovimentoAte);

        PaginacaoVO paginacao = new PaginacaoVO();

        paginacao.setOrdenacao(Ordenacao.ASC);
        paginacao.setPaginaAtual(page);
        paginacao.setQtdResultadosPorPagina(rp);
        paginacao.setSortOrder(sortorder);
        paginacao.setSortColumn(sortname);

        FiltroConsultaNotaEnvioDTO filtroConsultaNotaEnvioDTO = new FiltroConsultaNotaEnvioDTO();

        filtroConsultaNotaEnvioDTO.setDataEmissao(dataEmissao);
        filtroConsultaNotaEnvioDTO.setIdFornecedores(listaIdFornecedores);
        filtroConsultaNotaEnvioDTO.setIdRota(idRota);
        filtroConsultaNotaEnvioDTO.setIdRoteiro(idRoteiro);
        filtroConsultaNotaEnvioDTO.setIntervaloBox(intervaloBox);
        filtroConsultaNotaEnvioDTO.setIntervaloCota(intervaloCota);
        filtroConsultaNotaEnvioDTO.setIntervaloMovimento(intervaloDateMovimento);
        filtroConsultaNotaEnvioDTO.setExibirNotasEnvio(exibirNotasEnvio);
        filtroConsultaNotaEnvioDTO.setPaginacaoVO(paginacao);

        session.setAttribute(FILTRO_CONSULTA_NOTA_ENVIO, filtroConsultaNotaEnvioDTO);

        return filtroConsultaNotaEnvioDTO;

    }

    @Get
    public void visualizarNE() {

        FiltroConsultaNotaEnvioDTO filtro = this.getFiltroNotaEnvioSessao();

        List<NotaEnvio> notaEnvio = null;

        if(filtro.getIntervaloCota().getDe() != null) {
            notaEnvio = geracaoNotaEnvioService.visualizar(filtro);

        } else {
            result.include("errorMessage", "É necessário informar o número da Cota.");
        }

        String dataRecolhimento = null;

    	if (filtro.getIntervaloMovimento().getDe().equals(filtro.getIntervaloMovimento().getAte())) {
    		dataRecolhimento =  DateUtil.formatarDataPTBR(filtro.getIntervaloMovimento().getDe());
    	} else {
    		dataRecolhimento =  "De "  + DateUtil.formatarDataPTBR(filtro.getIntervaloMovimento().getDe()) + "</br>" + "até " + DateUtil.formatarDataPTBR(filtro.getIntervaloMovimento().getAte());
    	}
    	
    	result.include("dataLancamento", dataRecolhimento);
    	result.include("notaEnvio", notaEnvio);
    	
    }
    
    @Post
    public void enviarEmail() {
    	
    	FiltroConsultaNotaEnvioDTO filtro = this.getFiltroNotaEnvioSessao();
    	
    	
    	filtro.setEnvioEmail(true);
    	
     	if(!distribuidorService.verificarParametroDistribuidorEmissaoDocumentosEmailCheck(null, TipoParametrosDistribuidorEmissaoDocumento.NOTA_ENVIO)){
     		throw new ValidacaoException(TipoMensagem.ERROR, "Notas de envio não podem ser enviadas por e-mail, distribuidor não aceita o envio deste documento.");
     	}
    	
		try {
			ValidacaoException mensagemValidacao = geracaoNotaEnvioService.enviarEmail(filtro);
			result.use(Results.json()).from(mensagemValidacao.getValidacao(), Constantes.PARAM_MSGS).recursive().serialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
}