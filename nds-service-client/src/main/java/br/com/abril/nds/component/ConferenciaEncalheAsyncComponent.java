package br.com.abril.nds.component;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;

public interface ConferenciaEncalheAsyncComponent {

	public void finalizarConferenciaEncalheAsync(ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Usuario usuario,
			boolean indConferenciaContingencia,
			BigDecimal reparte);
	
	public void salvarConferenciaEncalhe(
			final ControleConferenciaEncalheCota controleConfEncalheCota, 
			final List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			final Usuario usuario, 
			final boolean indConferenciaContingencia);
	
}
