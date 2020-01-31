package org.hazelcast.bytecodestream;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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