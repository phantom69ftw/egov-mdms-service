var dat = {
  "swm.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "vehicleFuellingDetails",
    "url": "/swm-services/vehiclefuellingdetails/_search",
    "groups": [
      {
        "name": "search",
        "label": "vehiclefuellingdetails.search.title",
        "fields": [
          {
            "name": "transactionNo",
            "jsonPath": "transactionNo",
            "label": "vehiclefuellingdetails.create.transactionNo",
            "type": "autoCompelete",
            "isDisabled": false,
            "patternErrorMsg": "swm.create.field.message.transactionNo",
	    "url": "swm-services/vehiclefuellingdetails/_search?|$.vehicleFuellingDetails.*.transactionNo|$.vehicleFuellingDetails.*.transactionNo"
          }
        ]
      }
    ],
    "result": {
      "header": [
        {
          "label": "vehiclefuellingdetails.create.transactionNo"
        },
        {
          "label": "vehiclefuellingdetails.create.transactionDate"
        },
        {
          "label": "vehiclefuellingdetails.create.vehicleType"
        },
        {
          "label": "vehiclefuellingdetails.create.regNumber"
        },
        {
          "label": "vehiclefuellingdetails.search.result.refuellingStation"
        }
      ],
      "values": [
        "transactionNo",
        "transactionDate",
        "vehicle.vehicleType.name",
        "vehicle.regNumber",
        "refuellingStation.name"
      ],
      "resultPath": "vehicleFuellingDetails",
      "rowClickUrlUpdate": "/update/swm/vehiclefuellingdetails/{transactionNo}",
      "rowClickUrlView": "/view/swm/vehiclefuellingdetails/{transactionNo}"
    }
  },
  "swm.create": {
    "numCols": 12/3,
    "useTimestamp": true,
    "objectName": "vehicleFuellingDetails",
    "idJsonPath": "vehicleFuellingDetails[0].transactionNo",
    "title": "vehiclefuellingdetails.create.group.title.VehicleDetails1",
    "groups": [
      {
        "name": "VehicleDetails1",
        "label": "",
        "fields": [
          {
            "name": "transactionDate",
            "jsonPath": "vehicleFuellingDetails[0].transactionDate",
            "label": "vehiclefuellingdetails.create.transactionDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails2",
        "label": "vehiclefuellingdetails.create.group.title.VehicleDetails2",
        "fields": [
 	{
            "name":"regNumber",
            "jsonPath":"vehicleFuellingDetails[0].vehicle.regNumber",
            "label":"vehiclefuellingdetails.create.regNumber",
            "pattern":"",
            "type":"autoCompelete",
            "isRequired":true,
            "isDisabled":false,
            "defaultValue":"",
            "maxLength":12,
            "minLength":6,
            "patternErrorMsg":"",
            "url": "swm-services/vehicles/_search?|$.vehicles.*.regNumber|$.vehicles.*.regNumber",
            "autoCompleteDependancy": {
              "autoCompleteUrl": "/swm-services/vehicles/_search?regNumber={vehicleFuellingDetails[0].vehicle.regNumber}",
              "autoFillFields": {
                "vehicleFuellingDetails[0].vehicle.vehicleType.code": "vehicles[0].vehicleType.name"
              }
            }
          },
          {
             "name":"name",
             "jsonPath":"vehicleFuellingDetails[0].vehicle.vehicleType.code",
             "label":"vehiclefuellingdetails.create.vehicleType",
             "pattern":"",
             "type":"text",
             "isRequired":false,
             "isDisabled":true,
             "defaultValue":"",
             "maxLength":128,
             "minLength":1,
             "patternErrorMsg":"",
             "url": ""
          },
          {
            "name": "vehicleReadingDuringFuelling",
            "jsonPath": "vehicleFuellingDetails[0].vehicleReadingDuringFuelling",
            "label": "vehiclefuellingdetails.create.vehicleReadingDuringFuelling",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails3",
        "label": "vehiclefuellingdetails.create.group.title.VehicleDetails3",
        "fields": [
          {
            "name": "name",
            "jsonPath": "vehicleFuellingDetails[0].refuellingStation.code",
            "label": "vehiclefuellingdetails.search.result.refuellingStation",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/swm-services/refillingpumpstations/_search?|$.refillingPumpStations.*.code|$.refillingPumpStations.*.name"
          },
          {
            "name": "typeOfFuel",
            "jsonPath": "vehicleFuellingDetails[0].typeOfFuel.code",
            "label": "vehiclefuellingdetails.search.result.typeOfFuel",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": "",
	    "isStateLevel": true,
	    "url": "/egov-mdms-service/v1/_get?&moduleName=swm&masterName=FuelType|$..code|$..code"
          },
          {
            "name": "fuelFilled",
            "jsonPath": "vehicleFuellingDetails[0].fuelFilled",
            "label": "vehiclefuellingdetails.search.result.fuelFilled",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "totalCostIncurred",
            "jsonPath": "vehicleFuellingDetails[0].totalCostIncurred",
            "label": "vehiclefuellingdetails.search.result.totalCostIncurred",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "receiptNo",
            "jsonPath": "vehicleFuellingDetails[0].receiptNo",
            "label": "vehiclefuellingdetails.search.result.receiptNo",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "receiptDate",
            "jsonPath": "vehicleFuellingDetails[0].receiptDate",
            "label": "vehiclefuellingdetails.search.result.receiptDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "/swm-services/vehiclefuellingdetails/_create",
    "tenantIdRequired": true
  },
  "swm.view": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "vehicleFuellingDetails",
    "searchUrl": "swm-services/vehiclefuellingdetails/_search?transactionNo={transactionNo}",     
    "groups": [
      {
        "name": "VehicleDetails2",
        "label": "vehiclefuellingdetails.create.group.title.VehicleDetails2",
        "fields": [
	{
            "name": "transactionNo",
            "jsonPath": "vehicleFuellingDetails[0].transactionNo",
            "label": "vehiclefuellingdetails.create.transactionNo",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": true,
            "patternErrorMsg": ""
          },
	{
            "name": "transactionDate",
            "jsonPath": "vehicleFuellingDetails[0].transactionDate",
            "label": "vehiclefuellingdetails.create.transactionDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "name",
            "jsonPath": "vehicleFuellingDetails[0].vehicle.vehicleType.name",
            "label": "vehiclefuellingdetails.create.vehicleType",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 4,
            "patternErrorMsg": ""
          },
          {
            "name": "regNumber",
            "jsonPath": "vehicleFuellingDetails[0].vehicle.regNumber",
            "label": "vehiclefuellingdetails.create.regNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 12,
            "minLength": 6,
            "patternErrorMsg": ""
          },
          {
            "name": "vehicleReadingDuringFuelling",
            "jsonPath": "vehicleFuellingDetails[0].vehicleReadingDuringFuelling",
            "label": "vehiclefuellingdetails.create.vehicleReadingDuringFuelling",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails3",
        "label": "vehiclefuellingdetails.create.group.title.VehicleDetails3",
        "fields": [
          {
            "name": "name",
            "jsonPath": "vehicleFuellingDetails[0].refuellingStation.name",
            "label": "vehiclefuellingdetails.search.result.refuellingStation",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "fuelFilled",
            "jsonPath": "vehicleFuellingDetails[0].fuelFilled",
            "label": "vehiclefuellingdetails.search.result.fuelFilled",
            "type": "textArea",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "typeOfFuel",
            "jsonPath": "vehicleFuellingDetails[0].typeOfFuel.name",
            "label": "vehiclefuellingdetails.search.result.typeOfFuel",
            "type": "textArea",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "totalCostIncurred",
            "jsonPath": "vehicleFuellingDetails[0].totalCostIncurred",
            "label": "vehiclefuellingdetails.search.result.totalCostIncurred",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "receiptNo",
            "jsonPath": "vehicleFuellingDetails[0].receiptNo",
            "label": "vehiclefuellingdetails.search.result.receiptNo",
            "type": "textArea",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "receiptDate",
            "jsonPath": "vehicleFuellingDetails[0].receiptDate",
            "label": "vehiclefuellingdetails.search.result.receiptDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "tenantIdRequired": true,
    "url": "swm-services/vehiclefuellingdetails/_search?transactionNo={transactionNo}"
  },
  "swm.update": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "vehicleFuellingDetails",
    "idJsonPath": "vehicleFuellingDetails[0].transactionNo",
    "groups": [
      {
        "name": "VehicleDetails1",
        "label": "",
        "fields": [
          {
            "name": "transactionDate",
            "jsonPath": "vehicleFuellingDetails[0].transactionDate",
            "label": "vehiclefuellingdetails.create.transactionDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails2",
        "label": "vehiclefuellingdetails.create.group.title.VehicleDetails2",
        "fields": [
 	{
            "name":"regNumber",
            "jsonPath":"vehicleFuellingDetails[0].vehicle.regNumber",
            "label":"vehiclefuellingdetails.create.regNumber",
            "pattern":"",
            "type":"autoCompelete",
            "isRequired":true,
            "isDisabled":false,
            "defaultValue":"",
            "maxLength":12,
            "minLength":6,
            "patternErrorMsg":"",
            "url": "swm-services/vehicles/_search?|$.vehicles.*.regNumber|$.vehicles.*.regNumber",
            "autoCompleteDependancy": {
              "autoCompleteUrl": "/swm-services/vehicles/_search?regNumber={vehicleFuellingDetails[0].vehicle.regNumber}",
              "autoFillFields": {
                "vehicleFuellingDetails[0].vehicle.vehicleType.code": "vehicles[0].vehicleType.name"
              }
            }
          },
          {
             "name":"name",
             "jsonPath":"vehicleFuellingDetails[0].vehicle.vehicleType.code",
             "label":"vehiclefuellingdetails.create.vehicleType",
             "pattern":"",
             "type":"text",
             "isRequired":false,
             "isDisabled":true,
             "defaultValue":"",
             "maxLength":128,
             "minLength":1,
             "patternErrorMsg":"",
             "url": ""
          },
          {
            "name": "vehicleReadingDuringFuelling",
            "jsonPath": "vehicleFuellingDetails[0].vehicleReadingDuringFuelling",
            "label": "vehiclefuellingdetails.create.vehicleReadingDuringFuelling",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails3",
        "label": "vehiclefuellingdetails.create.group.title.VehicleDetails3",
        "fields": [
          {
            "name": "name",
            "jsonPath": "vehicleFuellingDetails[0].refuellingStation.code",
            "label": "vehiclefuellingdetails.search.result.refuellingStation",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/swm-services/refillingpumpstations/_search?|$.refillingPumpStations.*.code|$.refillingPumpStations.*.name"
          },
          {
            "name": "typeOfFuel",
            "jsonPath": "vehicleFuellingDetails[0].typeOfFuel.code",
            "label": "vehiclefuellingdetails.search.result.typeOfFuel",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": "",
	    "isStateLevel": true,
	    "url": "/egov-mdms-service/v1/_get?&moduleName=swm&masterName=FuelType|$..code|$..code"
          },
          {
            "name": "fuelFilled",
            "jsonPath": "vehicleFuellingDetails[0].fuelFilled",
            "label": "vehiclefuellingdetails.search.result.fuelFilled",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "totalCostIncurred",
            "jsonPath": "vehicleFuellingDetails[0].totalCostIncurred",
            "label": "vehiclefuellingdetails.search.result.totalCostIncurred",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "receiptNo",
            "jsonPath": "vehicleFuellingDetails[0].receiptNo",
            "label": "vehiclefuellingdetails.search.result.receiptNo",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "receiptDate",
            "jsonPath": "vehicleFuellingDetails[0].receiptDate",
            "label": "vehiclefuellingdetails.search.result.receiptDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "/swm-services/vehiclefuellingdetails/_update",
    "tenantIdRequired": true,
    "searchUrl": "/swm-services/vehiclefuellingdetails/_search?transactionNo={transactionNo}"
  }
}
 export default dat;
