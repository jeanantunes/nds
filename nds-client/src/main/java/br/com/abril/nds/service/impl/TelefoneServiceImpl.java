package br.com.abril.nds.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.TelefoneFornecedorRepository;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class TelefoneServiceImpl implements TelefoneService {

	@Autowired
	private TelefoneCotaRepository telefoneCotaRepository;
	
	@Autowired
	private TelefoneFornecedorRepository telefoneFornecedorRepository;
	
	@Transactional(readOnly = true)
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesCota(Long idCota, Set<Long> idsIgnorar) {
		
		if (idCota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "IdCota é obrigatório");
		}
		
		return this.telefoneCotaRepository.buscarTelefonesCota(idCota, idsIgnorar);
	}

	@Transactional
	@Override
	public void salvarTelefonesCota(List<TelefoneCota> listaTelefonesCota) {
		
		if (listaTelefonesCota != null){
			for (TelefoneCota telefoneCota : listaTelefonesCota){
				if (telefoneCota == null){
					throw new ValidacaoException(TipoMensagem.ERROR, "Telefone cota é obrigatório");
				}
				
				this.valiadarTelefone(telefoneCota.getTelefone(), telefoneCota.getTipoTelefone());
				
				if (telefoneCota.getCota() == null || telefoneCota.getCota().getId() == null){
					throw new ValidacaoException(TipoMensagem.ERROR, "Cota é obrigatório");
				}
				
				if (telefoneCota.getTelefone().getId() == null){
					this.telefoneCotaRepository.adicionar(telefoneCota);
				} else {
					this.telefoneCotaRepository.alterar(telefoneCota);
				}
			}
		}
	}

	@Transactional
	@Override
	public void removerTelefonesCota(Collection<Long> listaTelefonesCota) {
		
		if (listaTelefonesCota != null && !listaTelefonesCota.isEmpty()){
			this.telefoneCotaRepository.removerTelefonesCota(listaTelefonesCota);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesFornecedor(Long idFornecedor, Set<Long> idsIgnorar) {
		
		if (idFornecedor == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "IdFornecedor é obrigatório");
		}
		
		return this.telefoneFornecedorRepository.buscarTelefonesFornecedor(idFornecedor, idsIgnorar);
	}

	@Transactional
	@Override
	public void salvarTelefonesFornecedor(List<TelefoneFornecedor> listaTelefonesFornecedor) {
		
		if (listaTelefonesFornecedor != null){
			for (TelefoneFornecedor telefoneFornecedor : listaTelefonesFornecedor){
				if (telefoneFornecedor == null){
					throw new ValidacaoException(TipoMensagem.ERROR, "Telefone fornecedor é obrigatório");
				}
				
				this.valiadarTelefone(telefoneFornecedor.getTelefone(), telefoneFornecedor.getTipoTelefone());
				
				if (telefoneFornecedor.getFornecedor() == null || telefoneFornecedor.getFornecedor().getId() == null){
					throw new ValidacaoException(TipoMensagem.ERROR, "Fornecedor é obrigatório");
				}
				
				if (telefoneFornecedor.getTelefone().getId() == null){
					this.telefoneFornecedorRepository.adicionar(telefoneFornecedor);
				} else {
					this.telefoneFornecedorRepository.alterar(telefoneFornecedor);
				}
			}
		}
	}

	@Transactional
	@Override
	public void removerTelefonesFornecedor(Collection<Long> listaTelefonesFornecedor) {
		if (listaTelefonesFornecedor != null && !listaTelefonesFornecedor.isEmpty()){
			this.telefoneFornecedorRepository.removerTelefonesFornecedor(listaTelefonesFornecedor);
		}
	}
	
	private void valiadarTelefone(Telefone telefone, TipoTelefone tipoTelefone){
		
		if (telefone == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Telefone é obrigatório");
		}
		
		if (telefone.getDdd() == null || telefone.getDdd().trim().isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "DDD é obrigatório");
		}
		
		if (telefone.getNumero() == null || telefone.getNumero().trim().isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Número é obrigatório");
		}
		
		if (telefone.getRamal() == null || telefone.getRamal().trim().isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Ramal é obrigatório");
		}
		
		if (tipoTelefone == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo Telefone é obrigatório");
		}
	}
}