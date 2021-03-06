/*
 * Copyright (c) 2016, LinkedKeeper
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * - Neither the name of LinkedKeeper nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.linkedkeeper.tcp.connector.api;

import com.linkedkeeper.tcp.connector.Connector;
import com.linkedkeeper.tcp.connector.Session;
import com.linkedkeeper.tcp.connector.SessionManager;
import com.linkedkeeper.tcp.exception.DispatchException;
import com.linkedkeeper.tcp.exception.PushException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by frank@linkedkeeper.com on 17/1/10.
 */
public abstract class ExchangeConnector<T> implements Connector<T> {

    private final static Logger logger = LoggerFactory.getLogger(ExchangeConnector.class);

    public void send(SessionManager sessionManager, String sessionId, T message) throws Exception {
        Session session = sessionManager.getSession(sessionId);
        if (session == null) {
            throw new Exception(String.format("session %s no exist.", sessionId));
        }
        try {
            session.getConnection().send(message);
            session.access();
        } catch (PushException e) {
            logger.error("ExchangeConnector send occur PushException.", e);
            session.close();
            throw new DispatchException(e);
        } catch (Exception e) {
            logger.error("ExchangeConnector send occur Exception.", e);
            session.close();
            throw new DispatchException(e);
        }
    }
}
