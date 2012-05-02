package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Motorista;
import br.com.abril.nds.repository.MotoristaRepository;
import br.com.abril.nds.service.MotoristaService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class MotoristaServiceImpl implements MotoristaService {

	@Autowired
	private MotoristaRepository motoristaRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Motorista> buscarMotoristas() {
		
		return this.motoristaRepository.buscarTodos();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Motorista buscarMotoristaPorId(Long idMotorista){
		
		if (idMotorista == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id Motorista é obrigatório.");
		}
		
		return this.motoristaRepository.buscarPorId(idMotorista);
	}

	@Override
	@Transactional
	public void cadastarMotorista(Motorista motorista) {
		
		if (motorista == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Motorista é obrigatório");
		}
		
		List<String> msgs = new ArrayList<String>();
		
		if (motorista.getNome() == null || motorista.getNome().isEmpty()){
			
			msgs.add("Nome é obrigatório");
		} else {
			
			motorista.setNome(motorista.getNome().trim());
		}
		
		if (motorista.getCnh() == null || motorista.getCnh().isEmpty()){
			
			msgs.add("CNH é obrigatório");
		} else {
			
			motorista.setCnh(motorista.getCnh().trim());
		}
		
		if (!msgs.isEmpty()){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgs));
		}
		
		if (motorista.getId() == null){
			
			this.motoristaRepository.adicionar(motorista);
		} else {
			
			this.motoristaRepository.alterar(motorista);
		}
	}

	@Override
	@Transactional
	public void excluirMotorista(Long idMotorista) {
		
		if (idMotorista == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id Motorista é obrigatório");
		}
		
		this.motoristaRepository.removerPorId(idMotorista);
	}
}