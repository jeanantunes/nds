package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.dto.filtro.FiltroEdicaoBaseDistribuicaoVendaMedia;
import br.com.abril.nds.repository.DistribuicaoVendaMediaRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.DistribuicaoVendaMediaService;
import br.com.abril.nds.service.EstoqueProdutoService;

@Service
@Transactional(readOnly = true)
public class DistribuicaoVendaMediaServiceImpl implements DistribuicaoVendaMediaService {
    
    @Autowired
    private DistribuicaoVendaMediaRepository distribuicaoVendaMediaRepository;
    
    @Autowired
    private LancamentoRepository lancamentoRepository;
    
    @Autowired
    private EstoqueProdutoService estoqueProdutoService;
    
    /**
     * @param filtro
     * @return
     * @see br.com.abril.nds.repository.DistribuicaoVendaMediaRepository#pesquisar(br.com.abril.nds.dto.filtro.FiltroEdicaoBaseDistribuicaoVendaMedia)
     */
    @Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(FiltroEdicaoBaseDistribuicaoVendaMedia filtro) {
        
		List<ProdutoEdicaoVendaMediaDTO> prodEdicaoParabaseEstudo = new ArrayList<>();
    	
    	if((filtro.getIdLancamento()!=null) && (lancamentoRepository.isLancamentoParcial(filtro.getIdLancamento()))) {
    		
    		prodEdicaoParabaseEstudo = distribuicaoVendaMediaRepository.pesquisarEdicoesBasesParaLancamentoParcial(filtro, true);
    		
    	} else {
    		
    		prodEdicaoParabaseEstudo = distribuicaoVendaMediaRepository.pesquisar(filtro);
    	}
    	
    	tratarVendaZero(prodEdicaoParabaseEstudo);
    	
    	return prodEdicaoParabaseEstudo;
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
    @Transactional(readOnly=true)
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao, Long idClassificacao, boolean usarICD, boolean isParcial) {

    	List<ProdutoEdicaoVendaMediaDTO> prodEdicaoParabaseEstudo = new ArrayList<>();
    	
    	if(isParcial) {
    		
    		prodEdicaoParabaseEstudo = distribuicaoVendaMediaRepository.pesquisarEdicoesBasesParaLancamentoParcial(new FiltroEdicaoBaseDistribuicaoVendaMedia(codigoProduto, nomeProduto, edicao, idClassificacao, 0L), usarICD);
    	} else {
    		
    		prodEdicaoParabaseEstudo = distribuicaoVendaMediaRepository.pesquisar(codigoProduto, nomeProduto, edicao, idClassificacao, usarICD);
    	}
    	
    	tratarVendaZero(prodEdicaoParabaseEstudo);
    	
    	return prodEdicaoParabaseEstudo;
    	
    }

	private void tratarVendaZero(List<ProdutoEdicaoVendaMediaDTO> prodEdicaoParabaseEstudo) {
		
		for (ProdutoEdicaoVendaMediaDTO peVendaMediaDTO : prodEdicaoParabaseEstudo) {
			if((peVendaMediaDTO.getStatus() != null && peVendaMediaDTO.getStatus().equalsIgnoreCase("FECHADO")) 
					&& (peVendaMediaDTO.getVenda() == null || peVendaMediaDTO.getVenda().compareTo(BigInteger.ZERO) <= 0)) {
				
				BigDecimal vendas = estoqueProdutoService.obterVendaBaseadoNoEstoque(peVendaMediaDTO.getId().longValue());
				peVendaMediaDTO.setVenda(vendas != null ? vendas.toBigInteger() : BigInteger.valueOf(0));
			}
		}
		
	}

}
