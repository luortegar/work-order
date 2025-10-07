package cl.creando.skappserver.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final HandlerMappingIntrospector introspector;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()

                .requestMatchers(new MvcRequestMatcher(introspector, "/**")).permitAll()

                .requestMatchers(new MvcRequestMatcher(introspector, "/public/v1/auth/authenticate")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/public/v1/work-orders/*/ot.pdf")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/public/v1/pfdExample")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/public/v1/auth/refresh-token")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/public/v1/auth/register")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/public/v1/auth/recovery-password")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/public/v1/auth/authenticate-with-code")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/public/v1/auth/change-password-and-login-with-code")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/public/v1/tenants")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/public/v1/files/**")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/public/v1/videos/**")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/actuator/health")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/v3/api-docs")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/v3/api-docs/swagger-config")).permitAll()
                .requestMatchers(new MvcRequestMatcher(introspector, "/swagger-ui/*")).permitAll()

                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}
