package br.com.abril.nds.serialization.custom;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.converters.ConvertDataJSON;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.serialization.ProxyInitializer;
import br.com.caelum.vraptor.serialization.xstream.XStreamSerializer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

@Component
public class DefaultFlexiGridJson implements FlexiGridJson {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFlexiGridJson.class);
	
	private XStreamSerializer serializer;
    private XStream xstream;
    private TableModel wrapper;
    
    private HttpServletResponse response;
    private TypeNameExtractor extractor;
    private ProxyInitializer initializer;

    public DefaultFlexiGridJson(HttpServletResponse response, TypeNameExtractor extractor, ProxyInitializer initializer,Localization localization)
            throws IOException {
    	    
    	    this.response = response;
    	    this.extractor = extractor;
    	    this.initializer = initializer;
    	    
            xstream = new XStream(new JsonHierarchicalStreamDriver() {
                @Override
                public HierarchicalStreamWriter createWriter(Writer writer) {
                return new JsonWriter(writer, new char[0], "", JsonWriter.DROP_ROOT_MODE) {
                        @Override
                        public void addAttribute(String key, String value) {
                            if (!key.equals("class")) {
                                super.addAttribute(key, value);
                            }
                        }
                    };
                }
            });
            
            xstream.registerConverter(new ConvertDataJSON(localization));
            
            xstream.aliasField("rows", TableModel.class, "rows");
            serializer = new XStreamSerializer(xstream, response.getWriter(), extractor, initializer);
        }

        public FlexiGridJson from(List rows) {
            wrapper = new TableModel();
            wrapper.setRows(CellModelKeyValue.toCellModelKeyValue(rows));
            
            serializer.from(rows);
            return this;
        }

        public FlexiGridJson exclude(String... names) {
            serializer.exclude(names);
            return this;
        }

        public FlexiGridJson include(String... fields) {
            serializer.include(fields);
            return this;
        }

        
        public FlexiGridJson serialize() {        	
        	serializer.from(wrapper).recursive().serialize();
            return this;
        }
    
       

        public FlexiGridJson total(Integer total) {
            wrapper.setTotal(total);
            return this;
        }
 
 
		public FlexiGridJson noReference() {
			
			try{
			    xstream.setMode(XStream.NO_REFERENCES);
			    xstream.aliasField("rows", TableModel.class, "rows");
			    serializer = new XStreamSerializer(xstream, this.response.getWriter(), this.extractor, this.initializer);
			}
			catch(IOException e){
				LOGGER.error(e.getMessage(), e);
			}
			    
			return this;
		}
		

		@Override
		public FlexiGridJson page(Integer page) {
			
			wrapper.setPage(page);
			return this;
		}

}
