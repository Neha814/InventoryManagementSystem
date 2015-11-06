package com.example.database;

public class ItemDetail {

	public String Account_id;

	public String quantity;

	public String inventory_count_id;
	public String item_id;
	public String employee_id;
	public String description;

	public ItemDetail() {

	}

	// constructor
	public ItemDetail(String account_id, String qnty, String inv_count_id,
			String item_id, String employee_id, String description) {
		this.Account_id = account_id;
		this.quantity = qnty;
		this.inventory_count_id = inv_count_id;
		this.item_id = item_id;
		this.employee_id = employee_id;
		this.description = description;

	}

	public String getAccount_id() {
		return Account_id;
	}

	public void setAccount_id(String account_id) {
		Account_id = account_id;
	}

	public String getInventory_count_id() {
		return inventory_count_id;
	}

	public void setInventory_count_id(String inventory_count_id) {
		this.inventory_count_id = inventory_count_id;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
