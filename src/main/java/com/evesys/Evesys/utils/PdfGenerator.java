package com.evesys.Evesys.utils;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import jakarta.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.util.Map;

@Component
public class PdfGenerator {

    private final TemplateEngine templateEngine;

    public PdfGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void generarPdfDeHtml(String templateName, Map<String, Object> variables, HttpServletResponse response) throws Exception {
        Context context = new Context();
        context.setVariables(variables);

        String html = templateEngine.process(templateName, context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_eventos.pdf");

        OutputStream out = response.getOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(out);
        out.close();
    }
}