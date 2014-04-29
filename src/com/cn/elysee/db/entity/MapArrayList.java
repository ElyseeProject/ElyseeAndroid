
package com.cn.elysee.db.entity;

import java.util.ArrayList;


/**
 * @author hzx
 * 2014Äê4ÔÂ18ÈÕ
 * @version V1.0
 */
public class MapArrayList<T extends Object> extends ArrayList<BaseHashMap<T>>
{
	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(BaseHashMap<T> taHashMap)
	{
		if (taHashMap != null)
		{
			return super.add(taHashMap);
		} else
		{
			return false;
		}
	}
}
