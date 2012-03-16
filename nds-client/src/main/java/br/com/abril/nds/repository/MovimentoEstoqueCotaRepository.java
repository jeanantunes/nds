package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;


public interface MovimentoEstoqueCotaRepository extends Repository<MovimentoEstoqueCota, Long>{

	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(
			Date dataRecolhimentoDistribuidor,
			TipoMovimentoEstoque tipoMovimento,
			List<Long> listaIdProdutoDosFornecedores);

	
}
