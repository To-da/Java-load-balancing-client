# Upstream load-balancing client

Settings of the upstreams servers is in the /resources/application.properties file.
The structure of settings for ribbon is  'name-of-the-client'.ribbon.'specific-property'=value.

Example:
upstreamServerClient.ribbon.listOfServers=localhost:8080,localhost:8070