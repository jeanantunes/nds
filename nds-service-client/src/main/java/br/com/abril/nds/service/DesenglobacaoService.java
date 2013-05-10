package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.DesenglobaVO;
import br.com.abril.nds.dto.DesenglobacaoDTO;
import br.com.abril.nds.model.seguranca.Usuario;

public interface DesenglobacaoService {
	
	List<DesenglobacaoDTO> obterDesenglobacaoPorCota(Long cotaId);
	
	boolean inserirDesenglobacao(List<DesenglobaVO> desenglobaDTO, Usuario usuario);
	
	public List<DesenglobacaoDTO> obterDesenglobacaoPorCotaDesenglobada(Long cotaId);

	boolean alterarDesenglobacao(List<DesenglobaVO> desenglobaDTO,
			Usuario usuarioLogado);

	void excluirDesenglobacao(Long id);
}
