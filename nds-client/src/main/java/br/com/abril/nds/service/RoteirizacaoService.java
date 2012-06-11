package br.com.abril.nds.service;

import java.util.List;

import org.hibernate.criterion.MatchMode;
import br.com.abril.nds.dto.CotaDisponivelRoteirizacaoDTO;
import br.com.abril.nds.model.LogBairro;
import br.com.abril.nds.model.LogLocalidade;
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

}
	
