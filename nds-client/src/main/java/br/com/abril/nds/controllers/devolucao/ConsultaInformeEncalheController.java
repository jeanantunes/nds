package br.com.abril.nds.controllers.devolucao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ColunaRelatorioInformeEncalhe;
import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.dto.TipoImpressaoInformeEncalheDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.DistribuicaoFornecedorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

/**
 * 
 * Classe responsável por controlas as ações da pagina de Informe de
 * Recolhimentos.
 * 
 * @author Discover Technology
 * 
 */
@Resource
@Path(value = "/devolucao/informeEncalhe")
@Rules(Permissao.ROLE_RECOLHIMENTO_CONSULTA_INFORME_ENCALHE)
public class ConsultaInformeEncalheController extends BaseController {
        
    @Autowired
    private Result result;
    
    @Autowired
    private LancamentoService lancamentoService;
    
    @Autowired
    private FornecedorService fornecedorService;
    
    @Autowired
    private CapaService capaService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private CalendarioService calendarioService;
    
    @Autowired
    private DistribuicaoFornecedorService distribuicaoFornecedorService;
    
    private final DiaSemana inicioDaSemana;
    
    
    public ConsultaInformeEncalheController(final DistribuidorService distribuidorService) {
        inicioDaSemana = distribuidorService.inicioSemanaRecolhimento();
    }
    
    @Path("/")
    public void index() {
        result.include("fornecedores", fornecedorService
                .obterFornecedoresIdNome(SituacaoCadastro.ATIVO, true));
    }
    
    @Post("/busca.json")
    public void busca(final Long idFornecedor, final Integer semanaRecolhimento, final Calendar dataRecolhimento, final String sortname, final String sortorder, final int rp, final int page) {
        
        Calendar dataInicioRecolhimento = null, dataFimRecolhimento = null;
        
        if ((semanaRecolhimento == null) && (dataRecolhimento == null)) {
            
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Informe [Semana] ou [Data Recolhimento]"));
        }
        
        if (semanaRecolhimento != null) {
            
            final String strSemanaRecolhimento = semanaRecolhimento.toString();
            
            if (strSemanaRecolhimento.length() != 6) {
                
                throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Semana deve estar no padrão ano+semana (Ex: semana 4 de 2012, 201204)"));
            }
            
            dataInicioRecolhimento = Calendar.getInstance();
            dataFimRecolhimento = Calendar.getInstance();
            
            final Intervalo<Date> intervalo = obterDataDaSemana(strSemanaRecolhimento);
            dataInicioRecolhimento.setTime(intervalo.getDe());
            dataFimRecolhimento.setTime(intervalo.getAte());
            
        } else if (dataRecolhimento != null) {
            dataInicioRecolhimento = dataRecolhimento;
            dataFimRecolhimento = dataRecolhimento;
        }
        
        final Long quantidade = lancamentoService.quantidadeLancamentoInformeRecolhimento(idFornecedor, dataInicioRecolhimento, dataFimRecolhimento);
        
        if (quantidade > 0) {
            final List<InformeEncalheDTO> informeEncalheDTOs = lancamentoService.obterLancamentoInformeRecolhimento(idFornecedor, dataInicioRecolhimento, dataFimRecolhimento, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page * rp - rp, rp);
            result.use(FlexiGridJson.class).from(informeEncalheDTOs).total(quantidade.intValue()).page(page).serialize();
        } else {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Registros não encontrados."));
        }
    }
    
    private Intervalo<Date> obterDataDaSemana(final String anoSemana) {
        
        final Date data = obterDataBase(anoSemana, distribuidorService.obterDataOperacaoDistribuidor());
        
        final Integer semana = Integer.parseInt(anoSemana.substring(4));
        
        final Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        
        final Date dataInicioSemana = SemanaUtil.obterDataDaSemanaNoAno(semana, distribuidorService.inicioSemanaRecolhimento().getCodigoDiaSemana(), cal.get(Calendar.YEAR));
        
        final Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
        
        final Intervalo<Date> periodoRecolhimento = new Intervalo<Date>(dataInicioSemana, dataFimSemana);
        
        return periodoRecolhimento;
        
    }
    
    private Date obterDataBase(final String anoSemana, final Date data) {
        
        final String ano = anoSemana.substring(0,4);
        final Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.YEAR, Integer.parseInt(ano));
        
