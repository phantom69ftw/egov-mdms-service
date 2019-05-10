package org.egov.receipt.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class InstrumentAccountCodeSearchContract extends InstrumentAccountCodeContract {
    private String ids;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}