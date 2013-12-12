package br.com.abril.nds.integracao.ems0129.outbound;

import java.math.BigDecimal;

public interface ModeloPickingDetalheInterface {
	
	public void setCodigoCota(Integer codigoCota) ;
	
	public void setSequenciaNotaEnvio(Integer sequenciaNotaEnvio);
	
	public void setCodigoProduto(String codigoProduto);
	
	public void setEdicao(Long edicao);
	
	public void setNomePublicacao(String nomePublicacao);
	
	public void setPrecoCusto(BigDecimal precoCusto);
	
	public void setPrecoVenda(BigDecimal precoVenda);
	
	public void setDesconto(BigDecimal desconto);
	
	public void setQuantidade(Long quantidade);
}
