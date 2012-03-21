package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.InfoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.MockPerfilUsuario;

public interface ContagemDevolucaoService {

	public InfoContagemDevolucaoDTO obterInfoContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa, MockPerfilUsuario mockPerfilUsuario);

	public void inserirListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, MockPerfilUsuario mockPerfilUsuario);
	
	public void confirmarContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario);
	
}
