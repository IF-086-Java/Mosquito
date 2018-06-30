package com.softserve.mosquito.controllers.social;//package com.softserve.mosquito.controllers.social;


import com.softserve.mosquito.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
//import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


@Service
public class FacebookProvider  {

	private static final String FACEBOOK = "facebook";
	private static final String REDIRECT_LOGIN = "redirect:/login";

    	@Autowired
    	BaseProvider baseProvider ;


	public String getFacebookUserData(Model model, User userForm) {

		ConnectionRepository connectionRepository = baseProvider.getConnectionRepository();
//		if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
//			return REDIRECT_LOGIN;
//		}
		populateUserDetailsFromFacebook(userForm);
		model.addAttribute("loggedInUser",userForm);
		return "user";
	}

	protected void populateUserDetailsFromFacebook(User userForm) {
		Facebook facebook = baseProvider.getFacebook();
		org.springframework.social.facebook.api.User userFb = facebook.userOperations().getUserProfile();
		userForm.setEmail(userFb.getEmail());
		userForm.setFirstName(userFb.getFirstName());
		userForm.setLastName(userFb.getLastName());
	}


}
