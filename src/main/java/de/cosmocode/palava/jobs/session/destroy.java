/**
 * Copyright 2010 CosmoCode GmbH
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

package de.cosmocode.palava.jobs.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Command;
import de.cosmocode.palava.bridge.command.CommandException;
import de.cosmocode.palava.bridge.content.PhpContent;
import de.cosmocode.palava.bridge.session.HttpSession;

/**
 * Deletes the actual session.
 * 
 * @author Tobias Sarnowski
 */
@Singleton
public class destroy implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(destroy.class);
    
    @Override
    public Content execute(Call call) throws CommandException {
        final HttpSession session = call.getHttpRequest().getHttpSession();
        if (session == null) {
            LOG.warn("No session found, cant destroy.");
        } else {
            LOG.debug("Destroying sessiong {}", session);
            session.clear();
        }
        return PhpContent.OK;
    }
    
}
