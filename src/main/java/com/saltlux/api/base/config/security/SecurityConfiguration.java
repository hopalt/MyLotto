package com.saltlux.api.base.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final JwtTokenProvider jwtTokenProvider;

  @Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .httpBasic().disable()  //rest api 이므로 기본 설정 사용 안함. 
      .csrf().disable()       //rest api 이므로 csrf 보안 필요없음
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //jwt token으로 인증 하므로 세션은 필요 없음
      .and()
        .authorizeRequests()  //다음 리퀘스트에 대한 사용 훤한 체크
          .antMatchers("/*/login", "/*/signup").permitAll()        //가입 및 인증은 누구나 접근 가능
          .antMatchers(HttpMethod.GET, "helloworld/**").permitAll() //helloworld로 시작하는 get 요청 리소스는 누구나 접근 가능
          .anyRequest().hasRole("USER") //그외 요청은 인증된 회원만 가능
      .and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);  //jwt token 필터를 id/password 인증 필터 전에 넣는다
  }

	@Override
	public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
    //하고자 할려는 방식에서는 이건 굳이 필요 없을듯
	}


}
