package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.util.Intervalo;

public interface MovimentoEstoqueCotaService {

	List<MovimentoEstoqueCota> obterMovimentoCotaPorTipoMovimento(Date data, Long idCota, GrupoMovimentoEstoque grupoMovimentoEstoque);
	
	List<MovimentoEstoqueCota> obterMovimentoCotaPorTipoMovimento(Date data, Integer numCota, GrupoMovimentoEstoque grupoMovimentoEstoque);

	List<MovimentoEstoqueCotaDTO> obterMovimentoDTOCotaPorTipoMovimento(Date date,List<Integer> numCotas, GrupoMovimentoEstoque envioJornaleiro);
	
	List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaPor(Distribuidor distribuidor, Long idCota, 
			TipoNotaFiscal tipoNotaFiscal, List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques, 
			Intervalo<Date> periodo, List<Long> listaFornecedores, List<Long> listaProdutos);

	Long obterQuantidadeReparteProdutoCota(Long idProdutoEdicao,
			Integer numeroCota);
	
	/**
	 * Método responsável por realizar a transferência do reparte, referente ao Estoque da Cota
	 * para o Estoque do Distribuidor, como "suplementar".
	 * 
	 * @param distribuidor
	 * @param idCota
	 * @param periodo
	 * @param listaIdFornecedores
	 * @param listaIdProduto
	 * @param tipoNotaFiscal
	 */
	void transferirReparteParaSuplementar(Distribuidor distribuidor, List<Long> idsCota, Intervalo<Date> periodo, 
			List<Long> listaIdFornecedores, List<Long> listaIdProduto, TipoNotaFiscal tipoNotaFiscal);
	
	/**
	 * Gera movimento para cancelamento de nota, 
	 * remove produtos da nota do estoque da Cota
	 * 
	 * @param notaFiscal
	 */
	void envioConsignadoNotaCancelada(NotaFiscal notaFiscalCancelada);

}
