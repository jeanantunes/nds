package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaNaoRecebeSegmentoDTO;
import br.com.abril.nds.dto.SegmentoNaoRecebeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.SegmentoNaoRecebido;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.SegmentoNaoRecebidoRepository;
import br.com.abril.nds.service.SegmentoNaoRecebidoService;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class SegmentoNaoRecebidoServiceImpl implements SegmentoNaoRecebidoService {

	@Autowired
	private SegmentoNaoRecebidoRepository segmentoNaoRecebidoRepo;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Transactional(readOnly = true)
	@Override
	public List<CotaNaoRecebeSegmentoDTO> obterCotasNaoRecebemSegmento(FiltroSegmentoNaoRecebidoDTO filtro) {
		return segmentoNaoRecebidoRepo.obterCotasNaoRecebemSegmento(filtro);
	}

	@Transactional
	@Override
	public void excluirSegmentoNaoRecebido(Long segmentoNaoRecebidoId) {
		segmentoNaoRecebidoRepo.removerPorId(segmentoNaoRecebidoId);	
	}

	@Transactional
	@Override
	public void inserirCotasSegmentoNaoRecebido(List<SegmentoNaoRecebido> segmentosNaoRecebido) {
		for (SegmentoNaoRecebido segmentoNaoRecebido : segmentosNaoRecebido) {
			segmentoNaoRecebidoRepo.adicionar(segmentoNaoRecebido);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaDTO> obterCotasNaoEstaoNoSegmento(FiltroSegmentoNaoRecebidoDTO filtro) {
		if (segmentoNaoRecebidoRepo.isCotaJaInserida(filtro.getTipoSegmentoProdutoId(), filtro.getNumeroCota())) {
			Cota cota = cotaRepository.obterPorNumeroDaCota(filtro.getNumeroCota());
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Cota Duplicada! Status: " + cota.getSituacaoCadastro().toString()));
		}
		return  segmentoNaoRecebidoRepo.obterCotasNaoEstaoNoSegmento(filtro);
	}

	@Transactional(readOnly = true)
	@Override
	public List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebidosCadastradosNaCota(FiltroSegmentoNaoRecebidoDTO filtro) {
		return segmentoNaoRecebidoRepo.obterSegmentosNaoRecebidosCadastradosNaCota(filtro);
	}

	@Transactional(readOnly = true)
	@Override
	public List<TipoSegmentoProduto> obterSegmentosElegiveisParaInclusaoNaCota(FiltroSegmentoNaoRecebidoDTO filtro) {
		return segmentoNaoRecebidoRepo.obterSegmentosElegiveisParaInclusaoNaCota(filtro);
	}

	@Override
	@Transactional
	public List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebidosCadastradosNaCota(Cota cota) {
		return segmentoNaoRecebidoRepo.obterSegmentosNaoRecebidosCadastradosNaCota(cota);
	}
	
	@Override
	@Transactional
	public List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebidosCadastradosCotaBase(Long idCota) {
		return segmentoNaoRecebidoRepo.obterSegmentosNaoRecebidosCadastradosNaCotaBase(idCota);
	}
	 
}
