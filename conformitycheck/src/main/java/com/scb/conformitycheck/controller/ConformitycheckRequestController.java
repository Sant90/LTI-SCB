package com.scb.conformitycheck.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.scb.conformitycheck.model.CustomerRequestData;
import com.scb.conformitycheck.model.CustomerResponse;
import com.scb.conformitycheck.service.CustomerConformityService;
import com.scb.conformitycheck.utils.ReceiverConstants;

import lombok.extern.log4j.Log4j2;


@Component
@RestController 
@Log4j2
@RequestMapping(ReceiverConstants.CONFORMITY_CHECK_URL)
public class ConformitycheckRequestController {
	@Autowired
	private CustomerConformityService customerConformityService;

	@RequestMapping(value = ReceiverConstants.CONFORMITY_CHECK_REQUEST_HANDLE_URL, produces = { "application/json", "application/xml" })
	public ResponseEntity<CustomerResponse> customerRequestHandle(@RequestBody CustomerRequestData customerRequestData) {
		CustomerResponse customerResponse = customerConformityService.customerConformityCheckService(customerRequestData);
		
		return new ResponseEntity<CustomerResponse>(customerResponse, HttpStatus.OK);
	}

	
}
