package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;
import br.com.abril.nds.util.Intervalo;

public interface ItemChamadaEncalheFornecedorRepository extends Repository<ItemChamadaEncalheFornecedor, Long>{

	public BigDecimal obterTotalDoDescontoItensChamadaEncalheFornecedor(Long idChamadaEncalheFornecedor);
	
	List<ItemChamadaEncalheFornecedor> obterItensChamadaEncalheFornecedor(Long idChamadaEncalheFornecedor,Intervalo<Date> periodo);

	
}
