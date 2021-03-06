/*
 * The MIT License
 * Copyright (c) 2020 CSC - IT Center for Science, http://www.csc.fi
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

package fi.csc.idp.stepup.api.decoding.impl;

import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.decoder.MessageDecodingException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import fi.csc.idp.stepup.api.messaging.ApiRequest;

public class ApiRequestDecoderTest {

    private MockHttpServletRequest httpRequest;

    private ApiRequestDecoder decoder;

    @BeforeMethod
    protected void setUp() throws Exception {
        httpRequest = new MockHttpServletRequest();
        httpRequest.addParameter("token", "sometoken");
        httpRequest.addParameter("userid", "randomuser");
        decoder = new ApiRequestDecoder();
        decoder.setHttpServletRequest(httpRequest);
        decoder.initialize();
    }

    @Test
    public void testRequestDecoding() throws MessageDecodingException {
        decoder.decode();
        final MessageContext messageContext = decoder.getMessageContext();
        // We are not testing the message implementation here. It is enough to test the
        // decoding was successful
        Assert.assertNotNull(((ApiRequest) messageContext.getMessage()).getRequestParameterMap());
    }

    @Test(expectedExceptions = MessageDecodingException.class)
    public void testInvalidRequestDecoding() throws MessageDecodingException {
        httpRequest.addParameter("token", "anothertoken");
        decoder.decode();
    }

}