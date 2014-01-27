package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaQueNaoRecebeClassificacaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeClassificacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.ClassificacaoNaoRecebida;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.repository.ClassificacaoNaoRecebidaRepository;
import br.com.abril.nds.service.ClassificacaoNaoRecebidaService;
import br.com.abril.nds.service.CotaService;

@Service
public class ClassificacaoNaoRecebidaServiceImpl implements	ClassificacaoNaoRecebidaService {

	@Autowired
	private ClassificacaoNaoRecebidaRepository classificacaoNaoRecebidarRepository; 
	
	@Autowired
	private CotaService cotaService;
	
	@Transactional(readOnly = true)
	@Override
	public List<CotaQueRecebeClassificacaoDTO> obterCotasQueRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro) {
		return classificacaoNaoRecebidarRepository.obterCotasQueRecebemClassificacao(filtro);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaQueNaoRecebeClassificacaoDTO> obterCotasQueNaoRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro) {
		return classificacaoNaoRecebidarRepository.obterCotasQueNaoRecebemClassificacao(filtro);
	}

	@Transactional
	@Override
	public void excluirClassificacaoNaoRecebida(Long id) {
		this.classificacaoNaoRecebidarRepository.removerPorId(id);
	}

	@Transactional
	@Override
	public void inserirListaClassificacaoNaoRecebida(List<ClassificacaoNaoRecebida> listaClassificacaoNaoRecebida) {
		for (ClassificacaoNaoRecebida classificacaoNaoRecebida : listaClassificacaoNaoRecebida) {
			classificacaoNaoRecebidarRepository.adicionar(classificacaoNaoRecebida);
		}
	}
	
	@Transactional
	@Override
	public List<ClassificacaoNaoRecebidaDTO> obterClassificacoesNaoRecebidasPelaCota(FiltroClassificacaoNaoRecebidaDTO filtro) {
		
		Cota cota = null;
		
		if (filtro.getCotaDto().getNumeroCota() != null && !filtro.getCotaDto().getNumeroCota().equals(0)) {
			cota = cotaService.obterPorNumeroDaCota(filtro.getCotaDto().getNumeroCota());
		}else {
			cota = cotaService.obterCotasPorNomePessoa(filtro.getCotaDto().getNomePessoa()).get(0);
		}
		
		switch (cota.getSituacaoCadastro()) {
		case INATIVO:
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota com status INATIVO.");
			
		case PENDENTE:
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota com status PENDENTE.");
			
		default:
			return classificacaoNaoRecebidarRepository.obterClassificacoesNaoRecebidasPelaCota(filtro);
		}
	}
	

	@Transactional
	public List<ClassificacaoNaoRecebidaDTO> obterClassificacoesNaoRecebidasPelaCota(Cota cota) {
		
		switch (cota.getSituacaoCadastro()) {
		case INATIVO:
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota com status INATIVO.");
			
		default:
			FiltroClassificacaoNaoRecebidaDTO filtro = new FiltroClassificacaoNaoRecebidaDTO();
			CotaDTO cotadto = new CotaDTO();
			cotadto.setNumeroCota(cota.getNumeroCota());
			filtro.setCotaDto(cotadto);
			return classificacaoNaoRecebidarRepository.obterClassificacoesNaoRecebidasPelaCota(filtro);
		}
	}

	@Transactional
	@Override
	public List<TipoClassificacaoProduto> obterClassificacoesRecebidasPelaCota(FiltroClassificacaoNaoRecebidaDTO filtro) {
		return classificacaoNaoRecebidarRepository.obterClassificacoesRecebidasPelaCota(filtro);
	}

}
