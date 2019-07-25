package com.github.noteitdown.workspace.controller;

import com.github.noteitdown.common.security.ExtendedUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Created by cenkakin
 */
@RestController
public class WorkspaceController {

    @GetMapping
    public String getInitialWorkspace(@AuthenticationPrincipal ExtendedUserDetails extendedUserDetails) {
        System.out.println(extendedUserDetails);
        return "HELLO HELLO!";
    }
}
