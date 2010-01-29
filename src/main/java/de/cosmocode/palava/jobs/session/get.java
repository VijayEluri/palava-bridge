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

package de.cosmocode.palava.jobs.session;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.call.Arguments;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Command;
import de.cosmocode.palava.bridge.command.CommandException;
import de.cosmocode.palava.bridge.content.ConversionException;
import de.cosmocode.palava.bridge.content.PhpContent;
import de.cosmocode.palava.bridge.session.HttpSession;

/**
 * get a session data entry
 * @author Detlef Hüttemann
 */
@Singleton
public class get implements Command {
    
    @Override
    public Content execute(Call call) throws CommandException {
        final Arguments arguments = call.getArguments();
        arguments.require("key");
        
        final Object key = arguments.get("key");
        final HttpSession session = call.getHttpRequest().getHttpSession();
        Preconditions.checkNotNull(session, "Session");
        
        final Object value = session.get(key);
        
        try {
            return new PhpContent(value);
        } catch (ConversionException e) {
            throw new CommandException(e);
        }
    }

}