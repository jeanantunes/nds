package br.com.abril.nds.service;

import java.util.Collection;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.List;

import br.com.abril.nds.dto.ContagemDevolucaoConferenciaCegaDTO;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.InfoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.model.estoque.CEDevolucaoFornecedor;
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;
import br.com.abril.nds.model.seguranca.Usuario;

public interface ContagemDevolucaoService {

	public InfoContagemDevolucaoDTO obterInfoContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa, boolean indPerfilUsuarioEncarregado);

	public void inserirListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario, boolean indPerfilUsuarioEncarregado);
	
	public void confirmarContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) throws FileNotFoundException, IOException;

	public abstract List<ContagemDevolucaoConferenciaCegaDTO> obterInfoContagemDevolucaoCega(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa,
			boolean indPerfilUsuarioEncarregado);

	public abstract void gerarNotasFiscaisPorFornecedor(List<ContagemDevolucaoDTO> listaContagemDevolucaoAprovada) throws FileNotFoundException, IOException;
	
	public Collection<CEDevolucaoFornecedor> gerarCEDevolucao(Collection<ConferenciaEncalheParcial> conferencias);
	
	public byte[] gerarImpressaoCEDevolucao(Collection<CEDevolucaoFornecedor> devolucoes);
	
	
}
