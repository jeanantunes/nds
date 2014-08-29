package br.com.abril.nds.devolucao.builder;

import br.com.abril.nds.dto.CotaEmissaoDTO;

public class ChamadaEncalheBuilder {
    
    public CotaEmissaoDTO montarHearderEmissao() {
        return null;
    }
    
    public void montarItensEmissao() {
        
        /*
        
        BigDecimal vlrReparte = BigDecimal.ZERO;    
        BigDecimal vlrDesconto = BigDecimal.ZERO;
        BigDecimal vlrEncalhe = BigDecimal.ZERO;    
        
        List<Long> idsProdutoEdicao = new ArrayList<>();
        
        
        for(ProdutoEmissaoDTO produtoDTO : cota.getProdutos()) {
            
            idsProdutoEdicao.add(produtoDTO.getIdProdutoEdicao());
            
            if(!produtoDTO.isApresentaQuantidadeEncalhe()) {
                produtoDTO.setQuantidadeDevolvida(null);
            }
            
            produtoDTO.setReparte( (produtoDTO.getReparte()==null) ? BigInteger.ZERO : produtoDTO.getReparte() );
            
            produtoDTO.setVlrDesconto( (produtoDTO.getVlrDesconto() == null) ? BigDecimal.ZERO :  produtoDTO.getVlrDesconto());
            
            produtoDTO.setQuantidadeDevolvida(  (produtoDTO.getQuantidadeDevolvida() == null) ? BigInteger.ZERO : produtoDTO.getQuantidadeDevolvida());
            
            if(produtoDTO.getConfereciaRealizada() == true) { 
                produtoDTO.setVendido(produtoDTO.getReparte().subtract(produtoDTO.getQuantidadeDevolvida()));
            } else { 
                produtoDTO.setVendido(BigInteger.ZERO);
            }
            
            produtoDTO.setVlrVendido(CurrencyUtil.formatarValor(produtoDTO.getVlrPrecoComDesconto().multiply(BigDecimal.valueOf(produtoDTO.getVendido().longValue()))));
            
            vlrReparte = vlrReparte.add( produtoDTO.getPrecoVenda().multiply(BigDecimal.valueOf(produtoDTO.getReparte().longValue())));

            vlrDesconto = vlrDesconto.add(produtoDTO.getPrecoVenda().subtract(produtoDTO.getVlrPrecoComDesconto())
                    .multiply(BigDecimal.valueOf(produtoDTO.getReparte().longValue())));
            
            vlrEncalhe = vlrEncalhe.add(produtoDTO.getVlrPrecoComDesconto()
                    .multiply( BigDecimal.valueOf(produtoDTO.getQuantidadeDevolvida().longValue()) ));
            
            formatarLinhaExtraSupRedistCE(cota, produtoDTO, produtosSupRedist);
            
        }
        
        BigDecimal vlrReparteLiquido = vlrReparte.subtract(vlrDesconto);
        
        BigDecimal totalLiquido = vlrReparteLiquido.subtract(vlrEncalhe);
        
        cota.setVlrReparte(CurrencyUtil.formatarValor(vlrReparte));
        cota.setVlrComDesconto(CurrencyUtil.formatarValorQuatroCasas(vlrDesconto));
        cota.setVlrReparteLiquido(CurrencyUtil.formatarValorQuatroCasas(vlrReparteLiquido));
        cota.setVlrEncalhe(CurrencyUtil.formatarValorQuatroCasas(vlrEncalhe));
        cota.setVlrTotalLiquido(CurrencyUtil.formatarValorQuatroCasas(totalLiquido));
         */
    }
}
