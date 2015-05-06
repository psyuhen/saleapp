/**
 * 
 */
package com.yuhen.saleapp.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * @author yangyu 功能描述：弹窗内部子类项（绘制标题和图标）
 */
public class ActionItem {
	// 定义图片对象
	private Drawable drawable;
	// 定义文本对象
	private CharSequence title;

	private Integer titleId; 

	// public ActionItem(Drawable drawable, CharSequence title){
	// this.mDrawable = drawable;
	// this.mTitle = title;
	// }

	public ActionItem(Context context, int titleId, int drawableId) {
		this.titleId = titleId; 

		this.title = context.getResources().getText(titleId);
		this.drawable = context.getResources().getDrawable(drawableId);
	}

	// public ActionItem(Context context, CharSequence title, int drawableId) {
	// this.mDrawableId = drawableId;
	// this.mTitle = title;
	// this.mDrawable = context.getResources().getDrawable(drawableId);
	// }
	
	public Drawable getDrawable() {
		return drawable;
	}

	public CharSequence getTitle() {
		return title;
	}

	public Integer getTitleId() {
		return titleId;
	}
}