package br.com.abril.nds.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.NotaFiscalService;


/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.fiscal.nota.NotaFiscal}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class NotaFiscalServiceImpl implements NotaFiscalService {
	
	@Autowired
	private NotaFiscalRepository notaFiscalDAO;
	
	@Override
	public Map<Long, Integer> obterTotalItensNotaFiscalPorCotaEmLote(
			ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NotaFiscal> gerarDadosNotaFicalEmLote(
			ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.NotaFiscalService#processarRetornoNotaFiscal(br.com.abril.nds.dto.RetornoNFEDTO)
	 */
	@Override
	@Transactional
	public void processarRetornoNotaFiscal(RetornoNFEDTO dadosRetornoNFE) {
		
		NotaFiscal notaFiscal = this.notaFiscalDAO.buscarPorId(dadosRetornoNFE.getIdNotaFiscal());
		
		notaFiscal.getStatusProcessamentoInterno();
		//TODO: Atualizar apenas se o status interno for diferente de retornado, 
		//		exceto em caso de cancelamento
		
		IdentificacaoEmitente emitente = notaFiscal.getIdentificacaoEmitente();
		emitente.getPessoaEmitente().getDocumento();
		//TODO: Atualizar apenas se o emitente da nota for o distribuidor
		
		InformacaoEletronica informacaoEletronica = new InformacaoEletronica();
		informacaoEletronica.setChaveAcesso(dadosRetornoNFE.getChaveAcesso());
		
		RetornoComunicacaoEletronica retornoComunicacaoEletronica = new RetornoComunicacaoEletronica();
		retornoComunicacaoEletronica.setDataRecebimento(dadosRetornoNFE.getDataRecebimento());
		retornoComunicacaoEletronica.setMotivo(dadosRetornoNFE.getMotivo());
		retornoComunicacaoEletronica.setProtocolo(dadosRetornoNFE.getProtocolo());
		retornoComunicacaoEletronica.setStatus(dadosRetornoNFE.getStatus());
		
		informacaoEletronica.setRetornoComunicacaoEletronica(retornoComunicacaoEletronica);
		
		notaFiscal.setInformacaoEletronica(informacaoEletronica);
		
		//TODO: persistir nota atualizada
	}

	@Override
	public void cancelarNotaFiscal(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void denegarNotaFiscal(Long id) {
		// TODO Auto-generated method stub
		
	}
}
