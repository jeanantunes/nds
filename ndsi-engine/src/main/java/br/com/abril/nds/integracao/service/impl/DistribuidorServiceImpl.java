package br.com.abril.nds.integracao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DistribuidorDTO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.repository.DistribuidorRepository;

@Service
public class DistribuidorServiceImpl implements DistribuidorService {

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Override
	public boolean isDistribuidor(Integer codigo) {
		if (obter().getCodigo().equals(codigo))
			return true;
		return false;
	}

	@Override
	@Transactional
	public Distribuidor obter() {
		return distribuidorRepository.obter();
	}

	@Override
	@Transactional
	public void alterar(Distribuidor distribuidor) {
		distribuidorRepository.alterar(distribuidor);
	}

	@Override
	@Transactional
	public DistribuidorDTO obterDadosEmissao() {
		
		Distribuidor distribuidor = obter();
		
		DistribuidorDTO dto = new DistribuidorDTO();
		
		dto.setRazaoSocial(distribuidor.getJuridica().getRazaoSocial().toUpperCase());
		Endereco endereco = distribuidor.getEnderecos().get(0).getEndereco(); 
		dto.setEndereco(endereco.getLogradouro().toUpperCase()  + " " + endereco.getNumero());
		dto.setCnpj(distribuidor.getJuridica().getCnpj());
		dto.setCidade(endereco.getCidade().toUpperCase());
		dto.setUf(endereco.getUf().toUpperCase());
		dto.setCep(endereco.getCep());
		dto.setInscricaoEstatual(distribuidor.getJuridica().getInscricaoEstadual());
		
		return dto;
	}
	
}
