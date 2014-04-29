package com.cn.elysee.util.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.cn.elysee.util.cache.CallBackHandler;

/**
 * @author hzx 
 * 2014年4月18日
 * @version V1.0
 */
public class BitmapCallBackHanlder extends CallBackHandler<ImageView>
{
	private Bitmap mLoadingBitmap;

	@Override
	public void onStart(ImageView t, Object data)
	{
		super.onStart(t, data);
		onSuccess(t, data, null);
	}

	@Override
	public void onSuccess(ImageView imageView, Object data, byte[] buffer)
	{
		super.onSuccess(imageView, data, buffer);
		if (buffer != null && imageView != null)
		{
			Bitmap bitmap = null;
			try
			{
				if (buffer != null)
				{
					bitmap = BitmapFactory.decodeByteArray(buffer, 0,
							buffer.length);
				}
				setImageBitmap(imageView, bitmap);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			if (mLoadingBitmap != null)
			{
				setImageBitmap(imageView, mLoadingBitmap);
			}
		}
	}

	@Override
	public void onFailure(ImageView t, Object data)
	{
		super.onFailure(t, data);
	}

	/**
	 * 设置默认的加载图片
	 * 
	 * @param defaultBitmap
	 */
	public void setLoadingImage(Bitmap bitmap)
	{
		this.mLoadingBitmap = bitmap;
	}

	public void setLoadingImage(Context context, int resId)
	{
		this.mLoadingBitmap = BitmapFactory.decodeResource(
				context.getResources(), resId);
	}

	/**
	 * 设置Bitmap到ImageView
	 * 
	 * @param imageView
	 * @param bitmap
	 */
	private void setImageBitmap(ImageView imageView, Bitmap bitmap)
	{
		imageView.setImageBitmap(bitmap);
	}
}
