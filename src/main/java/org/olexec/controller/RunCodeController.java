package org.olexec.controller;

import org.olexec.service.ExecuteStringSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ycw
 */

@Controller
public class RunCodeController {

    private static final String defaultSource = "public class Run {\n"
            + "    public static void main(String[] args) {\n"
            + "        \n"
            + "    }\n"
            + "}";

    @Autowired
    private ExecuteStringSourceService executeStringSource;

    @RequestMapping(path = {"/"}, method = RequestMethod.GET)
    public String runCode(Model model) {
        model.addAttribute("lastSource", defaultSource);
        return "ide";
    }

    @RequestMapping(path = {"/run"}, method = RequestMethod.POST)
    public String runCode(@RequestParam("source") String source,
                          @RequestParam("systemIn") String systemIn, Model model) {
        String runResult = executeStringSource.execute(source, systemIn);
        runResult = runResult.replaceAll(System.lineSeparator(), "<br/>");
        model.addAttribute("lastSource", source);
        model.addAttribute("lastSystemIn", systemIn);
        model.addAttribute("runResult", runResult);
        return "ide";
    }
}
