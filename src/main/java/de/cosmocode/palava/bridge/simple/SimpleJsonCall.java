/**
 * palava - a java-php-bridge
 * Copyright (C) 2007-2010  CosmoCode GmbH
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.cosmocode.palava.bridge.simple;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import de.cosmocode.collections.utility.AbstractUtilityMap;
import de.cosmocode.collections.utility.UtilityMap;
import de.cosmocode.collections.utility.UtilitySet;
import de.cosmocode.json.JSON;
import de.cosmocode.palava.bridge.ConnectionLostException;
import de.cosmocode.palava.bridge.Header;
import de.cosmocode.palava.bridge.call.Arguments;
import de.cosmocode.palava.bridge.call.JsonCall;
import de.cosmocode.palava.bridge.call.MissingArgumentException;
import de.cosmocode.palava.bridge.command.Command;
import de.cosmocode.palava.bridge.request.HttpRequest;

/**
 * parse the request content as a json object.
 * 
 * @deprecated 
 * 
 * @author Detlef Huettemann
 */
@Deprecated
class SimpleJsonCall extends SimpleTextCall implements JsonCall {
    
    private JSONObject json;
    private UtilityMap<String, Object> map;
    private Arguments arguments;

    SimpleJsonCall(HttpRequest request, Command command, Header header, InputStream stream) {
        super(request, command, header, stream);
    }

    public final JSONObject getJSONObject() throws ConnectionLostException, JSONException {
        if (json == null) {
            json = new JSONObject(getText());
        }
        return json;
    }
    
    @Deprecated
    public final <K, V> UtilityMap<K, V> getArgs() {
        if (arguments == null) {
            arguments = new InternalArguments();
        }
        return (UtilityMap<K, V>) arguments;
    }
    
    public final Arguments getArguments() {
        if (arguments == null) {
            arguments = new InternalArguments();
        }
        return arguments;
    }
    
    private class InternalArguments extends AbstractUtilityMap<String, Object> implements Arguments {
        
        @Override
        public void require(String... keys) throws MissingArgumentException {
            final Set<String> keySet = keySet();
            for (String key : keys) {
                if (keySet.contains(key)) continue;
                throw new MissingArgumentException(key);
            }                
        }
        
        private void lazyLoad() {
            if (json == null) {
                try {
                    json = getJSONObject();
                } catch (JSONException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            if (map == null) {
                map = JSON.asMap(json);
            }
        }
        
        @Override
        public UtilitySet<Map.Entry<String, Object>> entrySet() {
            lazyLoad();
            return map.entrySet();
        }
        
        @Override
        public Object put(String key, Object value) {
            lazyLoad();
            try {
                return json.put(key, value);
            } catch (JSONException e) {
                throw new IllegalArgumentException(e);
            }
        }
        
    }

    public final Map<String, String> getStringedArguments() throws ConnectionLostException {
        if (!getText().startsWith("{")) {
            // dirty hack to ignore everything that is not a JsonObject
            return new LinkedHashMap<String, String>();
        } else {
            final Map<String, String> stringed = new LinkedHashMap<String, String>();
            for (final Map.Entry<String, Object> entry : getArguments().entrySet()) {
                final Object value = entry.getValue();
                stringed.put(entry.getKey(), value == null ? null : value.toString());
            }
            return stringed;
        }
    }

}