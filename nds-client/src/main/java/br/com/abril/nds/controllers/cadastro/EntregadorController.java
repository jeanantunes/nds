package br.com.abril.nds.controllers.cadastro;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.EntregadorCotaProcuracaoPaginacaoVO;
import br.com.abril.nds.client.vo.EntregadorCotaProcuracaoVO;
import br.com.abril.nds.client.vo.EntregadorPessoaFisicaVO;
import br.com.abril.nds.client.vo.EntregadorPessoaJuridicaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EntregadorDTO;
import br.com.abril.nds.dto.ProcuracaoCotaDTO;
import br.com.abril.nds.dto.ProcuracaoImpressaoDTO;
import br.com.abril.nds.dto.ProcuracaoImpressaoWrapper;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO.OrdenacaoColunaEntregador;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProcuracaoEntregador;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EntregadorService;
import br.com.abril.nds.service.PessoaFisicaService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.stella.format.CNPJFormatter;
import br.com.caelum.stella.format.CPFFormatter;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller para a tela de cadastro de entregadores.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/cadastro/entregador")
@Rules(Permissao.ROLE_CADASTRO_ENTREGADOR)
public class EntregadorController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EntregadorController.class);
    
    @Autowired
    private Result result;
    
    @Autowired
    private HttpSession session;
    
    @Autowired
    private HttpServletResponse httpResponse;
    
    @Autowired
    private CotaService cotaService;
    
    @Autowired
    private EntregadorService entregadorService;
    
    @Autowired
    private PessoaFisicaService pessoaFisicaService;
    
    @Autowired
    private PessoaJuridicaService pessoaJuridicaService;
    
    @Autowired
    private TelefoneService telefoneService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    private static final String CPF = "CPF";
    
    private static final String CNPJ = "CNPJ";
    
    private static final String NOME_ARQUIVO_PROCURACAO = "procuracao";
    
    public static final String LISTA_TELEFONES_SALVAR_SESSAO = "listaTelefonesSalvarSessaoEntregador";
    
    public static final String LISTA_TELEFONES_REMOVER_SESSAO = "listaTelefonesRemoverSessaoEntregador";
    
    public static final String LISTA_TELEFONES_EXIBICAO = "listaTelefonesExibicaoEntregador";
    
    public static final String LISTA_ENDERECOS_SALVAR_SESSAO = "listaEnderecosSalvarSessaoEntregador";
    
    public static final String LISTA_ENDERECOS_REMOVER_SESSAO = "listaEnderecosRemoverSessaoEntregador";
    
    public static final String LISTA_ENDERECOS_EXIBICAO = "listaEnderecosExibicaoEntregador";
    
    
    @Path("/")
    public void index() {
        
    }
    
	                /**
     * Método responsável por popular a grid de Entregadores.
     * 
     * @param filtroEntregador - Filtro utilizado na pesquisa.
     * 
     * @param page - Número da página atual da grid.
     */
    @SuppressWarnings("deprecation")
    public void pesquisarEntregadores(
            FiltroEntregadorDTO filtroEntregador, final int page, final int rp,
            final String sortname, final String sortorder) {
        
        filtroEntregador = prepararFiltroEntregador(filtroEntregador, page, sortname, sortorder, rp);
        
        filtroEntregador.setNomeRazaoSocial(PessoaUtil.removerSufixoDeTipo(filtroEntregador.getNomeRazaoSocial()));
        filtroEntregador.setApelidoNomeFantasia(PessoaUtil.removerSufixoDeTipo(filtroEntregador.getApelidoNomeFantasia()));
        
        final List<Entregador> listaEntregador = entregadorService.obterEntregadoresPorFiltro(filtroEntregador);
        
        if (listaEntregador == null || listaEntregador.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro foi encontrado.");
        }
        
        final Long quantidadeRegistrosEntregadores = entregadorService.obterContagemEntregadoresPorFiltro(filtroEntregador);
        
        final TableModel<CellModel> tableModelEntregador = obterTableModel(listaEntregador);
        
        tableModelEntregador.setPage(page);
        
        tableModelEntregador.setTotal(quantidadeRegistrosEntregadores.intValue());
        
        result.use(Results.json()).withoutRoot().from(tableModelEntregador).recursive().serialize();
    }
    
	                /**
     * Método que realiza o cadastro de um entregador com papel de Pessoa
     * Física.
     * 
     * @param idEntregador
     * 
     * @param codigoEntregador
     * 
     * @param isComissionado
     * 
     * @param percentualComissao
     * 
     * @param procuracao
     * 
     * @param cpfEntregador
     * 
     * @param procuracaoEntregador
     */
    public void cadastrarEntregadorPessoaFisica(final Long idEntregador,
            final Long codigoEntregador,
            final boolean isComissionado,
            final String percentualComissao,
            String cpfEntregador,
            final String dataNascimento,
            final PessoaFisica pessoaFisica) {
        
        final Map<String, String> cpf = new HashMap<String, String>();
        cpf.put(CPF, cpfEntregador);
        
        validarParametrosEntradaCadastroEntregador(pessoaFisica,
                dataNascimento,
                idEntregador,
                codigoEntregador,
                isComissionado,
                percentualComissao,
                cpf);
        
        String mensagemSucesso = "Cadastro realizado com sucesso.";
        
        if (idEntregador != null) {
            
            mensagemSucesso = "Edição realizada com sucesso.";
        }
        
        cpfEntregador = cpfEntregador.replaceAll("\\.", "").replaceAll("-", "");
        
        pessoaFisica.setCpf(cpfEntregador);
        
        if (dataNascimento != null && !dataNascimento.isEmpty()) {
            final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                pessoaFisica.setDataNascimento(sdf.parse(dataNascimento));
            } catch (final ParseException e) {
                final String msg = "Ocorreu um erro ao tentar gravar a data de nascimento.";
                LOGGER.debug(msg, e);
                throw new ValidacaoException(TipoMensagem.ERROR, msg);
            }
        }
        
        PessoaFisica pessoaFisicaExistente = pessoaFisicaService.buscarPorCpf(cpfEntregador);
        
        if (pessoaFisicaExistente != null) {
            
            pessoaFisica.setId(pessoaFisicaExistente.getId());
        }
        
        pessoaFisicaExistente = pessoaFisicaService.salvarPessoaFisica(pessoaFisica);
        
        if (entregadorService.isPessoaJaCadastrada(pessoaFisicaExistente.getId(), idEntregador)) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Pessoa física já cadastrada para outro entregador.");
        }
        
        Entregador entregador = null;
        
        if (idEntregador == null) {
            
            entregador = new Entregador();
            
            entregador.setInicioAtividade(new Date());
            
        } else {
            
            entregador = entregadorService.buscarPorId(idEntregador);
        }
        
        entregador.setPessoa(pessoaFisicaExistente);
        entregador.setCodigo(codigoEntregador);
        entregador.setComissionado(isComissionado);
        
        if (percentualComissao != null && !percentualComissao.isEmpty()) {
            
            entregador.setPercentualComissao(new BigDecimal(getValorSemMascara(percentualComissao)));
        }
        
        entregador = entregadorService.salvarEntregadorProcuracao(entregador, null);
        
        processarEnderecosEntregador(entregador.getId());
        
        processarTelefonesEntregador(entregador.getId());
        
        final ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, mensagemSucesso);
        
        result.use(Results.json()).from(validacao, "result").recursive().serialize();
    }
    
	                /**
     * Método que realiza o cadastro de um entregador com papel de Pessoa
     * Juridica.
     * 
     * @param idEntregador
     * 
     * @param codigoEntregador
     * 
     * @param isComissionado
     * 
     * @param percentualComissao
     * 
     * @param procuracao
     * 
     * @param cnpjEntregador
     * 
     * @param procuracaoEntregador
     */
    public void cadastrarEntregadorPessoaJuridica(final Long idEntregador,
            final Long codigoEntregador,
            final boolean isComissionado,
            final String percentualComissao,
            String cnpjEntregador,
            final PessoaJuridica pessoaJuridica) {
        
        final Map<String, String> cnpj = new HashMap<String, String>();
        cnpj.put(CNPJ, cnpjEntregador);
        
        validarParametrosEntradaCadastroEntregador(pessoaJuridica,
                null,
                idEntregador,
                codigoEntregador,
                isComissionado,
                percentualComissao,
                cnpj);
        
        String mensagemSucesso = "Cadastro realizado com sucesso.";
        
        if (idEntregador != null) {
            
            mensagemSucesso = "Edição realizada com sucesso.";
        }
        
        cnpjEntregador = cnpjEntregador.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
        
        pessoaJuridica.setCnpj(cnpjEntregador);
        
        PessoaJuridica pessoaJuridicaExistente = pessoaJuridicaService.buscarPorCnpj(cnpjEntregador);
        
        if (pessoaJuridicaExistente != null) {
            
            pessoaJuridica.setId(pessoaJuridicaExistente.getId());
        }
        
        pessoaJuridicaExistente = pessoaJuridicaService.salvarPessoaJuridica(pessoaJuridica);
        
        if (entregadorService.isPessoaJaCadastrada(pessoaJuridicaExistente.getId(), idEntregador)) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Pessoa jurídica já cadastrada para outro entregador.");
        }
        
        Entregador entregador = null;
        
        if (idEntregador == null) {
            
            entregador = new Entregador();
            
            entregador.setInicioAtividade(new Date());
            
        } else {
            
            entregador = entregadorService.buscarPorId(idEntregador);
        }
        
        entregador.setCodigo(codigoEntregador);
        entregador.setComissionado(isComissionado);
        
        if (percentualComissao != null && !percentualComissao.isEmpty()) {
            
            entregador.setPercentualComissao(new BigDecimal(getValorSemMascara(percentualComissao )));
        }
        
        entregador.setPessoa(pessoaJuridicaExistente);
        
        entregador = entregadorService.salvarEntregadorProcuracao(entregador, null);
        
        processarEnderecosEntregador(entregador.getId());
        
        processarTelefonesEntregador(entregador.getId());
        
        final ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, mensagemSucesso);
        
        result.use(Results.json()).from(validacao, "result").recursive().serialize();
        
        
    }
    
    /**
     * 
     * @param cpf
     */
    public void obterPessoaFisica(String cpf) {
        
        cpf = cpf.replaceAll("\\.", "").replaceAll("-", "");
        
        PessoaFisica pessoaFisica = pessoaFisicaService.buscarPorCpf(cpf);
        
        if (pessoaFisica == null) {
            
            pessoaFisica = new PessoaFisica();
        }
        
        result.use(Results.json()).from(pessoaFisica, "result").serialize();
    }
    
    /**
     * 
     * @param cnpj
     */
    public void obterPessoaJuridica(String cnpj) {
        
        cnpj = cnpj.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
        
        PessoaJuridica pessoaJuridica = pessoaJuridicaService.buscarPorCnpj(cnpj);
        
        if (pessoaJuridica == null) {
            
            pessoaJuridica = new PessoaJuridica();
        }
        
        result.use(Results.json()).from(pessoaJuridica, "result").serialize();
    }
    
	                /**
     * Obtem os endereços referentes ao entregador informado e salva em sessao
     * 
     * @param idEntregador - identificador do entregador
     */
    private void obterEndereco(final Long idEntregador){
        
        final Boolean enderecoPendente = (Boolean) session.getAttribute(EnderecoController.ENDERECO_PENDENTE);
        
        if (enderecoPendente==null || !enderecoPendente){
            
            final List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = entregadorService.obterEnderecosPorIdEntregador(idEntregador);
            
            session.setAttribute(LISTA_ENDERECOS_EXIBICAO, listaEnderecoAssociacao);
        }
    }
    
	                /**
     * Método que prepara um Entregador para ser editado.
     * 
     * @param idEntregador - Id do entregador a ser editado.
     */
    @Rules(Permissao.ROLE_CADASTRO_ENTREGADOR_ALTERACAO)
    public void editarEntregador(final Long idEntregador) {
        
        session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
        session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
        
        final ProcuracaoEntregador procuracaoEntregador = entregadorService.obterProcuracaoEntregadorPorId(idEntregador);
        
        if (procuracaoEntregador == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Entregador não encontrado.");
        }
        
        final Entregador entregador = procuracaoEntregador.getEntregador();
        
        this.obterEndereco(idEntregador);
        
        final List<TelefoneAssociacaoDTO> listaTelefoneAssociacao =
                entregadorService.buscarTelefonesEntregador(idEntregador);
        
        final Map<Integer, TelefoneAssociacaoDTO> map = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
        
        for (final TelefoneAssociacaoDTO telefoneAssociacaoDTO : listaTelefoneAssociacao) {
            
            map.put(telefoneAssociacaoDTO.getReferencia(), telefoneAssociacaoDTO);
        }
        
        session.setAttribute(LISTA_TELEFONES_SALVAR_SESSAO, map);
        
        if (entregador.getPessoa() instanceof PessoaFisica) {
            
            final EntregadorPessoaFisicaVO entregadorPessoaFisica = new EntregadorPessoaFisicaVO();
            
            entregadorPessoaFisica.setEntregador(entregador);
            
            entregadorPessoaFisica.setDataNascimentoEntregadorFormatada(
                    DateUtil.formatarDataPTBR(((PessoaFisica) entregador.getPessoa()).getDataNascimento()));
            
            entregadorPessoaFisica.setInicioAtividadeFormatada(DateUtil.formatarDataPTBR(entregador.getInicioAtividade()));
            
            entregadorPessoaFisica.setProcuracaoEntregador(entregador.getProcuracaoEntregador());
            
            entregadorPessoaFisica.setPessoaFisica((PessoaFisica) entregador.getPessoa());
            
            result.use(Results.json()).from(
                    entregadorPessoaFisica, "result").include(
                            "entregador", "procuracaoEntregador", "procuracaoEntregador.cota", "pessoaFisica").serialize();
            
        } else if (entregador.getPessoa() instanceof PessoaJuridica) {
            
            final EntregadorPessoaJuridicaVO entregadorPessoaJuridica = new EntregadorPessoaJuridicaVO();
            
            entregadorPessoaJuridica.setEntregador(entregador);
            
            entregadorPessoaJuridica.setInicioAtividadeFormatada(DateUtil.formatarDataPTBR(entregador.getInicioAtividade()));
            
            entregadorPessoaJuridica.setProcuracaoEntregador(entregador.getProcuracaoEntregador());
            
            entregadorPessoaJuridica.setPessoaJuridica((PessoaJuridica) entregador.getPessoa());
            
            result.use(Results.json()).from(
                    entregadorPessoaJuridica, "result").include(
                            "entregador", "procuracaoEntregador", "procuracaoEntregador.cota", "pessoaJuridica").serialize();
            
        } else {
            
            result.nothing();
        }
    }
    
	                /**
     * Método responsável por remover um entregador a partir de seu ID.
     * 
     * @param idEntregador
     */
    @Rules(Permissao.ROLE_CADASTRO_ENTREGADOR_ALTERACAO)
    public void removerEntregador(final Long idEntregador) {
        
        if (idEntregador == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Id do entregador não pode ser nulo.");
        }
        
        entregadorService.removerEntregadorPorId(idEntregador);
        
        final ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Exclusão realizada com sucesso.");
        
        result.use(Results.json()).from(validacao, "result").recursive().serialize();
    }
    
	                /**
     * Método que prepara a tela para um novo cadastro.
     */
    @Rules(Permissao.ROLE_CADASTRO_ENTREGADOR_ALTERACAO)
    public void novoCadastro() {
        
        session.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
        session.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
        session.removeAttribute(LISTA_ENDERECOS_EXIBICAO);
        session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
        session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
        
        final boolean utilizaSugestaoIncrementoCodigo =
                distribuidorService.utilizaSugestaoIncrementoCodigo();
        
        Long novoCodigoEntregador = null;
        
        if(utilizaSugestaoIncrementoCodigo) {
            novoCodigoEntregador = entregadorService.obterMinCodigoEntregadorDisponivel();
        }
        
        final Map<String, Object> mapa = new TreeMap<String, Object>();
        mapa.put("data", DateUtil.formatarDataPTBR(new Date()));
        
        if (novoCodigoEntregador != null) {
            mapa.put("nextCodigo", novoCodigoEntregador);
        }
        
        result.use(CustomJson.class).from(mapa).serialize();
        
    }
    
	                /**
     * Método que obtém uma cota através de seu número.
     * 
     * @param numeroCota
     */
    public void obterCotaPorNumero(final Integer numeroCota) {
        
        final Cota cota = cotaService.obterPorNumeroDaCota(numeroCota);
        
        if (cota == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota inválida!");
        }
        
        final EnderecoCota endereco = cotaService.obterEnderecoPrincipal(cota.getId());
        
        final ProcuracaoCotaDTO procuracaoCota = parseCotaProcuracao(cota, endereco);
        
        result.use(Results.json()).from(procuracaoCota, "result").recursive().serialize();
    }
    
	                /**
     * Renderiza o arquivo de impressão de dividas
     * 
     * @param procuracaoImpressao
     * 
     * @throws IOException
     */
    public void imprimirProcuracao(final ProcuracaoImpressaoDTO procuracaoImpressao) throws Exception {
        
        
        final List<ProcuracaoImpressaoDTO> list = new ArrayList<ProcuracaoImpressaoDTO>();
        
        list.add(procuracaoImpressao);
        
        final ProcuracaoImpressaoWrapper procuracaoImpressaoWrapper = new ProcuracaoImpressaoWrapper();
        
        procuracaoImpressaoWrapper.setListaProcuracaoImpressao(list);
        
        final List<ProcuracaoImpressaoWrapper> listaWrapper = new ArrayList<ProcuracaoImpressaoWrapper>();
        
        listaWrapper.add(procuracaoImpressaoWrapper);
        
        final byte[] arquivo = entregadorService.getDocumentoProcuracao(listaWrapper);
        
        httpResponse.setContentType("application/pdf");
        
        httpResponse.setHeader("Content-Disposition", "attachment; filename=" + NOME_ARQUIVO_PROCURACAO + ".pdf");
        
        final OutputStream output = httpResponse.getOutputStream();
        
        output.write(arquivo);
        
        httpResponse.getOutputStream().close();
        
        result.use(Results.nothing());
    }
    
    public void validarDadosImpressao(final ProcuracaoImpressaoDTO procuracaoImpressao) {
        
        final List<String> listaMensagens = new ArrayList<String>();
        
        if (procuracaoImpressao.getNomeJornaleiro() == null || procuracaoImpressao.getNomeJornaleiro().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [Nome do Jornaleiro] é obrigatório.");
        }
        
        if (procuracaoImpressao.getBoxCota() == null || procuracaoImpressao.getBoxCota().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [Box] é obrigatório.");
        }
        
        if (procuracaoImpressao.getNacionalidade() == null || procuracaoImpressao.getNacionalidade().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [Nacionalidade do Entregador] é obrigatório.");
        }
        
        if (procuracaoImpressao.getEstadoCivil() == null || procuracaoImpressao.getEstadoCivil().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [Estado Civil do Entregador] é obrigatório.");
        }
        
        if (procuracaoImpressao.getEnderecoPDV() == null || procuracaoImpressao.getEnderecoPDV().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [Endereco do PDV] é obrigatório.");
        }
        
        if (procuracaoImpressao.getBairroPDV() == null || procuracaoImpressao.getBairroPDV().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [Bairro do PDV] é obrigatório.");
        }
        
        if (procuracaoImpressao.getCidadePDV() == null || procuracaoImpressao.getCidadePDV().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [Cidade do PDV] é obrigatório.");
        }
        
        if (procuracaoImpressao.getNumeroPermissao() == null || procuracaoImpressao.getNumeroPermissao().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [Número da permissão] é obrigatório.");
        }
        
        if (procuracaoImpressao.getRgJornaleiro() == null || procuracaoImpressao.getRgJornaleiro().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [RG do Jornaleiro] é obrigatório.");
        }
        
        if (procuracaoImpressao.getCpfJornaleiro() == null || procuracaoImpressao.getCpfJornaleiro().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [CPF do Jornaleiro] é obrigatório.");
        }
        
        if (procuracaoImpressao.getNomeProcurador() == null || procuracaoImpressao.getNomeProcurador().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [Nome do Procurador] é obrigatório.");
        }
        
        if (procuracaoImpressao.getRgProcurador() == null || procuracaoImpressao.getRgProcurador().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [RG do Procurador] é obrigatório.");
        }
        
        if (procuracaoImpressao.getEstadoCivilProcurador() == null || procuracaoImpressao.getEstadoCivilProcurador().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [Estado civil do procurador] é obrigatório.");
        }
        
        if (procuracaoImpressao.getNacionalidadeProcurador() == null || procuracaoImpressao.getNacionalidadeProcurador().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [Nacionalidade do Procurador] é obrigatório.");
        }
        
        if (procuracaoImpressao.getEnderecoDoProcurado() == null || procuracaoImpressao.getEnderecoDoProcurado().isEmpty()) {
            
            listaMensagens.add("O preenchimento do campo [Endereco do Procurado] é obrigatório.");
        }
        
        if (!listaMensagens.isEmpty()) {
            
            final ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagens);
            
            throw new ValidacaoException(validacao);
        }
    }
    
    @Post
    public void carregarAbaCota(final Long idEntregador){
        
        final Object[] dados = new Object[5];
        
        if (idEntregador != null){
            
            final Entregador entregador = entregadorService.buscarPorId(idEntregador);
            
            dados[0] = entregador.getPessoa().getNome();
            
            final Rota rota = entregador.getRota();
            if (rota != null){
                
                dados[1] = rota.getRoteiro() != null ? rota.getRoteiro().getDescricaoRoteiro() : "";
                dados[2] = rota.getRoteiro() != null && rota.getRoteiro().getRoteirizacao().getBox() != null ? rota.getRoteiro().getRoteirizacao().getBox().getNome() : "";
                dados[3] = entregador.getRota() != null ? entregador.getRota().getDescricaoRota() : "";
            } else {
                
                dados[1] = "";
                dados[2] = "";
                dados[3] = "";
            }
            
            final EntregadorCotaProcuracaoPaginacaoVO retorno =
                    entregadorService.buscarCotasAtendidas(idEntregador, 1, 15,
                            "numeroCota", "asc");
            
            final TableModel<CellModelKeyValue<EntregadorCotaProcuracaoVO>> tableModel =
                    new TableModel<CellModelKeyValue<EntregadorCotaProcuracaoVO>>();
            
            tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(retorno.getListaVO()));
            
            tableModel.setTotal(retorno.getTotalRegistros());
            
            tableModel.setPage(1);
            
            dados[4] = tableModel;
        } else {
            
            dados[0] = "";
            dados[1] = "";
            dados[2] = "";
            dados[3] = "";
            dados[4] = new TableModel<CellModelKeyValue<EntregadorCotaProcuracaoVO>>();
        }
        
        result.use(Results.json()).from(dados, "result").recursive().serialize();
    }
    
    public void atualizarGridCotas(final Long idEntregador, final int page, final int rp,
            final String sortname, final String sortorder){
        
        if (idEntregador == null){
            
            result.use(Results.json()).withoutRoot().from(new TableModel<CellModelKeyValue<EntregadorCotaProcuracaoVO>>()).recursive().serialize();
        } else {
            
            final EntregadorCotaProcuracaoPaginacaoVO retorno =
                    entregadorService.buscarCotasAtendidas(idEntregador, page, rp,
                            sortname, sortorder);
            
            final TableModel<CellModelKeyValue<EntregadorCotaProcuracaoVO>> tableModel =
                    new TableModel<CellModelKeyValue<EntregadorCotaProcuracaoVO>>();
            
            tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(retorno.getListaVO()));
            
            tableModel.setTotal(retorno.getTotalRegistros());
            
            tableModel.setPage(page);
            
            result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
        }
    }
    
    private void validarParametrosEntradaCadastroEntregador(final Pessoa pessoa,
            final String dataNascimento,
            final Long idEntregador,
            final Long codigoEntregador,
            final boolean isComissionado,
            final String percentualComissao,
            final Map<String, String> cpfCnpj) {
        
        final List<String> listaMensagens = new LinkedList<String>();
        
        if (codigoEntregador == null) {
            
            listaMensagens.add("O preenchimento do campo [Código do entregador] é obrigatório.");
            
        } else {
            
            final Entregador entregador = entregadorService.obterEntregadorPorCodigo(codigoEntregador);
            
            if(entregador!=null && !entregador.getId().equals(idEntregador)) {
                
                listaMensagens.add(" Valor do campo [Codigo] já esta sendo utilizado.");
                
            }
            
        }
        
        if (cpfCnpj.containsKey(CPF)) {
            
            
            
            if( ((PessoaFisica)pessoa).getRg() == null || ((PessoaFisica)pessoa).getRg().trim().isEmpty()) {
                
                listaMensagens.add("O preenchimento do campo [RG] é obrigatório.");
                
            } else if (((PessoaFisica)pessoa).getRg() != null && ((PessoaFisica)pessoa).getRg().length() >  PessoaUtil.RG_QUANTIDADE_DIGITOS) {
                
                listaMensagens.add("Quantidade de caracteres do campo [RG] excede o maximo de "
                        + PessoaUtil.RG_QUANTIDADE_DIGITOS + " dígitos");
            }
            
            if( ((PessoaFisica)pessoa).getOrgaoEmissor() == null || ((PessoaFisica)pessoa).getOrgaoEmissor().trim().isEmpty()) {
                listaMensagens.add("O preenchimento do campo [Orgão Emissor] é obrigatório.");
            }
            
            if( ((PessoaFisica)pessoa).getNome() == null || ((PessoaFisica)pessoa).getNome().isEmpty() ) {
                
                listaMensagens.add("O preenchimento do campo [Nome] é obrigatório.");
                
            }
            
            if( ((PessoaFisica)pessoa).getSexo() == null ) {
                
                listaMensagens.add("O preenchimento do campo [Sexo] é obrigatório.");
            }
            
            // Data de nascimento não é mais obrigatório - Definido em
            // 08/11/2012
			                                                /*
             * if(dataNascimento == null || dataNascimento.isEmpty()) {
             * 
             * listaMensagens.add(
             * "O preenchimento do campo [Data Nascimento] é obrigatório.");
             * 
             * } else
             */
            if (dataNascimento != null && !dataNascimento.isEmpty()) {
                if(!DateUtil.isValidDatePTBR(dataNascimento)){
                    
                    listaMensagens.add("Campo [Data Nascimento] inválido.");
                    
                }
            }
            
            final String cpfEntregador = cpfCnpj.get(CPF);
            
            if (cpfEntregador == null || cpfEntregador.isEmpty()) {
                
                listaMensagens.add("O preenchimento do campo [CPF] é obrigatório.");
                
            } else {
                
                final CPFValidator cpfValidator = new CPFValidator(true);
                
                try {
                    
                    cpfValidator.assertValid(cpfEntregador);
                    
                } catch(final InvalidStateException e) {
                    final String msg = "CPF inválido.";
                    LOGGER.debug(msg, e);
                    listaMensagens.add(msg);
                }
            }
            
        } else if (cpfCnpj.containsKey(CNPJ)) {
            
            if( ((PessoaJuridica)pessoa).getInscricaoEstadual() == null || ((PessoaJuridica)pessoa).getInscricaoEstadual().isEmpty()) {
                listaMensagens.add("O preenchimento do campo [Inscrição Estadual] é obrigatório.");
            }
            
            if( ((PessoaJuridica)pessoa).getRazaoSocial() == null || ((PessoaJuridica)pessoa).getRazaoSocial().isEmpty() ) {
                listaMensagens.add("O preenchimento do campo [Razão Social] é obrigatório.");
            }
            
            if( ((PessoaJuridica)pessoa).getNomeFantasia() == null || ((PessoaJuridica)pessoa).getNomeFantasia().isEmpty() ) {
                
                listaMensagens.add("O preenchimento do campo [Nome Fantasia] é obrigatório.");
                
            }
            
            final String cnpjEntregador = cpfCnpj.get(CNPJ);
            
            if (cnpjEntregador == null || cnpjEntregador.isEmpty()) {
                
                listaMensagens.add("O preenchimento do campo [CNPJ] é obrigatório.");
                
            } else {
                
                final CNPJValidator cnpjValidator = new CNPJValidator(true);
                
                try {
                    
                    cnpjValidator.assertValid(cnpjEntregador);
                    
                } catch(final InvalidStateException e) {
                    final String msg = "CNPJ inválido.";
                    LOGGER.debug(msg, e);
                    listaMensagens.add(msg);
                }
            }
            
            validarEndereco(listaMensagens);
            
        }
        
        validarTelefone(listaMensagens);
        
        if (isComissionado && percentualComissao == null) {
            
            listaMensagens.add("O preenchimento do campo [Percentual da comissão] é obrigatório.");
            
        } else if (isComissionado && percentualComissao != null) {
            
            try {
                
                if (Double.valueOf(getValorSemMascara(percentualComissao)) > 100) {
                    listaMensagens.add("O valor máximo de percentual de comissão não pode ultrapassar 100%.");
                }
                
            } catch (final NumberFormatException e) {
                LOGGER.debug(e.getMessage(), e);
                listaMensagens.add("O preenchimento do campo [Percentual da comissão] não é válido.");
            }
        }
        
        
        if (!listaMensagens.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, listaMensagens);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void validarEndereco(final List<String> listaMensagens) {
        
        final List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar =
                (List<EnderecoAssociacaoDTO>) session.getAttribute(
                        LISTA_ENDERECOS_SALVAR_SESSAO);
        
        
        if (listaEnderecoAssociacaoSalvar == null || listaEnderecoAssociacaoSalvar.isEmpty()) {
            
            listaMensagens.add("Pelo menos um endereço deve ser cadastrado para o entregador.");
            
        } else {
            
            boolean temPrincipal = false;
            
            for (final EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacaoSalvar) {
                
                if (enderecoAssociacao.isEnderecoPrincipal()) {
                    
                    temPrincipal = true;
                    
                    break;
                }
            }
            
            if (!temPrincipal) {
                
                listaMensagens.add("Deve haver ao menos um endereço principal para o entregador.");
            }
        }
        
        
    }
    
    private void validarTelefone(final List<String> listaMensagens) {
        
        final Map<Integer, TelefoneAssociacaoDTO> map = this.obterTelefonesSalvarSessao();
        
        if (map.isEmpty()) {
            
            listaMensagens.add("Pelo menos um telefone deve ser cadastrado para o entregador.");
            
        } else {
            
            boolean temPrincipal = false;
            
            for (final Entry<Integer, TelefoneAssociacaoDTO> entry : map.entrySet()){
                
                final TelefoneAssociacaoDTO telefoneAssociacaoDTO = entry.getValue();
                
                if (telefoneAssociacaoDTO.isPrincipal()) {
                    
                    temPrincipal = true;
                    
                    break;
                }
            }
            
            if (!temPrincipal) {
                
                listaMensagens.add("Deve haver ao menos um telefone principal para o entregador.");
            }
        }
        
        
    }
    
    
	                /*
     * Método responsável por processar os endereços do entregador.
     */
    @SuppressWarnings("unchecked")
    private void processarEnderecosEntregador(final Long idEntregador) {
        
        final List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar =
                (List<EnderecoAssociacaoDTO>) session.getAttribute(
                        LISTA_ENDERECOS_SALVAR_SESSAO);
        
        final List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover =
                (List<EnderecoAssociacaoDTO>) session.getAttribute(
                        LISTA_ENDERECOS_REMOVER_SESSAO);
        
        entregadorService.processarEnderecos(idEntregador,
                listaEnderecoAssociacaoSalvar,
                listaEnderecoAssociacaoRemover);
        
        session.setAttribute(EnderecoController.ENDERECO_PENDENTE, Boolean.FALSE);
        session.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
        session.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
    }
    
    
    private ProcuracaoCotaDTO parseCotaProcuracao(final Cota cota, final EnderecoCota enderecoCota) {
        
        final ProcuracaoCotaDTO cotaProcuracao = new ProcuracaoCotaDTO();
        
        if (cota == null || !(cota.getPessoa() instanceof PessoaFisica)) {
            return null;
        }
        
        final PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
        
        cotaProcuracao.setNumeroCota(cota.getNumeroCota());
        cotaProcuracao.setBox(cota.getBox().getCodigo() + " - " + cota.getBox().getNome());
        
        cotaProcuracao.setNacionalidade(pessoaFisica.getNacionalidade());
        cotaProcuracao.setNomeJornaleiro(pessoaFisica.getNome());
        cotaProcuracao.setRg(pessoaFisica.getRg());
        cotaProcuracao.setCpf(pessoaFisica.getCpf());
        cotaProcuracao.setEstadoCivil(pessoaFisica.getEstadoCivil());
        
        if (enderecoCota != null) {
            
            final Endereco endereco = enderecoCota.getEndereco();
            
            cotaProcuracao.setBairro(endereco.getBairro());
            cotaProcuracao.setCep(endereco.getCep());
            cotaProcuracao.setCidade(endereco.getCidade());
            cotaProcuracao.setEnderecoPDVPrincipal(endereco.getLogradouro());
        }
        
        return cotaProcuracao;
    }
    
	                /*
     * Método responsável por processar os telefones do entregador.
     */
    private void processarTelefonesEntregador(final Long idEntregador){
        
        final Map<Integer, TelefoneAssociacaoDTO> map = this.obterTelefonesSalvarSessao();
        
        final List<TelefoneEntregador> lista = new ArrayList<TelefoneEntregador>();
        
        for (final Entry<Integer, TelefoneAssociacaoDTO> entry : map.entrySet()){
            
            final TelefoneAssociacaoDTO telefoneAssociacaoDTO = entry.getValue();
            
            if (telefoneAssociacaoDTO.getTipoTelefone() != null){
                
                telefoneService.validarTelefone(
                        telefoneAssociacaoDTO.getTelefone(), telefoneAssociacaoDTO.getTipoTelefone());
                
                final TelefoneEntregador telefoneEntregador = new TelefoneEntregador();
                telefoneEntregador.setPrincipal(telefoneAssociacaoDTO.isPrincipal());
                final TelefoneDTO dto = telefoneAssociacaoDTO.getTelefone();
                final Telefone telefone = new Telefone(dto.getId(), dto.getNumero(), dto.getRamal(), dto.getDdd(), null);
                telefoneEntregador.setTelefone(telefone);
                telefoneEntregador.setTipoTelefone(telefoneAssociacaoDTO.getTipoTelefone());
                
                if (entry.getKey() > 0) {
                    
                    telefoneEntregador.setId(entry.getKey().longValue());
                }
                
                lista.add(telefoneEntregador);
            }
        }
        
        final Set<Long> telefonesRemover = this.obterTelefonesRemoverSessao();
        entregadorService.processarTelefones(idEntregador, lista, telefonesRemover);
        
        session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
        session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
    }
    
	                /*
     * Método que obtém os telefones a serem salvos, que estão na sessão.
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, TelefoneAssociacaoDTO> obterTelefonesSalvarSessao(){
        
        Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = (Map<Integer, TelefoneAssociacaoDTO>)
                session.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
        
        if (telefonesSessao == null){
            
            telefonesSessao = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
        }
        
        return telefonesSessao;
    }
    
	                /*
     * Método que obtém os telefones a serem removidos, que estão na sessão.
     */
    @SuppressWarnings("unchecked")
    private Set<Long> obterTelefonesRemoverSessao(){
        Set<Long> telefonesSessao = (Set<Long>)
                session.getAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
        
        if (telefonesSessao == null){
            telefonesSessao = new HashSet<Long>();
        }
        
        return telefonesSessao;
    }
    
    private String obterDocumentoFormatado(final CNPJFormatter cnpjFormatter, final CPFFormatter cpfFormatter, final Pessoa pessoa) {
        
        String documento = null;
        
        if(pessoa instanceof PessoaJuridica) {
            documento = ((PessoaJuridica) pessoa).getCnpj();
            try {
                return cnpjFormatter.format(documento);
            } catch (final IllegalArgumentException e) {
                LOGGER.debug(e.getMessage(), e);
                return "";
            }
        } else {
            documento = ((PessoaFisica) pessoa).getCpf();
            try {
                return cpfFormatter.format(documento);
            } catch (final IllegalArgumentException e) {
                LOGGER.debug(e.getMessage(), e);
                return "";
            }
        }
        
    }
    
	                /*
     * Método que cria um table model baseado no retorno da pesquisa de
     * entregadores.
     */
    @SuppressWarnings("deprecation")
    private TableModel<CellModel> obterTableModel(final List<Entregador> listaEntregador) {
        
        final CNPJFormatter cnpjFormatter = new CNPJFormatter();
        final CPFFormatter cpfFormatter = new CPFFormatter();
        
        final TableModel<CellModel> tableModel = new TableModel<CellModel>();
        
        final List<CellModel> listaCellModel = new ArrayList<CellModel>();
        
        for (final Entregador entregador : listaEntregador) {
            
            final String nome = entregador.getPessoa() instanceof PessoaJuridica ?
                    ((PessoaJuridica) entregador.getPessoa()).getRazaoSocial() :
                        ((PessoaFisica) entregador.getPessoa()).getNome();
                    
                    final String documento = obterDocumentoFormatado(cnpjFormatter, cpfFormatter, entregador.getPessoa());
                    
                    
                    final String apelido = entregador.getPessoa() instanceof PessoaJuridica ?
                            ((PessoaJuridica) entregador.getPessoa()).getNomeFantasia() :
                                ((PessoaFisica) entregador.getPessoa()).getApelido();
                            
                            final String email = entregador.getPessoa().getEmail();
                            
                            String telefone = "";
                            
                            for (final TelefoneEntregador telefoneEntregador : entregador.getTelefones()) {
                                
                                if (telefoneEntregador.isPrincipal()) {
                                    
                                    telefone = obterTelefoneFormatado(telefoneEntregador.getTelefone());
                                    
                                }
                            }
                            
                            final CellModel cellModel = new CellModel(
                                    entregador.getId().intValue(),
                                    formatField(entregador.getCodigo()),
                                    nome,
                                    documento,
                                    apelido,
                                    telefone,
                                    email);
                            
                            listaCellModel.add(cellModel);
        }
        
        tableModel.setRows(listaCellModel);
        
        return tableModel;
    }
    
    private String obterTelefoneFormatado(final Telefone telefonePrincipal) {
        
        final StringBuilder telefoneFormatado = new StringBuilder();
        
        if(telefonePrincipal == null) {
            return "";
        }
        
        if(telefonePrincipal.getDdd()!=null) {
            telefoneFormatado.append("(");
            telefoneFormatado.append(telefonePrincipal.getDdd());
            telefoneFormatado.append(") ");
        }
        
        if(telefonePrincipal.getNumero()!=null) {
            telefoneFormatado.append(telefonePrincipal.getNumero());
        }
        
        return telefoneFormatado.toString();
        
    }
    
	                /*
     * Método que realiza a formatação dos dados a serem apresentados na grid.
     */
    private static String formatField(final Object obj) {
        
        return obj == null ? "" : String.valueOf(obj);
    }
    
	                /*
     * Método que prepara o filtro para utilização na pesquisa.
     */
    private FiltroEntregadorDTO prepararFiltroEntregador(
            final FiltroEntregadorDTO filtroEntregador, final int page, final String sortname, final String sortorder, final int rp) {
        
        final PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
        
        filtroEntregador.setPaginacao(paginacao);
        
        filtroEntregador.setOrdenacaoColunaEntregador(
                Util.getEnumByStringValue(OrdenacaoColunaEntregador.values(), sortname));
        
        return filtroEntregador;
    }
    
    
    private String getValorSemMascara(String valor) {
        
        valor = valor.replaceAll(",", "\\.");
        
        return valor;
    }
    
    /**
     * Efetua consulta pelo nome da cota informado, utilizado para auto complete da tela
     * 
     * @param nomeCota - nome da cota
     */
    @Post
    public void autoCompletarPorNome(String nome) {
        
        nome = PessoaUtil.removerSufixoDeTipo(nome);
        
        final List<Entregador> listaEntregador = entregadorService.obterEntregadoresPorNome(nome);
        
        final List<ItemAutoComplete> listaEntregadorAutoComplete = new ArrayList<ItemAutoComplete>();
        
        if (listaEntregador != null && !listaEntregador.isEmpty()) {
            
            for (final Entregador entregador : listaEntregador) {
                
                final String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(entregador.getPessoa());
                
                final EntregadorDTO entregadorDTO = new EntregadorDTO(entregador.getId(), nomeExibicao);
                
                listaEntregadorAutoComplete.add(new ItemAutoComplete(nomeExibicao, null, entregadorDTO));
            }
        }
        
        result.use(Results.json()).from(listaEntregadorAutoComplete, "result").include("value", "chave").serialize();
    }
    
    /**
     * Efetua consulta pelo nome da cota informado
     * 
     * @param nomeCota - nome da cota
     */
    @Post
    public void pesquisarPorNome(String nome) {
        
        nome = PessoaUtil.removerSufixoDeTipo(nome);
        
        final Entregador entregador = entregadorService.obterPorNome(nome);
        
        if (entregador == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Entregador \"" + nome + "\" não encontrada!");
        }
        
        final String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(entregador.getPessoa());
        
        final EntregadorDTO entregadorDTO = new EntregadorDTO(entregador.getId(), nomeExibicao);
        
        result.use(Results.json()).from(entregadorDTO, "result").serialize();
    }
}