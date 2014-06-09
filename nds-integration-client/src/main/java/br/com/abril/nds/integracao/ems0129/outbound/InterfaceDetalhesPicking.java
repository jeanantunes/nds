package br.com.abril.nds.integracao.ems0129.outbound;

import java.math.BigDecimal;

public interface InterfaceDetalhesPicking {
	
	public void setCodigoCota(String codigoCota) ;
	
	public void setSequenciaNotaEnvio(String sequenciaNotaEnvio);
	
	public void setCodigoProduto(String codigoProduto);
	
	public void setEdicao(String edicao);
	
	public void setNomePublicacao(String nomePublicacao);
	
	public void setPrecoCusto(BigDecimal precoCusto);
	
	public void setPrecoVenda(BigDecimal precoVenda);
	
	public void setDesconto(BigDecimal desconto);
	
	public void setQuantidade(Long quantidade);
}
