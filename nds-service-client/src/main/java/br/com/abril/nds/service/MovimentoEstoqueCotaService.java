package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.util.Intervalo;

public interface MovimentoEstoqueCotaService {

	List<MovimentoEstoqueCota> obterMovimentoCotaPorTipoMovimento(Date data, Long idCota, GrupoMovimentoEstoque grupoMovimentoEstoque);
	
	List<MovimentoEstoqueCota> obterMovimentoCotaPorTipoMovimento(Date data, Integer numCota, GrupoMovimentoEstoque grupoMovimentoEstoque);

	List<MovimentoEstoqueCotaDTO> obterMovimentoDTOCotaPorTipoMovimento(Date date,List<Integer> numCotas, List<GrupoMovimentoEstoque> gruposMovimentoEstoque);
	
	List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaPor(ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor, Long idCota, 
			NaturezaOperacao tipoNotaFiscal, List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques, 
			Intervalo<Date> periodo, List<Long> listaFornecedores, List<Long> listaProdutos);

	Long obterQuantidadeReparteProdutoCota(Long idProdutoEdicao,
			Integer numeroCota);
	
	/**
	 * Método responsável por realizar a transferência do reparte, referente ao Estoque da Cota
	 * para o Estoque do Distribuidor, como "suplementar".
	 * 
	 * @param parametrosRecolhimentoDistribuidor
	 * @param idCota
	 * @param periodo
	 * @param listaIdFornecedores
	 * @param listaIdProduto
	 * @param tipoNotaFiscal
	 */
	void transferirReparteParaSuplementar(ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor, 
			List<Long> idsCota, Intervalo<Date> periodo, 
			List<Long> listaIdFornecedores, List<Long> listaIdProduto, NaturezaOperacao tipoNotaFiscal);
	
	/**
	 * Gera movimento para cancelamento de nota, 
	 * remove produtos da nota do estoque da Cota
	 * 
	 * @param notaFiscal
	 */
	void envioConsignadoNotaCancelada(NotaFiscal notaFiscalCancelada);
	
	Date obterDataUltimaMovimentacaoReparteExpedida(Integer numeroCota, Long idProdutoEdicao);

	void atualizarPrecoProdutoExpedido(final Long id,final BigDecimal precoProduto);
}
