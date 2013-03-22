package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.DesenglobaVO;
import br.com.abril.nds.dto.DesenglobacaoDTO;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.distribuicao.Desenglobacao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DesenglobacaoRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.service.DesenglobacaoService;

@Service
public class DesenglobacaoServiceImpl implements DesenglobacaoService {

	@Autowired
	private DesenglobacaoRepository repository;
	
	@Autowired
	private PdvRepository pdvRepository;
	
	@Override
	public List<DesenglobacaoDTO> obterDesenglobacaoPorCota(Long cotaId) {
		List<DesenglobacaoDTO> dtoList = new ArrayList<DesenglobacaoDTO>();
		List<Desenglobacao> desenglobaList = repository.obterDesenglobacaoPorCota(cotaId);
		
		for (Desenglobacao desenglobacao : desenglobaList) {
			DesenglobacaoDTO dto = new DesenglobacaoDTO();
			dto.setNumeroCota(desenglobacao.getEnglobadaNumeroCota());
			dto.setNomeCota(desenglobacao.getEnglobadaNomePessoa());
			dto.setNomePDV(desenglobacao.getTipoPDV().getDescricao());
			dto.setPorcentagemCota(desenglobacao.getEnglobadaPorcentagemCota());
			dto.setNomeUsuario(desenglobacao.getResponsavel().getNome());
			dto.setDataAlteracao(desenglobacao.getDataAlteracao());
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	@Transactional
	public boolean inserirDesenglobacao(List<DesenglobaVO> desenglobaDTO, Usuario usuario) {
		
		List<Desenglobacao> desenglobada = new ArrayList<Desenglobacao>();
		
		boolean isOk = verificaPorcentagemCota(desenglobaDTO);
		if (!isOk) {
			return false;
		}
		
		try {
			
			for (DesenglobaVO origem : desenglobaDTO) {
				Desenglobacao destino = new Desenglobacao();
				BeanUtils.copyProperties(destino, origem);
				trataEnglobacao(destino, usuario);
				desenglobada.add(destino);
			}
			
			repository.inserirCotasDesenglobadas(desenglobada);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	private void trataEnglobacao(Desenglobacao destino, Usuario usuario) {
		PDV pdv = this.pdvRepository.obterPDVPrincipal(destino.getEnglobadaNumeroCota());
		if (pdv.getSegmentacao().getTipoPontoPDV() == null) {
			TipoPontoPDV pontoPDV = new TipoPontoPDV();
			pontoPDV.setId(4L);
			pontoPDV.setCodigo(4L);
			destino.setTipoPDV(pontoPDV);
		} else {
			destino.setTipoPDV(pdv.getSegmentacao().getTipoPontoPDV());
		}
		
		destino.setResponsavel(usuario);
		destino.setDataAlteracao(new Date());
	}
	
	private boolean verificaPorcentagemCota(List<DesenglobaVO> desenglobaDTO) {
		float somatoriaPorcentagemEnglobada = 0;
		float porcentagemCota = 0;
		
		for (DesenglobaVO desenglobaVO : desenglobaDTO) {
			
			List<Desenglobacao> existeCotaEnglobada = repository.obterDesenglobacaoPorCota(desenglobaVO.getDesenglobaNumeroCota().longValue());
			if (!existeCotaEnglobada.isEmpty()) {
				porcentagemCota = repository.verificaPorcentagemCota(desenglobaVO.getDesenglobaNumeroCota().longValue());
			}
			
			somatoriaPorcentagemEnglobada += desenglobaVO.getEnglobadaPorcentagemCota();
		}
		
		if ((porcentagemCota + somatoriaPorcentagemEnglobada) > 100) {
			return false;
		}
		
		return true;
	}
}
