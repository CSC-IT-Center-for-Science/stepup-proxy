/*
 * The MIT License
 * Copyright (c) 2015-2020 CSC - IT Center for Science, http://www.csc.fi
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
import javax.annotation.Nullable;

import org.opensaml.messaging.context.BaseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Context for that holds information specific to step up authentication flow.
 */
public class StepUpMethodContext extends BaseContext {

    /** Class logger. */
    @Nonnull
    private final Logger log = LoggerFactory.getLogger(StepUpMethodContext.class);

    /** Subject of the user. */
    @Nullable
    private String subject;

    /** Active step up account. */
    @Nullable
    private StepUpAccount account;

    /** Active step up method. */
    @Nullable
    private StepUpMethod method;

    /**
     * Get subject of the user.
     * 
     * @return subject of the user
     */
    @Nullable
    public String getSubject() {
        return subject;
    }

    /**
     * Set subject of the user.
     * 
     * @param sub subject of the user
     */
    public void setSubject(@Nullable String sub) {
        subject = sub;
    }

    /**
     * Get active step up method.
     * 
     * @return active method.
     */
    @Nullable
    public StepUpMethod getStepUpMethod() {
        return method;
    }

    /**
     * Set active step up method.
     * 
     * @param method active step up method.
     */
    public void setStepUpMethod(@Nullable StepUpMethod method) {
        this.method = method;
    }

    /**
     * Get active step up account.
     * 
     * @return active step up account.
     */
    @Nullable
    public StepUpAccount getStepUpAccount() {
        return account;
    }

    /**
     * Set active step up account.
     * 
     * @param account active stepup account
     */
    public void setStepUpAccount(@Nullable StepUpAccount account) {
        this.account = account;
    }

}
