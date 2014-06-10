package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CaracteristicaDistribuicaoDTO;
import br.com.abril.nds.dto.CaracteristicaDistribuicaoSimplesDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoSimplesDTO;

public interface CaracteristicaDistribuicaoRepository {


	public List<CaracteristicaDistribuicaoDTO> obterCaracteristicaDistribuicaoDetalhe(FiltroConsultaCaracteristicaDistribuicaoDetalheDTO filtro);
	
	
	public List<CaracteristicaDistribuicaoSimplesDTO> obterCaracteristicaDistribuicaoSimples(FiltroConsultaCaracteristicaDistribuicaoSimplesDTO filtro);
}
