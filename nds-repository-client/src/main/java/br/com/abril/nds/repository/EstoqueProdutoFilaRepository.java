package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.EstoqueProdutoFilaDTO;
import br.com.abril.nds.model.estoque.EstoqueProdutoFila;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;

public interface EstoqueProdutoFilaRepository extends Repository<EstoqueProdutoFila, Long> {
	
	boolean verificarExitenciaEstoqueProdutoFila();
	
	List<EstoqueProdutoFilaDTO> buscarTodosEstoqueProdutoFila();
	
	List<EstoqueProdutoFila> buscarEstoqueProdutoFilaDaCota(Long idCota);
	
	List<EstoqueProdutoFila> buscarEstoqueProdutoFilaNumeroCota(Integer numeroCota);

	public abstract void insert(Long idCota, Long idProdutoEdicao, TipoEstoque tipoEstoque,
			OperacaoEstoque operacaoEstoque, BigInteger qtde);
	
}
