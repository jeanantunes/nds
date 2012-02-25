package br.com.abril.nds.service.impl;

<<<<<<< .merge_file_a04564

=======
>>>>>>> .merge_file_a00252
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.repository.NotaFiscalDAO;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.vo.filtro.FiltroConsultaNotaFiscalVO;

<<<<<<< .merge_file_a04564

=======
>>>>>>> .merge_file_a00252
@Service
public class NotaFiscalServiceImpl implements NotaFiscalService {

	@Autowired
	private NotaFiscalDAO notaFiscalDAO;
<<<<<<< .merge_file_a04564
		
	@Transactional
	public void inserirNotaFiscal(NotaFiscal notaFiscal){
		
	}
=======
>>>>>>> .merge_file_a00252
	
	@Override
	@Transactional
	public List<NotaFiscal> obterNotasFiscaisCadastradas(
			FiltroConsultaNotaFiscalVO filtroConsultaNotaFiscal) {
		return notaFiscalDAO.obterNotasFiscaisCadastradas(filtroConsultaNotaFiscal);
	}
}
<<<<<<< .merge_file_a04564
	


	
	
	
=======
>>>>>>> .merge_file_a00252
