package iths.glenn.drick.security;

import iths.glenn.drick.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // SQL database needs an authority(username, authority) table.

    @Autowired
    private UserPrincipalDetailsService userPrincipalDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private DataSource dataSource;

    public WebSecurityConfig(UserPrincipalDetailsService customUserDetailsService) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
    }

    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .withUser("zeuser").password("password").roles("USER")
                .and()
                .withUser("zeadmin").password("password").roles("USER", "ADMIN");
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager()))
                .addFilter(new AuthorizationFilter(authenticationManager(), this.userRepository))
                .authorizeRequests()
                .antMatchers("/scrape/all").hasRole("admin")
                .antMatchers("/scrape/**").hasRole("user")
                .antMatchers(HttpMethod.POST, "/user/").permitAll()
                .antMatchers("/user/**").hasRole("user")
                .antMatchers("/drinks").permitAll()
                .antMatchers("/drinks/**").hasRole("user")
                .antMatchers("/trips/**").hasRole("user")
                .antMatchers(HttpMethod.POST, "/trips").hasRole("admin")
                .antMatchers(HttpMethod.DELETE, "/trips/{startpoint}/{endpoint}/{tripinfo}/{wayoftravel}").hasRole("admin")
                .antMatchers(HttpMethod.PUT, "/trips/{startpoint}/{endpoint}/{tripinfo}/{wayoftravel}").hasRole("admin")
                .antMatchers("/trips/{startpoint}/{endpoint}/{tripinfo}/{wayoftravel}").hasRole("user")
                .antMatchers("/calculate/drunkPrice/{username}/{budget}/{fetchAmount}").hasRole("user")
                .antMatchers("/calculate/drunksForBudget/{username}/{budget}/{fetchAmount}").hasRole("user")
                .antMatchers("/calculate/drunksForBudget/{username}/{budget}/{fetchAmount}/{type}").hasRole("user")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login").permitAll()

                .and()
                .httpBasic();

                //.authenticationEntryPoint(authenticationEntryPoint);
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);

        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}