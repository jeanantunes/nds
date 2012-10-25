package br.com.abril.nds.integracao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DistribuidorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoStatusGarantia;
import br.com.abril.nds.repository.DistribuidorRepository;

@Service
public class DistribuidorServiceImpl implements DistribuidorService {

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Override
	public boolean isDistribuidor(Integer codigo) {
		if (obter().getCodigoDistribuidorDinap().equals(codigo.toString()))
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
		Endereco endereco = distribuidor.getEnderecoDistribuidor().getEndereco(); 
		dto.setEndereco(endereco.getLogradouro().toUpperCase()  + " " + endereco.getNumero());
		dto.setCnpj(distribuidor.getJuridica().getCnpj());
		dto.setCidade(endereco.getCidade().toUpperCase());
		dto.setUf(endereco.getUf().toUpperCase());
		dto.setCep(endereco.getCep());
		dto.setInscricaoEstatual(distribuidor.getJuridica().getInscricaoEstadual());
		
		return dto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> obterNomeCNPJDistribuidor(){
		
		return this.distribuidorRepository.obterNomeCNPJDistribuidor();
	}

	@Override
	public List<ItemDTO<TipoGarantia, String>> getComboTiposGarantia() {
		
		List<ItemDTO<TipoGarantia,String>> comboTiposGarantia =  new ArrayList<ItemDTO<TipoGarantia,String>>();
		for (TipoGarantia itemTipoGarantia: TipoGarantia.values()){
			comboTiposGarantia.add(new ItemDTO<TipoGarantia,String>(itemTipoGarantia, itemTipoGarantia.getDescTipoGarantia()));
		}
		return comboTiposGarantia;
		
	}

	@Override
	public List<ItemDTO<TipoStatusGarantia, String>> getComboTiposStatusGarantia() {
		
		List<ItemDTO<TipoStatusGarantia,String>> comboTiposStatusGarantia =  new ArrayList<ItemDTO<TipoStatusGarantia,String>>();
		for (TipoStatusGarantia itemTipoStatusGarantia: TipoStatusGarantia.values()){
			comboTiposStatusGarantia.add(new ItemDTO<TipoStatusGarantia,String>(itemTipoStatusGarantia, itemTipoStatusGarantia.getDescTipoStatusGarantia()));
		}
		return comboTiposStatusGarantia;
	}


}