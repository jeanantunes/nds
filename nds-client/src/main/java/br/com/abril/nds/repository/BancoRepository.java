package br.com.abril.nds.repository;

import java.util.List;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.model.cadastro.Banco;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Banco}  
 * 
 * @author Discover Technology
 *
 */
public interface BancoRepository extends Repository<Banco,Long>{

	/**
	 * Obtém uma lista de Bancos para os parametros passados.
	 * 
	 * @param filtro - parametros de busca
	 * 
	 * @return {@link List<Banco>}
	 */
	List<Banco> obterBancos(FiltroConsultaBancosDTO filtro);
	
	/**
	 * Obtém a quantidade de Bancos para os parametros passados.
	 * 
	 * @param filtro - parametros de busca
	 * 
	 * @return quantidade
	 */
	long obterQuantidadeBancos(FiltroConsultaBancosDTO filtro);
	
	/**
	 * Obtém Banco por numero
	 * @param numero
	 * @return
	 */
    Banco obterbancoPorNumero(String numero);
	
    /**
     * Obtém Banco por nome
     * @param nome
     * @return
     */
	Banco obterbancoPorNome(String nome);
	
	/**
	 * Desativa o Banco
	 * @param idBanco
	 * @return 
	 * @return
	 */
	void desativarBanco(long idBanco);
	
	/**
	 * Método responsável por verificar se o banco possui documentos pendentes relacionados
	 * @return True se possuir pendências
	 */
	boolean verificarPedencias(long idBanco);
	
}
