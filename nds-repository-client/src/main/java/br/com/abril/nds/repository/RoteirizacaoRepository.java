package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.BoxRoteirizacaoDTO;
import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.MapaRoteirizacaoDTO;
import br.com.abril.nds.dto.RotaRoteirizacaoDTO;
import br.com.abril.nds.dto.RoteiroRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface RoteirizacaoRepository extends Repository<Roteirizacao, Long> {
	
	Roteirizacao buscarRoteirizacaoDeCota(Integer numeroCota);
	
	/**
	 * Retorna uma lista de Roteirizacao.
	 * @param sortname - nome do campo para ordenação
	 * @param ordenacao - tipo da ordenção 
	 * @return List<Rota>
	 */
    List<Roteirizacao> buscarRoterizacaoPorRota(Long rotaId);
    
    Integer buscarMaiorOrdem(Long rotaId);
    
    List<PDV> buscarPdvRoteirizacaoNumeroCota(Integer numeroCota, Long rotaId, Roteiro roteiro  );
    
    List<PDV> buscarRoteirizacaoPorEndereco (String CEP, String uf, String municipio, String bairro, Long rotaId , Roteiro roteiro );
    	
	List<ConsultaRoteirizacaoDTO> buscarRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro);
	
	List<ConsultaRoteirizacaoDTO>  buscarRoteirizacaoPorNumeroCota(Integer numeroCota, TipoRoteiro tipoRoteiro, String  orderBy, Ordenacao ordenacao, int initialResult, int maxResults);
	
	List<ConsultaRoteirizacaoDTO> buscarRoteirizacaoSumarizadoPorCota(FiltroConsultaRoteirizacaoDTO filtro);

	Integer buscarQuantidadeRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro);

	Integer buscarQuantidadeRoteirizacaoSumarizadoPorCota(FiltroConsultaRoteirizacaoDTO filtro); 
	
	
	//NOVA ROTEIRIZAÇÃO
	
	/**
	 * Obtém lista com dados de cotas relativas a determinado box, rota e roteiro.
	 * 
	 * @param idBox
	 * @param idRota
	 * @param idRoteiro
	 * 
	 * @return List<ConsultaRoteirizacaoDTO>
	 */
	List<ConsultaRoteirizacaoDTO> obterCotasParaBoxRotaRoteiro(Long idBox, Long idRota, Long idRoteiro, String sortname, String sortorder);

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
	 * Obtém a roteirização por Box
	 * @param idBox identificador do Box para recuperação da roteirização
	 * @return Roteirizacao Roteirização associada ao Box ou null caso não
	 * exista Roteirização associada ao box
	 */
	Roteirizacao obterRoteirizacaoPorBox(Long idBox);
	
	/**
	 * Obtém o Box de um PDV
	 * @param idPdv
	 * @return
	 */
	Box obterBoxDoPDV(Long... idPdv);
	
	List<Integer> obterNumerosCotaOrdenadosRoteirizacao();
	
	boolean existeRotaParaCota(Integer numeroCota);

	List<ConsultaRoteirizacaoDTO> obterDetalheRoteiricao(FiltroConsultaRoteirizacaoDTO filtro);
	
	 List<MapaRoteirizacaoDTO> sumarizadoRelPorCota(FiltroConsultaRoteirizacaoDTO filtro);
}

