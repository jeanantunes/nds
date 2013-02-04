package br.com.abril.nds.service;

import java.util.Date;

import org.springframework.stereotype.Service;

/**
 * Classe de interface de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.HistoricoSituacaoCota}
 * @author InfoA2
 */
@Service
public interface HistoricoSituacaoCotaService {

	public Date buscarUltimaSuspensaoCotasDia(Date dataOperacao);
	
	public Date buscarDataUltimaSuspensaoCotas();
	
}
