/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.persistence.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.ReceiptCommonModel;
import org.egov.collection.model.ReceiptDetail;
import org.egov.collection.model.ReceiptHeader;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.model.enums.CollectionType;
import org.egov.collection.model.enums.ReceiptType;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.repository.ReceiptRepository;
import org.egov.collection.repository.QueryBuilder.ReceiptDetailQueryBuilder;
import org.egov.collection.repository.rowmapper.ReceiptRowMapper;
import org.egov.collection.web.contract.Bill;
import org.egov.collection.web.contract.BillAccountDetail;
import org.egov.collection.web.contract.BillDetail;
import org.egov.collection.web.contract.Purpose;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptReq;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ReceiptRepositoryTest {
	@Mock
	JdbcTemplate jdbcTemplate;

	@Mock
	private CollectionProducer collectionProducer;

	@Mock
	@Autowired
	private ApplicationProperties applicationProperties;

	@Mock
	private ReceiptDetailQueryBuilder receiptDetailQueryBuilder;

	@Mock
	private ReceiptRowMapper receiptRowMapper;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private ReceiptRepository receiptRepository;

	@Before
	public void before() {
		receiptRepository = new ReceiptRepository(jdbcTemplate, collectionProducer, applicationProperties,
				receiptDetailQueryBuilder, receiptRowMapper, restTemplate, namedParameterJdbcTemplate);
	}

	@Test
	public void test_should_search_receipt_as_per_criteria() throws ParseException {
		ReceiptCommonModel commonModel = getReceiptCommonModel();
		when(receiptDetailQueryBuilder.getQuery(any(ReceiptSearchCriteria.class), any(List.class))).thenReturn("");
		when(jdbcTemplate.query(any(String.class), any(Object[].class), any(ReceiptRowMapper.class)))
				.thenReturn(getListReceiptHeader());
		assertTrue(commonModel.equals(receiptRepository.findAllReceiptsByCriteria(getReceiptSearchCriteria())));
	}

	@Test
	public void test_should_be_able_to_cancel_the_receipt_before_bank_remittance() {
		when(receiptDetailQueryBuilder.getCancelQuery()).thenReturn("");
		int[] integers = { 1, 2 };
		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(integers);
		assertTrue(getReceiptRequest().getReceipt()
				.equals(receiptRepository.cancelReceipt(getReceiptRequest()).getReceipt()));
	}

	@Test
	public void test_should_be_able_to_push_cancel_request_to_kafka() {
		receiptRepository.pushReceiptCancelDetailsToQueue(getReceiptRequest());
		when(applicationProperties.getCancelReceiptTopicKey()).thenReturn("");
		when(applicationProperties.getCancelReceiptTopicName()).thenReturn("");
		verify(collectionProducer).producer(any(String.class), any(String.class), any(ReceiptReq.class));
		assertTrue(getReceiptRequest().getReceipt()
				.equals(receiptRepository.pushReceiptCancelDetailsToQueue(getReceiptRequest())));
	}

	private ReceiptReq getReceiptRequest() {
		Calendar calender = Calendar.getInstance();
		calender.set(2016, 12, 22);
		User userInfo = User.builder().id(1L).build();
		RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.collection").ver("1.0").action("POST")
				.did("4354648646").key("xyz").msgId("654654").ts(calender.getTime()).requesterId("61").authToken("ksnk")
				.userInfo(userInfo).build();
		BillAccountDetail detail1 = BillAccountDetail.builder().glcode("456").isActualDemand(true).id("1")
				.tenantId("default").billDetail("1").creditAmount(BigDecimal.valueOf(800))
				.debitAmount(BigDecimal.valueOf(600)).purpose(Purpose.REBATE).build();
		BillAccountDetail detail2 = BillAccountDetail.builder().glcode("490").isActualDemand(true).id("2")
				.tenantId("default").billDetail("1").creditAmount(BigDecimal.valueOf(800))
				.debitAmount(BigDecimal.valueOf(700)).purpose(Purpose.REBATE).build();

		BillDetail detail = BillDetail.builder().id("1").billNumber("REF1234").consumerCode("CON12343556")
				.consumerType("Good").minimumAmount(BigDecimal.valueOf(125)).totalAmount(BigDecimal.valueOf(150))
				.collectionModesNotAllowed(Arrays.asList("Bill based")).tenantId("default").receiptNumber("REC1234")
				.receiptType(ReceiptType.valueOf("ADHOC")).channel("567hfghr").voucherHeader("VOUHEAD").collectionType(CollectionType.valueOf("COUNTER")).boundary("67")
				.reasonForCancellation("Data entry mistake")
				.cancellationRemarks("receipt number data entered is not proper").status("CANCELLED")
				.displayMessage("receipt created successfully").billAccountDetails(Arrays.asList(detail1, detail2))
				.businessService("TL").build();
		Bill billInfo = Bill.builder().payeeName("abc").payeeAddress("abc nagara").payeeEmail("abc567@gmail.com")
				.billDetails(Arrays.asList(detail)).tenantId("default").paidBy("abc").build();
		Receipt receipt = Receipt.builder().tenantId("default").bill(Arrays.asList(billInfo)).build();
		return ReceiptReq.builder().requestInfo(requestInfo).receipt(receipt).build();
	}

	private ReceiptSearchCriteria getReceiptSearchCriteria() {
		return ReceiptSearchCriteria.builder().collectedBy("1").tenantId("default").status("CREATED")
				.sortBy("payeename").sortOrder("desc").fromDate("2016-02-02 00:00:00")
				.toDate("2017-07-11 13:25:45.794050").build();
	}

	private ReceiptCommonModel getReceiptCommonModel() throws ParseException {

		ReceiptHeader receiptHeader = ReceiptHeader.builder().id(1L).build();
		ReceiptDetail detail1 = ReceiptDetail.builder().id(1L).chartOfAccount("456").dramount(600.00).cramount(800.00)
				.ordernumber(5L).receiptHeader(receiptHeader).actualcramountToBePaid(800.00)
				.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
				.tenantId("default").build();
		ReceiptDetail detail2 = ReceiptDetail.builder().id(2L).chartOfAccount("490").dramount(700.00).cramount(800.00)
				.ordernumber(6L).receiptHeader(receiptHeader).actualcramountToBePaid(800.00)
				.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
				.tenantId("default").build();
		Set<ReceiptDetail> detailSet = new HashSet<>();
		detailSet.add(detail1);
		detailSet.add(detail2);
		ReceiptHeader header = ReceiptHeader.builder().id(1L).payeename("abc").payeeAddress("abc nagara")
				.payeeEmail("abc567@gmail.com").paidBy("abc").referenceNumber("REF1234").receiptType("ADHOC")
				.receiptNumber("REC1234").referenceDesc("REFDESC45").manualReceiptNumber("MAN67").businessDetails("TL")
				.collectionType(CollectionType.valueOf("COUNTER").toString()).displayMsg("receipt created successfully").reference_ch_id(1L).stateId(3L)
				.location(1L).isReconciled(true).status("CREATED").reasonForCancellation("documents not proper")
				.minimumAmount(125.00).totalAmount(150.00).collModesNotAllwd("Bill based").consumerCode("CON12343556")
				.channel("567hfghr").consumerType("Known").fund("56").fundSource("78").function("678").boundary("67")
				.department("78").voucherheader("VOUHEAD").depositedBranch("ICICI").version(1L).createdBy(1L)
				.lastModifiedBy(1L).tenantId("default").build();
		return ReceiptCommonModel.builder().receiptHeaders(Arrays.asList(header))
				.receiptDetails(Arrays.asList(detail1, detail2)).build();
	}

	private List<ReceiptHeader> getListReceiptHeader() {
		Set<ReceiptDetail> setReceipt = new HashSet<>();
		Set<ReceiptDetail> setReceiptDetail = new HashSet<>();
		ReceiptHeader receiptHeader = ReceiptHeader.builder().id(1L).build();
		setReceiptDetail.add(ReceiptDetail.builder().id(2L).chartOfAccount("490").dramount(700.00).cramount(800.00)
				.ordernumber(6L).receiptHeader(receiptHeader).actualcramountToBePaid(800.00)
				.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
				.tenantId("default").build());
		setReceipt.add(ReceiptDetail.builder().id(1L).chartOfAccount("456").dramount(600.00).cramount(800.00)
				.ordernumber(5L).receiptHeader(receiptHeader).actualcramountToBePaid(800.00)
				.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
				.tenantId("default").build());
		ReceiptHeader header1 = ReceiptHeader.builder().id(1L).payeename("abc").payeeAddress("abc nagara")
				.payeeEmail("abc567@gmail.com").paidBy("abc").referenceNumber("REF1234").receiptType("ADHOC")
				.receiptNumber("REC1234").referenceDesc("REFDESC45").manualReceiptNumber("MAN67").businessDetails("TL")
				.collectionType(CollectionType.valueOf("COUNTER").toString()).displayMsg("receipt created successfully").reference_ch_id(1L).stateId(3L)
				.location(1L).isReconciled(true).status("CREATED").reasonForCancellation("documents not proper")
				.minimumAmount(125.00).totalAmount(150.00).collModesNotAllwd("Bill based").consumerCode("CON12343556")
				.channel("567hfghr").consumerType("Known").fund("56").fundSource("78").function("678").boundary("67")
				.department("78").voucherheader("VOUHEAD").depositedBranch("ICICI").version(1L).createdBy(1L)
				.lastModifiedBy(1L).tenantId("default").receiptDetails(setReceipt).build();
		ReceiptHeader header2 = ReceiptHeader.builder().id(1L).payeename("abc").payeeAddress("abc nagara")
				.payeeEmail("abc567@gmail.com").paidBy("abc").referenceNumber("REF1234").receiptType("ADHOC")
				.receiptNumber("REC1234").referenceDesc("REFDESC45").manualReceiptNumber("MAN67").businessDetails("TL")
				.collectionType(CollectionType.valueOf("COUNTER").toString()).displayMsg("receipt created successfully").reference_ch_id(1L).stateId(3L)
				.location(1L).isReconciled(true).status("CREATED").reasonForCancellation("documents not proper")
				.minimumAmount(125.00).totalAmount(150.00).collModesNotAllwd("Bill based").consumerCode("CON12343556")
				.channel("567hfghr").consumerType("Known").fund("56").fundSource("78").function("678").boundary("67")
				.department("78").voucherheader("VOUHEAD").depositedBranch("ICICI").version(1L).createdBy(1L)
				.lastModifiedBy(1L).tenantId("default").receiptDetails(setReceiptDetail).build();

		return Arrays.asList(header1, header2);
	}
}