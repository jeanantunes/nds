package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.dto.filtro.FiltroEdicaoBaseDistribuicaoVendaMedia;

public interface DistribuicaoVendaMediaRepository {

    List<ProdutoEdicaoVendaMediaDTO> pesquisar(FiltroEdicaoBaseDistribuicaoVendaMedia filtro);
    
    List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao);
    
    List<ProdutoEdicaoVendaMediaDTO> pesquisarEdicoesParciais(String codigoProduto, Integer periodo, Long edicao);
}
