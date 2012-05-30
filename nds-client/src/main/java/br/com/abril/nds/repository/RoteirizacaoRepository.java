package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.Roteirizacao;

public interface RoteirizacaoRepository extends Repository<Roteirizacao, Long> {
	
	Roteirizacao buscarRoteirizacaoDeCota(Integer numeroCota);
}
