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
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ConsultaGrupoPermissaoDTO;
import br.com.abril.nds.dto.GrupoPermissaoDTO;
import br.com.abril.nds.dto.UsuarioDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaGrupoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaPermissaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.GrupoPermissao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
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
			permissoes.add(Permissao.valueOf(permissao));
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
		}

		if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
			listaMensagemValidacao.add("O preenchimento do campo e-mail é obrigatório!");
		}

		if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
			listaMensagemValidacao.add("O preenchimento do campo senha é obrigatório!");
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
	 * @param usuario
	 * @param rp
	 * @param page
	 * @param sortname
	 * @param sortorder
	 */
	@Get
	@Path("/salvarUsuario")
	public void salvarUsuario(UsuarioDTO usuarioDTO) {
		this.validarDadosUsuario(usuarioDTO);

		Usuario usuario = new Usuario();
		usuario.setId(usuarioDTO.getId());
		usuario.setNome(usuarioDTO.getNome());
		
		Set<Permissao> permissoes = new HashSet<Permissao>();
		for (String permissao : usuarioDTO.getPermissoesSelecionadas().split(",")) {
			permissoes.add(Permissao.valueOf(permissao));
		}
		usuario.setPermissoes(permissoes);

		Set<GrupoPermissao> grupos = new HashSet<GrupoPermissao>();
		for (String grupo : usuarioDTO.getGruposSelecionados().split(",")) {
			grupos.add(grupoPermissaoService.buscar(Long.parseLong(grupo)));
		}
		usuario.setGruposPermissoes(grupos);
		
		usuarioService.salvar(usuario);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Usuário salvo com Sucesso."),"result").recursive().serialize();
	
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

		/*ConsultaGrupoPermissaoDTO dto = new ConsultaGrupoPermissaoDTO();
		
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
		
		result.use(Results.json()).from(dto, "result").recursive().serialize();*/
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
