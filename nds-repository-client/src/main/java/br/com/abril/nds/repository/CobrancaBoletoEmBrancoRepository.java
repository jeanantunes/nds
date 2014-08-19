package br.com.abril.nds.repository;

import br.com.abril.nds.model.financeiro.CobrancaBoletoEmBranco;


public interface CobrancaBoletoEmBrancoRepository extends Repository<CobrancaBoletoEmBranco, Long> {
    
    CobrancaBoletoEmBranco obterPorNossoNumero(String nossoNumero);
}
