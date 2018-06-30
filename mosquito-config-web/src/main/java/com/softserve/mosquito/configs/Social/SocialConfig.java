package com.softserve.mosquito.configs.Social;//package com.softserve.mosquito.configs;

import com.softserve.mosquito.entities.User;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.connect.*;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.linkedin.api.LinkedIn;

//import org.springframework.social.google.connect.GoogleConnectionFactory;


import javax.inject.Inject;
import javax.sql.DataSource;


@Configuration
@PropertySource({"classpath:application.properties"})
@EnableSocial
public class SocialConfig {
    @Inject
    private Environment environment;

    @Inject
    private DataSource dataSource;

    /**
     * When a new provider is added to the app, register its {@link ConnectionFactory} here.
     * //        * @see GoogleConnectionFactory
     */
    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new FacebookConnectionFactory(
                environment.getProperty("spring.social.facebook.appId"),
                environment.getProperty("spring.social.facebook.appSecret")));

        return registry;
    }

    /**
     * Singleton data access object providing access to connections across all users.
     */


    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public UsersConnectionRepository usersConnectionRepository() {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator(), Encryptors.noOpText());
        repository.setConnectionSignUp(new SimpleConnectionSignUp());
        return repository;
    }

    /**
     * Request-scoped data access object providing access to the current user's connections.
     */
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        User user = SecurityContext.getCurrentUser();
        return usersConnectionRepository().createConnectionRepository(Long.toString(user.getId()));
    }

    /**
     * A proxy to a request-scoped object representing the current user's primary Google account.
     * @throws NotConnectedException if the user is not connected to Google.
     */

    /**
     * The Spring MVC Controller that allows users to sign-in with their provider accounts.
     */
    @Bean
    public ProviderSignInController providerSignInController() {
        return new ProviderSignInController(connectionFactoryLocator(), usersConnectionRepository(),
                new SimpleSignInAdapter());
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Facebook facebook(ConnectionRepository repository) {
        Connection<Facebook> connection = repository
                .findPrimaryConnection(Facebook.class);
        return connection != null ? connection.getApi() : null;
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Google google(final ConnectionRepository repository) {
        final Connection<Google> connection = repository
                .findPrimaryConnection(Google.class);
        return connection != null ? connection.getApi() : null;
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public LinkedIn linkedIn(final ConnectionRepository repository) {
        final Connection<LinkedIn> connection = repository
                .findPrimaryConnection(LinkedIn.class);
        return connection != null ? connection.getApi() : null;
    }
}
