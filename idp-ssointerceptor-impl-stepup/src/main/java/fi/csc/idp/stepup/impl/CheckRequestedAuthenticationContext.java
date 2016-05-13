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
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.security.auth.Subject;

import net.shibboleth.idp.attribute.IdPAttribute;
import net.shibboleth.idp.authn.AbstractAuthenticationAction;
import net.shibboleth.idp.authn.context.AuthenticationContext;
import net.shibboleth.idp.saml.authn.principal.AuthnContextClassRefPrincipal;
import net.shibboleth.idp.saml.authn.principal.AuthnContextDeclRefPrincipal;
import net.shibboleth.utilities.java.support.annotation.constraint.NonnullElements;
import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.logic.Constraint;

import org.opensaml.messaging.context.navigate.MessageLookup;
import org.opensaml.profile.action.ActionSupport;
import org.opensaml.profile.context.ProfileRequestContext;
import org.opensaml.profile.context.navigate.InboundMessageContextLookup;
import org.opensaml.saml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml.saml2.core.AuthnContextDeclRef;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.saml.saml2.core.RequestedAuthnContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import fi.csc.idp.stepup.api.StepUpEventIds;

/**
 * An action that checks if step up authentication is requested.
 * 
 */

@SuppressWarnings("rawtypes")
public class CheckRequestedAuthenticationContext extends
        AbstractAuthenticationAction {

    /** Class logger. */
    @Nonnull
    private final Logger log = LoggerFactory
            .getLogger(CheckRequestedAuthenticationContext.class);

    /** Lookup strategy function for obtaining {@link AuthnRequest}. */
    @Nonnull
    private Function<ProfileRequestContext, AuthnRequest> authnRequestLookupStrategy;

    /** The request message to read from. */
    @Nullable
    private AuthnRequest authnRequest;

    /** The attribute to match against. */
    @Nullable
    private IdPAttribute attribute;
    
    /** The authentication methods indicating step up. */
    @Nonnull
    private Subject stepupPrincipals;

    /** Constructor. */
    public CheckRequestedAuthenticationContext() {
        log.trace("Entering");
        authnRequestLookupStrategy = Functions.compose(new MessageLookup<>(
                AuthnRequest.class), new InboundMessageContextLookup());
        log.trace("Leaving");
    }

    /**
     * Set the strategy used to locate the {@link AuthnRequest} to read from.
     * 
     * @param strategy
     *            lookup strategy
     */

    public void setAuthnRequestLookupStrategy(
            @Nonnull final Function<ProfileRequestContext, AuthnRequest> strategy) {
        log.trace("Entering");
        ComponentSupport
                .ifInitializedThrowUnmodifiabledComponentException(this);
        authnRequestLookupStrategy = Constraint.isNotNull(strategy,
                "AuthnRequest lookup strategy cannot be null");
        log.trace("Leaving");
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    protected boolean doPreExecute(
            @Nonnull final ProfileRequestContext profileRequestContext,
            @Nonnull final AuthenticationContext authenticationContext) {

        log.trace("Entering");
        if (!super.doPreExecute(profileRequestContext, authenticationContext)) {
            log.trace("Leaving");
            return false;
        }

        authnRequest = authnRequestLookupStrategy.apply(profileRequestContext);
        if (authnRequest == null) {
            log.debug(
                    "{} AuthnRequest message was not returned by lookup strategy",
                    getLogPrefix());
            
            // TODO :Add StepUpEventIds.EXCEPTION to supported errors, map it
            ActionSupport.buildEvent(profileRequestContext,
                    StepUpEventIds.EXCEPTION);
            log.trace("Leaving");
            return false;
        }
        log.trace("Leaving");
        return true;
    }
    
    /**
     * Sets the list of authentication methods requiring step up.
     * 
     * 
     * @param <T>
     *            a type of principal to add, if not generic
     * @param principals
     *            supported principals to add
     */
    
    public <T extends Principal> void setInternalPrincipals(
            @Nonnull @NonnullElements final Collection<T> principals) {
        log.trace("Entering");
        ComponentSupport
                .ifInitializedThrowUnmodifiabledComponentException(this);
        Constraint
                .isNotNull(principals, "Principal collection cannot be null.");
        if (stepupPrincipals == null) {
            stepupPrincipals = new Subject();
        }
        stepupPrincipals.getPrincipals().clear();
        stepupPrincipals.getPrincipals().addAll(
                Collections2.filter(principals, Predicates.notNull()));
        log.trace("Leaving");
    }
    
   
    /** {@inheritDoc} */
    @Override
    protected void doExecute(
            @Nonnull final ProfileRequestContext profileRequestContext,
            @Nonnull final AuthenticationContext authenticationContext) {
        log.trace("Entering");
        final RequestedAuthnContext requestedCtx = authnRequest
                .getRequestedAuthnContext();
        if (requestedCtx == null || !stepupRequested(requestedCtx,stepupPrincipals)) {
            log.debug(
                    "{} AuthnRequest did not contain a RequestedAuthnContext matching any StepUp",
                    getLogPrefix());
            ActionSupport.buildEvent(profileRequestContext,
                    StepUpEventIds.EVENTID_AUTHNCONTEXT_NOT_STEPUP);
            log.trace("Leaving");
            return;
        }
        ActionSupport.buildEvent(profileRequestContext,
                StepUpEventIds.EVENTID_CONTINUE_STEPUP);
        log.trace("Leaving");
    }
    
    /**
     * Method checks if the requested authentication method is listed as step up.
     * 
     * @param requestedCtx  requested method
     * @param stepups stepup methods
     * @return true if the requested method requires step up.
     */
    private boolean stepupRequested(RequestedAuthnContext requestedCtx, Subject stepups){
        log.trace("Entering");
        for (AuthnContextClassRef authnContextClassRef : requestedCtx
                .getAuthnContextClassRefs()) {
            for (Principal matchingPrincipal : stepups
                    .getPrincipals()) {
                if (matchingPrincipal instanceof AuthnContextClassRefPrincipal
                        && authnContextClassRef
                                .getAuthnContextClassRef()
                                .equals(((AuthnContextClassRefPrincipal) matchingPrincipal)
                                        .getAuthnContextClassRef()
                                        .getAuthnContextClassRef())) {
                    log.debug("stepup requested {}",authnContextClassRef
                                .getAuthnContextClassRef());
                    log.trace("leaving");
                    return true;
                }

            }
        }
        for (AuthnContextDeclRef authnContextDeclRef : requestedCtx
                .getAuthnContextDeclRefs()) {
            for (Principal matchingPrincipal : stepups
                    .getPrincipals()) {
                if (matchingPrincipal instanceof AuthnContextDeclRefPrincipal
                        && authnContextDeclRef
                                .getAuthnContextDeclRef()
                                .equals(((AuthnContextDeclRefPrincipal) matchingPrincipal)
                                        .getAuthnContextDeclRef()
                                        .getAuthnContextDeclRef())) {
                    log.debug("stepup requested {}",authnContextDeclRef
                            .getAuthnContextDeclRef());
                    log.trace("leaving");
                    return true;
                }

            }
        }
        log.trace("Leaving");
        return false;
    }

}