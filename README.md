#SHR Unstructured Data Module

This module provides a framework for adding data stores for unstructured data to OpenMRS. 

To add a new data store, you need to implement the UnstructuredDAO interface and register your class with the UnstructuredDataService on module startup.

A DAO providing storage to a Riak NoSQL cluster has been implemented as a reference.

