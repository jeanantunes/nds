package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.MovimentoAprovacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO;
import br.com.abril.nds.model.movimentacao.Movimento;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * Interface que define os serviços referentes
 * ao controle de aprovações.
 * 
 * @author Discover Technology
 */
public interface ControleAprovacaoService {

	/**
	 * Obtém os movimentos para aprovação de acordo com o filtro.
	 * 
	 * @param filtro - filtro para a pesquisa
	 * 
	 * @return {@link List<MovimentoAprovacaoDTO>}
	 */
	List<MovimentoAprovacaoDTO> obterMovimentosAprovacao(FiltroControleAprovacaoDTO filtro);
	
	/**
	 * Obtém o total de movimentos para aprovação de acordo com o filtro.
	 * 
	 * @param filtro - filtro para a pesquisa
	 * 
	 * @return total de movimentos para aprovação
	 */
	Long obterTotalMovimentosAprovacao(FiltroControleAprovacaoDTO filtro);
	
	/**
	 * Aprova o movimento referente ao identificador informado.
	 * 
	 * @param idMovimento - identificador do movimento
	 * @param usuario - usuário aprovador
	 */
	void aprovarMovimento(Long idMovimento, Usuario usuario);
	
	/**
	 * Rejeita o movimento referente ao identificador informado.
	 * 
	 * @param idMovimento - identificador do movimento
	 * @param motivo - motivo da rejeição
	 * @param usuario - usuário aprovador
	 */
	void rejeitarMovimento(Long idMovimento, String motivo, Usuario usuario);
	
	/**
	 * Realiza aprovação do movimento e chama 
	 * a atualização do estoque do produto se necessário.
	 * 
	 * @param movimento - movimento
	 * @param usuario - usuário
	 */
	void realizarAprovacaoMovimento(Movimento movimento, Usuario usuario);
}
