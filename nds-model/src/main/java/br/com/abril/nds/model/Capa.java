package br.com.abril.nds.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.lightcouch.Attachment;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author Diego Fernandes
 *
 */
public class Capa implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3120778807006882744L;
	
	@SerializedName("_id")
	private String id;
	
	@SerializedName("_attachments")
	private Map<String, Attachment> attachments;
	
	
	public Capa() {
		this.attachments = new HashMap<String, Attachment>();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the attachments
	 */
	public Map<String, Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(Map<String, Attachment> attachments) {
		this.attachments = attachments;
	}
}
