/*
 * Copyright 2012 Manuel Schulze <manuel_schulze@i-entwicklung.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.iew.web.isc;

import org.springframework.ui.ExtendedModelMap;

/**
 * Abstrakte Basisklasse für DSResponse-Implementierungen für Smartclient.
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 21.11.12 - 23:42
 */
public abstract class DSResponseAbstract extends ExtendedModelMap {
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_CONNECTION_RESET_ERROR">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_CONNECTION_RESET_ERROR</a>
     */
    public static final int STATUS_CONNECTION_RESET_ERROR = -92;
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_FAILURE">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_FAILURE</a>
     */
    public static final int STATUS_FAILURE = -1;
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_LOGIN_INCORRECT">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_LOGIN_INCORRECT</a>
     */
    public static final int STATUS_LOGIN_INCORRECT = -5;
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_LOGIN_REQUIRED">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_LOGIN_REQUIRED</a>
     */
    public static final int STATUS_LOGIN_REQUIRED = -7;
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_LOGIN_SUCCESS">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_LOGIN_SUCCESS</a>
     */
    public static final int STATUS_LOGIN_SUCCESS = -8;
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_MAX_LOGIN_ATTEMPTS_EXCEEDED">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_MAX_LOGIN_ATTEMPTS_EXCEEDED</a>
     */
    public static final int STATUS_MAX_LOGIN_ATTEMPTS_EXCEEDED = -6;
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_OFFLINE">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_OFFLINE</a>
     */
    public static final int STATUS_OFFLINE = 1;
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_SERVER_TIMEOUT">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_SERVER_TIMEOUT</a>
     */
    public static final int STATUS_SERVER_TIMEOUT = -100;
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_SUCCESS">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_SUCCESS</a>
     */
    public static final int STATUS_SUCCESS = 0;
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_TRANSACTION_FAILED">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_TRANSACTION_FAILED</a>
     */
    public static final int STATUS_TRANSACTION_FAILED = -10;
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_TRANSPORT_ERROR">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_TRANSPORT_ERROR</a>
     */
    public static final int STATUS_TRANSPORT_ERROR = -90;
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_UNKNOWN_HOST_ERROR">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_UNKNOWN_HOST_ERROR</a>
     */
    public static final int STATUS_UNKNOWN_HOST_ERROR = -91;
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_UPDATE_WITHOUT_PK_ERROR">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_UPDATE_WITHOUT_PK_ERROR</a>
     */
    public static final int STATUS_UPDATE_WITHOUT_PK_ERROR = -9;
    /**
     * @see <a href="http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_VALIDATION_ERROR">http://www.smartclient.com/docs/8.2/a/b/c/go.html#classAttr..RPCResponse.STATUS_VALIDATION_ERROR</a>
     */
    public static final int STATUS_VALIDATION_ERROR = -4;

    public int getStatus() {
        return (Integer) getResponseModel().get("status");
    }

    public void setStatus(int status) {
        getResponseModel().put("status", status);
    }

    protected ExtendedModelMap getResponseModel() {
        if (!containsKey("response")) {
            put("response", new ExtendedModelMap());
        }
        return (ExtendedModelMap) get("response");
    }

}
