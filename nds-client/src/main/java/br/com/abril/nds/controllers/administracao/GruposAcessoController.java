package br.com.abril.nds.controllers.administracao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ResultadoGrupoVO;
import br.com.abril.nds.client.vo.ResultadoPermissaoVO;
import br.com.abril.nds.dto.ConsultaGrupoPermissaoDTO;
import br.com.abril.nds.dto.GrupoPermissaoDTO;
import br.com.abril.nds.dto.UsuarioDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaGrupoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaPermissaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.GrupoPermissao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.GrupoPermissaoService;
import br.com.abril.nds.service.PermissaoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
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
 * @author infoA2
 * Controller dos grupos de acesso
 */
@Resource
@Path("/administracao/gruposAcesso")
public class GruposAcessoController {

	@Autowired
	private Result result;

	@Autowired
	private GrupoPermissaoService grupoPermissaoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PermissaoService permissaoService;

	@Autowired
	private HttpSession session;
	
	public GruposAcessoController() {
		super();
	}
	
	private static final String FILTRO_PESQUISA_CONSULTA_PERMISSAO_SESSION_ATTRIBUTE = "filtroPesquisaConsultaPermissao";
	private static final String FILTRO_PESQUISA_CONSULTA_GRUPO_SESSION_ATTRIBUTE = "filtroPesquisaConsultaGrupo";
	
	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_GRUPOS_ACESSO)
	public void index() {
	}

	// GRUPO PERMISSÃO
	
	/**
	 * 
	 */
	@Get
	@Path("/excluirGrupoPermissao")
	public void excluirGrupoPermissao(Long codigoGrupo) {
		grupoPermissaoService.excluir(codigoGrupo);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Grupo de Permissões excluído com Sucesso."),"result").recursive().serialize();
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
	public void editarGrupoPermissao(Long codigoGrupo) {

		ConsultaGrupoPermissaoDTO dto = new ConsultaGrupoPermissaoDTO();
		
		List<Permissao> permissoes = permissaoService.buscar();
		if (codigoGrupo != null) {
			GrupoPermissao grupoPermissao = grupoPermissaoService.buscar(codigoGrupo);
			List<Permissao> permissoesSelecionadas = new ArrayList<Permissao>(grupoPermissao.getPermissoes());
			dto.setId(grupoPermissao.getId());
			dto.setNome(grupoPermissao.getNome());
			dto.setPermissoesSelecionadas(permissoesSelecionadas);
			// Remove da lista as permissões selecionadas anteriormente
			for (Permissao p : permissoesSelecionadas) {
				if (permissoes.contains(p)) {
					permissoes.remove(permissoes.indexOf(p));
				}
			}
		}
		dto.setPermissoes(permissoes);
		
		result.use(Results.json()).from(dto, "result").recursive().serialize();
	}

	/**
	 * @param dataDe
	 * @param dataAte
	 */
	private void validarDadosGrupoPermissao(GrupoPermissaoDTO grupoPermissaoDTO) {
		List<String> listaMensagemValidacao = new ArrayList<String>();

		if (grupoPermissaoDTO.getNome() == null || grupoPermissaoDTO.getNome().isEmpty()) {
			listaMensagemValidacao
					.add("O preenchimento do campo nome é obrigatório!");
		}

		if (grupoPermissaoDTO.getPermissoesSelecionadas() == null || grupoPermissaoDTO.getPermissoesSelecionadas().isEmpty()) {
			listaMensagemValidacao
					.add("É necessário selecionar ao menos uma permissão!");
		}

		if (!listaMensagemValidacao.isEmpty()) {
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR,
					listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}

	}
	
	/**
	 * @param codigoGrupo
	 */
	@Post
	@Path("/salvarGrupoPermissao")
	public void salvarGrupoPermissao(GrupoPermissaoDTO grupoPermissaoDTO) throws Exception {
		this.validarDadosGrupoPermissao(grupoPermissaoDTO);

		GrupoPermissao grupoPermissao = new GrupoPermissao();
		grupoPermissao.setId(grupoPermissaoDTO.getId());
		grupoPermissao.setNome(grupoPermissaoDTO.getNome());
		
		Set<Permissao> permissoes = new HashSet<Permissao>();
		for (String permissao : grupoPermissaoDTO.getPermissoesSelecionadas().split(",")) {
			permissoes = adicionarPermissoes(permissoes, Permissao.valueOf(permissao));
			//permissoes.add(Permissao.valueOf(permissao));
		}
		
		grupoPermissao.setPermissoes(permissoes);
		
		grupoPermissaoService.salvar(grupoPermissao);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Grupo de Permissões salvo com Sucesso."),"result").recursive().serialize();
		
	}

	/**
	 * Retorna a lista de grupos do sistema
	 * @return
	 */
	@Get
	@Path("/pesquisarGrupos")
	public void pesquisarGrupos(FiltroConsultaGrupoDTO filtroConsultaGrupoDTO, int rp, int page, String sortname, String sortorder) {
		filtroConsultaGrupoDTO = carregarFiltroConsultaGrupos(filtroConsultaGrupoDTO, rp, page, sortname, sortorder);
		
		List<ResultadoGrupoVO> grupos = grupoPermissaoService.listar(filtroConsultaGrupoDTO);

		if (grupos == null || grupos.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		int qtdeTotalRegistros = grupos.size();
		
		List<ResultadoGrupoVO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(grupos, filtroConsultaGrupoDTO.getPaginacao(), filtroConsultaGrupoDTO.getOrdenacaoColuna().toString());
		
		TableModel<CellModelKeyValue<ResultadoGrupoVO>> tableModel = new TableModel<CellModelKeyValue<ResultadoGrupoVO>>();

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
	private FiltroConsultaGrupoDTO carregarFiltroConsultaGrupos(FiltroConsultaGrupoDTO filtro, int rp, int page, String sortname, String sortorder) {

		if (filtro != null) {
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			filtro.setPaginacao(paginacao);
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroConsultaGrupoDTO.ColunaOrdenacao.values(), sortname));
		}

		FiltroConsultaGrupoDTO filtroSessao = (FiltroConsultaGrupoDTO) this.session.getAttribute(FILTRO_PESQUISA_CONSULTA_GRUPO_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}

		session.setAttribute(FILTRO_PESQUISA_CONSULTA_GRUPO_SESSION_ATTRIBUTE, filtro);

		return filtro;
		
	}
	
	// USUARIOS
	
	/**
	 * Retorna a lista de usuários
	 * @param usuario
	 * @param rp
	 * @param page
	 * @param sortname
	 * @param sortorder
	 */
	@Get
	@Path("/pesquisarUsuarios")
	public void pesquisarUsuarios(Usuario usuario, int rp, int page, String sortname, String sortorder) {
		List<Usuario> usuarios = usuarioService.listar(usuario, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp, rp);
		int quantidadeRegistros = usuarioService.quantidade(usuario);
		if (usuarios == null || usuarios.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		result.use(FlexiGridJson.class).from(usuarios).total(quantidadeRegistros).page(page).serialize();
	}

	private void validarDadosUsuario(UsuarioDTO usuario) {
		List<String> listaMensagemValidacao = new ArrayList<String>();

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

		if ( (usuario.getPermissoesSelecionadas() == null || usuario.getPermissoesSelecionadas().isEmpty()) && 
			 (usuario.getGruposSelecionados() == null || usuario.getGruposSelecionados().isEmpty()) ) {
			listaMensagemValidacao.add("É necessário selecionar ao menos uma permissão e/ou grupo!");
		}

		if (!listaMensagemValidacao.isEmpty()) {
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}

	}

	/**
	 * @return
	 */
	private UsuarioDTO criptografarSenhas(UsuarioDTO usuarioDTO) {
		
		if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty())
			usuarioDTO.setSenha(Util.md5(usuarioDTO.getSenha()));
		
		if (usuarioDTO.getConfirmaSenha() != null && !usuarioDTO.getConfirmaSenha().isEmpty())
			usuarioDTO.setConfirmaSenha(Util.md5(usuarioDTO.getConfirmaSenha()));
		return usuarioDTO;
	}


	private void validarAlteracaoSenhas(UsuarioDTO usuarioDTO) {
		List<String> listaMensagemValidacao = new ArrayList<String>();
		if (usuarioDTO.getSenha() == null || usuarioDTO.getSenha().isEmpty()) {
			listaMensagemValidacao.add("O preenchimento do campo senha é obrigatório!");
		}

		if (usuarioDTO.getConfirmaSenha() == null || usuarioDTO.getConfirmaSenha().isEmpty()) {
			listaMensagemValidacao.add("O preenchimento do campo confirma senha é obrigatório!");
		}

		if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty() && usuarioDTO.getConfirmaSenha() != null && !usuarioDTO.getConfirmaSenha().isEmpty()) {
			if (!usuarioDTO.getConfirmaSenha().equals(usuarioDTO.getSenha())) {
				listaMensagemValidacao.add("Os campos nova senha e confirmação de senha não correspondem!");
			}
		}

		if (!listaMensagemValidacao.isEmpty()) {
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}
	}
	
	@Post
	@Path("/alterarSenha")
	public void alterarSenha(UsuarioDTO usuarioDTO) {
		usuarioDTO = this.criptografarSenhas(usuarioDTO);
		validarAlteracaoSenhas(usuarioDTO);
		
		Usuario usuario = new Usuario();
		usuario.setId(usuarioDTO.getId());
		usuario.setSenha(usuarioDTO.getSenha());
		usuario.setLembreteSenha(usuarioDTO.getLembreteSenha());
		
		usuarioService.alterarSenha(usuario);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Senha alterada com Sucesso."),"result").recursive().serialize();
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
	public void salvarUsuario(UsuarioDTO usuarioDTO) {

		this.validarDadosUsuario(usuarioDTO);
		
		if (usuarioDTO.getId() == null || usuarioDTO.getId() == 0) {
			validarAlteracaoSenhas(usuarioDTO);
		}
		
		Usuario usuario = getUsuarioEntity(usuarioDTO);
		
		Set<Permissao> permissoes = new HashSet<Permissao>();
		for (String p : usuarioDTO.getPermissoesSelecionadas().split(",")) {
			if ( p != null && !p.isEmpty() ) {
				//permissoes.add(Permissao.valueOf(p));
				permissoes = adicionarPermissoes(permissoes, Permissao.valueOf(p));
			}
		}
		usuario.setPermissoes(permissoes);

		Set<GrupoPermissao> grupos = new HashSet<GrupoPermissao>();
		for (String grupo : usuarioDTO.getGruposSelecionados().split(",")) {
			if (grupo != null && !grupo.isEmpty())
				grupos.add(grupoPermissaoService.buscar(Long.parseLong(grupo)));
		}
		usuario.setGruposPermissoes(grupos);
		
		usuarioService.salvar(usuario);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Usuário salvo com Sucesso."),"result").recursive().serialize();
	
	}

	/**
	 * @param permissoes
	 * @param permissao
	 * @return
	 */
	private Set<Permissao> adicionarPermissoes(Set<Permissao> permissoes, Permissao permissao) {
		if (permissao.getPermissaoPai() != null) {
			permissoes = adicionarPermissoes(permissoes, permissao.getPermissaoPai());
		}
		permissoes.add(permissao);
		return permissoes;
	}
	
	/**
	 * @param usuarioDTO
	 * @return
	 */
	private Usuario getUsuarioEntity(UsuarioDTO usuarioDTO) {
		
		Usuario usuario = null;
		if (usuarioDTO.getId() == null || usuarioDTO.getId() == 0) {
			usuario = new Usuario();
			usuario.setSenha(usuarioDTO.getSenha());
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
		if (usuarioDTO.getContaAtiva().equals(UsuarioDTO.ATIVA)) {
			usuario.setContaAtiva(true);
		} else {
			usuario.setContaAtiva(false);
		}
		return usuario;
		
	}
	
	/**
	 * @param usuario
	 * @return
	 */
	private UsuarioDTO getUsuarioDTO(Usuario usuario) {
		UsuarioDTO dto = new UsuarioDTO();
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
		
		dto.setPermissoesSelecionadasList(usuarioService.buscarPermissoes(usuario.getId()));
		
		List<GrupoPermissaoDTO> grupos = new ArrayList<GrupoPermissaoDTO>();
		GrupoPermissaoDTO grupo = null;
		for (GrupoPermissao g : usuarioService.buscarGrupoPermissoes(usuario.getId())) {
			grupo = new GrupoPermissaoDTO();
			grupo.setId(g.getId());
			grupo.setNome(g.getNome());
			grupos.add(grupo);
		}
		dto.setGruposSelecionadosList(grupos);
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
	public void excluirUsuario(Long codigoUsuario) {
		usuarioService.excluir(codigoUsuario);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Usuário excluído com Sucesso."),"result").recursive().serialize();
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
	public void editarUsuario(Long codigoUsuario) {

		UsuarioDTO dto = new UsuarioDTO();
		
		List<Permissao> permissoes = permissaoService.buscar();
		List<GrupoPermissaoDTO> grupos = grupoPermissaoService.listarDTOs();
		if (codigoUsuario != null && codigoUsuario != 0) {
			Usuario usuario = usuarioService.buscar(codigoUsuario);
			dto = this.getUsuarioDTO(usuario);
			
			// Remove da lista as permissões selecionadas anteriormente
			for (Permissao p : dto.getPermissoesSelecionadasList()) {
				if (permissoes.contains(p)) {
					permissoes.remove(permissoes.indexOf(p));
				}
			}

			// Remove da lista de grupos selecionados anteriormente
			for (GrupoPermissaoDTO g : dto.getGruposSelecionadosList()) {
				if (grupos.contains(g)) {
					grupos.remove(grupos.indexOf(g));
				}
			}

		}
		dto.setPermissoes(permissoes);
		dto.setGrupos(grupos);
		
		
		result.use(CustomJson.class).from(dto).serialize();
	}

	
	// REGRAS (PERMISSÕES)
	
	/**
	 * Retorna a lista de regras do sistema
	 * @return
	 */
	@Get
	@Path("/pesquisarRegras")
	public void pesquisarRegras(FiltroConsultaPermissaoDTO filtroConsultaPermissaoDTO, int rp, int page, String sortname, String sortorder) {
		filtroConsultaPermissaoDTO = carregarFiltroConsultaRegras(filtroConsultaPermissaoDTO, rp, page, sortname, sortorder);
		List<ResultadoPermissaoVO> permissoes = permissaoService.buscarResultado(filtroConsultaPermissaoDTO);

		if (permissoes == null || permissoes.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		int qtdeTotalRegistros = permissoes.size();
		
		List<ResultadoPermissaoVO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(permissoes, filtroConsultaPermissaoDTO.getPaginacao(), filtroConsultaPermissaoDTO.getOrdenacaoColuna().toString());
		
		TableModel<CellModelKeyValue<ResultadoPermissaoVO>> tableModel = new TableModel<CellModelKeyValue<ResultadoPermissaoVO>>();

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
	private FiltroConsultaPermissaoDTO carregarFiltroConsultaRegras(FiltroConsultaPermissaoDTO filtro, int rp, int page, String sortname, String sortorder) {

		if (filtro != null) {
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			filtro.setPaginacao(paginacao);
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroConsultaPermissaoDTO.ColunaOrdenacao.values(), sortname));
		}

		FiltroConsultaPermissaoDTO filtroSessao = (FiltroConsultaPermissaoDTO) this.session.getAttribute(FILTRO_PESQUISA_CONSULTA_PERMISSAO_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}

		session.setAttribute(FILTRO_PESQUISA_CONSULTA_PERMISSAO_SESSION_ATTRIBUTE, filtro);

		return filtro;
		
	}

}
