Shibboleth IdP v3 Blue Green Test
=============================

This project is a sample/starter project that can do end-to-end SAML SP integration testing. This style of testing 
is ideal for validating a Shibboleth IdP's operation before and after a configuration change to ensure that all integrations
are working as expected.

The Blue Green name comes from the notion that a set of service are brought online and tested before being put into
service. This project could facilitate this testing. 

 ## Docker Env
 
 In this example, the Docker-based env provides some SPs to test against. In a real-world deployment this would be
 un-necessary as we should be testing a deployed (running) instance with configured SAML SP and CAS-Clients.
 
 ## Pipeline
 
 After deploying a Shibboleth IdP image, our build pipeline would:
 
 1. run these test
 2. store the test results
 
 It could rollback the deployment if the testing failed, and theoretically it could connect to a load balancer and
 update mappings bringing the new deployment online and draining existing nodes, which could also be shutdown via the
 pipeline.
 
 ## Running Test
 
 ```bash
 ./gradlew
 ```
 
 ## Additional Notes
 
 The current configuration uses fixed ports, etc. This project can be modified to support concurrent test. The
  `docker-compose.yaml` can be modified to set port mappings through environment variables as well as the ports used
 by the test framework. 