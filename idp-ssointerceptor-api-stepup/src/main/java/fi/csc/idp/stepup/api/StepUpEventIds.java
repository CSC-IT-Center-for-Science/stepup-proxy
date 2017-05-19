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

package fi.csc.idp.stepup.api;

import javax.annotation.Nonnull;

import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;

/**
 * Constants to use for error results related to step up interceptor flow.
 */
public final class StepUpEventIds {

    /** Generic ID for exception thrown. */
    @Nonnull
    @NotEmpty
    public static final String EXCEPTION = "StepUpException";

    /** event id for case of missing or invalid relying party context. */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_INVALID_RPCONTEXT = "InvalidRPContext";

    /** event id for case of missing verifier implementation. */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_MISSING_VERIFIERIMPL = "MissingVerifierImpl";

    /** event id for case of missing sender implementation. */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_MISSING_SENDERIMPL = "MissingSenderImpl";

    /** event id for case of missing generator implementation. */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_MISSING_GENERATORIMPL = "MissingGeneratorImpl";

    /** event id for case of missing attribute context. */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_MISSING_ATTRIBUTECONTEXT = "MissingAttributeContext";

    /** event id for case of missing shibboleth sp context. */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_MISSING_SHIBSPCONTEXT = "MissingShibSPContext";

    /** event id for case of missing stepup context. */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_MISSING_STEPUPMETHODCONTEXT = "MissingStepUpMethodContext";

    /** event id for case of invalid shibboleth sp context. */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_INVALID_SHIBSPCONTEXT = "InvalidShibSPContext";

    /**
     * event id for case of requested authentication context that is not step
     * up.
     */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_AUTHNCONTEXT_NOT_STEPUP = "AuthnContextNotStepUp";

    /**
     * event id for case of provider already offering step up level up.
     */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_AUTHNCONTEXT_STEPUP = "AuthnContextStepUp";

    /** event id for the case of continuing with stepup. */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_CONTINUE_STEPUP = "ContinueStepUp";

    /** event id for case of user not able to perform step up. */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_INVALID_USER = "InvalidUser";

    /** event id for case of user not able to perform step up. */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_INVALID_RESPONSE = "StepUpWrongResponse";

    /** event id for case of user reaching retry limit for challenge response. */
    @Nonnull
    @NotEmpty
    public static final String EVENTID_RESPONSE_LIMIT = "StepUpLimitReached";

    /** private constructor to prohibit use of it. */
    private StepUpEventIds() {

    };

}
