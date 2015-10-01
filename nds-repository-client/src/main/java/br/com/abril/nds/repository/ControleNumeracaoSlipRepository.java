package br.com.abril.nds.repository;

import java.math.BigInteger;

import br.com.abril.nds.model.TipoSlip;
import br.com.abril.nds.model.movimentacao.ControleNumeracaoSlip;

public interface ControleNumeracaoSlipRepository extends Repository<ControleNumeracaoSlip, Long>{

	public ControleNumeracaoSlip obterControleNumeracaoSlip(TipoSlip tipoSlip);
	public void alterarPorId(Long id, Long proximo);

}
