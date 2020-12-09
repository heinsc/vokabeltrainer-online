package de.heins.vokabeltraineronline.web.controller;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.heins.vokabeltraineronline.business.service.IndexBoxService;
import de.heins.vokabeltraineronline.web.entities.IndexBoxes;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.LearnFilterIndexBoxesModAtt;

@Controller
public class LearnFilterIndexBoxesController {
	private static enum Constants {
		learnFilterIndexBoxesPage
		, learnFilterIndexBoxesModAtt
	}
	@Autowired
	private LearnDoLearnController learnDoLearnController;
	@Autowired
	private MenuController menuController;
	@Autowired
	private IndexBoxService indexBoxService;
	public LearnFilterIndexBoxesController() {
		super();
	}

	public String showLearnFilterIndexBoxesPage(//
			StandardSessionFacade session//
			, Model model
	) {
		LearnFilterIndexBoxesModAtt learnFilterIndexBoxesModAtt = new LearnFilterIndexBoxesModAtt();
		learnFilterIndexBoxesModAtt.setMandatoryViolated(false);
		model.addAttribute(Constants.learnFilterIndexBoxesModAtt.name(), learnFilterIndexBoxesModAtt);
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		IndexBoxes indexBoxAttrRefList = indexBoxService.findAllForAppUser(sessionAppUser);
		session.setAttribute(ControllerConstants.sessionIndexBoxAttrRefList.name(), indexBoxAttrRefList);
		return Constants.learnFilterIndexBoxesPage.name();

	}
	@RequestMapping(value = "/controlActionLearnFilterIndexBoxes", method = RequestMethod.POST, params = {"learn"})
	public String learn(
			StandardSessionFacade session//
			, Model model
			, @ModelAttribute("learnFilterIndexBoxesModAtt")
			LearnFilterIndexBoxesModAtt learnFilterIndexBoxesModAtt//
	) {

		IndexBoxes indexBoxAttrRefList = (IndexBoxes) session.getAttribute(//
				ControllerConstants.sessionIndexBoxAttrRefList.name()//
		);
		boolean mandatoryViolated = !checkAtLeastOneIndexBoxIsSelected(indexBoxAttrRefList);
		if (mandatoryViolated) {
			learnFilterIndexBoxesModAtt.setMandatoryViolated(//
					mandatoryViolated//
			);
			return Constants.learnFilterIndexBoxesPage.name();
		}
		// save filter
		for (IndexBoxAttrRef indexBoxAttrRef : indexBoxAttrRefList) {
			SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(ControllerConstants.sessionAppUser.name());
			indexBoxService.update(//
					sessionAppUser//
					, indexBoxAttrRef//
					, indexBoxAttrRef.getName()//
					, indexBoxAttrRef.getSubject()//
			);
		}
		return learnDoLearnController.showLearnDoLearnPages(model, session);

	}
	private boolean checkAtLeastOneIndexBoxIsSelected(IndexBoxes indexBoxAttrRefList) {
		for (IndexBoxAttrRef indexBoxAttrRef : indexBoxAttrRefList) {
			if (indexBoxAttrRef.isFilterOn()) {
				return true;
			}
		}
		return false;
	}

	@RequestMapping(value = "/controlActionLearnFilterIndexBoxes", method = RequestMethod.POST, params = {"cancel"})
	public String cancel(Model model, StandardSessionFacade session) {
		session.removeAttribute(ControllerConstants.sessionIndexBoxAttrRefList.name());
		return menuController.showMenuPage(model, session);
	}
	@RequestMapping({"controlLinkAddIndexBoxToFilterOnLearningPage"})
	public String addIndexBoxToFilter(//
		@RequestParam(name = "index", required = false, defaultValue = "")
        int index//
		, StandardSessionFacade session//
		, @ModelAttribute("learnFilterIndexBoxesModAtt")
		LearnFilterIndexBoxesModAtt learnFilterIndexBoxesModAtt
	) {
		IndexBoxes indexBoxAttrRefList = (IndexBoxes) session.getAttribute(//
				ControllerConstants.sessionIndexBoxAttrRefList.name()//
		);
		IndexBoxAttrRef indexBox = indexBoxAttrRefList.get(index);
		indexBox.setFilterOn(true);
		return Constants.learnFilterIndexBoxesPage.name();
	}

	@RequestMapping({"controlLinkRemoveIndexBoxFromFilterOnLearningPage"})
	public String removeIndexBoxFromFilter(//
			@RequestParam(name = "index", required = false, defaultValue = "")
	        int index//
			, StandardSessionFacade session//
			, @ModelAttribute("learnFilterIndexBoxesModAtt")
			LearnFilterIndexBoxesModAtt learnFilterIndexBoxesModAtt
	) {
		IndexBoxes indexBoxAttrRefList = (IndexBoxes) session.getAttribute(ControllerConstants.sessionIndexBoxAttrRefList.name());
		IndexBoxAttrRef indexBox = indexBoxAttrRefList.get(index);
		indexBox.setFilterOn(false);
		learnFilterIndexBoxesModAtt.setMandatoryViolated(!checkAtLeastOneIndexBoxIsSelected(indexBoxAttrRefList));
		return Constants.learnFilterIndexBoxesPage.name();
	}


}