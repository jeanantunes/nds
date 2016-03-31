package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;

public interface ItemChamadaEncalheFornecedorRepository extends Repository<ItemChamadaEncalheFornecedor, Long>{

	public BigDecimal obterTotalDoDescontoItensChamadaEncalheFornecedor(Long idChamadaEncalheFornecedor);
	
	List<ItemChamadaEncalheFornecedor> obterItensChamadaEncalheFornecedor(Long idChamadaEncalheFornecedor);

	void removerItensChamadaEncalheFornecedor(List<ItemChamadaEncalheFornecedor> itens);
}
