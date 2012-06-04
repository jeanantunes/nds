package br.com.abril.nds.service;

import java.util.List;

import org.hibernate.criterion.MatchMode;
import br.com.abril.nds.dto.CotaDisponivelRoteirizacaoDTO;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
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
	
	List<CotaDisponivelRoteirizacaoDTO> buscarPvsPorCota(Integer numeroCota);
	
	void gravaRoteirizacao(List<CotaDisponivelRoteirizacaoDTO> lista,  Long idRota);
	
	Integer buscarMaiorOrdemRoteiro();
	
	Integer buscarMaiorOrdemRota(Long idRoteiro);
}
