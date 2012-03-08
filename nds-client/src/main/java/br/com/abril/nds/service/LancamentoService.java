package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.vo.PaginacaoVO;

public interface LancamentoService {

	/**
	 * Busca por Lançamentos com Recebimento físico ainda não expedidos
	 * 
	 * @param paginacaoVO - Objeto com informações pertinentes a paginação
	 * @param data - Filtro - Data de Lançamento
	 * @param idFornecedor - Filtro - Código do Fornecedor
	 * @param estudo - Filtro - Booleando que define se filtrará apenas lançamentos com estudo gerado
	 * @return
	 */
	List<LancamentoNaoExpedidoDTO> obterLancamentosNaoExpedidos(
			PaginacaoVO paginacaoVO, Date data, Long idFornecedor, Boolean estudo);
	
	Long obterTotalLancamentosNaoExpedidos(Date data, Long idFornecedor, Boolean estudo);
	
	/**
	 * Confirma expedição de lançamento
	 * 
	 * @param idLancamento - Código do lançamento
	 * @param idUsuario - Código do usuario
	 */
	void confirmarExpedicao(Long idLancamento, Long idUsuario);
	
	Lancamento obterPorId(Long idLancamento);
}
 