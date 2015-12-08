package org.jasig.cas.util;

import java.util.Enumeration;
import java.util.Iterator;

public class Itermeration<T> implements Enumeration<T> {

	private Iterator<T> ite;

	public Itermeration(Iterator<T> it) {
		this.ite = it;
	}

	@Override
	public boolean hasMoreElements() {
		// TODO Auto-generated method stub
		return ite.hasNext();
	}

	@Override
	public T nextElement() {
		// TODO Auto-generated method stub
		return ite.next();
	}

}
