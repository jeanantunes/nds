package br.com.abril.nds.server.service;

import java.util.List;

import br.com.abril.nds.server.model.Distribuidor;

public interface PainelOperacionalService {

	List<Distribuidor> buscarIndicadoresPorDistribuidor();
}