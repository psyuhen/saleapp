/**
 * 
 */
package com.yuhen.saleapp.domain;

/**
 * 商品信息
 * @author ps
 *
 */
public class MerchInfo {
	private int merch_id;
	private int store_id;
	private int user_id;
	private int image_resource_id;
	private String name;
	private float price;
	private int sales_volume;
	private int in_stock;
	private int classify_id;
	private String published_date;
	private String out_published;
	private String last_modify_time;
	private String create_time;
	private String order_by_clause;
	public int getMerch_id() {
		return merch_id;
	}
	public void setMerch_id(int merch_id) {
		this.merch_id = merch_id;
	}
	public int getStore_id() {
		return store_id;
	}
	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getImage_resource_id() {
		return image_resource_id;
	}
	public void setImage_resource_id(int image_resource_id) {
		this.image_resource_id = image_resource_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getSales_volume() {
		return sales_volume;
	}
	public void setSales_volume(int sales_volume) {
		this.sales_volume = sales_volume;
	}
	public int getIn_stock() {
		return in_stock;
	}
	public void setIn_stock(int in_stock) {
		this.in_stock = in_stock;
	}
	public int getClassify_id() {
		return classify_id;
	}
	public void setClassify_id(int classify_id) {
		this.classify_id = classify_id;
	}
	public String getPublished_date() {
		return published_date;
	}
	public void setPublished_date(String published_date) {
		this.published_date = published_date;
	}
	public String getOut_published() {
		return out_published;
	}
	public void setOut_published(String out_published) {
		this.out_published = out_published;
	}
	public String getLast_modify_time() {
		return last_modify_time;
	}
	public void setLast_modify_time(String last_modify_time) {
		this.last_modify_time = last_modify_time;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getOrder_by_clause() {
		return order_by_clause;
	}
	public void setOrder_by_clause(String order_by_clause) {
		this.order_by_clause = order_by_clause;
	}
	
}
