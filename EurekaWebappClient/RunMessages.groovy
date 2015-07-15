@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7.1')
import groovyx.net.http.HTTPBuilder

def callWebappServer = {
    try {
        def http = new HTTPBuilder('http://localhost:8080')
        http.get( path : "/message/${it}")
                { resp ->
                    println resp.entity.content.text
                }
    }
    catch(groovyx.net.http.HttpResponseException e)
    {
        println e.toString()
    }
}

(0..20).each(callWebappServer)