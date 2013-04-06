package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.repository.CotasQueNaoEntraramNoEstudoRepository;
import br.com.abril.nds.service.CotasQueNaoEntraramNoEstudoService;

@Service
public class CotasQueNaoEntraramNoEstudoServiceImpl implements CotasQueNaoEntraramNoEstudoService{
	
	@Autowired
	private CotasQueNaoEntraramNoEstudoRepository analiseNormalRepository;

	@Override
	public List<CotaQueNaoEntrouNoEstudoDTO> buscaCotasQuerNaoEntraramNoEstudo(
			CotasQueNaoEntraramNoEstudoQueryDTO queryDTO) {
		List<CotaQueNaoEntrouNoEstudoDTO> cotasQueNaoEntraramNoEstudo = analiseNormalRepository.buscaCotasQuerNaoEntraramNoEstudo(queryDTO);
		
		for (CotaQueNaoEntrouNoEstudoDTO cota : cotasQueNaoEntraramNoEstudo) {
				//TODO implementar lógica das cotas que não entraram no estudo.
			
		}
		return cotasQueNaoEntraramNoEstudo;
	}

}
