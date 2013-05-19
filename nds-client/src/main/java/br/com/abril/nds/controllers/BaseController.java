package br.com.abril.nds.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.export.NDSFileHeader;


public abstract class BaseController {

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	protected NDSFileHeader getNDSFileHeader() {
		
		return obterNDSFileHeader(null);
	}
	
	protected NDSFileHeader getNDSFileHeader(Date data) {
		
		return obterNDSFileHeader(data);
	}
	
	private NDSFileHeader obterNDSFileHeader(Date data){
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		List<String> dadosDistribuidor = this.distribuidorService.obterNomeCNPJDistribuidor();
		
		if (dadosDistribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(dadosDistribuidor.get(0));
			ndsFileHeader.setCnpjDistribuidor(dadosDistribuidor.get(1));
		}
		
		ndsFileHeader.setData( (data == null) ?  new Date() : data);
		
		ndsFileHeader.setNomeUsuario(this.getUsuarioLogado().getNome());
		
		ndsFileHeader.setLogo(this.getLogoDistribuidor());
		
		return ndsFileHeader;
	}
	
	protected Usuario getUsuarioLogado() {
		return usuarioService.getUsuarioLogado();
	}
	
	protected InputStream getLogoDistribuidor(){
		
		InputStream inputStream = parametrosDistribuidorService.getLogotipoDistribuidor();
		
		if(inputStream == null){
		  
			return new ByteArrayInputStream(new byte[0]);
		}
		
		return inputStream;
	}

}
