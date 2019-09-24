package com.koowalk.shop.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventDispatcher {
	Map<String, List<EventHandler>> handlers;
	List<Pair<String, Event>> queue;
	
	public EventDispatcher() {
		handlers = new HashMap<>();
		queue = new LinkedList<>();
	}
	
	public EventHandler registerHandler(String channel) {
		EventHandler handler = new EventHandler();
		getChannel(channel).add(handler);
		return handler;
	}
	
	public void queueEvent(String channel, Event event) {
		queue.add(new Pair<String, Event>(channel, event));
	}
	
	public void update() {
		while (queue.size() > 0) {
			Pair<String, Event> pair = queue.remove(0);
			broadcastEvent(pair.getP0(), pair.getP1());
		}
	}
	
	private void broadcastEvent(String channel, Event event) {
		getChannel(channel).forEach(h -> h.handleEvent(event));
	}
	
	private List<EventHandler> getChannel(String name) {
		if (handlers.containsKey(name.toLowerCase())) {
			return handlers.get(name.toLowerCase());
		} else {
			List<EventHandler> list = new LinkedList<>();
			handlers.put(name.toLowerCase(), list);
			return list;
		}
	}
}
