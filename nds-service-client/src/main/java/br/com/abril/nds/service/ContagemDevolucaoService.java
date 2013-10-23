package br.com.abril.nds.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.ProdutoEdicaoFechadaVO;
import br.com.abril.nds.dto.ContagemDevolucaoConferenciaCegaDTO;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.InfoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.Intervalo;

public interface ContagemDevolucaoService {

	public InfoContagemDevolucaoDTO obterInfoContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa, boolean indPerfilUsuarioEncarregado);

	public void inserirListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario, boolean indPerfilUsuarioEncarregado);
	
	public void confirmarContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) throws FileNotFoundException, IOException;

	public abstract List<ContagemDevolucaoConferenciaCegaDTO> obterInfoContagemDevolucaoCega(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa,
			boolean indPerfilUsuarioEncarregado);

	public abstract void gerarNotasFiscaisPorFornecedor(List<ContagemDevolucaoDTO> listaContagemDevolucaoAprovada, Usuario usuario, boolean indConfirmarContagemDevolucao) throws FileNotFoundException, IOException;

	public List<ContagemDevolucaoDTO> obterContagemDevolucaoEdicaoFechada(
			boolean checkAll, List<ProdutoEdicaoFechadaVO> listaEdicoesFechadas, FiltroDigitacaoContagemDevolucaoDTO filtro);
	
    /**
     * Gera o arquivo para impressão das chamadas de encalhe dos fornecedores de
     * acordo com os parâmetros
     * 
     *  @param idFornecedor
     *            identificador do fornecedor, pode ser nulo, neste caso, indica
     *            todos os fornecedores
     * @param numeroSemana
     *            número da semana para recuperação das chamadas de encalhe,
     *            pode ser nulo, neste caso a geração do arquivo será por intervalo de
     *            recolhimento
     * @param periodo
     *            período de recolhimento das chamadas de encalhe, pode ser
     *            nulo, neste caso a geração do arquivo será por número da semana de
     *            recolhimento

     * @return arquivo gerado com as informações de chamada de encalhe dos
     *         fornecedor de acordo com os parâmetros
     */
	public byte[] gerarImpressaoChamadaEncalheFornecedor(Long idFornecedor, Integer numeroSemana, Intervalo<Date> periodo);

	void gerarNotasFiscaisPorFornecedorFecharLancamentos(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) throws FileNotFoundException, IOException;
    
	
}
