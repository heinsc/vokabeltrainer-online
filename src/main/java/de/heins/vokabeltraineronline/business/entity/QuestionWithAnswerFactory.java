package de.heins.vokabeltraineronline.business.entity;


import org.springframework.stereotype.Component;
@Component
public class QuestionWithAnswerFactory {
    private Long id;

    private String question;
    
	private String answer;
	
	private AppUser appUser;
    
	private LearningStrategy learningStrategy;

    public QuestionWithAnswerFactory() {
    }

	public QuestionWithAnswerFactory setId(Long id) {
		this.id = id;
		return this;
	}
	public QuestionWithAnswerFactory setQuestion(String question) {
		this.question = question;
		return this;
	}
	public QuestionWithAnswerFactory setAnswer(String answer) {
		this.answer = answer;
		return this;
	}
	public QuestionWithAnswerFactory setAppUser(AppUser appUser) {
		this.appUser=appUser;
		return this;
	}
	public QuestionWithAnswerFactory setLearningStrategy(LearningStrategy learningStrategy) {
		this.learningStrategy = learningStrategy;
		return this;
	}
    public QuestionWithAnswer getNewObject() {
    	return new QuestionWithAnswer(//
    			this.id//
    			, this.learningStrategy//
    			, this.question
    			, this.answer//
    			, this.appUser//
    	);
 	}
}
