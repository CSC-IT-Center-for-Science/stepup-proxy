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
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.csc.idp.stepup.api.SharedSecretStorage;

/** class implementing challenge sending by writing it to log. */
public class SQLSharedSecretStorage implements SharedSecretStorage {

    /** Class logger. */
    @Nonnull
    private final Logger log = LoggerFactory.getLogger(SQLSharedSecretStorage.class);

    private static DataSource datasource;

    private static String jdbcUrl;
    private static String userName;
    private static String password;
    private static int poolSize;
    private static String insertStatement;

    public void setJdbcUrl(String jdbcUrl) {
        SQLSharedSecretStorage.jdbcUrl = jdbcUrl;
    }

    public void setPassword(String password) {
        SQLSharedSecretStorage.password = password;
    }

    public void setUserName(String userName) {
        SQLSharedSecretStorage.userName = userName;
    }

    public void setPoolSize(int poolSize) {
        SQLSharedSecretStorage.poolSize = poolSize;
    }

    public void setInsertStatement(String insertStatement) {
        SQLSharedSecretStorage.insertStatement = insertStatement;
    }

    public static DataSource getDataSource() {

        if (datasource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(userName);
            config.setPassword(password);
            config.setMaximumPoolSize(poolSize);
            config.setAutoCommit(true);
            datasource = new HikariDataSource(config);
        }
        return datasource;
    }

    @Override
    public void store(String secret, String id) throws Exception {
        log.trace("Entering");
        log.debug("Storing secret " + secret + " by key " + id);
        /**
         * To this point we setup a encrypter
         */
        //TODO: check SQL syntax to be correct
        //TODO: Use prepared statement
        getDataSource().getConnection().createStatement().executeUpdate(String.format(insertStatement, id, secret));
        log.trace("Leaving");
    }

}
