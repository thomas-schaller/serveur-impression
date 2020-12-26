package fr.schaller.uploadingfiles;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class AppErrorController implements ErrorController {


    private static final String PATH = "/error";

    @PostMapping("/error")
    public String error(HttpServletRequest request) {
        Object status =  request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object erreur =request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception =request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        StringBuilder sb = new StringBuilder();
        if (status != null)
        {
            sb.append(" code erreur : ").append(status.toString());
        }
        if (exception != null)
        {
            sb.append(" exception : ").append(exception.toString());
        }
        if (erreur != null && !erreur.toString().isEmpty())
        {
            sb.append(" message : ").append(erreur.toString());
        }

        return sb.toString();
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
