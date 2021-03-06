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

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.csc.idp.stepup.api.ChallengeSender;

/**
 * Class implementing Step Up Account of type Challenge Sender. This includes
 * types like SMS, Email etc.
 */
public class ChallengeSenderStepUpAccount extends AbstractStepUpAccount {

    /** Class logger. */
    @Nonnull
    private final Logger log = LoggerFactory.getLogger(ChallengeSenderStepUpAccount.class);

    /** Challenge Sender implementation. */
    private ChallengeSender challengeSender;

    /**
     * Set the Challenge Sender implementation.
     * 
     * @param sender
     *            Challenge Sender implementation
     */
    public void setChallengeSender(ChallengeSender sender) {
        this.challengeSender = sender;
    }

    /**
     * Send Challenge using the configured implementation.
     * 
     * @throws Exception
     *             if something unexpected occurred.
     */
    @Override
    public void doSendChallenge() throws Exception {
        if (challengeSender == null) {
            throw new Exception("Bean not configured with ChallengeSender");
        }
        challengeSender.send(getChallenge(), getTarget());

    }

}
