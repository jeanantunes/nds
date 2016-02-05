package br.com.abril.nds.controllers.financeiro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.ContaCorrenteCotaVO;
import br.com.abril.nds.client.vo.ContaCorrenteVO;
import br.com.abril.nds.client.vo.FooterTotalFornecedorVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaVendaEncalheDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.InfoTotalFornecedorDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroDTO;
import br.com.abril.nds.dto.ResultadosContaCorrenteConsignadoDTO;
import br.com.abril.nds.dto.ResultadosContaCorrenteEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoVendaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.ConsolidadoFinanceiroService;
import br.com.abril.nds.service.ContaCorrenteCotaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/contaCorrenteCota")
@Rules(Permissao.ROLE_FINANCEIRO_CONTA_CORRENTE)
public class ContaCorrenteCotaController extends BaseController {
    
    @Autowired
    private Result result;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private CotaService cotaService;
    
    @Autowired
    private ConsolidadoFinanceiroService consolidadoFinanceiroService;
    
    @Autowired
    private HttpSession session;
    
    @Autowired
    private HttpServletResponse httpServletResponse;
    
    @Autowired
    private ContaCorrenteCotaService contaCorrenteCotaService;
    
    @Autowired
    private EmailService emailService;
    
    private static final String FILTRO_SESSION_ATTRIBUTE = "filtroContaCorrente";
    
    private static final String FILTRO_SESSION_ATTRIBUTE_ENCALHE = "filtroContaCorrenteEncalhe";
    
    private static final String FILTRO_SESSION_ATTRIBUTE_CONSIGNADO = "filtroContaCorrenteConsignado";
    
    @Autowired
    private HttpServletRequest request;
    
    public ContaCorrenteCotaController() {
    }
    
    @Path("/")
    public void index() {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	
    	Calendar c = Calendar.getInstance();
    	c.setTime(distribuidorService.obterDataOperacaoDistribuidor());
    	c.add(Calendar.DAY_OF_MONTH, -10);
    	result.include("dataDe", sdf.format(c.getTime()));
    	c.setTime(distribuidorService.obterDataOperacaoDistribuidor());
    	c.add(Calendar.DAY_OF_MONTH, 3);
    	result.include("dataExtracaoDe", sdf.format(new Date()));
    	result.include("dataExtracaoAte", sdf.format(new Date()));
    	result.include("dataAte", sdf.format(c.getTime()));
    }
    
    /**
     * Método que consulta a conta corrente da Cota selecionada
     * 
     * @param filtroViewContaCorrenteCotaDTO
     * @param sortname
     * @param sortorder
     * @param rp
     * @param page
     */
    public void consultarContaCorrenteCota(FiltroViewContaCorrenteCotaDTO filtroViewContaCorrenteCotaDTO,
            String sortname, String sortorder, int rp, int page) {
        
        this.validarDadosEntradaPesquisa(filtroViewContaCorrenteCotaDTO.getNumeroCota());
        
        this.prepararFiltro(filtroViewContaCorrenteCotaDTO, sortorder, sortname, page, rp);
        
        this.session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroViewContaCorrenteCotaDTO);
        
        BigInteger total = this.consolidadoFinanceiroService.countObterContaCorrente(filtroViewContaCorrenteCotaDTO);
        
