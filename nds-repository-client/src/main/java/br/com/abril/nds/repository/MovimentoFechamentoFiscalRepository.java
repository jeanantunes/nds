package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalCota;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalFornecedor;

public interface MovimentoFechamentoFiscalRepository extends Repository<MovimentoFechamentoFiscal, Long> {
	
	List<MovimentoFechamentoFiscalCota> buscarPorChamadaEncalheCota(Long chamadaEncalheId);

	MovimentoFechamentoFiscalFornecedor buscarPorProdutoEdicaoTipoMovimentoEstoque(ProdutoEdicao produtoEdicao, TipoMovimentoEstoque tipoMovimentoEstoque);

	List<Long> obterMECIdsPelosMovFechamentosFiscaisCota(List<Long> idsMFFC);

	void atualizarMovimentosFechamentosFiscaisPorLancamento(long lancamentoId, boolean desobrigaEmissaoDevolucaoSimbolica, boolean desobrigaEmissaoVendaConsignado);
	
}