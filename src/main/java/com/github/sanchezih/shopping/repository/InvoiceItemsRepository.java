package com.github.sanchezih.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.sanchezih.shopping.entity.InvoiceItem;

public interface InvoiceItemsRepository extends JpaRepository<InvoiceItem, Long> {
}