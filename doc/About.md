### Description

Loads RDF data constructed via SPARQL into Virtuoso

This DPU has no input data unit. The input for this DPU is specified in the configuration as a SPARQL Construct query.

### Configuration parameters

| Name | Description |
|:----|:----|
|**Virtuoso JDBC URL** | URL for establishing JDBC session with Virtuoso server |
|**Username** | Username for Virtuoso server |
|**Password** | Password for the username |
|**Clear destination graph before loading (checkbox)** | Self-descriptive |
|**Target Graph** | Target graph URI. May be empty to indicate per-graph loading. In per-graph mode, each graph on input is loaded into separate graph on output, graph name is taken from VirtualGraph, if VirtualGraph is not set, internal RDF store graph name is used |
|**Thread count** | How many threads may be used to speed up loading|
|**Skip file on error (checkbox)** | Do not stop the pipeline when error occurs (if checked) |
|**SPARQL construct query** | SPARQL construct query |

### Inputs and outputs

|Name |Type | DataUnit | Description | Mandatory |
|:--------|:------:|:------:|:-------------|:---------------------:|
|rdfOutput |o| RDFDataUnit | Metadata about the RDF data loaded to Virtuoso | &nbsp; |

