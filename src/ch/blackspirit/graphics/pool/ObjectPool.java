/*
 * Copyright 2008-2009 Markus Koller
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
package ch.blackspirit.graphics.pool;

/**
 * Object pool based on an array.
 * @param <T> Object to pool.
 * @author Markus Koller
 */
public class ObjectPool<T extends java.lang.Object> {
	private Object[] cachedObjects;
	private T prototype;
	int index;
	
	@SuppressWarnings("unchecked")
	public ObjectPool(T prototype, int initialSize) {
		super();
		this.prototype = prototype;
		cachedObjects = new Object[initialSize * 2];
		for(int i = 0; i < initialSize; i++) {
			try {
				cachedObjects[i] = ((T)prototype.getClass().newInstance());
			} catch (java.lang.Exception e) {
				throw new IllegalArgumentException("Cached object must have empty constructor!", e);
			}
		}
		index = initialSize;
	}

	@SuppressWarnings("unchecked")
	public T get() {
		index--;
		if(index >= 0) {
			return (T)cachedObjects[index];
		} else {
			index++;
			try {
				return (T)prototype.getClass().newInstance();
			} catch (java.lang.Exception e) {
				throw new IllegalArgumentException("Cached object must have empty constructor!", e);
			}
		}
	}
	
	public void free(T object) {
		if(index >= cachedObjects.length) {
			// resize the object array
			Object[] newArray = new Object[cachedObjects.length * 2];
			System.arraycopy(cachedObjects, 0, newArray, 0, cachedObjects.length);
			cachedObjects = newArray;
		}
		cachedObjects[index] = object;
   		index++;
	}
}
