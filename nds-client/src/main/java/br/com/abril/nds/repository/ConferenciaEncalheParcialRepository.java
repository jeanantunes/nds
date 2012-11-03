package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;

public interface ConferenciaEncalheParcialRepository extends Repository<ConferenciaEncalheParcial, Long>  {

	public BigInteger obterQtdTotalEncalheParcial(StatusAprovacao statusAprovacao, Date dataMovimento, String codigoProduto, Long numeroEdicao);
	
	
	public List<ConferenciaEncalheParcial> obterListaConferenciaEncalhe(
			Boolean diferencaApurada,
			Boolean nfParcialGerada,
			StatusAprovacao statusAprovacao, 
			Date dataMovimento, 
			Long idProdutoEdicao,
			String codigoProduto, 
			Long numeroEdicao);
	
	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(
			Boolean diferencaApurada,
			Boolean nfParcialGerada,
			StatusAprovacao statusAprovacao, 
			Long idProdutoEdicao,
			String codigoProduto,
			Long numeroEdicao,
			Date dataMovimento);
	
	public ConferenciaEncalheParcial obterConferenciaEncalheParcialPor(Long idProduto, Date dataMovimento);
	
}
