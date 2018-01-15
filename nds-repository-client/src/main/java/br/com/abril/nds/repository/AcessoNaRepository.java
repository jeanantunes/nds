package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.AcessoNA;

public interface AcessoNaRepository extends Repository<AcessoNA, Long> {

    AcessoNA obterPorIdCota(Long idCota);
}
