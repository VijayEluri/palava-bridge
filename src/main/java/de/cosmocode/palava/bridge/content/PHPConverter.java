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

package de.cosmocode.palava.bridge.content;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.cosmocode.json.JSONRenderer;


/**
 * convert java objects into php.
 * 
 * @author Detlef Hüttemann
 * @deprecated use {@link JSONRenderer} instead
 */
@Deprecated
public class PHPConverter extends ContentConverter {

    public void convertMap( StringBuffer buf, Map map ) throws ConversionException {
        buf.append("array(");
        Iterator<Map.Entry> iter = map.entrySet().iterator();
        boolean hasNext = iter.hasNext();
        if (!hasNext)
        {
            buf.append(")");
            return;
        }
        while( hasNext ) {
            Map.Entry me = iter.next();

            Object key = me.getKey();
            Object value = me.getValue();
            hasNext = iter.hasNext();

            convertKeyValue( buf, key.toString(), value, hasNext ? KeyValueState.INSIDE : KeyValueState.LAST ) ;
        }
    }

    public void convertKeyValue( StringBuffer buf, String key, Object value, KeyValueState state ) throws ConversionException {
        if ( state == KeyValueState.ZERO) {
            buf.append("array()");
            return;
        }
        else if ( state == KeyValueState.SINGLE || state == KeyValueState.START ) buf.append("array(");

    convert(buf, key);
        buf.append("=>");
        convert(buf, value);

        if ( state != KeyValueState.LAST ) buf.append(",");
        if ( state == KeyValueState.LAST || state == KeyValueState.SINGLE) buf.append(")");
    }


    public void convertNull( StringBuffer buf ) throws ConversionException {
        buf.append("null");
    }

    public void convertString( StringBuffer buf, String object ) throws ConversionException {
        String buffer = object.toString().replace("\\", "\\\\");
        buffer = buffer.replace("'", "\\'");
        buf.append("'").append( buffer ).append("'");
    }

    public void convertDate( StringBuffer buf, java.util.Date object ) throws ConversionException {
        // java timestamp:  number of milliseconds since January 1, 1970, 00:00:00 GMT
        // php timestamp:   number of seconds since January 1, 1970, 00:00:00 GMT
        buf.append( (object.getTime() / 1000) );
    }

    public static Date getJavaDate(String phpdate)
    {
        if (phpdate == null) return null;
        long phptime = Long.parseLong(phpdate);
        return new Date(phptime * 1000);
    }

    public void convertNumber( StringBuffer buf, Number object ) throws ConversionException {
        buf.append( object );
    }

    public void convertList( StringBuffer buf, List object ) throws ConversionException {
        buf.append("array(");
        Iterator iter = object.iterator();
        boolean hasNext = iter.hasNext();
        while( hasNext ) {
            Object key = iter.next();
            convert( buf, key ) ;
            hasNext = iter.hasNext();
            if ( hasNext ) 
                buf.append(",");
        }
        buf.append(")");
    }

    public void convertDefault( StringBuffer buf, Object object ) throws ConversionException {
        convertString( buf, object.toString() );
    }

}
