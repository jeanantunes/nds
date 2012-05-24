package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.client.vo.TipoDescontoVO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.repository.TipoDescontoRepository;
import br.com.abril.nds.service.TipoDescontoService;

@Service
public class TipoDescontoServiceImpl implements TipoDescontoService {

	@Autowired
	private TipoDescontoRepository tipoDescontoRepository;
	
	@Override
	public void incluirDescontoGeral(TipoDesconto tipoDesconto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void atualizarDistribuidores(BigDecimal desconto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TipoDescontoVO> obterTipoDescontoGeral() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cota obterCota(int numeroCota) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TipoDesconto> obterTodosTiposDescontos() {
		
		return this.tipoDescontoRepository.buscarTodos();
	}

}
