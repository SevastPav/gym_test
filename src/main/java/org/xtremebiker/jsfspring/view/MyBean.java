package org.xtremebiker.jsfspring.view;

import org.springframework.beans.factory.annotation.Autowired;
/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.xtremebiker.jsfspring.entity.Rle;
import org.xtremebiker.jsfspring.entity.UserProfile;
import org.xtremebiker.jsfspring.repository.UserProfileRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
//@Scope("view")
@SessionScope
public class MyBean {

/*	@Autowired
	private RoleRepository roleRepository;*/

	private final UserProfileRepository userProfileRepository;

	public MyBean(UserProfileRepository userProfileRepository) {
		this.userProfileRepository = userProfileRepository;
		System.out.println("Created!");
	}

	public int getAdminCount(){
		return userProfileRepository.findAllByRoles(Collections.singleton(Rle.ADMIN)).size();
	}

	public int getClientCount(){
		return userProfileRepository.findAllByRoles(Collections.singleton(Rle.USER)).size();
	}

	public int getTrainerCount(){
		return userProfileRepository.findAllByRoles(Collections.singleton(Rle.TRAINER)).size();
	}

	public String getFrom() {
		return this.toString();
	}

	public String getDate() {
		return new Date().toString();
	}

	public String getName() {
		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
		//return "test";
	}

/*	public Role getRole(){
		Optional<Role> role = roleRepository.findByRoleId((long) 1);
		if (role.isPresent())
			return role.get();
		return null;
	}*/

}
