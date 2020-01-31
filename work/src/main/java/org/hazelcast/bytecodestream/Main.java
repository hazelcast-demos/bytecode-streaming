package org.hazelcast.bytecodestream;

import org.apache.catalina.startup.Tomcat;

public class Main {

    public static void main(String[] args) throws Exception {
        var tomcat = new Tomcat();
        var ctx = tomcat.addContext("", null);
        Tomcat.addServlet(ctx, "Hello", new HelloServlet());
        ctx.addServletMappingDecoded("/hello", "Hello");
        tomcat.getConnector(); // This is required
        tomcat.start();
        tomcat.getServer().await();
    }
}
