package com.github.sanchezih.shopping.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
@Table(name = "tbl_invoce_items")
public class InvoiceItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Positive(message = "El stock debe ser mayor que cero")
	private Double quantity;
	private Double price;

	@Column(name = "product_id")
	private Long productId;

	@Transient
	private Double subTotal;

	public Double getSubTotal() {
		if (this.price > 0 && this.quantity > 0) {
			return this.quantity * this.price;
		} else {
			return (double) 0;
		}
	}

	public InvoiceItem() {
		this.quantity = (double) 0;
		this.price = (double) 0;

	}
}