package org.hazelcast.bytecodestream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var out = resp.getWriter();
        resp.setContentType("text/html");
        out.write("<html><body><h1>Hello world!</h1>");
        out.flush();
        out.close();
    }
}
