/**
 * 
 */
package com.yuhen.saleapp.goods;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.activity.BaseActivity;

/**
 * 商品详情
 * 
 * @author ps
 * 
 */
public class GoodsDetailActivity extends BaseActivity {
	private GridView gridView1; // 网格显示缩略图
	private final int IMAGE_OPEN = 1; // 打开图片标记
	private String pathImage; // 选择图片路径
	private Bitmap bmp; // 导入临时图片
	private ArrayList<HashMap<String, Object>> imageItem;
	private SimpleAdapter simpleAdapter; // 适配器
	
	private SwitchButton switchButtonRecommend;
	private SwitchButton switchButtonFreeShip;
	
	private ImageView toClassifyArrow;
	private ImageView toLabelArrow;
	private TextView toClassify;
	private TextView toClassifyId;

	@Override
	public void initView() {
		super.initView();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail);
		initView();
		head_center_text.setText("商品详情");

		gridView1 = (GridView) findViewById(R.id.gridView1);
		/*
		 * 载入默认图片添加图片加号 通过适配器实现 SimpleAdapter参数imageItem为数据源
		 * R.layout.griditem_addpic为布局
		 */
		// 获取资源图片加号
		bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.gridview_addpic);
		imageItem = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("itemImage", bmp);
		imageItem.add(map);
		simpleAdapter = new SimpleAdapter(this, imageItem,
				R.layout.griditem_addpic, new String[] { "itemImage" },
				new int[] { R.id.imageView1 });
		/*
		 * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如 map.put("itemImage",
		 * R.drawable.img); 解决方法: 1.自定义继承BaseAdapter实现 2.ViewBinder()接口实现 参考
		 * http://blog.csdn.net/admin_/article/details/7257901
		 */
		simpleAdapter.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if (view instanceof ImageView && data instanceof Bitmap) {
					ImageView i = (ImageView) view;
					i.setImageBitmap((Bitmap) data);
					return true;
				}
				return false;
			}
		});
		gridView1.setAdapter(simpleAdapter);

		/*
		 * 监听GridView点击事件 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
		 */
		gridView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if (imageItem.size() == 10) { // 第一张为默认图片
					Toast.makeText(GoodsDetailActivity.this, "图片数9张已满",
							Toast.LENGTH_SHORT).show();
				} else if (position == 0) { // 点击图片位置为+ 0对应0张图片
					Toast.makeText(GoodsDetailActivity.this, "添加图片",
							Toast.LENGTH_SHORT).show();
					// 选择图片
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, IMAGE_OPEN);
					// 通过onResume()刷新数据
				} else {
					dialog(position);
					// Toast.makeText(MainActivity.this, "点击第"+(position +
					// 1)+" 号图片",
					// Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		/**  滑动开关   **/
		switchButtonRecommend = (SwitchButton)findViewById(R.id.switchBtnRecommend);
		switchButtonFreeShip = (SwitchButton)findViewById(R.id.switchBtnFreeShip);
		
		switchButtonRecommend.setChecked(false);
		switchButtonFreeShip.setChecked(false);
		
		/**  选择分类   **/
		toClassifyArrow = (ImageView)findViewById(R.id.to_classify_arrow);
		toClassify = (TextView)findViewById(R.id.to_classify);
		toClassifyId = (TextView)findViewById(R.id.to_classify_id);
		toLabelArrow = (ImageView)findViewById(R.id.to_label_arrow);
		toClassifyArrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GoodsDetailActivity.this, ClassifyActivity.class);
				startActivityForResult(intent, R.id.classify_list);
			}
		});
		toLabelArrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GoodsDetailActivity.this, ClassifyActivity.class);
				startActivityForResult(intent, R.id.classify_list);
			}
		});
	}

	// 获取图片路径 响应startActivityForResult
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 打开图片
		if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
			Uri uri = data.getData();
			if (!TextUtils.isEmpty(uri.getAuthority())) {
				// 查询选择图片
				Cursor cursor = getContentResolver().query(uri,
						new String[] { MediaStore.Images.Media.DATA }, null,
						null, null);
				// 返回 没找到选择图片
				if (null == cursor) {
					return;
				}
				// 光标移动至开头 获取图片路径
				cursor.moveToFirst();
				pathImage = cursor.getString(cursor
						.getColumnIndex(MediaStore.Images.Media.DATA));
			}
		}else if (requestCode == R.id.classify_list && resultCode == R.id.classify_list) { // end if 打开图片
			String classifyId = data.getStringExtra("classify_id");
			String classifyName = data.getStringExtra("name");
			toClassify.setText(classifyName);
			toClassifyId.setText(classifyId);
		}
	}

	// 刷新图片
	@Override
	protected void onResume() {
		super.onResume();
		if (!TextUtils.isEmpty(pathImage)) {
			Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", addbmp);
			imageItem.add(map);
			simpleAdapter = new SimpleAdapter(this, imageItem,
					R.layout.griditem_addpic, new String[] { "itemImage" },
					new int[] { R.id.imageView1 });
			simpleAdapter.setViewBinder(new ViewBinder() {
				@Override
				public boolean setViewValue(View view, Object data,
						String textRepresentation) {
					if (view instanceof ImageView && data instanceof Bitmap) {
						ImageView i = (ImageView) view;
						i.setImageBitmap((Bitmap) data);
						return true;
					}
					return false;
				}
			});
			gridView1.setAdapter(simpleAdapter);
			simpleAdapter.notifyDataSetChanged();
			// 刷新后释放防止手机休眠后自动添加
			pathImage = null;
		}
	}

	/*
	 * Dialog对话框提示用户删除操作 position为删除图片位置
	 */
	protected void dialog(final int position) {
		AlertDialog.Builder builder = new Builder(GoodsDetailActivity.this);
		builder.setMessage("确认移除已添加图片吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				imageItem.remove(position);
				simpleAdapter.notifyDataSetChanged();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
}
