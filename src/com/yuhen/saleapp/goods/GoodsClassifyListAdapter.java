/**
 * 
 */
package com.yuhen.saleapp.goods;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.domain.ClassifyInfo;

/**
 * @author ps
 * 
 */
public class GoodsClassifyListAdapter extends BaseAdapter {
	List<ClassifyInfo> items;
	Activity context;

	public GoodsClassifyListAdapter(Activity context, List<ClassifyInfo> items) {
		super();
		this.context = context;
		this.items = items;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return items.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public ClassifyInfo getItem(int position) {
		return items.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ClassifyInfo item = items.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(this.context).inflate(R.layout.custom_goods_classify_list_view, null);
			holder.classify_image=(ImageView) convertView.findViewById(R.id.classify_image);
			holder.classify_title=(TextView) convertView.findViewById(R.id.classify_title);
			holder.classify_num=(TextView) convertView.findViewById(R.id.classify_num);
			
			// 将holder绑定到convertView  
            convertView.setTag(holder);
		}else {  
            holder = (ViewHolder) convertView.getTag();  
        }  
//		holder.classify_image.setImageResource(item.getClassify_id());
		holder.classify_title.setText(item.getName() + "");
		holder.classify_num.setText("共"+item.getClassify_num()+"件商品");
		
		return convertView;
	}

	/** 
     * ViewHolder类用以储存item中控件的引用 
     */  
    final class ViewHolder {  
        ImageView classify_image;
        TextView classify_title;
        TextView classify_num;
        ImageView classify_arrow;
    }  
}
