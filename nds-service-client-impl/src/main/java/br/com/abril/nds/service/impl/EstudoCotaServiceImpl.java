package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.TipoEstudoCota;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.service.EstudoCotaService;

/**
 * Classe de implementação de serviços referentes a entidade {@link br.com.abril.nds.model.planejamento.EstudoCota}.
 * 
 * @author Discover Technology
 * 
 */
@Service
public class EstudoCotaServiceImpl implements EstudoCotaService {


	@Autowired
	private EstudoCotaRepository estudoCotaRepository;
	
	@Transactional(readOnly = true)
	public EstudoCota obterEstudoCota(Integer numeroCota, Date dataReferencia) {
		
		return this.estudoCotaRepository.obterEstudoCota(numeroCota, dataReferencia);
	}

	@Transactional(readOnly = true)
	public EstudoCota obterEstudoCotaDeLancamentoComEstudoFechado(Date dataLancamentoDistribuidor, 
																  Long idProdutoEdicao,
																  Integer numeroCota) {
		
		return this.estudoCotaRepository.obterEstudoCotaDeLancamentoComEstudoFechado(dataLancamentoDistribuidor, idProdutoEdicao, numeroCota);
	}
	
	@Transactional
	public EstudoCota criarEstudoCotaJuramentado(ProdutoEdicao produtoEdicao, Estudo estudo, BigInteger reparte,Cota cota){
		
		EstudoCota estudoCota = new EstudoCota();
		estudoCota.setCota(cota);
		estudoCota.setEstudo(estudo);
		estudoCota.setQtdeEfetiva(reparte);
		estudoCota.setQtdePrevista(reparte);
		estudoCota.setReparte(reparte);
		estudoCota.setTipoEstudo(TipoEstudoCota.JURAMENTADO);
		
		return estudoCotaRepository.merge(estudoCota);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EstudoCota> obterEstudosCota(Long idEstudo) {
		return this.estudoCotaRepository.obterEstudosCota(idEstudo);
	}

}
