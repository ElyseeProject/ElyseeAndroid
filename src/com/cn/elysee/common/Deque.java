
package com.cn.elysee.common;

import java.util.Iterator;


/**
 * @author hzx
 * 2014Äê4ÔÂ21ÈÕ
 * @version V1.0
 */
public interface Deque<E> extends Queue<E>
{

	void addFirst(E e);
	
	void addLast(E e);
	
	boolean offerFirst(E e);
	
	boolean offerLast(E e);
	
	E removeFirst();
	
	E removeLast();
	
	E pollFirst();
	
	E pollLast();
	
	E getFirst();
	
	E getLast();
	
	E peekFirst();
	
	E peekLast();
	
	boolean removeFirstOccurrence(Object o);
	
	boolean removeLastOccurrence(Object o);
	
	@Override
	boolean add(E e);
	
	@Override
	boolean offer(E e);
	
	@Override
	E remove();
	
	@Override
	E poll();
	
	@Override
	E element();
	
	@Override
	E peek();
	
	void push(E e);
	
	E pop();
	
	@Override
	boolean remove(Object o);
	
	@Override
	boolean contains(Object o);
	
	@Override
	public int size();
	
	@Override
	Iterator<E> iterator();
	
	Iterator<E> descendingIterator();
}
