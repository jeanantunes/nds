package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CaracteristicaDistribuicaoDTO;
import br.com.abril.nds.dto.CaracteristicaDistribuicaoSimplesDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoSimplesDTO;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;

public interface CaracteristicaDistribuicaoService {
	public List<TipoClassificacaoProduto> obterClassificacoesProduto();

	public List<CaracteristicaDistribuicaoDTO> buscarComFiltroCompleto(FiltroConsultaCaracteristicaDistribuicaoDetalheDTO filtro);

	public List<CaracteristicaDistribuicaoSimplesDTO> buscarComFiltroSimples(FiltroConsultaCaracteristicaDistribuicaoSimplesDTO filtro);
}
