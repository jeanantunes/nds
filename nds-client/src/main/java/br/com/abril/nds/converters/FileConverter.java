package br.com.abril.nds.converters;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFileConverter;
import br.com.caelum.vraptor.ioc.RequestScoped;

@Convert(UploadedFile.class)
@RequestScoped
public class FileConverter extends UploadedFileConverter {

	public FileConverter(HttpServletRequest request) {
		super(request);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UploadedFile convert(String value, Class<? extends UploadedFile> type, ResourceBundle bundle) {		
		
		if (value == null || value.isEmpty()) {
		
			return null;
		}
		
		return super.convert(value, type, bundle);		
	}
}
