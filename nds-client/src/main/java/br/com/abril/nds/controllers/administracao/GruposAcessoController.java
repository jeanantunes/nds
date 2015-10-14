package br.com.abril.nds.controllers.administracao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ResultadoGrupoVO;
import br.com.abril.nds.client.vo.ResultadoPermissaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AcessoDTO;
import br.com.abril.nds.dto.GrupoPermissaoDTO;
import br.com.abril.nds.dto.UsuarioDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaGrupoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaPermissaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.GrupoPermissao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.RestricoesAcesso;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.GrupoPermissaoService;
import br.com.abril.nds.service.PermissaoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * @author infoA2 Controller dos grupos de acesso
 */
@Resource
@Rules(Permissao.ROLE_ADMINISTRACAO_GRUPOS_ACESSO)
@Path("/administracao/gruposAcesso")
public class GruposAcessoController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GruposAcessoController.class);
    
    private static final String FILTRO_PESQUISA_CONSULTA_PERMISSAO_SESSION_ATTRIBUTE = "filtroPesquisaConsultaPermissao";
    
    private static final String FILTRO_PESQUISA_CONSULTA_GRUPO_SESSION_ATTRIBUTE = "filtroPesquisaConsultaGrupo";
    
    @Autowired
    private Result result;
    
    @Autowired
    private GrupoPermissaoService grupoPermissaoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private PermissaoService permissaoService;
    
    @Autowired
    private HttpSession session;
    
    public GruposAcessoController() {
        super();
    }
    
    @Path("/")
    public void index() {
        
    }
    
    public void obterPermissoes() {
        
        final List<AcessoDTO> permissoes = new ArrayList<AcessoDTO>();
        
        for (final Permissao p : Permissao.values()) {
            AcessoDTO dto = null;
            
            if (!p.isPermissaoAlteracao()) {
                dto = new AcessoDTO(p);
            } else if (p.isPermissaoAlteracao() && !p.isPermissaoMenu() && p.getPermissaoPai() != null) {
                dto = new AcessoDTO();
                dto.setAlteracao(p);
                dto.setDescricao(p.getDescricao());
                dto.setPai(p.getPermissaoPai());
                dto.setObservacao(p.getObservacao());
            }
            
            if (dto != null) {
                
                if (p.getRestricoesAcesso() != null) {
                    
                    this.tratarRestricoesAcesso(p.getRestricoesAcesso(), dto);
                }
                
                permissoes.add(dto);
            }
        }
        
        result.use(FlexiGridJson.class).from(permissoes).total(permissoes.size()).page(1).serialize();
        
    }
    
    private void tratarRestricoesAcesso(final RestricoesAcesso restricao, final AcessoDTO dto) {
        
        switch (restricao) {
        
        case CONF_CEGA_RECEBIMENTO_FISICO:
            this.tratarRestricaoAcessoConferenciaCegaRecebimentoFisico(dto);
            break;
        case CONF_CEGA_FECHAMENTO_ENCALHE:
            this.tratarRestricaoAcessoConferenciaCegaFechamentoEncalhe(dto);
            break;
        default:
            break;
        }
    }
    
    private void tratarRestricaoAcessoConferenciaCegaRecebimentoFisico(final AcessoDTO dto) {
        dto.setHabilitado(distribuidorService.isConferenciaCegaRecebimentoFisico());
    }
    
    private void tratarRestricaoAcessoConferenciaCegaFechamentoEncalhe(final AcessoDTO dto) {
        dto.setHabilitado(distribuidorService.isConferenciaCegaFechamentoEncalhe());
    }
    
    /**
     * 
     */
    @Get
    @Path("/excluirGrupoPermissao")
    @Rules(Permissao.ROLE_ADMINISTRACAO_GRUPOS_ACESSO_ALTERACAO)
    public void excluirGrupoPermissao(final Long codigoGrupo) {
        grupoPermissaoService.excluir(codigoGrupo);
        result.use(Results.json()).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Grupo de Permissões excluído com Sucesso."), "result")
                .recursive().serialize();
    }
    
    /**
     * 
     */
    @Get
    @Path("/novoGrupoPermissao")
    public void novoGrupoPermissao() {
        editarGrupoPermissao(null);
    }
    
    /**
     * @param filtro
     * @param rp
     * @param page
     * @param sortname
     * @param sortorder
     */
    @Get
    @Path("/editarGrupoPermissao")
    @Rules(Permissao.ROLE_ADMINISTRACAO_GRUPOS_ACESSO_ALTERACAO)
    public void editarGrupoPermissao(final Long codigoGrupo) {
        
        final GrupoPermissao grupoPermissao = grupoPermissaoService.buscar(codigoGrupo);
        
        if ( grupoPermissao == null )
        	throw new ValidacaoException(TipoMensagem.ERROR, "Grupo de Permissões não encontrado.");
        final GrupoPermissaoDTO dto = new GrupoPermissaoDTO(grupoPermissao.getId(), grupoPermissao.getNome(),
                new ArrayList<Permissao>(grupoPermissao.getPermissoes()));
        
        result.use(Results.json()).from(dto, "result").recursive().serialize();
    }
    
    /**
     * @param dataDe
     * @param dataAte
     */
    private void validarDadosGrupoPermissao(final GrupoPermissaoDTO grupoPermissaoDTO) {
        final List<String> listaMensagemValidacao = new ArrayList<String>();
        
        if (grupoPermissaoDTO.getNome() == null || grupoPermissaoDTO.getNome().isEmpty()) {
            listaMensagemValidacao.add("O preenchimento do campo nome é obrigatório!");
        }
        
        if (grupoPermissaoDTO.getPermissoes() == null || grupoPermissaoDTO.getPermissoes().isEmpty()) {
            listaMensagemValidacao.add("É necessário selecionar ao menos uma permissão!");
        }
        
        if (!listaMensagemValidacao.isEmpty()) {
            final ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.WARNING, listaMensagemValidacao);
            throw new ValidacaoException(validacaoVO);
        }
        
    }
    
    /**
     * @param codigoGrupo
     */
    @Post
    @Path("/salvarGrupoPermissao")
    @Rules(Permissao.ROLE_ADMINISTRACAO_GRUPOS_ACESSO_ALTERACAO)
    public void salvarGrupoPermissao(final GrupoPermissaoDTO grupoPermissaoDTO) throws Exception {
        
        this.validarDadosGrupoPermissao(grupoPermissaoDTO);
        
        final GrupoPermissao grupoPermissao = new GrupoPermissao();
        grupoPermissao.setId(grupoPermissaoDTO.getId());
        grupoPermissao.setNome(grupoPermissaoDTO.getNome());
        
        if (grupoPermissaoDTO.getPermissoes() == null) {
            grupoPermissaoDTO.setPermissoes(new ArrayList<Permissao>());
        }
        
        addPais(grupoPermissaoDTO.getPermissoes());
        
        grupoPermissao.setPermissoes(new HashSet<Permissao>(grupoPermissaoDTO.getPermissoes()));
        
        grupoPermissaoService.salvar(grupoPermissao);
        
        result.use(Results.json()).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Grupo de Permissões salvo com Sucesso."), "result").recursive()
                .serialize();
        
    }
    
    private void addPais(final List<Permissao> permissoes) {
        
        final List<Permissao> pais = new ArrayList<Permissao>();
        
        for (final Permissao permissao : permissoes) {
            
            final Permissao pai = permissao.getPermissaoPai();
            
            boolean paiEncontrado = false;
            
            for (final Permissao pComparar : permissoes) {
                if (pai != null && pai.equals(pComparar)) {
                    paiEncontrado = true;
                    continue;
                }
            }
            
            if (!paiEncontrado) {
                pais.add(pai);
            }
        }
        
        permissoes.addAll(pais);
    }
    
    /**
     * Retorna a lista de grupos do sistema
     * 
     * @return
     */
    @Get
    @Path("/pesquisarGrupos")
    public void pesquisarGrupos(FiltroConsultaGrupoDTO filtroConsultaGrupoDTO, final int rp, final int page,
            final String sortname, final String sortorder) {
        filtroConsultaGrupoDTO = carregarFiltroConsultaGrupos(filtroConsultaGrupoDTO, rp, page, sortname, sortorder);
        
        final List<ResultadoGrupoVO> grupos = grupoPermissaoService.listar(filtroConsultaGrupoDTO);
        
        if (grupos == null || grupos.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
        }
        
        final int qtdeTotalRegistros = grupos.size();
        
        final List<ResultadoGrupoVO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(grupos,
                filtroConsultaGrupoDTO.getPaginacao(), filtroConsultaGrupoDTO.getOrdenacaoColuna().toString());
        
        final TableModel<CellModelKeyValue<ResultadoGrupoVO>> tableModel = new TableModel<CellModelKeyValue<ResultadoGrupoVO>>();
        
        tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPaginado));
        tableModel.setPage(filtroConsultaGrupoDTO.getPaginacao().getPaginaAtual());
        tableModel.setTotal(qtdeTotalRegistros);
        
        result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
        
    }
    
    /**
     * @param filtro
     * @param rp
     * @param page
     * @param sortname
     * @param sortorder
     * @return FiltroConsultaPermissaoDTO
     */
    private FiltroConsultaGrupoDTO carregarFiltroConsultaGrupos(final FiltroConsultaGrupoDTO filtro, final int rp,
            final int page, final String sortname, final String sortorder) {
        
        if (filtro != null) {
            final PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
            filtro.setPaginacao(paginacao);
            filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroConsultaGrupoDTO.ColunaOrdenacao.values(),
                    sortname));
        }
        
        final FiltroConsultaGrupoDTO filtroSessao = (FiltroConsultaGrupoDTO) session
                .getAttribute(FILTRO_PESQUISA_CONSULTA_GRUPO_SESSION_ATTRIBUTE);
        
        if (filtroSessao != null && !filtroSessao.equals(filtro)) {
            filtro.getPaginacao().setPaginaAtual(1);
        }
        
        session.setAttribute(FILTRO_PESQUISA_CONSULTA_GRUPO_SESSION_ATTRIBUTE, filtro);
        
        return filtro;
        
    }
    
    // USUARIOS
    
    /**
     * Retorna a lista de usuários
     * 
     * @param usuario
     * @param rp
     * @param page
     * @param sortname
     * @param sortorder
     */
    @Get
    @Path("/pesquisarUsuarios")
    public void pesquisarUsuarios(final Usuario usuario, final int rp, final int page, final String sortname,
            final String sortorder) {
        final List<Usuario> usuarios = usuarioService.listar(usuario, sortname, Ordenacao.valueOf(sortorder
                .toUpperCase()), page * rp - rp, rp);
        final int quantidadeRegistros = usuarioService.quantidade(usuario);
        if (usuarios == null || usuarios.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
        }
        result.use(FlexiGridJson.class).from(usuarios).total(quantidadeRegistros).page(page).serialize();
    }
    
    private void validarDadosUsuario(final UsuarioDTO usuario) {
        final List<String> listaMensagemValidacao = new ArrayList<String>();
        
        if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
            listaMensagemValidacao.add("O preenchimento do campo nome é obrigatório!");
        }
        
        if (usuario.getLogin() == null || usuario.getLogin().isEmpty()) {
            listaMensagemValidacao.add("O preenchimento do campo login é obrigatório!");
        } else {
            if ((usuario.getId() == null || usuario.getId() == 0) && usuarioService.existeUsuario(usuario.getLogin())) {
                listaMensagemValidacao.add("Login de usuário já existente no sistema!");
            }
        }
        
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            listaMensagemValidacao.add("O preenchimento do campo e-mail é obrigatório!");
        } else {
            
            if (!Util.validarEmail(usuario.getEmail())) {
                listaMensagemValidacao.add("E-mail inválido!");
            }
            
        }
        
        if ((usuario.getPermissoes() == null || usuario.getPermissoes().isEmpty())
                && (usuario.getIdsGrupos() == null || usuario.getIdsGrupos().isEmpty())) {
            listaMensagemValidacao.add("É necessário selecionar ao menos uma permissão e/ou grupo!");
        }
        
        if (!listaMensagemValidacao.isEmpty()) {
            final ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.WARNING, listaMensagemValidacao);
            throw new ValidacaoException(validacaoVO);
        }
        
    }
    
    /**
     * @return
     * @throws NoSuchAlgorithmException
     */
    private UsuarioDTO criptografarSenhas(final UsuarioDTO usuarioDTO) throws NoSuchAlgorithmException {
        
        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
            usuarioDTO.setSenha(Util.md5(usuarioDTO.getSenha()));
        }
        
        if (usuarioDTO.getConfirmaSenha() != null && !usuarioDTO.getConfirmaSenha().isEmpty()) {
            usuarioDTO.setConfirmaSenha(Util.md5(usuarioDTO.getConfirmaSenha()));
        }
        return usuarioDTO;
    }
    
    private void validarAlteracaoSenhas(final UsuarioDTO usuarioDTO) {
        final List<String> listaMensagemValidacao = new ArrayList<String>();
        if (StringUtils.isEmpty(usuarioDTO.getSenha())) {
            listaMensagemValidacao.add("O preenchimento do campo senha é obrigatório!");
        }
        
        if (StringUtils.isEmpty(usuarioDTO.getConfirmaSenha())) {
            listaMensagemValidacao.add("O preenchimento do campo confirma senha é obrigatório!");
        }
        
        if (!StringUtils.equals(usuarioDTO.getSenha(), usuarioDTO.getConfirmaSenha())) {
            listaMensagemValidacao.add("Os campos nova senha e confirmação de senha não correspondem!");
            
        }
        
        if (!listaMensagemValidacao.isEmpty()) {
            final ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, listaMensagemValidacao);
            throw new ValidacaoException(validacaoVO);
        }
    }
    
    @Post
    @Path("/alterarSenha")
    @Rules(Permissao.ROLE_ADMINISTRACAO_GRUPOS_ACESSO_ALTERACAO)
    public void alterarSenha(UsuarioDTO usuarioDTO) throws NoSuchAlgorithmException {
        usuarioDTO = this.criptografarSenhas(usuarioDTO);
        validarAlteracaoSenhas(usuarioDTO);
        
        final Usuario usuario = new Usuario();
        usuario.setId(usuarioDTO.getId());
        usuario.setSenha(usuarioDTO.getSenha());
        usuario.setLembreteSenha(usuarioDTO.getLembreteSenha());
        
        usuarioService.alterarSenha(usuario);
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Senha alterada com Sucesso."), "result")
        .recursive().serialize();
    }
    
    /**
     * @param usuario
     * @param rp
     * @param page
     * @param sortname
     * @param sortorder
     */
    @Post
    @Path("/salvarUsuario")
    @Rules(Permissao.ROLE_ADMINISTRACAO_GRUPOS_ACESSO_ALTERACAO)
    public void salvarUsuario(final UsuarioDTO usuarioDTO) {
        
        this.validarDadosUsuario(usuarioDTO);
        
        if (usuarioDTO.getId() == null || usuarioDTO.getId() == 0) {
            validarAlteracaoSenhas(usuarioDTO);
        }
        
        final Usuario usuario = getUsuarioEntity(usuarioDTO);
        
        if (usuarioDTO.getPermissoes() == null) {
            usuarioDTO.setPermissoes(new ArrayList<Permissao>());
        }
        
        addPais(usuarioDTO.getPermissoes());
        
        if (usuarioDTO.getPermissoes().isEmpty()) {
            usuario.setPermissoes(new HashSet<Permissao>());
        } else {
            usuario.setPermissoes(new HashSet<Permissao>(usuarioDTO.getPermissoes()));
        }
        
        final Set<GrupoPermissao> grupos = new HashSet<GrupoPermissao>();
        
        if (usuarioDTO.getIdsGrupos() != null && !usuarioDTO.getIdsGrupos().isEmpty()) {
            for (final Long id : usuarioDTO.getIdsGrupos()) {
                grupos.add(grupoPermissaoService.buscar(id));
            }
        }
        
        usuario.setGruposPermissoes(grupos);
        
        usuarioService.salvar(usuario);
        
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Usuário salvo com Sucesso."), "result")
        .recursive().serialize();
    }
    
    /**
     * @param usuarioDTO
     * @return
     */
    private Usuario getUsuarioEntity(final UsuarioDTO usuarioDTO) {
        
        Usuario usuario = null;
        String senhaEncriptada = null;
        if (usuarioDTO.getId() == null || usuarioDTO.getId() == 0) {
            
            try {
                
                senhaEncriptada = Util.encriptar(usuarioDTO.getSenha());
                
            } catch (final NoSuchAlgorithmException e) {
                LOGGER.error(e.getMessage(), e);
                final ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR,
                        "Houve problema com a senha informada. Tente novamente.");
                throw new ValidacaoException(validacaoVO);
            } catch (final UnsupportedEncodingException e) {
                LOGGER.error(e.getMessage(), e);
                final ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR,
                        "Houve problema com a senha informada. Tente novamente.");
                throw new ValidacaoException(validacaoVO);
            }
            
            if (senhaEncriptada == null) {
                final ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR,
                        "Houve problema com a senha informada. Tente novamente.");
                throw new ValidacaoException(validacaoVO);
            }
            
            usuario = new Usuario();
            
            usuario.setSenha(senhaEncriptada);
            usuario.setLembreteSenha(usuarioDTO.getLembreteSenha());
        } else {
            usuario = usuarioService.buscar(usuarioDTO.getId());
        }
        usuario.setCep(usuarioDTO.getCep());
        usuario.setCidade(usuarioDTO.getCidade());
        usuario.setDdd(usuarioDTO.getDdd());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setEndereco(usuarioDTO.getEndereco());
        usuario.setId(usuarioDTO.getId());
        usuario.setLogin(usuarioDTO.getLogin());
        usuario.setNome(usuarioDTO.getNome());
        usuario.setPais(usuarioDTO.getPais());
        usuario.setSobrenome(usuarioDTO.getSobrenome());
        usuario.setTelefone(usuarioDTO.getTelefone());
        if ("true".equals(usuarioDTO.getContaAtiva()) || usuarioDTO.getContaAtiva().equals(UsuarioDTO.ATIVA)) {
            usuario.setContaAtiva(true);
        } else {
            usuario.setContaAtiva(false);
        }
        
        usuario.setSupervisor(usuarioDTO.isSupervisor());
        
        return usuario;
    }
    
    /**
     * @param usuario
     * @return
     */
    private UsuarioDTO getUsuarioDTO(final Usuario usuario) {
        final UsuarioDTO dto = new UsuarioDTO();
        dto.setCep(usuario.getCep());
        dto.setCidade(usuario.getCidade());
        if (usuario.isContaAtiva()) {
            dto.setContaAtiva(UsuarioDTO.ATIVA);
        } else {
            dto.setContaAtiva("");
        }
        dto.setDdd(usuario.getDdd());
        dto.setEmail(usuario.getEmail());
        dto.setEndereco(usuario.getEndereco());
        dto.setId(usuario.getId());
        dto.setLembreteSenha(usuario.getLembreteSenha());
        dto.setLogin(usuario.getLogin());
        dto.setNome(usuario.getNome());
        dto.setPais(usuario.getPais());
        dto.setSobrenome(usuario.getSobrenome());
        dto.setTelefone(usuario.getTelefone());
        
        dto.setPermissoes(usuarioService.buscarPermissoes(usuario.getId()));
        
        final List<GrupoPermissaoDTO> grupos = new ArrayList<GrupoPermissaoDTO>();
        GrupoPermissaoDTO grupo = null;
        for (final GrupoPermissao g : usuarioService.buscarGrupoPermissoes(usuario.getId())) {
            grupo = new GrupoPermissaoDTO();
            grupo.setId(g.getId());
            grupo.setNome(g.getNome());
            grupos.add(grupo);
        }
        dto.setGruposSelecionadosList(grupos);
        dto.setSupervisor(usuario.isSupervisor());
        
        return dto;
    }
    
    /**
     * 
     */
    @Get
    @Path("/novoUsuario")
    public void novoUsuario() {
        editarUsuario(null);
    }
    
    /**
     * 
     */
    @Get
    @Path("/excluirUsuario")
    @Rules(Permissao.ROLE_ADMINISTRACAO_GRUPOS_ACESSO_ALTERACAO)
    public void excluirUsuario(final Long codigoUsuario) {
        usuarioService.excluir(codigoUsuario);
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Usuário excluído com Sucesso."),
                "result").recursive().serialize();
    }
    
    /**
     * @param filtro
     * @param rp
     * @param page
     * @param sortname
     * @param sortorder
     */
    @Get
    @Path("/editarUsuario")
    @Rules(Permissao.ROLE_ADMINISTRACAO_GRUPOS_ACESSO_ALTERACAO)
    public void editarUsuario(final Long codigoUsuario) {
        
        UsuarioDTO dto = new UsuarioDTO();
        
        final List<GrupoPermissaoDTO> grupos = grupoPermissaoService.listarDTOs();
        if (codigoUsuario != null && codigoUsuario != 0) {
            final Usuario usuario = usuarioService.buscar(codigoUsuario);
            dto = this.getUsuarioDTO(usuario);
            
            // Remove da lista de grupos selecionados anteriormente
            for (final GrupoPermissaoDTO g : dto.getGruposSelecionadosList()) {
                if (grupos.contains(g)) {
                    grupos.remove(grupos.indexOf(g));
                }
            }
            
        }
        
        dto.setGrupos(grupos);
        
        result.use(Results.json()).from(dto, "usuarioDTO").recursive().serialize();
    }
    
    // REGRAS (PERMISSÕES)
    
    /**
     * Retorna a lista de regras do sistema
     * 
     * @return
     */
    @Get
    @Path("/pesquisarRegras")
    public void pesquisarRegras(FiltroConsultaPermissaoDTO filtroConsultaPermissaoDTO, final int rp, final int page,
            final String sortname, final String sortorder) {
        filtroConsultaPermissaoDTO = carregarFiltroConsultaRegras(filtroConsultaPermissaoDTO, rp, page, sortname,
                sortorder);
        final List<ResultadoPermissaoVO> permissoes = permissaoService.buscarResultado(filtroConsultaPermissaoDTO);
        
        if (permissoes == null || permissoes.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
        }
        
        final int qtdeTotalRegistros = permissoes.size();
        
        final List<ResultadoPermissaoVO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(permissoes,
                filtroConsultaPermissaoDTO.getPaginacao(), filtroConsultaPermissaoDTO.getOrdenacaoColuna().toString());
        
        final TableModel<CellModelKeyValue<ResultadoPermissaoVO>> tableModel = new TableModel<CellModelKeyValue<ResultadoPermissaoVO>>();
        
        tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPaginado));
        tableModel.setPage(filtroConsultaPermissaoDTO.getPaginacao().getPaginaAtual());
        tableModel.setTotal(qtdeTotalRegistros);
        
        result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
    }
    
    /**
     * @param filtro
     * @param rp
     * @param page
     * @param sortname
     * @param sortorder
     * @return FiltroConsultaPermissaoDTO
     */
    private FiltroConsultaPermissaoDTO carregarFiltroConsultaRegras(final FiltroConsultaPermissaoDTO filtro,
            final int rp, final int page, final String sortname, final String sortorder) {
        
        if (filtro != null) {
            final PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
            filtro.setPaginacao(paginacao);
            filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroConsultaPermissaoDTO.ColunaOrdenacao.values(),
                    sortname));
        }
        
        final FiltroConsultaPermissaoDTO filtroSessao = (FiltroConsultaPermissaoDTO) session
                .getAttribute(FILTRO_PESQUISA_CONSULTA_PERMISSAO_SESSION_ATTRIBUTE);
        
        if (filtroSessao != null && !filtroSessao.equals(filtro)) {
            filtro.getPaginacao().setPaginaAtual(1);
        }
        
        session.setAttribute(FILTRO_PESQUISA_CONSULTA_PERMISSAO_SESSION_ATTRIBUTE, filtro);
        
        return filtro;
        
    }
    
}
