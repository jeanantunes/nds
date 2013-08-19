/**
 * 
 */
package br.com.abril.nds.serialization.custom;

import java.util.List;

import br.com.caelum.vraptor.View;

/**
 * @author Diego Fernandes
 *
 */
public interface FlexiGridJson extends View {
	
	
	public FlexiGridJson from(List rows);


	public FlexiGridJson exclude(String... names);

	public FlexiGridJson include(String... fields);

	public FlexiGridJson serialize();

	public FlexiGridJson total(Integer total);
	
	public FlexiGridJson page(Integer page);
	
	
	public FlexiGridJson noReference();

}
