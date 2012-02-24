package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.MatrizLancamentoService;

@Service
public class MatrizLancamentoServiceImpl implements MatrizLancamentoService {
	
	private static final String FORMATO_DATA_LANCAMENTO = "dd/MM/yyyy";
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Override
	@Transactional
	public List<LancamentoDTO> buscarLancamentosBalanceamento(Date inicio,
			Date fim, Long... idsFornecedores) {
		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosBalanceamentoMartriz(inicio, fim,
						idsFornecedores);
		List<LancamentoDTO> dtos = new ArrayList<LancamentoDTO>(
				lancamentos.size());
		
		return dtos;
	}

}
