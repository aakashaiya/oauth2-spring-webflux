package oauth2.spring.webflux.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		
		http
			.cors()
			.and()
			.csrf().disable()
			.httpBasic().disable()
			.authorizeExchange()
			.pathMatchers("/login**","/oauth2/authorization/**")
			.permitAll()
			.anyExchange().authenticated()
			.and()
			.oauth2Login(oauth2 -> oauth2
			.authenticationMatcher(redirectUrl())
			);
		return http.build();
	}


	private ServerWebExchangeMatcher redirectUrl(){
		return new PathPatternParserServerWebExchangeMatcher("/home");
	}
	
	@Bean
	public RouterFunction<ServerResponse> indexRouter(@Value("classpath:static/index.html") final Resource indexHtml){
		return RouterFunctions.route(RequestPredicates.GET("/home"), 
				request -> ServerResponse.ok().contentType(MediaType.TEXT_HTML).bodyValue(indexHtml));
	}
}
