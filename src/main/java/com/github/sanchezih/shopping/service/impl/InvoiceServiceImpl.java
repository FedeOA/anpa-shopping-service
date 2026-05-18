package com.github.sanchezih.shopping.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.github.sanchezih.shopping.client.CustomerFallback;
import com.github.sanchezih.shopping.client.ProductFallback;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import com.github.sanchezih.shopping.client.CustomerClient;
import com.github.sanchezih.shopping.client.ProductClient;
import com.github.sanchezih.shopping.entity.Invoice;
import com.github.sanchezih.shopping.model.Customer;
import com.github.sanchezih.shopping.model.Product;
import com.github.sanchezih.shopping.repository.InvoiceRepository;
import com.github.sanchezih.shopping.service.InvoiceService;
import com.github.sanchezih.shopping.entity.InvoiceItem;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

	private final InvoiceRepository invoiceRepository;
	private final CustomerClient customerClient;
	private final ProductClient productClient;
	private final CustomerFallback customerFallback;
	private CircuitBreakerFactory<?, ?> circuitBreakerFactory;
	private final ProductFallback productFallback;

	/*----------------------------------------------------------------------------*/

	public InvoiceServiceImpl(InvoiceRepository invoiceRepository, CustomerClient customerClient,
							  ProductFallback productFallback, ProductClient productClient, CustomerFallback customerFallback,
							  CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
		this.invoiceRepository = invoiceRepository;
		this.customerClient = customerClient;
		this.productClient = productClient;
		this.customerFallback = customerFallback;
		this.circuitBreakerFactory = circuitBreakerFactory;
		this.productFallback = productFallback;
    }

	/*----------------------------------------------------------------------------*/

	@Override
	public List<Invoice> findInvoiceAll() {
		return invoiceRepository.findAll();
	}

	@Override
	public Invoice createInvoice(Invoice invoice) {
		Invoice invoiceDB = invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
		if (invoiceDB != null) {
			return invoiceDB;
		}
		invoice.setState("CREATED");

		invoiceDB = invoiceRepository.save(invoice);

		invoiceDB.getItems().forEach(invoiceItem -> {
			productClient.updateStockProduct(invoiceItem.getProductId(), invoiceItem.getQuantity() * -1);
		});

		return invoiceDB;
	}

	@Override
	public Invoice updateInvoice(Invoice invoice) {
		Invoice invoiceDB = getInvoice(invoice.getId());
		if (invoiceDB == null) {
			return null;
		}
		invoiceDB.setCustomerId(invoice.getCustomerId());
		invoiceDB.setDescription(invoice.getDescription());
		invoiceDB.setNumberInvoice(invoice.getNumberInvoice());
		invoiceDB.getItems().clear();
		invoiceDB.setItems(invoice.getItems());
		return invoiceRepository.save(invoiceDB);
	}

	@Override
	public Invoice deleteInvoice(Invoice invoice) {
		Invoice invoiceDB = getInvoice(invoice.getId());
		if (invoiceDB == null) {
			return null;
		}
		invoiceDB.setState("DELETED");
		return invoiceRepository.save(invoiceDB);
	}

	@Override
	public Invoice getInvoice(Long id) {
		Invoice invoice = invoiceRepository.findById(id).orElse(null);

		if (invoice != null) {

			// Circuit Breaker sobre la llamada al cliente Feign
			Customer customer = circuitBreakerFactory.create("customerCircuitBreaker").run(
					() -> customerClient.getCustomer(invoice.getCustomerId()).getBody(), customerFallback::fallback);

			invoice.setCustomer(customer);

			List<InvoiceItem> listItem = invoice.getItems().stream().map(invoiceItem -> {

				// Circuit Breaker sobre la llamada al cliente Feign
				Product product = circuitBreakerFactory.create("productCircuitBreaker").run(
						() -> productClient.getProduct(invoiceItem.getProductId()).getBody(),
						productFallback::fallback);

				invoiceItem.setProduct(product);
				return invoiceItem;
			}).collect(Collectors.toList());

			invoice.setItems(listItem);
		}
		return invoice;
	}
}