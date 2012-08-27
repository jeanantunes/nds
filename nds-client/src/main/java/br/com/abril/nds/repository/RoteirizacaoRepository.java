package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.model.LogBairro;
import br.com.abril.nds.model.LogLocalidade;
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
    	
	List<String> buscarUF();
	
	List<LogLocalidade> buscarMunicipioPorUf(String uf);
	
	List<LogBairro> buscarBairroPorMunicipio(Long municipio, String uf);
	
	List<ConsultaRoteirizacaoDTO> buscarRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro);
	
	List<ConsultaRoteirizacaoDTO>  buscarRoteirizacaoPorNumeroCota(Integer numeroCota, TipoRoteiro tipoRoteiro, String  orderBy, Ordenacao ordenacao, int initialResult, int maxResults);
	
	void atualizaOrdenacao(Roteirizacao roteirizacao );
	
	void atualizaOrdenacaoAsc(Roteirizacao roteirizacao);
	
	void atualizaOrdenacaoDesc(Roteirizacao roteirizacao );	
	
	List<ConsultaRoteirizacaoDTO> buscarRoteirizacaoSumarizadoPorCota(FiltroConsultaRoteirizacaoDTO filtro);

	Integer buscarQuantidadeRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro);

	Integer buscarQuantidadeRoteirizacaoSumarizadoPorCota(FiltroConsultaRoteirizacaoDTO filtro); 
}

