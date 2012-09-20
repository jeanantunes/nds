package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.SocioCota;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaSocio;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.SocioCotaRepository;
import br.com.abril.nds.repository.TelefoneRepository;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.SocioCotaService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class SocioCotaServiceImpl implements SocioCotaService {

	@Autowired
	private SocioCotaRepository socioCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private TelefoneRepository telefoneRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly=true)
	public SocioCota obterSocioPorId(Long idSocioCota) {

		return this.socioCotaRepository.buscarPorId(idSocioCota);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<SocioCota> obterSociosCota(Long idCota) {
		
		return socioCotaRepository.obterSocioCotaPorIdCota(idCota);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void salvarSocioCota(SocioCota socioCota, Long idCota ){
		
		if (socioCota.getId() == null && socioCota.getPrincipal()   
									  && this.socioCotaRepository.existeSocioPrincipalCota(idCota)) {

			throw new ValidacaoException(TipoMensagem.WARNING,"Deve ser informado um sócio principal!");
		}

		if (idCota == null ) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Parâmetro Cota inválido!");
		}
		
		Cota cota  = cotaRepository.buscarPorId(idCota);
		
		if (cota == null ) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Parâmetro Cota inválido!");
		}

		Telefone telefone = this.telefoneRepository.merge(socioCota.getTelefone());
		
		Endereco endereco = this.enderecoService.salvarEndereco(socioCota.getEndereco());

		socioCota.setTelefone(telefone);
		
		socioCota.setEndereco(endereco);

		socioCota.setCota(cota);

		this.socioCotaRepository.merge(socioCota);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void removerSocioCota(Long idSocioCota) {

		if (idSocioCota == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Sócio da cota é inválido.");
		}

		SocioCota socioCota = this.socioCotaRepository.buscarPorId(idSocioCota);

		if (socioCota == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Sócio da cota é inválido.");
		}
		
		this.socioCotaRepository.remover(socioCota);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void confirmarCadastroSociosCota(Long idCota) {

		List<SocioCota> sociosCota = this.obterSociosCota(idCota);
		
		if (sociosCota == null || sociosCota.isEmpty()) {
			
			return;
		}
		
		boolean principalEncontrado = false;
		
		for (SocioCota socioCota : sociosCota) {
			
			if (socioCota.getPrincipal()) {
				
				principalEncontrado = true;
				
				break;
			}
		}
		
		if (!principalEncontrado) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Deve ser informado ao menos 1 sócio principal.");
		}
	}

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<HistoricoTitularidadeCotaSocio> obterSociosHistoricoTitularidadeCota(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");

        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        return historico.getSocios() == null ? new ArrayList<HistoricoTitularidadeCotaSocio>()
                : new ArrayList<HistoricoTitularidadeCotaSocio>(historico.getSocios());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public HistoricoTitularidadeCotaSocio obterSocioHistoricoTitularidadeCota(Long idSocioCota) {
        Validate.notNull(idSocioCota, "Identificador do sócio não deve ser nulo");
        return cotaRepository.obterSocioHistoricoTitularidade(idSocioCota);
    }
}
