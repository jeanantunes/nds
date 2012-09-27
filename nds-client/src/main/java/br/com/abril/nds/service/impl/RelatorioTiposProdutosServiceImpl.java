package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RelatorioTiposProdutosDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioTiposProdutos;
import br.com.abril.nds.repository.RelatorioTiposProdutosRepository;
import br.com.abril.nds.service.RelatorioTiposProdutosService;

@Service
public class RelatorioTiposProdutosServiceImpl implements RelatorioTiposProdutosService {

	@Autowired
	private RelatorioTiposProdutosRepository repository;

	@Override
	@Transactional
	public List<RelatorioTiposProdutosDTO> gerarRelatorio(FiltroRelatorioTiposProdutos filtro) {

		return repository.gerarRelatorio(filtro);
	}
}
