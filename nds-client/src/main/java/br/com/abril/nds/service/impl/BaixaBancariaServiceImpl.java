package br.com.abril.nds.service.impl;

import java.util.Date;

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
	
	private ControleBaixaBancariaRepository baixaBancariaRepository;
	@Override
	@Transactional
	public boolean verificarBaixaBancariaNaData(Date data) {
		
		ControleBaixaBancaria controle =  baixaBancariaRepository.obterPorData(data);
		if ( controle != null 
				&& controle.getStatus() != null 
				&& controle.getStatus().equals(StatusControle.CONCLUIDO_SUCESSO)) {
			return true;
		}
		return false;
	}

}
