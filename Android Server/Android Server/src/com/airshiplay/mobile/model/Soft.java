/**
 * 
 */
package com.airshiplay.mobile.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author airshiplay
 * @Create Date 2013-2-3
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table
public class Soft {
	private Set<SoftGroup> groups=new HashSet<SoftGroup>();
	private String id;
	private String resid;
	private String name;
	private String pkg;
	private String versionName;
	private long versionCode;
	private String description;
	private String os;
	private Date publish;
	private boolean enable;



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

	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public long getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(long versionCode) {
		this.versionCode = versionCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	@Temporal(TemporalType.DATE)
	@NotNull
	@Column(updatable = false)
	public Date getPublish() {
		return publish;
	}

	public void setPublish(Date publish) {
		this.publish = publish;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getResid() {
		return resid;
	}

	public void setResid(String resid) {
		this.resid = resid;
	}
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE },  
		    fetch = FetchType.LAZY, mappedBy="softs" ) 
	public Set<SoftGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<SoftGroup> groups) {
		this.groups = groups;
	}

}
