package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaFuroDTO;
import br.com.abril.nds.model.movimentacao.FuroProduto;

public interface FuroProdutoRepository extends Repository<FuroProduto, Long>{

	FuroProduto obterFuroProdutoPor(Long lancamentoId, Long produtoEdicaoId);

	List<CotaFuroDTO> obterCobrancaRealizadaParaCotaVista(Long idProdutoEdicao, Date dataFuro, Long idLancamento);
	
}
