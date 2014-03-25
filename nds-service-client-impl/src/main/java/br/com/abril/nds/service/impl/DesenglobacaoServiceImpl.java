package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DesenglobacaoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.distribuicao.Desenglobacao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DesenglobacaoRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.service.DesenglobacaoService;
import br.com.abril.nds.util.DateUtil;

@Service
public class DesenglobacaoServiceImpl implements DesenglobacaoService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DesenglobacaoServiceImpl.class);

    @Autowired
    private DesenglobacaoRepository desenglobacaoRepository;

    @Autowired
    private PdvRepository pdvRepository;

    @Autowired
    private CotaRepository cotaRepository;

    @Override
    @Transactional
    public List<DesenglobacaoDTO> obterDesenglobacaoPorCota(Integer numeroCota) {
		List<DesenglobacaoDTO> dtoList = new ArrayList<DesenglobacaoDTO>();
		List<Desenglobacao> desenglobaList = desenglobacaoRepository.obterDesenglobacaoPorCota(numeroCota);
	
		for (Desenglobacao desenglobacao : desenglobaList) {
		    DesenglobacaoDTO dto = copyToDTO(desenglobacao);
		    dto.setIdCotaDesenglobada(desenglobacao.getCotaDesenglobada().getId());
		    dto.setIdCotaEnglobada(desenglobacao.getCotaEnglobada().getId());
		    dtoList.add(dto);
		}
	return dtoList;
    }

    private DesenglobacaoDTO copyToDTO(Desenglobacao desenglobacao) {
		
    	DesenglobacaoDTO dto = new DesenglobacaoDTO();
		dto.setNumeroCotaEnglobada(desenglobacao.getCotaEnglobada().getNumeroCota());
		dto.setIdCotaEnglobada(desenglobacao.getCotaEnglobada().getId());
		dto.setNomeCotaEnglobada(desenglobacao.getCotaEnglobada().getPessoa().getNome());
		dto.setNomePDV(desenglobacao.getTipoPDV().getDescricao());
		dto.setPorcentagemCota(desenglobacao.getPorcentagemCota());
		dto.setNomeUsuario(desenglobacao.getResponsavel().getNome());
		dto.setDataAlteracao(desenglobacao.getDataAlteracao());
		dto.setHora(DateUtil.formatarHoraMinuto(desenglobacao.getDataAlteracao()));
		dto.setNumeroCotaDesenglobada(desenglobacao.getCotaDesenglobada().getNumeroCota());
		dto.setIdCotaDesenglobada(desenglobacao.getCotaDesenglobada().getId());
		dto.setNomeCotaDesenglobada(desenglobacao.getCotaDesenglobada().getPessoa().getNome());
		dto.setIdDesenglobacao(desenglobacao.getId());
		
	return dto;
    }

    @Override
    @Transactional
    public List<DesenglobacaoDTO> obterDesenglobacaoPorCotaDesenglobada(Integer numeroCota) {
		List<DesenglobacaoDTO> dtoList = new ArrayList<DesenglobacaoDTO>();
		List<Desenglobacao> desenglobaList = desenglobacaoRepository.obterDesenglobacaoPorCotaDesenglobada(numeroCota);

		for (Desenglobacao desenglobacao : desenglobaList) {
		    DesenglobacaoDTO dto = copyToDTO(desenglobacao);
		    dto.setIdCotaDesenglobada(desenglobacao.getCotaDesenglobada().getId());
		    dto.setIdCotaEnglobada(desenglobacao.getCotaEnglobada().getId());
		    
		    dtoList.add(dto);
		}
	return dtoList;
    }

    @Override
    @Transactional
    public boolean inserirDesenglobacao(List<DesenglobacaoDTO> desenglobaDTO, Usuario usuario) {

		List<Desenglobacao> desenglobada = new ArrayList<Desenglobacao>();
	
		boolean isOk = verificaPorcentagemCota(desenglobaDTO);
		if (!isOk) {
		    return false;
		}
	
		try {
		    if (desenglobaDTO.size() > 0) {
			Cota cotaDesenglobada = cotaRepository.obterPorNumeroDaCota(desenglobaDTO.get(0).getNumeroCotaDesenglobada());
			for (DesenglobacaoDTO origem : desenglobaDTO) {
			    Desenglobacao destino = new Desenglobacao();
			    BeanUtils.copyProperties(origem, destino);
			    Cota cotaEnglobada = cotaRepository.obterPorNumeroDaCota(origem.getNumeroCotaEnglobada());
			    destino.setCotaEnglobada(cotaEnglobada);
			    destino.setCotaDesenglobada(cotaDesenglobada);
			    trataEnglobacao(destino, usuario);
			    desenglobada.add(destino);
			}
		    }
		    desenglobacaoRepository.inserirCotasDesenglobadas(desenglobada);
		} catch (Exception e) {
		    LOGGER.error(e.getMessage(), e);
		}
	return true;
    }

    private void trataEnglobacao(Desenglobacao destino, Usuario usuario) {
		
    	PDV pdv = this.pdvRepository.obterPDVPrincipal(destino.getCotaEnglobada().getId());
		
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

    private boolean verificaPorcentagemCota(List<DesenglobacaoDTO> desenglobaDTO) {
	
    	float somatoriaPorcentagemEnglobada = 0;
	
    	for (DesenglobacaoDTO desenglobaVO : desenglobaDTO) {
    		somatoriaPorcentagemEnglobada += desenglobaVO.getPorcentagemCota();
    	}

		if ((somatoriaPorcentagemEnglobada) > 100) {
		    return false;
		}

	return true;
    }

    @Transactional
    @Override
    public boolean alterarDesenglobacao(List<DesenglobacaoDTO> desenglobaDTO, Usuario usuarioLogado) {

		boolean isOk = verificaPorcentagemCota(desenglobaDTO);
			if (!isOk) {
			    return false;
			}
	
		boolean res = desenglobacaoRepository.removerPorCotaDesenglobada(Long.valueOf(desenglobaDTO.get(0).getNumeroCotaDesenglobada()));
			if(res){
			    res = inserirDesenglobacao(desenglobaDTO, usuarioLogado);
			}
	return res;
    }

    @Override
    @Transactional
    public void excluirDesenglobacao(Long id) {
    	this.desenglobacaoRepository.removerPorId(id);
    }
}
