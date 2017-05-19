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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.csc.idp.stepup.api.StepUpAccount;
import fi.csc.idp.stepup.api.StepUpAccountStorage;

import org.springframework.security.crypto.encrypt.TextEncryptor;

/** SQL implementation of Step Up Account storage. */
public class SQLStepUpAccountStorage implements StepUpAccountStorage {

    /** Class logger. */
    @Nonnull
    private final Logger log = LoggerFactory.getLogger(SQLStepUpAccountStorage.class);
    /** datasource constructed. */
    private DataSource datasource;
    /** statement for adding items. */
    private String addStatement;
    /** statement for updating items. */
    private String updateStatement;
    /** statement for removing items. */
    private String removeStatement;
    /** statement for listing items. */
    private String listStatement;
    /** encryptor for the fields. */
    private TextEncryptor encryptor;
    /** if name parameter should be encrypted. */
    private boolean encryptName;
    /** if target parameter should be encrypted. */
    private boolean encryptTarget;
    /** if key parameter should be encrypted. */
    private boolean encryptKey;

    /**
     * Setter for account field cryptor.
     * 
     * @param cryptor
     *            TextEncryptor
     */
    public void setEncryptor(TextEncryptor cryptor) {
        this.encryptor = cryptor;
    }

    /**
     * Setter for name encryption option.
     * 
     * @param encrypt
     *            parameter or not
     */
    public void setEncryptName(boolean encrypt) {
        this.encryptName = encrypt;
    }

    /**
     * Setter for target encryption option.
     * 
     * @param encrypt
     *            parameter or not
     */
    public void setEncryptTarget(boolean encrypt) {
        this.encryptTarget = encrypt;
    }

    /**
     * Setter for key encryption option.
     * 
     * @param encrypt
     *            parameter or not
     */
    public void setEncryptKey(boolean encrypt) {
        this.encryptKey = encrypt;
    }

    /**
     * Setter for add statement.
     * 
     * @param statement
     *            add statement.
     */
    public void setAddStatement(String statement) {

        this.addStatement = statement;
    }

    /**
     * Setter for update statement.
     * 
     * @param statement
     *            update statement.
     */
    public void setUpdateStatement(String statement) {

        this.updateStatement = statement;
    }

    /**
     * Setter for remove statement.
     * 
     * @param statement
     *            remove statement.
     */
    public void setRemoveStatement(String statement) {

        this.removeStatement = statement;
    }

    /**
     * Setter for list statement.
     * 
     * @param statement
     *            list statement.
     */
    public void setListStatement(String statement) {

        this.listStatement = statement;
    }

    /**
     * Set the datasource.
     * 
     * @param source
     *            datasource
     */
    public void setDataSource(DataSource source) {

        this.datasource = source;

    }

    /**
     * Get the datasource.
     * 
     * @return datasource
     * @throws Exception
     *             if datasource has not been set
     */
    private DataSource getDataSource() throws Exception {

        if (datasource == null) {
            throw new Exception("Datasource must be set");
        }

        return datasource;
    }

    /**
     * Encrypts parameter with encryptor.
     * 
     * @param parameter
     *            to be encrypted
     * @return encrypted parameter
     * @throws Exception
     *             if something unexpected occurs
     */
    private String encrypt(String parameter) throws Exception {

        if (encryptor == null) {

            throw new Exception("Encryptor not set");
        }
        if (parameter == null) {

            return null;
        }
        String result = encryptor.encrypt(parameter);
        log.debug("Encrypt({})={}", parameter, result);

        return result;
    }

    /**
     * Decrypts parameter with encryptor.
     * 
     * @param parameter
     *            to be decrypted
     * @return decrypted parameter
     * @throws Exception
     *             if something unexpected occurs
     */
    private String decrypt(String parameter) throws Exception {

        if (encryptor == null) {

            throw new Exception("Encryptor not set");
        }
        if (parameter == null) {

            return null;
        }
        String result = encryptor.decrypt(parameter);
        log.debug("Decrypt({})={}", parameter, result);

        return result;
    }

    /**
     * Encrypts key if needed.
     * 
     * @param key
     *            to be encrypted.
     * @return encrypted key
     * @throws Exception
     *             if something unexpected occurs
     */
    private String encryptKey(String key) throws Exception {

        if (!encryptKey) {

            return key;
        }

        return encrypt(key);
    }

