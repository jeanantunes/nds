package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoReparteDTO;
import br.com.abril.nds.repository.ResumoReparteFecharDiaRepository;
import br.com.abril.nds.service.ResumoReparteFecharDiaService;
import br.com.abril.nds.vo.PaginacaoVO;

@Service
public class ResumoReparteFecharDiaServiceImpl  implements ResumoReparteFecharDiaService {

	@Autowired
	private ResumoReparteFecharDiaRepository resumoFecharDiaRepository;

	@Override
	@Transactional
	public List<ReparteFecharDiaDTO> obterResumoReparte(Date dataOperacao, PaginacaoVO paginacao) {
		return this.resumoFecharDiaRepository.obterResumoReparte(dataOperacao, paginacao);
	}

    @Override
    @Transactional(readOnly = true)
    public Long contarLancamentosExpedidos(Date data) {
        Objects.requireNonNull(data, "Data para contagem dos lançamentos expedidos não deve ser nula!");
        return resumoFecharDiaRepository.contarLancamentosExpedidos(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public SumarizacaoReparteDTO obterSumarizacaoReparte(Date data) {
        Objects.requireNonNull(data, "Data para sumarização do reparte não deve ser nula!");
        return resumoFecharDiaRepository.obterSumarizacaoReparte(data);
    }

}
