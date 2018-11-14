package com.scb.transmitter.dao;

import java.util.List;

//import java.awt.List;

import com.scb.transmitter.model.CustomerRequestData;

public interface CustomerDataReposatory  {
	List<CustomerRequestData> findByCustomerNameAndCustomerId(String customername, long customerId);
	List<CustomerRequestData> findByCorelationId(long corelationId);
	//extends JpaRepository<CustomerRequestData, Long>

}
