package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotaRotaRoteiroDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Box }  
 * 
 * @author Discover Technology
 */
public interface BoxRepository extends Repository<Box,Long> {
	
	/**
	 * Obtém lista de box de um tipo de box relacionado a determinado usuário.
	 * 
	 * @param idUsuario
	 * 
	 * @return Box
	 */
	public List<Box> obterBoxUsuario(Long idUsuario, TipoBox tipoBox) ;

	
	/**
	 * Obtém lista de Box de acordo com tipoBox.
	 * @param nomeBox nome do box para consulta
	 * 
	 * @param tipoBox tipo do box
	 * 
	 * @return List - Box
	 */
	List<Box> obterListaBox(String nomeBox, List<TipoBox> tipoBox);
	
	/**
     * Obtém lista de Box de acordo com tipoBox.
     * 
     * @param tipoBox tipo do box
     * 
     * @return List - Box
     */
    List<Box> obterListaBox(List<TipoBox> tipoBox);

	
	/**
	 * Retorna uma lista de boxes referente um determinado produto.
	 * @param codigoProduto - código do propduto
	 * @return List<Box>
	 */
	List<Box> obterBoxPorProduto(String codigoProduto);
	
	/**
	 * Obtem a quantidade de registros respeitando as restricoes parametrizadas.
	 * @param codigoBox Código do Box
	 * @param tipoBox Tipo do Box {@link TipoBox}
	 * @return Quantidade de registros
	 */
	public abstract Long quantidade(Integer codigoBox, TipoBox tipoBox);
	
	/**
	 * Busca os Box respeitando as restricoes parametrizadas.
 	 * @param codigoBox Código do Box
	 * @param tipoBox Tipo do Box {@link TipoBox}
	 * @param orderBy nome do campo para compor a ordenação
	 * @param ordenacao tipo da ordenação
	 * @param initialResult resultado inicial
	 * @param maxResults numero maximo de resultados
	 * @return
	 */
	public abstract List<Box> busca(Integer codigoBox, TipoBox tipoBox, String  orderBy,
			Ordenacao ordenacao, Integer initialResult, Integer maxResults);
	
	/**
	 * Verifica a existência do Código do {@link Box}.
	 * @param codigoBox Código do {@link Box} a ser verificado.
	 * @param id (Opcional) id do {@link Box} a ser ignorado na verificação.
	 * @return <code>true</code> se o Código ja estiver em uso por um {@link Box} diferente ao do id.
	 */
	public abstract boolean hasCodigo(Integer codigoBox, Long id);
	
	/**
	 * Recupera as {@link Cota} relacionadas suas {@link Rota} e roteiros relacionadas ao {@link Box}
	 * @param id Id do {@link Box}
	 * @param sortorder
	 * @param sortname
	 * @return List<CotaRotaRoteiroDTO>
	 */
	public abstract List<CotaRotaRoteiroDTO> obtemCotaRotaRoteiro(long idBox, String sortname, String sortorder);
	
	Integer obterCodigoBoxPadraoUsuario(Long idUsuario);

	/**
	 * Verifica se há {@link Cota} vincula ao {@link Box}
	 * @param idBox
	 * @return <code>true</code>Possui vinculo
	 */
	public abstract boolean hasCotasVinculadas(long idBox);

	/**
	 * Verifica se há {@link Roteiro} vincula ao {@link Box}
	 * @param idBox
	 * @return <code>true</code>Possui vinculo
	 */
	public abstract boolean hasRoteirosVinculados(long idBox);
	
	/**
	 * Obtém Lista de Cotas por Box, Rota e Roteiro
	 * @param idBox
	 * @param idRoteiro
	 * @param idRota
	 * @return List<Cota>
	 */
	List<Cota> obterCotasPorBoxRoteiroRota(Long idBox, Long idRoteiro, Long idRota);
	
	/**
	 * Obtém Quantidade de Cotas por Box, Rota e Roteiro
	 * @param idBox
	 * @param idRoteiro
	 * @param idRota
	 * @return Número de Cotas encontradas
	 */
	Long obterQuantidadeCotasPorBoxRoteiroRota(Long idBox, Long idRoteiro, Long idRota);


	/**
	 * Obtém Box associado a roteirização da rota parametrizada
	 * 
	 * @param rotaID
	 * @return
	 */
	public Box obterBoxPorRota(Long rotaID);

	/**
	 * Obtém Box associado a roteirização do roteiro parametrizado
	 * 
	 * @param rotaID
	 * @return
	 */
	public Box obterBoxPorRoteiro(Long roteiroID);

	/**
	 * Obtem lista de Box por intervalo de Código
	 * @param codigoBoxDe
	 * @param codigoBoxAte
	 * @return List<Box>
	 */
	List<Box> obterBoxPorIntervaloCodigo(Integer codigoBoxDe, Integer codigoBoxAte);
	
	/**
	 * Busca lista de Box por Rota
	 * @param rotaId
	 * @return List<Box>
	 */
	List<Box> buscarBoxPorRota(Long rotaId);
	
	/**
	 * Busca lista de Box por Roteiro
	 * @param roteiroId
	 * @return List<Box>
	 */
	List<Box> buscarBoxPorRoteiro(Long roteiroId);


	public String obterDescricaoBoxPorCota(Integer numeroCota);
	
	/**
	 * Obtem lista de Box por Código
	 * @param codigosBox
	 * @return List<Box>
	 */
	List<Box> obterBoxPorCodigo(Integer codigoBox);
	
	Long obterIdBoxEspecial();
}