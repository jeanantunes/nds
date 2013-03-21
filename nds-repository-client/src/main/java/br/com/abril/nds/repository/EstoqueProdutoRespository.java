package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ProdutoEdicaoSuplementarDTO;
import br.com.abril.nds.model.estoque.EstoqueProduto;

public interface EstoqueProdutoRespository extends Repository<EstoqueProduto, Long> {

	EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao);
	
	EstoqueProduto buscarEstoqueProdutoPorProdutoEdicao(Long idProdutoEdicao);
	
	List<ProdutoEdicaoSuplementarDTO> obterProdutosEdicaoSuplementarNaoDisponivel(Long idCotaAusente, Date dataMovimento);
}
