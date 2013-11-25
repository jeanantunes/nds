package br.com.abril.nds.repository;

<<<<<<< HEAD
import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroEstoqueProdutosRecolhimento;
=======
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ProdutoEdicaoSuplementarDTO;
>>>>>>> fase2
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoDTO;
import br.com.abril.nds.model.estoque.EstoqueProdutoRecolimentoDTO;

public interface EstoqueProdutoRespository extends Repository<EstoqueProduto, Long> {

	EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao);
	
	EstoqueProduto buscarEstoqueProdutoPorProdutoEdicao(Long idProdutoEdicao);
	
<<<<<<< HEAD
	List<EstoqueProdutoDTO> buscarEstoquesProdutos();

	Long buscarEstoqueProdutoRecolhimentoCount(FiltroEstoqueProdutosRecolhimento filtro);

	List<EstoqueProdutoRecolimentoDTO> buscarEstoqueProdutoRecolhimento(
			FiltroEstoqueProdutosRecolhimento filtro);
}
=======
	List<ProdutoEdicaoSuplementarDTO> obterProdutosEdicaoSuplementarNaoDisponivel(Long idCotaAusente, Date dataMovimento);
}
>>>>>>> fase2
