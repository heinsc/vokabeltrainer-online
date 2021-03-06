package de.heins.vokabeltraineronline.business.service;

import org.springframework.stereotype.Service;


@Service
public class FaultToleranceService {
	private static class FaultCounter {
		int faultCounter;
		private FaultCounter(int anInteger) {
			this.faultCounter=anInteger;
		}
		public String toString() {
			return String.valueOf(faultCounter);
		}
	}
	private Integer resolveMaxNumberOfLettersByFaultTolerance(//
			String faultTolerance
			, String answer//
	) {
		int lengthOfAnswer = answer.length();
		if ("EVERY_TENTH_CHARACTER_MAY_BE_FALSE".equals(faultTolerance)) {
			return Math.floorDiv(lengthOfAnswer, 10);
		}
		if ("EVERY_EIGHTH_CHARACTER_MAY_BE_FALSE".equals(faultTolerance)) {
			return Math.floorDiv(lengthOfAnswer, 8);
		}
		if ("EVERY_SIXTH_CHARACTER_MAY_BE_FALSE".equals(faultTolerance)) {
			return Math.floorDiv(lengthOfAnswer, 6);
		}
		if ("EVERY_FIFTH_CHARACTER_MAY_BE_FALSE".equals(faultTolerance)) {
			return Math.floorDiv(lengthOfAnswer, 5);
		}
		if ("NO_TOLERANCE".equals(faultTolerance)) {
			return 0;
		}
		//ORALLY
		return null;
	}


	private boolean checkOnEqualnessOfCurrentIndexPair(//
			FaultCounter faultCounter//
			, int currentIndexOfAnswer//
			, int currentIndexOfAnswerByUser//
			, String answer//
			, String answerByUser//
			, Integer maxNumberOfWrongLetters//
	) {
		int lengthOfAnswerByUser = answerByUser.length();
		if (lengthOfAnswerByUser == 0) {
			return false;
		}
		int lengthOfAnswer = answer.length();
		char currentCharOfAnwer = (char)answer.getBytes()[currentIndexOfAnswer];
		char currentCharOfAnswerByUser = (char)answerByUser.getBytes()[currentIndexOfAnswerByUser];
		if (currentCharOfAnwer != currentCharOfAnswerByUser) {
			faultCounter.faultCounter++;
			if (faultCounter.faultCounter > maxNumberOfWrongLetters) {
				return false;
			}
		}
		int nextIndexOfAnswer = currentIndexOfAnswer + 1;
		int nextIndexOfAnswerByUser = currentIndexOfAnswerByUser + 1;
		if (//
				nextIndexOfAnswer < lengthOfAnswer //
				&& nextIndexOfAnswerByUser < lengthOfAnswerByUser//
		) {
			return checkOnEqualnessOfCurrentIndexPair(//
					new FaultCounter(faultCounter.faultCounter)//
					, nextIndexOfAnswer//
					, nextIndexOfAnswerByUser//
					, answer//
					, answerByUser//
					, maxNumberOfWrongLetters//
			) || checkOnEqualnessOfCurrentIndexPair(//
					new FaultCounter(faultCounter.faultCounter)//
					, nextIndexOfAnswer//
					, currentIndexOfAnswerByUser//
					, answer//
					, answerByUser//
					, maxNumberOfWrongLetters//
			) || checkOnEqualnessOfCurrentIndexPair(//
					new FaultCounter(faultCounter.faultCounter)//
					, currentIndexOfAnswer//
					, nextIndexOfAnswerByUser//
					, answer//
					, answerByUser//
					, maxNumberOfWrongLetters//
			);
		}
		if (nextIndexOfAnswer < lengthOfAnswer) {
			return checkOnEqualnessOfCurrentIndexPair(//
					new FaultCounter(faultCounter.faultCounter)//
					, nextIndexOfAnswer//
					, currentIndexOfAnswerByUser//
					, answer//
					, answerByUser//
					, maxNumberOfWrongLetters//
			);
		}
		if (nextIndexOfAnswerByUser < lengthOfAnswerByUser) {
			return checkOnEqualnessOfCurrentIndexPair(//
					new FaultCounter(faultCounter.faultCounter)//
					, currentIndexOfAnswer//
					, nextIndexOfAnswerByUser//
					, answer//
					, answerByUser//
					, maxNumberOfWrongLetters//
			);
		}
		return true;
	}


	public boolean checkOnEqualnessOfCurrentIndexPair(//
			String answer//
			, String answerByUser//
			, String faultTolerance//
	) {
		Integer maxNumberOfWrongLetters = resolveMaxNumberOfLettersByFaultTolerance(faultTolerance, answer);
		if (null == maxNumberOfWrongLetters) {
			//ORALLY
			return true;
		} else {
			return this.checkOnEqualnessOfCurrentIndexPair(//
					new FaultCounter(0)//
					, 0
					, 0
					, answer
					, answerByUser
					, maxNumberOfWrongLetters
			);
		}
	}


}
