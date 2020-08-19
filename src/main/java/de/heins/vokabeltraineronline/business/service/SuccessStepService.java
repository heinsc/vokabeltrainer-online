package de.heins.vokabeltraineronline.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.repository.SuccessStepRepository;
import de.heins.vokabeltraineronline.business.repository.AppUserRepository;
import de.heins.vokabeltraineronline.business.entity.BehaviourIfWrong;
import de.heins.vokabeltraineronline.business.entity.SuccessStep;
import de.heins.vokabeltraineronline.business.entity.SuccessStepFactory;
import de.heins.vokabeltraineronline.business.entity.AppUser;
import de.heins.vokabeltraineronline.web.entities.SessionAppUserForm;
import de.heins.vokabeltraineronline.web.entities.SuccessStepForm;


@Service
public class SuccessStepService {
	@Autowired
	private SuccessStepRepository successStepRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private SuccessStepFactory successStepFactory;
	
	public static final SuccessStepForm EMPTY_SUCCESS_STEP = new SuccessStepForm();
	public List<SuccessStepForm> findAllForAppUser(SessionAppUserForm sessionAppUserForm) {
		List<AppUser> appUsers = appUserRepository.findByEmail(sessionAppUserForm.getEmail());
		if (appUsers.size() == 1) {
			AppUser appUser = appUsers.get(0);
			List<SuccessStepForm> successStepForms = new ArrayList<SuccessStepForm>();
			try {
				List<SuccessStep> successSteps = successStepRepository.findByAppUser(appUser);
				successSteps.iterator().forEachRemaining(successStep -> {
					SuccessStepForm successStepForm = new SuccessStepForm();
					successStepForm.setName(successStep.getName());
					successStepForm.setNextAppearanceInDays(successStep.getNextAppearanceInDays());
					successStepForm.setBehaviourIfWrong(successStep.getBehaviourIfWrong().name());
					successStepForms.add(successStepForm);
				});
			} catch (Exception e) {
				// this occurs only if there are no items in the database table,
				// or the table wasn't created yet.
				// nothing to do then.
			}
			return successStepForms;
		} else {
			throw new RuntimeException("No AppUser found or AppUser not unique by email");
		}
	}
	public SuccessStepForm findForAppUserAndName(SessionAppUserForm sessionAppUserForm, String name) {
		AppUser appUser = appUserRepository.findByEmail(sessionAppUserForm.getEmail()).get(0);
		
		try {
			List<SuccessStep> resultByAppUserAndName = successStepRepository.findByAppUserAndName(appUser, name);
			if (!resultByAppUserAndName.isEmpty()) {
				SuccessStep successStep = resultByAppUserAndName.get(0);
				SuccessStepForm successStepForm = new SuccessStepForm();
				successStepForm.setName(successStep.getName());
				successStepForm.setNextAppearanceInDays(successStep.getNextAppearanceInDays());
				successStepForm.setBehaviourIfWrong(successStep.getBehaviourIfWrong().name());
				return successStepForm;
			}
		} catch (Exception e) {
			// this occurs only if there are no items in the database table,
			// or the table wasn't created yet.
			// nothing to do then.
		}
		return EMPTY_SUCCESS_STEP;
	}
	public void update(SessionAppUserForm appUserForm, SuccessStepForm successStepForm, String oldName) {
		AppUser appUser = appUserRepository.findByEmail(appUserForm.getEmail()).get(0);
		List<SuccessStep> findByAppUserAndNameList = new ArrayList<SuccessStep>();
		try {
			findByAppUserAndNameList = successStepRepository.findByAppUserAndName(appUser, oldName);
		} catch (Exception e) {
			// this occurs only if there are no items in the database table,
			// or the table wasn't created yet.
			// nothing to do then.
		}
		SuccessStep successStep;
		if (findByAppUserAndNameList.isEmpty()) {//
				successStep = successStepFactory//
					.setName(successStepForm.getName())//
					.setNextAppearanceInDays(successStepForm.getNextAppearanceInDays())//
					.setBehaviourIfWrong(BehaviourIfWrong.valueOf(successStepForm.getBehaviourIfWrong()))//
					.setAppUser(appUser)//
					.getNewObject();
		} else {
			successStep = findByAppUserAndNameList.get(0); //
			successStep.setName(successStepForm.getName());//
			successStep.setNextAppearanceInDays(successStepForm.getNextAppearanceInDays());//
			successStep.setBehaviourIfWrong(BehaviourIfWrong.valueOf(successStepForm.getBehaviourIfWrong()));
		}
		successStepRepository.save(successStep);
	}
}
