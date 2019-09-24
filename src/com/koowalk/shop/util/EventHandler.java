package com.koowalk.shop.util;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class EventHandler {
	private List<Consumer<? super Event>> consumers;
	private List<Function<? super Event, Boolean>> filters;
	
	private Object peg;
	
	public EventHandler() {
		consumers = new LinkedList<>();
		filters = new LinkedList<>();
	}
		
	public void handleEvent(Event event) {
		if ((peg == null || event.checkPeg(peg)) && checkFilters(event))
			consumers.forEach(c -> c.accept(event));
	}
	
	private boolean checkFilters(Event e) {
		for (Function<? super Event, Boolean> filter : filters)
			if (!filter.apply(e))
				return false;
		
		return true;
	}
	
	public EventHandler call(Consumer<? super Event> consumer) {
		consumers.add(consumer);
		return this;
	}
	
	public EventHandler filter(Function<? super Event, Boolean> filter) {
		filters.add(filter);
		return this;
	}
	
	public EventHandler peg(Object o) {
		peg = o;
		return this;
	}
}
