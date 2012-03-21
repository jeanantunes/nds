package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;

public interface ConferenciaEncalheParcialRepository extends Repository<ConferenciaEncalheParcial, Long>  {

	public BigDecimal obterQtdTotalEncalheParcial(StatusAprovacao statusAprovacao, Date dataRecolhimentoDistribuidor, String codigoProduto, Long numeroEdicao);
	
}
