package fi.csc.idp.stepup.impl;

import java.util.Map;

import net.shibboleth.idp.profile.RequestContextBuilder;
import net.shibboleth.idp.profile.context.RelyingPartyContext;
import net.shibboleth.idp.profile.context.navigate.WebflowRequestContextProfileRequestContextLookup;
import org.opensaml.profile.context.ProfileRequestContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import fi.csc.idp.stepup.api.OidcStepUpContext;

public class TestInitializeRelyingPartyContext {

    private InitializeRelyingPartyContext action;

    protected RequestContext src;
    @SuppressWarnings("rawtypes")
    protected ProfileRequestContext prc;
    protected OidcStepUpContext oidcCtx;
    protected Map<String, String> claimToAttributeMap;

    @BeforeMethod
    public void setUp() throws Exception {
        AuthenticationRequest req = AuthenticationRequest
                .parse("max_age=0&response_type=code&client_id=s6BhdRkqt3&login_hint=foo&redirect_uri=https%3A%2F%2Fclient.example.org%2Fcb&scope=openid%20profile&state=af0ifjsldkj&nonce=n-0S6_WzA2Mj");
        src = new RequestContextBuilder().buildRequestContext();
        prc = new WebflowRequestContextProfileRequestContextLookup().apply(src);
        oidcCtx = new OidcStepUpContext();
        prc.addSubcontext(oidcCtx);
        action = new InitializeRelyingPartyContext();
        oidcCtx.setRequest(req);
        oidcCtx.setRedirectUriValidated(true);
        action.initialize();

    }

    /**
     * Test that action is able to build relying party context
     * 
     * @throws Exception
     */
    @Test
    public void testSuccess() throws Exception {

        final Event event = action.execute(src);
        Assert.assertNull(event);
        RelyingPartyContext ctx = prc.getSubcontext(RelyingPartyContext.class, false);
        Assert.assertNotNull(ctx);
        Assert.assertTrue(ctx.isVerified());
        Assert.assertEquals(ctx.getRelyingPartyId(), "s6BhdRkqt3");

    }

}
