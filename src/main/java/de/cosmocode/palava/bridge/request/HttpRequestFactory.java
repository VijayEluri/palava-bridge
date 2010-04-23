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

package de.cosmocode.palava.bridge.request;

import java.util.Map;

import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.core.Service;

/**
 * A factory for {@link HttpRequest}s.
 *
 * @author Willi Schoenborn
 */
public interface HttpRequestFactory extends Service {

    /**
     * Creates a new {@link HttpRequest} which will be associated with the specified
     * session and backed by the given server variable.
     * 
     * @param session the associated session
     * @param serverVariable the content of the $_SERVER variable
     * @return a new {@link HttpRequest}
     */
    HttpRequest create(HttpSession session, Map<String, String> serverVariable);
    
}
