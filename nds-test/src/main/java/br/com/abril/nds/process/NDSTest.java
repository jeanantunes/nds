package br.com.abril.nds.process;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import br.com.abril.nds.config.NDSConfig;

@ContextConfiguration(classes = NDSConfig.class, loader = AnnotationConfigContextLoader.class)
public class NDSTest extends AbstractTestNGSpringContextTests {

}
