package br.com.abril.nds.service;

import java.util.List;

import org.hibernate.criterion.MatchMode;

import br.com.abril.nds.dto.BoxRoteirizacaoDTO;
import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.CotaDisponivelRoteirizacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MapaRoteirizacaoDTO;
import br.com.abril.nds.dto.PdvRoteirizacaoDTO;
import br.com.abril.nds.dto.RotaRoteirizacaoDTO;
import br.com.abril.nds.dto.RoteirizacaoDTO;
import br.com.abril.nds.dto.RoteiroRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;

public interface RoteirizacaoService {
	
	List<Roteiro> buscarRoteiro(String sortname, Ordenacao ordenacao);
	
	List<Rota> buscarRota(String sortname, Ordenacao ordenacao);

	void incluirRoteiro(Roteiro roteiro);
	
	List<Roteiro> buscarRoteiroPorDescricao(String descricao, MatchMode matchMode);
	
	List<Rota> buscarRotasPorRoteiro(Long idRoteiro);
	
	void incluirRota(Rota rota);
	
	void excluirListaRota(List<Long> rotasId, Long roteiroId);
	
	void transferirListaRota(List<Long> rotasId, Long roteiroId);
	
	void transferirListaRotaComNovoRoteiro(List<Long> rotasId, Roteiro roteiro);
	
	List<Rota> buscarRotas();
	
	List<Roteiro> buscarRoteiros();
	
	List<Roteiro> buscarRoteiroDeBox(Long idBox);
	
	List<Roteiro> buscarRoteiroCodigoBox(Long codigoBoxDe, Long codigoBoxAte);
	
	List<Rota> buscarRotaDeBox(Long idBox);
	
	Roteirizacao buscarRoteirizacaoDeCota(Integer numeroCota);
	
	Roteirizacao buscarRoteirizacaoPorId(Long idRoteirizacao);
	
	List<Rota> buscarRotaPorRoteiro(String descRoteiro);
	
	Rota buscarRotaPorId(Long idRota);
	 
	Roteiro buscarRoteiroPorId(Long idRoteiro);

	List<Rota> buscarRotaPorNome(Long roteiroId, String rotaNome, MatchMode matchMode);
	
	List<CotaDisponivelRoteirizacaoDTO> buscarPvsPorCota(Integer numeroCota,  Long rotaId,  Long roteiroId );

	/**
	 * Obtem rotas por numero da cota
	 * 
	 * @param numeroCota - Numero da Cota
	 * @return Lista de Rotas
	 */
	
	List<Rota> obterRotasPorCota(Integer numeroCota);
	
	Integer buscarMaiorOrdemRoteiro();
	
	Integer buscarMaiorOrdemRota(Long idRoteiro);
	
	void excluirRoteirizacao(List<Long> roteirizacaoId);
	
	List<CotaDisponivelRoteirizacaoDTO> buscarRoteirizacaoPorEndereco (String CEP, String uf, String municipio, String bairro,  Long rotaId ,  Long roteiroId);
	
	List<Roteiro> buscarRoteiroEspecial();
	
	List<ConsultaRoteirizacaoDTO> buscarRoteirizacaoSumarizadoPorCota(FiltroConsultaRoteirizacaoDTO filtro);
	
