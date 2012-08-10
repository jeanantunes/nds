package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ResultadoGrupoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.GrupoPermissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaGrupoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.GrupoPermissao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.GrupoPermissaoRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.GrupoPermissaoService;
import br.com.abril.nds.util.TipoMensagem;

/**
 * @author InfoA2
 */
@Service
public class GrupoPermissaoServiceImpl implements GrupoPermissaoService {

	@Autowired
	GrupoPermissaoRepository grupoPermissaoRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Transactional(readOnly = true)
	@Override
	public GrupoPermissao buscar(Long codigo) {
		return grupoPermissaoRepository.buscarPorId(codigo);
	}

	@Override
	@Transactional
	public void salvar(GrupoPermissao grupoPermissao) {
		if (grupoPermissao.getId() == null || grupoPermissao.getId() == 0) {
			grupoPermissaoRepository.adicionar(grupoPermissao);
		}
		grupoPermissaoRepository.alterar(grupoPermissao);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.GrupoPermissaoService#listar(br.com.abril.nds.dto.filtro.FiltroConsultaGrupoDTO)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ResultadoGrupoVO> listar(FiltroConsultaGrupoDTO filtro) {
		return grupoPermissaoRepository.buscaFiltrada(filtro);
	}

	@Override
	@Transactional
	public void excluir(Long codigoGrupo) {
		
		if (usuarioRepository.hasUsuarioPorGrupoPermissao(codigoGrupo)){
			
			List<String> listaMensagemValidacao = new ArrayList<String>();

			listaMensagemValidacao.add("Este grupo já está sendo referenciado por um usuário!");
			
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}

		GrupoPermissao grupoPermissao = this.buscar(codigoGrupo);
		grupoPermissao.setPermissoes(new HashSet<Permissao>());
		
		this.salvar(grupoPermissao);
		
		grupoPermissaoRepository.remover(grupoPermissao);
	}

	@Override
	@Transactional
	public List<GrupoPermissaoDTO> listarDTOs() {
		List<GrupoPermissaoDTO> grupos = new ArrayList<GrupoPermissaoDTO>();
		GrupoPermissaoDTO grupo = null;
		for (ResultadoGrupoVO g : grupoPermissaoRepository.buscaFiltrada(null)) {
			grupo = new GrupoPermissaoDTO();
			grupo.setId(g.getId());
			grupo.setNome(g.getNome());
			grupos.add(grupo);
		}
		return grupos;
	}

}
