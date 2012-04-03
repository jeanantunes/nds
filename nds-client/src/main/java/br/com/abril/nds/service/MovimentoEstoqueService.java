package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;

public interface MovimentoEstoqueService {

	void gerarMovimentoEstoqueDeExpedicao(Date dataLancamento, Long idProdutoEdicao, Long idUsuario);
	
	MovimentoEstoque gerarMovimentoEstoque(Date dataLancamento, Long idProdutoEdicao,Long idUsuario,BigDecimal quantidade,TipoMovimentoEstoque tipoMovimentoEstoque);
	
	void gerarMovimentoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, Long idUsuario, BigDecimal quantidade,TipoMovimentoEstoque tipoMovimentoEstoque);
	
	void enviarSuplementarCotaAusente(Date data, Long idCota,List<MovimentoEstoqueCota> listaMovimentoCota);
}
