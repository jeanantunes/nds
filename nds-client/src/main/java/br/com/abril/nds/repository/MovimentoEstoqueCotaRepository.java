package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;


public interface MovimentoEstoqueCotaRepository extends Repository<MovimentoEstoqueCota, Long>{

	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(Date dataOperacao, List<Long> listaIdProdutoDosFornecedores);

	
}
