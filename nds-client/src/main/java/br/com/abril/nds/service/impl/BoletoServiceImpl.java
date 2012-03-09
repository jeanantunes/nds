package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.repository.impl.BoletoRepositoryImpl;
import br.com.abril.nds.service.BoletoService;

@Service
public class BoletoServiceImpl implements BoletoService {

	@Autowired
	private BoletoRepository boletoRepository=new BoletoRepositoryImpl();

	@Transactional
	public List<Boleto> obterBoletosPorCota(Integer numeroCota,Date vencimentoDe, Date vencimentoAte, StatusCobranca status) {
		return this.boletoRepository.obterBoletosPorCota(numeroCota,vencimentoDe,vencimentoAte, status);
	}

}
