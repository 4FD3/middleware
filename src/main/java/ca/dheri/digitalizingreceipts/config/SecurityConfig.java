package ca.dheri.digitalizingreceipts.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
class SecurityConfig {
    @Autowired
    private MockUserFilter mockUserFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {

        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setRedirectStrategy(new DefaultRedirectStrategy() {
            @Override
            public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
                // Custom logic here, e.g., appending a token or redirecting to a different frontend route
                String frontendUrl = "http://localhost:3000";
                super.sendRedirect(request, response, frontendUrl);
            }
        });

        http
                .addFilterBefore(mockUserFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
//                        .requestMatchers("/").permitAll()
//                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .dispatcherTypeMatchers(DispatcherType.REQUEST).authenticated()
                        .anyRequest().authenticated())
                .headers((headers) -> headers
                        .defaultsDisabled()
                        .cacheControl(withDefaults())
                        .frameOptions((f) -> f.disable())
                )
                .csrf((csrf) -> {
//                    csrf.ignoringRequestMatchers(toH2Console());
                    csrf.ignoringRequestMatchers("/api/**");
                })
                .cors(customizer -> customizer.configurationSource(corsConfigurationSource))

//                .oauth2Login(
//                        oauth2 -> oauth2
//                                .defaultSuccessUrl("http://localhost:3000", true)
//
//                )
//                .oauth2Client(withDefaults())

                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .logout((l -> l.logoutSuccessUrl("/api/").permitAll()));
        return http.build();
    }

//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source; // Correctly return the instance without casting
//    }
}