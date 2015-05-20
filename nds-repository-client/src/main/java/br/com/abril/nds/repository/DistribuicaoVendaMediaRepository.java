package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.dto.filtro.FiltroEdicaoBaseDistribuicaoVendaMedia;

public interface DistribuicaoVendaMediaRepository {

    List<ProdutoEdicaoVendaMediaDTO> pesquisar(FiltroEdicaoBaseDistribuicaoVendaMedia filtro);

    List<ProdutoEdicaoVendaMediaDTO> pesquisar(FiltroEdicaoBaseDistribuicaoVendaMedia filtro, boolean usarICD);

    List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao, Long idClassificacao);

    List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao, Long idClassificacao, boolean usarICD);

	List<ProdutoEdicaoVendaMediaDTO> pesquisarEdicoesBasesParaLancamentoParcial(FiltroEdicaoBaseDistribuicaoVendaMedia filtro, boolean isFindByICD);

	Date obterDataLancamentoValidaParaParcialConsolidada(BigInteger idProduto, BigInteger numeroEdicao);

}
