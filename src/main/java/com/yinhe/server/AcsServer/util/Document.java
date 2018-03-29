package com.yinhe.server.AcsServer.util;

import java.io.Serializable;

public class Document implements Serializable, Comparable<Document> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2808878044452863358L;
	private String name;
	private Long id;
    private String type;
    
    public Document(){
    	
    }
    
    public Document(String name,Long id,String type){
    	this.name = name;
    	this.id = id;
    	this.type = type;
    }
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	//Eclipse Generated hashCode and equals
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Document other = (Document) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

	@Override
	public String toString() {
	    return id.toString();
	}
	  
	@Override
	public int compareTo(Document document) {
		 return this.getName().compareTo(document.getName());
	}

}