        return c.getTime();
    }
    
    
    @Post
    public void relatorioInformeEncalhe(final Long idFornecedor, Integer semanaRecolhimento,
            final Calendar dataRecolhimento,
            final TipoImpressaoInformeEncalheDTO tipoImpressao, final String sortorder){
        
        final String sortname = "sequenciaMatriz";
        
        Calendar dataInicioRecolhimento = null, dataFimRecolhimento = null;
        
        if (tipoImpressao == null || tipoImpressao.getColunas() == null || tipoImpressao.getColunas().isEmpty()){
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Parâmetro inválido para geração do relaório.");
        }
        
        if ((semanaRecolhimento == null) && (dataRecolhimento == null)) {
            
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Informe [Semana] ou [Data Recolhimento]"));
        }
        
        final ParametrosRecolhimentoDistribuidor parametros = distribuidorService.parametrosRecolhimentoDistribuidor();
        
        final int maxDiaSemanaRecolhimento = obterMaxDiaRecolhimentoDistribuidor(parametros);
        
        if (semanaRecolhimento != null) {
            dataInicioRecolhimento = Calendar.getInstance();
            
            semanaRecolhimento = Integer.parseInt(semanaRecolhimento.toString().substring(4));
            
            if (semanaRecolhimento > dataInicioRecolhimento.getMaximum(Calendar.WEEK_OF_YEAR)) {
                throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Semana inválida."));
            }
            
            dataInicioRecolhimento.set(Calendar.WEEK_OF_YEAR, semanaRecolhimento);
            
            dataInicioRecolhimento.set(Calendar.DAY_OF_WEEK, inicioDaSemana.getCodigoDiaSemana());
            dataFimRecolhimento = Calendar.getInstance();
            dataFimRecolhimento.setTime(calendarioService.adicionarDiasUteis(dataInicioRecolhimento.getTime(), maxDiaSemanaRecolhimento));
            
        } else if (dataRecolhimento != null) {
            dataInicioRecolhimento = dataRecolhimento;
            dataFimRecolhimento = Calendar.getInstance();
            
            List<Integer> diasRec = this.distribuicaoFornecedorService.obterCodigosDiaDistribuicaoFornecedor(idFornecedor, OperacaoDistribuidor.RECOLHIMENTO);
            
            Date dataFim = calendarioService.adicionarDiasUteis(dataInicioRecolhimento.getTime(), 1);
            Calendar c = Calendar.getInstance();
            c.setTime(dataFim);
            
            if (diasRec != null && !diasRec.isEmpty()){
                while (!diasRec.contains(c.get(Calendar.DAY_OF_WEEK))){
                    dataFim = calendarioService.adicionarDiasUteis(c.getTime(), 1);
                    c.setTime(dataFim);
                }
            }
            
            dataFimRecolhimento.setTime(dataFim);
        }
        
        final List<InformeEncalheDTO> dados = lancamentoService.obterLancamentoInformeRecolhimento(idFornecedor, dataInicioRecolhimento, dataInicioRecolhimento, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), null, null);
        
        result.include("diaMesInicioRecolhimento", 1);
        
        if (dataInicioRecolhimento != null) {
            result.include("dataInicioRecolhimento", new SimpleDateFormat("dd/MM").format(dataInicioRecolhimento.getTime()));
            result.include("diaSemanaInicioRecolhimento", SemanaUtil.obterDiaSemana(dataInicioRecolhimento.get(Calendar.DAY_OF_WEEK)));
        }
        
        result.include("diaMesFimRecolhimento", maxDiaSemanaRecolhimento);
        
        if (semanaRecolhimento != null){
            
            dataFimRecolhimento = null;
        }
        
        if (dataFimRecolhimento != null) {
            result.include("dataFimRecolhimento", new SimpleDateFormat("dd/MM").format(dataFimRecolhimento.getTime()));
            result.include("diaSemanaFimRecolhimento", SemanaUtil.obterDiaSemana(dataFimRecolhimento.get(Calendar.DAY_OF_WEEK)));
        }
        
        final List<ColunaRelatorioInformeEncalhe> colunas = new ArrayList<ColunaRelatorioInformeEncalhe>();
        
        if (tipoImpressao != null && tipoImpressao.getColunas() != null){
            
        	final int qtdColunas = tipoImpressao.getColunas().isEmpty() ? 1 : tipoImpressao.getColunas().size();
            
            if (tipoImpressao.getColunas().contains("sequenciaMatriz")){
                
                colunas.add(new ColunaRelatorioInformeEncalhe("Sequência", this.calcularTamanhoColunaRelatorio(qtdColunas, 4), "sequenciaMatriz"));
            }
            
            if (tipoImpressao.getColunas().contains("codigoProduto")){
                
                colunas.add(new ColunaRelatorioInformeEncalhe("Código", this.calcularTamanhoColunaRelatorio(qtdColunas, 4), "codigoProduto"));
            }
            
            if (tipoImpressao.getColunas().contains("nomeProduto")){
                
                colunas.add(new ColunaRelatorioInformeEncalhe("Produto", this.calcularTamanhoColunaRelatorio(qtdColunas, 13), "nomeProduto"));
            }
            
            if (tipoImpressao.getColunas().contains("numeroEdicao")){
                
                colunas.add(new ColunaRelatorioInformeEncalhe("ED.", this.calcularTamanhoColunaRelatorio(qtdColunas, 4), "numeroEdicao"));
            }
            
            if (tipoImpressao.getColunas().contains("chamadaCapa")){
                
                colunas.add(new ColunaRelatorioInformeEncalhe("Chamada de Capa", this.calcularTamanhoColunaRelatorio(qtdColunas, 10), "chamadaCapa"));
            }
            
            if (tipoImpressao.getColunas().contains("codigoDeBarras")){
                
                colunas.add(new ColunaRelatorioInformeEncalhe("Código de Barras", this.calcularTamanhoColunaRelatorio(qtdColunas, 9), "codigoDeBarras"));
            }
            
            if (tipoImpressao.getColunas().contains("precoVenda")){
                
                colunas.add(new ColunaRelatorioInformeEncalhe("R$ Capa", this.calcularTamanhoColunaRelatorio(qtdColunas, 4), "precoVenda"));
            }
            
            if (tipoImpressao.getColunas().contains("nomeEditor")){
                
                colunas.add(new ColunaRelatorioInformeEncalhe("Editor", this.calcularTamanhoColunaRelatorio(qtdColunas, 14), "nomeEditor"));
            }
            
            if (tipoImpressao.getColunas().contains("brinde")){
                
                colunas.add(new ColunaRelatorioInformeEncalhe("Brinde", this.calcularTamanhoColunaRelatorio(qtdColunas, 4), "brinde"));
            }
            
            if (tipoImpressao.getColunas().contains("dataLancamento")){
                
                colunas.add(new ColunaRelatorioInformeEncalhe("Lanc.", this.calcularTamanhoColunaRelatorio(qtdColunas, 4), "dataLancamento"));
            }
            
            if (tipoImpressao.getColunas().contains("dataRecolhimento")){
                
                colunas.add(new ColunaRelatorioInformeEncalhe("Rec.", this.calcularTamanhoColunaRelatorio(qtdColunas, 4), "dataRecolhimento"));
            }
            
            if (tipoImpressao.getColunas().contains("tipoLancamentoParcial")){
                
                colunas.add(new ColunaRelatorioInformeEncalhe("TR", this.calcularTamanhoColunaRelatorio(qtdColunas, 3), "tipoLancamentoParcial"));
            }
            
            if (tipoImpressao.getColunas().contains("pacotePadrao")){
                
                colunas.add(new ColunaRelatorioInformeEncalhe("Pacote Padrao", this.calcularTamanhoColunaRelatorio(qtdColunas, 2), "pacotePadrao"));
            }
        }
        
        final String nomeDistribuidor = distribuidorService.obterRazaoSocialDistribuidor();
        
        result.include("nomeDistribuidor", nomeDistribuidor);
        
        result.include("colunas", colunas);
        
        int qtdReg = 0;
        
        int quebra = 0;

        int qtdImg = 0;
        		
        
        if(tipoImpressao.getCapas().equals(TipoImpressaoInformeEncalheDTO.Capas.FIM)){
        	quebra = TipoImpressaoInformeEncalheDTO.Capas.PAR.equals(tipoImpressao.getCapas()) ? 72 : 41;
        	qtdImg = 72;
        	
        }else{
        	quebra = TipoImpressaoInformeEncalheDTO.Capas.PAR.equals(tipoImpressao.getCapas()) ? 35 : 41;
        	qtdImg = 35;
        }
        
        int indexImg = 0;
        
        int imgAdd = 0;
        
        final List<InformeEncalheDTO> listaResult = new ArrayList<InformeEncalheDTO>();
        
        boolean primeiraPagina = true;
        
        for (final InformeEncalheDTO info : dados){
            
            listaResult.add(info);
            qtdReg++;
            
            if (qtdReg == quebra){
                
                //pra aproveitar melhor o tamanho da pagina
                if (primeiraPagina && !TipoImpressaoInformeEncalheDTO.Capas.PAR.equals(tipoImpressao.getCapas())){
                    
                    quebra += 5;
                    primeiraPagina = false;
                }
                
                qtdReg = 0;
                
                switch (tipoImpressao.getCapas()) {
                
                case FIM:
                case NAO:
                    
                    final InformeEncalheDTO dto = new InformeEncalheDTO();
                    dto.setImagem(true);
                    dto.setSequenciaMatriz(info.getSequenciaMatriz());
                    listaResult.add(dto);
                    break;
                    
                case PAR:
                    
                    for (int i = indexImg ; i < indexImg + qtdImg ; i++){
                        
                        final InformeEncalheDTO img = new InformeEncalheDTO();
                        img.setImagem(true);
                        
                        if (i < dados.size()){
                            
                            img.setIdProdutoEdicao(dados.get(i).getIdProdutoEdicao());
                            img.setSequenciaMatriz(dados.get(i).getSequenciaMatriz());
                        }
                        
                        listaResult.add(img);
                        
                        imgAdd++;
                    }
                    
                    indexImg += qtdImg;
                    break;
                }
                
            }
        }
        
        while (qtdReg != quebra && qtdReg > 0){
            
            final InformeEncalheDTO inDto = new InformeEncalheDTO();
            listaResult.add(inDto);
            qtdReg++;
        }
        
        if (!TipoImpressaoInformeEncalheDTO.Capas.NAO.equals(tipoImpressao.getCapas())){
            
            if (TipoImpressaoInformeEncalheDTO.Capas.PAR.equals(tipoImpressao.getCapas())){
                
                for (int i = indexImg ; i < indexImg + qtdImg ; i++){
                    
                    final InformeEncalheDTO img = new InformeEncalheDTO();
                    img.setImagem(true);
                    
                    if (i < dados.size()){
                        
                        img.setIdProdutoEdicao(dados.get(i).getIdProdutoEdicao());
                        img.setSequenciaMatriz(dados.get(i).getSequenciaMatriz());
                    }
                    
                    listaResult.add(img);
                    
                    imgAdd++;
                }
                
            }
        } else {
            
            final InformeEncalheDTO dt = new InformeEncalheDTO();
            dt.setImagem(true);
            listaResult.add(dt);
        }
        
        if(!TipoImpressaoInformeEncalheDTO.Capas.NAO.equals(tipoImpressao.getCapas())){
            
            if (TipoImpressaoInformeEncalheDTO.Capas.PAR.equals(tipoImpressao.getCapas())){
                
                while (imgAdd % qtdImg != 0){
                    
                    final InformeEncalheDTO inDto = new InformeEncalheDTO();
                    inDto.setImagem(true);
                    listaResult.add(inDto);
                    
                    imgAdd++;
                }
                
            } else {
                
                final int sizeList = listaResult.size();
                
                for (int index = 0 ; index < sizeList ; index++){
                    
                    if (listaResult.get(index).getIdProdutoEdicao() == null){
                        
                        continue;
                    }
                    
                    final InformeEncalheDTO inDto = new InformeEncalheDTO();
                    inDto.setImagem(true);
                    inDto.setIdProdutoEdicao(listaResult.get(index).getIdProdutoEdicao());
                    inDto.setSequenciaMatriz(listaResult.get(index).getSequenciaMatriz());
                    listaResult.add(inDto);
                    
                    imgAdd++;
                    
                    if (imgAdd % qtdImg == 0){
                        
                        listaResult.add(null);
                    }
                }
            }
        }
        
        result.include("dados", listaResult);
    }
    
    private int calcularTamanhoColunaRelatorio(final int qtdColunas, final int porcentual){
        
        final int tamanhoTotalTable = 1000;
        
        return tamanhoTotalTable / qtdColunas * porcentual / 100;
    }
    
    private int obterMaxDiaRecolhimentoDistribuidor(final ParametrosRecolhimentoDistribuidor recolhimento) {
        
        if (recolhimento.isDiaRecolhimentoQuinto()) {
            return 5;
        } else if (recolhimento.isDiaRecolhimentoQuarto()) {
            return 4;
        } else if (recolhimento.isDiaRecolhimentoTerceiro()) {
            return 3;
        } else if (recolhimento.isDiaRecolhimentoSegundo()) {
            return 2;
        }
        
        return 0;
    }
    
}