        if (total == null || BigInteger.ZERO.compareTo(total) == 0) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
        }
        
        List<ContaCorrenteCotaVO> listaItensContaCorrenteCota = consolidadoFinanceiroService.obterContaCorrente(filtroViewContaCorrenteCotaDTO);
        
        result.use(FlexiGridJson.class).from(listaItensContaCorrenteCota).page(page).total(total.intValue()).serialize();
    }
    
    /**
     * Consulta os encalhes da cota em uma determinada data
     * 
     * @param filtroConsolidadoEncalheDTO
     * @param sortname
     * @param sortorder
     * @param rp
     * @param page
     */
    public void consultarEncalheCota(FiltroConsolidadoEncalheCotaDTO filtro) {
        
        request.getSession().setAttribute(FILTRO_SESSION_ATTRIBUTE_ENCALHE, filtro);
        
        List<EncalheCotaDTO> listaEncalheCota = consolidadoFinanceiroService.obterMovimentoEstoqueCotaEncalhe(filtro);
        
        Collection<InfoTotalFornecedorDTO> listaInfoTotalFornecedor = mostrarInfoTotalForncedores(listaEncalheCota);
        
        if (listaEncalheCota != null) {
            
            TableModel<CellModelKeyValue<EncalheCotaDTO>> tableModel = new TableModel<CellModelKeyValue<EncalheCotaDTO>>();
            
            tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaEncalheCota));
            tableModel.setPage(1);
            tableModel.setTotal(listaEncalheCota.size());
            
            ResultadosContaCorrenteEncalheDTO resultado = new ResultadosContaCorrenteEncalheDTO(tableModel, filtro
                    .getDataConsolidado().toString(), listaInfoTotalFornecedor);
            
            boolean temMaisQueUm = listaInfoTotalFornecedor.size() > 1;
            
            Object[] dados = new Object[3];
            dados[0] = temMaisQueUm;
            dados[1] = resultado;
            
            BigDecimal totalGeral = BigDecimal.ZERO;
            
            for (InfoTotalFornecedorDTO dto : listaInfoTotalFornecedor) {
                
                totalGeral = totalGeral.add(dto.getValorTotal());
            }
            
            dados[2] = totalGeral.setScale(4, RoundingMode.HALF_EVEN);
            
            result.use(Results.json()).from(dados, "result").recursive().serialize();
        } else {
            throw new ValidacaoException(TipoMensagem.WARNING, "Dados do Encalhe não encontrado.");
        }
        
    }
    
    /**
     * Consulta Consignados da cota em uma determinada data
     * 
     * @param filtroConsolidadoConsignadoCotaDTO
     * @param sortname
     * @param sortorder
     * @param rp
     * @param page
     */
    public void consultarConsignadoCota(FiltroConsolidadoConsignadoCotaDTO filtro, String sortname, String sortorder) {
        
        request.getSession().setAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO, filtro);
        
        List<ConsignadoCotaDTO> listaConsignadoCota = consolidadoFinanceiroService.obterMovimentoEstoqueCotaConsignado(filtro);
        
        if (listaConsignadoCota != null) {
            Collection<InfoTotalFornecedorDTO> listaInfoTotalFornecedor = mostrarInfoTotalForncedoresConsignado(listaConsignadoCota);
            
            TableModel<CellModelKeyValue<ConsignadoCotaDTO>> tableModel = new TableModel<CellModelKeyValue<ConsignadoCotaDTO>>();
            
            tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConsignadoCota));
            tableModel.setPage(1);
            tableModel.setTotal(listaConsignadoCota.size());
            
            if (listaInfoTotalFornecedor != null && !listaInfoTotalFornecedor.isEmpty()) {
                
                for (InfoTotalFornecedorDTO info : listaInfoTotalFornecedor) {
                    info.setValorTotal(info.getValorTotal().setScale(4, RoundingMode.HALF_EVEN));
                }
                
            }
            
            ResultadosContaCorrenteConsignadoDTO resultado = new ResultadosContaCorrenteConsignadoDTO(tableModel,
                    filtro.getDataConsolidado().toString(), listaInfoTotalFornecedor);
            
            boolean temMaisQueUm = listaInfoTotalFornecedor.size() > 1;
            
            Object[] dados = new Object[3];
            dados[0] = temMaisQueUm;
            dados[1] = resultado;
            
            BigDecimal total = BigDecimal.ZERO;
            for (ConsignadoCotaDTO c : listaConsignadoCota) {
                
                total = total.add(c.getTotal());
            }
            total = total.setScale(4, RoundingMode.HALF_EVEN);
            dados[2] = total;
            
            result.use(Results.json()).from(dados, "result").recursive().serialize();
            
        } else {
            throw new ValidacaoException(TipoMensagem.WARNING, "Dados do Consolidado não encontrado.");
        }
    }
    
    /**
     * Método que armazena informações para exibição do nome fornecedor e o
     * total por fornecedor
     * 
     * @param listaEncalheCota
     */
    private Collection<InfoTotalFornecedorDTO> mostrarInfoTotalForncedores(List<EncalheCotaDTO> listaEncalheCota) {
        
        Map<String, InfoTotalFornecedorDTO> mapFornecedores = new HashMap<String, InfoTotalFornecedorDTO>();
        
        String key;
        BigDecimal valor;
        
        for (EncalheCotaDTO encalhe : listaEncalheCota) {
            key = encalhe.getNomeFornecedor();
            valor = encalhe.getTotal() == null ? BigDecimal.ZERO : encalhe.getTotal();
            if (mapFornecedores.containsKey(key)) {
                valor = mapFornecedores.get(key).getValorTotal().add(valor);
            }
            
            mapFornecedores.put(key, new InfoTotalFornecedorDTO(key, valor.setScale(4, RoundingMode.HALF_EVEN)));
            
        }
        
        for (Entry<String, InfoTotalFornecedorDTO> info : mapFornecedores.entrySet()) {
            
            info.getValue().setValorTotal(info.getValue().getValorTotal().setScale(4, RoundingMode.HALF_EVEN));
        }
        
        List<InfoTotalFornecedorDTO> infoTotalFornecedorDTOs = new ArrayList<InfoTotalFornecedorDTO>();
        infoTotalFornecedorDTOs.addAll(mapFornecedores.values());
        
        return infoTotalFornecedorDTOs;
        
    }
    
    /**
     * Método que armazena informações para exibição do nome fornecedor e o
     * total por fornecedor
     * 
     * @param listaConsignadoCota
     */
    private Collection<InfoTotalFornecedorDTO> mostrarInfoTotalForncedoresConsignado(
            List<ConsignadoCotaDTO> listaConsignadoCota) {
        
        Map<String, InfoTotalFornecedorDTO> mapFornecedores = new HashMap<String, InfoTotalFornecedorDTO>();
        
        String key;
        BigDecimal valor;
        
        for (ConsignadoCotaDTO consignado : listaConsignadoCota) {
            key = consignado.getNomeFornecedor();
            valor = consignado.getTotal();
            if (mapFornecedores.containsKey(key) && valor != null) {
                valor = mapFornecedores.get(key).getValorTotal().add(valor);
            }
            
            if (valor == null) {
                
                valor = BigDecimal.ZERO;
            }
            
            mapFornecedores.put(key, new InfoTotalFornecedorDTO(key, valor.setScale(4, RoundingMode.HALF_EVEN)));
            
        }
        List<InfoTotalFornecedorDTO> infoTotalFornecedorDTOs = new ArrayList<InfoTotalFornecedorDTO>();
        infoTotalFornecedorDTOs.addAll(mapFornecedores.values());
        
        return infoTotalFornecedorDTOs;
        
    }
    
    public void exportarEncalhe(FileType fileType) throws IOException {
        FiltroConsolidadoEncalheCotaDTO filtro = this.obterFiltroExportacaoEncalhe();
        
        List<EncalheCotaDTO> listaEncalheCota = consolidadoFinanceiroService.obterMovimentoEstoqueCotaEncalhe(filtro);
        
        String nomeCota = cotaService.obterNomeResponsavelPorNumeroDaCota(filtro.getNumeroCota());
        String cota = filtro.getNumeroCota() + " - " + nomeCota;
        filtro.setCota(cota);
        
        HashMap<String, BigDecimal> totais = new HashMap<String, BigDecimal>();
        
        for (EncalheCotaDTO encalheCotaDTO : listaEncalheCota) {
            String key = encalheCotaDTO.getNomeFornecedor();
            if (totais.containsKey(key)) {
                totais.put(key, totais.get(key).add(encalheCotaDTO.getTotal()));
            } else {
                totais.put(key, encalheCotaDTO.getTotal());
            }
        }
        
        FileExporter.to("encalhe-cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro,
                new FooterTotalFornecedorVO(totais), listaEncalheCota, EncalheCotaDTO.class, this.httpServletResponse);
        
        result.use(Results.nothing());
        
    }
    
    private FiltroConsolidadoEncalheCotaDTO obterFiltroExportacaoEncalhe() {
        
        FiltroConsolidadoEncalheCotaDTO filtro = (FiltroConsolidadoEncalheCotaDTO) session
                .getAttribute(FILTRO_SESSION_ATTRIBUTE_ENCALHE);
        
        if (filtro != null) {
            
            if (filtro.getPaginacao() != null) {
                
                filtro.getPaginacao().setPaginaAtual(null);
                filtro.getPaginacao().setQtdResultadosPorPagina(null);
            }
            
        }
        
        return filtro;
    }
    
    /**
     * Trata valores negativos para a exportação
     * 
     * @param listaItensContaCorrenteCota
     */
    private void tratarValoresNegativosESaldo(List<ContaCorrenteCotaVO> listaItensContaCorrenteCota){
    	
    	for (ContaCorrenteCotaVO item : listaItensContaCorrenteCota){
    		
    		item.setEncalhe(item.getEncalhe()!= null ?item.getEncalhe().negate():BigDecimal.ZERO);
    		
    		item.setValorPostergado(item.getValorPostergado()!= null ?item.getValorPostergado().negate():BigDecimal.ZERO);
    		
    		item.setDebitoCredito(item.getDebitoCredito() != null ?item.getDebitoCredito().negate(): BigDecimal.ZERO);
    		
    		item.setTotal(item.getTotal() != null ?item.getTotal().negate():BigDecimal.ZERO);
    		
    		item.setSaldo(item.getSaldo()!= null ? item.getSaldo().negate():BigDecimal.ZERO);
    		
    		if ((item.getStatusDivida()==null || !(item.getStatusDivida().equals(StatusDivida.NEGOCIADA) || item.getStatusDivida().equals(StatusDivida.POSTERGADA)))&&
    		   (!item.isInadimplente())&&
    		   (!item.getCobrado())){
    			
    			item.setSaldo(item.getTotal());
    		}
    	}
    }
    
    public void exportar(FileType fileType) throws IOException {
        
        FiltroViewContaCorrenteCotaDTO filtro = this.obterFiltroExportacao();
        
        List<ContaCorrenteCotaVO> listaItensContaCorrenteCota = consolidadoFinanceiroService.obterContaCorrente(filtro);
        
        this.tratarValoresNegativosESaldo(listaItensContaCorrenteCota);
        
        FileExporter.to("conta-corrente-cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro,
                listaItensContaCorrenteCota, ContaCorrenteCotaVO.class, this.httpServletResponse);
    }
    
    private FiltroViewContaCorrenteCotaDTO obterFiltroExportacao() {
        
        FiltroViewContaCorrenteCotaDTO filtro = (FiltroViewContaCorrenteCotaDTO) session
                .getAttribute(FILTRO_SESSION_ATTRIBUTE);
        
        if (filtro != null) {
            
            if (filtro.getPaginacao() != null) {
                
                filtro.getPaginacao().setPaginaAtual(null);
                filtro.getPaginacao().setQtdResultadosPorPagina(null);
            }
            
            if (filtro.getNumeroCota() != null) {
                
                Cota cota = this.cotaService.obterPorNumeroDaCota(filtro.getNumeroCota());
                
                if (cota != null) {
                    
                    Pessoa pessoa = cota.getPessoa();
                    
                    if (pessoa instanceof PessoaFisica) {
                        
                        filtro.setNomeCota(((PessoaFisica) pessoa).getNome());
                        
                    } else if (pessoa instanceof PessoaJuridica) {
                        
                        filtro.setNomeCota(((PessoaJuridica) pessoa).getRazaoSocial());
                    }
                }
            }
        }
        
        return filtro;
    }
    
    private void prepararFiltro(FiltroViewContaCorrenteCotaDTO filtroViewContaCorrenteCotaDTO, String sortorder,
            String sortname, int page, int rp) {
        
        PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
        
        filtroViewContaCorrenteCotaDTO.setPaginacao(paginacao);
        
        paginacao.setPaginaAtual(page);
        
        filtroViewContaCorrenteCotaDTO.setColunaOrdenacao(sortname);
    }
    
    private void validarDadosEntradaPesquisa(Integer numeroCota) {
        List<String> listaMensagemValidacao = new ArrayList<String>();
        
        if (numeroCota == null) {
            listaMensagemValidacao.add("O Preenchimento do campo Cota é obrigatório.");
        } else {
            if (!Util.isNumeric(numeroCota.toString())) {
                listaMensagemValidacao.add("A Cota permite apenas valores números.");
            }
        }
        
        if (!listaMensagemValidacao.isEmpty()) {
            ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.WARNING, listaMensagemValidacao);
            throw new ValidacaoException(validacaoVO);
        }
    }
    
    public void obterMovimentoVendaEncalhe(Long idConsolidado, Date dataEscolhida, Integer numeroCota, String sortname,
            String sortorder, int rp, int page) {
        
        FiltroConsolidadoVendaCotaDTO filtro = new FiltroConsolidadoVendaCotaDTO();
        
        filtro.setIdConsolidado(idConsolidado);
        filtro.setDataConsolidado(dataEscolhida);
        filtro.setNumeroCota(numeroCota);
        
        filtro.setOrdenacaoColuna(FiltroConsolidadoVendaCotaDTO.OrdenacaoColuna.valueOf(sortname));
        filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder));
        
        List<ConsultaVendaEncalheDTO> encalheDTOs = consolidadoFinanceiroService.obterMovimentoVendaEncalhe(filtro);
        
        for (ConsultaVendaEncalheDTO eDTO : encalheDTOs) {
            
            eDTO.setPrecoComDesconto((eDTO.getPrecoComDesconto() == null) ? BigDecimal.ZERO : eDTO
                    .getPrecoComDesconto().setScale(4, RoundingMode.HALF_EVEN));
            
            eDTO.setTotal((eDTO.getTotal() == null) ? BigDecimal.ZERO : eDTO.getTotal().setScale(4,
                    RoundingMode.HALF_EVEN));
            
            if (eDTO.getPrecoCapa() == null) {
                eDTO.setPrecoCapa(BigDecimal.ZERO);
            }
            
            eDTO.setPrecoCapa(eDTO.getPrecoCapa().setScale(2, RoundingMode.HALF_EVEN));
        }
        
        TableModel<CellModelKeyValue<ConsultaVendaEncalheDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaVendaEncalheDTO>>();
        
        tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(encalheDTOs));
        tableModel.setPage(1);
        tableModel.setTotal(encalheDTOs.size());
        
        result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
        
    }
    
    public void exportarVendaEncalhe(FileType fileType, Long idConsolidado, Date dataEscolhida, Integer numeroCota)
            throws IOException {
        
        Cota cotaBanco = this.cotaService.obterPorNumeroDaCota(numeroCota);
        FiltroConsolidadoVendaCotaDTO filtro = new FiltroConsolidadoVendaCotaDTO();
        filtro.setDataConsolidado(dataEscolhida);
        filtro.setNumeroCota(numeroCota);
        
        filtro.setIdConsolidado(idConsolidado);
        String cota = numeroCota + " - " + PessoaUtil.obterNomeExibicaoPeloTipo(cotaBanco.getPessoa());
        filtro.setCota(cota);
        
        List<ConsultaVendaEncalheDTO> encalheDTOs = consolidadoFinanceiroService.obterMovimentoVendaEncalhe(filtro);
        
        HashMap<String, BigDecimal> totais = new HashMap<String, BigDecimal>();
        
        for (ConsultaVendaEncalheDTO encalheDTO : encalheDTOs) {
            String key = encalheDTO.getNomeFornecedor();
            if (totais.containsKey(key)) {
                totais.put(key, totais.get(key).add(encalheDTO.getTotal()));
            } else {
                totais.put(key, encalheDTO.getTotal());
            }
        }
        FileExporter.to("venda-encalhe", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro,
                new FooterTotalFornecedorVO(totais), encalheDTOs, ConsultaVendaEncalheDTO.class,
                this.httpServletResponse);
        
        result.use(Results.nothing());
    }
    
    public void exportarConsignadoCota(FileType fileType) throws IOException {
        FiltroConsolidadoConsignadoCotaDTO filtro = (FiltroConsolidadoConsignadoCotaDTO) request.getSession()
                .getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO);
        String nomeCota = cotaService.obterNomeResponsavelPorNumeroDaCota(filtro.getNumeroCota());
        String cota = filtro.getNumeroCota() + " - " + nomeCota;
        filtro.setCota(cota);
        
        List<ConsignadoCotaDTO> listConsignadoCotaDTO = consolidadoFinanceiroService
                .obterMovimentoEstoqueCotaConsignado(filtro);
        HashMap<String, BigDecimal> totais = new HashMap<String, BigDecimal>();
        
        for (ConsignadoCotaDTO consignadoDTO : listConsignadoCotaDTO) {
            String key = consignadoDTO.getNomeFornecedor();
            if (totais.containsKey(key)) {
                totais.put(key, totais.get(key).add(consignadoDTO.getTotal()));
            } else {
                totais.put(key, consignadoDTO.getTotal());
            }
        }
        
        FileExporter.to("consignado-encalhe", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro,
                new FooterTotalFornecedorVO(totais), listConsignadoCotaDTO, ConsignadoCotaDTO.class,
                this.httpServletResponse);
        
        result.use(Results.nothing());
    }
    
    @Rules(Permissao.ROLE_FINANCEIRO_CONTA_CORRENTE_ALTERACAO)
    public void enviarEmail(String mensagem, String[] destinatarios) throws IOException {
        
        AnexoEmail anexoXLS = new AnexoEmail("conta-corrente-cota", this.gerarAnexo(FileType.XLS), TipoAnexo.XLS);
        AnexoEmail anexoPDF = new AnexoEmail("conta-corrente-cota", this.gerarAnexo(FileType.PDF), TipoAnexo.PDF);
        
        List<AnexoEmail> anexos = new ArrayList<AnexoEmail>();
        anexos.add(anexoXLS);
        anexos.add(anexoPDF);
        
        if (destinatarios[1] != null && destinatarios[1] != "") {
            String destinatario = destinatarios[0].trim();
            String[] copiaPara = destinatarios[1].split("[;]");
            destinatarios = new String[copiaPara.length + 1];
            destinatarios[0] = destinatario;
            for (int i = 0; i < copiaPara.length; i++) {
                destinatarios[i + 1] = copiaPara[i].trim();
            }
        } else {
            String destinatario = destinatarios[0].trim();
            destinatarios = new String[1];
            destinatarios[0] = destinatario;
        }
        
        String assunto = "Resumo Conta Corrente";
        try {
            emailService.enviar(assunto, mensagem, destinatarios, anexos);
            throw new ValidacaoException(TipoMensagem.SUCCESS, "E-mail enviado com sucesso");
        } catch (AutenticacaoEmailException e) {
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "[Falha de autenticação] Não foi possível enviar o e-mail, "
                        + "verifique o servidor de e-mail e os dados de autenticação.");
        }
    }
    
    
    public void extracaoCC( Date dataExtracaoDe, Date dataExtracaoAte) throws IOException {
       
    
    	FiltroViewContaCorrenteDTO filtro = new FiltroViewContaCorrenteDTO();
        filtro.setInicioPeriodo(dataExtracaoDe);
        filtro.setFimPeriodo(dataExtracaoAte);
        
        List<ContaCorrenteVO> listacc =  this.consolidadoFinanceiroService.obterContaCorrenteExtracao(filtro);
       
        FileExporter.to("contacorrente", FileType.XLS).inHTTPResponse(this.getNDSFileHeader(), filtro, listacc, ContaCorrenteVO.class, this.httpServletResponse);
        
        result.use(Results.nothing());
    }
    
    public void pesquisarEmailCota(Integer numeroCota) {
        
        String email = cotaService.obterPorNumeroDaCota(numeroCota).getPessoa().getEmail();
        
        email = email == null ? "" : email;
        
        this.result.use(Results.json()).withoutRoot().from(email).recursive().serialize();
    }
    
    private byte[] gerarAnexo(FileType tipo) throws IOException {
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        if (tipo.equals(FileType.XLS)) {
            exportarAnexo(FileType.XLS, os);
        } else {
            exportarAnexo(FileType.PDF, os);
        }
        
        return os.toByteArray();
    }
    
    public void exportarAnexo(FileType fileType, OutputStream output) throws IOException {
        
        FiltroViewContaCorrenteCotaDTO filtro = this.obterFiltroExportacao();
       
        List<ContaCorrenteCotaVO> listaItensContaCorrenteCota = this.consolidadoFinanceiroService
                .obterContaCorrente(filtro);
        
        FileExporter.to("conta-corrente-cota", fileType).inOutputStream(this.getNDSFileHeader(), filtro, null,
                listaItensContaCorrenteCota, ContaCorrenteCotaVO.class, output);
    }
    
    @Post
    public void consultarDebitoCreditoCota(Long idConsolidado, Date data, Integer numeroCota, String sortname,
            String sortorder) {
        
        List<DebitoCreditoCota> movs = this.contaCorrenteCotaService.consultarDebitoCreditoCota(idConsolidado, data, numeroCota, sortorder, sortname);
        
        this.result.use(FlexiGridJson.class).from(movs).page(1).total(movs.size()).serialize();
    }
    
    public void exportarDebitoCreditoCota(FileType fileType, Long idConsolidado, Date data, Integer numeroCota,
            String sortname, String sortorder) throws IOException {
        
        List<DebitoCreditoCota> movs = this.contaCorrenteCotaService.consultarDebitoCreditoCota(idConsolidado, data, numeroCota, sortorder, sortname);
        
        FileExporter.to("debito-credito", fileType).inHTTPResponse(this.getNDSFileHeader(),
                this.obterFiltroExportacao(), movs, DebitoCreditoCota.class, this.httpServletResponse);
        
        result.nothing();
    }
    
    @Post
    public void consultarEncargosCota(Long idConsolidado, Date data, Integer numeroCota) {
        
        List<BigDecimal> dados = new ArrayList<BigDecimal>();
        
        BigDecimal valor = this.contaCorrenteCotaService.consultarJurosCota(idConsolidado, data, numeroCota);
        dados.add(valor == null ? BigDecimal.ZERO.setScale(4, RoundingMode.HALF_EVEN) : valor.setScale(4,
                RoundingMode.HALF_EVEN));
        
        valor = this.contaCorrenteCotaService.consultarMultaCota(idConsolidado, data, numeroCota);
        dados.add(valor == null ? BigDecimal.ZERO.setScale(4, RoundingMode.HALF_EVEN) : valor.setScale(4,
                RoundingMode.HALF_EVEN));
        
        this.result.use(Results.json()).from(dados, "result").recursive().serialize();
    }
    
    @Post
    public void consultarValorVendaDia(Long idConsolidado, Date data, Integer numeroCota, String sortname,
            String sortorder) {
        
        List<MovimentoFinanceiroDTO> movs = this.contaCorrenteCotaService.consultarValorVendaDia(numeroCota,
                idConsolidado, data);
        
        this.result.use(FlexiGridJson.class).from(movs).page(1).total(movs.size()).serialize();
    }
}