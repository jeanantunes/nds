package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Veiculo;
import br.com.abril.nds.repository.VeiculoRepository;
import br.com.abril.nds.service.VeiculoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class VeiculoServiceImpl implements VeiculoService {

	@Autowired
	private VeiculoRepository veiculoRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Veiculo> buscarVeiculos() {
		
		return this.veiculoRepository.buscarTodos();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Veiculo buscarVeiculoPorId(Long idVeiculo){
		
		if (idVeiculo == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id Veículo é obrigatório.");
		}
		
		return this.veiculoRepository.buscarPorId(idVeiculo);
	}

	@Override
	@Transactional
	public void cadastarVeiculo(Veiculo veiculo) {
		
		if (veiculo == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Veículo é obrigatório");
		}
		
		List<String> msgs = new ArrayList<String>();
		
		if (veiculo.getTipoVeiculo() == null || veiculo.getTipoVeiculo().trim().isEmpty()){
			
			msgs.add("Tipo veículo é obrigatório.");
		} else {
			
			veiculo.setTipoVeiculo(veiculo.getTipoVeiculo().trim());
		}
		
		if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()){
			
			msgs.add("Placa é obrigatório.");
		} else {
			veiculo.setPlaca(veiculo.getPlaca().trim());
		}
		
		if (!msgs.isEmpty()){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgs));
		}
		
		if (veiculo.getId() == null){
			
			this.veiculoRepository.adicionar(veiculo);
		} else {
			
			this.veiculoRepository.alterar(veiculo);
		}
	}

	@Override
	@Transactional
	public void excluirVeiculo(Long idVeiculo) {
		
		if (idVeiculo == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id Veículo é obrigatório.");
		}
		
		this.veiculoRepository.removerPorId(idVeiculo);
	}
}