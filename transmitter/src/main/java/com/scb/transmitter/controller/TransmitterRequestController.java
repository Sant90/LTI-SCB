package com.scb.transmitter.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scb.transmitter.model.CustomerRequest;
import com.scb.transmitter.model.CustomerRequestData;
import com.scb.transmitter.model.CustomerResponse;
import com.scb.transmitter.service.TransmitterRequestService;
import com.scb.transmitter.utils.ReceiverConstants;

import lombok.extern.log4j.Log4j2;


@RestController @Log4j2
@RequestMapping(ReceiverConstants.TRANSMITTER_URL)
public class TransmitterRequestController {
	@Autowired
	private TransmitterRequestService  customerTransmitterService;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private com.scb.transmitter.servicelmpl.JMSCorrelationalConfig jmsCorrelationalConfig;

	@RequestMapping(value = ReceiverConstants.TRANSMITTER_REQUEST_HANDLE_URL, method = RequestMethod.POST, produces = { "application/json", "application/xml" })
	public ResponseEntity<CustomerRequestData> customerRequestHandle(@RequestBody CustomerRequestData customerRequestData ) {
		log.info("Request received "+ customerRequestData.toString());
		CustomerResponse customerResponse = new CustomerResponse();
			customerResponse = customerTransmitterService.CustomertransmitterService(customerRequestData);
		log.info("Response: "+customerResponse.toString());
		
		return new ResponseEntity<CustomerRequestData>(customerResponse.getCustomerRequestData(), HttpStatus.OK);
		//return customerResponse;
	}

	@RequestMapping(value = ReceiverConstants.TRANSMITTER_REQUEST_HANDLE_URL_REQUEST)
	public CustomerRequest customerRequestHandleExampleRequest( ) {

		return CustomerRequest.builder().customerAccType("Saving").customerId(22).customerName("Test Customer")
				.customerRegion("India").correlationId(200).build();
		// return
		// customerRequestService.customerRequestHandleService(customerRequest);

	}
	
	@RequestMapping(value = ReceiverConstants.TRANSMITTER_JMS_REQUEST_HANDLE_URL_REQUEST, method = RequestMethod.POST)
	public CustomerResponse customerJMSRequestHandleExampleRequest(@RequestBody CustomerRequestData customerRequestData) {
		log.info("JMS Request received "+ customerRequestData.toString());
		CustomerResponse customerResponse = new CustomerResponse();
			customerResponse = customerTransmitterService.CustomertransmitterService(customerRequestData);
		log.info("Response: "+customerResponse.toString());
		jmsTemplate.convertAndSend("CustomerRequestData",customerResponse.getCustomerRequestData());
		
		return customerResponse;

	}
	
	
	/*@RequestMapping(value ="/jmsTest", method = RequestMethod.POST)
	public CustomerRequestData customerJMStest(@RequestBody CustomerRequestData customerRequestData) {
		//log.info("JMS Request received "+ message);
		CustomerRequestData responseFromJMS = jmsCorrelationalConfig.send(customerRequestData);
		log.info("Response: "+ responseFromJMS );
		
		return responseFromJMS;

	}*/
}