	List<ConsultaRoteirizacaoDTO> buscarRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro);
	
	List<ConsultaRoteirizacaoDTO> buscarRoteirizacaoPorNumeroCota(Integer numeroCota, TipoRoteiro tipoRoteiro, String  orderBy, Ordenacao ordenacao, int initialResult, int maxResults);
	
	void atualizaOrdenacao(Roteirizacao roteirizacao);

	Integer buscarQuantidadeRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro);
	
	List<ConsultaRoteirizacaoDTO> obterCotasParaBoxRotaRoteiro(Long idBox, Long idRota, Long idRoteiro, String sortname, String sortorder);
	
	
	
	//NOVA ROTEIRIZAÇÃO
	
	/**
	 * Obtém um Roteiro do box considerando a ordem
	 * @param idBox
	 * @return Roteiro
	 */
	Roteiro obterRoteiroDeBoxPorOrdem(Long idBox);
	
	/**
	 * Obtém o Roteiro ao qual a rota parametrizada pertence
	 * 
	 * @param rotaID
	 * @return
	 */
	Roteiro obterRoteiroPorRota(Long rotaID);
	
	/**
	 * Obtém um Rota do Roteiro considerando a ordem
	 * @param idRoteiro
	 * @return Rota
	 */
	Rota obterRotaDeRoteiroPorOrdem(Long idRoteiro);
	
	/**
	 * Obtém Boxes por nome
	 * 
	 * @param nome
	 * @return
	 */
	List<BoxRoteirizacaoDTO> obterBoxesPorNome(String nome);

	/**
	 * Filtra Roteiros por nome e boxes
	 * 
	 * @param nome
	 * @param idsBoxes
	 * @return
	 */
	List<RoteiroRoteirizacaoDTO> obterRoteirosPorNomeEBoxes(String nome, List<Long> idsBoxes);
	
	/**
	 * Filtra Rotas por nome e Roteiros
	 * 
	 * @param nome
	 * @param idsRoteiros
	 * @return
	 */
	List<RotaRoteirizacaoDTO> obterRotasPorNomeERoteiros(String nome, List<Long> idsRoteiros);
    
	/**
     * Obtem lista de Box do tipo lançamento
     * @param nomeBox do box para consulta
     * @return List<Box>
     */
	public List<Box> obterListaBoxLancamento(String nomeBox);
	
	/**
     * Obtem lista de Roteiro por Box
	 * @param descricaoRoteiro  descricao do roteiro para consulta
     * @return List<Roteiro>
     */
	public List<Roteiro> obterListaRoteiroPorBox(Long idBox, String descricaoRoteiro);
	
	/**
     * Obtem lista de Rota por Roteiro
     * @param idRoteiro identificador do roteiro
	 * @param descricaoRota descrição da rota para consulta
     * @return List<Rota>
     */
	public List<Rota> obterListaRotaPorRoteiro(Long idRoteiro, String descricaoRota);

	/**
	 * Obtém lista de Roteiros
	 * 
	 * @param numeroCota
	 * @return
	 */
	public List<Roteiro> obterRoteirosPorCota(Integer numeroCota);
	
	/**
     * Obtém a roteirização pelo identificador
     * @param id identificador da roteirização
     * @return RoteirizacaoDTO DTO com as informações da roteirização
     */
	RoteirizacaoDTO obterRoteirizacaoPorId(Long id);
	
	/**
	 * Obtém PDVS's disponiveis
	 * @param boxID 
	 * @return List<PdvRoteirizacaoDTO>
	 */
	public List<PdvRoteirizacaoDTO> obterPdvsDisponiveis(Integer numCota, String municipio, String uf, String bairro, String cep, boolean pesquisaPorCota, Long boxID);
	
	/**
	 * Verifica se pdv esta disponivel (não vinculado a um box roteirizado)
	 * @param idPdv
	 * @return boolean - true:disponivel
	 */
	 public boolean verificaDisponibilidadePdv(Long idPdv, Long idBox);
	
	/**
	 * Inclui Cota Pdv na Roteirização
	 * @param List<Long> idPdvs
	 * @param idRota
	 */
	public void incluirCotaPdv(List<Long> idPdvs, Long idRota);
	
	/**
	 * Exclui Cota Pdv na Roteirização
	 * @param List<Long> idPdvs
	 * @param idRota
	 */
	public void excluirCotaPdv(List<Long> idPdvs, Long idRota);
	
    /**
     * Recupera a roteirização pelo Box
     * 
     * @param idBox
     *            identificador do box para recuperação da roteirização
     * @return roteirização associada ao box ou nulo caso não exista
     *         roteirização associada ao Box
     */
	RoteirizacaoDTO obterRoteirizacaoPorBox(Long idBox);

    /**
     * Processa as informações de roteirização 
     * armazenadas no DTO
     * @param dto dto com as informações de Roteirização
     * @return {@link Roteirizacao} roteirização confirmada
     */
	Roteirizacao confirmarRoteirizacao(RoteirizacaoDTO dto);
	
    /**
     * Valida as informações da roteirização
     * 
     * @param dto
     *            DTO com as informações da roteirização
     * @return {@link ValidacaoVO} com a validação da roteirização
     */
	ValidacaoVO validarRoteirizacao(RoteirizacaoDTO dto);

	/**
	 * Carrega os entregadores em forma de rota no roteiro parametrizado
	 * 
	 * @param roteiroDTO
	 */
	void carregarRotasEntregadores(RoteiroRoteirizacaoDTO roteiroDTO);

	/**
	 * Carrega os entregadores em forma de rota em todos os roteiros da roteirização parametrizada
	 * 
	 * @param roteirizacaoDTO
	 */
	void carregarRotasEntregadores(RoteirizacaoDTO roteirizacao);
	
	/**
	 * Obtém as rotas que não pertencem ao roteiro parametrizado
	 * 
	 * @param roteiroID
	 * @return
	 */
	List<RotaRoteirizacaoDTO> obterRotasNaoAssociadasAoRoteiro(Long roteiroID);

	void validarAssociacaoRotaTransportador(Long rotaId, Long roteiroId);

	void validarAssociacaoRoteiroTransportador(Long roteiroId);

	List<RotaRoteirizacaoDTO> buscarRotasEspeciais();

	List<RoteiroRoteirizacaoDTO> buscarRoteirosNaoAssociadosAoBox(Long idBox);

	Long obterQtdRotasPorCota(Integer numeroCota);

	/**
	 * Obtem combo com todos os Roteiros
	 * @return List<ItemDTO<Long, String>>
	 */
	List<ItemDTO<Long, String>> getComboTodosRoteiros();

	/**
	 * Obtem combo com todas as Rotas
	 * @return List<ItemDTO<Long, String>>
	 */
	List<ItemDTO<Long, String>> getComboTodosRotas();

	/**
     * Obtem combo com todos os Boxes
     * @return List<ItemDTO<Long, String>>
     */
	List<ItemDTO<Long, String>> getComboTodosBoxes();

	/**
	 * Carrega o combo Box por Rota
	 * @param idRota
	 */
	List<ItemDTO<Long, String>> getComboBoxPorRota(Long idRota);

	/**
	 * Carrega o combo Box por Roteiro
	 * @param idRoteiro
	 */
	List<ItemDTO<Long, String>> getComboBoxPorRoteiro(Long idRoteiro);

	/**
	 * Carrega o combo Rota por intervalo de Box
	 * @param codigoBoxDe
	 * @param codigoBoxAte
	 */
	List<ItemDTO<Long, String>> getComboRotaPorBox(Integer codigoBoxDe,
			Integer codigoBoxAte);

	/**
	 * Carrega o combo Rota por Roteiro
	 * @param idRoteiro
	 */
	List<ItemDTO<Long, String>> getComboRotaPorRoteiro(Long idRoteiro);

	/**
	 * Carrega o combo Roteiro por Rota
	 * @param idRota
	 */
	List<ItemDTO<Long, String>> getComboRoteiroPorRota(Long idRota);

	/**
	 * Carrega o combo Roteiro por intervalo de Box
	 * @param codigoBoxDe
	 * @param codigoBoxAte
	 */
	List<ItemDTO<Long, String>> getComboRoteiroPorBox(Integer codigoBoxDe,
			Integer codigoBoxAte);

	List<MapaRoteirizacaoDTO> obterDetalheRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro);

	void roteiroTest();
}