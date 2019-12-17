package org.xtremebiker.jsfspring.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Date;
import java.util.Optional;

@Component
//@Scope("view")
@SessionScope
public class MyBean {

/*	@Autowired
	private RoleRepository roleRepository;*/

	public MyBean() {
		System.out.println("Created!");
	}

	public String getFrom() {
		return this.toString();
	}

	public String getDate() {
		return new Date().toString();
	}

	public String getName() {
		/*Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();*/
		return "test";
	}

/*	public Role getRole(){
		Optional<Role> role = roleRepository.findByRoleId((long) 1);
		if (role.isPresent())
			return role.get();
		return null;
	}*/

}
