package org.egov.workflow.persistence.repository;


import java.util.List;

import org.egov.workflow.persistence.entity.WorkflowTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository 
public interface WorkflowTypesRepository extends JpaRepository<WorkflowTypes,java.lang.Long>,JpaSpecificationExecutor<WorkflowTypes>  {
	 WorkflowTypes findByTypeAndEnabledIsTrue(String type);

	    WorkflowTypes findByType(String type);
	    @Query("select type from WorkflowTypes where enabled=:isEnabled  ")
	    List<String>  findTypeEnabled(@Param("isEnabled") Boolean enabled);
	    @Query("select type from WorkflowTypes where enabled is null  ")
	    List<String>  findTypeByEnabledIsNull();
}