package org.xtremebiker.jsfspring.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xtremebiker.jsfspring.entity.Role;
import org.xtremebiker.jsfspring.repository.RoleRepository;

import java.util.Date;
import java.util.Optional;

@Component
@Scope("view")
public class MyBean {

	@Autowired
	private RoleRepository roleRepository;

	public MyBean() {
		System.out.println("Created!");
	}

	public String getFrom() {
		return this.toString();
	}

	public String getDate() {
		return new Date().toString();
	}

	public Role getRole(){
		Optional<Role> role = roleRepository.findByRoleId((long) 1);
		if (role.isPresent())
			return role.get();
		return null;
	}

}
