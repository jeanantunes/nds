package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalCota;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalFornecedor;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;


public interface MovimentoFechamentoFiscalRepository extends Repository<MovimentoFechamentoFiscal, Long> {
	
	MovimentoFechamentoFiscalCota buscarPorChamadaEncalheCota(ChamadaEncalheCota chamadaEncalheCota);

	MovimentoFechamentoFiscalFornecedor buscarPorProdutoEdicaoTipoMovimentoEstoque(ProdutoEdicao produtoEdicao, TipoMovimentoEstoque tipoMovimentoEstoque);
	
}