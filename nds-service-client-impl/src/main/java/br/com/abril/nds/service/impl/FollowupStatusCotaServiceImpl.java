package br.com.abril.nds.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaFollowupStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupStatusCotaDTO;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FollowupStatusCotaRepository;
import br.com.abril.nds.service.FollowupStatusCotaService;
import br.com.abril.nds.util.DateUtil;

@Service
public class FollowupStatusCotaServiceImpl implements FollowupStatusCotaService {

	@Autowired
	private FollowupStatusCotaRepository followupStatusCotaRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Override
	@Transactional
	public List<ConsultaFollowupStatusCotaDTO> obterStatusCota(
			FiltroFollowupStatusCotaDTO filtro) {
		
		Date dataOperacao = this.distribuidorRepository.obterDataOperacaoDistribuidor();
		
		Date proximaData = DateUtil.adicionarDias(dataOperacao, 1);
		
		List<Date> datas = Arrays.asList(dataOperacao, proximaData);
		 
		return this.followupStatusCotaRepository.obterConsignadosParaChamadao(filtro, datas);
	}

}
