package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.dto.filtro.FiltroEdicaoBaseDistribuicaoVendaMedia;
import br.com.abril.nds.repository.DistribuicaoVendaMediaRepository;
import br.com.abril.nds.service.DistribuicaoVendaMediaService;

@Service
@Transactional(readOnly = true)
public class DistribuicaoVendaMediaServiceImpl implements DistribuicaoVendaMediaService {
    
    @Autowired
    private DistribuicaoVendaMediaRepository distribuicaoVendaMediaRepository;
    
    /**
     * @param filtro
     * @return
     * @see br.com.abril.nds.repository.DistribuicaoVendaMediaRepository#pesquisar(br.com.abril.nds.dto.filtro.FiltroEdicaoBaseDistribuicaoVendaMedia)
     */
    @Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(FiltroEdicaoBaseDistribuicaoVendaMedia filtro) {
        return distribuicaoVendaMediaRepository.pesquisar(filtro);
    }
    
    /**
     * @param filtro
     * @param usarICD
     * @return
     * @see br.com.abril.nds.repository.DistribuicaoVendaMediaRepository#pesquisar(br.com.abril.nds.dto.filtro.FiltroEdicaoBaseDistribuicaoVendaMedia,
     *      boolean)
     */
    @Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(FiltroEdicaoBaseDistribuicaoVendaMedia filtro, boolean usarICD) {
        return distribuicaoVendaMediaRepository.pesquisar(filtro, usarICD);
    }
    
    /**
     * @param codigoProduto
     * @param nomeProduto
     * @param edicao
     * @param idClassificacao
     * @return
     * @see br.com.abril.nds.repository.DistribuicaoVendaMediaRepository#pesquisar(java.lang.String,
     *      java.lang.String, java.lang.Long, java.lang.Long)
     */
    @Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao,
            Long idClassificacao) {
        return distribuicaoVendaMediaRepository.pesquisar(codigoProduto, nomeProduto, edicao, idClassificacao);
    }
    
    /**
     * @param codigoProduto
     * @param nomeProduto
     * @param edicao
     * @param idClassificacao
     * @param usarICD
     * @return
     * @see br.com.abril.nds.repository.DistribuicaoVendaMediaRepository#pesquisar(java.lang.String,
     *      java.lang.String, java.lang.Long, java.lang.Long, boolean)
     */
    @Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao,
            Long idClassificacao, boolean usarICD) {
        return distribuicaoVendaMediaRepository.pesquisar(codigoProduto, nomeProduto, edicao, idClassificacao, usarICD);
    }

    /**
     * @param codigoProduto
     * @param periodo
     * @param edicao
     * @return
     * @see br.com.abril.nds.repository.DistribuicaoVendaMediaRepository#pesquisarEdicoesParciais(java.lang.String,
     *      java.lang.Integer, java.lang.Long)
     */
    @Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisarEdicoesParciais(String codigoProduto, Integer periodo, Long edicao) {
        return distribuicaoVendaMediaRepository.pesquisarEdicoesParciais(codigoProduto, periodo, edicao);
    }
    
}