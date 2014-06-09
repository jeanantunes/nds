package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ComboTipoFornecedorDTO;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.repository.TipoFornecedorRepository;
import br.com.abril.nds.service.TipoFornecedorService;

@Service
public class TipoFornecedorServiceImpl implements TipoFornecedorService {

	@Autowired
	private TipoFornecedorRepository tipoFornecedorRepository;
	
	@Override
	@Transactional
	public List<ComboTipoFornecedorDTO> obterComboTipoFornecedor() {

		return this.tipoFornecedorRepository.obterComboTipoFornecedor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public TipoFornecedor obterTipoFornecedorPorId(Long id) {

		return this.tipoFornecedorRepository.buscarPorId(id);
	}
}
