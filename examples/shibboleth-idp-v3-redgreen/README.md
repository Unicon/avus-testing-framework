Shibboleth IdP v3 Red Green Test
=============================

This project is a sample/starter project that can do closed system application configuration testing of a Shibboleth IdP. 
This style of testing is appropriate for testing a configured containerized Shibboleth IdP before publishing the image to 
the image repository. 

The Reg Green name comes from the notion of unit/integration testing that produces a red/green response during the
source code compilation process. The tests in this project tests the basic functionality and general configuration of the
Shibboleth IdP (usually connected to a contrived and static data source) ensuring that an image has passed basic integration
test before being committed to the image repository (and likely promoting a broken image to live staging environment). 

## Docker Env

In this example, the Docker-based env is completely standalone. In real-world deployment, our IdP Dockerfile would
reference our newly built Shibboleth IdP image. We would copy in or change the `relaying-party.xml` file, such that SAML
responses would not be XML encrypted. In a best case scenario we'd also have a mock authn source, such as an LDAP 
directory container with fixed accounts, etc.

## Pipeline

After building our Shibboleth IdP image, our build pipeline would:

1. build and start our testing Docker env
2. run these test
3. store the test results
4. shutdown our testing Docker env

Then it would probably publish our new image assuming that the test completed successfully.

## Running Test

```bash
./gradlew
```

## Additional Notes

The current configuration uses fixed ports, etc. This project can be modified to support concurrent test. The
 `docker-compose.yaml` can be modified to set port mappings through environment variables as well as the ports used
by the test framework. 
