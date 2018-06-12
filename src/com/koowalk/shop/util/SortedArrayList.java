package com.koowalk.shop.util;

import java.util.ArrayList;
import java.util.Collections;

public class SortedArrayList<T extends Comparable> extends ArrayList<T> {
	public void addSorted(T item) {		
		for (int i = 0; i < size(); i++) {
			if (item.compareTo(get(i)) <= 0) {
				add(i, item);
				return;
			}
		}
		
		add(item);
	}
	
	// Decided to not use the java one because it would require an extra comparison to decide if it was or was not in the list
	public int findIndex(T thing) {
		int l = 0;
		int r = size() - 1;
		int middle = r / 2;
		
		while (l <= r) {			
			int comparison = get(middle).compareTo(thing);
			if (comparison < 0) {
				l = middle + 1;
			} else if (comparison > 0) {
				r = middle - 1;
			} else {
				return middle;
			}
			
			middle = (l + r) / 2;
		}
		
		return -1;
	}
	
	public T find(T thing) {
		try {
			return get(findIndex(thing));
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public void sort() {
		Collections.sort(this);
	}
	
	public void reverse() {
		Collections.reverse(this);
	}
}
