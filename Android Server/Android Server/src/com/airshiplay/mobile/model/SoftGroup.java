/**
 * 
 */
package com.airshiplay.mobile.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author airshiplay
 * @Create Date 2013-2-3
 * @version 1.0
 * @since 1.0
 */
@Entity @Table
public class SoftGroup {
	private String id;
	private String name;
	private String description;
	private Set<Soft> softs = new HashSet<Soft>();
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE },   
			fetch = FetchType.LAZY)   
			@JoinTable(name="Soft_Group", joinColumns={@JoinColumn(name="GROUP_ID")},  
			  inverseJoinColumns={@JoinColumn(name="Soft_ID")})  
	public Set<Soft> getSofts() {
		return softs;
	}

	public void setSofts(Set<Soft> softs) {
		this.softs = softs;
	}

}
