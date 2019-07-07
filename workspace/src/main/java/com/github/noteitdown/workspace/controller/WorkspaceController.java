package com.github.noteitdown.workspace.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by cenkakin
 */
@RestController
public class WorkspaceController {

    @GetMapping("/workspace")
    public String getInitialWorkspace() {
        return "HELLO HELLO!";
    }
}
