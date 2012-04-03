package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.TelefoneFornecedorRepository;
import br.com.abril.nds.repository.TelefoneRepository;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class TelefoneServiceImpl implements TelefoneService {

	@Autowired
	private TelefoneCotaRepository telefoneCotaRepository;
	
	@Autowired
	private TelefoneFornecedorRepository telefoneFornecedorRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Transactional(readOnly = true)
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesCota(Long idCota, Set<Long> idsIgnorar) {
		
		if (idCota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "IdCota é obrigatório");
		}
		
		List<TelefoneAssociacaoDTO> listaTelAssoc =
				this.telefoneCotaRepository.buscarTelefonesCota(idCota, idsIgnorar);
		
		List<Telefone> listaTel = this.telefoneCotaRepository.buscarTelefonesPessoaPorCota(idCota);
		
		for (TelefoneAssociacaoDTO tDto : listaTelAssoc){
			listaTel.remove(tDto.getTelefone());
		}
		
		for (Telefone telefone : listaTel){
			TelefoneAssociacaoDTO telefoneAssociacaoDTO = new TelefoneAssociacaoDTO(false, telefone, null);
			listaTelAssoc.add(telefoneAssociacaoDTO);
		}
		
		return listaTelAssoc;
	}
	
	@Transactional
	@Override
	public void cadastrarTelefonesCota(List<TelefoneCota> listaTelefonesAdicionar, Collection<Long> listaTelefonesRemover){
		this.salvarTelefonesCota(listaTelefonesAdicionar);
		this.removerTelefonesCota(listaTelefonesRemover);
	}

	@Transactional
	@Override
	public void salvarTelefonesCota(List<TelefoneCota> listaTelefonesCota) {
		
		if (listaTelefonesCota != null){
			boolean isTelefonePrincipal = false;
			
			for (TelefoneCota telefoneCota : listaTelefonesCota){
				if (telefoneCota == null){
					throw new ValidacaoException(TipoMensagem.ERROR, "Telefone cota é obrigatório");
				}
				
				this.valiadarTelefone(telefoneCota.getTelefone(), telefoneCota.getTipoTelefone());
				
				if (isTelefonePrincipal && telefoneCota.isPrincipal()){
					throw new ValidacaoException(TipoMensagem.WARNING, "Apenas um telefone principal é permitido.");
				}
				
				if (telefoneCota.isPrincipal()){
					isTelefonePrincipal = telefoneCota.isPrincipal();
				}
				
				if (telefoneCota.getCota() == null || telefoneCota.getCota().getId() == null){
					throw new ValidacaoException(TipoMensagem.ERROR, "Cota é obrigatório");
				}
				
				if (telefoneCota.getTelefone().getId() == null){
					this.telefoneRepository.adicionar(telefoneCota.getTelefone());
				} else {
					this.telefoneRepository.alterar(telefoneCota.getTelefone());
				}
				
				if (telefoneCota.getId() == null){
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
			
			this.removerTelefone(listaTelefonesCota);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesFornecedor(Long idFornecedor, Set<Long> idsIgnorar) {
		
		if (idFornecedor == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "IdFornecedor é obrigatório");
		}
		
		List<TelefoneAssociacaoDTO> listaTelAssoc =
				this.telefoneFornecedorRepository.buscarTelefonesFornecedor(idFornecedor, idsIgnorar);
		
		List<Telefone> listaTel = this.telefoneFornecedorRepository.buscarTelefonesPessoaPorFornecedor(idFornecedor);
		
		for (Telefone telefone : listaTel){
			TelefoneAssociacaoDTO telefoneAssociacaoDTO = new TelefoneAssociacaoDTO(false, telefone, null);
			listaTelAssoc.add(telefoneAssociacaoDTO);
		}
		
		return listaTelAssoc;
	}
	
	@Transactional
	@Override
	public void cadastrarTelefonesFornecedor(List<TelefoneFornecedor> listaTelefonesAdicionar, Collection<Long> listaTelefonesRemover){
		this.salvarTelefonesFornecedor(listaTelefonesAdicionar);
		this.removerTelefonesFornecedor(listaTelefonesRemover);
	}

	@Transactional
	@Override
	public void salvarTelefonesFornecedor(List<TelefoneFornecedor> listaTelefonesFornecedor) {
		
		if (listaTelefonesFornecedor != null){
			boolean isTelefonePrincipal = false;
			
			for (TelefoneFornecedor telefoneFornecedor : listaTelefonesFornecedor){
				if (telefoneFornecedor == null){
					throw new ValidacaoException(TipoMensagem.ERROR, "Telefone fornecedor é obrigatório.");
				}
				
				this.valiadarTelefone(telefoneFornecedor.getTelefone(), telefoneFornecedor.getTipoTelefone());
				
				if (isTelefonePrincipal && telefoneFornecedor.isPrincipal()){
					throw new ValidacaoException(TipoMensagem.WARNING, "Apenas um telefone principal é permitido.");
				}
				
				if (telefoneFornecedor.isPrincipal()){
					isTelefonePrincipal = telefoneFornecedor.isPrincipal();
				}
				
				if (telefoneFornecedor.getFornecedor() == null || telefoneFornecedor.getFornecedor().getId() == null){
					throw new ValidacaoException(TipoMensagem.ERROR, "Fornecedor é obrigatório.");
				}
				
				if (telefoneFornecedor.getTelefone().getId() == null){
					this.telefoneRepository.adicionar(telefoneFornecedor.getTelefone());
				} else {
					this.telefoneRepository.alterar(telefoneFornecedor.getTelefone());
				}
				
				if (telefoneFornecedor.getId() == null){
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
			
			this.removerTelefone(listaTelefonesFornecedor);
		}
	}
	
	private void valiadarTelefone(Telefone telefone, TipoTelefone tipoTelefone){
		
		List<String> mensagensValidacao = new ArrayList<String>();
		
		if (telefone == null){
			mensagensValidacao.add("Telefone é obrigatório.");
		}
		
		if (telefone.getDdd() == null || telefone.getDdd().trim().isEmpty()){
			mensagensValidacao.add("DDD é obrigatório.");
		} else if (telefone.getDdd().trim().length() > 255){
			mensagensValidacao.add("Valor maior que o permitido para o campo DDD.");
		}
		telefone.setDdd(telefone.getDdd() != null ? telefone.getDdd().trim() : null);
		
		if (telefone.getNumero() == null || telefone.getNumero().trim().isEmpty()){
			mensagensValidacao.add("Número é obrigatório.");
		} else if (telefone.getNumero().trim().length() > 255){
			mensagensValidacao.add("Valor maior que o permitido para o campo Número.");
		}
		telefone.setNumero(telefone.getNumero() != null ? telefone.getNumero().trim() : null);
		
		if (tipoTelefone == null){
			mensagensValidacao.add("Tipo Telefone é obrigatório.");
		}
		
		if (telefone.getRamal() != null && telefone.getRamal().trim().length() > 255){
			mensagensValidacao.add("Valor maior que o permitido para o campo Ramal.");
		}
		telefone.setRamal(telefone.getRamal() != null ? telefone.getRamal().trim() : null);
		
		if (!mensagensValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
		}
	}
	
	private void removerTelefone(Collection<Long> listaTelefonesCota) {
		try {
			this.telefoneRepository.removerTelefones(listaTelefonesCota);
		} catch (Exception e) {
			//caso o telefone esteja associado a outra pessoa na base de dados não pode ser apagado.
			//nem todas as associações estão definidas, por isso é melhor tratar dessa maneira do que fazer
			//selects que terão que ser alterados até que todas as associações estejam definidas.
		}
	}
}