package br.com.abril.nds.controllers;

import br.com.caelum.vraptor.interceptor.multipart.DefaultMultipartConfig;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class CustomMultipartConfig extends DefaultMultipartConfig {

	public long getSizeLimit() {
       //Define que é ilimitado o tamanho dos arquivos upload
		return -1;
    }
}
