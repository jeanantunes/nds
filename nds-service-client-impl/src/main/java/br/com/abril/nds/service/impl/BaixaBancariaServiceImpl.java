package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.service.BaixaBancariaSerivice;

@Service
public class BaixaBancariaServiceImpl implements BaixaBancariaSerivice{

	@Autowired
	private ControleBaixaBancariaRepository controleBaixaBancariaRepository;
	
	@Override
	@Transactional(readOnly = true)
	public boolean verificarBaixaBancariaNaData(Date data) {
		
		List<ControleBaixaBancaria> listaControleBaixaBancaria =
			this.controleBaixaBancariaRepository.obterListaControleBaixaBancaria(
				data, StatusControle.CONCLUIDO_SUCESSO);
		
		if (listaControleBaixaBancaria != null && !listaControleBaixaBancaria.isEmpty()) {
			
			return true;
		}
		
		return false;
	}

}
