# Upstream server simulator

Play role of the upstream server for load balancing client tests

## How to run it ?
For example run the main method in JavaLoadBalancerServerApplication with --port=8070 param to run it on port 8070. Default port is 8080

or

mvn clean install   and than   java -jar target/javaloadbalancerserver.jar  --port=8070


## What app do ?
The application will respond and log every /message and /ping request
