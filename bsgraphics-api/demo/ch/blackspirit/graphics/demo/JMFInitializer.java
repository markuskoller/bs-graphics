/*
 * Copyright 2009 Markus Koller
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
package ch.blackspirit.graphics.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.sun.media.util.Registry;

/**
 * Helps initializing JMF from a JMF.properties file not on the root of the classpath.
 * This also works for webstart!
 * @author Markus Koller
 */
public class JMFInitializer {
    public static void initJMF(URL url) throws IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            if (in == null) {
                throw new IllegalArgumentException("No such jmf properties file: " + url);
            }
             readJMFRegistry(in);
        } finally {
           if (in != null) {
               try {
                   in.close();
               } catch (IOException e) {}
           }
        }
    }

    private static void readJMFRegistry(InputStream ris) throws IOException {
    	ObjectInputStream ois = null;
    	try {
	        ois = new ObjectInputStream(ris);
	
	        int tableSize = ois.readInt();
	        int version = ois.readInt();
	        if (version > 200) {
	            System.err.println("Version number mismatch. There could be errors in reading the registry");
	        }
	        HashMap<String, Object> hash = new HashMap<String, Object>();
	        for (int i = 0; i < tableSize; i++) {
	            String key = ois.readUTF();
	            try {
	                 Object value = ois.readObject();
	                 hash.put(key, value);
	            } catch (ClassNotFoundException e) {
	                 throw new RuntimeException("Class not found for jmf registry entry", e);
	            } catch (OptionalDataException e) {
	                 throw new RuntimeException("Invalid jmf properties content", e);
	            }
	        }
	        for (Map.Entry<String, Object> entry : hash.entrySet()) {
	             Registry.set(entry.getKey(), entry.getValue());
	         }
    	} finally {
            if (ois != null) {
                try {
                	ois.close();
                } catch (IOException e) {}
            }
    	}
     }
}