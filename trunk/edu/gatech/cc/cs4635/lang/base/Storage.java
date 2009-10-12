package edu.gatech.cc.cs4635.lang.base;

import java.util.Set;


import javolution.util.FastMap;

public abstract class Storage<T extends Frame> {

	private FastMap<String, T> storage = new FastMap<String, T>();
	
	public T get(String key) {
		return storage.get(key);
	}
	
	public void add(String key, T value) {
		storage.put(key, value);
	}
	
	public T remove(String key) {
		return storage.remove(key);
	}
	
	public Set<String> entries() {
		return storage.keySet();
	}
	
	public final void debug() {
		for(String key : storage.keySet()) {
			storage.get(key).debug();
			System.out.print("\n");
		}
	}
	
	public FastMap<String, T> internal() {
		return storage;
	}

}
