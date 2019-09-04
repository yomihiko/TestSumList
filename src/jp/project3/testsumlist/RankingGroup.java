package jp.project3.testsumlist;

public class RankingGroup {
	private Student[] studentArray;
	private int rank;
	public RankingGroup(Student...students) {
		this.studentArray = students;
	}
	public Student[] getStudents() {
		return studentArray;
	}
}
