package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoDescontoDistribuidor;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.TipoDescontoDistribuidorRepository;
import br.com.abril.nds.service.TipoDescontoDistribuidorService;

@Service
public class TipoDescontoDistribuidorServiceImpl implements
		TipoDescontoDistribuidorService {
	
	@Autowired
	private TipoDescontoDistribuidorRepository tipoDescontoDistribuidorRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Override
	@Transactional
	public void incluirDescontoDistribuidor(TipoDescontoDistribuidor tipoDescontoDistribuidor) {
		 this.tipoDescontoDistribuidorRepository.adicionar(tipoDescontoDistribuidor);
	}

	@Override
	@Transactional
	public void atualizarDistribuidores(BigDecimal desconto) {
		List<Distribuidor> listaDeDistribuidores = this.distribuidorRepository.buscarTodos();
		for(Distribuidor dist: listaDeDistribuidores){
			dist.setFatorDesconto(desconto);
			this.distribuidorRepository.alterar(dist);
		}
	}

	@Override
	@Transactional
	public int obterUltimoSequencial() {		 
		return this.tipoDescontoDistribuidorRepository.obterSequencial();
	}

	@Override
	public void excluirDesconto(
			TipoDescontoDistribuidor tipoDescontoDistribuidor) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Distribuidor> obterDistribuidores() {
		// TODO Auto-generated method stub
		return null;
	}

}
