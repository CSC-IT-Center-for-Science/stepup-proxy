/*
 * The MIT License
 * Copyright (c) 2015 CSC - IT Center for Science, http://www.csc.fi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package fi.csc.idp.stepup.impl;


import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.shibboleth.idp.authn.AbstractAuthenticationAction;
import net.shibboleth.idp.authn.context.AuthenticationContext;
import net.shibboleth.idp.saml.authn.principal.AuthnContextClassRefPrincipal;
import net.shibboleth.idp.saml.authn.principal.AuthnContextDeclRefPrincipal;

import org.opensaml.profile.action.ActionSupport;
import org.opensaml.profile.context.ProfileRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.csc.idp.stepup.api.StepUpEventIds;
import fi.okm.mpass.shibboleth.authn.context.ShibbolethSpAuthenticationContext;

/**
 * This action is performed always in a phase where need for step up
 * has already been established.
 * 
 * Action checks if the originally provided authentication context class
 * value matches a whitelist of idp,method - values. If there is a match,  
 * stepup is regarded as already performed.
 * 
 * 
 */
@SuppressWarnings("rawtypes")
public class CheckProvidedAuthenticationContext extends
        AbstractAuthenticationAction {
    
    
    /** Class logger. */
    @Nonnull
    private final Logger log = LoggerFactory
            .getLogger(CheckProvidedAuthenticationContext.class);

     
    /** Trusted providers and their stepup methods */
    private  Map<String, List<Principal>> trustedStepupProviders;
    
     
    @SuppressWarnings("unchecked")
    public <T extends Principal> void setTrustedStepupProviders(@Nonnull Map<String, List<T>> stepupProviders) {
        log.trace("Entering");
        this.trustedStepupProviders = new HashMap<String, List<Principal>>();
        for ( Map.Entry<String, List<T>>entry:stepupProviders.entrySet()){
            this.trustedStepupProviders.put(entry.getKey(), (List<Principal>) entry.getValue());   
        }
        log.trace("Leaving");
    }
    

        
   
    /** {@inheritDoc} */
    @Override
    protected void doExecute(
            @Nonnull final ProfileRequestContext profileRequestContext,
            @Nonnull final AuthenticationContext authenticationContext) {
        log.trace("Entering");
        
        final ShibbolethSpAuthenticationContext shibbolethContext = authenticationContext
                .getSubcontext(ShibbolethSpAuthenticationContext.class);
        if (shibbolethContext == null) {
            log.debug("{} Could not get shib proxy context", getLogPrefix());
            ActionSupport.buildEvent(profileRequestContext,
                    StepUpEventIds.EXCEPTION);
            log.trace("Leaving");
            return;
        }
        if (shibbolethContext.getIdp() == null){
            log.debug("{} Could not get provider entitytid ", getLogPrefix());
            ActionSupport.buildEvent(profileRequestContext,
                    StepUpEventIds.EXCEPTION);
            log.trace("Leaving");
            return;
        }
        Principal providedMethod=null;
        if (shibbolethContext.getContextClass() != null){
            providedMethod=new AuthnContextClassRefPrincipal(shibbolethContext.getContextClass()); 
        }else if (shibbolethContext.getContextDecl() != null){
            providedMethod=new AuthnContextDeclRefPrincipal(shibbolethContext.getContextDecl()); 
        }
        if (providedMethod == null){
            log.debug("{} Could not get authentication method ", getLogPrefix());
            ActionSupport.buildEvent(profileRequestContext,
                    StepUpEventIds.EXCEPTION);
            log.trace("Leaving");
            return;
        }
        if (trustedStepupProviders == null || !trustedStepupProviders.containsKey(shibbolethContext.getIdp())){
            //We continue with stepup as the idp is not trusted
            //to provide stepup
            ActionSupport.buildEvent(profileRequestContext,
                    StepUpEventIds.EVENTID_CONTINUE_STEPUP);
            log.trace("Leaving");
            return;
        }
        if (trustedStepupProviders.get(shibbolethContext.getIdp()).contains(providedMethod)){
            //The provided value is treated as step up, step up not performed
            ActionSupport.buildEvent(profileRequestContext,
                    StepUpEventIds.EVENTID_AUTHNCONTEXT_STEPUP);
            return;   
        }
        //Continue with stepup
        ActionSupport.buildEvent(profileRequestContext,
                StepUpEventIds.EVENTID_CONTINUE_STEPUP);
        log.trace("Leaving");
    }
    
    

}