package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaNaoRecebeSegmentoDTO;
import br.com.abril.nds.dto.SegmentoNaoRecebeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.SegmentoNaoRecebido;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.repository.SegmentoNaoRecebidoRepository;
import br.com.abril.nds.service.SegmentoNaoRecebidoService;

@Service
public class SegmentoNaoRecebidoServiceImpl implements SegmentoNaoRecebidoService {

	@Autowired
	private SegmentoNaoRecebidoRepository segmentoNaoRecebidoRepo;
	
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
		return  segmentoNaoRecebidoRepo.obterCotasNaoEstaoNoSegmento(filtro);
	}

	@Transactional(readOnly = true)
	@Override
	public List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebidosCadastradosNaCota(
			FiltroSegmentoNaoRecebidoDTO filtro) {
		return segmentoNaoRecebidoRepo.obterSegmentosNaoRecebidosCadastradosNaCota(filtro);
	}

	@Transactional(readOnly = true)
	@Override
	public List<TipoSegmentoProduto> obterSegmentosElegiveisParaInclusaoNaCota(
			FiltroSegmentoNaoRecebidoDTO filtro) {
		return segmentoNaoRecebidoRepo.obterSegmentosElegiveisParaInclusaoNaCota(filtro);
	}

	@Override
	@Transactional
	public List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebidosCadastradosNaCota(
			Cota cota) {
		return segmentoNaoRecebidoRepo.obterSegmentosNaoRecebidosCadastradosNaCota(cota);
	}

}