    /**
     * Encrypts name if needed.
     * 
     * @param name
     *            to be encrypted.
     * @return encrypted name
     * @throws Exception
     *             if something unexpected occurs
     */
    private String encryptName(String name) throws Exception {

        if (!encryptName) {

            return name;
        }

        return encrypt(name);
    }

    /**
     * Decrypts name if needed.
     * 
     * @param name
     *            to be decrypted.
     * @return decrypted name
     * @throws Exception
     *             if something unexpected occurs
     */
    private String decryptName(String name) throws Exception {

        if (!encryptName) {

            return name;
        }

        return decrypt(name);
    }

    /**
     * Encrypts target if needed.
     * 
     * @param target
     *            to be encrypted.
     * @return encrypted target
     * @throws Exception
     *             if something unexpected occurs
     */
    private String encryptTarget(String target) throws Exception {

        if (!encryptTarget) {

            return target;
        }

        return encrypt(target);
    }

    /**
     * Decrypts target if needed.
     * 
     * @param target
     *            to be decrypted.
     * @return decrypted target
     * @throws Exception
     *             if something unexpected occurs
     */
    private String decryptTarget(String target) throws Exception {

        if (!encryptTarget) {

            return target;
        }

        return decrypt(target);
    }

    @Override
    public void add(StepUpAccount account, String key) throws Exception {

        Connection conn = getDataSource().getConnection();
        PreparedStatement add = conn.prepareStatement(addStatement);
        try {
            add.setString(1, encryptName(account.getName()));
            add.setBoolean(2, account.isEnabled());
            add.setBoolean(3, account.isEditable());
            add.setString(4, encryptTarget(account.getTarget()));
            add.setString(5, encryptKey(key));
            add.executeUpdate();
            conn.commit();
        } finally {
            add.close();
            conn.close();
        }

    }

    @Override
    public void remove(StepUpAccount account, String key) throws Exception {

        Connection conn = getDataSource().getConnection();
        PreparedStatement remove = conn.prepareStatement(removeStatement);
        try {
            remove.setLong(1, account.getId());
            remove.executeUpdate();
            conn.commit();
        } finally {
            remove.close();
            conn.close();
        }

    }

    @Override
    public void update(StepUpAccount account, String key) throws Exception {

        Connection conn = getDataSource().getConnection();
        PreparedStatement update = conn.prepareStatement(updateStatement);
        try {
            update.setString(1, encryptName(account.getName()));
            update.setBoolean(2, account.isEnabled());
            update.setBoolean(3, account.isEditable());
            update.setString(4, encryptTarget(account.getTarget()));
            update.setString(5, encryptKey(key));
            update.setLong(6, account.getId());
            update.executeUpdate();
        } finally {
            conn.commit();
            update.close();
            conn.close();
        }

    }

    @Override
    public <T> List<StepUpAccount> getAccounts(String key, Class<T> aClass) throws Exception {

        log.debug("About to read accounts for {}", key);
        Connection conn = getDataSource().getConnection();
        List<StepUpAccount> accounts = new ArrayList<StepUpAccount>();
        PreparedStatement list = conn.prepareStatement(listStatement);
        try {
            list.setString(1, encryptKey(key));
            ResultSet rs = list.executeQuery();
            while (rs.next()) {
                Object obj = aClass.newInstance();
                if (!(obj instanceof StepUpAccount)) {
                    conn.close();
                    throw new Exception("Unable to instantiate StepUpAccount");
                }
                StepUpAccount stepUpAccount = (StepUpAccount) obj;
                stepUpAccount.setId(rs.getInt("id"));
                stepUpAccount.setName(decryptName(rs.getString("name")));
                stepUpAccount.setEnabled(rs.getBoolean("enabled"));
                stepUpAccount.setTarget(decryptTarget(rs.getString("target")));
                // This is set last
                stepUpAccount.setEditable(rs.getBoolean("editable"));
                accounts.add(stepUpAccount);
            }

            rs.close();
        } finally {
            list.close();
            conn.close();
        }
        return accounts;
    }

}
