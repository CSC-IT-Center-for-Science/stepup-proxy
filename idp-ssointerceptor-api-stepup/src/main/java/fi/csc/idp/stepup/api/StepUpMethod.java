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

import java.util.List;

import net.shibboleth.idp.attribute.context.AttributeContext;

/** interface for managing Step Up Methods. */
public interface StepUpMethod {
    
    /** string code for addAccount().*/
    public static final String ADD_ACCOUNT = "addaccount";

    /** string code for removeAccount().*/
    public static final String REMOVE_ACCOUNT = "removeaccount";

    /**
     * This is called before any other calls to initialize the Step Up Method
     * and possibly existing accounts.
     * 
     * @param attributeContext
     *            may be used by initialization.
     * @return true if initialization was successful.
     * @throws Exception
     *             if something unexpected occurred
     */

    public boolean initialize(AttributeContext attributeContext) throws Exception;

    /**
     * Name of the Step Up method.
     * 
     * @return name of the method.
     */
    public String getName();

    /**
     * If accounts can be added or removed.
     * 
     * @return true if accounts can be added.
     */
    public boolean isEditable();

    /**
     * Existing accounts of the method.
     * 
     * @return list of accounts
     * @throws Exception
     *             if something unexpected occurred
     */
    public List<StepUpAccount> getAccounts() throws Exception;

    /**
     * Adds a new account.
     * 
     * @return new account.
     * @throws Exception
     *             if something unexpected occurred
     */
    public StepUpAccount addAccount() throws Exception;

    /**
     * Remove a account.
     * 
     * @param account
     *            to be removed.
     */
    public void removeAccount(StepUpAccount account);
    
    /**
     * Update a account.
     * 
     * @param account
     *            to be updated.
     * @throws Exception
     *             if something unexpected occurred
     */
    public void updateAccount(StepUpAccount account) throws Exception;

}
