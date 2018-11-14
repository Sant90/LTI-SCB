package com.scb.transmitter.service;

import com.scb.transmitter.model.CustomerRequest;
import com.scb.transmitter.model.CustomerRequestData;
import com.scb.transmitter.model.CustomerResponse;

//import org.jvnet.hk2.annotations.Service;

public interface TransmitterRequestService{

	public CustomerResponse transmitterRequestHandleService(CustomerRequestData customerRequestData);
	
	public CustomerResponse  TransactionType(CustomerRequest transactionRequestData);

	public CustomerResponse CustomertransmitterService(CustomerRequestData customerRequestData);
	CustomerResponse transmitterRequestHandleService(CustomerRequest customerRequest);
		
		
}
