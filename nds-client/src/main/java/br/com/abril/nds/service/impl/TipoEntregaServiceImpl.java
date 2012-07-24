package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.repository.TipoEntregaRepository;
import br.com.abril.nds.service.TipoEntregaService;

/**
 * Serviço para TipoEntrega.
 * 
 * @author Discover Technology.
 */
@Service
public class TipoEntregaServiceImpl implements TipoEntregaService {

	@Autowired
	private TipoEntregaRepository tipoEntregaRepository;
	
	/**
	 * @see br.com.abril.nds.service.TipoEntregaService#obterTodos()
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TipoEntrega> obterTodos() {
		return tipoEntregaRepository.buscarTodos();
	}

	/**
	 * @see br.com.abril.nds.service.TipoEntregaService#obterTipoEntrega(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=false)
	public TipoEntrega obterTipoEntrega(Long id) {

		return this.tipoEntregaRepository.buscarPorId(id);
	}

}
