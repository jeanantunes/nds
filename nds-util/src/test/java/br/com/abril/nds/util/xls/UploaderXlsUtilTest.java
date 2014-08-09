package br.com.abril.nds.util.xls;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import br.com.abril.nds.util.upload.XlsMapper;
import br.com.abril.nds.util.upload.XlsUploaderUtils;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

public class UploaderXlsUtilTest {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UploaderXlsUtilTest.class);



    private static URL url = ClassLoader.getSystemResource("cota-template.xlsx");

    /**
     * @param args
     * @throws URISyntaxException
     * 
     *             Para o funcionamento desse serviço, deve-se anotar o DTO(ou
     *             qualquer bean que desejar) com a anotação {@link XlsMapper}
     *             Nessa anotação, você basicamente vai fazer o mapeamento,
     *             dizendo qual coluna do XLS, corresponde o seu field do seu
     *             bean. Vejam o exemplo no Bean de teste criado
     *             {@link CotaXlsDTO}
     * 
     */
    public static void main(String[] args) throws URISyntaxException {
	
	List<CotaTemplateDTO> lista = XlsUploaderUtils.getBeanListFromXls(CotaTemplateDTO.class, getUploadedFile());
		for (CotaTemplateDTO cotaTemplateDTO : lista) {
		    System.out.println(cotaTemplateDTO.getCodigoProduto());
		    System.out.println(cotaTemplateDTO.getNumeroCota());
		    System.out.println(cotaTemplateDTO.getReparteMinimo());
		    System.out.println(cotaTemplateDTO.getReparteMaximo());
		}
    }
    
    public static UploadedFile getUploadedFile() {
	return new UploadedFile() {

	    @Override
	    public String getFileName() {
		try {
		    return url.toURI().toString();
		} catch (URISyntaxException e) {
		    LOGGER.error(e.getMessage(), e);
		}
		return "";
	    }

	    @Override
	    public InputStream getFile() {
		try {
		    return new FileInputStream(url.toURI().getPath());
		} catch (FileNotFoundException | URISyntaxException e) {
		    LOGGER.error(e.getMessage(), e);
		}
		return null;
	    }

	    @Override
	    public String getContentType() {
		return "multipart/form-data";
	    }

		@Override
		public long getSize() {
			// TODO Auto-generated method stub
			return 0;
		}
	};
    }
}