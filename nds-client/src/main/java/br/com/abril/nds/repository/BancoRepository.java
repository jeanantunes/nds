package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.TipoCobranca;

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
	 * @param filtro: Data Transfer Object com parametros de busca
	 * @return {@link List<Banco>}
	 */
	List<Banco> obterBancos(FiltroConsultaBancosDTO filtro);
	
	/**
	 * Obtém a quantidade de Bancos para os parametros passados.
	 * @param filtro: Data Transfer Object com parametros de busca
	 * @return quantidade de bancos
	 */
	long obterQuantidadeBancos(FiltroConsultaBancosDTO filtro);
	
	/**
	 * Obtém Banco por numero
	 * @param numero
	 * @return {@link br.com.abril.nds.model.cadastro.Banco} 
	 */
    Banco obterbancoPorNumero(String numero);
	
    /**
     * Obtém Banco por nome
     * @param nome
     * @return {@link br.com.abril.nds.model.cadastro.Banco} 
     */
	Banco obterbancoPorNome(String nome);
	
	/**
	 * Desativa o Banco
	 * @param idBanco
	 */
	void desativarBanco(long idBanco);
	
	/**
	 * Método responsável por verificar se o banco possui documentos pendentes relacionados
	 * @param idBanco
	 * @return boolean: true, caso o banco esteja relacionado com alguma cobranca em aberto
	 */
	boolean verificarPedencias(long idBanco);
	
	/**
	 * Método responsável por obter Carteiras cadastradas
	 * @return {@link List<Carteira>}: Lista de Carteiras.
	 */
	List<Carteira> obterCarteiras();
	
	/**
	 * Método responsável por obter Carteira por id.
	 * @return {@link br.com.abril.nds.model.cadastro.Carteira} 
	 */
	Carteira obterCarteiraPorCodigo(Integer codigoCarteira);
	
	/**
	 * Obtém uma lista de Bancos para os parametros passados.
	 * @param Tipo de Cobrança
	 * @return {@link List<Banco>}
	 */
	List<Banco> obterBancosPorTipoDeCobranca(TipoCobranca tipoCobranca);
	
}
