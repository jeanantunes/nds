package br.com.abril.nds.controllers.cadastro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.BancoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO.OrdenacaoColunaBancos;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.PessoaService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela tela de consulta, cadastro e alteração de Bancos.
 * 
 * @author Discover Technology
 * 
 */

@Resource
@Path("/banco")
@Rules(Permissao.ROLE_CADASTRO_BANCO)
public class BancoController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BancoController.class);
    
    @Autowired
    private BancoService bancoService;
    
    @Autowired
    private PessoaService pessoaService;
    
    private final Result result;
    
    private final HttpSession httpSession;
    
    private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaBancos";
    
    private static final String APELIDO_ANTIGO_SESSION_ATTRIBUTE = "apelidoAntigo";
    
    /**
     * Construtor da classe
     * 
     * @param result
     * @param httpSession
     * @param httpResponse
     */
    public BancoController(final Result result, final HttpSession httpSession) {
        this.result = result;
        this.httpSession = httpSession;
    }
    
    /**
     * Método de chamada da página
     */
    @Get
    @Path("/")
    public void bancos() {
        
    }
    
    /**
     * Método de consulta de bancos
     * 
     * @param nome
     * @param numero
     * @param cedente
     * @param status
     * @param sortorder
     * @param sortname
     * @param page
     * @param rp
     * @throws Mensagem de nenhum registro encontrado
     */
    @Post
    @Path("/consultaBancos")
    public void consultaBancos(final String nome, final String numero, final String cedente, final boolean ativo,
            final String sortorder, final String sortname, final int page, final int rp) {
        
        // CONFIGURAR PAGINA DE PESQUISA
        final FiltroConsultaBancosDTO filtroAtual = new FiltroConsultaBancosDTO(nome != null ? nome.trim() : null,
                numero != null ? numero.trim() : null, cedente != null ? cedente.trim() : null, ativo);
        final PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
        filtroAtual.setPaginacao(paginacao);
        filtroAtual.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaBancos.values(), sortname));
        
        final FiltroConsultaBancosDTO filtroSessao = (FiltroConsultaBancosDTO) httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
        
        if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
            filtroAtual.getPaginacao().setPaginaAtual(1);
        }
        
        httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);
        
        // BUSCA BANCOS
        final List<Banco> bancos = bancoService.obterBancos(filtroAtual);
        
        // CARREGA DIRETO DA ENTIDADE PARA A TABELA
        final List<CellModel> listaModelo = new LinkedList<CellModel>();
        
        if (bancos.size() == 0) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
        }
        
        for (final Banco banco : bancos) {
            listaModelo.add(new CellModel(
                    1, 
                    banco.getId() != null ? banco.getId().toString() : "", 
                    banco.getNumeroBanco() != null ? banco.getNumeroBanco() : "", 
                    banco.getNome() != null ? banco.getNome() : "", 
                    banco.getAgencia() != null ? banco.getAgencia().toString() + "-" + banco.getDvAgencia() : "",
                    banco.getConta() != null ? banco.getConta().toString() + (banco.getDvConta() != null ? "-" + banco.getDvConta() : "") : "", 
                    banco.getCodigoCedente() != null ? banco.getCodigoCedente().toString() : "",
                    banco.getApelido() != null ? banco.getApelido().toString() : "",
                    banco.getCarteira() != null ? banco.getCarteira() : "", 
                    banco.isAtivo() ? "Ativo" : "Desativado",
                    ""));
        }
        
        final TableModel<CellModel> tm = new TableModel<CellModel>();
        
        // DEFINE TOTAL DE REGISTROS NO TABLEMODEL
        tm.setTotal((int) bancoService.obterQuantidadeBancos(filtroAtual));
        
        // DEFINE CONTEUDO NO TABLEMODEL
        tm.setRows(listaModelo);
        
        // DEFINE PAGINA ATUAL NO TABLEMODEL
        tm.setPage(filtroAtual.getPaginacao().getPaginaAtual());
        
        // PREPARA RESULTADO PARA A VIEW (HASHMAP)
        final Map<String, TableModel<CellModel>> resultado = new HashMap<String, TableModel<CellModel>>();
        resultado.put("TblModelBancos", tm);
        
        // RETORNA HASHMAP EM FORMATO JSON PARA A VIEW
        result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
        
    }
    
    /**
     * Método responsável pela inclusão de novo Banco
     * 
     * @param numero
     * @param nome
     * @param codigoCedente
     * @param agencia
     * @param conta
     * @param digito
     * @param apelido
     * @param carteira
     * @param juros
     * @param ativo
     * @param multa
     * @param vrMulta
     * @param instrucoes
     */
    @Post
    @Path("/novoBanco")
    @Rules(Permissao.ROLE_CADASTRO_BANCO_ALTERACAO)
    public void novoBanco(final String numero, final String nome, final String codigoCedente, final String digitoCodigoCedente, final String agencia,
            final String digitoAgencia, final String conta, final String digito, final String apelido, final Long idPessoaCedente,
            final Integer carteira, final BigDecimal juros, final boolean ativo, final BigDecimal multa,
            final BigDecimal vrMulta, final String instrucoes1, final String instrucoes2, final String instrucoes3, final String instrucoes4,
            String convenio) {
        
        if (bancoService.obterBancoPorApelido(apelido) != null) {
            throw new ValidacaoException(TipoMensagem.ERROR, "Já existe um banco com este apelido.");
        }
        
        validarCadastroBanco(numero, nome, codigoCedente, idPessoaCedente, agencia, digitoAgencia, conta, digito, apelido, juros, multa, vrMulta);
        
        final long lAgencia = Long.parseLong(agencia);
        final long lConta = Long.parseLong(conta);
        
        final Banco banco = new Banco();
        banco.setNumeroBanco(numero);
        banco.setNome(nome);
        banco.setPessoaJuridicaCedente(pessoaService.buscarPessoaPorId(idPessoaCedente));
        banco.setCodigoCedente(codigoCedente);
        banco.setDigitoCodigoCedente(digitoCodigoCedente);
        banco.setAgencia(lAgencia);
        banco.setDvAgencia(digitoAgencia);
        banco.setConta(lConta);
        banco.setDvConta(digito);
        banco.setApelido(apelido);
        banco.setCarteira(carteira);
        banco.setJuros(juros);
        banco.setAtivo(ativo);
        banco.setMulta(multa);
        banco.setVrMulta(vrMulta);
        banco.setInstrucoes1(instrucoes1);
        banco.setInstrucoes2(instrucoes2);
        banco.setInstrucoes3(instrucoes3);
        banco.setInstrucoes4(instrucoes4);
        banco.setConvenio(convenio);
        
        try {
            bancoService.incluirBanco(banco);
        } catch (final DataIntegrityViolationException e) {
            final String msg = "Já existe outro registro com este Número de Banco, Agência, Dígito da Agência, Conta e Dígito da Conta.";
            LOGGER.error(msg, e);
            throw new ValidacaoException(TipoMensagem.ERROR, msg);
        }
        
        result.use(Results.json()).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Banco " + nome + " cadastrado com sucesso."), "result")
                .recursive().serialize();
    }
    
    /**
     * Método responsável por buscar os dados do banco para alteração.
     * 
     * @param idBanco
     * @throws Mensagem de banco não encontrado.
     */
    @Post
    @Path("/buscaBanco")
    public void buscaBanco(final long idBanco) {
    	
        final BancoVO bancoVO = bancoService.obterDadosBanco(idBanco);
        
        if (bancoVO == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Banco " + idBanco + " não encontrado.");
        }
        
        httpSession.setAttribute(APELIDO_ANTIGO_SESSION_ATTRIBUTE, bancoVO.getApelido());
        
        result.use(Results.json()).from(bancoVO, "result").serialize();
    }
    
    /**
     * Método responsável pela alteração de um Banco
     * 
     * @param idBanco
     * @param numero
     * @param nome
     * @param codigoCedente
     * @param agencia
     * @param conta
     * @param digito
     * @param apelido
     * @param carteira
     * @param juros
     * @param ativo
     * @param multa
     * @param vrMulta
     * @param instrucoes
     * @throws Mensagem de pendencias financeiras do banco
     */
    @Post
    @Path("/alteraBanco")
    @Rules(Permissao.ROLE_CADASTRO_BANCO_ALTERACAO)
    public void alteraBanco(final long idBanco, final String numero, final String nome, final String codigoCedente, final String digitoCodigoCedente,
            final String agencia, final String digitoAgencia, final String conta, final String digito,
            final String apelido, final Integer carteira, final BigDecimal juros, final boolean ativo,
            final BigDecimal multa, final BigDecimal vrMulta, final long idPessoaCedente, final String instrucoes1,
            final String instrucoes2, final String instrucoes3, final String instrucoes4, String convenio) {
        
        if (!httpSession.getAttribute(APELIDO_ANTIGO_SESSION_ATTRIBUTE).equals(apelido)
                && bancoService.obterBancoPorApelido(apelido) != null) {
            throw new ValidacaoException(TipoMensagem.ERROR, "Já existe um banco com este apelido.");
        }
        
        validarCadastroBanco(numero, nome, codigoCedente, idPessoaCedente, agencia, digitoAgencia, conta, digito, apelido, juros, multa, vrMulta);
        
        if (!ativo) {
            if (bancoService.verificarPendencias(idBanco)) {
                throw new ValidacaoException(TipoMensagem.WARNING, "O banco " + nome
                        + " possui pendências e não pode ser desativado.");
            }
        }
        
        final long lAgencia = Long.parseLong(agencia);
        final long lConta = Long.parseLong(conta);
        
        final Banco banco = bancoService.obterBancoPorId(idBanco);
        banco.setNumeroBanco(numero);
        banco.setNome(nome);
        banco.setPessoaJuridicaCedente(pessoaService.buscarPessoaPorId(idPessoaCedente));
        banco.setCodigoCedente(codigoCedente);
        banco.setDigitoCodigoCedente(digitoCodigoCedente);
        banco.setAgencia(lAgencia);
        banco.setDvAgencia(digitoAgencia);
        banco.setConta(lConta);
        banco.setDvConta(digito);
        banco.setApelido(apelido);
        banco.setCarteira(carteira);
        banco.setJuros(juros);
        banco.setAtivo(ativo);
        banco.setMulta(multa);
        banco.setVrMulta(vrMulta);
        banco.setInstrucoes1(instrucoes1);
        banco.setInstrucoes2(instrucoes2);
        banco.setInstrucoes3(instrucoes3);
        banco.setInstrucoes4(instrucoes4);
        banco.setConvenio(convenio);
        
        try {
            bancoService.alterarBanco(banco);
        } catch (final DataIntegrityViolationException e) {
            final String msg = "Já existe outro registro com este Número de Banco, Agência, Dígito da Agência, Conta e Dígito da Conta.";
            LOGGER.error(msg, e);
            throw new ValidacaoException(TipoMensagem.ERROR, msg);
        }
        
        result.use(Results.json()).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Banco " + nome + " alterado com sucesso."), "result")
                .recursive().serialize();
    }
    
    /**
     * Método responsável por validar os dados de um novo banco ou de uma
     * alteração de banco.
     * 
     * @param numero
     * @param nome
     * @param codigoCedente
     * @param agencia
     * @param digitoAgencia
     * @param conta
     * @param digito
     * @param apelido
     * @param juros
     * @param multa
     * @param vrMulta
     */
    private void validarCadastroBanco(final String numero, final String nome, final String codigoCedente, final Long idPessoaCedente,
            final String agencia, final String digitoAgencia, final String conta, final String digito,
            final String apelido, final BigDecimal juros, final BigDecimal multa, final BigDecimal vrMulta) {
        
        final List<String> errorMsgs = new LinkedList<String>();
        
        if (idPessoaCedente == null || idPessoaCedente < 0) {
            errorMsgs.add("Selecione o Cedente.");
        }
        
        if (StringUtils.isBlank(numero)) {
            errorMsgs.add("Preencha o número do banco.");
        }
        
        if (StringUtils.isBlank(nome)) {
            errorMsgs.add("Preencha o nome do banco.");
        }
        
        if (StringUtils.isBlank(codigoCedente)) {
            errorMsgs.add("Preencha o código do cedente.");
        }
        
        if (StringUtils.isBlank(agencia)) {
            errorMsgs.add("Preencha o campo agência.");
        }
        
        if (StringUtils.isBlank(digitoAgencia)) {
            errorMsgs.add("Preencha o campo dígito da agência.");
        }
        
        if (StringUtils.isBlank(conta)) {
            errorMsgs.add("Preencha o campo conta.");
        }
        
        if (StringUtils.isBlank(apelido)) {
            errorMsgs.add("Preencha o campo apelido.");
        }
        
        if (juros == null) {
            errorMsgs.add("Especifique a taxa de juros.");
        }
        
        if (multa == null && vrMulta == null) {
            errorMsgs.add("Especifique a taxa ou o valor da multa.");
        }
        
        if (errorMsgs != null && !errorMsgs.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, errorMsgs);
        }
        
    }
    
    /**
     * Método responsável por desativar um banco.
     * 
     * @param idBanco
     * @throws Mansagens de validação segundo as regras de desativação de banco
     */
    @Post
    @Path("/excluirBanco")
    @Rules(Permissao.ROLE_CADASTRO_BANCO_ALTERACAO)
    public void excluirBanco(final long idBanco) {
        
        final Banco banco = bancoService.obterBancoPorId(idBanco);
        final String nomebanco = banco.getNome();
        
        String msg = null;
        TipoMensagem tipoMsg = TipoMensagem.SUCCESS;
        
        try {
            bancoService.excluirBanco(idBanco);
            msg = "Banco " + nomebanco + " excluido com sucesso.";
        } catch (final DataIntegrityViolationException e) {
            
            if (!banco.isAtivo()) {
                tipoMsg = TipoMensagem.WARNING;
                msg = "Banco " + nomebanco + " está em uso, não pode ser excluido.";
            } else {
                bancoService.dasativarBanco(idBanco);
                msg = "Banco " + nomebanco + " desativado com sucesso.";
            }
            
            LOGGER.error(msg, e);
        }
        
        result.use(Results.json()).from(new ValidacaoVO(tipoMsg, msg), "result").recursive().serialize();
    }
    
    @Post
    public void autoCompletarPorNomeBanco(final String nomeBanco) {
        
        final List<Banco> listabancos = bancoService.obterBancosPorNome(nomeBanco,
                Constantes.QTD_MAX_REGISTROS_AUTO_COMPLETE);
        
        final List<ItemAutoComplete> listaCotasAutoComplete = new ArrayList<ItemAutoComplete>();
        
        if (listabancos != null && !listabancos.isEmpty()) {
            
            for (final Banco banco : listabancos) {
                
                listaCotasAutoComplete.add(new ItemAutoComplete(banco.getNome(), null, banco.getId()));
            }
        }
        
        result.use(Results.json()).from(listaCotasAutoComplete, "result").include("value", "chave").serialize();
    }
    
    @Post
    public void obterPessoasDisponiveisParaCedente() {
    	
    	final List<ItemDTO<Long, String>> pessoasCedente = bancoService.obterPessoasDisponiveisParaCedente();

    	if (pessoasCedente != null) {
    		result.include("listaPessoas", pessoasCedente);
    	}

    	result.use(CustomJson.class).from(pessoasCedente).serialize();
    }
    
}