package br.com.abril.nds.service;

import java.util.List;

import org.hibernate.criterion.MatchMode;

import br.com.abril.nds.dto.BoxRoteirizacaoDTO;
import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.CotaDisponivelRoteirizacaoDTO;
import br.com.abril.nds.dto.RotaRoteirizacaoDTO;
import br.com.abril.nds.dto.RoteiroRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.model.LogBairro;
import br.com.abril.nds.model.LogLocalidade;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface RoteirizacaoService {
	
	List<Roteiro> buscarRoteiro(String sortname, Ordenacao ordenacao);
	
	List<Rota> buscarRota(String sortname, Ordenacao ordenacao);

	void incluirRoteiro(Roteiro roteiro);
	
	List<Roteiro> buscarRoteiroPorDescricao(String descricao, MatchMode matchMode);
	
	List<Rota> buscarRotaPorRoteiro(Long idRoteiro);
	
	void incluirRota(Rota rota);
	
	void excluirListaRota(List<Long> rotasId, Long roteiroId);
	
	void transferirListaRota(List<Long> rotasId, Long roteiroId);
	
	void transferirListaRotaComNovoRoteiro(List<Long> rotasId, Roteiro roteiro);
	
	List<Rota> buscarRotas();
	
	List<Roteiro> buscarRoteiros();
	
	List<Roteiro> buscarRoteiroDeBox(Long idBox);
	
	List<Rota> buscarRotaDeBox(Long idBox);
	
	Roteirizacao buscarRoteirizacaoDeCota(Integer numeroCota);
	
	List<Rota> buscarRotaPorRoteiro(String descRoteiro);
	
	Rota buscarRotaPorId(Long idRota);
	 
	Roteiro buscarRoteiroPorId(Long idRoteiro);

	List<Rota> buscarRotaPorNome(Long roteiroId, String rotaNome, MatchMode matchMode);
	
	List<CotaDisponivelRoteirizacaoDTO> buscarRoterizacaoPorRota(Long rotaId);
	
	List<CotaDisponivelRoteirizacaoDTO> buscarPvsPorCota(Integer numeroCota,  Long rotaId,  Long roteiroId );
	
	void gravaRoteirizacao(List<CotaDisponivelRoteirizacaoDTO> lista,  Long idRota);

	/**
	 * Obtem rotas por numero da cota
	 * 
	 * @param numeroCota - Numero da Cota
	 * @return Lista de Rotas
	 */
	
	List<Rota> obterRotasPorCota(Integer numeroCota);
	
	Integer buscarMaiorOrdemRoteiro();
	
	Integer buscarMaiorOrdemRota(Long idRoteiro);
	
	void transferirRoteirizacao(List<Long> roteirizacaoId,Rota rota);
	
	void excluirRoteirizacao(List<Long> roteirizacaoId);
	
	List<CotaDisponivelRoteirizacaoDTO> buscarRoteirizacaoPorEndereco (String CEP, String uf, String municipio, String bairro,  Long rotaId ,  Long roteiroId);
	
	List<String> buscarUF();
	
	List<LogLocalidade> buscarMunicipioPorUf(String uf);
	
	List<LogBairro> buscarBairroPorMunicipio(Long municipio, String uf);
	
	List<Roteiro> buscarRoteiroEspecial();
	
	List<ConsultaRoteirizacaoDTO> buscarRoteirizacaoSumarizadoPorCota(FiltroConsultaRoteirizacaoDTO filtro);
	
	List<ConsultaRoteirizacaoDTO> buscarRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro);
	
	List<ConsultaRoteirizacaoDTO> buscarRoteirizacaoPorNumeroCota(Integer numeroCota, TipoRoteiro tipoRoteiro, String  orderBy, Ordenacao ordenacao, int initialResult, int maxResults);
	
	void transferirRoteirizacaoComNovaRota(List<Long> roteirizacaoId,Rota rota);
	
	void atualizaOrdenacao(Roteirizacao roteirizacao);
	
	void atualizaOrdenacaoAsc(Roteirizacao roteirizacao);
	
	void atualizaOrdenacaoDesc(Roteirizacao roteirizacao);

	Integer buscarQuantidadeRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro);
	
	List<ConsultaRoteirizacaoDTO> obterCotasParaBoxRotaRoteiro(Long idBox, Long idRota, Long idRoteiro);
	
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
     * @return List<Box>
     */
	public List<Box> obterListaBoxLancamento();
	
	/**
     * Obtem lista de Roteiro por Box
     * @return List<Roteiro>
     */
	public List<Roteiro> obterListaRoteiroPorBox(Long idBox);
	
	/**
     * Obtem lista de Rota por Roteiro
     * @return List<Rota>
     */
	public List<Rota> obterListaRotaPorRoteiro(Long idRoteiro);
}
	
