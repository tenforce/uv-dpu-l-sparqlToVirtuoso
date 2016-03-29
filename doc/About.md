### Description

Loads RDF data constructed via SPARQL into Virtuoso

This DPU has no input data unit. The input for this DPU is specified in the configuration as a SPARQL Construct query.

### Configuration parameters

| Name | Description |
|:----|:----|
|**Virtuoso JDBC URL** | URL for establishing JDBC session with Virtuoso server |
|**Username** | Username for Virtuoso server |
|**Password** | Password for the username |
|**SPARQL construct query** | SPARQL construct query |

### Inputs and outputs

|Name |Type | DataUnit | Description | Mandatory |
|:--------|:------:|:------:|:-------------|:---------------------:|
|rdfOutput |o| RDFDataUnit | Metadata about the RDF data loaded to Virtuoso | &nbsp; |


### Notes

Use: 
'''
DEFINE sql:log-enable 3
'''
