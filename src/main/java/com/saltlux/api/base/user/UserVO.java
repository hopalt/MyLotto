package com.saltlux.api.base.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserVO implements UserDetails {

	private static final long serialVersionUID = -8502206097778719729L;

	@Id
	@Column(name = "user_id", nullable = false, unique = true, length = 30)
	private String userId;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "password", nullable = false, length = 100)
	private String password;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@ElementCollection(fetch = FetchType.EAGER)
	@Builder.Default
	private List<String> roles = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public String getPassword() {
		return this.password;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public String getUsername() {
		return this.userId;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isEnabled() {
		return true;
	}

}
