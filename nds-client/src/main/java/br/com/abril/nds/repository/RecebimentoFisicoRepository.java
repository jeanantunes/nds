package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.estoque.RecebimentoFisico;

public interface RecebimentoFisicoRepository extends Repository<RecebimentoFisico, Long> {
	List<RecebimentoFisico> obterRecebimentoFisico();
	void adicionarRecebimentoFisico(RecebimentoFisico recebimentoFisico);
}