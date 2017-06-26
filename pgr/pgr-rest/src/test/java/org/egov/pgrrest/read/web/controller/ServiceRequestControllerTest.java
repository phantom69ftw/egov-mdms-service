package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.Resources;
import org.egov.pgrrest.TestConfiguration;
import org.egov.pgrrest.common.contract.SevaRequest;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Requester;
import org.egov.pgrrest.common.model.UserType;
import org.egov.pgrrest.common.repository.UserRepository;
import org.egov.pgrrest.read.domain.exception.ServiceRequestIdMandatoryException;
import org.egov.pgrrest.read.domain.exception.TenantIdMandatoryException;
import org.egov.pgrrest.read.domain.exception.UpdateServiceRequestNotAllowedException;
import org.egov.pgrrest.read.domain.model.*;
import org.egov.pgrrest.read.domain.service.ServiceRequestService;
import org.egov.pgrrest.read.domain.service.UpdateServiceRequestEligibilityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ServiceRequestController.class)
@Import(TestConfiguration.class)
public class ServiceRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Resources resources = new Resources();

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ServiceRequestService serviceRequestService;

    @MockBean
    private UpdateServiceRequestEligibilityService updateEligibilityService;

    @Test
    public void test_should_return_error_response_when_tenant_id_is_not_present_on_creating_a_complaint()
        throws Exception {
        when(userRepository.getUser("authToken")).thenReturn(getCitizen());
        doThrow(new TenantIdMandatoryException()).when(serviceRequestService)
            .save(any(ServiceRequest.class), any(SevaRequest.class));

        mockMvc.perform(post("/seva/_create")
            .param("foo", "b1", "b2")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("createComplaintRequest.json")))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(resources.getFileContents("tenantIdMandatoryErrorResponse.json")));
    }

    @Test
    public void test_for_updating_a_complaint_not_assigned_to_redresal_officer()
        throws Exception {
        when(userRepository.getUser("authToken")).thenReturn(getCitizen());
        doThrow(new UpdateServiceRequestNotAllowedException()).when(serviceRequestService)
            .update(any(ServiceRequest.class), any(SevaRequest.class));
        mockMvc.perform(post("/seva/_update")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("updateComplaintRequestRedresalOfficer.json")))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(resources.getFileContents("updateComplaintErrorResponseForRedressal.json")));
    }

    @Test
    public void test_for_creating_a_complaint()
        throws Exception {
        when(userRepository.getUser("authToken")).thenReturn(getCitizen());

        mockMvc.perform(post("/seva/_create")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("createComplaintRequest.json")))
            .andExpect(status().isCreated())
            .andExpect(content().json(resources.getFileContents("createComplaintResponse.json")));
    }

    @Test
    public void test_for_updating_a_complaint()
        throws Exception {
        when(userRepository.getUser("authToken")).thenReturn(getCitizen());

        mockMvc.perform(post("/seva/_update")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("updateComplaintRequest.json")))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("updateComplaintResponse.json")));
    }

    @Test
    public void test_update_request_should_return_error_when_service_request_is_not_present()
        throws Exception {
        when(userRepository.getUser("authToken")).thenReturn(getCitizen());
        doThrow(new ServiceRequestIdMandatoryException()).when(serviceRequestService)
            .update(any(ServiceRequest.class), any(SevaRequest.class));

        mockMvc.perform(post("/seva/_update")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("updateComplaintRequest.json")))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(resources.getFileContents("serviceRequestIdMandatoryErrorResponse.json")));
    }

    @Test
    public void test_update_eligibility_returns_true_when_eligible() throws Exception {
        doNothing()
            .when(updateEligibilityService).validate(eq("crn"), eq("tenantId"), any());
        mockMvc.perform(post("/seva/v1/_get?tenantId=tenantId&crn=crn")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("updateComplaintRequestEligibility.json")))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("updateComplaintResponseEligible.json")));
    }

    @Test
    public void test_update_eligibility_returns_false_when_not_eligible() throws Exception {
        doThrow(new UpdateServiceRequestNotAllowedException())
            .when(updateEligibilityService).validate(eq("crn"), eq("tenantId"), any());
        mockMvc.perform(post("/seva/v1/_get?tenantId=tenantId&crn=crn")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("updateComplaintRequestEligibility.json")))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("updateComplaintResponseNotEligible.json")));
    }

    @Test
    public void test_should_return_masked_userdetails_in_service_requests_for_anonymous_user_search() throws Exception {

        String name = "kumar";
        ServiceRequest complaint = getServiceRequestForSearch();

        ServiceRequestSearchCriteria criteria = ServiceRequestSearchCriteria.builder()
            .assignmentId(10L)
            .endDate(null)
            .escalationDate(null)
            .lastModifiedDatetime(null)
            .serviceCode("serviceCode_123")
            .serviceRequestId("serid_123").startDate(null)
            .status(Arrays.asList("REGISTERED", "FORWARDED"))
            .userId(10L)
            .emailId("abc@gmail.com")
            .mobileNumber("74742487428")
            .name(name)
            .locationId(4L)
            .childLocationId(5L)
            .receivingMode(5L)
            .tenantId("tenantId")
            .pageSize(20)
            .fromIndex(2)
            .isAnonymous(true)
            .build();

        List<ServiceRequest> complaints = new ArrayList<>(Collections.singletonList(complaint));
        when(serviceRequestService.findAll(criteria)).thenReturn(complaints);

        mockMvc.perform(
            post("/seva/_search")
                .param("tenantId", "tenantId")
                .param("serviceRequestId", "serid_123")
                .param("serviceCode", "serviceCode_123")
                .param("status", "REGISTERED")
                .param("status", "FORWARDED")
                .param("assignmentId", "10")
                .param("userId", "10")
                .param("name", "kumar")
                .param("emailId", "abc@gmail.com")
                .param("mobileNumber", "74742487428")
                .param("receivingMode", "5")
                .param("locationId", "4")
                .param("childLocationId", "5")
                .param("fromIndex", "2")
                .param("sizePerPage", "20")
                .content(resources.getFileContents("anonymousrequestinfobody.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(resources.getFileContents("getServiceRequests.json")));
    }

    @Test
    public void test_should_return_service_requests_for_given_search_criteria() throws Exception {

        String name = "kumar";
        ServiceRequest complaint = getServiceRequestForSearch();

        ServiceRequestSearchCriteria criteria = ServiceRequestSearchCriteria.builder()
            .assignmentId(10L)
            .endDate(null)
            .escalationDate(null)
            .lastModifiedDatetime(null)
            .serviceCode("serviceCode_123")
            .serviceRequestId("serid_123").startDate(null)
            .status(Arrays.asList("REGISTERED", "FORWARDED"))
            .userId(10L)
            .emailId("abc@gmail.com")
            .mobileNumber("74742487428")
            .name(name)
            .locationId(4L)
            .childLocationId(5L)
            .receivingMode(5L)
            .tenantId("tenantId")
            .pageSize(20)
            .fromIndex(2)
            .isAnonymous(false)
            .build();

        List<ServiceRequest> complaints = new ArrayList<>(Collections.singletonList(complaint));
        when(serviceRequestService.findAll(criteria)).thenReturn(complaints);

        mockMvc.perform(
            post("/seva/_search")
                .param("tenantId", "tenantId")
                .param("serviceRequestId", "serid_123")
                .param("serviceCode", "serviceCode_123")
                .param("status", "REGISTERED")
                .param("status", "FORWARDED")
                .param("assignmentId", "10")
                .param("userId", "10")
                .param("name", "kumar")
                .param("emailId", "abc@gmail.com")
                .param("mobileNumber", "74742487428")
                .param("receivingMode", "5")
                .param("locationId", "4")
                .param("childLocationId", "5")
                .param("fromIndex", "2")
                .param("sizePerPage", "20")
                .content(resources.getFileContents("requestinfobody.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(resources.getFileContents("getServiceRequests.json")));
    }

    @Test
    public void test_should_return_count_of_matching_service_requests_for_given_search_criteria() throws Exception {
        ServiceRequestSearchCriteria criteria = ServiceRequestSearchCriteria.builder()
            .assignmentId(10L)
            .endDate(null)
            .escalationDate(null)
            .lastModifiedDatetime(null)
            .serviceCode("serviceCode_123")
            .serviceRequestId("serid_123").startDate(null)
            .status(Arrays.asList("REGISTERED", "FORWARDED"))
            .userId(10L)
            .emailId("abc@gmail.com")
            .mobileNumber("74742487428")
            .name("kumar")
            .locationId(4L)
            .childLocationId(5L)
            .receivingMode(5L)
            .tenantId("tenantId")
            .build();

        when(serviceRequestService.getCount(criteria)).thenReturn(30L);

        mockMvc.perform(
            post("/seva/_count")
                .param("tenantId", "tenantId")
                .param("serviceRequestId", "serid_123")
                .param("serviceCode", "serviceCode_123")
                .param("status", "REGISTERED")
                .param("status", "FORWARDED")
                .param("assignmentId", "10")
                .param("userId", "10")
                .param("name", "kumar")
                .param("emailId", "abc@gmail.com")
                .param("mobileNumber", "74742487428")
                .param("receivingMode", "5")
                .param("locationId", "4")
                .param("childLocationId", "5")
                .content(resources.getFileContents("requestinfobody.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(resources.getFileContents("getServiceRequestCount.json")));
    }

    private ServiceRequest getServiceRequestForSearch() {
        String crn = "1234";
        String receivingMode = "MANUAL";
        String receivingCenter = "Commissioner Office";
        String stateId = "1";
        Long assigneeId = 2L;
        String address = null;
        List<String> mediaUrls = new ArrayList<>();
        mediaUrls.add(null);
        mediaUrls.add(null);
        String jurisdictionId = "1";
        String description = null;
        String mobileNumber = null;
        String emailId = null;
        String name = "kumar";
        long id = 67;
        boolean anonymousUser = false;
        AuthenticatedUser user = AuthenticatedUser.builder()
            .mobileNumber(mobileNumber)
            .email(emailId)
            .name(name)
            .id(id)
            .anonymousUser(anonymousUser)
            .type(UserType.CITIZEN)
            .tenantId("tenantId")
            .build();
        final Requester domainComplainant =
            new Requester("kumar", null, null, "mico layout", "user");
        final Coordinates coordinates = new Coordinates(0.0, 0.0);
        final ServiceRequestLocation serviceRequestLocation = new ServiceRequestLocation(coordinates,
            null, "34");
        final ServiceRequestType serviceRequestType =
            new ServiceRequestType("abc", "complaintCode", "tenantId", null);
        ServiceRequest complaint = ServiceRequest.builder()
            .authenticatedUser(user)
            .crn(crn)
            .serviceRequestType(serviceRequestType)
            .address(address)
            .mediaUrls(mediaUrls)
            .serviceRequestLocation(serviceRequestLocation)
            .requester(domainComplainant)
            .tenantId(jurisdictionId)
            .description(description)
            .state(stateId)
            .assignee(assigneeId)
            .receivingCenter(receivingCenter)
            .receivingMode(receivingMode)
            .serviceRequestStatus("FORWARDED")
            .childLocation("Gadu Veedhi")
            .department(3L)
            .tenantId("tenantId")
            .build();

        return complaint;
    }

    private AuthenticatedUser getCitizen() {
        return AuthenticatedUser.builder().id(1L).type(UserType.CITIZEN).build();
    }


}