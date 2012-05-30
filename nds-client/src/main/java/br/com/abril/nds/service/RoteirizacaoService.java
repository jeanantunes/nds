package br.com.abril.nds.service;

import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.springframework.transaction.annotation.Transactional;

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
	
	void excluirListaRota(List<Long> rotasId);
	
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
}
