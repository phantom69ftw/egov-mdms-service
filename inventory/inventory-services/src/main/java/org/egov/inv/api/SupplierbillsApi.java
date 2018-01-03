/**
 * NOTE: This class is auto generated by the swagger code generator program (2.2.3).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.egov.inv.api;

import io.swagger.annotations.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.SupplierBillRequest;
import org.egov.inv.model.SupplierBillResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-28T09:20:06.607Z")

@Api(value = "supplierbills", description = "the supplierbills API")
public interface SupplierbillsApi {

    @ApiOperation(value = "Create Supplier Bill .", notes = "Create a new Supplier Bill .", response = SupplierBillResponse.class, tags = {"SB",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "supplier bill created", response = SupplierBillResponse.class),
            @ApiResponse(code = 400, message = "Invalid input.", response = ErrorRes.class)})

    @RequestMapping(value = "/supplierbills/_create",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<SupplierBillResponse> supplierbillsCreatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId, @ApiParam(value = "Details for the new Supplier Bill .", required = true) @Valid @RequestBody SupplierBillRequest supplierBillReceipt);


    @ApiOperation(value = "Get list of supplier bill based on below search parameter(s).", notes = "Provides the list of supplier bill information based on the search parameters. ", response = SupplierBillResponse.class, tags = {"SB",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response, returns list of Supplier Bill (s).", response = SupplierBillResponse.class),
            @ApiResponse(code = 400, message = "Invalid input.", response = ErrorRes.class)})

    @RequestMapping(value = "/supplierbills/_search",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<SupplierBillResponse> supplierbillsSearchPost(@ApiParam(value = "Request header for the service request details.", required = true) @Valid @RequestBody org.egov.common.contract.request.RequestInfo requestInfo, @NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId, @Size(max = 50) @ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids, @ApiParam(value = "The store code of the supplier bill") @RequestParam(value = "store", required = false) String store, @ApiParam(value = "invoice number of the supplier bill.") @RequestParam(value = "invoiceNumber", required = false) String invoiceNumber, @ApiParam(value = "invoice date of the supplier bill.") @RequestParam(value = "invoiceDate", required = false) Long invoiceDate, @ApiParam(value = "approved date of the supplier bill") @RequestParam(value = "approvedDate", required = false) Long approvedDate, @Min(0) @Max(100) @ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize, @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy);


    @ApiOperation(value = "Update existing supplier bill.", notes = "Existing supplier bill can be updated.", response = SupplierBillResponse.class, tags = {"SB",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Supplier Bill (s) Updated Successfully.", response = SupplierBillResponse.class),
            @ApiResponse(code = 400, message = "Invalid Input", response = ErrorRes.class)})

    @RequestMapping(value = "/supplierbills/_update",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<SupplierBillResponse> supplierbillsUpdatePost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId, @ApiParam(value = "Details for the new material receipts.", required = true) @Valid @RequestBody SupplierBillRequest supplierBillReceipt);

}
