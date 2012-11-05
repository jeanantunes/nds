package br.com.abril.nds.integracao.service.impl;

import java.text.DateFormat;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateFormatUtils;
import org.exolab.castor.types.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.dto.SolicitacaoDTO;
import br.com.abril.nds.integracao.icd.model.DetalheFaltaSobra;
import br.com.abril.nds.integracao.icd.model.DetalheFaltaSobra.DfsPK;
import br.com.abril.nds.integracao.icd.model.SolicitacaoFaltaSobra;
import br.com.abril.nds.integracao.icd.model.SolicitacaoFaltaSobra.SfsPK;
import br.com.abril.nds.integracao.model.canonic.EMS0128Input;
import br.com.abril.nds.integracao.model.canonic.EMS0128InputItem;
import br.com.abril.nds.integracao.repository.SolicitacaoFaltasSobrasRepository;
import br.com.abril.nds.integracao.service.IcdObjectService;


@Service
public class IcdObjectServiceImpl implements IcdObjectService {
	
	@Autowired
	private SolicitacaoFaltasSobrasRepository solicitacaoFaltasSobrasRepository;
	

	public IcdObjectServiceImpl() {
		
	}
	
	@Override
	public Set<Integer> recuperaSolicitacoesSolicitadas(Long distribuidor) {

		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoesSolicitadas(distribuidor) ;
	
	}

	@Override
	public Set<Integer> recuperaSolicitacoesAcertadas(Long distribuidor) {
	
		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoesAcertadas(distribuidor);

	}

	@Override
	public List<SolicitacaoDTO> recuperaSolicitacoes(Long distribuidor) {
		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoes(distribuidor);
	}

	@Override
	public void insereSolicitacao(EMS0128Input doc) {
		SolicitacaoFaltaSobra sfs = new SolicitacaoFaltaSobra();

		SfsPK sfsPK = new SfsPK();		
		sfsPK.setCodigoDistribuidor( Long.valueOf( doc.getCodigoDistribuidor() ) );
		sfsPK.setDataSolicitacao(doc.getDataSolicitacao());
		sfsPK.setHoraSolicitacao( java.sql.Time.valueOf( DateFormatUtils.format(doc.getHoraDeCriacao(), "hh:mm:ss")  ) );
		
		sfs.setSfsPK(sfsPK);
		sfs.setCodigoForma(doc.getFormaSolicitacao());
		sfs.setCodigoSituacao(doc.getSituacaoSolicitacao());
		
		for (EMS0128InputItem item : doc.getItems()) {
			DetalheFaltaSobra dfs = new DetalheFaltaSobra();
			
			DfsPK dfsPK = new DfsPK();
			dfs.setDfsPK(dfsPK);
			dfs.setCodigoTipoAcerto(item.getTipoAcerto());
			dfs.setCodigoAcerto(doc.getSituacaoSolicitacao());
			
//			dfs.setMotivoSituacaoFaltaSobra();
			
			
			sfs.getItens().add(dfs);			
		}		
		
		solicitacaoFaltasSobrasRepository.adicionar(sfs);
		
	}
	

}
