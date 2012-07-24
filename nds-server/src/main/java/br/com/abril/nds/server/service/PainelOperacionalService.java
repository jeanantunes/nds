package br.com.abril.nds.server.service;

import java.util.List;

import br.com.abril.nds.server.model.OperacaoDistribuidor;

public interface PainelOperacionalService {

	List<OperacaoDistribuidor> buscarIndicadoresPorDistribuidor();
}