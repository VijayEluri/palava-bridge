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

package de.cosmocode.palava.bridge.scope;

import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.SessionScoped;

import de.cosmocode.palava.bridge.call.filter.definition.Matchers;
import de.cosmocode.palava.bridge.inject.AbstractApplication;
import de.cosmocode.palava.bridge.request.RequestFilter;

/**
 * A {@link Module} for the {@link de.cosmocode.palava.bridge.scope} package.
 *
 * @author Willi Schoenborn
 */
public final class ScopeModule extends AbstractApplication {

    @Override
    protected void configureApplication() {
        final CallScope callScope = new CallScope();
        binder().requestInjection(callScope);
        binder().bindScope(CallScoped.class, callScope);
        binder().bind(CallScope.class).toInstance(callScope);
        
        filter(Matchers.any()).through(CallScopeFilter.class);
        
        final RequestScope requestScope = new RequestScope();
        binder().requestInjection(requestScope);
        binder().bindScope(RequestScoped.class, requestScope);
        binder().bind(RequestScope.class).toInstance(requestScope);
        
        Multibinder.newSetBinder(binder(), RequestFilter.class).addBinding().to(RequestScopeFilter.class);
        
        final SessionScope sessionScope = new SessionScope();
        binder().requestInjection(requestScope);
        binder().bindScope(SessionScoped.class, sessionScope);
        binder().bind(SessionScope.class).toInstance(sessionScope);
    }
    
}
