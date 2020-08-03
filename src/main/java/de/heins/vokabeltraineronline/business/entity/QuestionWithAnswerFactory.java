package de.heins.vokabeltraineronline.business.entity;


import org.springframework.stereotype.Component;
@Component
public class QuestionWithAnswerFactory {
    private Long id;

    private String question;
    
	private String answer;
	
	private User user;
    
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
	public QuestionWithAnswerFactory setUser(User user) {
		this.user=user;
		return this;
	}
    public QuestionWithAnswer getNewObject() {
    	return new QuestionWithAnswer(//
    			this.id//
    			, this.question
    			, this.answer//
    			, this.user//
    	);
 	}
}
