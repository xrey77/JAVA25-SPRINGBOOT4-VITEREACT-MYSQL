package com.java25.java25.springboot4.mysql.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn; 
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
@EntityListeners(AuditingEntityListener.class) // Enable auditing for this entity
public class User implements Serializable, UserDetails {
	
	private static final long serialVersionUID = 4637886253474968134L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="firstname")
	private String firstname;
	
	@Column(name="lastname")
	private String lastname;

	@Column(name="email", unique = true, nullable = false)
	private String email;
	
	@Column(name="mobile")
	private String mobile;
	
	// utf8mb4_bin WILL MAKE USERNAME CASE SENSITIVE
	@Column(columnDefinition = "VARCHAR(255) COLLATE utf8mb4_bin", unique = true, nullable = false)
	private String username;

	@Column(name="password")
	private String password;

	@Column(name="userpic", columnDefinition = "VARCHAR(25) DEFAULT 'pix.png'")
	private String userpic;

	@Column(name="isactivated", columnDefinition = "integer default 1")
	private int isactivated;

	@Column(name="isblocked", columnDefinition = "integer default 0")
	private int isblocked;

	@Column(name="mailtoken",  columnDefinition = "integer default 0")
	private int mailtoken;

    @Column(name="secret", columnDefinition = "TEXT")
	private String secret;
	
    @Column(name="qrcodeurl", columnDefinition = "TEXT")
    private String qrcodeurl;
	
	// Auditing fields
    @CreatedDate
	@Column(name="created_at", nullable = false, updatable = false) // Ensures column name is correct and value set once
	private LocalDateTime created_at;
    
    @LastModifiedDate
	@Column(name="updated_at")
	private LocalDateTime updated_at;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getUserpic() {
		return userpic;
	}

	public void setUserpic(String userpic) {
		this.userpic = userpic;
	}

	public int getIsactivated() {
		return isactivated;
	}

	public void setIsactivated(int isactivated) {
		this.isactivated = isactivated;
	}

	public int getIsblocked() {
		return isblocked;
	}

	public void setIsblocked(int isblocked) {
		this.isblocked = isblocked;
	}

	public int getMailtoken() {
		return mailtoken;
	}

	public void setMailtoken(int mailtoken) {
		this.mailtoken = mailtoken;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getQrcodeurl() {
		return qrcodeurl;
	}

	public void setQrcodeurl(String qrcodeurl) {
		this.qrcodeurl = qrcodeurl;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	    name = "user_roles",
	    joinColumns = @JoinColumn(name = "user_id"),
	    inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles;	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    if (this.roles == null) {
	        return Collections.emptyList();
	    }
	    return this.roles.stream()
	            .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
	            .collect(Collectors.toList());
	}

	
	public void setRoles(Set<Role> roles) {
	    if (this.roles == null) {
	        this.roles = roles;
	    } else {
	        this.roles.clear();
	        if (roles != null) {
	            this.roles.addAll(roles);
	        }
	    }
	}

	public Object map(Object object) {
		// TODO Auto-generated method stub
		return null;
	}	
}
