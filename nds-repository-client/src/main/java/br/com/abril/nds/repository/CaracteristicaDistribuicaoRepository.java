package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CaracteristicaDistribuicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoDetalheDTO;

public interface CaracteristicaDistribuicaoRepository {


	public List<CaracteristicaDistribuicaoDTO> obterCaracteristicaDistribuicaoDetalhe(FiltroConsultaCaracteristicaDistribuicaoDetalheDTO filtro);

}
