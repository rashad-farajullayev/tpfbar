package com.example.auth.service;

import com.example.auth.model.AuthToken;
import com.example.auth.model.JwtValidationResponse;
import com.example.auth.model.LdapLoginRequest;
import com.example.auth.model.LdapLoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.Iterator;
import java.util.LinkedList;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    public static final String CN_PREFIX = "cn";
    private final LdapTemplate ldapTemplate;
    private final TokenService tokenService;

    @Autowired
    public AuthServiceImpl(LdapTemplate ldapTemplate, TokenService tokenService) {
        this.ldapTemplate = ldapTemplate;
        this.tokenService = tokenService;
    }

    @Override
    public LdapLoginResponse login(LdapLoginRequest request) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(CN_PREFIX, request.getUserName()));
        log.info("Checking to see if username and password is correct for user: " + filter.encode());
        boolean result = ldapTemplate.authenticate("", filter.encode(), request.getPassword());

        AuthToken token = null;

        if (result)
            token = tokenService.generateToken(request.getUserName());

        return LdapLoginResponse.builder().successfull(result).authToken(token).build();
    }

    @Override
    public JwtValidationResponse validate(String jwtToken) {

        String userName = tokenService.validate(jwtToken);

        if (userName == null)
            return new JwtValidationResponse(userName, false, null);

        log.info("Validating user token for " + userName);

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(CN_PREFIX, userName));


        LinkedList<Attribute> result = (LinkedList) ldapTemplate.search(
                "", filter.encode(),
                new AttributesMapper() {
                    public Object mapFromAttributes(Attributes attrs)
                            throws NamingException, javax.naming.NamingException {
                        Iterator<? extends Attribute> iterator = attrs.getAll().asIterator();
                        while (iterator.hasNext())
                        {
                            Attribute attr = iterator.next();
                            log.info("ID: " + attr.getID() + "; Value: " + attr.get().toString());
                        }

                        return attrs.get("mail");
                    }
                });

        if (result == null || result.size() != 1)
            return new JwtValidationResponse(userName, false, null);

        String email = null;
        try {
            if (result.size() > 0 && result.get(0) != null)
                email = (String) result.get(0).get();
        } catch (javax.naming.NamingException e) {
            e.printStackTrace();
        }

        return new JwtValidationResponse(userName, true, email);
    }
}
