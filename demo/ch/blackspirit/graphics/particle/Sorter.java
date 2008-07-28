
/*
 * Copyright 2008 Markus Koller
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
package ch.blackspirit.graphics.particle;

import java.util.ArrayList;
import java.util.Comparator;

@SuppressWarnings("unchecked")
/**
 * @author Markus Koller
 */
public class Sorter<T extends Object> {
	private ArrayList<T> src = new ArrayList<T>(10000);
	private Object[] srcArr = new Object[10000];
	
	public void sort(Object[] list, Comparator<T> comparator){
		int low = 0;
		int high = list.length;
		int off = 0;
		System.arraycopy(list, 0, srcArr, 0, list.length);
		sort(srcArr, list, low, high, off, comparator);
	}

	public void sort(Object[] list, Comparator<T> comparator, int low, int high) {
		int off = low;
		System.arraycopy(list, 0, srcArr, 0, list.length);
		sort(srcArr, list, low, high, off, comparator);
	}

	
	private void sort(Object[] src, Object[] dest, int low, int high, int off, Comparator<T> c){
		int length = high - low;

		// Insertion sort on smallest arrays
		if (length < 7) {
		    for (int i=low; i<high; i++)
			for (int j=i; j>low && c.compare((T)dest[j-1], (T)dest[j])>0; j--)
			    swap(dest, j, j-1);
		    return;
		}

        // Recursively sort halves of dest into src
        int destLow  = low;
        int destHigh = high;
        low  += off;
        high += off;
        int mid = (low + high) >>> 1;
        sort(dest, src, low, mid, -off, c);
        sort(dest, src, mid, high, -off, c);

        // If list is already sorted, just copy from src to dest.  This is an
        // optimization that results in faster sorts for nearly ordered lists.
        if (c.compare((T)src[mid-1], (T)src[mid]) <= 0) {
//           System.arraycopy(src, low, dest, destLow, length);
           for(int i = low; i < low + length - 1; i++) {
        	   dest[i] = src[i];
           }
           return;
        }

        // Merge sorted halves (now in src) into dest
        for(int i = destLow, p = low, q = mid; i < destHigh; i++) {
            if (q >= high || p < mid && c.compare((T)src[p], (T)src[q]) <= 0)
                dest[i] = src[p++];
            else
                dest[i] = src[q++];
        }
	}

	
    /**
     * Swaps x[a] with x[b].
     */
    private void swap(Object[] x, int a, int b) {
		Object t = x[a];
		x[a] = x[b];
		x[b] = t;
    }
	
	@SuppressWarnings("unchecked")
	public void sort(ArrayList<T> list, Comparator<T> comparator){
		int low = 0;
		int high = list.size();
		int off = 0;
		src.clear();
		src.addAll(list);
		sort(src, list, low, high, off, comparator);
	}
		
	private void sort(ArrayList<T> src, ArrayList<T> dest, int low, int high, int off, Comparator<T> c){
		int length = high - low;

		// Insertion sort on smallest arrays
		if (length < 7) {
		    for (int i=low; i<high; i++)
			for (int j=i; j>low && c.compare(dest.get(j-1), dest.get(j))>0; j--)
			    swap(dest, j, j-1);
		    return;
		}

        // Recursively sort halves of dest into src
        int destLow  = low;
        int destHigh = high;
        low  += off;
        high += off;
        int mid = (low + high) >>> 1;
        sort(dest, src, low, mid, -off, c);
        sort(dest, src, mid, high, -off, c);

        // If list is already sorted, just copy from src to dest.  This is an
        // optimization that results in faster sorts for nearly ordered lists.
        if (c.compare(src.get(mid-1), src.get(mid)) <= 0) {
//           System.arraycopy(src, low, dest, destLow, length);
           for(int i = low; i < low + length - 1; i++) {
        	   dest.set(i, src.get(i));
           }
           return;
        }

        // Merge sorted halves (now in src) into dest
        for(int i = destLow, p = low, q = mid; i < destHigh; i++) {
            if (q >= high || p < mid && c.compare(src.get(p), src.get(q)) <= 0)
                dest.set(i, src.get(p++));
            else
                dest.set(i, src.get(q++));
        }
	}

	
    /**
     * Swaps x[a] with x[b].
     */
    private void swap(ArrayList<T> x, int a, int b) {
		T t = x.get(a);
		x.set(a, x.get(b));
		x.set(b, t);
    }
    
    public static void main(String[] args) {
    	ArrayList<Integer> list = new ArrayList<Integer>();
    	list.add(50);
    	list.add(5);
    	list.add(100);
    	list.add(20);
    	list.add(50);
    	list.add(5);
    	list.add(100);
    	list.add(20);
     	list.add(100);
    	list.add(20);
    	list.add(50);
    	Sorter<Integer> sorter = new Sorter<Integer>();
    	sorter.sort(list, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
    	});
    	System.out.println(list);
    }

}
