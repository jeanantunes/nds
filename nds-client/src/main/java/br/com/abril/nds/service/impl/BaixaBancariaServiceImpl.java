package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.abril.nds.service.BaixaBancariaSerivice;

@Service
public class BaixaBancariaServiceImpl implements BaixaBancariaSerivice{
	

	@Override
	public boolean verificarBaixaBancariaNaData(Date data) {
		
		return true;
	}

}
