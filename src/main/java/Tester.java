public class Tester {

	public static void main(String[] args) {

		String subjectString = "Das wäre schön...";
		String resultString = subjectString.replaceAll("[^\\p{L}\\p{Nd}]+", "");
		System.out.println(resultString);
	}
}
