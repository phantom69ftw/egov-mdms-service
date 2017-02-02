/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.entity;

import org.egov.pgr.entity.enums.CitizenFeedback;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static org.egov.pgr.entity.Complaint.SEQ_COMPLAINT;

@Entity
@Table(name = "egpgr_complaint")
@SequenceGenerator(name = SEQ_COMPLAINT, sequenceName = SEQ_COMPLAINT, allocationSize = 1)
public class Complaint extends AbstractAuditable {

    public static final String SEQ_COMPLAINT = "SEQ_EGPGR_COMPLAINT";
    private static final long serialVersionUID = 4020616083055647372L;
    @Id
    @GeneratedValue(generator = SEQ_COMPLAINT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "crn", unique = true)
    @Length(max = 32)
    @SafeHtml
    private String crn = "";

    @ManyToOne
    @JoinColumn(name = "complaintType", nullable = true)
    private ComplaintType complaintType;

    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name = "complainant", nullable = false)
    private Complainant complainant = new Complainant();

    private Long assignee;

    private Long location;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "status")
    private ComplaintStatus status = new ComplaintStatus();

    @Length(min = 10, max = 500)
    @SafeHtml
    private String details;

    @Length(max = 200)
    @SafeHtml
//	@Column(name = "landmarkdetails")
    private String landmarkDetails;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
//	@Column(name = "receivingmode")
    private ReceivingMode receivingMode = ReceivingMode.WEBSITE;

    @ManyToOne
    @JoinColumn(name = "receivingCenter", nullable = true)
    private ReceivingCenter receivingCenter;

//	private Set<Long> supportDocs;

    private double lng;

    private double lat;

    @Column(name = "escalation_date", nullable = false)
    private Date escalationDate;

    @Column(name = "createddate")
    private Date createdDate;

    @Column(name = "lastmodifieddate")
    private Date lastModifiedDate;

    private Long department;

    @Enumerated(EnumType.ORDINAL)
//	@Column(name = "citizenfeedback")
    private CitizenFeedback citizenFeedback;

    //	@Column(name = "childlocation")
    private Long childLocation;

    @Transient
    private String latlngAddress;

    /*
     * For indexing the below fields are kept. These will not be added to the
     * database. This will be available only in index.
     */
    @Transient
    private Long crossHierarchyId;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCrn() {
        return this.crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }


    public ComplaintType getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(ComplaintType complaintType) {
        this.complaintType = complaintType;
    }

    public Complainant getComplainant() {
        return this.complainant;
    }

    public void setComplainant(Complainant complainant) {
        this.complainant = complainant;
    }

    public Long getAssignee() {
        return this.assignee;
    }

    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    public ComplaintStatus getStatus() {
        return this.status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ReceivingMode getReceivingMode() {
        return this.receivingMode;
    }

    public void setReceivingMode(ReceivingMode receivingMode) {
        this.receivingMode = receivingMode;
    }

    public ReceivingCenter getReceivingCenter() {
        return this.receivingCenter;
    }

    public void setReceivingCenter(ReceivingCenter receivingCenter) {
        this.receivingCenter = receivingCenter;
    }

//	public Set<Long> getSupportDocs() {
//		return this.supportDocs;
//	}
//
//	public void setSupportDocs(Set<Long> supportDocs) {
//		this.supportDocs = supportDocs;
//	}

    public Long getLocation() {
        return this.location;
    }

    public void setLocation(Long location) {
        this.location = location;
    }

    public String getLandmarkDetails() {
        return this.landmarkDetails;
    }

    public void setLandmarkDetails(String landmarkDetails) {
        this.landmarkDetails = landmarkDetails;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return this.lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Date getEscalationDate() {
        return null == this.escalationDate ? null : this.escalationDate;
    }

    public void setEscalationDate(Date escalationDate) {
        this.escalationDate = null == escalationDate ? null : escalationDate;
    }

    public Long getDepartment() {
        return this.department;
    }

    @Override
    public String toString() {
        return "Complaint [id=" + id + ", crn=" + crn + ", complaintType="
                + complaintType + ", complainant=" + complainant
                + ", assignee=" + assignee + ", location=" + location
                + ", status=" + status + ", details=" + details
                + ", landmarkDetails=" + landmarkDetails + ", receivingMode="
                + receivingMode + ", receivingCenter=" + receivingCenter
                + ", lng=" + lng + ", lat=" + lat + ", escalationDate="
                + escalationDate + ", department=" + department
                + ", citizenFeedback=" + citizenFeedback + ", childLocation="
                + childLocation + ", latlngAddress=" + latlngAddress
                + ", crossHierarchyId=" + crossHierarchyId + "]";
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public CitizenFeedback getCitizenFeedback() {
        return this.citizenFeedback;
    }

    public void setCitizenFeedback(CitizenFeedback citizenFeedback) {
        this.citizenFeedback = citizenFeedback;
    }

    public Long getChildLocation() {
        return this.childLocation;
    }

    public void setChildLocation(Long childLocation) {
        this.childLocation = childLocation;
    }

    public Long getCrossHierarchyId() {
        return this.crossHierarchyId;
    }

    public void setCrossHierarchyId(Long crossHierarchyId) {
        this.crossHierarchyId = crossHierarchyId;
    }

    public String getLatlngAddress() {
        return this.latlngAddress;
    }

    public void setLatlngAddress(String latlngAddress) {
        this.latlngAddress = latlngAddress;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

	/*
     * @Override public String myLinkId() { return this.crn; }
	 */
    // @Override
    // public String getStateDetails() {
    // SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    // return
    // String.format("Complaint Number %s for %s filed on %s. Date of resolution %s",
    // this.getCrn(),
    // this.getComplaintType().getName(),
    // formatter.format(this.getCreatedDate()),
    // formatter.format(this.getEscalationDate()));
    // }

    // @Override
    // protected StateInfoBuilder buildStateInfo() {
    // return
    // super.buildStateInfo().citizenName(this.getComplainant().getName()).refDate(this.getCreatedDate()).citizenPhoneno(this.getComplainant().getMobile())
    // .citizenAddress(this.getComplainant().getAddress()).refNum(this.getCrn()).location(this.getLocation().getName()).task("Grievance").status(this.getStatus().getName());
    // }

}