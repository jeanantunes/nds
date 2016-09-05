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
    Banco obterBancoPorNumero(String numero);
    
    /**
	 * Obtém Banco de acordo com os parametros informados
	 * 
	 * @param numeroBanco - número do banco
	 * @param numeroAgencia - número da agência
	 * @param numeroConta - número da conta
	 * 
	 * @return {@link br.com.abril.nds.model.cadastro.Banco} 
	 */
    Banco obterBanco(String numeroBanco, Long numeroAgencia, Long numeroConta);
	
    /**
     * Obtém Banco por nome
     * @param nome
     * @return {@link br.com.abril.nds.model.cadastro.Banco} 
     */
	Banco obterbancoPorNome(String nome);

	
    /**
     * Obtém Banco por nome
     * @param nome
     * @return {@link br.com.abril.nds.model.cadastro.Banco} 
     */
	Banco obterbancoPorApelido(String apelido);

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

	List<Banco> obterBancosPorNome(String nomeBanco);
	
	List<Banco> obterBancosPorNome(String nomeBanco, Integer qtdMaxResult);

	/**
	 * Obtém bancos por status de atividade
	 * @param ativo - Filtro utilizado na pesquisa. Ativo(true) - Inativo(false)
	 * @return
	 */
	List<Banco> obterBancosPorStatus(Boolean ativo);

	Banco buscarBancoPorIdCobranca(Long idCobranca);
	
	List<Banco> obterBancoBoletoAvulso();
}