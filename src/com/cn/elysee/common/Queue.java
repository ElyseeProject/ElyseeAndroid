package com.cn.elysee.common;

import java.util.Collection;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public interface Queue<E> extends Collection<E>
{
	@Override
	boolean add(E e);

	boolean offer(E e);

	E remove();

	E poll();

	E element();

	E peek();
}
