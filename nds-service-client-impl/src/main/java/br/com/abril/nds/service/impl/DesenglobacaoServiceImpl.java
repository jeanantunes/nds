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

    @Autowired
    private DesenglobacaoRepository desenglobacaoRepository;

    @Autowired
    private PdvRepository pdvRepository;

    @Autowired
    private CotaRepository cotaRepository;

    @Override
    public List<DesenglobacaoDTO> obterDesenglobacaoPorCota(Long cotaId) {
	List<DesenglobacaoDTO> dtoList = new ArrayList<DesenglobacaoDTO>();
	List<Desenglobacao> desenglobaList = desenglobacaoRepository.obterDesenglobacaoPorCota(cotaId);

	for (Desenglobacao desenglobacao : desenglobaList) {
	    DesenglobacaoDTO dto = copyToDTO(desenglobacao, false);

	    dtoList.add(dto);
	}
	return dtoList;
    }

    private DesenglobacaoDTO copyToDTO(Desenglobacao desenglobacao, boolean inverterNumeroCota) {
	DesenglobacaoDTO dto = new DesenglobacaoDTO();
	if (inverterNumeroCota) {
	    Cota cotaEnglobada = cotaRepository.buscarCotaPorID(desenglobacao.getEnglobadaNumeroCota());
	    if (cotaEnglobada != null) {
		dto.setNumeroCota(new Long(cotaEnglobada.getNumeroCota()));
	    }
	} else {
	    dto.setNumeroCota(desenglobacao.getEnglobadaNumeroCota());
	}
	dto.setNomeCota(desenglobacao.getEnglobadaNomePessoa());
	dto.setNomePDV(desenglobacao.getTipoPDV().getDescricao());
	dto.setPorcentagemCota(desenglobacao.getEnglobadaPorcentagemCota());
	dto.setNomeUsuario(desenglobacao.getResponsavel().getNome());
	dto.setDataAlteracao(desenglobacao.getDataAlteracao());
	dto.setHora(DateUtil.formatarHoraMinuto(desenglobacao.getDataAlteracao()));
	if (inverterNumeroCota) {
	    Cota cotaDesenglobada = cotaRepository.buscarCotaPorID(desenglobacao.getDesenglobaNumeroCota());
	    if (cotaDesenglobada != null) {
		dto.setNumeroCotaDesenglobada(new Long(cotaDesenglobada.getNumeroCota()));
	    }
	} else {
	    dto.setNumeroCotaDesenglobada(desenglobacao.getDesenglobaNumeroCota());
	}
	dto.setIdDesenglobacao(desenglobacao.getId());
	return dto;
    }

    @Override
    public List<DesenglobacaoDTO> obterDesenglobacaoPorCotaDesenglobada(Long cotaNumero) {
	List<DesenglobacaoDTO> dtoList = new ArrayList<DesenglobacaoDTO>();
	List<Desenglobacao> desenglobaList = desenglobacaoRepository.obterDesenglobacaoPorCotaDesenglobada(cotaNumero);

	for (Desenglobacao desenglobacao : desenglobaList) {
	    DesenglobacaoDTO dto = copyToDTO(desenglobacao, true);
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

	    desenglobacaoRepository.inserirCotasDesenglobadas(desenglobada);

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

	/*for (DesenglobaVO desenglobaVO : desenglobaDTO) {

			List<Desenglobacao> existeCotaEnglobada = desenglobacaoRepository.obterDesenglobacaoPorCota(desenglobaVO.getDesenglobaNumeroCota().longValue());
			if (!existeCotaEnglobada.isEmpty()) {
				porcentagemCota = desenglobacaoRepository.verificaPorcentagemCota(desenglobaVO.getDesenglobaNumeroCota().longValue());
			}

			somatoriaPorcentagemEnglobada += desenglobaVO.getEnglobadaPorcentagemCota();
		}*/
	for (DesenglobaVO desenglobaVO : desenglobaDTO) {
	    somatoriaPorcentagemEnglobada += desenglobaVO.getEnglobadaPorcentagemCota();
	}
	//		if ((porcentagemCota + somatoriaPorcentagemEnglobada) > 100) {
	if ((somatoriaPorcentagemEnglobada) > 100) {
	    return false;
	}

	return true;
    }

    @Transactional
    @Override
    public boolean alterarDesenglobacao(List<DesenglobaVO> desenglobaDTO, Usuario usuarioLogado) {

	boolean isOk = verificaPorcentagemCota(desenglobaDTO);
	if (!isOk) {
	    return false;
	}

	boolean res = desenglobacaoRepository.removerPorCotaDesenglobada(new Long(desenglobaDTO.get(0).getDesenglobaNumeroCota()));
	if(res){
	    res = inserirDesenglobacao(desenglobaDTO, usuarioLogado);
	}
	return res;
    }

    @Override
    public void excluirDesenglobacao(Long id) {
	this.desenglobacaoRepository.removerPorId(id);

    }
}
