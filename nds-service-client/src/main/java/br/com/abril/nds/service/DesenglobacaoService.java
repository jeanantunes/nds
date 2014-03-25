package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.DesenglobacaoDTO;
import br.com.abril.nds.model.seguranca.Usuario;

public interface DesenglobacaoService {
	
	List<DesenglobacaoDTO> obterDesenglobacaoPorCota(Integer numeroCota);
	
	boolean inserirDesenglobacao(List<DesenglobacaoDTO> desenglobacaoDTO, Usuario usuario);
	
	public List<DesenglobacaoDTO> obterDesenglobacaoPorCotaDesenglobada(Integer numeroCota);

	boolean alterarDesenglobacao(List<DesenglobacaoDTO> desenglobacaoDTO, Usuario usuarioLogado);

	void excluirDesenglobacao(Long idCota);
}
