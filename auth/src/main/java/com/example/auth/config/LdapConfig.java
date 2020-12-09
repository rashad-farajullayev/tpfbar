package com.example.auth.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfig {

    @Value("${ldap.urls}")
    private String dcUrlPaths;

    @Value("${ldap.base}")
    private String ldapBase;

    @Value("${ldap.dn}")
    private String ldapDN;

    @Value("${ldap.pass}")
    private String ldapPassword;

    @Bean
    @Qualifier("ldapContext")
    public LdapContextSource getContextSource() throws Exception {

        LdapContextSource contextSource = new LdapContextSource();
        String[] paths = dcUrlPaths.split(",");
        contextSource.setUrls(paths);
        contextSource.setBase(ldapBase);
        contextSource.setUserDn(ldapDN);
        contextSource.setPassword(ldapPassword);
        contextSource.setReferral("follow");
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() throws Exception {
        LdapTemplate ldapTemplate = new LdapTemplate(getContextSource());
        ldapTemplate.setIgnorePartialResultException(true);
        ldapTemplate.setContextSource(getContextSource());
        return ldapTemplate;
    }
}
