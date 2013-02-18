package br.com.abril.nds.integracao.engine.test;

import org.junit.Test;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthField;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;

public class TestPoc {

	@Test
	public void testFixedLength() {
		FixedLengthRouteTemplate model = new FixedLengthRouteTemplate() {
			
			@Override
			public void setupTypeMapping() {
				addTypeMapping(new FixedLengthField(1, 1, "A"), TestFixedModelHeader.class);
				addTypeMapping(new FixedLengthField(1, 1, "B"), TestFixedModelDetail.class);
				addTypeMapping(new FixedLengthField(1, 1, "C"), TestFixedModelFooter.class);				
			}

			@Override
			public String getUri() {
				return "c:/test/test.txt";
			}

			@Override
			public MessageProcessor getMessageProcessor() {
				return TestProcessor.getInstance();
			}

			@Override
			public String getFileFilterExpression() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public RouteInterface getRouteInterface() {
				// TODO Auto-generated method stub
				return null;
			}			
		};
		
//		DynamicRouter.route(model);
	}
	
	@Test
	public void testFixedLength2() {
		FixedLengthRouteTemplate model = new FixedLengthRouteTemplate() {
			
			@Override
			public void setupTypeMapping() {
				setTypeMapping(TestFixedModelHeader.class);
			}

			@Override
			public String getUri() {
				return "c:/test/test2.txt";
			}

			@Override
			public MessageProcessor getMessageProcessor() {
				return TestProcessor.getInstance();
			}

			@Override
			public String getFileFilterExpression() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public RouteInterface getRouteInterface() {
				// TODO Auto-generated method stub
				return null;
			}
		};
				
//		DynamicRouter.route(model);
	}
}
