package com.scb.transmitter.servicelmpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.scb.transmitter.*;
import com.scb.transmitter.config.CustomerConfig;
import com.scb.transmitter.model.CustomerRequest;
import com.scb.transmitter.model.CustomerRequestData;
import com.scb.transmitter.model.CustomerResponse;
import com.scb.transmitter.model.MsAuditLog;
import com.scb.transmitter.service.TransmitterRequestService;
import com.scb.transmitter.utils.SCBCommonMethods;

/*import com.scb.transmitter.config.CustomerConfig;
import com.scb.transmitter.model.CustomerRequest;
import com.scb.transmitter.model.CustomerRequestData;
import com.scb.transmitter.model.CustomerResponse;
import com.scb.transmitter.model.MsAuditLog;
import com.scb.transmitter.service.TransmitterRequestService;
import com.scb.transmitter.utils.SCBCommonMethods;*/

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public abstract class CustomerRequestServiceImpl implements TransmitterRequestService {
	@Autowired
	private SCBCommonMethods commonMethods;
	@Autowired
	private CustomerConfig propertiesConfig;
	@Autowired
	private GcgInternalApiCall gcgInternalApiCall;
	@Autowired
	private JMSCorrelationalConfig jmsCorrelationalConfig;

	public CustomerResponse transmitterRequestHandleService(CustomerRequest customerRequest) {
		CustomerRequestData customerRequestData = commonMethods.getCustomerDataFromRequest(customerRequest);
		ResponseEntity<MsAuditLog> msAuditLogApiResponse = null;
		if (propertiesConfig.getIsEnableAuditLog().equalsIgnoreCase("yes")) {
			MsAuditLog parseAuditLog = commonMethods.getAuditLogDetails(customerRequestData);
			msAuditLogApiResponse = gcgInternalApiCall.msAuditLogApiCall(parseAuditLog);
		}
		
		ResponseEntity<CustomerResponse> customerResponseFromPersistDb = null;
		ResponseEntity<CustomerResponse> customerResponseFromvalidator = gcgInternalApiCall.msValidatorCall(customerRequestData);
		String downstreamProtocol = customerResponseFromvalidator.getBody().getResponseMessage();
		log.debug("Down stream protocol: .."+downstreamProtocol);
		if(customerResponseFromvalidator.getBody().getResponseCode() == 200){
			customerResponseFromPersistDb = gcgInternalApiCall
					.msCustomerPersistApiCall(customerRequestData);
		}else{
			return customerResponseFromvalidator.getBody();
		}
		
	//	ResponseEntity<CustomerResponse> customerResponseFromPersistDb = gcgInternalApiCall.msCustomerPersistApiCall(customerRequestData);
		ResponseEntity<CustomerResponse> customerResponseFromDownStream = null;
		if (customerResponseFromPersistDb.getBody().getResponseCode() == 200) {
			
			if(downstreamProtocol.trim().equalsIgnoreCase("JMS")){
				log.debug("JMS call ");
				CustomerRequestData customerRequestDataFromJMS = jmsCorrelationalConfig.send(customerResponseFromPersistDb.getBody().getCustomerRequestData());
				return commonMethods.getSuccessResponse(customerRequestDataFromJMS, "Successful response from JMS");
			} else if(downstreamProtocol.trim().equalsIgnoreCase("HTTP")){
				customerResponseFromDownStream = gcgInternalApiCall
						.msDownStreamCall(customerResponseFromPersistDb.getBody().getCustomerRequestData());
			}
			else{
				customerResponseFromDownStream = gcgInternalApiCall
						.msDownStreamCall(customerResponseFromPersistDb.getBody().getCustomerRequestData());
			}
			
		} else {
			return customerResponseFromPersistDb.getBody();
		}
		
		return  customerResponseFromDownStream.getBody();
	}

}
