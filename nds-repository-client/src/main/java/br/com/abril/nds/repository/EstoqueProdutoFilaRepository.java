package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.estoque.EstoqueProdutoFila;

public interface EstoqueProdutoFilaRepository extends Repository<EstoqueProdutoFila, Long> {
	
	List<EstoqueProdutoFila> buscarEstoqueProdutoFilaDaCota(Long idCota);
	
	List<EstoqueProdutoFila> buscarEstoqueProdutoFilaNumeroCota(Integer numeroCota);
	
}
