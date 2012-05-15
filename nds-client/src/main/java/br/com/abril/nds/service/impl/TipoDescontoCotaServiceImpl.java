package br.com.abril.nds.service.impl;

<<<<<<< HEAD
import java.math.BigDecimal;
import java.util.List;

=======
>>>>>>> 34176c9cc07d4b88a054b67ade2ed7dabdf236af
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.repository.DistribuidorRepository;
=======
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
>>>>>>> 34176c9cc07d4b88a054b67ade2ed7dabdf236af
import br.com.abril.nds.repository.TipoDescontoCotaRepository;
import br.com.abril.nds.service.TipoDescontoCotaService;

@Service
public class TipoDescontoCotaServiceImpl implements TipoDescontoCotaService {
	
	@Autowired
	private TipoDescontoCotaRepository  tipoDescontoCotaRepository;
	
<<<<<<< HEAD
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
=======
>>>>>>> 34176c9cc07d4b88a054b67ade2ed7dabdf236af
	/**
	 * Método responsável por incluir um desconto
	 * @param {@link br.com.abril.nds.model.cadastro.TipoDescontoCota} 
	 */
	@Transactional
	@Override
	public void incluirDescontoGeral(TipoDescontoCota tipoDescontoCota) {
		 this.tipoDescontoCotaRepository.adicionar(tipoDescontoCota);
<<<<<<< HEAD
	}
	
	@Transactional
	@Override
	public void atualizarDistribuidos(Long desconto) {
		List<Distribuidor> listaDeDistribuidores = this.distribuidorRepository.buscarTodos();
		for(Distribuidor dist: listaDeDistribuidores){
			BigDecimal descontoBig = new BigDecimal(desconto);
			dist.setFatorDesconto(descontoBig);
			this.distribuidorRepository.alterar(dist);
		}
		
=======

>>>>>>> 34176c9cc07d4b88a054b67ade2ed7dabdf236af
	}

}
