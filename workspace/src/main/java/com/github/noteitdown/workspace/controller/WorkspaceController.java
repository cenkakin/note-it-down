package com.github.noteitdown.workspace.controller;

import com.github.noteitdown.common.security.Identity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by cenkakin
 */
@RestController
public class WorkspaceController {

    @GetMapping
    public String getInitialWorkspace(@AuthenticationPrincipal Identity identity) {
        return "HELLO HELLO " + identity.getUsername() + " with id: " + identity.getId();
    }
}
