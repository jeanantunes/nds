package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.dto.filtro.FiltroEdicaoBaseDistribuicaoVendaMedia;

public interface DistribuicaoVendaMediaService {

    public abstract List<ProdutoEdicaoVendaMediaDTO> pesquisarEdicoesParciais(String codigoProduto, Integer periodo, Long edicao);

    public abstract List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao, Long idClassificacao, boolean usarICD);

    public abstract List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao, Long idClassificacao);

    public abstract List<ProdutoEdicaoVendaMediaDTO> pesquisar(FiltroEdicaoBaseDistribuicaoVendaMedia filtro, boolean usarICD);

    public abstract List<ProdutoEdicaoVendaMediaDTO> pesquisar(FiltroEdicaoBaseDistribuicaoVendaMedia filtro);
    
}
