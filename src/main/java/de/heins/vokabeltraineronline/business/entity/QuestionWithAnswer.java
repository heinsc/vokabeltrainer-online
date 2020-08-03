package de.heins.vokabeltraineronline.business.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"user", "question"})})
public class QuestionWithAnswer extends UserOwnedObject{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String question;
    
	private String answer;
    
    private Date nextAppearance;
    
    private SuccessStep actualSuccessStep;
    public QuestionWithAnswer() {
    }
    public QuestionWithAnswer(//
    		Long id2//
    		, String question2//
    		, String answer2//
    		, User user2//
    	) {
    	this.id=id2;
		this.question=question2;
		this.answer=answer2;
		this.user=user2;
		this.nextAppearance = Calendar.getInstance().getTime();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Date getNextAppearance() {
		return nextAppearance;
	}
	public void setNextAppearance(Date nextAppearance) {
		this.nextAppearance = nextAppearance;
	}
	public SuccessStep getActualSuccessStep() {
		return actualSuccessStep;
	}
	public void setActualSuccessStep(SuccessStep actualSuccessStep) {
		this.actualSuccessStep = actualSuccessStep;
	}

}
