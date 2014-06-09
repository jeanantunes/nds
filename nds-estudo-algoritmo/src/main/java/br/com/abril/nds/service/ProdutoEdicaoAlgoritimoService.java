package br.com.abril.nds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;

@Service
@Transactional(readOnly = true)
public class ProdutoEdicaoAlgoritimoService {
    
    @Autowired
    private ProdutoEdicaoDAO produtoEdicaoDAO;
    
    /**
     * @param cota
     * @return
     * @see br.com.abril.nds.dao.ProdutoEdicaoDAO#getEdicaoRecebidas(br.com.abril.nds.model.estudo.CotaEstudo)
     */
    public List<ProdutoEdicaoEstudo> getEdicaoRecebidas(CotaEstudo cota) {
        return produtoEdicaoDAO.getEdicaoRecebidas(cota);
    }
    
    /**
     * @param cota
     * @param produto
     * @return
     * @see br.com.abril.nds.dao.ProdutoEdicaoDAO#getEdicaoRecebidas(br.com.abril.nds.model.estudo.CotaEstudo,
     *      br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo)
     */
    public List<ProdutoEdicaoEstudo> getEdicaoRecebidas(CotaEstudo cota, ProdutoEdicaoEstudo produto) {
        return produtoEdicaoDAO.getEdicaoRecebidas(cota, produto);
    }
    
    /**
     * @param cota
     * @param produtoEdicao
     * @return
     * @see br.com.abril.nds.dao.ProdutoEdicaoDAO#getQtdeVezesReenviadas(br.com.abril.nds.model.estudo.CotaEstudo,
     *      br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo)
     */
    public int getQtdeVezesReenviadas(CotaEstudo cota, ProdutoEdicaoEstudo produtoEdicao) {
        return produtoEdicaoDAO.getQtdeVezesReenviadas(cota, produtoEdicao);
    }
    
    /**
     * @param codigoProduto
     * @param numeroEdicao
     * @param idLancamento
     * @return
     * @see br.com.abril.nds.dao.ProdutoEdicaoDAO#getProdutoEdicaoEstudo(java.lang.String,
     *      java.lang.Long, java.lang.Long)
     */
    public ProdutoEdicaoEstudo getProdutoEdicaoEstudo(String codigoProduto, Long numeroEdicao, Long idLancamento) {
        return produtoEdicaoDAO.getProdutoEdicaoEstudo(codigoProduto, numeroEdicao, idLancamento);
    }

}
