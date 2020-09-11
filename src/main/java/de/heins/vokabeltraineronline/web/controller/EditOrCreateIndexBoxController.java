package de.heins.vokabeltraineronline.web.controller;

import org.apache.catalina.session.StandardSessionFacade;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.heins.vokabeltraineronline.business.service.IndexBoxService;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.IndexBoxAttrRef;
import de.heins.vokabeltraineronline.web.entities.htmlmodelattribute.EditOrCreateIndexBoxModAtt;

@Controller
public class EditOrCreateIndexBoxController {
	private static enum Constants {
		editOrCreateIndexBoxPage//
		, editOrCreateIndexBoxModAtt//
	}
	
	@Autowired
	private IndexBoxService indexBoxService;

	public EditOrCreateIndexBoxController() {
		super();
	}

	@RequestMapping({ "/controlEditOrCreateIndexBox" })
	public String showEditOrCreateIndexBoxPage(//
			StandardSessionFacade session//
			, Model model//
	) throws Exception {
		
		SessionAppUser sessionAppUser = (SessionAppUser) session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		EditOrCreateIndexBoxModAtt editOrCreateIndexBoxModAtt = new EditOrCreateIndexBoxModAtt();
		String oldVersionOfIndexBoxName = (String) session.getAttribute(//
				ControllerConstants.sessionOldVersionOfIndexBoxName.name()//
		);
		String oldVersionOfIndexBoxSubject = (String) session.getAttribute(//
				ControllerConstants.sessionOldVersionOfIndexBoxSubject.name()//
		);
		if (Strings.isEmpty(oldVersionOfIndexBoxName) && Strings.isEmpty(oldVersionOfIndexBoxSubject)) {
			IndexBoxAttrRef indexBoxAttrRef = new IndexBoxAttrRef();
			editOrCreateIndexBoxModAtt.setIndexBox(indexBoxAttrRef);
		} else {
			editOrCreateIndexBoxModAtt.setIndexBox(//
					indexBoxService.findForAppUserAndNameAndSubject(//
							sessionAppUser//
							, oldVersionOfIndexBoxName//
							, oldVersionOfIndexBoxSubject//
					)//
			);
		}
		model.addAttribute(//
				Constants.editOrCreateIndexBoxModAtt.name()//
				, editOrCreateIndexBoxModAtt//
		);
		return Constants.editOrCreateIndexBoxPage.name();
	}

	@RequestMapping(value="/controlActionEditOrCreateIndexBox", method=RequestMethod.POST, params= {"submit"})
	public String submit(//
			StandardSessionFacade session//
			, @ModelAttribute(name = "editOrCreateIndexBoxModAtt")
			EditOrCreateIndexBoxModAtt editOrCreateIndexBoxModAtt
	) {
		SessionAppUser sessionAppUser = (SessionAppUser)session.getAttribute(//
				ControllerConstants.sessionAppUser.name()//
		);
		String oldVersionOfIndexBoxName = (String) session.getAttribute(//
				ControllerConstants.sessionOldVersionOfIndexBoxName.name()//
		);
		String oldVersionOfIndexBoxSubject = (String) session.getAttribute(//
				ControllerConstants.sessionOldVersionOfIndexBoxSubject.name()//
		);
		if (//
				Strings.isEmpty(editOrCreateIndexBoxModAtt.getIndexBox().getName())//
				&& Strings.isEmpty(editOrCreateIndexBoxModAtt.getIndexBox().getSubject())//
		){
			editOrCreateIndexBoxModAtt.setMandatoryViolated(true);
			return Constants.editOrCreateIndexBoxPage.name();
		}
		if (//
				(//
						Strings.isEmpty(oldVersionOfIndexBoxName)//
						&& Strings.isEmpty(oldVersionOfIndexBoxSubject)//
				) || !oldVersionOfIndexBoxName.equals(//
						editOrCreateIndexBoxModAtt.getIndexBox().getName()//
				) || !oldVersionOfIndexBoxSubject.equals(//
						editOrCreateIndexBoxModAtt.getIndexBox().getSubject()//
				)//
			) {
			// look for duplicates
			IndexBoxAttrRef fromDataBase = indexBoxService.findForAppUserAndNameAndSubject(//
					sessionAppUser//
					, editOrCreateIndexBoxModAtt.getIndexBox().getName()//
					, editOrCreateIndexBoxModAtt.getIndexBox().getSubject()//
			);
			if (IndexBoxService.EMPTY_INDEX_BOX != fromDataBase) {
				editOrCreateIndexBoxModAtt.setIndexBoxWithThisNameAndSubjectAlreadyExists(true);
				return Constants.editOrCreateIndexBoxPage.name();
			}
		}

		indexBoxService.update(//
				sessionAppUser//
				, editOrCreateIndexBoxModAtt.getIndexBox()//
				, oldVersionOfIndexBoxName//
				, oldVersionOfIndexBoxSubject
		);
		return "redirect:" + ControllerConstants.controlManageConfigurations.name();
	}
	@RequestMapping(value="/controlActionEditOrCreateIndexBox", method=RequestMethod.POST, params= {"cancel"})
	public String cancel() {
		return "redirect:" + ControllerConstants.controlManageConfigurations.name();
		
	}

}
