package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusRetornado;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamento;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.CancelaNotaFiscalService;

@Service
public class CancelaNotaFiscalServiceImpl implements CancelaNotaFiscalService {

	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	@Override
	@Transactional
	public List<NfeDTO> pesquisar() {


		return null;
	}
	
}
