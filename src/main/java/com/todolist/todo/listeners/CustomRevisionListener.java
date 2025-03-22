package com.todolist.todo.listeners;

import com.todolist.todo.model.CustomRevisionEntity;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object o) {
        CustomRevisionEntity rev = (CustomRevisionEntity) o;
        String username="system";
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(authentication!=null&& authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if(principal instanceof UserDetails){
                username=((UserDetails)principal).getUsername();
            }
            else{
                username=principal.toString();
            }
        }
        rev.setUsername(username);
    }
}
