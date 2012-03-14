package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.controllers.exception.ValidacaoException;
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
	
	@Override
	public List<TelefoneCota> buscarTelefonesCota(Long idCota) {
		
		if (idCota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "IdCota é obrigatório");
		}
		
		return this.telefoneCotaRepository.buscarTelefonesCota(idCota);
	}

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

	@Override
	public void removerTelefonesCota(List<TelefoneCota> listaTelefonesCota) {
		
		List<Long> idsTelefones = new ArrayList<Long>();
		
		if (listaTelefonesCota != null){
			for (TelefoneCota telefoneCota : listaTelefonesCota){
				if (telefoneCota == null || telefoneCota.getId() == null){
					throw new ValidacaoException(TipoMensagem.ERROR, "Telefone Cota é obrigatório");
				}
				
				idsTelefones.add(telefoneCota.getId());
			}
		}
		
		if (!idsTelefones.isEmpty()){
			this.telefoneCotaRepository.removerTelefonesCota(idsTelefones);
		}
	}

	@Override
	public List<TelefoneFornecedor> buscarTelefonesFornecedor(Long idFornecedor) {
		
		if (idFornecedor == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "IdFornecedor é obrigatório");
		}
		
		return this.telefoneFornecedorRepository.buscarTelefonesFornecedor(idFornecedor);
	}

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

	@Override
	public void removerTelefonesFornecedor(List<TelefoneFornecedor> listaTelefonesFornecedor) {

		List<Long> idsTelefones = new ArrayList<Long>();
		
		if (listaTelefonesFornecedor != null){
			for (TelefoneFornecedor telefoneFornecedor : listaTelefonesFornecedor){
				if (telefoneFornecedor == null || telefoneFornecedor.getId() == null){
					throw new ValidacaoException(TipoMensagem.ERROR, "Telefone Fornecedor é obrigatório");
				}
				
				idsTelefones.add(telefoneFornecedor.getId());
			}
		}
		
		if (!idsTelefones.isEmpty()){
			this.telefoneFornecedorRepository.removerTelefonesFornecedor(idsTelefones);
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