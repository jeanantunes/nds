package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;

public interface DistribuicaoVendaMediaRepository {

    List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao);
}
