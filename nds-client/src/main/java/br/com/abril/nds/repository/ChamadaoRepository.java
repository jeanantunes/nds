package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.model.cadastro.Cota;

public interface ChamadaoRepository extends Repository<Cota,Long> {

	List<ConsignadoCotaChamadaoDTO> obterConsignadosParaChamadao(FiltroChamadaoDTO filtro);
	
	Long obterTotalConsignadosParaChamadao(FiltroChamadaoDTO filtro);
	
	ResumoConsignadoCotaChamadaoDTO obterResumoConsignadosParaChamadao(FiltroChamadaoDTO filtro);

}
